package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.CorrelatedIdentifiersDaoImpl;

import org.hl7.v3.AddPatientCorrelationSecuredRequestType;
import org.hl7.v3.AddPatientCorrelationSecuredResponseType;
import org.hl7.v3.RetrievePatientCorrelationsSecuredRequestType;
import org.hl7.v3.RetrievePatientCorrelationsSecuredResponseType;

public class PatientCorrelationServiceSecuredFactory
		implements
		PatientCorrelationServiceFactory<RetrievePatientCorrelationsSecuredRequestType, RetrievePatientCorrelationsSecuredResponseType, AddPatientCorrelationSecuredRequestType, AddPatientCorrelationSecuredResponseType>
 {
	private PatientCorrelationOrch orchestration;
	
	private static PatientCorrelationServiceSecuredFactory INSTANCE = new PatientCorrelationServiceSecuredFactory(new PatientCorrelationOrchImpl(new CorrelatedIdentifiersDaoImpl()));

	 PatientCorrelationServiceSecuredFactory(
			PatientCorrelationOrch orchestration) {
		this.orchestration = orchestration;
	}

	@Override
	public PatientCorrelationService<RetrievePatientCorrelationsSecuredRequestType, RetrievePatientCorrelationsSecuredResponseType, AddPatientCorrelationSecuredRequestType, AddPatientCorrelationSecuredResponseType> createPatientCorrelationService() {
		return new PatientCorrelationServiceSecuredServiceImpl(orchestration);
	}

	
	public static PatientCorrelationServiceSecuredFactory getInstance() {
		return INSTANCE;
	}
}
