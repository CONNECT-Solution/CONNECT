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
// FILE: ClientMessage.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: ClientMessage.java
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY:
//
//
//Feb 24 2010 PTR xxx R.Robinson   Initial Coding.
//
//********************************************************************
package gov.hhs.fha.nhinc.lift.common.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author rrobin20
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ClientMessage")
public class ClientMessage {
	/*
	 * This class embodies the information a NotificationConsumer needs to send
	 * to the proxy system.
	 */

	@XmlElement(name = "AssertionElement", required = true)
	private SecurityToken securityToken;

	@XmlElement(name = "DataElement", required = true)
	private DataToken data;

	@XmlElement(name = "SubscriptionID", required = false)
	private String subscriptionID;

	@XmlElement(name = "TransferType", required = false)
	private String transferType;

	public ClientMessage() {
		super();
	}

	public ClientMessage(SecurityToken user, DataToken data) {
		super();
		this.securityToken = user;
		this.data = data;
	}
	
	public ClientMessage(SecurityToken user, DataToken data, String transferType) {
		super();
		this.securityToken = user;
		this.data = data;
		this.transferType = transferType;
	}

	public ClientMessage(SecurityToken user, DataToken data,
			String subscriptionID, String transferType) {
		super();
		this.securityToken = user;
		this.data = data;
		this.subscriptionID = subscriptionID;
		this.transferType = transferType;
	}

	public SecurityToken getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(SecurityToken user) {
		this.securityToken = user;
	}

	public DataToken getData() {
		return data;
	}

	public void setData(DataToken data) {
		this.data = data;
	}

	public String getSubscriptionID() {
		return subscriptionID;
	}

	public void setSubscriptionID(String subscriptionID) {
		this.subscriptionID = subscriptionID;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public String getTransferType() {
		return this.transferType;
	}
}
