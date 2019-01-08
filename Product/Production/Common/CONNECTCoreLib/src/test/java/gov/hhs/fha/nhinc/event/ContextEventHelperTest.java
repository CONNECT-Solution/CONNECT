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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.logging.transaction.TransactionStore;
import gov.hhs.fha.nhinc.logging.transaction.factory.TransactionStoreFactory;
import javax.xml.ws.WebServiceContext;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.junit.Test;

public class ContextEventHelperTest {

    @Test
    public void defaultsToMagicImpls() {
        ContextEventHelper helper = new ContextEventHelper();
        WebServiceContext context = helper.getContext();
        assertTrue(context instanceof WebServiceContextImpl);

        AsyncMessageIdExtractor extractor = helper.getExtractor();
        assertNotNull(extractor);
    }

    @Test
    public void getsMessageIdFromContext() {
        AsyncMessageIdExtractor mockExtractor = mock(AsyncMessageIdExtractor.class);
        when(mockExtractor.getMessageId(any(WebServiceContext.class))).thenReturn("messageId");

        ContextEventHelper helper = new ContextEventHelper();
        helper.setAsyncMessageIdExtractor(mockExtractor);

        String messageId = helper.getMessageId();
        assertEquals("messageId", messageId);
    }

    @Test
    public void getsTransactionIdFromContext() {
        final AsyncMessageIdExtractor mockExtractor = mock(AsyncMessageIdExtractor.class);
        final TransactionStoreFactory mockFactory = mock(TransactionStoreFactory.class);
        TransactionStore store = mock(TransactionStore.class);

        when(mockExtractor.getAsyncRelatesTo(any(WebServiceContext.class))).thenReturn(ImmutableList.of("relatesto"));
        when(mockFactory.getTransactionStore()).thenReturn(store);
        when(store.getTransactionId("relatesto")).thenReturn("transactionId");

        ContextEventHelper helper = new ContextEventHelper();
        helper.setAsyncMessageIdExtractor(mockExtractor);
        helper.setTransactionStoreFacotyr(mockFactory);

        String transactionId = helper.getTransactionId();
        assertEquals("transactionId", transactionId);
    }

    // TODO: missing exhaustive tests for getTransactionId
}
