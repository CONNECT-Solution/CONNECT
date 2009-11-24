package gov.hhs.fha.nhinc.policyengine.adapterpolicyengine;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;

import gov.hhs.fha.nhinc.policyengine.adapterpolicyengineorchestrator.proxy.AdapterPolicyEngineOrchestratorProxy;
import gov.hhs.fha.nhinc.policyengine.adapterpolicyengineorchestrator.proxy.AdapterPolicyEngineOrchestratorProxyObjectFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is the implementation of the AdapterPolicyEngine.   It is the entry
 * point from the Gateway to the Adapter Policy Engine.
 *
 * @author Les Westberg
 */
public class AdapterPolicyEngineImpl
{
    private static Log log = LogFactory.getLog(AdapterPolicyEngineImpl.class);

    /**
     * Given a request to check the access policy, this service will interface
     * with the Adapter PEP to determine if access is to be granted or denied.
     * @param checkPolicyRequest The request to check defined policy
     * @return The response which contains the access decision
     */
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest)
        throws AdapterPolicyEngineException
    {
        CheckPolicyResponseType oCheckPolicyResp = new CheckPolicyResponseType();

        AdapterPolicyEngineOrchestratorProxyObjectFactory oFactory = new AdapterPolicyEngineOrchestratorProxyObjectFactory();
        AdapterPolicyEngineOrchestratorProxy oOrchProxy = oFactory.getAdapterPolicyEngineOrchestratorProxy();
        log.debug("AdapterPolicyEngineOrchestrator Proxy selected: " + oOrchProxy.getClass());

        try
        {
            oCheckPolicyResp = oOrchProxy.checkPolicy(checkPolicyRequest);
        }
        catch (Exception e)
        {
            String sMessage = "Error occurred calling AdapterPolicyEngineOrhcestratorProxy.checkPolicy.  Error: " +
                e.getMessage();
            log.error(sMessage, e);
            throw new AdapterPolicyEngineException(sMessage, e);
        }
        return oCheckPolicyResp;
    }
}
