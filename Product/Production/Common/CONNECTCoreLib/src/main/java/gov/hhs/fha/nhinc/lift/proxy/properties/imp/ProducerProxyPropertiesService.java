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
// FILE: ProducerProxyPropertiesService.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: ProducerProxyPropertiesService.java
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

import gov.hhs.fha.nhinc.lift.common.util.LiftConnectionRequestToken;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import gov.hhs.fha.nhinc.lift.dao.LiftTransferDataRecordDao;
import gov.hhs.fha.nhinc.lift.model.LiftTransferDataRecord;
import gov.hhs.fha.nhinc.lift.proxy.properties.interfaces.ProducerProxyPropertiesFacade;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProducerProxyPropertiesService implements
        ProducerProxyPropertiesFacade {

    private Log log = null;

    public ProducerProxyPropertiesService() {
        log = createLogger();
    }

    protected Log createLogger() {
        if (log == null) {
            log = LogFactory.getLog(getClass());
        }
        return log;
    }

    @Override
    public boolean verifySecurityForRequest(LiftConnectionRequestToken request) {
        boolean result = false;

        LiftTransferDataRecordDao dbDao = new LiftTransferDataRecordDao();
        List<LiftTransferDataRecord> dbRecs = dbDao.findForGuid(request.getRequestGUID());

        if (dbRecs.size() == 1) {
            if (dbRecs.get(0).getTransferState().equalsIgnoreCase(NhincConstants.LIFT_TRANSFER_DB_STATE_ENTERED)) {
                log.debug("Found a single database entry matching the request GUID");
                // update record to inprogress
                LiftTransferDataRecord rec = new LiftTransferDataRecord();
                rec.setId(dbRecs.get(0).getId());
                rec.setRequestKeyGuid(dbRecs.get(0).getRequestKeyGuid());
                rec.setTransferState(NhincConstants.LIFT_TRANSFER_DB_STATE_PROCESSING);
                dbDao.updateRecord(rec);
                result = true;
            } else {
                log.warn("One record was found, but it was marked as PROCESSING");
                result = false;
            }
        } else if (dbRecs.size() > 1) {
            log.error("Multiple records were found for the request GUID");
        } else {
            log.error("No records were found for the request GUID");
        }

        if (result) {
            System.out.println("Processing request " + request.getRequestGUID());
        } else {
            System.out.println("Can not process request " + request.getRequestGUID());
        }
        return result;
    }

    @Override
    public void completeProcessingForRequest(LiftConnectionRequestToken request) {
        removeTransferRecord(request);
    }

    @Override
    public void terminateProcessingForRequest(LiftConnectionRequestToken request) {
        removeTransferRecord(request);
    }

    private void removeTransferRecord(LiftConnectionRequestToken request) {
        LiftTransferDataRecordDao dbDao = new LiftTransferDataRecordDao();
        List<LiftTransferDataRecord> dbRecs = dbDao.findForGuid(request.getRequestGUID());

        if (dbRecs.size() == 1) {
            log.debug("Removing database record for the request GUID " + request.getRequestGUID());
            // remove record
            dbDao.delete(dbRecs.get(0));
        } else if (dbRecs.size() > 1) {
            log.error("Multiple records were found for the request GUID");
        } else {
            log.error("No records were found for the request GUID");
        }
    }

    @Override
    public Socket getSocketToServerForRequest(LiftConnectionRequestToken request)
            throws IOException {

        Socket socket = new Socket();
        try {
            String fileServerIP = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_FILESERVER_IP);
            String fileServerPort = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_FILESERVER_PORT);
            int portNum = Integer.parseInt(fileServerPort);

            SocketAddress socketAddr = new InetSocketAddress(fileServerIP, portNum);
            socket.connect(socketAddr);

        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }
        log.debug("Creating socket " + socket.getInetAddress() + ": " + socket.getPort());
        return socket;
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
