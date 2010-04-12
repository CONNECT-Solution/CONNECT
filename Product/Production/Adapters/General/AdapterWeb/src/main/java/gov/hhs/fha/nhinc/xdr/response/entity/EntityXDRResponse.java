/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.response.entity;

import javax.jws.WebService;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "EntityXDRResponse_Service", portName = "EntityXDRResponse_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincentityxdr.async.response.EntityXDRResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincentityxdr:async:response", wsdlLocation = "WEB-INF/wsdl/EntityXDRResponse/EntityXDRResponse.wsdl")
public class EntityXDRResponse {

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType provideAndRegisterDocumentSetResponseRequest) {
     
        //TODO implement this method
        return new EntityXDRResponseImpl().provideAndRegisterDocumentSetBResponse(provideAndRegisterDocumentSetResponseRequest);
    }

}
