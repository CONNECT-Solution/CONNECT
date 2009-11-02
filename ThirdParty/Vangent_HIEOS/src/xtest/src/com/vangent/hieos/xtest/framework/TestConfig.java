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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class TestConfig {
	// Directory holding testplan.xml and its support files
	static public String base_path = "./";
	
	// Output directory for log.xml files (organized in same dir structure as testkit)
	static public String log_dir = null;
	
	// Full path to xdstoolkit/xdstest directory
	static public String testmgmt_dir = null;
	
	// REST service to call to allocate a patient id for testing 
	// Configured in actors.xml
	static public String pid_allocate_endpoint = null;
	
	// Endpoint Selection support
	static public HashMap endpoints = null;
	static public HashMap secure_endpoints = null;
	static public String target = null;   // target machine - site name in actors.xml
	
	static public HashMap repositories = null;
	static public HashMap secureRepositories = null;
	
	// XCR home => endpoint
	static public HashMap xRepositories = null;
	static public HashMap xSecRepositories = null;
	
	// pointer into actors.xml
	
	// XPath (with no trailing /) to element defining selected site
	static public String siteXPath = null;
	
	static public String currentStep = null;
	
	static public boolean verbose  = false;
	
	static public boolean secure = false;
	
	
	
	
	static public void rememberPatientId(String pid) throws FileNotFoundException, IOException {
		if (testmgmt_dir == null) return;
		
		FileOutputStream fos = new FileOutputStream(new File(testmgmt_dir + File.separatorChar + "patientid_base.txt"));
		fos.write(pid.substring(0,pid.indexOf("^")).getBytes());
		fos.close();

		fos = new FileOutputStream(new File(testmgmt_dir + File.separatorChar + "assigning_authority.txt"));
		fos.write(pid.substring(pid.lastIndexOf("^")+1).getBytes());
		fos.close();
	}
}
