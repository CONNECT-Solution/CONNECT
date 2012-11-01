/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above
 *     copyright notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of the United States Government nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 *THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 *DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.aspect;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.aspectj.lang.JoinPoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
                Object [] ret = {};
                return ret;
            }});
    }
    
    @After
    public void cleanUpMocks() {
        reset(mockJoinPoint);
    }

    @Test
    public void verifyInboundMessageEvent() {
        InboundMessageEvent mockAnnotation = mock(InboundMessageEvent.class);

        when(mockAnnotation.serviceType()).then(serviceTypeAnswer);
        when(mockAnnotation.version()).then(versionAnswer);

        advice.beginInboundMessageEvent(mockJoinPoint, mockAnnotation);

        verify(inboundMessageAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"));
        
        advice.endInboundMessageEvent(mockJoinPoint, mockAnnotation);

        verify(inboundMessageAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"));
    }
    
    @Test
    public void verifyOutboundMessageEvent() {
        OutboundMessageEvent mockAnnotation = mock(OutboundMessageEvent.class);

        when(mockAnnotation.serviceType()).then(serviceTypeAnswer);
        when(mockAnnotation.version()).then(versionAnswer);

        advice.beginOutboundMessageEvent(mockJoinPoint, mockAnnotation);

        verify(outboundMessageAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"));
        
        advice.endOutboundMessageEvent(mockJoinPoint, mockAnnotation);

        verify(outboundMessageAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"));
    }
    
    @Test
    public void verifyAdapterDelegationEvent() {
        AdapterDelegationEvent mockAnnotation = mock(AdapterDelegationEvent.class);

        when(mockAnnotation.serviceType()).then(serviceTypeAnswer);
        when(mockAnnotation.version()).then(versionAnswer);

        advice.beginAdapterDelegationEvent(mockJoinPoint, mockAnnotation);

        verify(adapterDelegationAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"));
  
        advice.endAdapterDelegationEvent(mockJoinPoint, mockAnnotation);
        
        verify(adapterDelegationAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"));
    }
    
    @Test
    public void verifyInboundProcessingEvent() {
        InboundProcessingEvent mockAnnotation = mock(InboundProcessingEvent.class);

        when(mockAnnotation.serviceType()).then(serviceTypeAnswer);
        when(mockAnnotation.version()).then(versionAnswer);

        advice.beginInboundProcessingEvent(mockJoinPoint, mockAnnotation);

        verify(inboundProcessingAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"));
       
        advice.endInboundProcessingEvent(mockJoinPoint, mockAnnotation);
        
        verify(inboundProcessingAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"));
    }
    
    @Test
    public void verifyOutboundProcessingEvent() {
        OutboundProcessingEvent mockAnnotation = mock(OutboundProcessingEvent.class);

        when(mockAnnotation.serviceType()).then(serviceTypeAnswer);
        when(mockAnnotation.version()).then(versionAnswer);

        advice.beginOutboundProcessingEvent(mockJoinPoint, mockAnnotation);

        verify(outboundProcessingAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"));
      
        advice.endOutboundProcessingEvent(mockJoinPoint, mockAnnotation);
        
        verify(outboundProcessingAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"));
    }
    
    @Test
    public void verifyNwhinInvocationEvent() {
        NwhinInvocationEvent mockAnnotation = mock(NwhinInvocationEvent.class);
        
        when(mockAnnotation.serviceType()).then(serviceTypeAnswer);
        when(mockAnnotation.version()).then(versionAnswer);
        
        advice.beginNwhinInvocationEvent(mockJoinPoint, mockAnnotation);
        
        verify(nwhinInvocationAdviceDelegate).begin(any(Object[].class), eq("test-serviceType"), eq("test-version"));
       
        advice.endNwhinInvocationEvent(mockJoinPoint, mockAnnotation);
        
        verify(nwhinInvocationAdviceDelegate).end(any(Object[].class), eq("test-serviceType"), eq("test-version"));
    }
    @Test
    public void verifyFailEvent() {
        
        advice.failEvent(mockJoinPoint);
        
        verify(failureAdviceDelegate).fail(any(Object[].class));
    }
}
