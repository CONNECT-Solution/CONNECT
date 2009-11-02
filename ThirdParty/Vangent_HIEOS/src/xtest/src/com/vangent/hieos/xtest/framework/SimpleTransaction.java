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

import com.vangent.hieos.xutil.metadata.structure.MetadataTypes;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.response.RegistryResponseParser;
import com.vangent.hieos.xutil.xml.Util;
import com.vangent.hieos.xtest.main.XTestDriver;

import java.io.File;
import java.util.Iterator;
import org.apache.axiom.om.OMElement;

public class SimpleTransaction extends BasicTransaction {
	OMElement metadata_ele = null;
	String action = null;
	
	public SimpleTransaction(StepContext s_ctx, OMElement instruction, OMElement instruction_output) {
		super(s_ctx, instruction, instruction_output);
	}
	
	
	public void run()  throws XdsException {

		Iterator elements = instruction.getChildElements();
		while (elements.hasNext()) {
			OMElement part = (OMElement) elements.next();
			String part_name = part.getLocalName();
			if (part_name.equals("MetadataFile")) {
				metadata_filename = TestConfig.base_path + part.getText();
				metadata_ele = Util.parse_xml(new File(metadata_filename));
				s_ctx.add_name_value(instruction_output, "MetadataFile", metadata_filename);
			} else if (part_name.equals("Metadata")) { 
				metadata_filename = "";
				metadata_ele = part.getFirstElement();
			}
			else if (part_name.equals("Action")) {
				action = part.getText();
			}
			else if (part_name.equals("Addressing")) {
				useAddressing = true;
			}
			else if (part_name.equals("Mtom")) {
				useMtom = true;
			}
			else
				this.parse_instruction(part);
		}

		parseEndpoint("r");

		if (metadata_ele == null && metadata != null)
			this.prepare_metadata();

		if (metadata_ele == null && metadata == null)
			throw new XdsException("SimpleTransaction: both metadata_ele and metadata are null");

		if (metadata_ele == null) 
			metadata_ele = metadata.getRoot();

		xds_version = this.xds_b;

		if (metadata_filename == null)
			throw new XdsInternalException("No MetadataFile element found for RegisterTransaction instruction within step " + this.s_ctx.get("step_id"));

		OMElement request = metadata_ele;

		log_metadata(request);

		if (XTestDriver.prepair_only)
			return;
		
		soap_1_2 = false;

		try {
			setMetadata(request);
			soapCall();
			OMElement result = getSoapResult();
			validate_registry_response(
					result, 
					(xds_version == xds_a) ? MetadataTypes.METADATA_TYPE_R : MetadataTypes.METADATA_TYPE_SQ);


			if (result != null) {
				this.s_ctx.add_name_value(instruction_output, "Result", result);
			} else {
				this.s_ctx.add_name_value(instruction_output, "Result", "None");
				s_ctx.set_error("Result was null");
			}

			s_ctx.add_name_value(instruction_output, "Result", result);

			RegistryResponseParser registry_response = new RegistryResponseParser(result);

			if ( registry_response.is_error())
				fail(registry_response.get_regrep_error_msg());

		} 
		catch (Exception e) {
			fail(e.getMessage());
		}


	}


	protected String getRequestAction() {
		return action;
	}

}
