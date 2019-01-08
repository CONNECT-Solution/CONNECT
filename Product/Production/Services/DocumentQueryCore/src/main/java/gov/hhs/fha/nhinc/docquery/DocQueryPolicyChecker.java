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
package gov.hhs.fha.nhinc.docquery;

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestMessageType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryResponseMessageType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryResultEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;

/**
 *
 * @author JHOPPESC
 */
public class DocQueryPolicyChecker {

    /**
     * Checks to see if the security policy will permit the query to be executed.
     *
     * @param message The AdhocQuery request message.
     * @param assertion Assertion received.
     * @return Returns true if the security policy permits the query; false if denied.
     */
    public boolean checkIncomingPolicy(AdhocQueryRequest message, AssertionType assertion) {
        // convert the request message to an object recognized by the policy engine
        AdhocQueryRequestMessageType request = new AdhocQueryRequestMessageType();
        request.setAssertion(assertion);
        request.setAdhocQueryRequest(message);

        AdhocQueryRequestEventType policyCheckReq = new AdhocQueryRequestEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        policyCheckReq.setMessage(request);

        CheckPolicyRequestType policyReq = buildPolicyRequest(policyCheckReq, assertion);

        return checkPolicy(policyReq, assertion);
    }

    /**
     * checks the outgoing policy for the Query.
     * @param message The AdhocQuery request message.
     * @param assertion Assertion received.
     * @return Returns true if the security policy permits the query; false if denied.
     */
    public boolean checkOutgoingPolicy(AdhocQueryRequest message, AssertionType assertion) {
        // convert the request message to an object recognized by the policy engine
        AdhocQueryRequestMessageType request = new AdhocQueryRequestMessageType();
        request.setAssertion(assertion);
        request.setAdhocQueryRequest(message);

        AdhocQueryRequestEventType policyCheckReq = new AdhocQueryRequestEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        policyCheckReq.setMessage(request);

        CheckPolicyRequestType policyReq = buildPolicyRequest(policyCheckReq, assertion);

        return checkPolicy(policyReq, assertion);
    }

    /**
     * Chceks Incoming Response security policy.
     * @param message The AdhocQuery request message.
     * @param assertion Assertion received.
     * @return Returns true if the security policy permits the query; false if denied.
     */
    public boolean checkIncomingResponsePolicy(AdhocQueryResponse message, AssertionType assertion) {
        // convert the request message to an object recognized by the policy engine
        AdhocQueryResponseMessageType request = new AdhocQueryResponseMessageType();
        request.setAssertion(assertion);
        request.setAdhocQueryResponse(message);

        AdhocQueryResultEventType policyCheckReq = new AdhocQueryResultEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        policyCheckReq.setMessage(request);

        CheckPolicyRequestType policyReq = buildPolicyRequest(policyCheckReq, assertion);

        return checkPolicy(policyReq, assertion);
    }

    /**
     * Chceks outgoing Response security policy.
     * @param message The AdhocQuery request message.
     * @param assertion Assertion received.
     * @param receiverHcid target communityID.
     * @return Returns true if the security policy permits the query; false if denied.
     */
    public boolean checkOutgoingResponsePolicy(AdhocQueryResponse message, AssertionType assertion,
            HomeCommunityType receiverHcid) {
        // convert the request message to an object recognized by the policy engine
        AdhocQueryResponseMessageType request = new AdhocQueryResponseMessageType();
        request.setAssertion(assertion);
        request.setAdhocQueryResponse(message);

        AdhocQueryResultEventType policyCheckReq = new AdhocQueryResultEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        policyCheckReq.setMessage(request);
        policyCheckReq.setReceivingHomeCommunity(receiverHcid);

        CheckPolicyRequestType policyReq = buildPolicyRequest(policyCheckReq, assertion);

        // call the policy engine to check the permission on the request
        return checkPolicy(policyReq, assertion);
    }

    /**
     * Chceks outgoing Request security policy.
     * @param message The AdhocQuery request message.
     * @param assertion Assertion received.
     * @param receiverHcid target communityID.
     * @return Returns true if the security policy permits the query; false if denied.
     */
    public boolean checkOutgoingRequestPolicy(AdhocQueryRequest message, AssertionType assertion,
            HomeCommunityType receiverHcid) {
        // convert the request message to an object recognized by the policy engine
        AdhocQueryRequestMessageType request = new AdhocQueryRequestMessageType();
        request.setAssertion(assertion);
        request.setAdhocQueryRequest(message);

        AdhocQueryRequestEventType policyCheckReq = new AdhocQueryRequestEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        policyCheckReq.setMessage(request);
        policyCheckReq.setReceivingHomeCommunity(receiverHcid);

        CheckPolicyRequestType policyReq = buildPolicyRequest(policyCheckReq, assertion);

        // call the policy engine to check the permission on the request
        return checkPolicy(policyReq, assertion);
    }

    /**
     * Build PolicyRequest.
     * @param policyCheckReq policyCheckRequest received.
     * @param assertion Assertion received.
     * @return policyReq built.
     */
    protected CheckPolicyRequestType buildPolicyRequest(AdhocQueryRequestEventType policyCheckReq,
            AssertionType assertion) {
        // call the policy engine to check the permission on the request
        CheckPolicyRequestType policyReq = getPolicyChecker().checkPolicyAdhocQuery(policyCheckReq);
        policyReq.setAssertion(assertion);
        return policyReq;
    }

    /**
     * Build PolicyRequest.
     * @param policyCheckResult policyCheckRequest received.
     * @param assertion Assertion received.
     * @return policyReq built.
     */
    protected CheckPolicyRequestType buildPolicyRequest(AdhocQueryResultEventType policyCheckResult,
            AssertionType assertion) {
        // call the policy engine to check the permission on the request
        CheckPolicyRequestType policyReq = getPolicyChecker().checkPolicyAdhocQueryResponse(policyCheckResult);
        policyReq.setAssertion(assertion);
        return policyReq;
    }

    /**
     * check the policy engine's response, return true if response = permit.
     *
     * @param policyReq policyreq received.
     * @param assertion Assertion received.
     * @return return true if response = permit
     */
    protected boolean checkPolicy(CheckPolicyRequestType policyReq, AssertionType assertion) {
        CheckPolicyResponseType policyResp = getPolicyEngine().checkPolicy(policyReq, assertion);
        return validatePolicyResponse(policyResp);
    }

    /**
     * @return PolicyEngineChecker.
     */
    protected PolicyEngineChecker getPolicyChecker() {
        return new PolicyEngineChecker();
    }

    /**
     * @return policyEngine Bean instantiated.
     */
    protected PolicyEngineProxy getPolicyEngine() {
        return (new PolicyEngineProxyObjectFactory()).getPolicyEngineProxy();
    }

    /**
     * This method validates Policy in the Response.
     * @param policyResp policyResponse received.
     * @return true if Permit; else false.
     */
    protected boolean validatePolicyResponse(CheckPolicyResponseType policyResp) {
        return policyResp.getResponse() != null && NullChecker.isNotNullish(policyResp.getResponse().getResult())
                && policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT;
    }
}
