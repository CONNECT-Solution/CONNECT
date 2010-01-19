/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.eventcommon.PatDiscReqEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxyObjectFactory;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author jhoppesc
 */
public class PatientDiscoveryPolicyChecker {

    private static Log log = LogFactory.getLog(PatientDiscoveryPolicyChecker.class);

    public boolean check201305Policy(PRPAIN201306UV02 message, II patIdOverride, AssertionType assertion) {
        String roid = null;
        String soid = null;

        if (message != null &&
                NullChecker.isNotNullish(message.getReceiver()) &&
                message.getReceiver().get(0) != null &&
                message.getReceiver().get(0).getDevice() != null &&
                message.getReceiver().get(0).getDevice().getAsAgent() != null &&
                message.getReceiver().get(0).getDevice().getAsAgent().getValue() != null &&
                message.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                message.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(message.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                message.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(message.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            roid = message.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        }

        if (message != null &&
                message.getSender() != null &&
                message.getSender().getDevice() != null &&
                message.getSender().getDevice().getAsAgent() != null &&
                message.getSender().getDevice().getAsAgent().getValue() != null &&
                message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            soid = message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        }

        PatDiscReqEventType policyCheckReq = new PatDiscReqEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);

        policyCheckReq.setPRPAIN201306UV02(message);
        policyCheckReq.setAssertion(assertion);
        HomeCommunityType senderHC = new HomeCommunityType();
        senderHC.setHomeCommunityId(soid);
        policyCheckReq.setSendingHomeCommunity(senderHC);
        HomeCommunityType receiverHC = new HomeCommunityType();
        receiverHC.setHomeCommunityId(roid);
        policyCheckReq.setReceivingHomeCommunity(receiverHC);

        return invokePolicyEngine(policyCheckReq);
    }

    protected boolean invokePolicyEngine(PatDiscReqEventType policyCheckReq) {
        boolean policyIsValid = false;

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyPatDiscRequest(policyCheckReq);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq);

        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        return policyIsValid;
    }
}
