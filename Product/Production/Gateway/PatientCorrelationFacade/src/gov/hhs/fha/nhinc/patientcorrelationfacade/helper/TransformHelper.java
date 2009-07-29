package gov.hhs.fha.nhinc.patientcorrelationfacade.helper;

import gov.hhs.fha.nhinc.componentpatientcorrelationfacadedte.NhincComponentPatientCorrelationFacadeDteService;
import gov.hhs.fha.nhinc.componentpatientcorrelationfacadedte.PatientCorrelationFacadeDte;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RemovePatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsResponseType;

/**
 * Patient Correlation transform helper
 * 
 * @author Neil Webb
 */
public class TransformHelper
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(TransformHelper.class);

    private static final String ENDPOINT_ADDRESS_TRANSFORM = "http://localhost:8080/PatientCorrelationFacadeDteService";

    /**
     * Cached web service object. Cached to prevent port creation delay after
     * the initial occurance.
     */
    private static NhincComponentPatientCorrelationFacadeDteService dteService;

    /**
     * Retrieve the DTE web service port object.
     *
     * @return DTE web service port object.
     */
    private PatientCorrelationFacadeDte getPort()
    {
        if(dteService == null)
        {
            dteService = new NhincComponentPatientCorrelationFacadeDteService();
        }
        PatientCorrelationFacadeDte port = dteService.getPatientCorrelationFacadeDteBindingPort();
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ENDPOINT_ADDRESS_TRANSFORM);
        return port;
    }

    /**
     * Call the web service operation to create a PIX retrieve message that is
     * used to retrieve patient correlation records.
     *
     * @param request Retrieve correlations request message
     * @return 201309 retrieve message.
     */
    public org.hl7.v3.PRPAIN201309UV createPixRetrieve(RetrievePatientCorrelationsRequestType request)
    {
        org.hl7.v3.PRPAIN201309UV result = null;
        try
        { // Call Web Service Operation
            PatientCorrelationFacadeDte port = getPort();

            // Create transform input object
            org.hl7.v3.CreatePixRetrieveRequestType createPixRetrieveRequest = new org.hl7.v3.CreatePixRetrieveRequestType();
            createPixRetrieveRequest.setRetrievePatientCorrelationsRequest(request);

            // Capture results for return
            org.hl7.v3.CreatePixRetrieveResponseType response = port.createPixRetrieve(createPixRetrieveRequest);
            if(response != null)
            {
                result = response.getPRPAIN201309UV();
            }
        }
        catch (Exception ex)
        {
            log.error("Exception encountered calling createPixRetrieve: " + ex.getMessage(), ex);
        }
        return result;
    }

    /**
     * Call the web service operation to create a retrieval response message.
     *
     * @param request 201310 retreival response message
     * @return Retrieve patient correlations response message.
     */
    public RetrievePatientCorrelationsResponseType createFacadeRetrieveResult(org.hl7.v3.PRPAIN201310UV request)
    {
        RetrievePatientCorrelationsResponseType response = null;
        try
        { // Call Web Service Operation
            PatientCorrelationFacadeDte port = getPort();

            org.hl7.v3.CreateFacadeRetrieveResultRequestType createFacadeRetrieveResultRequest = new org.hl7.v3.CreateFacadeRetrieveResultRequestType();
            createFacadeRetrieveResultRequest.setPRPAIN201310UV(request);
            
            // Capture results for return
            org.hl7.v3.CreateFacadeRetrieveResultResponseType result = port.createFacadeRetrieveResult(createFacadeRetrieveResultRequest);
            if(result != null)
            {
                response = result.getRetrievePatientCorrelationsResponse();
            }
        }
        catch (Exception ex)
        {
            log.error("Exception encountered calling createFacadeRetrieveResult: " + ex.getMessage(), ex);
        }
        return response;
    }

    /**
     * Call the web service operation to create a PIX add message that is used
     * to store a patient correlation record.
     * 
     * @param request Add correlation request message
     * @return 201301 add message
     */
    public org.hl7.v3.PRPAIN201301UV createPixAdd(AddPatientCorrelationRequestType request)
    {
        org.hl7.v3.PRPAIN201301UV response = null;
        try
        { // Call Web Service Operation
            PatientCorrelationFacadeDte port = getPort();

            org.hl7.v3.CreatePixAddRequestType createPixAddRequest = new org.hl7.v3.CreatePixAddRequestType();
            createPixAddRequest.setAddPatientCorrelationRequest(request);

            // Capture results for return
            org.hl7.v3.CreatePixAddResponseType result = port.createPixAdd(createPixAddRequest);
            if(result != null)
            {
                response = result.getPRPAIN201301UV();
            }
        }
        catch (Exception ex)
        {
            log.error("Exception encountered calling createPixAdd: " + ex.getMessage(), ex);
        }
        return response;
    }

    /**
     * Call the web service operation to create a PIX Revoke message that is
     * used to remove a patient correlation record.
     *
     * @param request Remove correlation request message
     * @return 201303 revoke message.
     */
    public org.hl7.v3.PRPAIN201303UV createPixRevoke(RemovePatientCorrelationRequestType request)
    {
        org.hl7.v3.PRPAIN201303UV response = null;
        try
        { // Call Web Service Operation
            PatientCorrelationFacadeDte port = getPort();

            org.hl7.v3.CreatePixRevokeRequestType createPixRevokeRequest = new org.hl7.v3.CreatePixRevokeRequestType();
            createPixRevokeRequest.setRemovePatientCorrelationRequest(request);

            // Capture results for return
            org.hl7.v3.CreatePixRevokeResponseType result = port.createPixRevoke(createPixRevokeRequest);
            if(result != null)
            {
                response = result.getPRPAIN201303UV();
            }
        }
        catch (Exception ex)
        {
            log.error("Exception encountered calling createPixRevoke: " + ex.getMessage(), ex);
        }
        return response;
    }

}
