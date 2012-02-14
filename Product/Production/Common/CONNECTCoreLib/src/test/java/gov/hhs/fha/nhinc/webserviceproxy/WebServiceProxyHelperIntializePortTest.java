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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import com.sun.xml.ws.developer.WSBindingProvider;

public class WebServiceProxyHelperIntializePortTest extends AbstractWebServiceProxyHelpTest {

    final WSBindingProvider mockPort = context.mock(WSBindingProvider.class);
    @SuppressWarnings("unchecked")
    final Map<String, Object> mockRequestContext = context.mock(Map.class);
    final Log mockLog = context.mock(Log.class);

    WebServiceProxyHelper oHelper;

    @Before
    public void before() throws PropertyAccessException {
        initializationExpectations();
        oHelper = new WebServiceProxyHelper(mockLog, mockPropertyAccessor);
    }

    /**
     * Test the initializePort no assertion class method happy path.
     * 
     * @throws PropertyAccessException
     */
    @Test
    public void testInitializePortNoAssertionHappyPath() throws PropertyAccessException {

        context.checking(new Expectations() {

            {
                ignoring(mockLog).warn(with(any(String.class)));
                ignoring(mockLog).info(with(any(String.class)));
                ignoring(mockLog).debug(with(any(String.class)));
                ignoring(mockLog).isInfoEnabled();

                allowing(mockPort).getRequestContext();
                will(returnValue(mockRequestContext));

                ignoring(mockRequestContext).put(with(any(String.class)), with(any(Object.class)));

                allowing(mockRequestContext).get(WebServiceProxyHelper.KEY_URL);
                will(returnValue("http://www.someurlnew.com"));

                allowing(mockRequestContext).containsKey(WebServiceProxyHelper.KEY_URL);
                will(returnValue(true));
            }
        });

        oHelper.initializeUnsecurePort(mockPort, "http://www.someurlnew.com", null, null);

    }

    /**
     * Test the initializePort with assertion class method happy path.
     * 
     * @throws PropertyAccessException
     */
    @Test
    public void testInitializePortWithAssertionHappyPath() throws PropertyAccessException {

        context.checking(new Expectations() {

            {
                ignoring(mockLog).warn(with(any(String.class)));
                ignoring(mockLog).info(with(any(String.class)));
                ignoring(mockLog).debug(with(any(String.class)));
                ignoring(mockLog).isInfoEnabled();

                allowing(mockPort).getRequestContext();
                will(returnValue(mockRequestContext));

                ignoring(mockRequestContext).put(with(any(String.class)), with(any(Object.class)));

                ignoring(mockRequestContext).putAll(with(any(Map.class)));

                allowing(mockRequestContext).get(WebServiceProxyHelper.KEY_URL);
                will(returnValue("http://www.someurlnew.com"));

                allowing(mockRequestContext).containsKey(WebServiceProxyHelper.KEY_URL);
                will(returnValue(true));

                oneOf(mockPort).setOutboundHeaders(with(any(List.class)));

            }
        });

        AssertionType oAssertion = new AssertionType();
        oHelper.initializeSecurePort(mockPort, "http://www.someurlnew.com", "service", "Test-ws-action", oAssertion);

    }

    /**
     * Test the initializePort with assertion class method with empty serviceaction.
     * 
     * @throws PropertyAccessException
     */
    @Test(expected = RuntimeException.class)
    public void testInitializePortWithAssertionEmptyServiceAction() throws PropertyAccessException {
        context.checking(new Expectations() {

            {
                ignoring(mockLog).warn(with(any(String.class)));
                ignoring(mockLog).info(with(any(String.class)));
                ignoring(mockLog).debug(with(any(String.class)));
                ignoring(mockLog).isInfoEnabled();

            }
        });

        AssertionType oAssertion = new AssertionType();

        oHelper.initializeSecurePort(mockPort, "http://www.someurlnew.com", "", null, oAssertion);

        // "Unable to initialize secure port (serviceAction null)";
    }

