/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.docquery.nhin;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.DocQueryPolicyChecker;
import gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxy;
import gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;

/**
 *
 * @author jhoppesc
 */
public class NhinDocQueryOrchImpl {

    private Log log = null;

    /**
     * Default constructor creates log.
     */
    public NhinDocQueryOrchImpl() {
        log = createLogger();
    }

    /**
     * @return Log log
     */
    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    /** This method forwards DocQUeryRequest to Nhin.
     *
     * @param msg The AdhocQuery Request message received.
     * @param assertion Assertion received.
     * @return AdhocQuery Response.
     */
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion) {
        log.info("Begin - NhinDocQueryOrchImpl.respondingGatewayCrossGatewayQuery()");

        RespondingGatewayCrossGatewayQueryRequestType crossGatewayQueryRequest =
                                                      new RespondingGatewayCrossGatewayQueryRequestType();
        AdhocQueryResponse resp = new AdhocQueryResponse();
        crossGatewayQueryRequest.setAdhocQueryRequest(msg);
        crossGatewayQueryRequest.setAssertion(assertion);

        String requestCommunityID = null;
        if (msg != null) {
            requestCommunityID = HomeCommunityMap.getCommunityIdForDeferredQDRequest(msg.getAdhocQuery());
        }
        // Audit the incomming query
        auditAdhocQueryRequest(crossGatewayQueryRequest, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE, requestCommunityID);

        // AssignProcessFlag: 'true' = $GetPropertyOut.GetPropertyResponse/propacc:propertyValue
        // Get local home community id for adapter audit log
        String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();
        // Check to see if in adapter pass through mode for this service
        if (isInPassThroughMode()) {
            log.info("Passthrough mode is enabled, sending message to the Adapter");
            resp = forwardToAgency(crossGatewayQueryRequest, homeCommunityId);
        } else {
            resp = queryInternalDocRegistry(crossGatewayQueryRequest, homeCommunityId);
        }
        // AssignEmptyResponse
        resp.setTotalResultCount(NhincConstants.NHINC_ADHOC_QUERY_NO_RESULT_COUNT);
        resp.setStatus(NhincConstants.NHINC_ADHOC_QUERY_SUCCESS_RESPONSE);

        // create an audit record for the response
        auditAdhocQueryResponse(resp, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE, crossGatewayQueryRequest.getAssertion(), requestCommunityID);

        log.info("End - NhinDocQueryOrchImpl.respondingGatewayCrossGatewayQuery()");

        return resp;
    }

    /**
     * Creates an audit log for an AdhocQueryRequest.
     *
     * @param crossGatewayDocQueryRequest AdhocQueryRequest message to log
     * @param direction Indicates whether the message is going out or comming in
     * @param msgInterface Indicates which interface component is being logged??
     * @param requestCommunityId HomeCommunityId to be audit logged.
     * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
     */
    private AcknowledgementType auditAdhocQueryRequest(RespondingGatewayCrossGatewayQueryRequestType msg,
            String direction, String msgInterface, String requestCommunityID) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.auditDQRequest(msg.getAdhocQueryRequest(), msg.getAssertion(), direction,
                msgInterface, requestCommunityID);

        return ack;
    }

    /**
     * Creates an audit log for an AdhocQueryResponse.
     *
     * @param crossGatewayDocQueryResponse AdhocQueryResponse message to log
     * @param direction Indicates whether the message is going out or comming in
     * @param msgInterface Indicates which interface component is being logged??
     * @param requestCommunityID to be audit logged.
     * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
     */
    private AcknowledgementType auditAdhocQueryResponse(AdhocQueryResponse msg, String direction, String msgInterface,
            AssertionType assertion, String requestCommunityID) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger
                .auditDQResponse(msg, assertion, direction, msgInterface, requestCommunityID);

        return ack;
    }

    /**
     * Checks to see if the security policy will permit the query to be executed.
     *
     * @param message The AdhocQuery request message.
     * @return Returns true if the security policy permits the query; false if denied.
     */
    private boolean checkPolicy(RespondingGatewayCrossGatewayQueryRequestType message) {
        boolean policyIsValid = new DocQueryPolicyChecker().checkIncomingPolicy(message.getAdhocQueryRequest(),
                message.getAssertion());

        return policyIsValid;
    }

    /**
     * Checks to see if the query should be handled internally or passed through to an adapter.
     *
     * @return Returns true if the documentQueryPassthrough property of the gateway.properties file is true.
     */
    private boolean isInPassThroughMode() {
        boolean passThroughModeEnabled = false;
        try {
            passThroughModeEnabled = PropertyAccessor.getInstance()
                    .getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
                            NhincConstants.NHINC_DOCUMENT_QUERY_SERVICE_PASSTHRU_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_QUERY_SERVICE_PASSTHRU_PROPERTY
                    + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return passThroughModeEnabled;
    }

    /**
     * Forwards the AdhocQueryRequest to an agency's adapter doc query service.
     *
     * @param adhocQueryRequestMsg The AdhocQuery Request Message Received.
     * @param communityID The request communityId.
     * @return
     */
    private AdhocQueryResponse forwardToAgency(RespondingGatewayCrossGatewayQueryRequestType adhocQueryRequestMsg,
            String communityID) {
        AdhocQueryResponse resp = new AdhocQueryResponse();

        // Audit the Audit Log Query Request Message sent to the Adapter Interface
        auditAdhocQueryRequest(adhocQueryRequestMsg, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, communityID);

        gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType
        crossGatewayQueryEventsRequest = new gov.hhs.fha.nhinc.common.nhinccommonadapter.
                                                    RespondingGatewayCrossGatewayQueryRequestType();
        crossGatewayQueryEventsRequest.setAssertion(adhocQueryRequestMsg.getAssertion());
        crossGatewayQueryEventsRequest.setAdhocQueryRequest(adhocQueryRequestMsg.getAdhocQueryRequest());

        AdapterDocQueryProxyObjectFactory adapterFactory = new AdapterDocQueryProxyObjectFactory();
        AdapterDocQueryProxy adapterProxy = adapterFactory.getAdapterDocQueryProxy();
        resp = adapterProxy.respondingGatewayCrossGatewayQuery(adhocQueryRequestMsg.getAdhocQueryRequest(),
                adhocQueryRequestMsg.getAssertion());

        // Audit the Audit Log Query Request Message sent to the Adapter Interface
        auditAdhocQueryResponse(resp, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, adhocQueryRequestMsg.getAssertion(), communityID);

        return resp;
    }

    /**
     * Forwards the AdhocQueryRequest to this agency's adapter doc query service.
     *
     * @param adhocQueryRequestMsg AdhocQUery Request Message received.
     * @param requestCommunityID The request communityId.
     * @return
     */
    private AdhocQueryResponse queryInternalDocRegistry(
            RespondingGatewayCrossGatewayQueryRequestType adhocQueryRequestMsg, String requestCommunityID) {
        AdhocQueryResponse resp = new AdhocQueryResponse();
        RespondingGatewayCrossGatewayQueryRequestType request = new RespondingGatewayCrossGatewayQueryRequestType();
        request.setAssertion(adhocQueryRequestMsg.getAssertion());
        request.setAdhocQueryRequest(adhocQueryRequestMsg.getAdhocQueryRequest());
        // Check the policy engine to make sure processing can proceed
        if (checkPolicy(adhocQueryRequestMsg)) {
            resp = forwardToAgency(adhocQueryRequestMsg, requestCommunityID);
        }
        return resp;
    }
}
