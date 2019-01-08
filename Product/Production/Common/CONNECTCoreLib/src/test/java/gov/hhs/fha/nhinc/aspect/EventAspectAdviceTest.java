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
package gov.hhs.fha.nhinc.aspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.DefaultEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.EventRecorder;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * @author bhumphrey
 *
 */
@RunWith(value = MockitoJUnitRunner.class)
public class EventAspectAdviceTest {

    @Mock(name="adapterDelegationAdviceDelegate")
    private EventAdviceDelegate adapterDelegationAdviceDelegate;
    @Mock(name="inboundMessageAdviceDelegate")
    private EventAdviceDelegate inboundMessageAdviceDelegate;
    @Mock(name="inboundProcessingAdviceDelegate")
    private EventAdviceDelegate inboundProcessingAdviceDelegate;
    @Mock(name="nwhinInvocationAdviceDelegate")
    private EventAdviceDelegate nwhinInvocationAdviceDelegate;
    @Mock(name="outboundMessageAdviceDelegate")
    private EventAdviceDelegate outboundMessageAdviceDelegate;
    @Mock(name="outboundProcessingAdviceDelegate")
    private EventAdviceDelegate outboundProcessingAdviceDelegate;
    @Mock(name="eventmanager")
    private EventRecorder eventRecorder;
    @Mock
    private ProceedingJoinPoint mockJoinPoint;
    @Mock
    private InboundMessageEvent inboundMessageEvent;
    @Mock
    private InboundProcessingEvent inboundProcessingEvent;
    @Mock
    private AdapterDelegationEvent adapterDelegationEvent;
    @Mock
    private OutboundMessageEvent outboundMessageEvent;
    @Mock
    private OutboundProcessingEvent outboundProcessingEvent;
    @Mock
    private NwhinInvocationEvent nwhinInvocationEvent;

    @InjectMocks
    @Spy
    private EventAspectAdvice advice;

    private final String serviceType = "test-serviceType";
    private final String version = "test-version";
    private static final String EXCEPTION_MESSAGE = "The method failed, Woe is me!";
    private static final String CUSTOM_MESSAGE = "I am a custom message";
    private static final String OVERRIDE = "I am an override";

    private Class[] annotations = new Class[] {  InboundMessageEvent.class,  InboundProcessingEvent.class,
            AdapterDelegationEvent.class,  OutboundMessageEvent.class,  OutboundProcessingEvent.class,
            NwhinInvocationEvent.class};

    Answer<Class<? extends BaseEventDescriptionBuilder>> beforeBuilderAnswer = new Answer<Class<? extends BaseEventDescriptionBuilder>>() {
        @Override
        public Class<? extends BaseEventDescriptionBuilder> answer(InvocationOnMock invocation) throws Throwable {
            return DefaultEventDescriptionBuilder.class;
        }
    };

    Answer<Class<? extends BaseEventDescriptionBuilder>> afterReturningBuilderAnswer = new Answer<Class<? extends BaseEventDescriptionBuilder>>() {
        @Override
        public Class<? extends BaseEventDescriptionBuilder> answer(InvocationOnMock invocation) throws Throwable {
            return BaseEventDescriptionBuilder.class;
        }
    };


