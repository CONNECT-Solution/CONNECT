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
package gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.MCCIIN000002UV01EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.OutboundPatientDiscoveryDeferredRequestDelegate;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.OutboundPatientDiscoveryDeferredRequestOrchestratable;

import java.lang.reflect.Method;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.junit.Test;

/**
 * @author achidamb
 * 
 */
public class PassthroughOutboundPatientDiscoveryDeferredRequestTest {

    @Test
    public void hasOutboundProcessingEvent() throws Exception {
        Class<PassthroughOutboundPatientDiscoveryDeferredRequest> clazz = PassthroughOutboundPatientDiscoveryDeferredRequest.class;
        Method method = clazz.getMethod("processPatientDiscoveryAsyncReq", PRPAIN201305UV02.class, AssertionType.class,
                NhinTargetCommunitiesType.class);
        OutboundProcessingEvent annotation = method.getAnnotation(OutboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(PRPAIN201305UV02EventDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(MCCIIN000002UV01EventDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Patient Discovery Deferred Request", annotation.serviceType());
        assertEquals("1.0", annotation.version());
    }

    @Test
    public void invoke() {
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        MCCIIN000002UV01 expectedResponse = new MCCIIN000002UV01();

        PatientDiscoveryAuditor auditLogger = mock(PatientDiscoveryAuditor.class);
        OutboundPatientDiscoveryDeferredRequestDelegate delegate = mock(OutboundPatientDiscoveryDeferredRequestDelegate.class);
        OutboundPatientDiscoveryDeferredRequestOrchestratable returnedOrchestratable = mock(OutboundPatientDiscoveryDeferredRequestOrchestratable.class);

        when(returnedOrchestratable.getResponse()).thenReturn(expectedResponse);

        when(delegate.process(any(OutboundPatientDiscoveryDeferredRequestOrchestratable.class))).thenReturn(
                returnedOrchestratable);

        PassthroughOutboundPatientDiscoveryDeferredRequest passthroughPatientDiscovery = new PassthroughOutboundPatientDiscoveryDeferredRequest(
                auditLogger, delegate);

        MCCIIN000002UV01 actualResponse = passthroughPatientDiscovery.processPatientDiscoveryAsyncReq(request,
                assertion, targets);

        assertSame(expectedResponse, actualResponse);

        verify(auditLogger, never()).auditNhinDeferred201305(any(PRPAIN201305UV02.class), any(AssertionType.class),
                any(String.class));

        verify(auditLogger, never()).auditAck(any(MCCIIN000002UV01.class), any(AssertionType.class), any(String.class),
                any(String.class));
    }

}
