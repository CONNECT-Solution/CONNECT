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
package gov.hhs.fha.nhinc.mail;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import org.junit.Test;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import org.mockito.stubbing.Stubber;

/**
 * Test {@link MailUtils}.
 */
public class MailUtilsTest {

    /**
     * Test {@link MailUtils#closeQuietly(Store)}.
     * @throws MessagingException messaging exception
     */
    @Test
    public void canCloseQuietlyOnStoreException() throws MessagingException {
        MailUtils.closeQuietly(getMockStore(true));
    }

    /**
     * Test {@link MailUtils#closeQuietly(Store, Folder, boolean)}.
     * @throws MessagingException messaging exception
     */
    @Test
    public void canCloseBothQuietlyOnStoreException() throws MessagingException {
        MailUtils.closeQuietly(getMockStore(true), getMockFolder(false), MailUtils.FOLDER_EXPUNGE_INBOX_TRUE);
    }

    /**
     * Test {@link MailUtils#closeQuietly(Store, Folder, boolean)}.
     * @throws MessagingException messaging exception
     */
    @Test
    public void canCloseBothQuietlyOnFolderException() throws MessagingException {
        MailUtils.closeQuietly(getMockStore(false), getMockFolder(true), MailUtils.FOLDER_EXPUNGE_INBOX_TRUE);
    }

    /**
     * Test {@link MailUtils#setDeletedQuietly(MimeMessage)}.
     * @throws MessagingException messaging exception
     */
    @Test
    public void canSetDeletedQuietlyOnException() throws MessagingException {
        MimeMessage mockMimeMsg = mock(MimeMessage.class);
        doThrowMessagingException().when(mockMimeMsg).setFlag(Flags.Flag.DELETED, true);
        MailUtils.setDeletedQuietly(mockMimeMsg);
    }

    private Store getMockStore(boolean throwOnClose) throws MessagingException {
        Store mockStore = mock(Store.class);
        if (throwOnClose) {
            doThrowMessagingException().when(mockStore).close();
        }
        return mockStore;
    }

    private Folder getMockFolder(boolean throwOnClose) throws MessagingException {
        Folder mockFolder = mock(Folder.class);
        if (throwOnClose) {
            doThrowMessagingException().when(mockFolder).close(anyBoolean());
        }
        return mockFolder;
    }

    private Stubber doThrowMessagingException() {
        return doThrow(new MessagingException("contrived exception"));
    }

}
