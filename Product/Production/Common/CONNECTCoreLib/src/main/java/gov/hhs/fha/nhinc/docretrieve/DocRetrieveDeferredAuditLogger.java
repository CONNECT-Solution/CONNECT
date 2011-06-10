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
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class defines all the required operations for document retrieve deferred Audit logging requirements
 * @author Sai Valluripalli
 */
public class DocRetrieveDeferredAuditLogger {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public DocRetrieveDeferredAuditLogger() {
        log = createLogger();
        debugEnabled = setLog4jDebugValue();
    }

    /**
     * Creating log4j logger instance
     * @return Log
     */
    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * Returns an instance of AuditRepositoryProxy
     * @param auditRepoFactory
     * @return AuditRepositoryProxy
     */
    protected AuditRepositoryProxy getAuditProxy() {
        return new AuditRepositoryProxyObjectFactory().getAuditRepositoryProxy();
    }

    /**
     * Verify the log4j properties if debug enabled or disabled
     * @return boolean
     */
    protected boolean setLog4jDebugValue() {
        return log.isDebugEnabled();
    }

    /**
     * This method will log Document Retrieve Requests received/sent on a particular public interface
     *
     * @param message The Document Retrieve Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return An acknowledgement of whether or not the message was successfully logged.
     */
    protected AcknowledgementType logDocRetrieveDeferred(DocRetrieveMessageType message, String direction, String _interface, AssertionType assertion, String responseCommunityId) {
        if (debugEnabled) {
            log.debug("Entering DocRetrieveDeferredAuditLogger.logDocRetrieveDeferred(...)");
        }
        AcknowledgementType ack = null;
        LogEventRequestType auditLogMsg = new AuditRepositoryLogger().logDocRetrieve(message, direction, _interface, responseCommunityId);

        if (auditLogMsg != null) {
            if (debugEnabled) {
                log.debug("Inside: DocRetrieveAuditLog.logDocRetrieve(...) - calling AuditRepositoryProxyObjectFactory.getAuditRepositoryProxy(...)");
            }
            AuditRepositoryProxy proxy = getAuditProxy();
            if (debugEnabled) {
                log.debug("Inside: DocRetrieveAuditLog.logDocRetrieve(...) - calling AuditRepositoryProxy.auditLog(...)");
            }
            if (null != proxy) {
                ack = new AcknowledgementType();
                ack = proxy.auditLog(auditLogMsg, assertion);
            }
            if (debugEnabled) {
                log.debug("Inside: DocRetrieveAuditLog.logDocRetrieve(...) - returned from calling AuditRepositoryProxy.auditLog(...)");
            }
        }
        if (debugEnabled) {
            log.debug("Exiting DocRetrieveDeferredAuditLog.logDocRetrieveDeferred(...)");
        }

        return ack;
    }

    /**
     *
     * @param auditMsg Log a document retrieve deferred message received on the entity interface (entity or NHIN Proxy).
     * @param assertion Assertion information
     * @return Audit acknowledgement
     */
    public AcknowledgementType auditDocRetrieveDeferredRequest(RetrieveDocumentSetRequestType auditMsg, AssertionType assertion) {
        return auditDocRetrieveDeferredRequest(auditMsg, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, assertion, null);
    }

    /**
     *
     * @param auditMsg Log a document retrieve deferred message received on the entity interface (entity or NHIN Proxy).
     * @param assertion Assertion information
     * @param  responseCommunityId
     * @return Audit acknowledgement
     */
    public AcknowledgementType auditDocRetrieveDeferredRequest(RetrieveDocumentSetRequestType auditMsg, String direction, String _interface, AssertionType assertion, String responseCommunityId) {
        if (debugEnabled) {
            log.debug("Entering DocRetrieveDeferredAuditLog.auditDocRetrieveDeferredRequest(...)");
        }
        DocRetrieveMessageType auditRequestMsg = new DocRetrieveMessageType();
        auditRequestMsg.setRetrieveDocumentSetRequest(auditMsg);
        auditRequestMsg.setAssertion(assertion);
        AcknowledgementType ack = logDocRetrieveDeferred(auditRequestMsg, direction, _interface, assertion, responseCommunityId);
        if (debugEnabled) {
            log.debug("Exiting DocRetrieveDeferredAuditLog.auditDocRetrieveDeferredRequest(...)");
        }
        return ack;
    }

