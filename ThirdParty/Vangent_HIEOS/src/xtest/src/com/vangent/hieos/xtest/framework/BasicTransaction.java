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

package com.vangent.hieos.xtest.framework;

import com.vangent.hieos.xutil.exception.ExceptionUtil;
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.NoMetadataException;
import com.vangent.hieos.xutil.exception.SchemaValidationException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.structure.IdParser;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataParser;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.registry.OmLogger;
import com.vangent.hieos.xutil.response.RegistryErrorList;
import com.vangent.hieos.xutil.response.RegistryResponseParser;
import com.vangent.hieos.xutil.registry.RegistryUtility;
import com.vangent.hieos.xutil.soap.SoapActionFactory;
import com.vangent.hieos.xutil.soap.Soap;
import com.vangent.hieos.xutil.xml.Util;
import com.vangent.hieos.xtest.main.XTestDriver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.parsers.FactoryConfigurationError;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.log4j.Logger;

public abstract class BasicTransaction extends OmLogger {
	protected OMElement instruction;
	protected OMElement instruction_output;
	protected StepContext s_ctx;

	XTestDriver xTestDriver;
	boolean step_failure = false;
	protected boolean parse_metadata = true;
	// metadata building linkage
	protected ArrayList<OMElement> use_id;
	protected ArrayList<OMElement> use_xpath ;
	protected ArrayList<OMElement> use_object_ref;
	protected ArrayList<OMElement> use_repository_unique_id;
	// assertion linkage
	protected ArrayList<OMElement> data_refs;
	protected ArrayList<OMElement> assertions;

	static public final short xds_none = 0;
	static public final short xds_a = 1;
	static public final short xds_b = 2;
	protected short xds_version = BasicTransaction.xds_none;
	protected String endpoint = null;
	protected HashMap<String, String> linkage ;
	protected String metadata_filename = null;
	protected boolean assign_uuids = false;
	protected boolean assign_uids = true;
	protected String no_assign_uid_to = null;
	protected boolean assign_patient_id = true;
	protected Metadata metadata = null;
	protected boolean soap_1_2 = true;
	protected String repositoryUniqueId = null;
	protected boolean async = false;
	private final static Logger logger = Logger.getLogger(BasicTransaction.class);
	protected HashMap<String, String> nameUuidMap = null;
	private Soap soap;
	protected boolean useMtom;
	protected boolean useAddressing;
	OMElement metadata_element;

	public abstract void run() throws XdsException;
	protected abstract String getRequestAction();

	public String toString() {
		String exceptionString = "";

		try {
			throw new Exception("foo");
		} catch (Exception e) { exceptionString = ExceptionUtil.exception_local_stack(e); }
		return new StringBuffer()
		.append("Called From:\n")
		.append(exceptionString)
		.append("Step = ").append(s_ctx.getId()).append("\n")
		.append("transaction = ").append(this.getClass().getName()).append("\n")
		.append("step_failure = ").append(step_failure).append("\n")
		.append("parse_metadata = ").append(parse_metadata).append("\n")
		.append("xds_version = ").append(xdsVersionName()).append("\n")
		.append("use_id = ").append((use_id == null) ? null : use_id.toString())
		.append("use_xpath = ").append((use_xpath == null) ? null : use_xpath.toString())
		.append("use_object_ref = ").append((use_object_ref == null) ? null : use_object_ref.toString())
		.append("use_repository_unique_id = ").append((use_repository_unique_id == null) ? null : use_repository_unique_id.toString())
		.append("data_refs = ").append((data_refs == null) ? null : data_refs.toString())
		.append("assertions = ").append((assertions == null) ? null : assertions.toString())
		.append("endpoint = ").append(endpoint).append("\n")
		.append("linkage = ").append((linkage == null) ? null : linkage.toString())
		.append("metadata_filename = ").append(metadata_filename).append("\n")
		.append("assign_uuids = ").append(assign_uuids).append("\n")
		.append("assign_uids = ").append(assign_uids).append("\n")
		.append("no_assign_uid_to = ").append(no_assign_uid_to).append("\n")
		.append("assign_patient_id = ").append(assign_patient_id).append("\n")
		.append("soap_1_2 = ").append(soap_1_2).append("\n")
		.append("repositoryUniqueId = ").append(repositoryUniqueId).append("\n")
		.append("nameUuidMap = ").append(nameUuidMap).append("\n")

		.toString();


	}

