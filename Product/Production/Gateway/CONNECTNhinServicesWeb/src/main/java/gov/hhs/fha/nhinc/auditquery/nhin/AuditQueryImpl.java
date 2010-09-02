/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.auditquery.nhin;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import gov.hhs.fha.nhinc.adapterauditquery.proxy.AdapterAuditQueryProxy;
import gov.hhs.fha.nhinc.adapterauditquery.proxy.AdapterAuditQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.FindAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class AuditQueryImpl {

    private static Log log = LogFactory.getLog(AuditQueryImpl.class);

    /**
     * This method will perform an audit log query to the local audit repository.  A list of audit
     * log records will be returned to the calling community that match the search criteria.
     *
     * @param query The audit log query search criteria
     * @return A list of Audit Log records that match the specified criteria
     */
    public FindAuditEventsResponseType query(FindAuditEventsType query, WebServiceContext context) {
        log.debug("Entering AuditQueryImpl.query...");

        FindAuditEventsResponseType resp = new FindAuditEventsResponseType();

        // Set up the audit query request message
        FindAuditEventsRequestType request = new FindAuditEventsRequestType();
        request.setFindAuditEvents(query);
        request.setAssertion(SamlTokenExtractor.GetAssertion(context));

        // Audit the Audit Log Query Request Message received on the Nhin Interface
        AcknowledgementType ack = audit(request, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        // Check the policy engine to make sure processing can proceed
        if (checkPolicy(request)) {
            // Check if the Audit Log Query Service is enabled
            if (isServiceEnabled()) {
                // Check to see if in adapter pass through mode for this service
                if (isInPassThroughMode()) {
                    resp = forwardToAgency(request);
                } else {
                    resp = queryAuditRepo(request);
                }
            }
            else {
                log.warn("Audit Query Service is not enabled");
            }
        }
        else {
            log.error("Audit Query request failed policy check");
        }

        log.debug("Exiting AuditQueryImpl.query...");
        return resp;
    }

    private FindAuditEventsResponseType forwardToAgency(FindAuditEventsRequestType auditMsg) {
        FindAuditEventsResponseType resp = new FindAuditEventsResponseType();

        // Audit the Audit Log Query Request Message received on the Nhin Interface
        AcknowledgementType ack = audit(auditMsg, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        gov.hhs.fha.nhinc.common.nhinccommonadapter.FindAuditEventsRequestType findAuditEventsRequest = new gov.hhs.fha.nhinc.common.nhinccommonadapter.FindAuditEventsRequestType();
        findAuditEventsRequest.setAssertion(auditMsg.getAssertion());
        findAuditEventsRequest.setFindAuditEvents(auditMsg.getFindAuditEvents());

        AdapterAuditQueryProxyObjectFactory adapterFactory = new AdapterAuditQueryProxyObjectFactory();
        AdapterAuditQueryProxy adapterProxy = adapterFactory.getAdapterAuditQueryProxy();
        resp = adapterProxy.auditQuery(findAuditEventsRequest);

        return resp;
    }

    private FindAuditEventsResponseType queryAuditRepo(FindAuditEventsRequestType auditMsg) {
        FindAuditEventsResponseType resp = new FindAuditEventsResponseType();

        FindCommunitiesAndAuditEventsResponseType result = new FindCommunitiesAndAuditEventsResponseType();
        FindCommunitiesAndAuditEventsRequestType request = new FindCommunitiesAndAuditEventsRequestType();
        request.setAssertion(auditMsg.getAssertion());
        request.setFindAuditEvents(auditMsg.getFindAuditEvents());

        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy auditProxy = auditRepoFactory.getAuditRepositoryProxy();
        result = auditProxy.auditQuery(request);
        resp = result.getFindAuditEventResponse();

        return resp;
    }

    private AcknowledgementType audit(FindAuditEventsRequestType auditMsg, String direction, String _interface) {
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

        // Need to resolve the namespace issues here because this type is defined in multiple schemas
        gov.hhs.fha.nhinc.common.auditlog.FindAuditEventsMessageType message = new gov.hhs.fha.nhinc.common.auditlog.FindAuditEventsMessageType();
        message.setAssertion(auditMsg.getAssertion());
        message.setFindAuditEvents(auditMsg.getFindAuditEvents());

        LogEventRequestType auditLogMsg = auditLogger.logFindAuditEvents(message, direction, _interface);

        if (auditLogMsg != null) {
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            ack = proxy.auditLog(auditLogMsg, auditMsg.getAssertion());
        }

        return ack;
    }

    private boolean checkPolicy(FindAuditEventsRequestType message) {
        boolean policyIsValid = false;

        AssertionType assertion = null;
        FindAuditEventsEventType policyCheckReq = new FindAuditEventsEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsMessageType request = new gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsMessageType();
        if(message != null)
        {
            assertion = message.getAssertion();
            request.setAssertion(assertion);
            request.setFindAuditEvents(message.getFindAuditEvents());
        }
        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyFindAuditEvents(policyCheckReq);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        return policyIsValid;
    }

    private boolean isServiceEnabled() {
        boolean serviceEnabled = false;
        try {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.AUDIT_LOG_SERVICE_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.AUDIT_LOG_SERVICE_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        return serviceEnabled;
    }

    private boolean isInPassThroughMode() {
        boolean passThroughModeEnabled = false;
        try {
            passThroughModeEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.AUDIT_LOG_SERVICE_PASSTHRU_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.AUDIT_LOG_SERVICE_PASSTHRU_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        return passThroughModeEnabled;
    }
}
