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
//    private static final String EPR_PROPERTY_FILE_NAME = "connectionEPR";
//    private static final String EPR_NAMESPACE_URI = "NamespaceURI";
//    private static final String EPR_PORT_NAME = "PortName";
//    private static final String EPR_SERVICE_NAME = "ServiceName";
//    private static final String EPR_NAMESPACE_PREFIX = "NamespacePrefix";

//    /**
//     * This method returns specific EPR data about a service.  The information is
//     * in the connectionEPR.properties file.
//     *
//     * @param sServiceName The Uniform Service name - this is used as a key to the properties in
//     *                     the property file that are specific to this service.
//     * @param sEPRKey The specific key that is being wanted.
//     * @return The value of that key for that service.
//     * @throws ConnectionManagerException
//     */
//    public static String getServiceSpecificEPRInfo(String sServiceName, String sEPRKey) throws ConnectionManagerException {
//        String sKey = sServiceName + "." + sEPRKey;
//        log.debug("Getting value for property '" + sKey + "'");
//        String sValue = "";
//
//        try {
//            sValue = PropertyAccessor.getProperty(EPR_PROPERTY_FILE_NAME, sKey);
//        } catch (PropertyAccessException e) {
//            String sErrorMessage = "Failed to retrieve property: '" + sKey + "' from " +
//                    EPR_PROPERTY_FILE_NAME + ".properties file.  Exception: " +
//                    e.getMessage();
//            log.error(sErrorMessage, e);
//            throw new ConnectionManagerException(sErrorMessage, e);
//        }
//
//        if (sValue == null) {
//            sValue = "";
//        }
//
//        return sValue;
//    }

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
//        oEpr.setNamespacePrefix(getServiceSpecificEPRInfo(sUniformServiceName, EPR_NAMESPACE_PREFIX));
//        oEpr.setNamespaceURI(getServiceSpecificEPRInfo(sUniformServiceName, EPR_NAMESPACE_URI));
//        oEpr.setPortName(getServiceSpecificEPRInfo(sUniformServiceName, EPR_PORT_NAME));
//        oEpr.setServiceName(getServiceSpecificEPRInfo(sUniformServiceName, EPR_SERVICE_NAME));

        return oEpr;
    }
}
