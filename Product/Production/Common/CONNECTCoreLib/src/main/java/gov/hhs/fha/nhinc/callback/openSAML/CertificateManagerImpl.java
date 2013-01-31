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
package gov.hhs.fha.nhinc.callback.openSAML;

import gov.hhs.fha.nhinc.cryptostore.StoreUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import org.apache.log4j.Logger;

/**
 * @author mweaver
 *
 */
public class CertificateManagerImpl implements CertificateManager {
    private static final Logger LOG = Logger.getLogger(CertificateManagerImpl.class);

    private KeyStore keyStore = null;

    private KeyStore trustStore = null;

    private CertificateManagerImpl() {
        try {
            if (keyStore == null) {
                initKeyStore();
            }
            if (trustStore == null) {
                initTrustStore();
            }
        } catch (Exception e) {
            LOG.error("unable to initialize keystores", e);
            e.printStackTrace();
        }
    }

    public static CertificateManager getInstance() {
        return new CertificateManagerImpl();
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
     * @throws Exception
     */
    private void initKeyStore() throws Exception {
        LOG.debug("SamlCallbackHandler.initKeyStore() -- Begin");

        InputStream is = null;
        String storeType = System.getProperty("javax.net.ssl.keyStoreType");
        String password = System.getProperty("javax.net.ssl.keyStorePassword");
        String storeLoc = System.getProperty("javax.net.ssl.keyStore");

        if (storeType == null) {
            LOG.error("javax.net.ssl.keyStoreType is not defined");
            LOG.warn("Default to JKS keyStoreType");
            storeType = "JKS";
        }
        if (password == null || storeLoc == null) {
            LOG.error("Store password or store location not defined");
            LOG.error("Please define javax.net.ssl.keyStorePassword and javax.net.ssl.keyStore");
        }

        if ("JKS".equals(storeType) && storeLoc == null) {
            LOG.error("javax.net.ssl.keyStore is not defined");
        } else {
            try {
                keyStore = KeyStore.getInstance(storeType);
                is = new FileInputStream(storeLoc);
                keyStore.load(is, password.toCharArray());
            } catch (NoSuchAlgorithmException ex) {
                LOG.error("Error initializing KeyStore: " + ex);
                throw new Exception(ex.getMessage());
            } catch (CertificateException ex) {
                LOG.error("Error initializing KeyStore: " + ex);
                throw new Exception(ex.getMessage());
            } catch (KeyStoreException ex) {
                LOG.error("Error initializing KeyStore: " + ex);
                throw new Exception(ex.getMessage());
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ex) {
                    LOG.debug("KeyStoreCallbackHandler " + ex);
                }
            }
        }

        LOG.debug("SamlCallbackHandler.initKeyStore() -- End");
    }

    /**
     * Initializes the truststore access using the system properties defined in the domain.xml javax.net.ssl.trustStore
     * and javax.net.ssl.trustStorePassword
     *
     * @throws Exception
     */
    private void initTrustStore() throws Exception {
        LOG.debug("SamlCallbackHandler.initTrustStore() -- Begin");

        InputStream is = null;
        String storeType = System.getProperty("javax.net.ssl.trustStoreType");
        String password = System.getProperty("javax.net.ssl.trustStorePassword");
        String storeLoc = System.getProperty("javax.net.ssl.trustStore");

        if (storeType == null) {
            LOG.error("javax.net.ssl.trustStoreType is not defined in domain.xml");
            LOG.warn("Default to JKS trustStoreType");
            storeType = "JKS";
        }
        if (password != null) {
            if ("JKS".equals(storeType) && storeLoc == null) {
                LOG.error("javax.net.ssl.trustStore is not defined in domain.xml");
            } else {
                try {
                    trustStore = KeyStore.getInstance(storeType);
                    is = new FileInputStream(storeLoc);
                    trustStore.load(is, password.toCharArray());
                } catch (NoSuchAlgorithmException ex) {
                    LOG.error("Error initializing TrustStore: " + ex);
                    throw new Exception(ex.getMessage());
                } catch (CertificateException ex) {
                    LOG.error("Error initializing TrustStore: " + ex);
                    throw new IOException(ex.getMessage());
                } catch (KeyStoreException ex) {
                    LOG.error("Error initializing TrustStore: " + ex);
                    throw new IOException(ex.getMessage());
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException ex) {
                        LOG.debug("KeyStoreCallbackHandler " + ex);
                    }
                }
            }
        } else {
            LOG.error("javax.net.ssl.trustStorePassword is not defined in domain.xml");
        }
        LOG.debug("SamlCallbackHandler.initTrustStore() -- End");
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.callback.openSAML.CertificateManager#getDefaultCertificate()
     */
    public X509Certificate getDefaultCertificate() throws Exception {
        return (X509Certificate) getPrivateKeyEntry().getCertificate();
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.callback.openSAML.CertificateManager#getDefaultPrivateKey()
     */
    @Override
    public PrivateKey getDefaultPrivateKey() throws Exception {
        return getPrivateKeyEntry().getPrivateKey();
    }

    private PrivateKeyEntry getPrivateKeyEntry() throws Exception {
        LOG.debug("SamlCallbackHandler.getDefaultPrivKeyCert() -- Begin");
        KeyStore.PrivateKeyEntry pkEntry = null;

        String client_key_alias = StoreUtil.getInstance().getPrivateKeyAlias();
        if (client_key_alias != null) {
            String password = System.getProperty("javax.net.ssl.keyStorePassword");
            if (password != null) {
                try {
                    pkEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(client_key_alias,
                            new KeyStore.PasswordProtection(password.toCharArray()));

                } catch (NoSuchAlgorithmException ex) {
                    LOG.error("Error initializing Private Key: " + ex);
                    throw new Exception(ex.getMessage());
                } catch (KeyStoreException ex) {
                    LOG.error("Error initializing Private Key: " + ex);
                    throw new Exception(ex.getMessage());
                } catch (UnrecoverableEntryException ex) {
                    LOG.error("Error initializing Private Key: " + ex);
                    throw new Exception(ex.getMessage());
                }

            } else {
                LOG.error("javax.net.ssl.keyStorePassword is not a defined system property.");
            }

        } else {
            LOG.error(CertificateManager.CLIENT_KEY_ALIAS + " is not a defined system property.");
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
        } catch (Exception e) {
            LOG.error(e);

        }
        return null;
    }

}
