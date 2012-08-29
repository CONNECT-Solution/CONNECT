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
package gov.hhs.fha.nhinc.hiem.configuration;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.hiem.processor.common.HiemProcessorConstants;

import gov.hhs.fha.nhinc.hiem.processor.faults.ConfigurationException;
import gov.hhs.fha.nhinc.hiem.processor.faults.SoapFaultFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

/**
 * 
 * @author rayj
 */
public class ConfigurationManager {

    public boolean isAuditEnabled() {
        return false;
    }

    public String getSubscriptionServiceMode() throws ConfigurationException {
        if (isInPassThroughMode(NhincConstants.HIEM_SUBSCRIPTION_SERVICE_PASSTHRU_PROPERTY)) {
            return HiemProcessorConstants.HIEM_SERVICE_MODE_PASSTHROUGH;
        } else if (isServiceEnabled(NhincConstants.HIEM_SUBSCRIPTION_SERVICE_PROPERTY) == false) {
            return HiemProcessorConstants.HIEM_SERVICE_MODE_NOT_SUPPORTED;
        } else {
            return HiemProcessorConstants.HIEM_SERVICE_MODE_SUPPORTED;
        }
    }

    public String getAdapterSubscriptionMode() throws ConfigurationException {
        String value = null;
        try {
            value = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.HIEM_ADAPTER_SUBSCRIPTION_MODE_PROPERTY);
        } catch (PropertyAccessException ex) {
            throw new SoapFaultFactory().getConfigurationException("Failed to determine adapter subscription mode ['"
                    + NhincConstants.HIEM_ADAPTER_SUBSCRIPTION_MODE_PROPERTY + "'].", ex);
        }
        return value;
    }

    public String getNotificationServiceMode() throws ConfigurationException {
        if (isInPassThroughMode(NhincConstants.HIEM_NOTIFY_SERVICE_PASSTHRU_PROPERTY)) {
            return HiemProcessorConstants.HIEM_SERVICE_MODE_PASSTHROUGH;
        } else if (isServiceEnabled(NhincConstants.HIEM_NOTIFY_SERVICE_PROPERTY) == false) {
            return HiemProcessorConstants.HIEM_SERVICE_MODE_NOT_SUPPORTED;
        } else {
            return HiemProcessorConstants.HIEM_SERVICE_MODE_SUPPORTED;
        }
    }

    private boolean isServiceEnabled(String service) throws ConfigurationException {
        boolean serviceEnabled = false;

        try {
            serviceEnabled = PropertyAccessor.getInstance().getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, service);
        } catch (PropertyAccessException ex) {
            throw new SoapFaultFactory().getConfigurationException("Failed to determine if service '" + service
                    + "' is enabled.", ex);
        }

        return serviceEnabled;
    }

    private boolean isInPassThroughMode(String service) throws ConfigurationException {
        boolean passThroughModeEnabled = false;

        try {
            passThroughModeEnabled = PropertyAccessor.getInstance().getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, service);
        } catch (PropertyAccessException ex) {
            throw new SoapFaultFactory().getConfigurationException("Failed to determine if service '" + service
                    + "' is pass through mode.", ex);
        }

        return passThroughModeEnabled;
    }

    public String getEntityNotificationConsumerAddress() throws ConfigurationException {
        String url = null;
        try {
            url = ConnectionManagerCache.getInstance().getInternalEndpointURLByServiceName(
                    NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            throw new ConfigurationException("Unable to determine EntityNotificationConsumerAddress", ex);
        }
        return url;
    }
}
