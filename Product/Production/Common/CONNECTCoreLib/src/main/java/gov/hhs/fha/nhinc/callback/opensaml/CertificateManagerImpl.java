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
package gov.hhs.fha.nhinc.callback.opensaml;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import gov.hhs.fha.nhinc.cryptostore.StoreUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mweaver
 *
 */
public class CertificateManagerImpl implements CertificateManager {

    private static final Logger LOG = LoggerFactory.getLogger(CertificateManagerImpl.class);

    private KeyStore keyStore = null;
    private KeyStore trustStore = null;

    public static final String TRUST_STORE_TYPE_KEY = "javax.net.ssl.trustStoreType";
    public static final String TRUST_STORE_SECRET_KEY = "javax.net.ssl.trustStorePassword";
    public static final String TRUST_STORE_KEY = "javax.net.ssl.trustStore";
    public static final String KEY_STORE_TYPE_KEY = "javax.net.ssl.keyStoreType";
    public static final String KEY_STORE_SECRET_KEY = "javax.net.ssl.keyStorePassword";
    public static final String KEY_STORE_KEY = "javax.net.ssl.keyStore";
    public static final String JKS_TYPE = "JKS";
    public static final String PKCS11_TYPE = "PKCS11";
    private static final String KEYSTORE_ERROR_MSG = "Error initializing KeyStore: {}";

    private CertificateManagerImpl() {
        try {
            initKeyStore();
            initTrustStore();

        } catch (final Exception e) {
            LOG.error("Unable to initialize keystores: " + e.getLocalizedMessage(), e);
        }
    }

    public static CertificateManager getInstance() {
        return new CertificateManagerImpl();
    }

    /**
     * For Unit testing, to pass in testable system properties to the instance.
     *
     * @param keyStoreProperties
     * @param trustStoreProperties
     * @return
     */
    static CertificateManager getInstance(final HashMap<String, String> keyStoreProperties,
        final HashMap<String, String> trustStoreProperties) {
        return new CertificateManagerImpl() {
            @Override
            protected HashMap<String, String> getKeyStoreSystemProperties() {
                return keyStoreProperties;
            }

            @Override
            protected HashMap<String, String> getTrustStoreSystemProperties() {
                return trustStoreProperties;
            }
        };
    }

    /**
     * @return the keyStore
     */
    @Override
    public KeyStore getKeyStore() {
        return keyStore;
    }

    /**
     * @return the trustStore
     */
    @Override
    public KeyStore getTrustStore() {
        return trustStore;
    }

    /**
     * Initializes the keystore access using the system properties defined in the domain.xml javax.net.ssl.keyStore and
     * javax.net.ssl.keyStorePassword
     *
     * @throws CertificateManagerException
     */
    private void initKeyStore() throws CertificateManagerException {
        LOG.debug("SamlCallbackHandler.initKeyStore() -- Begin");


        final HashMap<String, String> keyStoreProperties = getKeyStoreSystemProperties();
        String storeType = keyStoreProperties.get(KEY_STORE_TYPE_KEY);
        final String password = keyStoreProperties.get(KEY_STORE_SECRET_KEY);
        final String storeLoc = keyStoreProperties.get(KEY_STORE_KEY);

        if (storeType == null) {
            LOG.warn("javax.net.ssl.keyStoreType is not defined.  Switch to use JKS by default");
            storeType = JKS_TYPE;
        }
        if (password !=null){
            if (JKS_TYPE.equals(storeType) && storeLoc == null) {
                LOG.error("javax.net.ssl.keyStore is not defined");
            } else {
                keyStore = loadKeyStore(storeType, password, storeLoc);
            }
        }else{
            LOG.error("Please define javax.net.ssl.keyStorePassword");
        }
        LOG.debug("SamlCallbackHandler.initKeyStore() -- End");
    }

