/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.nhin.deferred.request.proxy;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.document.DocRetrieveAckTranforms;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RespondingGatewayDeferredRequestRetrievePortType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * Send deferred retrieve document request over the secured NHIN
 *
 * @author richard.ettema
 */
public class NhinDocRetrieveDeferredReqProxyWebServiceSecuredImpl implements NhinDocRetrieveDeferredReqProxy {

    private static final Log log = LogFactory.getLog(NhinDocRetrieveDeferredReqProxyWebServiceSecuredImpl.class);
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    private static final String SERVICE_LOCAL_PART = "RespondingGatewayDeferredRequest_Retrieve_Service";
    private static final String PORT_LOCAL_PART = "RespondingGatewayDeferredRequest_Retrieve_Port_Soap";
    private static final String WSDL_FILE = "NhinDocRetrieveDeferredReq.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:xds-b:2007:Deferred:CrossGatewayRetrieve_Message";
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    protected WebServiceProxyHelper getWebServiceProxyHelper() {
        return oProxyHelper;
    }

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    /**
     *
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected RespondingGatewayDeferredRequestRetrievePortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        RespondingGatewayDeferredRequestRetrievePortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayDeferredRequestRetrievePortType.class);
            getWebServiceProxyHelper().initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
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

    public DocRetrieveAcknowledgementType sendToRespondingGateway(RetrieveDocumentSetRequestType body, AssertionType assertion, NhinTargetSystemType target) {
        log.debug("Begin respondingGatewayCrossGatewayQuery");

        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();

        RetrieveDocumentSetRequestType retrieveResponseTypeCopy = asyncProcess.copyRetrieveDocumentSetRequestTypeObject(body);

        String url = null;
        String ackMessage = null;
        DocRetrieveAcknowledgementType response = null;

        // Audit nhin outbound deferred retrieve document request message
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        String responseCommunityId = target.getHomeCommunity().getHomeCommunityId();
        auditLog.auditDocRetrieveDeferredRequest(retrieveResponseTypeCopy, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, assertion, responseCommunityId);

        try {
            if (body != null) {
                log.debug("Before target system URL look up.");
                url = getWebServiceProxyHelper().getUrlFromTargetSystem(target, NhincConstants.NHIN_DOCRETRIEVE_DEFERRED_REQUEST);
                log.debug("After target system URL look up. URL for service: " + NhincConstants.NHIN_DOCRETRIEVE_DEFERRED_REQUEST + " is: " + url);

                if (NullChecker.isNotNullish(url)) {
                    RespondingGatewayDeferredRequestRetrievePortType port = getPort(url, NhincConstants.DOC_RETRIEVE_ACTION, WS_ADDRESSING_ACTION, assertion);
                    response = (DocRetrieveAcknowledgementType) getWebServiceProxyHelper().invokePort(port, RespondingGatewayDeferredRequestRetrievePortType.class, "respondingGatewayDeferredRequestCrossGatewayRetrieve", body);
                } else {
                    ackMessage = "Failed to call the web service (" + NhincConstants.NHIN_DOCRETRIEVE_DEFERRED_REQUEST + ").  The URL is null.";
                    response = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_RETRIEVE_DEFERRED_ACK_ERROR_INVALID, ackMessage);

                    log.error(ackMessage);
                }
            } else {
                ackMessage = "Failed to call the web service (" + NhincConstants.NHIN_DOCRETRIEVE_DEFERRED_REQUEST + ").  The input parameter is null.";
                response = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_RETRIEVE_DEFERRED_ACK_ERROR_INVALID, ackMessage);

                log.error(ackMessage);
            }
        } catch (Exception e) {
            ackMessage = "Failed to call the web service (" + NhincConstants.NHIN_DOCRETRIEVE_DEFERRED_REQUEST + ").  An unexpected exception occurred: " + e.getMessage();
            response = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_RETRIEVE_DEFERRED_ACK_ERROR_INVALID, ackMessage);

            log.error(ackMessage + "  Exception: " + e.getMessage(), e);
        }

        // Log the inbound acknowledgement response -- Audit Logging
        auditLog.auditDocRetrieveDeferredAckResponse(response.getMessage(), retrieveResponseTypeCopy, null, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, responseCommunityId);

        log.debug("End respondingGatewayCrossGatewayQuery");

        return response;
    }
}
