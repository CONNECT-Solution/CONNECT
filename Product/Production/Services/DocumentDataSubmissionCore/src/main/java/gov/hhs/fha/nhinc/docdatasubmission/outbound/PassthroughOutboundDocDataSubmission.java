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
package gov.hhs.fha.nhinc.docdatasubmission.outbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docdatasubmission.audit.DocDataSubmissionAuditLogger;
import gov.hhs.fha.nhinc.docdatasubmission.entity.OutboundDocDataSubmissionDelegate;
import gov.hhs.fha.nhinc.docdatasubmission.entity.OutboundDocDataSubmissionOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.policy.SubjectHelper;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import ihe.iti.xds_b._2007.RegisterDocumentSetRequestType;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PassthroughOutboundDocDataSubmission implements OutboundDocDataSubmission {

    private static final Logger LOG = LoggerFactory.getLogger(PassthroughOutboundDocDataSubmission.class);
    private DocDataSubmissionAuditLogger auditLogger = null;

    public PassthroughOutboundDocDataSubmission() {
        auditLogger = getDocDataSubmissionAuditLogger();
    }

    @Override
    public RegistryResponseType registerDocumentSetB(RegisterDocumentSetRequestType body, AssertionType assertion,
        NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {

        RegistryResponseType response;
        MessageGeneratorUtils.getInstance().generateMessageId(assertion);

        RespondingGatewayRegisterDocumentSetSecuredRequestType request = createRequestForInternalProcessing(body,
            targets, urlInfo);
        NhinTargetSystemType target = MessageGeneratorUtils.getInstance().convertFirstToNhinTargetSystemType(targets);
        auditRequest(request.getRegisterDocumentSetRequest(), assertion, target);

        response = getResponseFromTarget(request, assertion);
        return response;
    }

    protected boolean hasNhinTargetHomeCommunityId(RespondingGatewayRegisterDocumentSetSecuredRequestType request) {

        if (request != null && request.getNhinTargetCommunities() != null) {
            List<NhinTargetCommunityType> targetCommunities = request.getNhinTargetCommunities()
                .getNhinTargetCommunity();
            return targetCommunities.get(0) != null && targetCommunities.get(0).getHomeCommunity() != null
                && NullChecker.isNotNullish(targetCommunities.get(0).getHomeCommunity().getHomeCommunityId());
        }
        return false;
    }

    protected DocDataSubmissionAuditLogger getDocDataSubmissionAuditLogger() {
        return new DocDataSubmissionAuditLogger();
    }

    protected SubjectHelper getSubjectHelper() {
        return new SubjectHelper();
    }

    protected OutboundDocDataSubmissionDelegate getOutboundDocDataSubmissionDelegate() {
        return new OutboundDocDataSubmissionDelegate();
    }

    private static RespondingGatewayRegisterDocumentSetSecuredRequestType createRequestForInternalProcessing(
        RegisterDocumentSetRequestType msg, NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {
        RespondingGatewayRegisterDocumentSetSecuredRequestType request =
            new RespondingGatewayRegisterDocumentSetSecuredRequestType();
        request.setRegisterDocumentSetRequest(msg);
        request.setNhinTargetCommunities(targets);
        request.setUrl(urlInfo);

        return request;
    }

    private RegistryResponseType getResponseFromTarget(RespondingGatewayRegisterDocumentSetSecuredRequestType request,
        AssertionType assertion) {

        RegistryResponseType nhinResponse = new RegistryResponseType();
        if (hasNhinTargetHomeCommunityId(request)) {

            try {
                NhinTargetSystemType nhinTargetSystemType = MessageGeneratorUtils.getInstance()
                    .convertFirstToNhinTargetSystemType(request.getNhinTargetCommunities());
                nhinResponse = sendToNhinProxy(request, assertion, nhinTargetSystemType);
            } catch (Exception e) {
                String hcid = getNhinTargetHomeCommunityId(request);
                nhinResponse = MessageGeneratorUtils.getInstance()
                    .createRegistryBusyErrorResponse("Failed to send request to community " + hcid);
                LOG.error("Fault encountered while trying to send message to the nhin {}", hcid, e);
            }
        } else {
            LOG.warn("The request to the Nhin did not contain a target home community id.");
        }

        return nhinResponse;
    }

    private RegistryResponseType sendToNhinProxy(RespondingGatewayRegisterDocumentSetSecuredRequestType request,
        AssertionType assertion, NhinTargetSystemType nhinTargetSystemType) {

        OutboundDocDataSubmissionDelegate ddsDelegate = getOutboundDocDataSubmissionDelegate();
        OutboundDocDataSubmissionOrchestratable ddsOrchestratable = createOrchestratable(ddsDelegate, request,
            assertion);
        ddsOrchestratable.setTarget(nhinTargetSystemType);
        return ((OutboundDocDataSubmissionOrchestratable) ddsDelegate.process(ddsOrchestratable)).getResponse();

    }

    protected OutboundDocDataSubmissionOrchestratable createOrchestratable(OutboundDocDataSubmissionDelegate delegate,
        RespondingGatewayRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {

        OutboundDocDataSubmissionOrchestratable ddsOrchestratable = new OutboundDocDataSubmissionOrchestratable(
            delegate);
        ddsOrchestratable.setAssertion(assertion);
        ddsOrchestratable.setRequest(request.getRegisterDocumentSetRequest());

        return ddsOrchestratable;
    }

    private void auditRequest(RegisterDocumentSetRequestType request, AssertionType assertion,
        NhinTargetSystemType target) {
        auditLogger.auditRequestMessage(request, assertion, target, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
            NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.TRUE, null, NhincConstants.NHINC_XDS_SERVICE_NAME);
    }

    private static HomeCommunityType getNhinTargetHomeCommunity(
        RespondingGatewayRegisterDocumentSetSecuredRequestType request) {
        return request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity();
    }

    private static String getNhinTargetHomeCommunityId(RespondingGatewayRegisterDocumentSetSecuredRequestType request) {
        return getNhinTargetHomeCommunity(request).getHomeCommunityId();
    }
}
