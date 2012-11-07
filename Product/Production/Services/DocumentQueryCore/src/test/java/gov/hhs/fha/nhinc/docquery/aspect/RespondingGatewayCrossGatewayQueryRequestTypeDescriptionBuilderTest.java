/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
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
package gov.hhs.fha.nhinc.docquery.aspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.event.BaseDescriptionBuilderTest;
import gov.hhs.fha.nhinc.event.EventContextAccessor;
import gov.hhs.fha.nhinc.event.EventDescription;
import gov.hhs.fha.nhinc.event.MessageRoutingAccessor;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import org.junit.Before;
import org.junit.Test;

public class RespondingGatewayCrossGatewayQueryRequestTypeDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    private RespondingGatewayCrossGatewayQueryRequestTypeDescriptionBuilder builder;

    @Before
    public void before() {
        builder = new RespondingGatewayCrossGatewayQueryRequestTypeDescriptionBuilder();
    }

    @Test
    public void delegateCreated() {
        RespondingGatewayCrossGatewayQueryRequestType request = mock(RespondingGatewayCrossGatewayQueryRequestType.class);
        AdhocQueryRequest adhocMock = mock(AdhocQueryRequest.class);
        AssertionType mockAssertion = mock(AssertionType.class);
        when(request.getAdhocQueryRequest()).thenReturn(adhocMock);
        when(request.getAssertion()).thenReturn(mockAssertion);

        builder.setArguments(request);
        AdhocQueryRequestDescriptionBuilder delegate = builder.getDelegate();
        assertNotNull(delegate);
        assertEquals(adhocMock, delegate.getRequest().get());
        assertEquals(mockAssertion, delegate.getAssertion().get());
    }

    @Test
    public void buildMethodsDelegate() {
        AdhocQueryRequestDescriptionBuilder mockDelegate = mock(AdhocQueryRequestDescriptionBuilder.class);
        EventDescription eventDescription = mock(EventDescription.class);
        when(mockDelegate.getEventDescription()).thenReturn(eventDescription);
        builder.setDelegate(mockDelegate);

        EventDescription result = getEventDescription(builder);
        assertEquals(eventDescription, result);

        verify(mockDelegate).buildTimeStamp();
        verify(mockDelegate).buildStatuses();
        verify(mockDelegate).buildRespondingHCIDs();
        verify(mockDelegate).buildPayloadSize();
        verify(mockDelegate).buildPayloadTypes();
        verify(mockDelegate).buildNPI();
        verify(mockDelegate).buildInitiatingHCID();
        verify(mockDelegate).buildErrorCodes();
        verify(mockDelegate).buildMessageId();
        verify(mockDelegate).buildTransactionId();
        verify(mockDelegate).buildServiceType();
        verify(mockDelegate).buildResponseMsgIdList();
        verify(mockDelegate).buildAction();
    }

    @Test
    public void messageArgs() {
        EventContextAccessor contextAccessor = mock(EventContextAccessor.class);
        MessageRoutingAccessor routingAccessor = mock(MessageRoutingAccessor.class);
        builder.setMsgContext(contextAccessor);
        builder.setMsgRouting(routingAccessor);

        AdhocQueryRequestDescriptionBuilder mockDelegate = mock(AdhocQueryRequestDescriptionBuilder.class);
        builder.setDelegate(mockDelegate);

        verify(mockDelegate).setMsgContext(any(EventContextAccessor.class));
        verify(mockDelegate).setMsgRouting(eq(routingAccessor));
    }

    @Test
    public void returnValueDelegates() {
        AdhocQueryRequestDescriptionBuilder mockDelegate = mock(AdhocQueryRequestDescriptionBuilder.class);
        builder.setDelegate(mockDelegate);

        Object returnValue = new Object();
        builder.setReturnValue(returnValue);

        verify(mockDelegate).setReturnValue(returnValue);
    }
}
