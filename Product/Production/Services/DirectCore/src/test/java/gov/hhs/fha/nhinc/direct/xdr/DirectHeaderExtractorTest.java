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

import gov.hhs.fha.nhinc.direct.DirectBaseTest;
import gov.hhs.fha.nhinc.direct.DirectException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

/**
 *
 * @author jasonasmith
 */
public class DirectHeaderExtractorTest extends DirectBaseTest {

    private static final String MESSAGE_ID = "12345";
    private static final String ACTION = "Direct Action";
    private static final String RELATES_TO = "54321";
    private static final String REPLY_TO = "Address 1";
    private static final String FROM = "Address 2";
    private static final String TO = "To Address";
    private static final String DIRECT_FROM = "Direct From";
    private static final String DIRECT_TO = "Direct To";
    private static final String DIRECT_META = "Direct Metadata";
    private static final String REMOTE_HOST = "remote host";
    private static final String SERVER_NAME = "server name";


    @Test(expected = DirectException.class)
    public void testGetHeaderProperties_WebServiceContextNull() {
        WebServiceContext context = null;

        new DirectHeaderExtractor().getHeaderProperties(context);
    }

    @Test(expected = DirectException.class)
    public void testGetHeaderProperties_MessageContextNull() {
        WebServiceContext context = mock(WebServiceContext.class);

        when(context.getMessageContext()).thenReturn(null);

        new DirectHeaderExtractor().getHeaderProperties(context);
    }

    @Test(expected = DirectException.class)
    public void testGetHeaderProperties_ContentNull() {
        WebServiceContext context = mock(WebServiceContext.class);
        Message msg = mock(Message.class);
        WrappedMessageContext messageContext = new WrappedMessageContext(msg);

        when(context.getMessageContext()).thenReturn(messageContext);

        new DirectHeaderExtractor().getHeaderProperties(context);
    }

    @Test(expected = DirectException.class)
    public void testGetHeaderProperties_EnvelopeNull() {
        WebServiceContext context = mock(WebServiceContext.class);
        Message msg = mock(Message.class);
        Node content = mock(Node.class);
        NodeListImpl contentChildren = new NodeListImpl();

        WrappedMessageContext messageContext = new WrappedMessageContext(msg);

        when(context.getMessageContext()).thenReturn(messageContext);
        when(msg.getContent(Node.class)).thenReturn(content);
        when(content.getChildNodes()).thenReturn(contentChildren);

        new DirectHeaderExtractor().getHeaderProperties(context);
    }

