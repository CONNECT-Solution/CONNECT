/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.entity.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.DocQueryPolicyChecker;
import gov.hhs.fha.nhinc.docquery.entity.EntityDocQueryHelper;
import gov.hhs.fha.nhinc.docquery.passthru.deferred.request.proxy.PassthruDocQueryDeferredRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.docquery.passthru.deferred.request.proxy.PassthruDocQueryDeferredRequestProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.transform.document.DocQueryAckTranforms;
import gov.hhs.fha.nhinc.transform.document.DocumentQueryTransform;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import java.sql.Timestamp;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation class for Entity Document Query Deferred request message
 * @author Mark Goldman
 */
public class EntityDocQueryDeferredReqOrchImpl {

    private static final Log log = LogFactory.getLog(EntityDocQueryDeferredReqOrchImpl.class);

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    /**
     *
     * @param message
     * @param assertion
     * @param target
     * @return <code>DocQueryAcknowledgementType</code>
     */
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest message, AssertionType assertion, NhinTargetCommunitiesType target) {
        log.debug("start respondingGatewayCrossGatewayQuery(AdhocQueryRequest message, AssertionType assertion, NhinTargetCommunitiesType target)");

        DocQueryAcknowledgementType nhincResponse = new DocQueryAcknowledgementType();
        String targetCommunityHcid = "";
        String ackMsg = "";
        boolean bIsQueueOk = false;
        String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();

        DocQueryAuditLog auditLog = new DocQueryAuditLog();
        auditLog.auditDQRequest(message, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, homeCommunityId);

        try {
            CMUrlInfos urlInfoList = getEndpoints(target);

            if (urlInfoList != null && NullChecker.isNotNullish(urlInfoList.getUrlInfo())) {

                // Log the start of the performance record
                Timestamp starttime = new Timestamp(System.currentTimeMillis());
                Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime, "Deferred"+NhincConstants.DOC_QUERY_SERVICE_NAME, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, homeCommunityId);

                AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();
                EntityDocQueryHelper helper = new EntityDocQueryHelper();
                DocumentQueryTransform transform = new DocumentQueryTransform();

                for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {
                    //create a new request to send out to each target community
                    if (log.isDebugEnabled()) {
                        log.debug(String.format("Target: {0}", urlInfo.getHcid()));
                    }
                    List<SlotType1> slotList = message.getAdhocQuery().getSlot();

                    List<QualifiedSubjectIdentifierType> correlationsResult = helper.retreiveCorrelations(slotList, urlInfoList, assertion, true, HomeCommunityMap.getLocalHomeCommunityId());

                    // Make sure valid correlation results are returned
                    if (NullChecker.isNotNullish(correlationsResult)) {

                        for (QualifiedSubjectIdentifierType subjectId : correlationsResult) {

                            if (subjectId != null) {

                                HomeCommunityType targetCommunity = helper.lookupHomeCommunityId(subjectId.getAssigningAuthorityIdentifier(), helper.getLocalAssigningAuthority(slotList), HomeCommunityMap.getLocalHomeCommunityId());

                                // Create new request; add new deferred queue record
                                RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest = new RespondingGatewayCrossGatewayQueryRequestType();
                                AdhocQueryRequest newRequest = transform.replaceAdhocQueryPatientId(message, HomeCommunityMap.getLocalHomeCommunityId(), subjectId.getAssigningAuthorityIdentifier(), subjectId.getSubjectIdentifier());
                                respondingGatewayCrossGatewayQueryRequest.setAdhocQueryRequest(newRequest);

                                AssertionType newAssertion = asyncProcess.copyAssertionTypeObject(assertion);
                                respondingGatewayCrossGatewayQueryRequest.setAssertion(newAssertion);

                                // Assign target community
                                NhinTargetCommunitiesType newTargets = new NhinTargetCommunitiesType();
                                NhinTargetCommunityType newTarget = new NhinTargetCommunityType();
                                newTarget.setHomeCommunity(targetCommunity);
                                newTargets.getNhinTargetCommunity().add(newTarget);
                                respondingGatewayCrossGatewayQueryRequest.setNhinTargetCommunities(newTargets);
                                if (targetCommunity != null && NullChecker.isNotNullish(targetCommunity.getHomeCommunityId())) {
                                    targetCommunityHcid = targetCommunity.getHomeCommunityId();
                                }

                                //check the policy for the outgoing request to the target community
                                if (targetCommunity != null && NullChecker.isNotNullish(targetCommunity.getHomeCommunityId())) {

                                    if (checkPolicy(message, newAssertion, targetCommunity.getHomeCommunityId())) {

                                        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
                                        targetSystem.setHomeCommunity(targetCommunity);

                                        nhincResponse = getProxy().crossGatewayQueryRequest(newRequest, newAssertion, targetSystem);
                                    } else {
                                        ackMsg = "Policy Failed";

                                        // Set the error acknowledgement status of the deferred queue entry
                                        nhincResponse = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_AUTHORIZATION, ackMsg);
                                    }
                                } else {
                                    ackMsg = "Invalid target community";

                                    // Set the error acknowledgement status of the deferred queue entry
                                    nhincResponse = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_INVALID, ackMsg);
                                }

                            } else {
                                log.error("Invalid correlated subject found");
                            }
                        }
                    } else {
                        ackMsg = "No correlations were found";
                        log.error(ackMsg);
                        nhincResponse = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_INVALID, ackMsg);
                    }
                }

                // Log the end of the performance record
                Timestamp stoptime = new Timestamp(System.currentTimeMillis());
                PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);
            } else {
                ackMsg = "Failed to obtain target URL from connection manager";
                log.error(ackMsg);
                nhincResponse = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_INVALID, ackMsg);
            }
        } catch (Exception e) {
            log.error("Exception processing Deferred Query For Documents: ", e);
            nhincResponse = DocQueryAckTranforms.createAckMessage(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_QUERY_DEFERRED_ACK_ERROR_INVALID, e.getMessage());
        }

        auditLog.logDocQueryAck(nhincResponse,
                assertion,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        return nhincResponse;
    }

    /**
     *
     * @return PassthruDocQueryDeferredRequestProxy
     */
    protected PassthruDocQueryDeferredRequestProxy getProxy() {
        PassthruDocQueryDeferredRequestProxyObjectFactory objFactory = new PassthruDocQueryDeferredRequestProxyObjectFactory();
        PassthruDocQueryDeferredRequestProxy docRetrieveProxy = objFactory.getPassthruDocQueryDeferredRequestProxy();
        return docRetrieveProxy;
    }

    /**
     *
     * @param targetCommunities
     * @return Returns the endpoints for given target communities
     * @throws ConnectionManagerException
     */
    protected CMUrlInfos getEndpoints(final NhinTargetCommunitiesType targetCommunities) throws ConnectionManagerException {
        CMUrlInfos urlInfoList = null;

        urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(
                targetCommunities, NhincConstants.NHIN_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME);

        return urlInfoList;
    }

    /**
     *
     * @param message
     * @param assertion
     * @param hcid
     * @return Returns true if given home community is allowed to send requests
     */
    protected boolean checkPolicy(final AdhocQueryRequest message, final AssertionType assertion, final String hcid) {
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(hcid);

        boolean policyIsValid = new DocQueryPolicyChecker().checkOutgoingRequestPolicy(message, assertion, homeCommunity);

        return policyIsValid;
    }

    /**
     *
     * @param urlInfo
     * @return NhinTargetSystemType for given urlInfo
     */
    protected NhinTargetSystemType buildTargetSystem(final CMUrlInfo urlInfo) {
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        targetSystem.setUrl(urlInfo.getUrl());
        return targetSystem;
    }

}
