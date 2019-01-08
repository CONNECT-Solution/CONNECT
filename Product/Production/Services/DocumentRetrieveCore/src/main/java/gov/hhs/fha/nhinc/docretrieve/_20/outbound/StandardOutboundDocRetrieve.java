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
package gov.hhs.fha.nhinc.docretrieve._20.outbound;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.docretrieve._20.ResponseScrubber;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.audit.DocRetrieveAuditLogger;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.orchestration.CONNECTOutboundOrchestrator;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 * @author akong
 *
 */
public class StandardOutboundDocRetrieve extends gov.hhs.fha.nhinc.docretrieve.outbound.StandardOutboundDocRetrieve {

    private DocRetrieveAuditLogger auditLogger;

    /**
     * Constructor.
     */
    public StandardOutboundDocRetrieve() {
        super();
        auditLogger = new DocRetrieveAuditLogger();
    }

    /**
     * Constructor with dependency injection parameters.
     *
     * @param orchestrator
     */
    public StandardOutboundDocRetrieve(CONNECTOutboundOrchestrator orchestrator, DocRetrieveAuditLogger auditLogger) {
        super(orchestrator, auditLogger);
    }

    /**
     * Processes the document retrieve message. On success, will return a response from the NwHIN. This method is
     * specific to the DR 2.0 specification as it will remove fields that are not compliant.
     *
     * @param body
     * @param assertion
     * @param targets
     * @return response message to be sent back to the requester
     */
    @Override
    @OutboundProcessingEvent(beforeBuilder = RetrieveDocumentSetRequestTypeDescriptionBuilder.class,
        afterReturningBuilder = RetrieveDocumentSetResponseTypeDescriptionBuilder.class,
        serviceType = "Retrieve Document", version = "2.0")
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body,
        AssertionType assertion, NhinTargetCommunitiesType targets, ADAPTER_API_LEVEL entityAPILevel) {

        RetrieveDocumentSetResponseType response = super
            .respondingGatewayCrossGatewayRetrieve(body, assertion, targets, entityAPILevel);

        ResponseScrubber.getInstance().scrub(response);

        return response;
    }

}
