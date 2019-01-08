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
package gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a concrete implementation of the AdapterPolicyEngineOrchestrator interface that always returns a deny.
 *
 * @author Les Westberg
 */
public class AdapterPolicyEngineOrchProxyDenyNoOpImpl implements AdapterPolicyEngineOrchProxy {
    private static final Logger LOG = LoggerFactory.getLogger(AdapterPolicyEngineOrchProxyDenyNoOpImpl.class);

    /**
     * Given a request to check the access policy, this service will always return a deny response.
     *
     * @param checkPolicyRequest The request to check defined policy
     * @return The response which contains the access decision
     */
    @Override
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest, AssertionType assertion) {
        LOG.trace("Begin AdapterPolicyEngineOrchProxyDenyNoOpImpl.checkPolicy");
        CheckPolicyResponseType oPolicyResponse = new CheckPolicyResponseType();
        ResponseType oResponse = new ResponseType();
        ResultType oResult = new ResultType();
        oResult.setDecision(DecisionType.DENY);
        oResponse.getResult().add(oResult);
        oPolicyResponse.setResponse(oResponse);
        LOG.trace("End AdapterPolicyEngineOrchProxyDenyNoOpImpl.checkPolicy");
        return oPolicyResponse;
    }
}
