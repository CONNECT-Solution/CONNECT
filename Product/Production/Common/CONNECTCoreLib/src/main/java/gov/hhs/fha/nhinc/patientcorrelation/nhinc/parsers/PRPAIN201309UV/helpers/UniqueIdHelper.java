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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers;

import org.hl7.v3.II;

/**
 *
 * @author rayj
 */
public class UniqueIdHelper {

    public static II createUniqueId(String root) {
        II uniqueId = new II();
        String value;

        value = generateUID();
        uniqueId.setRoot(root);  //todo: refactor
        uniqueId.setExtension(value);

        return uniqueId;
    }

    public static II createUniqueId() {
        String myRoot = "1.1";
        return createUniqueId(myRoot);

    }

    private static String generateUID() {
        java.rmi.server.UID uid = new java.rmi.server.UID();
        return uid.toString();
    }
}
