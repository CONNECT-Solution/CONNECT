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
 * @author JHOPPESC
 */
@WebService(serviceName = "ProxyXDRAsyncRequest_Service", portName = "ProxyXDRAsyncRequest_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyxdr.async.request.ProxyXDRAsyncRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyxdr:async:request", wsdlLocation = "WEB-INF/wsdl/NhincProxyXDRRequest/NhincProxyXDRRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyXDRRequest {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType provideAndRegisterAsyncReqRequest) {
        return new NhincProxyXDRRequestImpl().provideAndRegisterDocumentSetBRequest(provideAndRegisterAsyncReqRequest, context);
    }

}
