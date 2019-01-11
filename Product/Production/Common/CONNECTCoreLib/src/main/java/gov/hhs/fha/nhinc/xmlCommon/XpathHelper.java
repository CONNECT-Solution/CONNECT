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
package gov.hhs.fha.nhinc.xmlCommon;

import gov.hhs.fha.nhinc.util.StringUtil;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 *
 * @author rayj
 */
public class XpathHelper {

    private static final Logger LOG = LoggerFactory.getLogger(XpathHelper.class);

    public static Node performXpathQuery(String sourceXml, String xpathQuery) throws XPathExpressionException {
        javax.xml.xpath.XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
        javax.xml.xpath.XPath xpath = factory.newXPath();

        InputSource inputSource = null;
        try {
            inputSource = new InputSource(new ByteArrayInputStream(sourceXml.getBytes(StringUtil.UTF8_CHARSET)));
        } catch (UnsupportedEncodingException ex) {
            LOG.error("Error converting String to UTF8 format: {}", ex.getLocalizedMessage(), ex);
        }

        LOG.debug("perform xpath query (query='" + xpathQuery + "'");
        Node result;
        if (XmlUtfHelper.isUtf16(sourceXml)) {
            try {
                result = (Node) xpath.evaluate(xpathQuery, inputSource, XPathConstants.NODE);
            } catch (Exception ex) {
                // Exception may be due to the encoding of the message being incorrect.
                // retry using UTF-8
                LOG.warn("failed to perform xpath query - retrying with UTF-8: {}", ex.getLocalizedMessage());
                LOG.trace("failed to perform xpath query - retrying with UTF-8: {}", ex.getLocalizedMessage(), ex);
                sourceXml = XmlUtfHelper.convertToUtf8(sourceXml);
                result = performXpathQuery(sourceXml, xpathQuery);
            }
        } else {
            result = (Node) xpath.evaluate(xpathQuery, inputSource, XPathConstants.NODE);
        }
        LOG.debug("xpath query complete [result?=" + result + "]");
        return result;
    }

    public static Node performXpathQuery(Element sourceElement, String xpathQuery) throws XPathExpressionException {
        return performXpathQuery(sourceElement, xpathQuery, null);
    }

    public static Node performXpathQuery(Element sourceElement, String xpathQuery, NamespaceContext namespaceContext)
            throws XPathExpressionException {
        javax.xml.xpath.XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
        javax.xml.xpath.XPath xpath = factory.newXPath();

        if (namespaceContext != null) {
            xpath.setNamespaceContext(namespaceContext);
        }

        LOG.debug("About to perform xpath query (query='" + xpathQuery + "'");
        return (Node) xpath.evaluate(xpathQuery, sourceElement, XPathConstants.NODE);
    }
}
