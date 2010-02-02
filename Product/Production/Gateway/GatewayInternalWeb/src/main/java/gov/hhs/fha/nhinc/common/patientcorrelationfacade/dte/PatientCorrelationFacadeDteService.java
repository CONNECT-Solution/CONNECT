package gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte;

import javax.jws.WebService;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NhincComponentPatientCorrelationFacadeDteService", portName = "PatientCorrelationFacadeDteBindingPort", endpointInterface = "gov.hhs.fha.nhinc.componentpatientcorrelationfacadedte.PatientCorrelationFacadeDte", targetNamespace = "urn:gov:hhs:fha:nhinc:componentpatientcorrelationfacadedte", wsdlLocation = "WEB-INF/wsdl/PatientCorrelationFacadeDteService/NhincComponentPatientCorrelationFacadeDte.wsdl")
public class PatientCorrelationFacadeDteService
{

    public org.hl7.v3.CreatePixRetrieveResponseType createPixRetrieve(org.hl7.v3.CreatePixRetrieveRequestType createPixRetrieveRequest)
    {
        return PixRetrieveBuilder.createPixRetrieve(createPixRetrieveRequest);
    }

    public org.hl7.v3.CreateFacadeRetrieveResultResponseType createFacadeRetrieveResult(org.hl7.v3.CreateFacadeRetrieveResultRequestType createFacadeRetrieveResultRequest)
    {
        return FacadeRetrieveResponseBuilder.createFacadeRetrieveResult(createFacadeRetrieveResultRequest);
    }

    public org.hl7.v3.CreatePixAddResponseType createPixAdd(org.hl7.v3.CreatePixAddRequestType createPixAddRequest)
    {
        return PixAddBuilder.createPixAdd(createPixAddRequest);
    }

    public org.hl7.v3.CreateAckResponseType createAck(org.hl7.v3.CreateAckRequestType createAckRequest)
    {
        return FacadeAckBuilder.createAck(createAckRequest);
    }

}