	public String xdsVersionName() {
		if (xds_version == BasicTransaction.xds_none)
			return "None";
		if (xds_version == BasicTransaction.xds_a)
			return "XDS.a";
		if (xds_version == BasicTransaction.xds_b)
			return "XDS.b";
		return "Unknown";
	}


	// to force sub-classes to use following constructor
	private BasicTransaction() {  }

	protected BasicTransaction(StepContext s_ctx, OMElement instruction, OMElement instruction_output) {
		this.s_ctx = s_ctx;
		this.instruction = instruction;
		this.instruction_output = instruction_output;

		use_id = new ArrayList<OMElement>();
		use_xpath = new ArrayList<OMElement>();
		use_object_ref = new ArrayList<OMElement>();
		use_repository_unique_id = new ArrayList<OMElement>();
		data_refs = new ArrayList<OMElement>();
		assertions = new ArrayList<OMElement>();
		linkage = new HashMap<String, String>();
	}

	String xds_version_name() {
		if (xds_version == xds_a)
			return "XDS.a";
		if (xds_version == xds_b)
			return "XDS.b";
		return "Unknown";
	}

	public void failed() {
		step_failure = true;
	}

	public void validate_registry_response_in_soap(OMElement env, int metadata_type) throws XdsException {
		if (!env.getLocalName().equals("Envelope")) 
			throw new XdsInternalException("Expected 'Envelope' but found " + env.getLocalName() + " instead");
		OMElement hdr = env.getFirstElement();
		if (hdr == null)
			throw new XdsInternalException("Expected 'Header' but found nothing instead");
		if (!hdr.getLocalName().equals("Header")) 
			throw new XdsInternalException("Expected 'Header' but found " + hdr.getLocalName() + " instead");
		Object next = hdr.getNextOMSibling();
		if (!(next instanceof OMElement))
			throw new XdsInternalException("Body not of type OMElement, instead found " + ((next == null) ? "null" : next.getClass().getName()	));
		OMElement body = (OMElement) next;
		if (body == null)
			throw new XdsInternalException("Expected 'Body' but found nothing instead");
		if (!body.getLocalName().equals("Body")) 
			throw new XdsInternalException("Expected 'Body' but found " + body.getLocalName() + " instead");
		validate_registry_response(body.getFirstElement(), metadata_type);

	}

	protected void validate_registry_response(OMElement result, int metadata_type) throws XdsException {
		// metadata type was MetadataTypes.METADATA_TYPE_PR
		validate_registry_response_no_set_status(result, metadata_type);		

		add_step_status_to_output();
	}


	public void validate_registry_response_no_set_status(OMElement registry_result, int metadata_type) throws XdsException {
		if (registry_result == null) {
			s_ctx.set_error("No Result message");
			step_failure = true;
			return;
		}

		RegistryResponseParser registry_response = new RegistryResponseParser(registry_result);

		// schema validate response
		String schema_results = "";
		try {
			RegistryUtility.schema_validate_local(registry_result, metadata_type);
		} catch (SchemaValidationException e) {
			s_ctx.set_error("Schema validation threw exception: " + e.getMessage());
			step_failure = true;
		}

		// check that status == success
		String status = registry_response.get_registry_response_status();
		ArrayList<String> returned_code_contexts = registry_response.get_error_code_contexts();

		RegistryErrorList rel  = null;

		try {
			rel = RegistryUtility.metadata_validator(new MetadataParser().parseNonSubmission(registry_result), false);
		} catch (NoMetadataException e) {
			// not all responses contain metadata
			rel = new RegistryErrorList((xds_version == xds_a ? RegistryErrorList.version_2 : RegistryErrorList.version_3));
		}

		ArrayList<String> validatorErrors = new RegistryResponseParser(rel.getRegistryErrorList()).get_error_code_contexts();
		returned_code_contexts.addAll(validatorErrors);

		eval_expected_status(status, returned_code_contexts);
		if (step_failure == false && validatorErrors.size() != 0) {
			StringBuffer msg = new StringBuffer();
			for (int i=0; i<validatorErrors.size(); i++) {
				msg.append(validatorErrors.get(i));
				msg.append("\n");
			}
			s_ctx.set_error(msg.toString());
			step_failure = true;
		}
	}

	String getExpectedStatusString() {
		return (s_ctx.getExpectedStatus()) ? "Success" : "Failure";
	}

