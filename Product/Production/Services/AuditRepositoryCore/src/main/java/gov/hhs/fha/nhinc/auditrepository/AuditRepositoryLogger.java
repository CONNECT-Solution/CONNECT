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
package gov.hhs.fha.nhinc.auditrepository;

import gov.hhs.fha.nhinc.common.auditlog.FindAuditEventsMessageType;
import gov.hhs.fha.nhinc.common.auditlog.FindAuditEventsResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogFindAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogFindAuditEventsResultRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.EntityCdcNotifyRequestMessageType;
import gov.hhs.fha.nhinc.common.hiemauditlog.EntityCdcSubscribeRequestMessageType;
import gov.hhs.fha.nhinc.common.hiemauditlog.EntityDocumentNotifyRequestMessageType;
import gov.hhs.fha.nhinc.common.hiemauditlog.EntityDocumentSubscribeRequestMessageType;
import gov.hhs.fha.nhinc.common.hiemauditlog.EntityNotifyResponseMessageType;
import gov.hhs.fha.nhinc.common.hiemauditlog.EntityUnsubscribeRequestMessageType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityCdcNotifyRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityCdcSubscribeRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityDocumentNotifyRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityDocumentSubscribeRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityNotifyResponseType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityUnsubscribeRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogNhinNotifyRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogNhinSubscribeRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogNhinUnsubscribeRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogSubscribeResponseType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogUnsubscribeResponseType;
import gov.hhs.fha.nhinc.common.hiemauditlog.SubscribeResponseMessageType;
import gov.hhs.fha.nhinc.common.hiemauditlog.UnsubscribeResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.UnsubscribeRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.audit.AdminDistTransforms;
import gov.hhs.fha.nhinc.transform.audit.FindAuditEventsTransforms;
import gov.hhs.fha.nhinc.transform.audit.NotifyTransforms;
import gov.hhs.fha.nhinc.transform.audit.SubscribeTransforms;
import gov.hhs.fha.nhinc.transform.audit.UnsubscribeTransforms;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

import org.apache.log4j.Logger;

/**
 *
 * @author Jon Hoppesch
 */
public class AuditRepositoryLogger implements AuditRepositoryDocumentRetrieveLogger {

    private static final Logger LOG = Logger.getLogger(AuditRepositoryLogger.class);
    private final AdminDistTransforms adAuditTransformer = new AdminDistTransforms();
    private NotifyTransforms transformLib = new NotifyTransforms();

    /**
     * Constructor code for the AuditRepositoryLogger. This instantiates the object.
     */
    public AuditRepositoryLogger() {
    }

