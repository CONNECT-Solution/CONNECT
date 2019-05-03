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
package gov.hhs.fha.nhinc.callback.opensaml;

import static gov.hhs.fha.nhinc.callback.opensaml.CertificateUtil.PKCS11_TYPE;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateUtil.createCertificate;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateUtil.loadKeyStore;

import gov.hhs.fha.nhinc.cryptostore.StoreUtil;
import gov.hhs.fha.nhinc.messaging.service.port.CachingCXFSecuredServicePortBuilder;
import gov.hhs.fha.nhinc.messaging.service.port.CachingCXFUnsecuredServicePortBuilder;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.activation.DataHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mweaver
 *
 */
public class CertificateManagerImpl implements CertificateManager {

    private static final String STORE_TYPE_ERROR = "{} is not defined. Switch to use JKS by default";
    private static final Logger LOG = LoggerFactory.getLogger(CertificateManagerImpl.class);
    private KeyStore keyStore = null;
    private KeyStore trustStore = null;
    public static final String TRUST_STORE_TYPE_KEY = "javax.net.ssl.trustStoreType";
    public static final String TRUST_STORE_PASSWORD_KEY = "javax.net.ssl.trustStorePassword";
    public static final String TRUST_STORE_KEY = "javax.net.ssl.trustStore";
    public static final String KEY_STORE_TYPE_KEY = "javax.net.ssl.keyStoreType";
    public static final String KEY_STORE_PASSWORD_KEY = "javax.net.ssl.keyStorePassword";
    public static final String KEY_STORE_KEY = "javax.net.ssl.keyStore";
    public static final String JKS_TYPE = "JKS";

