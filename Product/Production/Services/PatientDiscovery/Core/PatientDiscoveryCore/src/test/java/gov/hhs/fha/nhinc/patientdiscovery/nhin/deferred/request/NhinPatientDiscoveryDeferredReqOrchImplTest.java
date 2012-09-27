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
package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.request;

import static org.junit.Assert.assertSame;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.generic.GenericFactory;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.PolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.error.proxy.AdapterPatientDiscoveryDeferredReqErrorProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscoveryDeferredReqProxy;
import gov.hhs.fha.nhinc.properties.ServicePropertyAccessor;

/**
 *
 * @author JHOPPESC
 */
@RunWith(JMock.class)
public class NhinPatientDiscoveryDeferredReqOrchImplTest {

    Mockery context = new JUnit4Mockery();

    PatientDiscoveryAuditor auditor = context.mock(PatientDiscoveryAuditor.class);
    AdapterPatientDiscoveryDeferredReqProxy mockProxy = context.mock(AdapterPatientDiscoveryDeferredReqProxy.class);
    AdapterPatientDiscoveryDeferredReqErrorProxy mockErrorProxy = context
            .mock(AdapterPatientDiscoveryDeferredReqErrorProxy.class);

    @SuppressWarnings("unchecked")
    PolicyChecker<RespondingGatewayPRPAIN201305UV02RequestType, PRPAIN201305UV02> proxyPolicyChecker = context
            .mock(PolicyChecker.class);

    GenericFactory<AdapterPatientDiscoveryDeferredReqErrorProxy> proxyErrorFactory = new GenericFactory<AdapterPatientDiscoveryDeferredReqErrorProxy>() {
        @Override
        public AdapterPatientDiscoveryDeferredReqErrorProxy create() {
            return mockErrorProxy;
        }
    };

    GenericFactory<AdapterPatientDiscoveryDeferredReqProxy> proxyFactory = new GenericFactory<AdapterPatientDiscoveryDeferredReqProxy>() {
        @Override
        public AdapterPatientDiscoveryDeferredReqProxy create() {
            return mockProxy;
        }
    };

    protected NhinPatientDiscoveryDeferredReqOrch buildOrchestrationForTest(final boolean passThrough) {
        NhinPatientDiscoveryDeferredReqOrch impl = new NhinPatientDiscoveryDeferredReqOrchImpl(
                new ServicePropertyAccessor() {
                    @Override
                    public boolean isInPassThroughMode() {
                        return passThrough;
                    }
                }, auditor, proxyFactory, proxyErrorFactory, proxyPolicyChecker);
        return impl;
    }

    @Test
    public void notPassThroughPolicyCheckPasses() {
        NhinPatientDiscoveryDeferredReqOrch instance = buildOrchestrationForTest(false);

        final AssertionType assertion = null;
        final MCCIIN000002UV01 expectedResponse = new MCCIIN000002UV01();

        final PRPAIN201305UV02 body = new PRPAIN201305UV02();

        context.checking(new Expectations() {
            {
                oneOf(auditor).auditNhinDeferred201305(with(same(body)), with(same(assertion)), with("Inbound"));

                oneOf(proxyPolicyChecker).checkIncomingPolicy(with(same(body)), with(same(assertion)));
                will(returnValue(true));

                oneOf(mockProxy).processPatientDiscoveryAsyncReq(with(same(body)), with(same(assertion)));
                will(returnValue(expectedResponse));

                oneOf(auditor).auditAdapterDeferred201305(with(same(body)), with(same(assertion)), with("Outbound"));
                oneOf(auditor).auditAck(with(same(expectedResponse)), with(same(assertion)), with("Inbound"),
                        with("Adapter"));
                oneOf(auditor).auditAck(with(same(expectedResponse)), with(same(assertion)), with("Outbound"),
                        with("Nhin"));
            }
        });

        MCCIIN000002UV01 actualResponse = instance.respondingGatewayPRPAIN201305UV02(body, assertion);

        assertSame(expectedResponse, actualResponse);

    }

    @Test
    public void notPassThroughPolicyCheckFails() {
        NhinPatientDiscoveryDeferredReqOrch instance = buildOrchestrationForTest(false);

        final AssertionType assertion = null;
        final MCCIIN000002UV01 expectedResponse = new MCCIIN000002UV01();

        final PRPAIN201305UV02 body = new PRPAIN201305UV02();

        context.checking(new Expectations() {
            {
                oneOf(auditor).auditNhinDeferred201305(with(same(body)), with(same(assertion)), with("Inbound"));

                oneOf(proxyPolicyChecker).checkIncomingPolicy(with(same(body)), with(same(assertion)));
                will(returnValue(false));

                oneOf(auditor).auditAdapterDeferred201305(with(same(body)), with(same(assertion)), with("Outbound"));

                oneOf(mockErrorProxy).processPatientDiscoveryAsyncReqError(with(same(body)),
                        with(any(PRPAIN201306UV02.class)), with(same(assertion)), with("Policy Check Failed"));
                will(returnValue(expectedResponse));
                oneOf(auditor).auditAck(with(any(MCCIIN000002UV01.class)), with(same(assertion)), with("Inbound"),
                        with("Adapter"));
                oneOf(auditor).auditAck(with(any(MCCIIN000002UV01.class)), with(same(assertion)), with("Outbound"),
                        with("Nhin"));

            }
        });

        MCCIIN000002UV01 actualResponse = instance.respondingGatewayPRPAIN201305UV02(body, assertion);

        assertSame(expectedResponse, actualResponse);

    }

    @Test
    public void passThroughPolicy() {
        NhinPatientDiscoveryDeferredReqOrch instance = buildOrchestrationForTest(true);

        final AssertionType assertion = null;
        final MCCIIN000002UV01 expectedResponse = new MCCIIN000002UV01();

        final PRPAIN201305UV02 body = new PRPAIN201305UV02();

        context.checking(new Expectations() {
            {
                oneOf(auditor).auditNhinDeferred201305(with(same(body)), with(same(assertion)), with("Inbound"));

                oneOf(mockProxy).processPatientDiscoveryAsyncReq(with(same(body)), with(same(assertion)));
                will(returnValue(expectedResponse));

                oneOf(auditor).auditAdapterDeferred201305(with(same(body)), with(same(assertion)), with("Outbound"));
                oneOf(auditor).auditAck(with(same(expectedResponse)), with(same(assertion)), with("Inbound"),
                        with("Adapter"));
                oneOf(auditor).auditAck(with(same(expectedResponse)), with(same(assertion)), with("Outbound"),
                        with("Nhin"));
            }
        });

        MCCIIN000002UV01 actualResponse = instance.respondingGatewayPRPAIN201305UV02(body, assertion);

        assertSame(expectedResponse, actualResponse);

    }

}
