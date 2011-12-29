/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterXDRResponseSecured_Service", portName = "AdapterXDRResponseSecured_Port_Soap", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrresponsesecured.AdapterXDRResponseSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrresponsesecured", wsdlLocation = "WEB-INF/wsdl/AdapterXDRResponseSecured/AdapterXDRResponseSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class AdapterXDRResponseSecured
{
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType body)
    {
        return new AdapterXDRResponseImpl().provideAndRegisterDocumentSetBResponse(body, context);
    }
}
