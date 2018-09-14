/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.callback.opensaml.CertificateManager;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerException;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateUtil;
import gov.hhs.fha.nhinc.common.configadmin.DeleteCertificateRequestMessageType;
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
import gov.hhs.fha.nhinc.util.SHA2PasswordUtil;
import gov.hhs.fha.nhinc.util.UtilException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jassmit
 */
public class ConfigAdmin implements EntityConfigAdminPortType {

    private static final String UPDATE_CERTIFICATE_SUCCESS = "Update certificate success";
    private static final String INVALID_USER = "Bad token or Mismatch token";

    private static final Logger LOG = LoggerFactory.getLogger(ConfigAdmin.class);

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
        boolean hashCheck = false;
        try {
            hashCheck = SHA2PasswordUtil.checkPassword(hashToken.getBytes(),
                passKey.getBytes(),
                userName.getBytes());
        } catch (UtilException e) {
            LOG.error("Error while getting the hash token: ", e.getLocalizedMessage(), e);
        }

        return hashCheck;
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
        LOG.debug("listChainOfTrust--begin");
        String alias = request.getAlias();
        if(StringUtils.isBlank(alias)){
            return buildListCertificatesResponseMessageType(false, "Certificate Alias is required for chain-of-trust");
        }

        try {
            ListCertificatesResponseMessageType response = new ListCertificatesResponseMessageType();
            response.getCertList().addAll(buildChain(alias, getTrustStore()));
            return response;
        } catch (KeyStoreException | CertificateEncodingException ex) {
            LOG.error("unable to build chain-of-trust: {}", ex.getMessage(), ex);
            return buildListCertificatesResponseMessageType(false, "ERROR: Unable to build the chain-of-trust.");
        } finally {
            LOG.debug("listChainOfTrust--end");
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
}
