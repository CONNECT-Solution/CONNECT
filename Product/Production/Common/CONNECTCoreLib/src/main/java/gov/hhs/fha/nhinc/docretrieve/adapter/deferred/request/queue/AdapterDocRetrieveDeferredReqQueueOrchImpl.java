/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.queue;

import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.docrepository.adapter.proxy.AdapterComponentDocRepositoryProxyJavaImpl;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredPolicyChecker;
import gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response.proxy.PassthruDocRetrieveDeferredRespProxyObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response.proxy.PassthruDocRetrieveDeferredRespProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.redactionengine.adapter.proxy.AdapterRedactionEngineProxyJavaImpl;
import gov.hhs.fha.nhinc.transform.document.DocRetrieveAckTranforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author narendra.reddy
 */
public class AdapterDocRetrieveDeferredReqQueueOrchImpl {

    private static final Log log = LogFactory.getLog(AdapterDocRetrieveDeferredReqQueueOrchImpl.class);

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    /**
     * Document Retrieve Deferred Response implementation method
     * @param response
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetCommunitiesType target) {
        log.debug("Begin AdapterDocRetrieveDeferredReqQueueOrchImpl.crossGatewayRetrieveResponse");

        DocRetrieveAcknowledgementType respAck = new DocRetrieveAcknowledgementType();
        String ackMsg = "";

        RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequestType = new RespondingGatewayCrossGatewayRetrieveRequestType();
        respondingGatewayCrossGatewayRetrieveRequestType.setRetrieveDocumentSetRequest(request);
        respondingGatewayCrossGatewayRetrieveRequestType.setAssertion(assertion);
        respondingGatewayCrossGatewayRetrieveRequestType.setNhinTargetCommunities(target);

        String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();
        String responseCommunityId = assertion.getHomeCommunity().getHomeCommunityId();

        // Audit the incoming doc retrieve request Message
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        auditLog.auditDocRetrieveDeferredRequest(request, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, assertion, homeCommunityId);

        // ASYNCMSG PROCESSING - RSPPROCESS
        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();
        String messageId = assertion.getMessageId();

        // Generate a new response assertion
        AssertionType responseAssertion = asyncProcess.copyAssertionTypeObject(assertion);
        // Original request message id is now set as the relates to id
        responseAssertion.getRelatesToList().add(messageId);
        // Generate a new unique response assertion Message ID
        responseAssertion.setMessageId(AsyncMessageIdCreator.generateMessageId());
        // Set the reponse assertions home community with the local gateway's community id
        HomeCommunityType homeCommunityType = new HomeCommunityType();
        homeCommunityType.setHomeCommunityId(homeCommunityId);
        homeCommunityType.setName(homeCommunityId);
        responseAssertion.setHomeCommunity(homeCommunityType);
        if (responseAssertion.getUserInfo() != null &&
                responseAssertion.getUserInfo().getOrg() != null) {
            responseAssertion.getUserInfo().getOrg().setHomeCommunityId(homeCommunityId);
            responseAssertion.getUserInfo().getOrg().setName(homeCommunityId);
        }

        boolean bIsQueueOk = asyncProcess.processMessageStatus(messageId, AsyncMsgRecordDao.QUEUE_STATUS_RSPPROCESS);

        if (bIsQueueOk) {
            try {
                CMUrlInfos urlInfoList = getEndpoints(target);

                if (urlInfoList != null &&
                        NullChecker.isNotNullish(urlInfoList.getUrlInfo()) &&
                        urlInfoList.getUrlInfo().get(0) != null &&
                        NullChecker.isNotNullish(urlInfoList.getUrlInfo().get(0).getHcid()) &&
                        NullChecker.isNotNullish(urlInfoList.getUrlInfo().get(0).getUrl())) {

                    NhinTargetSystemType oTargetSystem = new NhinTargetSystemType();
                    oTargetSystem.setUrl(urlInfoList.getUrlInfo().get(0).getUrl());

                    // Audit the Retrieve Documents Request Message sent to the Adapter Interface
                    auditLog.auditDocRetrieveDeferredRequest(request, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, assertion, homeCommunityId);

                    // Get the RetrieveDocumentSetResponseType by passing the request to local adapter doc retrieve java implementation
                    RetrieveDocumentSetResponseType response = null;
                    AdapterComponentDocRepositoryProxyJavaImpl docRepositoryImpl = new AdapterComponentDocRepositoryProxyJavaImpl();
                    response = docRepositoryImpl.retrieveDocument(request, assertion);

                    /*
                     * TODO - Redaction Engine has a known issue with Retrieve Documents - GATEWAY-295.
                     * Uncomment and re-test use of the Redaction Engine when this issue has been resolved.
                     */
                    //AdapterRedactionEngineProxyJavaImpl redactEngineImpl = new AdapterRedactionEngineProxyJavaImpl();
                    //response = redactEngineImpl.filterRetrieveDocumentSetResults(request, response, assertion);

