/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 * 
 */
package gov.hhs.fha.nhinc.docretrieve._30.entity;

import gov.hhs.fha.nhinc.aspect.OutboundMessageEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetTransformingBuilder;
import gov.hhs.fha.nhinc.docretrieve.outbound.OutboundDocRetrieve;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrievePortType;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;


@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class EntityDocRetrieve extends BaseService implements EntityDocRetrievePortType {

    private OutboundDocRetrieve outboundDocRetrieve;
    
    private WebServiceContext context;

    @OutboundMessageEvent(beforeBuilder = RetrieveDocumentSetTransformingBuilder.class,
            afterReturningBuilder = RetrieveDocumentSetResponseTypeDescriptionBuilder.class,
            serviceType = "Retrieve Document", version = "3.0")
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            RespondingGatewayCrossGatewayRetrieveRequestType request) {
        AssertionType assertion = getAssertion(context, request.getAssertion());

        return outboundDocRetrieve.respondingGatewayCrossGatewayRetrieve(request.getRetrieveDocumentSetRequest(),
                assertion, request.getNhinTargetCommunities());
    }

    @Resource
    public void setContext(WebServiceContext context) {
        this.context = context;
    }

    public void setOutboundDocRetrieve(OutboundDocRetrieve outboundDocRetrieve) {
        this.outboundDocRetrieve = outboundDocRetrieve;
    }
}
