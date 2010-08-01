package gov.hhs.fha.nhinc.docretrievedeferred.nhin.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.response.NhinDocRetrieveDeferredRespWebServiceImpl;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * Created by
 * User: ralph
 * Date: Jul 28, 2010
 * Time: 11:23:42 AM
 */
@WebService(serviceName = "RespondingGatewayDeferredResponse_Retrieve_Service", portName = "RespondingGatewayDeferredResponse_Retrieve_Port_Soap", endpointInterface = "gov.hhs.fha.nhinc.nhindocretrievedeferredresponse.RespondingGatewayDeferredResponseRetrievePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhindocretrievedeferredresponse", wsdlLocation = "WEB-INF/wsdl/NhinDocRetrieveDeferredResp/NhinDocRetrieveDeferredResp.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhinDocRetrieveDeferredResp {

    private Logger      log;

    @Resource
    private WebServiceContext context;
    public DocRetrieveAcknowledgementType respondingGatewayDeferredRequest_CrossGatewayRetrieve(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body) {
        DocRetrieveAcknowledgementType      response;

        response = getResponse(body);

        return response;
    }

    protected DocRetrieveAcknowledgementType  getResponse(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body) {

        return new NhinDocRetrieveDeferredRespWebServiceImpl().sendToRespondingGateway(body, SamlTokenExtractor.GetAssertion(context));
    }

}
