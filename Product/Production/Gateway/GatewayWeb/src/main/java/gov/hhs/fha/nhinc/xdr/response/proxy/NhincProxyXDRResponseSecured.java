package gov.hhs.fha.nhinc.xdr.response.proxy;

import javax.jws.WebService;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import ihe.iti.xdr._2007.AcknowledgementType;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "ProxyXDRSecuredResponse_Service", portName = "ProxyXDRSecuredResponse_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.response.ProxyXDRSecuredResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyxdrsecured:async:response", wsdlLocation = "WEB-INF/wsdl/NhincProxyXDRResponseSecured/NhincProxyXDRSecuredResponse.wsdl")
public class NhincProxyXDRResponseSecured
{
    @Resource
    private WebServiceContext context;

    public AcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterResponseRequest)
    {
        return new NhincProxyXDRResponseSecuredImpl().provideAndRegisterDocumentSetBResponse(provideAndRegisterResponseRequest, context);
    }
}
