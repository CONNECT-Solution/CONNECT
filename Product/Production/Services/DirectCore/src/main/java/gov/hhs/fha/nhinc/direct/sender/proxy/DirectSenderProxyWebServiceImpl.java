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
package gov.hhs.fha.nhinc.direct.sender.proxy;

import gov.hhs.fha.nhinc.direct.ConnectCustomSendMimeMessage;
import gov.hhs.fha.nhinc.direct.DirectConstants;
import gov.hhs.fha.nhinc.direct.DirectSenderPortType;
import gov.hhs.fha.nhinc.direct.DirectSenderService;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.SOAPBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author svalluripalli
 */
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class DirectSenderProxyWebServiceImpl {

    private static final Logger LOG = LoggerFactory.getLogger(DirectSenderProxyWebServiceImpl.class);
    private static final WebServiceProxyHelper HELPER = new WebServiceProxyHelper();

    /**
     *
     * @param message
     */
    public void sendOutboundDirect(MimeMessage message) {
        LOG.debug("Begin DirectSenderUnsecuredProxy.sendOutboundDirect(MimeMessage)");

        try { // Call Web Service Operation
            String url = HELPER.getAdapterEndPointFromConnectionManager(DirectConstants.DIRECT_SENDER_SERVICE_NAME);
            DirectSenderPortType port = getPort(url);
            gov.hhs.fha.nhinc.direct.SendoutMessage parameters = new gov.hhs.fha.nhinc.direct.SendoutMessage();
            ConnectCustomSendMimeMessage senderMessage = new ConnectCustomSendMimeMessage();
            senderMessage.setContent((byte[]) message.getContent());
            InternetAddress senderAdds = (InternetAddress) message.getSender();
            senderMessage.setSender(senderAdds.getAddress());
            senderMessage.setSubject(message.getSubject());
            parameters.setMessage(senderMessage);
            port.sendOutboundDirect(parameters);
        } catch (IOException | MessagingException | ExchangeManagerException ex) {
            LOG.error("DirectSender WebService Failed : {}", ex.getLocalizedMessage(), ex);
        }
        LOG.debug("End DirectSenderUnsecuredProxy.sendOutboundDirect(MimeMessage)");
    }

    private static DirectSenderPortType getPort(String url) {
        DirectSenderService service = new DirectSenderService();
        DirectSenderPortType port = service.getDirectSenderPortType();
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        return port;
    }
}
