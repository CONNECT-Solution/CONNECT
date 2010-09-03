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

import gov.hhs.fha.nhinc.lift.common.util.RequestToken;
import gov.hhs.fha.nhinc.lift.proxy.client.Client;
import gov.hhs.fha.nhinc.lift.proxy.client.SSLClient;
import gov.hhs.fha.nhinc.lift.proxy.client.TestClientHandshaker;
import gov.hhs.fha.nhinc.lift.proxy.properties.interfaces.ConsumerProxyPropertiesFacade;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConsumerProxyPropertiesFacadeRI implements ConsumerProxyPropertiesFacade {

    private Log log = null;

    public ConsumerProxyPropertiesFacadeRI() {
        log = createLogger();
    }

    protected Log createLogger() {
        if (log == null) {
            log = LogFactory.getLog(getClass());
        }
        return log;
    }

    @Override
    public Client getClientInstance(InetAddress address,
            int port,
            RequestToken token) throws IOException {

        TestClientHandshaker h = new TestClientHandshaker();
        Client c = new SSLClient(address, port, token, h);
        log.debug("Create client " + c.getSocket().getInetAddress() + ": " + c.getSocket().getPort());

        return c;
    }

    @Override
    public InetAddress getClientProxyAddress() {
        try {
            //only expects connection on the localhost from the file downloader
            return InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public void setTrustStore() {
        try {
            String loc = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_TRUSTSTORE);
            String pass = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_TRUSTSTOREPASS);
            System.setProperty("javax.net.ssl.trustStore", loc);

            // Set the Keystore type to PKCS11 in FIPS mode
            if ("NONE".equalsIgnoreCase(loc)){
                System.setProperty("javax.net.ssl.trustStoreType", "PKCS11");
            }

            System.setProperty("javax.net.ssl.trustStorePassword", pass);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }
    }

    @Override
    public void setKeyStoreProperty() {
        try {
            String loc = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_KEYSTORE);
            String pass = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_KEYSTOREPASS);
            System.setProperty("javax.net.ssl.keyStore", loc);

            // Set the Keystore type to PKCS11 in FIPS mode
            if ("NONE".equalsIgnoreCase(loc)){
                System.setProperty("javax.net.ssl.keyStoreType", "PKCS11");
            }

            System.setProperty("javax.net.ssl.keyStorePassword", pass);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }
    }
}
