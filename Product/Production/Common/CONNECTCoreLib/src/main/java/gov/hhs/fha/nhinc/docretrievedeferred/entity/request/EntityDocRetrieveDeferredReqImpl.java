package gov.hhs.fha.nhinc.docretrievedeferred.entity.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * Implementation class for Entity Document Retrieve Deferred request message
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqImpl {

    /**
     * Constructor
     */
    public EntityDocRetrieveDeferredReqImpl() {
    }

    /**
     * Entity Implementation method
     * @param crossGatewayRetrieveRequest
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType message, AssertionType assertion, NhinTargetCommunitiesType target) {
        DocRetrieveAcknowledgementType ack = new DocRetrieveAcknowledgementType();
        RegistryResponseType resp = new RegistryResponseType();
        resp.setStatus("Success");
        ack.setMessage(resp);
        return ack;
    }
}
