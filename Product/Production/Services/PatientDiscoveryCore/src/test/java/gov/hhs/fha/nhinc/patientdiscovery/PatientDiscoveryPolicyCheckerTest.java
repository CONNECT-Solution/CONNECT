/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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

import static org.junit.Assert.assertEquals;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.generic.GenericFactory;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author JHOPPESC
 */
@RunWith(JMock.class)
public class PatientDiscoveryPolicyCheckerTest {

    Mockery context = new JUnit4Mockery();
    final PolicyEngineProxy mockPolicyEngineProxy = context.mock(PolicyEngineProxy.class);

    /**
     * Test of check201305Policy method, of class PatientDiscoveryPolicyChecker.
     */
    @Test
    public void testCheck201305Policy() {
        System.out.println("testCheck201305Policy");
        PRPAIN201306UV02 message = new PRPAIN201306UV02();
        final AssertionType assertion = new AssertionType();

        GenericFactory<PolicyEngineProxy> policyEngineProxyFactory = new GenericFactory<PolicyEngineProxy>() {

            @Override
            public PolicyEngineProxy create() {
                return mockPolicyEngineProxy;
            }

        };
        final CheckPolicyResponseType response = new CheckPolicyResponseType();
        createPermitPolicy(response);

        context.checking(new Expectations() {
            {
                oneOf(mockPolicyEngineProxy).checkPolicy(with(any(CheckPolicyRequestType.class)),
                    with(any(AssertionType.class)));
                will(returnValue(response));
            }
        });

        PatientDiscovery201306PolicyChecker instance = new PatientDiscovery201306PolicyChecker(policyEngineProxyFactory);

        boolean result = instance.check201305Policy(message, assertion);

        assertEquals(true, result);
    }

    protected void createPermitPolicy(CheckPolicyResponseType response) {
        ResponseType value = new ResponseType();
        ResultType resultType = new ResultType();
        resultType.setDecision(DecisionType.PERMIT);
        value.getResult().add(resultType);
        response.setResponse(value);

    }

    protected void createDenyPolicy(CheckPolicyResponseType response) {
        ResponseType value = new ResponseType();
        ResultType resultType = new ResultType();
        resultType.setDecision(DecisionType.DENY);
        value.getResult().add(resultType);
        response.setResponse(value);
    }

    protected void creatIndeterminatePolicy(CheckPolicyResponseType response) {
        ResponseType value = new ResponseType();
        ResultType resultType = new ResultType();
        resultType.setDecision(DecisionType.INDETERMINATE);
        value.getResult().add(resultType);
        response.setResponse(value);
    }

    protected void creatNotApplicablePolicy(CheckPolicyResponseType response) {
        ResponseType value = new ResponseType();
        ResultType resultType = new ResultType();
        resultType.setDecision(DecisionType.NOT_APPLICABLE);
        value.getResult().add(resultType);
        response.setResponse(value);
    }

    /**
     * Test of check201305Policy method, of class PatientDiscoveryPolicyChecker.
     */
    @Test
    public void testCheck201305PolicyFails() {
        System.out.println("testCheck201305PolicyFails");
        PRPAIN201306UV02 message = new PRPAIN201306UV02();
        final AssertionType assertion = new AssertionType();

        GenericFactory<PolicyEngineProxy> policyEngineProxyFactory = new GenericFactory<PolicyEngineProxy>() {

            @Override
            public PolicyEngineProxy create() {
                return mockPolicyEngineProxy;
            }

        };
        final CheckPolicyResponseType response = new CheckPolicyResponseType();
        createDenyPolicy(response);

        context.checking(new Expectations() {
            {
                oneOf(mockPolicyEngineProxy).checkPolicy(with(any(CheckPolicyRequestType.class)),
                    with(any(AssertionType.class)));
                will(returnValue(response));
            }
        });

        PatientDiscovery201306PolicyChecker instance = new PatientDiscovery201306PolicyChecker(policyEngineProxyFactory);

        boolean result = instance.check201305Policy(message, assertion);

        assertEquals(false, result);
    }

    /**
     * Test of checkOutgoingPolicy method, of class PatientDiscoveryPolicyChecker.
     */
    @Test
    public void testCheckOutgoingPolicy() {
        System.out.println("testCheckOutgoingPolicy");
        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();

        GenericFactory<PolicyEngineProxy> policyEngineProxyFactory = new GenericFactory<PolicyEngineProxy>() {

            @Override
            public PolicyEngineProxy create() {
                return mockPolicyEngineProxy;
            }

        };
        final CheckPolicyResponseType response = new CheckPolicyResponseType();
        createPermitPolicy(response);

        context.checking(new Expectations() {
            {
                oneOf(mockPolicyEngineProxy).checkPolicy(with(aNull(CheckPolicyRequestType.class)),
                    with(aNull(AssertionType.class)));
                will(returnValue(response));
            }
        });

        PatientDiscoveryPolicyChecker instance = new PatientDiscoveryPolicyChecker(policyEngineProxyFactory);

        boolean result = instance.checkOutgoingPolicy(request);

        assertEquals(true, result);
    }

    /**
     * Test of checkOutgoingPolicy method, of class PatientDiscoveryPolicyChecker.
     */
    @Test
    public void testCheckOutgoingPolicyFails() {
        System.out.println("testCheckOutgoingPolicyFails");
        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();

        GenericFactory<PolicyEngineProxy> policyEngineProxyFactory = new GenericFactory<PolicyEngineProxy>() {

            @Override
            public PolicyEngineProxy create() {
                return mockPolicyEngineProxy;
            }

        };
        final CheckPolicyResponseType response = new CheckPolicyResponseType();
        createDenyPolicy(response);

        context.checking(new Expectations() {
            {
                oneOf(mockPolicyEngineProxy).checkPolicy(with(aNull(CheckPolicyRequestType.class)),
                    with(aNull(AssertionType.class)));
                will(returnValue(response));
            }
        });

        PatientDiscoveryPolicyChecker instance = new PatientDiscoveryPolicyChecker(policyEngineProxyFactory);

        boolean result = instance.checkOutgoingPolicy(request);

        assertEquals(false, result);
    }

}
