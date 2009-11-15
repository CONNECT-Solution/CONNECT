/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;

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
//        AcknowledgementType ack = logPatientDiscoveryRequest(message, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return ack;
    }

    public AcknowledgementType auditProxyResponse(PRPAIN201306UV02 request, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();
//        SubjectAddedMessageType message = new SubjectAddedMessageType();
//        message.setPRPAIN201301UV02(request.getPRPAIN201301UV02());
//        message.setAssertion(request.getAssertion());
//        AcknowledgementType ack = logPatientDiscoveryResponse(message, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return ack;
    }
    
//    public AcknowledgementType logPatientDiscoveryRequest (, String direction, String _interface) {
//        AcknowledgementType ack = new AcknowledgementType();
//        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
//        LogEventRequestType auditLogMsg = auditLogger.(message, direction, _interface);
//
//        if (auditLogMsg != null) {
//            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
//            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
//            ack = proxy.auditLog(auditLogMsg, message.getAssertion());
//        }
//        return ack;
//    }
//    public AcknowledgementType logPatientDiscoveryResponse (, String direction, String _interface) {
//        AcknowledgementType ack = new AcknowledgementType();
//        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
//        LogEventRequestType auditLogMsg = auditLogger.(message, direction, _interface);
//
//        if (auditLogMsg != null) {
//            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
//            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
//            ack = proxy.auditLog(auditLogMsg, message.getAssertion());
//        }
//        return ack;
//    }
}
