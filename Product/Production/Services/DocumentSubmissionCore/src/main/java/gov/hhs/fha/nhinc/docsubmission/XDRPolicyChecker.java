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
package gov.hhs.fha.nhinc.docsubmission;

import gov.hhs.fha.nhinc.common.eventcommon.XDREventType;
import gov.hhs.fha.nhinc.common.eventcommon.XDRMessageType;
import gov.hhs.fha.nhinc.common.eventcommon.XDRResponseEventType;
import gov.hhs.fha.nhinc.common.eventcommon.XDRResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class XDRPolicyChecker {
    private static final Logger LOG = LoggerFactory.getLogger(XDRPolicyChecker.class);

    public boolean checkXDRRequestPolicy(ProvideAndRegisterDocumentSetRequestType message, AssertionType assertion,
            String senderHCID, String receiverHCID, String direction) {

        XDREventType policyCheckReq = new XDREventType();
        XDRMessageType policyMsg = new XDRMessageType();

        policyCheckReq.setDirection(direction);

        HomeCommunityType senderHC = new HomeCommunityType();
        senderHC.setHomeCommunityId(senderHCID);

        policyCheckReq.setSendingHomeCommunity(senderHC);
        HomeCommunityType receiverHC = new HomeCommunityType();

        receiverHC.setHomeCommunityId(receiverHCID);
        policyCheckReq.setReceivingHomeCommunity(receiverHC);

        policyMsg.setAssertion(assertion);
        policyMsg.setProvideAndRegisterDocumentSetRequest(message);

        policyCheckReq.setMessage(policyMsg);

        return invokePolicyEngine(policyCheckReq);
    }

    protected boolean invokePolicyEngine(XDREventType policyCheckReq) {
        boolean policyIsValid = false;

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyXDRRequest(policyCheckReq);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        AssertionType assertion = null;
        if (policyReq != null) {
            assertion = policyReq.getAssertion();
        }
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null && NullChecker.isNotNullish(policyResp.getResponse().getResult())
                && policyResp.getResponse().getResult().get(0) != null
                && policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        return policyIsValid;
    }

    /**
     *
     * @param message
     * @param assertion
     * @param senderHCID
     * @param receiverHCID
     * @param direction
     * @return
     */
    public boolean checkXDRResponsePolicy(RegistryResponseType message, AssertionType assertion, String senderHCID,
            String receiverHCID, String direction) {
        LOG.debug("Entering checkXDRResponsePolicy");

        XDRResponseEventType policyCheckReq = createXDRResponseEventType(message, assertion, senderHCID, receiverHCID,
                direction);

        boolean isPolicyValid = false;

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();

        CheckPolicyRequestType policyReq = policyChecker.checkPolicyXDRResponse(policyCheckReq);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null && NullChecker.isNotNullish(policyResp.getResponse().getResult())
                && policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            isPolicyValid = true;
        }

        LOG.debug("Exiting checkXDRResponsePolicy");

        return isPolicyValid;
    }

    /**
     *
     * @param message
     * @param assertion
     * @param senderHCID
     * @param receiverHCID
     * @param direction
     * @return
     */
    private XDRResponseEventType createXDRResponseEventType(RegistryResponseType message, AssertionType assertion,
            String senderHCID, String receiverHCID, String direction) {
        XDRResponseEventType policyCheckReq = new XDRResponseEventType();
        XDRResponseMessageType policyMsg = new XDRResponseMessageType();

        policyCheckReq.setDirection(direction);

        HomeCommunityType senderHC = new HomeCommunityType();
        senderHC.setHomeCommunityId(senderHCID);

        policyCheckReq.setSendingHomeCommunity(senderHC);
        HomeCommunityType receiverHC = new HomeCommunityType();

        receiverHC.setHomeCommunityId(receiverHCID);
        policyCheckReq.setReceivingHomeCommunity(receiverHC);

        policyMsg.setAssertion(assertion);
        policyMsg.setRegistryResponse(message);

        policyCheckReq.setMessage(policyMsg);

        return policyCheckReq;
    }
}
