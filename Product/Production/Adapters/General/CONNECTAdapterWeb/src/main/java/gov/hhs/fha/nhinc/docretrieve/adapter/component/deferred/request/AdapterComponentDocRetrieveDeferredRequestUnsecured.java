/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.AdapterDocRetrieveDeferredRequestImpl;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Ralph Saunders
 */
@WebService(serviceName = "AdapterComponentDocRetrieveDeferredRequest",
            portName = "AdapterComponentDocRetrieveDeferredReqPortSoap",
            endpointInterface = "gov.hhs.fha.nhinc.adaptercomponentdocretrievedeferredreq.AdapterComponentDocRetrieveDeferredReqPortType",
            targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentdocretrievedeferredreq",
            wsdlLocation = "WEB-INF/wsdl/AdapterComponentDocRetrieveDeferredRequest/AdapterComponentDocRetrieveDeferredRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterComponentDocRetrieveDeferredRequestUnsecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
        return new AdapterDocRetrieveDeferredRequestImpl().crossGatewayRetrieveRequest(crossGatewayRetrieveRequest, context);
    }

}
