/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docretrieve._30.entity;

import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import javax.xml.ws.WebServiceContext;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveAggregator_a0;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveAuditTransformer_a0;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveDelegate;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveOrchestratable;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveOrchestratableImpl;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveOrchestratorImpl;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrievePolicyTransformer_a0;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;

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

        // Log the start of the performance record
        String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(
                NhincConstants.DOC_RETRIEVE_SERVICE_NAME, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, homeCommunityId);

        PolicyTransformer pt = new OutboundDocRetrievePolicyTransformer_a0();
        AuditTransformer at = new OutboundDocRetrieveAuditTransformer_a0();
        OutboundDelegate nd = new OutboundDocRetrieveDelegate();
        NhinAggregator na = new OutboundDocRetrieveAggregator_a0();
        OutboundDocRetrieveOrchestratable EntityDROrchImpl = new OutboundDocRetrieveOrchestratableImpl(body, assertion,
                pt, at, nd, na, null);
        OutboundDocRetrieveOrchestratorImpl oOrchestrator = new OutboundDocRetrieveOrchestratorImpl();
        oOrchestrator.process(EntityDROrchImpl);

        // Log the end of the performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(NhincConstants.DOC_RETRIEVE_SERVICE_NAME,
                NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, homeCommunityId);

        return EntityDROrchImpl.getResponse();
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
