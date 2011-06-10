/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docquery.entity.deferred.request.queue;

import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.DocQueryPolicyChecker;
import gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxyJavaImpl;
import gov.hhs.fha.nhinc.docquery.passthru.deferred.response.proxy.PassthruDocQueryDeferredResponseProxy;
import gov.hhs.fha.nhinc.docquery.passthru.deferred.response.proxy.PassthruDocQueryDeferredResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.document.DocQueryAckTranforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author narendra.reddy
 */
public class EntityDocQueryDeferredReqQueueOrchImpl {

    private static final Log log = LogFactory.getLog(EntityDocQueryDeferredReqQueueOrchImpl.class);

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    /**
     *
     * @param msg
     * @param assertion
     * @param targets
     * @return DocQueryAcknowledgementType
     */
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion, NhinTargetCommunitiesType targets) {
        log.debug("Begin EntityDocQueryDeferredReqQueueOrchImpl.respondingGatewayCrossGatewayQuery");

        DocQueryAcknowledgementType respAck = new DocQueryAcknowledgementType();
        String ackMsg = "";

        RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequestType = new RespondingGatewayCrossGatewayQueryRequestType();
        respondingGatewayCrossGatewayQueryRequestType.setAdhocQueryRequest(msg);
        respondingGatewayCrossGatewayQueryRequestType.setAssertion(assertion);

        String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();

        // Audit the incoming doc query request Message
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.auditDQRequest(msg, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, homeCommunityId);

        // ASYNCMSG PROCESSING - RSPPROCESS
        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();
        String messageId = assertion.getMessageId();

        // Generate a new response assertion
        AssertionType responseAssertion = asyncProcess.copyAssertionTypeObject(assertion);
        // Original request message id is now set as the relates to id
        responseAssertion.getRelatesToList().add(messageId);
        // Generate a new unique response assertion Message ID
        responseAssertion.setMessageId(AsyncMessageIdCreator.generateMessageId());
        // Set user info homeCommunity
        HomeCommunityType homeCommunityType = new HomeCommunityType();
        homeCommunityType.setHomeCommunityId(homeCommunityId);
        homeCommunityType.setName(homeCommunityId);
        responseAssertion.setHomeCommunity(homeCommunityType);

        boolean bIsQueueOk = asyncProcess.processMessageStatus(messageId, AsyncMsgRecordDao.QUEUE_STATUS_RSPPROCESS);

        if (bIsQueueOk) {
            try {
                CMUrlInfos urlInfoList = getEndpoints(targets);

                if (urlInfoList != null &&
                        NullChecker.isNotNullish(urlInfoList.getUrlInfo()) &&
                        urlInfoList.getUrlInfo().get(0) != null &&
                        NullChecker.isNotNullish(urlInfoList.getUrlInfo().get(0).getHcid()) &&
                        NullChecker.isNotNullish(urlInfoList.getUrlInfo().get(0).getUrl())) {

                    HomeCommunityType targetHcid = new HomeCommunityType();
                    targetHcid.setHomeCommunityId(urlInfoList.getUrlInfo().get(0).getHcid());

                    if (isPolicyValid(msg, assertion, targetHcid)) {
                        NhinTargetSystemType target = new NhinTargetSystemType();
                        target.setUrl(urlInfoList.getUrlInfo().get(0).getUrl());

                        // Audit the Adhoc Query Request Message sent to the Adapter Interface
                        ack = auditAdhocQueryRequest(respondingGatewayCrossGatewayQueryRequestType, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, homeCommunityId);

                        // Get the AdhocQueryResponse by passing the request to this agency's adapter doc query service
                        AdapterDocQueryProxyJavaImpl orchImpl = new AdapterDocQueryProxyJavaImpl();
                        AdhocQueryResponse response = orchImpl.respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequestType.getAdhocQueryRequest(), responseAssertion);

                        // Audit the Adhoc Query Response Message sent to the Adapter Interface
                        ack = auditAdhocQueryResponse(response, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, assertion, homeCommunityId);

                        PassthruDocQueryDeferredResponseProxyObjectFactory factory = new PassthruDocQueryDeferredResponseProxyObjectFactory();
                        PassthruDocQueryDeferredResponseProxy proxy = factory.getPassthruDocQueryDeferredResponseProxy();

                        respAck = proxy.respondingGatewayCrossGatewayQuery(response, responseAssertion, target);
                    } else {
                        ackMsg = "Outgoing Policy Check Failed";
                        log.error(ackMsg);
                        respAck = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_AUTHORIZATION, ackMsg);
                        asyncProcess.processAck(messageId, AsyncMsgRecordDao.QUEUE_STATUS_REQSENTERR, AsyncMsgRecordDao.QUEUE_STATUS_REQSENTERR, respAck);
                    }
                } else {
                    ackMsg = "Failed to obtain target URL from connection manager";
                    log.error(ackMsg);
                    respAck = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_INVALID, ackMsg);
                    asyncProcess.processAck(messageId, AsyncMsgRecordDao.QUEUE_STATUS_REQSENTERR, AsyncMsgRecordDao.QUEUE_STATUS_REQSENTERR, respAck);
                }
            } catch (Exception e) {
                ackMsg = "Exception processing Deferred Query For Documents: " + e.getMessage();
                log.error(ackMsg, e);
                respAck = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_INVALID, ackMsg);
                asyncProcess.processAck(messageId, AsyncMsgRecordDao.QUEUE_STATUS_REQSENTERR, AsyncMsgRecordDao.QUEUE_STATUS_REQSENTERR, respAck);
            }
        } else {
            ackMsg = "Deferred Query For Documents response processing halted; deferred queue repository error encountered";

            // Set the error acknowledgement status
            // fatal error with deferred queue repository
            respAck = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_INVALID, ackMsg);
        }

        // Audit the responding Acknowledgement Message
        ack = auditLogger.logDocQueryAck(respAck, responseAssertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, homeCommunityId);

        return respAck;
    }

    /**
     *
     * @param message
     * @param assertion
     * @param target
     * @return boolean
     */
    private boolean isPolicyValid(AdhocQueryRequest message, AssertionType assertion, HomeCommunityType target) {
        boolean policyIsValid = new DocQueryPolicyChecker().checkOutgoingRequestPolicy(message, assertion, target);

        return policyIsValid;
    }

    /**
     *
     * @param targetCommunities
     * @return CMUrlInfos
     */
    private CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities) {
        CMUrlInfos urlInfoList = null;

        try {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.NHIN_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs", ex);
        }

        return urlInfoList;
    }

    /**
     * Creates an audit log for an AdhocQueryRequest.
     * @param crossGatewayDocQueryRequest AdhocQueryRequest message to log
     * @param direction Indicates whether the message is going out or comming in
     * @param _interface Indicates which interface component is being logged??
     * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
     */
    private AcknowledgementType auditAdhocQueryRequest(RespondingGatewayCrossGatewayQueryRequestType msg, String direction, String _interface, String requestCommunityID) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.auditDQRequest(msg.getAdhocQueryRequest(), msg.getAssertion(), direction, _interface, requestCommunityID);

        return ack;
    }

    /**
     * Creates an audit log for an AdhocQueryResponse.
     * @param crossGatewayDocQueryResponse AdhocQueryResponse message to log
     * @param direction Indicates whether the message is going out or comming in
     * @param _interface Indicates which interface component is being logged??
     * @param requestCommunityID
     * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
     */
    private AcknowledgementType auditAdhocQueryResponse(AdhocQueryResponse msg, String direction, String _interface, AssertionType assertion, String requestCommunityID) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.auditDQResponse(msg, assertion, direction, _interface, requestCommunityID);

        return ack;
    }

}
