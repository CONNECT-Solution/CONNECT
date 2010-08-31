package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "PatientCorrelationServiceSecured", portName = "PatientCorrelationSecuredPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation", wsdlLocation = "WEB-INF/wsdl/PatientCorrelationServiceSecured/NhincComponentPatientCorrelationSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class PatientCorrelationServiceSecured {

    @Resource
    private WebServiceContext context;

    public org.hl7.v3.RetrievePatientCorrelationsSecuredResponseType retrievePatientCorrelations(org.hl7.v3.RetrievePatientCorrelationsSecuredRequestType retrievePatientCorrelationsRequest) {
        return new PatientCorrelationServiceSecuredImpl().retrievePatientCorrelations(retrievePatientCorrelationsRequest, context);
    }

    public org.hl7.v3.AddPatientCorrelationSecuredResponseType addPatientCorrelation(org.hl7.v3.AddPatientCorrelationSecuredRequestType addPatientCorrelationRequest) {
        return new PatientCorrelationServiceSecuredImpl().addPatientCorrelation(addPatientCorrelationRequest, context);
    }
}
