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
package gov.hhs.fha.nhinc.callback.cxf.wss;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import java.util.ArrayList;
import java.util.Collection;
import javax.activation.DataHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.message.Attachment;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author akong
 *
 */
public class CONNECTSignatureProcessorTest {

    private static final LargeFileUtils FILE_UTILS = LargeFileUtils.getInstance();

    private static final String XML_SIGNATURE_NS = "http://www.w3.org/2000/09/xmldsig#";
    private static final String XOP_NS = "http://www.w3.org/2004/08/xop/include";
    private static final String DIG_REF_ID = "digRefId";
    private static final String SIG_REF_ID = "sigRefId";
    private static final String DIG_BINARY_DATA = "digital value check";
    private static final String DIG_BASE64_DATA = "ZGlnaXRhbCB2YWx1ZSBjaGVjaw==";
    private static final String SIG_BINARY_DATA = "signature value check";
    private static final String SIG_BASE64_DATA = "c2lnbmF0dXJlIHZhbHVlIGNoZWNr";

    @Test
    public void inlineIncludes() throws ParserConfigurationException, WSSecurityException {
        SoapMessage msg = mock(SoapMessage.class);

        Collection<Attachment> attachmentList = new ArrayList<>();
        attachmentList.add(createMockAttachment(DIG_REF_ID, DIG_BINARY_DATA));
        attachmentList.add(createMockAttachment(SIG_REF_ID, SIG_BINARY_DATA));

        when(msg.getAttachments()).thenReturn(attachmentList);

        Element signatureElem = createSignatureElementWithIncludes(DIG_REF_ID, SIG_REF_ID);

        CONNECTSignatureProcessor processor = new CONNECTSignatureProcessor();
        processor.inlineSignatureAttachments(msg, signatureElem);

        assertDigitalValueIsInline(signatureElem, DIG_BASE64_DATA);
        assertSignatureValueIsInline(signatureElem, SIG_BASE64_DATA);
    }

    @Test
    public void inlineIncludesWithPrefixRefId() throws ParserConfigurationException, WSSecurityException {
        SoapMessage msg = mock(SoapMessage.class);

        Collection<Attachment> attachmentList = new ArrayList<>();
        attachmentList.add(createMockAttachment(DIG_REF_ID, DIG_BINARY_DATA));
        attachmentList.add(createMockAttachment(SIG_REF_ID, SIG_BINARY_DATA));

        when(msg.getAttachments()).thenReturn(attachmentList);

        Element signatureElem = createSignatureElementWithIncludes("cid:" + DIG_REF_ID, "cid:" + SIG_REF_ID);

        CONNECTSignatureProcessor processor = new CONNECTSignatureProcessor();
        processor.inlineSignatureAttachments(msg, signatureElem);

        assertDigitalValueIsInline(signatureElem, DIG_BASE64_DATA);
        assertSignatureValueIsInline(signatureElem, SIG_BASE64_DATA);
    }

    @Test
    public void noIncludes() throws ParserConfigurationException, WSSecurityException {
        SoapMessage msg = mock(SoapMessage.class);

        Element signatureElem = createSignatureElement(DIG_BASE64_DATA, SIG_BASE64_DATA);

        CONNECTSignatureProcessor processor = new CONNECTSignatureProcessor();
        processor.inlineSignatureAttachments(msg, signatureElem);

        assertDigitalValueIsInline(signatureElem, DIG_BASE64_DATA);
        assertSignatureValueIsInline(signatureElem, SIG_BASE64_DATA);
    }

    private void assertDigitalValueIsInline(Element signatureElem, String expectedBase64Data) {
        NodeList digValueList = signatureElem.getElementsByTagNameNS(SamlConstants.XML_SIGNATURE_NS,
                SamlConstants.DIGEST_VALUE_TAG);

        assertEquals(1, digValueList.getLength());
        assertEquals(expectedBase64Data, digValueList.item(0).getTextContent());
    }

    private void assertSignatureValueIsInline(Element signatureElem, String expectedBase64Data) {
        NodeList sigValueList = signatureElem.getElementsByTagNameNS(SamlConstants.XML_SIGNATURE_NS,
                SamlConstants.SIGNATURE_VALUE_TAG);

        assertEquals(1, sigValueList.getLength());
        assertEquals(expectedBase64Data, sigValueList.item(0).getTextContent());
    }

    private Attachment createMockAttachment(String refId, String binaryData) {
        DataHandler dataHandler = FILE_UTILS.convertToDataHandler(binaryData);

        Attachment mockAttachment = mock(Attachment.class);
        when(mockAttachment.getId()).thenReturn(refId);
        when(mockAttachment.getDataHandler()).thenReturn(dataHandler);

        return mockAttachment;
    }

    private Element createSignatureElementWithIncludes(String digRefId, String sigRefId)
            throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();

        Element digestValue = createDigestValueWithIncludes(doc, digRefId);
        Element signatureValue = createSignatureValueWithIncludes(doc, sigRefId);

        Element signatureElem = doc.createElementNS(XML_SIGNATURE_NS, "Signature");
        signatureElem.appendChild(digestValue);
        signatureElem.appendChild(signatureValue);

        return signatureElem;
    }

    private Element createSignatureValueWithIncludes(Document doc, String refId) {
        Element signatureValue = doc.createElementNS(XML_SIGNATURE_NS, "SignatureValue");
        Element signatureValueInclude = doc.createElementNS(XOP_NS, "Include");
        signatureValueInclude.setAttribute("href", refId);
        signatureValue.appendChild(signatureValueInclude);

        return signatureValue;
    }

    private Element createDigestValueWithIncludes(Document doc, String refId) {
        Element digestValue = doc.createElementNS(XML_SIGNATURE_NS, "DigestValue");
        Element digestValueInclude = doc.createElementNS(XOP_NS, "Include");
        digestValueInclude.setAttribute("href", refId);
        digestValue.appendChild(digestValueInclude);

        return digestValue;
    }

    private Element createSignatureElement(String digBase64Data, String sigBase64Data)
            throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();

        Element digestValue = createDigestValue(doc, digBase64Data);
        Element signatureValue = createSignatureValue(doc, sigBase64Data);

        Element signatureElem = doc.createElementNS(XML_SIGNATURE_NS, "Signature");
        signatureElem.appendChild(digestValue);
        signatureElem.appendChild(signatureValue);

        return signatureElem;
    }

    private Element createSignatureValue(Document doc, String base64data) {
        Element signatureValue = doc.createElementNS(XML_SIGNATURE_NS, "SignatureValue");
        signatureValue.setTextContent(base64data);

        return signatureValue;
    }

    private Element createDigestValue(Document doc, String base64data) {
        Element digestValue = doc.createElementNS(XML_SIGNATURE_NS, "DigestValue");
        digestValue.setTextContent(base64data);

        return digestValue;
    }
}
