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
// FILE: ServerApp.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: ServerApp.java 
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
package gov.hhs.fha.nhinc.lift;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.lift.proxy.properties.interfaces.ProducerProxyPropertiesFacade;
import gov.hhs.fha.nhinc.lift.proxy.properties.imp.ProducerProxyPropertiesService;
import gov.hhs.fha.nhinc.lift.proxy.server.ConnectingHandler;
import gov.hhs.fha.nhinc.lift.proxy.server.SSLServer;
import gov.hhs.fha.nhinc.lift.proxy.server.Server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author rrobin20
 *
 */
public class ServerApp {

    private Log log = null;

    public ServerApp() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    private void startServer() {
        Server serv = createServer();
        if (serv != null) {
            Thread st = new Thread(serv);
            st.start();
            System.out.println("ServerApp started. ");
        }
    }

    private Server createServer() {
        Server serv = null;
        try {
            ProducerProxyPropertiesFacade props = setProxyProps();
            InetSocketAddress addr = createSocketAddr();
            
            int bufferSize = 65536;
            serv = new SSLServer(addr, new ConnectingHandler(props, bufferSize));
            log.debug("LiFT Server Connecting Handler Runnable created");
        } catch (IOException ex) {
            log.error("Failed to create LiFT Server " + ex.getMessage());
        }
        return serv;
    }

    private InetSocketAddress createSocketAddr() {
        InetSocketAddress addr = null;
        String proxyAddr = "";
        String proxyPort = "";
        try {
            proxyAddr = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_PROXY_ADDRESS);
            proxyPort = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_PROXY_PORT);
            int portNum = Integer.parseInt(proxyPort);

            InetAddress inetAddr = InetAddress.getByName(proxyAddr);
            addr = new InetSocketAddress(inetAddr, portNum);
            log.debug("LiFT Server Address defined as: " + addr.toString());
        } catch (UnknownHostException ex) {
            log.error("Unknown LiFT Proxy Address and Port " + proxyAddr + ":" + proxyPort + " " + ex.getMessage());
        } catch (PropertyAccessException ex) {
            log.error("Missing LiFT Proxy Address and Port Properties " + ex.getMessage());
        }
        return addr;
    }

    private ProducerProxyPropertiesFacade setProxyProps() {
        ProducerProxyPropertiesFacade props = new ProducerProxyPropertiesService();
        props.setKeyStoreProperty();
        props.setTrustStore();
        return props;
    }

    public static void main(String... args) throws InterruptedException {
        ServerApp app = new ServerApp();
        app.startServer();
    }
}
