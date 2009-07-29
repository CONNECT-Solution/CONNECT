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
public class AdapterMpi implements AdapterMpiPortType {

    public org.hl7.v3.PRPAIN201306UV findCandidates(org.hl7.v3.PIXConsumerPRPAIN201305UVRequestType findCandidatesRequest) {
        return AdapterMpiQuery.query(findCandidatesRequest);
    }
}
