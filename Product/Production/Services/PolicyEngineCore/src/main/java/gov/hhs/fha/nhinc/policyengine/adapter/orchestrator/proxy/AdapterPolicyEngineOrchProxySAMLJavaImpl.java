package gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.util.policyEngineUtil;

/**
 * @author achidambaram
 *
 */
public class AdapterPolicyEngineOrchProxySAMLJavaImpl implements AdapterPolicyEngineOrchProxy{

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.proxy.AdapterPolicyEngineOrchProxy#checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType)
     */
    @Override
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest, AssertionType assertion) {
        CheckPolicyResponseType oResponse = new CheckPolicyResponseType();
        policyEngineUtil util = new policyEngineUtil();
        oResponse = util.checkkAssertionAttributeStatement(assertion);
        return oResponse;
    }

}