    private CertificateManagerImpl() {
        try {
            initKeyStore();
            initTrustStore();
        } catch (final Exception e) {
            LOG.error("Unable to initialize keystores {} ", e.getLocalizedMessage(), e);
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
    public static CertificateManager getInstance(final Map<String, String> keyStoreProperties,
        final Map<String, String> trustStoreProperties) {
        return new CertificateManagerImpl() {
            @Override
            public Map<String, String> getKeyStoreSystemProperties() {
                return keyStoreProperties;
            }

            @Override
            public Map<String, String> getTrustStoreSystemProperties() {
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

    @Override
    public void importCertificate(String alias, DataHandler data, boolean refreshCache)
        throws CertificateManagerException {
        Certificate addCert = createCertificate(data);
        try {
            verifyCertInfo(alias, addCert);
            trustStore.setCertificateEntry(alias, addCert);
            storeCert();
            if (refreshCache) {
                refreshServices();
            }
        } catch (IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException ex) {
            checkAndRemoveCert(alias);
            throw new CertificateManagerException("Unable to store cert " + alias + "due to: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean deleteCertificate(String alias) throws CertificateManagerException {
        final Map<String, String> trustStoreProperties = getTrustStoreSystemProperties();
        String storeType = trustStoreProperties.get(TRUST_STORE_TYPE_KEY);
        final String storeLoc = trustStoreProperties.get(TRUST_STORE_KEY);
        final String passkey = trustStoreProperties.get(TRUST_STORE_PASSWORD_KEY);

        FileInputStream is = null;
        FileOutputStream os = null;
        boolean isDeleteSuccessful = false;
        try {
            KeyStore tstore = KeyStore.getInstance(storeType);
            if (!PKCS11_TYPE.equalsIgnoreCase(storeType)) {
                is = new FileInputStream(storeLoc);
            }
            tstore.load(is, passkey.toCharArray());
            // restrict deleting from its own public cert(s)
            List<X509Certificate> publicCerts = getDefaultCertificates();
            for (X509Certificate publicCert : publicCerts) {
                String publicAlias = tstore.getCertificateAlias(publicCert);
                if (StringUtils.equalsIgnoreCase(alias, publicAlias)) {
                    throw new CertificateManagerException("System cannot remove its own public cert");
                }
            }
            if (tstore.containsAlias(alias)) {
                tstore.deleteEntry(alias);
                os = new FileOutputStream(storeLoc);
                tstore.store(os, passkey.toCharArray());
                isDeleteSuccessful = true;
            }
        } catch (final IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException ex) {
            throw new CertificateManagerException(ex.getMessage(), ex);
        } finally {
            closeStream(is, os);
        }

        return isDeleteSuccessful;
    }

    @Override
    public boolean updateCertificate(String oldAlias, String newAlias, final String storeType, final String storeLoc,
        final String passkey, KeyStore storeCert) throws CertificateManagerException {
        boolean isUpdateSuccessful = false;
        FileInputStream is = null;
        try {
            if (!PKCS11_TYPE.equalsIgnoreCase(storeType)) {
                is = new FileInputStream(storeLoc);
            }
            storeCert.load(is, passkey.toCharArray());
            if (storeCert.containsAlias(oldAlias)) {
                updateCertEntry(oldAlias, newAlias, storeCert.getCertificate(oldAlias), storeLoc, passkey, storeCert);
                isUpdateSuccessful = true;
            }
        } catch (final IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException ex) {
            throw new CertificateManagerException(ex.getMessage(), ex);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return isUpdateSuccessful;

    }

    private static void updateCertEntry(final String oldAlias, final String newAlias, Certificate certificate,
        final String storeLoc, final String passkey, KeyStore tstore) throws CertificateManagerException {
        try (FileOutputStream os = new FileOutputStream(storeLoc)) {
            tstore.deleteEntry(oldAlias);
            tstore.setCertificateEntry(newAlias, certificate);
            tstore.store(os, passkey.toCharArray());
        } catch (final IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException ex) {
            throw new CertificateManagerException(ex.getMessage(), ex);
        }
    }

    private static void closeStream(FileInputStream is, FileOutputStream os) {
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);
    }

    @Override
    public X509Certificate getCertificateFromByteCode(DataHandler data) throws CertificateManagerException {
        return createCertificate(data);
    }

    private void verifyCertInfo(String alias, Certificate cert) throws CertificateManagerException, KeyStoreException {
        StringBuilder message = new StringBuilder();
        boolean hasError = false;

        if (StringUtils.isBlank(alias)) {
            hasError = true;
            message.append("Alias, ").append(alias).append(", is missing");
        } else if (trustStore.containsAlias(alias)) {
            hasError = true;
            message.append("Alias, ").append(alias).append(", already exists");
        } else if (cert == null) {
            hasError = true;
            message.append("Certificate for alias, ").append(alias).append(", is null");
        }

        if (hasError) {
            throw new CertificateManagerException("Certificate information invalid: " + message.toString());
        }
    }

    /**
     * Initializes the keystore access using the system properties defined in the domain.xml javax.net.ssl.keyStore and
     * javax.net.ssl.keyStorePassword
     *
     * @throws CertificateManagerException
     */
    private void initKeyStore() throws CertificateManagerException {
        LOG.debug("initKeyStore() -- Begin");

        final Map<String, String> keyStoreProperties = getKeyStoreSystemProperties();
        String storeType = keyStoreProperties.get(KEY_STORE_TYPE_KEY);
        final String password = keyStoreProperties.get(KEY_STORE_PASSWORD_KEY);
        final String storeLoc = keyStoreProperties.get(KEY_STORE_KEY);

        if (storeType == null) {
            LOG.warn(STORE_TYPE_ERROR, KEY_STORE_TYPE_KEY);
            storeType = JKS_TYPE;
        }
        if (password != null) {
            if (JKS_TYPE.equals(storeType) && storeLoc == null) {
                LOG.error("{} is not defined", KEY_STORE_TYPE_KEY);
            } else {
                keyStore = loadKeyStore(storeType, password, storeLoc);
            }
        } else {
            LOG.error("Please define {}", KEY_STORE_PASSWORD_KEY);
        }
        LOG.debug("initKeyStore() -- End");
    }

    /**
     * Initializes the truststore access using the system properties defined in the domain.xml javax.net.ssl.trustStore
     * and javax.net.ssl.trustStorePassword
     *
     * @throws CertificateManagerException
     */
    private void initTrustStore() throws CertificateManagerException {
        LOG.debug("SamlCallbackHandler.initTrustStore() -- Begin");

        final Map<String, String> trustStoreProperties = getTrustStoreSystemProperties();
        String storeType = trustStoreProperties.get(TRUST_STORE_TYPE_KEY);
        final String password = trustStoreProperties.get(TRUST_STORE_PASSWORD_KEY);
        final String storeLoc = trustStoreProperties.get(TRUST_STORE_KEY);

        if (storeType == null) {
            LOG.warn(STORE_TYPE_ERROR, TRUST_STORE_TYPE_KEY);
            storeType = JKS_TYPE;
        }
        if (password != null) {
            if (JKS_TYPE.equals(storeType) && storeLoc == null) {
                LOG.error("{} is not defined.", TRUST_STORE_KEY);
            } else {
                trustStore = loadKeyStore(storeType, password, storeLoc);
            }
        } else {
            LOG.error("{} is not defined. ", TRUST_STORE_PASSWORD_KEY);
        }
        LOG.debug("SamlCallbackHandler.initTrustStore() -- End");
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.callback.openSAML.CertificateManager#getCertificateBy()
     */
    @Override
    public X509Certificate getCertificateBy(String alias) throws CertificateManagerException {
        LOG.debug("debug -- CertificateManagerImpl-getDefaultCertificate:{}", alias);
        PrivateKeyEntry pkEntry = getPrivateKeyEntry(alias);
        if (pkEntry != null) {
            return (X509Certificate) pkEntry.getCertificate();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.callback.openSAML.CertificateManager#getDefaultPrivateKey()
     */
    @Override
    public PrivateKey getPrivateKeyBy(String alias) throws CertificateManagerException {
        LOG.debug("debug -- CertificateManagerImpl-getDefaultPrivateKey:{}", alias);
        PrivateKeyEntry pkEntry = getPrivateKeyEntry(alias);
        if (pkEntry != null) {
            return pkEntry.getPrivateKey();
        }
        return null;
    }

    private PrivateKeyEntry getPrivateKeyEntry(String alias) throws CertificateManagerException {
        LOG.debug("SamlCallbackHandler.getDefaultPrivKeyCert() -- Begin");
        PrivateKeyEntry pkEntry = null;

        final String clientkeyAlias = StringUtils.isNotBlank(alias) ? alias
            : StoreUtil.getPrivateKeyAlias();
        LOG.debug("getPrivateKeyEntry-clientkeyAlias:{}", clientkeyAlias);

        final String password = getKeyStoreSystemProperties().get(KEY_STORE_PASSWORD_KEY);
        if (password != null) {
            try {
                pkEntry = (PrivateKeyEntry) keyStore.getEntry(clientkeyAlias,
                    new KeyStore.PasswordProtection(password.toCharArray()));

            } catch (final NoSuchAlgorithmException | KeyStoreException | UnrecoverableEntryException ex) {
                throw new CertificateManagerException(ex.getLocalizedMessage(), ex);
            }

        } else {
            LOG.error("{} is not a defined system property.", KEY_STORE_PASSWORD_KEY);
        }

        LOG.debug("SamlCallbackHandler.getDefaultPrivKeyCert() -- End");
        return pkEntry;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.callback.openSAML.CertificateManager#getPublicKeyBy()
     */
    @Override
    public RSAPublicKey getPublicKeyBy(String alias) {
        LOG.debug("debug -- CertificateManagerImpl-getDefaultPublicKey:{}", alias);
        try {
            return (RSAPublicKey) getCertificateBy(alias).getPublicKey();
        } catch (final Exception e) {
            LOG.error("Could not get default public key: {}", e.getLocalizedMessage(), e);

        }
        return null;
    }

    @Override
    public Map<String, String> getTrustStoreSystemProperties() {
        final Map<String, String> map = new HashMap<>();
        map.put(TRUST_STORE_KEY, System.getProperty(TRUST_STORE_KEY));
        map.put(TRUST_STORE_PASSWORD_KEY, System.getProperty(TRUST_STORE_PASSWORD_KEY));
        map.put(TRUST_STORE_TYPE_KEY, System.getProperty(TRUST_STORE_TYPE_KEY));
        return map;
    }

    @Override
    public Map<String, String> getKeyStoreSystemProperties() {
        final Map<String, String> map = new HashMap<>();
        map.put(KEY_STORE_KEY, System.getProperty(KEY_STORE_KEY));
        map.put(KEY_STORE_TYPE_KEY, System.getProperty(KEY_STORE_TYPE_KEY));
        map.put(KEY_STORE_PASSWORD_KEY, System.getProperty(KEY_STORE_PASSWORD_KEY));
        return map;
    }

    @Override
    public KeyStore refreshKeyStore() {
        try {
            initKeyStore();
            return getKeyStore();
        } catch (CertificateManagerException ex) {
            LOG.error("Unable to refresh keystores: {} {}", ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    @Override
    public String getKeyStoreLocation() {
        return System.getProperty(KEY_STORE_KEY);
    }

    @Override
    public String getTrustStoreLocation() {
        return System.getProperty(TRUST_STORE_KEY);
    }

    @Override
    public KeyStore refreshTrustStore() {
        try {
            initTrustStore();
            return getTrustStore();
        } catch (CertificateManagerException ex) {
            LOG.error("Unable to refresh keystores: {} {}", ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    private void storeCert() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        final Map<String, String> trustStoreProperties = getTrustStoreSystemProperties();
        String storeType = trustStoreProperties.get(TRUST_STORE_TYPE_KEY);
        final String password = trustStoreProperties.get(TRUST_STORE_PASSWORD_KEY);
        final String storeLoc = trustStoreProperties.get(TRUST_STORE_KEY);

        if (storeType == null) {
            LOG.warn(STORE_TYPE_ERROR, TRUST_STORE_TYPE_KEY);
            storeType = JKS_TYPE;
        }
        if (password != null && !PKCS11_TYPE.equalsIgnoreCase(storeType)) {
            try (FileOutputStream os = new FileOutputStream(storeLoc)) {
                trustStore.store(os, password.toCharArray());
            }
        }
    }

    private void checkAndRemoveCert(String alias) {
        try {
            if (trustStore.containsAlias(alias)) {
                trustStore.deleteEntry(alias);
            }
        } catch (KeyStoreException ex) {
            LOG.error("Unable to remove certificate: {}", alias, ex);
        }
    }

    @Override
    public void refreshServices() {
        CachingCXFUnsecuredServicePortBuilder.clearCache();
        CachingCXFSecuredServicePortBuilder.clearCache();
    }

    @Override
    public DataHandler transformToHandler(byte[] encoded) {
        return new DataHandler(new CertificateSource(encoded));
    }

    @Override
    public byte[] transformToByteCode(DataHandler handler) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        handler.writeTo(output);
        return output.toByteArray();
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.callback.opensaml.CertificateManager#getDefaultCertificates()
     */
    @Override
    public List<X509Certificate> getDefaultCertificates() throws CertificateManagerException {
        List<X509Certificate> certList = new LinkedList<>();
        final String password = getKeyStoreSystemProperties().get(KEY_STORE_PASSWORD_KEY);

        if (password != null) {
            try {
                Enumeration enumeration = keyStore.aliases();
                while(enumeration.hasMoreElements()) {
                    String alias = (String)enumeration.nextElement();
                    certList.add((X509Certificate) keyStore.getCertificate(alias));
                }
            } catch (final KeyStoreException ex) {
                throw new CertificateManagerException(ex.getLocalizedMessage(), ex);
            }
        } else {
            LOG.error("{} is not a defined system property.", KEY_STORE_PASSWORD_KEY);
        }

        return certList;
    }


}
