/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.subjectdiscovery;

import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterSubjectDiscovery", portName = "AdapterSubjectDiscoveryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adaptersubjectdiscovery.AdapterSubjectDiscoveryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptersubjectdiscovery", wsdlLocation = "META-INF/wsdl/AdapterSubjectDiscovery/AdapterSubjectDiscovery.wsdl")
@Stateless
public class AdapterSubjectDiscovery {

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201301UV(org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType pixConsumerPRPAIN201301UVRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201302UV(org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType pixConsumerPRPAIN201302UVRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201304UV(org.hl7.v3.PIXConsumerPRPAIN201304UVRequestType pixConsumerPRPAIN201304UVRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType pixConsumerPRPAIN201309UV(org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType pixConsumerPRPAIN201309UVRequest) {
        return new AdapterSubjectDiscoveryImpl().pixConsumerPRPAIN201309UV(pixConsumerPRPAIN201309UVRequest);
    }

}
