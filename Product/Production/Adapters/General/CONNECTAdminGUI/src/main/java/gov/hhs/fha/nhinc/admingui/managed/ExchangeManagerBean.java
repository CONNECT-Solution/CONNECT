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

import gov.hhs.fha.nhinc.admingui.model.ConnectionEndpoint;
import gov.hhs.fha.nhinc.exchange.ExchangeInfoType;
import gov.hhs.fha.nhinc.exchange.ExchangeType;
import gov.hhs.fha.nhinc.exchange.TLSVersionType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tran Tang
 *
 */
@ManagedBean(name = "exchangeManagerBean")
@SessionScoped
public class ExchangeManagerBean {
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeManagerBean.class);
    private static final String DEFAULT_VALUE = "--";
    private ExchangeInfoType generalSetting = new ExchangeInfoType();
    private ExchangeType formExchange = new ExchangeType();
    private ExchangeType selectedExchange = null;
    private ConnectionEndpoint selectedEndpoint = null;
    private String filterOrganization;
    private OrganizationType orgFilter;
    private String filterExchange;
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

    public void setFilterOrganization(String orgAndHcid) {
        LOG.info("call-setFilterOrganization");
        filterOrganization = orgAndHcid;
        orgFilter = new OrganizationType();
        orgFilter.setName(orgAndHcid);
        orgFilter.setHcid(orgAndHcid);
    }

    public String getOrgName(){
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
        return DEFAULT_VALUE;
    }

    public String getOrgContacts() {
        if (null != orgFilter && CollectionUtils.isNotEmpty(orgFilter.getContact())) {
            return "join(ContactType,', ')";
        }
        return DEFAULT_VALUE;
    }

    public String getFilterExchange() {
        return filterExchange;
    }

    public void setFilterExchange(String exchange) {
        LOG.info("call-setFilterExchange");
        filterExchange = exchange;
    }

    public void onChangeFilterOgranization() {
        LOG.info("call-onChangeFilterOgranization");
    }

    public String getFormExchangeTLSVersion() {
        if (isNotEmptyTLSVersions()) {
            return formExchange.getTLSVersions().getSupports().get(0);
        }
        return null;
    }

    public void setFormExchangeTLSVersion(String tlsString) {
        TLSVersionType tlsVer = null;
        if (StringUtils.isNotBlank(tlsString)) {
            tlsVer = new TLSVersionType();
            tlsVer.getSupports().add(tlsString);
        }
        formExchange.setTLSVersions(tlsVer);
    }

    public boolean getFormExchangeEnable() {
        if (null == formExchange) {
            return true;
        }
        return !formExchange.isDisabled();
    }

    public void setFormExchangeEnable(boolean enable) {
        if (null != formExchange) {
            formExchange.setDisabled(!enable);
        }
    }

    // listing
    public List<ExchangeType> getExchanges() {
        List<ExchangeType> exchanges = new ArrayList<>();
        exchanges.add(new ExchangeType());
        exchanges.get(0).setName("exchange-name");
        exchanges.get(0).setUrl("exchange-URL");
        exchanges.get(0).setType("FHIR");
        return exchanges;
    }

    public List<ConnectionEndpoint> getConnectionEndpoints() {
        List<ConnectionEndpoint> list = new ArrayList<>();
        if (StringUtils.isNotBlank(filterOrganization)) {
            list.add(new ConnectionEndpoint(filterOrganization + " - Endpoint", "url", "version", null, null));
        }
        return list;
    }

    // action
    public boolean saveExchangeInfo() {
        LOG.info("save-exchangeInfo-call");
        return false;// dummy-method
    }

    public boolean refreshExchangeInfo() {
        LOG.info("refresh-exchangeInfo-call");
        return false;// dummy-method
    }

    public boolean saveExchange() {
        LOG.info("save-exchange-call");
        return false;// dummy-method
    }

    public boolean deleteExchange() {
        LOG.info("delete-exchange-call");
        return false;
    }

    public boolean pingEndpoint() {
        LOG.info("ping-endpoint-call");
        return false;
    }

    public boolean newExchange() {
        LOG.info("new-exchange-call");
        formExchange = new ExchangeType();
        return false;
    }

    // form - properties
    public ExchangeInfoType getFormExchangeInfo() {
        return generalSetting;
    }

    public ExchangeType getFormExchange() {
        return formExchange;
    }

    // List
    public List<OrganizationType> getListFilterOrganizations() {
        LOG.info("call-getListFilterOrganizations: update-orgs-lists");
        if (null != filterExchange) {
            LOG.info("return orgs-list");
            List<OrganizationType> orgs = new ArrayList<>();
            orgs.add(new OrganizationType());
            orgs.get(0).setName("org-name");
            orgs.get(0).setHcid("org-HCID");
            orgs.add(new OrganizationType());
            orgs.get(1).setName("org-name-2");
            orgs.get(1).setHcid("org-HCID-2");
            return orgs;
        }
        return new ArrayList<>();
    }

    public List<ExchangeType> getListFilterExchanges() {
        List<ExchangeType> exch = new ArrayList<>();
        exch.add(new ExchangeType());
        exch.get(0).setName("exch-name");
        exch.get(0).setUrl("exch-url");
        exch.add(new ExchangeType());
        exch.get(1).setName("exch-name-2");
        exch.get(1).setUrl("exch-url-2");
        return exch;
    }



    public Map<String, String> getListTLSes() {
        return getDummyList();
    }

    public Map<String, String> getListTypes() {
        return getDummyList();
    }

    public Map<String, String> getListDefaultExchanges() {
        return getDummyList();
    }

    private static Map<String, String> getDummyList() {
        Map<String, String> popList = new TreeMap<>();
        popList.put("Item-one", "1");
        popList.put("Item-two", "2");
        popList.put("Item-three", "3");
        return popList;
    }

    private boolean isNotEmptyTLSVersions() {
        return null != formExchange && null != formExchange.getTLSVersions()
            && CollectionUtils.isNotEmpty(formExchange.getTLSVersions().getSupports());
    }

    // disable-GUI
    public boolean getDisableButtonsExchange() {
        return null == selectedExchange;
    }

    public boolean getDisableButtonsEndpoint() {
        return null == selectedEndpoint;
    }

}
