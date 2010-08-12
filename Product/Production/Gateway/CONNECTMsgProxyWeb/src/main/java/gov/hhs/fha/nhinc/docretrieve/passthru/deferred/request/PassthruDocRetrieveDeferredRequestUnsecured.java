package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocRetrieveDeferredRequest", portName = "NhincProxyDocRetrieveDeferredRequestPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocretrievedeferredrequest.NhincProxyDocRetrieveDeferredRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocretrievedeferredrequest", wsdlLocation = "WEB-INF/wsdl/PassthruDocRetrieveDeferredRequestUnsecured/NhincProxyDocRetrieveDeferredReq.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class PassthruDocRetrieveDeferredRequestUnsecured extends PassthruDocRetrieveDeferredRequestImpl {

    @Resource
    private WebServiceContext context;

    /**
     * 
     * @param crossGatewayRetrieveRequest
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
        return crossGatewayRetrieveRequest(crossGatewayRetrieveRequest, context);
    }
}
