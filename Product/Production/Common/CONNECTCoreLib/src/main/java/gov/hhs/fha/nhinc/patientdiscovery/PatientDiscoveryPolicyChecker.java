/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.GenericFactory;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.policy.PatientDiscoveryPolicyTransformHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 *
 * @author jhoppesc
 */
public class PatientDiscoveryPolicyChecker extends AbstractPatientDiscoveryPolicyChecker<RespondingGatewayPRPAIN201305UV02RequestType, PRPAIN201305UV02> {

    static Log log = LogFactory.getLog(PatientDiscoveryPolicyChecker.class);
    
    private static PatientDiscoveryPolicyChecker INSTANCE = new PatientDiscoveryPolicyChecker(new PolicyEngineProxyObjectFactory());
    
    PatientDiscoveryPolicyChecker(GenericFactory<PolicyEngineProxy> policyEngFactory) {
    	super(policyEngFactory);
    }
    
    public static PatientDiscoveryPolicyChecker getInstance() {
    	return INSTANCE;
    }

  
    @Override
	public boolean checkIncomingPolicy(PRPAIN201305UV02 request, AssertionType assertion) {
        PatientDiscoveryPolicyTransformHelper transformer = new PatientDiscoveryPolicyTransformHelper();

        CheckPolicyRequestType checkPolicyRequest = transformer.transformPRPAIN201305UV02ToCheckPolicy(request, assertion);
        return invokePolicyEngine(checkPolicyRequest);
    }
    
    @Override
	public boolean checkOutgoingPolicy (RespondingGatewayPRPAIN201305UV02RequestType request) {
        log.debug("checking the policy engine for the new request to a target community");

        PatientDiscoveryPolicyTransformHelper oPatientDiscoveryPolicyTransformHelper = new PatientDiscoveryPolicyTransformHelper();
        CheckPolicyRequestType checkPolicyRequest = oPatientDiscoveryPolicyTransformHelper.transformPatientDiscoveryEntityToCheckPolicy(request);

        return invokePolicyEngine(checkPolicyRequest);
    }
}
