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
package gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import gov.hhs.fha.nhinc.event.EventRecorder;
import gov.hhs.fha.nhinc.event.initiator.BeginOutboundMessageEvent;
import gov.hhs.fha.nhinc.event.initiator.EndOutboundMessageEvent;
import gov.hhs.fha.nhinc.patientdiscovery._10.passthru.NhincProxyPatientDiscoveryImpl;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PatientDiscoveryEventAspect;

import javax.xml.ws.WebServiceContext;

import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.MessageImpl;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/EventFactoryConfig.xml", "aspectj-unit-tests.xml" })
public class NhincProxyPatientDiscoveryTest {

    private static final PatientDiscoveryServiceFactory mockFactory = mock(PatientDiscoveryServiceFactory.class);

    @Autowired
    private NhincProxyPatientDiscovery patientDiscovery;

    @Autowired
    private PatientDiscoveryEventAspect patientDiscoveryEventAspect;

    @Test
    public void testDefaultConstructor() {
        assertNotNull(patientDiscovery);
    }

    @Before
    public void mockWebserviceConext() {
        // Mock up message context
        MessageImpl msg = new MessageImpl();
        WrappedMessageContext msgCtx = new WrappedMessageContext(msg);
        WebServiceContextImpl.setMessageContext(msgCtx);
    }

    /**
     * Tests {@link NhincProxyPatientDiscovery#testProxyPRPAIN201305UV()} Ensure aspect advice is invoked.
     */
    @Test
    public void testProxyPRPAIN201305UV() {

        ProxyPRPAIN201305UVProxyRequestType requestType = mock(ProxyPRPAIN201305UVProxyRequestType.class);

        final PRPAIN201306UV02 expectedResponse = mock(PRPAIN201306UV02.class);
        final NhincProxyPatientDiscoveryImpl mockService = mock(NhincProxyPatientDiscoveryImpl.class);

        patientDiscovery.setOrchestratorImpl(mockService);

        when(mockService.proxyPRPAIN201305UV(eq(requestType), any(WebServiceContext.class))).thenReturn(
                expectedResponse);
        PRPAIN201305UV02 body = mock(PRPAIN201305UV02.class);
        when(requestType.getPRPAIN201305UV02()).thenReturn(body);

        EventRecorder eventRecorder = mock(EventRecorder.class);
        patientDiscoveryEventAspect.setEventRecorder(eventRecorder);

        PRPAIN201306UV02 actualResponse = patientDiscovery.proxyPRPAIN201305UV(requestType);

        InOrder inOrder = inOrder(eventRecorder);

        inOrder.verify(eventRecorder).recordEvent(isA(BeginOutboundMessageEvent.class));
        inOrder.verify(eventRecorder).recordEvent(isA(EndOutboundMessageEvent.class));

        assertSame(expectedResponse, actualResponse);
    }

    /**
     * This method is necessary for wiring in the factory to the NhincProxyPatientDiscovery in a spring config.
     * 
     * @return patient discovery service factory.
     */
    public static PatientDiscoveryServiceFactory getPatientDiscoveryServiceFactory() {
        return mockFactory;
    }

}
