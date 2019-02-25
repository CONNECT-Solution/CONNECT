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
package gov.hhs.fha.nhinc.configadmin;

import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.KEY_STORE_PASSWORD_KEY;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.TRUST_STORE_KEY;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.TRUST_STORE_PASSWORD_KEY;
import static gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl.TRUST_STORE_TYPE_KEY;
import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.CA_AUTHORITY_PROPERTY_FILE;

import gov.hhs.fha.nhinc.callback.opensaml.CertificateManager;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerException;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateUtil;
import gov.hhs.fha.nhinc.common.configadmin.CreateCSRRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.CreateCertificateRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.DeleteCertificateRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.DeleteGatewayNewRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.EditCertificateRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.EditCertificateRequestType;
import gov.hhs.fha.nhinc.common.configadmin.ImportCertificateRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListCertificateType;
import gov.hhs.fha.nhinc.common.configadmin.ListCertificatesResponseMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListChainOfTrustRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListKeyStoresRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListKeyStoresResponseMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListTrustStoresRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListTrustStoresResponseMessageType;
import gov.hhs.fha.nhinc.common.configadmin.SimpleCertificateResponseMessageType;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.CoreHelpUtils;
import gov.hhs.fha.nhinc.util.SHA2PasswordUtil;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jassmit
 */
public class ConfigAdmin implements EntityConfigAdminPortType {

    private static final String UPDATE_CERTIFICATE_SUCCESS = "Update certificate success";
    private static final String INVALID_USER = "Bad token or Mismatch token";
    private static final String DIR_NHINC_PROPERTIES = System.getProperty("nhinc.properties.dir");
    private static final String FILE_JKS_GATEWAY = System.getProperty("javax.net.ssl.keyStore");
    private static final String FILE_JKS_GATEWAY_NEW = MessageFormat.format("{0}/{1}", DIR_NHINC_PROPERTIES,
        "gateway_new.jks");
    private static final String VALIDITY = "certificate.validity";
    private static final String KEYSIZE = "certificate.keysize";
    private static final String ACT_SUCCESSFUL = "successful";
    private static final String ALGORITHM_SHA256_RSA = "SHA256withRSA";

    private static final String CERTIFICATE_REQUEST = "CERTIFICATE REQUEST";
    private static final String MIME_TEXT_PLAIN = "text/plain";

    private static final int DEFAULT_KEYSIZE = 2046;
    private static final int DEFAULT_VALIDITY = 365;

    private static final Logger LOG = LoggerFactory.getLogger(ConfigAdmin.class);
    private static final PropertyAccessor prop = PropertyAccessor.getInstance();
    private static KeyStore gatewayNew = null;

    @Override
    public SimpleCertificateResponseMessageType importCertificate(
        ImportCertificateRequestMessageType importCertificateRequest) {
        SimpleCertificateResponseMessageType response = new SimpleCertificateResponseMessageType();

        CertificateManager certManager = CertificateManagerImpl.getInstance();
        if (validateHash(importCertificateRequest.getConfigAssertion().getUserInfo().getUserName(),
            importCertificateRequest.getImportCertRequest().getHashToken(),
            certManager.getTrustStoreSystemProperties().get(TRUST_STORE_PASSWORD_KEY))) {
            try {
                certManager.importCertificate(importCertificateRequest.getImportCertRequest().getAlias(),
                    importCertificateRequest.getImportCertRequest().getCertData(),
                    importCertificateRequest.getImportCertRequest().isRefreshCache());
                response.setStatus(true);
                LOG.info("Certificate imported with alias {} by user {}.", importCertificateRequest.
                    getImportCertRequest().getAlias(), importCertificateRequest.getConfigAssertion().getUserInfo().
                    getUserName());
            } catch (CertificateManagerException ex) {
                LOG.error("Unable to import certificate due to: {}", ex.getLocalizedMessage(), ex);
                formatResponse(response, false, ex.getLocalizedMessage());
            }
        } else {
            formatResponse(response, false, INVALID_USER);
        }
        return response;
    }

