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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionDeferredRequestAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.OutboundDocSubmissionDeferredRequestDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.OutboundDocSubmissionDeferredRequestOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

public class PassthroughOutboundDocSubmissionDeferredRequest implements OutboundDocSubmissionDeferredRequest {

    private DocSubmissionDeferredRequestAuditLogger auditLogger = null;

    public PassthroughOutboundDocSubmissionDeferredRequest() {
        auditLogger = getAuditLogger();
    }

    @Override
    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(
        ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion,
        NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {
        logInfoServiceProcess(this.getClass());
        assertion = MessageGeneratorUtils.getInstance().generateMessageId(assertion);
        NhinTargetSystemType targetSystem = MessageGeneratorUtils.getInstance().convertFirstToNhinTargetSystemType(targets);
        auditRequest(body, assertion, targetSystem);
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = createRequestForInternalProcessing(
            body, targetSystem);

        OutboundDocSubmissionDeferredRequestDelegate delegate = getOutboundDocSubmissionDeferredRequestDelegate();
        OutboundDocSubmissionDeferredRequestOrchestratable dsOrchestratable = createOrchestratable(delegate, request,
            assertion);
        return ((OutboundDocSubmissionDeferredRequestOrchestratable) delegate
            .process(dsOrchestratable)).getResponse();
    }

    private OutboundDocSubmissionDeferredRequestOrchestratable createOrchestratable(
        OutboundDocSubmissionDeferredRequestDelegate delegate,
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {
        OutboundDocSubmissionDeferredRequestOrchestratable orchestratable = new OutboundDocSubmissionDeferredRequestOrchestratable(
            delegate);
        orchestratable.setAssertion(assertion);
        orchestratable.setRequest(request.getProvideAndRegisterDocumentSetRequest());
        orchestratable.setTarget(request.getNhinTargetSystem());

        return orchestratable;
    }

    private RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType createRequestForInternalProcessing(
        ProvideAndRegisterDocumentSetRequestType body, NhinTargetSystemType targetSystem) {
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        request.setNhinTargetSystem(targetSystem);
        request.setProvideAndRegisterDocumentSetRequest(body);

        return request;
    }

    protected OutboundDocSubmissionDeferredRequestDelegate getOutboundDocSubmissionDeferredRequestDelegate() {
        return new OutboundDocSubmissionDeferredRequestDelegate();
    }

    protected DocSubmissionDeferredRequestAuditLogger getAuditLogger() {
        return new DocSubmissionDeferredRequestAuditLogger();
    }

    private void auditRequest(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion,
        NhinTargetSystemType target) {
        auditLogger.auditRequestMessage(request, assertion, target, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
            NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.TRUE, null, NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME);
    }
}
