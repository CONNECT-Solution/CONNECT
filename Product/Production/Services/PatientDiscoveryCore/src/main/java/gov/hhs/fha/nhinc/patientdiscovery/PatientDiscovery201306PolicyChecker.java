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
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.eventcommon.PatDiscReqEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.generic.GenericFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

public class PatientDiscovery201306PolicyChecker extends
AbstractPatientDiscoveryPolicyChecker<RespondingGatewayPRPAIN201306UV02RequestType, PRPAIN201306UV02> {

    private static PatientDiscovery201306PolicyChecker INSTANCE = new PatientDiscovery201306PolicyChecker(
        new PolicyEngineProxyObjectFactory());

    PatientDiscovery201306PolicyChecker(GenericFactory<PolicyEngineProxy> policyEngFactory) {
        super(policyEngFactory);
    }

    public static PatientDiscovery201306PolicyChecker getInstance() {
        return INSTANCE;
    }

    public boolean check201305Policy(PRPAIN201306UV02 message, AssertionType assertion) {
        String roid = null;
        String soid = null;

        if (message != null && checkMessageReceiverForRepresentedOrg(message)) {
            roid = message.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue().getId().get(0).getRoot();
        }

        if (message != null && message.getSender() != null && checkMessageSenderForRepresentedOrg(message)) {
            soid = message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
                .getId().get(0).getRoot();
        }

        PatDiscReqEventType policyCheckReq = new PatDiscReqEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);

        policyCheckReq.setPRPAIN201306UV02(message);
        policyCheckReq.setAssertion(assertion);
        HomeCommunityType senderHC = new HomeCommunityType();
        senderHC.setHomeCommunityId(soid);
        policyCheckReq.setSendingHomeCommunity(senderHC);
        HomeCommunityType receiverHC = new HomeCommunityType();
        receiverHC.setHomeCommunityId(roid);
        policyCheckReq.setReceivingHomeCommunity(receiverHC);

        return invokePolicyEngine(policyCheckReq);
    }

    private static boolean checkMessageReceiverForRepresentedOrg(PRPAIN201306UV02 message) {
        return NullChecker.isNotNullish(message.getReceiver()) && message.getReceiver().get(0) != null
            && message.getReceiver().get(0).getDevice() != null
            && message.getReceiver().get(0).getDevice().getAsAgent() != null
            && message.getReceiver().get(0).getDevice().getAsAgent().getValue() != null
            && message.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization() != null
            && message.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
            .getValue() != null
            && NullChecker.isNotNullish(message.getReceiver().get(0).getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId())
            && message.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
            .getId().get(0) != null
            && NullChecker.isNotNullish(message.getReceiver().get(0).getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot());
    }

    private static boolean checkMessageSenderForRepresentedOrg(PRPAIN201306UV02 message) {
        return message.getSender().getDevice() != null
            && message.getSender().getDevice().getAsAgent() != null
            && message.getSender().getDevice().getAsAgent().getValue() != null
            && message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null
            && message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null
            && NullChecker.isNotNullish(
                message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId())
            && message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()
            .get(0) != null
            && NullChecker.isNotNullish(message.getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot());
    }

    @Override
    public boolean checkOutgoingPolicy(RespondingGatewayPRPAIN201306UV02RequestType request) {
        PRPAIN201306UV02 message = request.getPRPAIN201306UV02();
        AssertionType assertion = request.getAssertion();
        return check201305Policy(message, assertion);
    }

    @Override
    public boolean checkIncomingPolicy(PRPAIN201306UV02 request, AssertionType assertion) {
        // TODO Auto-generated method stub
        return false;
    }

}
