/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.NhinPatientDiscoveryOrchImpl;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.service.WebServiceHelper;
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
public class NhinPatientDiscoveryImpl extends WebServiceHelper {

	private static Log log = LogFactory.getLog(NhinPatientDiscoveryImpl.class);

	private Timestamp startTime = null;
	private Timestamp stopTime = null;
	private Long logId = null;

	public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(
			PRPAIN201305UV02 body, WebServiceContext context) {
		log.debug("Entering NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");

		AssertionType assertion = getSamlAssertion(context);

		auditIncoming201305Message(body, assertion);

		start(body);

		PRPAIN201306UV02 response = respondingGatewayPRPAIN201305UV02(body,
				assertion);

		stop();

		auditResponding201306Message(assertion, response);

		// Send response back to the initiating Gateway
		log.debug("Exiting NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");
		return response;

	}

	private void auditResponding201306Message(AssertionType assertion,
			PRPAIN201306UV02 response) {
		// Audit the responding 201306 Message - Response outbound to the NHIN
		PatientDiscoveryAuditLogger auditLogger = getAuditLogger();
		auditLogger.auditNhin201306(response, assertion,
				NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
	}

	private void auditIncoming201305Message(PRPAIN201305UV02 body,
			AssertionType assertion) {
		PatientDiscoveryAuditLogger auditLogger = getAuditLogger();
		auditLogger.auditNhin201305(body, assertion,
				NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
	}

	private void stop() {

		// Log the end of the nhin performance record

		stopTime = new Timestamp(System.currentTimeMillis());
		try {
			PerformanceManager.getPerformanceManagerInstance()
					.logPerformanceStop(logId, startTime, stopTime);
		} finally {
			logId = null;
			startTime = null;
			stopTime = null;
		}

	}

	private void start(PRPAIN201305UV02 body) {
		String targetCommunityId = getTargetCommunityId(body);

		// Log the start of the nhin performance record
		startTime = new Timestamp(System.currentTimeMillis());
		logId = getPerformanceManager()
				.logPerformanceStart(startTime,
						NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME,
						NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
						NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
						targetCommunityId);
	}
	
	protected PerformanceManager getPerformanceManager() {
		return PerformanceManager.getPerformanceManagerInstance();
	}

	private PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(
			PRPAIN201305UV02 body, AssertionType assertion) {
		NhinPatientDiscoveryOrchImpl oOrchestrator = getOrchestrator();

		PRPAIN201306UV02 response = oOrchestrator
				.respondingGatewayPRPAIN201305UV02(body, assertion);
		return response;
	}

	protected NhinPatientDiscoveryOrchImpl getOrchestrator() {
		NhinPatientDiscoveryOrchImpl oOrchestrator = new NhinPatientDiscoveryOrchImpl();
		return oOrchestrator;
	}

	protected PatientDiscoveryAuditLogger getAuditLogger() {
		PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
		return auditLogger;
	}

	private String getTargetCommunityId(PRPAIN201305UV02 body) {
		PatientDiscoveryTransforms transforms = getTransforms();
		String targetCommunityId = transforms
				.getPatientDiscoveryMessageCommunityId(body,
						NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
						NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
						NhincConstants.AUDIT_LOG_SYNC_TYPE,
						NhincConstants.AUDIT_LOG_REQUEST_PROCESS);
		return targetCommunityId;
	}

	protected PatientDiscoveryTransforms getTransforms() {
		return  new PatientDiscoveryTransforms();
	}
	



}
