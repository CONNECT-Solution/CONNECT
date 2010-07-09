/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.request.entity;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "EntityXDRSecuredAsyncRequest_Service", portName = "EntityXDRSecuredAsyncRequest_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincentityxdrsecured.async.request.EntityXDRSecuredAsyncRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincentityxdrsecured:async:request", wsdlLocation = "WEB-INF/wsdl/EntityXDRRequestSecured/EntityXDRSecuredRequest.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class EntityXDRRequestSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterAsyncReqRequest) {
        return new EntityXDRRequestSecuredImpl().provideAndRegisterDocumentSetBRequest(provideAndRegisterAsyncReqRequest, context);
    }

}
