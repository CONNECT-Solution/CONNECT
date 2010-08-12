package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocRetrieveDeferredRequestSecured", portName = "NhincProxyDocRetrieveDeferredRequestSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocretrievedeferredsecuredrequest.NhincProxyDocRetrieveDeferredRequestSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocretrievedeferredsecuredrequest", wsdlLocation = "WEB-INF/wsdl/PassthruDocRetrieveDeferredRequestSecured/NhincProxyDocRetrieveDeferredReqSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class PassthruDocRetrieveDeferredRequestSecured extends PassthruDocRetrieveDeferredRequestImpl {

    @Resource
    private WebServiceContext context;

    /**
     * 
     * @param body
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body) {
        return crossGatewayRetrieveRequest(body, context);
    }
}
