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
package gov.hhs.fha.nhinc.docquery.deferredresponse.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDeferredResponseOptionQueryType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.docquery.deferredresponse.adapter.proxy.description.AdapterDeferredResponseOptionQueryRequestPortDescriptor;
import gov.hhs.fha.nhinc.dq.adapterdeferredrequestquery.AdapterDeferredResponseOptionQueryRequestPortType;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import javax.xml.ws.WebServiceException;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ptambellini
 */
public class AdapterDocQueryDeferredProxyWebServiceUnsecuredImpl extends BaseAdapterDocQueryDeferredProxy {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterDocQueryDeferredProxyWebServiceUnsecuredImpl.class);

    public AdapterDeferredResponseOptionQueryRequestPortDescriptor
    getServicePortDescriptor(
        NhincConstants.ADAPTER_API_LEVEL apiLevel) {
        return new AdapterDeferredResponseOptionQueryRequestPortDescriptor();
    }

    @Override
    public String respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg,
        AssertionType assertion) {
        LOG.debug("Begin respondingGatewayCrossGatewayQuery");
        AdapterDeferredResponseOptionQueryType response = null;
        String url;
        try {
            //get the Adopter Endpoint URL
            url = getEndPointFromConnectionManagerByAdapterAPILevel(assertion, NhincConstants.ADAPTER_DOC_QUERY_SERVICE_NAME);
            //Call the service
            if (NullChecker.isNotNullish(url)) {
                LOG.debug("getEndPointFromConnectionManagerByAdapterAPILevel: {}", url);
                if (msg == null) {
                    throw new IllegalArgumentException("Request Message must be provided");
                } else if (assertion == null) {
                    throw new IllegalArgumentException("Assertion must be provided");
                } else {
                    RespondingGatewayCrossGatewayQueryRequestType request = new RespondingGatewayCrossGatewayQueryRequestType();
                    request.setAdhocQueryRequest(msg);
                    request.setAssertion(assertion);
                    ServicePortDescriptor<AdapterDeferredResponseOptionQueryRequestPortType> portDescriptor =
                        getServicePortDescriptor(NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);

                    CONNECTClient<AdapterDeferredResponseOptionQueryRequestPortType> client =
                        CONNECTClientFactory.getInstance()
                        .getCONNECTClientUnsecured(portDescriptor, url, assertion);

                    response =
                        (AdapterDeferredResponseOptionQueryType) client.invokePort(
                            AdapterDeferredResponseOptionQueryRequestPortType.class,
                            "respondingGatewayCrossGatewayQueryDeferred", request);
                }
            } else {
                throw new WebServiceException("Could not determine URL for Doc Query Adapter endpoint");
            }
        } catch (Exception ex) {
            throw new ErrorEventException(ex, getAdapterHelper().createErrorResponse(), "Unable to call Doc Query Adapter");
        }

        LOG.debug("End respondingGatewayCrossGatewayQuery");
        return response.getNewId();
    }
}
