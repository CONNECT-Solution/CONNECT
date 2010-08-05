package gov.hhs.fha.nhinc.docquerydeferred.entity.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

/**
 *
 * @author Mark Goldman
 */
public class EntityDocQueryDeferredRequestHelper {

    /**
     * Get AdhocQueryRequest from Responding Gateway Cross Gateway Query Request Type
     * @param request
     * @return AdhocQueryRequest
     */
    public AdhocQueryRequest getAdhocQueryRequest(RespondingGatewayCrossGatewayQueryRequestType request) {
        AdhocQueryRequest adhocQueryRequest = null;
        if (null != request) {
            adhocQueryRequest = request.getAdhocQueryRequest();
        }
        return adhocQueryRequest;
    }

    /**
     * Get AdhocQueryRequest from Responding Gateway Cross Gateway Query Secured Request Type
     * @param request
     * @return RetrieveDocumentSetRequestType
     */
    public AdhocQueryRequest getAdhocQueryRequest(RespondingGatewayCrossGatewayQuerySecuredRequestType request) {
        AdhocQueryRequest adhocQueryRequest = null;
        if (null != request) {
            adhocQueryRequest = request.getAdhocQueryRequest();
        }
        return adhocQueryRequest;
    }

    /**
     * Extract Assertion information from Responding Gateway Cross Gateway Query Request Type
     * @param request
     * @return AssertionType
     */
    public AssertionType getAssertion(RespondingGatewayCrossGatewayQueryRequestType request) {
        AssertionType assertion = null;
        if (null != request) {
            assertion = request.getAssertion();
        }
        return assertion;
    }

    /**
     * Extract assertion information from Web service context
     * @param context
     * @return AssertionType
     */
    public AssertionType getAssertion(WebServiceContext context) {
        AssertionType assertion = null;
        if (context != null) {
            assertion = extractAssertion(context);
        }
        return assertion;
    }

    /**
     * This method extracts the NhinTargetCommunities fromResponding Gateway Cross Gateway Query Request Type
     * @param request
     * @return NhinTargetCommunitiesType
     */
    public NhinTargetCommunitiesType getNhinTargetCommunities(RespondingGatewayCrossGatewayQueryRequestType request) {
        NhinTargetCommunitiesType nhinTargetCommunities = null;
        if (null != request) {
            nhinTargetCommunities = request.getNhinTargetCommunities();
        }
        return nhinTargetCommunities;
    }

    /**
     * This method extracts the NhinTargetCommunities from Responding Gateway Cross Gateway Query Secured Request Type
     * @param request
     * @return NhinTargetCommunitiesType
     */
    public NhinTargetCommunitiesType getNhinTargetCommunities(RespondingGatewayCrossGatewayQuerySecuredRequestType request) {
        NhinTargetCommunitiesType nhinTargetCommunities = null;
        if (null != request) {
            nhinTargetCommunities = request.getNhinTargetCommunities();
        }
        return nhinTargetCommunities;
    }

    /**
     * This method returns Assertion information from the Webservice context
     * for any secured web service calls
     * @param context
     * @return AssertionType
     */
    protected AssertionType extractAssertion(WebServiceContext context) {
        return SamlTokenExtractor.GetAssertion(context);
    }
}
