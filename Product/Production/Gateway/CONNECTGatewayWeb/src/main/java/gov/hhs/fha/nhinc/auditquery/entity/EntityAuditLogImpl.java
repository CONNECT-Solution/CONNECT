/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.auditquery.entity;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import gov.hhs.fha.nhinc.auditquery.EntityAuditLog;
import gov.hhs.fha.nhinc.auditquery.EntityAuditQuery;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.FindAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.FindAuditEventsSecuredRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.nhinauditquery.proxy.NhinAuditQueryProxy;
import gov.hhs.fha.nhinc.nhinauditquery.proxy.NhinAuditQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class EntityAuditLogImpl {

    private static Log log = LogFactory.getLog(EntityAuditLogImpl.class);

    /**
     * This method will perform an audit log query to a specified set of target communities and
     * aggregate the results from each community that match the specified search criteria into one List.
     * This combined list of audit log records will be returned to the user.
     *
     * @param findAuditEventsRequest The audit log query search criteria
     * @return A list of Audit Log records that match the specified criteria
     */
    public FindAuditEventsResponseType findAuditEvents(FindAuditEventsSecuredRequestType findAuditEventsRequest, WebServiceContext context) {
        log.debug("Entering EntityAuditLogImpl.findAuditEvents...");

        FindAuditEventsResponseType resp = new FindAuditEventsResponseType();
        EntityAuditQuery auditQuery = new EntityAuditQuery();
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();

        FindAuditEventsRequestType request = new FindAuditEventsRequestType();
        request.setAssertion(SamlTokenExtractor.GetAssertion(context));
        request.setFindAuditEvents(findAuditEventsRequest.getFindAuditEvents());
        request.setNhinTargetCommunities(findAuditEventsRequest.getNhinTargetCommunities());

        // Audit the Audit Log Query Request Message received on the Entity Interface
        EntityAuditLog auditLog = new EntityAuditLog();
        AcknowledgementType ack = auditLog.audit(request);

        // Check to see if any target communities were specified in the request message.
        // If there were no target communities provided then perform a local audit query
        // and build a list of target communities
        if (findAuditEventsRequest.getNhinTargetCommunities() == null ||
                NullChecker.isNullish(findAuditEventsRequest.getNhinTargetCommunities().getNhinTargetCommunity())) {
            // Perform a local audit query
            FindCommunitiesAndAuditEventsResponseType auditResults = auditQuery.query(request);

            // Save off the audit query results
            if (auditResults.getFindAuditEventResponse() != null &&
                    NullChecker.isNotNullish(auditResults.getFindAuditEventResponse().getFindAuditEventsReturn())) {
                for (AuditMessageType auditMsg : auditResults.getFindAuditEventResponse().getFindAuditEventsReturn()) {
                    resp.getFindAuditEventsReturn().add(auditMsg);
                }
            }

            // Create the list of target communties
            if (NullChecker.isNotNullish(auditResults.getCommunities())) {
                for (String communityId : auditResults.getCommunities()) {
                    NhinTargetCommunityType community = new NhinTargetCommunityType();
                    HomeCommunityType homeCommunity = new HomeCommunityType();
                    homeCommunity.setHomeCommunityId(communityId);
                    community.setHomeCommunity(homeCommunity);

                    targets.getNhinTargetCommunity().add(community);
                }
            }
        } else {
            targets = findAuditEventsRequest.getNhinTargetCommunities();
        }

        // For each Target Community perform an Audit Query and aggregate the results
        CMUrlInfos urlInfoList = null;
        if (NullChecker.isNotNullish(targets.getNhinTargetCommunity())) {
            // Obtain all the URLs for the targets being sent to
            try {
                urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(request.getNhinTargetCommunities(), NhincConstants.AUDIT_QUERY_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Failed to obtain target URLs");
                return null;
            }

            if (urlInfoList != null &&
                    NullChecker.isNotNullish(urlInfoList.getUrlInfo())) {

                NhinAuditQueryProxyObjectFactory auditFactory = new NhinAuditQueryProxyObjectFactory();
                NhinAuditQueryProxy proxy = auditFactory.getNhinAuditQueryProxy();

                for (CMUrlInfo targetComm : urlInfoList.getUrlInfo()) {

                    gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsRequestType proxyReq = new gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsRequestType();
                    proxyReq.setAssertion(request.getAssertion());
                    proxyReq.setFindAuditEvents(request.getFindAuditEvents());
                    NhinTargetSystemType targetSys = new NhinTargetSystemType();
                    targetSys.setUrl(targetComm.getUrl());
                    proxyReq.setNhinTargetSystem(targetSys);

                    log.info("Sending Audit Query Request to community: " + targetComm.getHcid());

                    // Audit the Audit Log Query Request Message sent on the Nhin Interface
                    EntityAuditLog entityAuditLog = new EntityAuditLog();
                    entityAuditLog.audit(proxyReq);

                    FindAuditEventsResponseType proxyResp = proxy.auditQuery(proxyReq);

                    if (NullChecker.isNotNullish(proxyResp.getFindAuditEventsReturn())) {
                        for (AuditMessageType auditMsg : proxyResp.getFindAuditEventsReturn()) {
                            resp.getFindAuditEventsReturn().add(auditMsg);
                        }
                    }
                }
            }
        }

        log.debug("Exiting EntityAuditLogImpl.findAuditEvents...");
        return resp;
    }
}
