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
package gov.hhs.fha.nhinc.connectmgr.nhinendpointmanager;

import gov.hhs.fha.nhinc.connectmgr.NhinEndpointManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.NHIN_SERVICE_NAMES;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import java.util.ArrayList;
import java.util.List;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author mweaver
 *
 */
public abstract class AbstractNhinEndpointManagerMockTest {

    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    protected final ExchangeManager mockExchangeManager = context.mock(ExchangeManager.class);
    protected final NhinEndpointManager mockNhinEndpointManager = createMockNhinEndpointManager();

    protected final String HCID = "1.1";

    /**
     * @return Overridden MockPurposeOfForDecider
     */
    protected NhinEndpointManager createMockNhinEndpointManager() {
        return new NhinEndpointManager() {

            @Override
            protected ExchangeManager getExchangeManager() {
                return mockExchangeManager;
            }
        };
    }

    /*
     * -----------------Test Methods---------------
     */
    /**
     * Test target only supporting 2010
     */
    @Test
    public void testTargetIs10() {
        set2010Expectations();

        GATEWAY_API_LEVEL level = mockNhinEndpointManager.getApiVersion(HCID, getService());
        assertTrue(level == GATEWAY_API_LEVEL.LEVEL_g0);
        context.assertIsSatisfied();
    }

    /**
     * Test target only supporting 2011
     */
    @Test
    public void testTargetIs20() {
        set2011Expectations();

        GATEWAY_API_LEVEL level = mockNhinEndpointManager.getApiVersion(HCID, getService());
        assertTrue(level == GATEWAY_API_LEVEL.LEVEL_g1);
        context.assertIsSatisfied();
    }

    /**
     * Test target supporting 2010 and 2011
     */
    @Test
    public void testTargetIsBoth() {
        expectConnectionManagerCacheBoth();

        GATEWAY_API_LEVEL level = mockNhinEndpointManager.getApiVersion(HCID, getService());
        assertTrue(level == GATEWAY_API_LEVEL.LEVEL_g1);
        context.assertIsSatisfied();
    }

    /*
     * -----------------Expectation Methods---------------
     */
    /**
     * Setup for no 1.0 specs (PDDeferred)
     */
    protected void expectNoConnectionManagerCache10() {
        expectConnectionManagerCache(null);
    }

    /**
     * Setup for 1.0 specs
     */
    protected void expectConnectionManagerCache10() {
        List<UDDI_SPEC_VERSION> list = new ArrayList<>();
        list.add(UDDI_SPEC_VERSION.SPEC_1_0);

        expectConnectionManagerCache(list);
    }

    /**
     * Setup for 1.1 specs
     */
    protected void expectConnectionManagerCache11() {
        List<UDDI_SPEC_VERSION> list = new ArrayList<>();
        list.add(UDDI_SPEC_VERSION.SPEC_1_1);

        expectConnectionManagerCache(list);
    }

    /**
     * Setup for 2.0 specs
     */
    protected void expectConnectionManagerCache20() {
        List<UDDI_SPEC_VERSION> list = new ArrayList<>();
        list.add(UDDI_SPEC_VERSION.SPEC_2_0);

        expectConnectionManagerCache(list);
    }

    /**
     * Setup for 3.0 specs
     */
    protected void expectConnectionManagerCache30() {
        List<UDDI_SPEC_VERSION> list = new ArrayList<>();
        list.add(UDDI_SPEC_VERSION.SPEC_3_0);

        expectConnectionManagerCache(list);
    }

    /**
     * Expectations for ConnectionManagerCache().
     */
    protected void expectConnectionManagerCache(final List<UDDI_SPEC_VERSION> list) {
        context.checking(new Expectations() {
            {
                exactly(1).of(mockExchangeManager).getSpecVersions(with(any(String.class)),
                    with(any(NHIN_SERVICE_NAMES.class)));
                will(returnValue(list));
            }
        });
    }

    /*
     * -----------------Setup Methods---------------
     */
    /**
     * @return NHIN_SERVICE_NAMES service name of the service under test
     */
    protected abstract NHIN_SERVICE_NAMES getService();

    protected abstract void set2010Expectations();

    protected abstract void set2011Expectations();

    protected abstract void expectConnectionManagerCacheBoth();
}
