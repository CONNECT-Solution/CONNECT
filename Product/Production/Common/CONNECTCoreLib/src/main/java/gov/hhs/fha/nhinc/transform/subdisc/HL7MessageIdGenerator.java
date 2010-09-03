/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7MessageIdGenerator {
    private static Log log = LogFactory.getLog(HL7MessageIdGenerator.class);
    
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

    public static II GenerateHL7MessageId() {
        String deviceId = getDefaultLocalDeviceId();
        
        return GenerateHL7MessageId(deviceId);
    }

    private static String getDefaultLocalDeviceId() {
        return HL7Constants.DEFAULT_LOCAL_DEVICE_ID;
    }
    
    public static String GenerateMessageId() {
        java.rmi.server.UID uid = new java.rmi.server.UID();
        log.debug("generated message id=" + uid.toString());
        return uid.toString();
    }
}
