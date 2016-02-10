/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.aspect;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.event.DefaultEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.EventLogger;
import gov.hhs.fha.nhinc.event.EventManager;
import gov.hhs.fha.nhinc.event.initiator.BeginNwhinInvocationEvent;
import gov.hhs.fha.nhinc.event.initiator.BeginOutboundMessageEvent;
import gov.hhs.fha.nhinc.event.initiator.BeginOutboundProcessingEvent;
import gov.hhs.fha.nhinc.event.initiator.EndNwhinInvocationEvent;
import gov.hhs.fha.nhinc.event.initiator.EndOutboundMessageEvent;
import gov.hhs.fha.nhinc.event.initiator.EndOutboundProcessingEvent;
import gov.hhs.fha.nhinc.event.responder.BeginAdapterDelegationEvent;
import gov.hhs.fha.nhinc.event.responder.BeginInboundMessageEvent;
import gov.hhs.fha.nhinc.event.responder.EndAdapterDelegationEvent;
import gov.hhs.fha.nhinc.event.responder.EndInboundMessageEvent;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.MessageImpl;
import org.aspectj.lang.JoinPoint;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author bhumphrey
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/CONNECT-context-test.xml", "/eventlogging.xml" })
public class EventAspectAdviceSpringContextTest {

    @Autowired
    private EventAspectAdvice eventAspectAdvice;

    @Autowired
    private EventManager eventManager;

    Answer<Class<DefaultEventDescriptionBuilder>> builderAnswer = new Answer<Class<DefaultEventDescriptionBuilder>>() {
        @Override
        public Class<DefaultEventDescriptionBuilder> answer(InvocationOnMock invocation) throws Throwable {
            return DefaultEventDescriptionBuilder.class;
        }
    };

    @Before
    public void mockWebserviceConext() {
        // Mock up message context
        MessageImpl msg = new MessageImpl();
        WrappedMessageContext msgCtx = new WrappedMessageContext(msg);
        WebServiceContextImpl.setMessageContext(msgCtx);
    }

    @Ignore
    @Test
    public void adapterDelegationEvent() {
        assertNotNull(eventAspectAdvice);

        EventLogger eventLogger = mock(EventLogger.class);
        eventManager.registerLogger(eventLogger);

        JoinPoint joinPoint = mock(JoinPoint.class);

        when(joinPoint.getArgs()).thenReturn(new Object[] {});

        AdapterDelegationEvent annotation = mock(AdapterDelegationEvent.class);

        when(annotation.serviceType()).thenReturn("Test Type");
        when(annotation.version()).thenReturn("version");
        when(annotation.beforeBuilder()).then(builderAnswer);
        when(annotation.afterReturningBuilder()).then(builderAnswer);

        eventAspectAdvice.beginAdapterDelegationEvent(joinPoint, annotation);
        verify(eventLogger).update(eq(eventManager), isA(BeginAdapterDelegationEvent.class));

        Object returnValue = mock(Object.class);

        eventAspectAdvice.endAdapterDelegationEvent(joinPoint, annotation, returnValue);
        verify(eventLogger).update(eq(eventManager), isA(EndAdapterDelegationEvent.class));
    }

    @Ignore
    @Test
    public void outboundMessageEvent() {
        assertNotNull(eventAspectAdvice);

        EventLogger eventLogger = mock(EventLogger.class);
        eventManager.registerLogger(eventLogger);

        JoinPoint joinPoint = mock(JoinPoint.class);

        when(joinPoint.getArgs()).thenReturn(new Object[] {});

        OutboundMessageEvent annotation = mock(OutboundMessageEvent.class);

        when(annotation.serviceType()).thenReturn("Test Type");
        when(annotation.version()).thenReturn("version");
        when(annotation.beforeBuilder()).then(builderAnswer);
        when(annotation.afterReturningBuilder()).then(builderAnswer);

        eventAspectAdvice.beginOutboundMessageEvent(joinPoint, annotation);
        verify(eventLogger).update(eq(eventManager), isA(BeginOutboundMessageEvent.class));

        Object returnValue = mock(Object.class);

        eventAspectAdvice.endOutboundMessageEvent(joinPoint, annotation, returnValue);
        verify(eventLogger).update(eq(eventManager), isA(EndOutboundMessageEvent.class));
    }

    @Ignore
    @Test
    public void outboundProcessingEvent() {
        assertNotNull(eventAspectAdvice);

        EventLogger eventLogger = mock(EventLogger.class);
        eventManager.registerLogger(eventLogger);

        JoinPoint joinPoint = mock(JoinPoint.class);
        when(joinPoint.getArgs()).thenReturn(new Object[] {});

        OutboundProcessingEvent annotation = mock(OutboundProcessingEvent.class);

        when(annotation.serviceType()).thenReturn("Test Type");
        when(annotation.version()).thenReturn("version");
        when(annotation.beforeBuilder()).then(builderAnswer);
        when(annotation.afterReturningBuilder()).then(builderAnswer);

        eventAspectAdvice.beginOutboundProcessingEvent(joinPoint, annotation);
        verify(eventLogger).update(eq(eventManager), isA(BeginOutboundProcessingEvent.class));

        Object returnValue = mock(Object.class);

        eventAspectAdvice.endOutboundProcessingEvent(joinPoint, annotation, returnValue);
        verify(eventLogger).update(eq(eventManager), isA(EndOutboundProcessingEvent.class));
    }

    @Ignore
    @Test
    public void inboundMessageEvent() {
        assertNotNull(eventAspectAdvice);

        EventLogger eventLogger = mock(EventLogger.class);
        eventManager.registerLogger(eventLogger);

        JoinPoint joinPoint = mock(JoinPoint.class);

        when(joinPoint.getArgs()).thenReturn(new Object[] {});

        InboundMessageEvent annotation = mock(InboundMessageEvent.class);

        when(annotation.serviceType()).thenReturn("Test Type");
        when(annotation.version()).thenReturn("version");
        when(annotation.beforeBuilder()).then(builderAnswer);
        when(annotation.afterReturningBuilder()).then(builderAnswer);

        eventAspectAdvice.beginInboundMessageEvent(joinPoint, annotation);
        verify(eventLogger).update(eq(eventManager), isA(BeginInboundMessageEvent.class));

        Object returnValue = mock(Object.class);

        eventAspectAdvice.endInboundMessageEvent(joinPoint, annotation, returnValue);
        verify(eventLogger).update(eq(eventManager), isA(EndInboundMessageEvent.class));
    }

    @Ignore
    @Test
    public void nwhinInvocationEvent() {
        assertNotNull(eventAspectAdvice);

        EventLogger eventLogger = mock(EventLogger.class);
        eventManager.registerLogger(eventLogger);

        JoinPoint joinPoint = mock(JoinPoint.class);

        when(joinPoint.getArgs()).thenReturn(new Object[] {});

        NwhinInvocationEvent annotation = mock(NwhinInvocationEvent.class);

        when(annotation.serviceType()).thenReturn("Test Type");
        when(annotation.version()).thenReturn("version");
        when(annotation.beforeBuilder()).then(builderAnswer);
        when(annotation.afterReturningBuilder()).then(builderAnswer);

        eventAspectAdvice.beginNwhinInvocationEvent(joinPoint, annotation);
        verify(eventLogger).update(eq(eventManager), isA(BeginNwhinInvocationEvent.class));

        Object returnValue = mock(Object.class);

        eventAspectAdvice.endNwhinInvocationEvent(joinPoint, annotation, returnValue);
        verify(eventLogger).update(eq(eventManager), isA(EndNwhinInvocationEvent.class));
    }

}
