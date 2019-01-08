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
package gov.hhs.fha.nhinc.docquery.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.docquery.entity.proxy.description.EntityDocQueryServicePortDescriptor;
import gov.hhs.fha.nhinc.entitydocquery.EntityDocQueryPortType;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 *
 */
public class EntityDocQueryProxyWebServiceUnsecuredImpl implements EntityDocQueryProxy {
    private static final Logger LOG = LoggerFactory.getLogger(EntityDocQueryProxyWebServiceUnsecuredImpl.class);

    private WebServiceProxyHelper oProxyHelper = null;
    private NhincConstants.UDDI_SPEC_VERSION version;

    /**
     * Default Constructor creates log and WebServiceProxyHelper.
     */
    public EntityDocQueryProxyWebServiceUnsecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
        version = null;
    }

    public EntityDocQueryProxyWebServiceUnsecuredImpl(NhincConstants.UDDI_SPEC_VERSION version) {
        this();
        this.version = version;
    }

    /**
     * @return WebServiceProxyHelper Object.
     */
    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * @param apiLevel  Adapter apiLevel for the Unsecured Service(DocQuery)
     * @return Adapter apiLevel implementation (a0,a1).
     */
    public ServicePortDescriptor<EntityDocQueryPortType> getServicePortDescriptor(
            NhincConstants.ADAPTER_API_LEVEL apiLevel) {
        return new EntityDocQueryServicePortDescriptor();
    }

    /** This method returns AdhocQueryResponse from entity Interface.
     * @param msg AdhocQUery Request (Body of message) received.
     * @param assertion Assertion received.
     * @param targets TargetCommunities to send request.
     * @return AdhocQueryResponse from Entity Interface.
     */
    @Override
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion,
            NhinTargetCommunitiesType targets) {
        LOG.debug("Begin respondingGatewayCrossGatewayQuery");
        AdhocQueryResponse response = null;

        try {
            String url;
            if(version != null) {
                url = oProxyHelper.getEndpointFromConnectionManagerByEntitySpecLevel(NhincConstants.ENTITY_DOC_QUERY_PROXY_SERVICE_NAME, version);
            } else {
                url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ENTITY_DOC_QUERY_PROXY_SERVICE_NAME);
            }

            if (msg == null) {
                throw new IllegalArgumentException("Request Message must be provided");
            } else if (assertion == null) {
                throw new IllegalArgumentException("Assertion must be provided");
            } else if (targets == null) {
                throw new IllegalArgumentException("Target Communities must be provided");
            } else {
                ServicePortDescriptor<EntityDocQueryPortType> portDescriptor = getServicePortDescriptor(
                        NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);

                CONNECTClient<EntityDocQueryPortType> client = CONNECTClientFactory.getInstance()
                        .getCONNECTClientUnsecured(portDescriptor, url, assertion);

                RespondingGatewayCrossGatewayQueryRequestType request =
                        new RespondingGatewayCrossGatewayQueryRequestType();
                request.setAdhocQueryRequest(msg);
                request.setAssertion(assertion);
                request.setNhinTargetCommunities(targets);

                response = (AdhocQueryResponse) client.invokePort(EntityDocQueryPortType.class,
                        "respondingGatewayCrossGatewayQuery", request);
            }
        } catch (Exception ex) {
            throw new ErrorEventException(ex, "Unable to call Entity Doc Query");
        }

        LOG.debug("End respondingGatewayCrossGatewayQuery");
        return response;
    }

}
