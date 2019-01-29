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
package gov.hhs.fha.nhinc.callback.cxf;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.callback.opensaml.CallbackProperties;
import gov.hhs.fha.nhinc.callback.opensaml.HOKSAMLAssertionBuilder;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import javax.security.auth.callback.Callback;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.cxf.message.Message;
import org.apache.wss4j.common.saml.SAMLCallback;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CXFSAMLCallbackHandlerTest {

    private static Element assertionElement;
    private static final String ASSERTION = "assertion";

    @BeforeClass
    public static void setUp() throws ParserConfigurationException {
        createAssertionElement();
        System.setProperty("nhinc.properties.dir", System.getProperty("user.dir") + "/src/test/resources");
    }

    private static void createAssertionElement() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        assertionElement = doc.createElement(ASSERTION);
        assertionElement.setTextContent(ASSERTION);
    }

    @Test
    public void testHandle() throws Exception {
        Callback[] callbackList = new Callback[1];
        SAMLCallback samlCallback = new SAMLCallback();
        callbackList[0] = samlCallback;
        final Message message = mock(Message.class);
        AssertionType assertionType = mock(AssertionType.class);
        HOKSAMLAssertionBuilder builder = mock(HOKSAMLAssertionBuilder.class);

        CXFSAMLCallbackHandler callbackHandler = new CXFSAMLCallbackHandler(builder) {
            @Override
            protected Message getCurrentMessage() {
                return message;
            }
        };

        when(message.get("assertion")).thenReturn(assertionType);
        when(message.get(Message.INBOUND_MESSAGE)).thenReturn(false);
        when(message.get(NhincConstants.WS_SOAP_TARGET_HOME_COMMUNITY_ID)).thenReturn("1.1");
        when(message.get(SamlConstants.TARGET_API_LEVEL)).thenReturn("G0");
        when(message.get(SamlConstants.ACTION_PROP)).thenReturn("Soap Action");
        when(builder.build(any(CallbackProperties.class), any(String.class))).thenReturn(assertionElement);

        callbackHandler.handle(callbackList);

        assertEquals(samlCallback.getSamlVersion(), org.opensaml.saml.common.SAMLVersion.VERSION_20);
        assertEquals(samlCallback.getAssertionElement().getTextContent(), ASSERTION);
    }

    @Test
    public void testGetResource() {
        final Message message = mock(Message.class);
        final String URL = "https://CONNECT/ENDPOINT";

        when(message.get(Message.INBOUND_MESSAGE)).thenReturn(false);
        when(message.get(Message.ENDPOINT_ADDRESS)).thenReturn(URL);

        CXFSAMLCallbackHandler callbackHandler = new CXFSAMLCallbackHandler();
        String resource = callbackHandler.getResource(message);

        assertEquals(resource, URL);
    }
}
