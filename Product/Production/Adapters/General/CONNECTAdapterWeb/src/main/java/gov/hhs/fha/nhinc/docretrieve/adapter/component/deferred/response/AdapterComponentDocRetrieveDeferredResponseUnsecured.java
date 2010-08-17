/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.fha.nhinc.docretrieve.adapter.deferred.response.AdapterDocRetrieveDeferredResponseImpl;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterComponentDocRetrieveDeferredResponse",
            portName = "AdapterComponentDocRetrieveDeferredRespPortSoap",
            endpointInterface = "gov.hhs.fha.nhinc.adaptercomponentdocretrievedeferredresp.AdapterComponentDocRetrieveDeferredRespPortType",
            targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentdocretrievedeferredresp",
            wsdlLocation = "WEB-INF/wsdl/AdapterComponentDocRetrieveDeferredResponse/AdapterComponentDocumentRetrieveDeferredResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterComponentDocRetrieveDeferredResponseUnsecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse) {
        return new AdapterDocRetrieveDeferredResponseImpl().crossGatewayRetrieveResponse(crossGatewayRetrieveResponse, context);
    }

}
