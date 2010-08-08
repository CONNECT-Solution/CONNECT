package gov.hhs.fha.nhinc.docretrieve.entity.proxy.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.docretrieve.entity.deferred.response.EntityDocRetrieveDeferredRespImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredRespJavaImpl implements EntityDocRetrieveDeferredRespProxy
{

    /**
     * 
     * @param response
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetResponseType response, AssertionType assertion, NhinTargetCommunitiesType target) {
        return new EntityDocRetrieveDeferredRespImpl().crossGatewayRetrieveResponse(response, assertion, target);
    }
}
