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
package gov.hhs.fha.nhinc.docsubmission._20.passthru.deferred.request;

import javax.xml.ws.WebServiceContext;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;
import gov.hhs.fha.nhinc.docsubmission.passthru.deferred.request.PassthruDocSubmissionDeferredRequestOrchImpl;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;

/**
 *
 * @author Neil Webb
 */
public class PassthruDocSubmissionDeferredRequestImpl_g1 {

    private PassthruDocSubmissionDeferredRequestOrchImpl orchImpl;

    PassthruDocSubmissionDeferredRequestImpl_g1(PassthruDocSubmissionDeferredRequestOrchImpl orchImpl) {
        this.orchImpl = orchImpl;
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest,
            WebServiceContext context) {
        AssertionType assertion = extractAssertionFromContext(context, null);

        return orchImpl.provideAndRegisterDocumentSetBRequest(
                provideAndRegisterRequestRequest.getProvideAndRegisterDocumentSetRequest(), assertion,
                provideAndRegisterRequestRequest.getNhinTargetSystem());
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(
            RespondingGatewayProvideAndRegisterDocumentSetRequestType provideAndRegisterRequestRequest,
            WebServiceContext context) {
        AssertionType assertion = extractAssertionFromContext(context, provideAndRegisterRequestRequest.getAssertion());

        return orchImpl.provideAndRegisterDocumentSetBRequest(
                provideAndRegisterRequestRequest.getProvideAndRegisterDocumentSetRequest(), assertion,
                provideAndRegisterRequestRequest.getNhinTargetSystem());

    }

    protected AssertionType extractAssertionFromContext(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (oAssertionIn == null) {
            SAML2AssertionExtractor extractor = SAML2AssertionExtractor.getInstance();
            assertion = extractor.extractSamlAssertion(context);
        } else {
            assertion = oAssertionIn;
        }
        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.getOrCreateAsyncMessageId(context));
        }

        return assertion;
    }

    public void setOrchestratorImpl(PassthruDocSubmissionDeferredRequestOrchImpl orchImpl) {
        this.orchImpl = orchImpl;
    }

}