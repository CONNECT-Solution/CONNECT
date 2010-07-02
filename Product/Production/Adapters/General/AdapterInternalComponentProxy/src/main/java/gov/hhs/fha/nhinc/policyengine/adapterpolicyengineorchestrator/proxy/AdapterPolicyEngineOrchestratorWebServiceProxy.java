package gov.hhs.fha.nhinc.policyengine.adapterpolicyengineorchestrator.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;

import gov.hhs.fha.nhinc.adapterpolicyengineorchestrator.AdapterPolicyEngineOrchestrator;
import gov.hhs.fha.nhinc.adapterpolicyengineorchestrator.AdapterPolicyEngineOrchestratorPortType;
import gov.hhs.fha.nhinc.policyengine.adapterpolicyengineorchestrator.AdapterPolicyEngineOrchestratorException;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;


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
            oResponse = oAdapterPolicyEngineOrchestratorPort.checkPolicy(checkPolicyRequest);
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
