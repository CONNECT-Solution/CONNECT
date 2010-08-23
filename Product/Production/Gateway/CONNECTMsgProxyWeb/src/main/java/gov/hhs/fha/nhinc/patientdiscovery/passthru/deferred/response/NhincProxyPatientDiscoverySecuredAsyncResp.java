package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "NhincProxyPatientDiscoverySecuredAsyncResp", portName = "NhincProxyPatientDiscoverySecuredAsyncRespPortType", endpointInterface = "gov.hhs.fha.nhinc.nhincproxypatientdiscoverysecuredasyncresp.NhincProxyPatientDiscoverySecuredAsyncRespPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscoverysecuredasyncresp", wsdlLocation = "WEB-INF/wsdl/NhincProxyPatientDiscoverySecuredAsyncResp/NhincProxyPatientDiscoverySecuredAsyncResp.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyPatientDiscoverySecuredAsyncResp {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(org.hl7.v3.ProxyPRPAIN201306UVProxySecuredRequestType proxyProcessPatientDiscoveryAsyncRespRequest) {
        return new NhincProxyPatientDiscoveryAsyncRespImpl().proxyProcessPatientDiscoveryAsyncResp(proxyProcessPatientDiscoveryAsyncRespRequest, context);
    }

}
