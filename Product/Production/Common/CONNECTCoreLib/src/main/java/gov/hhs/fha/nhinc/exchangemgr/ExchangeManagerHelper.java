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

import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationListType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class ExchangeManagerHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeManagerHelper.class);

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

    public boolean hasService(EndpointType epType, String sUniformServiceName) {
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

    public EndpointConfigurationType getEndPointConfigBasedOnSpecVersion(EndpointType epType, String version) {
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

    public String getHomeCommunityFromPropFile() {
        String sHomeCommunityId = null;
        try {
            sHomeCommunityId = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            LOG.error("Error: Failed to retrieve " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY + " from property file: "
                + NhincConstants.GATEWAY_PROPERTY_FILE, ex);
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

    public EndpointType getServiceEndpointType(OrganizationType org, String serviceName) {
        if (org.getEndpointList() != null && CollectionUtils.isNotEmpty(org.getEndpointList().getEndpoint())) {
            for (EndpointType epType : org.getEndpointList().getEndpoint()) {
                if (hasService(epType, serviceName)) {
                    return epType;
                }
            }
        }
        return null;
    }
}
