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
package gov.hhs.fha.nhinc.docretrieve;

import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveResponseMessageType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveResultEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.policy.DocRetrieveDeferredTransformHelper;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class DocRetrieveDeferredPolicyChecker {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public DocRetrieveDeferredPolicyChecker() {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
    }

    /**
     *
     * @return Log
     */
    protected Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     * 
     * @param request
     * @param assertion
     * @param target
     * @return boolean
     */
    public boolean checkOutgoingPolicy(RetrieveDocumentSetResponseType request, AssertionType assertion, String target) {
        if (debugEnabled) {
            log.debug("-- Begin DocRetrieveDeferredPolicyChecker.checkOutgoingPolicy() --");
        }
        if (debugEnabled) {
            log.debug("checking the policy engine for the new response to a target community");
        }
        DocRetrieveDeferredTransformHelper policyHelper = new DocRetrieveDeferredTransformHelper();
        DocRetrieveResultEventType eventRequest = new DocRetrieveResultEventType();
        DocRetrieveResponseMessageType respMessage = new DocRetrieveResponseMessageType();
        respMessage.setRetrieveDocumentSetResponse(request);
        respMessage.setAssertion(assertion);
        eventRequest.setMessage(respMessage);
        eventRequest.setMessage(respMessage);
        eventRequest.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        eventRequest.setInterface("Entity");
        if (null != assertion) {
            HomeCommunityType loclHC = assertion.getHomeCommunity();
            eventRequest.setSendingHomeCommunity(loclHC);
        }
        HomeCommunityType remoteHC = new HomeCommunityType();
        remoteHC.setHomeCommunityId(target);
        eventRequest.setReceivingHomeCommunity(remoteHC);
        CheckPolicyRequestType checkPolicyRequest = policyHelper.transformDocRetrieveDeferredRespToCheckPolicy(eventRequest);
        if (debugEnabled) {
            log.debug("-- End DocRetrieveDeferredPolicyChecker.checkOutgoingPolicy() --");
        }
        return invokePolicyEngine(checkPolicyRequest);
    }

    /**
     * 
     * @param request
     * @param assertion
     * @return boolean
     */
    public boolean checkIncomingPolicy(RetrieveDocumentSetResponseType request, AssertionType assertion) {
        if (debugEnabled) {
            log.debug("-- Begin DocRetrieveDeferredPolicyChecker.checkIncomingPolicy() --");
        }
        if (debugEnabled) {
            log.debug("checking the policy engine for the new request to a target community");
        }
        DocRetrieveDeferredTransformHelper policyHelper = new DocRetrieveDeferredTransformHelper();
        DocRetrieveResultEventType eventRequest = new DocRetrieveResultEventType();
        DocRetrieveResponseMessageType respMessage = new DocRetrieveResponseMessageType();
        respMessage.setRetrieveDocumentSetResponse(request);
        respMessage.setRetrieveDocumentSetResponse(request);
        eventRequest.setMessage(respMessage);
        eventRequest.setMessage(respMessage);
        eventRequest.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        eventRequest.setInterface("Nhin");
        if (null != assertion) {
            HomeCommunityType loclHC = assertion.getHomeCommunity();
            eventRequest.setSendingHomeCommunity(loclHC);
        }
        if (null != request && null != request.getDocumentResponse() && request.getDocumentResponse().size() > 0) {
            HomeCommunityType remoteHC = new HomeCommunityType();
            remoteHC.setHomeCommunityId(request.getDocumentResponse().get(0).getHomeCommunityId());
            eventRequest.setReceivingHomeCommunity(remoteHC);
        }
        CheckPolicyRequestType checkPolicyRequest = policyHelper.transformDocRetrieveDeferredRespToCheckPolicy(eventRequest);
        if (debugEnabled) {
            log.debug("-- End DocRetrieveDeferredPolicyChecker.checkIncomingPolicy() --");
        }
        return invokePolicyEngine(checkPolicyRequest);
    }

    /**
     * 
     * @param policyCheckReq
     * @return boolean
     */
    protected boolean invokePolicyEngine(CheckPolicyRequestType policyCheckReq) {
        if (debugEnabled) {
            log.debug("Begin DocRetrieveDeferredPolicyChecker.invokePolicyEngine");
        }
        boolean policyIsValid = false;
        if (debugEnabled) {
            log.debug("start invokePolicyEngine");
        }
        /* invoke check policy */
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        AssertionType assertion = null;
        if(policyCheckReq != null)
        {
            assertion = policyCheckReq.getAssertion();
        }
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyCheckReq, assertion);

        /* if response='permit' */
        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            if (debugEnabled) {
                log.debug("Policy engine check returned permit.");
            }
            policyIsValid = true;
        } else {
            if (debugEnabled) {
                log.debug("Policy engine check returned deny.");
            }
            policyIsValid = false;
        }
        if (debugEnabled) {
            log.debug("End DocRetrieveDeferredPolicyChecker.invokePolicyEngine");
        }
        return policyIsValid;
    }

}
