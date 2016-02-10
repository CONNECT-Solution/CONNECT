/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.saml;

import javax.security.auth.callback.CallbackHandler;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.saml.SAMLIssuer;
import org.apache.ws.security.saml.ext.AssertionWrapper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SAMLIssuerImplTest {

    private SAMLIssuerImpl impl;
    private SAMLIssuer mockDelegate;

    @Before
    public void before() {
        impl = new SAMLIssuerImpl();

        mockDelegate = mock(SAMLIssuer.class);
        impl.setDelegate(mockDelegate);
    }

    @Test
    public void hasDefaultDelegate() {
        SAMLIssuerImpl impl = new SAMLIssuerImpl();
        assertNotNull(impl.getDelegate());
        assertTrue(impl.getDelegate() instanceof org.apache.ws.security.saml.SAMLIssuerImpl);
    }

    @Test
    public void newAssertion() throws WSSecurityException {
        AssertionWrapper mockWrapper = mock(AssertionWrapper.class);
        when(mockDelegate.newAssertion()).thenReturn(mockWrapper);
        assertSame(mockWrapper, impl.newAssertion());
    }

    @Test
    public void getCallbackHandler() {
        CallbackHandler mockHandler = mock(CallbackHandler.class);
        when(mockDelegate.getCallbackHandler()).thenReturn(mockHandler);
        assertSame(mockHandler, impl.getCallbackHandler());
    }

    @Test
    public void getIssuerCrypto() {
        Crypto mockCrypto = mock(Crypto.class);
        when(mockDelegate.getIssuerCrypto()).thenReturn(mockCrypto);
        assertSame(mockCrypto, impl.getIssuerCrypto());
    }

    @Test
    public void getIssuerKeyName() {
        when(mockDelegate.getIssuerKeyName()).thenReturn("keyName");
        assertEquals("keyName", impl.getIssuerKeyName());
    }

    @Test
    public void getIssuerKeyPassword() {
        when(mockDelegate.getIssuerKeyPassword()).thenReturn("keyPassword");
        assertEquals("keyPassword", impl.getIssuerKeyPassword());
    }

    @Test
    public void getIssuerName() {
        when(mockDelegate.getIssuerName()).thenReturn("name");
        assertEquals("name", impl.getIssuerName());
    }

    @Test
    public void isSendKeyValue() {
        when(mockDelegate.isSendKeyValue()).thenReturn(true);
        assertEquals(true, impl.isSendKeyValue());
    }

    @Test
    public void isSignAssertion() {
        when(mockDelegate.isSignAssertion()).thenReturn(true);
        assertEquals(true, impl.isSignAssertion());
    }

    @Test(expected = RuntimeException.class)
    public void setCallbackHandler() {
        impl.setCallbackHandler(null);
    }

    @Test(expected = RuntimeException.class)
    public void setIssuerCrypto() {
        impl.setIssuerCrypto(null);
    }

    @Test(expected = RuntimeException.class)
    public void setIssuerKeyName() {
        impl.setIssuerKeyName("keyName");
    }

    @Test(expected = RuntimeException.class)
    public void setIssuerKeyPassword() {
        impl.setIssuerKeyPassword("keyPassword");
    }

    @Test(expected = RuntimeException.class)
    public void setIssuerName() {
        impl.setIssuerName("name");
    }

    @Test(expected = RuntimeException.class)
    public void setSendKeyValue() {
        impl.setSendKeyValue(true);
    }

    @Test(expected = RuntimeException.class)
    public void setSignAssertion() {
        impl.setSignAssertion(false);
    }
}
