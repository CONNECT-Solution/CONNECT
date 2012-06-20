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
package gov.hhs.fha.nhinc.docretrieve._20.entity;

import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import javax.xml.ws.WebServiceContext;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve._20.ResponseScrubber;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveAggregator_a0;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveAuditTransformer_a0;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveDelegate;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveOrchestratableImpl;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveOrchestratorImpl;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrievePolicyTransformer_a0;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;

/**
 *
 * @author dunnek
 */
public class EntityDocRetreiveImpl {

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body, final WebServiceContext context) {
        AssertionType assertion = getAssertion(context, null);

        return this.respondingGatewayCrossGatewayRetrieve(body, assertion);
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body, AssertionType assertion,
            final WebServiceContext context) {
        AssertionType assertionWithId = getAssertion(context, assertion);

        return this.respondingGatewayCrossGatewayRetrieve(body, assertionWithId);
    }

    /**
     * Entity inbound respondingGatewayCrossGatewayRetrieve
     *
     * @param body
     * @param assertion
     * @return
     */
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body, AssertionType assertion) {

        PolicyTransformer pt = new OutboundDocRetrievePolicyTransformer_a0();
        AuditTransformer at = new OutboundDocRetrieveAuditTransformer_a0();
        OutboundDelegate nd = new OutboundDocRetrieveDelegate();
        NhinAggregator na = new OutboundDocRetrieveAggregator_a0();
        OutboundDocRetrieveOrchestratable EntityDROrchImpl = new OutboundDocRetrieveOrchestratableImpl(body,
                assertion, pt, at, nd, na, null);
        OutboundDocRetrieveOrchestratorImpl oOrchestrator = new OutboundDocRetrieveOrchestratorImpl();
        oOrchestrator.process(EntityDROrchImpl);

        ResponseScrubber rs = new ResponseScrubber();
        return rs.Scrub(EntityDROrchImpl.getResponse());
    }

    private AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (oAssertionIn == null) {
            assertion = SamlTokenExtractor.GetAssertion(context);
        } else {
            assertion = oAssertionIn;
        }

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }

        return assertion;
    }
}
