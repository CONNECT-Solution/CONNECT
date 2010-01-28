/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.policyengine.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;

/**
 *
 * @author Jon Hoppesch
 */
public class PolicyEngineNoOpImpl implements PolicyEngineProxy {

    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest) {
        CheckPolicyResponseType checkPolicyResponse = new CheckPolicyResponseType();
        ResponseType response = new ResponseType();
        ResultType result = new ResultType();
        result.setDecision(DecisionType.PERMIT);
        response.getResult().add(result);
        checkPolicyResponse.setResponse(response);
        return checkPolicyResponse;
    }
}
