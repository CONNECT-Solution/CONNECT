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
package gov.hhs.fha.nhinc.admingui.managed;

import static gov.hhs.fha.nhinc.admingui.util.HelperUtil.execPFHideDialog;
import static gov.hhs.fha.nhinc.admingui.util.HelperUtil.execPFShowDialog;

import gov.hhs.fha.nhinc.admingui.model.ConnectionEndpoint;
import gov.hhs.fha.nhinc.admingui.services.ExchangeManagerService;
import gov.hhs.fha.nhinc.exchange.ExchangeInfoType;
import gov.hhs.fha.nhinc.exchange.ExchangeType;
import gov.hhs.fha.nhinc.exchange.TLSVersionType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerHelper;
import gov.hhs.fha.nhinc.exchangemgr.util.ExchangeDownloadStatus;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.EXCHANGE_TYPE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Tran Tang
 *
 */
@ManagedBean(name = "exchangeManagerBean")
@SessionScoped
@Component
public class ExchangeManagerBean {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeManagerBean.class);
    private static final String DEFAULT_VALUE = "--";
    private static final String DLG_SAVE_EXCHANGE = "wvDlgSaveExchange";
    private static final String DLG_REFRESH_EXCHANGE = "wvDlgRefreshExchangeStatus";
    private static final String[] BUTTONS_REFRESH_LOCKED = { "formDlgExchange:btnSaveExchange",
            "tabviewExchange:formGeneralSetting:btnStatusRefresh",
            "tabviewExchange:formGeneralSetting:btnSaveExchangeInfo",
            "tabviewExchange:formGeneralSetting:btnRefreshExchangeInfo",
    "tabviewExchange:accordionExchange:formExchange:deleteExchange" };

    @Autowired
    private ExchangeManagerService exchangeService;

    private ExchangeInfoType generalSetting;
    private ExchangeType formExchange;
    private ExchangeType selectedExchange;
    private ConnectionEndpoint selectedEndpoint;
    private String filterOrganization;
    private OrganizationType orgFilter;
    private String filterExchange;
    private boolean recordRefreshLocked;
    private boolean agreeOverwriteExchange;

    private List<String> tlses;
    private List<OrganizationType> organizations;
    private List<ExchangeDownloadStatus> exDownloadStatus;
    private List<ExchangeType> exchanges;

    public List<ExchangeDownloadStatus> getExDownloadStatus() {
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
        LOG.debug("organization-description does not exist need to be modified once decided.");
        return DEFAULT_VALUE;
    }

    public String getOrgContacts() {
        if (null != orgFilter && CollectionUtils.isNotEmpty(orgFilter.getContact())) {
            return StringUtils.join(orgFilter.getContact().toArray());
        }
        return DEFAULT_VALUE;
    }

    public String getFilterExchange() {
        return filterExchange;
    }

    public void setFilterExchange(String exchange) {
        filterExchange = exchange;
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
        exchanges = exchangeService.getAllExchanges();
        return exchanges;
    }

    public List<ExchangeType> getListFilterExchanges() {
        return exchangeService.getAllExchanges();
    }

    public List<ConnectionEndpoint> getConnectionEndpoints() {
        if (StringUtils.isNotBlank(filterOrganization) && StringUtils.isNotBlank(filterExchange)) {
            return exchangeService.getAllConnectionEndpoints(filterExchange, filterOrganization);
        }
        return new ArrayList<>();
    }

    public boolean refreshOrganizations() {
        if (StringUtils.isNotBlank(filterExchange)) {
            organizations = exchangeService.getAllOrganizations(filterExchange);
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
    }

    public void newExchange() {
        agreeOverwriteExchange = false;
        formExchange = new ExchangeType();
    }

    public boolean saveExchange() {
        boolean bSave = false;
        if (null != formExchange) {
            bSave = exchangeService.saveExchange(formExchange);
            if (bSave) {
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
                selectedExchange = null;
            }
        }
        return bDelete;
    }

    public boolean pingEndpoint() {
        return exchangeService.pingService(selectedEndpoint);
    }

    public boolean pingAllEndpoint() {
        List<ConnectionEndpoint> endpoints = getConnectionEndpoints();
        if (CollectionUtils.isEmpty(endpoints)) {
            LOG.debug("ping-all connection-endpoints: none found.");
            return false;
        }
        for (ConnectionEndpoint connEndpoint : endpoints) {
            exchangeService.pingService(connEndpoint);
        }
        return true;
    }

    // form - properties
    public ExchangeInfoType getFormExchangeInfo() {
        if (null == generalSetting) {
            generalSetting = exchangeService.getExchangeInfoView();
        }
        return generalSetting;
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
        return EXCHANGE_TYPE.values();
    }

    private boolean isNotEmptyTLSVersions() {
        return null != getFormExchange().getTLSVersions()
            && CollectionUtils.isNotEmpty(getFormExchange().getTLSVersions().getSupports());
    }

    // disable-GUI
    public boolean getDisableButtonsExchange() {
        return null == selectedExchange;
    }

    public boolean getDisableButtonsEndpoint() {
        return null == selectedEndpoint;
    }

    public boolean isRefreshLocked() {
        if (recordRefreshLocked != exchangeService.isRefreshLocked()) {
            recordRefreshLocked = exchangeService.isRefreshLocked();
            RequestContext.getCurrentInstance().update(Arrays.asList(BUTTONS_REFRESH_LOCKED));
        }
        return exchangeService.isRefreshLocked();
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

    public boolean toggleIsEnabledFor(String exchangeName) {
        return exchangeService.toggleExchangeIsEnabled(exchangeName);
    }
}