/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.inbound;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveAuditTransformer_g0;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveDelegate;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrievePolicyTransformer_g0;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundPassThroughDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundStandardDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.CONNECTInboundOrchestrator;
import gov.hhs.fha.nhinc.orchestration.InboundDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the Web Service implementation of Nhin Doc Retrieve.
 * 
 * @author vvickers, Les Westberg
 */
public class PassThroughDocRetrieve implements DocRetrieveService {

    private static Log log = LogFactory.getLog(PassThroughDocRetrieve.class);

    private PolicyTransformer pt;
    private AuditTransformer at;
    private InboundDelegate ad;
    private CONNECTInboundOrchestrator oOrchestrator;

    public PassThroughDocRetrieve() {
        pt = new InboundDocRetrievePolicyTransformer_g0();
        at = new InboundDocRetrieveAuditTransformer_g0();
        ad = new InboundDocRetrieveDelegate();
        oOrchestrator = new CONNECTInboundOrchestrator();
    }

    @InboundProcessingEvent(beforeBuilder = RetrieveDocumentSetRequestTypeDescriptionBuilder.class,
            afterReturningBuilder = RetrieveDocumentSetResponseTypeDescriptionBuilder.class, 
            serviceType = "Retrieve Document", version = "")
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body,
            AssertionType assertion) {
        log.debug("Entering DocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");

        InboundDocRetrieveOrchestratable message = new InboundPassThroughDocRetrieveOrchestratable(pt, at, ad);
        message.setAssertion(assertion);
        message.setRequest(body);

        InboundDocRetrieveOrchestratable OrchResponse = (InboundDocRetrieveOrchestratable) oOrchestrator
                .process(message);

        RetrieveDocumentSetResponseType response = OrchResponse.getResponse();

        // Send response back to the initiating Gateway
        log.debug("Exiting DocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");
        return response;
    }
}
