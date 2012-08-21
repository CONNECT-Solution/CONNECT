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
package gov.hhs.fha.nhinc.admindistribution.passthru;

import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.entity.OutboundAdminDistributionDelegate;
import gov.hhs.fha.nhinc.admindistribution.entity.OutboundAdminDistributionOrchestratable;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

/**
 *
 * @author dunnek
 */
public class PassthruAdminDistributionOrchImpl {

    private Log log = null;

    public PassthruAdminDistributionOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target,
            NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        log.info("begin sendAlert");
        AcknowledgementType ack = getLogger().auditNhincAdminDist(body, assertion, target,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        if (ack != null) {
            log.debug("ack: " + ack.getMessage());
        }

        RespondingGatewaySendAlertMessageType newRequest = createRespondingGatewaySendAlertMessageType(target,
                assertion, body);

        OutboundAdminDistributionDelegate adDelegate = new OutboundAdminDistributionDelegate();
        OutboundAdminDistributionOrchestratable orchestratable =
                new OutboundAdminDistributionOrchestratable(adDelegate);
        orchestratable.setRequest(newRequest);
        orchestratable.setAssertion(assertion);
        orchestratable.setTarget(target);

        execute(adDelegate, orchestratable);
    }

    private RespondingGatewaySendAlertMessageType createRespondingGatewaySendAlertMessageType(
            NhinTargetSystemType target, AssertionType assertion, EDXLDistribution body) {
        NhinTargetCommunitiesType targetCommunitiesType = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunityType = new NhinTargetCommunityType();
        targetCommunityType.setHomeCommunity(target.getHomeCommunity());
        targetCommunitiesType.getNhinTargetCommunity().add(targetCommunityType);
        RespondingGatewaySendAlertMessageType newRequest = new RespondingGatewaySendAlertMessageType();
        newRequest.setAssertion(assertion);
        newRequest.setEDXLDistribution(body);
        newRequest.setNhinTargetCommunities(targetCommunitiesType);
        return newRequest;
    }

    protected void execute(OutboundAdminDistributionDelegate adDelegate,
            OutboundAdminDistributionOrchestratable orchestratable) {
        adDelegate.process(orchestratable);
    }

    protected AdminDistributionAuditLogger getLogger() {
        return new AdminDistributionAuditLogger();
    }
}