    /**
     * @param response
     * @param ex
     */
    private static void formatResponse(SimpleCertificateResponseMessageType response, boolean status, String message) {
        response.setStatus(status);
        response.setMessage(message);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configadmin.EntityConfigAdminPortType#listKeyStores(gov.hhs.fha.nhinc.common.configadmin.
     * ListKeyStoresRequestMessageType)
     */
    @Override
    public ListKeyStoresResponseMessageType listKeyStores(ListKeyStoresRequestMessageType listKeyStoresRequest) {
        ListKeyStoresResponseMessageType response = new ListKeyStoresResponseMessageType();
        CertificateManager certManager = CertificateManagerImpl.getInstance();
        KeyStore keyStore = certManager.getKeyStore();
        response.getCertList().addAll(buildCertificate(certManager, keyStore));

        return response;
    }

    private static boolean validateHash(String userName, String hashToken, String passKey) {
        return SHA2PasswordUtil.checkPassword(hashToken.getBytes(),passKey.getBytes(),userName.getBytes());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.configadmin.EntityConfigAdminPortType#listTrustStores(gov.hhs.fha.nhinc.common.configadmin.
     * ListTrustStoresRequestMessageType)
     */
    @Override
    public ListTrustStoresResponseMessageType listTrustStores(
        ListTrustStoresRequestMessageType listTrustStoresRequest) {
        ListTrustStoresResponseMessageType response = new ListTrustStoresResponseMessageType();
        CertificateManager certManager = CertificateManagerImpl.getInstance();

        if (listTrustStoresRequest.isRefreshCertCache()) {
            certManager.refreshServices();
        }

        KeyStore keyStore = certManager.getTrustStore();
        response.getCertList().addAll(buildCertificate(certManager, keyStore));

        return response;
    }

    private static List<ListCertificateType> buildCertificate(CertificateManager certManager, KeyStore keyStore) {
        List<ListCertificateType> certList = new ArrayList<>();
        Set<String> keyStoreAliases;
        java.security.cert.Certificate jCert;

        try {
            keyStoreAliases = new HashSet<>(Collections.list(keyStore.aliases()));
            for (String alias : keyStoreAliases) {
                ListCertificateType cert = new ListCertificateType();
                cert.setAlias(alias);
                jCert = keyStore.getCertificate(alias);
                cert.setCertData(certManager.transformToHandler(jCert.getEncoded()));
                certList.add(cert);
            }
        } catch (KeyStoreException | CertificateEncodingException e) {
            LOG.error("Unable to fetch keystore: {}", e);
        }

        return certList;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.configadmin.EntityConfigAdminPortType#editCertificate(gov.hhs.fha.nhinc.common.configadmin.
     * EditCertificateRequestMessageType)
     */
    @Override
    public SimpleCertificateResponseMessageType editCertificate(
        EditCertificateRequestMessageType editCertificateRequest) {
        SimpleCertificateResponseMessageType response = new SimpleCertificateResponseMessageType();
        CertificateManager certManager = CertificateManagerImpl.getInstance();

        Map<String, String> keyTrustStoreProperties = certManager.getTrustStoreSystemProperties();
        String storeType = keyTrustStoreProperties.get(TRUST_STORE_TYPE_KEY);
        String storeLoc = keyTrustStoreProperties.get(TRUST_STORE_KEY);
        String passkey = keyTrustStoreProperties.get(TRUST_STORE_PASSWORD_KEY);

        if (validateHash(editCertificateRequest.getConfigAssertion().getUserInfo().getUserName(),
            editCertificateRequest.getEditCertRequest().getHashToken(), passkey)) {
            try {
                formatResponse(response,
                    updateTrustStoreAlias(certManager, editCertificateRequest, storeType, storeLoc, passkey, KeyStore.
                        getInstance(storeType)),
                    UPDATE_CERTIFICATE_SUCCESS);
            } catch (final KeyStoreException ex) {
                LOG.error("Unable to update the Certifiate: ", ex.getLocalizedMessage(), ex);
                formatResponse(response, false, ex.getLocalizedMessage());
            }
        } else {
            formatResponse(response, false, INVALID_USER);
        }

        return response;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.configadmin.EntityConfigAdminPortType#deleteCertificate(gov.hhs.fha.nhinc.common.configadmin.
     * DeleteCertificateRequestMessageType)
     */
    @Override
    public SimpleCertificateResponseMessageType deleteCertificate(
        DeleteCertificateRequestMessageType deleteCertificateRequest) {
        SimpleCertificateResponseMessageType response = new SimpleCertificateResponseMessageType();
        CertificateManager certManager = CertificateManagerImpl.getInstance();
        String alias = deleteCertificateRequest.getAlias();

        if (validateHash(deleteCertificateRequest.getConfigAssertion().getUserInfo().getUserName(),
            deleteCertificateRequest.getHashToken(),
            certManager.getTrustStoreSystemProperties().get(TRUST_STORE_PASSWORD_KEY))) {
            try {
                validateAndDeleteCertificate(response, certManager, alias);
            } catch (CertificateManagerException e) {
                LOG.error("Unable to delete the Certifiate: ", e.getLocalizedMessage(), e);
                formatResponse(response, false, e.getLocalizedMessage());
            }
        } else {
            formatResponse(response, false, INVALID_USER);
        }
        return response;
    }

