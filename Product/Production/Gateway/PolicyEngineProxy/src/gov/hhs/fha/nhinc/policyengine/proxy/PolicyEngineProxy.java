/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.policyengine.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;

/**
 *
 * @author Jon Hoppesch
 */
public interface PolicyEngineProxy {

    /**
     * Checks the policy for an operation.
     *
     * @param request Generic Policy Check request message
     * @return The policy check results
     */
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest);
}
