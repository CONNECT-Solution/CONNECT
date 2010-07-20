package gov.hhs.fha.nhinc.patientcorrelationfacade.helper;

import gov.hhs.fha.nhinc.componentpatientcorrelationfacadedte.NhincComponentPatientCorrelationFacadeDteService;
import gov.hhs.fha.nhinc.componentpatientcorrelationfacadedte.PatientCorrelationFacadeDte;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsResponseType;
import java.util.StringTokenizer;

/**
 * Patient Correlation transform helper
 * 
 * @author Neil Webb
 */
public class TransformHelper
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(TransformHelper.class);

    private static final String ENDPOINT_ADDRESS_TRANSFORM = "http://localhost:8080/CONNECTGatewayInternal/GatewayService/PatientCorrelationFacadeDteService";

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
		gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, ENDPOINT_ADDRESS_TRANSFORM);
        return port;
    }

    /**
     * Call the web service operation to create a PIX retrieve message that is
     * used to retrieve patient correlation records.
     *
     * @param request Retrieve correlations request message
     * @return 201309 retrieve message.
     */
    public org.hl7.v3.PRPAIN201309UV02 createPixRetrieve(RetrievePatientCorrelationsRequestType request)
    {
        org.hl7.v3.PRPAIN201309UV02 result = null;
        try
        { // Call Web Service Operation
            PatientCorrelationFacadeDte port = getPort();

            // Create transform input object
            org.hl7.v3.CreatePixRetrieveRequestType createPixRetrieveRequest = new org.hl7.v3.CreatePixRetrieveRequestType();
            createPixRetrieveRequest.setRetrievePatientCorrelationsRequest(request);

            // Capture results for return
            org.hl7.v3.CreatePixRetrieveResponseType response = null;
			
			
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    response = port.createPixRetrieve(createPixRetrieveRequest);
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
                            log.error("Thread Got Interrupted while waiting on NhincComponentPatientCorrelationFacadeDteService call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on NhincComponentPatientCorrelationFacadeDteService call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call NhincComponentPatientCorrelationFacadeDteService Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call NhincComponentPatientCorrelationFacadeDteService Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            response = port.createPixRetrieve(createPixRetrieveRequest);
        }
            if(response != null)
            {
                result = response.getPRPAIN201309UV02();
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
    public RetrievePatientCorrelationsResponseType createFacadeRetrieveResult(org.hl7.v3.PRPAIN201310UV02 request)
    {
        RetrievePatientCorrelationsResponseType response = null;
        try
        { // Call Web Service Operation
            PatientCorrelationFacadeDte port = getPort();

            org.hl7.v3.CreateFacadeRetrieveResultRequestType createFacadeRetrieveResultRequest = new org.hl7.v3.CreateFacadeRetrieveResultRequestType();
            createFacadeRetrieveResultRequest.setPRPAIN201310UV02(request);
            
            // Capture results for return
            org.hl7.v3.CreateFacadeRetrieveResultResponseType result = null;
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
        int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        if (retryCount > 0 && retryDelay > 0) {
            int i = 1;
            javax.xml.ws.WebServiceException catchExp = null;
            while (i <= retryCount) {
                try {
                    result = port.createFacadeRetrieveResult(createFacadeRetrieveResultRequest);
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
                        log.error("Thread Got Interrupted while waiting on NhincComponentPatientCorrelationFacadeDteService call :" + iEx);
                    } catch (IllegalArgumentException iaEx) {
                        log.error("Thread Got Interrupted while waiting on NhincComponentPatientCorrelationFacadeDteService call :" + iaEx);
                    }
                    retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                }
            }

            if (retryCount == 0 && catchExp != null) {
                log.error("Unable to call NhincComponentPatientCorrelationFacadeDteService Webservice due to  : " + catchExp);
                throw catchExp;
            }
        } else {
            result = port.createFacadeRetrieveResult(createFacadeRetrieveResultRequest);
        }
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
    public org.hl7.v3.PRPAIN201301UV02 createPixAdd(AddPatientCorrelationRequestType request)
    {
        org.hl7.v3.PRPAIN201301UV02 response = null;
        try
        { // Call Web Service Operation
            PatientCorrelationFacadeDte port = getPort();

            org.hl7.v3.CreatePixAddRequestType createPixAddRequest = new org.hl7.v3.CreatePixAddRequestType();
            createPixAddRequest.setAddPatientCorrelationRequest(request);

            // Capture results for return
            org.hl7.v3.CreatePixAddResponseType result = null;
			
			
			int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
			int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    result = port.createPixAdd(createPixAddRequest);
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
                            log.error("Thread Got Interrupted while waiting on NhincComponentPatientCorrelationFacadeDteService call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on NhincComponentPatientCorrelationFacadeDteService call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call NhincComponentPatientCorrelationFacadeDteService Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call NhincComponentPatientCorrelationFacadeDteService Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            result = port.createPixAdd(createPixAddRequest);
        }
		
            if(result != null)
            {
                response = result.getPRPAIN201301UV02();
            }
        }
        catch (Exception ex)
        {
            log.error("Exception encountered calling createPixAdd: " + ex.getMessage(), ex);
        }
        return response;
    }
}