    @Before
    public void setupMocks() throws Throwable {
        when(mockJoinPoint.getArgs()).thenReturn(new Object[]{});
        when(mockJoinPoint.proceed()).thenReturn(new Object());

        MethodSignature sig = Mockito.mock(MethodSignature.class);
        when(sig.getName()).thenReturn("SIGNATURE_NAME");
        when(sig.getDeclaringType()).thenReturn(EventAspectAdvice.class);
        when(mockJoinPoint.getSignature()).thenReturn(sig);

        when(inboundMessageEvent.serviceType()).thenReturn(serviceType);
        when(inboundMessageEvent.version()).thenReturn(version);
        when(inboundMessageEvent.beforeBuilder()).thenAnswer(beforeBuilderAnswer);
        when(inboundMessageEvent.afterReturningBuilder()).thenAnswer(afterReturningBuilderAnswer);

        when(inboundProcessingEvent.serviceType()).thenReturn(serviceType);
        when(inboundProcessingEvent.version()).thenReturn(version);
        when(inboundProcessingEvent.beforeBuilder()).thenAnswer(beforeBuilderAnswer);
        when(inboundProcessingEvent.afterReturningBuilder()).thenAnswer(afterReturningBuilderAnswer);

        when(adapterDelegationEvent.serviceType()).thenReturn(serviceType);
        when(adapterDelegationEvent.version()).thenReturn(version);
        when(adapterDelegationEvent.beforeBuilder()).thenAnswer(beforeBuilderAnswer);
        when(adapterDelegationEvent.afterReturningBuilder()).thenAnswer(afterReturningBuilderAnswer);

        when(outboundMessageEvent.serviceType()).thenReturn(serviceType);
        when(outboundMessageEvent.version()).thenReturn(version);
        when(outboundMessageEvent.beforeBuilder()).thenAnswer(beforeBuilderAnswer);
        when(outboundMessageEvent.afterReturningBuilder()).thenAnswer(afterReturningBuilderAnswer);

        when(outboundProcessingEvent.serviceType()).thenReturn(serviceType);
        when(outboundProcessingEvent.version()).thenReturn(version);
        when(outboundProcessingEvent.beforeBuilder()).thenAnswer(beforeBuilderAnswer);
        when(outboundProcessingEvent.afterReturningBuilder()).thenAnswer(afterReturningBuilderAnswer);

        when(nwhinInvocationEvent.serviceType()).thenReturn(serviceType);
        when(nwhinInvocationEvent.version()).thenReturn(version);
        when(nwhinInvocationEvent.beforeBuilder()).thenAnswer(beforeBuilderAnswer);
        when(nwhinInvocationEvent.afterReturningBuilder()).thenAnswer(afterReturningBuilderAnswer);

        when(eventRecorder.isRecordEventEnabled()).thenReturn(false);
    }

    @After
    public void cleanUpMocks() {
        reset(mockJoinPoint);
    }

    @Test
    public void verifyInboundMessageEvent() throws Throwable {

        advice.logInboundMessageEvent(mockJoinPoint, inboundMessageEvent);

        verify(inboundMessageAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(beforeBuilderAnswer.answer(null)));

        verify(inboundMessageAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(afterReturningBuilderAnswer.answer(null)), any(Object.class));
    }



    @Test
    public void verifyOutboundMessageEvent() throws Throwable {


        advice.logOutboundMessageEvent(mockJoinPoint, outboundMessageEvent);

        verify(outboundMessageAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(beforeBuilderAnswer.answer(null)));

        verify(outboundMessageAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(afterReturningBuilderAnswer.answer(null)), any(Object.class));
    }



    @Test
    public void verifyAdapterDelegationEvent() throws Throwable {

        advice.logAdapterDelegationEvent(mockJoinPoint, adapterDelegationEvent);

        verify(adapterDelegationAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(beforeBuilderAnswer.answer(null)));

        verify(adapterDelegationAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(afterReturningBuilderAnswer.answer(null)), any(Object.class));
    }



    @Test
    public void verifyInboundProcessingEvent() throws Throwable {

        advice.logInboundProcessingEvent(mockJoinPoint, inboundProcessingEvent);

        verify(inboundProcessingAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(beforeBuilderAnswer.answer(null)));

        verify(inboundProcessingAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(afterReturningBuilderAnswer.answer(null)), any(Object.class));
    }



    @Test
    public void verifyOutboundProcessingEvent() throws Throwable {

        advice.logOutboundProcessingEvent(mockJoinPoint, outboundProcessingEvent);

        verify(outboundProcessingAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(beforeBuilderAnswer.answer(null)));

        verify(outboundProcessingAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(afterReturningBuilderAnswer.answer(null)), any(Object.class));
    }

    @Test
    public void verifyNwhinInvocationEvent() throws Throwable {

        advice.logNwhinInvocationEvent(mockJoinPoint, nwhinInvocationEvent);

        verify(nwhinInvocationAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(beforeBuilderAnswer.answer(null)));

        verify(nwhinInvocationAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(afterReturningBuilderAnswer.answer(null)), any(Object.class));
    }

    @Test
    public void verifyAroundAnnotations() {
            ArrayList<Class> failures = new ArrayList<>();
            for (Class clazz : annotations) {
                if (!verifyAnnotation(clazz)) {
                    failures.add(clazz);
                }
            }

            if (failures.size() > 0) {
                fail("Failure to verify annotations and methods for events: " + failures.toString());
            }
    }

    @Test
    public void verifyAnnotationThrowsException() throws Throwable {
        ArrayList<Class> failures = new ArrayList<>();
        for (final Class clazz : annotations) {
            if (!verifyException(clazz)) {
                failures.add(clazz);
            }
        }

        if (failures.size() > 0) {
            fail("Method does not throw exception for annotations: " + failures.toString());
        }
    }

