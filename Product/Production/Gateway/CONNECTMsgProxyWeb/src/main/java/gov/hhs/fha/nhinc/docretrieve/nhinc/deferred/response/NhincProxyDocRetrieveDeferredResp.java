package gov.hhs.fha.nhinc.docretrieve.nhinc.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * This is Webservice class for Nhinc proxy doc retrieve deferred response unsecured service
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocRetrieveDeferredResponse", portName = "NhincProxyDocRetrieveDeferredResponsePortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocretrieveresponse.NhincProxyDocRetrieveDeferredResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocretrieveresponse", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocRetrieveDeferredResp/NhincProxyDocRetrieveDeferredResp.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyDocRetrieveDeferredResp {

    @Resource
    private WebServiceContext context;

    /**
     *
     * @param crossGatewayRetrieveResponse
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse) {
        DocRetrieveAcknowledgementType ack = sendToOrchImpl(crossGatewayRetrieveResponse);
        return ack;
    }

    /**
     *
     * @param crossGatewayRetrieveResponse
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType sendToOrchImpl(RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse) {
        DocRetrieveAcknowledgementType ack = new NhincProxyDocRetrieveDeferredRespOrchImpl().crossGatewayRetrieveResponse(crossGatewayRetrieveResponse, context);
        return ack;
    }

}
