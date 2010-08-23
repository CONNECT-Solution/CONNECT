package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NhincProxyPatientDiscoveryAsyncResp", portName = "NhincProxyPatientDiscoveryAsyncRespPortType", endpointInterface = "gov.hhs.fha.nhinc.nhincproxypatientdiscoveryasyncresp.NhincProxyPatientDiscoveryAsyncRespPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscoveryasyncresp", wsdlLocation = "WEB-INF/wsdl/NhincProxyPatientDiscoveryAsyncResp/NhincProxyPatientDiscoveryAsyncResp.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyPatientDiscoveryAsyncResp
{
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(org.hl7.v3.ProxyPRPAIN201306UVProxyRequestType proxyProcessPatientDiscoveryAsyncRespRequest)
    {
        return new NhincProxyPatientDiscoveryAsyncRespImpl().proxyProcessPatientDiscoveryAsyncResp(proxyProcessPatientDiscoveryAsyncRespRequest, context);
    }

}