    @SuppressWarnings("unused")
    @Test
    public void testExceptionThrowing() throws Throwable {
        boolean thrown = false;
        when(mockJoinPoint.proceed()).thenThrow(new IllegalStateException(EXCEPTION_MESSAGE));

        ArgumentCaptor<JoinPoint> joinPointCaptor = ArgumentCaptor.forClass(JoinPoint.class);
        ArgumentCaptor<Throwable> exceptionCaptor = ArgumentCaptor.forClass(Throwable.class);
        ArgumentCaptor<String> serviceCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> versionCaptor = ArgumentCaptor.forClass(String.class);

        try {
            advice.logEvents(mockJoinPoint, inboundMessageAdviceDelegate, DefaultEventDescriptionBuilder.class,
                BaseEventDescriptionBuilder.class, serviceType, version);
        } catch (Throwable e) {
           thrown = true;
        }

        verify(inboundMessageAdviceDelegate).begin(any(Object[].class), eq(serviceType), eq(version),
            eq(beforeBuilderAnswer.answer(null)));

        Mockito.verifyNoMoreInteractions(inboundMessageAdviceDelegate);

        verify(advice).logFailure(joinPointCaptor.capture(), exceptionCaptor.capture(),
            serviceCaptor.capture(), versionCaptor.capture());

        Throwable e = exceptionCaptor.getValue();
        assertTrue(e instanceof IllegalStateException);
        IllegalStateException exception = (IllegalStateException) e;
        assertTrue(EXCEPTION_MESSAGE.equals(exception.getMessage()));

        assertEquals(serviceType, serviceCaptor.getValue());
        assertEquals(version, versionCaptor.getValue());

        assertTrue(thrown);
    }

    @Test
    public void testEventExceptionOverride() throws Throwable {
        when(mockJoinPoint.proceed()).thenThrow(new ErrorEventException(new IllegalStateException(EXCEPTION_MESSAGE), OVERRIDE, CUSTOM_MESSAGE));
        ArgumentCaptor<JoinPoint> joinPointCaptor = ArgumentCaptor.forClass(JoinPoint.class);
        ArgumentCaptor<Throwable> exceptionCaptor = ArgumentCaptor.forClass(Throwable.class);
        ArgumentCaptor<String> serviceCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> versionCaptor = ArgumentCaptor.forClass(String.class);

        advice.logEvents(mockJoinPoint, inboundMessageAdviceDelegate, DefaultEventDescriptionBuilder.class,
            BaseEventDescriptionBuilder.class, serviceType, version);

        verify(inboundMessageAdviceDelegate).begin(any(Object[].class), eq(serviceType), eq(version),
            eq(beforeBuilderAnswer.answer(null)));

        Mockito.verifyNoMoreInteractions(inboundMessageAdviceDelegate);

        verify(advice).logFailure(joinPointCaptor.capture(), exceptionCaptor.capture(),
            serviceCaptor.capture(), versionCaptor.capture());

        Throwable e = exceptionCaptor.getValue();
        assertTrue(e instanceof ErrorEventException);
        ErrorEventException exception = (ErrorEventException) e;
        assertTrue(OVERRIDE.equals(exception.getReturnOverride()));
        assertTrue(CUSTOM_MESSAGE.equals(exception.getMessage()));
        assertTrue(exception.getCause() instanceof IllegalStateException);
        assertTrue(EXCEPTION_MESSAGE.equals(exception.getCause().getMessage()));

        assertEquals(serviceType, serviceCaptor.getValue());
        assertEquals(version, versionCaptor.getValue());

    }



    @SuppressWarnings({ "unused", "static-method" })
    private boolean verifyAnnotation(Class annotation) {
        try {
            Method method = EventAspectAdvice.class.getMethod("log" + annotation.getSimpleName(), ProceedingJoinPoint.class, annotation);
            Around anno = method.getAnnotation(Around.class);
            assertNotNull(anno);
        } catch (NoSuchMethodException | SecurityException e) {
            return false;
        }
        return true;
    }

    @SuppressWarnings({ "unused", "static-method" })
    private boolean verifyException(Class annotation) {
        try {
            Method method = EventAspectAdvice.class.getMethod("log" + annotation.getSimpleName(), ProceedingJoinPoint.class, annotation);
            Class<?>[] exceptions = method.getExceptionTypes();

            // Is it marked as throwing a throwable?
            return exceptions.length != 0;

        } catch (NoSuchMethodException | SecurityException e) {
           //do nothing. We don't care. Fail.
        }

        return false;
    }

}
