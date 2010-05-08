/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.response.proxy;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "ProxyXDRSecuredAsyncResponse_Service", portName = "ProxyXDRSecuredAsyncResponse_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.response.ProxyXDRSecuredAsyncResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyxdrsecured:async:response", wsdlLocation = "WEB-INF/wsdl/NhincProxyXDRResponseSecured/NhincProxyXDRSecuredResponse.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class NhincProxyXDRResponseSecured {
    @Resource
    private WebServiceContext context;

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterAsyncRespRequest) {
        return new NhincProxyXDRResponseSecuredImpl().provideAndRegisterDocumentSetBResponse(provideAndRegisterAsyncRespRequest, context);
    }

}
