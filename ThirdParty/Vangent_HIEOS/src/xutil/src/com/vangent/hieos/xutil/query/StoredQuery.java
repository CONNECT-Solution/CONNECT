/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vangent.hieos.xutil.query;

//import com.vangent.hieos.xutil.exception.ExceptionUtil;
import com.vangent.hieos.xutil.registry.BackendRegistry;
import com.vangent.hieos.xutil.metadata.structure.And;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.metadata.structure.MetadataParser;
import com.vangent.hieos.xutil.response.ErrorLogger;
import com.vangent.hieos.xutil.response.Response;
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.XDSRegistryOutOfResourcesException;
import com.vangent.hieos.xutil.exception.XMLParserException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.axiom.om.OMElement;

public abstract class StoredQuery extends BasicQuery {

	protected ErrorLogger response;
	protected XLogMessage log_message;
	protected HashMap<String, Object> params;
	protected StringBuffer query;
	protected BackendRegistry br;
	protected boolean return_leaf_class;
	public boolean has_validation_errors = false;
	protected boolean is_secure;

	abstract public Metadata run_internal() throws XdsException, XDSRegistryOutOfResourcesException;

	public StoredQuery(ErrorLogger response, XLogMessage log_message) {
		this.response = response;
		this.log_message = log_message;
		br = new BackendRegistry(response, log_message);

	}

	public StoredQuery(HashMap<String, Object> params, boolean return_objects, Response response, XLogMessage log_message, boolean is_secure) {
		this.response = response;
		this.log_message = log_message;
		this.params = params;
		this.is_secure = is_secure;
		br = new BackendRegistry(response, log_message);
		this.return_leaf_class = return_objects;
	}

	protected void setParams(HashMap<String, Object> params) {
		this.params = params;
	}

	public ArrayList<OMElement> run() throws XdsException, XDSRegistryOutOfResourcesException {
		Metadata metadata = run_internal();
		if (metadata == null)
			return null;
		if (return_leaf_class) {
			if (is_secure) secure_URI(metadata);
			return metadata.getAllObjects();//getV3();
		}
		else
			return metadata.getObjectRefs(metadata.getMajorObjects(), false);
	}

	protected ArrayList<String> get_ids_from_registry_response(OMElement rr) {
		ArrayList<String> ids = new ArrayList<String>();

		OMElement sqr = MetadataSupport.firstChildWithLocalName(rr, "RegistryObjectList") ;
		if (sqr == null)
			return ids;

		for (Iterator<OMElement> it=sqr.getChildElements(); it.hasNext();) {
			OMElement ele = (OMElement) it.next();
			if (ele.getLocalName().equals("ObjectRef"))
				continue;
			ids.add(ele.getAttributeValue(MetadataSupport.id_qname));
		}

		return ids;
	}

	protected String get_string_parm(String name) {
		Object o = params.get(name);
		if ( o instanceof String) {
			return (String) o;
		}
		return null;
	}

	protected String get_int_parm(String name) throws MetadataException {
		Object o = params.get(name);
		if (o == null)
			return null;
		if ( o instanceof Integer) {
			Integer i = (Integer) o;
			return i.toString();
		} 
		if ( o instanceof BigInteger) {
			BigInteger i = (BigInteger) o;
			return i.toString();
		} 
		else
			throw new MetadataException("Parameter " + name + " - expecting a number but got " + o.getClass().getName() + " instead");
	}

	protected ArrayList<String> get_arraylist_parm(String name) throws XdsInternalException, MetadataException {
		Object o = params.get(name);
		if (o == null)
			return null;
		if (o instanceof ArrayList) {
			ArrayList<String> a = (ArrayList<String>) o;
			if (a.size() == 0)
				throw new MetadataException("Parameter " + name + " is an empty list");
			return a;
		}
		throw new XdsInternalException("get_arraylist_parm(): bad type = " + o.getClass().getName());
	}

