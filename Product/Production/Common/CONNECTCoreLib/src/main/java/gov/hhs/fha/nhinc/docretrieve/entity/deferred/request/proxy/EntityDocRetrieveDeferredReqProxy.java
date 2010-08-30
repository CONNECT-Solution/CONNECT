package gov.hhs.fha.nhinc.docretrieve.entity.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

/**
 * Interface to implement Entity Doc retrieve deferred request service call
 * @author Sai Valluripalli
 */
public interface EntityDocRetrieveDeferredReqProxy
{
    /**
     * 
     * @param message
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType message, AssertionType assertion, NhinTargetCommunitiesType target);
}
