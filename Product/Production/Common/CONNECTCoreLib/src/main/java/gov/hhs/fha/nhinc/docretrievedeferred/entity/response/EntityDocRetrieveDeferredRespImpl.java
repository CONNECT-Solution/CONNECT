package gov.hhs.fha.nhinc.docretrievedeferred.entity.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.docretrievedeferred.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrievedeferred.DocRetrieveDeferredPolicyChecker;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.response.NhinDocRetrieveDeferredRespObjectFactory;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.response.NhinDocRetrieveDeferredRespProxy;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredRespImpl {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public EntityDocRetrieveDeferredRespImpl() {
        log = createLogger();
        debugEnabled = false;
    }

    /**
     *
     * @return Log
     */
    protected Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     * Document Retrieve Deferred Response implementation method
     * @param response
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetResponseType response, AssertionType assertion, NhinTargetCommunitiesType target) {
        if (debugEnabled) {
            log.debug("Begin EntityDocRetrieveDeferredRespImpl.crossGatewayRetrieveResponse");
        }
        DocRetrieveAcknowledgementType nhinResponse = null;
        String homeCommunityId = null;
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        try {
            if (null != response && (null != assertion) && (null != target)) {
                auditLog.auditDocRetrieveDeferredResponse(response, assertion);
                DocRetrieveDeferredPolicyChecker policyCheck = new DocRetrieveDeferredPolicyChecker();
                homeCommunityId = getHomeCommFromTarget(target);
                if (debugEnabled) {
                        log.debug("Calling Policy Engine");
                    }
                if (policyCheck.checkOutgoingPolicy(response, assertion, homeCommunityId)) {
                    RespondingGatewayCrossGatewayRetrieveSecuredResponseType req = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
                    req.setRetrieveDocumentSetResponse(response);
                    // Call NHIN proxy
                    auditLog.auditDocRetrieveDeferredResponse(response, assertion);
                    NhinDocRetrieveDeferredRespObjectFactory objFactory = new NhinDocRetrieveDeferredRespObjectFactory();
                    NhinDocRetrieveDeferredRespProxy docRetrieveProxy = objFactory.getDocumentDeferredResponseProxy();
                    if (debugEnabled) {
                        log.debug("Calling doc retrieve proxy response");
                    }
                    nhinResponse = docRetrieveProxy.sendToRespondingGateway(req, assertion);
                }
            }
        } catch (Throwable t) {
            log.error("Error sending doc retrieve deferred message...");
            nhinResponse = buildRegistryErrorAck(homeCommunityId, "Policy Check Failed on Doc retrieve deferred response for community ");
            log.error("Fault encountered processing internal document retrieve deferred for community " + homeCommunityId);
        }
        if (null != nhinResponse) {
            // Audit log - response
            auditLog.auditDocRetrieveDeferredAckResponse(nhinResponse.getMessage(), assertion, homeCommunityId);
        }
        if (debugEnabled) {
            log.debug("End EntityDocRetrieveDeferredRespImpl.crossGatewayRetrieveResponse");
        }
        return nhinResponse;
    }

    /**
     *
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType buildRegistryErrorAck(String homeCommunityId, String error)
    {
        DocRetrieveAcknowledgementType nhinResponse = new DocRetrieveAcknowledgementType();
        RegistryResponseType registryResponse = new RegistryResponseType();
            nhinResponse.setMessage(registryResponse);
            RegistryErrorList regErrList = new RegistryErrorList();
            RegistryError regErr = new RegistryError();
            regErrList.getRegistryError().add(regErr);
            regErr.setCodeContext(error +" " + homeCommunityId);
            regErr.setErrorCode("XDSRegistryNotAvailable");
            regErr.setSeverity("Error");
            registryResponse.setRegistryErrorList(regErrList);
            registryResponse.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        return nhinResponse;
    }
    
    /**
     *
     * @param target
     * @return String
     */
    protected String getHomeCommFromTarget(NhinTargetCommunitiesType target) {
        String sHomComm = null;
        if (target.getNhinTargetCommunity() != null &&
                target.getNhinTargetCommunity().size() > 0) {
            NhinTargetCommunityType comm = target.getNhinTargetCommunity().get(0);
            if (comm.getHomeCommunity() != null) {
                sHomComm = comm.getHomeCommunity().getHomeCommunityId();
            }
        }
        return sHomComm;
    }
}
