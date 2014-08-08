/*
 * Copyright (c) 2014, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.corex12.docsubmission.realtime.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRealTimeRequestType;
import gov.hhs.fha.nhinc.corex12.docsubmission.realtime.entity.proxy.description.EntityCORE_X12DSRealTimea0ServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhincentitycore.EntityCORETransactionPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.apache.log4j.Logger;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;

/**
 * @author cmay
 *
 */
public class EntityCORE_X12DSRealTimeProxyWebServiceUnsecuredImpl implements EntityCORE_X12DSRealTimeProxy {

    private static final Logger LOG = Logger.getLogger(EntityCORE_X12DSRealTimeProxyWebServiceUnsecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    public EntityCORE_X12DSRealTimeProxyWebServiceUnsecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    public ServicePortDescriptor<EntityCORETransactionPortType> getServicePortDescriptor(
        NhincConstants.ADAPTER_API_LEVEL apiLevel) {

        return new EntityCORE_X12DSRealTimea0ServicePortDescriptor();
    }

    @Override
    public COREEnvelopeRealTimeResponse realTimeRequest(COREEnvelopeRealTimeRequest message, AssertionType assertion,
        NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {

        LOG.debug("Begin EntityCORE_X12DSRealTimeProxyWebServiceUnsecuredImpl.realTimeRequest");
        COREEnvelopeRealTimeResponse response = new COREEnvelopeRealTimeResponse();

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ENTITY_CORE_X12DS_REALTIME_SERVICE_NAME);

            ServicePortDescriptor<EntityCORETransactionPortType> portDescriptor = getServicePortDescriptor(NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);

            CONNECTClient<EntityCORETransactionPortType> client = getCONNECTClient(portDescriptor, url, assertion);

            RespondingGatewayCrossGatewayRealTimeRequestType request = new RespondingGatewayCrossGatewayRealTimeRequestType();
            request.setNhinTargetCommunities(targets);
            request.setCOREEnvelopeRealTimeRequest(message);
            // TODO: URL Info??

            response = (COREEnvelopeRealTimeResponse) client.invokePort(EntityCORETransactionPortType.class,
                "realTimeRequest", request);

        } catch (Exception ex) {
            LOG.error("Error calling realTimeRequest: " + ex.getMessage(), ex);
        }

        LOG.debug("End EntityCORE_X12DSRealTimeProxyWebServiceUnsecuredImpl.realTimeRequest");
        return response;
    }

    /**
     * @param portDescriptor
     * @param url
     * @param assertion
     * @return
     */
    protected CONNECTClient<EntityCORETransactionPortType> getCONNECTClient(ServicePortDescriptor<EntityCORETransactionPortType> portDescriptor,
        String url, AssertionType assertion) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
    }
}
