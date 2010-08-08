package gov.hhs.fha.nhinc.docretrievedeferred.entity.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrieve.entity.deferred.request.EntityDocRetrieveDeferredReqImpl;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
public class ExtractEntityDocRetrieveDeferredRequestValues {

    /**
     * Create an instance of EntityDocRetrieveDeferredReqImpl Class
     * @return EntityDocRetrieveDeferredReqImpl
     */
    protected EntityDocRetrieveDeferredReqImpl getEntityDocRetrieveDeferredReqImpl() {
        return new EntityDocRetrieveDeferredReqImpl();
    }

    /**
     * Get Document retrieve request set from Cross gateway Retrieve documents request type
     * @param request
     * @return RetrieveDocumentSetRequestType
     */
    protected RetrieveDocumentSetRequestType extractRetrieveDocumentSetRequestType(RespondingGatewayCrossGatewayRetrieveRequestType request) {
        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = null;
        if (null != request) {
            retrieveDocumentSetRequest = request.getRetrieveDocumentSetRequest();
        }
        return retrieveDocumentSetRequest;
    }

    /**
     * Get Document retrieve request set from Cross gateway Retrieve documents secured request type
     * @param request
     * @return RetrieveDocumentSetRequestType
     */
    protected RetrieveDocumentSetRequestType extractRetrieveDocumentSetSecuredRequestType(RespondingGatewayCrossGatewayRetrieveSecuredRequestType request) {
        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = null;
        if (null != request) {
            retrieveDocumentSetRequest = request.getRetrieveDocumentSetRequest();
        }
        return retrieveDocumentSetRequest;
    }

    /**
     * Extract Assertion information from Cross gateway retrieve documents request type
     * @param request
     * @return AssertionType
     */
    protected AssertionType extractAssertion(RespondingGatewayCrossGatewayRetrieveRequestType request) {
        AssertionType assertion = null;
        if (null != request) {
            assertion = request.getAssertion();
        }
        return assertion;
    }

    /**
     * This method extracts the NhinTargetCommunities from Entity Cross gateway retrieve documents request type
     * @param request
     * @return NhinTargetCommunitiesType
     */
    protected NhinTargetCommunitiesType extractNhinTargetCommunities(RespondingGatewayCrossGatewayRetrieveRequestType request) {
        NhinTargetCommunitiesType nhinTargetCommunities = null;
        if (null != request) {
            nhinTargetCommunities = request.getNhinTargetCommunities();
        }
        return nhinTargetCommunities;
    }

    /**
     * This method extracts the NhinTargetCommunities from Entity Cross gateway retrieve documents Secured request type
     * @param request
     * @return NhinTargetCommunitiesType
     */
    protected NhinTargetCommunitiesType extractSecuredNhinTargetCommunities(RespondingGatewayCrossGatewayRetrieveSecuredRequestType request) {
        NhinTargetCommunitiesType nhinTargetCommunities = null;
        if (null != request) {
            nhinTargetCommunities = request.getNhinTargetCommunities();
        }
        return nhinTargetCommunities;
    }

    /**
     * Extract assertion information from Web service context
     * @param context
     * @return AssertionType
     */
    protected AssertionType extractSecuredAssertion(WebServiceContext context) {
        AssertionType assertion = null;
        if (context != null) {
            assertion = getSamlTokenExtractor(context);
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
            assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
        
        }
        return assertion;
    }

    /**
     * This method returns Assertion information from the Webservice context for any secured web service calls
     * @param context
     * @return AssertionType
     */
    protected AssertionType getSamlTokenExtractor(WebServiceContext context) {
        return SamlTokenExtractor.GetAssertion(context);
    }
   
}
