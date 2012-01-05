/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.connectmgr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.connectmgr.data.CMEprInfo;

/**
 * This class has utilities for constructing the information needed for an endpoint reference
 * object.  It retrieves the information based on a uniform service name from the
 * connectionsEPR.properties file.
 * 
 * @author Les Westberg
 */
public class CMEprUtil {

    private static Log log = LogFactory.getLog(CMEprUtil.class);

    /**
     * This method creates an endpoint for the given service name and URL.
     * 
     * @param sUniformServiceName The service name for the service.
     * @return The Endpoint reference to be returned.
     * @throws ConnectionManagerException 
     */
    public static CMEprInfo createEPR(String sUniformServiceName) throws ConnectionManagerException {
        log.debug("Getting EPR for service '" + sUniformServiceName + "'");

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0)) {
            return null;
        }

        CMEprInfo oEpr = new CMEprInfo();

        oEpr.setUniformServiceName(sUniformServiceName);

        return oEpr;
    }
}
