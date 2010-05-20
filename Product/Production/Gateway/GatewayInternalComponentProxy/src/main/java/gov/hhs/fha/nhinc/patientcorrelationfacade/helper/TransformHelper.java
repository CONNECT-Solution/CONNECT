package gov.hhs.fha.nhinc.patientcorrelationfacade.helper;

import gov.hhs.fha.nhinc.componentpatientcorrelationfacadedte.NhincComponentPatientCorrelationFacadeDteService;
import gov.hhs.fha.nhinc.componentpatientcorrelationfacadedte.PatientCorrelationFacadeDte;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

/**
 * Patient Correlation transform helper
 * 
 * @author Neil Webb
 */
public class TransformHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(TransformHelper.class);

    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String HOME_COMMUNITY_ID_PROPERTY = "localHomeCommunityId";
    private static final String SERVICE_NAME_PATIENT_CORRELATION_FACADE_DTE_SERVICE = "patientcorrelationfacadedteservice";
    
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
    private PatientCorrelationFacadeDte getPort() {
        if (dteService == null) {
            dteService = new NhincComponentPatientCorrelationFacadeDteService();
        }

        // Get the Home community ID for this box...
        //------------------------------------------
        String sHomeCommunityId = "";
        String sEndpointURL = "";
        try {
            sHomeCommunityId = PropertyAccessor.getProperty(GATEWAY_PROPERTY_FILE, HOME_COMMUNITY_ID_PROPERTY);
        } catch (Exception e) {
            log.error("Failed to read " + HOME_COMMUNITY_ID_PROPERTY +
                    " property from the " + GATEWAY_PROPERTY_FILE + ".properties  file.  Error: " +
                    e.getMessage(), e);
        }
        
        // Get the endpoint URL for the patient correlation facade DTE service
        //------------------------------------------
        if ((sHomeCommunityId != null) && (sHomeCommunityId.length() > 0)) {
            try {
                sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName(sHomeCommunityId, SERVICE_NAME_PATIENT_CORRELATION_FACADE_DTE_SERVICE);
            } catch (Exception e) {
                log.error("Failed to retrieve endpoint URL for service:" + SERVICE_NAME_PATIENT_CORRELATION_FACADE_DTE_SERVICE +
                        " from connection manager.  Error: " + e.getMessage(), e);
            }
        }
        
        PatientCorrelationFacadeDte port = dteService.getPatientCorrelationFacadeDteBindingPort();

        if ((sEndpointURL != null) &&
                (sEndpointURL.length() > 0)) {
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, sEndpointURL);
        } else {
            log.error("Did not find endpoint URL for service: " + SERVICE_NAME_PATIENT_CORRELATION_FACADE_DTE_SERVICE + " and " +
                    "Home Community: " + sHomeCommunityId + ".");
        }

        return port;
    }

    /**
     * Call the web service operation to create a PIX retrieve message that is
     * used to retrieve patient correlation records.
     *
     * @param request Retrieve correlations request message
     * @return 201309 retrieve message.
     */
    public org.hl7.v3.PRPAIN201309UV02 createPixRetrieve(RetrievePatientCorrelationsRequestType request) {
        org.hl7.v3.PRPAIN201309UV02 result = null;
        try { // Call Web Service Operation
            PatientCorrelationFacadeDte port = getPort();

            // Create transform input object
            org.hl7.v3.CreatePixRetrieveRequestType createPixRetrieveRequest = new org.hl7.v3.CreatePixRetrieveRequestType();
            createPixRetrieveRequest.setRetrievePatientCorrelationsRequest(request);

            // Capture results for return
            org.hl7.v3.CreatePixRetrieveResponseType response = port.createPixRetrieve(createPixRetrieveRequest);
            if (response != null) {
                result = response.getPRPAIN201309UV02();
            }
        } catch (Exception ex) {
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
    public RetrievePatientCorrelationsResponseType createFacadeRetrieveResult(org.hl7.v3.PRPAIN201310UV02 request) {
        RetrievePatientCorrelationsResponseType response = null;
        try { // Call Web Service Operation
            PatientCorrelationFacadeDte port = getPort();

            org.hl7.v3.CreateFacadeRetrieveResultRequestType createFacadeRetrieveResultRequest = new org.hl7.v3.CreateFacadeRetrieveResultRequestType();
            createFacadeRetrieveResultRequest.setPRPAIN201310UV02(request);

            // Capture results for return
            org.hl7.v3.CreateFacadeRetrieveResultResponseType result = port.createFacadeRetrieveResult(createFacadeRetrieveResultRequest);
            if (result != null) {
                response = result.getRetrievePatientCorrelationsResponse();
            }
        } catch (Exception ex) {
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
    public org.hl7.v3.PRPAIN201301UV02 createPixAdd(AddPatientCorrelationRequestType request) {
        org.hl7.v3.PRPAIN201301UV02 response = null;
        try { // Call Web Service Operation
            PatientCorrelationFacadeDte port = getPort();

            org.hl7.v3.CreatePixAddRequestType createPixAddRequest = new org.hl7.v3.CreatePixAddRequestType();
            createPixAddRequest.setAddPatientCorrelationRequest(request);

            // Capture results for return
            org.hl7.v3.CreatePixAddResponseType result = port.createPixAdd(createPixAddRequest);
            if (result != null) {
                response = result.getPRPAIN201301UV02();
            }
        } catch (Exception ex) {
            log.error("Exception encountered calling createPixAdd: " + ex.getMessage(), ex);
        }
        return response;
    }
}
