/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adaptercomponentmpi;

import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author Jon Hoppesch
 */
@WebService(serviceName = "AdapterComponentMpiService", portName = "AdapterComponentMpiPort", endpointInterface = "gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentmpi", wsdlLocation = "META-INF/wsdl/AdapterComponentMpi/AdapterComponentMpi.wsdl")
@Stateless
public class AdapterComponentMpi implements AdapterComponentMpiPortType {

    public org.hl7.v3.PRPAIN201306UV findCandidates(org.hl7.v3.PRPAIN201305UV findCandidatesRequest) {
        return PatientChecker.FindPatient(findCandidatesRequest);
    }

}