	protected ArrayList<Object> get_andor_parm(String name) throws XdsInternalException, MetadataException {
		Object o = params.get(name);
		if (o == null)
			return null;
		if (o instanceof ArrayList) {
			ArrayList<Object> a = (ArrayList<Object>) o;
			if (a.size() == 0)
				throw new MetadataException("Parameter " + name + " is an empty list");
			return a;
		}
		throw new XdsInternalException("get_arraylist_parm(): bad type = " + o.getClass().getName());
	}

	protected boolean isAnd(Object values) {
		return (values instanceof And);
	}

	protected int andSize(Object values) {
		if ( !isAnd(values)) return 0;
		And and = (And) values;
		return and.size();
	}

	protected ArrayList<String> getAndorVarNames(Object andor, String varname) {
		ArrayList<String> names = new ArrayList<String>();
		if ( !isAnd(andor)) {
			names.add(varname);
			return names;
		}
		And and = (And) andor;
		for (int i=0; i<and.size(); i++) {
			names.add(varname + i);
		}
		return names;
	}

	protected String declareClassifications(ArrayList<String> names) {
		StringBuffer buf = new StringBuffer();

		for (String name : names) 
			buf.append(", Classification " + name + "\n"); 

		return buf.toString();
	}

	ArrayList<String> query_for_object_refs() throws XMLParserException, XdsException {
		return br.queryForObjectRefs(query.toString());
	}

	protected OMElement query() throws XdsException {
        return query(true);
	}

	protected OMElement query(boolean leaf_class) throws XdsException {
		String q = query.toString();
		if (log_message != null)
			log_message.addOtherParam("raw query", q);
		return br.query(q, leaf_class);
	}

	protected void init() {
		query = new StringBuffer();
	}

	protected void a(String x) {
		query.append(x);
	}

	protected void a_quoted(String x) {
		query.append("'");
		query.append(x);
		query.append("'");
	}

	protected void n() {
		query.append("\n");
	}

	protected void a(ArrayList list) throws MetadataException {
		query.append("(");
		boolean first_time = true;
		for (int i=0; i<list.size(); i++) {
			if ( !first_time) 
				query.append(",");
			Object o = list.get(i);
			if (o instanceof String) 
				query.append("'" + (String) o + "'");
			else if (o instanceof Integer) 
				query.append(((Integer) o).toString());
			else {
                /*
				String trace = null;
				try {
					throw new Exception("foo");
				} catch (Exception e) {
					trace = ExceptionUtil.exception_details(e);
				}
                 */
				throw new MetadataException("Parameter value " + o + " cannot be decoded");
			}
			first_time = false;
		}
		query.append(")");
	}

