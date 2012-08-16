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
package gov.hhs.fha.nhinc.callback;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.xml.wss.impl.callback.KeyStoreCallback;

/**
 * This class uses the truststore system properties as established in the domain.xml file to allow the configuration of
 * the SAML Truststore policy statements.
 */
public class TrustStoreCallbackHandler implements CallbackHandler {

    private KeyStore trustStore = null;
    private final String password;
    private static Log log = LogFactory.getLog(TrustStoreCallbackHandler.class);

    /**
     * Creates the callback handler saving the truststore certificates information from the truststore file specified by
     * the system properties: javax.net.ssl.trustStore and javax.net.ssl.trustStorePassword.
     */
    public TrustStoreCallbackHandler() {
        log.debug("Entry TrustStoreCallbackHandler Constructor");
        InputStream is = null;
        String storeType = System.getProperty("javax.net.ssl.trustStoreType");
        password = System.getProperty("javax.net.ssl.trustStorePassword");
        String storeLoc = System.getProperty("javax.net.ssl.trustStore");

        if (storeType == null) {
            log.error("javax.net.ssl.trustStoreType is not defined in domain.xml");
            log.warn("Default to JKS trustStoreType");
            storeType = "JKS";
        }
        if (password != null) {
            if ("JKS".equals(storeType) && storeLoc == null) {
                log.error("javax.net.ssl.trustStore is not defined in domain.xml");
            } else {
                try {
                    trustStore = KeyStore.getInstance(storeType);
                    if ("JKS".equals(storeType)) {
                        is = new FileInputStream(storeLoc);
                    }
                    trustStore.load(is, password.toCharArray());
                } catch (IOException ex) {
                    log.debug("TrustStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } catch (NoSuchAlgorithmException ex) {
                    log.debug("TrustStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } catch (CertificateException ex) {
                    log.debug("TrustStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } catch (KeyStoreException ex) {
                    log.debug("TrustStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException ex) {
                        log.debug("TrustStoreCallbackHandler " + ex);
                    }
                }
            }
        } else {
            log.error("javax.net.ssl.trustStorePassword is not defined in domain.xml");
        }
        log.debug("Exit TrustStoreCallbackHandler Constructor");
    }

    /**
     * Implementing the callback, this method provides the truststore information to the input Callback object.
     *
     * @param callbacks The Callback which needs to have truststore information set.
     * @throws IOException IOException
     * @throws UnsupportedCallbackException UnsupportedCallbackException
     */
    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        log.debug("Entry TrustStoreCallbackHandler handle callback");
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof KeyStoreCallback) {
                KeyStoreCallback cb = (KeyStoreCallback) callbacks[i];
                // print(cb.getRuntimeProperties());
                cb.setKeystore(trustStore);
                log.debug("TrustStoreCallback set truststore: " + trustStore);
            } else {
                log.error("Unsupported TrustStoreCallbackHandler Callback: " + callbacks[i]);
                throw new UnsupportedCallbackException(callbacks[i]);
            }
        }
        log.debug("Exit TrustStoreCallbackHandler handle callback");
    }

    /*
     * private void print(Map context) { Iterator it = context.keySet().iterator(); while (it.hasNext()) {
     * log.debug("Prop " + it.next()); } }
     */
}
