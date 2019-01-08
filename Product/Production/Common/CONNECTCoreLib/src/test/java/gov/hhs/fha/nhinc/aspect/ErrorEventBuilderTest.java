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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.event.ContextEventHelper;
import gov.hhs.fha.nhinc.event.Event;
import gov.hhs.fha.nhinc.event.EventDirector;
import gov.hhs.fha.nhinc.event.error.MessageProcessingFailedEvent;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class ErrorEventBuilderTest {

    private ErrorEventBuilder builder;

    @Before
    public void before() {
        builder = new ErrorEventBuilder();
    }

    @Test
    public void eventWithoutThrowable() throws JSONException {

        ContextEventHelper helper = mock(ContextEventHelper.class);
        AssertionType assertion = new AssertionType();
        assertion.setMessageId("messageId");
        builder.setContextHelper(helper);
        when(helper.getMessageId(assertion)).thenReturn("messageId");
        when(helper.getTransactionId()).thenReturn("transactionId");

        Event event =  EventDirector.constructEvent(builder);

        assertNotNull(event);
        JSONAssert.assertEquals("{}", event.getDescription(), true);
        assertEquals(MessageProcessingFailedEvent.EVENT_NAME, event.getEventName());
    }

    @Test
    public void defaultContextHelper() {
        assertNotNull(builder.getContextHelper());
    }

    @Test
    public void delegatesToContextHelper() {
        ContextEventHelper helper = mock(ContextEventHelper.class);
        AssertionType assertion = new AssertionType();
        assertion.setMessageId("messageId");
        builder.setContextHelper(helper);
        builder.setAssertion(assertion);
        when(helper.getMessageId(assertion)).thenReturn("messageId");
        when(helper.getTransactionId()).thenReturn("transactionId");

        builder.buildMessageID();
        builder.buildTransactionID();

        assertEquals("messageId", builder.getEvent().getMessageID());
        assertEquals("transactionId", builder.getEvent().getTransactionID());
    }

    @Test
    public void descriptionFromThrowable() throws JSONException {
        Throwable t = mock(Throwable.class);
        when(t.getStackTrace()).thenReturn(new StackTraceElement[0]);
        when(t.getMessage()).thenReturn("message");
        builder.setThrowable(t);

        builder.buildDescription();

        Event event = builder.getEvent();
        assertNotNull(event.getDescription());
        assertEquals(MessageProcessingFailedEvent.EVENT_NAME, event.getEventName());
        JSONAssert.assertEquals("{\"exceptionMessage\":\"message\", \"exceptionClass\": \"" + t.getClass().getName() + "\", \"stackTrace\":[]}", event.getDescription(), true);
    }

    @Test
    public void descriptionFromInvoker() throws JSONException {

        builder.setInvoker("invokerOfMethod");
        builder.setMethod("methodOfFailure");
        builder.buildDescription();

        Event event = builder.getEvent();
        assertNotNull(event.getDescription());
        assertEquals(MessageProcessingFailedEvent.EVENT_NAME, event.getEventName());
        JSONAssert.assertEquals("{\"failedClass\":\"invokerOfMethod\", \"failedMethod\": \"methodOfFailure\"}", event.getDescription(), true);
    }

    @Test
    public void nullMessage() throws JSONException {
        Throwable t = mock(Throwable.class);
        when(t.getStackTrace()).thenReturn(new StackTraceElement[0]);
        builder.setThrowable(t);

        builder.buildDescription();

        Event event = builder.getEvent();
        assertNotNull(event.getDescription());
        assertEquals(MessageProcessingFailedEvent.EVENT_NAME, event.getEventName());
        JSONAssert.assertEquals("{\"exceptionMessage\":\"N/A\",\"exceptionClass\": \"" + t.getClass().getName() + "\", \"stackTrace\":[]}", event.getDescription(), true);
    }
}
