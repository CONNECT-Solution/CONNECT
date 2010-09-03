/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveDeferredResponseSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredRespProxyWebServiceSecuredImpl implements EntityDocRetrieveDeferredRespProxy {

    private Log log = null;
    private boolean debugEnabled = false;
    private WebServiceProxyHelper oProxyHelper = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entitydocretrievesecured";
    private static final String SERVICE_LOCAL_PART = "EntityDocRetrieveDeferredResponseSecured";
    private static final String PORT_LOCAL_PART = "EntityDocRetrieveDeferredResponseSecuredPortSoap";
    private static final String WSDL_FILE = "EntityDocRetrieveDeferredRespSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:entitydocretrievesecured:CrossGatewayRetrieveResponseMessage";

    /**
     * default constructor
     */
    public EntityDocRetrieveDeferredRespProxyWebServiceSecuredImpl() {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
        oProxyHelper = createWebServiceProxyHelper();
    }

    /**
     *
     * @return Log
     */
    private Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     *
     * @return WebServiceProxyHelper
     */
    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     *
     * @param response
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetResponseType response, AssertionType assertion, NhinTargetCommunitiesType target) {
        if (debugEnabled) {
            log.debug("-- Begin EntityDocRetrieveDeferredRespSecuredWebServiceImpl.crossGatewayRetrieveResponse(_... ) --");
        }
        DocRetrieveAcknowledgementType ack = null;
        String url = null;
        try {
            url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ENTITY_DOCRETRIEVE_DEFERRED_SECURED_RESPONSE);
            EntityDocRetrieveDeferredResponseSecuredPortType port = getPort(url, NhincConstants.DOCRETRIEVE_DEFERRED_ACTION, WS_ADDRESSING_ACTION, assertion);
            if (response == null) {
                log.error("Message was null");
            } else if (assertion == null) {
                log.error("assertion was null");
            } else if (target == null) {
                log.error("targets was null");
            } else if (port == null) {
                log.error("port was null");
            } else {
                RespondingGatewayCrossGatewayRetrieveSecuredResponseType resp = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
                resp.setNhinTargetCommunities(target);
                resp.setRetrieveDocumentSetResponse(response);
                ack = (DocRetrieveAcknowledgementType) oProxyHelper.invokePort(port, EntityDocRetrieveDeferredResponseSecuredPortType.class, "crossGatewayRetrieveResponse", resp);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        if (debugEnabled) {
            log.debug("-- End EntityDocRetrieveDeferredRespSecuredWebServiceImpl.crossGatewayRetrieveResponse(_... ) --");
        }
        return ack;
    }

    /**
     * 
     * @param url
     * @param serviceAction
     * @param wsAddressingAction
     * @param assertion
     * @return EntityDocRetrieveDeferredResponseSecuredPortType
     */
    private EntityDocRetrieveDeferredResponseSecuredPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        EntityDocRetrieveDeferredResponseSecuredPortType port = null;

        Service service = getService();
        if (service != null) {
            if (debugEnabled) {
                log.debug("Obtained service - creating port.");
            }
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityDocRetrieveDeferredResponseSecuredPortType.class);
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
}
