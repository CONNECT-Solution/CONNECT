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
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.logging.transaction.TransactionStore;
import gov.hhs.fha.nhinc.logging.transaction.factory.TransactionStoreFactory;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.xml.ws.WebServiceContext;
import org.junit.Test;

public class SOAPMessageRoutingAccessorTest {

    private static final String MESSAGE_ID = UUID.randomUUID().toString();
    private static final String RELATES_TO = UUID.randomUUID().toString();
    private static final String TRANSACTION_ID = UUID.randomUUID().toString();

    @Test
    public void nullMessageContext() {
        WebServiceContext context = mock(WebServiceContext.class);
        AsyncMessageIdExtractor extractor = mock(AsyncMessageIdExtractor.class);
        TransactionStoreFactory factory = mock(TransactionStoreFactory.class);
        SOAPMessageRoutingAccessor accessor = new SOAPMessageRoutingAccessor(context, extractor, factory);
        List<String> msgIdList = accessor.getResponseMsgIdList();
        assertNull(msgIdList);
    }

    @Test
    public void testWithRelatesTo() {
        WebServiceContext context = mock(WebServiceContext.class);
        AsyncMessageIdExtractor extractor = mock(AsyncMessageIdExtractor.class);
        TransactionStoreFactory factory = mock(TransactionStoreFactory.class);
        SOAPMessageRoutingAccessor accessor = new SOAPMessageRoutingAccessor(context, extractor, factory);

        TransactionStore store = mock(TransactionStore.class);
        when(extractor.getMessageId(any(WebServiceContext.class))).thenReturn(MESSAGE_ID);
        when(extractor.getAsyncRelatesTo(any(WebServiceContext.class)))
                .thenReturn(Collections.singletonList(RELATES_TO));
        when(factory.getTransactionStore()).thenReturn(store);
        when(store.getTransactionId(eq(RELATES_TO))).thenReturn(TRANSACTION_ID);

        assertEquals(TRANSACTION_ID, accessor.getTransactionId());
    }
}
