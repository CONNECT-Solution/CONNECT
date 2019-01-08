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
package gov.hhs.fha.nhinc.callback.cxf.largefile;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import javax.activation.DataHandler;
import org.apache.cxf.attachment.AttachmentDataSource;
import org.apache.cxf.message.Attachment;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.junit.Test;

public class AttachmentReleaseFaultOutInterceptorTest {

    @Test
    public void testHandleMessage() {
        final LargeFileUtils largeFileUtils = mock(LargeFileUtils.class);
        Message message = mock(Message.class);
        Exchange exchange = mock(Exchange.class);
        Collection<Attachment> attachments = new ArrayList<>();
        Attachment attachment = mock(Attachment.class);
        attachments.add(attachment);
        DataHandler dataHandler = mock(DataHandler.class);
        AttachmentDataSource dataSource = mock(AttachmentDataSource.class);

        AttachmentReleaseFaultOutInterceptor interceptor = new AttachmentReleaseFaultOutInterceptor() {
            @Override
            protected LargeFileUtils getLargeFileUtils() {
                return largeFileUtils;
            }
        };

        when(message.getExchange()).thenReturn(exchange);
        when(exchange.getInMessage()).thenReturn(message);
        when(message.getAttachments()).thenReturn(attachments);

        when(attachment.getDataHandler()).thenReturn(dataHandler);
        when(dataHandler.getDataSource()).thenReturn(dataSource);

        interceptor.handleMessage(message);

        verify(dataSource).release();
        verify(largeFileUtils).closeStreamWithoutException(any(InputStream.class));
    }

    @Test
    public void testGetLargeFileUtils() {
        AttachmentReleaseFaultOutInterceptor interceptor = new AttachmentReleaseFaultOutInterceptor();
        assertTrue(interceptor.getLargeFileUtils() instanceof LargeFileUtils);
    }
}
