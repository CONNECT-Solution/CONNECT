/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityDocRetrieveDeferredResponseSecured", portName = "EntityDocRetrieveDeferredResponseSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveDeferredResponseSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocretrievesecured", wsdlLocation = "WEB-INF/wsdl/EntityDocRetrieveDeferredRespSecured/EntityDocRetrieveDeferredRespSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class EntityDocRetrieveDeferredRespSecured extends EntityDocRetrieveDeferredResponseImpl {

    @Resource
    private WebServiceContext context;

    /**
     * 
     * @param body
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body) {
        return crossGatewayRetrieveResponse(body, context);
    }
}
