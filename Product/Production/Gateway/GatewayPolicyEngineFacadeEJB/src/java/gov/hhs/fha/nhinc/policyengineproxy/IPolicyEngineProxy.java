/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.policyengineproxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;

/**
 *
 * @author jhoppesc
 */
public interface IPolicyEngineProxy {
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest);
}
