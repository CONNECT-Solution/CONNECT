package gov.hhs.fha.nhinc.policyengine.adapterpolicyengineorchestrator.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;

import gov.hhs.fha.nhinc.adapterpolicyengineorchestrator.AdapterPolicyEngineOrchestrator;
import gov.hhs.fha.nhinc.adapterpolicyengineorchestrator.AdapterPolicyEngineOrchestratorPortType;
import gov.hhs.fha.nhinc.policyengine.adapterpolicyengineorchestrator.AdapterPolicyEngineOrchestratorException;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;


import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the concrete implementation for the web service based call to the
 * AdapterPolicyEngineOrchestrator.
 *
 * @author Les Westberg
 */
public class AdapterPolicyEngineOrchestratorWebServiceProxy implements AdapterPolicyEngineOrchestratorProxy
{
    private static Log log = LogFactory.getLog(AdapterPolicyEngineOrchestratorWebServiceProxy.class);
    private static AdapterPolicyEngineOrchestrator oAdapterPolicyEngineOrhcestratorService = null;
    private static String ADAPTER_POLICY_ENGINE_ORCHESTRATOR_SERVICE_NAME = "adapterpolicyengineorchestrator";
    private static String ADAPTER_POLICY_ENGINE_ORCHESTRATOR_DEFAULT_SERVICE_URL = "http://localhost:8080/NhinConnect/AdapterPolicyEngineOrchestrator";

    /**
     * Return a handle to the AdapterPolicyEngineOrchestrator web service.
     *
     * @return The handle to the Adapter Policy Engine Orchestrator port web service.
     */
    private AdapterPolicyEngineOrchestratorPortType getAdapterPolicyEngineOrchestratorPortType()
        throws AdapterPolicyEngineOrchestratorException
    {
        AdapterPolicyEngineOrchestratorPortType oAdapterPolicyEngineOrchestratorPort = null;

        try
        {
            if (oAdapterPolicyEngineOrhcestratorService == null)
            {
                oAdapterPolicyEngineOrhcestratorService = new AdapterPolicyEngineOrchestrator();
            }

            oAdapterPolicyEngineOrchestratorPort = oAdapterPolicyEngineOrhcestratorService.getAdapterPolicyEngineOrchestratorPortSoap11();

            // Get the real endpoint URL for this service.
            //--------------------------------------------
            String sEndpointURL = ConnectionManagerCache.getLocalEndpointURLByServiceName(ADAPTER_POLICY_ENGINE_ORCHESTRATOR_SERVICE_NAME);

            if ((sEndpointURL == null) ||
                (sEndpointURL.length() <= 0))
            {
                sEndpointURL = ADAPTER_POLICY_ENGINE_ORCHESTRATOR_DEFAULT_SERVICE_URL;
                String sErrorMessage = "Failed to retrieve the Endpoint URL for service: '" +
                                       ADAPTER_POLICY_ENGINE_ORCHESTRATOR_DEFAULT_SERVICE_URL + "'.  " +
                                       "Setting this to: '" + sEndpointURL + "'";
                log.warn(sErrorMessage);
            }
			gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) oAdapterPolicyEngineOrchestratorPort, sEndpointURL);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to retrieve a handle to the Adapter Policy Engine Orchestrator web service.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPolicyEngineOrchestratorException(sErrorMessage, e);
        }

        return oAdapterPolicyEngineOrchestratorPort;
    }

    /**
     * Given a request to check the access policy, this service will interface
     * with the Adapter PEP to determine if access is to be granted or denied.
     *
     * @param checkPolicyRequest The request to check defined policy
     * @return The response which contains the access decision
     */
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest)
    {
        CheckPolicyResponseType oResponse = new CheckPolicyResponseType();

        try
        {
            AdapterPolicyEngineOrchestratorPortType oAdapterPolicyEngineOrchestratorPort = getAdapterPolicyEngineOrchestratorPortType();
					
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    oResponse = oAdapterPolicyEngineOrchestratorPort.checkPolicy(checkPolicyRequest);
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
                            log.error("Thread Got Interrupted while waiting on AdapterPolicyEngineOrchestrator call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on AdapterPolicyEngineOrchestrator call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call AdapterPolicyEngineOrchestrator Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call AdapterPolicyEngineOrchestrator Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            oResponse = oAdapterPolicyEngineOrchestratorPort.checkPolicy(checkPolicyRequest);
        }
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling AdapterPolicyEngineOrchestratorWebServiceProxy.checkPolicy.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        return oResponse;
    }

}
