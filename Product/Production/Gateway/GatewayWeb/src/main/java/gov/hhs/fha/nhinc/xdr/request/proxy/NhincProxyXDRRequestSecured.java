/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.request.proxy;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "ProxyXDRSecuredAsyncRequest_Service", portName = "ProxyXDRSecuredAsyncRequest_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.request.ProxyXDRSecuredAsyncRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyxdrsecured:async:request", wsdlLocation = "WEB-INF/wsdl/NhincProxyXDRRequestSecured/NhincProxyXDRSecuredRequest.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class NhincProxyXDRRequestSecured {
    @Resource
    private WebServiceContext context;

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterAsyncReqRequest) {
        return new NhincProxyXDRRequestSecuredImpl().provideAndRegisterDocumentSetBRequest(provideAndRegisterAsyncReqRequest, context);
    }

}
