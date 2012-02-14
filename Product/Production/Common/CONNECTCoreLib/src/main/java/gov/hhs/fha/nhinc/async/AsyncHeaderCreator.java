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
package gov.hhs.fha.nhinc.async;

import java.util.List;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.api.message.Header;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class manages the creation of the header section of the soap message that defines WS-Addressing.
 */
public class AsyncHeaderCreator {

    private Log log = null;

    /**
     * Default Constructor defines the logger
     */
    public AsyncHeaderCreator() {
        log = createLogger();
    }

    /**
     * Creates the error logger
     * 
     * @return The Logger
     */
    protected Log createLogger() {
        if (log == null) {
            log = LogFactory.getLog(getClass());
        }
        return log;
    }

    /**
     * Creates the WS-Addressing headers. Specifically the To, Action, ReplyTo, Address, and MessageID elements are
     * created as Header objects and packaged in a List. This listing can be used to define a WSBindingProvider's
     * outbound headers.
     * 
     * @param url The Endpoint URL used to set the To header element
     * @param action The WS-Addressing action used to set the Action header element and should match the wsaw:Action as
     *            defined in the port definition section of the corresponding wsdl
     * @param messageId The UUID uniquely identifying the message used to set the MessageID header
     * @param relatesToIds A optional listing of ids specifying messages this one relates to. This listing may be null
     *            or empty if no such relationships exist.
     * @return A listing of the WS-Addressing headers
     */
    public List<Header> createOutboundHeaders(String url, String action, String messageId, List<String> relatesToIds) {

        List<Header> headers = new ArrayList<Header>();

        // The To header is required
        if (url == null) {
            log.warn("Provided endpoint URL is null");
        }
        QName qname = new QName(NhincConstants.WS_ADDRESSING_URL, NhincConstants.WS_SOAP_HEADER_TO);
        Header toHdr = Headers.create(qname, url);
        log.debug("Set WS-Addressing <To> " + url);
        headers.add(toHdr);

        // The Action header is required
        if (action == null) {
            log.warn("Provided endpoint URL is null");
        }
        qname = new QName(NhincConstants.WS_ADDRESSING_URL, NhincConstants.WS_SOAP_HEADER_ACTION);
        Header actionHdr = Headers.create(qname, action);
        log.debug("Set WS-Addressing <Action> " + action);
        headers.add(actionHdr);

        // The optional ReplyTo header contains the Address element
        final Document document;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final Element elemReplyTo = document.createElementNS(NhincConstants.WS_ADDRESSING_URL,
                    NhincConstants.WS_SOAP_HEADER_REPLYTO);
            final Element elemAddr = document.createElementNS(NhincConstants.WS_ADDRESSING_URL,
                    NhincConstants.WS_SOAP_HEADER_ADDRESS);
            elemAddr.setTextContent(NhincConstants.WS_ADDRESSING_URL_ANONYMOUS);
            elemReplyTo.appendChild(elemAddr);
            Header replyToHdr = Headers.create(elemReplyTo);
            headers.add(replyToHdr);
        } catch (ParserConfigurationException ex) {
            log.error("ReplyTo header element can not be created: " + ex.getMessage());
        }

        // The messageID header is an optional element
        if (messageId != null) {
            qname = new QName(NhincConstants.WS_ADDRESSING_URL, NhincConstants.WS_SOAP_HEADER_MESSAGE_ID);
            Header msgIdHdr = Headers.create(qname, messageId);
            log.debug("Set WS-Addressing <MessageID> " + messageId);
            headers.add(msgIdHdr);
        }

        // The RelatesTo header is an optional but potentially repeating element
        if (relatesToIds != null && !relatesToIds.isEmpty()) {
            qname = new QName(NhincConstants.WS_ADDRESSING_URL, NhincConstants.HEADER_RELATESTO);
            for (String id : relatesToIds) {
                Header relatesIdHdr = Headers.create(qname, id);
                log.debug("Set WS-Addressing <RelatesTo> " + id);
                headers.add(relatesIdHdr);
            }
        }
        return headers;
    }
}
