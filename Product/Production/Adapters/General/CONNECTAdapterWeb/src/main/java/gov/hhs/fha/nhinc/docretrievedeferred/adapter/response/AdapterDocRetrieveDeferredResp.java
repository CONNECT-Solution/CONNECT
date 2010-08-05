package gov.hhs.fha.nhinc.docretrievedeferred.adapter.response;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrievedeferred.adapter.request.AdapterDocRetrieveDeferredReqImpl;
import gov.hhs.fha.nhinc.docretrievedeferred.adapter.request.AdapterDocRetrieveDeferredReqSecuredImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Ralph Saunders
 */
@WebService(serviceName = "AdapterDocRetrieveDeferredResponse", portName = "AdapterDocRetrieveDeferredResponsePortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrievedeferredresp.AdapterDocRetrieveDeferredResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrievedeferredresp", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveDefferedResp/AdapterDocRetrieveDeferredResp.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocRetrieveDeferredResp {

    @Resource
    private WebServiceContext context;

    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveResponseType body) {
        return new AdapterDocRetrieveDeferredRespImpl().respondingGatewayCrossGatewayRetrieve(body, context);
    }

}
