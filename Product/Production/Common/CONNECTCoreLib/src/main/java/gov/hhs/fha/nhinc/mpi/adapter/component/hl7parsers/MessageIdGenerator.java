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
package gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers;

/**
 *
 * @author rayj
 */
public class MessageIdGenerator {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(MessageIdGenerator.class);

    public static String GenerateMessageId() {
        java.rmi.server.UID uid = new java.rmi.server.UID();
        log.debug("generated message id=" + uid.toString());
        return uid.toString();
    }
}
