//
// Non-Export Controlled Information
//
//####################################################################
//## The MIT License
//## 
//## Copyright (c) 2010 Harris Corporation
//## 
//## Permission is hereby granted, free of charge, to any person
//## obtaining a copy of this software and associated documentation
//## files (the "Software"), to deal in the Software without
//## restriction, including without limitation the rights to use,
//## copy, modify, merge, publish, distribute, sublicense, and/or sell
//## copies of the Software, and to permit persons to whom the
//## Software is furnished to do so, subject to the following conditions:
//## 
//## The above copyright notice and this permission notice shall be
//## included in all copies or substantial portions of the Software.
//## 
//## THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//## EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//## OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//## NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//## HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//## WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//## FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//## OTHER DEALINGS IN THE SOFTWARE.
//## 
//####################################################################
//********************************************************************
// FILE: ConsumerProxyPropertiesFacadeRI.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: ConsumerProxyPropertiesFacadeRI.java 
//              
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY:
//
//> Feb 24, 2010 PTR#  - R. Robinson
// Initial Coding.
//<
//********************************************************************
package gov.hhs.fha.nhinc.lift.proxy.properties.imp;

import gov.hhs.fha.nhinc.lift.common.util.SecurityToken;
import gov.hhs.fha.nhinc.lift.proxy.client.Client;
import gov.hhs.fha.nhinc.lift.proxy.client.SSLClient;
import gov.hhs.fha.nhinc.lift.proxy.client.TestClientHandshaker;
import gov.hhs.fha.nhinc.lift.proxy.properties.interfaces.ConsumerProxyPropertiesFacade;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;


/**
 * @author rrobin20
 *
 */
public class ConsumerProxyPropertiesFacadeRI implements ConsumerProxyPropertiesFacade {
	private final Properties prop;
	
	public ConsumerProxyPropertiesFacadeRI(Properties prop)
	{
		this.prop = prop;
	}

	@Override
	public Client getClientInstance(InetAddress address, 
									int port,
									SecurityToken token) throws IOException {
		/*
		 * Should load this from the properties file some how.  Could use 
		 * reflection perhaps, feels evil for some reason though.
		 */
		TestClientHandshaker h = new TestClientHandshaker();
		Client c = new SSLClient(address, port, token, h);
		
		return c;
	}

	@Override
	public InetAddress getClientProxyAddress() {
		try {
			return InetAddress.getByName(prop.getProperty("ClientProxyAddress"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void setTrustStore() {
		String loc = prop.getProperty("TrustStore");
		String pass = prop.getProperty("TrustStorePass");
		
		System.setProperty("javax.net.ssl.trustStore", loc);
		System.setProperty("javax.net.ssl.trustStorePassword", pass);
	}
	
	@Override
	public void setKeyStoreProperty() {
		String loc = prop.getProperty("KeyStore");
		String pass = prop.getProperty("KeyStorePass");
		System.setProperty("javax.net.ssl.keyStore", loc);
		System.setProperty("javax.net.ssl.keyStorePassword", pass);
	}

}
