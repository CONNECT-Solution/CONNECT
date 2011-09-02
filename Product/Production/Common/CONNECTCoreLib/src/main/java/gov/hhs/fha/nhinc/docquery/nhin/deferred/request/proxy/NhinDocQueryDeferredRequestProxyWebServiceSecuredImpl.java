/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.nhin.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.document.DocQueryAckTranforms;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import ihe.iti.xds_b._2007.RespondingGatewayQueryDeferredRequestPortType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class NhinDocQueryDeferredRequestProxyWebServiceSecuredImpl implements NhinDocQueryDeferredRequestProxy {

    //Logger
    private static final Log log = LogFactory.getLog(NhinDocQueryDeferredRequestProxyWebServiceSecuredImpl.class);
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    private static final String SERVICE_LOCAL_PART = "RespondingGateway_QueryDeferredRequest_Service";
    private static final String PORT_LOCAL_PART = "RespondingGateway_QueryDeferredRequest_Port_Soap";
    private static final String WSDL_FILE = "NhinDocQueryDeferredRequest.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:2007:CrossGatewayQuery";
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    protected WebServiceProxyHelper getWebServiceProxyHelper() {
        return oProxyHelper;
    }

    protected DocQueryAuditLog getDocQueryAuditLogger() {
        return new DocQueryAuditLog();
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected RespondingGatewayQueryDeferredRequestPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        RespondingGatewayQueryDeferredRequestPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayQueryDeferredRequestPortType.class);
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

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion, NhinTargetSystemType target) {
        log.debug("Begin respondingGatewayCrossGatewayQuery");

        String url = null;
        String ackMessage = null;
        DocQueryAcknowledgementType response = null;

        String responseCommunityID = null;
        if (target != null &&
                target.getHomeCommunity() != null &&
                target.getHomeCommunity().getHomeCommunityId() != null) {
            responseCommunityID = target.getHomeCommunity().getHomeCommunityId();
        }
        // Log the outbound request -- Audit Logging
        AcknowledgementType ack = getDocQueryAuditLogger().auditDQRequest(msg, assertion,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, responseCommunityID);

        try {
            if (msg != null) {
                log.debug("Before target system URL look up.");
                url = getWebServiceProxyHelper().getUrlFromTargetSystem(target, NhincConstants.NHIN_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME);
                log.debug("After target system URL look up. URL for service: " + NhincConstants.NHIN_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME + " is: " + url);

                if (NullChecker.isNotNullish(url)) {                   
                    RespondingGatewayQueryDeferredRequestPortType port = getPort(url, NhincConstants.DOC_QUERY_ACTION, WS_ADDRESSING_ACTION, assertion);
                    response = (DocQueryAcknowledgementType) getWebServiceProxyHelper().invokePort(port, RespondingGatewayQueryDeferredRequestPortType.class, "respondingGatewayCrossGatewayQuery", msg);

                } else {
                    ackMessage = "Failed to call the web service (" + NhincConstants.NHIN_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME + ").  The URL is null.";
                    response = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_INVALID, ackMessage);

                    log.error(ackMessage);
                }
            } else {
                ackMessage = "Failed to call the web service (" + NhincConstants.NHIN_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME + ").  The input parameter is null.";
                response = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_INVALID, ackMessage);
                log.error(ackMessage);
            }
        } catch (Exception e) {
            ackMessage = "Failed to call the web service (" + NhincConstants.NHIN_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME + ").  An unexpected exception occurred: " + e.getMessage();
            response = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_INVALID, ackMessage);

            log.error(ackMessage + "  Exception: " + e.getMessage(), e);
        }

        // Log the inbound acknowledgement response -- Audit Logging
        ack = getDocQueryAuditLogger().logDocQueryAck(response, assertion,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, responseCommunityID);

        log.debug("End respondingGatewayCrossGatewayQuery");

        return response;
    }

}
