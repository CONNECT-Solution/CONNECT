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

package gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.response.EntityXDRSecuredAsyncResponsePortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class EntityDocSubmissionDeferredResponseProxyWebServiceSecuredImpl implements EntityDocSubmissionDeferredResponseProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincentityxdrsecured:async:response";
    private static final String SERVICE_LOCAL_PART = "EntityXDRSecuredAsyncResponse_Service";
    private static final String PORT_LOCAL_PART = "EntityXDRSecuredAsyncResponse_Port";
    private static final String WSDL_FILE = "EntityXDRSecuredResponse.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincentityxdrsecured:async:response:ProvideAndRegisterDocumentSet-bAsyncResponse_Request";
    private WebServiceProxyHelper oProxyHelper = null;

    public EntityDocSubmissionDeferredResponseProxyWebServiceSecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected EntityXDRSecuredAsyncResponsePortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        EntityXDRSecuredAsyncResponsePortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityXDRSecuredAsyncResponsePortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }



    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(RegistryResponseType request, AssertionType assertion, NhinTargetCommunitiesType targets) {
        log.debug("Begin provideAndRegisterDocumentSetBAsyncResponse");
        XDRAcknowledgementType response = null;

        try
        {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ENTITY_XDR_RESPONSE_SECURED_SERVICE_NAME);
            EntityXDRSecuredAsyncResponsePortType port = getPort(url, NhincConstants.XDR_ACTION, WS_ADDRESSING_ACTION, assertion);

            if(request == null)
            {
                log.error("Message was null");
            }
            else if (targets == null)
            {
                log.error("targets was null");
            }
            else if(port == null)
            {
                log.error("port was null");
            }
            else
            {
                RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType msg = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
                msg.setRegistryResponse(request);
                msg.setNhinTargetCommunities(targets);

                response = (XDRAcknowledgementType)oProxyHelper.invokePort(port, EntityXDRSecuredAsyncResponsePortType.class, "provideAndRegisterDocumentSetBAsyncResponse", msg);
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling provideAndRegisterDocumentSetBAsyncResponse: " + ex.getMessage(), ex);
            response = new XDRAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);
            response.setMessage(regResp);
        }

        log.debug("End provideAndRegisterDocumentSetBAsyncResponse");
        return response;
    }
}
