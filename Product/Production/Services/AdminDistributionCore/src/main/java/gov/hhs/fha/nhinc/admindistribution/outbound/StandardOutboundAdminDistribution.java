/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.admindistribution.outbound;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.logInfoServiceProcess;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionPolicyChecker;
import gov.hhs.fha.nhinc.admindistribution.ADMessageGeneratorUtils;
import gov.hhs.fha.nhinc.admindistribution.aspect.ADRequestTransformingBuilder;
import gov.hhs.fha.nhinc.admindistribution.entity.OutboundAdminDistributionDelegate;
import gov.hhs.fha.nhinc.admindistribution.entity.OutboundAdminDistributionOrchestratable;
import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class StandardOutboundAdminDistribution implements OutboundAdminDistribution {

    private static final Logger LOG = LoggerFactory.getLogger(StandardOutboundAdminDistribution.class);
    private AdminDistributionAuditLogger auditLogger = null;
    private final ADMessageGeneratorUtils msgUtils = ADMessageGeneratorUtils.getInstance();

    /**
     * This method sends AlertMessage to the target.
     *
     * @param message SendAlertMessage received.
     * @param assertion Assertion received.
     * @param target NhinTargetCommunity received.
     */
    @Override
    @OutboundProcessingEvent(beforeBuilder = ADRequestTransformingBuilder.class, afterReturningBuilder
    = ADRequestTransformingBuilder.class, serviceType = "Admin Distribution", version = "")
    public void sendAlertMessage(RespondingGatewaySendAlertMessageSecuredType message, AssertionType assertion,
        NhinTargetCommunitiesType target) {
        RespondingGatewaySendAlertMessageType unsecured = msgUtils.convertToUnsecured(message,
            ADMessageGeneratorUtils.getInstance().generateMessageId(assertion), target);

        this.sendAlertMessage(unsecured, assertion, target);

    }

    /**
     * @param message SendAlerMessage Received.
     * @param assertion Assertion received.
     * @param target NhinTargetCommunity received.
     */
    @Override
    @OutboundProcessingEvent(beforeBuilder = ADRequestTransformingBuilder.class, afterReturningBuilder
    = ADRequestTransformingBuilder.class, serviceType = "Admin Distribution", version = "")
    public void sendAlertMessage(RespondingGatewaySendAlertMessageType message, AssertionType assertion,
        NhinTargetCommunitiesType target) {
        logInfoServiceProcess(this.getClass());
        auditMessage(message, ADMessageGeneratorUtils.getInstance().generateMessageId(assertion),
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        List<UrlInfo> urlInfoList = getEndpoints(target);

        if (urlInfoList == null || urlInfoList.isEmpty()) {
            LOG.warn("No targets were found for the Admin Distribution Request");
        } else {
            for (UrlInfo urlInfo : urlInfoList) {
                // create a new request to send out to each target community
                LOG.debug("Target: {}", urlInfo.getHcid());
                // check the policy for the outgoing request to the target community
                boolean bIsPolicyOk = checkPolicy(message, assertion, urlInfo.getHcid());

                if (bIsPolicyOk) {
                    NhinTargetSystemType targetSystem = buildTargetSystem(urlInfo);
                    sendToNhinProxy(message, assertion, targetSystem);
                } else {
                    LOG.error("The policy engine evaluated the request and denied the request.");
                }
            }
        }
    }

    /**
     * This method audits the AdminDist Entity Message.
     *
     * @param message SendAlertMessage received.
     * @param assertion Assertion received.
     * @param direction The direction can be either outbound or inbound.
     */
    protected void auditMessage(RespondingGatewaySendAlertMessageType message, AssertionType assertion,
        String direction) {
        AcknowledgementType ack = getAuditLogger().auditEntityAdminDist(message, assertion, direction);
        if (ack != null) {
            LOG.debug("ack: {}", ack.getMessage());
        }
    }

    /**
     * @return auditLogger to audit.
     */
    protected AdminDistributionAuditLogger getAuditLogger() {
        if (null == auditLogger) {
            auditLogger = new AdminDistributionAuditLogger();
        }
        return auditLogger;
    }

    private static NhinTargetSystemType buildTargetSystem(UrlInfo urlInfo) {
        LOG.debug("Begin buildTargetSystem");
        NhinTargetSystemType result = new NhinTargetSystemType();
        HomeCommunityType hc = new HomeCommunityType();

        hc.setHomeCommunityId(urlInfo.getHcid());
        result.setHomeCommunity(hc);
        result.setUrl(urlInfo.getUrl());

        return result;
    }

    /**
     * This method returns the list of url's of targetCommunities.
     *
     * @param targetCommunities NhinTargetCommunities received.
     * @return list of urlInfo for target Communities.
     */
    protected List<UrlInfo> getEndpoints(NhinTargetCommunitiesType targetCommunities) {
        List<UrlInfo> urlInfoList = null;

        try {
            urlInfoList = ExchangeManager.getInstance().getEndpointURLFromNhinTargetCommunities(
                targetCommunities, NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME);
        } catch (ExchangeManagerException ex) {
            LOG.error("Failed to obtain target URLs", ex);
        }

        return urlInfoList;
    }

    /**
     * This method returns boolean for the policyCheck for a specific HCID.
     *
     * @param request SendAlertMessage received.
     * @param assertion Assertion received.
     * @param hcid homeCommunityId to check policy.
     * @return true if checkpolicy is permit; else false.
     */
    protected boolean checkPolicy(RespondingGatewaySendAlertMessageType request, AssertionType assertion, String hcid) {
        if (request != null) {
            request.setAssertion(assertion);
        }
        return new AdminDistributionPolicyChecker().checkOutgoingPolicy(request, hcid);
    }

    /**
     * This method send message to Nhin Proxy.
     *
     * @param newRequest SendAlertMessage received.
     * @param assertion Assertion received.
     * @param target NhinTargetSystem received.
     */
    protected void sendToNhinProxy(RespondingGatewaySendAlertMessageType newRequest, AssertionType assertion,
        NhinTargetSystemType target) {
        LOG.debug("begin sendToNhinProxy");
        OutboundAdminDistributionDelegate adDelegate = getNewOutboundAdminDistributionDelegate();
        OutboundAdminDistributionOrchestratable orchestratable = new OutboundAdminDistributionOrchestratable(
            adDelegate);
        orchestratable.setRequest(newRequest);
        orchestratable.setAssertion(assertion);
        orchestratable.setTarget(target);
        orchestratable.setPassthru(false);

        adDelegate.process(orchestratable);
    }

    /**
     * @return an instance of OutboundAdminDistributionDelegate
     */
    protected OutboundAdminDistributionDelegate getNewOutboundAdminDistributionDelegate() {
        return new OutboundAdminDistributionDelegate();
    }
}
