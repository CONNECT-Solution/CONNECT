/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adaptercomponentmpi;

import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "AdapterComponentMpiSecuredService", portName = "AdapterComponentMpiSecuredPort", endpointInterface = "gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentmpi", wsdlLocation = "META-INF/wsdl/AdapterComponentMpiSecured/AdapterComponentSecuredMpi.wsdl")
@Stateless
public class AdapterComponentMpiSecured implements AdapterComponentMpiSecuredPortType {

    public org.hl7.v3.PRPAIN201306UV findCandidates(org.hl7.v3.PRPAIN201305UV findCandidatesRequest) {
        //TODO implement this method
        return PatientChecker.FindPatient(findCandidatesRequest);
    }

}
