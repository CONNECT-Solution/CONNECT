/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.nhin.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.DocQueryPolicyChecker;
import gov.hhs.fha.nhinc.docquery.adapter.deferred.request.error.proxy.AdapterDocQueryDeferredRequestErrorProxy;
import gov.hhs.fha.nhinc.docquery.adapter.deferred.request.error.proxy.AdapterDocQueryDeferredRequestErrorProxyObjectFactory;
import gov.hhs.fha.nhinc.docquery.adapter.deferred.request.proxy.AdapterDocQueryDeferredRequestProxy;
import gov.hhs.fha.nhinc.docquery.adapter.deferred.request.proxy.AdapterDocQueryDeferredRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class NhinDocQueryDeferredRequestOrchImpl {

    private static Log log = LogFactory.getLog(NhinDocQueryDeferredRequestOrchImpl.class);

    /**
     *
     * @param msg
     * @param assertion
     * @return <code>DocQueryAcknowledgementType</code>
     */
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion) {
        String ackMsg = null;
        DocQueryAcknowledgementType respAck = new DocQueryAcknowledgementType();

        String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();
        String requestCommunityID = null;
        if (msg != null) {
            requestCommunityID = HomeCommunityMap.getCommunityIdForDeferredQDRequest(msg.getAdhocQuery());
        }
        // Audit the incoming NHIN Message
        AcknowledgementType ack = auditRequest(msg, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, requestCommunityID);

        // Check if the service is enabled
        if (isServiceEnabled()) {

            // Check if in Pass-Through Mode
            if (!(isInPassThroughMode())) {

                // Perform the inbound policy check
                if (isPolicyValid(msg, assertion)) {
                    respAck = sendToAgency(msg, assertion, homeCommunityId);
                } else {
                    ackMsg = "Policy Check Failed for incoming Document Query Deferred Request";
                    log.error(ackMsg);
                    respAck = sendToAgencyError(msg, assertion, ackMsg, homeCommunityId);
                }
            } else {
                // Send the deferred request to the Adapter Interface
                respAck = sendToAgency(msg, assertion, homeCommunityId);
            }
        } else {
            // Send the error to the Adapter Error Interface
            ackMsg = "Document Query Deferred Request Service Not Enabled";
            log.error(ackMsg);
            respAck = sendToAgencyError(msg, assertion, ackMsg, homeCommunityId);
        }

        // Audit the outgoing NHIN Message
        ack = auditAck(respAck, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, requestCommunityID);

        return respAck;
    }

    private boolean isServiceEnabled() {
        boolean serviceEnabled = false;

        try {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        return serviceEnabled;
    }

    private boolean isInPassThroughMode() {
        boolean passThroughModeEnabled = false;
        try {
            passThroughModeEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_PASSTHRU_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_PASSTHRU_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return passThroughModeEnabled;
    }

    private boolean isPolicyValid(AdhocQueryRequest message, AssertionType assertion) {
        boolean policyIsValid = new DocQueryPolicyChecker().checkIncomingPolicy(message, assertion);

        return policyIsValid;
    }

    private DocQueryAcknowledgementType sendToAgencyError(AdhocQueryRequest request, AssertionType assertion, String errMsg, String requestCommunityID) {
        log.debug("Sending Request to Adapter Error Interface");

        // Audit the Adapter Request
        AcknowledgementType ack = auditRequest(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, requestCommunityID);

        AdapterDocQueryDeferredRequestErrorProxyObjectFactory factory = new AdapterDocQueryDeferredRequestErrorProxyObjectFactory();
        AdapterDocQueryDeferredRequestErrorProxy proxy = factory.getAdapterDocQueryDeferredRequestErrorProxy();
        DocQueryAcknowledgementType ackResp = proxy.respondingGatewayCrossGatewayQuery(request, assertion, errMsg);

        // Audit the incoming Adapter Message
        ack = auditAck(ackResp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, requestCommunityID);

        return ackResp;
    }

    private DocQueryAcknowledgementType sendToAgency(AdhocQueryRequest request, AssertionType assertion, String requestCommunityID) {
        log.debug("Sending Request to Adapter Interface");

        // Audit the Adapter Request
        AcknowledgementType ack = auditRequest(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, requestCommunityID);

        AdapterDocQueryDeferredRequestProxyObjectFactory factory = new AdapterDocQueryDeferredRequestProxyObjectFactory();
        AdapterDocQueryDeferredRequestProxy proxy = factory.getAdapterDocQueryDeferredRequestProxy();

        DocQueryAcknowledgementType ackResp = proxy.respondingGatewayCrossGatewayQuery(request, assertion);

        // Audit the incoming Adapter Message
        ack = auditAck(ackResp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, requestCommunityID);

        return ackResp;
    }

    private AcknowledgementType auditRequest(AdhocQueryRequest msg, AssertionType assertion, String direction, String _interface, String requestCommunityID) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.auditDQRequest(msg, assertion, direction, _interface, requestCommunityID);

        return ack;
    }

    private AcknowledgementType auditAck(DocQueryAcknowledgementType msg, AssertionType assertion, String direction, String _interface, String requestCommunityID) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.logDocQueryAck(msg, assertion, direction, _interface, requestCommunityID);

        return ack;
    }
}
