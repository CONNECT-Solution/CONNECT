package gov.hhs.fha.nhinc.docretrievedeferred.entity.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredRespImpl
{

    /**
     * Document Retrieve Deferred Response implementation method
     * @param response
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetResponseType response, AssertionType assertion, NhinTargetCommunitiesType target)
    {
        DocRetrieveAcknowledgementType ack = new DocRetrieveAcknowledgementType();
        RegistryResponseType respType = new RegistryResponseType();
        respType.setStatus("Success");
        ack.setMessage(respType);
        return ack;
    }
}
