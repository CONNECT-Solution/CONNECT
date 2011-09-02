/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.passthru.deferred.request.proxy.PassthruDocRetrieveDeferredReqProxyObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.passthru.deferred.request.proxy.PassthruDocRetrieveDeferredReqProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.document.DocRetrieveAckTranforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation class for Entity Document Retrieve Deferred request message
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqOrchImpl {

    private static final Log log = LogFactory.getLog(EntityDocRetrieveDeferredReqOrchImpl.class);

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType message, AssertionType assertion, NhinTargetCommunitiesType target) {

        log.debug("Begin EntityDocRetrieveDeferredRequestImpl.crossGatewayRetrieveRequest");

        DocRetrieveAcknowledgementType nhincResponse = new DocRetrieveAcknowledgementType();
        String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();
        String ackMsg = "";

        // Audit incoming entity deferred document request
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        auditLog.auditDocRetrieveDeferredRequest(message, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, assertion, homeCommunityId);

        try {
            // Log the start of the performance record
            Timestamp starttime = new Timestamp(System.currentTimeMillis());
            Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime, "Deferred"+NhincConstants.DOC_RETRIEVE_SERVICE_NAME, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, homeCommunityId);

            // instantiate NHIN doc retrieve proxy
            PassthruDocRetrieveDeferredReqProxyObjectFactory objFactory = new PassthruDocRetrieveDeferredReqProxyObjectFactory();
            PassthruDocRetrieveDeferredReqProxy docRetrieveProxy = objFactory.getNhincProxyDocRetrieveDeferredReqProxy();

            // Retrieve the list of doc retrieve request messages
            List<RespondingGatewayCrossGatewayRetrieveRequestType> retrieveRequestList = buildRetrieveRequestList(message, assertion);

            // Process each doc retrieve request message
            for (RespondingGatewayCrossGatewayRetrieveRequestType retrieveRequest : retrieveRequestList) {
                log.debug("----- Processing RespondingGatewayCrossGatewayRetrieveRequestType -----");

                // Create the target gateway system for the current doc retrieve request message
                NhinTargetSystemType oTargetSystem = buildHomeCommunity(retrieveRequest.getRetrieveDocumentSetRequest().getDocumentRequest().get(0).getHomeCommunityId());

                if (isPolicyValid(retrieveRequest.getRetrieveDocumentSetRequest(), retrieveRequest.getAssertion(), oTargetSystem.getHomeCommunity())) {
                    // Send the deferred doc retrieve request
                    nhincResponse = docRetrieveProxy.crossGatewayRetrieveRequest(retrieveRequest.getRetrieveDocumentSetRequest(), retrieveRequest.getAssertion(), oTargetSystem);
                } else {
                    ackMsg = "Policy Failed";

                    // Set the error acknowledgement status of the deferred queue entry
                    nhincResponse = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_RETRIEVE_DEFERRED_ACK_ERROR_AUTHORIZATION, ackMsg);
                }
            }

            // Log the end of the performance record
            Timestamp stoptime = new Timestamp(System.currentTimeMillis());
            PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);
        } catch (Exception e) {
            log.error("Exception processing Deferred Retrieve Documents: ", e);
            nhincResponse = DocRetrieveAckTranforms.createAckMessage(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG, NhincConstants.DOC_RETRIEVE_DEFERRED_ACK_ERROR_INVALID, e.getMessage());
        }

        // Audit final acknowledgement response
        auditLog.auditDocRetrieveDeferredAckResponse(nhincResponse.getMessage(), message, null, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, homeCommunityId);

        log.debug("End EntityDocRetrieveDeferredRequestImpl.crossGatewayRetrieveRequest");

        return nhincResponse;
    }

    /**
     * Build the list of doc retrieve request messages.  The process is controlled
     * by the gateway property deferredRetrieveDocumentsRequestProcess; valid values are
     * <ul>
     * <li>document - each document creates its own request and queue record</li>
     * <li>gateway  - all documents for a target community are sent in a single request and queue record</li>
     * </ul>
     * 
     * @param message Entity Retrieve Documents request
     * @param assertion from Entity Retrieve Documents request
     * @return
     */
    private List<RespondingGatewayCrossGatewayRetrieveRequestType> buildRetrieveRequestList(RetrieveDocumentSetRequestType message, AssertionType assertion) {
        log.debug("Begin EntityDocRetrieveDeferredRequestImpl.buildRetrieveRequestList");

        List<RespondingGatewayCrossGatewayRetrieveRequestType> retrieveRequestList = new ArrayList<RespondingGatewayCrossGatewayRetrieveRequestType>();
        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();

        String process = getDeferredRetrieveDocumentsRequestProcess();

        if (process != null && process.equalsIgnoreCase("document")) {
            // One request for each document
            for (DocumentRequest docRequest : message.getDocumentRequest()) {

                // Define a cross gateway deferred request for each document for logging
                RespondingGatewayCrossGatewayRetrieveRequestType nhinDocRetrieveMsg = new RespondingGatewayCrossGatewayRetrieveRequestType();
                // Set document request
                RetrieveDocumentSetRequestType nhinDocRequest = new RetrieveDocumentSetRequestType();
                nhinDocRequest.getDocumentRequest().add(docRequest);
                nhinDocRetrieveMsg.setRetrieveDocumentSetRequest(nhinDocRequest);
                
                AssertionType newAssertion = asyncProcess.copyAssertionTypeObject(assertion);
                nhinDocRetrieveMsg.setAssertion(newAssertion);

                retrieveRequestList.add(nhinDocRetrieveMsg);
            }
        } else {
            // Consolidate all documents for each target gateway; one request per target gateway
            HashMap<String, List<DocumentRequest>> requestMap = new HashMap<String, List<DocumentRequest>>();
            List<DocumentRequest> gatewayDocsRequestList = null;

            for (DocumentRequest docRequest : message.getDocumentRequest()) {
                String keyValue = docRequest.getHomeCommunityId();

                if (requestMap.containsKey(keyValue)) {
                    gatewayDocsRequestList = (List<DocumentRequest>)requestMap.get(keyValue);
                    gatewayDocsRequestList.add(docRequest);
                } else {
                    gatewayDocsRequestList = new ArrayList<DocumentRequest>();
                    gatewayDocsRequestList.add(docRequest);
                    requestMap.put(keyValue, gatewayDocsRequestList);
                }
            }

            if (!requestMap.isEmpty()) {
                for (Map.Entry<String,List<DocumentRequest>> entry : requestMap.entrySet()) {

                    gatewayDocsRequestList = entry.getValue();

                    // Define a cross gateway deferred request for each document for logging
                    RespondingGatewayCrossGatewayRetrieveRequestType nhinDocRetrieveMsg = new RespondingGatewayCrossGatewayRetrieveRequestType();
                    // Set document request
                    RetrieveDocumentSetRequestType nhinDocRequest = new RetrieveDocumentSetRequestType();
                    for (DocumentRequest docRequest : gatewayDocsRequestList) {
                        nhinDocRequest.getDocumentRequest().add(docRequest);
                    }
                    nhinDocRetrieveMsg.setRetrieveDocumentSetRequest(nhinDocRequest);
                    
                    AssertionType newAssertion = asyncProcess.copyAssertionTypeObject(assertion);
                    nhinDocRetrieveMsg.setAssertion(newAssertion);

                    retrieveRequestList.add(nhinDocRetrieveMsg);
                }
            }
        }

        log.debug("End EntityDocRetrieveDeferredRequestImpl.buildRetrieveRequestList");

        return retrieveRequestList;
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
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, oAssertion);
        /* if response='permit' */
        if (policyResp.getResponse().getResult().get(0).getDecision().value().equals(NhincConstants.POLICY_PERMIT)) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Return boolean performance monitor enabled indicator based on gateway property
     * @return
     */
    private static String getDeferredRetrieveDocumentsRequestProcess() {
        String process = "";
        try {
            // Use CONNECT utility class to access gateway.properties
            process = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, "deferredRetrieveDocumentsRequestProcess");

            if (process == null || process.equals("")) {
                process = "document";   // default is document
            }
        } catch (PropertyAccessException ex) {
            process = "document";   // default is document
            log.error("Error: Failed to retrieve deferredRetrieveDocumentsRequestProcess from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return process;
    }

}
