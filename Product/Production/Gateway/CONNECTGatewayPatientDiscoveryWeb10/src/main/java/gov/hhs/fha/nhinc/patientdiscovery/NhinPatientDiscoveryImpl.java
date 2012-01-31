/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.GenericFactory;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.NhinPatientDiscoveryOrchImpl;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.NhinPatientDiscoveryOrchestration;
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
	
	private GenericFactory<NhinPatientDiscoveryOrchestration> orchestrationFactory;
	private PatientDiscoveryAuditor auditLogger;
	
	public NhinPatientDiscoveryImpl(PatientDiscoveryAuditor auditLogger, GenericFactory<NhinPatientDiscoveryOrchestration> orchestrationFactory) {
		this.orchestrationFactory = orchestrationFactory;
		this.auditLogger = auditLogger;
	}

	public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(
			PRPAIN201305UV02 body, WebServiceContext context) throws PatientDiscoveryException {
		log.debug("Entering NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");

		AssertionType assertion = getSamlAssertion(context);

		start(body);

		PRPAIN201306UV02 response;
			response = respondingGatewayPRPAIN201305UV02(body,
					assertion);
		
		stop();


		// Send response back to the initiating Gateway
		log.debug("Exiting NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");
		return response;

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
			PRPAIN201305UV02 body, AssertionType assertion) throws PatientDiscoveryException {
		NhinPatientDiscoveryOrchestration oOrchestrator1 = orchestrationFactory.create();
		NhinPatientDiscoveryOrchestration oOrchestrator = oOrchestrator1;

		PRPAIN201306UV02 response = oOrchestrator
				.respondingGatewayPRPAIN201305UV02(body, assertion);
		return response;
	}

	protected PatientDiscoveryAuditor getAuditLogger() {
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
