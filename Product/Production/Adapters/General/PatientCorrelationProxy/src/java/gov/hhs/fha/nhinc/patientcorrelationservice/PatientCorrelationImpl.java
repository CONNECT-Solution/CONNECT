/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelationservice;

import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.*;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;

/**
 *
 * @author dunnek
 */
public class PatientCorrelationImpl
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PatientCorrelationImpl.class);
    private static PatientCorrelationServiceSecured service = new gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationServiceSecured();

    public org.hl7.v3.RetrievePatientCorrelationsResponseType retrievePatientCorrelations(org.hl7.v3.RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest)
    {
        org.hl7.v3.RetrievePatientCorrelationsResponseType response = null;
        org.hl7.v3.RetrievePatientCorrelationsSecuredResponseType securedResponse = null;
        try
        {
            log.info(retrievePatientCorrelationsRequest.getAssertion());
            
            String url = getURL();
            PatientCorrelationSecuredPortType port = getPort(url);
            AssertionType assertIn = retrievePatientCorrelationsRequest.getAssertion();

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.PAT_CORR_ACTION);

            org.hl7.v3.RetrievePatientCorrelationsSecuredRequestType body = new org.hl7.v3.RetrievePatientCorrelationsSecuredRequestType();
                        
            body.setPRPAIN201309UV(retrievePatientCorrelationsRequest.getPRPAIN201309UV());

            securedResponse = port.retrievePatientCorrelations(body);
            
            response = new org.hl7.v3.RetrievePatientCorrelationsResponseType();

            response.setPRPAIN201310UV(securedResponse.getPRPAIN201310UV());
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return response;
    }
    public org.hl7.v3.AddPatientCorrelationResponseType addPatientCorrelation(org.hl7.v3.AddPatientCorrelationRequestType addPatientCorrelationRequest)
    {
        org.hl7.v3.AddPatientCorrelationResponseType response = null;
        org.hl7.v3.AddPatientCorrelationSecuredResponseType securedResponse = null;

        try
        {
            String url = getURL();
            PatientCorrelationSecuredPortType port = getPort(url);
            AssertionType assertIn = addPatientCorrelationRequest.getAssertion();

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.PAT_CORR_ACTION);

            org.hl7.v3.AddPatientCorrelationSecuredRequestType body = new org.hl7.v3.AddPatientCorrelationSecuredRequestType();

 
            body.setPRPAIN201301UV(addPatientCorrelationRequest.getPRPAIN201301UV());

            securedResponse = port.addPatientCorrelation(body);

            response = new org.hl7.v3.AddPatientCorrelationResponseType();

            response.setMCCIIN000002UV01(securedResponse.getMCCIIN000002UV01());
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return response;
    }
    public org.hl7.v3.RemovePatientCorrelationResponseType removePatientCorrelation(org.hl7.v3.RemovePatientCorrelationRequestType removePatientCorrelationRequest)
    {
        org.hl7.v3.RemovePatientCorrelationResponseType response = null;
        org.hl7.v3.RemovePatientCorrelationSecuredResponseType securedResponse = null;
        try
        {
            String url = getURL();
            PatientCorrelationSecuredPortType port = getPort(url);
            AssertionType assertIn = removePatientCorrelationRequest.getAssertion();

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.PAT_CORR_ACTION);

            org.hl7.v3.RemovePatientCorrelationSecuredRequestType body = new org.hl7.v3.RemovePatientCorrelationSecuredRequestType();

            body.setPRPAIN201303UV(removePatientCorrelationRequest.getPRPAIN201303UV());

            securedResponse = port.removePatientCorrelation(body);

            response = new org.hl7.v3.RemovePatientCorrelationResponseType();

            response.setMCCIIN000002UV01(securedResponse.getMCCIIN000002UV01());
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return response;
    }

    private String getURL()
    {
        String url = "";

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.PATIENT_CORRELATION_SECURED_SERVICE_NAME);
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }
    private PatientCorrelationSecuredPortType getPort(String url)
    {

        

        PatientCorrelationSecuredPortType port = service.getPatientCorrelationSecuredPort();

        log.info("Setting endpoint address to Patient Correlation Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
}