    /**
     * Test of getHeaderProperties method, of class DirectHeaderExtractor.
     */
    @Test
    public void testGetHeaderProperties() {
        WebServiceContext context = mock(WebServiceContext.class);
        Message msg = mock(Message.class);
        WrappedMessageContext messageContext = new WrappedMessageContext(msg);

        when(context.getMessageContext()).thenReturn(messageContext);

        Node content = mock(Node.class);

        NodeListImpl envelope = new NodeListImpl();
        Node envNode = mock(Node.class);
        envelope.add(envNode);

        NodeListImpl header = new NodeListImpl();
        Node headerNode = mock(Node.class);
        header.add(headerNode);

        when(msg.getContent(Node.class)).thenReturn(content);
        when(content.getChildNodes()).thenReturn(envelope);

        when(envNode.getNodeName()).thenReturn("Envelope");

        when(envNode.getChildNodes()).thenReturn(header);
        when(headerNode.getNodeName()).thenReturn("Header");

        when(headerNode.getChildNodes()).thenReturn(getHeaderNodeList());

        ServletRequest request = mock(ServletRequest.class);
        when(messageContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(request);
        when(request.getRemoteHost()).thenReturn(REMOTE_HOST);
        when(request.getServerName()).thenReturn(SERVER_NAME);

        DirectHeaderExtractor extractor = new DirectHeaderExtractor();
        SoapEdgeContext edgeContext = extractor.getHeaderProperties(context);

        checkEdgeContext(edgeContext);
    }

    private NodeList getHeaderNodeList() {

        NodeListImpl headers = new NodeListImpl();

        Node hMessageId = new NodeImpl("MessageID", MESSAGE_ID);
        Node hAction = new NodeImpl("Action", ACTION);
        Node hRelatesTo = new NodeImpl("RelatesTo", RELATES_TO);
        Node hReplyTo = new NodeImpl("ReplyTo", null);
        Node hFrom = new NodeImpl("From", null);
        Node hTo = new NodeImpl("To", TO);
        Node hAddressBlock = new NodeImpl("addressBlock", null);

        headers.add(hMessageId);
        headers.add(hAction);
        headers.add(hRelatesTo);
        headers.add(hReplyTo);
        headers.add(hFrom);
        headers.add(hTo);
        headers.add(hAddressBlock);

        NodeListImpl fromChildren = new NodeListImpl();
        NodeListImpl replyToChildren = new NodeListImpl();
        NodeListImpl addressBlockChildren = new NodeListImpl();

        ((NodeImpl) hFrom).setChildren(fromChildren);
        ((NodeImpl) hReplyTo).setChildren(replyToChildren);
        ((NodeImpl) hAddressBlock).setChildren(addressBlockChildren);

        Node fromChild = new NodeImpl("Address", FROM);
        Node replyToChild = new NodeImpl("Address", REPLY_TO);

        fromChildren.add(fromChild);
        replyToChildren.add(replyToChild);

        Node directFromChild = new NodeImpl("from", DIRECT_FROM);
        Node directToChild = new NodeImpl("to", DIRECT_TO);
        Node directMetaChild = new NodeImpl("metadata-level", DIRECT_META);

        addressBlockChildren.add(directFromChild);
        addressBlockChildren.add(directToChild);
        addressBlockChildren.add(directMetaChild);

        return headers;
    }

    private void checkEdgeContext(SoapEdgeContext edgeContext) {

        assertNotNull(edgeContext);
        assertEquals(edgeContext.getMessageId(), MESSAGE_ID);
        assertEquals(edgeContext.getAuditableValues().get(SoapEdgeContext.ACTION), ACTION);
        assertEquals(edgeContext.getAuditableValues().get(SoapEdgeContext.RELATES_TO), RELATES_TO);
        assertEquals(edgeContext.getTo(), TO);
        assertEquals(edgeContext.getAuditableValues().get(SoapEdgeContext.REPLY_TO), REPLY_TO);
        assertEquals(edgeContext.getAuditableValues().get(SoapEdgeContext.FROM), FROM);
        assertEquals(edgeContext.getDirectFrom(), DIRECT_FROM);
        assertEquals(edgeContext.getDirectTo(), DIRECT_TO);
        assertEquals(edgeContext.getAuditableValues().get(SoapEdgeContext.DIRECT_METADATA_LEVEL), DIRECT_META);
        assertEquals(edgeContext.getRemoteHost(), REMOTE_HOST);
        assertEquals(edgeContext.getThisHost(), SERVER_NAME);
    }

    class NodeListImpl implements NodeList {

        private List<Node> nodes = new ArrayList<>();

        public void add(Node node){
            nodes.add(node);
        }

        @Override
        public Node item(int index) {
            return nodes.get(index);
        }

        @Override
        public int getLength() {
            return nodes.size();
        }

    }

    class NodeImpl implements Node {

        private String nodeName;
        private String textContent;
        private NodeListImpl children;

        public NodeImpl(String nodeName, String textContent){
            this.nodeName = nodeName;
            this.textContent = textContent;
        }

        public void setChildren(NodeListImpl children){
            this.children = children;
        }

        @Override
        public String getNodeName() {
            return nodeName;
        }

        @Override
        public String toString(){
            return nodeName;
        }

        @Override
        public String getTextContent() throws DOMException {
            return textContent;
        }

        @Override
        public NodeList getChildNodes() {
            return children;
        }

        @Override
        public String getNodeValue() throws DOMException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setNodeValue(String nodeValue) throws DOMException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public short getNodeType() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Node getParentNode() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Node getFirstChild() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Node getLastChild() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Node getPreviousSibling() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Node getNextSibling() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public NamedNodeMap getAttributes() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Document getOwnerDocument() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Node insertBefore(Node newChild, Node refChild) throws DOMException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Node removeChild(Node oldChild) throws DOMException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Node appendChild(Node newChild) throws DOMException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean hasChildNodes() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Node cloneNode(boolean deep) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void normalize() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isSupported(String feature, String version) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getNamespaceURI() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getPrefix() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setPrefix(String prefix) throws DOMException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getLocalName() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean hasAttributes() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getBaseURI() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public short compareDocumentPosition(Node other) throws DOMException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setTextContent(String textContent) throws DOMException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isSameNode(Node other) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String lookupPrefix(String namespaceURI) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isDefaultNamespace(String namespaceURI) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String lookupNamespaceURI(String prefix) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isEqualNode(Node arg) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getFeature(String feature, String version) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object setUserData(String key, Object data, UserDataHandler handler) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getUserData(String key) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

}
