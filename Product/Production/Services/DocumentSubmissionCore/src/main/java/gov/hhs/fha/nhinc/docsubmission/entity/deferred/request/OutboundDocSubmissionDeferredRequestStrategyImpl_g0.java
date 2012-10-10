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

package gov.hhs.fha.nhinc.docsubmission.entity.deferred.request;

import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.nhin.deferred.request.proxy11.NhinDocSubmissionDeferredRequestProxy;
import gov.hhs.fha.nhinc.docsubmission.nhin.deferred.request.proxy11.NhinDocSubmissionDeferredRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

/**
 *
 * @author akong
 */
public class OutboundDocSubmissionDeferredRequestStrategyImpl_g0 implements OrchestrationStrategy {

    private static Log log = LogFactory.getLog(OutboundDocSubmissionDeferredRequestStrategyImpl_g0.class);

    /**
     * Generic constructor method.
     */
    public OutboundDocSubmissionDeferredRequestStrategyImpl_g0() {
    }

    /**
     * Returns the log object.
     * @return
     */
    protected Log getLogger() {
        return log;
    }

    /**
     *
     * @return
     */
    protected NhinDocSubmissionDeferredRequestProxy getNhinDocSubmissionDeferredRequestProxy() {
        return new NhinDocSubmissionDeferredRequestProxyObjectFactory().getNhinDocSubmissionDeferredRequestProxy();
    }

    /**
     * Executes the strategy methodology.
     * @param message
     */
    @Override
    public void execute(Orchestratable message) {
        if (message instanceof OutboundDocSubmissionDeferredRequestOrchestratable) {
            execute((OutboundDocSubmissionDeferredRequestOrchestratable) message);
        } else {
            getLogger().error("Not an OutboundDocSubmissionDeferredRequestOrchestratable.");
        }
    }

    /**
     * Executes the strategy to audit the message and forward to NHIN proxy.
     * @param message
     */
    public void execute(OutboundDocSubmissionDeferredRequestOrchestratable message) {
        getLogger().debug("Begin OutboundDocSubmissionOrchestratableImpl_g0.process");

        auditRequestToNhin(message.getRequest(), message.getAssertion());
        
        NhinDocSubmissionDeferredRequestProxy nhincDocSubmission = getNhinDocSubmissionDeferredRequestProxy();
        XDRAcknowledgementType response = nhincDocSubmission.provideAndRegisterDocumentSetBRequest11(
                message.getRequest(), message.getAssertion(), message.getTarget());
        message.setResponse(response);

        auditResponseFromNhin(response, message.getAssertion());
        getLogger().debug("End OutboundDocSubmissionDeferredRequestStrategyImpl_g0.process");
    }

    /**
     * Gets an instance of the XDRAuditLogger
     * @return
     */
    protected XDRAuditLogger getXDRAuditLogger() {
        return new XDRAuditLogger();
    }

    /**
     * Creates a generic Audit Log message for a request to the NHIN
     * @param request
     * @param assertion
     */
    private void auditRequestToNhin(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion) {
        getXDRAuditLogger().auditNhinXDR(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    /**
     * Creates a generic Audit Log message for a request from the NHIN
     * @param response
     * @param assertion
     */
    private void auditResponseFromNhin(XDRAcknowledgementType response,  AssertionType assertion) {
        getXDRAuditLogger().auditAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.XDR_REQUEST_ACTION);
    }

}
