/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.sql.Timestamp;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author dunnek
 */
public class EntityDocRetreiveImpl {

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body, final WebServiceContext context) {
        AssertionType assertion = getAssertion(context, null);

        return this.respondingGatewayCrossGatewayRetrieve(body, assertion);
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body, AssertionType assertion, final WebServiceContext context) {
        AssertionType assertionWithId = getAssertion(context, assertion);

        return this.respondingGatewayCrossGatewayRetrieve(body, assertionWithId);
    }

    /**
     *  Entity inbound respondingGatewayCrossGatewayRetrieve
     * @param body
     * @param assertion
     * @return
     */
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body, AssertionType assertion) {

        // Log the start of the performance record
        String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();
        Timestamp starttime = new Timestamp(System.currentTimeMillis());
        Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime, NhincConstants.DOC_RETRIEVE_SERVICE_NAME, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, homeCommunityId);

        RetrieveDocumentSetResponseType response = getEntityOrchImpl().respondingGatewayCrossGatewayRetrieve(body, assertion);

        // Log the end of the performance record
        Timestamp stoptime = new Timestamp(System.currentTimeMillis());
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);

        return response;
    }

    protected EntityDocRetrieveOrchImpl getEntityOrchImpl() {
        return new EntityDocRetrieveOrchImpl();
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
