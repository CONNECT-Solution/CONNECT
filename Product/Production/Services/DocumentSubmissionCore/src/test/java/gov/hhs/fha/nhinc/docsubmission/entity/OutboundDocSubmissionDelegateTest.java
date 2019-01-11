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
package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.orchestration.OrchestrationContextFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class OutboundDocSubmissionDelegateTest {

    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private static final String RESPONSE_ID_G0 = "g0";
    private static final String RESPONSE_ID_G1 = "g1";

    private final OrchestrationContextFactory mockContextFactory = context.mock(OrchestrationContextFactory.class);
    private final OrchestrationContext mockOrchestrationContext = context.mock(OrchestrationContext.class);

    @Test
    public void testOrchestration_G0Context() {
        setMockContextFactoryToReturnG0();

        OutboundDocSubmissionDelegate delegate = createOutboundDocSubmissionDelegate();
        OutboundDocSubmissionOrchestratable dsOrchestratable = createOutboundDocSubmissionOrchestratable(delegate);
        RegistryResponseType response = ((OutboundDocSubmissionOrchestratable) delegate.process(dsOrchestratable)).getResponse();

        context.assertIsSatisfied();
        assertNotNull(response);
        assertEquals(RESPONSE_ID_G0, response.getRequestId());
    }

    @Test
    public void testOrchestration_G1Context() {
        setMockContextFactoryToReturnG1();

        OutboundDocSubmissionDelegate delegate = createOutboundDocSubmissionDelegate();
        OutboundDocSubmissionOrchestratable dsOrchestratable = createOutboundDocSubmissionOrchestratable(delegate);
        RegistryResponseType response = ((OutboundDocSubmissionOrchestratable) delegate.process(dsOrchestratable)).getResponse();

        context.assertIsSatisfied();
        assertNotNull(response);
        assertEquals(RESPONSE_ID_G1, response.getRequestId());
    }

    @Test
    public void testOrchestration_UnknownContext() {
        setMockContextFactoryToReturnNull();

        OutboundDocSubmissionDelegate delegate = createOutboundDocSubmissionDelegate();
        OutboundDocSubmissionOrchestratable dsOrchestratable = createOutboundDocSubmissionOrchestratable(delegate);
        Orchestratable response = delegate.process(dsOrchestratable);

        context.assertIsSatisfied();
        assertNull(response);
    }

    @Test
    public void testOrchestration_GenericOrchestratable() {
        setMockContextFactoryToReturnG0();

        OutboundDocSubmissionDelegate delegate = createOutboundDocSubmissionDelegate();
        Orchestratable orchestratable = createOutboundDocSubmissionOrchestratable(delegate);
        RegistryResponseType response = ((OutboundDocSubmissionOrchestratable) delegate.process(orchestratable)).getResponse();

        context.assertIsSatisfied();
        assertNotNull(response);
        assertEquals(RESPONSE_ID_G0, response.getRequestId());
    }

    @Test
    public void testOrchestration_NullOrchestratable() {
        OutboundDocSubmissionDelegate delegate = createOutboundDocSubmissionDelegate();
        Orchestratable response = delegate.process(null);

        context.assertIsSatisfied();
        assertNull(response);
    }

    @Test
    public void testOrchestration_UnknownOrchestratable() {
        TestOrchestratable wrongOrchestratable = new TestOrchestratable();
        OutboundDocSubmissionDelegate delegate = createOutboundDocSubmissionDelegate();
        Orchestratable response = delegate.process(wrongOrchestratable);

        context.assertIsSatisfied();
        assertNull(response);
    }

    @Test
    public void testGetters() {
        OutboundDocSubmissionDelegate delegate = new OutboundDocSubmissionDelegate();

        context.assertIsSatisfied();
        assertNotNull(delegate.getOrchestrationContextFactory());
    }

    @Test
    public void testCreateErrorResponse() {
        OutboundDocSubmissionDelegate delegate = new OutboundDocSubmissionDelegate();
        delegate.createErrorResponse(null, null);
    }

    private OutboundDocSubmissionOrchestratable createOutboundDocSubmissionOrchestratable(OutboundDocSubmissionDelegate delegate) {
        NhinTargetSystemType target = new NhinTargetSystemType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId("1.1");
        target.setHomeCommunity(homeCommunity);

        OutboundDocSubmissionOrchestratable dsOrchestratable = new OutboundDocSubmissionOrchestratable(delegate);
        dsOrchestratable.setAssertion(null);
        dsOrchestratable.setRequest(null);
        dsOrchestratable.setTarget(target);

        return dsOrchestratable;
    }

    private void setMockContextFactoryToReturnG0() {
        context.checking(new Expectations() {
            {
                oneOf(mockContextFactory).getBuilder(with(any(NhinTargetSystemType.class)), with(equal(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION)));
                will(returnValue(createOutboundDocSubmissionOrchestrationContextBuilder_g0()));
            }
        });
    }

    private OutboundDocSubmissionOrchestrationContextBuilder_g0 createOutboundDocSubmissionOrchestrationContextBuilder_g0() {
        context.checking(new Expectations() {
            {
                oneOf(mockOrchestrationContext).execute();
                will(returnValue(createOutboundOrchestratable(RESPONSE_ID_G0)));
            }
        });

        return new OutboundDocSubmissionOrchestrationContextBuilder_g0() {
            @Override
            public OrchestrationContext build() {
                return mockOrchestrationContext;
            }
        };
    }

    private void setMockContextFactoryToReturnG1() {
        context.checking(new Expectations() {
            {
                oneOf(mockContextFactory).getBuilder(with(any(NhinTargetSystemType.class)), with(equal(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION)));
                will(returnValue(createOutboundDocSubmissionOrchestrationContextBuilder_g1()));
            }
        });
    }

    private OutboundDocSubmissionOrchestrationContextBuilder_g1 createOutboundDocSubmissionOrchestrationContextBuilder_g1() {
        context.checking(new Expectations() {
            {
                oneOf(mockOrchestrationContext).execute();
                will(returnValue(createOutboundOrchestratable(RESPONSE_ID_G1)));
            }
        });

        return new OutboundDocSubmissionOrchestrationContextBuilder_g1() {
            @Override
            public OrchestrationContext build() {
                return mockOrchestrationContext;
            }
        };
    }

    private void setMockContextFactoryToReturnNull() {
        context.checking(new Expectations() {
            {
                oneOf(mockContextFactory).getBuilder(with(any(NhinTargetSystemType.class)), with(equal(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION)));
                will(returnValue(null));
            }
        });
    }

    private OutboundOrchestratable createOutboundOrchestratable(String id) {
        OutboundDocSubmissionOrchestratable orchestratable = new OutboundDocSubmissionOrchestratable(null);

        RegistryResponseType response = new RegistryResponseType();
        response.setRequestId(id);
        orchestratable.setResponse(response);

        return orchestratable;
    }

    private OutboundDocSubmissionDelegate createOutboundDocSubmissionDelegate() {
        return new OutboundDocSubmissionDelegate() {
            @Override
            protected OrchestrationContextFactory getOrchestrationContextFactory() {
                return mockContextFactory;
            }
        };
    }
}
