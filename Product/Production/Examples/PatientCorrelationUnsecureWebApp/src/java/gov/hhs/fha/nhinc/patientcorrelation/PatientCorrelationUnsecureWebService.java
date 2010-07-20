/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelation;

import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "PatientCorrelationService", portName = "PatientCorrelationPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation", wsdlLocation = "WEB-INF/wsdl/PatientCorrelationUnsecureWebService/NhincComponentPatientCorrelation.wsdl")
public class PatientCorrelationUnsecureWebService {
    private static Log log = LogFactory.getLog(PatientCorrelationUnsecureWebService.class);

    public org.hl7.v3.RetrievePatientCorrelationsResponseType retrievePatientCorrelations(org.hl7.v3.RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest) {
        log.info("ENTERED EXAMPLE UNSECURE OVERRIDE - RETRIEVE METHOD");
        return new  org.hl7.v3.RetrievePatientCorrelationsResponseType();
    }

    public org.hl7.v3.AddPatientCorrelationResponseType addPatientCorrelation(org.hl7.v3.AddPatientCorrelationRequestType addPatientCorrelationRequest) {
        log.info("ENTERED EXAMPLE UNSECURE OVERRIDE - ADD METHOD");
        return new org.hl7.v3.AddPatientCorrelationResponseType();
    }

}
