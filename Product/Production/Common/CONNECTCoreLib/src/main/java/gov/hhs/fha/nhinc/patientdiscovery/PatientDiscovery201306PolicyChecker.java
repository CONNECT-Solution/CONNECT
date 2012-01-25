package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.eventcommon.PatDiscReqEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.GenericFactory;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

public class PatientDiscovery201306PolicyChecker
		extends
		AbstractPatientDiscoveryPolicyChecker<RespondingGatewayPRPAIN201306UV02RequestType, PRPAIN201306UV02> {

	static Log log = LogFactory.getLog(PatientDiscoveryPolicyChecker.class);

	private static PatientDiscovery201306PolicyChecker INSTANCE = new PatientDiscovery201306PolicyChecker(
			new PolicyEngineProxyObjectFactory());

	PatientDiscovery201306PolicyChecker(
			GenericFactory<PolicyEngineProxy> policyEngFactory) {
		super(policyEngFactory);
	}

	public static PatientDiscovery201306PolicyChecker getInstance() {
		return INSTANCE;
	}

	
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
	
	
	
	@Override
	public boolean checkOutgoingPolicy(
			RespondingGatewayPRPAIN201306UV02RequestType request) {
		PRPAIN201306UV02 message = request.getPRPAIN201306UV02();
		AssertionType assertion = request.getAssertion();
		String roid = null;
		String soid = null;

		if (message != null
				&& NullChecker.isNotNullish(message.getReceiver())
				&& message.getReceiver().get(0) != null
				&& message.getReceiver().get(0).getDevice() != null
				&& message.getReceiver().get(0).getDevice().getAsAgent() != null
				&& message.getReceiver().get(0).getDevice().getAsAgent()
						.getValue() != null
				&& message.getReceiver().get(0).getDevice().getAsAgent()
						.getValue().getRepresentedOrganization() != null
				&& message.getReceiver().get(0).getDevice().getAsAgent()
						.getValue().getRepresentedOrganization().getValue() != null
				&& NullChecker.isNotNullish(message.getReceiver().get(0)
						.getDevice().getAsAgent().getValue()
						.getRepresentedOrganization().getValue().getId())
				&& message.getReceiver().get(0).getDevice().getAsAgent()
						.getValue().getRepresentedOrganization().getValue()
						.getId().get(0) != null
				&& NullChecker.isNotNullish(message.getReceiver().get(0)
						.getDevice().getAsAgent().getValue()
						.getRepresentedOrganization().getValue().getId().get(0)
						.getRoot())) {
			roid = message.getReceiver().get(0).getDevice().getAsAgent()
					.getValue().getRepresentedOrganization().getValue().getId()
					.get(0).getRoot();
		}

		if (message != null
				&& message.getSender() != null
				&& message.getSender().getDevice() != null
				&& message.getSender().getDevice().getAsAgent() != null
				&& message.getSender().getDevice().getAsAgent().getValue() != null
				&& message.getSender().getDevice().getAsAgent().getValue()
						.getRepresentedOrganization() != null
				&& message.getSender().getDevice().getAsAgent().getValue()
						.getRepresentedOrganization().getValue() != null
				&& NullChecker.isNotNullish(message.getSender().getDevice()
						.getAsAgent().getValue().getRepresentedOrganization()
						.getValue().getId())
				&& message.getSender().getDevice().getAsAgent().getValue()
						.getRepresentedOrganization().getValue().getId().get(0) != null
				&& NullChecker.isNotNullish(message.getSender().getDevice()
						.getAsAgent().getValue().getRepresentedOrganization()
						.getValue().getId().get(0).getRoot())) {
			soid = message.getSender().getDevice().getAsAgent().getValue()
					.getRepresentedOrganization().getValue().getId().get(0)
					.getRoot();
		}

		PatDiscReqEventType policyCheckReq = new PatDiscReqEventType();
		policyCheckReq
				.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);

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

	@Override
	public boolean checkIncomingPolicy(PRPAIN201306UV02 request,
			AssertionType assertion) {
		// TODO Auto-generated method stub
		return false;
	}

}
