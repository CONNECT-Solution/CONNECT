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

package com.vangent.hieos.xtest.main;

import com.vangent.hieos.xutil.exception.ExceptionUtil;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xtest.framework.TestConfig;
import com.vangent.hieos.xtest.framework.StringSub;
import com.vangent.hieos.xtest.framework.BasicTransaction;
import com.vangent.hieos.xtest.framework.PlanContext;
import com.vangent.hieos.xutil.xml.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.parsers.FactoryConfigurationError;
import org.apache.axiom.om.OMElement;

public class XTestDriver  {
	static public String version = "xx.yy";
	static public ArrayList only_steps = new ArrayList();
	static public boolean direct_call = false;
	static public boolean prepair_only = false;
	static public String actor_config_file = null;
	static public OMElement actor_config = null;  // entire configuration
	static public OMElement actor_element = null; // selected actor
	static public boolean xds_a = false;
	static public boolean xds_b = false;
	static public boolean l_option = false;
	//static public String target = null;
	static public String args = "";
	static StringSub str_sub = new StringSub();


	static public void main(String[] argv) {
		// This gets around a bug in Leopard (MacOS X 10.5) on Macs
		//System.setProperty("http.nonProxyHosts", "");
		
		for (int i=0; i<argv.length; i++)
			args += argv[i] + " ";
		try {
			for (int i=0; i<argv.length; i++) {
				String cmd = argv[i];
				if (cmd.equals("--prepare"))
					prepair_only = true;
				else if (cmd.equals("--target") || cmd.equals("-t")) {
					i++;
					if (i >= argv.length) { System.out.println("--target missing value"); throw new Exception(""); }
					TestConfig.target = argv[i];
				}
				else if (cmd.equals("-l"))
					l_option = true;
				else if (cmd.equals("--runcheck")) {
					System.out.println("xdstest2 status: Xdstest2 is running");
					System.exit(0);
				}
				else if (cmd.equals("--xdsa"))
					xds_a = true;
				else if (cmd.equals("--xdsb"))
					xds_b = true;
				else if (cmd.equals("--actorconfig")) {
					i++;
					if (i >= argv.length) { System.out.println("--actorconfig missing value"); throw new Exception(""); }
					actor_config_file = argv[i];
				}
				else if (cmd.equals("--testmgmt")) {
					i++;
					if (i >= argv.length) { System.out.println("--testmgmt missing value"); throw new Exception(""); }
					TestConfig.testmgmt_dir = argv[i];
				}
				else if (cmd.equals("--logstatus")) {
					i++;
					if (i >= argv.length) { System.out.println("--logstatus missing value"); throw new Exception(""); }
					String logname = argv[i];
					parse_log_status(logname);
				}
				else if (cmd.equals("--sub")) {
					i++;
					if (i >= argv.length) { System.out.println("--sub missing values"); throw new Exception(""); }
					String from = argv[i];
					i++;
					if (i >= argv.length) { System.out.println("--sub missing values"); throw new Exception(""); }
					String to = argv[i];
					str_sub.addSub(from, to);
				}
				else 
					only_steps.add(cmd);
			}
		} catch (Exception e) {
			usage();
			System.exit(-1);
		}
		validate();

		boolean stat = runTest(TestConfig.base_path);
		if (stat)
			System.out.println("xdstest2 status: Pass");
		else
			System.out.println("xdstest2 status: Fail");
	}
	
	static void parse_log_status(String logname) {
		OMElement log = null;
		try {
		 log = Util.parse_xml(new File(logname));
		} catch (XdsInternalException e) {
			System.out.println("Cannot open or parse " + logname);
			System.exit(-1);
		}
		String status = log.getAttributeValue(new QName("status"));
		if (status != null && status.equals("Pass"))
			System.exit(0);
		System.exit(-1);
	}

	private static void validate() throws FactoryConfigurationError {
//		if (xds_a == false && xds_b == false) {
//			System.out.println("--xdsa or --xdsb must be specified");
//			usage();
//			System.exit(-1);
//		}
		
		if (actor_config_file == null) {
			System.out.println("--actorconfig configfile is required");
			usage();
			System.exit(-1);
		}
		
		if (TestConfig.testmgmt_dir == null) {
			System.out.println("--testmgmt dir is required");
			usage();
			System.exit(-1);
		}
		
		if ( !TestConfig.testmgmt_dir.endsWith("/"))
			TestConfig.testmgmt_dir = TestConfig.testmgmt_dir + "/";
		
		try {
			actor_config = Util.parse_xml(new File(actor_config_file));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			usage();
			System.exit(-1);
		}
		
		int system_count = 0;
		for (Iterator it=actor_config.getChildElements(); it.hasNext() ; ) {
			it.next();
			system_count++;
		}
		
		if (system_count == 0) {
			System.out.println("No systems configured in " + actor_config_file);
			usage();
			System.exit(-1);
		}
		
//		if (target == null && system_count > 1) {
//			System.out.println("--target was not specified and more than one system is configured in " + actor_config_file);
//			usage();
//			System.exit(-1);
//		}
		
		if (TestConfig.target == null) {
			actor_element = (OMElement) actor_config.getChildElements().next();
		} else {
			for (Iterator it=actor_config.getChildElements(); it.hasNext(); ) {
				OMElement actor = (OMElement) it.next();
				String name = actor.getLocalName();
				if (name.equals(TestConfig.target)) {
					actor_element = actor;
					break;
				}
			}
			if (actor_element == null) {
				System.out.println("Actor definitions for target implementation " + TestConfig.target + " was not found in configuration file " + actor_config_file);
				usage();
				System.exit(-1);
			}
		}
	}

