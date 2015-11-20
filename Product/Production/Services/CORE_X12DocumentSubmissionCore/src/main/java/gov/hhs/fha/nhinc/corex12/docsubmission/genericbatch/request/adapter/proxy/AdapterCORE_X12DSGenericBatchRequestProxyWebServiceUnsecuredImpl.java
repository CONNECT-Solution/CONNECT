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
package gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.request.adapter.proxy;

import gov.hhs.fha.nhinc.adaptercore.AdapterCOREGenericBatchTransactionPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterBatchSubmissionRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterBatchSubmissionResponseType;
import gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.request.adapter.proxy.service.AdapterCORE_X12DSGenericBatchRequestUnsecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.corex12.docsubmission.utils.CORE_X12DSAdapterExceptionBuilder;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;

/**
 * @author svalluripalli
 *
 */
public class AdapterCORE_X12DSGenericBatchRequestProxyWebServiceUnsecuredImpl extends CORE_X12DSAdapterExceptionBuilder implements AdapterCORE_X12DGenericBatchRequestProxy {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterCORE_X12DSGenericBatchRequestProxyWebServiceUnsecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    /**
     * Constructor
     */
    public AdapterCORE_X12DSGenericBatchRequestProxyWebServiceUnsecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    /**
     *
     * @return WebServiceProxyHelper
     */
    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     *
     * @param msg
     * @param assertion
     * @return COREEnvelopeBatchSubmissionResponse
     */
    @Override
    public COREEnvelopeBatchSubmissionResponse batchSubmitTransaction(COREEnvelopeBatchSubmission msg, AssertionType assertion) {
        COREEnvelopeBatchSubmissionResponse oResponse = null;
        try {
            String url = oProxyHelper.getAdapterEndPointFromConnectionManager(NhincConstants.ADAPTER_CORE_X12DS_GENERICBATCH_REQUEST_SERVICE_NAME);
            if (NullChecker.isNotNullish(url)) {
                ServicePortDescriptor<AdapterCOREGenericBatchTransactionPortType> portDescriptor = new AdapterCORE_X12DSGenericBatchRequestUnsecuredServicePortDescriptor();
                CONNECTClient<AdapterCOREGenericBatchTransactionPortType> client = CONNECTClientFactory.getInstance()
                        .getCONNECTClientUnsecured(portDescriptor, url, assertion);
                AdapterBatchSubmissionRequestType requestWrapper = new AdapterBatchSubmissionRequestType();
                requestWrapper.setCOREEnvelopeBatchSubmission(msg);
                client.enableMtom();
                AdapterBatchSubmissionResponseType responseWrapper = (AdapterBatchSubmissionResponseType) client.invokePort(AdapterCOREGenericBatchTransactionPortType.class, "batchSubmitTransaction", requestWrapper);
                oResponse = responseWrapper.getCOREEnvelopeBatchSubmissionResponse();
            } else {
                oResponse = new COREEnvelopeBatchSubmissionResponse();
                buildCOREEnvelopeGenericBatchErrorResponse(msg, oResponse);
            }
        } catch (Exception ex) {
            LOG.error("Error sending Adapter CORE X12 Doc Submission Request Unsecured message: " + ex.getMessage(), ex);
            oResponse = new COREEnvelopeBatchSubmissionResponse();
            oResponse.setErrorCode(NhincConstants.CORE_X12DS_ACK_ERROR_CODE);
            oResponse.setErrorMessage(NhincConstants.CORE_X12DS_ACK_ERROR_MSG);
        }
        return oResponse;
    }
}
