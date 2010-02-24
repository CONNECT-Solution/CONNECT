package gov.hhs.fha.nhinc.xdr.request.proxy;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "ProxyXDRSecuredRequest_Service", portName = "ProxyXDRSecuredRequest_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.request.ProxyXDRSecuredRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyxdrsecured:async:request", wsdlLocation = "WEB-INF/wsdl/NhincProxyXDRRequestSecured/NhincProxyXDRSecuredRequest.wsdl")
public class NhincProxyXDRRequestSecured
{
    @Resource
    private WebServiceContext context;

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest)
    {
        return new NhincProxyXDRRequestSecuredImpl().provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestRequest, context);
    }

}
