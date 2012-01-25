/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.eventcommon.PatDiscReqEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.GenericFactory;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;

import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;

import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

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
        II patIdOverride = new II();
        final AssertionType assertion = new AssertionType();
        
        
        GenericFactory<PolicyEngineProxy> policyEngineProxyFactory = new GenericFactory<PolicyEngineProxy>() {

			@Override
			public PolicyEngineProxy create() {
				return mockPolicyEngineProxy;
			}
        	
        };
        final  CheckPolicyResponseType response = new CheckPolicyResponseType();
		createPermitPolicy(response);
        
        context.checking(new Expectations() {{
            oneOf(mockPolicyEngineProxy).checkPolicy(with(any(CheckPolicyRequestType.class)), with(any(AssertionType.class)));
            will(returnValue(response));
        }});
        
        PatientDiscovery201306PolicyChecker instance = new PatientDiscovery201306PolicyChecker(policyEngineProxyFactory);

        boolean result = instance.check201305Policy(message, patIdOverride, assertion);
        
        
        

        assertEquals(true, result);
    }

	protected void  createPermitPolicy(CheckPolicyResponseType response) {
		    ResponseType value = new ResponseType();
		    ResultType resultType = new ResultType();
		    resultType.setDecision(DecisionType.PERMIT);
			value.getResult().add(resultType);
			response.setResponse(value);
		
	}
	
	protected void  createDenyPolicy(CheckPolicyResponseType response) {
	    ResponseType value = new ResponseType();
	    ResultType resultType = new ResultType();
	    resultType.setDecision(DecisionType.DENY);
		value.getResult().add(resultType);
		response.setResponse(value);
	}
	
	protected void  creatIndeterminatePolicy(CheckPolicyResponseType response) {
	    ResponseType value = new ResponseType();
	    ResultType resultType = new ResultType();
	    resultType.setDecision(DecisionType.INDETERMINATE);
		value.getResult().add(resultType);
		response.setResponse(value);
	}

	protected void  creatNotApplicablePolicy(CheckPolicyResponseType response) {
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
        II patIdOverride = new II();
        final AssertionType assertion = new AssertionType();
        
        
        GenericFactory<PolicyEngineProxy> policyEngineProxyFactory = new GenericFactory<PolicyEngineProxy>() {

			@Override
			public PolicyEngineProxy create() {
				return mockPolicyEngineProxy;
			}
        	
        };
        final  CheckPolicyResponseType response = new CheckPolicyResponseType();
		createDenyPolicy(response);
        
        context.checking(new Expectations() {{
            oneOf(mockPolicyEngineProxy).checkPolicy(with(any(CheckPolicyRequestType.class)), with(any(AssertionType.class)));
            will(returnValue(response));
        }});
        
        PatientDiscovery201306PolicyChecker instance = new PatientDiscovery201306PolicyChecker(policyEngineProxyFactory);


        boolean result = instance.check201305Policy(message, patIdOverride, assertion);

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
        final  CheckPolicyResponseType response = new CheckPolicyResponseType();
		createPermitPolicy(response);
        
        context.checking(new Expectations() {{
            oneOf(mockPolicyEngineProxy).checkPolicy(with(any(CheckPolicyRequestType.class)), with(any(AssertionType.class)));
            will(returnValue(response));
        }});
        
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
        final  CheckPolicyResponseType response = new CheckPolicyResponseType();
		createDenyPolicy(response);
        
        context.checking(new Expectations() {{
            oneOf(mockPolicyEngineProxy).checkPolicy(with(any(CheckPolicyRequestType.class)), with(any(AssertionType.class)));
            will(returnValue(response));
        }});
        
        PatientDiscoveryPolicyChecker instance = new PatientDiscoveryPolicyChecker(policyEngineProxyFactory);


        boolean result = instance.checkOutgoingPolicy(request);

        assertEquals(false, result);
    }

}