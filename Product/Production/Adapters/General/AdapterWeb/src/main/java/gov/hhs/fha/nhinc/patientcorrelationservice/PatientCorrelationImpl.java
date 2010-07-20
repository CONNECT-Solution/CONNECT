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
import java.util.StringTokenizer;

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
            
			
			
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    securedResponse = port.retrievePatientCorrelations(body);
                    break;
                } catch (javax.xml.ws.WebServiceException e) {
                    catchExp = e;
                    int flag = 0;
                    StringTokenizer st = new StringTokenizer(exceptionText, ",");
                    while (st.hasMoreTokens()) {
                        if (e.getMessage().contains(st.nextToken())) {
                            flag = 1;
                        }
                    }
                    if (flag == 1) {
                        log.warn("Exception calling ... web service: " + e.getMessage());
                        System.out.println("retrying the connection for attempt [ " + i + " ] after [ " + retryDelay + " ] seconds");
                        log.info("retrying attempt [ " + i + " ] the connection after [ " + retryDelay + " ] seconds");
                        i++;
                        try {
                            Thread.sleep(retryDelay);
                        } catch (InterruptedException iEx) {
                            log.error("Thread Got Interrupted while waiting on PatientCorrelationServiceSecured call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on PatientCorrelationServiceSecured call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call PatientCorrelationServiceSecured Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call PatientCorrelationServiceSecured Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            securedResponse = port.retrievePatientCorrelations(body);
        }
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

            
			
					int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    securedResponse = port.addPatientCorrelation(body);
                    break;
                } catch (javax.xml.ws.WebServiceException e) {
                    catchExp = e;
                    int flag = 0;
                    StringTokenizer st = new StringTokenizer(exceptionText, ",");
                    while (st.hasMoreTokens()) {
                        if (e.getMessage().contains(st.nextToken())) {
                            flag = 1;
                        }
                    }
                    if (flag == 1) {
                        log.warn("Exception calling ... web service: " + e.getMessage());
                        System.out.println("retrying the connection for attempt [ " + i + " ] after [ " + retryDelay + " ] seconds");
                        log.info("retrying attempt [ " + i + " ] the connection after [ " + retryDelay + " ] seconds");
                        i++;
                        try {
                            Thread.sleep(retryDelay);
                        } catch (InterruptedException iEx) {
                            log.error("Thread Got Interrupted while waiting on PatientCorrelationServiceSecured call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on PatientCorrelationServiceSecured call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call PatientCorrelationServiceSecured Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call PatientCorrelationServiceSecured Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            securedResponse = port.addPatientCorrelation(body);
        }
		
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