	protected void validate_parm(HashMap parms, String name, boolean required, boolean multiple, boolean is_string, String same_size_as, String alternative) {
		Object value = parms.get(name);
		if (value == null) {
			if (required ) {
				if (alternative != null) {
					Object value2 = parms.get(alternative);
					if (value2 == null) {
						response.add_error("XDSRegistryError", "Parameter, " + name + " or " + alternative + " is required, neither are present", this.getClass().getName(), log_message);
						this.has_validation_errors = true;
						return;
					}
				} else {
					response.add_error("XDSRegistryError", "Required parameter, " + name + ", not present in query request", this.getClass().getName(), log_message);
					this.has_validation_errors = true;
					return;
				}
			}
			return;
		}
		if (multiple && !(value instanceof ArrayList)) {
			response.add_error("XDSRegistryError", "Parameter, " + name + ", accepts multiple values but (  ) syntax is missing", this.getClass().getName(), log_message);
			this.has_validation_errors = true;
			return;
		}
		if (!multiple && (value instanceof ArrayList)) {
			response.add_error("XDSRegistryError", "Parameter, " + name + ", accepts single value value only but (  )  syntax is present", this.getClass().getName(), log_message);
			this.has_validation_errors = true;
			return;
		}
		if (multiple && (value instanceof ArrayList) && ((ArrayList) value).size() == 0) {
			response.add_error("XDSRegistryError", "Parameter, " + name + ", (  )  syntax is present but list is empty", this.getClass().getName(), log_message);
			this.has_validation_errors = true;
			return;
		}

		if ( ! (value instanceof ArrayList) )
			return;

		ArrayList values = (ArrayList) value;

		for (int i=0; i<values.size(); i++) {
			Object a_o = values.get(i);
			if (	is_string && 
					!(a_o instanceof String) && 
					!(     (a_o instanceof ArrayList)   &&   
							((ArrayList)a_o).size() > 0    &&   
							( ((ArrayList)a_o).get(0) instanceof String) 
					)
			) {
				response.add_error("XDSRegistryError", "Parameter, " + name + ", is not coded as a string (is type " + a_o.getClass().getName() + ") (single quotes missing?)", this.getClass().getName(), log_message);
				this.has_validation_errors = true;
			}
			if (!is_string && !(a_o instanceof Integer)) {
				response.add_error("XDSRegistryError", "Parameter, " + name + " is not coded as a number (is type " + a_o.getClass().getName() + ") (single quotes present)", this.getClass().getName(), log_message);
				this.has_validation_errors = true;
			}
		}

		if (same_size_as == null)
			return;

		Object same_as_value = parms.get(same_size_as);
		if ( !(same_as_value instanceof ArrayList)) {
			response.add_error("XDSRegistryError", "Parameter, " + same_size_as + " must have same number of values as parameter " + name, this.getClass().getName(), log_message);
			this.has_validation_errors = true;
			return;
		}
		ArrayList same_as_values = (ArrayList) same_as_value;

		if ( !(value instanceof ArrayList)) {
			response.add_error("XDSRegistryError", "Parameter, " + same_size_as + " must have same number of values as parameter " + name, this.getClass().getName(), log_message);
			this.has_validation_errors = true;
			return;
		}

		if (same_as_values.size() != values.size()) {
			response.add_error("XDSRegistryError", "Parameter, " + same_size_as + " must have same number of values as parameter " + name, this.getClass().getName(), log_message);
			this.has_validation_errors = true;
			return;
		}

	}

	protected OMElement get_doc_by_uuid(String uuid) throws XdsException {
		ArrayList<String> ids = new ArrayList<String>();
		ids.add(uuid);
		return get_doc_by_uuid(ids);
	}

	protected OMElement get_doc_by_uuid(ArrayList<String> uuids) throws XdsException {
		init();
		if (this.return_leaf_class)
			a("SELECT * FROM ExtrinsicObject eo");
		else
			a("SELECT eo.id FROM ExtrinsicObject eo");  
		n();
		a("WHERE ");  n();
		a("	  eo.id IN "); a(uuids);  n(); 

		return query(this.return_leaf_class);
	}

	protected OMElement get_doc_by_uid(String uid)  throws XdsException {
		ArrayList<String> uids = new ArrayList<String>();
		uids.add(uid);
		return get_doc_by_uid(uids);
	}

	protected OMElement get_doc_by_uid(ArrayList<String> uids) throws XdsException {
		init();
		if (this.return_leaf_class)
			a("SELECT * from ExtrinsicObject eo, ExternalIdentifier ei"); 
		else
			a("SELECT eo.id from ExtrinsicObject eo, ExternalIdentifier ei"); 
		n();
		a("WHERE "); n();
		a("  ei.registryObject=eo.id AND"); n();
		a("  ei.identificationScheme='urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab' AND"); n();
		a("  ei.value IN "); a(uids);  n();

		return query(this.return_leaf_class);
	}

	protected String get_doc_id_from_uid(String uid) throws XdsException {
		boolean rlc = return_leaf_class;
		this.return_leaf_class = false;
		OMElement result = get_doc_by_uid(uid);
		Metadata metadata = MetadataParser.parseNonSubmission(result);
		ArrayList<OMElement> obj_refs = metadata.getObjectRefs();
		if (obj_refs.size() == 0)
			return null;

		return_leaf_class = rlc;

		return metadata.getId(obj_refs.get(0));
	}

