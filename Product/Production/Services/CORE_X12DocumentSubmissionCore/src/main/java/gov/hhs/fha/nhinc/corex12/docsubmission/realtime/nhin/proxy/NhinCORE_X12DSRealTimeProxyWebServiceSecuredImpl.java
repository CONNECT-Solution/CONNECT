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
package gov.hhs.fha.nhinc.corex12.docsubmission.realtime.nhin.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.corex12.docsubmission.realtime.nhin.proxy.service.NhinCORE_X12DSRealTimeServicePortDescriptor;
import gov.hhs.fha.nhinc.corex12.docsubmission.utils.CORE_X12DSEntityExceptionBuilder;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.caqh.soap.wsdl.CORETransactions;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cmay
 *
 */
public class NhinCORE_X12DSRealTimeProxyWebServiceSecuredImpl implements NhinCORE_X12DSRealTimeProxy {

    private static final Logger LOG = LoggerFactory.getLogger(NhinCORE_X12DSRealTimeProxyWebServiceSecuredImpl.class);
    private WebServiceProxyHelper proxyHelper = null;

    public NhinCORE_X12DSRealTimeProxyWebServiceSecuredImpl() {
        proxyHelper = new WebServiceProxyHelper();
    }

    protected CONNECTClient<CORETransactions> getCONNECTClientSecured(
        ServicePortDescriptor<CORETransactions> portDescriptor, AssertionType assertion, String url,
        String targetHomeCommunityId, String serviceName) {

        return CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, assertion, url,
            targetHomeCommunityId, serviceName);
    }

    @Override
    public COREEnvelopeRealTimeResponse realTimeTransaction(COREEnvelopeRealTimeRequest msg, AssertionType assertion,
        NhinTargetSystemType targetSystem, NhincConstants.GATEWAY_API_LEVEL apiLevel) {

        COREEnvelopeRealTimeResponse response;
        String targetHCID = null;
        if (targetSystem != null && targetSystem.getHomeCommunity() != null && targetSystem.getHomeCommunity().getHomeCommunityId() != null) {
            targetHCID = targetSystem.getHomeCommunity().getHomeCommunityId();
        }

        try {
            String url = proxyHelper.getUrlFromTargetSystemByGatewayAPILevel(targetSystem,
                NhincConstants.CORE_X12DS_REALTIME_SERVICE_NAME, apiLevel);
            if ((url != null) && (!url.isEmpty())) {
                ServicePortDescriptor<CORETransactions> portDescriptor = new NhinCORE_X12DSRealTimeServicePortDescriptor();

                CONNECTClient<CORETransactions> client = getCONNECTClientSecured(portDescriptor, assertion,
                    url, targetHCID, NhincConstants.CORE_X12DS_REALTIME_SERVICE_NAME);

                response = (COREEnvelopeRealTimeResponse) client.invokePort(CORETransactions.class,
                    "realTimeTransaction", msg);
            } else {
                response = CORE_X12DSEntityExceptionBuilder.createErrorResponse(msg, targetHCID);
                return response;
            }

        } catch (Exception ex) {
            // TODO: We need to add error handling here based on CORE X12 DS RealTime use cases
            // e.g., Connection error, etc.
            LOG.error("Error calling realTimeTransaction: " + ex.getMessage(), ex);
            response = CORE_X12DSEntityExceptionBuilder.createWebServiceErrorResponse(msg, ex.getMessage());
            return response;
        }

        return response;
    }

}
