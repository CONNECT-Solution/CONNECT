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

package gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.nhincentityxdr.async.request.EntityXDRAsyncRequestPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class EntityDocSubmissionDeferredRequestProxyWebServiceUnsecuredImpl implements EntityDocSubmissionDeferredRequestProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincentityxdr:async:request";
    private static final String SERVICE_LOCAL_PART = "EntityXDRAsyncRequest_Service";
    private static final String PORT_LOCAL_PART = "EntityXDRAsyncRequest_Port";
    private static final String WSDL_FILE = "EntityXDRRequest.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincentityxdr:async:request:ProvideAndRegisterDocumentSet-bAsyncRequest_RequestMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public EntityDocSubmissionDeferredRequestProxyWebServiceUnsecuredImpl() {
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
    protected EntityXDRAsyncRequestPortType getPort(String url, String wsAddressingAction, AssertionType assertion) {
        EntityXDRAsyncRequestPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityXDRAsyncRequestPortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
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

    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {
        log.debug("Begin provideAndRegisterDocumentSetBAsyncRequest");
        XDRAcknowledgementType response = null;

        try
        {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ENTITY_XDR_REQUEST_SERVICE_NAME);
            EntityXDRAsyncRequestPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);

            if(request == null)
            {
                log.error("Message was null");
            }
            else if (assertion == null)
            {
                log.error("assertion was null");
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
                RespondingGatewayProvideAndRegisterDocumentSetRequestType msg = new RespondingGatewayProvideAndRegisterDocumentSetRequestType();
                msg.setProvideAndRegisterDocumentSetRequest(request);
                msg.setAssertion(assertion);
                msg.setNhinTargetCommunities(targets);

                if (urlInfo != null) {
                    msg.setUrl(urlInfo);
                }

                response = (XDRAcknowledgementType)oProxyHelper.invokePort(port, EntityXDRAsyncRequestPortType.class, "provideAndRegisterDocumentSetBAsyncRequest", msg);
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling provideAndRegisterDocumentSetBAsyncRequest: " + ex.getMessage(), ex);
            response = new XDRAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);
            response.setMessage(regResp);
        }

        log.debug("End provideAndRegisterDocumentSetBAsyncRequest");
        return response;
    }

}
