package gov.hhs.fha.nhinc.docretrieve.nhinc.proxy.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docretrieve.nhinc.deferred.response.NhincProxyDocRetrieveDeferredRespOrchImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 *
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredRespJavaImpl implements NhincProxyDocRetrieveDeferredRespProxy
{

    /**
     * 
     * @param retrieveDocumentSetResponse
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetSystemType target) {
        return new NhincProxyDocRetrieveDeferredRespOrchImpl().crossGatewayRetrieveResponse(retrieveDocumentSetResponse, assertion, target);
    }

}
