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
package gov.hhs.fha.nhinc.docsubmission.passthru.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.OutboundDocSubmissionDeferredResponseDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.OutboundDocSubmissionDeferredResponseOrchestratable;
import gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.proxy11.NhinDocSubmissionDeferredResponseProxy;
import gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.proxy11.NhinDocSubmissionDeferredResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PassthruDocSubmissionDeferredResponseOrchImpl {
    private Log log = LogFactory.getLog(PassthruDocSubmissionDeferredResponseOrchImpl.class);
    private XDRAuditLogger auditLogger = null;

    public PassthruDocSubmissionDeferredResponseOrchImpl() {
        log = getLogger();
        auditLogger = getXDRAuditLogger();
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body,
            AssertionType assertion, NhinTargetSystemType targetSystem) {

        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request = createRequestForInternalProcessing(
                body, targetSystem);

        auditRequestToNhin(request, assertion);

        OutboundDocSubmissionDeferredResponseDelegate delegate = getOutboundDocSubmissionDeferredResponseDelegate();
        OutboundDocSubmissionDeferredResponseOrchestratable dsOrchestratable = createOrchestratable(delegate, request,
                assertion);
        XDRAcknowledgementType response = ((OutboundDocSubmissionDeferredResponseOrchestratable) delegate
                .process(dsOrchestratable)).getResponse();

        auditResponseFromNhin(response, assertion);

        return response;
    }

    private OutboundDocSubmissionDeferredResponseOrchestratable createOrchestratable(
            OutboundDocSubmissionDeferredResponseDelegate delegate,
            RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request, AssertionType assertion) {
        OutboundDocSubmissionDeferredResponseOrchestratable orchestratable = new OutboundDocSubmissionDeferredResponseOrchestratable(
                delegate);
        orchestratable.setAssertion(assertion);
        orchestratable.setRequest(request.getRegistryResponse());
        orchestratable.setTarget(request.getNhinTargetSystem());

        return orchestratable;
    }

    private RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType createRequestForInternalProcessing(
            RegistryResponseType body, NhinTargetSystemType targetSystem) {
        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
        request.setNhinTargetSystem(targetSystem);
        request.setRegistryResponse(body);

        return request;
    }

    private void auditRequestToNhin(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request,
            AssertionType assertion) {
        auditLogger.auditNhinXDRResponseRequest(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    private void auditResponseFromNhin(XDRAcknowledgementType response, AssertionType assertion) {
        auditLogger.auditAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.XDR_RESPONSE_ACTION);
    }

    protected XDRAuditLogger getXDRAuditLogger() {
        return new XDRAuditLogger();
    }

    protected Log getLogger() {
        return log;
    }
    
    protected OutboundDocSubmissionDeferredResponseDelegate getOutboundDocSubmissionDeferredResponseDelegate() {
        return new OutboundDocSubmissionDeferredResponseDelegate();
    }

    protected NhinDocSubmissionDeferredResponseProxy createNhinProxy() {
        return new NhinDocSubmissionDeferredResponseProxyObjectFactory().getNhinDocSubmissionDeferredResponseProxy();
    }

}
