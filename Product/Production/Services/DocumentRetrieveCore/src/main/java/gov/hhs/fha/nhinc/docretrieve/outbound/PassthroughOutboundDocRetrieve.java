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
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveFileUtils;
import gov.hhs.fha.nhinc.docretrieve.MessageGenerator;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveDelegate;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundPassthroughDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.orchestration.CONNECTOutboundOrchestrator;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import java.io.IOException;

import org.apache.log4j.Logger;

public class PassthroughOutboundDocRetrieve extends AbstractOutboundDocRetrieve implements OutboundDocRetrieve {

    private static final Logger LOG = Logger.getLogger(PassthroughOutboundDocRetrieve.class);

    private final CONNECTOutboundOrchestrator orchestrator;

    /**
     * Constructor.
     */
    public PassthroughOutboundDocRetrieve() {
        orchestrator = new CONNECTOutboundOrchestrator();
    }

    /**
     * Constructor with dependency injection parameters.
     * 
     * @param orchestrator
     */
    public PassthroughOutboundDocRetrieve(CONNECTOutboundOrchestrator orchestrator) {
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
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetCommunitiesType targets,
            ADAPTER_API_LEVEL entityAPILevel) {

        RetrieveDocumentSetResponseType response = null;
        if (validateGuidance(targets, entityAPILevel)) {

            NhinTargetSystemType targetSystem = MessageGeneratorUtils.getInstance().convertFirstToNhinTargetSystemType(
                    targets);

            OutboundDocRetrieveDelegate delegate = new OutboundDocRetrieveDelegate();
            OutboundDocRetrieveOrchestratable orchestratable = new OutboundPassthroughDocRetrieveOrchestratable(null,
                    null, delegate, null, request, assertion, targetSystem);

            OutboundDocRetrieveOrchestratable orchResponse = (OutboundDocRetrieveOrchestratable) orchestrator
                    .process(orchestratable);
            response = orchResponse.getResponse();

            try {
                DocRetrieveFileUtils.getInstance().streamDocumentsToFileSystemIfEnabled(response);
            } catch (IOException ioe) {
                LOG.error("Failed to save documents to file system.", ioe);
                response = MessageGenerator.getInstance().createRegistryResponseError(
                        "Adapter Document Retrieve Processing: " + ioe.getLocalizedMessage());
            }
        } else {
            response = createGuidanceErrorResponse(entityAPILevel);
        }

        return response;
    }

}
