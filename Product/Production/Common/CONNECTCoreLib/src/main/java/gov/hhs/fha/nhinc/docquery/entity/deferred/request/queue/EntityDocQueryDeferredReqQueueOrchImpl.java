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
package gov.hhs.fha.nhinc.docquery.entity.deferred.request.queue;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.docquery.DocQueryPolicyChecker;
import gov.hhs.fha.nhinc.docquery.entity.EntityDocQueryOrchImpl;
import gov.hhs.fha.nhinc.docquery.passthru.deferred.response.proxy.PassthruDocQueryDeferredResponseProxy;
import gov.hhs.fha.nhinc.docquery.passthru.deferred.response.proxy.PassthruDocQueryDeferredResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author narendra.reddy
 */
public class EntityDocQueryDeferredReqQueueOrchImpl {

    private static final Log log = LogFactory.getLog(EntityDocQueryDeferredReqQueueOrchImpl.class);

    /**
     * 
     * @param msg
     * @param assertion
     * @param targets
     * @return DocQueryAcknowledgementType
     */
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion, NhinTargetCommunitiesType targets) {
        DocQueryAcknowledgementType respAck = new DocQueryAcknowledgementType();

        RegistryResponseType regResp = new RegistryResponseType();
        respAck.setMessage(regResp);

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


                    // Get the AdhocQueryResponse by passing the request to the QD Sync Flow.
                    AdhocQueryResponse response = null;
                    EntityDocQueryOrchImpl orchImpl = new EntityDocQueryOrchImpl();
                    response = orchImpl.respondingGatewayCrossGatewayQuery(msg, assertion, targets);

                    PassthruDocQueryDeferredResponseProxyObjectFactory factory = new PassthruDocQueryDeferredResponseProxyObjectFactory();
                    PassthruDocQueryDeferredResponseProxy proxy = factory.getPassthruDocQueryDeferredResponseProxy();


                    respAck = proxy.respondingGatewayCrossGatewayQuery(response, assertion, target);
                } else {
                    log.error("Outgoing Policy Check Failed");
                    regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG);
                }
            } else {
                log.error("Failed to obtain target URL from connection manager");
                regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG);
            }
        } catch (Exception ex) {
            log.error(ex);
            regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG);
        }
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
}