	public void eval_expected_status(String status,
			ArrayList<String> returned_code_contexts) throws XdsInternalException {
		boolean expected_status = s_ctx.getExpectedStatus();
		//		if (!expected_status) {
		//			step_failure = false;
		//			this.s_ctx.resetStatus();
		//		}
		if (xds_version == xds_a) {
			if ("Success".equals(status) != expected_status) {
				s_ctx.set_error("Status is " + status + ", expected status is " + getExpectedStatusString());
				step_failure = true;
			}
		} else {
			if ( ! status.startsWith(MetadataSupport.response_status_type_namespace)) {
				s_ctx.set_error("Status is " + status + " , expected status is " + MetadataSupport.response_status_type_namespace + getExpectedStatusString());
				step_failure = true;
			} else {
				String[] parts = status.split(":");
				if ( "Success".equals(parts[parts.length-1]) != expected_status) {
					s_ctx.set_error("Status is " + status + ", expected status is " + MetadataSupport.response_status_type_namespace + getExpectedStatusString());
					step_failure = true;
				}
			}
		}

		// if expected error message specified then expected status must be Failure
		if (  !s_ctx.expectedErrorMessage.equals("") ) {
			if ( s_ctx.expectedstatus)
				fatal("ExpectedErrorMessage specified but ExpectedStatus is Success. This does not make sense");
		}

		// check that the error message matches expected
		boolean error_message_found = false;
		if (  !s_ctx.expectedErrorMessage.equals("") ) {
			for (int i=0; i<returned_code_contexts.size(); i++) {
				String message = returned_code_contexts.get(i);
				if (message.indexOf(s_ctx.expectedErrorMessage) != -1) {
					error_message_found = true;
				}
			}
			if (!error_message_found) {
				s_ctx.set_error("Did not find expected string in error messages: " + s_ctx.expectedErrorMessage + "\n");
				step_failure = true;
			}
		}
	}


	public void add_step_status_to_output() {
		s_ctx.setStatusInOutput(!step_failure);
		if (step_failure)
			s_ctx.add_name_value(instruction_output, "StepStatus", "Failure");
		else
			s_ctx.add_name_value(instruction_output, "StepStatus", "Success");
	}


	protected OMElement generate_xml(String root_name, HashMap map) {
		OMElement root = om_factory().createOMElement(root_name, null);
		for (Iterator it =map.keySet().iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			String value = (String) map.get(key);
			OMElement new_ele = om_factory().createOMElement("Assign", null);
			new_ele.addAttribute("symbol", key, null);
			new_ele.addAttribute("id", value, null);
			root.addChild(new_ele);
		}
		return root;
	}

	protected OMElement generate_xml(String root_name, String value) {
		OMElement root = om_factory().createOMElement(root_name, null);
		OMElement new_ele = om_factory().createOMElement("Assign", null);
		new_ele.addAttribute("id", value, null);
		root.addChild(new_ele);
		return root;
	}


	protected OMFactory om_factory() {
		return OMAbstractFactory.getOMFactory();
	}




	void print_step_history(OMElement this_instruction_output) {
		System.out.println("Step History:");
		OMElement step_output = (OMElement) this_instruction_output.getParent();
		while (step_output != null) {
			String id = step_output.getAttributeValue(new QName("id"));
			System.out.println("id = " + id);
			step_output = (OMElement) step_output.getPreviousOMSibling();
		}
	}

	void addToLinkage(HashMap<String, String> in) {
		for (Iterator<String> it=in.keySet().iterator(); it.hasNext(); ) {
			String key = it.next();
			String value = in.get(key);
			linkage.put(key, value);
		}
	}


	protected void compileUseRepositoryUniqueId(Metadata m, ArrayList<OMElement> useInputMetadata) throws XdsException {
		Linkage l = new Linkage(instruction_output, m);
		l.setUseRepositoryUniqueId(useInputMetadata);
		try {
			HashMap<String, String> in = l.compile();
			addToLinkage(in);
			repositoryUniqueId = l.getRepositoryUniqueId();
		}
		catch (XdsException e) {
			throw new XdsInternalException(ExceptionUtil.exception_details(e, "Step is " + s_ctx.get("step_id")));
		}
		//		catch (java.lang.NullPointerException e) {
		//		System.out.println("got it");
		//		System.out.println(ExceptionUtil.exception_details(e));
		//		System.out.println("had it");
		//		System.out.flush();
		//		}
	}



