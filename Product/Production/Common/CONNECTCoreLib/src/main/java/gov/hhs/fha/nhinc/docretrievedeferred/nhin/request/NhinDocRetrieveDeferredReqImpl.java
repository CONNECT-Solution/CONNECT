package gov.hhs.fha.nhinc.docretrievedeferred.nhin.request;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.docretrievedeferred.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrievedeferred.adapter.proxy.request.AdapterDocRetrieveDeferredReqObjectFactory;
import gov.hhs.fha.nhinc.docretrievedeferred.adapter.proxy.request.AdapterDocRetrieveDeferredReqProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * Created by
 * User: ralph
 * Date: Aug 2, 2010
 * Time: 1:50:15 PM
 */
public class NhinDocRetrieveDeferredReqImpl {

    private static Log log = LogFactory.getLog(NhinDocRetrieveDeferredReqImpl.class);

    /**
     * 
     * @param body
     * @param context
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType sendToRespondingGateway(RetrieveDocumentSetRequestType body, WebServiceContext context) {
        DocRetrieveAcknowledgementType response = null;
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        auditLog.auditDocRetrieveDeferredRequest(body, assertion);

        try {
            String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();
            if (isServiceEnabled()) {
                if (isInPassThroughMode()) {
                    response = sendDocRetrieveDeferredRequestToAgency(body, assertion);
                } else {
                    response = serviceDocRetrieveInternal(body, assertion, homeCommunityId);
                }
            } else {
                log.debug("Doc retrieve service is not enabled. returning an empty response");
                response = createEmptyResponse(homeCommunityId);
            }

        } catch (Throwable t) {
            log.error("Error processing NHIN Doc Retrieve: " + t.getMessage(), t);
            response = createErrorResponse("Processing document retrieve message");
        }
        auditLog.auditDocRetrieveDeferredAckResponse(response.getMessage(), assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        return response;
    }

    /**
     * 
     * @param request
     * @param assertion
     * @param homeCommunityId
     * @return
     */
    private DocRetrieveAcknowledgementType serviceDocRetrieveInternal(RetrieveDocumentSetRequestType request, AssertionType assertion, String homeCommunityId) {
        log.debug("Begin DocRetrieveImpl.serviceDocRetrieveInternal");
        DocRetrieveAcknowledgementType response = null;
        HomeCommunityType hcId = new HomeCommunityType();
        hcId.setHomeCommunityId(homeCommunityId);
        if (isPolicyValid(request, assertion, hcId)) {
            log.debug("Adapter doc retrieve deferred policy check successful");
            response = sendDocRetrieveDeferredRequestToAgency(request, assertion);
        } else {
            log.debug("Adapter doc retrieve deferred policy check failed");
            response = createErrorResponse("Policy Check Failed on NHIN community : "+homeCommunityId);
        }
        log.debug("End DocRetrieveImpl.serviceDocRetrieveInternal");
        return response;
    }

    /**
     * 
     * @param request
     * @param assertion
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType sendDocRetrieveDeferredRequestToAgency(RetrieveDocumentSetRequestType request, AssertionType assertion) {
        log.debug("Begin DocRetrieveImpl.sendDocRetrieveToAgency");
        DocRetrieveAcknowledgementType response = null;
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        log.debug("Calling audit log for doc retrieve request sent to adapter");
        auditRequestMessage(request, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, assertion);

        log.debug("Creating adapter doc retrieve proxy");
        AdapterDocRetrieveDeferredReqProxy proxy = new AdapterDocRetrieveDeferredReqObjectFactory().getDocumentDeferredRequestProxy();
        gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType body = new gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
        body.setRetrieveDocumentSetRequest(request);
        log.debug("Sending adapter doc retrieve to adapter");
        response = proxy.sendToAdapter(body, assertion);

        log.debug("Calling audit log for doc retrieve response received from adapter");
        auditLog.auditDocRetrieveDeferredAckResponse(response.getMessage(), assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        log.debug("End DocRetrieveImpl.sendDocRetrieveToAgency");
        return response;
    }

    /**
     * 
     * @param request
     * @param direction
     * @param connectInterface
     * @param assertion
     */
    private void auditRequestMessage(RetrieveDocumentSetRequestType request, String direction, String connectInterface, AssertionType assertion) {
        gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType message = new gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType();
        message.setRetrieveDocumentSetRequest(request);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieve(message, direction, connectInterface);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }

    /**
     * 
     * @param auditLogMsg
     * @param assertion
     * @return AcknowledgementType
     */
    private AcknowledgementType auditMessage(LogEventRequestType auditLogMsg, AssertionType assertion) {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy.auditLog(auditLogMsg, assertion);
    }

    /**
     * 
     * @param homeCommunityId
     * @return RetrieveDocumentSetResponseType
     */
    private DocRetrieveAcknowledgementType createEmptyResponse(String homeCommunityId) {
        DocRetrieveAcknowledgementType response = new DocRetrieveAcknowledgementType();
        RegistryResponseType responseType = new RegistryResponseType();
        response.setMessage(responseType);
        responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success");
        responseType.setRequestId(homeCommunityId);
        return response;
    }

    /**
     *
     * @return boolean
     */
    private boolean isServiceEnabled() {
        boolean serviceEnabled = false;
        try {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_RETRIEVE_DEFERRED_REQUEST_SERVICE_KEY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_RETRIEVE_DEFERRED_REQUEST_SERVICE_KEY + " from property file " + NhincConstants.GATEWAY_PROPERTY_FILE + ": " + ex.getMessage(), ex);
        }

        return serviceEnabled;
    }

    /**
     *
     * @param oEachNhinRequest
     * @param oAssertion
     * @param targetCommunity
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

    /**
     * 
     * @return boolean
     */
    private boolean isInPassThroughMode() {
        boolean passThroughModeEnabled = false;
        try {
            passThroughModeEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_RETRIEVE_DEFERRED_REQUEST_SERVICE_PASSTHRU_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_RETRIEVE_DEFERRED_REQUEST_SERVICE_PASSTHRU_PROPERTY + " from property file " + NhincConstants.GATEWAY_PROPERTY_FILE + ": " + ex.getMessage(), ex);
        }
        return passThroughModeEnabled;
    }

    /**
     * 
     * @param codeContext
     * @return RetrieveDocumentSetResponseType
     */
    private DocRetrieveAcknowledgementType createErrorResponse(String codeContext) {
        DocRetrieveAcknowledgementType response = new DocRetrieveAcknowledgementType();
        RegistryResponseType responseType = new RegistryResponseType();
        response.setMessage(responseType);
        responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        RegistryErrorList regErrList = new RegistryErrorList();
        responseType.setRegistryErrorList(regErrList);
        RegistryError regErr = new RegistryError();
        regErrList.getRegistryError().add(regErr);
        regErr.setCodeContext(codeContext);
        regErr.setErrorCode("XDSRepositoryError");
        regErr.setSeverity("Error");
        return response;
    }
    
}
