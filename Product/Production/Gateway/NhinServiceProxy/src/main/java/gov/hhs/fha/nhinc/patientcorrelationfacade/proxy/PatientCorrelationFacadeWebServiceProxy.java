package gov.hhs.fha.nhinc.patientcorrelationfacade.proxy;

import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationResponseType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsResponseType;
import gov.hhs.fha.nhinc.patientcorrelationfacade.PatientCorrelationCreator;
import gov.hhs.fha.nhinc.patientcorrelationfacade.PatientCorrelationRetriever;

/**
 * Java proxy for the patient correlation facade operations.
 * 
 * @author Neil Webb
 */
public class PatientCorrelationFacadeWebServiceProxy implements PatientCorrelationFacadeProxy
{

    /**
     * Call the Java library to retrieve patient correlation records.
     *
     * @param request Patient correlation request message.
     * @return Retrieved patient correlation records
     */
    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(RetrievePatientCorrelationsRequestType request)
    {
        return new PatientCorrelationRetriever().retrievePatientCorrelations(request);
    }

    /**
     * Call the Java library to add a patient correlation record.
     *
     * @param request Add patient correlation request message
     * @return Add correlation response message
     */
    public AddPatientCorrelationResponseType addPatientCorrelation(AddPatientCorrelationRequestType request)
    {
       
        return new PatientCorrelationCreator().addPatientCorrelation(request);
    }
    
}
