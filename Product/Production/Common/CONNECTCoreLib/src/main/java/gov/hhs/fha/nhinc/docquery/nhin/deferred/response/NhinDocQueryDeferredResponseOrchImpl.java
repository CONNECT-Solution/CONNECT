/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.nhin.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryResponseType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.DocQueryPolicyChecker;
import gov.hhs.fha.nhinc.docquery.adapter.deferred.response.proxy.AdapterDocQueryDeferredResponseProxy;
import gov.hhs.fha.nhinc.docquery.adapter.deferred.response.proxy.AdapterDocQueryDeferredResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.document.DocQueryAckTranforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class NhinDocQueryDeferredResponseOrchImpl {

    private static Log log = LogFactory.getLog(NhinDocQueryDeferredResponseOrchImpl.class);

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    /**
     *
     * @param msg
     * @param assertion
     * @return <code>DocQueryAcknowledgementType</code>
     */
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryResponse msg, AssertionType assertion) {
        log.debug("Begin - .NhinDocQueryDeferredResponseOrchImplrespondingGatewayCrossGatewayQuery");

        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();

        DocQueryAcknowledgementType respAck = new DocQueryAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_STATUS_MSG);
        respAck.setMessage(regResp);
        String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();
        String responseCommunityId = HomeCommunityMap.getCommunitIdForDeferredQDResponse(msg);
        String ackMsg = "";

        // Audit the incoming NHIN Message
        AcknowledgementType ack = auditResponse(msg, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, responseCommunityId);

        // ASYNCMSG PROCESSING - RSPRCVD
        RespondingGatewayCrossGatewayQueryResponseType nhinResponse = new RespondingGatewayCrossGatewayQueryResponseType();
        nhinResponse.setAdhocQueryResponse(msg);
        nhinResponse.setAssertion(assertion);

        // Use messageId as WebServiceHelper has moved the RelatesToList.get(0) to MessageId already
        String messageId = "";
        if (assertion.getMessageId() != null) {
            messageId = assertion.getMessageId();
        }

        boolean bIsQueueOk = asyncProcess.processQueryForDocumentsResponse(messageId, AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVD, AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVDERR, nhinResponse);

        // Check if the service is enabled
        if (isServiceEnabled()) {
            // Check if in Pass-Through Mode
            if (!(isInPassThroughMode())) {
                // Perform the inbound policy check
                if (isPolicyValid(msg, assertion)) {
                    respAck = sendToAgency(msg, assertion, homeCommunityId);
                } else {
                    // Policy Check Failed for incoming Document Query Deferred Response
                    ackMsg = "Policy Check Failed for incoming Document Query Deferred Response";
                    log.warn(ackMsg);

                    // Set the error acknowledgement status
                    respAck = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_AUTHORIZATION, ackMsg);
                }
            } else {
                // Send the deferred response to the Adapter Interface
                respAck = sendToAgency(msg, assertion, homeCommunityId);
            }
        } else {
            // Service is not enabled so we are not doing anything with this response
            ackMsg = "Document Query Deferred Response Service Not Enabled";
            log.warn(ackMsg);

            // Set the error acknowledgement status
            respAck = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_INVALID, ackMsg);
        }

        // ASYNCMSG PROCESSING - RSPRCVDACK
        bIsQueueOk = asyncProcess.processAck(messageId, AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVDACK, AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVDERR, respAck);

        // Audit the outgoing NHIN Message
        ack = auditAck(respAck, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, responseCommunityId);

        log.debug("End - NhinDocQueryDeferredResponseOrchImpl.respondingGatewayCrossGatewayQuery");

        return respAck;
    }

    private boolean isServiceEnabled() {
        boolean serviceEnabled = false;

        try {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_NAME);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_NAME + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        return serviceEnabled;
    }

    private boolean isInPassThroughMode() {
        boolean passThroughModeEnabled = false;
        try {
            passThroughModeEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_PASSTHRU_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_PASSTHRU_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return passThroughModeEnabled;
    }

    private DocQueryAcknowledgementType sendToAgency(AdhocQueryResponse request, AssertionType assertion, String responseHomeCommunityId) {
        log.debug("Sending Response to Adapter Interface");

        // Audit the Adapter Response Message
        AcknowledgementType ack = auditResponse(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, responseHomeCommunityId);

        AdapterDocQueryDeferredResponseProxyObjectFactory factory = new AdapterDocQueryDeferredResponseProxyObjectFactory();
        AdapterDocQueryDeferredResponseProxy proxy = factory.getAdapterDocQueryDeferredResponseProxy();

        DocQueryAcknowledgementType ackResp = proxy.respondingGatewayCrossGatewayQuery(request, assertion);

        // Audit the incoming Adapter Message
        ack = auditAck(ackResp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, responseHomeCommunityId);

        return ackResp;
    }

    private AcknowledgementType auditResponse(AdhocQueryResponse msg, AssertionType assertion, String direction, String _interface, String responseHomeCommunityId) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.auditDQResponse(msg, assertion, direction, _interface, responseHomeCommunityId);

        return ack;
    }

    private AcknowledgementType auditAck(DocQueryAcknowledgementType msg, AssertionType assertion, String direction, String _interface, String responseHomeCommunityId) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.logDocQueryAck(msg, assertion, direction, _interface, responseHomeCommunityId);

        return ack;
    }

    private boolean isPolicyValid(AdhocQueryResponse message, AssertionType assertion) {
        boolean policyIsValid = new DocQueryPolicyChecker().checkIncomingResponsePolicy(message, assertion);

        return policyIsValid;
    }
}
