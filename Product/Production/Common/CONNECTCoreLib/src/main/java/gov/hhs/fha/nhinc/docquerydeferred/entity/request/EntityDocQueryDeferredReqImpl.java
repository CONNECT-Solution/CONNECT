package gov.hhs.fha.nhinc.docquerydeferred.entity.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * Implementation class for Entity Document Query Deferred request message
 * @author Mark Goldman
 */
public class EntityDocQueryDeferredReqImpl {

  /**
   * Constructor
   */
  public EntityDocQueryDeferredReqImpl() {
  }

  /**
   * Entity Implementation method
   * @param retrieveDocumentSetRequest
   * @param assertion
   * @param nhinTargetCommunities
   * @return DocRetrieveAcknowledgementType
   */
  DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(
          AdhocQueryRequest retrieveDocumentSetRequest,
          AssertionType assertion,
          NhinTargetCommunitiesType nhinTargetCommunities) {
    DocQueryAcknowledgementType ack = new DocQueryAcknowledgementType();
    RegistryResponseType resp = new RegistryResponseType();
    resp.setStatus("Success");
    ack.setMessage(resp);
    return ack;
  }
}
