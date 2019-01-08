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
package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import gov.hhs.fha.nhinc.policyengine.DocumentRetrievePolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;

/**
 *
 * @author mweaver
 */
public class InboundDocRetrievePolicyTransformer_g0 implements PolicyTransformer {

    private  DocumentRetrievePolicyEngineChecker policyChecker;


    public InboundDocRetrievePolicyTransformer_g0() {
        policyChecker = new PolicyEngineChecker();
    }

    public InboundDocRetrievePolicyTransformer_g0(DocumentRetrievePolicyEngineChecker policyChecker) {
        this.policyChecker = policyChecker;
    }

    /**
     * @param message
     * @param direction - doesn't make sense. This is inbound.
     */
    @Override
    public CheckPolicyRequestType transform(Orchestratable message, Direction direction) {
        CheckPolicyRequestType policyReq = null;
        if (message instanceof InboundDocRetrieveOrchestratable) {
            policyReq = transformInbound((InboundDocRetrieveOrchestratable) message);
        }
        return policyReq;
    }

    /**
     * To be removed!
     * @param direction
     * @param orchestratable
     * @return
     */
    public CheckPolicyRequestType transformOutbound(InboundDocRetrieveOrchestratable orchestratable) {
        CheckPolicyRequestType policyReq;
        DocRetrieveEventType policyCheckReq = new DocRetrieveEventType();
            policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);

        gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType request = new gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType();
        request.setAssertion(orchestratable.getAssertion());
        request.setRetrieveDocumentSetRequest(orchestratable.getRequest());
        policyCheckReq.setMessage(request);
        policyReq = policyChecker.checkPolicyDocRetrieve(policyCheckReq);
        return policyReq;
    }

    /**
     * @param direction
     * @param orchestratable
     * @return
     */
    public CheckPolicyRequestType transformInbound(InboundDocRetrieveOrchestratable orchestratable) {
        DocRetrieveMessageType request = new DocRetrieveMessageType();
        request.setAssertion(orchestratable.getAssertion());
        request.setRetrieveDocumentSetRequest(orchestratable.getRequest());
        return transformInbound(request);
    }

    /**
     * @param request
     * @return
     */
    public CheckPolicyRequestType transformInbound(DocRetrieveMessageType request) {
        DocRetrieveEventType policyCheckReq = new DocRetrieveEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        policyCheckReq.setMessage(request);
        return policyChecker.checkPolicyDocRetrieve(policyCheckReq);
    }

}
