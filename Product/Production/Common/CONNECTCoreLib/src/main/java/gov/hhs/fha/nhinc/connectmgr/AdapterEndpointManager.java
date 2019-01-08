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
package gov.hhs.fha.nhinc.connectmgr;

import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerHelper;
import gov.hhs.fha.nhinc.exchangemgr.InternalExchangeManager;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdapterEndpointManager {

    public static final String ADAPTER_API_LEVEL_KEY = "CONNECT:adapter:apilevel";
    public static final Logger LOG = LoggerFactory.getLogger(AdapterEndpointManager.class);

    public ADAPTER_API_LEVEL getApiVersion(String serviceName) {
        ADAPTER_API_LEVEL result = null;
        try {
            Set<ADAPTER_API_LEVEL> apiLevels = getAdapterAPILevelsByServiceName(serviceName);
            result = getHighestGatewayApiLevel(apiLevels);
        } catch (Exception ex) {
            LOG.error("Error getting API version: ", ex);
        }

        return result == null ? ADAPTER_API_LEVEL.LEVEL_a1 : result;
    }

    private ADAPTER_API_LEVEL getHighestGatewayApiLevel(Set<ADAPTER_API_LEVEL> apiLevels) {
        ADAPTER_API_LEVEL highestApiLevel = null;

        try {
            for (ADAPTER_API_LEVEL apiLevel : apiLevels) {
                if (highestApiLevel == null || apiLevel.ordinal() > highestApiLevel.ordinal()) {
                    highestApiLevel = apiLevel;
                }
            }
        } catch (Exception ex) {
            LOG.error("Error getting highest API Level: ", ex);
        }

        return highestApiLevel;
    }

    public Set<ADAPTER_API_LEVEL> getAdapterAPILevelsByServiceName(String serviceName) {
        Set<ADAPTER_API_LEVEL> apiLevels = null;

        try {
            String sHomeCommunityId = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY);

            OrganizationType org = InternalExchangeManager.getInstance().getOrganization(sHomeCommunityId);
            apiLevels = getAPILevelsFromOrganization(org, serviceName);

        } catch (ExchangeManagerException | PropertyAccessException ex) {
            LOG.error("Error getting API Level by Service Name: {}", ex.getLocalizedMessage(), ex);
        }
        return apiLevels;
    }

    private Set<ADAPTER_API_LEVEL> getAPILevelsFromOrganization(OrganizationType org, String serviceName) {
        Set<ADAPTER_API_LEVEL> apiLevels = new HashSet<>();
        if (null != org && null != org.getEndpointList() && CollectionUtils.isNotEmpty(org.getEndpointList().
            getEndpoint())) {
            for (EndpointType epType : org.getEndpointList().getEndpoint()) {
                apiLevels.addAll(getAPILevels(epType, serviceName));
            }
        }
        return apiLevels;
    }

    private Set<ADAPTER_API_LEVEL> getAPILevels(EndpointType epType, String serviceName) {
        Set<ADAPTER_API_LEVEL> apiLevels = new HashSet<>();
        if (ExchangeManagerHelper.hasService(epType, serviceName) && null != epType.getEndpointConfigurationList()
            && CollectionUtils.
                isNotEmpty(epType.getEndpointConfigurationList().getEndpointConfiguration())) {
            for (EndpointConfigurationType config : epType.getEndpointConfigurationList().getEndpointConfiguration()) {
                apiLevels.add(ADAPTER_API_LEVEL.valueOf(config.getVersion()));
            }
        }
        return apiLevels;
    }
}
