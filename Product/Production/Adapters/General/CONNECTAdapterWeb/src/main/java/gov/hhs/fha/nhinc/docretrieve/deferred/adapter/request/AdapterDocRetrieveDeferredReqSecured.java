package gov.hhs.fha.nhinc.docretrieve.deferred.adapter.request;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Ralph Saunders
 */
@WebService(serviceName = "AdapterDocRetrieveDeferredRequestSecured", portName = "AdapterDocRetrieveDeferredRequestSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrievedeferredreqsecured.AdapterDocRetrieveDeferredRequestSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrievedeferredreqsecured", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveDeferredReq/AdapterDocRetrieveDeferredReqSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocRetrieveDeferredReqSecured {

    @Resource
    private WebServiceContext context;

    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body) {
        return new AdapterDocRetrieveDeferredReqSecuredImpl().respondingGatewayCrossGatewayRetrieve(body, context);
    }

}
