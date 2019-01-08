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
package gov.hhs.fha.nhinc.admindistribution;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.exchangemgr.InternalExchangeManager;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class AdminDistributionHelper {

    private static final Logger LOG = LoggerFactory.getLogger(AdminDistributionHelper.class);
    private final WebServiceProxyHelper webServiceProxyHelper;

    /**
     * Constructs an instance of WebServiceProxyHelper.
     */
    public AdminDistributionHelper() {
        webServiceProxyHelper = new WebServiceProxyHelper();
    }

    /**
     * @param webServiceProxyHelper The instance of webServiceProxyHelper to use
     */
    public AdminDistributionHelper(WebServiceProxyHelper webServiceProxyHelper) {
        this.webServiceProxyHelper = webServiceProxyHelper;
    }

    /**
     * @param targetHCID the targetHCID
     * @return NhinTargetSystemType
     */
    public NhinTargetSystemType createNhinTargetSystemType(String targetHCID) {
        if (NullChecker.isNotNullish(targetHCID)) {
            HomeCommunityType hc = new HomeCommunityType();
            hc.setHomeCommunityId(targetHCID);
            return createNhinTargetSystemType(hc);
        } else {
            LOG.error("Target ID is null");
        }

        return null;
    }

    /**
     * @param hc HomeCommunityType
     * @return NhinTargetSystemType
     */
    public NhinTargetSystemType createNhinTargetSystemType(HomeCommunityType hc) {
        NhinTargetSystemType result = new NhinTargetSystemType();
        result.setHomeCommunity(hc);

        return result;
    }

    /**
     * @return The Local HCID
     */
    public String getLocalCommunityId() {
        return readStringGatewayProperty(NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
    }

    /**
     * @param targetHCID the targetHCID
     * @param targetSystem the target system
     * @param apiLevel which gateway/spec version to use
     * @return the url
     */
    public String getUrl(String targetHCID, String targetSystem, GATEWAY_API_LEVEL apiLevel) {
        LOG.debug("begin getUrl targetHCID/targetSystem: " + targetHCID + " / " + targetSystem);

        NhinTargetSystemType ts = createNhinTargetSystemType(targetHCID);
        return getUrl(ts, targetSystem, apiLevel);
    }

    /**
     * This method retrieves url of the Nhin targetcommunity.
     *
     * @param target The Nhin target Community received.
     * @param targetSystem The targetsystem received.
     * @param apiLevel The gateway apiLevel g0 or g1.
     * @return url Url of the targetcommunity based on g0 or g1 apiLevel.
     */
    public String getUrl(NhinTargetSystemType target, String targetSystem, GATEWAY_API_LEVEL apiLevel) {
        LOG.debug("begin getUrl target/targetSystem: " + target + " / " + targetSystem);
        String url = null;

        if (target != null) {
            try {
                url = webServiceProxyHelper.getUrlFromTargetSystemByGatewayAPILevel(target, targetSystem, apiLevel);
            } catch (Exception ex) {
                LOG.error("Error: Failed to retrieve url for service {}: {}", targetSystem, ex.getLocalizedMessage(),
                    ex);
            }
        } else {
            LOG.error("Target system passed into the proxy is null");
        }

        LOG.debug("end getUrl target/targetSystem url= " + url);

        return url;
    }

    /**
     * This method retrieves adapterservice url for AdminDist..
     *
     * @param adapterServcice The name of AdapterService in internalconnectionInfo.xml.
     * @param adapterApiLevel The adapter apiLevel a0 or a1.
     * @return adapter url from internalconnectioninfo.xml.
     */
    public String getAdapterUrl(String adapterServcice, NhincConstants.ADAPTER_API_LEVEL adapterApiLevel) {
        try {
            return InternalExchangeManager.getInstance().getEndpointURL(adapterServcice, adapterApiLevel);
        } catch (ExchangeManagerException ex) {
            LOG.error("Error: Failed to retrieve url for service {}: {}",
                NhincConstants.ADAPTER_ADMIN_DIST_SECURED_SERVICE_NAME, ex.getLocalizedMessage(), ex);
        }

        return null;
    }

    /**
     * This method read pasased in property value from gateway.property and returns boolean.
     *
     * @param propertyName The Property name passed in to read property value from gateway.properties.
     * @return true or false value from gateway.properties for that specific property.
     */
    public boolean readBooleanGatewayProperty(String propertyName) {
        boolean result = false;
        try {
            result = PropertyAccessor.getInstance().getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
                propertyName);
        } catch (PropertyAccessException ex) {
            LOG.error("Error: Failed to retrieve {} from property file {}: ", propertyName,
                NhincConstants.GATEWAY_PROPERTY_FILE, ex.getLocalizedMessage(), ex);
        }
        return result;
    }

    /**
     * This method read PropertyName from gateway.properties.
     *
     * @param propertyName Property received to read from gateway.properties.
     * @return PropertyName from gateway.properties.
     */
    public String readStringGatewayProperty(String propertyName) {
        String result = "";
        try {
            result = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, propertyName);
        } catch (Exception ex) {
            LOG.error("Unable to retrieve property {} from {}.properties: {}", propertyName,
                NhincConstants.GATEWAY_PROPERTY_FILE, ex.getLocalizedMessage(), ex);
        }
        return result;
    }

    /**
     * This method builds NhinTargetSystem for the homeCommunityId passed.
     *
     * @param homeCommunityId homeCommunityId received.
     * @return targetSystem.
     */
    public NhinTargetSystemType buildHomeCommunity(String homeCommunityId) {
        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(homeCommunityId);
        nhinTargetSystem.setHomeCommunity(homeCommunity);
        return nhinTargetSystem;
    }
}
