/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docretrieve._30.nhin;

import gov.hhs.fha.nhinc.aspect.InboundMessageEvent;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.nhin.NhinDocRetrieveOrchImpl;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 * 
 * @author Neil Webb
 */

@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
@Addressing(enabled = true)
public class DocRetrieve implements ihe.iti.xds_b._2007.RespondingGatewayRetrievePortType {
    
    private NhinDocRetrieveOrchImpl orchImpl;
    
    @Resource
    private WebServiceContext context;

    /**
     * The web service implementation for Document Retrieve.
     * @param body the message of the request
     * @return the document set of the retrieve
     */
    @InboundMessageEvent(beforeBuilder = RetrieveDocumentSetRequestTypeDescriptionBuilder.class,
            afterReturningBuilder = RetrieveDocumentSetResponseTypeDescriptionBuilder.class, 
            serviceType = "Retrieve Document",version = "3.0")
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body) {
        return new DocRetrieveImpl(orchImpl).respondingGatewayCrossGatewayRetrieve(body, context);
    }
    
    public void setOrchestratorImpl(NhinDocRetrieveOrchImpl orchImpl) {
        this.orchImpl = orchImpl;
    }
}
