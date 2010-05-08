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
 * @author jhoppesc
 */
@WebService(serviceName = "EntityXDRAsyncRequest_Service", portName = "EntityXDRAsyncRequest_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincentityxdr.async.request.EntityXDRAsyncRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincentityxdr:async:request", wsdlLocation = "WEB-INF/wsdl/EntityXDRRequest/EntityXDRRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityXDRRequest {
    @Resource
    private WebServiceContext context;

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType provideAndRegisterAsyncReqRequest) {
        return new EntityXDRRequestImpl().provideAndRegisterDocumentSetBRequest(provideAndRegisterAsyncReqRequest, context);
    }

}
