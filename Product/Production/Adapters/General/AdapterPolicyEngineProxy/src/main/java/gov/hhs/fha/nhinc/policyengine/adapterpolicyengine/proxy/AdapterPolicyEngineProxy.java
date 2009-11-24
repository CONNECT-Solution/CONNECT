package gov.hhs.fha.nhinc.policyengine.adapterpolicyengine.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;

/**
 * This is the proxy interface for the AdapterPolicyEngine.  It
 * is used to abstract the concrete implementations of the AdapterPolicyEngine.
 *
 * @author Les Westberg
 */
public interface AdapterPolicyEngineProxy
{
    /**
     * Given a request to check the access policy, this service will interface
     * with the AdapterPolicyEngineOrchestrator to determine if access is to be
     * granted or denied.
     *
     * @param checkPolicyRequest The request to check defined policy
     * @return The response which contains the access decision
     */
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest);

}
