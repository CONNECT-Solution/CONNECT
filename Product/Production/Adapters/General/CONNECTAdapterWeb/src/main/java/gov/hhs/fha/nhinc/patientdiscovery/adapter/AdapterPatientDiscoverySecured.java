/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.adapter;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPatientDiscoverySecured", portName = "AdapterPatientDiscoverySecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscoverysecured.AdapterPatientDiscoverySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoverysecured", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoverySecured/AdapterPatientDiscoverySecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterPatientDiscoverySecured {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request) {
        return new AdapterPatientDiscoveryImpl().respondingGatewayPRPAIN201305UV02(true, respondingGatewayPRPAIN201305UV02Request, context);
    }
}
