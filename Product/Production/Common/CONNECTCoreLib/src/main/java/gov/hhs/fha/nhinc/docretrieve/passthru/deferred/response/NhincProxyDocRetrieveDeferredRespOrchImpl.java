/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.nhin.deferred.response.proxy.NhinDocRetrieveDeferredRespProxy;
import gov.hhs.fha.nhinc.docretrieve.nhin.deferred.response.proxy.NhinDocRetrieveDeferredRespProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.document.DocRetrieveAckTranforms;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredRespOrchImpl {

    private static final Log log = LogFactory.getLog(NhincProxyDocRetrieveDeferredRespOrchImpl.class);

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    /**
     *
     * @param retrieveDocumentSetResponse
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetRequestType request, RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetSystemType target) {
        log.debug("Begin NhincProxyDocRetrieveDeferredRespOrchImpl.processCrossGatewayRetrieveResponse(...)");

        DocRetrieveAcknowledgementType respAck = new DocRetrieveAcknowledgementType();
        String ackMsg = "";

        String responseCommunityId = target.getHomeCommunity().getHomeCommunityId();

        // Audit request message
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        auditLog.auditDocRetrieveDeferredResponse(retrieveDocumentSetResponse, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, assertion, responseCommunityId);

        // ASYNCMSG PROCESSING - RSPSENT
        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();

        // Call the NHIN Interface
        NhinDocRetrieveDeferredRespProxyObjectFactory objFactory = new NhinDocRetrieveDeferredRespProxyObjectFactory();
        NhinDocRetrieveDeferredRespProxy docRetrieveProxy = objFactory.getNhinDocRetrieveDeferredResponseProxy();

        RespondingGatewayCrossGatewayRetrieveResponseType respondingGatewayCrossGatewayRetrieveResponseType = new RespondingGatewayCrossGatewayRetrieveResponseType();
        respondingGatewayCrossGatewayRetrieveResponseType.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);
        respondingGatewayCrossGatewayRetrieveResponseType.setAssertion(assertion);
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        targetCommunity.setHomeCommunity(target.getHomeCommunity());
        targets.getNhinTargetCommunity().add(targetCommunity);
        respondingGatewayCrossGatewayRetrieveResponseType.setNhinTargetCommunities(targets);

        String messageId = "";
        if (assertion.getRelatesToList() != null && assertion.getRelatesToList().size() > 0) {
            messageId = assertion.getRelatesToList().get(0);
        }

        boolean bIsQueueOk = asyncProcess.processRetrieveDocumentsResponse(messageId, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENT, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTERR, respondingGatewayCrossGatewayRetrieveResponseType);

        // check for valid queue update
        if (bIsQueueOk) {
            respAck = docRetrieveProxy.sendToRespondingGateway(retrieveDocumentSetResponse, assertion, target);
        } else {
            ackMsg = "Deferred Retrieve Documents response processing halted; deferred queue repository error encountered";
            log.error(ackMsg);

            // Set the error acknowledgement status
            // fatal error with deferred queue repository
            respAck = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_RETRIEVE_DEFERRED_ACK_ERROR_INVALID, ackMsg);
        }

        // ASYNCMSG PROCESSING - REQSENTACK
        bIsQueueOk = asyncProcess.processAck(messageId, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTACK, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTERR, respAck);

        // Audit response message
        auditLog.auditDocRetrieveDeferredAckResponse(respAck.getMessage(), request, retrieveDocumentSetResponse, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, responseCommunityId);

        log.debug("End NhincProxyDocRetrieveDeferredRespOrchImpl.processCrossGatewayRetrieveResponse(...)");

        return respAck;
    }

}
