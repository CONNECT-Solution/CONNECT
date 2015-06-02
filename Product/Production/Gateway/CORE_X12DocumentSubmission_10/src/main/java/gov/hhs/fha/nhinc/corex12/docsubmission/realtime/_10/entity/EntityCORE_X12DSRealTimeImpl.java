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
package gov.hhs.fha.nhinc.corex12.docsubmission.realtime._10.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRealTimeRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRealTimeResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRealTimeSecuredRequestType;
import gov.hhs.fha.nhinc.corex12.docsubmission.realtime.outbound.OutboundCORE_X12DSRealTime;
import gov.hhs.fha.nhinc.corex12.docsubmission.realtime.outbound.PassthroughOutboundCORE_X12DSRealTime;
import gov.hhs.fha.nhinc.corex12.docsubmission.utils.CORE_X12DSEntityExceptionBuilder;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import javax.xml.ws.WebServiceContext;
import org.apache.log4j.Logger;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;

/**
 *
 * @author svalluripalli
 */
public class EntityCORE_X12DSRealTimeImpl extends CORE_X12DSEntityExceptionBuilder {

    private static final Logger LOG = Logger.getLogger(EntityCORE_X12DSRealTimeImpl.class);

    private OutboundCORE_X12DSRealTime outboundCORE_X12DSRealTime;

    /**
     *
     * @param outboundCORE_X12DSRealTime
     */
    public EntityCORE_X12DSRealTimeImpl(OutboundCORE_X12DSRealTime outboundCORE_X12DSRealTime) {
        // TODO: Once injection for outboundCORE_X12DSRealTime is fully implemented, remove this conditional
        // statement, the entire else clause, and the import for PassthroughOutboundCORE_X12DSRealTime
        if (outboundCORE_X12DSRealTime != null) {
            this.outboundCORE_X12DSRealTime = outboundCORE_X12DSRealTime;
        } else {
            this.outboundCORE_X12DSRealTime = new PassthroughOutboundCORE_X12DSRealTime();
        }
    }

    /**
     *
     * @param body
     * @return RespondingGatewayCrossGatewayRealTimeResponseType
     */
    public RespondingGatewayCrossGatewayRealTimeResponseType realTimeTransaction(RespondingGatewayCrossGatewayRealTimeRequestType body, WebServiceContext context) {
        COREEnvelopeRealTimeResponse realTimeResponse = callOutboundRealTimeTransaction(body.getCOREEnvelopeRealTimeRequest(), body.getAssertion(), body.getNhinTargetCommunities());
        RespondingGatewayCrossGatewayRealTimeResponseType response = new RespondingGatewayCrossGatewayRealTimeResponseType();
        response.setCOREEnvelopeRealTimeResponse(realTimeResponse);
        return response;
    }

    /**
     *
     * @param body
     * @return RespondingGatewayCrossGatewayRealTimeResponseType
     */
    public RespondingGatewayCrossGatewayRealTimeResponseType realTimeTransactionSecured(RespondingGatewayCrossGatewayRealTimeSecuredRequestType body, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, null);
        COREEnvelopeRealTimeResponse realTimeResponse = callOutboundRealTimeTransaction(body.getCOREEnvelopeRealTimeRequest(), assertion, body.getNhinTargetCommunities());
        RespondingGatewayCrossGatewayRealTimeResponseType response = new RespondingGatewayCrossGatewayRealTimeResponseType();
        response.setCOREEnvelopeRealTimeResponse(realTimeResponse);
        return response;
    }

    private COREEnvelopeRealTimeResponse callOutboundRealTimeTransaction(COREEnvelopeRealTimeRequest oCOREEnvelopeRealTimeRequest, AssertionType assertion, NhinTargetCommunitiesType target) {
        COREEnvelopeRealTimeResponse realTimeResponse = null;
        try {
            if (null != HomeCommunityMap.getCommunityIdFromTargetCommunities(target) && HomeCommunityMap.getCommunityIdFromTargetCommunities(target).length() > 0) {
                realTimeResponse = outboundCORE_X12DSRealTime.realTimeTransaction(oCOREEnvelopeRealTimeRequest, assertion, target, null);
            } else {
                realTimeResponse = new COREEnvelopeRealTimeResponse();
                buildCOREEnvelopeRealTimeErrorResponse(oCOREEnvelopeRealTimeRequest, realTimeResponse);
            }
        } catch (Exception e) {
            LOG.error("Failed to send X12DS request to Nwhin.", e);
        }
        return realTimeResponse;
    }
}
