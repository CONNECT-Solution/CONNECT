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
import static gov.hhs.fha.nhinc.admingui.util.HelperUtil.getHashCodeBy;

import gov.hhs.fha.nhinc.admingui.comparators.ExchangesComparator;
import gov.hhs.fha.nhinc.admingui.comparators.OrganizationsComparator;
import gov.hhs.fha.nhinc.admingui.model.ConnectionEndpoint;
import gov.hhs.fha.nhinc.admingui.services.ExchangeManagerService;
import gov.hhs.fha.nhinc.admingui.services.impl.ExchangeManagerServiceImpl;
import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
import gov.hhs.fha.nhinc.common.exchangemanagement.ExchangeDownloadStatusType;
import gov.hhs.fha.nhinc.exchange.ExchangeInfoType;
import gov.hhs.fha.nhinc.exchange.ExchangeType;
import gov.hhs.fha.nhinc.exchange.TLSVersionType;
import gov.hhs.fha.nhinc.exchange.directory.ContactType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.EXCHANGE_TYPE;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tran Tang
 *
 */
@ManagedBean(name = "exchangeManagerBean")
@ViewScoped
public class ExchangeManagerBean {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeManagerBean.class);
    private static final String DEFAULT_VALUE = "--";
    private static final String DLG_SAVE_EXCHANGE = "wvDlgSaveExchange";
    private static final String DLG_REFRESH_EXCHANGE = "wvDlgRefreshExchangeStatus";
    private static final String DLG_CONFIRM_OVERWRITE_EXCHAGE = "wvConfirmationOverwrite";
    private ExchangeManagerService exchangeService = new ExchangeManagerServiceImpl();

    private ExchangeInfoType generalSetting;
    private ExchangeType formExchange;
    private ExchangeType selectedExchange;
    private ConnectionEndpoint selectedEndpoint;
    private String filterOrganization;
    private OrganizationType orgFilter;
    private String filterExchange;
    private boolean agreeOverwriteExchange;

    private List<String> tlses;
    private List<OrganizationType> organizations;
    private List<ExchangeDownloadStatusType> exDownloadStatus;
    private List<ExchangeType> exchanges;
    private long cachedExchangesTimestamp = 0;
    private long cachedExchangesTimestampUpdated = 1;

    private List<ConnectionEndpoint> endpoints = new ArrayList<>();
    private int cachedEndpointHashCode;
    private boolean disableButtonsExchange;
    private boolean isEditing;
    private String dlgHeader;


    @PostConstruct
    public void initialize() {
        generalSetting = exchangeService.getExchangeInfoView();
    }

    public boolean getDisableButtonsExchange() {
        return disableButtonsExchange || null == selectedExchange;
    }

    public ExchangeInfoType getGeneralSetting() {
        return generalSetting;
    }

    public List<ExchangeDownloadStatusType> getExDownloadStatus() {
        return exDownloadStatus;
    }

    // properties
    public ExchangeType getSelectedExchange() {
        return selectedExchange;
    }

    public void setSelectedExchange(ExchangeType exchange) {
        selectedExchange = exchange;
    }

    public ConnectionEndpoint getSelectedEndpoint() {
        return selectedEndpoint;
    }

    public void setSelectedEndpoint(ConnectionEndpoint endpoint) {
        selectedEndpoint = endpoint;
    }

    public String getFilterOrganization() {
        return filterOrganization;
    }

    public void setFilterOrganization(String orgHcid) {
        filterOrganization = orgHcid;
        orgFilter = ExchangeManagerHelper.findOrganizationTypeBy(organizations, orgHcid);
    }

    public String getOrgName() {
        if (null == orgFilter) {
            return DEFAULT_VALUE;
        }
        return orgFilter.getName();
    }

    public String getOrgHcid() {
        if (null == orgFilter) {
            return DEFAULT_VALUE;
        }
        return orgFilter.getHcid();
    }

    public String getOrgDescription() {
        if (null == orgFilter) {
            return DEFAULT_VALUE;
        }
        return StringUtils.join(orgFilter.getDescription(), "\n");
    }

    public String getOrgContacts() {
        if (null != orgFilter && CollectionUtils.isNotEmpty(orgFilter.getContact())) {
            return formatContact(orgFilter.getContact().get(0));
        }
        return DEFAULT_VALUE;
    }

    public String getFilterExchange() {
        return filterExchange;
    }

    public void setFilterExchange(String exchange) {
        filterExchange = exchange;
        filterOrganization = null;
        orgFilter = null;
        endpoints = null;
        refreshOrganizations();
    }

    public String getFormExchangeTLSVersion() {
        if (isNotEmptyTLSVersions()) {
            return getFormExchange().getTLSVersions().getSupports().get(0);
        }
        return null;
    }

    public void setFormExchangeTLSVersion(String tlsString) {
        TLSVersionType tlsVer = null;
        if (StringUtils.isNotBlank(tlsString)) {
            tlsVer = new TLSVersionType();
            tlsVer.getSupports().add(tlsString);
        }
        getFormExchange().setTLSVersions(tlsVer);
    }

    public boolean getFormExchangeEnable() {
        return !getFormExchange().isDisabled();
    }

    public void setFormExchangeEnable(boolean enable) {
        getFormExchange().setDisabled(!enable);
    }

    // datatable-list
    public List<ExchangeType> getExchanges() {
        return refreshCacheExchanges();
    }

    public List<ExchangeType> getListFilterExchanges() {
        return refreshCacheExchanges();
    }

    public List<ConnectionEndpoint> getConnectionEndpoints() {
        return refreshCacheEndpoints();
    }

    public String getDlgHeader() {
        return dlgHeader;
    }

    public void setDlgHeader(String dlgHeader) {
        this.dlgHeader = dlgHeader;
    }

    public boolean refreshOrganizations() {
        if (StringUtils.isNotBlank(filterExchange)) {
            organizations = exchangeService.getAllOrganizations(filterExchange);
            Collections.sort(organizations, new OrganizationsComparator());
            return true;
        }
        organizations = new ArrayList<>();
        return false;
    }

    public List<OrganizationType> getListFilterOrganizations() {
        if (null == organizations) {
            refreshOrganizations();
        }
        return organizations;
    }

    // action
    public boolean saveExchangeInfo() {
        return exchangeService.saveGeneralSetting(generalSetting);
    }

    public void refreshExchangeInfo() {
        exDownloadStatus = exchangeService.refreshExchangeManager();
        execPFShowDialog(DLG_REFRESH_EXCHANGE);
        modifiedExchangesCache();
    }

    public void newExchange() {
        isEditing = false;
        setDlgHeader("New Exchange");
        agreeOverwriteExchange = false;
        formExchange = new ExchangeType();
    }

    public void editExchange() {
        isEditing = true;
        formExchange = new ExchangeType();
        if (selectedExchange != null) {
            setDlgHeader("Editing " + selectedExchange.getName());
            formExchange.setCertificateAlias(selectedExchange.getCertificateAlias());
            formExchange.setUrl(selectedExchange.getUrl());
            formExchange.setDisabled(selectedExchange.isDisabled());
            formExchange.setKey(selectedExchange.getKey());
            formExchange.setName(selectedExchange.getName());
            formExchange.setSniName(selectedExchange.getSniName());
            formExchange.setTLSVersions(selectedExchange.getTLSVersions());
            formExchange.setType(selectedExchange.getType());
        }
    }

    public boolean editExistingExchange() {

        if (!formExchange.getName().equals(selectedExchange.getName()) && isNotUniqueExchangeName()) {
            HelperUtil.addFacesMessageBy("dlgExchangeErrors", FacesMessage.SEVERITY_ERROR, formExchange.getName() +
                " already exists. Please choose a different name.");
            return false;
        }

        boolean bSave = exchangeService.saveExchange(formExchange, selectedExchange.getName());
        if (bSave) {
            modifiedExchangesCache();
            execPFHideDialog(DLG_SAVE_EXCHANGE);
        }

        return bSave;
    }

    public boolean saveExchange() {
        if (isEditing) {
            return editExistingExchange();
        } else {
            return saveExchangeWith(false);
        }
    }

    public boolean overwriteExchange() {
        return saveExchangeWith(true);
    }

    private boolean saveExchangeWith(boolean confirmOverwrite) {
        boolean bSave = false;
        if (null != formExchange) {
            if (!confirmOverwrite && isNotUniqueExchangeName()) {
                execPFShowDialog(DLG_CONFIRM_OVERWRITE_EXCHAGE);
                return false;
            }
            bSave = exchangeService.saveExchange(formExchange, null);
            if (bSave) {
                modifiedExchangesCache();
                execPFHideDialog(DLG_SAVE_EXCHANGE);
            }
        }
        return bSave;
    }

    public boolean deleteExchange() {
        boolean bDelete = false;
        if (null != selectedExchange && StringUtils.isNotBlank(selectedExchange.getName())) {
            bDelete = exchangeService.deleteExchange(selectedExchange.getName());
            if (bDelete) {
                modifiedExchangesCache();
                selectedExchange = null;
                filterExchange = null;
                filterOrganization = null;
                orgFilter = null;
                endpoints = null;
                refreshOrganizations();
            }
        }
        return bDelete;
    }

    public int pingEndpoint() {
        for (ConnectionEndpoint connEndpoint : endpoints) {
            if (connEndpoint.getName().equals(selectedEndpoint.getName())
                && connEndpoint.getServiceSpec().equals(selectedEndpoint.getServiceSpec())) {
                return exchangeService.pingService(connEndpoint, filterExchange, filterOrganization);
            }
        }
        return 0;
    }

    public boolean pingAllEndpoint() {
        refreshCacheEndpoints();
        if (CollectionUtils.isEmpty(endpoints)) {
            LOG.debug("ping-all connection-endpoints: none found.");
            return false;
        }

        for (ConnectionEndpoint connEndpoint : endpoints) {
            exchangeService.pingService(connEndpoint, filterExchange, filterOrganization);
        }
        return true;
    }

    public ExchangeType getFormExchange() {
        if (null == formExchange) {
            formExchange = new ExchangeType();
        }
        return formExchange;
    }

    // List
    public List<String> getListTLSes() {
        if (null == tlses) {
            tlses = new ArrayList<>();
            tlses.add("1.2");
            tlses.add("1.1");
            tlses.add("1.0");
        }
        return tlses;
    }

    public EXCHANGE_TYPE[] getListTypes() {
        return ExchangeManagerHelper.getDisplayExchangeTypes();
    }

    private boolean isNotEmptyTLSVersions() {
        return null != getFormExchange().getTLSVersions()
            && CollectionUtils.isNotEmpty(getFormExchange().getTLSVersions().getSupports());
    }

    public boolean getDisableButtonsEndpoint() {
        return null == selectedEndpoint;
    }

    public boolean isRefreshLocked() {
        // web-service no-longer return refreshLocked; action will fail if the exchangeManager is refreshLocked
        return false;
    }

    public boolean isNotUniqueExchangeName() {
        if (null != formExchange && StringUtils.isNotEmpty(formExchange.getName())
            && CollectionUtils.isNotEmpty(getExchanges())) {
            return null != ExchangeManagerHelper.findExchangeTypeBy(exchanges, formExchange.getName());
        }
        return false;
    }

    public boolean getOverwriteExchange() {
        return agreeOverwriteExchange;
    }

    public void setOverwriteExchange(boolean overwriteValue) {
        agreeOverwriteExchange = overwriteValue;
    }

    public boolean toggleIsEnabledFor(ExchangeType exchange) {
        boolean bCurrentStage = exchange.isDisabled();
        exchange.setDisabled(!bCurrentStage);
        boolean saveSuccessful = exchangeService.saveExchange(exchange, null);
        if (!saveSuccessful) {
            exchange.setDisabled(bCurrentStage);
        }
        return saveSuccessful;
    }

    private static String formatContact(ContactType contact) {
        if (CollectionUtils.isEmpty(contact.getFullName())) {
            return "";
        }
        if (CollectionUtils.isNotEmpty(contact.getPhone())) {
            return MessageFormat.format("{0} {1}", contact.getFullName().get(0), contact.getPhone().get(0));
        }
        return contact.getFullName().get(0);
    }

    private List<ConnectionEndpoint> refreshCacheEndpoints() {
        if(StringUtils.isBlank(filterOrganization) || StringUtils.isBlank(filterExchange)){
            return new ArrayList<>();
        }
        if (cachedEndpointHashCode == getHashCodeBy(filterExchange, filterOrganization)
            && CollectionUtils.isNotEmpty(endpoints)) {
            return endpoints;
        }

        endpoints = exchangeService.getAllConnectionEndpoints(filterExchange, filterOrganization);
        cachedEndpointHashCode = getHashCodeBy(filterExchange, filterOrganization);
        return endpoints;
    }

    private void modifiedExchangesCache() {
        cachedExchangesTimestampUpdated = new Date().getTime();
    }

    private void updatedExchangesCaches() {
        cachedExchangesTimestamp = new Date().getTime();
        cachedExchangesTimestampUpdated = cachedExchangesTimestamp;
        disableButtonsExchange = exchanges.size() > 1 ? false : true;
    }

    private List<ExchangeType> refreshCacheExchanges() {
        if (cachedExchangesTimestamp == cachedExchangesTimestampUpdated) {
            return exchanges;
        }

        exchanges = exchangeService.getAllExchanges();
        Collections.sort(exchanges, new ExchangesComparator());
        if (!exchanges.isEmpty()) {
            selectedExchange = exchanges.get(0);
        }
        updatedExchangesCaches();
        return exchanges;
    }

}
