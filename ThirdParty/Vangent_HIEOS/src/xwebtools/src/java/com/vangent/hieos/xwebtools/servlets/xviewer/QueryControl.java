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

package com.vangent.hieos.xwebtools.servlets.xviewer;

import com.vangent.hieos.xwebtools.servlets.framework.HttpUtils;
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.xml.Util;
import com.vangent.hieos.xutil.xml.XmlFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.axiom.om.OMElement;

public class QueryControl {
	String endpoint;
	// uuid => <query_type, query_contents>
	// in case of error, uuid could be uid instead
	HashMap<String, HashMap<String, QueryContents>> map;
	ArrayList<QueryContents> query_contents;
	// repositoryUniqueId -> [secure endpoint, unsecure endpoint]
	HashMap<String, ArrayList<String>> repositories; 
	Metadata metadata;  // contents of all queries combined
	// query types
	static final String ssac="ssac";  // GetSubmissionSetAndContents
	boolean leafClassQuery = true;
	String queryAction = "urn:ihe:iti:2007:RegistryStoredQuery";
	String queryResultAction = "urn:ihe:iti:2007:RegistryStoredQueryResponse";
	boolean isXca = false;
	String home = "";
	boolean soap11 = false;
	
	public void setSoap11(boolean soap11) {
		this.soap11 = soap11;
	}
	public void setHome(String home) {
		this.home = home;
	}
	
	public void leafClassQuery(boolean newvalue) {
		leafClassQuery = newvalue;
	}
	
	public void crossGateway() {
		this.queryAction = "urn:ihe:iti:2007:CrossGatewayQuery";
		this.queryResultAction = "urn:ihe:iti:2007:CrossGatewayQueryResponse";
		isXca = true;
	}

	public QueryControl() {
		map = new HashMap<String, HashMap<String, QueryContents>>();
		metadata = new Metadata();
		query_contents = new ArrayList<QueryContents>();
	}

	public QueryControl(QueryContents qc) {
		this.endpoint = null;
		map = null;
		metadata = qc.getMetadata();
		query_contents = new ArrayList<QueryContents>();
		query_contents.add(qc);
	}

	void log_exception(Exception e, QueryContents qc) {
		StackTraceElement[] st = e.getStackTrace();
		StringBuffer buf = new StringBuffer();
		for (StackTraceElement ste : st) {
			buf.append(ste.toString());
			buf.append("\n");
		}
		qc.addFatalError(buf.toString());
	}

	public QueryControl(String file_contents) {
		this.endpoint = null;
		this.map = null;
		this.metadata = new Metadata();
		this.query_contents = new ArrayList<QueryContents>();
		OMElement ele = null;
		try {
			ele = Util.parse_xml(file_contents);
		}
		catch (Exception e) {
			QueryContents qc = new BasicQueryContents();
			qc.setQueryType("BadResults");
			log_exception(e, qc);
			try {
				this.addQueryContents(qc);
			}
			catch (Exception e1 ) { log_exception(e1, qc); }
		}

		System.out.println("file input parse complete: root is " + ele.getLocalName());

		if (ele.getLocalName().equals("TestResults")) {
			OMElement fatal_ele = MetadataSupport.firstChildWithLocalName(ele, "FatalError");
			if (fatal_ele != null) {
				System.out.println("fatal error: " + fatal_ele.getText());
				String fatal_message = fatal_ele.getText();
				if (fatal_message != null && !fatal_message.equals("")) {
					QueryContents qc = new BasicQueryContents();
					qc.addFatalError(fatal_message);
					qc.setQueryType("FatalError");
					try {
						this.addQueryContents(qc);
					} catch (Exception e) { log_exception(e,qc); }
				}
			}
			for(OMElement ts : MetadataSupport.childrenWithLocalName(ele, "TestStep")) {

				try {
					String id = ts.getAttributeValue(MetadataSupport.id_qname);
					String step_status = ts.getAttributeValue(MetadataSupport.status_qname);
					OMElement trans_output = find_transaction_output(ts);
					if (trans_output == null) {
						throw new Exception("Cannot find transaction output from step " + id);
					}
					OMElement input = MetadataSupport.firstChildWithLocalName(trans_output, "InputMetadata");
					if (input == null) {
						throw new Exception("Cannot find InputMetadata from step " + id);
					}
					OMElement result = MetadataSupport.firstChildWithLocalName(trans_output, "Result");
					if (result == null) {
						throw new Exception("Cannot find Result from step " + id);
					}
					OMElement result_1 = result.getFirstElement();
					if (result_1 == null) {
						throw new Exception("Cannot find content of Result from step " + id);
					}
					String result_status = result_1.getAttributeValue(MetadataSupport.status_qname);
					OMElement expected_status_ele = MetadataSupport.firstChildWithLocalName(ts, "ExpectedStatus");
					String expected_status = "none";
					if (expected_status_ele == null) {
						throw new Exception("Cannot find ExpectedStatus from step " + id);
					} else {
						expected_status = expected_status_ele.getText();
					}
					LogFileQueryContents qc = new LogFileQueryContents();
					qc.setQueryType("Step " + id);
					qc.expected_status = expected_status;
					qc.reference_id = id;
					qc.step_status = step_status;
					qc.input = (input != null) ? input.getFirstElement() : null;
					qc.result = (result != null) ? result.getFirstElement() : null;
					qc.teststep = ts;
					qc.findMetadata();
					System.out.println("Found step " + id);
					//qc.validate_inputs();
					if (qc.getMetadata() == null)
						System.out.println("Did not find metadata in step");
					else
						System.out.println("Found metadata in step");
					try {
						this.addQueryContents(qc);
					}
					catch (Exception e1 ) {  log_exception(e1, qc); }

				}
				catch (Exception e) {
					QueryContents qc = new BasicQueryContents();
					qc.setQueryType("BadResults");
					log_exception(e, qc);
					System.out.println("log exception: " + e.getMessage());
					try {
						this.addQueryContents(qc);
					}
					catch (Exception e1 ) { log_exception(e1, qc); }
				}

			}
		} else {
			QueryContents qc = new SSandContentsQueryContents();
			qc.parse_metadata_from_log_file(ele);
			try {
				this.addQueryContents(qc);
			} catch (Exception e ) { log_exception(e, qc); }
		}
	}

