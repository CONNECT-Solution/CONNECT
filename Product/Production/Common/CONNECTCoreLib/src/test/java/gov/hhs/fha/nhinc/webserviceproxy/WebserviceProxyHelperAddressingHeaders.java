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
package gov.hhs.fha.nhinc.webserviceproxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.hhs.fha.nhinc.async.AsyncHeaderCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.junit.Test;

import com.sun.xml.ws.developer.WSBindingProvider;

public class WebserviceProxyHelperAddressingHeaders extends AbstractWebServiceProxyHelpTest {

    final WSBindingProvider mockPort = context.mock(WSBindingProvider.class);
    @SuppressWarnings("unchecked")
    final Map<String, Object> mockRequestContext = context.mock(Map.class);
    final HashMap<String, Object> oMap = new HashMap<String, Object>();
    final SamlTokenCreator mockTokenCreator = context.mock(SamlTokenCreator.class);
    final AsyncHeaderCreator mockAsyncHeaderCreator = context.mock(AsyncHeaderCreator.class);

    final Log mockLog = context.mock(Log.class);

    WebServiceProxyHelper oHelper;

    /**
     * Tests the getWSAddressing method
     * 
     * @throws PropertyAccessException
     */
    @Test
    public void testGetWSAddressingHeaders() throws PropertyAccessException {

        initializationExpectations();

        WebServiceProxyHelper oHelper = new WebServiceProxyHelper(mockLog, mockPropertyAccessor) {

            @Override
            protected Log createLogger() {
                return mockLog;
            }

            @Override
            protected AsyncHeaderCreator getAsyncHeaderCreator() {
                return new AsyncHeaderCreator() {

                    @Override
                    public List createOutboundHeaders(String url, String action, String messageId,
                            List<String> relatesToIds) {

                        List headers = new ArrayList();
                        headers.add(url);
                        headers.add(action);
                        headers.add(messageId);
                        headers.addAll(relatesToIds);
                        return headers;
                    }
                };
            }

            @Override
            protected String getMessageId(AssertionType assertion) {
                return "Test_Message_Id";
            }

            @Override
            protected List<String> getRelatesTo(AssertionType assertion) {
                List<String> allRelatesTo = new ArrayList();
                allRelatesTo.add("Test_Relates_1");
                allRelatesTo.add("Test_Relates_2");
                return allRelatesTo;
            }
        };

        AssertionType oAssertion = new AssertionType();
        List returnedHeaders = oHelper.getWSAddressingHeaders("Test_URL", "Test_ws_action", oAssertion);
        assertEquals("Number of created Headers is invalid.", 5, returnedHeaders.size());
        assertTrue("Test_URL header not found", returnedHeaders.contains("Test_URL"));
        assertTrue("Test_ws_action header not found", returnedHeaders.contains("Test_ws_action"));
        assertTrue("Test_Message_Id header not found", returnedHeaders.contains("Test_Message_Id"));
        assertTrue("Test_Relates_1 header not found", returnedHeaders.contains("Test_Relates_1"));
        assertTrue("Test_Relates_2 header not found", returnedHeaders.contains("Test_Relates_2"));
    }

}
