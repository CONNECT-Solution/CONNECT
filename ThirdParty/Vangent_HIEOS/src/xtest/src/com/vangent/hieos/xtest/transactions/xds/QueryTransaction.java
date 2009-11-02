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
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataParser;
import com.vangent.hieos.xtest.validator.TestValidator;

import org.apache.axiom.om.OMElement;

abstract public class QueryTransaction extends BasicTransaction {
	
	public QueryTransaction(StepContext s_ctx, OMElement instruction, OMElement instruction_output) {
		super(s_ctx, instruction, instruction_output);
	}
	
	protected String validate_expected_contents(OMElement result, int metadata_type, OMElement test_assertions) 
	throws XdsInternalException, MetadataException, MetadataValidationException {

		Metadata m = MetadataParser.parseNonSubmission(result);

		TestValidator v = new TestValidator(test_assertions);
		v.run_test_assertions(m);

		return v.getErrors();
	}


}