	protected void compileUseIdLinkage(Metadata m, ArrayList use_id) throws XdsException {
		Linkage l = new Linkage(instruction_output, m, use_id);
		try {
			addToLinkage(l.compile());
		}
		catch (XdsException e) {
			throw new XdsException(s_ctx.get("step_id") + ": " + e.getMessage());
		}

	}

	protected void compileUseObjectRefLinkage(Metadata m, ArrayList use_object_ref) throws XdsException {
		Linkage l = new Linkage(instruction_output, m);
		l.setUseObjectRef(use_object_ref);
		try {
			addToLinkage(l.compile());
		}
		catch (XdsException e) {
			throw new XdsException(s_ctx.get("step_id") + ": " + e.getMessage());
		}

	}

	protected void compileUseXPathLinkage(Metadata m, ArrayList use_xpath) throws XdsException {
		Linkage l = new Linkage(instruction_output, m);
		l.setUseXPath(use_xpath);
		try {
			HashMap<String, String> link = l.compile();
			addToLinkage(link);
			s_ctx.set("UseXPath", link.toString());
		}
		catch (XdsException e) {
			throw new XdsException(s_ctx.get("step_id") + ": " + e.getMessage() + ExceptionUtil.exception_details(e));
		}

	}

	protected void parseEndpoint(String trans) throws XdsInternalException {
		endpoint = this.s_ctx.getRegistryEndpoint();   // this is busted, always returns null
		if (endpoint == null || endpoint.equals("")) {
			//endpoint = this.s_ctx.getRegistryEndpoint(trans, xds_version);
			if (TestConfig.endpoints != null) {

				if (trans.endsWith(".as")) {
					xds_version = xds_b;
					if (TestConfig.secure) {
						endpoint = (String) TestConfig.secure_endpoints.get(trans);
					} else {
						endpoint = (String) TestConfig.endpoints.get(trans);
					}

				} else if (xds_version == xds_b) {
					if (TestConfig.secure) {
						endpoint = (String) TestConfig.secure_endpoints.get(trans + ".b");
						if (endpoint == null)
							endpoint = (String) TestConfig.secure_endpoints.get(trans);
					} else {
						endpoint = (String) TestConfig.endpoints.get(trans + ".b");
						if (endpoint == null)
							endpoint = (String) TestConfig.endpoints.get(trans);
					}
				} else {
					if (TestConfig.secure) {
						endpoint = (String) TestConfig.secure_endpoints.get(trans + ".a");
						if (endpoint == null)
							endpoint = (String) TestConfig.secure_endpoints.get(trans);
					} else {
						endpoint = (String) TestConfig.endpoints.get(trans + ".a");
						if (endpoint == null)
							endpoint = (String) TestConfig.endpoints.get(trans);
					}
				}
			}
			if (endpoint == null || endpoint.equals(""))
				fatal("No endpoint specified for transaction " + trans + " and XDS version " + xds_version_name() + 
						" and secure = " + TestConfig.secure +
						" on server " + TestConfig.target + "\nactor config is " + TestConfig.endpoints.toString());
			s_ctx.add_name_value(instruction_output, "Endpoint", endpoint);
			logger.info("Set Registry Endpoint = " + endpoint);
		}
	}

	protected void parseRepEndpoint(String repositoryUniqueId, boolean isSecure) throws XdsInternalException {
		endpoint = null;
		if (isSecure) {
			if (TestConfig.secureRepositories == null) {
				fatal("BasicTransaction: parseRetEndpoint(): secureRepositories not configured");
			}
			endpoint = (String) TestConfig.secureRepositories.get(
					repositoryUniqueId + ((async) ? ".as" : "" )
					);
		} else {
			if (TestConfig.repositories == null) {
				fatal("BasicTransaction: parseRetEndpoint(): repositories not configured");
			}
			endpoint = (String) TestConfig.repositories.get(
					repositoryUniqueId + ((async) ? ".as" : "" )
					);
		}
		
		
		s_ctx.add_name_value(instruction_output, "Endpoint", endpoint);
		if (endpoint == null) {
			fatal("Cannot get repository endpoint from configuration:" +
			"\nrepositoryUniqueId=" + repositoryUniqueId +
			"\nisSecure = " + isSecure +
			"\nasync = " + async
			);
		} 
	}

