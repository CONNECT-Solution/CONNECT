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
package gov.hhs.fha.nhinc.auditrepository;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

import gov.hhs.fha.nhinc.common.auditlog.AdhocQueryMessageType;
import gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.FindAuditEventsMessageType;
import gov.hhs.fha.nhinc.common.auditlog.FindAuditEventsResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogAdhocQueryRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogAdhocQueryResultRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogDocRetrieveRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogDocRetrieveResultRequestType;
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
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.UnsubscribeRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.audit.AdminDistTransforms;
import gov.hhs.fha.nhinc.transform.audit.DocumentQueryTransforms;
import gov.hhs.fha.nhinc.transform.audit.DocumentRetrieveTransforms;
import gov.hhs.fha.nhinc.transform.audit.FindAuditEventsTransforms;
import gov.hhs.fha.nhinc.transform.audit.NotifyTransforms;
import gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms;
import gov.hhs.fha.nhinc.transform.audit.SubscribeTransforms;
import gov.hhs.fha.nhinc.transform.audit.UnsubscribeTransforms;
import gov.hhs.fha.nhinc.transform.audit.XDRTransforms;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;

/**
 * 
 * @author Jon Hoppesch
 */
public class AuditRepositoryLogger {

    private static Log log = LogFactory.getLog(AuditRepositoryLogger.class);
    private final PatientDiscoveryTransforms pdAuditTransformer = new PatientDiscoveryTransforms();
    private final XDRTransforms xdrAuditTransformer = new XDRTransforms();
    private final AdminDistTransforms adAuditTransformer = new AdminDistTransforms();
    private final DocumentQueryTransforms dqAuditTransforms = new DocumentQueryTransforms();
    private NotifyTransforms transformLib = new NotifyTransforms();

    /**
     * Constructor code for the AuditRepositoryLogger. This instantiates the object.
     */
    public AuditRepositoryLogger() {
    }

