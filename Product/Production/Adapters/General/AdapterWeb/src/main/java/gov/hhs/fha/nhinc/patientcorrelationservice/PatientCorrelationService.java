package gov.hhs.fha.nhinc.patientcorrelationservice;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "PatientCorrelationService", portName = "PatientCorrelationPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation", wsdlLocation = "WEB-INF/wsdl/PatientCorrelationService/NhincComponentPatientCorrelation.wsdl")
public class PatientCorrelationService {

    public org.hl7.v3.RetrievePatientCorrelationsResponseType retrievePatientCorrelations(org.hl7.v3.RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.AddPatientCorrelationResponseType addPatientCorrelation(org.hl7.v3.AddPatientCorrelationRequestType addPatientCorrelationRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
