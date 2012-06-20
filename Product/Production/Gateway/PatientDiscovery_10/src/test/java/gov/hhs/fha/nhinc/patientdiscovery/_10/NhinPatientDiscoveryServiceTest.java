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
package gov.hhs.fha.nhinc.patientdiscovery._10;

import static org.junit.Assert.assertSame;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryImpl;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.GenericFactory;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.InboundPatientDiscoveryOrchestration;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms;

public class NhinPatientDiscoveryServiceTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testHappyPath() throws PatientDiscoveryException {

        final PRPAIN201305UV02 request = context.mock(PRPAIN201305UV02.class);
        final WebServiceContext webServiceContext = context.mock(WebServiceContext.class);
        final PRPAIN201306UV02 expectedResponse = context.mock(PRPAIN201306UV02.class);
        final InboundPatientDiscoveryOrchestration mockOrchestration = context
                .mock(InboundPatientDiscoveryOrchestration.class);
        final AssertionType mockAssertion = context.mock(AssertionType.class);
        final PatientDiscoveryAuditLogger mockAuditLogger = context.mock(PatientDiscoveryAuditLogger.class);
        final PatientDiscoveryTransforms mockPatientDiscoveryTransforms = context
                .mock(PatientDiscoveryTransforms.class);
        final PerformanceManager mockPerformanceManager = context.mock(PerformanceManager.class);

        GenericFactory<InboundPatientDiscoveryOrchestration> orchestrationFactory = new GenericFactory<InboundPatientDiscoveryOrchestration>() {

            public InboundPatientDiscoveryOrchestration create() {
                return mockOrchestration;
            }
        };

        NhinPatientDiscoveryImpl service = new NhinPatientDiscoveryImpl(mockAuditLogger, orchestrationFactory) {

            @Override
            protected AssertionType getSamlAssertion(WebServiceContext context) {
                return mockAssertion;
            }

            @Override
            protected PatientDiscoveryTransforms getTransforms() {
                return mockPatientDiscoveryTransforms;
            }

            @Override
            protected PerformanceManager getPerformanceManager() {
                return mockPerformanceManager;
            }

        };

        context.checking(new Expectations() {
            {
                allowing(mockOrchestration).respondingGatewayPRPAIN201305UV02(with(same(request)),
                        with(same(mockAssertion)));
                will(returnValue(expectedResponse));

                oneOf(mockPatientDiscoveryTransforms).getPatientDiscoveryMessageCommunityId(request,
                        NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                        NhincConstants.AUDIT_LOG_SYNC_TYPE, NhincConstants.AUDIT_LOG_REQUEST_PROCESS);
                will(returnValue("1.1"));

                oneOf(mockPerformanceManager).logPerformanceStart(
                        with(same(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME)),
                        with(same(NhincConstants.AUDIT_LOG_NHIN_INTERFACE)),
                        with(same(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION)), with(same("1.1")));

                oneOf(mockPerformanceManager).logPerformanceStop(
                        with(same(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME)),
                        with(same(NhincConstants.AUDIT_LOG_NHIN_INTERFACE)),
                        with(same(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION)), with(same("1.1")));

                oneOf(mockAuditLogger).auditNhin201305(with(same(request)), with(same(mockAssertion)),
                        with(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION));
                oneOf(mockAuditLogger).auditNhin201306(with(same(expectedResponse)), with(same(mockAssertion)),
                        with(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION));

            }
        });

        PRPAIN201306UV02 actualResponse = service.respondingGatewayPRPAIN201305UV02(request, webServiceContext);

        assertSame(expectedResponse, actualResponse);

    }

}
