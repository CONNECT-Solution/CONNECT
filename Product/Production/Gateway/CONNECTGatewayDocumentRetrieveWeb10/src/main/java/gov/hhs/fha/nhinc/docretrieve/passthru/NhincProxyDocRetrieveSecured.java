/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.passthru;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocRetrieveSecured", portName = "NhincProxyDocRetrieveSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocretrievesecured.NhincProxyDocRetrieveSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocretrievesecured", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocRetrieveSecured/NhincProxyDocRetrieveSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class NhincProxyDocRetrieveSecured
{
    @Resource
    private WebServiceContext context;

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType body)
    {
        return new NhincProxyDocRetrieveImpl().respondingGatewayCrossGatewayRetrieve(body, context);
    }

}
