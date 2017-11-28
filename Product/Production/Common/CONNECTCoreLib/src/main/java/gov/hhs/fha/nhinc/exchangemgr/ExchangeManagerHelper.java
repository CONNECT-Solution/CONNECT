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
package gov.hhs.fha.nhinc.exchangemgr;

import static gov.hhs.fha.nhinc.util.HomeCommunityMap.equalsIgnoreCaseForHCID;
import static gov.hhs.fha.nhinc.util.NhincCollections.addAll;
import static gov.hhs.fha.nhinc.util.NhincCollections.combine;
import static gov.hhs.fha.nhinc.util.NhincCollections.equalsIgnoreCaseAny;

import gov.hhs.fha.nhinc.exchange.ExchangeInfoType;
import gov.hhs.fha.nhinc.exchange.ExchangeListType;
import gov.hhs.fha.nhinc.exchange.ExchangeType;
import gov.hhs.fha.nhinc.exchange.OrganizationListType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationListType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointListType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class ExchangeManagerHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeManagerHelper.class);
    private static final boolean ELEMENT_NOT_REQUIRED = false;
    private static final boolean REQUIRED_ELEMENT = true;

    public ExchangeManagerHelper() {
    }

    public boolean organizationHasService(OrganizationType org, String sUniformServiceName) {
        if (org.getEndpointList() != null && CollectionUtils.isNotEmpty(org.getEndpointList().getEndpoint())) {
            for (EndpointType epType : org.getEndpointList().getEndpoint()) {
                if (hasService(epType, sUniformServiceName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasService(EndpointType epType, String sUniformServiceName) {
        if (epType != null && CollectionUtils.isNotEmpty(epType.getName())) {
            for (String name : epType.getName()) {
                if (sUniformServiceName.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<NhincConstants.UDDI_SPEC_VERSION> getSpecVersions(EndpointType epType) {
        List<NhincConstants.UDDI_SPEC_VERSION> specVersionList = new ArrayList<>();
        if (null != epType && null != epType.getEndpointConfigurationList() && CollectionUtils.isNotEmpty(
            epType.getEndpointConfigurationList().getEndpointConfiguration())) {
            for (EndpointConfigurationType config : epType.getEndpointConfigurationList().getEndpointConfiguration()) {
                specVersionList.add(NhincConstants.UDDI_SPEC_VERSION.fromString(config.getVersion()));
            }
        }
        return specVersionList;
    }

    public static EndpointConfigurationType getEndPointConfigBasedOnSpecVersion(EndpointType epType, String version) {
        if (null != epType && epType.getEndpointConfigurationList() != null && CollectionUtils.isNotEmpty(
            epType.getEndpointConfigurationList().getEndpointConfiguration())) {
            for (EndpointConfigurationType configType : epType.getEndpointConfigurationList().
                getEndpointConfiguration()) {
                if (version.equalsIgnoreCase(configType.getVersion())) {
                    return configType;
                }
            }
        }
        return null;
    }

    public static String getHomeCommunityFromPropFile() {
        String sHomeCommunityId = null;
        try {
            sHomeCommunityId = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            LOG.error("Error: Failed to retrieve {} from property file: {}", NhincConstants.HOME_COMMUNITY_ID_PROPERTY,
                NhincConstants.GATEWAY_PROPERTY_FILE, ex);
        }
        return sHomeCommunityId;
    }

    public NhincConstants.UDDI_SPEC_VERSION getHighestUDDISpecVersion(
        List<NhincConstants.UDDI_SPEC_VERSION> specVersions) {
        NhincConstants.UDDI_SPEC_VERSION highestSpecVersion = null;

        try {
            for (NhincConstants.UDDI_SPEC_VERSION specVersion : specVersions) {
                if (highestSpecVersion == null || specVersion.ordinal() > highestSpecVersion.ordinal()) {
                    highestSpecVersion = specVersion;
                }
            }
        } catch (Exception ex) {
            LOG.error("Error deducing highest spec version.", ex);
        }

        return highestSpecVersion;
    }

    public String getEndpointURLByServiceNameSpecVersion(OrganizationType org, String serviceName,
        NhincConstants.UDDI_SPEC_VERSION version) {
        String epURL = "";
        if (org.getEndpointList() != null && CollectionUtils.isNotEmpty(org.getEndpointList().getEndpoint())) {
            for (EndpointType epType : org.getEndpointList().getEndpoint()) {
                if (hasService(epType, serviceName)) {
                    epURL = getEndpointURLBySpecVersion(epType.getEndpointConfigurationList(), version);
                }
            }
        }
        return epURL;
    }

    public String getEndpointURLBySpecVersion(EndpointConfigurationListType epConfigList,
        NhincConstants.UDDI_SPEC_VERSION version) {
        if (null != epConfigList && CollectionUtils.isNotEmpty(epConfigList.getEndpointConfiguration())) {
            for (EndpointConfigurationType epConfig : epConfigList.getEndpointConfiguration()) {
                if (version.toString().equals(epConfig.getVersion())) {
                    return epConfig.getUrl();
                }
            }
        }
        return "";
    }

    public static EndpointType getServiceEndpointType(OrganizationType org, String serviceName) {
        if (null != org && org.getEndpointList() != null && CollectionUtils.isNotEmpty(org.getEndpointList().
            getEndpoint())) {
            for (EndpointType epType : org.getEndpointList().getEndpoint()) {
                if (hasService(epType, serviceName)) {
                    return epType;
                }
            }
        }
        return null;
    }

    // static-methods-ExchangeInfo_xml
    public static List<OrganizationType> getOrganizationTypeBy(ExchangeInfoType exchangeInfo) {
        return getOrganizationTypeBy(exchangeInfo, ELEMENT_NOT_REQUIRED);
    }

    public static List<OrganizationType> getOrganizationTypeBy(ExchangeInfoType exchangeInfo, boolean requiredElement) {
        return getOrganizationTypeBy(exchangeInfo, null, requiredElement);
    }

    public static List<OrganizationType> getOrganizationTypeBy(ExchangeType exchange) {
        return getOrganizationTypeBy(exchange, ELEMENT_NOT_REQUIRED);
    }

    public static List<OrganizationType> getOrganizationTypeBy(ExchangeType exchange, boolean requiredElement) {
        if (null != exchange ) {
            if(requiredElement && null == exchange.getOrganizationList()) {
                exchange.setOrganizationList(new OrganizationListType());
            }
            if(null != exchange.getOrganizationList()){
                return exchange.getOrganizationList().getOrganization();
            }
        }
        return new ArrayList<>();
    }

    public static List<OrganizationType> getOrganizationTypeBy(ExchangeInfoType exchangeInfo, String exchangeName,
        boolean requiredElement) {
        return getOrganizationTypeBy(findExchangeTypeBy(getExchangeTypeBy(exchangeInfo, requiredElement), exchangeName),
            requiredElement);
    }

    public static OrganizationType findOrganizationTypeBy(List<OrganizationType> organizations, String hcid) {
        if (CollectionUtils.isEmpty(organizations)) {
            return null;
        }
        if (StringUtils.isBlank(hcid)) {
            return organizations.get(0);
        }
        for (OrganizationType organization : organizations) {
            if (equalsIgnoreCaseForHCID(hcid, organization.getHcid())) {
                return organization;
            }
        }
        return null;
    }

    public static List<OrganizationType> getOrganizationTypeAll(ExchangeInfoType exchangeInfo) {
        List<ExchangeType> exchanges = getExchangeTypeBy(exchangeInfo);
        List<OrganizationType> organizations = new ArrayList<>();
        for (ExchangeType exchange : exchanges) {
            addAll(organizations, getOrganizationTypeBy(exchange));
        }
        return organizations;
    }

    public static List<ExchangeType> getExchangeTypeBy(ExchangeInfoType exchangeInfo) {
        return getExchangeTypeBy(exchangeInfo, ELEMENT_NOT_REQUIRED);
    }

    public static List<ExchangeType> getExchangeTypeBy(ExchangeInfoType exchangeInfo, boolean requiredElement) {
        if (null != exchangeInfo) {
            if (requiredElement && null == exchangeInfo.getExchanges()) {
                exchangeInfo.setExchanges(new ExchangeListType());
            }
            if (null != exchangeInfo.getExchanges()) {
                return exchangeInfo.getExchanges().getExchange();
            }
        }
        return new ArrayList<>();
    }

    public static ExchangeType findExchangeTypeBy(List<ExchangeType> exchanges, String exchangeName) {
        if(CollectionUtils.isEmpty(exchanges)) {
            return null;
        }
        if (StringUtils.isBlank(exchangeName)) {
            return exchanges.get(0);
        }
        for (ExchangeType exchange : exchanges) {
            if (exchange.getName().equalsIgnoreCase(exchangeName)) {
                return exchange;
            }
        }
        return null;
    }

    public static EndpointConfigurationType findEndpointConfigurationTypeBy(EndpointType endpoint,
        UDDI_SPEC_VERSION version) {
        return findEndpointConfigurationTypeBy(getEndpointConfigurationTypeBy(endpoint), version);
    }

    public static List<EndpointConfigurationType> getEndpointConfigurationTypeBy(EndpointType endpoint) {
        return getEndpointConfigurationTypeBy(endpoint, ELEMENT_NOT_REQUIRED);
    }

    public static List<EndpointConfigurationType> getEndpointConfigurationTypeBy(EndpointType endpoint,
        boolean requiredElement) {
        if (null != endpoint) {
            if (requiredElement && null == endpoint.getEndpointConfigurationList()) {
                endpoint.setEndpointConfigurationList(new EndpointConfigurationListType());
            }
            if (null != endpoint.getEndpointConfigurationList()) {
                return endpoint.getEndpointConfigurationList().getEndpointConfiguration();
            }
        }
        return new ArrayList<>();
    }

    public static EndpointConfigurationType findEndpointConfigurationTypeBy(
        List<EndpointConfigurationType> epConfigurations, UDDI_SPEC_VERSION version) {
        if (CollectionUtils.isEmpty(epConfigurations)) {
            return null;
        }
        if (null == version) {
            return epConfigurations.get(0);
        }
        for (EndpointConfigurationType epConfiguration : epConfigurations) {
            if (epConfiguration.getVersion().equalsIgnoreCase(version.toString())) {
                return epConfiguration;
            }
        }
        return null;
    }

    public static List<EndpointType> getEndpointTypeBy(OrganizationType organization) {
        return getEndpointTypeBy(organization, ELEMENT_NOT_REQUIRED);
    }

    public static List<EndpointType> getEndpointTypeBy(OrganizationType organization, boolean requiredElement) {
        if (null != organization) {
            if (requiredElement && null == organization.getEndpointList()) {
                organization.setEndpointList(new EndpointListType());
            }
            if (null != organization.getEndpointList()) {
                return organization.getEndpointList().getEndpoint();
            }
        }
        return new ArrayList<>();
    }

    public static EndpointType findEndpointTypeBy(OrganizationType organization, String serviceName) {
        return findEndpointTypeBy(getEndpointTypeBy(organization), serviceName);
    }

    public static EndpointType findEndpointTypeBy(List<EndpointType> endpoints, String serviceName) {
        if (CollectionUtils.isEmpty(endpoints)) {
            return null;
        }
        if (StringUtils.isBlank(serviceName)) {
            return endpoints.get(0);
        }
        for (EndpointType endpoint : endpoints) {
            if (containsIgnoreCaseBy(endpoint.getName(), serviceName)) {
                return endpoint;
            }
        }
        return null;
    }

    // merging-exchangeInfo
    public static ExchangeInfoType mergeExchangeInfoType(ExchangeInfoType itemA, final ExchangeInfoType itemB) {
        combineExchangeType(getExchangeTypeBy(itemA, REQUIRED_ELEMENT), getExchangeTypeBy(itemB));
        return itemA;
    }

    public static List<ExchangeType> combineExchangeType(List<ExchangeType> listA, final List<ExchangeType> listB) {
        for (ExchangeType exchangeB : listB) {
            mergeExchangeTypeByName(listA, exchangeB);
        }
        return listA;
    }

    private static List<ExchangeType> mergeExchangeTypeByName(List<ExchangeType> listA, ExchangeType itemB) {
        for (ExchangeType itemA : listA) {
            if (itemA.getName() != null && itemB.getName() != null) {
                if (itemA.getName().equalsIgnoreCase(itemB.getName())) {
                    combineOrganizationType(getOrganizationTypeBy(itemA, REQUIRED_ELEMENT),
                        getOrganizationTypeBy(itemB));
                    return listA;
                }
            } else if (itemA.getName() == null && itemB.getName() == null) {
                combineOrganizationType(getOrganizationTypeBy(itemA, REQUIRED_ELEMENT), getOrganizationTypeBy(itemB));
                return listA;
            }
        }
        listA.add(itemB);
        return listA;
    }

    public static List<OrganizationType> combineOrganizationType(List<OrganizationType> listA,
        final List<OrganizationType> listB) {
        for (OrganizationType organizationB : listB) {
            mergeOrganizationTypeByHcid(listA, organizationB);
        }
        return listA;
    }

    private static List<OrganizationType> mergeOrganizationTypeByHcid(List<OrganizationType> listA,
        OrganizationType itemB) {
        for (OrganizationType itemA : listA) {
            if (equalsIgnoreCaseForHCID(itemA.getHcid(), itemB.getHcid())) {
                combineEndpointType(getEndpointTypeBy(itemA, REQUIRED_ELEMENT), getEndpointTypeBy(itemB));
                combine(itemA.getAddress(), itemB.getAddress());
                combine(itemA.getContact(), itemB.getContact());
                combine(itemA.getTargetRegion(), itemB.getTargetRegion());
                return listA;
            }
        }
        listA.add(itemB);
        return listA;
    }

    public static List<EndpointType> combineEndpointType(List<EndpointType> listA, final List<EndpointType> listB) {
        for (EndpointType endpointB : listB) {
            mergeEndpointTypeByServiceNames(listA, endpointB);
        }
        return listA;
    }

    private static List<EndpointType> mergeEndpointTypeByServiceNames(List<EndpointType> listA, EndpointType itemB) {
        for (EndpointType itemA : listA) {
            if (equalsIgnoreCaseAny(itemA.getName(), itemB.getName())) {
                combine(getEndpointConfigurationTypeBy(itemA, REQUIRED_ELEMENT), getEndpointConfigurationTypeBy(itemB));
                combine(itemA.getName(), itemB.getName());
                combine(itemA.getPayloadFormat(), itemB.getPayloadFormat());
                combine(itemA.getPayloadType(), itemB.getPayloadType());
                return listA;
            }
        }
        listA.add(itemB);
        return listA;
    }

    //private-methods
    private static boolean containsIgnoreCaseBy(List<String> stringContainers, String compareTo) {
        if (CollectionUtils.isNotEmpty(stringContainers)) {
            for (String compareWith : stringContainers) {
                if (compareWith.equalsIgnoreCase(compareTo)) {
                    return true;
                }
            }
        }
        return false;
    }


}
