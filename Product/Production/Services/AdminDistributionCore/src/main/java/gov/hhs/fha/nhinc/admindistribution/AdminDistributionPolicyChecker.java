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
package gov.hhs.fha.nhinc.admindistribution;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class AdminDistributionPolicyChecker {

    private static final Logger LOG = LoggerFactory.getLogger(AdminDistributionPolicyChecker.class);

    /**
     * @param request SendAlertMessage Request received.
     * @param target Nhin Target
     * @return true if checkPolicy is Permit; else denied.
     */
    public boolean checkOutgoingPolicy(RespondingGatewaySendAlertMessageType request, String target) {
        LOG.debug("checking the policy engine for the new request to a target community");

        gov.hhs.fha.nhinc.transform.policy.AdminDistributionTransformHelper policyHelper;

        policyHelper = new gov.hhs.fha.nhinc.transform.policy.AdminDistributionTransformHelper();

        CheckPolicyRequestType checkPolicyRequest = policyHelper.transformEntityAlertToCheckPolicy(request, target);

        return invokePolicyEngine(checkPolicyRequest);
    }

    /**
     * This method checks the incoming policy and returns boolean.
     * 
     * @param request Emergency Message Distribution Element transaction message request.
     * @param assertion Assertion received.
     * @return true or false.
     */
    public boolean checkIncomingPolicy(EDXLDistribution request, AssertionType assertion) {
        LOG.debug("checking the policy engine for the new request to a target community");

        gov.hhs.fha.nhinc.transform.policy.AdminDistributionTransformHelper policyHelper;

        policyHelper = new gov.hhs.fha.nhinc.transform.policy.AdminDistributionTransformHelper();

        CheckPolicyRequestType checkPolicyRequest = policyHelper.transformNhinAlertToCheckPolicy(request, assertion);

        return invokePolicyEngine(checkPolicyRequest);
    }

    /**
     * This method returns boolean and true if policycheck is Permit; else denied.
     * 
     * @param policyCheckReq CheckPolicyRequestType request received.
     * @return boolean true if Permit;else denied.
     */
    protected boolean invokePolicyEngine(CheckPolicyRequestType policyCheckReq) {
        boolean policyIsValid;

        LOG.debug("start invokePolicyEngine");
        /* invoke check policy */
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        AssertionType assertion = null;
        if (policyCheckReq != null) {
            assertion = policyCheckReq.getAssertion();
        }
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyCheckReq, assertion);

        /* if response='permit' */
        if (policyResp.getResponse() != null && NullChecker.isNotNullish(policyResp.getResponse().getResult())
                && policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            LOG.debug("Policy engine check returned permit.");
            policyIsValid = true;
        } else {
            LOG.debug("Policy engine check returned deny.");
            policyIsValid = false;
        }

        return policyIsValid;
    }

}
