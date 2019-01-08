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
package gov.hhs.fha.nhinc.corex12.ds.realtime._10.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRealTimeRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRealTimeResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRealTimeSecuredRequestType;
import gov.hhs.fha.nhinc.corex12.ds.realtime.outbound.OutboundX12RealTime;
import gov.hhs.fha.nhinc.corex12.ds.realtime.outbound.PassthroughOutboundX12RealTime;
import gov.hhs.fha.nhinc.corex12.ds.utils.X12EntityExceptionBuilder;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.lang.StringUtils;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author svalluripalli, cmay
 */
public class EntityX12RealTimeImpl extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(EntityX12RealTimeImpl.class);
    private final OutboundX12RealTime outboundCORE_X12DSRealTime;

    /**
     * Constructor
     *
     * @param outboundCORE_X12DSRealTime
     */
    public EntityX12RealTimeImpl(OutboundX12RealTime outboundCORE_X12DSRealTime) {
        // TODO: Will never be null once injection for outboundCORE_X12DSRealTime is implemented
        if (outboundCORE_X12DSRealTime != null) {
            this.outboundCORE_X12DSRealTime = outboundCORE_X12DSRealTime;
        } else {
            this.outboundCORE_X12DSRealTime = new PassthroughOutboundX12RealTime();
        }
    }

    /**
     *
     * @param body
     * @param context
     * @return RespondingGatewayCrossGatewayRealTimeResponseType
     */
    public RespondingGatewayCrossGatewayRealTimeResponseType realTimeTransaction(
        RespondingGatewayCrossGatewayRealTimeRequestType body, WebServiceContext context) {

        COREEnvelopeRealTimeResponse realTimeResponse = callOutboundRealTimeTransaction(
            body.getCOREEnvelopeRealTimeRequest(), body.getAssertion(), body.getNhinTargetCommunities());

        RespondingGatewayCrossGatewayRealTimeResponseType response
            = new RespondingGatewayCrossGatewayRealTimeResponseType();

        response.setCOREEnvelopeRealTimeResponse(realTimeResponse);
        return response;
    }

    /**
     *
     * @param body
     * @param context
     * @return RespondingGatewayCrossGatewayRealTimeResponseType
     */
    public RespondingGatewayCrossGatewayRealTimeResponseType realTimeTransactionSecured(
        RespondingGatewayCrossGatewayRealTimeSecuredRequestType body, WebServiceContext context) {

        COREEnvelopeRealTimeResponse realTimeResponse = callOutboundRealTimeTransaction(
            body.getCOREEnvelopeRealTimeRequest(), getAssertion(context), body.getNhinTargetCommunities());

        RespondingGatewayCrossGatewayRealTimeResponseType response
            = new RespondingGatewayCrossGatewayRealTimeResponseType();

        response.setCOREEnvelopeRealTimeResponse(realTimeResponse);
        return response;
    }

    /**
     *
     * @param coreEnvelopeRealTimeRequest
     * @param assertion
     * @param target
     * @return COREEnvelopeRealTimeResponse
     */
    private COREEnvelopeRealTimeResponse callOutboundRealTimeTransaction(
        COREEnvelopeRealTimeRequest coreEnvelopeRealTimeRequest, AssertionType assertion,
        NhinTargetCommunitiesType target) {

        COREEnvelopeRealTimeResponse realTimeResponse = null;
        try {
            if (StringUtils.isNotEmpty(HomeCommunityMap.getCommunityIdFromTargetCommunities(target))) {
                realTimeResponse = outboundCORE_X12DSRealTime
                    .realTimeTransaction(coreEnvelopeRealTimeRequest, assertion, target, null);
            } else {
                realTimeResponse = X12EntityExceptionBuilder.getInstance()
                    .buildCOREEnvelopeRealTimeErrorResponse(coreEnvelopeRealTimeRequest);
            }
        } catch (Exception e) {
            LOG.error("Failed to send X12 DS request to Nwhin: {}", e.getLocalizedMessage(), e);
        }
        return realTimeResponse;
    }
}
