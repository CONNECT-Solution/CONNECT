package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docretrieve.passthru.deferred.request.NhincProxyDocRetrieveDeferredReqOrchImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

/**
 *
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredReqProxyJavaImpl implements NhincProxyDocRetrieveDeferredReqProxy
{
    /**
     *
     * @param request
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType request,
                                                                      AssertionType assertion,
                                                                      NhinTargetSystemType target) {
        return new NhincProxyDocRetrieveDeferredReqOrchImpl().crossGatewayRetrieveRequest(request, assertion, target);
    }

}
