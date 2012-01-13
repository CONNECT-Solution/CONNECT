/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
/**
 *
 * @author dunnek
 */
public class AdminDistributionHelper {
    private Log log = null;

    public AdminDistributionHelper()
    {
        log = createLogger();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    public NhinTargetSystemType createNhinTargetSystemType(String homeCommunityId)
    {
        HomeCommunityType hc = new HomeCommunityType();

        hc.setHomeCommunityId(homeCommunityId);

        return createNhinTargetSystemType(hc);
    }
    public NhinTargetSystemType createNhinTargetSystemType(HomeCommunityType hc)
    {
        NhinTargetSystemType result = new NhinTargetSystemType();
        result.setHomeCommunity(hc);

        return result;
    }
    public String getLocalCommunityId()
    {
        return readStringGatewayProperty(NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
    }
    public String getUrl(String targetHCID, String targetSystem) {
        log.debug("begin getUrl targetHCID/targetSystem: " + targetHCID + " / " + targetSystem);
        String url = null;

        if (targetHCID != null) {
            try {

                NhinTargetSystemType ts = this.createNhinTargetSystemType(targetHCID);

                url = getWebServiceProxyHelper().getUrlFromTargetSystem(ts, targetSystem);
            } catch (Exception ex) {
                log.error("Error: Failed to retrieve url for service: " + targetSystem);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }

        return url;
    }
    public String getUrl(NhinTargetSystemType target, String targetSystem) {
        log.debug("begin getUrl target/targetSystem: " + target + " / " + targetSystem);
        String url = null;

        if (target != null) {
            try {
                url = getWebServiceProxyHelper().getUrlFromTargetSystem(target, targetSystem);

            } catch (Exception ex) {
                log.error("Error: Failed to retrieve url for service: " + targetSystem);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }

        return url;
    }
    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }

    public boolean isInPassThroughMode()
    {
        return readBooleanGatewayProperty(NhincConstants.NHIN_ADMIN_DIST_SERVICE_PASSTHRU_PROPERTY);
    }
    public boolean isServiceEnabled()
    {
        return readBooleanGatewayProperty(NhincConstants.NHIN_ADMIN_DIST_SERVICE_ENABLED);
    }
    public boolean readBooleanGatewayProperty(String propertyName)
    {
        boolean result = false;
        try {
            result = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, propertyName);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + propertyName + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return result;
    }
    public String readStringGatewayProperty(String propertyName) {
        String result = "";
        try {
            result = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, propertyName);
        }
        catch(Exception ex) {
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
    // TODO: Hack the api version for testing 
    public String getApiVersion() {
        return readStringGatewayProperty("ad-api-version");
    }
}
