package gov.hhs.fha.nhinc.policyengine.adapterpolicyengineorchestrator.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;


/**
 * This is a concrete implementation of the AdapterPolicyEngineOrchestrator interface
 * that always returns a permit.
 *
 * @author Les Westberg
 */
public class AdapterPolicyEngineOrchestratorPermitNoOpImpl implements AdapterPolicyEngineOrchestratorProxy
{
    /**
     * Given a request to check the access policy, this service will always
     * return a permit response.
     *
     * @param checkPolicyRequest The request to check defined policy
     * @return The response which contains the access decision
     */
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest)
    {
        CheckPolicyResponseType oPolicyResponse = new CheckPolicyResponseType();
        ResponseType oResponse = new ResponseType();
        ResultType oResult = new ResultType();
        oResult.setDecision(DecisionType.PERMIT);
        oResponse.getResult().add(oResult);
        oPolicyResponse.setResponse(oResponse);
        return oPolicyResponse;
    }
}
