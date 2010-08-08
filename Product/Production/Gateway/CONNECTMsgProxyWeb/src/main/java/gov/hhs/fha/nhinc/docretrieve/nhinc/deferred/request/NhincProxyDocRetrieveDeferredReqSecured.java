package gov.hhs.fha.nhinc.docretrieve.nhinc.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * This is an Nhinc Proxy secured service for Document Retrieve deferred Request message
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocRetrieveDeferredRequestSecured", portName = "NhincProxyDocRetrieveDeferredRequestSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocretrievedeferredsecuredrequest.NhincProxyDocRetrieveDeferredRequestSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocretrievedeferredsecuredrequest", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocRetrieveDeferredReqSecured/NhincProxyDocRetrieveDeferredReqSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyDocRetrieveDeferredReqSecured {

    @Resource
    private WebServiceContext context;

    /**
     *
     * @param body
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body) {
        DocRetrieveAcknowledgementType ack = sendToOrchImpl(body);
        return ack;
    }


    /**
     *
     * @param crossGatewayRetrieveRequest
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType sendToOrchImpl(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body) {
        NhincProxyDocRetrieveDeferredReqOrchImpl orchImpl = new NhincProxyDocRetrieveDeferredReqOrchImpl();
        DocRetrieveAcknowledgementType ack = orchImpl.crossGatewayRetrieveRequest(body, context);
        return ack;
    }
}
