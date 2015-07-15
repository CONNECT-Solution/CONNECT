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
package gov.hhs.fha.nhinc.corex12.docsubmission.realtime.adapter.proxy;

import gov.hhs.fha.nhinc.adaptercoresecured.AdapterCORETransactionSecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterCOREEnvelopeRealTimeResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterCOREEnvelopeRealTimeSecuredRequestType;
import gov.hhs.fha.nhinc.corex12.docsubmission.realtime.adapter.proxy.service.AdapterCORE_X12DSRealTimeSecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.corex12.docsubmission.utils.CORE_X12DSAdapterExceptionBuilder;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;

/**
 * @author cmay
 *
 */
public class AdapterCORE_X12DSRealTimeProxyWebServiceSecuredImpl extends CORE_X12DSAdapterExceptionBuilder implements AdapterCORE_X12DSRealTimeProxy {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterCORE_X12DSRealTimeProxyWebServiceSecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterCORE_X12DSRealTimeProxyWebServiceSecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     *
     * @param msg
     * @param assertion
     * @return
     */
    @Override
    public COREEnvelopeRealTimeResponse realTimeTransaction(COREEnvelopeRealTimeRequest msg, AssertionType assertion) {
        COREEnvelopeRealTimeResponse response;

        try {
            String url = oProxyHelper
                .getAdapterEndPointFromConnectionManager(NhincConstants.ADAPTER_CORE_X12DS_REALTIME_SECURED_SERVICE_NAME);
            if (NullChecker.isNotNullish(url)) {

                ServicePortDescriptor<AdapterCORETransactionSecuredPortType> portDescriptor = new AdapterCORE_X12DSRealTimeSecuredServicePortDescriptor();

                CONNECTClient<AdapterCORETransactionSecuredPortType> client = CONNECTClientFactory.getInstance()
                    .getCONNECTClientSecured(portDescriptor, url, assertion);

                AdapterCOREEnvelopeRealTimeSecuredRequestType requestWrapper = new AdapterCOREEnvelopeRealTimeSecuredRequestType();
                requestWrapper.setCOREEnvelopeRealTimeRequest(msg);

                AdapterCOREEnvelopeRealTimeResponseType responseWrapper = (AdapterCOREEnvelopeRealTimeResponseType) client.invokePort(
                    AdapterCORETransactionSecuredPortType.class, "realTimeTransaction", requestWrapper);

                response = responseWrapper.getCOREEnvelopeRealTimeResponse();

            } else {
                response = new COREEnvelopeRealTimeResponse();
                buildCOREEnvelopeRealTimeErrorResponse(msg, response);
                LOG.error("Failed to call the web service (" + NhincConstants.ADAPTER_CORE_X12DS_REALTIME_SECURED_SERVICE_NAME
                    + "); the URL is null.");
            }
        } catch (Exception ex) {
            // TODO: We need to add error handling here based on CORE X12 DS RealTime use cases
            // e.g., Adapter not found, timeout, etc.
            LOG.error("Error sending Adapter CORE X12 Doc Submission Secured message: " + ex.getMessage(), ex);
            response = new COREEnvelopeRealTimeResponse();

            response.setErrorMessage(NhincConstants.CORE_X12DS_ACK_ERROR_MSG);
            response.setErrorCode(NhincConstants.CORE_X12DS_ACK_ERROR_CODE);
        }

        return response;
    }
}
