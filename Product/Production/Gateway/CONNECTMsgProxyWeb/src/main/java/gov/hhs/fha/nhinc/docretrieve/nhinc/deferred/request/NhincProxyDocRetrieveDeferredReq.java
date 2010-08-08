package gov.hhs.fha.nhinc.docretrieve.nhinc.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * This is an Nhinc Proxy Unsecure service for Document Retrieve deferred Request message
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocRetrieveDeferredRequest", portName = "NhincProxyDocRetrieveDeferredRequestPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocretrievedeferredrequest.NhincProxyDocRetrieveDeferredRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocretrievedeferredrequest", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocRetrieveDeferredReq/NhincProxyDocRetrieveDeferredReq.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyDocRetrieveDeferredReq {

    @Resource
    private WebServiceContext context;

    /**
     * This is the cross gateway retrieve request operation exposed as a service and call underlying implementation method
     * @param crossGatewayRetrieveRequest
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest)
    {
        DocRetrieveAcknowledgementType ack = sendToOrchImpl(crossGatewayRetrieveRequest);
        return ack;
    }

    /**
     *
     * @param crossGatewayRetrieveRequest
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType sendToOrchImpl(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
        NhincProxyDocRetrieveDeferredReqOrchImpl orchImpl = new NhincProxyDocRetrieveDeferredReqOrchImpl();
        DocRetrieveAcknowledgementType ack = orchImpl.crossGatewayRetrieveRequest(crossGatewayRetrieveRequest, context);
        return ack;
    }
}
