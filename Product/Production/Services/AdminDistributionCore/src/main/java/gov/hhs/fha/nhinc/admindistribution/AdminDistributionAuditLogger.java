/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class AdminDistributionAuditLogger {

    private static final Logger LOG = LoggerFactory.getLogger(AdminDistributionAuditLogger.class);

    private static AcknowledgementType audit(LogEventRequestType auditLogMsg, AssertionType assertion) {
        LOG.debug("begin audit()");

        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy.auditLog(auditLogMsg, assertion);
    }

    /**
     * This method audits the Entity AdminDist.
     *
     * @param request SendAlertMessage received.
     * @param assertion Assertion received.
     * @param direction The direction could be eigther outbound or inbound.
     * @return ack Acknowledgement
     */
    public AcknowledgementType auditEntityAdminDist(RespondingGatewaySendAlertMessageType request,
        AssertionType assertion, String direction) {

        LOG.debug("begin auditEntityAdminDist() " + direction);
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logEntityAdminDist(request, assertion, direction);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }
        LOG.debug("end auditEntityAdminDist() " + direction);
        return ack;
    }

    /**
     * This method audits the MsgProxy AdminDist.
     *
     * @param body Emergency Message Distribution Element transaction body received.
     * @param assertion Assertion received.
     * @param target Target community to send/receive to be audited.
     * @param direction The direction could be eigther outbound or indound.
     * @return ack Acknowledgement.
     */
    public AcknowledgementType auditNhincAdminDist(EDXLDistribution body, AssertionType assertion,
        NhinTargetSystemType target, String direction) {

        LOG.debug("begin auditNhincAdminDist() " + direction);
        AcknowledgementType ack = null;
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

        LogEventRequestType auditLogMsg = auditLogger.logNhincAdminDist(body, assertion, target, direction,
            NhincConstants.AUDIT_LOG_PROXY_INTERFACE);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        } else {
            LOG.warn("Ack was null");
        }
        LOG.debug("end auditNhincAdminDist() " + direction);
        return ack;
    }

    /**
     * This method audits the Nhin AdminDist.
     *
     * @param body Emergency Message Distribution Element transaction body received.
     * @param assertion Assertion received.
     * @param direction The direction could be outbound/inbound.
     * @param target
     * @param logInterface The logInterface could be Adapter/Entity/MsgProxy.
     * @return ack Acknowledgement.
     */
    public AcknowledgementType auditNhinAdminDist(EDXLDistribution body, AssertionType assertion, String direction,
        NhinTargetSystemType target, String logInterface) {

        LOG.debug("begin auditNhinAdminDist() " + direction);
        AcknowledgementType ack = null;
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

        LOG.debug("body == null = " + (body == null));
        LOG.debug("assertion == null = " + (assertion == null));

        LogEventRequestType auditLogMsg = auditLogger.logNhincAdminDist(body, assertion, target, direction,
            logInterface);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        } else {
            LOG.warn("Ack was null");
        }
        LOG.debug("end auditNhinAdminDist() " + direction);
        return ack;
    }
}