    /**
     * This method will create the generic Audit Log Message from a NHIN Patient Discovery Request.
     * 
     * @param message The Patient Discovery Request message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinPatientDiscAck(MCCIIN000002UV01 message, AssertionType assertion,
            String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logNhinPatientDiscReq(...)");
        LogEventRequestType auditMsg = null;
            auditMsg = pdAuditTransformer.transformAck2AuditMsg(message, assertion, direction, _interface);
        log.debug("Exiting AuditRepositoryLogger.logNhinPatientDiscReq(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a NHIN Patient Discovery Request.
     * 
     * @param message The Patient Discovery Request message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _type The type of service (Synchronous or Deferred)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinPatientDiscReq(PRPAIN201305UV02 message, AssertionType assertion,
            String direction, String _type) {
        log.debug("Entering AuditRepositoryLogger.logNhinPatientDiscReq(...)");
        LogEventRequestType auditMsg = null;
            auditMsg = pdAuditTransformer.transformNhinPRPAIN201305RequestToAuditMsg(message, assertion, direction,
                    NhincConstants.AUDIT_LOG_NHIN_INTERFACE, _type, null);
        log.debug("Exiting AuditRepositoryLogger.logNhinPatientDiscReq(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a NHIN Patient Discovery Response.
     * 
     * @param message The Patient Discovery Response message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _type The type of service (Synchronous or Deferred)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinPatientDiscResp(PRPAIN201306UV02 message, AssertionType assertion,
            String direction, String _type) {
        log.debug("Entering AuditRepositoryLogger.logNhinPatientDiscResp(...)");
        LogEventRequestType auditMsg = null;
            auditMsg = pdAuditTransformer.transformNhinPRPAIN201306ResponseToAuditMsg(message, assertion, direction,
                    NhincConstants.AUDIT_LOG_NHIN_INTERFACE, _type);
        log.debug("Exiting AuditRepositoryLogger.logNhinPatientDiscResp(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Adapter Patient Discovery Request.
     * 
     * @param message The Patient Discovery Request message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _type The type of service (Synchronous or Deferred)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdapterPatientDiscReq(PRPAIN201305UV02 message, AssertionType assertion,
            String direction, String _type) {
        log.debug("Entering AuditRepositoryLogger.logAdapterPatientDiscReq(...)");
        LogEventRequestType auditMsg = null;
            auditMsg = pdAuditTransformer.transformAdapterPRPAIN201305RequestToAuditMsg(message, assertion, direction,
                    NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, _type, null);
        log.debug("Exiting AuditRepositoryLogger.logAdapterPatientDiscReq(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Adapter Patient Discovery Response.
     * 
     * @param message The Patient Discovery Response message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _type The type of service (Synchronous or Deferred)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdapterPatientDiscResp(PRPAIN201306UV02 message, AssertionType assertion,
            String direction, String _type) {
        log.debug("Entering AuditRepositoryLogger.logAdapterPatientDiscResp(...)");
        LogEventRequestType auditMsg = null;
            auditMsg = pdAuditTransformer.transformNhinPRPAIN201306ResponseToAuditMsg(message, assertion, direction,
                    NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, _type);
        log.debug("Exiting AuditRepositoryLogger.logAdapterPatientDiscResp(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Entity Patient Discovery Request.
     * 
     * @param message The Patient Discovery Request message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _type The type of service (Synchronous or Deferred)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityPatientDiscReq(RespondingGatewayPRPAIN201305UV02RequestType message,
            AssertionType assertion, String direction, String _type, String _process) {
        log.debug("Entering AuditRepositoryLogger.logEntityPatientDiscReq(...)");
        LogEventRequestType auditMsg = null;
            auditMsg = pdAuditTransformer.transformEntityPRPAIN201305RequestToAuditMsg(message, assertion, direction,
                    NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, _type, _process);
        log.debug("Exiting AuditRepositoryLogger.logEntityPatientDiscReq(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Entity Patient Discovery Response.
     * 
     * @param message The Patient Discovery Response message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _type The type of service (Synchronous or Deferred)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityPatientDiscResp(RespondingGatewayPRPAIN201306UV02ResponseType message,
            AssertionType assertion, String direction, String _type) {
        log.debug("Entering AuditRepositoryLogger.logEntityPatientDiscResp(...)");
        LogEventRequestType auditMsg = null;
            auditMsg = pdAuditTransformer.transformEntityPRPAIN201306ResponseToAuditMsg(message, assertion, direction,
                    NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, _type);
        log.debug("Exiting AuditRepositoryLogger.logEntityPatientDiscResp(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Entity Patient Discovery Async Response.
     * 
     * @param message The Patient Discovery Async Response message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _type The type of service (Synchronous or Deferred)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityPatientDiscAsyncResp(RespondingGatewayPRPAIN201306UV02RequestType message,
            AssertionType assertion, String direction, String _type) {
        log.debug("Entering AuditRepositoryLogger.logEntityPatientDiscAsyncResp(...)");
        LogEventRequestType auditMsg = null;
        auditMsg = pdAuditTransformer.transformEntityPRPAIN201306AsyncResponseToAuditMsg(message, assertion, direction,
                NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, _type);
        log.debug("Exiting AuditRepositoryLogger.logEntityPatientDiscAsyncResp(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Entity XDR Request.
     *
     * @param message The XDR Request message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityXDRReq(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType message, AssertionType assertion,
            String direction) {
        log.debug("Entering AuditRepositoryLogger.logEntityXDRReq(...)");
        LogEventRequestType auditMsg = null;
        auditMsg = xdrAuditTransformer.transformRequestToAuditMsg(message.getProvideAndRegisterDocumentSetRequest(),
                assertion, direction, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        log.debug("Exiting AuditRepositoryLogger.logEntityXDRReq(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log message from an Audit XDR Response.
     *
     * @param response The Registry Response message to be audit logged
     * @param assertion The assertion of the message to be audit logged
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdapterXDRResponse(RegistryResponseType response, AssertionType assertion,
            String direction) {
        log.debug("Entering AuditRepositoryLogger.logAdapterXDRResponse(...)");
        LogEventRequestType auditMsg = null;
        log.debug("Exiting AuditRepositoryLogger.logAdapterXDRResponse(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log message from an Entity XDR Response.
     *
     * @param response The Registry Response message to be audit logged
     * @param assertion The assertion of the message to be audit logged
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityXDRResponse(RegistryResponseType response, AssertionType assertion,
            String direction) {
        log.debug("Entering AuditRepositoryLogger.logEntityXDRResponse(...)");
        LogEventRequestType auditMsg = null;
            auditMsg = xdrAuditTransformer.transformResponseToAuditMsg(response, assertion, direction,
                    NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        log.debug("Exiting AuditRepositoryLogger.logEntityXDRResponse(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Entity XDR Request Response.
     *
     * @param response The XDR Request Response message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityXDRResponseRequest(
            gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType response,
            AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logEntityXDRResponse(...)");
        LogEventRequestType auditMsg = null;
            auditMsg = xdrAuditTransformer.transformRequestToAuditMsg(response, assertion, direction,
                    NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        log.debug("Exiting AuditRepositoryLogger.logEntityXDRResponse(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a document query request.
     * 
     * @param message The Document Query Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdhocQuery(AdhocQueryMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logAdhocQuery(...)");
        return logAdhocQuery(message, direction, _interface, null);
    }

    /**
     * This method will create the generic Audit Log Message from a document query request.
     * 
     * @param message The Document Query Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @param responseCommunityId
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdhocQuery(AdhocQueryMessageType message, String direction, String _interface,
            String responseCommunityId) {
        log.debug("Entering AuditRepositoryLogger.logAdhocQuery(...)");
        LogEventRequestType auditMsg = null;
            LogAdhocQueryRequestType logReqMsg = new LogAdhocQueryRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            auditMsg = dqAuditTransforms.transformDocQueryReq2AuditMsg(logReqMsg, responseCommunityId);
        log.debug("Exiting AuditRepositoryLogger.logAdhocQuery(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a document query response.
     * 
     * @param message The Document Query Response message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdhocQueryResult(AdhocQueryResponseMessageType message, String direction,
            String _interface) {
        return this.logAdhocQueryResult(message, direction, _interface, null);
    }

    /**
     * This method will create the generic Audit Log Message from a document query response.
     * 
     * @param message The Document Query Response message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @param requestCommunityID
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdhocQueryResult(AdhocQueryResponseMessageType message, String direction,
            String _interface, String requestCommunityID) {
        log.debug("Entering AuditRepositoryLogger.logAdhocQueryResult(...)");
        LogEventRequestType auditMsg = null;
            LogAdhocQueryResultRequestType logReqMsg = new LogAdhocQueryResultRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            auditMsg = dqAuditTransforms.transformDocQueryResp2AuditMsg(logReqMsg, requestCommunityID);
        log.debug("Exiting AuditRepositoryLogger.logAdhocQueryResult(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a document query deferred acknowledgment.
     * 
     * @param acknowledgement The DocQuery Acknowledgment
     * @param assertion The Assertion Class containing SAML information
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdhocQueryDeferredAck(DocQueryAcknowledgementType acknowledgement,
            AssertionType assertion, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logAdhocQueryResult(...)");
        return logAdhocQueryDeferredAck(acknowledgement, assertion, direction, _interface, null);
    }

    /**
     * This method will create the generic Audit Log Message from a document query deferred acknowledgment.
     * 
     * @param acknowledgement The DocQuery Acknowledgment
     * @param assertion The Assertion Class containing SAML information
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @param requestCommunityID The Request Community ID
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdhocQueryDeferredAck(DocQueryAcknowledgementType acknowledgement,
            AssertionType assertion, String direction, String _interface, String requestCommunityID) {
        log.debug("Entering AuditRepositoryLogger.logAdhocQueryResult(...)");
        LogEventRequestType auditMsg = null;
            auditMsg = dqAuditTransforms.transformAcknowledgementToAuditMsg(acknowledgement, assertion, direction,
                    _interface, requestCommunityID);
        log.debug("Exiting AuditRepositoryLogger.logAdhocQueryResult(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a document retrieve request.
     * 
     * @param message The Document Retrieve Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logDocRetrieve(DocRetrieveMessageType message, String direction, String _interface) {
        return logDocRetrieve(message, direction, _interface, null);
    }

    /**
     * This method will create the generic Audit Log Message from a document retrieve request.
     * 
     * @param message The Document Retrieve Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @param responseCommunityID The response Community ID
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logDocRetrieve(DocRetrieveMessageType message, String direction, String _interface,
            String responseCommunityID) {
        log.debug("Entering AuditRepositoryLogger.logDocRetrieve(...)");
        LogEventRequestType auditMsg = null;
            LogDocRetrieveRequestType logReqMsg = new LogDocRetrieveRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            auditMsg = DocumentRetrieveTransforms.transformDocRetrieveReq2AuditMsg(logReqMsg, responseCommunityID);
        log.debug("Exiting AuditRepositoryLogger.logDocRetrieve(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a document retrieve response.
     * 
     * @param message The Document Retrieve Response message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logDocRetrieveResult(DocRetrieveResponseMessageType message, String direction,
            String _interface) {
        return logDocRetrieveResult(message, direction, _interface, null);
    }

    /**
     * This method will create the generic Audit Log Message from a document retrieve response.
     * 
     * @param message The Document Retrieve Response message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @param requestCommunityID The Request Community ID
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logDocRetrieveResult(DocRetrieveResponseMessageType message, String direction,
            String _interface, String requestCommunityID) {
        log.debug("Entering AuditRepositoryLogger.logDocRetrieveResult(...)");
        LogEventRequestType auditMsg = null;
            LogDocRetrieveResultRequestType logReqMsg = new LogDocRetrieveResultRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            auditMsg = DocumentRetrieveTransforms.transformDocRetrieveResp2AuditMsg(logReqMsg, requestCommunityID);
        log.debug("Exiting AuditRepositoryLogger.logDocRetrieveResult(...)");
        return auditMsg;
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
        log.debug("Entering AuditRepositoryLogger.logFindAuditEvents(...)");
        LogEventRequestType auditMsg = null;
            LogFindAuditEventsRequestType logReqMsg = new LogFindAuditEventsRequestType();
            logReqMsg.setMessage(message);
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            auditMsg = FindAuditEventsTransforms.transformFindAuditEventsReq2AuditMsg(logReqMsg);
        log.debug("Exiting AuditRepositoryLogger.logFindAuditEvents(...)");
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
        log.debug("Entering AuditRepositoryLogger.logFindAuditEventsResult(...)");
        LogEventRequestType auditMsg = null;
            LogFindAuditEventsResultRequestType logReqMsg = new LogFindAuditEventsResultRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            log.warn("logFindAuditEventsResult method is not implemented");
        log.debug("Exiting AuditRepositoryLogger.logFindAuditEventsResult(...)");
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
        log.debug("Entering AuditRepositoryLogger.logNhinSubscribeRequest(...)");
        LogEventRequestType auditMsg = null;
            SubscribeTransforms transformLib = new SubscribeTransforms();
            LogNhinSubscribeRequestType logReqMsg = new LogNhinSubscribeRequestType();
            logReqMsg.setMessage(message);
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);

            auditMsg = transformLib.transformNhinSubscribeRequestToAuditMessage(logReqMsg);
        log.debug("Exiting AuditRepositoryLogger.logNhinSubscribeRequest(...)");
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
        log.debug("Entering AuditRepositoryLogger.logNhinNotifyRequest(...)");
        LogEventRequestType auditMsg = null;
            LogNhinNotifyRequestType logReqMsg = new LogNhinNotifyRequestType();
            logReqMsg.setMessage(message);
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);

            auditMsg = transformLib.transformNhinNotifyRequestToAuditMessage(logReqMsg);
        log.debug("Exiting AuditRepositoryLogger.logNhinNotifyRequest(...)");
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
        log.debug("Entering AuditRepositoryLogger.logNhinUnsubscribeRequest(...)");
        LogEventRequestType auditMsg = null;
            UnsubscribeTransforms transformLib = new UnsubscribeTransforms();
            LogNhinUnsubscribeRequestType logReqMsg = new LogNhinUnsubscribeRequestType();
            logReqMsg.setMessage(message);
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            auditMsg = transformLib.transformNhinUnsubscribeRequestToAuditMessage(logReqMsg);
        log.debug("Exiting AuditRepositoryLogger.logNhinUnsubscribeRequest(...)");
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
        log.debug("Entering AuditRepositoryLogger.logUnsubscribeResponse(...)");
        LogEventRequestType auditMsg = null;
            LogUnsubscribeResponseType logReqMsg = new LogUnsubscribeResponseType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            UnsubscribeTransforms transformLib = new UnsubscribeTransforms();
            auditMsg = transformLib.transformUnsubscribeResponseToGenericAudit(logReqMsg);
        log.debug("Exiting AuditRepositoryLogger.logUnsubscribeResponse(...)");
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
        log.debug("Entering AuditRepositoryLogger.logSubscribeResponse(...)");
        LogEventRequestType auditMsg = null;
            LogSubscribeResponseType logReqMsg = new LogSubscribeResponseType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            SubscribeTransforms transformLib = new SubscribeTransforms();
            auditMsg = transformLib.transformSubscribeResponseToAuditMessage(logReqMsg);
        log.debug("Exiting AuditRepositoryLogger.logSubscribeResponse(...)");
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
        log.debug("Entering AuditRepositoryLogger.logEntityDocSubscribeRequest(...)");
        LogEventRequestType auditMsg = null;
            LogEntityDocumentSubscribeRequestType logReqMsg = new LogEntityDocumentSubscribeRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            log.warn("logEntityDocSubscribeRequest method is not implemented");
        log.debug("Exiting AuditRepositoryLogger.logEntityDocSubscribeRequest(...)");
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
        log.debug("Entering AuditRepositoryLogger.logEntityCdcSubscribeRequest(...)");
        LogEventRequestType auditMsg = null;
            LogEntityCdcSubscribeRequestType logReqMsg = new LogEntityCdcSubscribeRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            log.warn("logEntityCdcSubscribeRequest method is not implemented");
        log.debug("Exiting AuditRepositoryLogger.logEntityCdcSubscribeRequest(...)");
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
        log.debug("Entering AuditRepositoryLogger.logEntityDocNotifyRequest(...)");
        LogEventRequestType auditMsg = null;
            LogEntityDocumentNotifyRequestType logReqMsg = new LogEntityDocumentNotifyRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            log.warn("logEntityDocNotifyRequest method is not implemented");
        log.debug("Exiting AuditRepositoryLogger.logEntityDocNotifyRequest(...)");
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
        log.debug("Entering AuditRepositoryLogger.logEntityCdcNotifyRequest(...)");
        LogEventRequestType auditMsg = null;
            LogEntityCdcNotifyRequestType logReqMsg = new LogEntityCdcNotifyRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            log.warn("logEntityCdcNotifyRequest method is not implemented");
        log.debug("Exiting AuditRepositoryLogger.logEntityCdcNotifyRequest(...)");
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
        log.debug("Entering AuditRepositoryLogger.logEntityNotifyResponse(...)");
        LogEventRequestType auditMsg = null;
            LogEntityNotifyResponseType logReqMsg = new LogEntityNotifyResponseType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            log.warn("logEntityNotifyRespRequest method is not implemented");
        log.debug("Exiting AuditRepositoryLogger.logEntityNotifyResponse(...)");
        return auditMsg;
    }

    /**
     * Creates a generic audit log for an XDR Request at the NHIN interface.
     *
     * @param message The generic XDR Request message
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return
     */
    public LogEventRequestType logXDRReq(ProvideAndRegisterDocumentSetRequestType message, AssertionType assertion,
            String direction) {
        log.debug("Entering AuditRepositoryLogger.logNhinXDRReq(...)");
        LogEventRequestType auditMsg = null;

        if (message == null) {
            log.error("Message is null");
            return null;
        }
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformRequestToAuditMsg(message, assertion, direction,
                    NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        log.debug("Exiting AuditRepositoryLogger.logNhinXDRReq(...)");
        return auditMsg;
    }

    /**
     * Create a generic audit log for an XDR Request at the Adapter interface.
     *
     * @param message The generic XDR Request message
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return
     */
    public LogEventRequestType logAdapterXDRReq(ProvideAndRegisterDocumentSetRequestType message, AssertionType assertion,
            String direction) {
        log.debug("Entering AuditRepositoryLogger.logAdapterXDRReq(...)");
        LogEventRequestType auditMsg = null;

        if (message == null) {
            log.error("Message is null");
            return null;
        }

        log.debug("Exiting AuditRepositoryLogger.logAdapterXDRReq(...)");
        return auditMsg;
    }

    /**
     * Create a generic audit log for a secured XDR Request.
     *
     * @param message The generic secured XDR Request message
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return
     */
    public LogEventRequestType logXDRReq(
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType message,
            AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logXDRReq(...)");
        LogEventRequestType auditMsg = null;
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformRequestToAuditMsg(message, assertion, direction,
                    NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        log.debug("Exiting AuditRepositoryLogger.logNhinXDRReq(...)");
        return auditMsg;
    }

    /**
     * Create an audit log for an XDR Response at the NHIN interface.
     * 
     * @param message The generic XDR Response message
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return
     */
    public LogEventRequestType logNhinXDRResponse(RegistryResponseType message, AssertionType assertion,
            String direction) {
        log.debug("Entering AuditRepositoryLogger.logNhinXDRReq(...)");
        LogEventRequestType auditMsg = null;
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformResponseToAuditMsg(message, assertion, direction,
                    NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        log.debug("Exiting AuditRepositoryLogger.logNhinXDRReq(...)");
        return auditMsg;
    }

    /**
     * Create an audit log for an XDR Response Request at the NHIN interface.
     * 
     * @param message The Nhin XDR Response
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return
     */
    public LogEventRequestType logNhinXDRResponseRequest(
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType message,
            AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logNhinXDRReq(...)");
        LogEventRequestType auditMsg = null;
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformRequestToAuditMsg(message, assertion, direction,
                    NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        log.debug("Exiting AuditRepositoryLogger.logNhinXDRReq(...)");
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
        log.debug("Entering AuditRepositoryLogger.logEntityUnsubscribeRequest(...)");
        LogEventRequestType auditMsg = null;
            LogEntityUnsubscribeRequestType logReqMsg = new LogEntityUnsubscribeRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityUnsubscribeRequest method is not implemented");
        log.debug("Exiting AuditRepositoryLogger.logEntityUnsubscribeRequest(...)");
        return auditMsg;
    }

    /**
     * Log an XDR Acknowledgement at the Nhin interface.
     * @param acknowledgement The XDR Acknowledgement to be audited
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param action The type of the current message (Request or Response)
     * @return
     */
    public LogEventRequestType logAcknowledgement(XDRAcknowledgementType acknowledgement, AssertionType assertion,
            String direction, String action) {
        getLogger().debug("Entering AuditRepositoryLogger.logAcknowledgement(...)");

        LogEventRequestType auditMsg = null;

            auditMsg = xdrAuditTransformer.transformAcknowledgementToAuditMsg(acknowledgement, assertion, direction,
                    NhincConstants.AUDIT_LOG_NHIN_INTERFACE, action);

        getLogger().debug("Exiting AuditRepositoryLogger.logAcknowledgement(...)");
        return auditMsg;
    }

    /**
     * Log an XDR Acknowledgement at the Adapter interface.
     * @param acknowledgement The XDR Acknowledgement to be audited
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param action The type of the current message (Request or Response)
     * @return
     */
    public LogEventRequestType logAdapterAcknowledgement(XDRAcknowledgementType acknowledgement, AssertionType assertion,
            String direction, String action) {
        getLogger().debug("Entering AuditRepositoryLogger.logAcknowledgement(...)");

        LogEventRequestType auditMsg = null;
        getLogger().debug("Exiting AuditRepositoryLogger.logAcknowledgement(...)");
        return auditMsg;
    }

    /**
     * Log an XDR Acknowledgement at the Entity interface.
     * @param acknowledgement The XDR Acknowledgement to be audited
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param action The type of the current message (Request or Response)
     * @return
     */
    public LogEventRequestType logEntityAcknowledgement(XDRAcknowledgementType acknowledgement,
            AssertionType assertion, String direction, String action) {
        getLogger().debug("Entering AuditRepositoryLogger.logAcknowledgement(...)");
        LogEventRequestType auditMsg = null;
            auditMsg = xdrAuditTransformer.transformAcknowledgementToAuditMsg(acknowledgement, assertion, direction,
                    NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, action);
        getLogger().debug("Exiting AuditRepositoryLogger.logAcknowledgement(...)");
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
        log.debug("Entering AuditRepositoryLogger.logEntityPatientDiscResp(...)");
        LogEventRequestType auditMsg = null;
            AdminDistTransforms auditTransformer = new AdminDistTransforms();
            auditMsg = auditTransformer.transformEntitySendAlertToAuditMsg(message, assertion, direction,
                    NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        log.debug("Exiting AuditRepositoryLogger.logEntityPatientDiscResp(...)");
        return auditMsg;
    }

    /**
     * Create a generic Audit Log for an Admin Distribution message.
     * @param message The Admin Distribution message to be audited
     * @param assertion The assertion to be audited
     * @param target
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return
     */
    public LogEventRequestType logNhincAdminDist(EDXLDistribution message, AssertionType assertion,
            NhinTargetSystemType target, String direction) {

        return logNhincAdminDist(message, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE );
    }

    /**
     * Create a generic Audit Log for an Admin Distribution message.
     * @param message The Admin Distribution message to be audited
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param logInterface The interface this message is sent from(NHIN, Adapter, or Entity)
     * @return
     */
    public LogEventRequestType logNhincAdminDist(EDXLDistribution message, AssertionType assertion, String direction,
            String logInterface) {
        log.debug("Entering AuditRepositoryLogger.logEntityPatientDiscResp(...)");
        LogEventRequestType auditMsg = null;
        auditMsg = adAuditTransformer.transformEDXLDistributionRequestToAuditMsg(message, assertion, direction,
                logInterface);
        log.debug("Exiting AuditRepositoryLogger.logEntityPatientDiscResp(...)");
        return auditMsg;
    }

    /**
     * Returns the Log object.
     * @return the log object
     */
    protected Log getLogger() {
        return log;
    }

}
