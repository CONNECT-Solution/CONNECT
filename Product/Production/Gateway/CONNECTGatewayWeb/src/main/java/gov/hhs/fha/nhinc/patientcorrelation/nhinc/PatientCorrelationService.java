package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

public interface PatientCorrelationService<RETRIEVE_REQUEST, RETRIEVE_RESPONSE, ADD_REQUEST, ADD_RESPONSE> {

	public RETRIEVE_RESPONSE retrievePatientCorrelations(
			RETRIEVE_REQUEST request,
			AssertionType assertionType);

	public ADD_RESPONSE addPatientCorrelation(
			ADD_REQUEST request,
			AssertionType assertionType);

}
