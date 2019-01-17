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
package gov.hhs.fha.nhinc.messaging.service.decorator.cxf;

import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.DISABLE_CN_CHECK;
import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_PROPERTY_FILE;

import gov.hhs.fha.nhinc.callback.opensaml.CertificateManager;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl;
import gov.hhs.fha.nhinc.cryptostore.StoreUtil;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.transport.https.SSLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bhumphrey
 *
 */
public class TLSClientParametersFactory {

    private static final Logger LOG = LoggerFactory.getLogger(TLSClientParametersFactory.class);

    private static TLSClientParametersFactory tlsCPFactory = new TLSClientParametersFactory();
    private KeyManagerFactory keyFactory;
    private TrustManagerFactory trustFactory;

    private TLSClientParametersFactory() {
        this(CertificateManagerImpl.getInstance());
    }

    private TLSClientParametersFactory(CertificateManager cm) {
        try {
            keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyFactory.init(cm.getKeyStore(), getKeystorePassword());

            trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustFactory.init(cm.getTrustStore());
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
            LOG.error("Could not initialize key and trust managers: {} ", e.getLocalizedMessage(), e);
        }

    }

    public static TLSClientParametersFactory getInstance() {
        return tlsCPFactory;
    }
    /**
     * Construct TLSClientParameter based on protocol version.
     * @param protocols either TLS or SSLv3
     * @return
     */
    public TLSClientParameters getTLSClientParameters(final String protocols, String gatewayAlias, String sniName) {
        TLSClientParameters tlsCP = new TLSClientParameters();
        tlsCP.setSecureSocketProtocol(protocols);
        return constructTLSClient(tlsCP, gatewayAlias, sniName);


    }

    public TLSClientParameters getTLSClientParameters(String certificateAlias, String sniName) {
        return constructTLSClient(new TLSClientParameters(), certificateAlias, sniName);
    }

    private static TLSClientParameters constructTLSClient(TLSClientParameters tlsClientParameters,
        String certificateAlias, String sniName) {
        try {
            boolean isDisableCNCheck = PropertyAccessor.getInstance().getPropertyBoolean(GATEWAY_PROPERTY_FILE,
                DISABLE_CN_CHECK, false);

            if (StringUtils.isNotBlank(certificateAlias)
                && StoreUtil.getPrivateKeyAlias().equalsIgnoreCase(certificateAlias)) {
                LOG.debug("SSLContext.setCertAlias:{}", certificateAlias);
                tlsClientParameters.setCertAlias(certificateAlias);
            }

            setKeyManagers(tlsClientParameters);
            setTrustManager(tlsClientParameters);
            tlsClientParameters.setDisableCNCheck(isDisableCNCheck);
            LOG.debug("tlsClientParameters--disableCNCheck-is-set {} and sniName {}", isDisableCNCheck, sniName);
            if (StringUtils.isNotBlank(sniName)) {
                SSLContext sslContext = SSLUtils.getSSLContext(tlsClientParameters);
                SSLParameters sslParamters = sslContext.getDefaultSSLParameters();
                List<SNIServerName> sniHostList = new ArrayList<>();
                sniHostList.add(new SNIHostName(sniName));
                sslParamters.setServerNames(sniHostList);
                SSLSocketFactoryWrapper wrapper = new SSLSocketFactoryWrapper(sslContext.getSocketFactory(),
                    sslParamters);
                tlsClientParameters.setSSLSocketFactory(wrapper);
            } else {
                SSLSocketFactory factory = SSLUtils.getSSLContext(tlsClientParameters).getSocketFactory();
                tlsClientParameters.setSSLSocketFactory(factory);
            }
        } catch (GeneralSecurityException | IllegalStateException e) {
            throw new SecurityException("Could not create SSL Context.", e);
        }
        return tlsClientParameters;
    }

    /**
     * @param tlsClientParameters
     */
    private static void setTrustManager(TLSClientParameters tlsClientParameters) {
        try {
            TrustManagerFactory trustFactory = getInstance().trustFactory;
            if (trustFactory != null) {
                TrustManager[] trustManager = trustFactory.getTrustManagers();
                if (trustManager != null) {
                    tlsClientParameters.setTrustManagers(trustManager);
                }
            } else {
                LOG.debug("Trust Factory is empty");
            }
        } catch (IllegalStateException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * @param tlsClientParameters
     */
    private static void setKeyManagers(TLSClientParameters tlsClientParameters) {
        try {
            KeyManagerFactory keyFactory = getInstance().keyFactory;
            if (keyFactory != null) {
                KeyManager[] keyManager = keyFactory.getKeyManagers();
                if (keyManager != null) {
                    tlsClientParameters.setKeyManagers(keyManager);
                }
            } else {
                LOG.debug("Key Factory is null");
            }
        } catch (IllegalStateException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }

    }

    private static char[] getKeystorePassword() throws UnrecoverableKeyException {
        String keystorePassword = System.getProperty("javax.net.ssl.keyStorePassword");
        if (keystorePassword == null || keystorePassword.isEmpty()) {
            throw new UnrecoverableKeyException("Password for key is null or empty.");
        }

        return keystorePassword.toCharArray();
    }
}
