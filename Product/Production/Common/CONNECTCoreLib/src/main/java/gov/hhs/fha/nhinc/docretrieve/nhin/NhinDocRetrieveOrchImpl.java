/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.adapter.proxy.AdapterDocRetrieveProxy;
import gov.hhs.fha.nhinc.docretrieve.adapter.proxy.AdapterDocRetrieveProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.sql.Timestamp;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;

/**
 * The business logic for orchestrating a doc retrieve message from the nhin.
 *
 * @author westberg
 */
public class NhinDocRetrieveOrchImpl {

    private static Log log = LogFactory.getLog(NhinDocRetrieveOrchImpl.class);

    /**
     *
     * @param body
     * @param assertion
     * @return <code>RetrieveDocumentSetResponseType</code>
     */
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body, AssertionType assertion) {
        log.debug("Entering NhinDocRetrieveOrchImpl.respondingGatewayCrossGatewayRetrieve");

        RetrieveDocumentSetResponseType response = null;
        String requestCommunityID = HomeCommunityMap.getCommunityIdForRDRequest(body);
        log.debug("Calling audit on doc retrieve request received from NHIN");
        auditRequestMessage(body, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, assertion, requestCommunityID);

        // Log the start of the nhin performance record
        Timestamp starttime = new Timestamp(System.currentTimeMillis());
        Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime, NhincConstants.DOC_RETRIEVE_SERVICE_NAME, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, requestCommunityID);

        try {
            String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();
            if (isServiceEnabled()) {
                log.debug("Doc retrieve service is enabled. Procesing message");

                if (isInPassThroughMode()) {
                    log.debug("In passthrough mode. Sending adapter doc retrieve directly to adapter");
                    response = sendDocRetrieveToAgency(body, assertion, homeCommunityId);
                } else {
                    log.debug("Not in passthrough mode. Calling internal processing for adapter doc retrieve");
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

        // Log the end of the nhin performance record
        Timestamp stoptime = new Timestamp(System.currentTimeMillis());
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);

        log.debug("Calling audit on doc retrieve response returned to NHIN");
        auditResponseMessage(response, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, assertion, requestCommunityID);

        log.debug("Exiting NhinDocRetrieveOrchImpl.respondingGatewayCrossGatewayRetrieve");
        return response;
    }

    private RetrieveDocumentSetResponseType serviceDocRetrieveInternal(RetrieveDocumentSetRequestType request, AssertionType assertion, String homeCommunityId) {
        log.debug("Begin DocRetrieveImpl.serviceDocRetrieveInternal");
        RetrieveDocumentSetResponseType response = null;

        if (checkPolicy(request, assertion)) {
            log.debug("Adapter doc query policy check successful");
            response = sendDocRetrieveToAgency(request, assertion, homeCommunityId);
        } else {
            log.debug("Adapter doc query policy check failed");
            response = createEmptyResponse(homeCommunityId);
        }

        log.debug("End DocRetrieveImpl.serviceDocRetrieveInternal");
        return response;
    }

    private RetrieveDocumentSetResponseType sendDocRetrieveToAgency(RetrieveDocumentSetRequestType request, AssertionType assertion, String communityID) {
        log.debug("Begin DocRetrieveImpl.sendDocRetrieveToAgency");
        RetrieveDocumentSetResponseType response = null;

        log.debug("Calling audit log for doc retrieve request sent to adapter");
        auditRequestMessage(request, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, assertion, communityID);

        log.debug("Creating adapter doc retrieve proxy");
        AdapterDocRetrieveProxy proxy = new AdapterDocRetrieveProxyObjectFactory().getAdapterDocRetrieveProxy();
        log.debug("Sending adapter doc retrieve to adapter");
        response = proxy.retrieveDocumentSet(request, assertion);

        log.debug("Calling audit log for doc retrieve response received from adapter");
        auditResponseMessage(response, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, assertion, communityID);

        log.debug("End DocRetrieveImpl.sendDocRetrieveToAgency");
        return response;
    }

    private void auditRequestMessage(RetrieveDocumentSetRequestType request, String direction, String connectInterface, AssertionType assertion, String requestCommunityID) {
        gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType message = new gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType();
        message.setRetrieveDocumentSetRequest(request);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieve(message, direction, connectInterface, requestCommunityID);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }

    private void auditResponseMessage(RetrieveDocumentSetResponseType response, String direction, String connectInterface, AssertionType assertion, String requestCommunityID) {
        gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType message = new gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType();
        message.setRetrieveDocumentSetResponse(response);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieveResult(message, direction, connectInterface, requestCommunityID);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
    }

    private AcknowledgementType auditMessage(LogEventRequestType auditLogMsg, AssertionType assertion) {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy.auditLog(auditLogMsg, assertion);
    }

    private boolean isServiceEnabled() {
        boolean serviceEnabled = false;
        try {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_RETRIEVE_SERVICE_KEY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_RETRIEVE_SERVICE_KEY + " from property file " + NhincConstants.GATEWAY_PROPERTY_FILE + ": " + ex.getMessage(), ex);
        }

        return serviceEnabled;
    }

    private boolean isInPassThroughMode() {
        boolean passThroughModeEnabled = false;
        try {
            passThroughModeEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_RETRIEVE_SERVICE_PASSTHRU_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_RETRIEVE_SERVICE_PASSTHRU_PROPERTY + " from property file " + NhincConstants.GATEWAY_PROPERTY_FILE + ": " + ex.getMessage(), ex);
        }
        return passThroughModeEnabled;
    }

    private boolean checkPolicy(RetrieveDocumentSetRequestType message, AssertionType assertion) {
        boolean policyIsValid = false;

        //convert the request message to an object recognized by the policy engine
        DocRetrieveEventType policyCheckReq = new DocRetrieveEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType request = new gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType();
        request.setAssertion(assertion);
        request.setRetrieveDocumentSetRequest(message);
        policyCheckReq.setMessage(request);

        //call the policy engine to check the permission on the request
        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyDocRetrieve(policyCheckReq);
        policyReq.setAssertion(assertion);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        //check the policy engine's response, return true if response = permit
        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        return policyIsValid;
    }

    private RetrieveDocumentSetResponseType createEmptyResponse(String homeCommunityId) {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        RegistryResponseType responseType = new RegistryResponseType();
        response.setRegistryResponse(responseType);
        responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success");
        RetrieveDocumentSetResponseType.DocumentResponse docResponse = new RetrieveDocumentSetResponseType.DocumentResponse();
        docResponse.setHomeCommunityId(homeCommunityId);
        response.getDocumentResponse().add(docResponse);
        return response;
    }

    private RetrieveDocumentSetResponseType createErrorResponse(String codeContext) {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        RegistryResponseType responseType = new RegistryResponseType();
        response.setRegistryResponse(responseType);
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
