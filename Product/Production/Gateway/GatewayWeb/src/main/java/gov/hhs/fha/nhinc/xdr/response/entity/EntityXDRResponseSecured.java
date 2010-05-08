/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.response.entity;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "EntityXDRSecuredAsyncResponse_Service", portName = "EntityXDRSecuredAsyncResponse_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincentityxdrsecured.async.response.EntityXDRSecuredAsyncResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincentityxdrsecured:async:response", wsdlLocation = "WEB-INF/wsdl/EntityXDRResponseSecured/EntityXDRSecuredResponse.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class EntityXDRResponseSecured {
    @Resource
    private WebServiceContext context;

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterDocumentSetSecuredAsyncRespRequest) {
        return new EntityXDRResponseSecuredImpl().provideAndRegisterDocumentSetBResponse(provideAndRegisterDocumentSetSecuredAsyncRespRequest, context);
    }

}
