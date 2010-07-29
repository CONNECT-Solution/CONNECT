package gov.hhs.fha.nhinc.docretrievedeferred.entity.response;

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
        if(null != body)
        {
            RetrieveDocumentSetResponseType retrieveDocumentSetResponse = body.getRetrieveDocumentSetResponse();
            NhinTargetCommunitiesType nhinTargetCommunities = body.getNhinTargetCommunities();
            AssertionType assertion = extractAssertionInfo();
            EntityDocRetrieveDeferredRespImpl impl = new EntityDocRetrieveDeferredRespImpl();
            ack = impl.crossGatewayRetrieveResponse(retrieveDocumentSetResponse, assertion, nhinTargetCommunities);
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

}
