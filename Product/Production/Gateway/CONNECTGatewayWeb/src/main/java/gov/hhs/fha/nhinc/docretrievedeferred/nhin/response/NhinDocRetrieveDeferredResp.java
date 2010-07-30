package gov.hhs.fha.nhinc.docretrievedeferred.nhin.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.NhinDocRetrieveDeferredRespImpl;
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
@WebService(serviceName = "RespondingGatewayDeferredResponse_Retrieve_Service", portName = "RespondingGatewayDeferredResponse_Retrieve_Port_Soap", endpointInterface = "ihe/iti/xds_b/_2007/RespondingGatewayDeferredResponseRetrievePortType", targetNamespace = "XX-urn:ihe:iti:xds_b:_2007", wsdlLocation = "XX-WEB-INF/wsdl/NhinDocRetrieveDeferredReq/NhinDocRetrieveDeferredResp.wsdl")
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

        return new NhinDocRetrieveDeferredRespImpl().sendToAdapter(body, SamlTokenExtractor.GetAssertion(context));
    }

}
