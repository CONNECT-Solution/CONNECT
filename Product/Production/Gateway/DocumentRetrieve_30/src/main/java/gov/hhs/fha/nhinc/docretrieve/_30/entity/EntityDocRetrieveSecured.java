/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 * 
 */
package gov.hhs.fha.nhinc.docretrieve._30.entity;

import gov.hhs.fha.nhinc.aspect.OutboundMessageEvent;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.entity.EntityDocRetrieveOrchImpl;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 * 
 * @author Sai Valluripalli
 */

@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class EntityDocRetrieveSecured implements gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveSecuredPortType {

    private EntityDocRetrieveOrchImpl orchImpl;
    
    @Resource
    private WebServiceContext context;

    @OutboundMessageEvent(beforeBuilder = RetrieveDocumentSetRequestTypeDescriptionBuilder.class,
            afterReturningBuilder = RetrieveDocumentSetResponseTypeDescriptionBuilder.class, 
            serviceType = "Retrieve Document",version = "3.0")
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body) {
        return getImpl().respondingGatewayCrossGatewayRetrieve(body, context);
    }

    protected EntityDocRetrieveImpl getImpl() {
        return new EntityDocRetrieveImpl(orchImpl);
    }
    
    public void setOrchestratorImpl(EntityDocRetrieveOrchImpl orchImpl) {
        this.orchImpl = orchImpl;
    }
}
