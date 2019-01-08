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
package gov.hhs.fha.nhinc.corex12.ds.genericbatch.request.adapter;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterBatchSubmissionRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterBatchSubmissionResponseSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterBatchSubmissionResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterBatchSubmissionSecuredRequestType;
import gov.hhs.fha.nhinc.corex12.ds.genericbatch.common.adapter.AdapterX12BatchOrchImpl;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author svalluripalli, cmay
 */
public class AdapterX12BatchRequestImpl extends BaseService {

    /**
     *
     * @param body
     * @param context
     * @return AdapterBatchSubmissionResponseSecuredType
     */
    public AdapterBatchSubmissionResponseSecuredType batchSubmitTransaction(
        AdapterBatchSubmissionSecuredRequestType body, WebServiceContext context) {

        AdapterBatchSubmissionResponseSecuredType response = new AdapterBatchSubmissionResponseSecuredType();
        response.setCOREEnvelopeBatchSubmissionResponse(new AdapterX12BatchOrchImpl()
            .batchSubmitTransaction(body.getCOREEnvelopeBatchSubmission(), getAssertion(context)));

        return response;
    }

    /**
     *
     * @param body
     * @param context
     * @return AdapterBatchSubmissionResponseType
     */
    public AdapterBatchSubmissionResponseType batchSubmitTransaction(AdapterBatchSubmissionRequestType body,
        WebServiceContext context) {

        AdapterBatchSubmissionResponseType response = new AdapterBatchSubmissionResponseType();
        response.setCOREEnvelopeBatchSubmissionResponse(new AdapterX12BatchOrchImpl()
            .batchSubmitTransaction(body.getCOREEnvelopeBatchSubmission(), getAssertion(context, body.getAssertion())));

        return response;
    }
}
