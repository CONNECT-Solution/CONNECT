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
package gov.hhs.fha.nhinc.webserviceproxy;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import javax.xml.ws.WebServiceException;
import org.jmock.Expectations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class WebServiceProxyHelperInvokePortTest extends AbstractWebServiceProxyHelpTest {


    WebServiceProxyHelper oHelper;

    /**
     * This method is used to test out some of the dynamic invocaton methods.
     *
     * @param x an integer.
     * @param y an integer.
     * @param a result.
     */
    public Integer helperMethod2(Integer x, Integer y) {
        return x;
    }

    /**
     * This method is used to test out some of the dynamic invocaton methods.
     *
     * @param x an integer.
     * @param a result.
     */
    public Integer helperMethod(Integer x) {
        return x;
    }

    public Integer exceptionalMethod(Integer x) throws Exception {
        throw new SocketTimeoutException("SocketTimeoutException");
    }

    public Integer exceptionalWSMethod(Integer x) throws Exception {
        throw new WebServiceException("WebServiceExpcetion");
    }

    @Before
    public void before() throws PropertyAccessException {
        context.checking(new Expectations() {
            {
                oneOf(mockPropertyAccessor).getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, WebServiceProxyHelperProperties.CONFIG_KEY_TIMEOUT);
                will(returnValue("0"));

                oneOf(mockPropertyAccessor).getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, WebServiceProxyHelperProperties.CONFIG_KEY_RETRYATTEMPTS);
                will(returnValue("0"));

                oneOf(mockPropertyAccessor).getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, WebServiceProxyHelperProperties.CONFIG_KEY_RETRYDELAY);
                will(returnValue("10"));

                exactly(3).of(mockPropertyAccessor).getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, WebServiceProxyHelperProperties.CONFIG_KEY_EXCEPTION);
                will(returnValue("SocketTimeoutException"));

            }
        });

        oHelper = new WebServiceProxyHelper(mockPropertyAccessor);
    }

    /**
     * Test the getMethod method.
     */
    @Test
    public void testGetMethod() {
        Method oMethod = oHelper.getMethod(this.getClass(), "helperMethod");
        assertNotNull("getMethod failed", oMethod);
        assertEquals("Incorrect method returned.", "helperMethod", oMethod.getName());

    }

    /**
     * Test the invokePort method happy path.
     *
     * @throws Exception
     */
    @Test
    public void testInvokePortHappyPath() throws Exception {


        Integer oResponse = (Integer) oHelper.invokePort(this, this.getClass(), "helperMethod", new Integer(100));
        assertNotNull("invokePort failed to return a value.", oResponse);
        assertTrue("Response was incorrect type.", oResponse instanceof Integer);
        assertEquals("Incorrect value returned.", 100, oResponse.intValue());

    }

    /**
     * Test the invokePort method illegal argument exception.
     *
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvokePortIllegalArgumentException() throws Exception {

        oHelper.invokePort(this, this.getClass(), "helperMethod2", new Integer(100));

    }

    /**
     * Test the invokePort method with retry settings with exception.
     *
     * @throws Exception
     */
    @Test(expected = SocketTimeoutException.class)
    public void testInvokePortWithInvocationTargetException() throws Exception {


        oHelper.invokePort(this, this.getClass(), "exceptionalMethod", 100);

    }

    /**
     * Test the invokePort method with retry settings happy path.
     *
     * @throws Exception
     */
    @Test
    public void testInvokePortRetrySettingsHappyPath() throws Exception {


        Integer oResponse = (Integer) oHelper.invokePort(this, this.getClass(), "helperMethod", new Integer(100));
        assertNotNull("invokePort failed to return a value.", oResponse);
        assertTrue("Response was incorrect type.", oResponse instanceof Integer);

    }

    /**
     * Test the invokePort method with retry settings with exception.
     *
     * @throws Exception
     */
    @Test(expected = WebServiceException.class)
    public void testInvokePortRetrySettingsWithWebServiceException() throws Exception {


        Integer oResponse = (Integer) oHelper
                .invokePort(this, this.getClass(), "exceptionalWSMethod", new Integer(100));

    }

    /**
     * Test the invokePort method with retry settings with exception.
     *
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvokePortRetrySettingsWithWebServiceExceptionNoTextMatch() throws Exception {


        oHelper.invokePort(this, this.getClass(), "badMethodName", new Integer(100));

    }

    /**
     * Test the invokePort method with retry settings with exception.
     *
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvokePortRetrySettingsWithIllegalArgumentException() throws Exception {


        oHelper.invokePort(this, this.getClass(), "exceptionalMethod", "100");
    }

    /**
     * Test the invokePort method with retry settings with exception.
     *
     * @throws Exception
     */
    @Test(expected = WebServiceException.class)
    public void testInvokePortRetrySettingsWithInvocationTargetException() throws Exception {



        oHelper.invokePort(this, this.getClass(), "exceptionalWSMethod", 100);
    }

}
