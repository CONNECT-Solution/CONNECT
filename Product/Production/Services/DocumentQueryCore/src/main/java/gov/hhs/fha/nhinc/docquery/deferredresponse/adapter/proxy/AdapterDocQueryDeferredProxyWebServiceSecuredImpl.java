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
//import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDeferredResponseOptionQueryType;
import gov.hhs.fha.nhinc.docquery.deferred.impl.AdapterDeferredResponseOptionImpl;
import gov.hhs.fha.nhinc.docquery.deferredresponse.adapter.proxy.description.AdapterDeferredResponseOptionQueryRequestSecuredPortDescriptor;
import gov.hhs.fha.nhinc.dq.adapterdeferredrequestquerysecured.AdapterDeferredResponseOptionQueryRequestSecuredPortType;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import javax.xml.ws.WebServiceException;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

/**
 *
 *
 * @author ptambellini
 */
public class AdapterDocQueryDeferredProxyWebServiceSecuredImpl extends BaseAdapterDocQueryDeferredProxy {

    /**
     * @param apiLevel Adapter ApiLevel Param.
     * @return Adapter apiLevel implementation to be used (a0 or a1 level).
     */
    public ServicePortDescriptor<AdapterDeferredResponseOptionQueryRequestSecuredPortType> getServicePortDescriptor(
        final NhincConstants.ADAPTER_API_LEVEL apiLevel) {
        return new AdapterDeferredResponseOptionQueryRequestSecuredPortDescriptor();
    }

    @Override
    public String respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg,
        AssertionType assertion) {
        String response = null;
        String url;
        try {
            // get the Adopter Endpoint URL
            url = getEndPointFromConnectionManagerByAdapterAPILevel(assertion,
                NhincConstants.ADAPTER_DOC_QUERY_SECURED_SERVICE_NAME);

            // Call the service
            if (NullChecker.isNotNullish(url)) {
                if (msg == null) {
                    throw new IllegalArgumentException("Request Message must be provided");
                }
                final ServicePortDescriptor<AdapterDeferredResponseOptionQueryRequestSecuredPortType> portDescriptor =
                    getServicePortDescriptor(
                        NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);

                final CONNECTClient<AdapterDeferredResponseOptionQueryRequestSecuredPortType> client =
                    CONNECTClientFactory.getInstance()
                    .getCONNECTClientSecured(portDescriptor, url, assertion);

                response = (String) client.invokePort(
                    AdapterDeferredResponseOptionQueryRequestSecuredPortType.class,
                    "respondingGatewayCrossGatewayQuery",
                    new AdapterDeferredResponseOptionImpl().respondingGatewayCrossGatewayQuerySecured(msg, assertion)
                        .getNewId());

            } else {
                throw new WebServiceException("Could not determine URL for Doc Query Adapter endpoint");
            }
        } catch (final Exception ex) {
            throw new ErrorEventException(ex, getAdapterHelper().createErrorResponse(), "Unable to call Doc Query Adapter");
        }

        return response;
    }
}
