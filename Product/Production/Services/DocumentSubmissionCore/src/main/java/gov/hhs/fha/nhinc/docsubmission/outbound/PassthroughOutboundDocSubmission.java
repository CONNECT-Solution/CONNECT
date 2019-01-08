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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public class PassthroughOutboundDocSubmission implements OutboundDocSubmission {

    private DocSubmissionAuditLogger auditLogger = new DocSubmissionAuditLogger();
    private OutboundDocSubmissionDelegate dsDelegate = new OutboundDocSubmissionDelegate();

    public PassthroughOutboundDocSubmission() {
        super();
    }

    public PassthroughOutboundDocSubmission(DocSubmissionAuditLogger auditLogger, OutboundDocSubmissionDelegate dsDelegate) {
        this.auditLogger = auditLogger;
        this.dsDelegate = dsDelegate;
    }

    @Override
    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType body,
        AssertionType assertion, NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {
        logInfoServiceProcess(this.getClass());

        NhinTargetSystemType targetSystem = MessageGeneratorUtils.getInstance().convertFirstToNhinTargetSystemType(
            targets);
        assertion = MessageGeneratorUtils.getInstance().generateMessageId(assertion);
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = createAuditRequest(body, assertion,
            targetSystem);

        auditRequest(request, assertion, request.getNhinTargetSystem());

        OutboundDocSubmissionOrchestratable dsOrchestratable = createOrchestratable(dsDelegate, body, targetSystem,
            assertion);
        RegistryResponseType response = ((OutboundDocSubmissionOrchestratable) dsDelegate.process(dsOrchestratable))
            .getResponse();

        return response;
    }

    private void auditRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request,
        AssertionType assertion, NhinTargetSystemType target) {
        auditLogger.auditRequestMessage(request.getProvideAndRegisterDocumentSetRequest(), assertion, target,
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.TRUE, null,
            NhincConstants.NHINC_XDR_SERVICE_NAME);
    }

    private OutboundDocSubmissionOrchestratable createOrchestratable(OutboundDocSubmissionDelegate delegate,
        ProvideAndRegisterDocumentSetRequestType request, NhinTargetSystemType targetSystem, AssertionType assertion) {

        OutboundDocSubmissionOrchestratable dsOrchestratable = new OutboundDocSubmissionOrchestratable(delegate);
        dsOrchestratable.setAssertion(assertion);
        dsOrchestratable.setRequest(request);
        dsOrchestratable.setTarget(targetSystem);

        return dsOrchestratable;
    }

    private RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType createAuditRequest(
        ProvideAndRegisterDocumentSetRequestType msg, AssertionType assertion, NhinTargetSystemType targetSystem) {

        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request
        = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        request.setNhinTargetSystem(targetSystem);
        request.setProvideAndRegisterDocumentSetRequest(msg);

        return request;
    }
}
