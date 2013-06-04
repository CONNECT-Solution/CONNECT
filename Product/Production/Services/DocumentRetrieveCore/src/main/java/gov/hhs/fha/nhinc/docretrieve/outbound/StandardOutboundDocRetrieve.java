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
package gov.hhs.fha.nhinc.docretrieve.outbound;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveAggregator_a0;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveAuditTransformer_a0;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveDelegate;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrievePolicyTransformer_a0;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundStandardDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundStandardDocRetrieveOrchestrator;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.CONNECTOutboundOrchestrator;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

public class StandardOutboundDocRetrieve extends AbstractOutboundDocRetrieve implements OutboundDocRetrieve {

    private final CONNECTOutboundOrchestrator orchestrator;

    /**
     * Constructor.
     */
    public StandardOutboundDocRetrieve() {
        orchestrator = new OutboundStandardDocRetrieveOrchestrator();
    }

    /**
     * Constructor with dependency injection parameters.
     * 
     * @param orchestrator
     */
    public StandardOutboundDocRetrieve(CONNECTOutboundOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * gov.hhs.fha.nhinc.docretrieve.outbound.OutboundDocRetrieve#respondingGatewayCrossGatewayRetrieve(ihe.iti.xds_b
     * ._2007.RetrieveDocumentSetRequestType, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType)
     */
    @Override
    @OutboundProcessingEvent(beforeBuilder = RetrieveDocumentSetRequestTypeDescriptionBuilder.class, afterReturningBuilder = RetrieveDocumentSetResponseTypeDescriptionBuilder.class, serviceType = "Retrieve Document", version = "")
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body,
            AssertionType assertion, NhinTargetCommunitiesType targets, ADAPTER_API_LEVEL entityAPILevel) {

        RetrieveDocumentSetResponseType response = null;
        if (validateGuidance(targets, entityAPILevel)) {

            PolicyTransformer pt = new OutboundDocRetrievePolicyTransformer_a0();
            AuditTransformer at = new OutboundDocRetrieveAuditTransformer_a0();
            OutboundDelegate nd = new OutboundDocRetrieveDelegate();
            NhinAggregator na = new OutboundDocRetrieveAggregator_a0();
            NhinTargetSystemType target = MessageGeneratorUtils.getInstance().convertFirstToNhinTargetSystemType(
                    targets);
            OutboundDocRetrieveOrchestratable orchestratable = new OutboundStandardDocRetrieveOrchestratable(pt, at,
                    nd, na, body, assertion, target);
            OutboundDocRetrieveOrchestratable orchResponse = (OutboundDocRetrieveOrchestratable) orchestrator
                    .process(orchestratable);
            response = orchResponse.getResponse();
        } else {
            response = createGuidanceErrorResponse(entityAPILevel);
        }
        return response;
    }

}
