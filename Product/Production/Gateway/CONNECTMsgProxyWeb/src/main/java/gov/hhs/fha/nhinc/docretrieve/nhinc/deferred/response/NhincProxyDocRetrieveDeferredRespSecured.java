package gov.hhs.fha.nhinc.docretrieve.nhinc.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response.NhincProxyDocRetrieveDeferredRespOrchImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * This is Webservice class for Nhinc proxy doc retrieve deferred response secured service
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocRetrieveDeferredResponseSecured", portName = "NhincProxyDocRetrieveDeferredResponseSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocretrievesecuredresponse.NhincProxyDocRetrieveDeferredResponseSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocretrievesecuredresponse", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocRetrieveDeferredRespSecured/NhincProxyDocRetrieveDeferredRespSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyDocRetrieveDeferredRespSecured {

    @Resource
    private WebServiceContext context;

    /**
     *
     * @param body
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body) {
        DocRetrieveAcknowledgementType ack = sendToOrchImpl(body);
        return ack;
    }

    /**
     *
     * @param body
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType sendToOrchImpl(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body) {
        DocRetrieveAcknowledgementType ack = new NhincProxyDocRetrieveDeferredRespOrchImpl().crossGatewayRetrieveResponse(body, context);
        return ack;
    }
}
