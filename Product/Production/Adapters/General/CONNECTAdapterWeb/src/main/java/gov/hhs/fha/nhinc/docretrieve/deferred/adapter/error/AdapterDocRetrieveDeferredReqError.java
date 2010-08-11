package gov.hhs.fha.nhinc.docretrieve.deferred.adapter.error;

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
@WebService(serviceName = "AdapterDocRetrieveDeferredRequestErrorService", portName = "AdapterDocRetrieveRequestErrorPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrievedeferredrequesterror.AdapterDocRetrieveDeferredRequestErrorPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrievedeferredrequesterror", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveDeferredReqError/AdapterDocRetrieveDeferredReqError.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocRetrieveDeferredReqError {

    @Resource
    private WebServiceContext context;

    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequestError(RespondingGatewayCrossGatewayRetrieveRequestType body) {
        return new AdapterDocRetrieveDeferredReqErrorImpl().respondingGatewayCrossGatewayRetrieve(body, context);
    }

}