	public void deleteQueryContents(int i) {
		if (i < this.query_contents.size())
			this.query_contents.set(i, null);
	}

	OMElement find_transaction_output(OMElement step_ele) {
		for (Iterator<OMElement> it=step_ele.getChildElements(); it.hasNext(); ) {
			OMElement child = it.next();
			String name = child.getLocalName();
			if (name.endsWith("Transaction"))
				return child;
		}
		return null;
	}



	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public boolean hasEndpoint() { return endpoint != null; }

	boolean is_uuid(String id) {
		return id.startsWith("urn:uuid:");
	}

	void add_results_for_object(String uuid, String query_type, QueryContents contents) {
		HashMap<String, QueryContents> contents_map = map.get(uuid);
		if (contents_map == null) {
			contents_map = new HashMap<String, QueryContents>();
			map.put(uuid, contents_map);
		}
		contents_map.put(query_type, contents);
		query_contents.add(contents);
	}

	public QueryContents get_results_for_object(String uuid, String query_type) {
		HashMap<String, QueryContents> contents_map = map.get(uuid);
		if (contents_map == null)
			return null;
		return contents_map.get(query_type);
	}

	public ArrayList<QueryContents> getAllQueryContents() {
		return query_contents;
	}

	public QueryContents getQueryContents(int i) {
		if (i < 0 || i >= query_contents.size()) return null;
		return query_contents.get(i);
	}
	
	public int getQueryContentsIndex(QueryContents contents) {
		for (int i=0; i<query_contents.size(); i++) {
			if (query_contents.get(i) == contents)
				return i;
		}
		return -1;
	}

	boolean is_endpoint_secure() {
		return endpoint.startsWith("https");
	}

	public QueryContents queryFindFol(String pid) throws MetadataValidationException {
		FolQueryContents query_contents = new FolQueryContents();
		query_contents.setQueryType("Find Fol for");
		run_query(singleton(pid), new FindFolders(), query_contents, queryAction, queryResultAction, soap11);


		Metadata m = query_contents.getMetadata();
		boolean object_refs = m.isObjectRefsOnly();
		if (object_refs) {
			query_contents.init(this);
			query_contents.type_ss();
			query_contents.preload();
		}

		return query_contents;
	}

	public QueryContents queryFindSS(String pid) throws MetadataValidationException {
		SSQueryContents query_contents = new SSQueryContents();
		query_contents.setQueryType("Find SS for");
		run_query(singleton(pid), new FindSubmissionSets(), query_contents, queryAction, queryResultAction, soap11);


		Metadata m = query_contents.getMetadata();
		boolean object_refs = m.isObjectRefsOnly();
		if (object_refs) {
			query_contents.init(this);
			query_contents.type_ss();
			query_contents.preload();
		}

		return query_contents;
	}

