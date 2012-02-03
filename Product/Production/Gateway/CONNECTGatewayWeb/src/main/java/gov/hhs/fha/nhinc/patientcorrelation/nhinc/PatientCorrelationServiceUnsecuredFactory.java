package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.CorrelatedIdentifiersDaoImpl;

import org.hl7.v3.AddPatientCorrelationRequestType;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.RetrievePatientCorrelationsRequestType;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

public class PatientCorrelationServiceUnsecuredFactory
		implements
		PatientCorrelationServiceFactory<RetrievePatientCorrelationsRequestType, RetrievePatientCorrelationsResponseType, AddPatientCorrelationRequestType, AddPatientCorrelationResponseType> {

	private PatientCorrelationOrch orchestration;
	
	private static PatientCorrelationServiceUnsecuredFactory INSTANCE = new PatientCorrelationServiceUnsecuredFactory(new PatientCorrelationOrchImpl(new CorrelatedIdentifiersDaoImpl()));
	
	public PatientCorrelationServiceUnsecuredFactory(PatientCorrelationOrch orchestration) {
		this.orchestration = orchestration;
	}
	
	@Override
	public PatientCorrelationService<RetrievePatientCorrelationsRequestType, RetrievePatientCorrelationsResponseType, AddPatientCorrelationRequestType, AddPatientCorrelationResponseType> createPatientCorrelationService() {
		return new PatientCorrelationServiceUnsecuredImpl(orchestration);
	}

	public static PatientCorrelationServiceUnsecuredFactory getInstance() {
		return INSTANCE;
	}

}
