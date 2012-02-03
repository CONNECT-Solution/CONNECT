/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.subjectdiscovery;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectAddedMessageType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectReidentificationRequestMessageType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectReidentificationResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.NhinSubjectDiscoveryAckMessageType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectRevisedMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerMCCIIN000002UV01RequestType;
import org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PRPAIN201310UV02;

/**
 *
 * @author Jon Hoppesch
 */
public class SubjectDiscoveryAuditLogger {

    /**
     * Creates an audit log for a 201301 message.
     * @param request 201301 message to log
     * @param direction Indicates whether the message is going out or comming in
     * @param _interface Indicates which interface component is being logged??
     * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
     */
    public AcknowledgementType audit201301(PIXConsumerPRPAIN201301UVRequestType request, String direction, String _interface) {
        AcknowledgementType ack = new AcknowledgementType();

        SubjectAddedMessageType message = new SubjectAddedMessageType();
        message.setAssertion(request.getAssertion());
        message.setPRPAIN201301UV02(request.getPRPAIN201301UV02());

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logSubjectAdded(message, direction, _interface);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, request.getAssertion());
        }

        return ack;
    }

    /**
     * Creates an audit log for a 201302 message.
     * @param request 201302 message to log
     * @param direction Indicates whether the message is going out or comming in
     * @param _interface Indicates which interface component is being logged??
     * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
     */
    public AcknowledgementType audit201302(PIXConsumerPRPAIN201302UVRequestType request, String direction, String _interface) {
        AcknowledgementType ack = new AcknowledgementType();

        SubjectRevisedMessageType message = new SubjectRevisedMessageType();
        message.setAssertion(request.getAssertion());
        message.setPRPAIN201302UV02(request.getPRPAIN201302UV02());

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logSubjectRevised(message, direction, _interface);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, request.getAssertion());
        }

        return ack;
    }

    /**
     * Creates an audit log for a 201309 message.
     * @param request 201309 message to log
     * @param direction Indicates whether the message is going out or comming in
     * @param _interface Indicates which interface component is being logged??
     * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
     */
    public AcknowledgementType audit201309(PIXConsumerPRPAIN201309UVRequestType request, String direction, String _interface) {
        AcknowledgementType ack = new AcknowledgementType();

        SubjectReidentificationRequestMessageType message = new SubjectReidentificationRequestMessageType();
        message.setAssertion(request.getAssertion());
        message.setPRPAIN201309UV02(request.getPRPAIN201309UV02());

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logSubjectReident(message, direction, _interface);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, request.getAssertion());
        }

        return ack;
    }

    /**
     * Creates an audit log for a 201310 message.
     * @param request 201310 message to log
     * @param direction Indicates whether the message is going out or comming in
     * @param _interface Indicates which interface component is being logged??
     * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
     */
    public AcknowledgementType audit201310(PRPAIN201310UV02 request, String direction, String _interface, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();

        SubjectReidentificationResponseMessageType message = new SubjectReidentificationResponseMessageType();
        message.setPRPAIN201310UV02(request);

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logSubjectReidentResult(message, direction, _interface);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    /**
     * Creates an audit log for a 201310 message.
     * @param request 201310 message to log
     * @param direction Indicates whether the message is going out or comming in
     * @param _interface Indicates which interface component is being logged??
     * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
     */
    public AcknowledgementType auditNhinAck(MCCIIN000002UV01 request, AssertionType assertion, String direction, String _interface) {
        AcknowledgementType ack = new AcknowledgementType();

        PIXConsumerMCCIIN000002UV01RequestType ackReq = new PIXConsumerMCCIIN000002UV01RequestType();
        ackReq.setAssertion(assertion);
        ackReq.setMCCIIN000002UV01(request);

        NhinSubjectDiscoveryAckMessageType message = new NhinSubjectDiscoveryAckMessageType();
        message.setPIXConsumerMCCIIN000002UV01Request(ackReq);

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logNhinSubjectDiscoveryAck(message, direction, _interface);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    /**
     * Creates an audit log for an AdhocQueryRequest or AdhocQueryResponse
     * @param auditLogMsg AdhocQueryRequest or AdhocQueryResponse message to log.
     * @return Returns an AcknowledgementType object indicating whether the audit message was successfully stored.
     */
    private AcknowledgementType audit(LogEventRequestType auditLogMsg, AssertionType assertion)
    {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy.auditLog(auditLogMsg, assertion);
    }
}