	protected OMElement get_associations(ArrayList<String> uuids, ArrayList<String> assoc_types) throws XdsException {

		init();
		if (this.return_leaf_class)
			a("SELECT * FROM Association a");
		else
			a("SELECT a.id FROM Association a");  
		n();
		a("WHERE ");  n();
		a("	  (a.sourceObject IN "); a(uuids); a(" OR");  n(); 
		a("	  a.targetObject IN "); a(uuids); a(" )"); n(); 
		if (assoc_types != null) {
			a("   AND a.associationType IN "); a(assoc_types); n();
		}

		return query(this.return_leaf_class);
	}

	protected void add_code(String code_var, String code_scheme_var, String code_class_uuid,
			ArrayList codes, ArrayList schemes) throws MetadataException {
		if (codes != null)  {                        a("AND ("); a(code_var);    a(".classifiedobject = doc.id AND "); n(); }
		if (codes != null)  {                        a("  ");    a(code_var);    a(".classificationScheme = '"); a(code_class_uuid); a("' AND "); n(); }  
		if (codes != null)  {                        a("  ");    a(code_var);    a(".nodeRepresentation IN "); a(codes);  a(" )"); n();                 }            

		if (schemes != null) {                  a("AND ("); a(code_scheme_var); a(".parent = "); a(code_var); a(".id AND   "); n();     }
		if (schemes != null) {                  a("  "); a(code_scheme_var); a(".name = 'codingScheme' AND   "); n();                    }                       
		if (schemes != null) {                  a("  "); a(code_scheme_var); a(".value IN "); a(schemes); a(" )"); n();                   }           
	}

	protected void add_code(ArrayList<String> code_vars, String code_scheme_var, String code_class_uuid, 
			ArrayList<Object> codes, ArrayList schemes) throws MetadataException {
		if (codes == null)
			return;

		if (code_vars.size() == 1) {
			if (codes instanceof And)
				throw new MetadataException("StoredQuery.add_code(): code_vars.size()==1 but codes have type And");
			String code_var = code_vars.get(0);
			a("AND ("); a(code_var);    a(".classifiedobject = doc.id AND "); n(); 
			a("  ");    a(code_var);    a(".classificationScheme = '"); a(code_class_uuid); a("' AND "); n(); 
			a("  ");    a(code_var);    a(".nodeRepresentation IN "); a(codes);  a(" )"); n();          

			if (schemes != null) {                  
				a("AND ("); a(code_scheme_var); a(".parent = "); a(code_var); a(".id AND   "); n();     
				a("  "); a(code_scheme_var); a(".name = 'codingScheme' AND   "); n();                                          
				a("  "); a(code_scheme_var); a(".value IN "); a(schemes); a(" )"); n();   
			}           
		} else {
			if ( !(codes instanceof And))
				throw new MetadataException("StoredQuery.add_code():  code_vars.size() > 1 but codes not of type And");
			
			for (int i=0; i<code_vars.size(); i++) {
				String code_var = code_vars.get(i);
				ArrayList<String> codes2 = (ArrayList<String>) codes.get(i);

				a("AND ("); a(code_var);    a(".classifiedobject = doc.id AND "); n(); 
				a("  ");    a(code_var);    a(".classificationScheme = '"); a(code_class_uuid); a("' AND "); n();  
				a("  ");    a(code_var);    a(".nodeRepresentation IN "); a(codes2);  a(" )"); n();                           
			}
		}
	}

	// times come in as numeric values but convert them to string values to avoid numeric overflow
	protected void add_times(String att_name, String from_var, String to_var,
			String from_limit, String to_limit, String var_name) {
		if (from_limit != null)  { a("AND (");  a(from_var); a(".parent = " + var_name + ".id AND ");      n();      }                                         
		if (from_limit != null)  {  a("  ");a(from_var);  a(".name = '"); a(att_name); a("' AND     ");      n(); }                                              
		if (from_limit != null)  { a("  "); a(from_var); a(".value >= "); a_quoted(from_limit); a(" ) "); n(); }

		if (to_limit != null)    { a("AND (");  a(to_var); a(".parent = " + var_name + ".id AND ");      n();         }                                      
		if (to_limit != null)   { a("  "); a(to_var);  a(".name = '"); a(att_name); a("' AND     ");      n(); }                                              
		if (to_limit != null)   { a("  "); a(to_var); a(".value < "); a_quoted(to_limit); a(" ) "); n();            }                  
	}

