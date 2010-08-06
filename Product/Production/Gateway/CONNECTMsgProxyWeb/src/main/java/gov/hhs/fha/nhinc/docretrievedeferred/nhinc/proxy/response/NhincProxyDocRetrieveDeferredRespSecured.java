package gov.hhs.fha.nhinc.docretrievedeferred.nhinc.proxy.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * This is Webservice class for Nhinc proxy doc retrieve deferred response secured service
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocRetrieveResponseSecured", portName = "NhincProxyDocRetrieveResponseSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocretrievesecured.NhincProxyDocRetrieveResponseSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocretrievesecured", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocRetrieveDeferredRespSecured/NhincProxyDocumentRetrieveDeferredResponseSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyDocRetrieveDeferredRespSecured {

    @Resource
    WebServiceContext context;

    /**
     * Operation for Doc retrieve deferred response secured service retruns an ack after receiving the response
     * @param body
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body) {
        DocRetrieveAcknowledgementType ack = null;
        if (null != body) {
            RetrieveDocumentSetResponseType retrieveDocumentSetResponse = body.getRetrieveDocumentSetResponse();
            NhinTargetSystemType nhinTargetSystem = body.getNhinTargetSystem();
            AssertionType assertion = extractAssertionInfo();
            if (null != assertion) {
                assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
                assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
            }
            ack = sendToNhincProxyImpl(retrieveDocumentSetResponse, assertion, nhinTargetSystem);
        }
        return ack;
    }

    /**
     * This method extracts assertion information from Webservice context
     * @return AssertionType
     */
    protected AssertionType extractAssertionInfo() {
        return SamlTokenExtractor.GetAssertion(context);
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
