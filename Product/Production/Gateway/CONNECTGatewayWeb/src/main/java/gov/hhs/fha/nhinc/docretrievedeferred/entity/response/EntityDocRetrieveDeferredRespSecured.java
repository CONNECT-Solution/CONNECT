package gov.hhs.fha.nhinc.docretrievedeferred.entity.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * Webservice class for Entity Document Retrieve deferred Response secured Service
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityDocRetrieveDeferredResponseSecured", portName = "EntityDocRetrieveDeferredResponseSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveDeferredResponseSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocretrievesecured", wsdlLocation = "WEB-INF/wsdl/EntityDocRetrieveDeferredRespSecured/EntityDocumentRetrieveDeferredResponseSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityDocRetrieveDeferredRespSecured {

    @Resource
    WebServiceContext context;

    /**
     * Webservice operation for Entity Document Retrieve deferred Secured service
     * @param body
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body) {
        DocRetrieveAcknowledgementType ack = null;
        if (null != body) {
            RetrieveDocumentSetResponseType retrieveDocumentSetResponse = body.getRetrieveDocumentSetResponse();
            NhinTargetCommunitiesType nhinTargetCommunities = body.getNhinTargetCommunities();
            AssertionType assertion = extractAssertionInfo();
            if (null != assertion) {
                assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
                assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
            }
            ack = sendToCrossGatewayRetrieveResponseImpl(retrieveDocumentSetResponse, assertion, nhinTargetCommunities);
        }
        return ack;
    }

    /**
     * Extract Assertion information from WebserviceContext
     * @return AssertionType
     */
    protected AssertionType extractAssertionInfo() {
        return SamlTokenExtractor.GetAssertion(context);
    }

    /**
     * This helper method makes call out to implementation to get the Retrieve response
     * @param retrieveDocumentSetResponse
     * @param assertion
     * @param nhinTargetCommunities
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType sendToCrossGatewayRetrieveResponseImpl(RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetCommunitiesType nhinTargetCommunities) {
        return new EntityDocRetrieveDeferredRespImpl().crossGatewayRetrieveResponse(retrieveDocumentSetResponse, assertion, nhinTargetCommunities);
    }
}
