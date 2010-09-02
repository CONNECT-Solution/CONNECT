/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 *
 *
 * @author Neil Webb
 */
public class DocRetrieveAuditLog
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(DocRetrieveAuditLog.class);

    /**
     *
     * @param auditMsg Log a document retrieve message received on the entity interface (entity or NHIN Proxy).
     * @param assertion Assertion information
     * @return Audit acknowledgement
     */
    public AcknowledgementType auditDocRetrieveRequest(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType auditMsg, AssertionType assertion)
    {
        log.debug("Entering DocRetrieveAuditLog.auditDocRetrieveRequest(...)");
        DocRetrieveMessageType auditRequestMsg = new DocRetrieveMessageType();
        auditRequestMsg.setRetrieveDocumentSetRequest(auditMsg);
        auditRequestMsg.setAssertion(assertion);

        AcknowledgementType ack = logDocRetrieve(auditRequestMsg, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, assertion);

        log.debug("Exiting DocRetrieveAuditLog.auditDocRetrieveRequest(...)");
        return ack;
    }

//    /**
//     *
//     * @param auditMsg Log a document retrieve message received on the entity interface.
//     * @param assertion Assertion information
//     * @return Audit acknowledgement
//     */
//    public AcknowledgementType auditEntityDocRetrieveRequest(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType auditMsg, AssertionType assertion)
//    {
//        log.debug("Entering DocRetrieveAuditLog.auditEntityDocRetrieveRequest(...)");
//        DocRetrieveMessageType auditRequestMsg = new DocRetrieveMessageType();
//        auditRequestMsg.setRetrieveDocumentSetRequest(auditMsg);
//        auditRequestMsg.setAssertion(assertion);
//
//        AcknowledgementType ack = logDocRetrieve(auditRequestMsg, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
//
//        log.debug("Exiting DocRetrieveAuditLog.auditEntityDocRetrieveRequest(...)");
//        return ack;
//    }
//
//    /**
//     * Logs a document retrieve message received on the NHIN Proxy interface.
//     *
//     * @param auditMsg Document retrieve message.
//     * @param assertion Assertion information
//     * @return Audit acknowledgement
//     */
//    public AcknowledgementType auditNhinProxyDocRetrieveRequest(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType auditMsg, AssertionType assertion)
//    {
//        log.debug("Entering DocRetrieveAuditLog.auditNhinProxyDocRetrieveRequest(...)");
//        DocRetrieveMessageType auditRequestMsg = new DocRetrieveMessageType();
//        auditRequestMsg.setRetrieveDocumentSetRequest(auditMsg.getRetrieveDocumentSetRequest());
//        auditRequestMsg.setAssertion(assertion);
//
//        AcknowledgementType ack = logDocRetrieve(auditRequestMsg, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
//
//        log.debug("Exiting DocRetrieveAuditLog.auditNhinProxyDocRetrieveRequest(...)");
//        return ack;
//    }

    /**
     * This method will log Document Retrieve Requests received/sent on a particular public interface
     *
     * @param message The Document Retrieve Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return An acknowledgement of whether or not the message was successfully logged.
     */
    private AcknowledgementType logDocRetrieve(DocRetrieveMessageType message, String direction, String _interface, AssertionType assertion)
    {
        log.debug("Entering DocRetrieveAuditLog.logDocRetrieve(...)");
        AcknowledgementType ack = new AcknowledgementType();
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieve(message, direction, _interface);

        if (auditLogMsg != null)
        {
            log.debug("Inside: DocRetrieveAuditLog.logDocRetrieve(...) - Creating AuditRepositoryProxyObjectFactory object.");
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            log.debug("Inside: DocRetrieveAuditLog.logDocRetrieve(...) - calling AuditRepositoryProxyObjectFactory.getAuditRepositoryProxy(...)");
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            log.debug("Inside: DocRetrieveAuditLog.logDocRetrieve(...) - calling AuditRepositoryProxy.auditLog(...)");
            ack = proxy.auditLog(auditLogMsg, assertion);
            log.debug("Inside: DocRetrieveAuditLog.logDocRetrieve(...) - returned from calling AuditRepositoryProxy.auditLog(...)");
        }
        log.debug("Exiting DocRetrieveAuditLog.logDocRetrieve(...)");
        return ack;
    }

    /**
     * This method will log Document Query Responses received/sent on a particular public interface
     *
     * @param message The Document Query Response message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return An acknowledgement of whether or not the message was successfully logged.
     */
    public AcknowledgementType auditResponse(RetrieveDocumentSetResponseType message, AssertionType assertion)
    {
        log.debug("Entering DocRetrieveAuditLog.auditResponse(...)");
        DocRetrieveResponseMessageType responseAudit = new DocRetrieveResponseMessageType();
        responseAudit.setRetrieveDocumentSetResponse(message);
        responseAudit.setAssertion(assertion);

        AcknowledgementType ack = new AcknowledgementType();
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieveResult(responseAudit, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        if (auditLogMsg != null) {
            log.debug("Inside: DocRetrieveAuditLog.auditResponse(...) - Creating AuditRepositoryProxyObjectFactory object.");
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            log.debug("Inside: DocRetrieveAuditLog.auditResponse(...) - calling AuditRepositoryProxyObjectFactory.getAuditRepositoryProxy(...)");
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            log.debug("Inside: DocRetrieveAuditLog.auditResponse(...) - calling AuditRepositoryProxy.auditLog(...)");
            ack = proxy.auditLog(auditLogMsg, assertion);
            log.debug("Inside: DocRetrieveAuditLog.auditResponse(...) - returned from calling AuditRepositoryProxy.auditLog(...)");
        }

        log.debug("Exiting DocRetrieveAuditLog.auditResponse(...)");
        return ack;
    }
}
