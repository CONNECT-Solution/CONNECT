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
package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.event.EventFactory;
import gov.hhs.fha.nhinc.event.EventRecorder;
import gov.hhs.fha.nhinc.event.responder.BeginInboundProcessingEvent;
import gov.hhs.fha.nhinc.event.responder.EndInboundProcessingEvent;

import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.MessageImpl;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test {@link PatientDiscoveryEventAspect}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/EventFactoryConfig.xml" })
public class PatientDiscoveryEventAspectTest {

    @Autowired
    private EventFactory eventFactory;


    @Before
    public void mockWebserviceConext() {
        // Mock up message context
        MessageImpl msg = new MessageImpl();
        WrappedMessageContext msgCtx = new WrappedMessageContext(msg);
        WebServiceContextImpl.setMessageContext(msgCtx);
    }

    @Test
    public void beginInboundMessageEvent() {
        EventRecorder mockEventRecorder = mock(EventRecorder.class);

        PatientDiscoveryEventAspect aspect = new PatientDiscoveryEventAspect();
        aspect.setEventFactory(eventFactory);
        aspect.setEventRecorder(mockEventRecorder);

        ProxyPRPAIN201305UVProxyRequestType mockRequestType = mock(ProxyPRPAIN201305UVProxyRequestType.class);

        PRPAIN201305UV02 mockPRPAIN201305UV02 = mock(PRPAIN201305UV02.class);

        when(mockRequestType.getPRPAIN201305UV02()).thenReturn(mockPRPAIN201305UV02);

        aspect.beginInboundProcessingEvent(mockRequestType);

        verify(mockEventRecorder).recordEvent(any(BeginInboundProcessingEvent.class));

    }

    @Test
    public void endInboundMessageEvent() {
        EventRecorder mockEventRecorder = mock(EventRecorder.class);
        PatientDiscoveryEventAspect aspect = new PatientDiscoveryEventAspect();
        aspect.setEventFactory(eventFactory);
        aspect.setEventRecorder(mockEventRecorder);
        ProxyPRPAIN201305UVProxyRequestType mockRequestType = mock(ProxyPRPAIN201305UVProxyRequestType.class);

        PRPAIN201305UV02 mockPRPAIN201305UV02 = mock(PRPAIN201305UV02.class);

        when(mockRequestType.getPRPAIN201305UV02()).thenReturn(mockPRPAIN201305UV02);


        aspect.endInboundProcessingEvent(mockRequestType);

        verify(mockEventRecorder).recordEvent(any(EndInboundProcessingEvent.class));

    }

}
