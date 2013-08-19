/*
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
package gov.hhs.fha.nhinc.callback.cxf.largefile;

import gov.hhs.fha.nhinc.largefile.LargeFileUtils;

import java.util.Collection;

import javax.activation.DataSource;

import org.apache.cxf.attachment.AttachmentDataSource;
import org.apache.cxf.message.Attachment;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * This fault out interceptor closes out all the attachment input sources to ensure that streamed tmp files are deleted
 * properly.
 * 
 * @author akong
 * 
 */
public class AttachmentReleaseFaultOutInterceptor extends AbstractPhaseInterceptor<Message> {

    /**
     * Constructor.
     */
    public AttachmentReleaseFaultOutInterceptor() {
        super(Phase.SETUP_ENDING);
    }

    /**
     * Releases and closes all incoming attachment input streams. This is a workaround for a CXF bug that is not
     * deleting tmp files when a soap fault occurs during file transfers.
     * 
     * @param message The message object of the session
     */
    public void handleMessage(Message message) {
        Collection<Attachment> attachments = message.getExchange().getInMessage().getAttachments();
        if (attachments != null) {
            for (Attachment attachment : attachments) {
                DataSource ds = getDataSource(attachment);
                if (ds instanceof AttachmentDataSource) {
                    AttachmentDataSource ads = (AttachmentDataSource) ds;
                    ads.release();
                    getLargeFileUtils().closeStreamWithoutException(ads.getInputStream());
                }
            }
        }
    }

    private DataSource getDataSource(Attachment attachment) {
        if ((attachment != null) && (attachment.getDataHandler() != null)) {
            return attachment.getDataHandler().getDataSource();
        }

        return null;
    }
    
    protected LargeFileUtils getLargeFileUtils(){
    	return LargeFileUtils.getInstance();
    }

}
