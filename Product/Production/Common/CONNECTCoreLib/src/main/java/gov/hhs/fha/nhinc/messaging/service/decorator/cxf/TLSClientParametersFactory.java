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
package gov.hhs.fha.nhinc.messaging.service.decorator.cxf;

import gov.hhs.fha.nhinc.callback.opensaml.CertificateManager;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bhumphrey
 *
 */
public class TLSClientParametersFactory {

    private static final Logger LOG = LoggerFactory.getLogger(TLSClientParametersFactory.class);

    private static TLSClientParametersFactory INSTANCE = null;
    private KeyManagerFactory keyFactory;
    private TrustManagerFactory trustFactory;

    private TLSClientParametersFactory() {
        this(CertificateManagerImpl.getInstance());
    }

    TLSClientParametersFactory(CertificateManager cm) {
        try {
            keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyFactory.init(cm.getKeyStore(), getKeystorePassword());

            trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustFactory.init(cm.getTrustStore());
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
            LOG.error("Could not initialize key and trust managers: " + e.getLocalizedMessage(), e);
        }

    }

    public static TLSClientParametersFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TLSClientParametersFactory();
        }
        return INSTANCE;
    }

    public TLSClientParameters getTLSClientParameters() {
        TLSClientParameters tlsCP = new TLSClientParameters();

        try {
            SSLContext context = SSLContext.getDefault();
            SSLSocketFactory factory = context.getSocketFactory();
            if (factory != null) {
                tlsCP.setSSLSocketFactory(factory);
            } else {
                throw new RuntimeException("Couldn't get the SSLSocketFactory.");
            }
            tlsCP.setDisableCNCheck(true);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Could not get TLS client parameters: " + e.getLocalizedMessage(), e);
            throw new RuntimeException("Could not create SSL Context.", e);
        }

        return tlsCP;
    }

    private char[] getKeystorePassword() throws UnrecoverableKeyException {
        String keystorePassword = System.getProperty("javax.net.ssl.keyStorePassword");
        if (keystorePassword == null || keystorePassword.isEmpty()) {
            throw new UnrecoverableKeyException("Password for key is null or empty.");
        }

        return keystorePassword.toCharArray();
    }
}
