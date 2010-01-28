package gov.hhs.fha.nhinc.adaptermpimanager;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterMpiManagerService", portName = "AdapterMpiManagerPortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.adaptermpimanager.AdapterMpiManagerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptermpimanager", wsdlLocation = "WEB-INF/wsdl/MpiManager/AdapterMpiManager.wsdl")
public class MpiManager {

    public org.hl7.v3.MCCIIN000002UV01 addPatient(org.hl7.v3.PRPAIN201301UV02 addPatientRequest) {
        org.hl7.v3.MCCIIN000002UV01 result;
        // Add Patient to MPI
        result = PatientSaver.SavePatient(addPatientRequest);

        return result;
    }

}