	static void usage() {
		System.out.println(
				"\n" + 
				"Usage: xds2 --xdsa|--xdsb [options] \n" +
				"     where options are:\n" +
				"   --xdsa : selects XDS.a coding and protocols\n" +
				"   --xdsb : selects XDS.b coding and protocols\n" +
				"   --actorconfig filename : XML file defining the Registry and Repository actors and their endpoints\n" +
				"   --target system : the system, configured in actor configuration (--actorconfig option) to send tests to.\n" +
				"             if this is specified, --trans must also be specified.\n" +
				"   --trans transactionname :  used to select a transaction from the machine from the above configuration file (--target must also be specified)\n" +
				"      IF --machine AND --trans ARE specified then they override the <RegistryEndpoint/> element of testplan.xml\n" +
				"   --testmgmt directoryname : directory holding the test management info like uniqueId counter etc.\n" +
				"   -t system         The system requested here must be an Element in the actor configuration. If only one system is\n" +
				"                     configured then this option need not be specified\n" +
				"   --prepair : prepair the submission, add it to the log file and exit (no trancation is sent)\n" +
				"   --sub <from> <to> : substitute string <to> for string <from> in testplan.xml before using\n" +
				"   -l : list steps and exit\n" + 
				"   teststep : zero or more test step names, test steps are executed in order of listing. If no\n" +
				"            test steps are provided, all test steps will be run.\n" +
				"\n" +
				"If no teststeps are specified, all test steps in testplan.xml are executed in order.\n" +
				"All results go into log.xml"
		);
	}
	
	static public String getEndpoint(String service, short xds_version) {
		// service should be "r", "pr" "q", "sq", "ret"
		String target_name = service + ((xds_version == BasicTransaction.xds_a) ? ".a" : ".b");
		OMElement ele = MetadataSupport.firstChildWithLocalName(actor_element, target_name); 
		if (ele == null) {
			System.out.println("Actor " + actor_element.getLocalName() + " does not have and endpoint for service " + target_name);
			usage();
			System.exit(-1);
		}
		return ele.getText();
	}

	public static boolean runTestDirect(String test_path, short xds_version, String config, String mgmt, String target_config, ArrayList<String> steps) {
		if (xds_version == BasicTransaction.xds_a)
			xds_a = true;
		else
			xds_b = true;
		actor_config_file = config;
		TestConfig.testmgmt_dir = mgmt;
		TestConfig.target = target_config;
		TestConfig.base_path = test_path;
		if ( !TestConfig.base_path.endsWith("/"))
			TestConfig.base_path = TestConfig.base_path + "/";
		
		direct_call = true;
		
		validate();
		
		if (steps != null) {
			for (String step : steps) {
				only_steps.add(step);
			}
		}
		
		return runTest(test_path);
	}


	public static boolean runTest(String test_path) {
		// This gets around a bug in Leopard (MacOS X 10.5) on Macs and their 
		// interaction with addressing.mar
		//System.setProperty("http.nonProxyHosts", "");
		if ( !test_path.endsWith("/"))
			test_path = test_path + "/";
		TestConfig.base_path = test_path;
		try {
//			String default_config_file = TestConfig.testmgmt_dir + "/default.xml";
//			OMElement default_config = Util.parse_xml(new File(default_config_file));
			
			PlanContext plan = new PlanContext((xds_a) ? BasicTransaction.xds_a : BasicTransaction.xds_b);
//			plan.setDefaultConfig(default_config);
			return plan.run(test_path + "testplan.xml", str_sub);
		} 
		catch (XdsException e) {
			System.out.println("XdsException thrown: " + exception_details(e));
		}
		catch (NullPointerException e) {
			System.out.println(ExceptionUtil.exception_details(e));
		}
		return false;
	}

	static public String exception_details(Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);

		return "Exception thrown: " + e.getClass().getName() + "\n" + e.getMessage() + "\n" + new String(baos.toByteArray());
	}




}
