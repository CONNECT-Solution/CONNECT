package gov.hhs.fha.nhinc.docquery.entity.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
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
    LOG.debug("start respondingGatewayCrossGatewayQuery(AdhocQueryRequest message, AssertionType assertion, NhinTargetCommunitiesType target)");

    DocQueryAcknowledgementType nhincResponse = null;
    logEntityDocQuery(message, assertion);

    CMUrlInfos urlInfoList = new CMUrlInfos();// = getEndpoints(target);

    for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {
      //create a new request to send out to each target community
      LOG.debug("Target: " + urlInfo.getHcid());
      //check the policy for the outgoing request to the target community
      boolean bIsPolicyOk = checkPolicy(message, assertion, urlInfo.getHcid());

      if (bIsPolicyOk) {
        NhinTargetSystemType targetSystem = buildTargetSystem(urlInfo);

        //sendToNhinProxy(message, assertion, targetSystem);

      } else {
        LOG.error("The policy engine evaluated the request and denied the request.");
      }
    }
    return nhincResponse;
  }

  private void logEntityDocQuery(AdhocQueryRequest message, AssertionType assertion) {
    getDocQueryAuditLog().audit(message, assertion);
  }

  protected CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities) throws ConnectionManagerException {
    CMUrlInfos urlInfoList = null;

    urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.NHIN_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME);

    return urlInfoList;
  }

  private boolean checkPolicy(AdhocQueryRequest message, AssertionType assertion, String hcid) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public boolean isServiceEnabled() {
    return readBooleanGatewayProperty(NhincConstants.NHIN_ADMIN_DIST_SERVICE_ENABLED);
  }

  public boolean readBooleanGatewayProperty(String propertyName) {
    boolean result = false;
    try {
      result = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, propertyName);
    } catch (PropertyAccessException ex) {
      LOG.error("Error: Failed to retrieve " + propertyName + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
      LOG.error(ex.getMessage());
    }
    return result;
  }

  private NhinTargetSystemType buildTargetSystem(CMUrlInfo urlInfo) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
