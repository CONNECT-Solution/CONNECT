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
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.iosupport.Io;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.registry.RegistryUtility;
import com.vangent.hieos.xutil.soap.Soap;
import com.vangent.hieos.xutil.xml.Util;
import com.vangent.hieos.xtest.main.XTestDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

public class PlanContext extends BasicContext {
	OMElement results_document = null;
	String default_registry_endpoint = null;
	String default_patient_id = null;
	String registry_endpoint = null;
	String patient_id = null;
	boolean status = true;
	String test_num = "0";
	short xds_version = BasicTransaction.xds_none;
	protected boolean phone_home = false;
	ArrayList<String> phone_home_log_files = null;
	private final static Logger logger = Logger.getLogger(PlanContext.class);

	void setRegistryEndpoint(String end_point) {
		registry_endpoint = end_point;
		logger.info("Set Registry Endpoint = " + end_point);
		set("RegistryEndpoint", end_point);
	}

	void setPatientId(String patient_id) {
		this.patient_id = patient_id;
		set("PatientId", patient_id);
	}

	protected void plan_to_phone_home(OMElement phonehome) {
		this.phone_home = true;
		this.phone_home_log_files = new ArrayList<String>();
		for (OMElement log_file_rel_path_ele : MetadataSupport.childrenWithLocalName(phonehome, "Log")) {
			String log_file_rel_path = log_file_rel_path_ele.getText();
			String log_path = TestConfig.base_path + log_file_rel_path;
			this.phone_home_log_files.add(log_path);
		}
	}

	public OMElement getResultsDocument() {
		return results_document;
	}
	
	public PlanContext() {
		super(null);
	}

	public PlanContext(short xds_version) {
		super(null);
		this.xds_version = xds_version;
	}
	
	void set_status_in_output() {
		results_document.addAttribute("status", (status) ? "Pass" : "Fail", null);	
	}

	boolean getStatus() {
		return status;
	}

	void setDefaultConfig(OMElement config) {
		Iterator<OMElement> elements = config.getChildElements();
		while (elements.hasNext()) {
			OMElement part = elements.next();
			String part_name = part.getLocalName();

			String value = part.getText();
			
			set(part_name, value);
		}
	}

	public boolean run(String testplan_filename, StringSub str_sub)  throws XdsException {
		try {
			results_document = build_results_document();	

			add_name_value(results_document, "version", XTestDriver.version);
			add_name_value(results_document, "args", XTestDriver.args);

			String testplan_str = Io.stringFromFile(new File(testplan_filename));
			str_sub.setString(testplan_str);
			testplan_str = str_sub.toString();

//			OMElement testplan = Util.parse_xml(new File(testplan_filename));
			OMElement testplan = Util.parse_xml(testplan_str);

			Iterator elements = testplan.getChildElements();
			while (elements.hasNext()) {
				OMElement part = (OMElement) elements.next();
				String part_name = part.getLocalName();

				// as we step through the plan, all plan elements are copied to results file with
				// add_name_value()
				if (part_name.equals("RegistryEndpoint")) 
				{
					default_registry_endpoint = part.getText();
					add_name_value(results_document, part); 
					setRegistryEndpoint(default_registry_endpoint);
				} 
				else if (part_name.equals("PatientId")) 
				{
					default_patient_id = part.getText();
					add_name_value(results_document, part);
					setPatientId(default_patient_id);
				} 
				else if (part_name.equals("Test")) 
				{
					add_name_value(results_document, part);
					test_num = part.getText();
				} 
				else if (part_name.equals("PhoneHome")) {
					this.plan_to_phone_home(part);
				}
				else if (part_name.equals("Rule")) 
				{
				} 
				else if (part_name.equals("TestStep")) 
				{
					StepContext step_context = new StepContext(this);
					step_context.run(part, this);

					if ( !step_context.getStatus() )
						status = false;
				} 
				else 
				{
					throw new XdsInternalException("Don't understand test step named " + part_name);
				}
			}

			if (test_num.equals("0") )
				throw new XdsInternalException("<Test/> missing from testplan");

			set_status_in_output();


		} 
		catch (MetadataValidationException e) {
			add_name_value(results_document, "MetadataValidationError", e.getMessage());
			System.out.println(e.getMessage());
			status = false;
			set_status_in_output();
		}
		catch (XdsInternalException e) {  
			add_name_value(results_document, "FatalError", e.getMessage());
			System.out.println("Fatal Error: " + ExceptionUtil.exception_details(e));
			status = false;
			set_status_in_output();
		}
		catch (Exception e) {  
			add_name_value(results_document, "FatalError", e.getMessage());
			System.out.println("A Fatal Error: " + ExceptionUtil.exception_details(e));
			status = false;
			set_status_in_output();
		}


		try {
			String logDir = TestConfig.log_dir;
			if (logDir == null)
				logDir = TestConfig.base_path;
			FileOutputStream os = new FileOutputStream(new File(logDir + "log.xml"));
			//System.out.println(results_document.toString());
			os.write(results_document.toString().getBytes());
			os.flush();
			os.close();
			if (this.phone_home) {
				System.out.println("Phoning home");
				phone_home_now(results_document);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Cannot create file log.xml");
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Cannot write to file log.xml");
			System.exit(-1);
		}

		return getStatus();

	}

	private void phone_home_now(OMElement log_doc) throws XdsInternalException {
		OMElement val_req_ele = MetadataSupport.om_factory.createOMElement("ValidateTestRequest", null);
		String[] test_num_parts = test_num.split("/");
		String test = test_num;
		if (test_num_parts.length > 1)
			test = test_num_parts[0];

		OMElement test_ele = MetadataSupport.om_factory.createOMElement("Test", null);
		test_ele.setText(test);
		val_req_ele.addChild(test_ele);

		OMElement log_ele = MetadataSupport.om_factory.createOMElement("Log", null);
		val_req_ele.addChild(log_ele);
		log_ele.addChild(log_doc);

		if (this.phone_home_log_files != null)
			for (String log_path : this.phone_home_log_files) {
				OMElement secondary_log_ele = MetadataSupport.om_factory.createOMElement("Log", null);
				val_req_ele.addChild(secondary_log_ele);
				OMElement secondary_log_doc = null;
				try {
					secondary_log_doc = Util.parse_xml(new File(log_path));
				}
				catch (XdsInternalException e) {
					System.out.println(ExceptionUtil.exception_details(e, "Cannot open secondary log file " + log_path));
				}
				secondary_log_ele.addChild(secondary_log_doc);
			}

		Soap soap = new Soap();
		try {
			OMElement response = soap.soapCall(val_req_ele, 
					"http://localhost:9080/axis2/services/testvalidator", 
					false,   // mtom 
					false, // addressing 
					false, // soap12, 
					null,    // action, 
					null);   //expected_return_action
			System.out.println(response.toString());
		}
		catch (Exception e) {
			System.out.println(RegistryUtility.exception_details(e));
		}

	}
}
