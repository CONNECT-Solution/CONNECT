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
package gov.hhs.fha.nhinc.docsubmission._11.entity.deferred.response;

import gov.hhs.fha.nhinc.aspect.OutboundMessageEvent;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionArgTransformerBuilder;
import gov.hhs.fha.nhinc.docsubmission.outbound.deferred.response.OutboundDocSubmissionDeferredResponse;
import gov.hhs.fha.nhinc.nhincentityxdr.async.response.EntityXDRAsyncResponsePortType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.SOAPBinding;

@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class EntityDocSubmissionDeferredResponseUnsecured implements EntityXDRAsyncResponsePortType {

    private WebServiceContext context;
    private OutboundDocSubmissionDeferredResponse outboundDocSubmissionResponse;

    @Override
    @OutboundMessageEvent(beforeBuilder = DocSubmissionArgTransformerBuilder.class,
        afterReturningBuilder = DocSubmissionArgTransformerBuilder.class,
        serviceType = "Document Submission Deferred Response",
        version = "1.1")
    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(
        RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType provideAndRegisterDocumentSetAsyncRespRequest) {
        return new EntityDocSubmissionDeferredResponseImpl(outboundDocSubmissionResponse)
            .provideAndRegisterDocumentSetBAsyncResponse(provideAndRegisterDocumentSetAsyncRespRequest, context);
    }

    @Resource
    public void setContext(WebServiceContext context) {
        this.context = context;
    }

    public void setOutboundDocSubmissionResponse(OutboundDocSubmissionDeferredResponse outboundDocSubmissionResponse) {
        this.outboundDocSubmissionResponse = outboundDocSubmissionResponse;
    }

    /**
     * Gets the outbound doc submission.
     *
     * @return the outbound doc submission
     */
    public OutboundDocSubmissionDeferredResponse getOutboundDocSubmission() {
        return this.outboundDocSubmissionResponse;
    }
}
