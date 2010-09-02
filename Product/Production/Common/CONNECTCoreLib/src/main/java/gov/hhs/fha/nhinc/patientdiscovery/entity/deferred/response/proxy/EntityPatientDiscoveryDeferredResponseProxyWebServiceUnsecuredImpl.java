/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.entitypatientdiscoveryasyncresp.EntityPatientDiscoveryAsyncRespPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.hl7.v3.MCCIIN000002UV01;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

/**
 *
 * @author dunnek
 */
public class EntityPatientDiscoveryDeferredResponseProxyWebServiceUnsecuredImpl implements EntityPatientDiscoveryDeferredResponseProxy
{

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entitypatientdiscoveryasyncresp";
    private static final String SERVICE_LOCAL_PART = "EntityPatientDiscoveryAsyncResp";
    private static final String PORT_LOCAL_PART = "EntityPatientDiscoveryAsyncRespPortType";
    private static final String WSDL_FILE = "EntityPatientDiscoveryAsyncResp.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:entitypatientdiscoveryasyncresp:ProcessPatientDiscoveryAsyncRespAsyncRequest";
    private WebServiceProxyHelper oProxyHelper = null;

    public EntityPatientDiscoveryDeferredResponseProxyWebServiceUnsecuredImpl()
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

    protected EntityPatientDiscoveryAsyncRespPortType getPort(String url, String wsAddressingAction, AssertionType assertion)
    {
        EntityPatientDiscoveryAsyncRespPortType port = null;

        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityPatientDiscoveryAsyncRespPortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(PRPAIN201306UV02 request, AssertionType assertion, NhinTargetCommunitiesType target)
    {
        log.debug("Begin EntityPatientDiscoveryDeferredResponseProxyWebServiceUnsecuredImpl.processPatientDiscoveryAsyncResp(...)");
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
                RespondingGatewayPRPAIN201306UV02RequestType wsRequest = new RespondingGatewayPRPAIN201306UV02RequestType();
                if (request != null)
                {
                    wsRequest.setPRPAIN201306UV02(request);
                    wsRequest.setAssertion(assertion);
                    wsRequest.setNhinTargetCommunities(target);
                }
                EntityPatientDiscoveryAsyncRespPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);
                response = (MCCIIN000002UV01) oProxyHelper.invokePort(port, EntityPatientDiscoveryAsyncRespPortType.class, "processPatientDiscoveryAsyncResp", wsRequest);
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

        log.debug("End EntityPatientDiscoveryDeferredResponseProxyWebServiceUnsecuredImpl.processPatientDiscoveryAsyncResp(...)");
        return response;
    }
}
