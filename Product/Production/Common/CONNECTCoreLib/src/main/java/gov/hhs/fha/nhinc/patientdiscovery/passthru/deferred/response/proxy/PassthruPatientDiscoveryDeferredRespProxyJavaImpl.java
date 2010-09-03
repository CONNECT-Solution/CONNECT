/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response.PassthruPatientDiscoveryDeferredRespOrchImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author Neil Webb
 */
public class PassthruPatientDiscoveryDeferredRespProxyJavaImpl implements PassthruPatientDiscoveryDeferredRespProxy
{
    private Log log = null;

    public PassthruPatientDiscoveryDeferredRespProxyJavaImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }


    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(PRPAIN201306UV02 request, AssertionType assertion, NhinTargetSystemType targetSystem)
    {
        log.debug("Begin PassthruPatientDiscoveryDeferredRespProxyJavaImpl.processPatientDiscoveryAsyncResp(...)");
        MCCIIN000002UV01 response = null;
        response = new PassthruPatientDiscoveryDeferredRespOrchImpl().proxyProcessPatientDiscoveryAsyncResp(request, assertion, targetSystem);
        log.debug("End PassthruPatientDiscoveryDeferredRespProxyJavaImpl.processPatientDiscoveryAsyncResp(...)");
        return response;
    }

}
