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
import gov.hhs.fha.nhinc.common.configadmin.EditCertificateRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.EditCertificateRequestType;
import gov.hhs.fha.nhinc.common.configadmin.ImportCertificateChainRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ImportCertificateRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListCertificateType;
import gov.hhs.fha.nhinc.common.configadmin.ListCertificatesResponseMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListChainOfTrustRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListKeyStoresRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListKeyStoresResponseMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListTrustStoresRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ListTrustStoresResponseMessageType;
import gov.hhs.fha.nhinc.common.configadmin.SimpleCertificateRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.SimpleCertificateResponseMessageType;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.CoreHelpUtils;
import gov.hhs.fha.nhinc.util.SHA2PasswordUtil;
import gov.hhs.fha.nhinc.util.UtilException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.activation.DataHandler;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.PKCS8Generator;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8EncryptorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.bouncycastle.x509.X509V3CertificateGenerator;
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
    private static final String FILE_JKS_CACERTS = System.getProperty("javax.net.ssl.trustStore");
    private static final String VALIDITY = "certificate.validity";
    private static final String KEYSIZE = "certificate.keysize";
    private static final String ACT_SUCCESSFUL = "successful";
    private static final String ALGORITHM_SHA256_RSA = "SHA256withRSA";

    private static final String CERTIFICATE_REQUEST = "CERTIFICATE REQUEST";
    private static final int DEFAULT_KEYSIZE = 2048;
    private static final int DEFAULT_VALIDITY = 365;

    private static final Logger LOG = LoggerFactory.getLogger(ConfigAdmin.class);
    private static final PropertyAccessor prop = PropertyAccessor.getInstance();

    private static final String FORMAT_ALIAS_INTERMEDIATE = "{0}_intermediate_{1}";
    private static final String FORMAT_ALIAS_ROOT = "{0}_root";
    private static final String FOLDER_BACKUP = "importWizard/backup";
    private static final String FOLDER_NEW = "importWizard/new";
    private static final String FOLDER_TEMP = "importWizard/temp";
    private String filenameKeystore = null;
    private String filenameTruststore = null;
    private KeyStore tempKeystore = null;
    private KeyStore tempTruststore = null;
    private String tempKeystorePath = null;
    private String tempTruststorePath = null;

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
        return buildChain(alias, keyStore.getCertificate(alias), keyStore);
    }

    private static List<ListCertificateType> buildChain(String alias, Certificate serverCert, KeyStore keyStore)
        throws KeyStoreException, CertificateEncodingException {
        Map<String, Certificate> chain = CertificateUtil.getChain(alias, serverCert, keyStore);
        List<ListCertificateType> certList = new ArrayList<>();
        for (Map.Entry<String, Certificate> certEntry : chain.entrySet()) {
            if (null != certEntry && StringUtils.isNotBlank(certEntry.getKey())) {
                certList.add(buildListCertificateType(certEntry.getKey(), certEntry.getValue()));
            }
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

    @Override
    public SimpleCertificateResponseMessageType createCSR(CreateCSRRequestMessageType request) {

        String alias = request.getAlias();
        if(StringUtils.isBlank(alias)){
            return buildSimpleResponse(false, "Alias is required.");
        }

        // keytool -certreq -alias gateway -keystore gateway.jks -file gateway-yyyyMMdd.csr
        try {
            PrivateKeyEntry entry = CertificateUtil.getPrivateKeyEntry(getTempKeystore(), alias, getPasswordKeystore());
            if (null == entry) {
                return buildSimpleResponse(false, "Unable to find KeyPair for the alias: " + alias);
            }
            savePrivateKey(alias, entry.getPrivateKey(), getPasswordKeystore());

            DataHandler certPem = CertificateUtil
                .getDataHandlerForPem(getPemCsrBy(getTempKeystore(), getPasswordKeystore(), alias));
            SimpleCertificateResponseMessageType response = buildSimpleResponse(true, ACT_SUCCESSFUL);
            response.setCsrData(certPem);
            return response;
        } catch (Exception e) {
            LOG.error("Error occured while generating the certificate signing request(CSR): {}", alias, e);
            return buildSimpleResponse(false, "Fail to generate the certificate signing request(CSR): " + alias);
        }

    }

    @Override
    public SimpleCertificateResponseMessageType createCertificate(CreateCertificateRequestMessageType request) {

        String alias = request.getAlias();
        String ou = request.getOrganizationalUnit();
        String o = request.getOrganization();
        String c = request.getCountryName();
        String cn = request.getCommonName();
        if (StringUtils.isBlank(alias) || StringUtils.isBlank(ou) || StringUtils.isBlank(o) || StringUtils.isBlank(c)
            || StringUtils.isBlank(cn)) {
            return buildSimpleResponse(false,
                "Alias, Organizational Unit(OU), Organization(o), Country Name(C) and Common Name(CN) are required.");
        }

        tempKeystore = null;
        if (!copyFile(FILE_JKS_GATEWAY, getTempKeystorePath())) {
            return buildSimpleResponse(false, "Unable to make a copy of gateway-jks.");
        }
        // keytool -genkey -alias gateway -keyalg RSA -keystore gateway.jks -dname CN=<reference number>, OU=NHIN,
        // O=HHS-ONC, C=US -validity 365 -keysize 2048
        try {

            String dn = MessageFormat.format("CN={0}, OU={1}, O={2}, C={3}", cn, ou, o, c);
            KeyPair pair = CoreHelpUtils.generateKeyPair(getKeySize(), null);
            X509Certificate x509cert = generateCertificate(pair, dn, getValidityDays(), ALGORITHM_SHA256_RSA);
            KeyStore jksGatewayNew = getTempKeystore();
            if (jksGatewayNew.containsAlias(alias)) {
                jksGatewayNew.deleteEntry(alias);
            }
            jksGatewayNew.setKeyEntry(alias, pair.getPrivate(), getPasswordKeystore().toCharArray(),
                CoreHelpUtils.getCertificateChain(x509cert));
            CoreHelpUtils.saveJksTo(jksGatewayNew, getPasswordKeystore(), getTempKeystorePath());
        } catch (Exception e) {
            LOG.error("Error occured while generating the certificate for temporary KeyStore: {}", alias, e);
            return buildSimpleResponse(false, "Fail to generate the certificate: " + alias);
        }
        return buildSimpleResponse(true, ACT_SUCCESSFUL);

    }

    private static SimpleCertificateResponseMessageType buildSimpleResponse(boolean status, String msg) {
        SimpleCertificateResponseMessageType resp = new SimpleCertificateResponseMessageType();
        resp.setStatus(status);
        resp.setMessage(msg);
        return resp;
    }

    private KeyStore getTempKeystore() throws CertificateManagerException {
        if (null == tempKeystore) {
            File destinationFile = new File(getTempKeystorePath());
            if(!destinationFile.exists()){
                copyFile(FILE_JKS_GATEWAY, getTempKeystorePath());
            }
            tempKeystore = CertificateUtil.loadKeyStore(CertificateManagerImpl.JKS_TYPE, getPasswordKeystore(),
                getTempKeystorePath());
        }
        return tempKeystore;
    }

    private KeyStore getTempTruststore() throws CertificateManagerException {
        if (null == tempTruststore) {
            File destinationFile = new File(getTempTrustorePath());
            if(!destinationFile.exists()){
                copyFile(FILE_JKS_CACERTS, getTempTrustorePath());
            }
            tempTruststore = CertificateUtil.loadKeyStore(CertificateManagerImpl.JKS_TYPE, getPasswordTruststore(),
                getTempTrustorePath());
        }
        return tempTruststore;
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
                FileUtils.copyFile(sourceFile, destinationFile, false);
                LOG.info("copy successful: {}", destinationFileName);
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
        throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException,
        OperatorCreationException {

        X509Certificate x509Cert = (X509Certificate) keystore.getCertificate(alias);
        X500Principal principal = x509Cert.getSubjectX500Principal();
        PublicKey publicKey = x509Cert.getPublicKey();
        PrivateKey privateKey = (PrivateKey) keystore.getKey(alias, keystorePass.toCharArray());

        PKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(principal, publicKey);
        ContentSigner signGen = new JcaContentSignerBuilder(ALGORITHM_SHA256_RSA).build(privateKey);
        PKCS10CertificationRequest csr = builder.build(signGen);

        PemObject pemObject = new PemObject(CERTIFICATE_REQUEST, csr.getEncoded());
        StringWriter str = new StringWriter();
        PemWriter pemWriter = new PemWriter(str);
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        str.close();

        return str.toString().getBytes();

    }

    private X509Certificate generateCertificate(KeyPair keypair, String dn, int days, String signatureAlgorithm)
        throws InvalidKeyException, SignatureException, CertificateEncodingException, NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

        // add some options
        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certGen.setSubjectDN(new X509Name(dn));
        certGen.setIssuerDN(new X500Principal(dn));
        certGen.setNotBefore(new Date(System.currentTimeMillis()));
        certGen.setNotAfter(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * days));
        certGen.setPublicKey(keypair.getPublic());
        certGen.setSignatureAlgorithm(signatureAlgorithm);
        certGen.addExtension(X509Extensions.ExtendedKeyUsage, true,
            new ExtendedKeyUsage(KeyPurposeId.id_kp_timeStamping));
        return certGen.generate(keypair.getPrivate());
    }

    @Override
    public SimpleCertificateResponseMessageType deleteTemporaryKeystore(SimpleCertificateRequestMessageType request) {
        boolean deleted = false;
        tempKeystore = null;
        tempTruststore = null;
        try {
            deleted = deleteFolder(getPathFolder(FOLDER_TEMP));
        } catch (IOException e) {
            LOG.error("Error occured while cleaning up the Temporary folder: {}", FOLDER_TEMP, e);
            return buildSimpleResponse(false, "Error while occurred cleaning up the temporary folder.");
        }

        return buildSimpleResponse(deleted,
            deleted ? "Temporary files were deleted successfully."
                : "Temporary files did not exist. Skipping temporary file cleanup.");
    }

    private boolean deleteFolder(String pathFolder) throws IOException {
        File tmpFolder = new File(pathFolder);
        if (tmpFolder.exists()) {
            FileUtils.deleteDirectory(tmpFolder);
            return true;
        }
        return false;
    }

    @Override
    public SimpleCertificateResponseMessageType importToKeystore(ImportCertificateChainRequestMessageType request) {
        String alias = request.getAlias();
        if (StringUtils.isBlank(alias) || null == request.getServerCert()) {
            return buildSimpleResponse(false, "Alias and Server Certificate are required.");
        }

        PrivateKey privateKey = readPrivateKey(alias, getPasswordKeystore());
        if (null == privateKey) {
            return buildSimpleResponse(false, "Create a CSR and get SSL Certificates from your CA Providers before replacing existing Certificates with alias: " + alias);
        }

        try {
            Certificate publicKey = CertificateUtil.createCertificate(request.getServerCert());
            if (null == publicKey) {
                return buildSimpleResponse(false, "Server certificate was not uploaded.");
            }

            if (null != request.getRootCert() && CollectionUtils.isNotEmpty(request.getIntermediateList())) {
                // KeyStore
                removeIntermediateFrom(getTempKeystore(), alias);
                Map<String, Certificate> mapCerts = convertCertificateMap(alias, request);
                for (Entry<String, Certificate> entryCert : mapCerts.entrySet()) {
                    getTempKeystore().setCertificateEntry(entryCert.getKey(), entryCert.getValue());
                }

                // TrustStore
                Certificate oldCertificate = CertificateManagerImpl.getInstance().getCertificateBy(alias);
                List<ListCertificateType> oldChain = buildChain(alias, oldCertificate, getTempTruststore());
                for (ListCertificateType item : oldChain) {
                    getTempTruststore().deleteEntry(item.getAlias());
                }

                removeIntermediateFrom(getTempTruststore(), alias);
                for (Entry<String, Certificate> entryCert : mapCerts.entrySet()) {
                    getTempTruststore().setCertificateEntry(entryCert.getKey(), entryCert.getValue());
                }

                saveTempTruststore();
            }

            getTempKeystore().setKeyEntry(alias, privateKey, getPasswordKeystore().toCharArray(),
                CoreHelpUtils.getCertificateChain(publicKey));
            saveTempKeystore();
        } catch (CertificateManagerException | UtilException | KeyStoreException | CertificateEncodingException e) {
            LOG.error("Error occurred while importing to temporary KeyStore/TrustStore: {}", e.getLocalizedMessage(),
                e);
            return buildSimpleResponse(false, "Error occurred while importing to temporary KeyStore/TrustStore.");
        }
        return buildSimpleResponse(true, "Import to temporary KeyStore/TrustStore is successful.");
    }

    private static void savePrivateKey(String alias, PrivateKey privatekey, String password)
        throws CertificateManagerException {
        try (FileWriter fos = new FileWriter(getPathPrivateKey(alias)); JcaPEMWriter pem = new JcaPEMWriter(fos)) {
            Security.addProvider(new BouncyCastleProvider());
            JceOpenSSLPKCS8EncryptorBuilder encryptBuilder = new JceOpenSSLPKCS8EncryptorBuilder(
                PKCS8Generator.PBE_SHA1_3DES);
            encryptBuilder.setRandom(new SecureRandom());
            encryptBuilder.setPasssword(password.toCharArray());

            JcaPKCS8Generator pkcs8 = new JcaPKCS8Generator(privatekey, encryptBuilder.build());
            pem.writeObject(pkcs8.generate());
            pem.flush();
        } catch (IOException | OperatorCreationException e) {
            throw new CertificateManagerException("Error saving the privatekey to file.", e);
        }
    }

    private static PrivateKey readPrivateKey(String alias, String password) {
        try (FileReader reader = new FileReader(getPathPrivateKey(alias));
            PEMParser pemParser = new PEMParser(reader)) {
            Security.addProvider(new BouncyCastleProvider());
            JceOpenSSLPKCS8DecryptorProviderBuilder decryptBuilder = new JceOpenSSLPKCS8DecryptorProviderBuilder();
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");

            PKCS8EncryptedPrivateKeyInfo keyPem = (PKCS8EncryptedPrivateKeyInfo) pemParser.readObject();
            PrivateKeyInfo keyInfo = keyPem.decryptPrivateKeyInfo(decryptBuilder.build(password.toCharArray()));
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyInfo.getEncoded());

            return keyFactory.generatePrivate(keySpec);
        } catch (IOException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException
            | OperatorCreationException | PKCSException e) {
            LOG.error("Error reading privateKey: {}", alias, e);
        }
        return null;
    }

    private static String getPasswordKeystore() {
        return System.getProperty(CertificateManagerImpl.KEY_STORE_PASSWORD_KEY);
    }

    private static String getPasswordTruststore() {
        return System.getProperty(CertificateManagerImpl.TRUST_STORE_PASSWORD_KEY);
    }

    @Override
    public SimpleCertificateResponseMessageType completeImportWizard(SimpleCertificateRequestMessageType request) {
        String pathBackupKeystore = getPathKeystore(FOLDER_BACKUP);
        String pathBackupTruststore = getPathTruststore(FOLDER_BACKUP);
        String pathNewKeystore = getPathKeystore(FOLDER_NEW);
        String pathNewTruststore = getPathTruststore(FOLDER_NEW);

        File tempFile = new File(getTempKeystorePath());
        if (!tempFile.exists()) {
            return buildSimpleResponse(false, "The import process has not been started yet.");
        }

        try {
            deleteFolder(getPathFolder(FOLDER_BACKUP));
            deleteFolder(getPathFolder(FOLDER_NEW));
        } catch (IOException ex) {
            LOG.error("Error occured while cleaning up the import folders: {}, {}", FOLDER_BACKUP, FOLDER_NEW, ex);
            return buildSimpleResponse(false, "Error while occurred cleaning up the import folders.");
        }

        // backup the original
        if (!copyFile(FILE_JKS_GATEWAY, pathBackupKeystore)) {
            return buildSimpleResponse(false, "Error occured while backup KeyStore.");
        }
        if (!copyFile(FILE_JKS_CACERTS, pathBackupTruststore)) {
            return buildSimpleResponse(false, "Error occured while backup TrustStore.");
        }
        // make copy of new KeyStore and TrustStore
        if (!copyFile(getTempKeystorePath(), pathNewKeystore)) {
            return buildSimpleResponse(false, "Error occured while copying new KeyStore.");
        }

        File fileTemp = new File(getTempTrustorePath());
        if (!fileTemp.exists()){
            copyFile(FILE_JKS_CACERTS, pathNewTruststore);
        }else{
            if(!copyFile(getTempTrustorePath(), pathNewTruststore)) {
                return buildSimpleResponse(false, "Error occured while copy new TrustStore.");
            }
        }

        try {
            deleteFolder(getPathFolder(FOLDER_TEMP));
        } catch (IOException ex) {
            LOG.error("Error occured while cleaning up the temporary folder: {}", FOLDER_TEMP, ex);
            return buildSimpleResponse(false, "Error while occurred cleaning up the import folders.");
        }

        tempKeystore = null;
        tempTruststore = null;

        return buildSimpleResponse(true,
            MessageFormat.format(
                "Please replace your existing KeyStore and TrustStore with the ones provided at {0}/importWizard/new. Note: A server restart may be required",
                DIR_NHINC_PROPERTIES));
    }

    private String getFilenameKeystore() {
        if (null == filenameKeystore) {
            File file = new File(FILE_JKS_GATEWAY);
            filenameKeystore = file.getName();
        }
        return filenameKeystore;
    }

    private String getFilenameTruststore() {
        if (null == filenameTruststore) {
            File file = new File(FILE_JKS_CACERTS);
            filenameTruststore = file.getName();
        }
        return filenameTruststore;
    }

    private String getPathFolder(String folder) {
        return MessageFormat.format("{0}/{1}", DIR_NHINC_PROPERTIES, folder);
    }

    private String getPathKeystore(String folder) {
        return MessageFormat.format("{0}/{1}/{2}", DIR_NHINC_PROPERTIES, folder, getFilenameKeystore());
    }

    private String getPathTruststore(String folder) {
        return MessageFormat.format("{0}/{1}/{2}", DIR_NHINC_PROPERTIES, folder, getFilenameTruststore());
    }

    private String getTempKeystorePath() {
        if (null == tempKeystorePath) {
            tempKeystorePath = getPathKeystore(FOLDER_TEMP);
        }
        return tempKeystorePath;
    }

    private String getTempTrustorePath() {
        if (null == tempTruststorePath) {
            tempTruststorePath = getPathTruststore(FOLDER_TEMP);
        }
        return tempTruststorePath;
    }

    private static String getPathPrivateKey(String alias) {
        return MessageFormat.format("{0}/{1}/{2}_privatekey.pem", DIR_NHINC_PROPERTIES, FOLDER_TEMP, alias);
    }

    @Override
    public SimpleCertificateResponseMessageType listTemporaryAlias(SimpleCertificateRequestMessageType request) {
        try {
            SimpleCertificateResponseMessageType response = buildSimpleResponse(true, "Temporary Aliases");
            response.getAliasList().addAll(getAliasKeypair(getTempKeystore(), getPasswordKeystore()));
            return response;
        } catch (CertificateManagerException | KeyStoreException | NoSuchAlgorithmException
            | UnrecoverableEntryException e) {
            LOG.error("Error while getting temporary alias list: {}", e.getLocalizedMessage(), e);
            return buildSimpleResponse(false, "Failed to retrieve alias list");
        }
    }

    private static Map<String, Certificate> convertCertificateMap(String alias,
        ImportCertificateChainRequestMessageType request) throws CertificateManagerException {
        Map<String, Certificate> mapCerts = new HashMap<>();

        Certificate certRoot = CertificateUtil.createCertificate(request.getRootCert());
        if (null != certRoot) {
            mapCerts.put(MessageFormat.format(FORMAT_ALIAS_ROOT, alias), certRoot);
        }
        int indexInt = 0;
        for (DataHandler data : request.getIntermediateList()) {
            if (null != data) {
                mapCerts.put(MessageFormat.format(FORMAT_ALIAS_INTERMEDIATE, alias, indexInt++),
                    CertificateUtil.createCertificate(data));
            }
        }

        return mapCerts;
    }

    @Override
    public SimpleCertificateResponseMessageType undoImportKeystore(ImportCertificateChainRequestMessageType request) {
        if (null == request.getRootCert() || CollectionUtils.isEmpty(request.getIntermediateList())) {
            return buildSimpleResponse(false, "Undoing an import requires the CA Root, and Intermediate certificates.");
        }

        try{
            Map<String, Certificate> removeCerts = convertCertificateMap("noalias", request);
            removeCertificatesFrom(getTempKeystore(), removeCerts.values());
            removeCertificatesFrom(getTempTruststore(), removeCerts.values());
            saveTempTruststore();
            saveTempKeystore();
        } catch (UtilException | KeyStoreException | CertificateManagerException ex) {
            LOG.error("Error occured on undo import in temporary KeyStore: {}", ex.getLocalizedMessage(), ex);
            return buildSimpleResponse(false, "Error occured on undo import in temporary KeyStore.");
        }

        return buildSimpleResponse(true, "Undo import in temporary Keystore successful.");
    }

    private void saveTempKeystore() throws UtilException, CertificateManagerException {
        CoreHelpUtils.saveJksTo(getTempKeystore(), getPasswordKeystore(), getTempKeystorePath());
    }

    private void saveTempTruststore() throws UtilException, CertificateManagerException {
        CoreHelpUtils.saveJksTo(getTempTruststore(), getPasswordTruststore(), getTempTrustorePath());
    }

    private void removeIntermediateFrom(KeyStore keystore, String alias) throws KeyStoreException {
        int indexInt = 0;
        String intermediateAlias = MessageFormat.format(FORMAT_ALIAS_INTERMEDIATE, alias, indexInt++);
        Certificate certificate = keystore.getCertificate(intermediateAlias);
        while (null != certificate) {
            keystore.deleteEntry(intermediateAlias);
            intermediateAlias = MessageFormat.format(FORMAT_ALIAS_INTERMEDIATE, alias, indexInt++);
            certificate = keystore.getCertificate(intermediateAlias);
        }
    }

    private void removeCertificatesFrom(KeyStore keystore, Collection<Certificate> certs) throws KeyStoreException {
        for (Certificate item : certs) {
            String removeAlias =keystore.getCertificateAlias(item);
            if (StringUtils.isNotEmpty(removeAlias)) {
                keystore.deleteEntry(removeAlias);
            }
        }
    }

    private static List<String> getAliasKeypair(KeyStore keystore, String password)
        throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException {
        List<String> kpAliases = new ArrayList<>();

        for(String alias : Collections.list(keystore.aliases())){
            try {
                keystore.getEntry(alias, new KeyStore.PasswordProtection(password.toCharArray()));
                kpAliases.add(alias);
            } catch (UnsupportedOperationException ex) {
                LOG.trace("Ignore certificate without keypair: {}", alias, ex);
            }
        }

        return kpAliases;
    }

}