    /**
     * @param response
     * @param certManager
     * @param alias
     * @throws CertificateManagerException
     */
    private static void validateAndDeleteCertificate(SimpleCertificateResponseMessageType response,
        CertificateManager certManager, String alias) throws CertificateManagerException {
        if (StringUtils.isNotBlank(alias)) {
            formatResponse(response, certManager.deleteCertificate(alias), "Delete certificate success");
        } else {
            formatResponse(response, false, "Delete certificate alias is null or empty");
        }
    }

    private static boolean updateTrustStoreAlias(CertificateManager certManager,
        EditCertificateRequestMessageType editCertificateRequest, final String storeType,
        final String storeLoc, final String passkey, KeyStore storeCert) {
        EditCertificateRequestType certRequestParam = editCertificateRequest.getEditCertRequest();
        boolean isUpdateSuccessful;
        try {
            isUpdateSuccessful = certManager.updateCertificate(certRequestParam.getOldAlias(),
                certRequestParam.getNewAlias(), storeType,
                storeLoc, passkey, storeCert);
        } catch (CertificateManagerException e) {
            LOG.error("Unable to update keystore/truststore: {}", e);
            isUpdateSuccessful = false;
        }
        return isUpdateSuccessful;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.configadmin.EntityConfigAdminPortType#listChainOfTrust(gov.hhs.fha.nhinc.common.configadmin.
     * ListChainOfTrustRequestMessageType)
     */
    @Override
    public ListCertificatesResponseMessageType listChainOfTrust(ListChainOfTrustRequestMessageType request) {
        LOG.debug("listChainOfTrust: begin");
        String alias = request.getAlias();
        if(StringUtils.isBlank(alias)){
            return buildListCertificatesResponseMessageType(false, "Certificate Alias is required for chain of trust");
        }

        try {
            ListCertificatesResponseMessageType response = new ListCertificatesResponseMessageType();
            response.getCertList().addAll(buildChain(alias, getTrustStore()));
            return response;
        } catch (KeyStoreException | CertificateEncodingException ex) {
            LOG.error("unable to build chain-of-trust: {}", ex.getMessage(), ex);
            return buildListCertificatesResponseMessageType(false, "Unable to build the chain of trust.");
        } finally {
            LOG.debug("listChainOfTrust: end");
        }
    }

    private static List<ListCertificateType> buildChain(String alias, KeyStore keyStore)
        throws KeyStoreException, CertificateEncodingException {
        List<Certificate> chain = CertificateUtil.getChain(keyStore.getCertificate(alias), keyStore);
        List<ListCertificateType> certList = new ArrayList<>();
        for (Certificate cert : chain) {
            certList.add(buildListCertificateType(keyStore.getCertificateAlias(cert), cert));
        }
        return certList;
    }

    private static ListCertificateType buildListCertificateType(String alias, Certificate cert) throws CertificateEncodingException{
        ListCertificateType retVal = new ListCertificateType();
        retVal.setAlias(alias);
        retVal.setCertData(CertificateUtil.getDataHandlerFrom(cert));
        return retVal;
    }

