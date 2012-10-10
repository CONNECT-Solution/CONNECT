/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docretrieve._30.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.entity.EntityDocRetrieveOrchImpl;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import javax.xml.ws.WebServiceContext;

/**
 * 
 * @author dunnek
 */
public class EntityDocRetrieveImpl extends BaseService {

    private EntityDocRetrieveOrchImpl orchImpl;
    
    public EntityDocRetrieveImpl(EntityDocRetrieveOrchImpl orchImpl) {
        this.orchImpl = orchImpl;
    }
    
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
        
        return orchImpl.respondingGatewayCrossGatewayRetrieve(body, assertion);
    }
}
