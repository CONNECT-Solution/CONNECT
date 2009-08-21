package gov.hhs.fha.nhinc.patientcorrelationfacade.helper;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;

import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.*;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;

/**
 * Helper class for the patient correlation service
 * 
 * @author Neil Webb
 */
public class PatientCorrelationHelper
{
    private static final String PROPERTY_FILE_NAME = "gateway";
    private static final String PROPERTY_LOCAL_HOME_COMMUNITY = "localHomeCommunityId";
    private static final String SERVICE_NAME_PATIENT_CORRELATION = "patientcorrelation";

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PatientCorrelationHelper.class);

    /**
     * Cached patient correlation service object. Cached to prevent port
     * creation delay after the first occurance.
     */
    private static PatientCorrelationService patientCorrelationService;
    private static PatientCorrelationServiceSecured service = new gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationServiceSecured();

    /**
     * Get patient correlation service port object.
     * 
     * @return Patient correlation service port.
     */
    public PatientCorrelationSecuredPortType getPort(String url)
    {
        if(service == null)
        {
            service = new gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationServiceSecured();
        }
        PatientCorrelationSecuredPortType port = service.getPatientCorrelationSecuredPort();

        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        return port;
    }



    /**
     * Call the web service operation to retreive patient correlations for
     * the patient provided.
     *
     * @param correlationInput 201309 retrieve request message
     * @return Retrieved patient correlations
     */
    public org.hl7.v3.PRPAIN201310UV retrievePatientCorrelations(org.hl7.v3.PRPAIN201309UV correlationInput)
    {
        org.hl7.v3.PRPAIN201310UV response = null;

        log.error("Method No longer Supported. Must pass assertion information");
        return response;
    }
    private PatientCorrelationPortType getPatientCorrelationPort()
    {
        if(patientCorrelationService == null)
        {
            patientCorrelationService = new gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationService();
        }
        PatientCorrelationPortType port = patientCorrelationService.getPatientCorrelationPort();
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getPatientCorrelationEndpointAddress());
        return port;
    }
    public org.hl7.v3.PRPAIN201310UV retrievePatientCorrelations(org.hl7.v3.PRPAIN201309UV correlationInput, AssertionType assertion)
    {
        org.hl7.v3.PRPAIN201310UV response = null;
        
        try
        { // Call Web Service Operation
            //String url = getPatientCorrelationEndpointAddress();
            //PatientCorrelationSecuredPortType port = getPort(url);
            PatientCorrelationPortType port = getPatientCorrelationPort();

           //org.hl7.v3.RetrievePatientCorrelationsSecuredRequestType retrievePatientCorrelationsRequest = new org.hl7.v3.RetrievePatientCorrelationsSecuredRequestType();
            //retrievePatientCorrelationsRequest.setPRPAIN201309UV(correlationInput);

            //SamlTokenCreator tokenCreator = new SamlTokenCreator();
            //Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.PAT_CORR_ACTION);

            // TODO process result here
            //org.hl7.v3.RetrievePatientCorrelationsSecuredResponseType result = port.retrievePatientCorrelations(retrievePatientCorrelationsRequest, assertion);

            org.hl7.v3.RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest = new org.hl7.v3.RetrievePatientCorrelationsRequestType();

            retrievePatientCorrelationsRequest.setAssertion(assertion);

            retrievePatientCorrelationsRequest.setPRPAIN201309UV(correlationInput);

            org.hl7.v3.RetrievePatientCorrelationsResponseType result = port.retrievePatientCorrelations(retrievePatientCorrelationsRequest);

            if(result != null)
            {
                response = result.getPRPAIN201310UV();
            }
        }
        catch (Exception ex)
        {
            log.error("Exception encountered retrieving patient correlations: " + ex.getMessage(), ex);
        }
        return response;
    }

    /**
     * Call the web service operation to add a patient correlation record.
     *
     * @param request 201301 request message
     * @return Add correlation response message
     */
    public org.hl7.v3.MCCIIN000002UV01 addPatientCorrelation(org.hl7.v3.PRPAIN201301UV request)
    {
        org.hl7.v3.MCCIIN000002UV01 response = null;
        log.error("Method No longer Supported. Must pass assertion information");

        return response;
    }
    public org.hl7.v3.MCCIIN000002UV01 addPatientCorrelation(org.hl7.v3.PRPAIN201301UV request, AssertionType assertion)
    {
        org.hl7.v3.MCCIIN000002UV01 response = null;
        try
        { // Call Web Service Operation
            String url = getPatientCorrelationEndpointAddress();
            PatientCorrelationSecuredPortType port = getPort(url);

            org.hl7.v3.AddPatientCorrelationSecuredRequestType addPatientCorrelationRequest = new org.hl7.v3.AddPatientCorrelationSecuredRequestType();
            addPatientCorrelationRequest.setPRPAIN201301UV(request);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.PAT_CORR_ACTION);

            // TODO process result here
            org.hl7.v3.AddPatientCorrelationSecuredResponseType result = port.addPatientCorrelation(addPatientCorrelationRequest);
            if(result != null)
            {
                response = result.getMCCIIN000002UV01();
            }
        }
        catch (Exception ex)
        {
            log.error("Exception encountered adding a patient correlation: " + ex.getMessage(), ex);
        }
        return response;
    }

    /**
     * Call the web service operation to remove a patient correlation record.
     *
     * @param request 201303 request message
     * @return Removal response message
     */
    public org.hl7.v3.MCCIIN000002UV01 removePatientCorrelation(org.hl7.v3.PRPAIN201303UV request)
    {
        org.hl7.v3.MCCIIN000002UV01 response = null;
        log.error("Method No longer Supported. Must pass assertion information");

        return response;
    }
    public org.hl7.v3.MCCIIN000002UV01 removePatientCorrelation(org.hl7.v3.PRPAIN201303UV request, AssertionType assertion)
    {
        org.hl7.v3.MCCIIN000002UV01 response = null;
        try
        { // Call Web Service Operation
            String url = getPatientCorrelationEndpointAddress();
            PatientCorrelationSecuredPortType port = getPort(url);

            org.hl7.v3.RemovePatientCorrelationSecuredRequestType removePatientCorrelationRequest = new org.hl7.v3.RemovePatientCorrelationSecuredRequestType();
            removePatientCorrelationRequest.setPRPAIN201303UV(request);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.PAT_CORR_ACTION);

            // TODO process result here
            org.hl7.v3.RemovePatientCorrelationSecuredResponseType result = port.removePatientCorrelation(removePatientCorrelationRequest);
            if(result != null)
            {
                response = result.getMCCIIN000002UV01();
            }
        }
        catch (Exception ex)
        {
            log.error("Exception encountered removing a patient correlation: " + ex.getMessage(), ex);
        }
        return response;
    }

    /**
     * Retrieve the endpoint address for the patient correlation service.
     *
     * @return Patient correlation service endpoint address.
     */
    private String getPatientCorrelationEndpointAddress()
    {
        String endpointAddress = null;
        try
        {
            // Lookup home community id
            String homeCommunity = getHomeCommunityId();
            // Get endpoint url
            endpointAddress = ConnectionManagerCache.getEndpointURLByServiceName(homeCommunity, NhincConstants.PATIENT_CORRELATION_SERVICE_NAME);
            log.debug("Patient correlation endpoint address: " + endpointAddress);
        }
        catch (PropertyAccessException pae)
        {
            log.error("Exception encountered retrieving the local home community: " + pae.getMessage(), pae);
        }
        catch (ConnectionManagerException cme)
        {
            log.error("Exception encountered retrieving the patient correlation connection endpoint address: " + cme.getMessage(), cme);
        }
        return endpointAddress;
    }



    /**
     * Retrieve the local home community id
     * 
     * @return Local home community id
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException
     */
    public String getHomeCommunityId() throws PropertyAccessException
    {
        return PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_LOCAL_HOME_COMMUNITY);
    }
}
