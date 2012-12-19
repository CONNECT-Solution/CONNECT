/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.generic.GenericFactory;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.policy.PatientDiscoveryPolicyTransformHelper;

import org.apache.log4j.Logger;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 * 
 * @author jhoppesc
 */
public class PatientDiscoveryPolicyChecker extends
        AbstractPatientDiscoveryPolicyChecker<RespondingGatewayPRPAIN201305UV02RequestType, PRPAIN201305UV02> {

    private static final Logger LOG = Logger.getLogger(PatientDiscoveryPolicyChecker.class);

    private static PatientDiscoveryPolicyChecker INSTANCE = new PatientDiscoveryPolicyChecker(
            new PolicyEngineProxyObjectFactory());

    PatientDiscoveryPolicyChecker(GenericFactory<PolicyEngineProxy> policyEngFactory) {
        super(policyEngFactory);
    }

    public static PatientDiscoveryPolicyChecker getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean checkIncomingPolicy(PRPAIN201305UV02 request, AssertionType assertion) {
        PatientDiscoveryPolicyTransformHelper transformer = new PatientDiscoveryPolicyTransformHelper();

        CheckPolicyRequestType checkPolicyRequest = transformer.transformPRPAIN201305UV02ToCheckPolicy(request,
                assertion);
        return invokePolicyEngine(checkPolicyRequest);
    }

    @Override
    public boolean checkOutgoingPolicy(RespondingGatewayPRPAIN201305UV02RequestType request) {
        LOG.debug("checking the policy engine for the new request to a target community");

        PatientDiscoveryPolicyTransformHelper oPatientDiscoveryPolicyTransformHelper = new PatientDiscoveryPolicyTransformHelper();
        CheckPolicyRequestType checkPolicyRequest = oPatientDiscoveryPolicyTransformHelper
                .transformPatientDiscoveryEntityToCheckPolicy(request);

        return invokePolicyEngine(checkPolicyRequest);
    }
}
