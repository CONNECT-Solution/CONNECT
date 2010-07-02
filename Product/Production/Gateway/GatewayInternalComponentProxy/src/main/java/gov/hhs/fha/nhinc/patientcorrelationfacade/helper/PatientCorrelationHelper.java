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

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }



    /**
     * Call the web service operation to retreive patient correlations for
     * the patient provided.
     *
     * @param correlationInput 201309 retrieve request message
     * @return Retrieved patient correlations
     */
    public org.hl7.v3.PRPAIN201310UV02 retrievePatientCorrelations(org.hl7.v3.PRPAIN201309UV02 correlationInput)
    {
        org.hl7.v3.PRPAIN201310UV02 response = null;

        log.error("Method No longer Supported. Must pass assertion information");
        return response;
    }

    public org.hl7.v3.PRPAIN201310UV02 retrievePatientCorrelations(org.hl7.v3.PRPAIN201309UV02 correlationInput, AssertionType assertion)
    {
        org.hl7.v3.PRPAIN201310UV02 response = null;
        log.debug("in PatientCorrelationHelper retrievePatientCorrelations");
        try
        { // Call Web Service Operation
            PatientCorrelationPortType port = getPatientCorrelationPort();

            org.hl7.v3.RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest = new org.hl7.v3.RetrievePatientCorrelationsRequestType();

            retrievePatientCorrelationsRequest.setAssertion(assertion);

            retrievePatientCorrelationsRequest.setPRPAIN201309UV02(correlationInput);

            org.hl7.v3.RetrievePatientCorrelationsResponseType result = port.retrievePatientCorrelations(retrievePatientCorrelationsRequest);

            if(result != null)
            {
                response = result.getPRPAIN201310UV02();
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
    public org.hl7.v3.MCCIIN000002UV01 addPatientCorrelation(org.hl7.v3.PRPAIN201301UV02 request)
    {
        org.hl7.v3.MCCIIN000002UV01 response = null;
        log.error("Method No longer Supported. Must pass assertion information");

        return response;
    }
    public org.hl7.v3.MCCIIN000002UV01 addPatientCorrelation(org.hl7.v3.PRPAIN201301UV02 request, AssertionType assertion)
    {
  
        log.debug("in PatientCorrelationHelper.removePatientCorrelation");
        org.hl7.v3.MCCIIN000002UV01 response = null;
        try
        { // Call Web Service Operation
            PatientCorrelationPortType port = getPatientCorrelationPort();

            org.hl7.v3.AddPatientCorrelationRequestType addPatientCorrelationRequest = new org.hl7.v3.AddPatientCorrelationRequestType();

            addPatientCorrelationRequest.setPRPAIN201301UV02(request);
            addPatientCorrelationRequest.setAssertion(assertion);

            // TODO process result here
            org.hl7.v3.AddPatientCorrelationResponseType result = port.addPatientCorrelation(addPatientCorrelationRequest);
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

    private PatientCorrelationPortType getPatientCorrelationPort()
    {
        if(patientCorrelationService == null)
        {
            patientCorrelationService = new gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationService();
        }
        PatientCorrelationPortType port = patientCorrelationService.getPatientCorrelationPort();
		gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, getPatientCorrelationEndpointAddress());
        return port;
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
