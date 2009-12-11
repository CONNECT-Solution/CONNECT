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
        PatientDiscoveryTransforms oPatientDiscoveryTransforms = new PatientDiscoveryTransforms();
        LogEventRequestType message = oPatientDiscoveryTransforms.transformNhinPRPAIN201305RequestToAuditMsg(request.getPRPAIN201305UV02(), assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        AcknowledgementType ack = logPatientDiscoveryRequest(message, assertion);//logPatientDiscoveryRequest(message, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return ack;
    }

    public AcknowledgementType auditProxyResponse(PRPAIN201306UV02 request, AssertionType assertion) {
        LogEventRequestType message = new PatientDiscoveryTransforms().transformNhinPRPAIN201306ResponseToAuditMsg(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        AcknowledgementType ack = logPatientDiscoveryResponse(message, assertion);//logPatientDiscoveryResponse(message, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return ack;
    }

    public AcknowledgementType auditEntityRequest(RespondingGatewayPRPAIN201305UV02RequestType request) {
        LogEventRequestType message = new PatientDiscoveryTransforms().transformEntityPRPAIN201305RequestToAuditMsg(request, request.getAssertion(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        AcknowledgementType ack = logPatientDiscoveryRequest(message, request.getAssertion());//, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return ack;
    }

    public AcknowledgementType auditEntityResponse(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion) {
        LogEventRequestType message = new PatientDiscoveryTransforms().transformEntityPRPAIN201306ResponseToAuditMsg(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        AcknowledgementType ack = logPatientDiscoveryResponse(message, assertion);

        return ack;
    }
    
    public AcknowledgementType logPatientDiscoveryRequest (LogEventRequestType auditLogRequest, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();

        if (auditLogRequest != null) {
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            ack = proxy.auditLog(auditLogRequest, assertion);
        }
        else
        {
            addLogError("There was a problem creating and audit log for the request. The audit record was not created.");
        }

        return ack;
    }
    public AcknowledgementType logPatientDiscoveryResponse (LogEventRequestType auditLogRequest, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();

        if (auditLogRequest != null) {
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            ack = proxy.auditLog(auditLogRequest, assertion);
        }
        else
        {
            addLogError("There was a problem creating and audit log for the response. The audit record was not created.");
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
