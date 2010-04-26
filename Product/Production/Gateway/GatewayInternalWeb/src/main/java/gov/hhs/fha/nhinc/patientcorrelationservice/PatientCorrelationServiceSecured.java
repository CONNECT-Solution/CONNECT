package gov.hhs.fha.nhinc.patientcorrelationservice;

import javax.jws.WebService;
import gov.hhs.fha.nhinc.patientcorrelationservice.impl.PatientCorrelationServiceImpl;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "PatientCorrelationServiceSecured", portName = "PatientCorrelationSecuredPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation", wsdlLocation = "WEB-INF/wsdl/PatientCorrelationServiceSecured/NhincComponentPatientCorrelationSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class PatientCorrelationServiceSecured
{

    public org.hl7.v3.RetrievePatientCorrelationsSecuredResponseType retrievePatientCorrelations(org.hl7.v3.RetrievePatientCorrelationsSecuredRequestType retrievePatientCorrelationsRequest)
    {
        return PatientCorrelationServiceImpl.retrievePatientCorrelations(retrievePatientCorrelationsRequest);
    }

    public org.hl7.v3.AddPatientCorrelationSecuredResponseType addPatientCorrelation(org.hl7.v3.AddPatientCorrelationSecuredRequestType addPatientCorrelationRequest)
    {
        return PatientCorrelationServiceImpl.addPatientCorrelation(addPatientCorrelationRequest);
    }

}
