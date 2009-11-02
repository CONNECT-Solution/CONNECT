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

package com.vangent.hieos.xtest.transactions.xca;

import com.vangent.hieos.xtest.framework.StepContext;
import com.vangent.hieos.xtest.transactions.xds.StoredQueryTransaction;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataParser;
import com.vangent.hieos.xutil.metadata.structure.HomeAttribute;

import java.util.Iterator;

import org.apache.axiom.om.OMElement;

public class XCQTransaction extends StoredQueryTransaction {
	String expectedHomeCommunityId = null;

	public XCQTransaction(StepContext s_ctx, OMElement instruction, OMElement instruction_output) {
		super(s_ctx, instruction, instruction_output);
	}

	public void run() throws XdsException {
		expectedHomeCommunityId = s_ctx.get("HomeCommunityId");
		parseParameters(s_ctx, instruction, instruction_output);

		if (async)
			parseEndpoint("xcq.as");
		else
			parseEndpoint("xcq");		

		parseMetadata();

		// manual linkage
		if (metadata == null) throw new XdsException("XGQTransaction.run(): metadata is null");

		setIsXCA(true);
		OMElement result_ele = runSQ();


		if (result_ele == null) 
			throw new XdsInternalException("Result to XCA Query is null");
		Metadata result_metadata = MetadataParser.parseNonSubmission(result_ele);

		if (expectedHomeCommunityId != null) {
			if ( !expectedHomeCommunityId.startsWith("urn:oid:")) {
				s_ctx.set_error("Expected homeCommunityId value is [" + expectedHomeCommunityId + "]. It is required to have a [urn:oid:] prefix.");
				failed();
			}

			HomeAttribute homeAtt = new HomeAttribute(expectedHomeCommunityId);
			String errors = homeAtt.validate(result_ele);
			if ( ! errors.equals("")) {
				s_ctx.set_error(errors);
				failed();
			}
		}
	}

	public void parseParameters(StepContext s_ctx, OMElement instruction,
			OMElement instruction_output) throws XdsException {
		Iterator<OMElement> elements = instruction.getChildElements();
		while (elements.hasNext()) {
			OMElement part = elements.next();
			String part_name = part.getLocalName();
			if (part_name.equals("homeCommunityId")) {
				expectedHomeCommunityId =  part.getText();
				s_ctx.add_name_value(instruction_output, "homeCommunityId", expectedHomeCommunityId);
			} 
			else
				super.parseParameter(s_ctx, instruction_output, part);
		}
	}


}
