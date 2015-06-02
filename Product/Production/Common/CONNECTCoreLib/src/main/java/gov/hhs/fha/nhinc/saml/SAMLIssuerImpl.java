/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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

import java.util.Properties;

import javax.security.auth.callback.CallbackHandler;

import org.apache.log4j.Logger;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.saml.SAMLIssuer;
import org.apache.ws.security.saml.ext.AssertionWrapper;

/**
 * This class replaces the default apache SAML issuer. The differences with the default class are:
 * <ol>
 * <li>passed in properties to the constructor are ignored. We always use the propreties from the nhinc configuration
 * directory. This is to allow the saml properties file to be configurable outside the ear file and is a workaround to a
 * WSS4J bug that expects that file to always be in the classpath. More info can be found here:
 * https://issues.connectopensource.org/browse/GATEWAY-3306, https://issues.apache.org/jira/browse/WSS-418
 * <li>We delegate <em>to a static instance</em> for the non-set methods. Setter methods throw exceptions. This static
 * instance is designed to improve performance, because Crypto initialization is expensive. This only works for
 * threadsafe Crypto implementations.
 * </ol>
 */
public class SAMLIssuerImpl implements SAMLIssuer {

    private static final Logger LOG = Logger.getLogger(SAMLIssuerImpl.class);
    private static SAMLIssuer DEFAULT_DELEGATE;
    private SAMLIssuer delegate = DEFAULT_DELEGATE;

    static {
        try {
            DEFAULT_DELEGATE = new org.apache.ws.security.saml.SAMLIssuerImpl(SAMLConfigFactory.getInstance()
                    .getConfiguration());
        } catch (WSSecurityException e) {
            LOG.fatal("Could not initialize SAMLIssuerImpl: " + e.getMessage(), e);
        }
    }

    /**
     * @param prop
     *            - this parameter is ignored
     * @throws WSSecurityException
     */
    public SAMLIssuerImpl(Properties prop) throws WSSecurityException {
        // do absolutely nothing
    }

    public SAMLIssuerImpl() {
        // do nothing
    }

    public AssertionWrapper newAssertion() throws WSSecurityException {
        return delegate.newAssertion();
    }

    public boolean isSendKeyValue() {
        return delegate.isSendKeyValue();
    }

    public boolean isSignAssertion() {
        return delegate.isSignAssertion();
    }

    public CallbackHandler getCallbackHandler() {
        return delegate.getCallbackHandler();
    }

    public Crypto getIssuerCrypto() {
        return delegate.getIssuerCrypto();
    }

    public String getIssuerName() {
        return delegate.getIssuerName();
    }

    public String getIssuerKeyName() {
        return delegate.getIssuerKeyName();
    }

    public String getIssuerKeyPassword() {
        return delegate.getIssuerKeyPassword();
    }

    public void setSendKeyValue(boolean sendKeyValue) {
        throw new RuntimeException("Setters not supported");
    }

    public void setSignAssertion(boolean signAssertion) {
        throw new RuntimeException("Setters not supported");
    }

    public void setCallbackHandler(CallbackHandler callbackHandler) {
        throw new RuntimeException("Setters not supported");
    }

    public void setIssuerCrypto(Crypto issuerCrypto) {
        throw new RuntimeException("Setters not supported");
    }

    public void setIssuerName(String issuer) {
        throw new RuntimeException("Setters not supported");
    }

    public void setIssuerKeyName(String issuerKeyName) {
        throw new RuntimeException("Setters not supported");
    }

    public void setIssuerKeyPassword(String issuerKeyPassword) {
        throw new RuntimeException("Setters not supported");
    }

    SAMLIssuer getDelegate() {
        return delegate;
    }

    void setDelegate(SAMLIssuer delegate) {
        this.delegate = delegate;
    }
}