    /**
     *
     * @param storeType
     * @param password
     * @param storeLoc
     * @throws CertificateManagerException
     */
    private static KeyStore loadKeyStore(final String storeType, final String password, final String storeLoc)
        throws CertificateManagerException {
        InputStream is = null;
        KeyStore secretStore = null;
        try {
            secretStore = KeyStore.getInstance(storeType);
            if (!PKCS11_TYPE.equalsIgnoreCase(storeType)) {
                is = new FileInputStream(storeLoc);
            }
            secretStore.load(is, password.toCharArray());
        } catch (final IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException ex) {
            LOG.error(KEYSTORE_ERROR_MSG, ex.getLocalizedMessage(), ex);
            throw new CertificateManagerException(ex.getMessage(), ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (final IOException ex) {
                LOG.error("Unable to close keyStoreStream- KeyStoreCallbackHandler {}", ex.getLocalizedMessage(), ex);
            }
        }
        return secretStore;
    }

    /**
     * Initializes the truststore access using the system properties defined in the domain.xml javax.net.ssl.trustStore
     * and javax.net.ssl.trustStorePassword
     *
     * @throws CertificateManagerException
     */
    private void initTrustStore() throws CertificateManagerException {
        LOG.debug("SamlCallbackHandler.initTrustStore() -- Begin");

        final HashMap<String, String> trustStoreProperties = getTrustStoreSystemProperties();
        String storeType = trustStoreProperties.get(TRUST_STORE_TYPE_KEY);
        final String password = trustStoreProperties.get(TRUST_STORE_SECRET_KEY);
        final String storeLoc = trustStoreProperties.get(TRUST_STORE_KEY);

        if (storeType == null) {
            LOG.warn("javax.net.ssl.trustStoreType is not defined. Switch to use JKS by default");
            storeType = JKS_TYPE;
        }
        if (password != null) {
            if (JKS_TYPE.equals(storeType) && storeLoc == null) {
                LOG.error("javax.net.ssl.trustStore is not defined in domain.xml");
            } else {
                trustStore = loadKeyStore(storeType, password, storeLoc);
            }
        } else {
            LOG.error("javax.net.ssl.trustStorePassword is not defined.");
        }
        LOG.debug("SamlCallbackHandler.initTrustStore() -- End");
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.callback.openSAML.CertificateManager#getDefaultCertificate()
     */
    @Override
    public X509Certificate getDefaultCertificate() throws CertificateManagerException {
        return (X509Certificate) getPrivateKeyEntry().getCertificate();
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.callback.openSAML.CertificateManager#getDefaultPrivateKey()
     */
    @Override
    public PrivateKey getDefaultPrivateKey() throws CertificateManagerException {
        return getPrivateKeyEntry().getPrivateKey();
    }

    private PrivateKeyEntry getPrivateKeyEntry() throws CertificateManagerException {
        LOG.debug("SamlCallbackHandler.getDefaultPrivKeyCert() -- Begin");
        KeyStore.PrivateKeyEntry pkEntry = null;

        final String clientkeyAlias = StoreUtil.getInstance().getPrivateKeyAlias();
        if (clientkeyAlias != null) {
            final String password = getKeyStoreSystemProperties().get(KEY_STORE_SECRET_KEY);
            if (password != null) {
                try {
                    pkEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(clientkeyAlias,
                        new KeyStore.PasswordProtection(password.toCharArray()));

                } catch (final NoSuchAlgorithmException | KeyStoreException | UnrecoverableEntryException ex) {
                    LOG.error("Error initializing Private Key: {}", ex.getLocalizedMessage(), ex);
                    throw new CertificateManagerException(ex.getLocalizedMessage(), ex);
                }

            } else {
                LOG.error("javax.net.ssl.keyStorePassword is not a defined system property.");
            }

        } else {
            LOG.error(NhincConstants.CLIENT_KEY_ALIAS + " is not a defined system property.");
        }

        LOG.debug("SamlCallbackHandler.getDefaultPrivKeyCert() -- End");
        return pkEntry;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.callback.openSAML.CertificateManager#getDefaultPublicKey()
     */
    @Override
    public RSAPublicKey getDefaultPublicKey() {
        try {
            return (RSAPublicKey) getDefaultCertificate().getPublicKey();
        } catch (final Exception e) {
            LOG.error("Could not get default public key: {}", e.getLocalizedMessage(), e);

        }
        return null;
    }

    protected HashMap<String, String> getTrustStoreSystemProperties() {
        final HashMap<String, String> map = new HashMap<>();
        map.put(TRUST_STORE_KEY, System.getProperty(TRUST_STORE_KEY));
        map.put(TRUST_STORE_SECRET_KEY, System.getProperty(TRUST_STORE_SECRET_KEY));
        map.put(TRUST_STORE_TYPE_KEY, System.getProperty(TRUST_STORE_TYPE_KEY));
        return map;
    }

    protected HashMap<String, String> getKeyStoreSystemProperties() {
        final HashMap<String, String> map = new HashMap<>();
        map.put(KEY_STORE_KEY, System.getProperty(KEY_STORE_KEY));
        map.put(KEY_STORE_TYPE_KEY, System.getProperty(KEY_STORE_TYPE_KEY));
        map.put(KEY_STORE_SECRET_KEY, System.getProperty(KEY_STORE_SECRET_KEY));
        return map;
    }

}
