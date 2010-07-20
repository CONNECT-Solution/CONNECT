/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelation.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationPortType;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import java.util.StringTokenizer;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.AddPatientCorrelationRequestType;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.RetrievePatientCorrelationsRequestType;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

/**
 *
 * @author jhoppesc
 */
public class PatientCorrelationProxyWebServiceUnsecuredImpl implements PatientCorrelationProxy {
    private static Log log = LogFactory.getLog(PatientCorrelationProxyWebServiceUnsecuredImpl.class);
    private static PatientCorrelationService service = new PatientCorrelationService();

    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(PRPAIN201309UV02 request, AssertionType assertion, NhinTargetCommunitiesType targets) {
        RetrievePatientCorrelationsResponseType result = new RetrievePatientCorrelationsResponseType();
        RetrievePatientCorrelationsRequestType msg = new RetrievePatientCorrelationsRequestType();
        msg.setAssertion(assertion);
        msg.setPRPAIN201309UV02(request);
        String url = null;

        url = getUrl();

        if (NullChecker.isNotNullish(url)) {
            PatientCorrelationPortType port = getPort(url, assertion);
            			
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    result = port.retrievePatientCorrelations(msg);
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
                            log.error("Thread Got Interrupted while waiting on PatientCorrelationService call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on PatientCorrelationService call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call PatientCorrelationService Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call PatientCorrelationService Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            result = port.retrievePatientCorrelations(msg);
        }
	
        }

        return result;
    }

    public AddPatientCorrelationResponseType addPatientCorrelation(PRPAIN201301UV02 request, AssertionType assertion, NhinTargetCommunitiesType targets) {
        AddPatientCorrelationResponseType result = new AddPatientCorrelationResponseType();
        AddPatientCorrelationRequestType msg = new AddPatientCorrelationRequestType();
        msg.setAssertion(assertion);
        msg.setPRPAIN201301UV02(request);

        String url = null;

        url = getUrl();

        if (NullChecker.isNotNullish(url)) {
            PatientCorrelationPortType port = getPort(url, assertion);
        
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
        int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        if (retryCount > 0 && retryDelay > 0) {
            int i = 1;
            javax.xml.ws.WebServiceException catchExp = null;
            while (i <= retryCount) {
                try {
                    result = port.addPatientCorrelation(msg);
                    break;
                } catch (javax.xml.ws.WebServiceException e) {
                    catchExp = e;
                    log.warn("Exception calling ... web service: " + e.getMessage());
                    System.out.println("retrying the connection for attempt [ " + i + " ] after [ " + retryDelay + " ] seconds");
                    log.info("retrying attempt [ " + i + " ] the connection after [ " + retryDelay + " ] seconds");
                    i++;
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException iEx) {
                        log.error("Thread Got Interrupted while waiting on DocumentRegistryService call :" + iEx);
                    } catch (IllegalArgumentException iaEx) {
                        log.error("Thread Got Interrupted while waiting on DocumentRegistryService call :" + iaEx);
                    }
                    retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                }
            }

            if (retryCount == 0 && catchExp != null) {
                log.error("Unable to call DocumentRegistryService Webservice due to  : " + catchExp);
                throw catchExp;
            }
        } else {
            result = port.addPatientCorrelation(msg);
        }
		


        }

        return result;
    }

    private String getUrl () {
        String url = null;

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.PATIENT_CORRELATION_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.PATIENT_CORRELATION_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        return url;
    }

    private PatientCorrelationPortType getPort(String url, AssertionType assertion) {
        PatientCorrelationPortType port = service.getPatientCorrelationPort();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.PAT_CORR_ACTION);

        ((BindingProvider) port).getRequestContext().putAll(requestContext);

        return port;
    }

}
