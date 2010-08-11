package gov.hhs.fha.nhinc.docquery.entity.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation class for Entity Document Query Deferred request message
 * @author Mark Goldman
 */
public class EntityDocQueryDeferredReqOrchImpl {

  private static final Log LOG = LogFactory.getLog(EntityDocQueryDeferredReqOrchImpl.class);

  /**
   *
   * @return
   */
  protected DocQueryAuditLog getDocQueryAuditLog() {
    return new DocQueryAuditLog();
  }

  /**
   * 
   * @param body
   * @param context
   * @return
   */
  public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(
          RespondingGatewayCrossGatewayQueryRequestType body,
          WebServiceContext context) {
    LOG.debug("start respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType body, WebServiceContext context)");
    DocQueryAcknowledgementType ack = null;
    if (null != body) {
      AdhocQueryRequest message = body.getAdhocQueryRequest();
      AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
      if (null != assertion) {
        assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        assertion.getRelatesToList().addAll(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
      }
      NhinTargetCommunitiesType target = body.getNhinTargetCommunities();
      ack = respondingGatewayCrossGatewayQuery(message, assertion, target);
    } else {
      ack = buildRegistryErrorAck(" ", "Entity Request was null unable to process");
    }

    LOG.debug("end respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType body, WebServiceContext context)");
    return ack;
  }

  public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(
          RespondingGatewayCrossGatewayQuerySecuredRequestType body,
          WebServiceContext context) {
    LOG.debug("start respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQuerySecuredRequestType body, WebServiceContext context)");
    DocQueryAcknowledgementType ack = null;
    if (null != body) {
      AdhocQueryRequest message = body.getAdhocQueryRequest();
      AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
      if (null != assertion) {
        assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        assertion.getRelatesToList().addAll(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
      }
      NhinTargetCommunitiesType target = body.getNhinTargetCommunities();
      ack = respondingGatewayCrossGatewayQuery(message, assertion, target);
    } else {
      ack = buildRegistryErrorAck(" ", "Entity Request was null unable to process");
    }

    LOG.debug("end respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQuerySecuredRequestType body, WebServiceContext context)");
    return ack;
  }

  /**
   *
   * @return DocRetrieveAcknowledgementType
   */
  protected DocQueryAcknowledgementType buildRegistryErrorAck(String homeCommunityId, String error) {
    DocQueryAcknowledgementType nhinResponse = new DocQueryAcknowledgementType();
    RegistryResponseType registryResponse = new RegistryResponseType();
    nhinResponse.setMessage(registryResponse);
    RegistryErrorList regErrList = new RegistryErrorList();
    RegistryError regErr = new RegistryError();
    regErrList.getRegistryError().add(regErr);
    regErr.setCodeContext(error + " " + homeCommunityId);
    regErr.setErrorCode("XDSRegistryNotAvailable");
    regErr.setSeverity("Error");
    registryResponse.setRegistryErrorList(regErrList);
    registryResponse.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
    return nhinResponse;
  }

  /**
   * 
   * @param message
   * @param assertion
   * @param target
   * @return
   */
  public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(
          AdhocQueryRequest message, AssertionType assertion, NhinTargetCommunitiesType target) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
