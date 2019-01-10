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
package gov.hhs.fha.nhinc.corex12.ds.genericbatch.common.nhin.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.corex12.ds.genericbatch.common.nhin.proxy.service.COREGenericBatchServicePortDescriptor;
import gov.hhs.fha.nhinc.corex12.ds.utils.X12EntityExceptionBuilder;
import gov.hhs.fha.nhinc.corex12.ds.utils.X12LargePayloadUtils;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.apache.commons.lang.StringUtils;
import org.caqh.soap.wsdl.GenericBatchTransactionPort;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cmay, svalluripalli
 */
public class NhinX12BatchProxyWSSecured implements NhinX12BatchProxy {

    private static final Logger LOG = LoggerFactory.getLogger(NhinX12BatchProxyWSSecured.class);
    private WebServiceProxyHelper proxyHelper = null;

    public NhinX12BatchProxyWSSecured() {
        proxyHelper = new WebServiceProxyHelper();
    }

    protected CONNECTClient<GenericBatchTransactionPort> getCONNECTClientSecured(
        ServicePortDescriptor<GenericBatchTransactionPort> portDescriptor, AssertionType assertion, String url,
        NhinTargetSystemType target, String serviceName) {

        return CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, assertion, url,
            target, serviceName);
    }

    @Override
    public COREEnvelopeBatchSubmissionResponse batchSubmitTransaction(COREEnvelopeBatchSubmission msg,
        AssertionType assertion, NhinTargetSystemType targetSystem, NhincConstants.GATEWAY_API_LEVEL apiLevel) {

        COREEnvelopeBatchSubmissionResponse response;

        try {
            String url = proxyHelper.getUrlFromTargetSystemByGatewayAPILevel(targetSystem, getServiceName(), apiLevel);

            if (StringUtils.isNotEmpty(url)) {
                X12LargePayloadUtils.convertFileLocationToDataIfEnabled(msg);
                ServicePortDescriptor<GenericBatchTransactionPort> portDescriptor
                    = new COREGenericBatchServicePortDescriptor();

                CONNECTClient<GenericBatchTransactionPort> client = getCONNECTClientSecured(portDescriptor, assertion,
                    url, targetSystem, getServiceName());
                client.enableMtom();
                response = (COREEnvelopeBatchSubmissionResponse) client.invokePort(GenericBatchTransactionPort.class,
                    "batchSubmitTransaction", msg);
                if (response != null && response.getPayload() != null) {
                    X12LargePayloadUtils.convertDataToFileLocationIfEnabled(response);
                }
            } else {
                String targetHCID = HomeCommunityMap.getCommunityIdFromTargetSystem(targetSystem);
                return X12EntityExceptionBuilder.getInstance().createErrorResponse(msg, targetHCID);
            }
        } catch (Exception ex) {
            LOG.error("Error calling batchSubmitTransaction: {}", ex.getLocalizedMessage(), ex);
            return X12EntityExceptionBuilder.getInstance().createWebServiceErrorResponse(msg,
                ex.getLocalizedMessage());
        }
        return response;
    }

    protected String getServiceName() {
        return NhincConstants.CORE_X12DS_GENERICBATCH_REQUEST_SERVICE_NAME;
    }
}
