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
package gov.hhs.fha.nhinc.callback;

import static org.junit.Assert.assertTrue;

import gov.hhs.fha.nhinc.callback.opensaml.CallbackMapProperties;
import gov.hhs.fha.nhinc.callback.opensaml.CallbackProperties;
import gov.hhs.fha.nhinc.callback.purposeuse.PurposeUseProxy;
import gov.hhs.fha.nhinc.connectmgr.NhinEndpointManager;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import java.util.Map;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * @author mweaver
 *
 */
public abstract class AbstractPurposeOfForDeciderMockBaseTest {

    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    protected final NhinEndpointManager mockNhinEndpointManager = context.mock(NhinEndpointManager.class);
    protected final PurposeUseProxy mockPurposeUseProxy = context.mock(PurposeUseProxy.class);

    protected final PurposeOfForDecider mockPurposeOfForDecider = createMockPurposeOfForDecider();

    /**
     * @return Overridden MockPurposeOfForDecider
     */
    protected PurposeOfForDecider createMockPurposeOfForDecider() {
        return new PurposeOfForDecider() {

            @Override
            protected NhinEndpointManager getNhinEndpointManager() {
                return mockNhinEndpointManager;
            }

            @Override
            protected PurposeUseProxy getPurposeUseProxyObjectFactory() {
                return mockPurposeUseProxy;
            }

        };
    }

    /*-----------------Test Methods---------------*/

    /**
     * Mock decider returns false, meaning we'll send PurposeOf
     */
    @Test
    public void testIsPurposeOfg0() {
        expectNoMockEndpointLookups();
        expectMockPurposeUseProxyReturnPurposeFor(false);

        CallbackProperties properties = new CallbackMapProperties(createTokenValuesg0());
        assertTrue(!mockPurposeOfForDecider.isPurposeFor(properties));
        context.assertIsSatisfied();
    }

    /**
     * Mock decider returns true, meaning we'll send PurposeFor
     */
    @Test
    public void testIsPurposeForg0() {
        expectNoMockEndpointLookups();
        expectMockPurposeUseProxyReturnPurposeFor(true);

        CallbackProperties properties = new CallbackMapProperties(createTokenValuesg0());
        assertTrue(mockPurposeOfForDecider.isPurposeFor(properties));
        context.assertIsSatisfied();
    }

    /**
     * g1 (2011 specs), meaning we'll always send PurposeOf
     */
    @Test
    public void testIsPurposeOfg1() {
        expectNoMockEndpointLookups();
        expectNoMockPurposeUseProxy();

        CallbackProperties properties = new CallbackMapProperties(createTokenValuesg1());
        assertTrue(!mockPurposeOfForDecider.isPurposeFor(properties));
        context.assertIsSatisfied();
    }

    @Test
    public void testIsPurposeOfg0APILookup() {
        expectMockEndpointLookup(GATEWAY_API_LEVEL.LEVEL_g0);
        expectMockPurposeUseProxyReturnPurposeFor(false);

        CallbackProperties properties = new CallbackMapProperties(createTokenValues());
        assertTrue(!mockPurposeOfForDecider.isPurposeFor(properties));
        context.assertIsSatisfied();
    }

    @Test
    public void testIsPurposeForg0APILookup() {
        expectMockEndpointLookup(GATEWAY_API_LEVEL.LEVEL_g0);
        expectMockPurposeUseProxyReturnPurposeFor(true);

        CallbackProperties properties = new CallbackMapProperties(createTokenValues());
        assertTrue(mockPurposeOfForDecider.isPurposeFor(properties));
        context.assertIsSatisfied();
    }

    @Test
    public void testIsPurposeOfg1APILookup() {
        expectMockEndpointLookup(GATEWAY_API_LEVEL.LEVEL_g1);
        expectNoMockPurposeUseProxy();

        CallbackProperties properties = new CallbackMapProperties(createTokenValues());
        assertTrue(!mockPurposeOfForDecider.isPurposeFor(properties));
        context.assertIsSatisfied();
    }

    /*-----------------Setup Methods---------------*/

    protected abstract Map<String, Object> createTokenValues();

    protected Map<String, Object> createTokenValuesg0() {
        Map<String, Object> tokenVals = createTokenValues();
        tokenVals.put(SamlConstants.TARGET_API_LEVEL, NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0);
        return tokenVals;
    }

    protected Map<String, Object> createTokenValuesg1() {
        Map<String, Object> tokenVals = createTokenValues();
        tokenVals.put(SamlConstants.TARGET_API_LEVEL, NhincConstants.GATEWAY_API_LEVEL.LEVEL_g1);
        return tokenVals;
    }

    /*-----------------Expectation Methods---------------*/

    /**
     * Expectations for expectMockEndpointLookups().
     */
    protected void expectNoMockEndpointLookups() {
        context.checking(new Expectations() {
            {
                exactly(0).of(mockNhinEndpointManager).getApiVersion(with(any(String.class)),
                    with(any(NhincConstants.NHIN_SERVICE_NAMES.class)));
            }
        });
    }

    /**
     * Expectations for expectMockEndpointLookups().
     */
    protected void expectMockEndpointLookup(final GATEWAY_API_LEVEL level) {
        context.checking(new Expectations() {
            {
                exactly(1).of(mockNhinEndpointManager).getApiVersion(with(any(String.class)),
                    with(any(NhincConstants.NHIN_SERVICE_NAMES.class)));
                will(returnValue(level));
            }
        });
    }

    /**
     * Expectations for expectMockEndpointLookups().
     */
    protected void expectNoMockPurposeUseProxy() {
        context.checking(new Expectations() {
            {
                exactly(0).of(mockPurposeUseProxy).isPurposeForUseEnabled(with(any(CallbackProperties.class)));
            }
        });
    }

    /**
     * Expectations for expectMockEndpointLookups().
     */
    protected void expectMockPurposeUseProxyReturnPurposeFor(final boolean isPurposeFor) {
        context.checking(new Expectations() {
            {
                exactly(1).of(mockPurposeUseProxy).isPurposeForUseEnabled(with(any(CallbackProperties.class)));
                will(returnValue(isPurposeFor));
            }
        });
    }

}
