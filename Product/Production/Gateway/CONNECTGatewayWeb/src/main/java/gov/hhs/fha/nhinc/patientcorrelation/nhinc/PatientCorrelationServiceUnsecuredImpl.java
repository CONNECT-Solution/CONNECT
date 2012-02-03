/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import org.hl7.v3.AddPatientCorrelationRequestType;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.RetrievePatientCorrelationsRequestType;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

/**
 * 
 * @author jhoppesc
 */
public class PatientCorrelationServiceUnsecuredImpl
		implements
		PatientCorrelationService<RetrievePatientCorrelationsRequestType, RetrievePatientCorrelationsResponseType, AddPatientCorrelationRequestType, AddPatientCorrelationResponseType> {

	private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
			.getLog(PatientCorrelationServiceUnsecuredImpl.class);

	private PatientCorrelationOrch orchestration;

	PatientCorrelationServiceUnsecuredImpl(PatientCorrelationOrch orchestration) {
		this.orchestration = orchestration;
	}

	public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(
			RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest,
			AssertionType assertionType) {

		log.info("Calling the Patient Correlation Retrieve Correlations Orch Impl");
		RetrievePatientCorrelationsResponseType response = orchestration
				.retrievePatientCorrelations(retrievePatientCorrelationsRequest
						.getPRPAIN201309UV02(),
						retrievePatientCorrelationsRequest.getAssertion());

		return response;
	}

	public AddPatientCorrelationResponseType addPatientCorrelation(
			AddPatientCorrelationRequestType addPatientCorrelationRequest,
			AssertionType assertionType) {

		log.info("Calling the Patient Correlation Add Correlations Orch Impl");
		AddPatientCorrelationResponseType response = orchestration
				.addPatientCorrelation(
						addPatientCorrelationRequest.getPRPAIN201301UV02(),
						addPatientCorrelationRequest.getAssertion());

		return response;
	}

}