	protected void parseGatewayEndpoint(String home, boolean isSecure) throws XdsInternalException {
		String xHome = home;
		if(async)
			xHome = xHome + ".as";
		if (isSecure) {
			endpoint = (String) TestConfig.xSecRepositories.get(xHome);
			if (endpoint == null)
				fatal("BasicTransaction:parseGatewayEndpoint(): no endpoint for home " + home + 
						" async is " + async +
						", XCR config is " + TestConfig.xSecRepositories.toString());
		}
		else { 
			endpoint = (String) TestConfig.xRepositories.get(xHome);
			if (endpoint == null)
				fatal("BasicTransaction:parseGatewayEndpoint(): no endpoint for home " + home + 
						" async is " + async +
						", XCR config is " + TestConfig.xRepositories.toString());
		}
		s_ctx.add_name_value(instruction_output, "Endpoint", endpoint);
	}

	public void fail(String msg) {
		s_ctx.set_error(msg);
		failed();
	}

	protected void fatal(String msg) throws XdsInternalException {
		throw new XdsInternalException(msg);
	}


	protected void log_metadata(OMElement submission) throws XdsInternalException {
		this.s_ctx.add_name_value(	instruction_output, 
				"InputMetadata", Util.deep_copy(submission));
	}

	protected OMElement prepare_metadata() throws XdsInternalException, MetadataValidationException, XdsException {
		try {
			if (parse_metadata) {
				metadata = new Metadata(new File(metadata_filename), parse_metadata);
			} else {
				metadata = MetadataParser.noParse(new File(metadata_filename));
			}

		} 
		catch (MetadataException e) {
			e.printStackTrace();
			fatal("Error parsing metadata: filename is " + metadata_filename + ", Error is: " + e.getMessage());
		}
		catch (MetadataValidationException e) {
			this.s_ctx.metadata_validation_error(e.getMessage());
		}

		if (parse_metadata) {
			// verify input is correct top-level request
			if ( ! metadata.getRoot().getLocalName().equals("SubmitObjectsRequest"))
				fatal("Register Transaction (as coded in testplan step '" + this.s_ctx.get("step_id") + "') must reference a file containing an SubmitObjectsRequest");

			TestMgmt tm = new TestMgmt();
			if ( assign_patient_id ) {
				// get and insert PatientId
				String forced_patient_id = s_ctx.get("PatientId");
				HashMap<String, String> patient_map;
				patient_map = tm.assignPatientId(metadata, forced_patient_id);
				this.s_ctx.add_name_value(instruction_output, generate_xml("AssignedPatientId", patient_map));
			}

			// compile in results of previous steps
			if (use_id.size() > 0) {
				compileUseIdLinkage(metadata, use_id);
			}

			if ( assign_uids ) {
				// assign uniqueIDs
				OMElement unique_ids_output = om_factory().createOMElement("UniqueIds", null);
				HashMap uniqueid_map = tm.assignUniqueIds(metadata, no_assign_uid_to);
				//this.s_ctx.add_name_value(instruction_output, tm.hashAsXml(uniqueid_map, "AssignedUniqueIds", "UniqueId"));
				this.s_ctx.add_name_value(instruction_output, generate_xml("AssignedUids", uniqueid_map));
			}

			// assign sourceId
			HashMap sourceid_map = tm.assignSourceId(metadata);
			this.s_ctx.add_name_value(instruction_output, generate_xml("AssignedSourceId", sourceid_map));



			// assign uuids
			if (assign_uuids) {
				IdParser ip = new IdParser(metadata);
				ip.compileSymbolicNamesIntoUuids();
				nameUuidMap = ip.getSymbolicNameUuidMap();
				this.s_ctx.add_name_value(instruction_output, generate_xml("AssignedUuids", nameUuidMap));
			}


		} 
		OMElement submission;
		if (parse_metadata) {
			submission = (xds_version == BasicTransaction.xds_a) ? metadata.getV2SubmitObjectsRequest() : metadata.getV3SubmitObjectsRequest();
		}
		else {
			submission = Util.deep_copy(metadata.getRoot());
		}

		//log_input_metadata(submission);

		return submission;
	}

	protected void parse_assertion_instruction(OMElement assertion_part) throws XdsException {
		Iterator elements = assertion_part.getChildElements();
		while (elements.hasNext()) {
			OMElement part = (OMElement) elements.next();
			String part_name = part.getLocalName();
			if (part_name.equals("DataRef")) {
				data_refs.add(part);
			} 
			else if (part_name.equals("Assert")) {
				assertions.add(part);
			} 
		}
	}

