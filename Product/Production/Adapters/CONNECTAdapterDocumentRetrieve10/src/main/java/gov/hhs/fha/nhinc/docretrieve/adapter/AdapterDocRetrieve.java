/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.adapter;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author westberg
 */
@WebService(serviceName = "AdapterDocRetrieve", portName = "AdapterDocRetrievePortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrieve.AdapterDocRetrievePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrieve", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieve/AdapterDocRetrieve.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocRetrieve
{
    @Resource
    private WebServiceContext context;

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest)
    {
        return new AdapterDocRetrieveImpl().respondingGatewayCrossGatewayRetrieveUnsecured(respondingGatewayCrossGatewayRetrieveRequest, context);
    }
}
