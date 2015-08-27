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
package gov.hhs.fha.nhinc.audit;

import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * ATNA-compliant audit logger template class. Each individual service logger will implement this class, but provide its
 * own implementation.
 *
 * @author achidamb
 * @param <T>
 * @param <K>
 */
public abstract class AuditLogger<T, K> {

    private static final Logger LOG = Logger.getLogger(AuditLogger.class);

    /**
     * ATNA-compliant logging for a request message of type T
     * <P>
     * TODO: This method should be final, but cannot due to the way PD inbound JUnit tests have been written.
     *
     * @param request Request to be audited
     * @param assertion assertion to be audited
     * @param target target community
     * @param direction defines the Outbound/Inbound message
     * @param _interface Entity, Adapter or Nwhin
     * @param isRequesting true for initiator, false for responder
     * @param webContextProperties Properties loaded from message context
     * @param serviceName Name of the Service being audited
     */
    public void auditRequestMessage(T request, AssertionType assertion, NhinTargetSystemType target, String direction,
        String _interface, Boolean isRequesting, Properties webContextProperties, String serviceName) {

        LOG.trace("--- Before auditing of request message ---");
        LogEventRequestType auditLogMsg = getAuditTransforms().transformRequestToAuditMsg(request, assertion, target,
            direction, _interface, isRequesting, webContextProperties, serviceName);
        auditLogMessages(auditLogMsg, assertion);
        LOG.trace("--- After auditing of request message ---");
    }

    /**
     * ATNA-compliant logging for a response message of type K
     * <P>
     * TODO: This method should be final, but cannot due to the way PD inbound JUnit tests have been written.
     *
     * @param response Response to be audited
     * @param assertion assertion to be audited
     * @param target target community
     * @param direction defines the Outbound/Inbound message
     * @param _interface Entity, Adapter or Nwhin
     * @param isRequesting true/false identifies initiator/responder
     * @param webContextProperties Properties loaded from message context
     * @param serviceName Name of the Service being audited
     */
    public void auditResponseMessage(K response, AssertionType assertion, NhinTargetSystemType target, String direction,
        String _interface, Boolean isRequesting, Properties webContextProperties, String serviceName) {

        LOG.trace("--- Before auditing of response message ---");
        LogEventRequestType auditLogMsg = getAuditTransforms().transformResponseToAuditMsg(response, assertion,
            target, direction, _interface, isRequesting, webContextProperties, serviceName);
        auditLogMessages(auditLogMsg, assertion);
        LOG.trace("--- After auditing of response message ---");
    }

    private void auditLogMessages(LogEventRequestType auditLogMsg, AssertionType assertion) {
        if (auditLogMsg != null && auditLogMsg.getAuditMessage() != null) {
            new AuditRepositoryProxyObjectFactory().getAuditRepositoryProxy().auditLog(auditLogMsg, assertion);
        } else {
            LOG.error("auditLogMsg is null, no auditing will take place.");
        }
    }

    /**
     * Returns the AuditTransforms implementation needed for auditing the current service.
     *
     * @return a constructed AuditTransforms implementation
     */
    protected abstract AuditTransforms<T, K> getAuditTransforms();
}
