package gov.hhs.fha.nhinc.docretrieve.entity.proxy.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.docretrieve.entity.deferred.request.EntityDocRetrieveDeferredReqImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

/**
 * Java implementation for Entity Document retrieve deferred unsecured request webservice call
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqJavaImpl implements EntityDocRetrieveDeferredReqProxy {

    /**
     * 
     * @param message
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType message, AssertionType assertion, NhinTargetCommunitiesType target) {
        return getEntityDocRetrieveDeferredReqImpl().crossGatewayRetrieveRequest(message, assertion, target);
    }

    /**
     * 
     * @return EntityDocRetrieveDeferredReqImpl
     */
    protected EntityDocRetrieveDeferredReqImpl getEntityDocRetrieveDeferredReqImpl()
    {
        return new EntityDocRetrieveDeferredReqImpl();
    }
}
