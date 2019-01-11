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
package gov.hhs.fha.nhinc.direct.xdr;

import gov.hhs.fha.nhinc.direct.DirectException;
import javax.servlet.ServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Class DirectHeaderExtractor.
 */
public class DirectHeaderExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(DirectHeaderExtractor.class);

    /**
     * Gets the header properties.
     *
     * @param context the context
     * @return the header properties
     */
    public SoapEdgeContext getHeaderProperties(WebServiceContext context) {

        if (context == null) {
            throw new DirectException("WebServiceContext was null.");
        }

        MessageContext msgContext = context.getMessageContext();
        SoapEdgeContext headers = getHeaderProperties(msgContext);

        ServletRequest sr = (ServletRequest) msgContext.get(MessageContext.SERVLET_REQUEST);
        if (sr != null) {
            headers.setRemoteHost(sr.getRemoteHost());
            headers.setThisHost(sr.getServerName());
        }

        return headers;
    }

    protected SoapEdgeContext getHeaderProperties(MessageContext msgContext) {

        WrappedMessageContext wMsgContext = null;
        if (msgContext instanceof WrappedMessageContext) {
            wMsgContext = (WrappedMessageContext) msgContext;
        }

        if (wMsgContext == null) {
            throw new DirectException("Could not obtain message context.");
        }

        return getHeaderProperties(wMsgContext);
    }

    protected SoapEdgeContext getHeaderProperties(WrappedMessageContext wMsgContext) {

        if (wMsgContext == null) {
            throw new DirectException("Could not obtain wrapped message context.");
        }
        Message msg = wMsgContext.getWrappedMessage();

        Node node = msg.getContent(org.w3c.dom.Node.class);

        if (node == null) {
            throw new DirectException("Could not obtain dom message.");
        }
        Node envelope = getChildNode(node.getChildNodes(), "Envelope");

        if (envelope == null) {
            throw new DirectException("Could not obtain envelope.");
        }
        Node header = getChildNode(envelope.getChildNodes(), "Header");

        return getHeaderProperties(header);
    }

    /**
     * @param childNodes
     * @param string
     * @return
     */
    protected Node getChildNode(NodeList childNodes, String childName) {
        Node child = null;

        for (int i = 0; i < childNodes.getLength(); i++) {
            Node n = childNodes.item(i);
            if (n.getNodeName().contains(childName)) {
                child = n;
            }
        }
        return child;
    }

    /**
     * Gets the header properties.
     *
     * @param sh the sh
     * @return the header properties
     */
    protected SoapEdgeContext getHeaderProperties(Node node) {

        if (node == null) {
            throw new DirectException("Could not obtain headers node.");
        }
        NodeList nodes = node.getChildNodes();

        if (nodes == null) {
            throw new DirectException("There were no header nodes.");
        }

        SoapEdgeContext headers = new SoapEdgeContextMapImpl();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node header = nodes.item(i);
            if (LOG.isTraceEnabled()) {
                LOG.debug(header.getNodeName());
            }

            if (StringUtils.contains(header.getNodeName(), "MessageID")) {
                headers.setMessageId(getTextContent(header));
            } else if (StringUtils.contains(header.getNodeName(), "Action")) {
                headers.setAction(getTextContent(header));
            } else if (StringUtils.contains(header.toString(), "RelatesTo")) {
                headers.setRelatesTo(getTextContent(header));
            } else if (StringUtils.contains(header.getNodeName(), "ReplyTo")) {
                headers.setReplyTo(getTextContentFromChildNode(header, "Address"));
            } else if (StringUtils.contains(header.toString(), "From")) {
                headers.setFrom(getTextContentFromChildNode(header, "Address"));
            } else if (StringUtils.contains(header.toString(), "To")) {// must be after ReplyTo
                headers.setTo(getTextContent(header));
            } else if (StringUtils.contains(header.toString(), "addressBlock")) {
                headers.setDirectFrom(getTextContentFromChildNode(header, "from"));
                headers.setDirectTo(getTextContentFromChildNode(header, "to"));
                headers.setDirectMetaDataLevel(getTextContentFromChildNode(header, "metadata-level"));
            }
        }
        return headers;
    }

    protected String getTextContentFromChildNode(Node node, String childName) {
        String s = null;
        NodeList reps = node.getChildNodes();
        for (int i = 0; i < reps.getLength(); i++) {
            Node childNode = reps.item(i);
            if (LOG.isTraceEnabled()) {
                LOG.trace("inspecting child node: " + childNode.getNodeName());
            }
            if (StringUtils.contains(childNode.getNodeName(), childName)) {
                s = getTextContent(childNode);
            }
        }
        return s;
    }

    protected String getTextContent(Node node) {
        String s = node.getTextContent();
        if (LOG.isTraceEnabled()) {
            LOG.trace("Extracted the following value from text node: " + s);
        }
        return s;
    }
}
