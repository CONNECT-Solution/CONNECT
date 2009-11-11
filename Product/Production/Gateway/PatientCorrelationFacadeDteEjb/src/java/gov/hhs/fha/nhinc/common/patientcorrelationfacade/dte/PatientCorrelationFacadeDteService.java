/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte;

import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NhincComponentPatientCorrelationFacadeDteService", portName = "PatientCorrelationFacadeDteBindingPort", endpointInterface = "gov.hhs.fha.nhinc.componentpatientcorrelationfacadedte.PatientCorrelationFacadeDte", targetNamespace = "urn:gov:hhs:fha:nhinc:componentpatientcorrelationfacadedte", wsdlLocation = "META-INF/wsdl/PatientCorrelationFacadeDteService/NhincComponentPatientCorrelationFacadeDte.wsdl")
@Stateless
public class PatientCorrelationFacadeDteService {

    public org.hl7.v3.CreatePixRetrieveResponseType createPixRetrieve(org.hl7.v3.CreatePixRetrieveRequestType createPixRetrieveRequest) {
        return PixRetrieveBuilder.createPixRetrieve(createPixRetrieveRequest);
    }

    public org.hl7.v3.CreateFacadeRetrieveResultResponseType createFacadeRetrieveResult(org.hl7.v3.CreateFacadeRetrieveResultRequestType createFacadeRetrieveResultRequest) {
        return FacadeRetrieveResponseBuilder.createFacadeRetrieveResult(createFacadeRetrieveResultRequest);
    }

    public org.hl7.v3.CreatePixAddResponseType createPixAdd(org.hl7.v3.CreatePixAddRequestType createPixAddRequest) {
        return PixAddBuilder.createPixAdd(createPixAddRequest);
    }

    public org.hl7.v3.CreateAckResponseType createAck(org.hl7.v3.CreateAckRequestType createAckRequest) {
        return FacadeAckBuilder.createAck(createAckRequest);
    }

}
