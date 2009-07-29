/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelationservice;

import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationPortType;
import gov.hhs.fha.nhinc.patientcorrelationservice.impl.PatientCorrelationServiceImpl;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author rayj
 */
@WebService(serviceName = "PatientCorrelationService", portName = "PatientCorrelationPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation", wsdlLocation = "META-INF/wsdl/PatientCorrelationService/NhincComponentPatientCorrelation.wsdl")
@Stateless
public class PatientCorrelationService implements PatientCorrelationPortType {

    public org.hl7.v3.RetrievePatientCorrelationsResponseType retrievePatientCorrelations(org.hl7.v3.RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest) {
        return PatientCorrelationServiceImpl.retrievePatientCorrelations(retrievePatientCorrelationsRequest);
    }

    public org.hl7.v3.AddPatientCorrelationResponseType addPatientCorrelation(org.hl7.v3.AddPatientCorrelationRequestType addPatientCorrelationRequest) {
        return PatientCorrelationServiceImpl.addPatientCorrelation(addPatientCorrelationRequest);
    }

    public org.hl7.v3.RemovePatientCorrelationResponseType removePatientCorrelation(org.hl7.v3.RemovePatientCorrelationRequestType removePatientCorrelationRequest) {
        return PatientCorrelationServiceImpl.removePatientCorrelation(removePatientCorrelationRequest);
    }

}
