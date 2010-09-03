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
package gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy;

import org.hl7.v3.PRPAIN201306UV02;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterPatientDiscoveryProxyNoOpImpl implements AdapterPatientDiscoveryProxy
{

    private Log log = null;

    /**
     * Default constructor.
     */
    public AdapterPatientDiscoveryProxyNoOpImpl()
    {
        log = createLogger();
    }

    /**
     * Creates the log object for logging.
     *
     * @return The log object.
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * This no op implementation - it simply returns an empty object.
     *
     * @param body The message to be sent to the web service.
     * @param assertion The assertion information to go with the message.
     * @return The response from the web service.
     */
    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(org.hl7.v3.PRPAIN201305UV02 body, AssertionType assertion)
    {

        log.debug("Entering AdapterPatientDiscoveryProxyNoOpImpl.respondingGatewayPRPAIN201305UV02");
        return new PRPAIN201306UV02();
    }
}
