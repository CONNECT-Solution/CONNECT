package gov.hhs.fha.nhinc.docretrievedeferred.entity.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * This is the Secured implementation of Document Retrieve Deferred Request Message
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityDocRetrieveDeferredRequestSecured", portName = "EntityDocRetrieveDeferredRequestSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveDeferredRequestSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocretrievesecured", wsdlLocation = "WEB-INF/wsdl/EntityDocRetrieveDeferredReqSecured/EntityDocumentRetrieveDeferredRequestSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityDocRetrieveDeferredReqSecured {

    @Resource
    private WebServiceContext context;

    /**
     * Entity Document Retrieve deferred request operation calls the implementation method with input values
     * @param body
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body) {
        ExtractEntityDocRetrieveDeferredRequestValues oExtract = new ExtractEntityDocRetrieveDeferredRequestValues();
        AssertionType assertion = oExtract.extractSecuredAssertion(context);
        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = oExtract.extractRetrieveDocumentSetSecuredRequestType(body);
        NhinTargetCommunitiesType nhinTargetCommunities = oExtract.extractSecuredNhinTargetCommunities(body);
        return getResponse(oExtract, retrieveDocumentSetRequest, assertion, nhinTargetCommunities);
    }

    /**
     *
     * @param oExtract
     * @param retrieveDocumentSetRequest
     * @param assertion
     * @param nhinTargetCommunities
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType getResponse(ExtractEntityDocRetrieveDeferredRequestValues oExtract, RetrieveDocumentSetRequestType retrieveDocumentSetRequest, AssertionType assertion, NhinTargetCommunitiesType nhinTargetCommunities) {
        return oExtract.getEntityDocRetrieveDeferredReqImpl().crossGatewayRetrieveRequest(retrieveDocumentSetRequest, assertion, nhinTargetCommunities);
    }
}
