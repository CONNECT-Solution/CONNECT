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
package gov.hhs.fha.nhinc.patientdiscovery.nhin;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.generic.GenericFactory;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryProcessor;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxy;
import gov.hhs.fha.nhinc.properties.ServicePropertyAccessor;

@RunWith(JMock.class)
public class NhinPatientDiscoveryOrchImplTest {

    Mockery context = new JUnit4Mockery();

    PatientDiscoveryAuditor auditor = context.mock(PatientDiscoveryAuditor.class);
    PatientDiscoveryProcessor mockProcessor = context.mock(PatientDiscoveryProcessor.class);
    AdapterPatientDiscoveryProxy mockProxy = context.mock(AdapterPatientDiscoveryProxy.class);

    GenericFactory<AdapterPatientDiscoveryProxy> proxyFactory = new GenericFactory<AdapterPatientDiscoveryProxy>() {
        @Override
        public AdapterPatientDiscoveryProxy create() {
            return mockProxy;
        }
    };

    @Test
    public void notPassThrough() throws PatientDiscoveryException {
        final PRPAIN201305UV02 body = new PRPAIN201305UV02();
        final AssertionType assertion = new AssertionType();
        final PRPAIN201306UV02 expectedResponse = new PRPAIN201306UV02();

        InboundPatientDiscoveryOrchestration impl = buildOrchestrationForTest(true, false);

        context.checking(new Expectations() {
            {
                oneOf(auditor).auditNhin201305(with(same(body)), with(same(assertion)), with("Inbound"));
                oneOf(auditor).auditAdapter201305(with(same(body)), with(same(assertion)), with("Outbound"));
                oneOf(mockProcessor).process201305(with(same(body)), with(same(assertion)));
                will(returnValue(expectedResponse));
                oneOf(auditor).auditAdapter201306(with(any(PRPAIN201306UV02.class)), with(same(assertion)), with("Inbound"));
                oneOf(auditor).auditNhin201306(with(any(PRPAIN201306UV02.class)), with(same(assertion)),
                        with("Outbound"));
            }
        });

        PRPAIN201306UV02 actualResponse = impl.respondingGatewayPRPAIN201305UV02(body, assertion);
        assertSame(expectedResponse, actualResponse);
    }

    @Test
    public void passThrough() throws PatientDiscoveryException {
        final PRPAIN201305UV02 body = new PRPAIN201305UV02();
        final AssertionType assertion = new AssertionType();
        final PRPAIN201306UV02 expectedResponse = new PRPAIN201306UV02();

        InboundPatientDiscoveryOrchestration impl = buildOrchestrationForTest(true, true);

        context.checking(new Expectations() {
            {
                oneOf(auditor).auditNhin201305(with(same(body)), with(same(assertion)), with("Inbound"));
                oneOf(mockProxy).respondingGatewayPRPAIN201305UV02(with(same(body)), with(same(assertion)));
                will(returnValue(expectedResponse));
                oneOf(auditor).auditNhin201306(with(any(PRPAIN201306UV02.class)), with(same(assertion)),
                        with("Outbound"));
            }
        });

        PRPAIN201306UV02 actualResponse = impl.respondingGatewayPRPAIN201305UV02(body, assertion);
        assertSame(expectedResponse, actualResponse);
    }

    protected InboundPatientDiscoveryOrchestration buildOrchestrationForTest(final boolean serviceEnabled,
            final boolean passThrough) {
        InboundPatientDiscoveryOrchestration impl = new NhinPatientDiscoveryOrchImpl(new ServicePropertyAccessor() {
            @Override
            public boolean isInPassThroughMode() {
                return passThrough;
            }
        }, auditor, mockProcessor, proxyFactory);
        return impl;
    }

}
