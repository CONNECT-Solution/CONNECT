/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.auditrepository;

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
import gov.hhs.fha.nhinc.common.auditlog.LogNhinSubjectDiscoveryAckRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectAddedRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectReidentificationRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectReidentificationResponseType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectRevisedRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectRevokedRequestType;
import gov.hhs.fha.nhinc.common.auditlog.NhinSubjectDiscoveryAckMessageType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectAddedMessageType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectReidentificationRequestMessageType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectReidentificationResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectRevisedMessageType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectRevokedMessageType;
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
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.UnsubscribeRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.audit.DocumentQueryTransforms;
import gov.hhs.fha.nhinc.transform.audit.DocumentRetrieveTransforms;
import gov.hhs.fha.nhinc.transform.audit.FindAuditEventsTransforms;
import gov.hhs.fha.nhinc.transform.audit.NotifyTransforms;
import gov.hhs.fha.nhinc.transform.audit.SubjectDiscoveryTransforms;
import gov.hhs.fha.nhinc.transform.audit.SubscribeTransforms;
import gov.hhs.fha.nhinc.transform.audit.UnsubscribeTransforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class AuditRepositoryLogger {

    private static Log log = LogFactory.getLog(AuditRepositoryLogger.class);

    /**
     * This method will create the generic Audit Log Message from an subject discovery announce request
     *
     * @param message The Audit Query Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logSubjectAdded(SubjectAddedMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;
        

        if (isServiceEnabled()) {
            LogSubjectAddedRequestType logReqMsg = new LogSubjectAddedRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            
            auditMsg = SubjectDiscoveryTransforms.transformPRPA2013012AuditMsg(logReqMsg);
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a document query request
     *
     * @param message The Document Query Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdhocQuery(AdhocQueryMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogAdhocQueryRequestType logReqMsg = new LogAdhocQueryRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            
            auditMsg = new DocumentQueryTransforms().transformDocQueryReq2AuditMsg(logReqMsg);
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a subject discovery revised request
     *
     * @param message The Subject Discovery Revised Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logSubjectRevised(SubjectRevisedMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogSubjectRevisedRequestType logReqMsg = new LogSubjectRevisedRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            
            auditMsg = SubjectDiscoveryTransforms.transformPRPA2013022AuditMsg(logReqMsg);
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a subject discovery revoke request
     *
     * @param message The Subject Discovery Revoke Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logSubjectRevoked(SubjectRevokedMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogSubjectRevokedRequestType logReqMsg = new LogSubjectRevokedRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = SubjectDiscoveryTransforms.transformPRPA2013032AuditMsg(logReqMsg);
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a subject discovery acknowledgement
     *
     * @param message The Subject Discovery Ack message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinSubjectDiscoveryAck(NhinSubjectDiscoveryAckMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogNhinSubjectDiscoveryAckRequestType logReqMsg = new LogNhinSubjectDiscoveryAckRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            auditMsg = SubjectDiscoveryTransforms.transformAck2AuditMsg(logReqMsg);
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a document query response
     *
     * @param message The Document Query Response message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdhocQueryResult(AdhocQueryResponseMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogAdhocQueryResultRequestType logReqMsg = new LogAdhocQueryResultRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            
            auditMsg = new DocumentQueryTransforms().transformDocQueryResp2AuditMsg(logReqMsg);
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a document retrieve request
     *
     * @param message The Document Retrieve Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logDocRetrieve(DocRetrieveMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogDocRetrieveRequestType logReqMsg = new LogDocRetrieveRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            
            auditMsg = DocumentRetrieveTransforms.transformDocRetrieveReq2AuditMsg(logReqMsg);
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a document retrieve response
     *
     * @param message The Document Retrieve Response message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logDocRetrieveResult(DocRetrieveResponseMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogDocRetrieveResultRequestType logReqMsg = new LogDocRetrieveResultRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = DocumentRetrieveTransforms.transformDocRetrieveResp2AuditMsg(logReqMsg);
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an audit query request
     *
     * @param message The Audit Query Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logFindAuditEvents(FindAuditEventsMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            LogFindAuditEventsRequestType logReqMsg = new LogFindAuditEventsRequestType();
            logReqMsg.setMessage(message);
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            
            auditMsg = FindAuditEventsTransforms.transformFindAuditEventsReq2AuditMsg(logReqMsg);
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an audit query response
     *
     * @param message The Audit Query Response message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logFindAuditEventsResult(FindAuditEventsResponseMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogFindAuditEventsResultRequestType logReqMsg = new LogFindAuditEventsResultRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logFindAuditEventsResult method is not implemented");
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a subject discovery reidentification request
     *
     * @param message The Subject Discovery Reidentification Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logSubjectReident(SubjectReidentificationRequestMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogSubjectReidentificationRequestType logReqMsg = new LogSubjectReidentificationRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = SubjectDiscoveryTransforms.transformPRPA2013092AuditMsg(logReqMsg);
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a subject discovery reidentification response
     *
     * @param message The Subject Discovery Reidentification Response message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logSubjectReidentResult(SubjectReidentificationResponseMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogSubjectReidentificationResponseType logReqMsg = new LogSubjectReidentificationResponseType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = SubjectDiscoveryTransforms.transformPRPA2013102AuditMsg(logReqMsg);
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a Nhin Subscribe request
     *
     * @param message The Nhin Subscribe Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinSubscribeRequest(SubscribeRequestType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            SubscribeTransforms transformLib = new SubscribeTransforms();
            LogNhinSubscribeRequestType logReqMsg = new LogNhinSubscribeRequestType();
            logReqMsg.setMessage(message);
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);

            auditMsg = transformLib.transformNhinSubscribeRequestToAuditMessage(logReqMsg);
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a Nhin notify request
     *
     * @param message The Nhin Notify Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinNotifyRequest(NotifyRequestType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            NotifyTransforms transformLib = new NotifyTransforms();
            LogNhinNotifyRequestType logReqMsg = new LogNhinNotifyRequestType();
            logReqMsg.setMessage(message);
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);

            auditMsg = transformLib.transformNhinNotifyRequestToAuditMessage(logReqMsg);
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a Nhin unsubscribe request
     *
     * @param message The Nhin Unsubscribe Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinUnsubscribeRequest(UnsubscribeRequestType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            UnsubscribeTransforms transformLib = new UnsubscribeTransforms();
            LogNhinUnsubscribeRequestType logReqMsg = new LogNhinUnsubscribeRequestType();
            logReqMsg.setMessage(message);
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);

            auditMsg = transformLib.transformNhinUnsubscribeRequestToAuditMessage(logReqMsg);
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an unsubscribe response
     *
     * @param message The Unsubscribe Response message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logUnsubscribeResponse(UnsubscribeResponseMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogUnsubscribeResponseType logReqMsg = new LogUnsubscribeResponseType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            UnsubscribeTransforms transformLib = new UnsubscribeTransforms();

            auditMsg = transformLib.transformUnsubscribeResponseToGenericAudit(logReqMsg);
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a subscribe response
     *
     * @param message The Subscribe Response message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logSubscribeResponse(SubscribeResponseMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogSubscribeResponseType logReqMsg = new LogSubscribeResponseType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            SubscribeTransforms transformLib = new SubscribeTransforms();

            auditMsg = transformLib.transformSubscribeResponseToAuditMessage(logReqMsg);
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity document subscribe request
     *
     * @param message The Entity Document Subscribe Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityDocSubscribeRequest(EntityDocumentSubscribeRequestMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityDocumentSubscribeRequestType logReqMsg = new LogEntityDocumentSubscribeRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityDocSubscribeRequest method is not implemented");
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity CDC subscribe request
     *
     * @param message The Entity CDC Subscribe Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityCdcSubscribeRequest(EntityCdcSubscribeRequestMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityCdcSubscribeRequestType logReqMsg = new LogEntityCdcSubscribeRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityCdcSubscribeRequest method is not implemented");
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity document notify request
     *
     * @param message The Entity Document Notify Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityDocNotifyRequest(EntityDocumentNotifyRequestMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityDocumentNotifyRequestType logReqMsg = new LogEntityDocumentNotifyRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityDocNotifyRequest method is not implemented");
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity CDC notify request
     *
     * @param message The Entity CDC Notify Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityCdcNotifyRequest(EntityCdcNotifyRequestMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityCdcNotifyRequestType logReqMsg = new LogEntityCdcNotifyRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityCdcNotifyRequest method is not implemented");
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity notify response
     *
     * @param message The Entity Notify Response message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityNotifyResponse(EntityNotifyResponseMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityNotifyResponseType logReqMsg = new LogEntityNotifyResponseType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityNotifyRespRequest method is not implemented");
        }

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity unsubscribe request
     *
     * @param message The Entity Unsubscribe Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityUnsubscribeRequest(EntityUnsubscribeRequestMessageType message, String direction, String _interface) {
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityUnsubscribeRequestType logReqMsg = new LogEntityUnsubscribeRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityUnsubscribeRequest method is not implemented");
        }

        return auditMsg;
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
}
