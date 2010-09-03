/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
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
// FILE: ServerProxyDataToken.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: ServerProxyDataToken.java
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
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServerProxyDataToken")
public class ServerProxyDataToken {
	@XmlElement(name = "ServerProxyAddress", required = true)
	private String serverProxyAddress;

	@XmlElement(name = "ServerProxyPort", required = true)
	private int serverProxyPort;

	public ServerProxyDataToken() {
	}

	public ServerProxyDataToken(String serverProxyAddress, int serverProxyPort) {
		super();
		this.serverProxyAddress = serverProxyAddress;
		this.serverProxyPort = serverProxyPort;
	}

	public String getServerProxyAddress() {
		return serverProxyAddress;
	}

	public void setServerProxyAddress(String serverProxyAddress) {
		this.serverProxyAddress = serverProxyAddress;
	}

	public int getServerProxyPort() {
		return serverProxyPort;
	}

	public void setServerProxyPort(int serverProxyPort) {
		this.serverProxyPort = serverProxyPort;
	}
}
