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
package gov.hhs.fha.nhinc.admingui.util;

import static gov.hhs.fha.nhinc.util.HomeCommunityMap.getHomeCommunityWithoutPrefix;

import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionHelper.class);

    public HashMap<String, OrganizationType> getRemoteHcidFromUUID() {
        HashMap<String, OrganizationType> organizationMap = new HashMap<>();
        List<OrganizationType> organizations = getAllOrganizations();
        if (NullChecker.isNotNullish(organizations)) {
            for (OrganizationType organization : organizations) {
                organizationMap.put(getOrganizationHcid(organization), organization);
            }
        }
        return organizationMap;
    }

    public HashMap<String, OrganizationType> getExternalOrganizationsMap() {
        HashMap<String, OrganizationType> organizationMap = getRemoteHcidFromUUID();
        if (organizationMap != null && !organizationMap.isEmpty()) {
            Iterator<Map.Entry<String, OrganizationType>> it = organizationMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, OrganizationType> entry = it.next();
                if (checkLocalHcid(entry.getValue().getHcid())) {
                    it.remove();
                }
            }
        }
        return organizationMap;
    }

    public OrganizationType getLocalOrganization() {
        OrganizationType localEntity = null;
        HashMap<String, OrganizationType> organizationMap = getRemoteHcidFromUUID();
        if (organizationMap != null && !organizationMap.isEmpty()) {
            Iterator<Map.Entry<String, OrganizationType>> it = organizationMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, OrganizationType> entry = it.next();
                if (checkLocalHcid(entry.getValue().getHcid())) {
                    localEntity = entry.getValue();
                }
            }
        }
        return localEntity;
    }

    private boolean checkLocalHcid(String entityHcid) {
        return StringUtils.equalsIgnoreCase(formatHcid(entityHcid), formatHcid(getLocalHcid()));
    }

    private static String getOrganizationName(OrganizationType organization) {
        String homeCommunityName = null;
        if (organization != null && StringUtils.isNotEmpty(organization.getName())) {
            homeCommunityName = organization.getName();
        }
        return homeCommunityName;
    }

    private static String getOrganizationHcid(OrganizationType organization) {
        if (null != organization && StringUtils.isNotEmpty(organization.getHcid())) {
            return organization.getHcid();
        }
        return "";
    }

    protected List<OrganizationType> getAllOrganizations() {
        List<OrganizationType> organizations = null;
        try {
            organizations = WebServiceProxyHelper.getGatewayAllOrganizations();
        } catch (ExchangeManagerException ex) {
            LOG.error("Exception while retrieving Exchange-Organization: {}", ex.getLocalizedMessage(), ex);
        }
        return organizations;
    }

    private String getLocalHcid() {
        String localHcid = null;
        try {
            localHcid = getPropertyAccessor().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            LOG.error("Exception retrieving property from gateway.properties: " + ex.getLocalizedMessage(), ex);
        }
        return localHcid;
    }

    private static String formatHcid(String hcid) {
        String formattedHcid = hcid;
        if (formattedHcid != null && hcid.startsWith(NhincConstants.HCID_PREFIX)) {
            formattedHcid = hcid.substring(NhincConstants.HCID_PREFIX.length(), hcid.length());
        }
        return formattedHcid;
    }

    protected PropertyAccessor getPropertyAccessor() {
        return PropertyAccessor.getInstance();
    }

    public Map<String, String> getOrgNameRemoteHcidExternalEntities() {
        HashMap<String, String> organizationMap = new HashMap<>();
        List<OrganizationType> externalOrganizations = new ArrayList<>(getExternalOrganizationsMap().values());
        if (CollectionUtils.isNotEmpty(externalOrganizations)) {
            for (OrganizationType organization : externalOrganizations) {
                if (organization !=null){
                    organizationMap.put(getOrganizationName(organization), organization.getHcid());
                }
            }
        }
        return organizationMap;
    }

    public Map<String, String> getRemoteHcidOrgNameMap() {
        HashMap<String, String> organizationMap = new HashMap<>();
        List<OrganizationType> organizations = getAllOrganizations();
        if (NullChecker.isNotNullish(organizations)) {
            for (OrganizationType organization : organizations) {
                organizationMap.put(getHomeCommunityWithoutPrefix(organization.getHcid()),
                    getOrganizationName(organization));
            }
        }
        return organizationMap;
    }

    public Map<String, String> getOrgNameAndRemoteHcidMap() {
        HashMap<String, String> organizationMap = new HashMap<>();
        List<OrganizationType> organizations = getAllOrganizations();
        if (CollectionUtils.isNotEmpty(organizations)) {
            for (OrganizationType organization : organizations) {
                if (organization != null){
                    organizationMap.put(getOrganizationName(organization), organization.getHcid());
                }
            }
        }
        return organizationMap;
    }

    public OrganizationType getLocalOrganizationFromDefaultExchange() throws ExchangeManagerException {
        OrganizationType localEntity = null;
        Map<String, OrganizationType> organizationMap = getOrganizationsFromDefaultExchange();
        if (organizationMap != null && !organizationMap.isEmpty()) {
            Iterator<Map.Entry<String, OrganizationType>> it = organizationMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, OrganizationType> entry = it.next();
                if (checkLocalHcid(entry.getValue().getHcid())) {
                    localEntity = entry.getValue();
                }
            }
        }
        return localEntity;
    }

    public Map<String, OrganizationType> getOrganizationsFromDefaultExchange() throws ExchangeManagerException {
        Map<String, OrganizationType> organizationMap = new HashMap<>();
        ExchangeManager exchangeManager = ExchangeManager.getInstance();
        List<OrganizationType> organizations = exchangeManager.getAllOrganizations(exchangeManager.getDefaultExchange());
        if (NullChecker.isNotNullish(organizations)) {
            for (OrganizationType organization : organizations) {
                organizationMap.put(getOrganizationHcid(organization), organization);
            }
        }
        return organizationMap;
    }
}
