/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.proxy;

import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NhincProxyPatientDiscovery", portName = "NhincProxyPatientDiscoveryPort", endpointInterface = "gov.hhs.fha.nhinc.nhincproxypatientdiscovery.NhincProxyPatientDiscoveryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscovery", wsdlLocation = "META-INF/wsdl/NhincProxyPatientDiscovery/NhincProxyPatientDiscovery.wsdl")
@Stateless
public class NhincProxyPatientDiscovery {

    public org.hl7.v3.PRPAIN201306UV02 proxyPRPAIN201305UV(org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType proxyPRPAIN201305UVProxyRequest) {
        return new NhincProxyPatientDiscoveryImpl().proxyPRPAIN201305UV(proxyPRPAIN201305UVProxyRequest);
    }

}