    private static ListCertificatesResponseMessageType buildListCertificatesResponseMessageType(Boolean status,
        String message) {
        ListCertificatesResponseMessageType response = new ListCertificatesResponseMessageType();
        response.setStatus(status);
        response.setMessage(message);
        return response;
    }

    private static KeyStore getTrustStore() {
        return CertificateManagerImpl.getInstance().getTrustStore();
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configadmin.EntityConfigAdminPortType#createCSR(gov.hhs.fha.nhinc.common.configadmin.
     * CreateCSRRequestMessageType)
     */
    @Override
    public SimpleCertificateResponseMessageType createCSR(CreateCSRRequestMessageType request) {

        String alias = request.getAlias();
        if(StringUtils.isBlank(alias)){
            return buildSimpleResponse(false, "alias is required.");
        }

        // keytool -certreq -alias gateway -keystore gateway.jks -file gateway-yyyyMMdd.csr
        try {
            ByteArrayDataSource dsCsr = new ByteArrayDataSource(
                getPemCsrBy(getGatewayNew(), System.getProperty(KEY_STORE_PASSWORD_KEY), alias), MIME_TEXT_PLAIN);
            SimpleCertificateResponseMessageType response = buildSimpleResponse(true, ACT_SUCCESSFUL);
            response.setCsrData(new DataHandler(dsCsr));
            return response;
        } catch (Exception e) {
            LOG.error("error while generating the csr: {}", alias, e);
            return buildSimpleResponse(false, "fail to generate the csr: " + alias);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.configadmin.EntityConfigAdminPortType#createCertificate(gov.hhs.fha.nhinc.common.configadmin.
     * CreateCertificateRequestMessageType)
     */
    @Override
    public SimpleCertificateResponseMessageType createCertificate(CreateCertificateRequestMessageType request) {

        String alias = request.getAlias();
        String ou = request.getOrganizationalUnit();
        String o = request.getOrganization();
        String c = request.getCountryName();
        String refNumber = request.getReferenceNumber();
        if (StringUtils.isBlank(alias) || StringUtils.isBlank(ou) || StringUtils.isBlank(o) || StringUtils.isBlank(c)
            || StringUtils.isBlank(refNumber)) {
            return buildSimpleResponse(false,
                "alias, Organizational Unit, Organization, Country Name and Reference Number are required.");
        }

        if (!copyFile(FILE_JKS_GATEWAY, FILE_JKS_GATEWAY_NEW)) {
            return buildSimpleResponse(false, "unable to make a copy of gateway-jks.");
        }
        // keytool -genkey -alias gateway -keyalg RSA -keystore gateway.jks -dname CN=<reference number>, OU=NHIN,
        // O=HHS-ONC, C=US -validity 365 -keysize 2048
        try {

            String dn = MessageFormat.format("CN={0}, OU={1}, O={2}, C={3}", refNumber, ou, o, c);
            KeyPair pair = CoreHelpUtils.generateKeyPair(getKeySize(), null);
            X509Certificate x509cert = CoreHelpUtils.generateCertificate(dn, pair, getValidityDays(),
                ALGORITHM_SHA256_RSA);
            KeyStore jksGatewayNew = getGatewayNew();
            if (jksGatewayNew.containsAlias(alias)) {
                jksGatewayNew.deleteEntry(alias);
            }
            jksGatewayNew.setKeyEntry(alias, pair.getPrivate(),
                System.getProperty(CertificateManagerImpl.KEY_STORE_PASSWORD_KEY).toCharArray(),
                CoreHelpUtils.getCertificateChain(x509cert));
            CoreHelpUtils.saveJksTo(jksGatewayNew, System.getProperty(CertificateManagerImpl.KEY_STORE_PASSWORD_KEY),
                FILE_JKS_GATEWAY_NEW);
        } catch (Exception e) {
            LOG.error("error while generating the certificate for gateway_new.jks: {}", alias, e);
            return buildSimpleResponse(false, "fail to generate the certificate: " + alias);
        }
        return buildSimpleResponse(true, ACT_SUCCESSFUL);

    }

    private static SimpleCertificateResponseMessageType buildSimpleResponse(boolean status, String msg) {
        SimpleCertificateResponseMessageType resp = new SimpleCertificateResponseMessageType();
        resp.setStatus(status);
        resp.setMessage(msg);
        return resp;
    }

    private static KeyStore getGatewayNew() throws CertificateManagerException {
        if (null == gatewayNew) {
            File destinationFile = new File(FILE_JKS_GATEWAY_NEW);
            if(!destinationFile.exists()){
                copyFile(FILE_JKS_GATEWAY, FILE_JKS_GATEWAY_NEW);
            }
            gatewayNew = CertificateUtil.loadKeyStore(CertificateManagerImpl.JKS_TYPE,
                System.getProperty(TRUST_STORE_PASSWORD_KEY), FILE_JKS_GATEWAY_NEW);
        }
        return gatewayNew;
    }

    private static int getKeySize() {
        try{
            return Integer
                .parseInt(prop.getProperty(CA_AUTHORITY_PROPERTY_FILE, KEYSIZE, String.valueOf(DEFAULT_KEYSIZE)));
        }catch (NumberFormatException e) {
            LOG.debug("unable to parse keysize: {}, {}", CA_AUTHORITY_PROPERTY_FILE, KEYSIZE, e);
            return DEFAULT_KEYSIZE;
        }
    }

    private static int getValidityDays() {
        try {
            return Integer
                .parseInt(prop.getProperty(CA_AUTHORITY_PROPERTY_FILE, VALIDITY, String.valueOf(DEFAULT_VALIDITY)));
        } catch (NumberFormatException e) {
            LOG.debug("unable to parse keysize: {}, {}", CA_AUTHORITY_PROPERTY_FILE, VALIDITY, e);
            return DEFAULT_VALIDITY;
        }
    }

    private static boolean copyFile(String sourceFileName, String destinationFileName) {

        File sourceFile = new File(sourceFileName);
        File destinationFile = new File(destinationFileName);

        LOG.debug("copy file from-to: {}, {}", sourceFileName, destinationFileName);

        if (sourceFile.exists()) {
            try {
                org.apache.commons.io.FileUtils.copyFile(sourceFile, destinationFile, false);
                LOG.info("copy successful: {}", destinationFileName);
                gatewayNew = null;
                return true;
            } catch (IOException e) {
                LOG.error("error while copy: {}", destinationFileName, e);
            }
        } else {
            LOG.error("error while copy source not found: {}", sourceFileName);
        }
        return false;
    }

    private byte[] getPemCsrBy(KeyStore keystore, String keystorePass, String alias)
        throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, InvalidKeyException,
        NoSuchProviderException, SignatureException, IOException {

        X509Certificate x509Cert = (X509Certificate) keystore.getCertificate(alias);
        X500Principal principal = x509Cert.getSubjectX500Principal();
        PublicKey publicKey = x509Cert.getPublicKey();
        PrivateKey privateKey = (PrivateKey) keystore.getKey(alias, keystorePass.toCharArray());

        PKCS10CertificationRequest csr = new PKCS10CertificationRequest(ALGORITHM_SHA256_RSA, principal, publicKey,
            null, privateKey);

        PemObject pemObject = new PemObject(CERTIFICATE_REQUEST, csr.getEncoded());
        StringWriter str = new StringWriter();
        PemWriter pemWriter = new PemWriter(str);
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        str.close();

        return str.toString().getBytes();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.configadmin.EntityConfigAdminPortType#deleteGatewayNew(gov.hhs.fha.nhinc.common.configadmin.
     * DeleteGatewayNewRequestMessageType)
     */
    @Override
    public SimpleCertificateResponseMessageType deleteGatewayNew(DeleteGatewayNewRequestMessageType request) {
        File deleteFile = new File(FILE_JKS_GATEWAY_NEW);
        if (deleteFile.exists() && deleteFile.delete()) {
            return buildSimpleResponse(true, "gateway-new delete successful.");
        }
        return buildSimpleResponse(false, "gateway-new doesn't exist.");
    }


}
