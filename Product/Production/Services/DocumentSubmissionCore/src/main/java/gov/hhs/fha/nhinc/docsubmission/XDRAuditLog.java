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
package gov.hhs.fha.nhinc.docsubmission;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.log4j.Logger;

import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;

/**
 *
 * @author dunnek
 */
public class XDRAuditLog {
    private static final Logger LOG = Logger.getLogger(XDRAuditLog.class);

    public AcknowledgementType auditProxyRequest(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();
        if (request == null) {
            LOG.error("Unable to create an audit log record for the proxy. The incomming request was null.");
        } else if (assertion == null) {
            LOG.error("Unable to create an audit log record for the proxy. The incomming request assertion was null.");
        } else {
            LogEventRequestType message = getLogEventRequestTypeForProxyRequestMessage(request, assertion);
            ack = logXDRRequest(message, assertion);
        }
        return ack;
    }

    public AcknowledgementType auditProxyResponse(RegistryResponseType response, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();
        if (response == null) {
            LOG.error("Unable to create an audit log record for the proxy. The incomming response was null.");
        } else if (assertion == null) {
            LOG.error("Unable to create an audit log record for the proxy. The incomming response assertion was null.");
        } else {
            LogEventRequestType message = getLogEventRequestTypeForProxyResponseMessage(response, assertion);
            ack = logXDRResponse(message, assertion);
        }
        return ack;
    }

    public AcknowledgementType logXDRRequest(LogEventRequestType auditLogRequest, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();

        if (auditLogRequest == null) {
            LOG.error("There was a problem creating an audit log for the request (LogEventRequestType parameter was null). The audit record was not created.");
        } else if (assertion == null) {
            LOG.error("There was a problem creating an audit log for the request (AssertionType parameter was null). The audit record was not created.");
        } else {
            AuditRepositoryProxyObjectFactory auditRepoFactory = getAuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = getAuditRepositoryProxy(auditRepoFactory);
            ack = getAuditLogProxyResponse(proxy, auditLogRequest, assertion);
        }

        return ack;
    }

    public AcknowledgementType logXDRResponse(LogEventRequestType auditLogRequest, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();

        if (auditLogRequest == null) {
            LOG.error("There was a problem creating an audit log for the response (LogEventRequestType parameter was null). The audit record was not created.");
        } else if (assertion == null) {
            LOG.error("There was a problem creating an audit log for the response (AssertionType parameter was null). The audit record was not created.");
        } else {
            AuditRepositoryProxyObjectFactory auditRepoFactory = getAuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = getAuditRepositoryProxy(auditRepoFactory);
            LOG.debug("Calling the audit log proxy to create the audit log.");
            ack = getAuditLogProxyResponse(proxy, auditLogRequest, assertion);
        }

        return ack;
    }

    protected LogEventRequestType getLogEventRequestTypeForProxyRequestMessage(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {

        LogEventRequestType message = new LogEventRequestType();

        return message;
    }

    protected AcknowledgementType getAuditLogProxyResponse(AuditRepositoryProxy proxy,
            LogEventRequestType auditLogRequest, AssertionType assertion) {
        AcknowledgementType ack;
        ack = proxy.auditLog(auditLogRequest, assertion);
        return ack;
    }

    protected LogEventRequestType getLogEventRequestTypeForProxyResponseMessage(RegistryResponseType response,
            AssertionType assertion) {
        // LogEventRequestType message = new PatientDiscoveryTransforms().transformNhinXDRResponseToAuditMsg(response,
        // assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        LogEventRequestType message = new LogEventRequestType();
        return message;
    }

    protected AuditRepositoryProxy getAuditRepositoryProxy(AuditRepositoryProxyObjectFactory auditRepoFactory) {
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy;
    }

    protected AuditRepositoryProxyObjectFactory getAuditRepositoryProxyObjectFactory() {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        return auditRepoFactory;
    }
}
