/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docquery.outbound;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryRequestDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.entity.OutboundDocQueryDelegate;
import gov.hhs.fha.nhinc.docquery.entity.OutboundDocQueryOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import java.util.Iterator;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.log4j.Logger;

public class PassthroughOutboundDocQuery implements OutboundDocQuery {

    private static final Logger LOG = Logger.getLogger(PassthroughOutboundDocQuery.class);

    private OutboundDocQueryDelegate delegate = new OutboundDocQueryDelegate();

    public PassthroughOutboundDocQuery() {
        super();
    }

    public PassthroughOutboundDocQuery(OutboundDocQueryDelegate delegate) {
        this.delegate = delegate;
    }

    /**
     * This method sends a AdhocQueryRequest to the NwHIN to a single gateway.
     * 
     * @param request
     *            the AdhocQueryRequest message to be sent
     * @param assertion
     *            the AssertionType instance received from the adapter
     * @param target
     *            NhinTargetCommunitiesType where DocQuery Request is to be sent. Only the first one is used.
     * @return AdhocQueryResponse received from the NHIN
     */
    @Override
    @OutboundProcessingEvent(beforeBuilder = AdhocQueryRequestDescriptionBuilder.class,
            afterReturningBuilder = AdhocQueryResponseDescriptionBuilder.class, serviceType = "Document Query",
            version = "")
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest request, AssertionType assertion,
            NhinTargetCommunitiesType targets) {

        NhinTargetSystemType target = MessageGeneratorUtils.getInstance().convertFirstToNhinTargetSystemType(targets);
        String targetHCID = getTargetHCID(target);

        if (targets.getNhinTargetCommunity().size() > 1) {
            warnTooManyTargets(targetHCID, targets);
        }

        AdhocQueryResponse response = sendRequestToNwhin(request, assertion, target, targetHCID);

        return response;
    }

    private String getTargetHCID(NhinTargetSystemType target) {
        String targetHCID = null;
        if (target != null && target.getHomeCommunity() != null) {
            targetHCID = target.getHomeCommunity().getHomeCommunityId();
        }

        return targetHCID;
    }

    private AdhocQueryResponse sendRequestToNwhin(AdhocQueryRequest request, AssertionType assertion,
            NhinTargetSystemType target, String targetCommunityID) {
        AdhocQueryResponse response = null;

        try {
            OutboundDocQueryOrchestratable orchestratable = new OutboundDocQueryOrchestratable(delegate, null, null,
                    assertion, NhincConstants.DOC_QUERY_SERVICE_NAME, target, request);
            response = delegate.process(orchestratable).getResponse();
        } catch (Exception ex) {
            String errorMsg = "Error from target homeId = " + targetCommunityID + ". " + ex.getMessage();
            response = MessageGeneratorUtils.getInstance().createRepositoryErrorResponse(errorMsg);
            LOG.error(errorMsg, ex);
        }

        return response;
    }

    private void warnTooManyTargets(String targetHCID, NhinTargetCommunitiesType targets) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Multiple targets in request message in passthrough mode."
                + "  Only sending to target HCID: " + targetHCID + ".");
        stringBuilder.append("  Not sending request to: ");
        Iterator<NhinTargetCommunityType> communityIterator = targets.getNhinTargetCommunity().iterator();
        Boolean first = true;
        while (communityIterator.hasNext()) {
            String nextTarget = communityIterator.next().getHomeCommunity().getHomeCommunityId();
            if (!nextTarget.equals(targetHCID)) {
                if (first) {
                    first = false;
                } else {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(nextTarget);
            }
        }
        stringBuilder.append(".");
        logWarning(stringBuilder.toString());
    }

    /**
     * For unit testing the multiple target warning.
     * 
     * @param warning
     */
    protected void logWarning(String warning) {
        LOG.warn(warning);
    }

}
