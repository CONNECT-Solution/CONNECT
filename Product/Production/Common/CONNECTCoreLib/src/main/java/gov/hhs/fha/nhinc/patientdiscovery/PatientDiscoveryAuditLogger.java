/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author jhoppesc
 */
public class PatientDiscoveryAuditLogger implements PatientDiscoveryAuditor {

    /* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditNhin201305(org.hl7.v3.PRPAIN201305UV02, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
    @Override
	public AcknowledgementType auditNhin201305 (PRPAIN201305UV02 request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logNhinPatientDiscReq(request, assertion, direction, NhincConstants.AUDIT_LOG_SYNC_TYPE);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    /* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditNhinDeferred201305(org.hl7.v3.PRPAIN201305UV02, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
    @Override
	public AcknowledgementType auditNhinDeferred201305 (PRPAIN201305UV02 request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logNhinPatientDiscReq(request, assertion, direction, NhincConstants.AUDIT_LOG_DEFERRED_TYPE);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    /* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditAdapter201305(org.hl7.v3.PRPAIN201305UV02, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
    @Override
	public AcknowledgementType auditAdapter201305 (PRPAIN201305UV02 request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAdapterPatientDiscReq(request, assertion, direction, NhincConstants.AUDIT_LOG_SYNC_TYPE);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    /* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditAdapterDeferred201305(org.hl7.v3.PRPAIN201305UV02, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
    @Override
	public AcknowledgementType auditAdapterDeferred201305 (PRPAIN201305UV02 request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAdapterPatientDiscReq(request, assertion, direction, NhincConstants.AUDIT_LOG_DEFERRED_TYPE);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    /* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditNhin201306(org.hl7.v3.PRPAIN201306UV02, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
    @Override
	public AcknowledgementType auditNhin201306 (PRPAIN201306UV02 request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logNhinPatientDiscResp(request, assertion, direction, NhincConstants.AUDIT_LOG_SYNC_TYPE);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }
    
    /* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditNhinDeferred201306(org.hl7.v3.PRPAIN201306UV02, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
    @Override
	public AcknowledgementType auditNhinDeferred201306 (PRPAIN201306UV02 request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logNhinPatientDiscResp(request, assertion, direction, NhincConstants.AUDIT_LOG_DEFERRED_TYPE);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    /* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditAdapter201306(org.hl7.v3.PRPAIN201306UV02, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
    @Override
	public AcknowledgementType auditAdapter201306 (PRPAIN201306UV02 request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAdapterPatientDiscResp(request, assertion, direction, NhincConstants.AUDIT_LOG_SYNC_TYPE);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    /* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditAdapterDeferred201306(org.hl7.v3.PRPAIN201306UV02, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
    @Override
	public AcknowledgementType auditAdapterDeferred201306 (PRPAIN201306UV02 request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAdapterPatientDiscResp(request, assertion, direction, NhincConstants.AUDIT_LOG_DEFERRED_TYPE);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    /* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditEntity201305(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
    @Override
	public AcknowledgementType auditEntity201305 (RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logEntityPatientDiscReq(request, assertion, direction, NhincConstants.AUDIT_LOG_SYNC_TYPE, null);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    /* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditEntityDeferred201305(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String, java.lang.String)
	 */
    @Override
	public AcknowledgementType auditEntityDeferred201305 (RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, String direction, String _process) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logEntityPatientDiscReq(request, assertion, direction, NhincConstants.AUDIT_LOG_DEFERRED_TYPE, _process);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    /* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditEntity201306(org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
    @Override
	public AcknowledgementType auditEntity201306 (RespondingGatewayPRPAIN201306UV02ResponseType request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logEntityPatientDiscResp(request, assertion, direction, NhincConstants.AUDIT_LOG_SYNC_TYPE);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    /* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditEntity201306(org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
    @Override
	public AcknowledgementType auditEntity201306 (RespondingGatewayPRPAIN201306UV02RequestType request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logEntityPatientDiscAsyncResp(request, assertion, direction, NhincConstants.AUDIT_LOG_DEFERRED_TYPE);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    /* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditAck(org.hl7.v3.MCCIIN000002UV01, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String, java.lang.String)
	 */
    @Override
	public AcknowledgementType auditAck (MCCIIN000002UV01 request, AssertionType assertion, String direction, String _interface) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logNhinPatientDiscAck(request, assertion, direction, _interface);

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
