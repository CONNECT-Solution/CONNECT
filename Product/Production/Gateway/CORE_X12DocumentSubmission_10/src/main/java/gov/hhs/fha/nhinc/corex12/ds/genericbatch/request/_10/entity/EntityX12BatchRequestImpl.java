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
package gov.hhs.fha.nhinc.corex12.ds.genericbatch.request._10.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayBatchSubmissionRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayBatchSubmissionResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayBatchSubmissionSecuredRequestType;
import gov.hhs.fha.nhinc.corex12.ds.genericbatch.common.outbound.OutboundX12Batch;
import gov.hhs.fha.nhinc.corex12.ds.utils.X12EntityExceptionBuilder;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.lang.StringUtils;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author svalluripalli, cmay
 */
public class EntityX12BatchRequestImpl extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(EntityX12BatchRequestImpl.class);
    private final OutboundX12Batch outboundCORE_X12DSGenericBatch;

    /**
     * Constructor
     *
     * @param outboundCORE_X12DSGenericBatch
     */
    public EntityX12BatchRequestImpl(OutboundX12Batch outboundCORE_X12DSGenericBatch) {
        this.outboundCORE_X12DSGenericBatch = outboundCORE_X12DSGenericBatch;
    }

    /**
     *
     * @param body
     * @param context
     * @return RespondingGatewayCrossGatewayBatchSubmissionResponseMessageSecuredRequestType
     */
    public RespondingGatewayCrossGatewayBatchSubmissionResponseMessageType batchSubmitTransaction(
        RespondingGatewayCrossGatewayBatchSubmissionSecuredRequestType body, WebServiceContext context) {

        RespondingGatewayCrossGatewayBatchSubmissionResponseMessageType response
            = new RespondingGatewayCrossGatewayBatchSubmissionResponseMessageType();
        response.setCOREEnvelopeBatchSubmissionResponse(callOutboundBatchSubmitTransaction(
            body.getCOREEnvelopeBatchSubmission(), getAssertion(context), body.getNhinTargetCommunities()));

        return response;
    }

    /**
     *
     * @param body
     * @param context
     * @return RespondingGatewayCrossGatewayBatchSubmissionResponseMessageRequestType
     */
    public RespondingGatewayCrossGatewayBatchSubmissionResponseMessageType batchSubmitTransaction(
        RespondingGatewayCrossGatewayBatchSubmissionRequestType body, WebServiceContext context) {

        RespondingGatewayCrossGatewayBatchSubmissionResponseMessageType response
            = new RespondingGatewayCrossGatewayBatchSubmissionResponseMessageType();
        response.setCOREEnvelopeBatchSubmissionResponse(callOutboundBatchSubmitTransaction(
            body.getCOREEnvelopeBatchSubmission(), body.getAssertion(), body.getNhinTargetCommunities()));

        return response;
    }

    /**
     *
     * @param coreEnvelopeBatchSubmission
     * @param assertion
     * @param target
     * @return COREEnvelopeBatchSubmissionResponse
     */
    private COREEnvelopeBatchSubmissionResponse callOutboundBatchSubmitTransaction(
        COREEnvelopeBatchSubmission coreEnvelopeBatchSubmission, AssertionType assertion,
        NhinTargetCommunitiesType target) {

        COREEnvelopeBatchSubmissionResponse batchSubmissionResponse = null;

        try {
            if (StringUtils.isNotEmpty(HomeCommunityMap.getCommunityIdFromTargetCommunities(target))) {
                batchSubmissionResponse = outboundCORE_X12DSGenericBatch
                    .batchSubmitTransaction(coreEnvelopeBatchSubmission, assertion, target, null);
            } else {
                batchSubmissionResponse = X12EntityExceptionBuilder.getInstance()
                    .buildCOREEnvelopeGenericBatchErrorResponse(coreEnvelopeBatchSubmission);
            }
        } catch (Exception e) {
            LOG.error("Failed to send X12DS request to Nwhin: {}", e.getLocalizedMessage(), e);
        }

        return batchSubmissionResponse;
    }
}
