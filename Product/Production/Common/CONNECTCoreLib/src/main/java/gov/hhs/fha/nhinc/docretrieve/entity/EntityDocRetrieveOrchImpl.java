/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveAuditLog;
import gov.hhs.fha.nhinc.gateway.aggregator.StartTransactionDocRetrieveRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocRetrieveAggregator;
import gov.hhs.fha.nhinc.docretrieve.nhin.proxy.NhinDocRetrieveProxy;
import gov.hhs.fha.nhinc.docretrieve.nhin.proxy.NhinDocRetrieveProxyObjectFactory;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.gateway.aggregator.GetAggResultsDocRetrieveRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.GetAggResultsDocRetrieveResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.gateway.aggregator.SetResponseMsgDocRetrieveRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;

/**
 *
 * @author dunnek
 */
public class EntityDocRetrieveOrchImpl {
    private static org.apache.commons.logging.Log log = null;
    private static final long SLEEP_MS = 1000;
    private static final long AGGREGATOR_TIMEOUT_MS = 40000;
    public EntityDocRetrieveOrchImpl()
    {
        log = createLogger();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body, AssertionType assertion)
    {
        RetrieveDocumentSetResponseType response = null;
        DocRetrieveAuditLog auditLog = new DocRetrieveAuditLog();
        DocRetrieveAggregator aggregator = new DocRetrieveAggregator();
        
        auditLog.auditDocRetrieveRequest(body, assertion);

        try {
            String transactionId = startTransaction(aggregator, body);
            sendRetrieveMessages(transactionId, body, assertion);
            response = retrieveDocRetrieveResults(aggregator, transactionId);
        } catch (Throwable t) {
            log.error("Error occured processing doc retrieve on entity interface: " + t.getMessage(), t);
            response = createErrorResponse("Fault encountered processing internal document retrieve");
        }

        // Audit log - response
        auditLog.auditResponse(response, assertion);

        log.debug("End EntityDocRetrieveSecuredImpl.respondingGatewayCrossGatewayRetrieve");
        return response;
 
    }

    private RetrieveDocumentSetResponseType retrieveDocRetrieveResults(DocRetrieveAggregator aggregator, String transactionId) {
        log.debug("Begin retrieveDocRetrieveResults");
        RetrieveDocumentSetResponseType response = null;
        boolean retrieveTimedOut = false;

        // Agggregator retrieve request message
        GetAggResultsDocRetrieveRequestType aggResultsRequest = new GetAggResultsDocRetrieveRequestType();
        aggResultsRequest.setTransactionId(transactionId);
        aggResultsRequest.setTimedOut(retrieveTimedOut);

        // Loop until responses are received
        long startTime = System.currentTimeMillis();
        while (response == null) {
            GetAggResultsDocRetrieveResponseType aggResultsResponse = aggregator.getAggResults(aggResultsRequest);
            String retrieveStatus = aggResultsResponse.getStatus();
            if (DocumentConstants.COMPLETE_TEXT.equals(retrieveStatus)) {
                response = aggResultsResponse.getRetrieveDocumentSetResponse();
            } else if (DocumentConstants.FAIL_TEXT.equals(retrieveStatus)) {
                log.error("Document retrieve aggregator reports failurt - returning error");
                response = createErrorResponse("Processing internal document retrieve");
            } else {
                retrieveTimedOut = retrieveTimedOut(startTime);
                if (retrieveTimedOut) {
                    aggResultsRequest.setTimedOut(retrieveTimedOut);
                    aggResultsResponse = aggregator.getAggResults(aggResultsRequest);
                    if (DocumentConstants.COMPLETE_TEXT.equals(retrieveStatus)) {
                        response = aggResultsResponse.getRetrieveDocumentSetResponse();
                    } else {
                        log.warn("Document retrieve aggregation timeout - returning error.");
                        response = createErrorResponse("Processing internal document retrieve - failure in timeout");
                    }
                } else {
                    try {
                        Thread.sleep(SLEEP_MS);
                    } catch (Throwable t) {
                    }
                }
            }
        }
        log.debug("End retrieveDocRetrieveResults");
        return response;
    }
    private boolean retrieveTimedOut(long startTime) {
        long timeout = startTime + AGGREGATOR_TIMEOUT_MS;
        return (timeout < System.currentTimeMillis());
    }
    protected NhinDocRetrieveProxy getNhincDocRetreive()
    {
        log.debug("Creating NHIN doc retrieve proxy");
             
        return new NhinDocRetrieveProxyObjectFactory().getNhinDocRetrieveProxy();
    }

    private void sendRetrieveMessages(String transactionId, RetrieveDocumentSetRequestType body, AssertionType assertion) {
        log.debug("Begin sendRetrieveMessages");

        for (DocumentRequest docRequest : body.getDocumentRequest()) {
            RespondingGatewayCrossGatewayRetrieveSecuredRequestType nhinDocRetrieveMsg = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
            // Set document request
            RetrieveDocumentSetRequestType nhinDocRequest = new RetrieveDocumentSetRequestType();
            nhinDocRetrieveMsg.setRetrieveDocumentSetRequest(nhinDocRequest);
            nhinDocRequest.getDocumentRequest().add(docRequest);
            nhinDocRetrieveMsg.setNhinTargetSystem(buildHomeCommunity(docRequest.getHomeCommunityId()));


            if (isPolicyValid(nhinDocRequest, assertion, nhinDocRetrieveMsg.getNhinTargetSystem().getHomeCommunity())) {
                // Create and start doc retrieve sender thread
                DocRetrieveSender docRetrieveSender = new DocRetrieveSender(transactionId, nhinDocRetrieveMsg, assertion);
                docRetrieveSender.sendMessage();
            } else {
                log.error("Fault encountered processing Policy Check");
            }
        }
        log.debug("End sendRetrieveMessages");
    }



    private String startTransaction(DocRetrieveAggregator aggregator, RetrieveDocumentSetRequestType body) {
        StartTransactionDocRetrieveRequestType docRetrieveStartTransaction = new StartTransactionDocRetrieveRequestType();
        docRetrieveStartTransaction.setRetrieveDocumentSetRequest(body);
        log.debug("Starting doc retrieve transaction");
        String transactionId = aggregator.startTransaction(docRetrieveStartTransaction);
        log.debug("Doc retrieve transaction id: " + transactionId);
        return transactionId;
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
        regErr.setErrorCode("XDSRegistryError");
        regErr.setSeverity("Error");
        return response;
    }
    private NhinTargetSystemType buildHomeCommunity(String homeCommunityId) {
        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(homeCommunityId);
        nhinTargetSystem.setHomeCommunity(homeCommunity);
        return nhinTargetSystem;
    }

    protected boolean isPolicyValid(RetrieveDocumentSetRequestType oEachNhinRequest, AssertionType oAssertion, HomeCommunityType targetCommunity) {
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

}
