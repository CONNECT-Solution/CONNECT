/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adaptermpi;

import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterMpiService", portName = "AdapterMpiPort", endpointInterface = "gov.hhs.fha.nhinc.adaptermpi.AdapterMpiPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptermpi", wsdlLocation = "META-INF/wsdl/AdapterMpi/AdapterMpi.wsdl")
@Stateless
public class AdapterMpi {

    public org.hl7.v3.PRPAIN201306UV02 findCandidates(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType findCandidatesRequest) {
        return new AdapterMpiProxyImpl().query(findCandidatesRequest);
    }

}
