/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.corex12.docsubmission.audit;

import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.transform.CORE_X12AuditDataTransform;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Class provides APIs to persist Audit data
 *
 * @author svalluripalli
 */
public class CORE_X12AuditLogger {

    private static final Logger LOG = Logger.getLogger(CORE_X12AuditLogger.class);
    private final CORE_X12AuditDataTransform coreX12AuditDataTransform = new CORE_X12AuditDataTransform();

    /**
     *
     * @param message
     * @param assertion
     * @param target
     * @param direction
     * @param isRequesting
     * @param webContextProperties
     * @param serviceName
     */
    public void auditNhinCoreX12RealtimeMessage(Object message, AssertionType assertion, NhinTargetSystemType target,
        String direction, boolean isRequesting, Properties webContextProperties, String serviceName) {
        LOG.trace("---Begin CORE_X12AuditLogger.auditNhinCoreX12RealtimeMessage()---");
        // Set up the audit logging request message
        LogEventRequestType auditLogMsg = coreX12AuditDataTransform.transformX12MsgToAuditMsg(message, assertion, target, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, isRequesting, webContextProperties, serviceName);
        if (auditLogMsg != null && auditLogMsg.getAuditMessage() != null) {
            audit(auditLogMsg, assertion);
        } else {
            LOG.error("Core X12 Realtime Request auditLogMsg is null");
        }
        LOG.trace("---End CORE_X12AuditLogger.auditNhinCoreX12RealtimeMessage()---");
    }

    /**
     *
     * @param message
     * @param assertion
     * @param target
     * @param direction
     * @param isRequesting
     * @param webContextProperties
     * @param serviceName
     */
    public void auditNhinCoreX12BatchMessage(Object message, AssertionType assertion, NhinTargetSystemType target,
        String direction, boolean isRequesting, Properties webContextProperties, String serviceName) {
        LOG.trace("---Begin CORE_X12AuditLogger.auditNhinCoreX12BatchRequest()---");
        LogEventRequestType auditLogMsg = coreX12AuditDataTransform.transformX12MsgToAuditMsg(message, assertion, target, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, isRequesting, webContextProperties, serviceName);
        if (auditLogMsg != null && auditLogMsg.getAuditMessage() != null) {
            audit(auditLogMsg, assertion);
        } else {
            LOG.error("Core X12 Nhin Batch Request auditLogMsg is null");
        }
        LOG.trace("---End CORE_X12AuditLogger.auditNhinCoreX12BatchRequest()---");
    }

    /**
     * Submits a generic Audit Log message to the Audit Log Repository.
     *
     * @param auditLogMsg The generic audit log to be audited
     * @param assertion The assertion to be audited
     * @return
     */
    private AcknowledgementType audit(LogEventRequestType auditLogMsg, AssertionType assertion) {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy.auditLog(auditLogMsg, assertion);
    }
}
