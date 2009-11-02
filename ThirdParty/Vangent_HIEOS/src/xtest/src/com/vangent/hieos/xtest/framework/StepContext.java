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

import com.vangent.hieos.xtest.transactions.xds.EchoV2Transaction;
import com.vangent.hieos.xtest.transactions.xds.EchoV3Transaction;
import com.vangent.hieos.xtest.transactions.xds.RetrieveTransaction;
import com.vangent.hieos.xtest.transactions.xds.SqlQueryTransaction;
import com.vangent.hieos.xtest.transactions.xds.StoredQueryTransaction;
import com.vangent.hieos.xtest.transactions.xds.RegisterTransaction;
import com.vangent.hieos.xtest.transactions.xds.ProvideAndRegisterTransaction;
import com.vangent.hieos.xtest.transactions.xca.XCAIGStoredQueryTransaction;
import com.vangent.hieos.xtest.transactions.xca.XCAIGRetrieveTransaction;
import com.vangent.hieos.xtest.transactions.xca.XCQTransaction;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xtest.main.XTestDriver;

import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;

public class StepContext extends BasicContext implements ErrorReportingInterface {
	OMElement output = null;
	OMElement test_step_output = null;
    boolean expectedstatus = true;
	public String expectedErrorMessage = "";
	boolean status = true;
	//short xds_version = BasicTransaction.xds_none;

	
//	public String xdsVersionName() {
//		if (xds_version == BasicTransaction.xds_none)
//			return "None";
//		if (xds_version == BasicTransaction.xds_a)
//			return "XDS.a";
//		if (xds_version == BasicTransaction.xds_b)
//			return "XDS.b";
//		return "Unknown";
//	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf
//		.append("XDS Version = ").append(xdsVersionName()).append("\n")
		.append("Expected Version = ").append(expectedstatus).append("\n")
		.append("Expected Error Message = ").append(expectedErrorMessage).append("\n");
		
