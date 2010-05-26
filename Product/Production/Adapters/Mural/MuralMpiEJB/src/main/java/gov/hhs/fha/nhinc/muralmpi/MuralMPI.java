/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.muralmpi;

import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterComponentMpiService", portName = "AdapterComponentMpiPort", endpointInterface = "gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentmpi", wsdlLocation = "META-INF/wsdl/AdapterMpi/AdapterComponentMpi.wsdl")
@Stateless
public class MuralMPI {

    public org.hl7.v3.PRPAIN201306UV02 findCandidates(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest) {
        return MuralMPIQuery.query(findCandidatesRequest);
    }

}
