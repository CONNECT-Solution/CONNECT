/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docquery.passthru.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryResponseType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.nhin.deferred.response.proxy.NhinDocQueryDeferredResponseProxy;
import gov.hhs.fha.nhinc.docquery.nhin.deferred.response.proxy.NhinDocQueryDeferredResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.document.DocQueryAckTranforms;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class PassthruDocQueryDeferredResponseOrchImpl {

    private static Log log = LogFactory.getLog(PassthruDocQueryDeferredResponseOrchImpl.class);

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    /**
     *
     * @param body
     * @param assertion
     * @param target
     * @return <code>DocQueryAcknowledgementType</code>
     */
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryResponse body, AssertionType assertion, NhinTargetSystemType target) {
        log.debug("Begin - respondingGatewayCrossGatewayQuery");

        DocQueryAcknowledgementType respAck = new DocQueryAcknowledgementType();

        // Audit the Query For Documents Response Message sent on the Nhin Interface
        DocQueryAuditLog auditLog = new DocQueryAuditLog();

        // The responding home community id is required in the audit log
        String responseCommunityID = null;
        if (assertion != null &&
                assertion.getUserInfo() != null &&
                assertion.getUserInfo().getOrg() != null) {
            responseCommunityID = assertion.getUserInfo().getOrg().getHomeCommunityId();
            log.debug("=====>>>>> PassthruDocQueryDeferredResponseOrchImpl --> responseCommunityID is [" + responseCommunityID + "]");
        }
        // Audit the outgoing NHIN Message
        AcknowledgementType ack = auditLog.auditDQResponse(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, responseCommunityID);

        // Call the NHIN Interface
        NhinDocQueryDeferredResponseProxyObjectFactory factory = new NhinDocQueryDeferredResponseProxyObjectFactory();
        NhinDocQueryDeferredResponseProxy proxy = factory.getNhinDocQueryDeferredResponseProxy();

        // ASYNCMSG PROCESSING - RSPSENT
        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();

        RespondingGatewayCrossGatewayQueryResponseType respondingGatewayCrossGatewayQueryResponseType = new RespondingGatewayCrossGatewayQueryResponseType();
        respondingGatewayCrossGatewayQueryResponseType.setAdhocQueryResponse(body);
        respondingGatewayCrossGatewayQueryResponseType.setAssertion(assertion);
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        targetCommunity.setHomeCommunity(target.getHomeCommunity());
        targets.getNhinTargetCommunity().add(targetCommunity);
        respondingGatewayCrossGatewayQueryResponseType.setNhinTargetCommunities(targets);

        String messageId = "";
        if (assertion.getRelatesToList() != null && assertion.getRelatesToList().size() > 0) {
            messageId = assertion.getRelatesToList().get(0);
        }

        boolean bIsQueueOk = asyncProcess.processQueryForDocumentsResponse(messageId, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENT, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTERR, respondingGatewayCrossGatewayQueryResponseType);

        // check for valid queue update
        if (bIsQueueOk) {
            respAck = proxy.respondingGatewayCrossGatewayQuery(body, assertion, target);
        } else {
            String ackMsg = "Deferred Query For Documents request processing halted; deferred queue repository error encountered";
            log.error(ackMsg);

            // Set the error acknowledgement status
            // fatal error with deferred queue repository
            respAck = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_INVALID, ackMsg);
        }

        // ASYNCMSG PROCESSING - REQSENTACK
        bIsQueueOk = asyncProcess.processAck(messageId, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTACK, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTERR, respAck);

        // Audit the incoming NHIN Acknowledgement Message
        ack = auditLog.logDocQueryAck(respAck, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, responseCommunityID);

        log.debug("End - respondingGatewayCrossGatewayQuery");

        return respAck;
    }

}
