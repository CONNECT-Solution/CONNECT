package gov.hhs.fha.nhinc.docretrievedeferred.entity.request;

import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrievedeferred.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.request.NhinDocRetrieveDeferredReqObjectFactory;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.request.NhinDocRetrieveDeferredReqProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation class for Entity Document Retrieve Deferred request message
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqImpl {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * Constructor
     */
    public EntityDocRetrieveDeferredReqImpl() {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
    }

    /**
     *
     * @return Log
     */
    private Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     * Entity Implementation method
     * @param crossGatewayRetrieveRequest
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType message, AssertionType assertion, NhinTargetCommunitiesType target) {
        log.debug("Begin EntityDocRetrieveDeferredRequestImpl.crossGatewayRetrieveRequest");
        DocRetrieveAcknowledgementType nhinResponse = null;
        String homeCommunityId = null;
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        try {
            if ((null != message) && (message.getDocumentRequest() != null) && (message.getDocumentRequest().size() > 0)) {
                auditLog.auditDocRetrieveDeferredRequest(message, assertion);
                DocumentRequest docRequest = message.getDocumentRequest().get(0);
                homeCommunityId = docRequest.getHomeCommunityId();
                RespondingGatewayCrossGatewayRetrieveSecuredRequestType nhinDocRetrieveMsg = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
                // Set document request
                RetrieveDocumentSetRequestType nhinDocRequest = new RetrieveDocumentSetRequestType();
                nhinDocRetrieveMsg.setRetrieveDocumentSetRequest(nhinDocRequest);
                nhinDocRequest.getDocumentRequest().add(docRequest);
                nhinDocRetrieveMsg.setNhinTargetSystem(buildHomeCommunity(docRequest.getHomeCommunityId()));
                if (isPolicyValid(nhinDocRequest, assertion, nhinDocRetrieveMsg.getNhinTargetSystem().getHomeCommunity())) {
                    // Call NHIN proxy
                    auditLog.auditDocRetrieveDeferredRequest(message, assertion);
                    log.debug("Creating NHIN doc retrieve proxy");
                    NhinDocRetrieveDeferredReqObjectFactory objFactory = new NhinDocRetrieveDeferredReqObjectFactory();
                    NhinDocRetrieveDeferredReqProxy docRetrieveProxy = objFactory.getDocumentDeferredRequestProxy();
                    gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType requestMsg = new gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
                    requestMsg.setRetrieveDocumentSetRequest(nhinDocRequest);
                    log.debug("Calling doc retrieve proxy");
                    nhinResponse = docRetrieveProxy.sendToRespondingGateway(requestMsg, assertion);
                }
            }
        } catch (Throwable t) {
            log.error("Error sending doc retrieve deferred message...");
            nhinResponse = new DocRetrieveAcknowledgementType();
            RegistryResponseType registryResponse = new RegistryResponseType();
            nhinResponse.setMessage(registryResponse);
            RegistryErrorList regErrList = new RegistryErrorList();
            RegistryError regErr = new RegistryError();
            regErrList.getRegistryError().add(regErr);
            regErr.setCodeContext("Fault encountered processing internal document retrieve deferred for community " + homeCommunityId);
            regErr.setErrorCode("XDSRegistryNotAvailable");
            regErr.setSeverity("Error");
            registryResponse.setRegistryErrorList(regErrList);
            registryResponse.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
            log.error("Fault encountered processing internal document retrieve deferred for community " + homeCommunityId);
        }
        if (null != nhinResponse) {
            // Audit log - response
            auditLog.auditDocRetrieveDeferredAckResponse(nhinResponse.getMessage(), assertion, homeCommunityId);
        }
        log.debug("End EntityDocRetrieveDeferredRequestImpl.crossGatewayRetrieveRequest");
        return nhinResponse;
    }

    /**
     * 
     * @param homeCommunityId
     * @return NhinTargetSystemType
     */
    private NhinTargetSystemType buildHomeCommunity(String homeCommunityId) {
        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(homeCommunityId);
        nhinTargetSystem.setHomeCommunity(homeCommunity);
        return nhinTargetSystem;
    }

    /**
     * Policy Engine check
     * @param body
     * @param assertion
     * @return boolean
     */
    private boolean isPolicyValid(RetrieveDocumentSetRequestType oEachNhinRequest, AssertionType oAssertion, HomeCommunityType targetCommunity) {
        boolean isValid = false;
        DocRetrieveEventType checkPolicy = new DocRetrieveEventType();
        DocRetrieveMessageType checkPolicyMessage = new DocRetrieveMessageType();
        checkPolicyMessage.setRetrieveDocumentSetRequest(oEachNhinRequest);
        checkPolicyMessage.setAssertion(oAssertion);
        checkPolicy.setMessage(checkPolicyMessage);
        checkPolicy.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        checkPolicy.setInterface(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        checkPolicy.setReceivingHomeCommunity(targetCommunity);
        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyDocRetrieve(checkPolicy);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq);
        /* if response='permit' */
        if (policyResp.getResponse().getResult().get(0).getDecision().value().equals(NhincConstants.POLICY_PERMIT)) {
            isValid = true;
        }
        return isValid;
    }
    
}