    /**
     *
     * @param message
     * @param direction
     * @param _interface
     * @param assertion
     * @return Audit acknowledgement
     */
    public AcknowledgementType auditDocRetrieveDeferredResponse(RetrieveDocumentSetResponseType message, String direction, String _interface, AssertionType assertion) {
        return auditDocRetrieveDeferredResponse(message, direction, _interface, assertion, null);
    }

    /**
     * This method will log Document Retrieve Deferred Responses received/sent on a particular public interface
     *
     * @param message The Document Query Response message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @param responseCommunityId
     * @return An acknowledgement of whether or not the message was successfully logged.
     */
    public AcknowledgementType auditDocRetrieveDeferredResponse(RetrieveDocumentSetResponseType message, String direction, String _interface, AssertionType assertion, String responseCommunityId) {
        log.debug("Entering DocRetrieveDeferredAuditLog.auditDocRetrieveDeferredResponse(...)");
        DocRetrieveResponseMessageType responseAudit = new DocRetrieveResponseMessageType();
        responseAudit.setRetrieveDocumentSetResponse(message);
        responseAudit.setAssertion(assertion);

        AcknowledgementType ack = new AcknowledgementType();
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieveResult(responseAudit, direction, _interface, responseCommunityId);

        if (auditLogMsg != null) {
            log.debug("Inside: DocRetrieveDeferredAuditLog.auditDocRetrieveDeferredResponse(...) - Creating AuditRepositoryProxyObjectFactory object.");
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            log.debug("Inside: DocRetrieveDeferredAuditLog.auditDocRetrieveDeferredResponse(...) - calling AuditRepositoryProxyObjectFactory.getAuditRepositoryProxy(...)");
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            log.debug("Inside: DocRetrieveDeferredAuditLog.auditDocRetrieveDeferredResponse(...) - calling AuditRepositoryProxy.auditLog(...)");
            ack = proxy.auditLog(auditLogMsg, assertion);
            log.debug("Inside: DocRetrieveDeferredAuditLog.auditDocRetrieveDeferredResponse(...) - returned from calling AuditRepositoryProxy.auditLog(...)");
        }

        log.debug("Exiting DocRetrieveDeferredAuditLog.auditDocRetrieveDeferredResponse(...)");
        return ack;
    }

    /**
     * This method will log Document Retrieve Deferred Ack Responses received/sent on a particular public interface
     * @param Response
     * @param assertion
     * @param direction
     * @return AcknowledgementType
     */
    public AcknowledgementType auditDocRetrieveDeferredAckResponse(RegistryResponseType Response, RetrieveDocumentSetRequestType request, RetrieveDocumentSetResponseType response, AssertionType assertion, String direction, String _interface) {
        return auditDocRetrieveDeferredAckResponse(Response, request, response, assertion, direction, _interface, null);
    }

    /**
     * This method will log Document Retrieve Deferred Ack Responses received/sent on a particular public interface
     * @param Response
     * @param assertion
     * @param direction
     * @param requestCommunityId
     * @return AcknowledgementType
     */
    public AcknowledgementType auditDocRetrieveDeferredAckResponse(RegistryResponseType Response, RetrieveDocumentSetRequestType request, RetrieveDocumentSetResponseType response, AssertionType assertion, String direction, String _interface, String requestCommunityId) {
        log.debug("Entering DocRetrieveDeferredAuditLog.auditDocRetrieveDeferredAckResponse(...)");
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieveAckResponse(Response, request, response, assertion, direction, _interface, requestCommunityId);

        if (auditLogMsg != null) {
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            ack = proxy.auditLog(auditLogMsg, assertion);
        }

        log.debug("Exiting DocRetrieveDeferredAuditLog.auditDocRetrieveDeferredAckResponse(...)");
        return ack;
    }
}
