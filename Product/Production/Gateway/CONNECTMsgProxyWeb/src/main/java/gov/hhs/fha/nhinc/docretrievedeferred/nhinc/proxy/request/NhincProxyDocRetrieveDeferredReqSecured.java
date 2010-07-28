package gov.hhs.fha.nhinc.docretrievedeferred.nhinc.proxy.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrievedeferred.nhinc.request.NhincProxyDocRetrieveDeferredReqImpl;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * This is an Nhinc Proxy secured service for Document Retrieve deferred Request message
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocRetrieveDeferredRequestSecured", portName = "NhincProxyDocRetrieveDeferredRequestSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocretrievedeferredsecured.NhincProxyDocRetrieveDeferredRequestSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocretrievedeferredsecured", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocRetrieveDeferredReqSecured/NhincProxyDocumentRetrieveDeferredRequestSecured.wsdl")
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
        DocRetrieveAcknowledgementType ack = null;
        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = null;
        NhinTargetSystemType nhinTargetSystem = null;
        AssertionType assertion = null;

        if (null != body) {
            retrieveDocumentSetRequest = extractDocRetrieveRequest(body);
            nhinTargetSystem = extractNhinTargetSystem(body);
            assertion = extractAssertionFromServiceContext();
            ack = sendToNhincProxyDocretrieveImplementation(retrieveDocumentSetRequest, assertion, nhinTargetSystem);
        }
        return ack;
    }

    /**
     * This method extract the SAML assertion information from the Webservice Context
     * @return AssertionType
     */
    protected AssertionType extractAssertionFromServiceContext() {
        return SamlTokenExtractor.GetAssertion(context);
    }

    /**
     * This method extracts the Document Retreieve request from the Secured Doc retrieve deferred request message
     * @param body
     * @return RetrieveDocumentSetRequestType
     */
    protected RetrieveDocumentSetRequestType extractDocRetrieveRequest(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body) {
        return body.getRetrieveDocumentSetRequest();
    }

    /**
     * 
     * @param body
     * @return NhinTargetSystemType
     */
    protected NhinTargetSystemType extractNhinTargetSystem(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body) {
        return body.getNhinTargetSystem();
    }

    /**
     * 
     * @param impl
     * @param retrieveDocumentSetRequest
     * @param assertion
     * @param nhinTargetSystem
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType sendToNhincProxyDocretrieveImplementation(RetrieveDocumentSetRequestType retrieveDocumentSetRequest, AssertionType assertion, NhinTargetSystemType nhinTargetSystem) {
        return new NhincProxyDocRetrieveDeferredReqImpl().crossGatewayRetrieveRequest(retrieveDocumentSetRequest, assertion, nhinTargetSystem);
    }
}
