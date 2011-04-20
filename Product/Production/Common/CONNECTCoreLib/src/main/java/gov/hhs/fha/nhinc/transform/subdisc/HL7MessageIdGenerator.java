/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7MessageIdGenerator {

    private static Log log = LogFactory.getLog(HL7MessageIdGenerator.class);
    private static final String PROPERTY_FILE = "adapter";
    private static final String PROPERTY_NAME = "assigningAuthorityId";

    /**
     * Generate the messageId based on the passed device id.  The device id is
     * the corresponding assigning authority id.
     * @param myDeviceId
     * @return messageId
     */
    public static II GenerateHL7MessageId(String myDeviceId) {
        II messageId = new II();
        
        if (NullChecker.isNullish(myDeviceId)) {
            myDeviceId = getDefaultLocalDeviceId();
        }
        
        log.debug("Using local device id " + myDeviceId);
        messageId.setRoot(myDeviceId);
        messageId.setExtension(GenerateMessageId());
        return messageId;
    }

    /**
     * Generate the messageId based on the default device id.  The device id is
     * the assigning authority id from the adapter.properties configuration file.
     * @return messageId
     */
    public static II GenerateHL7MessageId() {
        String deviceId = getDefaultLocalDeviceId();
        
        return GenerateHL7MessageId(deviceId);
    }

    private static String getDefaultLocalDeviceId() {
        String defaultLocalId = "";

        try {
            defaultLocalId = PropertyAccessor.getProperty(PROPERTY_FILE, PROPERTY_NAME);
        } catch (PropertyAccessException e) {
            log.error("PropertyAccessException - Default Assigning Authority property not defined in adapter.properties", e);
        }

        return defaultLocalId;
    }
    
    /**
     * Generate the messageId from a new UID.
     * @return messageId
     */
    public static String GenerateMessageId() {
        java.rmi.server.UID uid = new java.rmi.server.UID();
        log.debug("generated message id=" + uid.toString());
        return uid.toString();
    }
}
