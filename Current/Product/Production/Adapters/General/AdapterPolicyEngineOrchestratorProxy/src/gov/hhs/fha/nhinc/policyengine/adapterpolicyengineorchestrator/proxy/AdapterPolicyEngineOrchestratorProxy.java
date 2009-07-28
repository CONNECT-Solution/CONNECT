package gov.hhs.fha.nhinc.policyengine.adapterpolicyengineorchestrator.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;


/**
 * This is the proxy interface for the AdapterPolicyEngineOrchestrator.  It
 * is used to abstract the concrete implementations of the AdapterPolicyEngineOrchestrator.
 *
 * @author Les Westberg
 */
public interface AdapterPolicyEngineOrchestratorProxy
{
    /**
     * Given a request to check the access policy, this service will interface
     * with the Adapter PEP to determine if access is to be granted or denied.
     *
     * @param checkPolicyRequest The request to check defined policy
     * @return The response which contains the access decision
     */
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest);
    
}
