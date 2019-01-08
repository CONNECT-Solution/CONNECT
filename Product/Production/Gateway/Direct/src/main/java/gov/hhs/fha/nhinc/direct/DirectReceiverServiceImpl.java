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
package gov.hhs.fha.nhinc.direct;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author svalluripalli
 */
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class DirectReceiverServiceImpl extends DirectAdapterEntity implements DirectReceiverPortType, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(DirectReceiverServiceImpl.class);

    @Override
    public void receiveInbound(ConnectCustomMimeMessage message) {
        LOG.debug("-- Begin DirectReceiverServiceImpl.receiveInbound() --");
        try {
            InternetHeaders headers = new InternetHeaders();
            for (HeaderMap aHeader : message.getHeadersList()) {
                headers.addHeader(aHeader.getKey(), aHeader.getValue());
            }
            Properties prop = new Properties();
            prop.put("mail.imaps.partialfetch", false);
            Session session = Session.getDefaultInstance(prop, null);
            byte[] content = message.getContent().getBytes();
            MimeBodyPart prt = new MimeBodyPart(headers, content);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            prt.writeTo(out);
            MimeMessage mimeMessage = new MimeMessage(session, new ByteArrayInputStream(out.toByteArray()));
            mimeMessage.setFrom(new InternetAddress(message.getSender()));
            int receipientCount = message.getReceipients().size();
            InternetAddress[] addressTo = new InternetAddress[receipientCount];
            for (int i = 0; i < receipientCount; i++) {
                addressTo[i] = new InternetAddress(message.getReceipients().get(i));
            }
            mimeMessage.setRecipients(RecipientType.TO, addressTo);
            mimeMessage.setSubject(message.getSubject());
            getDirectReceiver().receiveInbound(mimeMessage);
        }catch (MessagingException | IOException ex) {
            LOG.error(ex.getLocalizedMessage(),ex);
        }
        LOG.debug("-- End DirectReceiverServiceImpl.receiveInbound() --");
    }
}
