/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxypatientdiscoverysecuredasyncreq.NhincProxyPatientDiscoverySecuredAsyncReqPortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;

public class PassthruPatientDiscoveryDeferredRequestProxyWebServiceSecuredImpl implements PassthruPatientDiscoveryDeferredRequestProxy
{

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscoverysecuredasyncreq";
    private static final String SERVICE_LOCAL_PART = "NhincProxyPatientDiscoverySecuredAsyncReq";
    private static final String PORT_LOCAL_PART = "NhincProxyPatientDiscoverySecuredAsyncReqPortType";
    private static final String WSDL_FILE = "NhincProxyPatientDiscoverySecuredAsyncReq.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscoverysecuredasyncreq:Proxy_ProcessPatientDiscoveryAsyncReqRequestMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public PassthruPatientDiscoveryDeferredRequestProxyWebServiceSecuredImpl()
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

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected NhincProxyPatientDiscoverySecuredAsyncReqPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        NhincProxyPatientDiscoverySecuredAsyncReqPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincProxyPatientDiscoverySecuredAsyncReqPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        } else
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
            } catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 message, AssertionType assertion, NhinTargetSystemType targets)
    {
        log.debug("Begin processPatientDiscoveryAsyncReq");
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        try
        {
                String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PATIENT_DISCOVERY_PASSTHRU_SECURED_ASYNC_REQ_SERVICE_NAME);
                NhincProxyPatientDiscoverySecuredAsyncReqPortType port = getPort(url, NhincConstants.PATIENT_DISCOVERY_ACTION, WS_ADDRESSING_ACTION, assertion);

                if (port != null)
                {
                    ProxyPRPAIN201305UVProxySecuredRequestType securedRequest = new ProxyPRPAIN201305UVProxySecuredRequestType();
                    securedRequest.setNhinTargetSystem(targets);
                    securedRequest.setPRPAIN201305UV02(message);

                    response = (MCCIIN000002UV01) oProxyHelper.invokePort(port, NhincProxyPatientDiscoverySecuredAsyncReqPortType.class, "proxyProcessPatientDiscoveryAsyncReq", securedRequest);
                } else
                {
                    log.error("NhincProxyPatientDiscoverySecuredAsyncReqPortType is null");
                }
        } catch (Exception ex)
        {
            log.error("Error calling proxyProcessPatientDiscoveryAsyncReq: " + ex.getMessage(), ex);
        }

        log.debug("End processPatientDiscoveryAsyncReq");
        return response;
    }
}
