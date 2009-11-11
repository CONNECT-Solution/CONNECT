package gov.hhs.fha.nhinc.patientcorrelationfacade;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationResponseType;
import gov.hhs.fha.nhinc.patientcorrelationfacade.helper.PatientCorrelationHelper;
import gov.hhs.fha.nhinc.patientcorrelationfacade.helper.TransformHelper;

/**
 * Class to add a new patient correlation record.
 * 
 * @author Neil Webb
 */
public class PatientCorrelationCreator
{
    /**
     * Adds a new patient correlation record.
     * 
     * @param request Patient correlation add request
     * @return Response message for the add operation
     */
    public AddPatientCorrelationResponseType addPatientCorrelation(AddPatientCorrelationRequestType request)
    {
        // Create patient correlation input
        TransformHelper transformHelper = new TransformHelper();
        org.hl7.v3.PRPAIN201301UV02 addInput =  transformHelper.createPixAdd(request);

        // Call the patient correlation service
        PatientCorrelationHelper correlationHelper = new PatientCorrelationHelper();
        org.hl7.v3.MCCIIN000002UV01 addOutput = correlationHelper.addPatientCorrelation(addInput, request.getAssertion());

        // Create response message
        AddPatientCorrelationResponseType response = new AddPatientCorrelationResponseType();
        AcknowledgementType ack = new AcknowledgementType();
        ack.setMessage("success");
        response.setAck(ack);

        return response;
    }
    
}
