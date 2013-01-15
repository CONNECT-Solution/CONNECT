/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.callback.purposeuse;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author rhalfert
 */
public abstract class PurposeUseProxyBaseImpl implements PurposeUseProxy {

    private static Log log = LogFactory.getLog(PurposeUseProxyBaseImpl.class);

    private static final String PURPOSE_FOR_USE_DEPRECATED_ENABLED = "purposeForUseEnabled";
    
     /**
     * Returns boolean condition on whether PurposeForUse is enabled.
     * @return The PurposeForUse enabled setting
     */
    public boolean isPurposeForUseEnabled() {
        log.info("Entering PurposeUseProxyBaseImpl.isPurposeForUseEnabled...");
        boolean match = false;
        try {
            // Use CONNECT utility class to access gateway.properties
            String purposeForUseEnabled = PropertyAccessor.getInstance()
                    .getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, PURPOSE_FOR_USE_DEPRECATED_ENABLED);
            if (purposeForUseEnabled != null && purposeForUseEnabled.equalsIgnoreCase("true")) {
                match = true;
            }
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + PURPOSE_FOR_USE_DEPRECATED_ENABLED
                    + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return match;
    }
}
