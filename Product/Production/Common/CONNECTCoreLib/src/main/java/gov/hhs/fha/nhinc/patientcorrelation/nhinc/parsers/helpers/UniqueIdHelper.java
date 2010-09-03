/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers;

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
        String myRoot = Configuration.getMyCommunityId();
        return createUniqueId(myRoot);

    }

    private static String generateUID() {
        java.rmi.server.UID uid = new java.rmi.server.UID();
        return uid.toString();
    }
}