		return buf.toString();
	}
	
	public boolean getExpectedStatus() {
		return expectedstatus;
	}
	public void setExpectedStatus(boolean expectedOutput) {
		this.expectedstatus = expectedOutput;
	}
	public StepContext(PlanContext plan) {
		super(plan);
	}
	public void setId(String id) {
		set("step_id", id);
	}
	public String getId() {
		return get("step_id");
	}

	void setStatus(boolean status) {
		this.status = status;
	}

	void setStatusInOutput(boolean status) {
		this.status = status;
		setStatusInOutput();
	}

	void setStatusInOutput() {
		test_step_output.addAttribute("status", (status) ? "Pass" : "Fail", null);
	}

	void resetStatus() {
		status = true;
	}

	public boolean getStatus()  {
		return status;
	}

	public void set_error(String msg) {
		error(test_step_output, msg);
		setStatus(false);
	}

	public void fail(String message) {
		set_error(message);
	}

	public void setInContext(String title, Object value) {
		set(title, value);
	}
	
	void run(OMElement step, PlanContext plan_context) throws XdsException {
		String step_id = null;
		step_id = null;
		String expected_status = null;
		String expected_error_message = null;

		OMAttribute id = step.getAttribute(new QName("id"));
		if (id == null)
			throw new XdsInternalException("Found TestStep without an id attribute");
		step_id = id.getAttributeValue();
		
		TestConfig.currentStep = step_id;
		
		if ( !XTestDriver.only_steps.isEmpty() && !XTestDriver.only_steps.contains(step_id)) {
			//System.out.println("Step " + step_id + " found but not requested - only steps " + Xdstest2.only_steps);
			return;
		}
		setId(step_id);
		System.out.println("step " + step_id);
		if (XTestDriver.l_option)
			return;


		test_step_output = add_simple_element_with_id(
				plan_context.getResultsDocument(), 
				"TestStep", 
				step_id);

		Iterator elements = step.getChildElements();
		while (elements.hasNext()) {
			OMElement instruction = (OMElement) elements.next();
			String instruction_name = instruction.getLocalName();
			InstructionContext ins_context = new InstructionContext(this);
			//System.out.println("******* " + instruction_name + " ***"); 

			if (instruction_name.equals("ExpectedStatus")) 
			{
				expected_status = instruction.getText();
				add_name_value(test_step_output, instruction_name, expected_status);
				setExpectedStatus("Success".equals(expected_status));
			} 
			else if (instruction_name.equals("Rule")) 
			{
			} 
			else if (instruction_name.equals("RegistryEndpoint")) 
			{
				plan_context.default_registry_endpoint = instruction.getText();
				add_name_value(test_step_output, instruction); 
				plan_context.setRegistryEndpoint(plan_context.default_registry_endpoint);
			} 
			else if (instruction_name.equals("NewPatientId"))  
			{
				PatientIdAllocator pia = new PatientIdAllocator();
				String pid = pia.allocate_new(); // allocate a new patient id
				add_name_value(test_step_output, "NewPatientID", pid); 
			} 
			else if (instruction_name.equals("ExpectedErrorMessage")) 
			{
				expected_error_message = instruction.getText();
				add_name_value(test_step_output, instruction_name, expected_error_message);
				setExpectedErrorMessage(expected_error_message);
			} 
			else {
				resetStatus();
				OMElement instruction_output = null;
				BasicTransaction transaction = null;
				
				instruction_output = add_simple_element(test_step_output, instruction_name);
				instruction_output.addAttribute("step", step_id, null);
				
				if (instruction_name.equals("SqlQueryTransaction")) 
				{
					transaction = new SqlQueryTransaction(this, instruction, instruction_output);
				} 
				else if (instruction_name.equals("StoredQueryTransaction")) 
				{
					transaction = new StoredQueryTransaction(this, instruction, instruction_output);
				} 
				/* Removed (BHT): else if (instruction_name.equals("MswTransaction"))
				{
					transaction = new MswTransaction(this, instruction, instruction_output);
				} */
                else if (instruction_name.equals("XCAIGStoredQueryTransaction"))
                {
                  transaction = new XCAIGStoredQueryTransaction(this, instruction, instruction_output);
                }
                else if (instruction_name.equals("XCAIGRetrieveTransaction"))
                {
                  transaction = new XCAIGRetrieveTransaction(this, instruction, instruction_output);
                }
				else if (instruction_name.equals("XCQTransaction")) 
				{
					transaction = new XCQTransaction(this, instruction, instruction_output);
				} 
				else if (instruction_name.equals("SimpleTransaction")) 
				{
					transaction = new SimpleTransaction(this, instruction, instruction_output);
				} 
				else if (instruction_name.equals("RetrieveTransaction")) 
				{
					transaction = new RetrieveTransaction(this, instruction, instruction_output);
				} 
				else if (instruction_name.equals("XCRTransaction"))
				{
					transaction = new RetrieveTransaction(this, instruction, instruction_output);
					((RetrieveTransaction)transaction).setIsXca(true);
				} 
				else if (instruction_name.equals("RegisterTransaction")) 
				{
					transaction = new RegisterTransaction(this, instruction, instruction_output);
				} 
				else if (instruction_name.equals("ProvideAndRegisterTransaction")) 
				{
					transaction = new ProvideAndRegisterTransaction(this, instruction, instruction_output);
				} 
				else if (instruction_name.equals("EchoV2Transaction")) 
				{
					transaction = new EchoV2Transaction(this, instruction, instruction_output);
				} 
				else if (instruction_name.equals("EchoV3Transaction")) 
				{
					transaction = new EchoV3Transaction(this, instruction, instruction_output);
				} 
				else 
				{
					dumpContextIntoOutput(test_step_output);
					throw new XdsInternalException(ins_context.error("StepContext: Don't understand instruction named " + instruction_name));
				}

				setTransaction(transaction);
				transaction.run();				
				
				if (transaction != null) {
					OMElement assertion_output = add_simple_element(
							test_step_output, 
							"Assertions");
					transaction.runAssertionEngine(instruction_output, this, assertion_output);
				}

				dumpContextIntoOutput(test_step_output);

				//System.out.println("xdstest2 step status : " + ((this.getStatus()) ? "Pass" : "Fail"));
				System.out.flush();
				setStatusInOutput();
			}
		}

	}
	public String getExpectedErrorMessage() {
		return get("ExpectedErrorMessage");
	}
	public void setExpectedErrorMessage(String expectedErrorMessage) {
		this.expectedErrorMessage = expectedErrorMessage;
		this.set("ExpectedErrorMessage", expectedErrorMessage);
	}
	public BasicTransaction getTransaction() {
		return (BasicTransaction) getObj("transaction");
	}

	public void setTransaction(BasicTransaction transaction) {
		parent_context.set("transaction", transaction);
	}

	public String getRegistryEndpoint() {
		return getRecursive("RegistryEndpoint");
	}

	public String getRegistryEndpoint(String transaction, short xds_version) {
		// transaction is "pr", "r", "q", "sq", "ret"
		return XTestDriver.getEndpoint(transaction, xds_version);
	}


}
