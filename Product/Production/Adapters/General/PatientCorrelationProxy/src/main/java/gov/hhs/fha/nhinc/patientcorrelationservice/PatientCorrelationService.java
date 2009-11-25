/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelationservice;

import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "PatientCorrelationService", portName = "PatientCorrelationPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation", wsdlLocation = "META-INF/wsdl/PatientCorrelationService/NhincComponentPatientCorrelation.wsdl")
@Stateless
public class PatientCorrelationService {

    public org.hl7.v3.RetrievePatientCorrelationsResponseType retrievePatientCorrelations(org.hl7.v3.RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest) {
        PatientCorrelationImpl impl = new PatientCorrelationImpl();

        return impl.retrievePatientCorrelations(retrievePatientCorrelationsRequest);
    }

    public org.hl7.v3.AddPatientCorrelationResponseType addPatientCorrelation(org.hl7.v3.AddPatientCorrelationRequestType addPatientCorrelationRequest) {
        PatientCorrelationImpl impl = new PatientCorrelationImpl();
        return impl.addPatientCorrelation(addPatientCorrelationRequest);
    }

}
