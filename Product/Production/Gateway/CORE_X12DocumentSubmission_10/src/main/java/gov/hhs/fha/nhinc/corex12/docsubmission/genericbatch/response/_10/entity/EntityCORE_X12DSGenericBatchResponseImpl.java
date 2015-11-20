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
package gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.response._10.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayBatchSubmissionRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayBatchSubmissionResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayBatchSubmissionSecuredRequestType;
import gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.response.outbound.OutboundCORE_X12DSGenericBatchResponse;
import gov.hhs.fha.nhinc.corex12.docsubmission.utils.CORE_X12DSEntityExceptionBuilder;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import javax.xml.ws.WebServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;

/**
 *
 * @author svalluripalli
 */
public class EntityCORE_X12DSGenericBatchResponseImpl extends CORE_X12DSEntityExceptionBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(EntityCORE_X12DSGenericBatchResponseImpl.class);
    private OutboundCORE_X12DSGenericBatchResponse outboundCORE_X12DSGenericBatchResponse;

    /**
     * Constructor
     *
     * @param outboundCORE_X12DSGenericBatchResponse
     */
    public EntityCORE_X12DSGenericBatchResponseImpl(OutboundCORE_X12DSGenericBatchResponse outboundCORE_X12DSGenericBatchResponse) {
        this.outboundCORE_X12DSGenericBatchResponse = outboundCORE_X12DSGenericBatchResponse;
    }

    /**
     *
     * @param body
     * @return RespondingGatewayCrossGatewayBatchSubmissionResponseMessageRequestType
     */
    public RespondingGatewayCrossGatewayBatchSubmissionResponseMessageType batchSubmitTransaction(RespondingGatewayCrossGatewayBatchSubmissionRequestType body, WebServiceContext context) {
        LOG.info("EntityCORE_X12DSGenericBatchResponseImpl.batchSubmitTransaction(RespondingGatewayCrossGatewayBatchSubmissionRequestType)");
        COREEnvelopeBatchSubmissionResponse oBatchSubmissionResponse = callOutboundBatchSubmitTransaction(body.getCOREEnvelopeBatchSubmission(), body.getAssertion(), body.getNhinTargetCommunities());
        RespondingGatewayCrossGatewayBatchSubmissionResponseMessageType oResponse = new RespondingGatewayCrossGatewayBatchSubmissionResponseMessageType();
        oResponse.setCOREEnvelopeBatchSubmissionResponse(oBatchSubmissionResponse);
        return oResponse;
    }

    /**
     *
     * @param body
     * @return RespondingGatewayCrossGatewayBatchSubmissionResponseMessageSecuredRequestType
     */
    public RespondingGatewayCrossGatewayBatchSubmissionResponseMessageType batchSubmitTransaction(RespondingGatewayCrossGatewayBatchSubmissionSecuredRequestType body, WebServiceContext context) {
        LOG.info("EntityCORE_X12DSGenericBatchResponseImpl.batchSubmitTransaction(RespondingGatewayCrossGatewayBatchSubmissionSecuredRequestType)");
        AssertionType assertion = getAssertion(context, null);
        COREEnvelopeBatchSubmissionResponse oBatchSubmissionResponse = callOutboundBatchSubmitTransaction(body.getCOREEnvelopeBatchSubmission(), assertion, body.getNhinTargetCommunities());
        RespondingGatewayCrossGatewayBatchSubmissionResponseMessageType oResponse = new RespondingGatewayCrossGatewayBatchSubmissionResponseMessageType();
        oResponse.setCOREEnvelopeBatchSubmissionResponse(oBatchSubmissionResponse);
        return oResponse;
    }

    private COREEnvelopeBatchSubmissionResponse callOutboundBatchSubmitTransaction(COREEnvelopeBatchSubmission oCOREEnvelopeBatchSubmission, AssertionType assertion, NhinTargetCommunitiesType target) {
        COREEnvelopeBatchSubmissionResponse oBatchSubmissionResponse = null;
        try {
            if (null != HomeCommunityMap.getCommunityIdFromTargetCommunities(target) && HomeCommunityMap.getCommunityIdFromTargetCommunities(target).length() > 0) {
                oBatchSubmissionResponse = outboundCORE_X12DSGenericBatchResponse.batchSubmitTransaction(oCOREEnvelopeBatchSubmission, assertion, target, null);
            } else {
                oBatchSubmissionResponse = new COREEnvelopeBatchSubmissionResponse();
                buildCOREEnvelopeGenericBatchErrorResponse(oCOREEnvelopeBatchSubmission, oBatchSubmissionResponse);
            }
        } catch (Exception e) {
            LOG.error("Failed to send X12DS request to Nwhin. " + e);
        }
        return oBatchSubmissionResponse;
    }
}
