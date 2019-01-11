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
package gov.hhs.fha.nhinc.admingui.managed;

import static gov.hhs.fha.nhinc.admingui.util.HelperUtil.execPFHideDialog;
import static gov.hhs.fha.nhinc.admingui.util.HelperUtil.execPFShowDialog;

import gov.hhs.fha.nhinc.admingui.services.CertificateManagerService;
import gov.hhs.fha.nhinc.admingui.services.impl.CertificateManagerServiceImpl;
import gov.hhs.fha.nhinc.admingui.util.GUIConstants.COLOR_CODING_CSS;
import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateDTO;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerException;
import gov.hhs.fha.nhinc.common.configadmin.SimpleCertificateResponseMessageType;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
@ManagedBean(name = "certificateBean")
@ViewScoped
public class CertficateBean {

    private List<CertificateDTO> keystores;
    private List<CertificateDTO> truststores;
    private final CertificateManagerService service;
    private List<CertificateDTO> importCertificate;
    private static final String TRUST_STORE_MSG = "trustStoreMsg";
    private static final String KEY_STORE_MSG = "keyStoreMsg";
    private static final String IMPORT_CERT_EXPIRY_MSG_ID = "importCertInfoMsg";
    private static final String IMPORT_CERT_ERR_MSG_ID = "importCertErrorMsg";
    private static final String VIEW_CERT_ERR_MSG_ID = "viewCertErrorMsg";
    private static final String IMPORT_PASS_ERR_MSG_ID = "importPassKeyErrorMsg";
    private static final String DELETE_PASS_ERR_MSG_ID = "deletePassKeyErrorMsg";
    private static final String EDIT_PASS_ERROR_MSG = "editPassKeyErrorMsg";
    private static final String ALIAS_PLACEHOLDER = "Enter Alias";
    private static final String BAD_MISMATCH_TOKEN = "Bad token or Mismatch token";
    private UploadedFile importCertFile;
    private CertificateDTO selectedCertificate;
    private CertificateDTO selectedTSCertificate;
    private CertificateDTO backupCertificate;
    private String trustStorePasskey;
    private static final String HASH_TOKEN = "certHashToken";
    private static final Logger LOG = LoggerFactory.getLogger(CertficateBean.class);
    private boolean expiredCert;
    private boolean rememberMe;
    private String oldAlias;
    private boolean refreshCache;
    private Map<String, CertificateDTO> chainOfTrust;
    private List<CertificateDTO> selectedCertsChain = new ArrayList<>();
    private List<String> keyStoreColorCodeList = new ArrayList<>();
    private List<String> trustStoreColorCodeList = new ArrayList<>();
    private boolean isChainCompleted;

    public CertficateBean() {
        service = new CertificateManagerServiceImpl();
        fetchKeyStore();
        fetchTrustStore();
    }

    public List<String> getKeyStoreColorCodeList() {
        return keyStoreColorCodeList;
    }

    public List<String> getTrustStoreColorCodeList() {
        return trustStoreColorCodeList;
    }

    public String getKeyStoreLocation() {
        return service.getKeyStoreLocation();
    }

    public String getTrustStoreLocation() {
        return service.getTrustStoreLocation();
    }

    public List<CertificateDTO> getKeystores() {
        return keystores;
    }

    public void refreshCacheForTrustStore() {
        try {
            truststores = setColorCodingStyle(service.refreshTrustStores(true), keyStoreColorCodeList);
        } catch (CertificateManagerException ex) {
            LOG.error("Unable to refresh certificate cache {}", ex.getLocalizedMessage(), ex);
            HelperUtil.addMessageError(TRUST_STORE_MSG, ex.getLocalizedMessage());
        }
    }

    public CertificateDTO getSelectedCertificate() {
        return selectedCertificate;
    }

    public void setSelectedCertificate(CertificateDTO selectedCertificate) {
        this.selectedCertificate = selectedCertificate;
    }

    public List<CertificateDTO> getTruststores() {
        return truststores;
    }

    public String getTrustStorePasskey() {
        return trustStorePasskey;
    }

    public void setTrustStorePasskey(String trustStorePasskey) {
        this.trustStorePasskey = trustStorePasskey;
    }

    public List<CertificateDTO> getImportCertificate() {
        return importCertificate;
    }

    public void setImportCertificate(List<CertificateDTO> importCertificate) {
        this.importCertificate = importCertificate;
    }

    private static boolean isHashTokenEmpty() {
        return StringUtils.isBlank(getHashTokenFromSession());
    }

    private static String getHashTokenFromSession() {
        return (String) HelperUtil.getHttpSession(false).getAttribute(HASH_TOKEN);
    }

    public CertificateDTO getSelectedTSCertificate() {
        return selectedTSCertificate;
    }

