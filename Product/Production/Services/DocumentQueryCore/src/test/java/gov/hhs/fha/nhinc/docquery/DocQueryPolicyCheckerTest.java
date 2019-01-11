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
package gov.hhs.fha.nhinc.docquery;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.policyengine.DocumentRetrievePolicyEngineChecker;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author goldmanm
 */
public class DocQueryPolicyCheckerTest {

    Mockery mockery;

    @Before
    public void setup() {
        mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    /**
     * Test of checkPolicy method, of class DocQueryPolicyChecker.
     */
    @Test
    public void testValidatePolicyResponse_null_PolicyResponse_returns_false() {
        final CheckPolicyResponseType mockResponse = mockery.mock(CheckPolicyResponseType.class);
        DocQueryPolicyChecker testSubject = new DocQueryPolicyChecker();

        mockery.checking(new Expectations() {

            {
                one(mockResponse).getResponse();
                will(returnValue(null));
            }
        });

        assertFalse(testSubject.validatePolicyResponse(mockResponse));

        mockery.assertIsSatisfied();
    }

    /**
     * Test of checkPolicy method, of class DocQueryPolicyChecker.
     */
    @Test
    public void testValidatePolicyResponse_null_Result_returns_false() {
        final CheckPolicyResponseType mockPolicyResponse = mockery.mock(CheckPolicyResponseType.class);
        final ResponseType mockResponse = mockery.mock(ResponseType.class);
        DocQueryPolicyChecker testSubject = new DocQueryPolicyChecker();

        mockery.checking(new Expectations() {

            {
                atLeast(1).of(mockPolicyResponse).getResponse();
                will(returnValue(mockResponse));
                atLeast(1).of(mockResponse).getResult();
                will(returnValue(null));
            }
        });

        assertFalse(testSubject.validatePolicyResponse(mockPolicyResponse));

        mockery.assertIsSatisfied();
    }

    /**
     * Test of checkPolicy method, of class DocQueryPolicyChecker.
     */
    @Test
    public void testValidatePolicyResponse_DecisionType_not_PERMIT_returns_false() {
        final CheckPolicyResponseType mockPolicyResponse = mockery.mock(CheckPolicyResponseType.class);
        final ResponseType mockResponse = mockery.mock(ResponseType.class);
        final ResultType mockResult = mockery.mock(ResultType.class);
        final List<ResultType> results = new ArrayList<>();
        results.add(mockResult);

        DocQueryPolicyChecker testSubject = new DocQueryPolicyChecker();

        mockery.checking(new Expectations() {

            {
                allowing(mockPolicyResponse).getResponse();
                will(returnValue(mockResponse));
                allowing(mockResponse).getResult();
                will(returnValue(results));
                one(mockResult).getDecision();
            }
        });

        assertFalse(testSubject.validatePolicyResponse(mockPolicyResponse));

        mockery.assertIsSatisfied();
    }

    /**
     * Test of checkPolicy method, of class DocQueryPolicyChecker.
     */
    @Test
    public void testValidatePolicyResponse_DecisionType_PERMIT_returns_true() {
        final CheckPolicyResponseType mockPolicyResponse = mockery.mock(CheckPolicyResponseType.class);
        final ResponseType mockResponse = mockery.mock(ResponseType.class);
        final ResultType mockResult = mockery.mock(ResultType.class);
        final List<ResultType> results = new ArrayList<>();
        results.add(mockResult);

        DocQueryPolicyChecker testSubject = new DocQueryPolicyChecker();

        mockery.checking(new Expectations() {

            {
                allowing(mockPolicyResponse).getResponse();
                will(returnValue(mockResponse));
                allowing(mockResponse).getResult();
                will(returnValue(results));
                one(mockResult).getDecision();
                will(returnValue(DecisionType.PERMIT));
            }
        });

        assertTrue(testSubject.validatePolicyResponse(mockPolicyResponse));

        mockery.assertIsSatisfied();
    }

    /**
     * Test of getPolicyChecker method, of class DocQueryPolicyChecker.
     */
    @Test
    public void testGetPolicyChecker() {
        DocQueryPolicyChecker testSubject = new DocQueryPolicyChecker();
        DocumentRetrievePolicyEngineChecker result = testSubject.getPolicyChecker();
        assertNotNull(result);
    }
}
