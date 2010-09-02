/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.entity.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifiersType;
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
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.document.DocumentQueryTransform;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation class for Entity Document Query Deferred request message
 * @author Mark Goldman
 */
public class EntityDocQueryDeferredReqOrchImpl {

    private String localHomeCommunity = null;
    private static final Log LOG = LogFactory.getLog(EntityDocQueryDeferredReqOrchImpl.class);

    /**
     *
     * @param message
     * @param assertion
     * @param target
     * @return
     */
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(
            AdhocQueryRequest message, AssertionType assertion, NhinTargetCommunitiesType target) {
        getLog().debug("start respondingGatewayCrossGatewayQuery(AdhocQueryRequest message, AssertionType assertion, NhinTargetCommunitiesType target)");

        DocQueryAcknowledgementType nhincResponse = new DocQueryAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        nhincResponse.setMessage(regResp);

        getAuditLog().audit(message, assertion);

        try {
            CMUrlInfos urlInfoList = getEndpoints(target);

            if (urlInfoList != null && NullChecker.isNotNullish(urlInfoList.getUrlInfo())) {
                for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {
                    //create a new request to send out to each target community
                    if (getLog().isDebugEnabled()) {
                        getLog().debug(String.format("Target: {0}", urlInfo.getHcid()));
                    }
                    List<SlotType1> slotList = message.getAdhocQuery().getSlot();

                    List<QualifiedSubjectIdentifierType> correlationsResult = new EntityDocQueryHelper().retreiveCorrelations(slotList, urlInfoList, assertion, true, getLocalHomeCommunityId());

                    // Make sure the valid results back
                    if (NullChecker.isNotNullish(correlationsResult)) {

                        for (QualifiedSubjectIdentifierType subjectId : correlationsResult) {
                            if (subjectId != null) {
                                EntityDocQueryHelper helper = new EntityDocQueryHelper();
                                HomeCommunityType targetCommunity = helper.lookupHomeCommunityId(subjectId.getAssigningAuthorityIdentifier(), helper.getLocalAssigningAuthority(slotList), getLocalHomeCommunityId());
                                //check the policy for the outgoing request to the target community
                                if (targetCommunity != null &&
                                        NullChecker.isNotNullish(targetCommunity.getHomeCommunityId())) {
                                    if (checkPolicy(message, assertion, targetCommunity.getHomeCommunityId())) {
                                        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
                                        targetSystem.setHomeCommunity(targetCommunity);

                                        DocumentQueryTransform transform = new DocumentQueryTransform();
                                        AdhocQueryRequest adhocQueryRequest = transform.replaceAdhocQueryPatientId(message, getLocalHomeCommunityId(), subjectId.getAssigningAuthorityIdentifier(), subjectId.getSubjectIdentifier());
                                        nhincResponse = getProxy().crossGatewayQueryRequest(adhocQueryRequest, assertion, targetSystem);
                                    } else {
                                        getLog().error("The policy engine evaluated the request and denied the request.");
                                        regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG);
                                    }
                                }
                                else {
                                    getLog().error("Could not find home community for assigning authority " + subjectId.getAssigningAuthorityIdentifier());
                                    regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG);
                                }
                            } else {
                                getLog().error("No correlations were found");
                                regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG);
                            }
                        }
                    } else {
                        getLog().error("No correlations were found");
                        regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG);
                    }
                }
            } else {
                getLog().error("Failed to obtain target URL from connection manager");
                regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG);
            }
        } catch (Exception e) {
            getLog().error(e);
            nhincResponse.getMessage().setStatus(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG);
        }

        getAuditLog().logDocQueryAck(nhincResponse,
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

    /**
     *
     * @return
     */
    protected Log getLog() {
        return LOG;
    }

    /**
     *
     * @return
     */
    protected DocQueryAuditLog getAuditLog() {
        return new DocQueryAuditLog();
    }

    protected String getLocalHomeCommunityId() {
        String sHomeCommunity = null;

        if (localHomeCommunity != null) {
            sHomeCommunity = localHomeCommunity;
        } else {
            try {
                sHomeCommunity = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            } catch (PropertyAccessException ex) {
                getLog().error(ex.getMessage());
            }
        }
        return sHomeCommunity;
    }
}
