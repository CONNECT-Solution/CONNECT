package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

public interface PatientCorrelationOrch {

	public abstract AddPatientCorrelationResponseType addPatientCorrelation(PRPAIN201301UV02 addPatientCorrelationRequest, AssertionType assertion);

	public abstract RetrievePatientCorrelationsResponseType retrievePatientCorrelations(PRPAIN201309UV02 retrievePatientCorrelationsRequest, AssertionType assertion);

}
