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

import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.AddPatientCorrelationSecuredRequestType;
import org.hl7.v3.AddPatientCorrelationSecuredResponseType;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;
import org.hl7.v3.RetrievePatientCorrelationsSecuredRequestType;
import org.hl7.v3.RetrievePatientCorrelationsSecuredResponseType;

/**
 * 
 * @author svalluripalli
 */
public class PatientCorrelationServiceSecuredServiceImpl
		implements
		PatientCorrelationService<RetrievePatientCorrelationsSecuredRequestType, RetrievePatientCorrelationsSecuredResponseType, AddPatientCorrelationSecuredRequestType, AddPatientCorrelationSecuredResponseType>

{

	private PatientCorrelationOrch orchestration;

	private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
			.getLog(PatientCorrelationServiceSecuredServiceImpl.class);

	 PatientCorrelationServiceSecuredServiceImpl(
			PatientCorrelationOrch orchestration) {
		this.orchestration = orchestration;
	}

	public RetrievePatientCorrelationsSecuredResponseType retrievePatientCorrelations(
			RetrievePatientCorrelationsSecuredRequestType request,
			AssertionType assertion) {
		RetrievePatientCorrelationsSecuredResponseType response = new RetrievePatientCorrelationsSecuredResponseType();

		log.info("Calling the Patient Correlation Retrieve Correlations Orch Impl");
		RetrievePatientCorrelationsResponseType unsecureResp = orchestration
				.retrievePatientCorrelations(request
						.getPRPAIN201309UV02(), assertion);

		if (unsecureResp != null && unsecureResp.getPRPAIN201310UV02() != null) {
			response.setPRPAIN201310UV02(unsecureResp.getPRPAIN201310UV02());
		}
		return response;
	}

	public AddPatientCorrelationSecuredResponseType addPatientCorrelation(
			AddPatientCorrelationSecuredRequestType request,
			AssertionType assertion) {
		AddPatientCorrelationSecuredResponseType response = new AddPatientCorrelationSecuredResponseType();

		log.info("Calling the Patient Correlation Add Correlations Orch Impl");
		AddPatientCorrelationResponseType unsecureResp = orchestration
				.addPatientCorrelation(
						request.getPRPAIN201301UV02(),
						assertion);

		if (unsecureResp != null && unsecureResp.getMCCIIN000002UV01() != null) {
			response.setMCCIIN000002UV01(unsecureResp.getMCCIIN000002UV01());
		}

		return response;
	}

}
