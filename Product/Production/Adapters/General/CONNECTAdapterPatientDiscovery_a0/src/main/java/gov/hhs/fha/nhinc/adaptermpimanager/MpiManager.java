/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.adaptermpimanager;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterMpiManagerService", portName = "AdapterMpiManagerPortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.adaptermpimanager.AdapterMpiManagerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptermpimanager", wsdlLocation = "WEB-INF/wsdl/MpiManager/AdapterMpiManager.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class MpiManager {

    public org.hl7.v3.MCCIIN000002UV01 addPatient(org.hl7.v3.PRPAIN201301UV02 addPatientRequest) {
        org.hl7.v3.MCCIIN000002UV01 result;
        // Add Patient to MPI
        result = PatientSaver.SavePatient(addPatientRequest);

        return result;
    }

}
