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
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.fha.nhinc.docquery.entity.proxy.description.EntityDocQuerySecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.entitydocquery.EntityDocQuerySecuredPortType;
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
public class EntityDocQueryProxyWebServiceSecuredImpl implements EntityDocQueryProxy {

    private static final Logger LOG = LoggerFactory.getLogger(EntityDocQueryProxyWebServiceSecuredImpl.class);

    private WebServiceProxyHelper oProxyHelper = null;

    /**
     * Default Constructor creates log and WebServiceProxyHelper.
     */
    public EntityDocQueryProxyWebServiceSecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    /**
     * @return WebServiceProxyHelper Object.
     */
    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * @param apiLevel Adapter apiLevel for the Secured Service(DocQuery)
     * @return Adapter apiLevel implementation (a0,a1).
     */
    public ServicePortDescriptor<EntityDocQuerySecuredPortType> getServicePortDescriptor(
            NhincConstants.ADAPTER_API_LEVEL apiLevel) {
        return new EntityDocQuerySecuredServicePortDescriptor();
    }

    /** This method returns AdhocQueryResponse from entity Interface.
     * @param body AdhocQUery Request (Body of message) received.
     * @param assertion Assertion received.
     * @param target TargetCommunities to send request.
     * @return AdhocQueryResponse from Entity Interface.
     */
    @Override
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest body, AssertionType assertion,
            NhinTargetCommunitiesType target) {
        LOG.debug("Begin respondingGatewayCrossGatewayQuery");
        AdhocQueryResponse response = null;

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ENTITY_DOC_QUERY_SECURED_SERVICE_NAME);

            if (body == null) {
                throw new IllegalArgumentException("Request Message must be provided");
            } else if (target == null) {
                throw new IllegalArgumentException("Target Communities must be provided");
            } else {
                ServicePortDescriptor<EntityDocQuerySecuredPortType> portDescriptor = getServicePortDescriptor(
                        NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);

                CONNECTClient<EntityDocQuerySecuredPortType> client = CONNECTClientFactory.getInstance()
                        .getCONNECTClientSecured(portDescriptor, url, assertion);

                RespondingGatewayCrossGatewayQuerySecuredRequestType request =
                        new RespondingGatewayCrossGatewayQuerySecuredRequestType();
                request.setAdhocQueryRequest(body);
                request.setNhinTargetCommunities(target);

                response = (AdhocQueryResponse) client.invokePort(EntityDocQuerySecuredPortType.class,
                        "respondingGatewayCrossGatewayQuery", request);
            }
        } catch (Exception ex) {
            throw new ErrorEventException(ex, "Unable to call Entity Doc Query");
        }

        LOG.debug("End respondingGatewayCrossGatewayQuery");
        return response;
    }
}