	public QueryContents queryFindDoc(String pid) throws MetadataValidationException {
		FindDocsQueryContents query_contents = new FindDocsQueryContents();
		query_contents.setQueryType("Find Docs for");
		if (leafClassQuery) 
			run_query(singleton(pid), new FindDocuments(true, query_contents), query_contents, queryAction, queryResultAction, soap11);
		else
			run_query(singleton(pid), new FindDocuments(false, query_contents), query_contents, queryAction, queryResultAction, soap11);

		Metadata m = query_contents.getMetadata();
		boolean object_refs = m.isObjectRefsOnly();
		if (object_refs) {
			query_contents.init(this);
			query_contents.type_doc();
			query_contents.preload();
		}
			
		// remember returned homeCommunityId for later if it is present
		ArrayList<OMElement> objects = m.getMajorObjects();
		home = "";
		for (OMElement ob : objects) {
			String hom = m.getHome(ob);
			if (hom != null && !hom.equals(""))
				home = hom;
		}

		return query_contents;
	}

	// id may be id or uid
	public QueryContents queryGetDocuments(ArrayList<String> ids) {
		QueryContents query_contents = new DocsQueryContents();
		query_contents.setQueryType("Documents");
		run_query(ids, new GetDocument(), query_contents, queryAction, queryResultAction, soap11);

		return query_contents;

	}

	// id may be id or uid
	public QueryContents queryGetSSandContents(ArrayList<String> ids)  {
		QueryContents query_contents = new SSandContentsQueryContents();
		query_contents.setQueryType("SSandContents");
		run_query(ids, new GetSubmissionSetAndContents(), query_contents, queryAction, queryResultAction, soap11);

		return query_contents;
	}

	// id may be id or uid
	public QueryContents queryGetFolandContents(ArrayList<String> ids)  {
		QueryContents query_contents = new FolandContentsQueryContents();
		query_contents.setQueryType("FolandContents");
		run_query(ids, new GetFolderAndContents(), query_contents, queryAction, queryResultAction, soap11);

		return query_contents;
	}

	public QueryContents retrieve_b(ArrayList<String> ids) {
		RetrieveBQueryContents query_contents = new RetrieveBQueryContents();
		query_contents.setQueryType("Retrieve.b");
		query_contents.secure(is_endpoint_secure());
		query_contents.setInitialEvidence(ids);
		query_contents.setEndpoint(this.endpoint);
		RetrieveBEngine ret = new RetrieveBEngine();
		ret.setIsXca(isXca);
		ret.retrieve(ids.get(0), query_contents, getMetadata(), repositories, home);

		try {
			Metadata save = query_contents.getMetadata();
			query_contents.setMetadata(null);
			this.addQueryContents(query_contents);
			query_contents.setMetadata(save);
		}
		catch (Exception e) {
			query_contents.addException(e);
		}
		return query_contents;
	}

	private void run_query(ArrayList<String> ids, Sq query, QueryContents query_contents, String action, String returnAction, boolean soap11) {
		query_contents.secure(is_endpoint_secure());
		query_contents.setInitialEvidence(ids);
		query_contents.setEndpoint(this.endpoint);
		OMElement result = null;
		try {
			result = query.run(endpoint, ids, action, returnAction, soap11);
			query_contents.parse_metadata_from_registry_response(result);
			this.addQueryContents(query_contents);
		}
		catch (Exception e) {
			query_contents.addException(e.getClass(), e.getMessage());
			try {
				this.addQueryContents(query_contents);
			} catch (Exception e1) {}
		}


		Metadata m = query_contents.getMetadata();
		String uuid;
		String id = ids.get(0);
		if ( m != null) {
			uuid = (is_uuid(id)) ? id : m.getSubmissionSetId();
		} else {
			uuid = id;  // best we can do
		}

		query_contents.setReferenceId(uuid);
	}


	public QueryContents queryGetSubmissionSets(ArrayList<String> ids) {
		QueryContents query_contents = new SSQueryContents();
		query_contents.setQueryType("Get SS of");
		run_query(ids, new GetSubmissionSet(), query_contents, queryAction, queryResultAction, soap11);

		Metadata m = query_contents.getMetadata();
		if (m.getSubmissionSets().size() == 0) 
			query_contents.addError("No SubmissionSet returned");
		if (m.getAssociations().size() == 0) 
			query_contents.addError("No Association returned");
		if (m.getSubmissionSets().size() > 1) 
			query_contents.addError("Multiple SubmissionSets returned");
		if (m.getAssociations().size() > 1) 
			query_contents.addError("Multiple Associations returned");

		return query_contents;
	}

	public QueryContents queryGetRelated(ArrayList<String> ids) {
		QueryContents query_contents = new RelatedQueryContents();
		query_contents.setQueryType("Get related to");
		run_query(ids, new GetRelatedDocuments(), query_contents, queryAction, queryResultAction, soap11);

		return query_contents;
	}

