/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery;

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxyObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;

/**
 *
 * @author JHOPPESC
 */
public class DocQueryPolicyChecker {
    /**
     * Checks to see if the security policy will permit the query to be executed.
     * @param message The AdhocQuery request message.
     * @return Returns true if the security policy permits the query; false if denied.
     */
    public boolean checkIncomingPolicy(AdhocQueryRequest message, AssertionType assertion) {
        boolean policyIsValid = false;

        //convert the request message to an object recognized by the policy engine
        AdhocQueryRequestEventType policyCheckReq = new AdhocQueryRequestEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestMessageType request = new gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestMessageType();
        request.setAssertion(assertion);
        request.setAdhocQueryRequest(message);
        policyCheckReq.setMessage(request);

        //call the policy engine to check the permission on the request
        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyAdhocQuery(policyCheckReq);
        policyReq.setAssertion(assertion);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq);

        //check the policy engine's response, return true if response = permit
        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT)
        {
            policyIsValid = true;
        }

        return policyIsValid;
    }

}
