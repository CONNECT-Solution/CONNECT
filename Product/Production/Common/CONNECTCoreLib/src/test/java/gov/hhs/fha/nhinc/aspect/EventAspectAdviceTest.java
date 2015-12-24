/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.DefaultEventDescriptionBuilder;
import org.aspectj.lang.JoinPoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author bhumphrey
 *
 */
public class EventAspectAdviceTest {

    private EventAspectAdvice advice = new EventAspectAdvice();
    private EventAdviceDelegate adapterDelegationAdviceDelegate = mock(EventAdviceDelegate.class);
    private EventAdviceDelegate inboundMessageAdviceDelegate = mock(EventAdviceDelegate.class);
    private EventAdviceDelegate inboundProcessingAdviceDelegate = mock(EventAdviceDelegate.class);
    private EventAdviceDelegate nwhinInvocationAdviceDelegate = mock(EventAdviceDelegate.class);
    private EventAdviceDelegate outboundMessageAdviceDelegate = mock(EventAdviceDelegate.class);
    private EventAdviceDelegate outboundProcessingAdviceDelegate = mock(EventAdviceDelegate.class);
    private FailureAdviceDelegate failureAdviceDelegate = mock(FailureAdviceDelegate.class);
    private JoinPoint mockJoinPoint = mock(JoinPoint.class);
    private Object mockReturnValue = mock(Object.class);

    private Answer<String> serviceTypeAnswer = new Answer<String>() {
        @Override
        public String answer(InvocationOnMock invocation) throws Throwable {
            return "test-serviceType";
        }
    };

    Answer<String> versionAnswer = new Answer<String>() {
        @Override
        public String answer(InvocationOnMock invocation) throws Throwable {
            return "test-version";
        }
    };

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
    public void setupMocks() {

        advice.setAdapterDelegationAdviceDelegate(adapterDelegationAdviceDelegate);
        advice.setInboundMessageAdviceDelegate(inboundMessageAdviceDelegate);
        advice.setInboundProcessingAdviceDelegate(inboundProcessingAdviceDelegate);
        advice.setNwhinInvocationAdviceDelegate(nwhinInvocationAdviceDelegate);
        advice.setOutboundMessageAdviceDelegate(outboundMessageAdviceDelegate);
        advice.setOutboundProcessingAdviceDelegate(outboundProcessingAdviceDelegate);
        advice.setFailureAdviceDelegate(failureAdviceDelegate);
        when(mockJoinPoint.getArgs()).then(new Answer<Object[]>() {

            @Override
            public Object[] answer(InvocationOnMock invocation) throws Throwable {
                return new Object[]{};
            }
        });
    }

    @After
    public void cleanUpMocks() {
        reset(mockJoinPoint);
    }

    @Test
    public void verifyInboundMessageEvent() throws Throwable {
        InboundMessageEvent mockAnnotation = mock(InboundMessageEvent.class);

        when(mockAnnotation.serviceType()).then(serviceTypeAnswer);
        when(mockAnnotation.version()).then(versionAnswer);
        when(mockAnnotation.beforeBuilder()).then(beforeBuilderAnswer);
        when(mockAnnotation.afterReturningBuilder()).then(afterReturningBuilderAnswer);

        advice.beginInboundMessageEvent(mockJoinPoint, mockAnnotation);

        verify(inboundMessageAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(beforeBuilderAnswer.answer(null)));

        advice.endInboundMessageEvent(mockJoinPoint, mockAnnotation, mockReturnValue);

        verify(inboundMessageAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(afterReturningBuilderAnswer.answer(null)), any(Object.class));
    }

    @Test
    public void verifyOutboundMessageEvent() throws Throwable {
        OutboundMessageEvent mockAnnotation = mock(OutboundMessageEvent.class);

        when(mockAnnotation.serviceType()).then(serviceTypeAnswer);
        when(mockAnnotation.version()).then(versionAnswer);
        when(mockAnnotation.beforeBuilder()).then(beforeBuilderAnswer);
        when(mockAnnotation.afterReturningBuilder()).then(afterReturningBuilderAnswer);

        advice.beginOutboundMessageEvent(mockJoinPoint, mockAnnotation);

        verify(outboundMessageAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(beforeBuilderAnswer.answer(null)));

        advice.endOutboundMessageEvent(mockJoinPoint, mockAnnotation, mockReturnValue);

        verify(outboundMessageAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(afterReturningBuilderAnswer.answer(null)), any(Object.class));
    }