	public void parse_instruction(OMElement part) throws XdsException {
		String part_name = part.getLocalName();
		if (part_name.equals("MetadataFile")) {
			metadata_filename = TestConfig.base_path + part.getText();
			this.s_ctx.add_name_value(this.instruction_output, "MetadataFile", metadata_filename);
		} 
		else if (part_name.equals("AssignUuids")) {
			assign_uuids = true;
		} 
		else if (part_name.equals("NoAssignUids")) {
			assign_uids = false;
			String id = part.getAttributeValue(MetadataSupport.id_qname);
			if (id != null && !id.equals("")) {
				no_assign_uid_to = id;
				assign_uids = true;
			}
		} 
		else if (part_name.equals("ParseMetadata")) {
			String value = part.getText();
			this.s_ctx.add_name_value(this.instruction_output, "ParseMetadata", value);
			if (value.equals("False"))
				parse_metadata = false;
		} 
		else if (part_name.equals("UseId")) {
			use_id.add(part);
			s_ctx.add_name_value(instruction_output, "UseId", part);
		}
		else if (part_name.equals("UseRepositoryUniqueId")) {
			use_repository_unique_id.add(part);
			s_ctx.add_name_value(instruction_output, "UseRepositoryUniqueId", part);
		}
		else if (part_name.equals("Assertions")) {
			parse_assertion_instruction(part);
		} 
		else if (part_name.equals("XDSb")) {
			xds_version = BasicTransaction.xds_b;
			this.s_ctx.add_simple_element(this.instruction_output, "Xdsb");
		} 
		else if (part_name.equals("XDSa")) {
			xds_version = BasicTransaction.xds_a;
			this.s_ctx.add_simple_element(this.instruction_output, "Xdsa");
		} 
		else if (part_name.equals("NoPatientId")) {
			assign_patient_id = false;
			this.s_ctx.add_simple_element(this.instruction_output, "NoPatientId");
		} 
		else if (part_name.equals("SOAP11")) {
			soap_1_2 = false;
			this.s_ctx.add_simple_element(this.instruction_output, "SOAP11");
		} 
		else if (part_name.equals("ASync")) {
			async = true;
			this.s_ctx.add_simple_element(this.instruction_output, "ASync");
		} 
		else {
			throw new XdsException("BasicTransaction: Don't understand instruction " + part_name);
		}

		if (TestConfig.verbose)
			System.out.println("<<<PRE-TRANSACTION>>>\n" + toString() + "<<<///PRE-TRANSACTION>>>\n");

	}

	public class DocDetails {
		public String uri;
		public String size;
		public String hash;
		public String mimeType;
	};

	protected DocDetails getDocDetailsFromLogfile(OMElement uri_ref)
	throws FactoryConfigurationError, XdsInternalException, XdsException,
	MetadataException, MetadataValidationException {
		DocDetails dd = new DocDetails();
		String log_file = uri_ref.getAttributeValue(new QName("log_file"));
		String step_id = uri_ref.getAttributeValue(new QName("step_id"));
		String transaction_type = uri_ref.getAttributeValue(new QName("trans_type"));

		Linkage l = new Linkage();
		OMElement log = Util.parse_xml(new File(TestConfig.log_dir + File.separator + log_file));
		OMElement res = null;
		try {
			res = l.find_instruction_output(log, step_id, transaction_type);
		}
		catch (Exception e) {
			fail ("find_instruction_output failed: " + RegistryUtility.exception_details(e));
			throw new XdsException(e.getMessage());
		}
		if (res == null) {
			fail ("Cannot find referenced instruction output");
			throw new XdsException("Cannot find referenced instruction output");
		}

		OMElement result = MetadataSupport.firstChildWithLocalName(res, "Result");
		if (result == null) {
			fail("Cannot find <Result/>");
			throw new XdsException("Cannot find <Result/>");
		}

		Metadata m = MetadataParser.parseNonSubmission(result.getFirstElement());
		if (m.getExtrinsicObjectIds().size() == 0) {
			fail("No ExtrinsicObjects found in log file " + log_file + " step " + step_id + 
					" with transaction type " + transaction_type);
			throw new XdsException("No ExtrinsicObjects found in log file " + log_file + " step " + step_id + 
					" with transaction type " + transaction_type);
		}
		OMElement eo = m.getExtrinsicObject(0);
		//dd.uri = m.getSlotValue(eo, "URI", 0);
		dd.uri = m.getURIAttribute(eo);
		dd.size = m.getSlotValue(eo, "size", 0);
		dd.hash = m.getSlotValue(eo, "hash", 0);
		dd.mimeType = m.getMimeType(eo);
		return dd;
	}