	public OMElement get_rp_by_uid(String uid, String identification_scheme) throws XdsException {
		init();
		a("SELECT * FROM RegistryPackage ss, ExternalIdentifier uniq"); n();
		a("WHERE");  n();
		a("  uniq.registryObject = ss.id AND");  n();
		a("  uniq.identificationScheme = '" + identification_scheme + "' AND");  n();
		a("  uniq.value = '" + uid + "'");
		return query();
	}

	public OMElement get_rp_by_uid(ArrayList<String> uids, String identification_scheme) throws XdsException {
		init();
		a("SELECT * FROM RegistryPackage ss, ExternalIdentifier uniq"); n();
		a("WHERE");  n();
		a("  uniq.registryObject = ss.id AND");  n();
		a("  uniq.identificationScheme = '" + identification_scheme + "' AND");  n();
		a("  uniq.value IN "); a(uids);
		return query();
	}

	protected OMElement get_rp_by_uuid(String ss_uuid, String identification_scheme)
	throws XdsException {
		init();
		a("SELECT * FROM RegistryPackage ss, ExternalIdentifier uniq");  n();
		a("WHERE ");  n();
		a("	  ss.id = '" + ss_uuid + "' AND");  n(); 
		a("   uniq.registryObject = ss.id AND");  n();
		a("   uniq.identificationScheme = '" + identification_scheme + "' ");  n();

		return query();
	}

	protected OMElement get_rp_by_uuid(ArrayList<String> ss_uuid, String identification_scheme)
	throws XdsException {
		init();
		a("SELECT * FROM RegistryPackage ss, ExternalIdentifier uniq");  n();
		a("WHERE ");  n();
		a("	  ss.id IN "); a(ss_uuid); a(" AND");  n(); 
		a("   uniq.registryObject = ss.id AND");  n();
		a("   uniq.identificationScheme = '" + identification_scheme + "' ");  n();

		return query();
	}

	protected OMElement get_objects_by_uuid(ArrayList<String> uuids) throws XdsException {
		if (uuids.size() == 0)
			return null;

		init();
		a("SELECT * FROM RegistryObject ro");  n();
		a("WHERE ");  n();
		a("	  ro.id IN "); a(uuids);  n();

		return query();
	}

	protected OMElement get_fol_by_uuid(String uuid) throws XdsException
	{ return get_rp_by_uuid(uuid, "urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a"); }

	protected OMElement get_fol_by_uuid(ArrayList<String> uuid) throws XdsException 
	{ return get_rp_by_uuid(uuid, "urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a"); }


	protected OMElement get_fol_by_uid(String uid) throws XdsException 
	{ return get_rp_by_uid(uid, "urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a"); }

	protected OMElement get_fol_by_uid(ArrayList<String> uid) throws XdsException 
	{ return get_rp_by_uid(uid, "urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a"); }

	protected OMElement get_ss_by_uuid(String uuid) throws XdsException 
	{ return get_rp_by_uuid(uuid, "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8"); }


	protected OMElement get_ss_by_uid(String uid) throws XdsException 
	{ return get_rp_by_uid(uid, "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8"); }

	protected OMElement get_ss_docs(String ss_uuid, ArrayList<String> format_codes, ArrayList<String> conf_codes)
	throws XdsException {
		init();
		a("SELECT * FROM ExtrinsicObject doc, Association a");  n();
		if (conf_codes != null) { a(", Classification conf");  n();  }
		if (format_codes != null){a(", Classification fmtCode");  n();  }
		if (false)     {          a(", Slot adomain");   }

		a("WHERE");   n();                                                                                     
		a("   a.sourceObject = '" + ss_uuid + "' AND");  n();                                                  
		a("   a.associationType = '");
        a(MetadataSupport.xdsB_eb_assoc_type_has_member);
        a("' AND");       n();
		a("   a.targetObject = doc.id ");   n();

		if (conf_codes != null && conf_codes.size() > 0) {
			a("  AND (");       n();
			a("   conf.classificationScheme = 'urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f' AND");  n();         
			a("   conf.classifiedObject = doc.id AND");             n();
			a("   conf.nodeRepresentation IN ");  a(conf_codes);  n();
			a(")");                         n();
		}

		if (format_codes != null && format_codes.size() > 0) {
			a("AND (");  n();
			a("  fmtCode.classifiedObject = doc.id AND");   n();  
			a("  fmtCode.classificationScheme = 'urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d' AND ");  n();
			a("  fmtCode.nodeRepresentation IN "); a(format_codes); n();
			a(" ) ");  n();
		}		
		if (false) {	
			a("  AND (adomain.name='AffinityDomain' AND adomain.parent=doc.id AND adomain.value=$affinitydomain) ");
		}

		return query();
	}

