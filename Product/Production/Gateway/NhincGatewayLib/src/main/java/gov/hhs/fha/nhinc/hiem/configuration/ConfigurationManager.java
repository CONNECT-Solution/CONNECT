/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
            value = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HIEM_ADAPTER_SUBSCRIPTION_MODE_PROPERTY);
        } catch (PropertyAccessException ex) {
            throw new SoapFaultFactory().getConfigurationException("Failed to determine adapter subscription mode ['" + NhincConstants.HIEM_ADAPTER_SUBSCRIPTION_MODE_PROPERTY + "'].", ex);
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
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, service);
        } catch (PropertyAccessException ex) {
            throw new SoapFaultFactory().getConfigurationException("Failed to determine if service '" + service + "' is enabled.", ex);
        }

        return serviceEnabled;
    }

    private boolean isInPassThroughMode(String service) throws ConfigurationException {
        boolean passThroughModeEnabled = false;

        try {
            passThroughModeEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, service);
        } catch (PropertyAccessException ex) {
            throw new SoapFaultFactory().getConfigurationException("Failed to determine if service '" + service + "' is pass through mode.", ex);
        }

        return passThroughModeEnabled;
    }

    public String getEntityNotificationConsumerAddress() throws ConfigurationException {
        String url = null;
        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            throw new ConfigurationException("Unable to determine EntityNotificationConsumerAddress", ex);
        }
        return url;
    }
}
