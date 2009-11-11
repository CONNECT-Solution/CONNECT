/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subjectdiscovery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PIXConsumerMCCIIN000002UV01RequestType;
import org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201301UVProxyRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVProxyRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201310UVRequestType;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectAddedMessageType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectReidentificationRequestMessageType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectReidentificationResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.NhinSubjectDiscoveryAckMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201301Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201309Transforms;
import java.util.List;

/**
 *
 * @author mflynn02
 */
public class SubjectDiscoveryAuditLog {

    private static Log log = LogFactory.getLog(SubjectDiscoveryAuditLog.class);

    public AcknowledgementType audit(PIXConsumerPRPAIN201301UVRequestType request) {

        SubjectAddedMessageType message = new SubjectAddedMessageType();
        message.setPRPAIN201301UV02(request.getPRPAIN201301UV02());
        message.setAssertion(request.getAssertion());
        AcknowledgementType ack = logSubjectAdded(message, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        return ack;

    }

    public AcknowledgementType audit(PIXConsumerPRPAIN201301UVProxyRequestType request) {

        SubjectAddedMessageType message = new SubjectAddedMessageType();
        PIXConsumerPRPAIN201301UVProxyRequestType logRequest = new PIXConsumerPRPAIN201301UVProxyRequestType();
        if (request.getPRPAIN201301UV02() != null) {
            org.hl7.v3.PRPAIN201301UV02 input = request.getPRPAIN201301UV02();
            if (input.getControlActProcess() != null &&
                    input.getControlActProcess().getSubject() != null) {
                List<org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = input.getControlActProcess().getSubject();
                if (subjects.get(0).getRegistrationEvent() != null) {
                    if (subjects.get(0).getRegistrationEvent().getSubject1() != null) {
                        if (subjects.get(0).getRegistrationEvent().getSubject1().getPatient() != null) {
                            logRequest.setPRPAIN201301UV02(HL7PRPA201301Transforms.createPRPA201301(subjects.get(0).getRegistrationEvent().getSubject1().getPatient(),
                                    null, input.getSender().getDevice().getId().get(0).getRoot(),
                                    input.getReceiver().get(0).getDevice().getId().get(0).getRoot()));
                        }
                    }

                }
            }
        }
        message.setPRPAIN201301UV02(logRequest.getPRPAIN201301UV02());
        message.setAssertion(request.getAssertion());

        AcknowledgementType ack = logSubjectAdded(message, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        return ack;

    }

    public AcknowledgementType audit(PIXConsumerPRPAIN201309UVRequestType request) {

        SubjectReidentificationRequestMessageType message = new SubjectReidentificationRequestMessageType();
        message.setPRPAIN201309UV02(request.getPRPAIN201309UV02());
        message.setAssertion(request.getAssertion());
        AcknowledgementType ack = logSubjectReidentification(message, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        return ack;

    }

    public AcknowledgementType audit(PIXConsumerPRPAIN201309UVProxyRequestType request) {
        SubjectReidentificationRequestMessageType message = new SubjectReidentificationRequestMessageType();
        AcknowledgementType ack = new AcknowledgementType();
        
        if (request.getPRPAIN201309UV02() != null &&
                request.getAssertion() != null) {
            message.setPRPAIN201309UV02(request.getPRPAIN201309UV02());
            message.setAssertion(request.getAssertion());
            ack = logSubjectReidentification(message, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }
        return ack;

    }

    public AcknowledgementType audit(PIXConsumerPRPAIN201310UVRequestType request) {

        SubjectReidentificationResponseMessageType message = new SubjectReidentificationResponseMessageType();
        message.setPRPAIN201310UV02(request.getPRPAIN201310UV02());
        AcknowledgementType ack = logSubjectReidentificationResponse(message, request.getAssertion(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        return ack;

    }

    public AcknowledgementType audit(PIXConsumerMCCIIN000002UV01RequestType request) {
        NhinSubjectDiscoveryAckMessageType message = new NhinSubjectDiscoveryAckMessageType();
        message.setPIXConsumerMCCIIN000002UV01Request(request);
        AcknowledgementType ack = logSubjectResponse(message, request.getAssertion(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        return ack;

    }

    public AcknowledgementType audit(PIXConsumerMCCIIN000002UV01RequestType request, String direction, String _interface) {

        NhinSubjectDiscoveryAckMessageType message = new NhinSubjectDiscoveryAckMessageType();
        message.setPIXConsumerMCCIIN000002UV01Request(request);

        AcknowledgementType ack = logSubjectResponse(message, request.getAssertion(), direction, _interface);

        return ack;

    }

    private AcknowledgementType logSubjectAdded(SubjectAddedMessageType message, String direction, String _interface) {
        AcknowledgementType ack = new AcknowledgementType();
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logSubjectAdded(message, direction, _interface);

        if (auditLogMsg != null) {
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            ack = proxy.auditLog(auditLogMsg, message.getAssertion());
        }
        return ack;
    }

    private AcknowledgementType logSubjectReidentification(SubjectReidentificationRequestMessageType message, String direction, String _interface) {
        AcknowledgementType ack = new AcknowledgementType();
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logSubjectReident(message, direction, _interface);

        if (auditLogMsg != null) {
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            ack = proxy.auditLog(auditLogMsg, message.getAssertion());
        }
        return ack;
    }

    private AcknowledgementType logSubjectReidentificationResponse(SubjectReidentificationResponseMessageType message, AssertionType assertion, String direction, String _interface) {
        AcknowledgementType ack = new AcknowledgementType();
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logSubjectReidentResult(message, direction, _interface);

        if (auditLogMsg != null) {
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            ack = proxy.auditLog(auditLogMsg, assertion);
        }
        return ack;
    }

    private AcknowledgementType logSubjectResponse(NhinSubjectDiscoveryAckMessageType message, AssertionType assertion, String direction, String _interface) {
        AcknowledgementType ack = new AcknowledgementType();
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logNhinSubjectDiscoveryAck(message, direction, _interface);

        if (auditLogMsg != null) {
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            ack = proxy.auditLog(auditLogMsg, assertion);
        }
        return ack;
    }
}
