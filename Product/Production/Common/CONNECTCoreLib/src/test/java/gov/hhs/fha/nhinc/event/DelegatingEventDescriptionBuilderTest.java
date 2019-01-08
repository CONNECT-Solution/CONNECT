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
package gov.hhs.fha.nhinc.event;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DelegatingEventDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    private DelegatingEventDescriptionBuilder realMethodsBuilder;

    @Before
    public void before() {
        realMethodsBuilder = mock(DelegatingEventDescriptionBuilder.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void buildMethodsDelegate() {
        EventDescriptionBuilder mockDelegate = mock(EventDescriptionBuilder.class);
        EventDescription eventDescription = mock(EventDescription.class);
        when(mockDelegate.getEventDescription()).thenReturn(eventDescription);
        realMethodsBuilder.setDelegate(mockDelegate);

        EventDescription result = getEventDescription(realMethodsBuilder);
        assertEquals(eventDescription, result);

        verify(mockDelegate).buildTimeStamp();
        verify(mockDelegate).buildStatuses();
        verify(mockDelegate).buildRespondingHCIDs();
        verify(mockDelegate).buildPayloadSizes();
        verify(mockDelegate).buildPayloadTypes();
        verify(mockDelegate).buildNPI();
        verify(mockDelegate).buildInitiatingHCID();
        verify(mockDelegate).buildErrorCodes();
        verify(mockDelegate).buildMessageId();
        verify(mockDelegate).buildTransactionId();
        verify(mockDelegate).buildServiceType();
        verify(mockDelegate).buildResponseMsgIdList();
        verify(mockDelegate).buildVersion();
    }

    @Test
    public void messageArgs() {
        EventContextAccessor contextAccessor = mock(EventContextAccessor.class);
        MessageRoutingAccessor routingAccessor = mock(MessageRoutingAccessor.class);
        realMethodsBuilder.setMsgContext(contextAccessor);
        realMethodsBuilder.setMsgRouting(routingAccessor);

        EventDescriptionBuilder mockDelegate = mock(EventDescriptionBuilder.class);
        realMethodsBuilder.setDelegate(mockDelegate);

        verify(mockDelegate).setMsgContext(any(EventContextAccessor.class));
        verify(mockDelegate).setMsgRouting(eq(routingAccessor));
    }
}
