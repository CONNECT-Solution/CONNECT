package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

public interface PatientCorrelationServiceFactory<RETRIEVE_REQUEST, RETRIEVE_RESPONSE, ADD_REQUEST, ADD_RESPONSE> {

	public PatientCorrelationService<RETRIEVE_REQUEST, RETRIEVE_RESPONSE, ADD_REQUEST, ADD_RESPONSE> createPatientCorrelationService();
	
}