    /**
     * This method will create the generic Audit Log Message from an audit query request.
     *
     * @param message The Audit Query Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logFindAuditEvents(FindAuditEventsMessageType message, String direction,
        String _interface) {
        LOG.debug("Entering AuditRepositoryLogger.logFindAuditEvents(...)");
        LogEventRequestType auditMsg = null;
        LogFindAuditEventsRequestType logReqMsg = new LogFindAuditEventsRequestType();
        logReqMsg.setMessage(message);
        logReqMsg.setDirection(direction);
        logReqMsg.setInterface(_interface);
        auditMsg = FindAuditEventsTransforms.transformFindAuditEventsReq2AuditMsg(logReqMsg);
        LOG.debug("Exiting AuditRepositoryLogger.logFindAuditEvents(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an audit query response.
     *
     * @param message The Audit Query Response message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logFindAuditEventsResult(FindAuditEventsResponseMessageType message, String direction,
        String _interface) {
        LOG.debug("Entering AuditRepositoryLogger.logFindAuditEventsResult(...)");
        LogEventRequestType auditMsg = null;
        LogFindAuditEventsResultRequestType logReqMsg = new LogFindAuditEventsResultRequestType();
        logReqMsg.setDirection(direction);
        logReqMsg.setInterface(_interface);
        logReqMsg.setMessage(message);
        LOG.warn("logFindAuditEventsResult method is not implemented");
        LOG.debug("Exiting AuditRepositoryLogger.logFindAuditEventsResult(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a Nhin Subscribe request.
     *
     * @param message The Nhin Subscribe Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinSubscribeRequest(SubscribeRequestType message, String direction, String _interface) {
        LOG.debug("Entering AuditRepositoryLogger.logNhinSubscribeRequest(...)");
        LogEventRequestType auditMsg = null;
        SubscribeTransforms transformLib = new SubscribeTransforms();
        LogNhinSubscribeRequestType logReqMsg = new LogNhinSubscribeRequestType();
        logReqMsg.setMessage(message);
        logReqMsg.setDirection(direction);
        logReqMsg.setInterface(_interface);

        auditMsg = transformLib.transformNhinSubscribeRequestToAuditMessage(logReqMsg);
        LOG.debug("Exiting AuditRepositoryLogger.logNhinSubscribeRequest(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a Nhin notify request.
     *
     * @param message The Nhin Notify Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinNotifyRequest(NotifyRequestType message, String direction, String _interface) {
        LOG.debug("Entering AuditRepositoryLogger.logNhinNotifyRequest(...)");
        LogEventRequestType auditMsg = null;
        LogNhinNotifyRequestType logReqMsg = new LogNhinNotifyRequestType();
        logReqMsg.setMessage(message);
        logReqMsg.setDirection(direction);
        logReqMsg.setInterface(_interface);

        auditMsg = transformLib.transformNhinNotifyRequestToAuditMessage(logReqMsg);
        LOG.debug("Exiting AuditRepositoryLogger.logNhinNotifyRequest(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a Nhin unsubscribe request.
     *
     * @param message The Nhin Unsubscribe Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinUnsubscribeRequest(UnsubscribeRequestType message, String direction,
        String _interface) {
        LOG.debug("Entering AuditRepositoryLogger.logNhinUnsubscribeRequest(...)");
        LogEventRequestType auditMsg = null;
        UnsubscribeTransforms transformLib = new UnsubscribeTransforms();
        LogNhinUnsubscribeRequestType logReqMsg = new LogNhinUnsubscribeRequestType();
        logReqMsg.setMessage(message);
        logReqMsg.setDirection(direction);
        logReqMsg.setInterface(_interface);
        auditMsg = transformLib.transformNhinUnsubscribeRequestToAuditMessage(logReqMsg);
        LOG.debug("Exiting AuditRepositoryLogger.logNhinUnsubscribeRequest(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an unsubscribe response.
     *
     * @param message The Unsubscribe Response message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logUnsubscribeResponse(UnsubscribeResponseMessageType message, String direction,
        String _interface) {
        LOG.debug("Entering AuditRepositoryLogger.logUnsubscribeResponse(...)");
        LogEventRequestType auditMsg = null;
        LogUnsubscribeResponseType logReqMsg = new LogUnsubscribeResponseType();
        logReqMsg.setDirection(direction);
        logReqMsg.setInterface(_interface);
        logReqMsg.setMessage(message);
        UnsubscribeTransforms transformLib = new UnsubscribeTransforms();
        auditMsg = transformLib.transformUnsubscribeResponseToGenericAudit(logReqMsg);
        LOG.debug("Exiting AuditRepositoryLogger.logUnsubscribeResponse(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a subscribe response.
     *
     * @param message The Subscribe Response message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logSubscribeResponse(SubscribeResponseMessageType message, String direction,
        String _interface) {
        LOG.debug("Entering AuditRepositoryLogger.logSubscribeResponse(...)");
        LogEventRequestType auditMsg = null;
        LogSubscribeResponseType logReqMsg = new LogSubscribeResponseType();
        logReqMsg.setDirection(direction);
        logReqMsg.setInterface(_interface);
        logReqMsg.setMessage(message);
        SubscribeTransforms transformLib = new SubscribeTransforms();
        auditMsg = transformLib.transformSubscribeResponseToAuditMessage(logReqMsg);
        LOG.debug("Exiting AuditRepositoryLogger.logSubscribeResponse(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity document subscribe request.
     *
     * @param message The Entity Document Subscribe Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityDocSubscribeRequest(EntityDocumentSubscribeRequestMessageType message,
        String direction, String _interface) {
        LOG.debug("Entering AuditRepositoryLogger.logEntityDocSubscribeRequest(...)");
        LogEventRequestType auditMsg = null;
        LogEntityDocumentSubscribeRequestType logReqMsg = new LogEntityDocumentSubscribeRequestType();
        logReqMsg.setDirection(direction);
        logReqMsg.setInterface(_interface);
        logReqMsg.setMessage(message);
        LOG.warn("logEntityDocSubscribeRequest method is not implemented");
        LOG.debug("Exiting AuditRepositoryLogger.logEntityDocSubscribeRequest(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity CDC subscribe request.
     *
     * @param message The Entity CDC Subscribe Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityCdcSubscribeRequest(EntityCdcSubscribeRequestMessageType message,
        String direction, String _interface) {
        LOG.debug("Entering AuditRepositoryLogger.logEntityCdcSubscribeRequest(...)");
        LogEventRequestType auditMsg = null;
        LogEntityCdcSubscribeRequestType logReqMsg = new LogEntityCdcSubscribeRequestType();
        logReqMsg.setDirection(direction);
        logReqMsg.setInterface(_interface);
        logReqMsg.setMessage(message);
        LOG.warn("logEntityCdcSubscribeRequest method is not implemented");
        LOG.debug("Exiting AuditRepositoryLogger.logEntityCdcSubscribeRequest(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity document notify request.
     *
     * @param message The Entity Document Notify Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityDocNotifyRequest(EntityDocumentNotifyRequestMessageType message,
        String direction, String _interface) {
        LOG.debug("Entering AuditRepositoryLogger.logEntityDocNotifyRequest(...)");
        LogEventRequestType auditMsg = null;
        LogEntityDocumentNotifyRequestType logReqMsg = new LogEntityDocumentNotifyRequestType();
        logReqMsg.setDirection(direction);
        logReqMsg.setInterface(_interface);
        logReqMsg.setMessage(message);
        LOG.warn("logEntityDocNotifyRequest method is not implemented");
        LOG.debug("Exiting AuditRepositoryLogger.logEntityDocNotifyRequest(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity CDC notify request.
     *
     * @param message The Entity CDC Notify Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityCdcNotifyRequest(EntityCdcNotifyRequestMessageType message, String direction,
        String _interface) {
        LOG.debug("Entering AuditRepositoryLogger.logEntityCdcNotifyRequest(...)");
        LogEventRequestType auditMsg = null;
        LogEntityCdcNotifyRequestType logReqMsg = new LogEntityCdcNotifyRequestType();
        logReqMsg.setDirection(direction);
        logReqMsg.setInterface(_interface);
        logReqMsg.setMessage(message);
        LOG.warn("logEntityCdcNotifyRequest method is not implemented");
        LOG.debug("Exiting AuditRepositoryLogger.logEntityCdcNotifyRequest(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity notify response.
     *
     * @param message The Entity Notify Response message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityNotifyResponse(EntityNotifyResponseMessageType message, String direction,
        String _interface) {
        LOG.debug("Entering AuditRepositoryLogger.logEntityNotifyResponse(...)");
        LogEventRequestType auditMsg = null;
        LogEntityNotifyResponseType logReqMsg = new LogEntityNotifyResponseType();
        logReqMsg.setDirection(direction);
        logReqMsg.setInterface(_interface);
        logReqMsg.setMessage(message);
        LOG.warn("logEntityNotifyRespRequest method is not implemented");
        LOG.debug("Exiting AuditRepositoryLogger.logEntityNotifyResponse(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity unsubscribe request.
     *
     * @param message The Entity Unsubscribe Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityUnsubscribeRequest(EntityUnsubscribeRequestMessageType message,
        String direction, String _interface) {
        LOG.debug("Entering AuditRepositoryLogger.logEntityUnsubscribeRequest(...)");
        LogEventRequestType auditMsg = null;
        LogEntityUnsubscribeRequestType logReqMsg = new LogEntityUnsubscribeRequestType();
        logReqMsg.setDirection(direction);
        logReqMsg.setInterface(_interface);
        logReqMsg.setMessage(message);

        LOG.warn("logEntityUnsubscribeRequest method is not implemented");
        LOG.debug("Exiting AuditRepositoryLogger.logEntityUnsubscribeRequest(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Entity Patient Discovery Response.
     *
     * @param message The Patient Discovery Response message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityAdminDist(
        gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType message,
        AssertionType assertion, String direction) {
        LOG.trace("Entering AuditRepositoryLogger.logEntityPatientDiscResp(...)");

        LogEventRequestType auditMsg = null;
        try {
            EDXLDistribution body = message.getEDXLDistribution();
            auditMsg = adAuditTransformer.transformEDXLDistributionRequestToAuditMsg(body, assertion, direction,
                NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        } catch (NullPointerException ex) {
            LOG.error("The Incoming Send Alert message was Null", ex);
        }

        LOG.trace("Exiting AuditRepositoryLogger.logEntityPatientDiscResp(...)");
        return auditMsg;
    }

    /**
     * Create a generic Audit Log for an Admin Distribution message.
     *
     * @param message The Admin Distribution message to be audited
     * @param assertion The assertion to be audited
     * @param target
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is sent from(NHIN, Adapter, or Entity)
     * @return
     */
    public LogEventRequestType logNhincAdminDist(EDXLDistribution message, AssertionType assertion,
        NhinTargetSystemType target, String direction, String _interface) {

        return adAuditTransformer.transformEDXLDistributionRequestToAuditMsg(message, assertion, target, direction, _interface);
    }

    /**
     * Create a generic Audit Log for an Admin Distribution message.
     *
     * @param message The Admin Distribution message to be audited
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is sent from(NHIN, Adapter, or Entity)
     * @return
     */
    public LogEventRequestType logNhincAdminDist(EDXLDistribution message, AssertionType assertion, String direction,
        String _interface) {

        return adAuditTransformer.transformEDXLDistributionRequestToAuditMsg(message, assertion, direction, _interface);
    }

}
