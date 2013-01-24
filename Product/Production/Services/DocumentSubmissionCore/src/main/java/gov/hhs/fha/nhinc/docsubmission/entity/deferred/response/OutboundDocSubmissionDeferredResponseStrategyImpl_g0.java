/**
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
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.proxy11.NhinDocSubmissionDeferredResponseProxy;
import gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.proxy11.NhinDocSubmissionDeferredResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.log4j.Logger;

/**
 *
 * @author akong
 */
public class OutboundDocSubmissionDeferredResponseStrategyImpl_g0 implements OrchestrationStrategy {

    private static final Logger LOG = Logger.getLogger(OutboundDocSubmissionDeferredResponseStrategyImpl_g0.class);

    protected NhinDocSubmissionDeferredResponseProxy getNhinDocSubmissionDeferredResponseProxy() {
        return new NhinDocSubmissionDeferredResponseProxyObjectFactory().getNhinDocSubmissionDeferredResponseProxy();
    }
    
    /**
     * Gets an instance of the XDRAuditLogger
     * @return
     */
    protected XDRAuditLogger getXDRAuditLogger() {
        return new XDRAuditLogger();
    }
    
    @Override
    public void execute(Orchestratable message) {
        if (message instanceof OutboundDocSubmissionDeferredResponseOrchestratable) {
            execute((OutboundDocSubmissionDeferredResponseOrchestratable) message);
        } else {
            LOG.error("Not an OutboundDocSubmissionDeferredResponseOrchestratable.");
        }
    }

    public void execute(OutboundDocSubmissionDeferredResponseOrchestratable message) {
        LOG.debug("Begin OutboundDocSubmissionOrchestratableImpl_g0.process");
        
        auditRequestToNhin(message.getRequest(), message.getAssertion(), message.getTarget());

        NhinDocSubmissionDeferredResponseProxy nhincDocSubmission = getNhinDocSubmissionDeferredResponseProxy();
        XDRAcknowledgementType response = nhincDocSubmission.provideAndRegisterDocumentSetBDeferredResponse11(
                message.getRequest(), message.getAssertion(), message.getTarget());
        message.setResponse(response);
        
        auditResponseFromNhin(response, message.getAssertion(), message.getTarget());

        LOG.debug("End OutboundDocSubmissionDeferredResponseStrategyImpl_g0.process");
    }
    
    private void auditRequestToNhin(RegistryResponseType request, AssertionType assertion, NhinTargetSystemType target) {
        getXDRAuditLogger().auditNhinXDRResponse(request, assertion, target,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, true);
    }

    private void auditResponseFromNhin(XDRAcknowledgementType response, AssertionType assertion,
            NhinTargetSystemType target) {
        getXDRAuditLogger().auditAcknowledgement(response, assertion, target,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.XDR_RESPONSE_ACTION);
    }

}
