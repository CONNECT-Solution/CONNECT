/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveDeferredRequestSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqProxyWebServiceSecuredImpl implements EntityDocRetrieveDeferredReqProxy {

    private Log log = null;
    private boolean enableDebug = false;
    private WebServiceProxyHelper oProxyHelper = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entitydocretrievesecured";
    private static final String SERVICE_LOCAL_PART = "EntityDocRetrieveDeferredRequestSecured";
    private static final String PORT_LOCAL_PART = "EntityDocRetrieveDeferredRequestSecuredPortSoap";
    private static final String WSDL_FILE = "EntityDocRetrieveDeferredReqSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:entitydocretrievesecured:CrossGatewayRetrieveRequestMessage";

    /**
     * default constructor
     */
    public EntityDocRetrieveDeferredReqProxyWebServiceSecuredImpl() {
        log = createLogger();
        enableDebug = log.isDebugEnabled();
        oProxyHelper = createWebServiceProxyHelper();
    }

    /**
     * 
     * @return WebServiceProxyHelper
     */
    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * create logger instance
     * @return Log
     */
    protected Log createLogger() {
        return log = (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     *
     * @param message
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType message, AssertionType assertion, NhinTargetCommunitiesType target) {
        if (enableDebug) {
            log.debug("Begin EntityDocRetrieveDeferredReqSecuredWebServiceImpl.crossGatewayRetrieveRequest ");
        }
        DocRetrieveAcknowledgementType ack = null;
        String url = null;

        try {
            url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ENTITY_DOCRETRIEVE_DEFERRED_SECURED_REQUEST);
            EntityDocRetrieveDeferredRequestSecuredPortType port = getPort(url, NhincConstants.DOCRETRIEVE_DEFERRED_ACTION, WS_ADDRESSING_ACTION, assertion);
            if (message == null) {
                log.error("Message was null");
            } else if (assertion == null) {
                log.error("assertion was null");
            } else if (target == null) {
                log.error("targets was null");
            } else if (port == null) {
                log.error("port was null");
            } else {
                RespondingGatewayCrossGatewayRetrieveSecuredRequestType request = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
                request.setNhinTargetCommunities(target);
                request.setRetrieveDocumentSetRequest(message);
                ack = (DocRetrieveAcknowledgementType) oProxyHelper.invokePort(port, EntityDocRetrieveDeferredRequestSecuredPortType.class, "crossGatewayRetrieveRequest", request);
            }

        } catch (Exception e) {
            log.error("Error: Failed to retrieve url for service: '" + NhincConstants.ENTITY_DOCRETRIEVE_DEFERRED_SECURED_REQUEST + "' ");
            log.error(e.getMessage());
            ack = new DocRetrieveAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG);
            ack.setMessage(regResp);
        }
        if (enableDebug) {
            log.debug("End EntityDocRetrieveDeferredReqSecuredWebServiceImpl.crossGatewayRetrieveRequest ");
        }
        return ack;
    }

    /**
     * 
     * @param url
     * @param serviceAction
     * @param wsAddressingAction
     * @param assertion
     * @return EntityDocRetrieveDeferredRequestSecuredPortType
     */
    protected EntityDocRetrieveDeferredRequestSecuredPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        EntityDocRetrieveDeferredRequestSecuredPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityDocRetrieveDeferredRequestSecuredPortType.class);
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
