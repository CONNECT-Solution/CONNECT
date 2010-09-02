/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.entitypatientdiscoverysecuredasyncreq.EntityPatientDiscoverySecuredAsyncReqPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType;

public class EntityPatientDiscoveryDeferredRequestProxyWebServiceSecuredImpl implements EntityPatientDiscoveryDeferredRequestProxy
{

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entitypatientdiscoverysecuredasyncreq";
    private static final String SERVICE_LOCAL_PART = "EntityPatientDiscoverySecuredAsyncReq";
    private static final String PORT_LOCAL_PART = "EntityPatientDiscoverySecuredAsyncReqPortSoap";
    private static final String WSDL_FILE = "EntityPatientDiscoverySecuredAsyncReq.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:entitypatientdiscoverysecuredasyncreq:ProcessPatientDiscoveryAsyncReqRequestMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public EntityPatientDiscoveryDeferredRequestProxyWebServiceSecuredImpl()
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
    protected EntityPatientDiscoverySecuredAsyncReqPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        EntityPatientDiscoverySecuredAsyncReqPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityPatientDiscoverySecuredAsyncReqPortType.class);
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

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 message, AssertionType assertion, NhinTargetCommunitiesType targets)
    {
        log.debug("Begin processPatientDiscoveryAsyncReq");
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        try
        {
                String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PATIENT_DISCOVERY_ENTITY_SECURED_ASYNC_REQ_SERVICE_NAME);
                EntityPatientDiscoverySecuredAsyncReqPortType port = getPort(url, NhincConstants.PATIENT_DISCOVERY_ACTION, WS_ADDRESSING_ACTION, assertion);

                if (port != null)
                {
                    RespondingGatewayPRPAIN201305UV02SecuredRequestType securedRequest = new RespondingGatewayPRPAIN201305UV02SecuredRequestType();
                    securedRequest.setNhinTargetCommunities(targets);
                    securedRequest.setPRPAIN201305UV02(message);

                    response = (MCCIIN000002UV01) oProxyHelper.invokePort(port, EntityPatientDiscoverySecuredAsyncReqPortType.class, "processPatientDiscoveryAsyncReq", securedRequest);
                } else
                {
                    log.error("EntityPatientDiscoverySecuredAsyncReqPortType is null");
                }
        } catch (Exception ex)
        {
            log.error("Error calling processPatientDiscoveryAsyncReq: " + ex.getMessage(), ex);
        }

        log.debug("End processPatientDiscoveryAsyncReq");
        return response;
    }
}
