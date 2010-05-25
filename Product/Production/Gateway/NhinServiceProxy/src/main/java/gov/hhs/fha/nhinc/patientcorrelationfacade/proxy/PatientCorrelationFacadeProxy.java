package gov.hhs.fha.nhinc.patientcorrelationfacade.proxy;

import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsResponseType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RemovePatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RemovePatientCorrelationResponseType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationResponseType;

/**
 * Component proxy Java interface for patient correlation
 *
 * @author Neil Webb
 */
public interface PatientCorrelationFacadeProxy
{
    /**
     * Retrieves all correlated patient identifiers.
     * 
     * @param request Correlation request input that includes the local patient
     * identifier and optional target assigning authorities or communities.
     * @return Correlated patient identifiers.
     */
    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(RetrievePatientCorrelationsRequestType request);

    /**
     * Add a patient correlation record.
     *
     * @param request Add correlation request that includes local and remote
     * identifiers.
     * @return Repsonse that is a simple ack.
     */
    public AddPatientCorrelationResponseType addPatientCorrelation(AddPatientCorrelationRequestType request);
}
