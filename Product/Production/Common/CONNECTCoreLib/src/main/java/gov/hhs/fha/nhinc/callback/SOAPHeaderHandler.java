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
package gov.hhs.fha.nhinc.callback;

import gov.hhs.fha.nhinc.async.AddressingHeaderCreator;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles various soap header values including adding mustUnderstand to action if missing and adding messageId if
 * missing and modifying it with appropriate prefix.
 *
 * @author rayj / jsmith
 */
public class SOAPHeaderHandler implements SOAPHandler<SOAPMessageContext> {

    private static final Logger LOG = LoggerFactory.getLogger(SOAPHeaderHandler.class);
    private static final String MESSAGE_ID_CONTEXT = "com.sun.xml.ws.addressing.response.messageID";

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
     */
    @Override
    public Set<QName> getHeaders() {
        LOG.debug("SoapHeaderHandler.getHeaders");
        return Collections.emptySet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.ws.handler.Handler#handleMessage(javax.xml.ws.handler.MessageContext)
     */
    @Override
    public boolean handleMessage(SOAPMessageContext messageContext) {
        LOG.debug("Entering SOAPHeaderHandler.handleMessage");
        Boolean isOutboundMessage = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        try {
            SOAPMessage oMessage = messageContext.getMessage();
            SOAPHeader oHeader = oMessage.getSOAPHeader();

            if (isOutboundMessage && !messageContext.containsKey(MESSAGE_ID_CONTEXT)) {
                adjustMessageId(messageContext, oHeader);
            } else {
                LOG.debug("Will not adjust messageID on inbound request");
            }

            if (isOutboundMessage) {
                addMustUnderstandAttribute(oHeader);
            }
        } catch (SOAPException e) {
            LOG.error("Unable to handle message: {}", e.getLocalizedMessage());
            LOG.trace("Unable to handle message: {}", e.getLocalizedMessage(), e);
        }

        return true;
    }

    /**
     * This method updates the messageID if found to be in an illegal format.
     *
     * @param messageContext
     * @param oHeader
     * @throws SOAPException
     */
    private void adjustMessageId(SOAPMessageContext messageContext, SOAPHeader oHeader) throws SOAPException {
        // Override the Message Id field
        String messageId;
        messageId = (String) messageContext.get(MESSAGE_ID_CONTEXT);
        if (NullChecker.isNullish(messageId)) {
            messageId = generateMessageId();
        } else if (illegalUUID(messageId, "uuid:")) {
            messageId = "urn:" + messageId;
        } else if (!legalMessageId(messageId)) {
            messageId = "urn:uuid:" + messageId;
        }

        // Steps that need to be performed
        SOAPElement oMessageIdElem = getFirstChild(oHeader, NhincConstants.WS_SOAP_HEADER_MESSAGE_ID,
                NhincConstants.WS_ADDRESSING_URL);
        if (oMessageIdElem != null) {
            oMessageIdElem.setTextContent(messageId);
        } else {
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            oMessageIdElem = soapFactory.createElement(NhincConstants.WS_SOAP_HEADER_MESSAGE_ID, "",
                    NhincConstants.WS_ADDRESSING_URL);
            oMessageIdElem.setTextContent(messageId);

            if (oHeader != null) {
                oHeader.addChildElement(oMessageIdElem);
            }
        }
    }

    /**
     * Returns a header object with a particular local name and namespace.
     *
     * @param header The header object from the message
     * @param name The local name of the element being searched for
     * @param ns The namespace of the object being searched for
     * @return The first instance that matches the localname and namespace or return null
     */
    private SOAPElement getFirstChild(SOAPHeader header, String name, String ns) {
        SOAPElement result = null;
        if (header == null || !header.hasChildNodes()) {
            return result;
        }

        QName qname = new QName(ns, name);
        Iterator iter = header.getChildElements(qname);
        if (iter.hasNext()) {
            result = (SOAPElement) iter.next();
        }
        return result;
    }

    /**
     * Check if UUID starts with an illegal prefix ("uuid:")
     *
     * @param messageId
     * @param illegalPrefix
     * @return
     */
    private boolean illegalUUID(String messageId, String illegalPrefix) {
        return messageId.trim().startsWith(illegalPrefix);
    }

    /**
     * Returns true if the messageID starts with a legal prefix ("urn:uuid")
     *
     * @param messageId
     * @return
     */
    private boolean legalMessageId(String messageId) {
        return messageId.trim().startsWith("urn:uuid:");
    }

    /**
     * Method handles a fault if one occurs
     *
     * @param messageContext
     * @return
     */
    @Override
    public boolean handleFault(SOAPMessageContext messageContext) {
        LOG.warn("SoapHeaderHandler.handleFault");

        Boolean isOutboundMessage = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        try {
            SOAPMessage oMessage = messageContext.getMessage();
            SOAPHeader oHeader = oMessage.getSOAPHeader();

            if (isOutboundMessage) {
                addMustUnderstandAttribute(oHeader);
            }
        } catch (SOAPException ex) {
            LOG.warn("Exception adding mustunderstand to fault: {}", ex.getLocalizedMessage());
            LOG.trace("Exception adding mustunderstand to fault: {}", ex.getLocalizedMessage(), ex);
        }

        return true;
    }

    /**
     *
     * @param context
     */
    @Override
    public void close(MessageContext context) {
        LOG.debug("SoapHeaderHandler.close");
    }

    protected String generateMessageId() {
        return AddressingHeaderCreator.generateMessageId();
    }

    private void addMustUnderstandAttribute(SOAPHeader oHeader) throws SOAPException {
        SOAPElement action = getFirstChild(oHeader, NhincConstants.WS_SOAP_HEADER_ACTION,
                NhincConstants.WS_ADDRESSING_URL);

        if (action != null && !action.hasAttribute(NhincConstants.WS_SOAP_ATTR_MUSTUNDERSTAND)) {
            QName mustUnderstandQ = new QName(NhincConstants.WS_SOAP_ENV_URL,
                    NhincConstants.WS_SOAP_ATTR_MUSTUNDERSTAND, NhincConstants.WS_SOAP_ENV_PREFIX);
            action.addAttribute(mustUnderstandQ, Boolean.TRUE.toString());
        }
    }
}
