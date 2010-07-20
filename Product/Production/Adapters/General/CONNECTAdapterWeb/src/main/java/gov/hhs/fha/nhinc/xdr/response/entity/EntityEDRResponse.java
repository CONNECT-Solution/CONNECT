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
 * @author JHOPPESC
 */
@WebService(serviceName = "EntityXDRAsyncResponse_Service", portName = "EntityXDRAsyncResponse_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincentityxdr.async.response.EntityXDRAsyncResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincentityxdr:async:response", wsdlLocation = "WEB-INF/wsdl/EntityEDRResponse/EntityXDRResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityEDRResponse {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType provideAndRegisterDocumentSetAsyncRespRequest) {
        return new EntityEDRResponseImpl().provideAndRegisterDocumentSetBResponse(provideAndRegisterDocumentSetAsyncRespRequest, context);
    }

}
