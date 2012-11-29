/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 * 
 */
package gov.hhs.fha.nhinc.docretrieve._30.entity;

import gov.hhs.fha.nhinc.aspect.OutboundMessageEvent;
import gov.hhs.fha.nhinc.docretrieve.aspect.RespondingGatewayCrossGatewayRetrieveRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.entity.EntityDocRetrieveOrchImpl;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.Addressing;

/**
 * 
 * @author dunnek
 */

@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class EntityDocRetrieve implements gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrievePortType {

    private EntityDocRetrieveOrchImpl orchImpl;
    
    @OutboundMessageEvent(beforeBuilder = RespondingGatewayCrossGatewayRetrieveRequestTypeDescriptionBuilder.class,
            afterReturningBuilder = RetrieveDocumentSetResponseTypeDescriptionBuilder.class, 
            serviceType = "Retrieve Document",version = "3.0")
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest) {
        return getImpl().respondingGatewayCrossGatewayRetrieve(
                respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest(),
                respondingGatewayCrossGatewayRetrieveRequest.getAssertion());
    }

    protected EntityDocRetrieveImpl getImpl() {
        return new EntityDocRetrieveImpl(orchImpl);
    }
    
    public void setOrchestratorImpl(EntityDocRetrieveOrchImpl orchImpl) {
        this.orchImpl = orchImpl;
    }
}
