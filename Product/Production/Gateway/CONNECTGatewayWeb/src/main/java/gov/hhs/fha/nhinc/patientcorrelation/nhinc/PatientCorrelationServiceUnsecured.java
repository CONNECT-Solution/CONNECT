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

package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "PatientCorrelationService", portName = "PatientCorrelationPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation", wsdlLocation = "WEB-INF/wsdl/PatientCorrelationServiceUnsecured/NhincComponentPatientCorrelation.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class PatientCorrelationServiceUnsecured {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.RetrievePatientCorrelationsResponseType retrievePatientCorrelations(org.hl7.v3.RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest) {
        return new PatientCorrelationServiceUnsecuredImpl().retrievePatientCorrelations(retrievePatientCorrelationsRequest, context);
    }

    public org.hl7.v3.AddPatientCorrelationResponseType addPatientCorrelation(org.hl7.v3.AddPatientCorrelationRequestType addPatientCorrelationRequest) {
        return new PatientCorrelationServiceUnsecuredImpl().addPatientCorrelation(addPatientCorrelationRequest, context);
    }

}
