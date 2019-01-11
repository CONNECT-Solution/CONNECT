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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import gov.hhs.fha.nhinc.event.Event;
import gov.hhs.fha.nhinc.event.EventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.EventRecorder;
import org.junit.Test;
import org.mockito.Mockito;

public class BaseEventAdviceDelegateTest {

    @Test
    public void beginNoEventRecorder() {
        BaseEventAdviceDelegate delegate = createAdviceDelegate(null);
        delegate.begin(new Object[] {}, "", "", EventDescriptionBuilder.class);
    }

    @Test
    public void beginShouldntCallRecordIfEventRecorderNotEnabled() {
        EventRecorder mockEventRecorder = mock(EventRecorder.class);
        BaseEventAdviceDelegate delegate = createAdviceDelegate(mockEventRecorder);
        delegate.begin(new Object[] {}, "", "", EventDescriptionBuilder.class);
        assertEventRecorderInteractions(mockEventRecorder);
    }

    @Test
    public void endNoEventRecorder() {
        BaseEventAdviceDelegate delegate = createAdviceDelegate(null);
        delegate.end(new Object[] {}, "", "", EventDescriptionBuilder.class, null);
    }

    @Test
    public void endShouldntCallRecordIfEventRecorderNotEnabled() {
        EventRecorder mockEventRecorder = mock(EventRecorder.class);
        BaseEventAdviceDelegate delegate = createAdviceDelegate(mockEventRecorder);
        delegate.end(new Object[] {}, "", "", EventDescriptionBuilder.class, null);
        assertEventRecorderInteractions(mockEventRecorder);
    }

    @Test
    public void failNoEventRecorder() {
        BaseEventAdviceDelegate delegate = createAdviceDelegate(null);
        delegate.fail(new Object[] {}, mock(Throwable.class));
    }

    @Test
    public void failShouldntCallRecordIfEventRecorderNotEnabled() {
        EventRecorder mockEventRecorder = mock(EventRecorder.class);
        BaseEventAdviceDelegate delegate = createAdviceDelegate(mockEventRecorder);
        delegate.fail(new Object[] {}, mock(Throwable.class));
        assertEventRecorderInteractions(mockEventRecorder);
    }

    private BaseEventAdviceDelegate createAdviceDelegate(EventRecorder mockEventRecorder) {
        BaseEventAdviceDelegate delegate = mock(BaseEventAdviceDelegate.class, Mockito.CALLS_REAL_METHODS);
        delegate.setEventRecorder(mockEventRecorder);
        return delegate;
    }

    private void assertEventRecorderInteractions(EventRecorder mockEventRecorder) {
        verify(mockEventRecorder).isRecordEventEnabled();
        verify(mockEventRecorder, times(0)).recordEvent(any(Event.class));
    }
}
