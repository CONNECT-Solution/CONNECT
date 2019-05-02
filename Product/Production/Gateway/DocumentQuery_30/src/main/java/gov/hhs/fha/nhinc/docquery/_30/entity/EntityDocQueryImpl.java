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
package gov.hhs.fha.nhinc.docquery._30.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.fha.nhinc.docquery.outbound.OutboundDocQuery;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.lang.StringUtils;

public class EntityDocQueryImpl extends BaseService {

    private OutboundDocQuery outboundDocQuery;
    private DeferredDocQueryCheck deferredDocQueryCheck = new DeferredDocQueryCheck(this);

    /**
     * Constructor.
     *
     * @param outboundDocQuery
     */
    public EntityDocQueryImpl(OutboundDocQuery outboundDocQuery) {
        this.outboundDocQuery = outboundDocQuery;
    }

    /**
     * Sends the request to the Nwhin. This method is invoked by the secured outbound interface and the assertion object
     * is read from the webservice context.
     *
     * @param request
     * @param context
     * @return AdhocQueryResponse
     */
    public AdhocQueryResponse respondingGatewayCrossGatewayQuerySecured(
        RespondingGatewayCrossGatewayQuerySecuredRequestType request, WebServiceContext context) {

        AssertionType assertion = getAssertion(context, null);

        return deferredDocQueryCheck.respondingGatewayCrossGatewayQuery(request.getAdhocQueryRequest(), assertion,
            request.getNhinTargetCommunities());
    }

    /**
     * Sends the request to the Nwhin. This method is invoked by the unsecured outbound interface and the assertion
     * object is read from the request object itself.
     *
     * @param request
     * @param context
     * @return AdhocQueryResponse
     */
    public AdhocQueryResponse respondingGatewayCrossGatewayQueryUnsecured(
        RespondingGatewayCrossGatewayQueryRequestType request, WebServiceContext context) {

        return deferredDocQueryCheck.respondingGatewayCrossGatewayQuery(request.getAdhocQueryRequest(),
            request.getAssertion(), request.getNhinTargetCommunities());

    }

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest request, AssertionType assertion,
        NhinTargetCommunitiesType targets) {

        if (targets == null) {
            targets = new ObjectFactory().createNhinTargetCommunitiesType();
        }

        if (StringUtils.isBlank(targets.getUseSpecVersion())) {
            targets.setUseSpecVersion("3.0");
        }
        return outboundDocQuery.respondingGatewayCrossGatewayQuery(request, assertion, targets);
    }
}
