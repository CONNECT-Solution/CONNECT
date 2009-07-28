package gov.hhs.fha.nhinc.patientcorrelationfacade.proxy;

import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationResponseType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RemovePatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RemovePatientCorrelationResponseType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsResponseType;

/**
 * Default patient correlation implementation that simply returns empty
 * response objects.
 * 
 * @author Neil Webb
 */
public class PatientCorrelationFacadeProxyNoOpImpl implements PatientCorrelationFacadeProxy
{

    /**
     * No-op implementation of the retrievePatientCorrelations method. Returns
     * an empty response message.
     * 
     * @param request Retrieve correlation request message
     * @return Empty response message
     */
    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(RetrievePatientCorrelationsRequestType request)
    {
        return new RetrievePatientCorrelationsResponseType();
    }

    /**
     * No-op implementation of the addPatientCorrelation method. Returns an
     * empty response message.
     * 
     * @param request Add patient correlation request message.
     * @return Empty response message
     */
    public AddPatientCorrelationResponseType addPatientCorrelation(AddPatientCorrelationRequestType request)
    {
        return new AddPatientCorrelationResponseType();
    }

    /**
     * No-op implementation of the removePatientCorrelation method. Returns an
     * empty response message.
     * 
     * @param request Remove correlation request messgae
     * @return Empty response message
     */
    public RemovePatientCorrelationResponseType removePatientCorrelation(RemovePatientCorrelationRequestType request)
    {
        return new RemovePatientCorrelationResponseType();
    }
    
}
