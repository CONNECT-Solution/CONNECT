/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelationservice;

import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationSecuredPortType;
import gov.hhs.fha.nhinc.patientcorrelationservice.impl.PatientCorrelationServiceImpl;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "PatientCorrelationServiceSecured", portName = "PatientCorrelationSecuredPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation", wsdlLocation = "META-INF/wsdl/PatientCorrelationServiceSecured/NhincComponentPatientCorrelationSecured.wsdl")
@Stateless
public class PatientCorrelationServiceSecured implements PatientCorrelationSecuredPortType {

    public org.hl7.v3.RetrievePatientCorrelationsSecuredResponseType retrievePatientCorrelations(org.hl7.v3.RetrievePatientCorrelationsSecuredRequestType retrievePatientCorrelationsRequest) {
        return PatientCorrelationServiceImpl.retrievePatientCorrelations(retrievePatientCorrelationsRequest);
    }

    public org.hl7.v3.AddPatientCorrelationSecuredResponseType addPatientCorrelation(org.hl7.v3.AddPatientCorrelationSecuredRequestType addPatientCorrelationRequest) {
        return PatientCorrelationServiceImpl.addPatientCorrelation(addPatientCorrelationRequest);
    }

    public org.hl7.v3.RemovePatientCorrelationSecuredResponseType removePatientCorrelation(org.hl7.v3.RemovePatientCorrelationSecuredRequestType removePatientCorrelationRequest) {
        return PatientCorrelationServiceImpl.removePatientCorrelation(removePatientCorrelationRequest);
    }

}
