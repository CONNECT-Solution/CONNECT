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
package gov.hhs.fha.nhinc.docsubmission.outbound.deferred.response;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.logInfoServiceProcess;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.audit.DSDeferredResponseAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.OutboundDocSubmissionDeferredResponseDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.OutboundDocSubmissionDeferredResponseOrchestratable;
import gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.proxy11.NhinDocSubmissionDeferredResponseProxy;
import gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.proxy11.NhinDocSubmissionDeferredResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public class PassthroughOutboundDocSubmissionDeferredResponse implements OutboundDocSubmissionDeferredResponse {

    private MessageGeneratorUtils msgUtils = MessageGeneratorUtils.getInstance();
    private DSDeferredResponseAuditLogger auditLogger = null;

    public PassthroughOutboundDocSubmissionDeferredResponse() {
        auditLogger = getAuditLogger();
    }

    @Override
    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(RegistryResponseType body,
        AssertionType assertion, NhinTargetCommunitiesType targets) {
        logInfoServiceProcess(this.getClass());

        NhinTargetSystemType targetSystem = msgUtils.convertFirstToNhinTargetSystemType(targets);
        assertion = MessageGeneratorUtils.getInstance().generateMessageId(assertion);
        OutboundDocSubmissionDeferredResponseDelegate delegate = getOutboundDocSubmissionDeferredResponseDelegate();
        OutboundDocSubmissionDeferredResponseOrchestratable dsOrchestratable = createOrchestratable(delegate, body,
            assertion, targetSystem);
        auditRequest(body, assertion, targetSystem);
        return ((OutboundDocSubmissionDeferredResponseOrchestratable) delegate
            .process(dsOrchestratable)).getResponse();
    }

    private OutboundDocSubmissionDeferredResponseOrchestratable createOrchestratable(
        OutboundDocSubmissionDeferredResponseDelegate delegate, RegistryResponseType request,
        AssertionType assertion, NhinTargetSystemType targetSystem) {
        OutboundDocSubmissionDeferredResponseOrchestratable orchestratable
        = new OutboundDocSubmissionDeferredResponseOrchestratable(delegate);
        orchestratable.setAssertion(assertion);
        orchestratable.setRequest(request);
        orchestratable.setTarget(targetSystem);

        return orchestratable;
    }

    protected OutboundDocSubmissionDeferredResponseDelegate getOutboundDocSubmissionDeferredResponseDelegate() {
        return new OutboundDocSubmissionDeferredResponseDelegate();
    }

    protected NhinDocSubmissionDeferredResponseProxy createNhinProxy() {
        return new NhinDocSubmissionDeferredResponseProxyObjectFactory().getNhinDocSubmissionDeferredResponseProxy();
    }

    private void auditRequest(RegistryResponseType request, AssertionType assertion,
        NhinTargetSystemType targets) {
        auditLogger.auditRequestMessage(request, assertion, targets,
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.TRUE, null,
            NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME);
    }

    protected DSDeferredResponseAuditLogger getAuditLogger() {
        return new DSDeferredResponseAuditLogger();
    }

}
