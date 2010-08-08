package gov.hhs.fha.nhinc.docretrieve.entity.proxy.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * This class is a very simple "No Op" implementation of the Entity Doc retrieve 
 * deferred request service java interface.Always return a Success message. .
 * The idea of this class is to provide a very fast - testable implementation of the service.
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqNoOpImpl implements EntityDocRetrieveDeferredReqProxy
{
    /**
     * 
     * @param message
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType message, AssertionType assertion, NhinTargetCommunitiesType target)
    {
        DocRetrieveAcknowledgementType ack = new DocRetrieveAcknowledgementType();
        RegistryResponseType resp = new RegistryResponseType();
        resp.setStatus("Success");
        ack.setMessage(resp);
        return ack;
    }
}
