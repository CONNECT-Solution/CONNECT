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

package gov.hhs.fha.nhinc.docsubmission;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class NhinDocSubmissionUtils {
    private static Log log = LogFactory.getLog(NhinDocSubmissionUtils.class);
    /**
     * Checks the gateway.properties file to see if a specified Patient Discovery Service is enabled.
     *
     * @return Returns true if a specified Patient Discovery Service is enabled in the properties file.
     */
    public static boolean isServiceEnabled(String serviceName) {
        boolean serviceEnabled = false;
        try {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, serviceName);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + serviceName + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        return serviceEnabled;
    }

    /**
     * Checks to see if the query should  be handled internally or passed through to an adapter.
     *
     * @return Returns true if the pass through property for a specified Patient Discovery Service in the gateway.properties file is true.
     */
    public static boolean isInPassThroughMode(String passThruProperty) {
        boolean passThroughModeEnabled = false;
        try {
            passThroughModeEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, passThruProperty);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + passThruProperty + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return passThroughModeEnabled;
    }

}
