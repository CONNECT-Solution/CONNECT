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
package gov.hhs.fha.nhinc.admindistribution.outbound;

import gov.hhs.fha.nhinc.admindistribution.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.admindistribution.aspect.ADRequestTransformingBuilder;
import gov.hhs.fha.nhinc.admindistribution.entity.OutboundAdminDistributionDelegate;
import gov.hhs.fha.nhinc.admindistribution.entity.OutboundAdminDistributionOrchestratable;
import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.event.DefaultEventDescriptionBuilder;

public class PassthroughOutboundAdminDistribution implements OutboundAdminDistribution {

    private MessageGeneratorUtils msgUtils = MessageGeneratorUtils.getInstance();
    private OutboundAdminDistributionDelegate adDelegate = new OutboundAdminDistributionDelegate();

    /**
     * Constructor.
     */
    public PassthroughOutboundAdminDistribution() {
        super();
    }

    /**
     * Constructor.
     */
    public PassthroughOutboundAdminDistribution(OutboundAdminDistributionDelegate adDelegate) {
        this.adDelegate = adDelegate;
    }

    /**
     * This method implements sendAlertMessage for AdminDist when in passthrumode.
     * 
     * @param request
     * @param assertion
     * @param target
     */
    @Override
    @OutboundProcessingEvent(beforeBuilder = ADRequestTransformingBuilder.class,
            afterReturningBuilder = DefaultEventDescriptionBuilder.class, serviceType = "Admin Distribution",
            version = "")
    public void sendAlertMessage(RespondingGatewaySendAlertMessageSecuredType message, AssertionType assertion,
            NhinTargetCommunitiesType target) {
        RespondingGatewaySendAlertMessageType request = msgUtils.convertToUnsecured(message, assertion, target);

        sendAlertMessage(request, assertion, target);
    }

    /**
     * This method implements sendAlertMessage for AdminDist when in passthrumode.
     * 
     * @param request
     * @param assertion
     * @param target
     */
    @Override
    @OutboundProcessingEvent(beforeBuilder = ADRequestTransformingBuilder.class,
            afterReturningBuilder = DefaultEventDescriptionBuilder.class, serviceType = "Admin Distribution",
            version = "")
    public void sendAlertMessage(RespondingGatewaySendAlertMessageType request, AssertionType assertion,
            NhinTargetCommunitiesType targetCommunities) {
        
    	NhinTargetSystemType target = msgUtils.convertFirstToNhinTargetSystemType(targetCommunities);
        sendToNhin(request, assertion, target);
    }

    private void sendToNhin(RespondingGatewaySendAlertMessageType request, AssertionType assertion,
            NhinTargetSystemType target) {

        OutboundAdminDistributionOrchestratable orchestratable = new OutboundAdminDistributionOrchestratable(adDelegate);
        orchestratable.setRequest(request);
        orchestratable.setAssertion(assertion);
        orchestratable.setTarget(target);
        orchestratable.setPassthru(true);

        adDelegate.process(orchestratable);
    }
}
