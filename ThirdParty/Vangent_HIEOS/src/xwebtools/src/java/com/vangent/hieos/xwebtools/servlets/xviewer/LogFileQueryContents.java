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
import com.vangent.hieos.xutil.metadata.structure.MetadataParser;
import com.vangent.hieos.xutil.xml.XmlFormatter;

import org.apache.axiom.om.OMElement;

public class LogFileQueryContents extends QueryContents {
	String step_status;
	OMElement input;
	OMElement result;
	OMElement teststep;
	String result_status;
	String expected_status;
	boolean metadata_found_in_input;
	boolean metadata_found = false;

	public void displayStructure(QueryControl q_cntl, int index, HttpUtils h,
			XView xv, String cntl)
	throws MetadataValidationException, MetadataException {
		QueryContents ssac = new SSandContentsQueryContents();
		ssac.dup(this);

		if (result != null ) {
			if ( result.getLocalName().equals("AdhocQueryResponse"))
				this.setMetadata(MetadataParser.parseNonSubmission(result));
		}
		if (input != null ) {
			if ( input.getLocalName().equals("SubmitObjectsRequest"))
				this.setMetadata(MetadataParser.parseNonSubmission(input));
		}

		ssac.displayStructure(q_cntl, index, h, xv, String.valueOf(index));



		StringBuffer buf = new StringBuffer();
		//buf.append("Step " + this.reference_id);
		if (teststep != null)
			buf.append(" " + xv.h().link("stepxml", "/xwebtools/xviewer/stepxml?cntl=" + index));
		if (input != null)
			buf.append(" " + xv.h().link("inputxml", "/xwebtools/xviewer/inputxml?cntl=" + index));
		if (result != null)
			buf.append(" " + xv.h().link("resultxml", "/xwebtools/xviewer/resultxml?cntl=" + index));
		xv.h().indent1(buf.toString());
		xv.h().indent1("StepStatus " + step_status);
		xv.h().indent1("ExpectedStatus " + expected_status);

	}

	public void displayInput(XView xv) {
		System.out.println("displayInput");
		if (input != null)
			xv.out(XmlFormatter.htmlize(input.toString(), false));
//		else
//		xv.out("null");
	}

	public void displayTestStep(XView xv) {
		System.out.println("displayTestStep");
		if (teststep != null)
			xv.out(XmlFormatter.htmlize(teststep.toString(), false));
//		else
//		xv.out("null");
	}

	public void displayResult(XView xv) {
		System.out.println("displayResult");
		if (result != null)
			xv.out(XmlFormatter.htmlize(result.toString(), false));
//		else
//		xv.out("null");
	}

	// not sure this works
	void findMetadata() {
		try {
			Metadata m1=null, m2=null;
			if (this.input != null) {
				m1 = MetadataParser.parseNonSubmission(this.input);
				if (m1.getAllObjects().size() > 0) {
					this.setMetadata(m1);
					this.metadata_found_in_input = true;
					this.metadata_found = true;
					return;
				}
			}
			if (this.result != null) {
				m2 = MetadataParser.parseNonSubmission(this.result);
				if (m2.getAllObjects().size() > 0) {
					this.setMetadata(m2);
					this.metadata_found_in_input = false;
					this.metadata_found = true;
					return;
				}
			}
		}
		catch (Exception e) {
		}
	}

	public void validate_inputs() {
		StringBuffer buf = new StringBuffer();

		buf.append("Validate Inputs\n");
		
		if (this.input == null)
			buf.append("Input is null\n");

		if (this.result == null)
			buf.append("Result is null\n");

		buf.append("Metadata found is " + ((this.metadata_found) ? "true" : "false") + "\n");
		if (this.metadata_found)
			buf.append("...in " + ((this.metadata_found_in_input) ? "input" : "result") + "\n");
		
		if (this.getMetadata() != null)
			buf.append("Metadata found\n");
		else
			buf.append("Metadata not found\n");


		this.addError(buf.toString());
		
		System.out.println(buf.toString());
		System.out.flush();
	}

}
