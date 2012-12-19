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
package gov.hhs.fha.nhinc.docsubmission.outbound.deferred.response;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.aspect.DeferredResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionArgTransformerBuilder;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.OutboundDocSubmissionDeferredResponseDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.OutboundDocSubmissionDeferredResponseOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.policy.SubjectHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.log4j.Logger;

public class StandardOutboundDocSubmissionDeferredResponse implements OutboundDocSubmissionDeferredResponse {

    private static final Logger LOG = Logger.getLogger(StandardOutboundDocSubmissionDeferredResponse.class);
    private XDRAuditLogger auditLogger = null;

    public StandardOutboundDocSubmissionDeferredResponse() {
        auditLogger = getXDRAuditLogger();
    }

    @OutboundProcessingEvent(beforeBuilder = DeferredResponseDescriptionBuilder.class,
            afterReturningBuilder = DocSubmissionArgTransformerBuilder.class,
            serviceType = "Document Submission Deferred Response", version = "")
    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(RegistryResponseType request,
            AssertionType assertion, NhinTargetCommunitiesType targets) {

        XDRAcknowledgementType response = null;

        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType internalRequest = createRequestForInternalProcessing(
                request, targets);

        auditRequestFromAdapter(internalRequest, assertion);

        if (isPolicyValid(internalRequest, assertion)) {
            LOG.info("Policy check successful");
            response = getResponseFromTarget(internalRequest, assertion);
        } else {
            LOG.error("Failed policy check.  Sending error response.");
            response = createFailedPolicyCheckResponse();
        }

        auditResponseToAdapter(response, assertion);

        return response;
    }

    private RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType createRequestForInternalProcessing(
            RegistryResponseType msg, NhinTargetCommunitiesType targets) {
        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
        request.setNhinTargetCommunities(targets);
        request.setRegistryResponse(msg);

        return request;
    }

    private boolean isPolicyValid(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request,
            AssertionType assertion) {
        boolean isValid = false;

        if (hasNhinTargetHomeCommunityId(request)) {
            String senderHCID = getSubjectHelper().determineSendingHomeCommunityId(assertion.getHomeCommunity(),
                    assertion);
            String receiverHCID = getNhinTargetHomeCommunityId(request);
            String direction = NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION;

            isValid = getXDRPolicyChecker().checkXDRResponsePolicy(request.getRegistryResponse(), assertion,
                    senderHCID, receiverHCID, direction);
        } else {
            LOG.warn("Check on policy requires a non null target home community ID specified in the request");
        }
        LOG.debug("Check on policy returns: " + isValid);

        return isValid;
    }

    private XDRAcknowledgementType getResponseFromTarget(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request, AssertionType assertion) {
        NhinTargetSystemType targetSystemType = new NhinTargetSystemType();
        targetSystemType.setHomeCommunity(getNhinTargetHomeCommunity(request));

        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType nhinRequest = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
        nhinRequest.setRegistryResponse(request.getRegistryResponse());
        nhinRequest.setNhinTargetSystem(targetSystemType);

        return sendToNhinProxy(nhinRequest, assertion);
    }

    private XDRAcknowledgementType sendToNhinProxy(
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request,
            AssertionType assertion) {

        OutboundDocSubmissionDeferredResponseDelegate delegate = getOutboundDocSubmissionDeferredResponseDelegate();
        OutboundDocSubmissionDeferredResponseOrchestratable orchestratable = createOrchestratable(delegate, request,
                assertion);
        return ((OutboundDocSubmissionDeferredResponseOrchestratable) delegate.process(orchestratable)).getResponse();
    }

    private OutboundDocSubmissionDeferredResponseOrchestratable createOrchestratable(
            OutboundDocSubmissionDeferredResponseDelegate delegate,
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request,
            AssertionType assertion) {

        OutboundDocSubmissionDeferredResponseOrchestratable orchestratable = new OutboundDocSubmissionDeferredResponseOrchestratable(
                delegate);
        orchestratable.setAssertion(assertion);
        orchestratable.setRequest(request.getRegistryResponse());
        orchestratable.setTarget(request.getNhinTargetSystem());

        return orchestratable;
    }

    private XDRAcknowledgementType createFailedPolicyCheckResponse() {
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);

        XDRAcknowledgementType response = new XDRAcknowledgementType();
        response.setMessage(regResp);

        return response;
    }

    private void auditRequestFromAdapter(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request, AssertionType assertion) {
        auditLogger.auditEntityXDRResponseRequest(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    private void auditResponseToAdapter(XDRAcknowledgementType response, AssertionType assertion) {
        auditLogger.auditEntityAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.XDR_RESPONSE_ACTION);
    }

    protected boolean hasNhinTargetHomeCommunityId(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request) {

        if (request != null
                && request.getNhinTargetCommunities() != null
                && NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity())
                && request.getNhinTargetCommunities().getNhinTargetCommunity().get(0) != null
                && request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity() != null
                && NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity().get(0)
                        .getHomeCommunity().getHomeCommunityId())) {
            return true;
        }

        return false;
    }

    private HomeCommunityType getNhinTargetHomeCommunity(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request) {
        return request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity();
    }

    private String getNhinTargetHomeCommunityId(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request) {
        return getNhinTargetHomeCommunity(request).getHomeCommunityId();
    }

    protected XDRAuditLogger getXDRAuditLogger() {
        return new XDRAuditLogger();
    }

    protected SubjectHelper getSubjectHelper() {
        return new SubjectHelper();
    }

    protected XDRPolicyChecker getXDRPolicyChecker() {
        return new XDRPolicyChecker();
    }

    protected OutboundDocSubmissionDeferredResponseDelegate getOutboundDocSubmissionDeferredResponseDelegate() {
        return new OutboundDocSubmissionDeferredResponseDelegate();
    }

}
