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

package com.vangent.hieos.xtest.transactions.xds;

import com.vangent.hieos.xtest.framework.BasicTransaction;
import com.vangent.hieos.xtest.framework.StepContext;
import com.vangent.hieos.xutil.metadata.structure.MetadataTypes;
import com.vangent.hieos.xutil.hl7.date.Hl7Date;
import com.vangent.hieos.xutil.exception.ExceptionUtil;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.structure.MetadataParser;
import com.vangent.hieos.xtest.framework.Linkage;
import com.vangent.hieos.xtest.framework.TestConfig;
import com.vangent.hieos.xutil.xml.Util;

import java.io.File;
import java.util.Iterator;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

public class StoredQueryTransaction extends QueryTransaction {
	String metadata_filename = null;
	//OMElement metadata = null;
	OMElement expected_contents = null;
	//Metadata m = null;  
	OMElement metadata_ele = null;
	boolean is_xca = false;

	public StoredQueryTransaction(StepContext s_ctx, OMElement instruction, OMElement instruction_output) {
		super(s_ctx, instruction, instruction_output);
	}

	public void run() throws XdsException {
		parseParameters(s_ctx, instruction, instruction_output);

		parseMetadata();

		runSQ();
	}

	public void setIsXCA(boolean isXca) { is_xca = isXca; }

	protected OMElement runSQ() throws XdsInternalException, FactoryConfigurationError,
	XdsException {
		OMElement result = null;

        //System.out.println("**** runSQ class Name: " + this.getClass().getName());
		if (this.getClass().getName().endsWith(".StoredQueryTransaction")) {
			// if invoked from sub-class, don't re-parse and set endpoint
			if (async)
				parseEndpoint("sq.as");
			else
				parseEndpoint("sq");		
		}

		if (metadata_filename == null && metadata == null)
			throw new XdsInternalException("No MetadataFile element or Metadata element found for QueryTransaction instruction within step " + s_ctx.get("step_id"));


		// compile in results of previous steps (linkage)
		if (use_id.size() > 0)
			compileUseIdLinkage(metadata, use_id);
		if (use_object_ref.size() > 0)
			compileUseObjectRefLinkage(metadata, use_object_ref);
		if (use_xpath.size() > 0) 
			compileUseXPathLinkage(metadata, use_xpath);

		Linkage lnk = new Linkage(instruction_output, metadata);
		lnk.add("$now$", new Hl7Date().now());
		lnk.add("$lastyear$", new Hl7Date().lastyear());
		lnk.compileLinkage();


		s_ctx.add_name_value(instruction_output, "InputMetadata", Util.deep_copy(metadata_ele));

		OMNamespace ns = metadata_ele.getNamespace();
		String ns_uri = ns.getNamespaceURI();
		int metadata_type = 0;

		if (ns_uri.equals("urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0")) {
			metadata_type = MetadataTypes.METADATA_TYPE_SQ;
		} else {
			throw new XdsInternalException("Don't understand version of metadata (namespace on root element): " + ns_uri);
		}


		// verify input is correct top-level request
		if (parse_metadata) {
			if (! metadata_ele.getLocalName().equals("AdhocQueryRequest")) 
				throw new XdsInternalException("Stored Query Transaction (as coded in testplan step '" + s_ctx.get("step_id") + 
				"') must reference a file containing an AdhocQueryRequest");
		}
		useMtom = false;
		useAddressing = true;

		try {
			this.setMetadata(metadata_ele);
			soapCall();
			result = getSoapResult();

			validate_registry_response_no_set_status(result, metadata_type);

			if (expected_contents != null ) {
				String errors = validate_expected_contents(result, metadata_type, expected_contents);

				if (errors.length() > 0) {
					fail(errors);
				}
			}

			add_step_status_to_output();


		}
		catch (Exception e) {
			fail(ExceptionUtil.exception_details(e));
		}

		return result;
	}

	protected String getRequestAction() {
		if (async) {
			if (is_xca) {
				return "urn:ihe:iti:2007:CrossGatewayQueryAsync"; 
			} else {
				return "urn:ihe:iti:2007:RegistryStoredQueryAsync";
			}
		} else {
			if (is_xca) {
				return "urn:ihe:iti:2007:CrossGatewayQuery";
			} else {
				return "urn:ihe:iti:2007:RegistryStoredQuery";
			}
		}
	}

	protected void parseMetadata() throws FactoryConfigurationError,
	XdsInternalException {
		// input file is read twice. Adding to log and then sending through Axis2 results in
		// no output in the log. Axiom does not seem to have a recursive cloner available.
		// so this is easier for now.
		if (metadata != null)
			return;
		if (metadata_filename != null && !metadata_filename.equals(""))
			metadata_ele = Util.parse_xml(new File(metadata_filename));

		metadata = MetadataParser.noParse(metadata_ele);
	}

	public void parseParameters(StepContext s_ctx, OMElement instruction,
			OMElement instruction_output) throws XdsException {
		Iterator elements = instruction.getChildElements();
		while (elements.hasNext()) {
			OMElement part = (OMElement) elements.next();
			parseParameter(s_ctx, instruction_output, part);
		}
	}

	public void parseParameter(StepContext s_ctx,
			OMElement instruction_output, OMElement part) throws XdsException {
		String part_name = part.getLocalName();
		if (part_name.equals("MetadataFile")) {
			metadata_filename = TestConfig.base_path + part.getText();
			s_ctx.add_name_value(instruction_output, "MetadataFile", metadata_filename);
		} 
		else if (part_name.equals("Metadata")) { 
			metadata_filename = "";
			metadata_ele = part.getFirstElement();
		} 
		else if (part_name.equals("ExpectedContents")) {
			expected_contents = part;
			s_ctx.add_name_value(instruction_output, "ExpectedContents", part);
		} 
		else if (part_name.equals("UseXPath")) {
			use_xpath.add(part);
			s_ctx.add_name_value(instruction_output, "UseXRef", part);
		}
		else if (part_name.equals("UseObjectRef")) {
			use_object_ref.add(part);
			s_ctx.add_name_value(instruction_output, "UseObjectRef", part);
		} 
		else if (part_name.equals("SOAP11")) {
			soap_1_2 = false;
			s_ctx.add_name_value(instruction_output, "SOAP11", part);
		}
		else if (part_name.equals("SOAP12")) {
			soap_1_2 = true;
			s_ctx.add_name_value(instruction_output, "SOAP12", part);
		}
		else {
			BasicTransaction rt = this;
			rt.parse_instruction(part);
		}
	}

}
