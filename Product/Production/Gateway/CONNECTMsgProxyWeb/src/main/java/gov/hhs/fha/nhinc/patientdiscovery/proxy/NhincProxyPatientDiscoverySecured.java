package gov.hhs.fha.nhinc.patientdiscovery.proxy;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NhincProxyPatientDiscoverySecured", portName = "NhincProxyPatientDiscoverySecuredPort", endpointInterface = "gov.hhs.fha.nhinc.nhincproxypatientdiscoverysecured.NhincProxyPatientDiscoverySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscoverysecured", wsdlLocation = "WEB-INF/wsdl/NhincProxyPatientDiscoverySecured/NhincProxyPatientDiscoverySecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyPatientDiscoverySecured {

    @Resource
    private WebServiceContext context;

    protected NhincProxyPatientDiscoverySecuredImpl getNhincProxyPatientDiscoverySecuredImpl() {
        return new NhincProxyPatientDiscoverySecuredImpl();
    }

    protected WebServiceContext getWebServiceContext() {
        return context;
    }

    public org.hl7.v3.PRPAIN201306UV02 proxyPRPAIN201305UV(org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType proxyPRPAIN201305UVProxyRequest) {

        return getNhincProxyPatientDiscoverySecuredImpl().proxyPRPAIN201305UV(proxyPRPAIN201305UVProxyRequest, getWebServiceContext());
    }
}
