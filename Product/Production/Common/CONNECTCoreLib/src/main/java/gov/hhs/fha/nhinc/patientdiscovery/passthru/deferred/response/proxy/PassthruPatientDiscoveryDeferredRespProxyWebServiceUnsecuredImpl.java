/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincproxypatientdiscoveryasyncresp.NhincProxyPatientDiscoveryAsyncRespPortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201306UVProxyRequestType;

/**
 *
 * @author Neil Webb
 */
public class PassthruPatientDiscoveryDeferredRespProxyWebServiceUnsecuredImpl implements PassthruPatientDiscoveryDeferredRespProxy
{
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscoveryasyncresp";
    private static final String SERVICE_LOCAL_PART = "NhincProxyPatientDiscoveryAsyncResp";
    private static final String PORT_LOCAL_PART = "NhincProxyPatientDiscoveryAsyncRespPortType";
    private static final String WSDL_FILE = "NhincProxyPatientDiscoveryAsyncResp.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscoveryasyncresp:Proxy_ProcessPatientDiscoveryAsyncRespRequest";
    private WebServiceProxyHelper oProxyHelper = null;

    public PassthruPatientDiscoveryDeferredRespProxyWebServiceUnsecuredImpl()
    {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }

    protected NhincProxyPatientDiscoveryAsyncRespPortType getPort(String url, String wsAddressingAction, AssertionType assertion)
    {
        NhincProxyPatientDiscoveryAsyncRespPortType port = null;

        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincProxyPatientDiscoveryAsyncRespPortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService()
    {
        if (cachedService == null)
        {
            try
            {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            }
            catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(PRPAIN201306UV02 request, AssertionType assertion, NhinTargetSystemType targetSystem)
    {
        log.debug("Begin PassthruPatientDiscoveryDeferredRespProxyWebServiceUnsecuredImpl.proxyProcessPatientDiscoveryAsyncResp(...)");
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        String serviceName = NhincConstants.PATIENT_DISCOVERY_PASSTHRU_ASYNC_RESP_SERVICE_NAME;

        try
        {
            log.debug("Before target system URL look up.");
            String url = oProxyHelper.getUrlLocalHomeCommunity(serviceName);
            if (log.isDebugEnabled())
            {
                log.debug("After target system URL look up. URL for service: " + serviceName + " is: " + url);
            }

            if (NullChecker.isNotNullish(url))
            {
                ProxyPRPAIN201306UVProxyRequestType wsRequest = new ProxyPRPAIN201306UVProxyRequestType();
                wsRequest.setPRPAIN201306UV02(request);
                wsRequest.setAssertion(assertion);
                wsRequest.setNhinTargetSystem(targetSystem);
                NhincProxyPatientDiscoveryAsyncRespPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);
                response = (MCCIIN000002UV01) oProxyHelper.invokePort(port, NhincProxyPatientDiscoveryAsyncRespPortType.class, "proxyProcessPatientDiscoveryAsyncResp", wsRequest);
            }
            else
            {
                log.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        }
        catch (Exception ex)
        {
            log.error("Error: Failed to retrieve url for service: " + serviceName + " for local home community");
            log.error(ex.getMessage(), ex);
        }

        log.debug("End PassthruPatientDiscoveryDeferredRespProxyWebServiceUnsecuredImpl.proxyProcessPatientDiscoveryAsyncResp(...)");
        return response;
    }

}
