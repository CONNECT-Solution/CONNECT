/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author jhoppesc
 */
public class PatientDiscoveryAuditLog {
    private Log log = null;//LogFactory.getLog(PatientDiscoveryTransforms.class);

    /**
     * Default Constructor
     */
    public PatientDiscoveryAuditLog()
    {
        log = createLogger();
    }

    public AcknowledgementType auditProxyRequest(ProxyPRPAIN201305UVProxySecuredRequestType request, AssertionType assertion) {
         AcknowledgementType ack = new AcknowledgementType();
        if (request == null)
        {
            addLogError("Unable to create an audit log record for the proxy. The incomming request was null.");
        }
        else if (assertion == null)
        {
            addLogError("Unable to create an audit log record for the proxy. The incomming request assertion was null.");
        }
        else
        {
//            PatientDiscoveryTransforms oPatientDiscoveryTransforms = new PatientDiscoveryTransforms();
            LogEventRequestType message = getLogEventRequestTypeForProxyRequestMessage( request,assertion);
            ack = logPatientDiscoveryRequest(message, assertion);
        }
        return ack;
    }

    public AcknowledgementType auditProxyResponse(PRPAIN201306UV02 response, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();
        if (response == null)
        {
            addLogError("Unable to create an audit log record for the proxy. The incomming response was null.");
        }
        else if (assertion == null)
        {
            addLogError("Unable to create an audit log record for the proxy. The incomming response assertion was null.");
        }
        else
        {
            LogEventRequestType message = getLogEventRequestTypeForProxyResponseMessage(response, assertion);
            ack = logPatientDiscoveryResponse(message, assertion);
        }
        return ack;
    }

    public AcknowledgementType auditEntityRequest(RespondingGatewayPRPAIN201305UV02RequestType request) {
        AcknowledgementType ack = new AcknowledgementType();
        if (request == null)
        {
            addLogError("Unable to create an audit log record for the entity. The incomming request was null.");
        }
        else
        {
            LogEventRequestType message = getLogEventRequestTypeForEntityRequestMessage(request);
            ack = logPatientDiscoveryRequest(message, request.getAssertion());
        }

        return ack;
    }

    public AcknowledgementType auditEntityResponse(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();
        if (response == null)
        {
            addLogError("Unable to create an audit log record for the entity. The incomming response was null.");
        }
        else if (assertion == null)
        {
            addLogError("Unable to create an audit log record for the entity. The incomming response assertion was null.");
        }
        else
        {
            LogEventRequestType message = getLogEventRequestTypeForEntityResponseMessage(response, assertion);
            ack = logPatientDiscoveryResponse(message, assertion);
        }
        
        return ack;
    }
    
    public AcknowledgementType logPatientDiscoveryRequest (LogEventRequestType auditLogRequest, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();

        if (auditLogRequest == null)
        {
            addLogError("There was a problem creating an audit log for the request (LogEventRequestType parameter was null). The audit record was not created.");
        }
        else if(assertion == null)
        {
            addLogError("There was a problem creating an audit log for the request (AssertionType parameter was null). The audit record was not created.");
        }
        else
        {
            AuditRepositoryProxyObjectFactory auditRepoFactory = getAuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = getAuditRepositoryProxy(auditRepoFactory);
            ack = getAuditLogProxyResponse(proxy, auditLogRequest, assertion);
        }

        return ack;
    }
    public AcknowledgementType logPatientDiscoveryResponse (LogEventRequestType auditLogRequest, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();

        if (auditLogRequest == null)
        {
            addLogError("There was a problem creating an audit log for the response (LogEventRequestType parameter was null). The audit record was not created.");
        }
        else if (assertion == null)
        {
            addLogError("There was a problem creating an audit log for the response (AssertionType parameter was null). The audit record was not created.");
        }
        else
        {
            AuditRepositoryProxyObjectFactory auditRepoFactory = getAuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = getAuditRepositoryProxy(auditRepoFactory);
            addLogDebug("Calling the audit log proxy to create the audit log.");
            ack = getAuditLogProxyResponse(proxy, auditLogRequest, assertion);
        }

        return ack;
    }

    /**
     * Instantiating log4j logger
     * @return
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected AcknowledgementType getAuditLogProxyResponse(AuditRepositoryProxy proxy, LogEventRequestType auditLogRequest, AssertionType assertion) {
        AcknowledgementType ack;
        ack = proxy.auditLog(auditLogRequest, assertion);
        return ack;
    }

    protected AuditRepositoryProxy getAuditRepositoryProxy(AuditRepositoryProxyObjectFactory auditRepoFactory) {
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy;
    }

    protected AuditRepositoryProxyObjectFactory getAuditRepositoryProxyObjectFactory() {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        return auditRepoFactory;
    }

    protected LogEventRequestType getLogEventRequestTypeForEntityRequestMessage(RespondingGatewayPRPAIN201305UV02RequestType request) {
        LogEventRequestType message = new PatientDiscoveryTransforms().transformEntityPRPAIN201305RequestToAuditMsg(request, request.getAssertion(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        return message;
    }

    protected LogEventRequestType getLogEventRequestTypeForEntityResponseMessage(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion) {
//        LogEventRequestType message = new PatientDiscoveryTransforms().transformEntityPRPAIN201306ResponseToAuditMsg(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        LogEventRequestType message = new PatientDiscoveryTransforms().transformEntityPRPAIN201306ResponseToAuditMsg(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        return message;
    }

    protected LogEventRequestType getLogEventRequestTypeForProxyRequestMessage(ProxyPRPAIN201305UVProxySecuredRequestType request, AssertionType assertion) {
//            PatientDiscoveryTransforms oPatientDiscoveryTransforms = new PatientDiscoveryTransforms();
        LogEventRequestType message = new PatientDiscoveryTransforms().transformNhinPRPAIN201305RequestToAuditMsg(request.getPRPAIN201305UV02(), assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        return message;
    }

    protected LogEventRequestType getLogEventRequestTypeForProxyResponseMessage(PRPAIN201306UV02 response, AssertionType assertion) {
        LogEventRequestType message = new PatientDiscoveryTransforms().transformNhinPRPAIN201306ResponseToAuditMsg(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        return message;
    }

    private void addLogInfo(String message){
        log.info(message);
    }

    private void addLogDebug(String message){
        log.debug(message);
    }

    private void addLogError(String message){
        log.error(message);
    }

}
