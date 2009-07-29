package gov.hhs.fha.nhinc.patientcorrelationfacade.proxy;

import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationResponseType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RemovePatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RemovePatientCorrelationResponseType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsResponseType;
import gov.hhs.fha.nhinc.componentpatientcorrelationfacade.PatientCorrelationService;
import gov.hhs.fha.nhinc.componentpatientcorrelationfacade.PatientCorrelationFacadePortType;
/**
 * Web implementation of the patient correlation facade
 * 
 * @author Neil Webb
 */
public class PatientCorrelationFacadeWebServiceProxy implements PatientCorrelationFacadeProxy
{
    private static String ENDPOINT_PATIENT_CORRELATION_FACADE = "http://localhost:9080/PatientCorrelationFacadeService";

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PatientCorrelationFacadeWebServiceProxy.class);

    /**
     * The PatientCorrelationService object is stored as a static member of this
     * class due to a delay of approximately two seconds the first time a port
     * is created from this object. There is no significant delay for any 
     * instance of this class after the first time this object is used to create
     * a port object.
     */
    private static PatientCorrelationService patientCorrelationService;

    /**
     * Retrieve a patient correlation facade port object to consume a patient
     * correlation facade web service. This method should be used so that the
     * service object is created as a static member of this class.
     *
     * @return PatientCorrelationFacadePort object for web service consumption.
     */
    private PatientCorrelationFacadePortType getPatientCorrelationFacadePort()
    {
        if(patientCorrelationService == null)
        {
            patientCorrelationService = new PatientCorrelationService();
        }
        PatientCorrelationFacadePortType port = patientCorrelationService.getPatientCorrelationFacadePort();
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ENDPOINT_PATIENT_CORRELATION_FACADE);
        return port;
    }

    /**
     * Call the patient correlation facade web service to retrieve patient
     * correlation records.
     *
     * @param request Patient correlation request message.
     * @return Retrieved patient correlation records
     */
    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(RetrievePatientCorrelationsRequestType request)
    {
        RetrievePatientCorrelationsResponseType response = null;
        try
        { // Call Web Service Operation
            PatientCorrelationFacadePortType port = getPatientCorrelationFacadePort();

            // Call web service & collect response for return
            response = port.retrievePatientCorrelations(request);
        }
        catch (Exception ex)
        {
            log.error("Exception encountered calling retrievePatientCorrelations: " + ex.getMessage(), ex);
        }
        return response;
    }

    /**
     * Call the patient correlation facade web service to add a new patient
     * correlation record
     *
     * @param request Add patient correlation request message
     * @return Add correlation response message
     */
    public AddPatientCorrelationResponseType addPatientCorrelation(AddPatientCorrelationRequestType request)
    {
        AddPatientCorrelationResponseType response = null;
        try
        { // Call Web Service Operation
            PatientCorrelationFacadePortType port = getPatientCorrelationFacadePort();

            // Call web service & collect response for return
            gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType result = port.addPatientCorrelation(request);
            response = new AddPatientCorrelationResponseType();
            response.setAck(result);
        }
        catch (Exception ex)
        {
            log.error("Exception encountered calling addPatientCorrelation: " + ex.getMessage(), ex);
        }
        return response;
    }

    /**
     * Call the patient correlation facade web service to remove a patient
     * correlation record.
     *
     * @param request Remove correlation request message
     * @return Remove correlation response message
     */
    public RemovePatientCorrelationResponseType removePatientCorrelation(RemovePatientCorrelationRequestType request)
    {
        RemovePatientCorrelationResponseType response = null;
        try
        { // Call Web Service Operation
            PatientCorrelationFacadePortType port = getPatientCorrelationFacadePort();

            // Call web service & collect response for return
            gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType result = port.removePatientCorrelation(request);
            response = new RemovePatientCorrelationResponseType();
            response.setAck(result);
        }
        catch (Exception ex)
        {
            log.error("Exception encountered calling removePatientCorrelation: " + ex.getMessage(), ex);
        }
        return response;
    }
    
}