	void addQueryContents(QueryContents qc) throws MetadataValidationException, MetadataException {
		query_contents.add(qc);
		if (qc.getMetadata() == null)
			qc.setMetadata(new Metadata());
		else
			metadata.addMetadata(qc.getMetadata(), false /* discard_duplicates */);
	}

	public String structures() {
		StringBuffer buf = new StringBuffer();

		buf.append(metadata.structure() + "\n");  

		for (int i=0; i<this.query_contents.size(); i++) {
			QueryContents qc = this.query_contents.get(i);
			buf.append(i + " " + qc.getMetadata().structure() + "\n");
		}

		return buf.toString();
	}

	public String structure() {
		return metadata.structure();
	}

	public Metadata getMetadata() {
		return metadata;
	}

	void displayDetail(String verb, Map<String, String[]> parms, HttpUtils h) {
		String local_id = getParm(parms, "id");
		String cntl = getParm(parms, "cntl");
		int cntl_i = 0;
		if (cntl != null)
			cntl_i = Integer.parseInt(cntl);

		if (local_id != null && !local_id.equals("")) {
			try {
				Metadata m;
				if (cntl != null) {
					QueryContents qc = getQueryContents(cntl_i);
					m = qc.getMetadata();
				} else
					m = this.getMetadata();
				System.out.println("Structure " + m.structure());
				System.out.println("verb " + verb);
				System.out.println("id " + local_id);
				System.out.println("docids " + m.getExtrinsicObjectIds());
				XView xv = new XView(m, h);
				xv.start();
				if (verb.equals("details")) {
					xv.displayRegistryObject(m, local_id);
				} 
				else if (verb.equals("xml")) {
					xv.displayXml(m, local_id);
				}
				xv.end();
			}
			catch (Exception e) {
				h.alert(e.getClass().getName() + ": " + e.getMessage());
				e.printStackTrace();
			}
		} 
		else if (verb.equals("errors") && cntl !=null) {
			QueryContents qc = this.getQueryContents(Integer.parseInt(cntl));
			if (qc != null) {
				display_arraylist(h, qc.getErrors());
			}

		} 
		else if (verb.equals("exceptions") && cntl !=null) {
			QueryContents qc = this.getQueryContents(Integer.parseInt(cntl));
			if (qc != null) {
				display_arraylist(h, qc.getExceptions());
			}

		} 
		else if (verb.equals("fatal") && cntl !=null) {
			QueryContents qc = this.getQueryContents(Integer.parseInt(cntl));
			if (qc != null) {
				display_arraylist(h, qc.getFatalErrors());
			}
		}
		else if (verb.equals("queryresponsexml") && cntl !=null) {
			QueryContents qc = this.getQueryContents(Integer.parseInt(cntl));
			if (qc != null) {
				OMElement ele = qc.getResultXml();
				XView xv = new XView(h);
				xv.start();
				if (ele != null)
					xv.out(XmlFormatter.htmlize(ele.toString(), false));
				else
					xv.out("null");
				xv.end();
			}
		}
		else if (verb.equals("queryrequestxml") && cntl !=null) {
			QueryContents qc = this.getQueryContents(Integer.parseInt(cntl));
			if (qc != null) {
				OMElement ele = qc.getRequestXml();
				XView xv = new XView(h);
				xv.start();
				if (ele != null)
					xv.out(XmlFormatter.htmlize(ele.toString(), false));
				else
					xv.out("null");
				xv.end();
			}
		}
		else if (verb.equals("inputxml")) {
			XView xv = new XView(h);
			xv.start();
			LogFileQueryContents qc = (LogFileQueryContents) this.getQueryContents(cntl_i);
			qc.displayInput(xv);
			xv.end();
		}
		else if (verb.equals("resultxml")) {
			XView xv = new XView(h);
			xv.start();
			LogFileQueryContents qc = (LogFileQueryContents) this.getQueryContents(cntl_i);
			qc.displayResult(xv);
			xv.end();
		}
		else if (verb.equals("stepxml")) {
			XView xv = new XView(h);
			xv.start();
			LogFileQueryContents qc = (LogFileQueryContents) this.getQueryContents(cntl_i);
			qc.displayTestStep(xv);
			xv.end();
		}
	}

	private String getParm(Map<String, String[]> parms, String parm_name) {
		String value;
		String[] id_array = (String[]) parms.get(parm_name);
		if (id_array != null && id_array.length > 0)
			value = id_array[0];
		else {
			value = null;
		}
		return value;
	}

	void display_arraylist(HttpUtils h, ArrayList<String> al) {
		for (String s : al) {
			h.o(s);
			h.br();
		}
	}

	ArrayList<String> singleton(String value) {
		ArrayList<String> al = new ArrayList<String>(1);
		al.add(value);
		return al;
	}


}
