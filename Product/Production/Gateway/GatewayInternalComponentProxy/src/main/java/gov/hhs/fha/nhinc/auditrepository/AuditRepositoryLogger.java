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
import gov.hhs.fha.nhinc.common.auditlog.NhinSubjectDiscoveryAckMessageType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectAddedMessageType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectReidentificationRequestMessageType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectReidentificationResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectRevisedMessageType;
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
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
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
import gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms;
import gov.hhs.fha.nhinc.transform.audit.SubjectDiscoveryTransforms;
import gov.hhs.fha.nhinc.transform.audit.SubscribeTransforms;
import gov.hhs.fha.nhinc.transform.audit.UnsubscribeTransforms;
import gov.hhs.fha.nhinc.transform.audit.XDRTransforms;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

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
        log.debug("Entering AuditRepositoryLogger.logSubjectAdded(...)");
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            LogSubjectAddedRequestType logReqMsg = new LogSubjectAddedRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = SubjectDiscoveryTransforms.transformPRPA2013012AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logSubjectAdded(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a NHIN Patient Discovery Request
     *
     * @param message The Patient Discovery Request message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinPatientDiscReq(PRPAIN201305UV02 message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logNhinPatientDiscReq(...)");
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            PatientDiscoveryTransforms auditTransformer = new PatientDiscoveryTransforms();
            auditMsg = auditTransformer.transformNhinPRPAIN201305RequestToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logNhinPatientDiscReq(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a NHIN Patient Discovery Response
     *
     * @param message The Patient Discovery Response message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinPatientDiscResp(PRPAIN201306UV02 message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logNhinPatientDiscResp(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            PatientDiscoveryTransforms auditTransformer = new PatientDiscoveryTransforms();
            auditMsg = auditTransformer.transformNhinPRPAIN201306ResponseToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logNhinPatientDiscResp(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Adapter Patient Discovery Request
     *
     * @param message The Patient Discovery Request message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdapterPatientDiscReq(PRPAIN201305UV02 message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logAdapterPatientDiscReq(...)");
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            PatientDiscoveryTransforms auditTransformer = new PatientDiscoveryTransforms();
            auditMsg = auditTransformer.transformAdapterPRPAIN201305RequestToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logAdapterPatientDiscReq(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Adapter Patient Discovery Response
     *
     * @param message The Patient Discovery Response message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdapterPatientDiscResp(PRPAIN201306UV02 message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logAdapterPatientDiscResp(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            PatientDiscoveryTransforms auditTransformer = new PatientDiscoveryTransforms();
            auditMsg = auditTransformer.transformNhinPRPAIN201306ResponseToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        }
        log.debug("Exiting AuditRepositoryLogger.logAdapterPatientDiscResp(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Entity Patient Discovery Request
     *
     * @param message The Patient Discovery Request message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityPatientDiscReq(RespondingGatewayPRPAIN201305UV02RequestType message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logEntityPatientDiscReq(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            PatientDiscoveryTransforms auditTransformer = new PatientDiscoveryTransforms();
            auditMsg = auditTransformer.transformEntityPRPAIN201305RequestToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logEntityPatientDiscReq(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Entity Patient Discovery Response
     *
     * @param message The Patient Discovery Response message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityPatientDiscResp(RespondingGatewayPRPAIN201306UV02ResponseType message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logEntityPatientDiscResp(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            PatientDiscoveryTransforms auditTransformer = new PatientDiscoveryTransforms();
            auditMsg = auditTransformer.transformEntityPRPAIN201306ResponseToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityPatientDiscResp(...)");

        return auditMsg;
    }

    public LogEventRequestType logEntityXDRReq(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logEntityXDRReq(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformRequestToAuditMsg(message.getProvideAndRegisterDocumentSetRequest(), assertion, direction, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logEntityXDRReq(...)");
        return auditMsg;
    }

    public LogEventRequestType logEntityXDRResponse(RegistryResponseType response, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logEntityXDRResponse(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformResponseToAuditMsg(response, assertion, direction, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logEntityXDRResponse(...)");
        return auditMsg;
    }

    public LogEventRequestType logEntityXDRResponseRequest(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType response, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logEntityXDRResponse(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformRequestToAuditMsg(response, assertion, direction, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logEntityXDRResponse(...)");
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
        log.debug("Entering AuditRepositoryLogger.logAdhocQuery(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogAdhocQueryRequestType logReqMsg = new LogAdhocQueryRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = new DocumentQueryTransforms().transformDocQueryReq2AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logAdhocQuery(...)");

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
        log.debug("Entering AuditRepositoryLogger.logSubjectRevised(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogSubjectRevisedRequestType logReqMsg = new LogSubjectRevisedRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = SubjectDiscoveryTransforms.transformPRPA2013022AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logSubjectRevised(...)");

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
        log.debug("Entering AuditRepositoryLogger.logNhinSubjectDiscoveryAck(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogNhinSubjectDiscoveryAckRequestType logReqMsg = new LogNhinSubjectDiscoveryAckRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            auditMsg = SubjectDiscoveryTransforms.transformAck2AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logNhinSubjectDiscoveryAck(...)");

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
        log.debug("Entering AuditRepositoryLogger.logAdhocQueryResult(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogAdhocQueryResultRequestType logReqMsg = new LogAdhocQueryResultRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = new DocumentQueryTransforms().transformDocQueryResp2AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logAdhocQueryResult(...)");

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
        log.debug("Entering AuditRepositoryLogger.logDocRetrieve(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogDocRetrieveRequestType logReqMsg = new LogDocRetrieveRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = DocumentRetrieveTransforms.transformDocRetrieveReq2AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logDocRetrieve(...)");

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
        log.debug("Entering AuditRepositoryLogger.logDocRetrieveResult(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogDocRetrieveResultRequestType logReqMsg = new LogDocRetrieveResultRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = DocumentRetrieveTransforms.transformDocRetrieveResp2AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logDocRetrieveResult(...)");

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
        log.debug("Entering AuditRepositoryLogger.logFindAuditEvents(...)");
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            LogFindAuditEventsRequestType logReqMsg = new LogFindAuditEventsRequestType();
            logReqMsg.setMessage(message);
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);

            auditMsg = FindAuditEventsTransforms.transformFindAuditEventsReq2AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logFindAuditEvents(...)");

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
        log.debug("Entering AuditRepositoryLogger.logFindAuditEventsResult(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogFindAuditEventsResultRequestType logReqMsg = new LogFindAuditEventsResultRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logFindAuditEventsResult method is not implemented");
        }
        log.debug("Exiting AuditRepositoryLogger.logFindAuditEventsResult(...)");

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
        log.debug("Entering AuditRepositoryLogger.logSubjectReident(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogSubjectReidentificationRequestType logReqMsg = new LogSubjectReidentificationRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = SubjectDiscoveryTransforms.transformPRPA2013092AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logSubjectReident(...)");

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
        log.debug("Entering AuditRepositoryLogger.logSubjectReidentResult(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogSubjectReidentificationResponseType logReqMsg = new LogSubjectReidentificationResponseType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = SubjectDiscoveryTransforms.transformPRPA2013102AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logSubjectReidentResult(...)");

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
        log.debug("Entering AuditRepositoryLogger.logNhinSubscribeRequest(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            SubscribeTransforms transformLib = new SubscribeTransforms();
            LogNhinSubscribeRequestType logReqMsg = new LogNhinSubscribeRequestType();
            logReqMsg.setMessage(message);
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);

            auditMsg = transformLib.transformNhinSubscribeRequestToAuditMessage(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logNhinSubscribeRequest(...)");

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
        log.debug("Entering AuditRepositoryLogger.logNhinNotifyRequest(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            NotifyTransforms transformLib = new NotifyTransforms();
            LogNhinNotifyRequestType logReqMsg = new LogNhinNotifyRequestType();
            logReqMsg.setMessage(message);
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);

            auditMsg = transformLib.transformNhinNotifyRequestToAuditMessage(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logNhinNotifyRequest(...)");

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
        log.debug("Entering AuditRepositoryLogger.logNhinUnsubscribeRequest(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            UnsubscribeTransforms transformLib = new UnsubscribeTransforms();
            LogNhinUnsubscribeRequestType logReqMsg = new LogNhinUnsubscribeRequestType();
            logReqMsg.setMessage(message);
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);

            auditMsg = transformLib.transformNhinUnsubscribeRequestToAuditMessage(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logNhinUnsubscribeRequest(...)");

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
        log.debug("Entering AuditRepositoryLogger.logUnsubscribeResponse(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogUnsubscribeResponseType logReqMsg = new LogUnsubscribeResponseType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            UnsubscribeTransforms transformLib = new UnsubscribeTransforms();

            auditMsg = transformLib.transformUnsubscribeResponseToGenericAudit(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logUnsubscribeResponse(...)");

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
        log.debug("Entering AuditRepositoryLogger.logSubscribeResponse(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogSubscribeResponseType logReqMsg = new LogSubscribeResponseType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            SubscribeTransforms transformLib = new SubscribeTransforms();

            auditMsg = transformLib.transformSubscribeResponseToAuditMessage(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logSubscribeResponse(...)");

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
        log.debug("Entering AuditRepositoryLogger.logEntityDocSubscribeRequest(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityDocumentSubscribeRequestType logReqMsg = new LogEntityDocumentSubscribeRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityDocSubscribeRequest method is not implemented");
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityDocSubscribeRequest(...)");

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
        log.debug("Entering AuditRepositoryLogger.logEntityCdcSubscribeRequest(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityCdcSubscribeRequestType logReqMsg = new LogEntityCdcSubscribeRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityCdcSubscribeRequest method is not implemented");
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityCdcSubscribeRequest(...)");

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
        log.debug("Entering AuditRepositoryLogger.logEntityDocNotifyRequest(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityDocumentNotifyRequestType logReqMsg = new LogEntityDocumentNotifyRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityDocNotifyRequest method is not implemented");
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityDocNotifyRequest(...)");

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
        log.debug("Entering AuditRepositoryLogger.logEntityCdcNotifyRequest(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityCdcNotifyRequestType logReqMsg = new LogEntityCdcNotifyRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityCdcNotifyRequest method is not implemented");
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityCdcNotifyRequest(...)");

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
        log.debug("Entering AuditRepositoryLogger.logEntityNotifyResponse(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityNotifyResponseType logReqMsg = new LogEntityNotifyResponseType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityNotifyRespRequest method is not implemented");
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityNotifyResponse(...)");

        return auditMsg;
    }

    public LogEventRequestType logXDRReq(ProvideAndRegisterDocumentSetRequestType message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logNhinXDRReq(...)");
        LogEventRequestType auditMsg = null;

        if (message == null) {
            log.error("Message is null");
            return null;
        }

        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformRequestToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logNhinXDRReq(...)");
        return auditMsg;
    }

    public LogEventRequestType logXDRReq(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logXDRReq(...)");
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformRequestToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logNhinXDRReq(...)");
        return auditMsg;
    }

    public LogEventRequestType logNhinXDRResponse(RegistryResponseType message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logNhinXDRReq(...)");
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformResponseToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logNhinXDRReq(...)");
        return auditMsg;
    }

    public LogEventRequestType logNhinXDRResponseRequest(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logNhinXDRReq(...)");
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformRequestToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logNhinXDRReq(...)");
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
        log.debug("Entering AuditRepositoryLogger.logEntityUnsubscribeRequest(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityUnsubscribeRequestType logReqMsg = new LogEntityUnsubscribeRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityUnsubscribeRequest method is not implemented");
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityUnsubscribeRequest(...)");

        return auditMsg;
    }

    private boolean isServiceEnabled() {
        log.debug("Entering AuditRepositoryLogger.isServiceEnabled(...)");
        boolean serviceEnabled = false;
        try {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.AUDIT_LOG_SERVICE_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.AUDIT_LOG_SERVICE_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage(), ex);
        }
        log.debug("Exiting AuditRepositoryLogger.isServiceEnabled(...) with value of: " + serviceEnabled);
        return serviceEnabled;
    }

    /**
     * 
     * @param acknowledgement
     * @param assertion
     * @param direction
     * @param action
     * @return
     */
    public LogEventRequestType logAcknowledgement(ihe.iti.xdr._2007.AcknowledgementType acknowledgement, AssertionType assertion, String direction, String action) {
        getLogger().debug("Entering AuditRepositoryLogger.logAcknowledgement(...)");

        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformAcknowledgementToAuditMsg(acknowledgement, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, action);
        }

        getLogger().debug("Exiting AuditRepositoryLogger.logAcknowledgement(...)");
        return auditMsg;
    }
    public LogEventRequestType logEntityAcknowledgement(ihe.iti.xdr._2007.AcknowledgementType acknowledgement, AssertionType assertion, String direction, String action) {
        getLogger().debug("Entering AuditRepositoryLogger.logAcknowledgement(...)");

        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformAcknowledgementToAuditMsg(acknowledgement, assertion, direction, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, action);
        }

        getLogger().debug("Exiting AuditRepositoryLogger.logAcknowledgement(...)");
        return auditMsg;
    }
    protected Log getLogger(){
        return log;
    }
}