                    // Audit the Retrieve Documents Query Response Message sent to the Adapter Interface
                    auditLog.auditDocRetrieveDeferredResponse(response, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, assertion, homeCommunityId);

                    DocRetrieveDeferredPolicyChecker policyCheck = new DocRetrieveDeferredPolicyChecker();

                    if (policyCheck.checkOutgoingPolicy(response, assertion, homeCommunityId)) {
                        // Use passthru proxy to call NHIN
                        PassthruDocRetrieveDeferredRespProxyObjectFactory objFactory = new PassthruDocRetrieveDeferredRespProxyObjectFactory();
                        PassthruDocRetrieveDeferredRespProxy docRetrieveProxy = objFactory.getNhincProxyDocRetrieveDeferredRespProxy();

                        // Create new target system for outbound NHIN DRD response request
                        oTargetSystem = new NhinTargetSystemType();
                        HomeCommunityType responseCommunityType = new HomeCommunityType();
                        responseCommunityType.setHomeCommunityId(responseCommunityId);
                        responseCommunityType.setName(responseCommunityId);
                        oTargetSystem.setHomeCommunity(responseCommunityType);

                        // Send NHIN DRD response request
                        respAck = docRetrieveProxy.crossGatewayRetrieveResponse(request, response, responseAssertion, oTargetSystem);

                        asyncProcess.processAck(messageId, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTACK, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTERR, respAck);
                    } else {
                        ackMsg = "Outgoing Policy Check Failed";
                        log.error(ackMsg);
                        respAck = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_RETRIEVE_DEFERRED_ACK_ERROR_AUTHORIZATION, ackMsg);
                        asyncProcess.processAck(messageId, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTERR, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTERR, respAck);
                    }
                } else {
                    ackMsg = "Failed to obtain target URL from connection manager";
                    log.error(ackMsg);
                    respAck = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_RETRIEVE_DEFERRED_ACK_ERROR_INVALID, ackMsg);
                    asyncProcess.processAck(messageId, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTERR, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTERR, respAck);
                }
            } catch (Exception e) {
                ackMsg = "Exception processing Deferred Retrieve Documents: " + e.getMessage();
                log.error(ackMsg, e);
                respAck = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_RETRIEVE_DEFERRED_ACK_ERROR_INVALID, ackMsg);
                asyncProcess.processAck(messageId, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTERR, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTERR, respAck);
            }
        } else {
            ackMsg = "Deferred Retrieve Documents response processing halted; deferred queue repository error encountered";

            // Set the error acknowledgement status
            // fatal error with deferred queue repository
            respAck = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_RETRIEVE_DEFERRED_ACK_ERROR_INVALID, ackMsg);
        }

        // Audit log - response
        auditLog.auditDocRetrieveDeferredAckResponse(respAck.getMessage(), request, null, responseAssertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, homeCommunityId);

        log.debug("End AdapterDocRetrieveDeferredRespOrchImpl.crossGatewayRetrieveResponse");

        return respAck;
    }

    /**
     *
     * @param targetCommunities
     * @return CMUrlInfos
     */
    protected CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities) {
        CMUrlInfos urlInfoList = null;
        try {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.NHIN_DOCRETRIEVE_DEFERRED_RESPONSE);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs", ex);
        }
        return urlInfoList;
    }
}
