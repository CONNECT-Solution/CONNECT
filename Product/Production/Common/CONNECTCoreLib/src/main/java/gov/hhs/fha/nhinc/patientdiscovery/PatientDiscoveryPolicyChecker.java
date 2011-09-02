/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
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
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.policy.PatientDiscoveryPolicyTransformHelper;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

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

    public boolean checkIncomingPolicy(PRPAIN201305UV02 request, AssertionType assertion) {
        PatientDiscoveryPolicyTransformHelper transformer = new PatientDiscoveryPolicyTransformHelper();

        CheckPolicyRequestType checkPolicyRequest = transformer.transformPRPAIN201305UV02ToCheckPolicy(request, assertion);
        return invokePolicyEngine(checkPolicyRequest);
    }

    public boolean checkOutgoingPolicy (RespondingGatewayPRPAIN201305UV02RequestType request) {
        log.debug("checking the policy engine for the new request to a target community");

        PatientDiscoveryPolicyTransformHelper oPatientDiscoveryPolicyTransformHelper = new PatientDiscoveryPolicyTransformHelper();
        CheckPolicyRequestType checkPolicyRequest = oPatientDiscoveryPolicyTransformHelper.transformPatientDiscoveryEntityToCheckPolicy(request);

        return invokePolicyEngine(checkPolicyRequest);
    }

    protected boolean invokePolicyEngine(PatDiscReqEventType policyCheckReq) {
        boolean policyIsValid = false;

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyPatDiscRequest(policyCheckReq);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        AssertionType assertion = null;
        if(policyReq != null)
        {
            assertion = policyReq.getAssertion();
        }
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        return policyIsValid;
    }

    protected boolean invokePolicyEngine(CheckPolicyRequestType policyCheckReq) {
        boolean policyIsValid = false;

         /* invoke check policy */
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        AssertionType assertion = null;
        if(policyCheckReq != null)
        {
            assertion = policyCheckReq.getAssertion();
        }
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyCheckReq, assertion);

        /* if response='permit' */
        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            log.debug("Policy engine check returned permit.");
            policyIsValid = true;
        } else {
            log.debug("Policy engine check returned deny.");
            policyIsValid = false;
        }

        return policyIsValid;
    }
}
