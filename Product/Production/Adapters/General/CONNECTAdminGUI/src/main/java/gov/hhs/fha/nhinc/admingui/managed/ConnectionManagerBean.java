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
package gov.hhs.fha.nhinc.admingui.managed;

import static gov.hhs.fha.nhinc.admingui.services.impl.PingServiceImpl.IGNORE_DEADHOST;
import static gov.hhs.fha.nhinc.admingui.util.HelperUtil.formatDate;

import gov.hhs.fha.nhinc.admingui.application.EndpointManagerCache;
import gov.hhs.fha.nhinc.admingui.model.ConnectionEndpoint;
import gov.hhs.fha.nhinc.admingui.services.PingService;
import gov.hhs.fha.nhinc.admingui.services.impl.PingServiceImpl;
import gov.hhs.fha.nhinc.admingui.util.ConnectionHelper;
import gov.hhs.fha.nhinc.exchange.directory.ContactType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerHelper;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jassmit
 */
@ManagedBean(name = "connectionManagerBean")
@ViewScoped
public class ConnectionManagerBean {

    private final ConnectionHelper connection = new ConnectionHelper();

    private HashMap<String, OrganizationType> externalEntities = new HashMap<>();
    private final List<String> entityNames = new ArrayList<>();

    private OrganizationType selectedEntity;

    private ConnectionEndpoint selectedEndpoint;

    private final PingService pingService = new PingServiceImpl();

    private static final String DATE_FORMAT = "MM-dd-yy hh:mm:ss";
    private static final String NULL_DISPLAY = "--";

    @PostConstruct
    public void init() {
        refresh();
    }

    public List<String> getEntityNames() {
        return entityNames;
    }

    public String getSelectedEntityName() {
        String name = NULL_DISPLAY;
        if (selectedEntity != null && StringUtils.isNotBlank(selectedEntity.getName())) {
            name = selectedEntity.getName();
        }
        return name;
    }

    public void setSelectedEntityName(String selectedEntityName) {
        if (selectedEntityName != null) {
            selectedEntity = externalEntities.get(selectedEntityName);
        }
    }

    public OrganizationType getSelectedEntity() {
        return selectedEntity;
    }

    public String getSelectedEntityDescription() {
        // this method need to be updated if we decided that the ogranizationType-description exist.
        return NULL_DISPLAY;
    }

    public String getSelectedEntityRegions() {
        String regions = NULL_DISPLAY;
        if (selectedEntity != null && CollectionUtils.isNotEmpty(selectedEntity.getTargetRegion())) {
            regions = StringUtils.join(selectedEntity.getTargetRegion(), ", ");
        }
        return regions;
    }

    public String getSelectedEntityContact() {
        String contactValue = NULL_DISPLAY;

        if (selectedEntity != null && CollectionUtils.isNotEmpty(selectedEntity.getContact())) {
            contactValue = getNameByDefault(selectedEntity.getContact().get(0), NULL_DISPLAY);
        }

        return contactValue;
    }

    public String getSelectedEntityHcid() {
        String hcid = NULL_DISPLAY;
        if (selectedEntity != null && StringUtils.isNotBlank(selectedEntity.getHcid())) {
            hcid = selectedEntity.getHcid();
        }
        return hcid;
    }

    public void ping() {
        if (selectedEndpoint != null) {
            selectedEndpoint.setResponseCode(pingService.ping(selectedEndpoint.getServiceUrl(), IGNORE_DEADHOST));
            EndpointManagerCache.getInstance().addOrUpdateEndpoint(selectedEndpoint.getServiceUrl(), new Date(),
                selectedEndpoint.isPingSuccessful(), selectedEndpoint.getResponseCode());
        }
    }

    public List<ConnectionEndpoint> getEndpoints() {
        List<ConnectionEndpoint> endpoints = new ArrayList<>();
        List<EndpointType> orgEndpoints = ExchangeManagerHelper.getEndpointTypeBy(selectedEntity);
        if (selectedEntity != null && CollectionUtils.isNotEmpty(orgEndpoints)) {
            for (EndpointType endpoint : orgEndpoints) {
                List<EndpointConfigurationType> epConfigurations = ExchangeManagerHelper
                    .getEndpointConfigurationTypeBy(endpoint);
                for (EndpointConfigurationType epConf : epConfigurations) {

                    String timestamp = null;
                    int code = 0;
                    String url = epConf.getUrl();
                    EndpointManagerCache.EndpointCacheInfo info = EndpointManagerCache.getInstance()
                        .getEndpointInfo(url);

                    if (info != null) {
                        timestamp = formatDate(DATE_FORMAT, info.getTimestamp());
                        code = info.getHttpCode();
                    }

                    endpoints.add(
                        new ConnectionEndpoint(endpoint.getName().get(0), url, epConf.getVersion(), code, timestamp));
                }
            }
        }
        return endpoints;
    }

    public void refreshConnections() {
        refresh();
    }

    private void refresh() {

        externalEntities = connection.getExternalOrganizationsMap();
        entityNames.addAll(externalEntities.keySet());
        if (!entityNames.isEmpty()) {
            selectedEntity = externalEntities.get(entityNames.get(0));
        }
    }

    public ConnectionEndpoint getSelectedEndpoint() {
        return selectedEndpoint;
    }

    public void setSelectedEndpoint(ConnectionEndpoint selectedEndpoint) {
        this.selectedEndpoint = selectedEndpoint;
    }

    private static String getNameByDefault(ContactType contact, String defaultValue) {
        String name = null;
        if (contact != null) {
            name = MessageFormat.format("{0} {1}", contact.getGivenName(), contact.getFamilyName());
            if (StringUtils.isBlank(name) && CollectionUtils.isNotEmpty(contact.getFullName())) {
                name = contact.getFullName().get(0);
            }
        }
        return StringUtils.isNotBlank(name) ? name : defaultValue;
    }
}
