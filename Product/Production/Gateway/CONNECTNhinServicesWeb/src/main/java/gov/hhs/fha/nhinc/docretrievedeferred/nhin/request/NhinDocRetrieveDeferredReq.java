package gov.hhs.fha.nhinc.docretrievedeferred.nhin.request;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.request.NhinDocRetrieveDeferredReqWebServiceImpl;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 11:42:45 AM
 */
@WebService(serviceName = "RespondingGatewayDeferredRequest_Retrieve_Service", portName = "RespondingGatewayDeferredRequest_Retrieve_Port_Soap", endpointInterface = "gov.hhs.fha.nhinc.nhindocretrievedeferredrequest.RespondingGatewayDeferredRequestRetrievePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhindocretrievedeferredrequest", wsdlLocation = "WEB-INF/wsdl/NhinDocRetrieveDeferredReq/NhinDocRetrieveDeferredReq.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhinDocRetrieveDeferredReq {

    @Resource
    private WebServiceContext context;
    public DocRetrieveAcknowledgementType respondingGatewayDeferredRequest_CrossGatewayRetrieve(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body) {
        DocRetrieveAcknowledgementType      response;

        response = getResponse(body);

        return response;
    }

    protected DocRetrieveAcknowledgementType  getResponse(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body) {

        return     new NhinDocRetrieveDeferredReqWebServiceImpl().sendToRespondingGateway(body, SamlTokenExtractor.GetAssertion(context));
    }
}
