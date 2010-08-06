package gov.hhs.fha.nhinc.docretrievedeferred.nhinc.proxy.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * This is Webservice class for Nhinc proxy doc retrieve deferred response unsecured service
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocRetrieveResponse", portName = "NhincProxyDocRetrieveResponsePortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocretrieve.NhincProxyDocRetrieveResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocretrieve", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocRetrieveDeferredResp/NhincProxyDocumentRetrieveDeferredResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyDocRetrieveDeferredResp {

    @Resource
    private WebServiceContext context;

    /**
     * Operation for Doc retrieve deferred response unsecured service retruns an ack after receiving the response
     * @param crossGatewayRetrieveResponse
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse) {
        DocRetrieveAcknowledgementType ack = null;
        if (null != crossGatewayRetrieveResponse) {
            RetrieveDocumentSetResponseType retrieveDocumentSetResponse = crossGatewayRetrieveResponse.getRetrieveDocumentSetResponse();
            AssertionType assertion = crossGatewayRetrieveResponse.getAssertion();
            if (null != assertion) {
                assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
                assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
            }
            NhinTargetSystemType nhinTargetSystem = crossGatewayRetrieveResponse.getNhinTargetSystem();
            ack = sendToNhincProxyImpl(retrieveDocumentSetResponse, assertion, nhinTargetSystem);
        }
        return ack;
    }

    /**
     *
     * @param retrieveDocumentSetResponse
     * @param assertion
     * @param nhinTargetSystem
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType sendToNhincProxyImpl(RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetSystemType nhinTargetSystem) {
        return new NhincProxyDocRetrieveDeferredRespImpl().crossGatewayRetrieveResponse(retrieveDocumentSetResponse, assertion, nhinTargetSystem);
    }
}
