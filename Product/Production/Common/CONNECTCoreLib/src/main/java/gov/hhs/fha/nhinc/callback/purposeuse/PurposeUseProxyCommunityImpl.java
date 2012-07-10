/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.callback.purposeuse;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author rhalfert
 */
public class PurposeUseProxyCommunityImpl extends PurposeUseProxyBaseImpl{

    private static Log log = LogFactory.getLog(PurposeUseProxyCommunityImpl.class);

    private static final String PURPOSE_FOR_USE_DEPRECATED_ENABLED = "purposeForUseEnabled";
    private static final String PURPOSE_FOR_USE_PROPERTY_FILE = "purposeUse";
    /**
     * Check if purposeForUse is required for the given home community.
     * @param tokens A list of tokens from the assertion
     * @return true if purposeForUse should be used
     */
    public boolean createPurposeUseElement(HashMap<Object, Object> tokens) {
        log.info("Entering PurposeUseProxyCommunityImpl.createPurposeUseElement...");
        String homeCommunityId = null;
        /*if (tokens.containsKey(SamlConstants.HOME_COM_PROP)
                && tokens.get(SamlConstants.HOME_COM_PROP) != null) {
            homeCommunityId = tokens.get(SamlConstants.HOME_COM_PROP).toString();
        }*/

        if (tokens.containsKey(NhincConstants.WS_SOAP_TARGET_HOME_COMMUNITY_ID)
                && tokens.get(NhincConstants.WS_SOAP_TARGET_HOME_COMMUNITY_ID) != null) {
            homeCommunityId = tokens.get(NhincConstants.WS_SOAP_TARGET_HOME_COMMUNITY_ID).toString();
        }
        return ((NullChecker.isNotNullish(homeCommunityId) 
                && isPurposeForUseEnabled(homeCommunityId))
                || isPurposeForUseEnabled());
    }

    /**
    * Returns boolean condition on whether PurposeForUse is enabled based on HCID.
    * @param  homeCommunityId The home community for which to check the property setting
    * @return The PurposeForUse enabled setting
    */
    private boolean isPurposeForUseEnabled(String homeCommunityId) {
        boolean match = false;
        try {
            String purposeForUseEnabled = PropertyAccessor.getInstance()
                    .getProperty(PURPOSE_FOR_USE_PROPERTY_FILE, homeCommunityId);
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
