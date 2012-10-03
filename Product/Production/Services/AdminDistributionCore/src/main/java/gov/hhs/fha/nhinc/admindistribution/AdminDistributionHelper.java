/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

/**
 *
 * @author dunnek
 */
public class AdminDistributionHelper {

    private final Log log = LogFactory.getLog(AdminDistributionHelper.class);
    private final WebServiceProxyHelper webServiceProxyHelper;

    /**
     * Constructs an instance of WebServiceProxyHelper.
     */
    public AdminDistributionHelper() {
        this.webServiceProxyHelper = new WebServiceProxyHelper();
    }

    /**
     * @param webServiceProxyHelper The instance of webServiceProxyHelper to use
     */
    public AdminDistributionHelper(WebServiceProxyHelper webServiceProxyHelper) {
        this.webServiceProxyHelper = webServiceProxyHelper;
    }

    /**
     *
     * @return Log
     */
    protected Log createLogger() {
        return log;
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
            log.error("Target ID is null");
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
        log.debug("begin getUrl targetHCID/targetSystem: " + targetHCID + " / " + targetSystem);

        NhinTargetSystemType ts = createNhinTargetSystemType(targetHCID);
        return getUrl(ts, targetSystem, apiLevel);
    }

    public String getUrl(NhinTargetSystemType target, String targetSystem, GATEWAY_API_LEVEL apiLevel) {
        log.debug("begin getUrl target/targetSystem: " + target + " / " + targetSystem);
        String url = null;

        if (target != null) {
            try {
                url = webServiceProxyHelper.getUrlFromTargetSystemByGatewayAPILevel(target, targetSystem, apiLevel);

            } catch (Exception ex) {
                log.error("Error: Failed to retrieve url for service: " + targetSystem);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }
        log.debug("end getUrl target/targetSystem url= " + url);

        return url;
    }

    public String getAdapterUrl(String adapterServcice, NhincConstants.ADAPTER_API_LEVEL adapterApiLevel) {
        try {
            return ConnectionManagerCache.getInstance().getAdapterEndpointURL(adapterServcice, adapterApiLevel);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: "
                    + NhincConstants.ADAPTER_ADMIN_DIST_SECURED_SERVICE_NAME);
            log.error(ex.getMessage());
        }

        return null;
    }

    public boolean isInPassThroughMode() {
        return readBooleanGatewayProperty(NhincConstants.NHIN_ADMIN_DIST_SERVICE_PASSTHRU_PROPERTY);
    }
    
    public boolean readBooleanGatewayProperty(String propertyName) {
        boolean result = false;
        try {
            result = PropertyAccessor.getInstance().getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, propertyName);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + propertyName + " from property file: "
                    + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return result;
    }

    public String readStringGatewayProperty(String propertyName) {
        String result = "";
        try {
            result = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, propertyName);
        } catch (Exception ex) {
            log.error("Unable to retrieve " + propertyName + " from Gateway.properties");
            log.error(ex);
        }
        log.debug("begin Gateway property: " + propertyName + " - " + result);
        return result;
    }

    public NhinTargetSystemType buildHomeCommunity(String homeCommunityId) {
        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(homeCommunityId);
        nhinTargetSystem.setHomeCommunity(homeCommunity);
        return nhinTargetSystem;
    }

}
