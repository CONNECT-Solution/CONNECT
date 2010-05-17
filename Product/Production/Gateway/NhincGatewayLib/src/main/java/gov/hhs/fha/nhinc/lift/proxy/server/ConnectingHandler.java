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
// FILE: ConnectingHandler.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: ConnectingHandler.java 
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
package gov.hhs.fha.nhinc.lift.proxy.server;

import gov.hhs.fha.nhinc.lift.common.util.AssertionUtil;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;

import gov.hhs.fha.nhinc.lift.common.util.SecurityToken;
import gov.hhs.fha.nhinc.lift.proxy.properties.interfaces.ProducerProxyPropertiesFacade;
import gov.hhs.fha.nhinc.lift.proxy.util.Connector;
import gov.hhs.fha.nhinc.lift.proxy.util.ProtocolWrapper;
import gov.hhs.fha.nhinc.lift.proxy.util.ProxyUtil;
import gov.hhs.fha.nhinc.lift.proxy.util.Response;

public class ConnectingHandler extends HandlerPrototype {
	private final ProducerProxyPropertiesFacade props;
	private final int bufferSize;
	
	private SecurityToken token;
	
	public ConnectingHandler(ProducerProxyPropertiesFacade props, int bufferSize) 
	{
		this.props = props;
		this.bufferSize = bufferSize;
	}
	
	@Override
	protected HandlerPrototype createInstance(ProtocolWrapper wrapper) {
		HandlerPrototype hand = new ConnectingHandler(props, bufferSize);

		hand.setWrapper(wrapper);

		return hand;
	}

	//NOTE: Copy and pasted from TestHandler
	@Override
	protected boolean performHandshake() throws IOException {
		System.out.println("HANDLER: Waiting for challenge.");
		
		String str = this.readLine();
		
		System.out.println("HANDLER: " + str);
		
		//Just using in seems to hang, was hoping it would realize when it could stop
//		Object obj = ProxyUtil.unmarshalFromReader(in, SecurityToken.class);
		
		Object obj = ProxyUtil.unmarshalFromReader(new StringReader(str), SecurityToken.class);
		
		System.out.println("HANDLER: Received challenge.");
		
		if(validChallenge(obj))
		{
			Response resp = new Response(true, "Handshake accepted.");
			String res = ProxyUtil.marshalToString(resp);
			
			//Should swap this out for something less dependent on the idea of a socket.
			this.sendLine(res);
			
			return true;
		}else{
			Response resp = new Response(false, "Handshake denied.");
			String res = ProxyUtil.marshalToString(resp);
			
			this.sendLine(res);
			
			return false;
		}
	}
	
	/**
	 * Need to decide if the given token proves the caller is allowed to 
	 * establish a connection.
	 * @param obj
	 * @return
	 */
	protected boolean validChallenge(Object obj)
	{
		if(obj instanceof SecurityToken)
		{
			SecurityToken token = (SecurityToken)obj;
			
			System.out.println("HANDLER: Challenge is a token: " + token + ", validating against properties.");
			
			SecurityToken known = props.getSecurityTokenForRequest(token.getRequest());
			
//			String knownStr = ProxyUtil.marshalToString(known);
//			String tokenStr = ProxyUtil.marshalToString(token);
//			
//			System.out.println("Comparing: " + knownStr);
//			System.out.println("      and: " + tokenStr);
//			
//			return knownStr.equals(tokenStr);
			
			try
			{
				boolean b = AssertionUtil.compareAssertions(token.getUser(), known.getUser());
				
				this.token = known;
				
				return b;
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("HANDLER: Challenge failed due to exception.");
				
				return false;
			}
		}else{
			System.out.println("HANDLER: Challenge is not a token.");
			
			return false;
		}
	}

	@Override
	public void run() {
		System.out.println("HANDLER: In Connecting handler.");
		
		/*
		 * Try to connect to the server for this request.  May want to check if
		 * a connection already exists which would then mean that this request
		 * should be denied.
		 */
		try {
			Thread toProxyThread, fromProxyThread;
			
			System.out.println("HANDLER: Connecting to server from properties facade.");
			Socket socket = props.getSocketToServerForRequest(token.getRequest());
			System.out.println("HANDLER: Connected to server: " + socket.getInetAddress()+ " on port: " + socket.getPort());
			
			try {
				System.out.println("HANDLER: Starting Connectors.");
				
				//Connection established, build Connectors to pass through messages.
				Connector toProxy = new Connector(socket.getInputStream(), this.getWrapper().getOutStream(), bufferSize);
				Connector fromProxy = new Connector(this.getWrapper().getInStream(), socket.getOutputStream(), bufferSize);
				
				toProxyThread = new Thread(toProxy);
				fromProxyThread = new Thread(fromProxy);
				
				toProxyThread.start();
				fromProxyThread.start();
			
				fromProxyThread.join();
				System.out.println("HANDLER: From proxy joined");
				
//				System.out.println("HANDLER: Closing socket because from proxy connector joined.");
//				socket.close();
//				System.out.println("HANDLER: Closing connection between proxies.");
//				this.getWrapper().close();
				
				toProxyThread.join();
				System.out.println("HANDLER: To proxy joined");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				socket.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println("HANDLER: Closing connection between proxies (if not yet closed).");
				this.getWrapper().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
