/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredPolicyChecker;
import gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response.proxy.PassthruDocRetrieveDeferredRespProxyObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response.proxy.PassthruDocRetrieveDeferredRespProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.document.DocRetrieveAckTranforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredRespOrchImpl {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public EntityDocRetrieveDeferredRespOrchImpl() {
        log = createLogger();
        debugEnabled = false;
    }

    /**
     *
     * @return Log
     */
    protected Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     * Document Retrieve Deferred Response implementation method
     * @param response
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetResponseType response, AssertionType assertion, NhinTargetCommunitiesType target) {
        log.debug("Begin EntityDocRetrieveDeferredRespOrchImpl.crossGatewayRetrieveResponse");

        String ackMessage = null;
        DocRetrieveAcknowledgementType nhinResponse = null;
        String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();

        try {
            if (null != response && (null != assertion) && (null != target)) {
                auditLog.auditDocRetrieveDeferredResponse(response, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, assertion, homeCommunityId);
                CMUrlInfos urlInfoList = getEndpoints(target);
                NhinTargetSystemType oTargetSystem = null;

                //loop through the communities and send request if results were not null
                if ((urlInfoList == null) || (urlInfoList.getUrlInfo().isEmpty())) {
                    ackMessage = "No targets were found for the Document retrieve deferred Response service.";
                    log.warn(ackMessage);
                    nhinResponse = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_RETRIEVE_DEFERRED_ACK_ERROR_INVALID, ackMessage);
                } else {
                    if (debugEnabled) {
                        log.debug("Creating NHIN doc retrieve proxy");
                    }
                    PassthruDocRetrieveDeferredRespProxyObjectFactory objFactory = new PassthruDocRetrieveDeferredRespProxyObjectFactory();
                    PassthruDocRetrieveDeferredRespProxy docRetrieveProxy = objFactory.getNhincProxyDocRetrieveDeferredRespProxy();
                    DocRetrieveDeferredPolicyChecker policyCheck = new DocRetrieveDeferredPolicyChecker();
                    for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {
                        // Call NHIN proxy
                        oTargetSystem = new NhinTargetSystemType();
                        oTargetSystem.setUrl(urlInfo.getUrl());
                        if (policyCheck.checkOutgoingPolicy(response, assertion, homeCommunityId)) {
                            // Call NHIN proxy
                            nhinResponse = docRetrieveProxy.crossGatewayRetrieveResponse(response, assertion, oTargetSystem);
                        } else {
                            ackMessage = "Policy check failed.";
                            nhinResponse = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_RETRIEVE_DEFERRED_ACK_ERROR_AUTHORIZATION, ackMessage);
                        }

                    }
                }

            }
        } catch (Exception ex) {
            log.error(ex);
            ackMessage = "Fault encountered processing internal document retrieve deferred response for community " + homeCommunityId;
            nhinResponse = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_RETRIEVE_DEFERRED_ACK_ERROR_INVALID, ackMessage);
            log.error(ackMessage);
        }

        // Audit log - response
        auditLog.auditDocRetrieveDeferredAckResponse(nhinResponse.getMessage(), null, response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, homeCommunityId);

        if (debugEnabled) {
            log.debug("End EntityDocRetrieveDeferredRespOrchImpl.crossGatewayRetrieveResponse");
        }
        return nhinResponse;
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
