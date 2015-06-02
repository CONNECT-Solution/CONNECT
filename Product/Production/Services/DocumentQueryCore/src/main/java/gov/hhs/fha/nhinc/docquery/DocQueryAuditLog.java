/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docquery;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.AdhocQueryMessageType;
import gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.log4j.Logger;

/**
 *
 * @author jhoppesc
 */
public class DocQueryAuditLog {

    private static final Logger LOG = Logger.getLogger(DocQueryAuditLog.class);

    AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
    AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();


    /**
     * This method will log Audit Query Requests received on the NHIN Proxy Interface.
     *
     * @param msg The Audit Query Request message to be audit logged.
     * @param assertion Assertion received.
     * @param direction The direction this message going inbound or outbound.
     * @param msgInterface The interface can be Entity,MsgProxy or Adapter.
     * @return An acknowledgment of whether or not the message was successfully logged.
     */
    public AcknowledgementType auditDQResponse(AdhocQueryResponse msg, AssertionType assertion, String direction,
            String msgInterface) {
        LOG.debug("Entering DocQueryAuditLog.auditDQResponse()...");
        return auditDQResponse(msg, assertion, direction, msgInterface, null);
    }

    /**
     * This method will log Audit Query Requests received on the NHIN Proxy Interface.
     *
     * @param msg The Audit Query Request message to be audit logged.
     * @param assertion Assertion received.
     * @param direction The direction the message is going inbound or outbound.
     * @param msgInterface The interface can be Entity,MsgProxy or Adapter.
     * @param requestCommunityID communityID of the request passed in.
     * @return An acknowledgment of whether or not the message was successfully logged.
     */
    public AcknowledgementType auditDQResponse(AdhocQueryResponse msg, AssertionType assertion, String direction,
            String msgInterface, String requestCommunityID) {
        LOG.debug("Entering DocQueryAuditLog.auditDQResponse()...");

        AdhocQueryResponseMessageType auditMsg = new AdhocQueryResponseMessageType();
        auditMsg.setAssertion(assertion);
        auditMsg.setAdhocQueryResponse(msg);

        AcknowledgementType ack = logDocQueryResponse(auditMsg, direction, msgInterface, requestCommunityID);

        LOG.debug("Exiting DocQueryAuditLog.auditDQResponse()...");
        return ack;
    }


    /**
     * This method will log Audit Query Requests received on the NHIN Proxy Interface.
     *
     * @param msg The Audit Query Request message to be audit logged.
     * @param assertion Assertion received.
     * @param direction The direction the message is going inbound or outbound.
     * @param msgInterface The interface can be Entity,MsgProxy or Adapter.
     * @return An acknowledgment of whether or not the message was successfully logged.
     */
    public AcknowledgementType auditDQRequest(AdhocQueryRequest msg, AssertionType assertion, String direction,
            String msgInterface) {
        LOG.debug("Entering DocQueryAuditLog.auditDQRequest()...");
        return auditDQRequest(msg, assertion, direction, msgInterface, null);
    }

    /**
     * This method will log Audit Query Requests received on the NHIN Proxy Interface.
     *
     * @param msg The Audit Query Request message to be audit logged.
     * @param assertion Assertion received.
     * @param responseCommunityId communityID of the response.
     * @param direction The direction the message is going inbound or outbound.
     * @param msgInterface The interface can be Entity,MsgProxy or Adapter.
     * @return An acknowledgment of whether or not the message was successfully logged.
     */
    public AcknowledgementType auditDQRequest(AdhocQueryRequest msg, AssertionType assertion, String direction,
            String msgInterface, String responseCommunityId) {
        LOG.debug("Entering DocQueryAuditLog.auditDQRequest()...");

        AdhocQueryMessageType auditMsg = new AdhocQueryMessageType();
        auditMsg.setAssertion(assertion);
        auditMsg.setAdhocQueryRequest(msg);

        AcknowledgementType ack = logDocQueryRequest(auditMsg, direction, msgInterface, responseCommunityId);

        LOG.debug("Exiting DocQueryAuditLog.auditDQRequest()...");
        return ack;
    }

    /**
     * This method will log Document Query Requests received/sent on a particular public interface.
     *
     * @param message The Document Query Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param msgInterface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @param responseCommunityId Target response community id
     * @return An acknowledgment of whether or not the message was successfully logged.
     */
    private AcknowledgementType logDocQueryRequest(AdhocQueryMessageType message, String direction, String msgInterface,
            String responseCommunityId) {
        LOG.debug("Entering DocQueryAuditLog.logDocQuery(...)...");
        LogEventRequestType auditLogMsg = auditLogger
                .logAdhocQuery(message, direction, msgInterface, responseCommunityId);

        AcknowledgementType ack = auditLog(message.getAssertion(), auditLogMsg);
        LOG.debug("Exiting DocQueryAuditLog.logDocQuery(...)...");
        return ack;
    }