	protected void runAssertionEngine(OMElement step_output, ErrorReportingInterface eri, OMElement assertion_output) {
		AssertionEngine engine = new AssertionEngine();
		engine.setDataRefs(data_refs);
		engine.setAssertions(assertions);
		engine.setOutput(step_output);
		engine.run(eri, assertion_output);
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getEndpointService() throws XdsException {
		if (endpoint == null || endpoint.equals(""))
			throw new XdsException("Endpoint not initialized");
		int doubleSlash = endpoint.indexOf("//");
		if (doubleSlash == -1)
			throw new XdsException("Endpoint contains no double slash");
		int singleSlash = endpoint.indexOf("/", doubleSlash+2);
		if (singleSlash == -1) 
			throw new XdsException("Endpoint has no service");
		return endpoint.substring(singleSlash);
	}

	public String getEndpointMachine() throws XdsException {
		if (endpoint == null || endpoint.equals(""))
			throw new XdsException("Endpoint not initialized");
		int doubleSlash = endpoint.indexOf("//");
		if (doubleSlash == -1)
			throw new XdsException("Endpoint contains no double slash");
		int singleSlash = endpoint.indexOf("/", doubleSlash+2);
		if (singleSlash == -1) 
			throw new XdsException("Endpoint has no service");

		String machAndPort = endpoint.substring(doubleSlash+2, singleSlash);
		int colon = machAndPort.indexOf(":");
		if (colon == -1)
			return machAndPort;
		return machAndPort.substring(0, colon);
	}

	public String getEndpointPort() throws XdsException {
		if (endpoint == null || endpoint.equals(""))
			throw new XdsException("Endpoint not initialized");
		int doubleSlash = endpoint.indexOf("//");
		if (doubleSlash == -1)
			throw new XdsException("Endpoint contains no double slash");
		int singleSlash = endpoint.indexOf("/", doubleSlash+2);
		if (singleSlash == -1) 
			throw new XdsException("Endpoint has no service");

		String machAndPort = endpoint.substring(doubleSlash+2, singleSlash);
		int colon = machAndPort.indexOf(":");
		if (colon == -1)
			return "80";
		return machAndPort.substring(colon+1);
	}

	protected void validate_xds_version() throws XdsInternalException {
		if (xds_version != BasicTransaction.xds_b)
			throw new XdsInternalException("<XDSb/> must be specified in testplan.xml");
	}

	String getResponseAction() {
		return SoapActionFactory.getResponseAction(getRequestAction());
	}

	protected void soapCall() {
		soap = new Soap();
		soap.setAsync(async);
		try {
			soap.soapCall(metadata_element, 
					endpoint, 
					useMtom, //mtom
					useAddressing,  // WS-Addressing
					soap_1_2,  // SOAP 1.2
					getRequestAction(),
					getResponseAction()
			);
		}
		catch (XdsException e) {
			s_ctx.set_error(e.getMessage());
			failed();
		}

		try {
			s_ctx.add_name_value(instruction_output, "OutAction", getRequestAction());
			s_ctx.add_name_value(instruction_output, "ExpectedInAction", getResponseAction());
			s_ctx.add_name_value(instruction_output, "OutHeader", soap.getOutHeader());
			s_ctx.add_name_value(instruction_output, "InHeader", soap.getInHeader());
			s_ctx.add_name_value(instruction_output, "Result", getSoapResult());
		} catch (Exception e) {
			System.out.println("oops");
			e.printStackTrace();
		}

		if (s_ctx.expectedstatus) {
			RegistryResponseParser registry_response = new RegistryResponseParser(getSoapResult());
			String errs = registry_response.get_regrep_error_msg();
			if (errs != null && !errs.equals("")) {
				s_ctx.set_error(errs);
				failed();
			}

		}
	}

	protected OMElement getSoapResult() {
		return soap.getResult();
	}

	protected void setMetadata(OMElement metadata_ele) {
		this.metadata_element = metadata_ele;
	}

}
