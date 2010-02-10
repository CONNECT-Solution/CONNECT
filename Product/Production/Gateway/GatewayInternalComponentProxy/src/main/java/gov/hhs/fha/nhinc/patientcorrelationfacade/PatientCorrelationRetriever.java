package gov.hhs.fha.nhinc.patientcorrelationfacade;

import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsResponseType;
import gov.hhs.fha.nhinc.patientcorrelationfacade.helper.PatientCorrelationHelper;
import gov.hhs.fha.nhinc.patientcorrelationfacade.helper.TransformHelper;

/**
 * Class to retrieve patient correlation records.
 * 
 * @author Neil Webb
 */
public class PatientCorrelationRetriever
{
    /**
     * Retrieve patient correlation records.
     *
     * @param request Retrieve correlations request
     * @return Patient correlation records.
     */
    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(RetrievePatientCorrelationsRequestType request)
    {
        // Create patient correlation input
        TransformHelper transformHelper = new TransformHelper();
        org.hl7.v3.PRPAIN201309UV02 correlationInput =  transformHelper.createPixRetrieve(request);

        // Call the patient correlation service
        PatientCorrelationHelper correlationHelper = new PatientCorrelationHelper();
        
        org.hl7.v3.PRPAIN201310UV02 correlationOutput = correlationHelper.retrievePatientCorrelations(correlationInput, request.getAssertion());

        // Create response message
        RetrievePatientCorrelationsResponseType response = transformHelper.createFacadeRetrieveResult(correlationOutput);

        return response;
    }

    
}