	protected OMElement get_fol_docs(String fol_uuid, ArrayList<String> format_codes, ArrayList<String> conf_codes) 
	throws XdsException {
		return get_ss_docs(fol_uuid, format_codes, conf_codes);
	}

	protected OMElement get_assocs(String package_uuid, ArrayList<String> content_uuids) throws XdsException {
		init();

		a("SELECT * FROM Association ass"); n();
		a("WHERE"); n();
		a("   ass.associationType = '");
        a(MetadataSupport.xdsB_eb_assoc_type_has_member);
        a("' AND"); n();
		a("   ass.sourceObject = '" + package_uuid + "' AND"); n();
		a("   ass.targetObject IN (" );
		for (int i=0; i<content_uuids.size(); i++) {
			if (i > 0) a(",");
			a("'" + (String) content_uuids.get(i) + "'");
		}
		a(")");
		n();

		return query();
	}

	protected OMElement get_assocs(ArrayList<String> package_uuids, ArrayList<String> content_uuids) throws XdsException {
		init();

		a("SELECT * FROM Association ass"); n();
		a("WHERE"); n();
		a("   ass.associationType = '");
        a(MetadataSupport.xdsB_eb_assoc_type_has_member);
        a("' AND"); n();
		a("   ass.sourceObject IN (");
		for (int i=0; i<package_uuids.size(); i++) {
			if (i > 0) a(",");
			a("'" + (String) package_uuids.get(i) + "'");
		}
		a(") AND"); n();
		a("ass.targetObject IN (" );
		for (int i=0; i<content_uuids.size(); i++) {
			if (i > 0) a(",");
			a("'" + (String) content_uuids.get(i) + "'");
		}
		a(")");
		n();

        return query(true);
	}

	protected OMElement get_assocs(ArrayList<String> package_uuids) throws XdsException {
		if (package_uuids == null || package_uuids.size() == 0)
			return null;
		
		init();

		a("SELECT * FROM Association ass"); n();
		a("WHERE"); n();
		a("   ass.associationType = '");
        a(MetadataSupport.xdsB_eb_assoc_type_has_member);
        a("' AND"); n();
		a("   ass.sourceObject IN (");
		for (int i=0; i<package_uuids.size(); i++) {
			if (i > 0) a(",");
			a("'" + (String) package_uuids.get(i) + "'");
		}
		a(")");
		n();

		return query();
	}

	protected OMElement get_folders_for_document(String uuid)  throws XdsException {
		init();
		if (this.return_leaf_class)
			a("SELECT * FROM RegistryPackage fol, ExternalIdentifier uniq");
		else
            a("SELECT fol.id FROM RegistryPackage fol, ExternalIdentifier uniq");
			//a("SELECT * FROM RegistryPackage fol, ExternalIdentifier uniq");
		a(", Association a"); n();
		a("WHERE"); n();

		a("   a.associationType = '");
        a(MetadataSupport.xdsB_eb_assoc_type_has_member);
        a("' AND"); n();
		a("   a.targetObject = '" + uuid + "' AND"); n();
		a("   a.sourceObject = fol.id AND"); n();
		a("   uniq.registryObject = fol.id AND");  n();
		a("   uniq.identificationScheme = '" + "urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a" + "' ");  n();

		// FIXME (BHT): Investigate (used to always return LEAF CLASS.
        return query(this.return_leaf_class);
        //return query();
	}
}