    @Test
    public void verifyAdapterDelegationEvent() throws Throwable {
        AdapterDelegationEvent mockAnnotation = mock(AdapterDelegationEvent.class);

        when(mockAnnotation.serviceType()).then(serviceTypeAnswer);
        when(mockAnnotation.version()).then(versionAnswer);
        when(mockAnnotation.beforeBuilder()).then(beforeBuilderAnswer);
        when(mockAnnotation.afterReturningBuilder()).then(afterReturningBuilderAnswer);

        advice.beginAdapterDelegationEvent(mockJoinPoint, mockAnnotation);

        verify(adapterDelegationAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(beforeBuilderAnswer.answer(null)));

        advice.endAdapterDelegationEvent(mockJoinPoint, mockAnnotation, mockReturnValue);

        verify(adapterDelegationAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(afterReturningBuilderAnswer.answer(null)), any(Object.class));
    }

    @Test
    public void verifyInboundProcessingEvent() throws Throwable {
        InboundProcessingEvent mockAnnotation = mock(InboundProcessingEvent.class);

        when(mockAnnotation.serviceType()).then(serviceTypeAnswer);
        when(mockAnnotation.version()).then(versionAnswer);
        when(mockAnnotation.beforeBuilder()).then(beforeBuilderAnswer);
        when(mockAnnotation.afterReturningBuilder()).then(afterReturningBuilderAnswer);

        advice.beginInboundProcessingEvent(mockJoinPoint, mockAnnotation);

        verify(inboundProcessingAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(beforeBuilderAnswer.answer(null)));

        advice.endInboundProcessingEvent(mockJoinPoint, mockAnnotation, mockReturnValue);

        verify(inboundProcessingAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(afterReturningBuilderAnswer.answer(null)), any(Object.class));
    }

    @Test
    public void verifyOutboundProcessingEvent() throws Throwable {
        OutboundProcessingEvent mockAnnotation = mock(OutboundProcessingEvent.class);

        when(mockAnnotation.serviceType()).then(serviceTypeAnswer);
        when(mockAnnotation.version()).then(versionAnswer);
        when(mockAnnotation.beforeBuilder()).then(beforeBuilderAnswer);
        when(mockAnnotation.afterReturningBuilder()).then(afterReturningBuilderAnswer);

        advice.beginOutboundProcessingEvent(mockJoinPoint, mockAnnotation);

        verify(outboundProcessingAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(beforeBuilderAnswer.answer(null)));

        advice.endOutboundProcessingEvent(mockJoinPoint, mockAnnotation, mockReturnValue);

        verify(outboundProcessingAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(afterReturningBuilderAnswer.answer(null)), any(Object.class));
    }

    @Test
    public void verifyNwhinInvocationEvent() throws Throwable {
        NwhinInvocationEvent mockAnnotation = mock(NwhinInvocationEvent.class);

        when(mockAnnotation.serviceType()).then(serviceTypeAnswer);
        when(mockAnnotation.version()).then(versionAnswer);
        when(mockAnnotation.beforeBuilder()).then(beforeBuilderAnswer);
        when(mockAnnotation.afterReturningBuilder()).then(afterReturningBuilderAnswer);

        advice.beginNwhinInvocationEvent(mockJoinPoint, mockAnnotation);

        verify(nwhinInvocationAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(beforeBuilderAnswer.answer(null)));

        advice.endNwhinInvocationEvent(mockJoinPoint, mockAnnotation, mockReturnValue);

        verify(nwhinInvocationAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"),
                eq(afterReturningBuilderAnswer.answer(null)), any(Object.class));
    }

    @Test
    public void verifyFailEvent() {
        Throwable mockThrowable = mock(Throwable.class);
        advice.failEvent(mockJoinPoint, mockThrowable);

        verify(failureAdviceDelegate).fail(any(Object[].class), eq(mockThrowable));
    }
}
