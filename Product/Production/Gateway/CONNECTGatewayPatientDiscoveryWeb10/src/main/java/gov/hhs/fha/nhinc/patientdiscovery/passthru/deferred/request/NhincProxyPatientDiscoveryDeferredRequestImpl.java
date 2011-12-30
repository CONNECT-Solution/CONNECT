/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.service.WebServiceHelper;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;

class NhincProxyPatientDiscoveryDeferredRequestImpl
{

    private Log log = null;

    public NhincProxyPatientDiscoveryDeferredRequestImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected WebServiceHelper createWebServiceHelper()
    {
        return new WebServiceHelper();
    }

    protected PassthruPatientDiscoveryDeferredRequestOrchImpl createPassthruProxyPatientDiscoveryDeferredRequestOrchImpl()
    {
        return new PassthruPatientDiscoveryDeferredRequestOrchImpl();
    }

    MCCIIN000002UV01 processPatientDiscoveryAsyncRequestSecured(ProxyPRPAIN201305UVProxySecuredRequestType request, WebServiceContext context)
    {
        log.info("Begin processPatientDiscoveryAsyncRequestSecured(ProxyPRPAIN201305UVProxySecuredRequestType, WebServiceContext)");
        WebServiceHelper oHelper = createWebServiceHelper();
        PassthruPatientDiscoveryDeferredRequestOrchImpl implOrch = createPassthruProxyPatientDiscoveryDeferredRequestOrchImpl();
        MCCIIN000002UV01 response = null;

        try
        {
            if (request != null)
            {
                PRPAIN201305UV02 message = request.getPRPAIN201305UV02();
                NhinTargetSystemType targets = request.getNhinTargetSystem();
                response = (MCCIIN000002UV01) oHelper.invokeSecureWebService(implOrch, implOrch.getClass(), "processPatientDiscoveryAsyncReq", message, targets, context);
            } else
            {
                log.error("Failed to call the web orchestration (" + implOrch.getClass() + ".processPatientDiscoveryAsyncReq).  The input parameter is null.");
            }
        } catch (Exception e)
        {
            log.error("Failed to call the web orchestration (" + implOrch.getClass() + ".processPatientDiscoveryAsyncReq).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }
        return response;
    }

    MCCIIN000002UV01 processPatientDiscoveryAsyncRequestUnsecured(ProxyPRPAIN201305UVProxyRequestType request, WebServiceContext context)
    {
        log.info("Begin processPatientDiscoveryAsyncRequestSecured(ProxyPRPAIN201305UVProxyRequestType, WebServiceContext)");
        WebServiceHelper oHelper = createWebServiceHelper();
        PassthruPatientDiscoveryDeferredRequestOrchImpl implOrch = createPassthruProxyPatientDiscoveryDeferredRequestOrchImpl();
        MCCIIN000002UV01 response = null;

        try
        {
            if (request != null)
            {
                PRPAIN201305UV02 message = request.getPRPAIN201305UV02();
                NhinTargetSystemType targets = request.getNhinTargetSystem();
                AssertionType assertion = request.getAssertion();
                response = (MCCIIN000002UV01) oHelper.invokeUnsecureWebService(implOrch, implOrch.getClass(), "processPatientDiscoveryAsyncReq", message, assertion, targets, context);
            } else
            {
                log.error("Failed to call the web orchestration (" + implOrch.getClass() + ".processPatientDiscoveryAsyncReq).  The input parameter is null.");
            }
        } catch (Exception e)
        {
            log.error("Failed to call the web orchestration (" + implOrch.getClass() + ".processPatientDiscoveryAsyncReq).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }
        return response;
    }
}