    /**
     * Test the initializePort with assertion class method with null serviceaction.
     */
    @Test(expected = RuntimeException.class)
    public void testInitializePortWithAssertionNullServiceAction() {
        context.checking(new Expectations() {

            {
                ignoring(mockLog).warn(with(any(String.class)));
                ignoring(mockLog).info(with(any(String.class)));
                ignoring(mockLog).debug(with(any(String.class)));
                ignoring(mockLog).isInfoEnabled();

            }
        });

        AssertionType oAssertion = new AssertionType();

        oHelper.initializeSecurePort(mockPort, "http://www.someurlnew.com", null, null, oAssertion);
    }

    /**
     * Test the initializePort with assertion class method with null port.
     */
    @Test(expected = RuntimeException.class)
    public void testInitializePortWithAssertionNullPort() {
        context.checking(new Expectations() {

            {
                ignoring(mockLog).warn(with(any(String.class)));
                ignoring(mockLog).info(with(any(String.class)));
                ignoring(mockLog).debug(with(any(String.class)));
                ignoring(mockLog).isInfoEnabled();

            }
        });

        AssertionType oAssertion = new AssertionType();

        oHelper.initializeSecurePort(null, "http://www.someurlnew.com", "service", null, oAssertion);
    }

    /**
     * Test the initializePort with assertion class method with null URL.
     */
    @Test(expected = RuntimeException.class)
    public void testInitializePortWithAssertionNullURL() {
        context.checking(new Expectations() {

            {
                ignoring(mockLog).warn(with(any(String.class)));
                ignoring(mockLog).info(with(any(String.class)));
                ignoring(mockLog).debug(with(any(String.class)));
                ignoring(mockLog).isInfoEnabled();

            }
        });

        AssertionType oAssertion = new AssertionType();

        oHelper.initializeSecurePort(mockPort, null, "service", null, oAssertion);
    }

    /**
     * Test the initializePort with assertion class method with empty URL.
     */
    @Test(expected = RuntimeException.class)
    public void testInitializePortWithAssertionEmptyURL() {
        context.checking(new Expectations() {

            {
                ignoring(mockLog).warn(with(any(String.class)));
                ignoring(mockLog).info(with(any(String.class)));
                ignoring(mockLog).debug(with(any(String.class)));
                ignoring(mockLog).isInfoEnabled();

            }
        });

        AssertionType oAssertion = new AssertionType();

        oHelper.initializeSecurePort(mockPort, null, "service", null, oAssertion);
    }

    /**
     * Test the initializePort with assertion class method with nullish RequestContext.
     * 
     * @throws PropertyAccessException
     */
    @Test(expected = RuntimeException.class)
    public void testInitializePortWithAssertionNullRequestContext() {
        context.checking(new Expectations() {

            {
                ignoring(mockLog).warn(with(any(String.class)));
                ignoring(mockLog).info(with(any(String.class)));
                ignoring(mockLog).debug(with(any(String.class)));
                ignoring(mockLog).isInfoEnabled();
                allowing(mockPort).getRequestContext();
                will(returnValue(null));

            }
        });

        AssertionType oAssertion = new AssertionType();

        oHelper.initializeSecurePort(mockPort, "http://www.someurl.com", "service", null, oAssertion);
    }

    /**
     * Test the initializePort no assertion class method with empty request context.
     */
    @Test(expected = RuntimeException.class)
    public void testInitializePortNoAssertionEmptyRequestContext() {
        context.checking(new Expectations() {

            {
                ignoring(mockLog).warn(with(any(String.class)));
                ignoring(mockLog).info(with(any(String.class)));
                ignoring(mockLog).debug(with(any(String.class)));
                ignoring(mockLog).isInfoEnabled();
                allowing(mockPort).getRequestContext();

            }
        });

        oHelper.initializeSecurePort(mockPort, "http://www.someurlnew.com", null, null, null);

    }

    /**
     * Test the initializePort no assertion class method with 0 timeout.
     * 
     * @throws PropertyAccessException
     */
    @Test
    public void testInitializePortNoAssertionZeroTimeout() throws PropertyAccessException {
        context.checking(new Expectations() {

            {
                ignoring(mockLog).warn(with(any(String.class)));
                ignoring(mockLog).info(with(any(String.class)));
                ignoring(mockLog).debug(with(any(String.class)));
                ignoring(mockLog).isInfoEnabled();
                allowing(mockPort).getRequestContext();

            }
        });

        oHelper.initializePort(mockPort, "http://www.someurlnew.com");

    }

}
