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
package gov.hhs.fha.nhinc.docsubmission;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;

/**
 *
 * @author dunnek
 */
public class XDRAuditLogger {
    
    private static Log log = null;

    /**
     * The default constructor. Creates the logger.
     */
    public XDRAuditLogger() {
        log = createLogger();
    }

    /**
     * This method creates the generic Audit Log message for a NHIN XDR Request
     * @param request The generic XDR request to be audited
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return acknowledgment type.
     */
    public AcknowledgementType auditNhinXDR(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion,
            String direction) {
        AcknowledgementType ack = new AcknowledgementType();

        log.debug("begin auditNhinXDR()");
        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logXDRReq(request, assertion, direction);

        if (auditLogMsg != null) {
            if (auditLogMsg.getAuditMessage() != null) {
                audit(auditLogMsg, assertion);
            } else {
                log.error("auditLogMsg.getAuditMessage() is null");
            }
        } else {
            log.error("auditLogMsg is null");
        }

        log.debug("end auditNhinXDR()");
        log.debug("Ack message = " + ack.getMessage());
        return ack;
    }

    /**
     * Creates a generic Audit Log message for an generic Adapter XDR message.
     * @param request The generic XDR message to be audited
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return
     */
    public AcknowledgementType auditAdapterXDR(ProvideAndRegisterDocumentSetRequestType request, 
            AssertionType assertion, String direction) {

        AcknowledgementType ack = new AcknowledgementType();
        log.debug("begin auditAdapterXDR()");


        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAdapterXDRReq(request, assertion, direction);

        if (auditLogMsg != null) {
            if (auditLogMsg.getAuditMessage() != null) {
                audit(auditLogMsg, assertion);
            } else {
                log.error("auditLogMsg.getAuditMessage() is null");
            }
        } else {
            log.error("auditLogMsg is null");
        }

        log.debug("end auditAdapterXDR()");
        log.debug("Ack message = " + ack.getMessage());
        return ack;
    }

    /**
     * Creates a generic Audit Log message for a generic XDR message.
     * @param request The generic XDR Request to be audited
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return
     */
    public AcknowledgementType auditXDR(
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request,
            AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logXDRReq(request, assertion, direction);

        if (auditLogMsg != null &&
            auditLogMsg.getAuditMessage() != null) {
            audit(auditLogMsg, assertion);
        }
        return ack;
    }

    /**
     * Creates a generic Audit Log message for a generic Entity XDR message.
     * @param request The generic audit request to be audited
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return
     */
    public AcknowledgementType auditEntityXDR(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request,
            AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

        LogEventRequestType auditLogMsg = auditLogger.logEntityXDRReq(request, assertion, direction);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    /**
     * Creates a generic Audit Log message for an Adapter XDR Response.
     * @param response The XDR Response to be audited
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return
     */
    public AcknowledgementType auditAdapterXDRResponse(RegistryResponseType response, AssertionType assertion,
            String direction) {
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAdapterXDRResponse(response, assertion, direction);

        if (auditLogMsg != null && auditLogMsg.getAuditMessage() != null) {
            audit(auditLogMsg, assertion);            
        }
        return ack;
    }

    /**
     * Creates a generic Audit Log message for an Entity XDR Response.
     * @param response The XDR Response to be audited
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return
     */
    public AcknowledgementType auditEntityXDRResponse(RegistryResponseType response, AssertionType assertion,
            String direction) {
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logEntityXDRResponse(response, assertion, direction);

        if (auditLogMsg != null && auditLogMsg.getAuditMessage() != null) {
            audit(auditLogMsg, assertion);
        }
        return ack;
    }

    /**
     * Creates a generic Audit Log message for an Entity XDR Response Request.
     * @param response The XDR Response Request to be audited
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return
     */
    public AcknowledgementType auditEntityXDRResponseRequest(
            gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType response,
            AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logEntityXDRResponseRequest(response, assertion, direction);

        if (auditLogMsg != null && auditLogMsg.getAuditMessage() != null) {
            audit(auditLogMsg, assertion);
        }
        return ack;
    }

    /**
     * Creates a generic Audit Log message for an Nhin XDR Respnse.
     * @param response The XDR Response message to be audited
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return
     */
    public AcknowledgementType auditNhinXDRResponse(RegistryResponseType response, AssertionType assertion,
            String direction) {        
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logNhinXDRResponse(response, assertion, direction);

        if (auditLogMsg != null && auditLogMsg.getAuditMessage() != null) {
            audit(auditLogMsg, assertion);
        }
        return ack;
    }

    /**
     * Creates a generic Audit Log message for an Nhin XDR Response Request.
     * @param request The XDR Response Request to be audited
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @return
     */
    public AcknowledgementType auditNhinXDRResponseRequest(
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request,
            AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logNhinXDRResponseRequest(request, assertion, direction);

        if (auditLogMsg != null && auditLogMsg.getAuditMessage() != null) {
            audit(auditLogMsg, assertion);
        }
        return ack;
    }

    /**
     * Returns or creates a log object.
     * @return the singleton log object
     */
    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * Submits a generic Audit Log message to the Audit Log Repository.
     * @param auditLogMsg The generic audit log to be audited
     * @param assertion The assertion to be audited
     * @return
     */
    private AcknowledgementType audit(LogEventRequestType auditLogMsg, AssertionType assertion) {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();

        return proxy.auditLog(auditLogMsg, assertion);
    }

    /**
     * This method logs the acknowledgement returned from XDR Request and Response services
     *
     * @param acknowledgement The acknowledgement message to be audited
     * @param assertion The assertion of the message to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param action The type of the current message (Request or Response)
     * @return
     */
    public AcknowledgementType auditAcknowledgement(XDRAcknowledgementType acknowledgement, AssertionType assertion,
            String direction, String action) {

        createLogger().debug("Start auditAcknowledgement for " + action);
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAcknowledgement(acknowledgement, assertion, direction, action);

        if (auditLogMsg != null && auditLogMsg.getAuditMessage() != null) {
            audit(auditLogMsg, assertion);
        }
        return ack;
    }

    
    /**
     * Creates a generic Audit Log message for an Adapter Acknowledgement.
     * @param acknowledgement The acknowledgement message to be audited
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param action The type of the current message (Request or Response)
     * @return
     */
    public AcknowledgementType auditAdapterAcknowledgement(XDRAcknowledgementType acknowledgement,
            AssertionType assertion, String direction, String action) {

        createLogger().debug("Start auditAcknowledgement for " + action);
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAdapterAcknowledgement(acknowledgement, assertion, direction,
                action);

        if (auditLogMsg != null &&
            auditLogMsg.getAuditMessage() != null) {
            audit(auditLogMsg, assertion);
        }
        return ack;
    }

    /**
     * Creates a generic Audit Log message for an Entity Acknowledgement.
     * @param acknowledgement The acknowledgement message to be audited
     * @param assertion The assertion to be audited
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param action The type of the current message (Request or Response)
     * @return
     */
    public AcknowledgementType auditEntityAcknowledgement(XDRAcknowledgementType acknowledgement,
            AssertionType assertion, String direction, String action) {

        createLogger().debug("Start auditAcknowledgement for " + action);
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logEntityAcknowledgement(acknowledgement, assertion, direction,
                action);

        if (auditLogMsg != null && auditLogMsg.getAuditMessage() != null) {
            audit(auditLogMsg, assertion);        }
        return ack;
    }
}