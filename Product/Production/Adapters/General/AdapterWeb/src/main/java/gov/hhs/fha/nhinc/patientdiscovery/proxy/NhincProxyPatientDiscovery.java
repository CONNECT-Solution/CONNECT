package gov.hhs.fha.nhinc.patientdiscovery.proxy;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyPatientDiscovery", portName = "NhincProxyPatientDiscoveryPort", endpointInterface = "gov.hhs.fha.nhinc.nhincproxypatientdiscovery.NhincProxyPatientDiscoveryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscovery", wsdlLocation = "WEB-INF/wsdl/NhincProxyPatientDiscovery/NhincProxyPatientDiscovery.wsdl")
public class NhincProxyPatientDiscovery {

    public org.hl7.v3.PRPAIN201306UV02 proxyPRPAIN201305UV(org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType proxyPRPAIN201305UVProxyRequest) {
        return new NhincProxyPatientDiscoveryImpl().proxyPRPAIN201305UV(proxyPRPAIN201305UVProxyRequest);
    }

}
