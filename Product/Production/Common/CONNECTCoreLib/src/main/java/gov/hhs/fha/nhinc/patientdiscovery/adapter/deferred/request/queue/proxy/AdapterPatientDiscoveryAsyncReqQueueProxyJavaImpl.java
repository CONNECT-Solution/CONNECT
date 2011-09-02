/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.queue.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.queue.AdapterPatientDiscoveryDeferredReqQueueOrchImpl;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

/**
 * This class is the java implementation of the AdapterPatientDiscoveryAsyncReqQueue
 * component proxy.
 *
 * @author Les westberg
 */
public class AdapterPatientDiscoveryAsyncReqQueueProxyJavaImpl implements AdapterPatientDiscoveryAsyncReqQueueProxy
{

    private Log log = null;

    /**
     * Default constructor.
     */
    public AdapterPatientDiscoveryAsyncReqQueueProxyJavaImpl()
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
    public MCCIIN000002UV01 addPatientDiscoveryAsyncReq(PRPAIN201305UV02 request, AssertionType assertion, NhinTargetCommunitiesType targets)
    {
        log.debug("Entering AdapterPatientDiscoveryAsyncReqQueueProxyJavaImpl.addPatientDiscoveryAsyncReq");
        AdapterPatientDiscoveryDeferredReqQueueOrchImpl oOrchestrator = new AdapterPatientDiscoveryDeferredReqQueueOrchImpl();
        log.debug("Leaving AdapterPatientDiscoveryAsyncReqQueueProxyJavaImpl.addPatientDiscoveryAsyncReq");
        return oOrchestrator.addPatientDiscoveryAsyncReq(request, assertion, targets);

    }
}
