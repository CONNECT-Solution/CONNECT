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
package gov.hhs.fha.nhinc.docretrieve.inbound;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.logInfoServiceProcess;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.audit.DocRetrieveAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveDelegate;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrievePolicyTransformer_g0;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundStandardDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.orchestration.CONNECTInboundOrchestrator;
import gov.hhs.fha.nhinc.orchestration.InboundDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.Properties;

public class StandardInboundDocRetrieve extends BaseInboundDocRetrieve {

    private final PolicyTransformer pt;
    private final InboundDelegate ad;
    private final CONNECTInboundOrchestrator orch;
    private final DocRetrieveAuditLogger auditLogger;

    /**
     * Constructor.
     */
    public StandardInboundDocRetrieve() {
        pt = new InboundDocRetrievePolicyTransformer_g0();
        ad = new InboundDocRetrieveDelegate();
        orch = new CONNECTInboundOrchestrator();
        auditLogger = new DocRetrieveAuditLogger();
    }

    /**
     * Constructor with dependency injection parameters.
     *
     * @param pt
     * @param at
     * @param ad
     * @param orch
     */
    public StandardInboundDocRetrieve(PolicyTransformer pt, InboundDelegate ad,
        CONNECTInboundOrchestrator orch, DocRetrieveAuditLogger auditLogger) {
        this.pt = pt;
        this.ad = ad;
        this.orch = orch;
        this.auditLogger = auditLogger;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.docretrieve.inbound.BaseInboundDocRetrieve#createInboundOrchestrable(ihe.iti.xds_b._2007.
     * RetrieveDocumentSetRequestType, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType)
     */
    @Override
    public InboundDocRetrieveOrchestratable createInboundOrchestrable(RetrieveDocumentSetRequestType body,
        AssertionType assertion, Properties webContextProperties) {
        InboundDocRetrieveOrchestratable inboundOrchestrable = new InboundStandardDocRetrieveOrchestratable(pt, ad);
        inboundOrchestrable.setAssertion(assertion);
        inboundOrchestrable.setRequest(body);
        inboundOrchestrable.setWebContextProperties(webContextProperties);

        return inboundOrchestrable;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.docretrieve.inbound.BaseInboundDocRetrieve#getOrchestrator()
     */
    @Override
    public CONNECTInboundOrchestrator getOrchestrator() {
        return orch;
    }

    @Override
    @InboundProcessingEvent(beforeBuilder = RetrieveDocumentSetRequestTypeDescriptionBuilder.class,
    afterReturningBuilder = RetrieveDocumentSetResponseTypeDescriptionBuilder.class,
    serviceType = "Retrieve Document", version = "")
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body,
        AssertionType assertion, Properties webContextProperties) {
        logInfoServiceProcess(this.getClass());

        InboundDocRetrieveOrchestratable inboundOrchestrable = createInboundOrchestrable(body, assertion,
            webContextProperties);

        InboundDocRetrieveOrchestratable orchResponse = (InboundDocRetrieveOrchestratable) getOrchestrator().process(
            inboundOrchestrable);
        auditResponse(body, orchResponse.getResponse(), assertion, webContextProperties);
        return orchResponse.getResponse();
    }

    @Override
    public DocRetrieveAuditLogger getAuditLogger() {
        return auditLogger;
    }
}
