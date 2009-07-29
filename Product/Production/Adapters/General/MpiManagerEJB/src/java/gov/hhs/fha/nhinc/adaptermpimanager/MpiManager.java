/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adaptermpimanager;

import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author mflynn02
 */
@WebService(serviceName = "AdapterMpiManagerService", portName = "AdapterMpiManagerPortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.adaptermpimanager.AdapterMpiManagerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptermpimanager", wsdlLocation = "META-INF/wsdl/MpiManager/AdapterMpiManager.wsdl")
@Stateless
public class MpiManager implements AdapterMpiManagerPortType {

    public org.hl7.v3.MCCIIN000002UV01 addPatient(org.hl7.v3.PRPAIN201301UV addPatientRequest) {
        org.hl7.v3.MCCIIN000002UV01 result;
        // Add Patient to MPI
        result = PatientSaver.SavePatient(addPatientRequest);
        
        return result;
    }

    public org.hl7.v3.MCCIIN000002UV01 removePatient(org.hl7.v3.PRPAIN201303UV removePatientRequest) {
        org.hl7.v3.MCCIIN000002UV01 result;
        // Add Patient to MPI
        result = PatientSaver.RevokePatient(removePatientRequest);
        
        return result;
    }

}
