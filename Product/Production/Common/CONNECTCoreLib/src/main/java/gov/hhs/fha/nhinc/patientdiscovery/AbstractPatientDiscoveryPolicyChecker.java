package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.eventcommon.PatDiscReqEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.GenericFactory;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractPatientDiscoveryPolicyChecker<OUTGOING,INCOMMING> implements PolicyChecker<OUTGOING,INCOMMING>  {

	static Log log = LogFactory.getLog(AbstractPatientDiscoveryPolicyChecker.class);
	   
	
	private GenericFactory<PolicyEngineProxy> policyEngFactory;

	protected AbstractPatientDiscoveryPolicyChecker(GenericFactory<PolicyEngineProxy> policyEngFactory) {
		this.policyEngFactory = policyEngFactory;
	}

	protected boolean invokePolicyEngine(PatDiscReqEventType policyCheckReq) {
	    boolean policyIsValid = false;
	
	    PolicyEngineChecker policyChecker = new PolicyEngineChecker();
	    CheckPolicyRequestType policyReq = policyChecker.checkPolicyPatDiscRequest(policyCheckReq);
	    PolicyEngineProxy policyProxy = policyEngFactory.create();
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
	    PolicyEngineProxy policyProxy = policyEngFactory.create();
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