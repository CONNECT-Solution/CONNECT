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
package gov.hhs.fha.nhinc.corex12.ds.genericbatch.common.adapter.proxy;

import gov.hhs.fha.nhinc.adaptercoresecured.AdapterCOREGenericBatchTransactionSecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterBatchSubmissionResponseSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterBatchSubmissionSecuredRequestType;
import gov.hhs.fha.nhinc.corex12.ds.genericbatch.common.adapter.proxy.service.AdapterX12BatchSecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;

/**
 *
 * @author cmay
 */
public abstract class AdapterX12BatchProxyWSSecured extends AdapterX12BatchProxyWS {

    @Override
    protected COREEnvelopeBatchSubmissionResponse getAdapterResponse(COREEnvelopeBatchSubmission msg,
        AssertionType assertion, String url) throws Exception {

        ServicePortDescriptor<AdapterCOREGenericBatchTransactionSecuredPortType> portDescriptor
            = new AdapterX12BatchSecuredServicePortDescriptor();

        CONNECTClient<AdapterCOREGenericBatchTransactionSecuredPortType> client
            = CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, url, assertion);
        client.enableMtom();

        AdapterBatchSubmissionSecuredRequestType request = new AdapterBatchSubmissionSecuredRequestType();
        request.setCOREEnvelopeBatchSubmission(msg);

        AdapterBatchSubmissionResponseSecuredType adapterResponse = (AdapterBatchSubmissionResponseSecuredType) client.invokePort(
            AdapterCOREGenericBatchTransactionSecuredPortType.class, METHOD_NAME, request);

        return adapterResponse.getCOREEnvelopeBatchSubmissionResponse();
    }
}
