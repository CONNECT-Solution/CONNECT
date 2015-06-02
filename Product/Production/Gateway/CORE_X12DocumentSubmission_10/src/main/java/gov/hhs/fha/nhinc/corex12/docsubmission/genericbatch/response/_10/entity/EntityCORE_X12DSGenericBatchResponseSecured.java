/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.response._10.entity;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayBatchSubmissionResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayBatchSubmissionSecuredRequestType;
import gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.response.outbound.OutboundCORE_X12DSGenericBatchResponse;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author svalluripalli
 */
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class EntityCORE_X12DSGenericBatchResponseSecured implements gov.hhs.fha.nhinc.nhincentitycoresecured.EntityCOREGenericBatchTransactionSecuredPortType {

    private WebServiceContext context;
    private OutboundCORE_X12DSGenericBatchResponse outboundCORE_X12DSGenericBatchResponse;

    @Resource
    public void setContext(WebServiceContext context) {
        this.context = context;
    }

    /**
     *
     * @param outboundCORE_X12DSGenericBatchResponse
     */
    public void setOutboundCORE_X12DSGenericBatchResponse(OutboundCORE_X12DSGenericBatchResponse outboundCORE_X12DSGenericBatchResponse) {
        this.outboundCORE_X12DSGenericBatchResponse = outboundCORE_X12DSGenericBatchResponse;
    }

    @Override
    public RespondingGatewayCrossGatewayBatchSubmissionResponseMessageType batchSubmitTransaction(RespondingGatewayCrossGatewayBatchSubmissionSecuredRequestType body) {
        return new EntityCORE_X12DSGenericBatchResponseImpl(outboundCORE_X12DSGenericBatchResponse).batchSubmitTransaction(body, context);
    }
}
