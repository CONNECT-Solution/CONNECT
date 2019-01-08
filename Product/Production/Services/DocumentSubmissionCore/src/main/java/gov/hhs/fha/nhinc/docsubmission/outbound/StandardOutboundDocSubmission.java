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
package gov.hhs.fha.nhinc.docsubmission.outbound;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.logInfoServiceProcess;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionOrchestratable;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.policy.SubjectHelper;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardOutboundDocSubmission implements OutboundDocSubmission {

    private static final Logger LOG = LoggerFactory.getLogger(StandardOutboundDocSubmission.class);
    private DocSubmissionAuditLogger auditLogger = null;

    public StandardOutboundDocSubmission() {
        auditLogger = getDocSubmissionAuditLogger();
    }

    @Override
    @OutboundProcessingEvent(beforeBuilder = DocSubmissionBaseEventDescriptionBuilder.class,
    afterReturningBuilder = DocSubmissionBaseEventDescriptionBuilder.class, serviceType = "Document Submission",
    version = "")
    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType body,
        AssertionType assertion, NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {
        logInfoServiceProcess(this.getClass());

        RegistryResponseType response;
        assertion = MessageGeneratorUtils.getInstance().generateMessageId(assertion);

        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = createRequestForInternalProcessing(
            body, targets, urlInfo);
        NhinTargetSystemType target = getMessageGeneratorUtils().convertFirstToNhinTargetSystemType(targets);
        auditRequest(request.getProvideAndRegisterDocumentSetRequest(), assertion, target);

        if (isPolicyValid(request, assertion)) {
            LOG.info("Policy check successful");
            response = getResponseFromTarget(request, assertion);
        } else {
            LOG.error("Failed policy check.  Sending error response.");
            response = MessageGeneratorUtils.getInstance().createFailedPolicyCheckResponse();
        }

        return response;
    }

    protected boolean hasNhinTargetHomeCommunityId(
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) {

        return request != null
            && request.getNhinTargetCommunities() != null
            && NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity())
            && request.getNhinTargetCommunities().getNhinTargetCommunity().get(0) != null
            && request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity() != null
            && NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity().get(0)
                .getHomeCommunity().getHomeCommunityId());
    }

    protected DocSubmissionAuditLogger getDocSubmissionAuditLogger() {
        return new DocSubmissionAuditLogger();
    }

    protected XDRPolicyChecker getXDRPolicyChecker() {
        return new XDRPolicyChecker();
    }

    protected SubjectHelper getSubjectHelper() {
        return new SubjectHelper();
    }

    protected MessageGeneratorUtils getMessageGeneratorUtils() {
        return MessageGeneratorUtils.getInstance();
    }

    protected OutboundDocSubmissionDelegate getOutboundDocSubmissionDelegate() {
        return new OutboundDocSubmissionDelegate();
    }

    private RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType createRequestForInternalProcessing(
        ProvideAndRegisterDocumentSetRequestType msg, NhinTargetCommunitiesType targets,
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

            isValid = getXDRPolicyChecker().checkXDRRequestPolicy(request.getProvideAndRegisterDocumentSetRequest(),
                assertion, senderHCID, receiverHCID, NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        } else {
            LOG.warn("Check on policy requires a non null target home community ID specified in the request");
        }
        LOG.debug("Check on policy returns: {}", isValid);

        return isValid;
    }

    private RegistryResponseType getResponseFromTarget(
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType nhinRequest;
        RegistryResponseType nhinResponse = new RegistryResponseType();
        if (hasNhinTargetHomeCommunityId(request)) {
            nhinRequest = createRequestForNhin(request);

            try {
                nhinResponse = sendToNhinProxy(nhinRequest, assertion);
            } catch (Exception e) {
                String hcid = getNhinTargetHomeCommunityId(request);
                nhinResponse = MessageGeneratorUtils.getInstance().createRegistryBusyErrorResponse("Failed to send "
                    + "request to community " + hcid);
                throw new ErrorEventException(e, nhinResponse, "Unable to call Nhin Doc Submission");
            }
        } else {
            LOG.warn("The request to the Nhin did not contain a target home community id.");
        }

        return nhinResponse;
    }

    private gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType
    createRequestForNhin(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) {

        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType nhinRequest
        = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();

        nhinRequest.setNhinTargetSystem(getMessageGeneratorUtils().convertFirstToNhinTargetSystemType(
            request.getNhinTargetCommunities()));
        nhinRequest.setProvideAndRegisterDocumentSetRequest(request.getProvideAndRegisterDocumentSetRequest());

        return nhinRequest;
    }

    private RegistryResponseType sendToNhinProxy(
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request,
        AssertionType assertion) {

        OutboundDocSubmissionDelegate dsDelegate = getOutboundDocSubmissionDelegate();
        OutboundDocSubmissionOrchestratable dsOrchestratable = createOrchestratable(dsDelegate, request, assertion);

        return ((OutboundDocSubmissionOrchestratable) dsDelegate.process(dsOrchestratable)).getResponse();
    }

    protected OutboundDocSubmissionOrchestratable createOrchestratable(
        OutboundDocSubmissionDelegate delegate,
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request,
        AssertionType assertion) {

        OutboundDocSubmissionOrchestratable dsOrchestratable = new OutboundDocSubmissionOrchestratable(delegate);
        dsOrchestratable.setAssertion(assertion);
        dsOrchestratable.setRequest(request.getProvideAndRegisterDocumentSetRequest());
        dsOrchestratable.setTarget(request.getNhinTargetSystem());

        return dsOrchestratable;
    }

    private void auditRequest(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion,
        NhinTargetSystemType target) {
        auditLogger.auditRequestMessage(request, assertion, target, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
            NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.TRUE, null, NhincConstants.NHINC_XDR_SERVICE_NAME);
    }

    private HomeCommunityType getNhinTargetHomeCommunity(
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) {
        return request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity();
    }

    private String getNhinTargetHomeCommunityId(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request) {
        return getNhinTargetHomeCommunity(request).getHomeCommunityId();
    }
}
