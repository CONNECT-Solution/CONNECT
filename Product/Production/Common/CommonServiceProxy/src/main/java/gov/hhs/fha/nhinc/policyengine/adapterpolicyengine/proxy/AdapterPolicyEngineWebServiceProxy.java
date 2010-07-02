package gov.hhs.fha.nhinc.policyengine.adapterpolicyengine.proxy;

//import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;

import gov.hhs.fha.nhinc.adapterpolicyengine.AdapterPolicyEngine;
import gov.hhs.fha.nhinc.adapterpolicyengine.AdapterPolicyEnginePortType;
import gov.hhs.fha.nhinc.policyengine.adapterpolicyengine.AdapterPolicyEngineException;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the concrete implementation for the web service based call to the
 * AdapterPolicyEngine.
 *
 * @author Les Westberg
 */
public class AdapterPolicyEngineWebServiceProxy implements AdapterPolicyEngineProxy
{
    private static Log log = LogFactory.getLog(AdapterPolicyEngineWebServiceProxy.class);
    private static AdapterPolicyEngine oAdapterPolicyEngineService = null;
    private static String ADAPTER_POLICY_ENGINE_SERVICE_NAME = "adapterpolicyengine";
    private static String ADAPTER_POLICY_ENGINE_DEFAULT_SERVICE_URL = "http://localhost:8080/NhinConnect/AdapterPolicyEngine";

    /**
     * Return a handle to the AdapterPolicyEngine web service.
     *
     * @return The handle to the Adapter Policy Engine port web service.
     */
    private AdapterPolicyEnginePortType getAdapterPolicyEnginePortType()
        throws AdapterPolicyEngineException
    {
        AdapterPolicyEnginePortType oAdapterPolicyEnginePort = null;

        try
        {
            if (oAdapterPolicyEngineService == null)
            {
                oAdapterPolicyEngineService = new AdapterPolicyEngine();
            }

            oAdapterPolicyEnginePort = oAdapterPolicyEngineService.getAdapterPolicyEnginePortSoap11();

            // Get the real endpoint URL for this service.
            //--------------------------------------------
            String sEndpointURL = ConnectionManagerCache.getLocalEndpointURLByServiceName(ADAPTER_POLICY_ENGINE_SERVICE_NAME);

            if ((sEndpointURL == null) ||
                (sEndpointURL.length() <= 0))
            {
                sEndpointURL = ADAPTER_POLICY_ENGINE_DEFAULT_SERVICE_URL;
                String sErrorMessage = "Failed to retrieve the Endpoint URL for service: '" +
                                       ADAPTER_POLICY_ENGINE_DEFAULT_SERVICE_URL + "'.  " +
                                       "Setting this to: '" + sEndpointURL + "'";
                log.warn(sErrorMessage);
            }
			gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) oAdapterPolicyEnginePort, sEndpointURL);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to retrieve a handle to the Adapter Policy Engine web service.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPolicyEngineException(sErrorMessage, e);
        }

        return oAdapterPolicyEnginePort;
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
            AdapterPolicyEnginePortType oAdapterPolicyEnginePort = getAdapterPolicyEnginePortType();
            oResponse = oAdapterPolicyEnginePort.checkPolicy(checkPolicyRequest);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling AdapterPolicyEngineWebServiceProxy.checkPolicy.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        return oResponse;
    }

}
