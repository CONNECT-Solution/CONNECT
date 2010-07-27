package gov.hhs.fha.nhinc.docretrievedeferred;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
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
        if (debugEnabled) {
            log.debug("Inside: DocRetrieveAuditLog.logDocRetrieve(...) - Creating AuditRepositoryProxyObjectFactory object.");
        }
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy;
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
    protected AcknowledgementType logDocRetrieveDeferred(DocRetrieveMessageType message, String direction, String _interface, AssertionType assertion) {
        if (debugEnabled) {
            log.debug("Entering DocRetrieveDeferredAuditLogger.logDocRetrieveDeferred(...)");
        }
        AcknowledgementType ack = null;
        LogEventRequestType auditLogMsg = auditRepositoryLogger(message, direction, _interface);

        if (auditLogMsg != null) {
            if (debugEnabled) {
                log.debug("Inside: DocRetrieveAuditLog.logDocRetrieve(...) - calling AuditRepositoryProxyObjectFactory.getAuditRepositoryProxy(...)");
            }
            AuditRepositoryProxy proxy = getAuditProxy();
            if (debugEnabled) {
                log.debug("Inside: DocRetrieveAuditLog.logDocRetrieve(...) - calling AuditRepositoryProxy.auditLog(...)");
            }
            if(null != proxy)
            {
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
     * Calls the audit log transformation operations
     * @param message
     * @param direction
     * @param _interface
     * @return LogEventRequestType
     */
    protected LogEventRequestType auditRepositoryLogger(DocRetrieveMessageType message, String direction, String _interface) {
        return new AuditRepositoryLogger().logDocRetrieve(message, direction, _interface);
    }

    /**
     *
     * @param auditMsg Log a document retrieve deferred message received on the entity interface (entity or NHIN Proxy).
     * @param assertion Assertion information
     * @return Audit acknowledgement
     */
    public AcknowledgementType auditDocRetrieveDeferredRequest(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType auditMsg, AssertionType assertion) {
        if (debugEnabled) {
            log.debug("Entering DocRetrieveAuditLog.auditDocRetrieveRequest(...)");
        }
        DocRetrieveMessageType auditRequestMsg = new DocRetrieveMessageType();
        auditRequestMsg.setRetrieveDocumentSetRequest(auditMsg);
        auditRequestMsg.setAssertion(assertion);
        AcknowledgementType ack = logDocRetrieveDeferred(auditRequestMsg, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, assertion);
        if (debugEnabled) {
            log.debug("Exiting DocRetrieveAuditLog.auditDocRetrieveRequest(...)");
        }
        return ack;
    }
}
