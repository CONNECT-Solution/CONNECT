package gov.hhs.fha.nhinc.docretrievedeferred.adapter.request;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Ralph Saunders
 */
@WebService(serviceName = "AdapterDocRetrieveDeferredRequest", portName = "AdapterDocRetrieveDeferredRequestPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrievedeferredreq.AdapterDocRetrieveDeferredRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrievedeferredreq", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveDeferredReq/AdapterDocRetrieveDeferredReq.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocRetrieveDeferredReq {

    @Resource
    private WebServiceContext context;

    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType body) {
        return new AdapterDocRetrieveDeferredReqImpl().respondingGatewayCrossGatewayRetrieve(body, context);
    }

}
