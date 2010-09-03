/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrieveDeferredRequestPortType;
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
 * Entity Doc Retrieve Deferred Request unsecured webservice implementation call
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqProxyWebServiceUnsecuredImpl implements EntityDocRetrieveDeferredReqProxy {

    private Log log = null;
    private boolean enableDebug = false;
    private WebServiceProxyHelper oProxyHelper = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entitydocretrieve";
    private static final String SERVICE_LOCAL_PART = "EntityDocRetrieveDeferredRequest";
    private static final String PORT_LOCAL_PART = "EntityDocRetrieveDeferredRequestPortSoap";
    private static final String WSDL_FILE = "EntityDocRetrieveDeferredReq.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:entitydocretrieve:CrossGatewayRetrieveRequestMessage";

    /**
     * default constructor
     */
    public EntityDocRetrieveDeferredReqProxyWebServiceUnsecuredImpl() {
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
     * Creating logger instance
     * @return Log
     */
    protected Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
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
            log.debug("Begin unsecure implementation of Entity Doc retrieve Request unsecured");
        }
        DocRetrieveAcknowledgementType ack = null;
        String url = null;
        try {
            url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ENTITY_DOCRETRIEVE_DEFERRED_UNSECURED_REQUEST);
            EntityDocRetrieveDeferredRequestPortType port = getPort(url, NhincConstants.DOCRETRIEVE_DEFERRED_ACTION, WS_ADDRESSING_ACTION, assertion);
            if (message == null) {
                log.error("Message was null");
            } else if (assertion == null) {
                log.error("assertion was null");
            } else if (target == null) {
                log.error("targets was null");
            } else if (port == null) {
                log.error("port was null");
            } else {
                RespondingGatewayCrossGatewayRetrieveRequestType request = new RespondingGatewayCrossGatewayRetrieveRequestType();
                request.setAssertion(assertion);
                request.setNhinTargetCommunities(target);
                request.setRetrieveDocumentSetRequest(message);
                ack = (DocRetrieveAcknowledgementType) oProxyHelper.invokePort(port, EntityDocRetrieveDeferredRequestPortType.class, "crossGatewayRetrieveRequest", request);
            }
        } catch (Exception e) {
            log.error("Unable to retrieve endpoint for service name '" + NhincConstants.ENTITY_DOCRETRIEVE_DEFERRED_UNSECURED_REQUEST + "' " + e.getMessage());
            ack = new DocRetrieveAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG);
            ack.setMessage(regResp);
        }
        if (enableDebug) {
            log.debug("End unsecure implementation of Entity Doc retrieve Request unsecured");
        }
        return ack;
    }

    /**
     * 
     * @param url
     * @param serviceAction
     * @param wsAddressingAction
     * @param assertion
     * @return EntityDocRetrieveDeferredRequestPortType
     */
    protected EntityDocRetrieveDeferredRequestPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        EntityDocRetrieveDeferredRequestPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityDocRetrieveDeferredRequestPortType.class);
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
}
