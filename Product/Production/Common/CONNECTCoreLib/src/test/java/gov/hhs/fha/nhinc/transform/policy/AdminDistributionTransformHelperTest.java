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
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import oasis.names.tc.xacml._2_0.policy.schema.os.SubjectType;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

/**
 * 
 * @author dunnek
 */
public class AdminDistributionTransformHelperTest {

    private Mockery context;

    public AdminDistributionTransformHelperTest() {
    }

    @Before
    public void setUp() {
        context = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @Test
    public void testTransformEntityAlertToCheckPolicy() {
        final Log mockLogger = context.mock(Log.class);
        AdminDistributionTransformHelper instance = new AdminDistributionTransformHelper() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                // Input Request Object can not be null
                exactly(1).of(mockLogger).error("Request is null.");
                will(returnValue(with(any(CheckPolicyRequestType.class))));
            }
        });

        instance.transformEntityAlertToCheckPolicy(null, null);
        context.assertIsSatisfied();
    }

    @Test
    public void testTransformEntityAlertToCheckPolicy_NoTarget() {
        final Log mockLogger = context.mock(Log.class);
        AdminDistributionTransformHelper instance = new AdminDistributionTransformHelper() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected oasis.names.tc.xacml._2_0.context.schema.os.SubjectType createSubject(HomeCommunityType hc,
                    AssertionType assertion) {
                return new oasis.names.tc.xacml._2_0.context.schema.os.SubjectType();
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                exactly(1).of(mockLogger).error(with(any(String.class)));
                will(returnValue(with(any(CheckPolicyRequestType.class))));
            }
        });

        RespondingGatewaySendAlertMessageType message = new RespondingGatewaySendAlertMessageType();
        message.setAssertion(new AssertionType());
        message.setEDXLDistribution(new EDXLDistribution());

        String target = "121";

        CheckPolicyRequestType result = instance.transformEntityAlertToCheckPolicy(message, "");
        context.assertIsSatisfied();
        assertNotNull(result);
    }

    @Test
    public void testTransformEntityAlertToCheckPolicy_NoBody() {
        final Log mockLogger = context.mock(Log.class);
        AdminDistributionTransformHelper instance = new AdminDistributionTransformHelper() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected oasis.names.tc.xacml._2_0.context.schema.os.SubjectType createSubject(HomeCommunityType hc,
                    AssertionType assertion) {
                return new oasis.names.tc.xacml._2_0.context.schema.os.SubjectType();
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                exactly(1).of(mockLogger).error(with(any(String.class)));
                will(returnValue(with(any(CheckPolicyRequestType.class))));
            }
        });

        RespondingGatewaySendAlertMessageType message = new RespondingGatewaySendAlertMessageType();
        message.setAssertion(new AssertionType());
        // message.setEDXLDistribution(new EDXLDistribution());

        String target = "121";

        CheckPolicyRequestType result = instance.transformEntityAlertToCheckPolicy(message, target);
        context.assertIsSatisfied();
        assertNotNull(result);
    }

    @Test
    public void testTransformEntityAlertToCheckPolicy_NoAssertion() {
        final Log mockLogger = context.mock(Log.class);
        AdminDistributionTransformHelper instance = new AdminDistributionTransformHelper() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected oasis.names.tc.xacml._2_0.context.schema.os.SubjectType createSubject(HomeCommunityType hc,
                    AssertionType assertion) {
                return new oasis.names.tc.xacml._2_0.context.schema.os.SubjectType();
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                exactly(1).of(mockLogger).error(with(any(String.class)));
                will(returnValue(with(any(CheckPolicyRequestType.class))));
            }
        });

        RespondingGatewaySendAlertMessageType message = new RespondingGatewaySendAlertMessageType();
        // message.setAssertion(new AssertionType());
        message.setEDXLDistribution(new EDXLDistribution());

        String target = "121";

        CheckPolicyRequestType result = instance.transformEntityAlertToCheckPolicy(message, target);
        context.assertIsSatisfied();
        assertNotNull(result);
    }

    @Test
    public void testTransformEntityAlertToCheckPolicy_Valid() {
        final Log mockLogger = context.mock(Log.class);
        AdminDistributionTransformHelper instance = new AdminDistributionTransformHelper() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected oasis.names.tc.xacml._2_0.context.schema.os.SubjectType createSubject(HomeCommunityType hc,
                    AssertionType assertion) {
                return new oasis.names.tc.xacml._2_0.context.schema.os.SubjectType();
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                will(returnValue(with(any(CheckPolicyRequestType.class))));
            }
        });

        RespondingGatewaySendAlertMessageType message = new RespondingGatewaySendAlertMessageType();
        HomeCommunityType hc = new HomeCommunityType();
        hc.setHomeCommunityId("1.1");

        AssertionType assertion = new AssertionType();
        assertion.setHomeCommunity(hc);

        message.setAssertion(assertion);
        message.setEDXLDistribution(new EDXLDistribution());

        String target = "121";

        instance.transformEntityAlertToCheckPolicy(message, target);
        context.assertIsSatisfied();
    }

    @Test
    public void transformNhinAlertToCheckPolicy_Valid() {
        final Log mockLogger = context.mock(Log.class);
        final AssertionType assertion = new AssertionType();

        AdminDistributionTransformHelper instance = new AdminDistributionTransformHelper() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected oasis.names.tc.xacml._2_0.context.schema.os.SubjectType createSubject(HomeCommunityType hc,
                    AssertionType assertion) {
                return new oasis.names.tc.xacml._2_0.context.schema.os.SubjectType();
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                will(returnValue(with(any(CheckPolicyRequestType.class))));
            }
        });

        RespondingGatewaySendAlertMessageType message = new RespondingGatewaySendAlertMessageType();
        message.setAssertion(new AssertionType());
        message.setEDXLDistribution(new EDXLDistribution());

        String target = "121";

        CheckPolicyRequestType result = instance.transformNhinAlertToCheckPolicy(new EDXLDistribution(), assertion);
        context.assertIsSatisfied();

        assertNotNull(result);
        assertNotNull(result.getAssertion());
        assertNotNull(result.getRequest());
        assertNotNull(result.getRequest().getAction());
        assertEquals(assertion, result.getAssertion());

        assertEquals(1, result.getRequest().getAction().getAttribute().size());
    }

    @Test
    public void transformNhinAlertToCheckPolicy_NoBody() {
        final Log mockLogger = context.mock(Log.class);
        final AssertionType assertion = new AssertionType();
        AdminDistributionTransformHelper instance = new AdminDistributionTransformHelper() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected oasis.names.tc.xacml._2_0.context.schema.os.SubjectType createSubject(HomeCommunityType hc,
                    AssertionType assertion) {
                return new oasis.names.tc.xacml._2_0.context.schema.os.SubjectType();
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                exactly(1).of(mockLogger).error(with(any(String.class)));
                will(returnValue(with(any(CheckPolicyRequestType.class))));
            }
        });

        String target = "121";

        CheckPolicyRequestType result = instance.transformNhinAlertToCheckPolicy(null, assertion);

        context.assertIsSatisfied();
        assertNotNull(result);
        assertNull(result.getRequest());

    }

    @Test
    public void transformNhinAlertToCheckPolicy_NoAssertion() {
        final Log mockLogger = context.mock(Log.class);
        final AssertionType assertion = new AssertionType();
        AdminDistributionTransformHelper instance = new AdminDistributionTransformHelper() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected oasis.names.tc.xacml._2_0.context.schema.os.SubjectType createSubject(HomeCommunityType hc,
                    AssertionType assertion) {
                return new oasis.names.tc.xacml._2_0.context.schema.os.SubjectType();
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                exactly(1).of(mockLogger).error(with(any(String.class)));
                will(returnValue(with(any(CheckPolicyRequestType.class))));
            }
        });

        String target = "121";

        CheckPolicyRequestType result = instance.transformNhinAlertToCheckPolicy(new EDXLDistribution(), null);

        context.assertIsSatisfied();
        assertNotNull(result);
        assertNull(result.getRequest());

    }

}