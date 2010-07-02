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
public class PatientCorrelationImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PatientCorrelationImpl.class);
    private static PatientCorrelationServiceSecured service = new gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationServiceSecured();

    public org.hl7.v3.RetrievePatientCorrelationsResponseType retrievePatientCorrelations(org.hl7.v3.RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest) {
        org.hl7.v3.RetrievePatientCorrelationsResponseType response = null;
        org.hl7.v3.RetrievePatientCorrelationsSecuredResponseType securedResponse = null;
        try {
            log.info(retrievePatientCorrelationsRequest.getAssertion());

            String url = getURL();
            PatientCorrelationSecuredPortType port = getPort(url);
            AssertionType assertIn = retrievePatientCorrelationsRequest.getAssertion();

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.PAT_CORR_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            org.hl7.v3.RetrievePatientCorrelationsSecuredRequestType body = new org.hl7.v3.RetrievePatientCorrelationsSecuredRequestType();

            body.setPRPAIN201309UV02(retrievePatientCorrelationsRequest.getPRPAIN201309UV02());

            securedResponse = port.retrievePatientCorrelations(body);

            response = new org.hl7.v3.RetrievePatientCorrelationsResponseType();

            response.setPRPAIN201310UV02(securedResponse.getPRPAIN201310UV02());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return response;
    }

    public org.hl7.v3.AddPatientCorrelationResponseType addPatientCorrelation(org.hl7.v3.AddPatientCorrelationRequestType addPatientCorrelationRequest) {
        org.hl7.v3.AddPatientCorrelationResponseType response = null;
        org.hl7.v3.AddPatientCorrelationSecuredResponseType securedResponse = null;

        try {
            String url = getURL();
            PatientCorrelationSecuredPortType port = getPort(url);
            AssertionType assertIn = addPatientCorrelationRequest.getAssertion();

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.PAT_CORR_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            org.hl7.v3.AddPatientCorrelationSecuredRequestType body = new org.hl7.v3.AddPatientCorrelationSecuredRequestType();


            body.setPRPAIN201301UV02(addPatientCorrelationRequest.getPRPAIN201301UV02());

            securedResponse = port.addPatientCorrelation(body);

            response = new org.hl7.v3.AddPatientCorrelationResponseType();

            if (securedResponse != null &&
                    securedResponse.getMCCIIN000002UV01() != null) {
                response.setMCCIIN000002UV01(securedResponse.getMCCIIN000002UV01());
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return response;
    }

    private String getURL() {
        String url = "";

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.PATIENT_CORRELATION_SECURED_SERVICE_NAME);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }

    private PatientCorrelationSecuredPortType getPort(String url) {



        PatientCorrelationSecuredPortType port = service.getPatientCorrelationSecuredPort();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
