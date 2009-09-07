/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.subdisc.proxy;

import gov.hhs.fha.nhinc.nhincproxysubjectdiscovery.NhincProxySubjectDiscoveryPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NhincProxySubjectDiscovery", portName = "NhincProxySubjectDiscoveryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxysubjectdiscovery.NhincProxySubjectDiscoveryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxysubjectdiscovery", wsdlLocation = "META-INF/wsdl/NhincProxySubjectDiscovery/NhincProxySubjectDiscovery.wsdl")
@Stateless
public class NhincProxySubjectDiscovery implements NhincProxySubjectDiscoveryPortType {

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201301UV(org.hl7.v3.PIXConsumerPRPAIN201301UVProxyRequestType pixConsumerPRPAIN201301UVProxyRequest) {
        return new NhincProxySubjectDiscoveryImpl().pixConsumerPRPAIN201301UV(pixConsumerPRPAIN201301UVProxyRequest);
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201302UV(org.hl7.v3.PIXConsumerPRPAIN201302UVProxyRequestType pixConsumerPRPAIN201302UVProxyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201303UV(org.hl7.v3.PIXConsumerPRPAIN201303UVProxyRequestType pixConsumerPRPAIN201303UVProxyRequest) {
        return new NhincProxySubjectDiscoveryImpl().pixConsumerPRPAIN201303UV(pixConsumerPRPAIN201303UVProxyRequest);
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201304UV(org.hl7.v3.PIXConsumerPRPAIN201304UVProxyRequestType pixConsumerPRPAIN201304UVProxyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.PRPAIN201310UV pixConsumerPRPAIN201309UV(org.hl7.v3.PIXConsumerPRPAIN201309UVProxyRequestType pixConsumerPRPAIN201309UVProxyRequest) {
        return new NhincProxySubjectDiscoveryImpl().pixConsumerPRPAIN201309UV(pixConsumerPRPAIN201309UVProxyRequest);
    }

}
