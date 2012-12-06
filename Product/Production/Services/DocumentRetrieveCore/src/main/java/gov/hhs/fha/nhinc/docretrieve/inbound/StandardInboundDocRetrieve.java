/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.inbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveAuditTransformer_g0;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveDelegate;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrievePolicyTransformer_g0;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundStandardDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.CONNECTInboundOrchestrator;
import gov.hhs.fha.nhinc.orchestration.InboundDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

public class StandardInboundDocRetrieve extends BaseInboundDocRetrieve {

    private final PolicyTransformer pt;
    private final AuditTransformer at;
    private final InboundDelegate ad;
    private final CONNECTInboundOrchestrator orch;

    /**
     * Constructor.
     */
    public StandardInboundDocRetrieve() {
        pt = new InboundDocRetrievePolicyTransformer_g0();
        at = new InboundDocRetrieveAuditTransformer_g0();
        ad = new InboundDocRetrieveDelegate();
        orch = new CONNECTInboundOrchestrator();
    }
    
    /**
     * Constructor with dependency injection parameters.
     * 
     * @param pt
     * @param at
     * @param ad
     * @param orch
     */
    public StandardInboundDocRetrieve(PolicyTransformer pt, AuditTransformer at, InboundDelegate ad,
            CONNECTInboundOrchestrator orch) {
        this.pt = pt;
        this.at = at;
        this.ad = ad;
        this.orch = orch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.docretrieve.inbound.BaseInboundDocRetrieve#createInboundOrchestrable(ihe.iti.xds_b._2007.
     * RetrieveDocumentSetRequestType, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType)
     */
    @Override
    public InboundDocRetrieveOrchestratable createInboundOrchestrable(RetrieveDocumentSetRequestType body,
            AssertionType assertion) {
        InboundDocRetrieveOrchestratable inboundOrchestrable = new InboundStandardDocRetrieveOrchestratable(pt, at, ad);
        inboundOrchestrable.setAssertion(assertion);
        inboundOrchestrable.setRequest(body);
        
        return inboundOrchestrable;
    }
    
    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.docretrieve.inbound.BaseInboundDocRetrieve#getOrchestrator()
     */
    @Override
    CONNECTInboundOrchestrator getOrchestrator() {
        return orch;
    }
}