    public void setSelectedTSCertificate(CertificateDTO selectedTSCertificate) {
        this.selectedTSCertificate = selectedTSCertificate;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public Map<String, CertificateDTO> getChainOfTrust() {
        return chainOfTrust;
    }

    public List<CertificateDTO> getSelectedCertsChain() {
        return selectedCertsChain;
    }

    public void setSelectedCertsChain(List<CertificateDTO> selectedList) {
        selectedCertsChain = selectedList;
    }

    public boolean getIsChainCompleted() {
        return isChainCompleted;
    }

    public void importFileUpload(FileUploadEvent event) {
        importCertFile = event.getFile();
        expiredCert = false;
        if (importCertFile != null) {
            importCertificate = new ArrayList<>();
            CertificateDTO cert = service.createCertificate(importCertFile.getContents());
            cert.setAlias(ALIAS_PLACEHOLDER);
            checkCertValidity(cert);
            if (!expiredCert) {
                if (cert.getExpiresInDays() > 30 && cert.getExpiresInDays() <= 90) {
                    HelperUtil.addMessageInfo(IMPORT_CERT_EXPIRY_MSG_ID, "This Certificate is expiring soon.");
                }
                importCertificate.add(cert);
            }
        }
    }

    private void checkCertValidity(CertificateDTO cert) {
        try {
            cert.getX509Cert().checkValidity();
        } catch (CertificateExpiredException ex) {
            LOG.error("Certificate expired {}", ex.getLocalizedMessage(), ex);
            expiredCert = true;
            HelperUtil.addMessageError(IMPORT_CERT_ERR_MSG_ID, "Expired Certificate");
        } catch (CertificateNotYetValidException ex) {
            LOG.error("Certificate not valid yet {}", ex.getLocalizedMessage(), ex);
            HelperUtil.addMessageError(IMPORT_CERT_ERR_MSG_ID, "Certificate not valid yet");
        }
    }

    public void onAliasValueEdit(CellEditEvent event) {
        if (CollectionUtils.isNotEmpty(importCertificate)) {
            importCertificate.get(0).setAlias((String) event.getNewValue());
        }
    }

    public void openTrustStorePasskeyDlgForDelete() throws CertificateManagerException {
        if (selectedTSCertificate == null) {
            HelperUtil.addMessageError(TRUST_STORE_MSG, "Select a certificate to delete");
        } else if (isHashTokenEmpty()) {
            // clear previous user enter
            trustStorePasskey = null;
            execPFShowDialog("deletePassKeyDlg");
        } else {
            validateAndDeleteCertificate();
        }
    }

    public void openTrustStorePasskeyDlgForUpdate() throws CertificateManagerException {
        if (selectedTSCertificate == null) {
            HelperUtil.addMessageError(TRUST_STORE_MSG, "Select a certificate to update");
        } else if (isHashTokenEmpty()) {
            execPFShowDialog("editPassKeyDlg");
        } else {
            validateAndUpdateCertificate();
        }
    }

    public void openTrustStorePasskeyDlgForImport() throws CertificateManagerException {
        if (selectedCertificate == null) {
            HelperUtil.addMessageError(IMPORT_PASS_ERR_MSG_ID, "Select certificate for import");
        } else if (StringUtils.isBlank(selectedCertificate.getAlias()) || ALIAS_PLACEHOLDER.equals(selectedCertificate.
            getAlias())) {
            HelperUtil.addMessageError(IMPORT_PASS_ERR_MSG_ID, "Enter an alias for the certificate");
        } else if (isHashTokenEmpty()) {
            execPFShowDialog("importPassKeyDlg");
        } else {
            validateAndImportCertificate();
        }
    }

    public void validateAndDeleteCertificate() throws CertificateManagerException {
        if (isHashTokenEmpty()) {
            String hashToken = service.getHashToken(trustStorePasskey);
            if (deleteCertificate(hashToken)) {
                saveHashToken(hashToken);
                execPFHideDialog("deletePassKeyDlg");
            }
        } else {
            deleteCertificate(getHashTokenFromSession());
        }
        trustStorePasskey = null;
    }

    private void saveHashToken(String hashToken) {
        if (isRememberMe()) {
            HelperUtil.getHttpSession(false).setAttribute(HASH_TOKEN, hashToken);
        }
    }

    public void validateAndImportCertificate() throws CertificateManagerException {
        if (isHashTokenEmpty()) {
            String hashToken = service.getHashToken(trustStorePasskey);
            if (importSelectedCertificate(hashToken)) {
                saveHashToken(hashToken);
                execPFHideDialog("importPassKeyDlg");
            } else {
                HelperUtil.addMessageError(IMPORT_PASS_ERR_MSG_ID, BAD_MISMATCH_TOKEN);
            }
        } else {
            importSelectedCertificate(getHashTokenFromSession());
        }
        trustStorePasskey = null;
    }

    private boolean importSelectedCertificate(String hashToken) throws CertificateManagerException {
        LOG.info("importSelectedCertificate");
        boolean importStatus = false;
        if (selectedCertificate != null && StringUtils.isNotBlank(selectedCertificate.getAlias())) {
            if (!service.isAliasInUse(selectedCertificate.getAlias(), service.fetchTrustStores())) {
                importStatus = importAction(hashToken);
            } else {
                HelperUtil.addMessageError(IMPORT_CERT_ERR_MSG_ID, "Alias already in use in TrustStore");
            }
        } else {
            if (null == selectedCertificate) {
                HelperUtil.addMessageError(IMPORT_CERT_ERR_MSG_ID, "Select a certificate for import");
            }
            if (selectedCertificate != null && StringUtils.isBlank(selectedCertificate.getAlias())) {
                HelperUtil.addMessageError(IMPORT_CERT_ERR_MSG_ID, "Enter an alias for importing certificate");
            }
        }
        return importStatus;
    }

    private boolean importAction(String hashToken) {
        boolean importStatus = false;
        try {
            importStatus = service.importCertificate(selectedCertificate, refreshCache, hashToken);
            truststores = service.refreshTrustStores(false);
            importCertFile = null;
            importCertificate = null;
            selectedCertificate = null;
            refreshCache = false;
            execPFHideDialog("importCertDlg");
            LOG.info("importCertificate -- successful");
        } catch (Exception ex) {
            LOG.error("Unable to import certificate {}", ex.getLocalizedMessage(), ex);
            HelperUtil.addMessageError(IMPORT_CERT_ERR_MSG_ID, ex.getLocalizedMessage());
        }
        return importStatus;
    }

    public void validateAndUpdateCertificate() throws CertificateManagerException {
        if (isHashTokenEmpty()) {
            String hashToken = service.getHashToken(trustStorePasskey);
            if (updateSelectedCertificateTS(hashToken)) {
                saveHashToken(hashToken);
                execPFHideDialog("editPassKeyDlg");
            } else {
                HelperUtil.addMessageError(EDIT_PASS_ERROR_MSG, BAD_MISMATCH_TOKEN);
            }
        } else {
            updateSelectedCertificateTS(getHashTokenFromSession());
        }
        trustStorePasskey = null;
    }

    public boolean updateSelectedCertificateTS(String hashToken) throws CertificateManagerException {
        if (selectedTSCertificate != null) {
            return updateCertificate(service.fetchTrustStores(), VIEW_CERT_ERR_MSG_ID, hashToken);
        }
        return false;
    }

    private boolean updateCertificate(List<CertificateDTO> certsForAliasCheck, String uiElement, String hashToken) {
        LOG.info("updateSelectedCertificate");
        boolean updateStatus = false;
        backupCertificate = selectedTSCertificate;
        String alias = selectedTSCertificate.getAlias();

        if (StringUtils.isNotBlank(alias) && !service.isAliasInUse(alias, certsForAliasCheck)) {
            try {
                updateStatus = service.updateCertificate(oldAlias, selectedTSCertificate, hashToken);
                postUpdateAction(updateStatus, uiElement, alias);
            } catch (CertificateManagerException ex) {
                LOG.error("Unable to update certificate {}", ex.getLocalizedMessage(), ex);
                HelperUtil.addMessageError(uiElement, ex.getLocalizedMessage());
            }
        } else {
            selectedTSCertificate.setAlias(oldAlias);
            HelperUtil.addMessageError(uiElement, "Alias already in use");
        }
        return updateStatus;
    }

    private void postUpdateAction(boolean updateStatus, String uiElement, String aliasToRefresh)
        throws CertificateManagerException {
        if (updateStatus) {
            refreshCerts();
            oldAlias = aliasToRefresh;
            HelperUtil.addMessageInfo(uiElement, "Update certificate successful");
        } else {
            selectedTSCertificate = backupCertificate;
        }
    }

    private List<CertificateDTO> refreshCerts() throws CertificateManagerException {
        List<CertificateDTO> certs;
        certs = service.refreshTrustStores(false);
        truststores = setColorCodingStyle(certs, trustStoreColorCodeList);
        return certs;
    }

    private boolean deleteCertificate(String hashToken) {
        boolean deleteStatus = false;
        try {
            SimpleCertificateResponseMessageType response = service.deleteCertificateFromTrustStore(
                selectedTSCertificate.getAlias(), hashToken);
            deleteStatus = response.isStatus();
            if (deleteStatus) {
                refreshCerts();
                selectedTSCertificate = null;
            } else {
                HelperUtil.addMessageError(DELETE_PASS_ERR_MSG_ID, response.getMessage());
            }
        } catch (CertificateManagerException ex) {
            LOG.error("Unable to delete certificate {}", ex.getLocalizedMessage(), ex);
            HelperUtil.addMessageError(DELETE_PASS_ERR_MSG_ID, ex.getLocalizedMessage());
        }
        return deleteStatus;
    }

    private void fetchKeyStore() {
        try {
            keystores = setColorCodingStyle(service.fetchKeyStores(), keyStoreColorCodeList);
        } catch (CertificateManagerException e) {
            LOG.error("Unable to get certificate details {}", e.getLocalizedMessage(), e);
            HelperUtil.addMessageError(KEY_STORE_MSG, "Unable to fetch certificate details");
        }
    }

    private void fetchTrustStore() {
        try {
            truststores = setColorCodingStyle(service.fetchTrustStores(), trustStoreColorCodeList);
        } catch (CertificateManagerException e) {
            LOG.error("Unable to get certificate details {}", e.getLocalizedMessage(), e);
            HelperUtil.addMessageError(TRUST_STORE_MSG, "Unable to fetch certificate details");
        }
    }

    public CertificateDTO viewCertificate() {
        return viewCert("viewCertDlg", TRUST_STORE_MSG);
    }

    public CertificateDTO viewCertificateKS() {
        return viewCert("viewCertDlgKS", KEY_STORE_MSG);
    }

    private static List<CertificateDTO> setColorCodingStyle(List<CertificateDTO> certDTOs,
        List<String> storeColorCodeList) {
        for (CertificateDTO dto : certDTOs) {
            if (dto.getExpiresInDays() <= 30) {
                dto.setExpiryColorCoding(COLOR_CODING_CSS.RED.toString());
                if (!storeColorCodeList.contains(COLOR_CODING_CSS.RED.toString())) {
                    storeColorCodeList.add(COLOR_CODING_CSS.RED.toString());
                }
            } else if (dto.getExpiresInDays() > 30 && dto.getExpiresInDays() <= 90) {
                dto.setExpiryColorCoding(COLOR_CODING_CSS.YELLOW.toString());
                if (!storeColorCodeList.contains(COLOR_CODING_CSS.YELLOW.toString())) {
                    storeColorCodeList.add(COLOR_CODING_CSS.YELLOW.toString());
                }
            } else {
                dto.setExpiryColorCoding(COLOR_CODING_CSS.GREEN.toString());
                if (!storeColorCodeList.contains(COLOR_CODING_CSS.GREEN.toString())) {
                    storeColorCodeList.add(COLOR_CODING_CSS.GREEN.toString());
                }
            }
        }
        return certDTOs;
    }

    private CertificateDTO viewCert(String dialogId, String uiElement) {
        if (selectedTSCertificate != null) {
            oldAlias = selectedTSCertificate.getAlias();
            execPFShowDialog(dialogId);
        } else {
            HelperUtil.addMessageError(uiElement, "Please choose a certificate to view details");
        }
        return selectedTSCertificate;
    }

    public boolean isRefreshCache() {
        return refreshCache;
    }

    public void setRefreshCache(boolean refreshCache) {
        this.refreshCache = refreshCache;
    }

    public void viewChainOfTrust() {
        chainOfTrust = new HashMap<>();
        selectedCertsChain = new ArrayList<>();
        selectedCertsChain.add(selectedTSCertificate);
        isChainCompleted = false;

        if (null == selectedTSCertificate) {
            HelperUtil.addMessageError(TRUST_STORE_MSG, "Please choose a certificate to view chain.");
            return;
        }

        try {
            chainOfTrust = buildChainOfTrustMap(service.listChainOfTrust(selectedTSCertificate.getAlias()));
            // Below line overrides the user selected certificate in chainOfTrust map.
            // This is done in order to highlight the alias on View Chain Of Trust when the page first loads.
            chainOfTrust.put(selectedTSCertificate.getAlias(), selectedTSCertificate);
            isChainCompleted = checkCompletedChain(chainOfTrust);
        } catch (CertificateManagerException ex) {
            LOG.error("Error while calling server to get certificate chain: {}", ex.getMessage(), ex);
        }
        HelperUtil.execPFShowDialog("wgvDlgChainOfTrust");
    }

    private static Map<String, CertificateDTO> buildChainOfTrustMap(List<CertificateDTO> list) {
        Map<String, CertificateDTO> retVal = new LinkedHashMap<>();

        for (CertificateDTO item : list) {
            retVal.put(item.getAlias(), item);
        }

        return retVal;
    }

    private static boolean checkCompletedChain(Map<String, CertificateDTO> chain) {
        List<String> certAKIDs = new ArrayList<>();

        for (CertificateDTO item : chain.values()) {
            certAKIDs.add(item.getAuthorityKeyID());
        }

        for (CertificateDTO item : chain.values()) {
            String issuerId = item.getIssuerUniqueIdentifier();
            if (null != issuerId && !certAKIDs.contains(issuerId)) {
                return false;
            }
        }
        return true;
    }

}
