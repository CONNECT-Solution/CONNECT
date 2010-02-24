package gov.hhs.fha.nhinc.xdr.request.proxy;

import javax.jws.WebService;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "ProxyXDRRequest_Service", portName = "ProxyXDRRequest_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyxdr.async.request.ProxyXDRRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyxdr:async:request", wsdlLocation = "WEB-INF/wsdl/NhincProxyXDRRequest/NhincProxyXDRRequest.wsdl")
public class NhincProxyXDRRequest
{

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType provideAndRegisterRequestRequest)
    {
        return new NhincProxyXDRRequestImpl().provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestRequest);
    }

}
