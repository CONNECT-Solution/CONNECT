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
import gov.hhs.fha.nhinc.admingui.services.PropertyService;
import gov.hhs.fha.nhinc.admingui.services.impl.CertificateManagerServiceImpl;
import gov.hhs.fha.nhinc.admingui.services.impl.PropertyServiceImpl;
import gov.hhs.fha.nhinc.admingui.util.GUIConstants.COLOR_CODING_CSS;
import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateDTO;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerException;
import gov.hhs.fha.nhinc.common.configadmin.SimpleCertificateResponseMessageType;
import gov.hhs.fha.nhinc.common.propertyaccess.PropertyType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.CoreHelpUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.activation.DataHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.helpers.IOUtils;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
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
    private static final String ERROR_INPUTSTREAM = "unable to open input-stream from response";
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

    private String alias;
    private String exchangeType;
    private String commonName;
    private String organizationalUnit;
    private String organization;
    private String countryName;
    private SortedSet<String> cacheAlias;

    private PropertyService propertyService = new PropertyServiceImpl();
    private String csrText;
    private String csrFileType;
    private String[] tabTitles = new String[] { "Start", "Create Certificate", "Certificate Signing Request",
            "CA Providers", "Import SSL Certificates" };
    private int importWizardTabIndex = 0;
    private Map<String, String> caProperties = null;
    private Map<String, String> caLinks = null;

    private UploadedFile uploadedFileServer;
    private Map<String, UploadedFile> listIntermediate = null;
    private UploadedFile uploadedFileRoot;
    private CertificateDTO certServer;
    private CertificateDTO certRoot;
    private boolean[] disableNext = new boolean[] { true, true, true, true, true, true };
    private boolean[] disableTab = new boolean[] {false, true, true, true, true, true};
    private boolean disableImportAction = true;

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

    private static boolean validateCertificate(CertificateDTO cert) {
        if (null == cert) {
            HelperUtil.addMessageError(null, "Certificate is invalid");
            return false;
        }
        try {
            cert.getX509Cert().checkValidity();
        } catch (CertificateExpiredException ex) {
            LOG.error("Certificate expired {}", ex.getLocalizedMessage(), ex);
            HelperUtil.addMessageError(null, "Expired Certificate");
            return false;
        } catch (CertificateNotYetValidException ex) {
            LOG.error("Certificate not valid yet {}", ex.getLocalizedMessage(), ex);
            HelperUtil.addMessageError(null, "Certificate not valid yet");
            return false;
        }
        if (cert.getExpiresInDays() > 30 && cert.getExpiresInDays() <= 90) {
            HelperUtil.addMessageInfo(null, "This Certificate is expiring soon.");
        }
        return true;
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
        } else if (StringUtils.isBlank(selectedCertificate.getAlias())
            || ALIAS_PLACEHOLDER.equals(selectedCertificate.getAlias())) {
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
            }
        } else {
            deleteCertificate(getHashTokenFromSession());
        }
        execPFHideDialog("deletePassKeyDlg");
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
            SimpleCertificateResponseMessageType response = service
                .deleteCertificateFromTrustStore(selectedTSCertificate.getAlias(), hashToken);
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
        setOrganizationalUnit(null);
        setOrganization(null);
        setCountryName(null);
        if (StringUtils.isNotBlank(exchangeType)) {
            String[] args = exchangeType.split(",");
            if (args.length == 3) {
                setOrganizationalUnit(args[0].trim());
                setOrganization(args[1].trim());
                setCountryName(args[2].trim());
            }
        }
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getOrganizationalUnit() {
        return organizationalUnit;
    }

    public void setOrganizationalUnit(String organizationalUnit) {
        this.organizationalUnit = organizationalUnit;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getImportWizardTabIndex() {
        return importWizardTabIndex;
    }

    public void setImportWizardTabIndex(int importWizardTabIndex) {
        if (importWizardTabIndex == 0) {
            cacheAlias = null; // clear out the cache on start importWizard

        }
        enableTab(importWizardTabIndex);
        this.importWizardTabIndex = importWizardTabIndex;
    }

    public void createCertificate() {

        List<String> serverValidation = new ArrayList<>();
        if (StringUtils.isBlank(getAlias())) {
            serverValidation.add("Alias is required.");
        }
        if (StringUtils.isBlank(getCommonName())) {
            serverValidation.add("Common Name (CN) s is required.");
        }
        if (StringUtils.isBlank(getOrganizationalUnit())) {
            serverValidation.add("Organizational Unit (OU) is required.");
        }
        if (StringUtils.isBlank(getOrganization())) {
            serverValidation.add("Organization (O) is required.");
        }
        if (StringUtils.isBlank(getCountryName())) {
            serverValidation.add("Country Name (C) is required.");
        }

        if (CollectionUtils.isNotEmpty(serverValidation)) {
            validationErrors(serverValidation);
            return;
        }


        boolean status = service.createCertificate(getAlias(), getCommonName(), getOrganizationalUnit(),
            getOrganization(), getCountryName());
        if (status) {
            HelperUtil.addMessageInfo(null,
                MessageFormat.format("Successfully created certificate for: {0}", getAlias()));
            cacheAlias.add(getAlias());
            createCSR();
            disableNext[1] = false;


            //Clear out the fields so they have to type it in again and cant spam create button.
            setCommonName(null);
            setOrganization(null);
            setOrganizationalUnit(null);
            setCountryName(null);
            setExchangeType(null);
        } else {
            HelperUtil.addMessageError(null, MessageFormat.format("Failed to create certificate for: {0}", getAlias()));
        }
    }

    public void createCSR() {
        if (StringUtils.isBlank(getAlias())) {
            csrText = null;
            csrFileType = null;
            return;
        }
        SimpleCertificateResponseMessageType response = service.createCSR(getAlias());

        if (null != response && response.isStatus()) {
            HelperUtil.addMessageInfo(null, MessageFormat.format("Successfully created CSR for: {0}", getAlias()));
            if (null != response.getCsrData()) {
                DataHandler data = response.getCsrData();
                try {
                    csrText = IOUtils.toString(data.getInputStream());
                    csrFileType = data.getContentType();
                } catch (IOException e) {
                    LOG.error(ERROR_INPUTSTREAM, e.getLocalizedMessage(), e);
                    HelperUtil.addMessageError(null, ERROR_INPUTSTREAM);
                }
            }
        } else {
            HelperUtil.addMessageError(null, MessageFormat.format("Failed to create CSR for: {0}", getAlias()));
        }
    }

    public StreamedContent getCsrFile() {
        enableNextCSR(false);
        String filename = MessageFormat.format("{0}-{1}.csr", getAlias(),
            CoreHelpUtils.formatDate("yyyyMMdd", new Date()));
        return new DefaultStreamedContent(new ByteArrayInputStream(csrText.getBytes()), csrFileType, filename);
    }

    public String getCsrText() {
        return csrText;
    }

    public void resumeImportWizard(int tabIndex) {
        clearImportWizard();
        setImportWizardTabIndex(tabIndex);
    }

    public void cancelImportWizard() {
        importWizardTabIndex = 0;
        clearImportWizard();
        alertUserOn(service.deleteTempKeystore());
    }

    private void clearImportWizard() {
        alias = null;
        oldAlias = null;
        exchangeType = null;
        commonName = null;
        organization = null;
        organizationalUnit = null;
        countryName = null;

        clearImport();

        csrFileType = null;
        csrText = null;
        disableNext = new boolean[] { true, true, true, true, true, true };
        enableTab(0);
        disableImportAction = true;
        refreshCacheForTrustStore();
    }

    public void next() {
        setImportWizardTabIndex(getImportWizardTabIndex() + 1);
    }

    private void loadCaProperties() {
        caProperties = new TreeMap<>();
        caLinks = new TreeMap<>();
        List<PropertyType> properties = propertyService.listProperties(NhincConstants.CA_AUTHORITY_PROPERTY_FILE);
        for (PropertyType item : properties) {
            if (item.getPropertyValue().indexOf(',') >= 0) {
                caProperties.put(item.getPropertyName().replace('.', ' '), item.getPropertyValue());
            } else if (item.getPropertyValue().indexOf("://") >= 0) {
                caLinks.put(item.getPropertyName().replace('.', ' '), item.getPropertyValue());
            }
        }
    }

    public Map<String, String> getCaProperties() {
        if (null == caProperties) {
            loadCaProperties();
        }
        return caProperties;
    }

    public Map<String, String> getCaLinks() {
        if (null == caLinks) {
            loadCaProperties();
        }
        return caLinks;
    }

    public void importToKeystore() {
        List<String> serverValidation = new ArrayList<>();
        if (StringUtils.isBlank(alias)) {
            serverValidation.add("Alias is required.");
        }
        if (null == uploadedFileServer) {
            serverValidation.add("Server Certificate is required.");
        }

        boolean rootMissing = MapUtils.isNotEmpty(listIntermediate) && null == uploadedFileRoot;
        boolean intermediateMissing = MapUtils.isEmpty(listIntermediate) && null != uploadedFileRoot;
        if ( rootMissing || intermediateMissing ) {
            serverValidation.add("Root and Intermediate Certificates must both be present in order to import Chain of Trust.");
        }
        if (CollectionUtils.isNotEmpty(serverValidation)) {
            validationErrors(serverValidation);
            return;
        }

        SimpleCertificateResponseMessageType response;
        if (MapUtils.isNotEmpty(listIntermediate) && null != uploadedFileRoot) {
            response = service.importToKeystore(alias, uploadedFileServer, listIntermediate, uploadedFileRoot);
        } else {
            response = service.importToKeystore(alias, uploadedFileServer, null, null);
        }
        if (response.isStatus()) {
            disableImportAction = false;
        }
        alertUserOn(response);
    }

    public void importFileServer(FileUploadEvent event) {
        uploadedFileServer = event.getFile();
        if (uploadedFileServer != null) {
            certServer = service.createCertificate(uploadedFileServer.getContents());
            validateCertificate(certServer);
        }
    }

    public void importFileIntermediate(FileUploadEvent event) {
        if (null == listIntermediate) {
            listIntermediate = new HashMap<>();
        }
        UploadedFile uploadedFile = event.getFile();
        if (uploadedFile != null) {
            CertificateDTO certIntermediate = service.createCertificate(uploadedFile.getContents());
            if (validateCertificate(certIntermediate)) {
                listIntermediate.put(uploadedFile.getFileName(), uploadedFile);
            }
        }
    }

    public void importFileRoot(FileUploadEvent event) {
        uploadedFileRoot = event.getFile();
        if (uploadedFileRoot != null) {
            certRoot = service.createCertificate(uploadedFileRoot.getContents());
            validateCertificate(certRoot);
        }
    }

    public String getSubjectServer() {
        if (null == certServer) {
            return "";
        }
        return certServer.getSubjectName();
    }

    public int getIntermediateCount() {
        if (MapUtils.isEmpty(listIntermediate)) {
            return 0;
        }
        return listIntermediate.size();
    }

    public String getSubjectRoot() {
        if (null == certRoot) {
            return "";
        }
        return certRoot.getSubjectName();
    }

    public String[] getTabTitles() {
        return tabTitles;
    }

    public int findTabIndexBy(String title) {
        if (StringUtils.isBlank(title)) {
            return 0;
        }
        for (int i = 0; i < tabTitles.length; i++) {
            if (title.equalsIgnoreCase(tabTitles[i])) {
                return i;
            }
        }
        return 0;
    }

    public void onTabChangeImportWizard(TabChangeEvent event) {
        importWizardTabIndex = findTabIndexBy(event.getTab().getTitle());
    }

    public void clearImport() {
        alias = null;
        certServer = null;
        certRoot = null;
        listIntermediate = null;
        uploadedFileRoot = null;
        uploadedFileServer = null;
    }

    public void clearIntermediate() {
        listIntermediate = null;
    }

    public boolean getDisableActionCsr() {
        return StringUtils.isBlank(csrText);
    }

    public void completeImportWizard() {
        SimpleCertificateResponseMessageType response = service.completeImportWizard();
        if (response.isStatus()) {
            disableImportAction = true;
        }
        alertUserOn(response);
    }

    private static void alertUserOn(SimpleCertificateResponseMessageType response) {
        if (null != response) {
            if (response.isStatus()) {
                HelperUtil.addMessageInfo(null, response.getMessage());
            } else {
                HelperUtil.addMessageError(null, response.getMessage());
            }
        } else {
            HelperUtil.addMessageError(null, "Error occured while calling webservice.");
        }
    }

    public SortedSet getCacheAlias() {
        if (null == cacheAlias) {
            cacheAlias = new TreeSet<>();
            SimpleCertificateResponseMessageType response = service.listTemporaryAlias();
            if (null == response) {
                HelperUtil.addMessageError(null, "Error loading temporary alias.");
            } else {
                for (String item : response.getAliasList()) {
                    cacheAlias.add(item);
                }
            }
        }
        return cacheAlias;
    }

    public void undo(String option) {
        List<String> serverValidation = new ArrayList<>();
        if (MapUtils.isEmpty(listIntermediate)) {
            serverValidation.add("CA Intermediate are required.");
        }
        if (null == uploadedFileRoot) {
            serverValidation.add("CA Root is Required.");
        }
        if (CollectionUtils.isNotEmpty(serverValidation)) {
            validationErrors(serverValidation);
            return;
        }
        alertUserOn(service.undoImportKeystore(getAlias(), listIntermediate, uploadedFileRoot));
    }

    public boolean getDisableNext(int index) {
        return disableNext[index];
    }

    private void enableTab(Integer index) {
        disableTab[0]=false;
        for (int i = 1; i < disableTab.length; i++) {
            disableTab[i]=true;
        }
        if (null != index) {
            disableTab[index] = false;
        }
    }
    public boolean getDisableTab(int index) {
        return disableTab[index];
    }

    public void enableNextCSR(boolean copyFlag) {
        if (copyFlag) {
            HelperUtil.addMessageInfo(null, "Copy Successful.");
        }
        disableNext[2] = false;
    }

    public boolean getDisableImportAction() {
        return disableImportAction;
    }

    private void validationErrors(List<String> errors) {
        for (String error : errors) {
            HelperUtil.addMessageError(null, error);
        }
    }
}
