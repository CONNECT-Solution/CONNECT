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
package gov.hhs.fha.nhinc.docsubmission.passthru;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class PassthruDocSubmissionOrchImpl {
    private Log log = LogFactory.getLog(PassthruDocSubmissionOrchImpl.class);

    public PassthruDocSubmissionOrchImpl() {
        log = getLogger();
    }

    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType body,
            AssertionType assertion, NhinTargetSystemType targetSystem) {

        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = createRequestForInternalProcessing(
                body, assertion, targetSystem);

        auditRequestToNhin(request, assertion);

        OutboundDocSubmissionDelegate dsDelegate = getOutboundDocSubmissionDelegate();
        OutboundDocSubmissionOrchestratable dsOrchestratable = createOrchestratable(dsDelegate, request, assertion);
        RegistryResponseType response = ((OutboundDocSubmissionOrchestratable) dsDelegate.process(dsOrchestratable))
                .getResponse();

        auditResponseFromNhin(response, assertion);

        return response;
    }

    protected Log getLogger() {
        return log;
    }
        
    protected XDRAuditLogger getXDRAuditLogger() {
        return new XDRAuditLogger();
    }

    protected OutboundDocSubmissionDelegate getOutboundDocSubmissionDelegate() {
        return new OutboundDocSubmissionDelegate();
    }
    
    private void auditRequestToNhin(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request,
            AssertionType assertion) {
        getXDRAuditLogger().auditXDR(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    private void auditResponseFromNhin(RegistryResponseType response, AssertionType assertion) {
        getXDRAuditLogger().auditNhinXDRResponse(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    private OutboundDocSubmissionOrchestratable createOrchestratable(OutboundDocSubmissionDelegate delegate,
            RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {
        
        OutboundDocSubmissionOrchestratable dsOrchestratable = new OutboundDocSubmissionOrchestratable(delegate);
        dsOrchestratable.setAssertion(assertion);
        dsOrchestratable.setRequest(request.getProvideAndRegisterDocumentSetRequest());
        dsOrchestratable.setTarget(request.getNhinTargetSystem());

        return dsOrchestratable;
    }

    private RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType createRequestForInternalProcessing(
            ProvideAndRegisterDocumentSetRequestType msg, AssertionType assertion, NhinTargetSystemType targetSystem) {

        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        request.setNhinTargetSystem(targetSystem);
        request.setProvideAndRegisterDocumentSetRequest(msg);

        return request;
    }
}
