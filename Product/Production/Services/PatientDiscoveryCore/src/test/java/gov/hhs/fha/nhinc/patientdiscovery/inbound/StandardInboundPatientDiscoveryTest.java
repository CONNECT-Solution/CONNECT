/*
 * Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201306UV02EventDescriptionBuilder;

import java.lang.reflect.Method;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.Test;

/**
 * @author akong
 * 
 */
public class StandardInboundPatientDiscoveryTest {

    @Test
    public void hasInboundProcessingEvent() throws Exception {
        Class<StandardInboundPatientDiscovery> clazz = StandardInboundPatientDiscovery.class;
        Method method = clazz.asSubclass(AbstractInboundPatientDiscovery.class).getDeclaredMethod("process",
                PRPAIN201305UV02.class, AssertionType.class);
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
        PRPAIN201306UV02 expectedResponse = new PRPAIN201306UV02();

        PatientDiscovery201305Processor patientDiscoveryProcessor = mock(PatientDiscovery201305Processor.class);
        PatientDiscoveryAuditLogger auditLogger = mock(PatientDiscoveryAuditLogger.class);

        when(patientDiscoveryProcessor.process201305(request, assertion)).thenReturn(expectedResponse);

        StandardInboundPatientDiscovery standardPatientDiscovery = new StandardInboundPatientDiscovery(
                patientDiscoveryProcessor, auditLogger);

        PRPAIN201306UV02 actualResponse = standardPatientDiscovery
                .respondingGatewayPRPAIN201305UV02(request, assertion);

        assertSame(expectedResponse, actualResponse);

        verify(auditLogger).auditNhin201305(eq(request), eq(assertion), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION));

        verify(auditLogger).auditNhin201306(eq(actualResponse), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION));

        verify(auditLogger).auditAdapter201305(eq(request), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION));

        verify(auditLogger).auditAdapter201306(eq(actualResponse), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION));

    }

}
