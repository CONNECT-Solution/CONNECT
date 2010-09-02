/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy;

import org.hl7.v3.PRPAIN201306UV02;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.AdapterPatientDiscoveryOrchImpl;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is the java implementation of the AdapterPatientDiscovery
 * component proxy.
 *
 * @author Les westberg
 */
public class AdapterPatientDiscoveryProxyJavaImpl implements AdapterPatientDiscoveryProxy
{

    private Log log = null;

    /**
     * Default constructor.
     */
    public AdapterPatientDiscoveryProxyJavaImpl()
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
     * This calls the java implementation for this method.
     *
     * @param body The message to be sent to the web service.
     * @param assertion The assertion information to go with the message.
     * @return The response from the web service.
     */
    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(org.hl7.v3.PRPAIN201305UV02 body, AssertionType assertion)
    {
        log.debug("Entering AdapterPatientDiscoveryProxyJavaImpl.respondingGatewayPRPAIN201305UV02");
        AdapterPatientDiscoveryOrchImpl oOrchestrator = new AdapterPatientDiscoveryOrchImpl();
        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
        request.setAssertion(assertion);
        request.setPRPAIN201305UV02(body);
        request.setNhinTargetCommunities(null);
        log.debug("Leaving AdapterPatientDiscoveryProxyJavaImpl.respondingGatewayPRPAIN201305UV02");
        return oOrchestrator.respondingGatewayPRPAIN201305UV02(request, assertion);

    }
}
