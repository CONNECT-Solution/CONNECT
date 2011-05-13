/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.request.queue;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredPolicyChecker;
import gov.hhs.fha.nhinc.docretrieve.entity.EntityDocRetrieveOrchImpl;
import gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response.proxy.PassthruDocRetrieveDeferredRespProxyObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response.proxy.PassthruDocRetrieveDeferredRespProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author narendra.reddy
 */
public class EntityDocRetrieveDeferredReqQueueOrchImpl {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public EntityDocRetrieveDeferredReqQueueOrchImpl() {
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
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetCommunitiesType target) {
        if (debugEnabled) {
            log.debug("Begin EntityDocRetrieveDeferredReqQueueOrchImpl.crossGatewayRetrieveResponse");
        }
        DocRetrieveAcknowledgementType nhinResponse = null;

        String homeCommunityId = HomeCommunityMap.getCommunityIdFromTargetCommunities(target);
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        try {
            if (null != request && (null != assertion) && (null != target)) {
                //auditLog.auditDocRetrieveDeferredResponse(request, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, assertion, homeCommunityId);
                CMUrlInfos urlInfoList = getEndpoints(target);
                NhinTargetSystemType oTargetSystem = null;
                //homeCommunityId = getHomeCommFromTarget(target);
                //loop through the communities and send request if results were not null
                if ((urlInfoList == null) || (urlInfoList.getUrlInfo().isEmpty())) {
                    log.warn("No targets were found for the Document retrieve deferred Response service");
                    nhinResponse = buildRegistryErrorAck();
                } else {
                    if (debugEnabled) {
                        log.debug("Creating NHIN doc retrieve proxy");
                    }

                    PassthruDocRetrieveDeferredRespProxyObjectFactory objFactory = new PassthruDocRetrieveDeferredRespProxyObjectFactory();
                    PassthruDocRetrieveDeferredRespProxy docRetrieveProxy = objFactory.getNhincProxyDocRetrieveDeferredRespProxy();

                    // Get the RetrieveDocumentSetResponseType by passing the request to the RD Sync Flow.
                    RetrieveDocumentSetResponseType response = null;
                    EntityDocRetrieveOrchImpl orchImpl = new EntityDocRetrieveOrchImpl();
                    response = orchImpl.respondingGatewayCrossGatewayRetrieve(request, assertion);

                    DocRetrieveDeferredPolicyChecker policyCheck = new DocRetrieveDeferredPolicyChecker();

                    for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {
                        // Call NHIN proxy
                        oTargetSystem = new NhinTargetSystemType();
                        oTargetSystem.setUrl(urlInfo.getUrl());
                        if (policyCheck.checkOutgoingPolicy(response, assertion, homeCommunityId)) {
                            // Call NHIN proxy
                            nhinResponse = docRetrieveProxy.crossGatewayRetrieveResponse(response, assertion, oTargetSystem);
                        } else {
                            nhinResponse = buildRegistryErrorAck();
                        }

                    }
                }

            }
        } catch (Exception ex) {
            log.error(ex);
            nhinResponse = buildRegistryErrorAck();
            log.error("Fault encountered processing internal document retrieve deferred for community " + homeCommunityId);
        }
        if (null != nhinResponse) {
            // Audit log - response
            auditLog.auditDocRetrieveDeferredAckResponse(nhinResponse.getMessage(), assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }
        if (debugEnabled) {
            log.debug("End EntityDocRetrieveDeferredRespOrchImpl.crossGatewayRetrieveResponse");
        }
        return nhinResponse;
    }

    /**
     *
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType buildRegistryErrorAck() {
        DocRetrieveAcknowledgementType nhinResponse = new DocRetrieveAcknowledgementType();
        RegistryResponseType registryResponse = new RegistryResponseType();
        nhinResponse.setMessage(registryResponse);
        registryResponse.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        return nhinResponse;
    }

    /**
     *
     * @param target
     * @return String
     */
    protected String getHomeCommFromTarget(NhinTargetCommunitiesType target) {
        String sHomComm = null;
        if (target.getNhinTargetCommunity() != null &&
                target.getNhinTargetCommunity().size() > 0) {
            NhinTargetCommunityType comm = target.getNhinTargetCommunity().get(0);
            if (comm.getHomeCommunity() != null) {
                sHomComm = comm.getHomeCommunity().getHomeCommunityId();
            }
        }
        return sHomComm;
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
