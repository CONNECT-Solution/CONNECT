/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission;

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class XDRAuditLogger {
    private static Log log = null;

    public XDRAuditLogger()
    {
        log= createLogger();
    }
    public AcknowledgementType auditNhinXDR (ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        log.debug("begin auditNhinXDR()");
        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logXDRReq(request, assertion, direction);

        if(auditLogMsg != null)
        {
            if(auditLogMsg.getAuditMessage() != null)
            {
                audit(auditLogMsg, assertion);
            }
            else
            {
                log.error("auditLogMsg.getAuditMessage() is null");
            }
        }
        else
        {
            log.error("auditLogMsg is null");
        }

        log.debug("begin auditNhinXDR()");
        log.debug("Ack message = " + ack.getMessage());
        return ack;
    }
    public AcknowledgementType auditXDR(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion, String direction)
    {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logXDRReq(request, assertion, direction);

        if(auditLogMsg != null)
        {
            if(auditLogMsg.getAuditMessage() != null)
            {
                audit(auditLogMsg, assertion);
            }

        }
        return ack;
    }

    public AcknowledgementType auditEntityXDR(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

        LogEventRequestType auditLogMsg = auditLogger.logEntityXDRReq(request, assertion, direction);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    public AcknowledgementType auditEntityXDRResponse(RegistryResponseType response, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logEntityXDRResponse(response, assertion, direction);

        if(auditLogMsg != null)
        {
            if(auditLogMsg.getAuditMessage() != null)
            {
                audit(auditLogMsg, assertion);
            }
        }
        return ack;
    }

    public AcknowledgementType auditEntityXDRResponseRequest(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType response, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logEntityXDRResponseRequest(response, assertion, direction);

        if(auditLogMsg != null)
        {
            if(auditLogMsg.getAuditMessage() != null)
            {
                audit(auditLogMsg, assertion);
            }
        }
        return ack;
    }

    public AcknowledgementType auditNhinXDRResponse (RegistryResponseType Response, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logNhinXDRResponse(Response, assertion, direction);

        if(auditLogMsg != null)
        {
            if(auditLogMsg.getAuditMessage() != null)
            {
                audit(auditLogMsg, assertion);
            }
        }
        return ack;
    }

    public AcknowledgementType auditNhinXDRResponseRequest (gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logNhinXDRResponseRequest(request, assertion, direction);

        if(auditLogMsg != null)
        {
            if(auditLogMsg.getAuditMessage() != null)
            {
                audit(auditLogMsg, assertion);
            }
        }
        return ack;
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    private AcknowledgementType audit(LogEventRequestType auditLogMsg, AssertionType assertion)
    {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();

        return proxy.auditLog(auditLogMsg, assertion);
    }

    /**
     * This method logs the acknowledgement returned from XDR Request and Response services
     *
     * @param acknowledgement
     * @param assertion
     * @param direction
     * @param action
     * @return
     */
    public AcknowledgementType auditAcknowledgement (XDRAcknowledgementType acknowledgement, AssertionType assertion, String direction, String action) {

        createLogger().debug("Start auditAcknowledgement for " + action);
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAcknowledgement(acknowledgement, assertion, direction, action);

        if(auditLogMsg != null)
        {
            if(auditLogMsg.getAuditMessage() != null)
            {
                audit(auditLogMsg, assertion);
            }
        }
        return ack;
    }
    public AcknowledgementType auditEntityAcknowledgement (XDRAcknowledgementType acknowledgement, AssertionType assertion, String direction, String action) {

        createLogger().debug("Start auditAcknowledgement for " + action);
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logEntityAcknowledgement(acknowledgement, assertion, direction, action);

        if(auditLogMsg != null)
        {
            if(auditLogMsg.getAuditMessage() != null)
            {
                audit(auditLogMsg, assertion);
            }
        }
        return ack;
    }
}