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
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.CONNECTOutboundOrchestrator;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;

/**
 *
 * @author mweaver
 */
public class OutboundStandardDocRetrieveOrchestrator extends CONNECTOutboundOrchestrator {

    /**
     * Processes the doc retrieve message by processing each individual request and aggregating the responses.
     *
     * @param message
     */
    @Override
    public Orchestratable processEnabledMessage(Orchestratable message) {
        OutboundDocRetrieveOrchestratable rdMessage = (OutboundDocRetrieveOrchestratable) message;

        for (DocumentRequest docRequest : rdMessage.getRequest().getDocumentRequest()) {
            RetrieveDocumentSetRequestType rdRequest = new RetrieveDocumentSetRequestType();
            rdRequest.getDocumentRequest().add(docRequest);

            OutboundDocRetrieveOrchestratable newMessage = rdMessage.create(rdMessage.getPolicyTransformer(),
                rdMessage.getNhinDelegate(), rdMessage.getAggregator());
            newMessage.setRequest(rdRequest);
            newMessage.setAssertion(message.getAssertion());
            newMessage.setTarget(buildHomeCommunity(docRequest.getHomeCommunityId(), rdMessage.getTarget().
                getUseSpecVersion(), rdMessage.getTarget().getExchangeName()));

            // Process and aggregate
            NhinAggregator agg = rdMessage.getAggregator();
            agg.aggregate((OutboundOrchestratable) message,
                (OutboundOrchestratable) super.processEnabledMessage(newMessage));
        }
        return message;
    }

    private static NhinTargetSystemType buildHomeCommunity(String homeCommunityId, String guidance,
        String exchangeName) {
        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        //set the prefix for the homecommunity ID if its not present
        homeCommunity.setHomeCommunityId(HomeCommunityMap.getHomeCommunityIdWithPrefix(homeCommunityId));
        nhinTargetSystem.setHomeCommunity(homeCommunity);
        nhinTargetSystem.setUseSpecVersion(guidance);
        nhinTargetSystem.setExchangeName(exchangeName);
        return nhinTargetSystem;
    }
}
