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
package gov.hhs.fha.nhinc.patientdiscovery.adapter;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * This is the code that is for the unsecured PatientDiscovery service that is used
 * when running in pass through mode.
 *
 * @author Les Westberg
 */
@WebService(serviceName = "AdapterPatientDiscovery", portName = "AdapterPatientDiscoveryPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscovery.AdapterPatientDiscoveryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscovery", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscovery/AdapterPatientDiscovery.wsdl")
public class AdapterPatientDiscovery
{
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request)
    {
        return new AdapterPatientDiscoveryImpl().respondingGatewayPRPAIN201305UV02(false, respondingGatewayPRPAIN201305UV02Request, context);
    }
}
