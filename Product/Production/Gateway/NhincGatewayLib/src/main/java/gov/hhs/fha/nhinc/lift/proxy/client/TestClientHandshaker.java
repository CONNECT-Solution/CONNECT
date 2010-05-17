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
// FILE: TestClientHandshaker.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: TestClientHandshaker.java 
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

import java.io.IOException;
import java.io.StringReader;

import gov.hhs.fha.nhinc.lift.proxy.util.ClientHandshaker;
import gov.hhs.fha.nhinc.lift.proxy.util.ProtocolWrapper;
import gov.hhs.fha.nhinc.lift.proxy.util.ProxyUtil;
import gov.hhs.fha.nhinc.lift.proxy.util.Response;

/**
 * @author rrobin20
 *
 */
public class TestClientHandshaker implements ClientHandshaker {

	@Override
	public boolean handshake(ProtocolWrapper wrapper, Client client) throws IOException {
		System.out.println("CLIENT: Sending challenge.");

		// Need to send security info to server for validation.
		wrapper.sendLine(ProxyUtil.marshalToString(client.getToken()));

		String response = wrapper.readLine();

		Response resp = (Response) ProxyUtil.unmarshalFromReader(
				new StringReader(response), Response.class);

		System.out.println("CLIENT: Response to challenge: "
				+ resp.getMessage());

		// Return if was a good response or not.
		return resp.isSuccess();
	}

}
