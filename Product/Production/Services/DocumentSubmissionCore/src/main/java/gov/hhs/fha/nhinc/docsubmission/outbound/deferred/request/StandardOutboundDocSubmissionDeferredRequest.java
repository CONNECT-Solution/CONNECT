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
package gov.hhs.fha.nhinc.docsubmission.outbound.deferred.request;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.logInfoServiceProcess;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionArgTransformerBuilder;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionDeferredRequestAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.OutboundDocSubmissionDeferredRequestDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.OutboundDocSubmissionDeferredRequestOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.policy.SubjectHelper;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardOutboundDocSubmissionDeferredRequest implements OutboundDocSubmissionDeferredRequest {

    private static final Logger LOG = LoggerFactory.getLogger(StandardOutboundDocSubmissionDeferredRequest.class);
    private DocSubmissionDeferredRequestAuditLogger auditLogger = null;

    public StandardOutboundDocSubmissionDeferredRequest() {
        auditLogger = getAuditLogger();
    }

    @OutboundProcessingEvent(beforeBuilder = DocSubmissionBaseEventDescriptionBuilder.class,
        afterReturningBuilder = DocSubmissionArgTransformerBuilder.class,
        serviceType = "Document Submission Deferred Request", version = "")
    @Override
    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(
        ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion,
        NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {
        logInfoServiceProcess(this.getClass());

        XDRAcknowledgementType response;
        assertion = MessageGeneratorUtils.getInstance().generateMessageId(assertion);
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType internalRequest
        = createRequestForInternalProcessing(request, assertion, targets, urlInfo);

        auditRequest(request, assertion, MessageGeneratorUtils.getInstance().convertFirstToNhinTargetSystemType(targets));

        if (isPolicyValid(internalRequest, assertion)) {
            LOG.info("Policy check successful");
            response = getResponseFromTarget(internalRequest, assertion);
        } else {
            LOG.error("Failed policy check.  Sending error response.");
            response = createFailedPolicyCheckResponse();
        }
        return response;
    }

    private static RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType createRequestForInternalProcessing(
        ProvideAndRegisterDocumentSetRequestType msg, AssertionType assertion, NhinTargetCommunitiesType targets,
        UrlInfoType urlInfo) {
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request
        = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        request.setProvideAndRegisterDocumentSetRequest(msg);
        request.setNhinTargetCommunities(targets);
        request.setUrl(urlInfo);

        return request;
    }

    protected boolean isPolicyValid(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request,
        AssertionType assertion) {
        boolean isValid = false;

        if (hasNhinTargetHomeCommunityId(request)) {

            String senderHCID = getSubjectHelper().determineSendingHomeCommunityId(assertion.getHomeCommunity(),
                assertion);
            String receiverHCID = getNhinTargetHomeCommunityId(request);
            String direction = NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION;

            isValid = getXDRPolicyChecker().checkXDRRequestPolicy(request.getProvideAndRegisterDocumentSetRequest(),
                assertion, senderHCID, receiverHCID, direction);
        } else {
            LOG.warn("Check on policy requires a non null target home community ID specified in the request");
        }
        LOG.debug("Check on policy returns: {}", isValid);

        return isValid;
    }

    private XDRAcknowledgementType getResponseFromTarget(
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {

        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType nhinRequest
        = createRequestForNhin(request);

        return sendToNhinProxy(nhinRequest, assertion);
    }

    private static gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType
    createRequestForNhin(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) {

        NhinTargetSystemType targetSystemType = MessageGeneratorUtils.getInstance().convertFirstToNhinTargetSystemType(
            request.getNhinTargetCommunities());
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType nhinRequest
        = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        nhinRequest.setNhinTargetSystem(targetSystemType);
        nhinRequest.setProvideAndRegisterDocumentSetRequest(request.getProvideAndRegisterDocumentSetRequest());

        return nhinRequest;
    }

    private XDRAcknowledgementType sendToNhinProxy(
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request,
        AssertionType assertion) {

        OutboundDocSubmissionDeferredRequestDelegate delegate = getOutboundDocSubmissionDeferredRequestDelegate();
        OutboundDocSubmissionDeferredRequestOrchestratable orchestratable = createOrchestratable(delegate, request,
            assertion);
        return ((OutboundDocSubmissionDeferredRequestOrchestratable) delegate.process(orchestratable)).getResponse();
    }

    protected static OutboundDocSubmissionDeferredRequestOrchestratable createOrchestratable(
        OutboundDocSubmissionDeferredRequestDelegate delegate,
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request,
        AssertionType assertion) {

        OutboundDocSubmissionDeferredRequestOrchestratable orchestratable
        = new OutboundDocSubmissionDeferredRequestOrchestratable(delegate);
        orchestratable.setAssertion(assertion);
        orchestratable.setRequest(request.getProvideAndRegisterDocumentSetRequest());
        orchestratable.setTarget(request.getNhinTargetSystem());

        return orchestratable;
    }

    private static XDRAcknowledgementType createFailedPolicyCheckResponse() {
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);

        XDRAcknowledgementType response = new XDRAcknowledgementType();
        response.setMessage(regResp);

        return response;
    }

    private void auditRequest(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion,
        NhinTargetSystemType target) {
        auditLogger.auditRequestMessage(request, assertion, target, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
            NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.TRUE, null, NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME);
    }

    private static HomeCommunityType getNhinTargetHomeCommunity(
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) {
        return request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity();
    }

    private static String getNhinTargetHomeCommunityId(
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) {
        return getNhinTargetHomeCommunity(request).getHomeCommunityId();
    }

    protected static boolean hasNhinTargetHomeCommunityId(
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) {

        return request != null
            && request.getNhinTargetCommunities() != null
            && NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity())
            && request.getNhinTargetCommunities().getNhinTargetCommunity().get(0) != null
            && request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity() != null
            && NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity().get(0)
                .getHomeCommunity().getHomeCommunityId());
    }

    protected DocSubmissionDeferredRequestAuditLogger getAuditLogger() {
        return new DocSubmissionDeferredRequestAuditLogger();
    }

    protected XDRPolicyChecker getXDRPolicyChecker() {
        return new XDRPolicyChecker();
    }

    protected SubjectHelper getSubjectHelper() {
        return new SubjectHelper();
    }

    protected OutboundDocSubmissionDeferredRequestDelegate getOutboundDocSubmissionDeferredRequestDelegate() {
        return new OutboundDocSubmissionDeferredRequestDelegate();
    }
}
