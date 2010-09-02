/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201306UVProxySecuredRequestType;

/**
 *
 * @author JHOPPESC
 */
public class NhincProxyPatientDiscoveryAsyncRespImpl
{

    private Log log = null;

    public NhincProxyPatientDiscoveryAsyncRespImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(ProxyPRPAIN201306UVProxySecuredRequestType request, WebServiceContext context)
    {
        log.debug("Begin NhincProxyPatientDiscoverySecuredAsyncRespImpl.proxyProcessPatientDiscoveryAsyncResp(Secured)");
        MCCIIN000002UV01 response = null;

        AssertionType assertion = getAssertion(context, null);
        PRPAIN201306UV02 pdRequest = null;
        NhinTargetSystemType targetSystem = null;
        if (request != null)
        {
            pdRequest = request.getPRPAIN201306UV02();
            targetSystem = request.getNhinTargetSystem();
        }

        response = new PassthruPatientDiscoveryDeferredRespOrchImpl().proxyProcessPatientDiscoveryAsyncResp(pdRequest, assertion, targetSystem);

        log.debug("End NhincProxyPatientDiscoverySecuredAsyncRespImpl.proxyProcessPatientDiscoveryAsyncResp(Secured)");
        return response;
    }

    public org.hl7.v3.MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(org.hl7.v3.ProxyPRPAIN201306UVProxyRequestType request, WebServiceContext context)
    {
        log.debug("Begin NhincProxyPatientDiscoverySecuredAsyncRespImpl.proxyProcessPatientDiscoveryAsyncResp(Unsecured)");
        MCCIIN000002UV01 response = null;

        AssertionType assertion = null;
        PRPAIN201306UV02 pdRequest = null;
        NhinTargetSystemType targetSystem = null;
        if (request != null)
        {
            pdRequest = request.getPRPAIN201306UV02();
            targetSystem = request.getNhinTargetSystem();
            assertion = request.getAssertion();
        }
        assertion = getAssertion(context, assertion);

        response = new PassthruPatientDiscoveryDeferredRespOrchImpl().proxyProcessPatientDiscoveryAsyncResp(pdRequest, assertion, targetSystem);

        log.debug("End NhincProxyPatientDiscoverySecuredAsyncRespImpl.proxyProcessPatientDiscoveryAsyncResp(Unsecured)");
        return response;
    }

    private AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn)
    {
        AssertionType assertion = null;
        if (oAssertionIn == null)
        {
            assertion = SamlTokenExtractor.GetAssertion(context);
        }
        else
        {
            assertion = oAssertionIn;
        }

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null)
        {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
            List<String> relatesToList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList))
            {
                assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context).get(0));
            }
        }

        return assertion;
    }

}
