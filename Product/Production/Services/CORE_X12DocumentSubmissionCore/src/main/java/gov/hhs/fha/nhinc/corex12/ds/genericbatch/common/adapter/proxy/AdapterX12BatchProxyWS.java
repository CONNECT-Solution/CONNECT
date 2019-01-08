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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.corex12.ds.utils.X12AdapterExceptionBuilder;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cmay
 */
public abstract class AdapterX12BatchProxyWS extends AbstractAdapterX12BatchProxy {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterX12BatchProxyWS.class);
    private WebServiceProxyHelper proxyHelper = null;

    protected static final String METHOD_NAME = "batchSubmitTransaction";

    public AdapterX12BatchProxyWS() {
        proxyHelper = new WebServiceProxyHelper();
    }

    protected final WebServiceProxyHelper getWSProxyHelper() {
        return proxyHelper;
    }

    @Override
    public final COREEnvelopeBatchSubmissionResponse batchSubmitTransaction(COREEnvelopeBatchSubmission msg,
        AssertionType assertion) {

        COREEnvelopeBatchSubmissionResponse response;

        try {
            // TODO: Unsecured and Secured Impls can be further refactored
            String url = getWSProxyHelper().getAdapterEndPointFromConnectionManager(getServiceName());

            if (NullChecker.isNotNullish(url)) {
                response = getAdapterResponse(msg, assertion, url);
            } else {
                response = X12AdapterExceptionBuilder.getInstance().buildCOREEnvelopeGenericBatchErrorResponse(msg);
                LOG.error("Failed to call the web service ({}).  The URL is null.", getServiceName());
            }
        } catch (Exception ex) {
            LOG.error("Error sending Adapter CORE X12 Doc Submission Request: {}", ex.getLocalizedMessage(), ex);

            response = new COREEnvelopeBatchSubmissionResponse();
            response.setErrorMessage(NhincConstants.CORE_X12DS_ACK_ERROR_MSG);
            response.setErrorCode(NhincConstants.CORE_X12DS_ACK_ERROR_CODE);
        }

        return response;
    }

    /**
     * TODO: This can be further refactored
     *
     * @param msg
     * @param assertion
     * @param url the already null-checked url
     * @return
     * @throws java.lang.Exception
     */
    protected abstract COREEnvelopeBatchSubmissionResponse getAdapterResponse(COREEnvelopeBatchSubmission msg,
        AssertionType assertion, String url) throws Exception;
}
