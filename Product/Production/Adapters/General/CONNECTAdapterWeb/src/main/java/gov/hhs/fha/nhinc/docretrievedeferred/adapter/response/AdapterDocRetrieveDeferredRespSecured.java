package gov.hhs.fha.nhinc.docretrievedeferred.adapter.response;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Ralph Saunders
 */
@WebService(serviceName = "AdapterDocRetrieveDeferredResponseSecured", portName = "AdapterDocRetrieveDeferredResponseSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrievedeferredrespsecured.AdapterDocRetrieveDeferredResponseSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrievedeferredrespsecured", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveDefferedResp/AdapterDocRetrieveDeferredRespSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocRetrieveDeferredRespSecured {

    @Resource
    private WebServiceContext context;

    public DocRetrieveAcknowledgementType respondingGatewayCrossGatewayRetrieve(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body) {
        return new AdapterDocRetrieveDeferredRespSecuredImpl().respondingGatewayCrossGatewayRetrieve(body, context);
    }

}
