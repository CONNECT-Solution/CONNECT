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
package gov.hhs.fha.nhinc.async;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author JHOPPESC- EDITED by sjarral
 */
public class AsyncMessageHandler implements SOAPHandler<SOAPMessageContext> {

    private static final String WSA_PREFIX = "wsa";
    private static final String WSA_NS = "http://www.w3.org/2005/08/addressing";

    private static final Logger LOG = LoggerFactory.getLogger(AsyncMessageHandler.class);

    @Override
    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }

    @Override
    public boolean handleMessage(SOAPMessageContext messageContext) {
        Boolean outboundProperty = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        try {
            SOAPMessage oMessage = messageContext.getMessage();
            SOAPHeader oHeader = oMessage.getSOAPHeader();

            if (outboundProperty) {
                if (messageContext.containsKey(NhincConstants.ASYNC_MSG_TYPE_PROP) == true) {
                    String msgType = (String) messageContext.get(NhincConstants.ASYNC_MSG_TYPE_PROP);

                    if (msgType.contentEquals(NhincConstants.ASYNC_REQUEST_MSG_TYPE_VAL)) {
                        LOG.debug("Detected an asynchronous request message");
                        // Override the Message Id field

                        if (messageContext.containsKey(NhincConstants.ASYNC_MESSAGE_ID_PROP) == true) {
                            String messageId = (String) messageContext.get(NhincConstants.ASYNC_MESSAGE_ID_PROP);

                            LOG.debug("Setting message ID to {}", messageId);

                            // Steps that need to be performed
                            SOAPElement oMessageIdElem = getFirstChild(oHeader, "MessageID", WSA_NS);
                            if (oMessageIdElem != null) {
                                oMessageIdElem.setTextContent(messageId);
                            }
                        }
                    } else if (msgType.contentEquals(NhincConstants.ASYNC_RESPONSE_MSG_TYPE_VAL)) {
                        LOG.debug("Detected an asynchronous response message");
                        // Override the Relates To Id field

                        if (messageContext.containsKey(NhincConstants.ASYNC_RELATES_TO_PROP) == true) {
                            String relatesToId = (String) messageContext.get(NhincConstants.ASYNC_RELATES_TO_PROP);

                            LOG.debug("Setting relates to ID to {}", relatesToId);

                            // Steps that need to be performed
                            SOAPElement relatesToElem = oHeader.addChildElement("RelatesTo", WSA_PREFIX, WSA_NS);
                            relatesToElem.setTextContent(relatesToId);
                        }
                    } else {
                        LOG.debug("Detected an synchronous request message");
                    }
                } else {
                    LOG.debug("Detected an synchronous request message");
                }

            } else {
                // Do nothing for an inbound message
            }
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
        }

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
        // Do nothing
    }

    private static SOAPElement getFirstChild(SOAPHeader header, String name, String ns) {
        SOAPElement result = null;
        QName qname = new QName(ns, name);
        Iterator iter = header.getChildElements(qname);
        if (iter.hasNext()) {
            result = (SOAPElement) iter.next();
        }
        return result;
    }
}
