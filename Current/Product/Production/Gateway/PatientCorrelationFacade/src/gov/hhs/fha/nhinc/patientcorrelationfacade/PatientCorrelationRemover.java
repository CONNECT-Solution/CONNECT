package gov.hhs.fha.nhinc.patientcorrelationfacade;

import gov.hhs.fha.nhinc.patientcorrelationfacade.helper.PatientCorrelationHelper;
import gov.hhs.fha.nhinc.patientcorrelationfacade.helper.TransformHelper;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RemovePatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RemovePatientCorrelationResponseType;

/**
 * Class to remove a patient correlation record.
 * 
 * @author Neil Webb
 */
public class PatientCorrelationRemover
{
    /**
     * Remove a patient correlation record.
     *
     * @param request Remove patient correlation request
     * @return Response message for the remove operation
     */
    public RemovePatientCorrelationResponseType removePatientCorrelation(RemovePatientCorrelationRequestType request)
    {
        // Create patient correlation input
        TransformHelper transformHelper = new TransformHelper();
        org.hl7.v3.PRPAIN201303UV revokeInput =  transformHelper.createPixRevoke(request);

        // Call the patient correlation service
        PatientCorrelationHelper correlationHelper = new PatientCorrelationHelper();
        org.hl7.v3.MCCIIN000002UV01 revokeOutput = correlationHelper.removePatientCorrelation(revokeInput);

        // Create response message
        RemovePatientCorrelationResponseType response = new RemovePatientCorrelationResponseType();
        AcknowledgementType ack = new AcknowledgementType();
        ack.setMessage("success");
        response.setAck(ack);

        return response;
    }
    
}
