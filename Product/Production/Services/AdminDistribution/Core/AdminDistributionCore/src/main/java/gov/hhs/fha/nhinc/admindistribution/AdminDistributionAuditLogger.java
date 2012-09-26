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
package gov.hhs.fha.nhinc.admindistribution;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;

/**
 * 
 * @author dunnek
 */
public class AdminDistributionAuditLogger {
    private Log log = null;

    public AdminDistributionAuditLogger() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    private AcknowledgementType audit(LogEventRequestType auditLogMsg, AssertionType assertion) {
        log.debug("begin audit()");

        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy.auditLog(auditLogMsg, assertion);
    }

    public AcknowledgementType auditEntityAdminDist(RespondingGatewaySendAlertMessageType request,
            AssertionType assertion, String direction) {
        log.debug("begin auditEntityAdminDist() " + direction);
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logEntityAdminDist(request, assertion, direction);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }
        log.debug("end auditEntityAdminDist() " + direction);
        return ack;
    }

    public AcknowledgementType auditNhincAdminDist(EDXLDistribution body, AssertionType assertion,
            NhinTargetSystemType target, String direction) {
        log.debug("begin auditNhincAdminDist() " + direction);
        AcknowledgementType ack = null;
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

        LogEventRequestType auditLogMsg = auditLogger.logNhincAdminDist(body, assertion, target, direction);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        } else {
            log.warn("Ack was null");
        }
        log.debug("end auditNhincAdminDist() " + direction);
        return ack;
    }

    public AcknowledgementType auditNhinAdminDist(EDXLDistribution body, AssertionType assertion, String direction,
            String logInterface) {
        log.debug("begin auditNhinAdminDist() " + direction);
        AcknowledgementType ack = null;
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

        log.debug("body == null = " + body == null);
        log.debug("assertion == null = " + assertion == null);

        LogEventRequestType auditLogMsg = auditLogger.logNhincAdminDist(body, assertion, direction, logInterface);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        } else {
            log.warn("Ack was null");
        }
        log.debug("end auditNhinAdminDist() " + direction);
        return ack;
    }

}
