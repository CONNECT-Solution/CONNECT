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
package gov.hhs.fha.nhinc.patientdiscovery.inbound;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201306UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.audit.transform.PatientDiscoveryAuditTransforms;
import java.lang.reflect.Method;
import java.util.Properties;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author akong
 *
 */
public class StandardInboundPatientDiscoveryTest {

    private final AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);
    private final PatientDiscovery201305Processor patientDiscoveryProcessor = mock(PatientDiscovery201305Processor.class);

    @Test
    public void hasInboundProcessingEvent() throws Exception {
        Class<StandardInboundPatientDiscovery> clazz = StandardInboundPatientDiscovery.class;
        Method method = clazz.getDeclaredMethod("respondingGatewayPRPAIN201305UV02",
            PRPAIN201305UV02.class, AssertionType.class, Properties.class);
        InboundProcessingEvent annotation = method.getAnnotation(InboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(PRPAIN201305UV02EventDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(PRPAIN201306UV02EventDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Patient Discovery", annotation.serviceType());
        assertEquals("1.0", annotation.version());
    }

    @Test
    public void standardInboundPatientDiscovery() throws PatientDiscoveryException {
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        AssertionType assertion = new AssertionType();
        Properties webContextProperties = new Properties();
        PRPAIN201306UV02 expectedResponse = new PRPAIN201306UV02();
        PatientDiscoveryAuditLogger auditLogger = getAuditLogger(true);

        when(patientDiscoveryProcessor.process201305(request, assertion)).thenReturn(expectedResponse);

        StandardInboundPatientDiscovery standardPatientDiscovery = new StandardInboundPatientDiscovery(
            patientDiscoveryProcessor, auditLogger);

        PRPAIN201306UV02 actualResponse = standardPatientDiscovery
            .respondingGatewayPRPAIN201305UV02(request, assertion, webContextProperties);

        assertSame(expectedResponse, actualResponse);

        verify(mockEJBLogger).auditResponseMessage(any(PRPAIN201305UV02.class), any(PRPAIN201306UV02.class),
            any(AssertionType.class), isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME), any(PatientDiscoveryAuditTransforms.class));
    }

    @Test
    public void standardInboundPDWithAuditLoggingOff() throws PatientDiscoveryException {
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        AssertionType assertion = new AssertionType();
        Properties webContextProperties = new Properties();
        PRPAIN201306UV02 expectedResponse = new PRPAIN201306UV02();

        PatientDiscoveryAuditLogger auditLogger = getAuditLogger(false);

        when(patientDiscoveryProcessor.process201305(request, assertion)).thenReturn(expectedResponse);

        StandardInboundPatientDiscovery standardPatientDiscovery = new StandardInboundPatientDiscovery(
            patientDiscoveryProcessor, auditLogger);

        PRPAIN201306UV02 actualResponse = standardPatientDiscovery
            .respondingGatewayPRPAIN201305UV02(request, assertion, webContextProperties);

        assertSame(expectedResponse, actualResponse);

        verify(mockEJBLogger, never()).auditResponseMessage(any(PRPAIN201305UV02.class), any(PRPAIN201306UV02.class),
            any(AssertionType.class), isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME), any(PatientDiscoveryAuditTransforms.class));
    }

    private PatientDiscoveryAuditLogger getAuditLogger(final boolean isLoggingOn) {
        return new PatientDiscoveryAuditLogger() {
            @Override
            protected AuditEJBLogger getAuditLogger() {
                return mockEJBLogger;
            }

            @Override
            protected boolean isAuditLoggingOn(String serviceName) {
                return isLoggingOn;
            }
        };
    }
}
