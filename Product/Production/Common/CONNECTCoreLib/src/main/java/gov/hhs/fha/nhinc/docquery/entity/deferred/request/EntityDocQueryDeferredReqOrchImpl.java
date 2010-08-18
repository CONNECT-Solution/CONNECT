package gov.hhs.fha.nhinc.docquery.entity.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.DocQueryPolicyChecker;
import gov.hhs.fha.nhinc.docquery.passthru.deferred.request.proxy.PassthruDocQueryDeferredRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.docquery.passthru.deferred.request.proxy.PassthruDocQueryDeferredRequestProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
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
   * @param message
   * @param assertion
   * @param target
   * @return
   */
  public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(
          AdhocQueryRequest message, AssertionType assertion, NhinTargetCommunitiesType target) {
    getLog().debug("start respondingGatewayCrossGatewayQuery(AdhocQueryRequest message, AssertionType assertion, NhinTargetCommunitiesType target)");

    DocQueryAcknowledgementType nhincResponse = new DocQueryAcknowledgementType();
    RegistryResponseType regResp = new RegistryResponseType();
    nhincResponse.setMessage(regResp);

    getAuditLog().audit(message, assertion);

    try {
      CMUrlInfos urlInfoList = getEndpoints(target);

      if (urlInfoList != null && NullChecker.isNotNullish(urlInfoList.getUrlInfo())) {
        for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {
          //create a new request to send out to each target community
          if (getLog().isDebugEnabled()) {
            getLog().debug(String.format("Target: {0}", urlInfo.getHcid()));
          }

          //check the policy for the outgoing request to the target community
          if (checkPolicy(message, assertion, urlInfo.getHcid())) {
            nhincResponse = getProxy().crossGatewayQueryRequest(message, assertion, buildTargetSystem(urlInfo));
          } else {
            getLog().error("The policy engine evaluated the request and denied the request.");
          }
        }
      } else {
        getLog().error("Failed to obtain target URL from connection manager");
        regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG);
      }
    } catch (Exception e) {
      getLog().error(e);
      nhincResponse.getMessage().setStatus(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG);
    }

    getAuditLog().logDocQueryAck(nhincResponse,
                                 assertion,
                                 NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                                 NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

    return nhincResponse;
  }

  /**
   *
   * @return PassthruDocQueryDeferredRequestProxy
   */
  protected PassthruDocQueryDeferredRequestProxy getProxy() {
    PassthruDocQueryDeferredRequestProxyObjectFactory objFactory = new PassthruDocQueryDeferredRequestProxyObjectFactory();
    PassthruDocQueryDeferredRequestProxy docRetrieveProxy = objFactory.getPassthruDocQueryDeferredRequestProxy();
    return docRetrieveProxy;
  }

  /**
   *
   * @param targetCommunities
   * @return Returns the endpoints for given target communities
   * @throws ConnectionManagerException
   */
  protected CMUrlInfos getEndpoints(final NhinTargetCommunitiesType targetCommunities) throws ConnectionManagerException {
    CMUrlInfos urlInfoList = null;

    urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(
            targetCommunities, NhincConstants.NHIN_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME);

    return urlInfoList;
  }

  /**
   *
   * @param message
   * @param assertion
   * @param hcid
   * @return Returns true if given home community is allowed to send requests
   */
  protected boolean checkPolicy(final AdhocQueryRequest message, final AssertionType assertion, final String hcid) {
    HomeCommunityType homeCommunity = new HomeCommunityType();
    homeCommunity.setHomeCommunityId(hcid);

    boolean policyIsValid = new DocQueryPolicyChecker().checkOutgoingRequestPolicy(message, assertion, homeCommunity);

    return policyIsValid;
  }

  /**
   *
   * @param urlInfo
   * @return NhinTargetSystemType for given urlInfo
   */
  protected NhinTargetSystemType buildTargetSystem(final CMUrlInfo urlInfo) {
    NhinTargetSystemType targetSystem = new NhinTargetSystemType();
    targetSystem.setUrl(urlInfo.getUrl());
    return targetSystem;
  }

  /**
   * 
   * @return
   */
  protected Log getLog() {
    return LOG;
  }

  /**
   *
   * @return
   */
  protected DocQueryAuditLog getAuditLog() {
    return new DocQueryAuditLog();
  }
}
