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
package gov.hhs.fha.nhinc.docsubmission.entity.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.passthru.deferred.response.proxy.PassthruDocSubmissionDeferredResponseProxy;
import gov.hhs.fha.nhinc.docsubmission.passthru.deferred.response.proxy.PassthruDocSubmissionDeferredResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.policy.SubjectHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author jhoppesc
 */
public class EntityDocSubmissionDeferredResponseOrchImpl {

    private Log log = null;
    private XDRAuditLogger auditLogger = null;

    public EntityDocSubmissionDeferredResponseOrchImpl() {
        log = createLogger();
        auditLogger = createAuditLogger();
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(RegistryResponseType request,
            AssertionType assertion, NhinTargetCommunitiesType targets) {
        log.info("Begin provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, AssertionType)");
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        response.setMessage(regResp);
        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterDocumentSetSecuredResponseRequest = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
        provideAndRegisterDocumentSetSecuredResponseRequest.setNhinTargetCommunities(targets);
        provideAndRegisterDocumentSetSecuredResponseRequest.setRegistryResponse(request);

        logRequest(provideAndRegisterDocumentSetSecuredResponseRequest, assertion);

        if (provideAndRegisterDocumentSetSecuredResponseRequest != null
                && provideAndRegisterDocumentSetSecuredResponseRequest.getNhinTargetCommunities() != null
                && NullChecker.isNotNullish(provideAndRegisterDocumentSetSecuredResponseRequest
                        .getNhinTargetCommunities().getNhinTargetCommunity())
                && provideAndRegisterDocumentSetSecuredResponseRequest.getNhinTargetCommunities()
                        .getNhinTargetCommunity().get(0) != null
                && provideAndRegisterDocumentSetSecuredResponseRequest.getNhinTargetCommunities()
                        .getNhinTargetCommunity().get(0).getHomeCommunity() != null
                && NullChecker.isNotNullish(provideAndRegisterDocumentSetSecuredResponseRequest
                        .getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity()
                        .getHomeCommunityId())) {

            if (checkPolicy(provideAndRegisterDocumentSetSecuredResponseRequest, assertion)) {
                log.info("Policy check successful");

                NhinTargetSystemType targetSystemType = new NhinTargetSystemType();
                targetSystemType.setHomeCommunity(provideAndRegisterDocumentSetSecuredResponseRequest
                        .getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity());

                gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType proxyRequest = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
                proxyRequest.setRegistryResponse(provideAndRegisterDocumentSetSecuredResponseRequest
                        .getRegistryResponse());
                proxyRequest.setNhinTargetSystem(targetSystemType);

                log.debug("Sending request from entity service to NHIN proxy service");
                response = callNhinXDRResponseProxy(proxyRequest, assertion);
            } else {
                log.error("Policy check unsuccessful");

            }
        } else {
            log.warn("There was not a target community provided in the Entity message");
        }

        logResponse(response, assertion);

        log.info("End provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, AssertionType)");
        return response;
    }

    protected XDRAcknowledgementType callNhinXDRResponseProxy(
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterResponseRequest,
            AssertionType assertion) {
        log.debug("Begin provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, AssertionType)");
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        response.setMessage(regResp);

        PassthruDocSubmissionDeferredResponseProxyObjectFactory factory = new PassthruDocSubmissionDeferredResponseProxyObjectFactory();
        PassthruDocSubmissionDeferredResponseProxy proxy = factory.getPassthruDocSubmissionDeferredResponseProxy();

        log.debug("Calling NHIN Proxy");
        response = proxy.provideAndRegisterDocumentSetBResponse(
                provideAndRegisterResponseRequest.getRegistryResponse(), assertion,
                provideAndRegisterResponseRequest.getNhinTargetSystem());

        log.debug("End provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, AssertionType)");
        return response;
    }

    private void logRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request,
            AssertionType assertion) {
        log.debug("Begin logRequest");
        auditLogger.auditEntityXDRResponseRequest(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        log.debug("End logRequest");
    }

    private void logResponse(XDRAcknowledgementType response, AssertionType assertion) {
        log.debug("Beging logResponse");
        auditLogger.auditEntityAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.XDR_RESPONSE_ACTION);
        log.debug("End logResponse");
    }

    protected XDRAuditLogger createAuditLogger() {
        return new XDRAuditLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected boolean checkPolicy(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request,
            AssertionType assertion) {
        log.debug("Begin checkPolicy");
        boolean bPolicyOk = false;

        if (request != null
                && request.getNhinTargetCommunities() != null
                && NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity())
                && request.getNhinTargetCommunities().getNhinTargetCommunity().get(0) != null
                && request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity() != null
                && NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity().get(0)
                        .getHomeCommunity().getHomeCommunityId())) {

            SubjectHelper subjHelp = new SubjectHelper();
            String senderHCID = subjHelp.determineSendingHomeCommunityId(assertion.getHomeCommunity(), assertion);
            String receiverHCID = request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity()
                    .getHomeCommunityId();
            String direction = NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION;
            log.debug("Checking the policy engine for the " + direction + " request from " + senderHCID + " to "
                    + receiverHCID);

            // return true if 'permit' returned, false otherwise
            XDRPolicyChecker policyChecker = new XDRPolicyChecker();
            bPolicyOk = policyChecker.checkXDRResponsePolicy(request.getRegistryResponse(), assertion, senderHCID,
                    receiverHCID, direction);
        } else {
            log.warn("EntityXDRResponseSecuredImpl check on policy requires a non null receiving home community ID specified in the RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType");
        }
        log.debug("EntityXDRResponseSecuredImpl check on policy returns: " + bPolicyOk);
        return bPolicyOk;
    }

}
