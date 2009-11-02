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
import com.vangent.hieos.xutil.exception.ExceptionUtil;
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.xml.Util;

import java.io.File;
import java.util.Iterator;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

public class EchoV3Transaction extends BasicTransaction {
	
	public EchoV3Transaction(StepContext s_ctx, OMElement instruction, OMElement instruction_output) {
		super(s_ctx, instruction, instruction_output);
	}
	
	public void run() 
	throws XdsException {
		String metadata_filename = null;
		Iterator elements = instruction.getChildElements();
		while (elements.hasNext()) {
			OMElement part = (OMElement) elements.next();
			String part_name = part.getLocalName();
			if (part_name.equals("MetadataFile")) {
				metadata_filename = part.getText();
				s_ctx.add_name_value(instruction_output, "MetadataFile", metadata_filename);
			} else if (part_name.equals("ParseMetadata")) {
				String value = part.getText();
				s_ctx.add_name_value(instruction_output, "ParseMetadata", value);
				if (value.equals("False"))
					parse_metadata = false;
			}
		}

		if (s_ctx.getRegistryEndpoint() == null)
			throw new XdsInternalException("No registry endpoint specified");
		if (metadata_filename == null)
			throw new XdsInternalException("No MetadataFile element found for EchoV3Transaction instruction within step " + s_ctx.get("step_id"));


		// input file is read twice. Adding to log and then sending through Axis2 results in
		// no output in the log. Axiom does not seem to have a recursive cloner available.
		// so this is easier for now.

		Metadata metadata = null;
		try {
			metadata = new Metadata(new File(metadata_filename), parse_metadata);
		} 
		catch (MetadataException e) {
			throw new XdsInternalException(ExceptionUtil.exception_details(e, "Error parsing metadata: filename is " + metadata_filename + ", Error is: " + e.getMessage()));
		}
		catch (MetadataValidationException e) {
			throw new XdsInternalException(ExceptionUtil.exception_details(e));
		}

		if (parse_metadata) {
			// verify input is correct top-level request
			if ( ! metadata.getRoot().getLocalName().equals("EchoV3Metadata"))
				throw new XdsInternalException("Echo Transaction (as coded in testplan step '" + s_ctx.get("step_id") + "') must reference a file containing an EchoV3Metadata");

		} 
		s_ctx.add_name_value(	instruction_output, 
				"InputMetadata", 
				Util.deep_copy(metadata.getRoot()));


		Options options = new Options();
		options.setTo(new EndpointReference(s_ctx.getRegistryEndpoint())); // this sets the location of MyService service
		try {
			ServiceClient serviceClient = new ServiceClient();
			serviceClient.setOptions(options);
			OMElement result = serviceClient.sendReceive(metadata.getRoot());

			s_ctx.add_name_value(instruction_output, "Result", result);

			validate_registry_response(result, MetadataTypes.METADATA_TYPE_SQ);

		} catch (AxisFault e) {
			throw new XdsInternalException(ExceptionUtil.exception_details(e));
		}

	}

	@Override
	protected String getRequestAction() {
		// TODO Auto-generated method stub
		return null;
	}


}
