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

package com.vangent.hieos.xwebtools.servlets.framework;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class SecurityProperties {
	java.util.Properties properties = null;
	private final static Logger logger = Logger.getLogger(SecurityProperties.class);
	private static SecurityProperties properties_object = null;

	static {
		BasicConfigurator.configure();
		(new SecurityProperties()).init();
	}

	SecurityProperties() {   }

//	public static SecurityProperties loader() {
//	if (properties_object == null) properties_object = new SecurityProperties();
//	return properties_object;
//	}

	void init() {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("security.properties");
		if (is == null) { logger.fatal("Cannot load security.properties" ); return; }
		properties = new java.util.Properties();
		try {
			properties.load(is);
		}
		catch (Exception e) {
			logger.fatal(exception_details(e));
		}

		String keystore = null;
		String keystorepass = null;
		String keystoretype = null;
		String certsfile = null;
		String certsfilepass = null;
		String certsfiletype = null;
		String debug_string = null;
		boolean debug = false;


		keystore = properties.getProperty("keystore");
		keystoretype = properties.getProperty("keystoretype");
		keystorepass = properties.getProperty("keystorepass");
		certsfile = properties.getProperty("certsfile");
		certsfiletype = properties.getProperty("certsfiletype");
		certsfilepass = properties.getProperty("certsfilepass");
		debug_string = properties.getProperty("debug");

		if (keystoretype == null || keystoretype.equals(""))
			keystoretype = null;
		if (certsfiletype == null || certsfiletype.equals(""))
			certsfiletype = null;

		if (debug_string != null && debug_string.equals("true"))
			debug = true;

		System.out.println("keystore = " + keystore);
		System.out.println("keystore format = " + keystoretype);

		System.out.println("truststore = " + certsfile);
		System.out.println("truststore format = " + certsfiletype);
		System.out.println("debug is " + debug);

		//set the system security properties
		System.setProperty("javax.net.ssl.keyStore", keystore);
		System.setProperty("javax.net.ssl.keyStorePassword", keystorepass);
		if (keystoretype != null)
			System.setProperty("javax.net.ssl.keyStoreType", keystoretype);
		System.setProperty("javax.net.ssl.trustStore",certsfile);
		System.setProperty("javax.net.ssl.trustStorePassword", certsfilepass);
		if (certsfiletype != null)
			System.setProperty("javax.net.ssl.trustStoreType", certsfiletype);
		if (debug)
			System.setProperty("javax.net.debug", "ssl");
	}

//	public String getString(String name) {
//	return properties.getProperty(name);
//	}

//	public boolean getBoolean(String name) {
//	return (properties.getProperty(name).equals("false")) ? false : true;
//	}

	public static String exception_details(Exception e) {
		if (e == null) 
			return "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);

		return "Exception thrown: " + e.getClass().getName() + "\n" + e.getMessage() + "\n" + new String(baos.toByteArray());
	}

}
