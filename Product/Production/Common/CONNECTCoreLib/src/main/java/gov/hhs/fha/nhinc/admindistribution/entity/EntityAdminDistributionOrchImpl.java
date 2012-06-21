/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.admindistribution.entity;

import java.util.List;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionPolicyChecker;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageSecuredType;
import gov.hhs.fha.nhinc.admindistribution.passthru.proxy.PassthruAdminDistributionProxy;
import gov.hhs.fha.nhinc.admindistribution.passthru.proxy.PassthruAdminDistributionProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;

/**
 * 
 * @author dunnek
 */
public class EntityAdminDistributionOrchImpl {

    private Log log = null;
    private AdminDistributionAuditLogger adLogger = null;

    public EntityAdminDistributionOrchImpl() {
        log = LogFactory.getLog(getClass());
    }

    public void sendAlertMessage(RespondingGatewaySendAlertMessageType message, AssertionType assertion,
            NhinTargetCommunitiesType target) {
        auditMessage(message, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        List<UrlInfo> urlInfoList = getEndpoints(target);

        if ((urlInfoList == null) || (urlInfoList.isEmpty())) {
            log.warn("No targets were found for the Admin Distribution Request");
        } else {
            for (UrlInfo urlInfo : urlInfoList) {
                // create a new request to send out to each target community
                log.debug("Target: " + urlInfo.getHcid());
                // check the policy for the outgoing request to the target community
                boolean bIsPolicyOk = checkPolicy(message, assertion, urlInfo.getHcid());

                if (bIsPolicyOk) {
                    NhinTargetSystemType targetSystem = buildTargetSystem(urlInfo);
                    sendToNhinProxy(message, assertion, targetSystem);
                } // if (bIsPolicyOk)
                else {
                    log.error("The policy engine evaluated the request and denied the request.");
                } // else policy enging did not return a permit response
            }
        }
    }

    protected void auditMessage(RespondingGatewaySendAlertMessageType message, AssertionType assertion, String direction) {
        AcknowledgementType ack = getLogger().auditEntityAdminDist(message, assertion, direction);
        if (ack != null) {
            log.debug("ack: " + ack.getMessage());
        }
    }

    protected AdminDistributionAuditLogger getLogger() {
        return (adLogger != null) ? adLogger : new AdminDistributionAuditLogger();
    }

    private NhinTargetSystemType buildTargetSystem(UrlInfo urlInfo) {
        log.debug("Begin buildTargetSystem");
        NhinTargetSystemType result = new NhinTargetSystemType();
        HomeCommunityType hc = new HomeCommunityType();

        hc.setHomeCommunityId(urlInfo.getHcid());
        result.setHomeCommunity(hc);
        result.setUrl(urlInfo.getUrl());

        return result;
    }

    public void sendAlertMessage(RespondingGatewaySendAlertMessageSecuredType message, AssertionType assertion,
            NhinTargetCommunitiesType target) {
        RespondingGatewaySendAlertMessageType unsecured = new RespondingGatewaySendAlertMessageType();

        unsecured.setAssertion(assertion);
        unsecured.setEDXLDistribution(message.getEDXLDistribution());
        unsecured.setNhinTargetCommunities(message.getNhinTargetCommunities());

        this.sendAlertMessage(unsecured, assertion, target);

    }

    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetCommunitiesType target) {
        RespondingGatewaySendAlertMessageType unsecured = new RespondingGatewaySendAlertMessageType();

        unsecured.setAssertion(assertion);
        unsecured.setEDXLDistribution(body);
        unsecured.setNhinTargetCommunities(target);

        this.sendAlertMessage(unsecured, assertion, target);

    }

    protected List<UrlInfo> getEndpoints(NhinTargetCommunitiesType targetCommunities) {
        List<UrlInfo> urlInfoList = null;

        try {
            urlInfoList = ConnectionManagerCache.getInstance().getEndpointURLFromNhinTargetCommunities(
                    targetCommunities, NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs", ex);
        }

        return urlInfoList;
    }

    protected boolean checkPolicy(RespondingGatewaySendAlertMessageType request, AssertionType assertion, String hcid) {
        if (request != null) {
            request.setAssertion(assertion);
        }
        return new AdminDistributionPolicyChecker().checkOutgoingPolicy(request, hcid);

    }

    protected void sendToNhinProxy(RespondingGatewaySendAlertMessageType newRequest, AssertionType assertion,
            NhinTargetSystemType target) {
        log.debug("begin sendToNhinProxy");
        OutboundAdminDistributionDelegate adDelegate = new OutboundAdminDistributionDelegate();
        OutboundAdminDistributionOrchestratable orchestratable = new OutboundAdminDistributionOrchestratable(adDelegate);
        orchestratable.setRequest(newRequest);
        orchestratable.setAssertion(assertion);
        orchestratable.setTarget(target);
        adDelegate.process(orchestratable);
    }

    protected PassthruAdminDistributionProxy getNhincAdminDist() {
        return new PassthruAdminDistributionProxyObjectFactory().getNhincAdminDistProxy();
    }
}
