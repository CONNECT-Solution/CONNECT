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
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author jhoppesc
 */
public class PatientDiscoveryAuditLog {

    public AcknowledgementType auditProxyRequest(ProxyPRPAIN201305UVProxySecuredRequestType request) {
        AcknowledgementType ack = new AcknowledgementType();
//        SubjectAddedMessageType message = new SubjectAddedMessageType();
//        message.setPRPAIN201301UV02(request.getPRPAIN201301UV02());
//        message.setAssertion(request.getAssertion());
//        LogEventRequestType message = new PatientDiscoveryTransforms().transformNHINPRPAIN201305RequestToAuditMsg(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
//        AcknowledgementType ack = logPatientDiscoveryRequest(message, assertion);//logPatientDiscoveryRequest(message, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return ack;
    }

    public AcknowledgementType auditProxyResponse(PRPAIN201306UV02 request, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();
//        SubjectAddedMessageType message = new SubjectAddedMessageType();
//        message.setPRPAIN201301UV02(request.getPRPAIN201301UV02());
        LogEventRequestType message = new PatientDiscoveryTransforms().transformNhinPRPAIN201306ResponseToAuditMsg(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
//        AcknowledgementType ack = logPatientDiscoveryResponse(message, assertion);//logPatientDiscoveryResponse(message, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return ack;
    }

    public AcknowledgementType auditEntityRequest(RespondingGatewayPRPAIN201305UV02RequestType request) {
//        AcknowledgementType ack = new AcknowledgementType();
        LogEventRequestType message = new PatientDiscoveryTransforms().transformEntityPRPAIN201305RequestToAuditMsg(request, request.getAssertion(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
//        message.setPRPAIN201301UV02(request.getPRPAIN201301UV02());
//        message.setAssertion(request.getAssertion());
        AcknowledgementType ack = logPatientDiscoveryRequest(message, request.getAssertion());//, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return ack;
    }

    public AcknowledgementType auditEntityResponse(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion) {
//        AcknowledgementType ack = new AcknowledgementType();
//        SubjectAddedMessageType message = new SubjectAddedMessageType();
//        message.setPRPAIN201301UV02(request.getPRPAIN201301UV02());
//        message.setAssertion(request.getAssertion());
        LogEventRequestType message = new PatientDiscoveryTransforms().transformEntityPRPAIN201306ResponseToAuditMsg(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        AcknowledgementType ack = logPatientDiscoveryResponse(message, assertion);

        return ack;
    }
    
    public AcknowledgementType logPatientDiscoveryRequest (LogEventRequestType auditLogRequest, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();
//        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
//        LogEventRequestType auditLogMsg = auditLogger.(message, direction, _interface);

        if (auditLogRequest != null) {
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            ack = proxy.auditLog(auditLogRequest, assertion);
        }
        return ack;
    }
    public AcknowledgementType logPatientDiscoveryResponse (LogEventRequestType auditLogRequest, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();
//        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
//        LogEventRequestType auditLogMsg = auditLogger.(message, direction, _interface);

        if (auditLogRequest != null) {
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            ack = proxy.auditLog(auditLogRequest, assertion);
        }
        return ack;
    }
}