    /**
     * This method will log Document Query Responses received/sent on a particular public interface.
     *
     * @param message The Document Query Response message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param msgInterface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return An acknowledgment of whether or not the message was successfully logged.
     */
    public AcknowledgementType logDocQueryResponse(AdhocQueryResponseMessageType message, String direction,
            String msgInterface) {
        return this.logDocQueryResponse(message, direction, msgInterface, null);
    }

    /**
     * This method will log Document Query Responses received/sent on a particular public interface.
     *
     * @param message The Document Query Response message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param msgInterface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @param requestCommunityID requesting communityID.
     * @return An acknowledgment of whether or not the message was successfully logged.
     */
    public AcknowledgementType logDocQueryResponse(AdhocQueryResponseMessageType message, String direction,
            String msgInterface, String requestCommunityID) {
        LOG.debug("Entering DocQueryAuditLog.auditResponse(...)...");
        LogEventRequestType auditLogMsg = auditLogger.logAdhocQueryResult(message, direction, msgInterface,
                requestCommunityID);

        AcknowledgementType ack = auditLog(message.getAssertion(), auditLogMsg);

        LOG.debug("Exiting DocQueryAuditLog.auditResponse(...)...");
        return ack;
    }

    /**
     * This method will log Document Query Responses received/sent on a particular public interface.
     *
     * @param message The Document Query Response message to be audit logged.
     * @param assertion Assertion received.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param msgInterface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return An acknowledgment of whether or not the message was successfully logged.
     */
    public AcknowledgementType logDocQueryAck(DocQueryAcknowledgementType message, AssertionType assertion,
            String direction, String msgInterface) {
        return logDocQueryAck(message, assertion, direction, msgInterface, null);
    }

    /**
     * This method will log Document Query Responses received/sent on a particular public interface.
     *
     * @param message The Document Query Response message to be audit logged.
     * @param assertion Assertion received.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param msgInterface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @param requestCommunityID Target response community id
     * @return An acknowledgment of whether or not the message was successfully logged.
     */
    public AcknowledgementType logDocQueryAck(DocQueryAcknowledgementType message, AssertionType assertion,
            String direction, String msgInterface, String requestCommunityID) {
        LOG.debug("Entering DocQueryAuditLog.auditResponse(...)...");
        LogEventRequestType auditLogMsg = auditLogger.logAdhocQueryDeferredAck(message, assertion, direction,
                msgInterface, requestCommunityID);

        AcknowledgementType ack = auditLog(assertion, auditLogMsg);

        LOG.debug("Exiting DocQueryAuditLog.auditResponse(...)...");
        return ack;
    }

    /**
     * Log the outbound doc query strategy request.
     *
     * @param request The AdhocQuery Request received.
     * @param assertion Assertion received.
     * @param requestCommunityID communityId passed.
     */
    public void auditOutboundDocQueryStrategyRequest(AdhocQueryRequest request, AssertionType assertion,
            String requestCommunityID) {
        AdhocQueryMessageType message = new AdhocQueryMessageType();
        message.setAdhocQueryRequest(request);
        message.setAssertion(assertion);
        LogEventRequestType auditLogMsg = auditLogger.logAdhocQuery(message,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                requestCommunityID);
        auditLog(assertion, auditLogMsg);

    }

    /**
     * Log the outbound doc query strategy response.
     *
     * @param response The AdhocQUery Response received.
     * @param assertion Assertion received.
     * @param requestCommunityID CommunityId passed.
     */
    public void auditOutboundDocQueryStrategyResponse(AdhocQueryResponse response, AssertionType assertion,
            String requestCommunityID) {
        AdhocQueryResponseMessageType message = new AdhocQueryResponseMessageType();
        message.setAdhocQueryResponse(response);
        message.setAssertion(assertion);
        LogEventRequestType auditLogMsg = auditLogger
                .logAdhocQueryResult(message, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                        NhincConstants.AUDIT_LOG_NHIN_INTERFACE, requestCommunityID);
        auditLog(assertion, auditLogMsg);
    }

    /**
     * @param assertion
     * @param auditLogMsg
     * @return
     */
    protected AcknowledgementType auditLog(AssertionType assertion, LogEventRequestType auditLogMsg) {
        AcknowledgementType ack = new AcknowledgementType();
         if (auditLogMsg != null) {
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            ack = proxy.auditLog(auditLogMsg, assertion);
        }
        return ack;
    }

}
