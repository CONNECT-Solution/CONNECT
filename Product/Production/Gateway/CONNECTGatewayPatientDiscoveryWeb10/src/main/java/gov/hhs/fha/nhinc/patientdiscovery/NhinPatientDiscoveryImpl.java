/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms;
import java.sql.Timestamp;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author jhoppesc
 */
public class NhinPatientDiscoveryImpl
{

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryImpl.class);

    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 body, WebServiceContext context)
    {
        log.debug("Entering NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");

        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        // Audit the incoming Nhin 201305 Message
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditNhin201305(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        // Log the start of the nhin performance record
        PatientDiscoveryTransforms transforms = new PatientDiscoveryTransforms();
        String targetCommunityId = transforms.getPatientDiscoveryMessageCommunityId(body, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhincConstants.AUDIT_LOG_SYNC_TYPE, NhincConstants.AUDIT_LOG_REQUEST_PROCESS);
        Timestamp starttimeNhin = new Timestamp(System.currentTimeMillis());
        Long logNhinId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttimeNhin, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, targetCommunityId);

        NhinPatientDiscoveryOrchImpl oOrchestrator = new NhinPatientDiscoveryOrchImpl();
        PRPAIN201306UV02 response = oOrchestrator.respondingGatewayPRPAIN201305UV02(body, assertion);

        // Log the end of the nhin performance record
        Timestamp stoptimeNhin = new Timestamp(System.currentTimeMillis());
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logNhinId, starttimeNhin, stoptimeNhin);

        // Audit the responding 201306 Message - Response outbound to the NHIN
        ack = auditLogger.auditNhin201306(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        // Send response back to the initiating Gateway
        log.debug("Exiting NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");
        return response;
    }
}
