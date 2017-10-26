/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.services.impl;

import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.JKS_TYPE;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.PKCS11_TYPE;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.TRUST_STORE_KEY;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.TRUST_STORE_PASSWORD_KEY;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.TRUST_STORE_TYPE_KEY;

import gov.hhs.fha.nhinc.admingui.event.model.Certificate;
import gov.hhs.fha.nhinc.admingui.services.CertificateManagerService;
import gov.hhs.fha.nhinc.admingui.util.X509CertificateHelper;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManager;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerException;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class CertificateManagerServiceImpl implements CertificateManagerService {

    private static final Logger LOG = LoggerFactory.getLogger(CertificateManagerServiceImpl.class);
    private final CertificateManager cmHelper = CertificateManagerImpl.getInstance();
    private final X509CertificateHelper x509CertificateHelper = new X509CertificateHelper();

    @Override
    public List<Certificate> fetchKeyStores() {
        return buildCertificateList(cmHelper.getKeyStore());
    }

    @Override
    public List<Certificate> fetchTrustStores() {
        return buildCertificateList(cmHelper.getTrustStore());
    }

    @Override
    public String getKeyStoreLocation() {
        return cmHelper.getKeyStoreLocation();
    }

    @Override
    public String getTrustStoreLocation() {
        return cmHelper.getTrustStoreLocation();
    }

    @Override
    public List<Certificate> refreshKeyStores() {
        return buildCertificateList(cmHelper.refreshKeyStore());
    }

    @Override
    public Certificate createCertificate(byte[] data) {
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(data);
            X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(bais);
            return x509CertificateHelper.buildCertificate(cert);
        } catch (CertificateException ex) {
            LOG.error("Unable to extract a valid X509 certificate {}", ex.getLocalizedMessage(), ex);
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                }
            } catch (IOException ex) {
                LOG.error("Unable to close the inputstream {}", ex.getLocalizedMessage(), ex);
            }
        }
        return null;
    }

    private List<Certificate> buildCertificateList(KeyStore keystore) {
        List<Certificate> certs = null;
        try {
            Enumeration<String> aliases = keystore.aliases();
            if (aliases != null) {
                long i = 1;
                certs = new ArrayList<>();
                while (aliases.hasMoreElements()) {
                    String alias = aliases.nextElement();
                    java.security.cert.Certificate jCert = keystore.getCertificate(alias);
                    Certificate obj = x509CertificateHelper.buildCertificate((X509Certificate) jCert);

                    obj.setId(i++);
                    obj.setAlias(alias);
                    certs.add(obj);
                }
            }
        } catch (KeyStoreException ex) {
            LOG.error("Unable to fetch keystore: {}", ex);
        }
        return certs;
    }

    @Override
    public List<Certificate> refreshTrustStores() {
        return buildCertificateList(cmHelper.refreshTrustStore());
    }

    /**
     * Checks if alias already exists in the truststore
     *
     * @param cert
     * @return
     */
    @Override
    public boolean isAliasInUse(Certificate cert) {
        List<Certificate> truststores = fetchTrustStores();
        String alias = cert.getAlias();
        for (Certificate trustCert : truststores) {
            if (trustCert.getAlias().equalsIgnoreCase(alias)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isLeafOnlyCertificate(Certificate cert) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * import a given certificate into the TrustStore
     *
     * @param cert
     * @param password
     * @throws gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerException
     */
    @Override
    public void importCertificate(Certificate cert) throws CertificateManagerException {
        final Map<String, String> trustStoreProperties = cmHelper.getTrustStoreSystemProperties();
        String storeType = trustStoreProperties.get(TRUST_STORE_TYPE_KEY);
        final String storeLoc = trustStoreProperties.get(TRUST_STORE_KEY);
        final String passkey = trustStoreProperties.get(TRUST_STORE_PASSWORD_KEY);
        if (storeType == null) {
            LOG.warn("{} is not defined. Switch to use JKS by default", TRUST_STORE_TYPE_KEY);
            storeType = JKS_TYPE;
        }

        if (StringUtils.isNotBlank(passkey) && !(JKS_TYPE.equals(storeType) && storeLoc == null) && cert != null) {
            addCertificateToTrustStore(storeType, storeLoc, passkey, cert);
        } else {
            LOG.info("importCertificate -- validation failed");
            if (JKS_TYPE.equals(storeType) && storeLoc == null) {
                LOG.error("{} is not defined.", TRUST_STORE_KEY);
            }
            if (StringUtils.isBlank(passkey)) {
                LOG.error("invalid password");
            }
            if (null == cert) {
                LOG.error("Certificate is null");
            }
        }
    }

    private static void closeFiles(FileInputStream is, FileOutputStream os) {
        try {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        } catch (final IOException ex) {
            LOG.error("Unable to close File Stream: {}", ex.getLocalizedMessage(), ex);
        }
    }

    private static boolean addCertificateToTrustStore(String storeType, String storeLoc, String passkey,
        Certificate cert) throws CertificateManagerException {
        FileInputStream is = null;
        FileOutputStream os = null;
        boolean isImportSuccessful = false;
        try {
            KeyStore secretStore = KeyStore.getInstance(storeType);
            if (!PKCS11_TYPE.equalsIgnoreCase(storeType)) {
                is = new FileInputStream(storeLoc);
            }
            secretStore.load(is, passkey.toCharArray());
            secretStore.setCertificateEntry(cert.getAlias(), cert.getX509Cert());
            os = new FileOutputStream(storeLoc);
            secretStore.store(os, passkey.toCharArray());
            isImportSuccessful = true;
        } catch (final IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException ex) {
            LOG.error("Unable to add the Certifiate: ", ex.getLocalizedMessage(), ex);
            throw new CertificateManagerException(ex.getMessage(), ex);
        } finally {
            closeFiles(is, os);
        }
        return isImportSuccessful;
    }

    @Override
    public boolean deleteCertificateFromTrustStore(String alias) throws
    CertificateManagerException {
        final Map<String, String> trustStoreProperties = cmHelper.getTrustStoreSystemProperties();
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
            if (tstore.containsAlias(alias)) {
                tstore.deleteEntry(alias);
                os = new FileOutputStream(storeLoc);
                tstore.store(os, passkey.toCharArray());
                isDeleteSuccessful = true;
            }
        } catch (final IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException ex) {
            LOG.error("Unable to delete the Certifiate: ", ex.getLocalizedMessage(), ex);
            throw new CertificateManagerException(ex.getMessage(), ex);
        } finally {
            closeFiles(is, os);
        }
        return isDeleteSuccessful;
    }

    @Override
    public boolean validateTrustStorePassKey(String passkey) {
        return cmHelper.getTrustStoreSystemProperties().get(TRUST_STORE_PASSWORD_KEY).equals(passkey);
    }
}
