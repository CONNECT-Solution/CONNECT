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
// FILE: ClientConnectorManager.javas
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: ClientConnectorManager.java 
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
package gov.hhs.fha.nhinc.lift.proxy.client;

import gov.hhs.fha.nhinc.lift.common.util.SecurityToken;
import gov.hhs.fha.nhinc.lift.proxy.properties.interfaces.ConsumerProxyPropertiesFacade;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;


/**
 * @author rrobin20
 *
 */
public class ClientConnectorManager {
	private static ClientConnectorManager instance;
	
	// should maintain references to currently active client connections it creates.
	private final Map<Integer, Thread> connectors;
	
	protected ClientConnectorManager()
	{
		connectors = new HashMap<Integer, Thread>();
	}
	
	/**
	 * This is a singleton so this method is in place to return a single 
	 * instance of this class.
	 * @return
	 */
	public static ClientConnectorManager getInstance()
	{
		if(instance == null)
		{
			instance = new ClientConnectorManager();
		}
		
		return instance;
	}
	
	/**
	 * This method will create a tunnel of a type defined by the properties 
	 * facade and will then bind a local temporary port for a client app to use
	 * to communicate through the proxy tunnel.  Returns an address to the 
	 * local server a client can talk to.
	 * 
	 * @param token
	 * @param serverProxyAddress
	 * @param serverProxyPort
	 * @return
	 * @throws IOException
	 */
	public InetSocketAddress startConnector(SecurityToken token, 
											InetAddress serverProxyAddress, 
											int serverProxyPort, 
											int bufferSize, 
											ConsumerProxyPropertiesFacade props) throws IOException
	{ 	
		/*
		 * Attempts to start up a connection with the desired server proxy.
		 * 
		 * Which type of client to use should come from configuration somewhere.
		 */
		System.out.println("Creating Client instance to connect to server proxy.");
		Client client = props.getClientInstance(serverProxyAddress, serverProxyPort, token);
		
		/*
		 * Start up a socket server bound to the local proxy hostname and to a
		 * port unique to this request.
		 */
		System.out.println("Getting local proxy address");
		InetAddress localProxyAddress = props.getClientProxyAddress();
		System.out.println("Local proxy address is: " + localProxyAddress);
		
		InetSocketAddress connectorAddress = new InetSocketAddress(localProxyAddress, 0);
		
		System.out.println("Starting server socket for real Client to access on port: " + connectorAddress.getPort());
		ServerSocket server = new ServerSocket();
		server.bind(connectorAddress);
		System.out.println("Server bound to port: " + server.getLocalPort());
		
		ClientConnector connector = new ClientConnector(server, client, bufferSize);
		
		Thread conn = new Thread(connector);
		connectors.put(connectorAddress.getPort(), conn);
		
		System.out.println("Starting ClientConnector.");
		conn.start();
		
		return new InetSocketAddress(server.getInetAddress(), server.getLocalPort()); 
	}
	
	/**
	 * Returns true if a connector thread was stopped.  False if not.  Blocks
	 * while waiting for the ClientConnector to halt.
	 * @param address
	 * @return
	 */
	public boolean stopConnector(InetSocketAddress address)
	{
		Thread t = connectors.get(address.getPort());
		
		try {
			if(t != null)
			{
				t.interrupt();
				
				t.join();
				
				return true;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
