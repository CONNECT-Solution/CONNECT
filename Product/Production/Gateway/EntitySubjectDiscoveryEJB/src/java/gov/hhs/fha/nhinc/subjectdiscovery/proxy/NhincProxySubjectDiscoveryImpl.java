/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subjectdiscovery.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.subjectdiscovery.SubjectDiscoveryAuditLog;
import gov.hhs.fha.nhinc.nhinsubjectdiscovery.proxy.NhinSubjectDiscoveryProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinsubjectdiscovery.proxy.NhinSubjectDiscoveryProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerPRPAIN201301UVProxyRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201303UVProxyRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVProxyRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201310UVRequestType;
import org.hl7.v3.PIXConsumerMCCIIN000002UV01RequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author mflynn02
 */
public class NhincProxySubjectDiscoveryImpl {

    private static Log log = LogFactory.getLog(NhincProxySubjectDiscoveryImpl.class);

    /**
     * This method will perform an subject announce to a specified community on the Nhin Interface
     * and return acknowledgement message
     *
     * @param request The subject announce
     * @return Acknowledgement message
     */
    public MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PIXConsumerPRPAIN201301UVProxyRequestType request) {
        log.debug("Entering NhincProxySubjectDiscoveryImpl.pixConsumerPRPAIN201301UV...");
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        // Audit the Subject Announce Request Message sent on the Nhin Interface
        SubjectDiscoveryAuditLog auditLog = new SubjectDiscoveryAuditLog();
        AcknowledgementType ack = auditLog.audit(request);

        NhinSubjectDiscoveryProxyObjectFactory subjectDiscoveryFactory = new NhinSubjectDiscoveryProxyObjectFactory();
        NhinSubjectDiscoveryProxy proxy = subjectDiscoveryFactory.getNhinSubjectDiscoveryProxy();

        response = proxy.pixConsumerPRPAIN201301UV(request.getPRPAIN201301UV(), request.getAssertion(), request.getNhinTargetSystem());

        // Audit the Subject Added Response Message received on the Nhin Interface
        PIXConsumerMCCIIN000002UV01RequestType auditMsg = new PIXConsumerMCCIIN000002UV01RequestType();
        auditMsg.setMCCIIN000002UV01(response);
        auditMsg.setAssertion(request.getAssertion());
        ack = auditLog.audit(auditMsg, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        log.debug("Exiting NhincProxySubjectDiscoveryImpl.pixConsumerPRPAIN201301UV...");
        return response;
    }
    /**
     * This method will perform an subject revoke to a specified community on the Nhin Interface
     * and return an acknowledgment message.
     *
     * @param request The subject revoke message
     * @return Acknowledgment message
     */
    public MCCIIN000002UV01 pixConsumerPRPAIN201303UV(PIXConsumerPRPAIN201303UVProxyRequestType request) {
        log.debug("Entering NhincProxySubjectDiscoveryImpl.pixConsumerPRPAIN201303UV...");
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        // Audit the Subject Revoke Request Message sent on the Nhin Interface
        SubjectDiscoveryAuditLog auditLog = new SubjectDiscoveryAuditLog();
        AcknowledgementType ack = auditLog.audit(request);

        NhinSubjectDiscoveryProxyObjectFactory subjectDiscoveryFactory = new NhinSubjectDiscoveryProxyObjectFactory();
        NhinSubjectDiscoveryProxy proxy = subjectDiscoveryFactory.getNhinSubjectDiscoveryProxy();

        response = proxy.pixConsumerPRPAIN201303UV(request.getPRPAIN201303UV(), request.getAssertion(), request.getNhinTargetSystem());

        // Audit the Subject Revoked Response Message received on the Nhin Interface
        PIXConsumerMCCIIN000002UV01RequestType auditMsg = new PIXConsumerMCCIIN000002UV01RequestType();
        auditMsg.setMCCIIN000002UV01(response);
        auditMsg.setAssertion(request.getAssertion());
        ack = auditLog.audit(auditMsg, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        log.debug("Exiting NhincProxySubjectDiscoveryImpl.pixConsumerPRPAIN201303UV...");
        return response;
    }

    /**
     * This method will perform an subject reidentification to a specified community on the Nhin Interface
     * and return an reidentifiction response message.
     *
     * @param request The subject reidentification
     * @return Reidentification Response message
     */
    public org.hl7.v3.PRPAIN201310UV pixConsumerPRPAIN201309UV(PIXConsumerPRPAIN201309UVProxyRequestType request) {
        log.debug("Entering NhincProxySubjectDiscoveryImpl.pixConsumerPRPAIN201309UV...");
        org.hl7.v3.PRPAIN201310UV response = new org.hl7.v3.PRPAIN201310UV();

        // Audit the Subject Revoke Request Message sent on the Nhin Interface
        SubjectDiscoveryAuditLog auditLog = new SubjectDiscoveryAuditLog();
        AcknowledgementType ack = auditLog.audit(request);

        NhinSubjectDiscoveryProxyObjectFactory subjectDiscoveryFactory = new NhinSubjectDiscoveryProxyObjectFactory();
        NhinSubjectDiscoveryProxy proxy = subjectDiscoveryFactory.getNhinSubjectDiscoveryProxy();

        response = proxy.pixConsumerPRPAIN201309UV(request.getPRPAIN201309UV(), request.getAssertion(), request.getNhinTargetSystem());

        // Audit the Subject Revoked Response Message received on the Nhin Interface
        PIXConsumerPRPAIN201310UVRequestType auditMsg = new PIXConsumerPRPAIN201310UVRequestType();
        auditMsg.setPRPAIN201310UV(response);
        auditMsg.setAssertion(request.getAssertion());
        ack = auditLog.audit(auditMsg);

        log.debug("Exiting NhincProxySubjectDiscoveryImpl.pixConsumerPRPAIN201303UV...");
        return response;
    }

}
