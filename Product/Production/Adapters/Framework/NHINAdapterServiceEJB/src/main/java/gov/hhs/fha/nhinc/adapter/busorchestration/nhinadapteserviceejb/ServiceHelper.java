/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Common Service Helper Functions
 *
 * @author Jerry Goodnough
 */
public class ServiceHelper {
   private static Log log = LogFactory.getLog(
            ServiceHelper.class);

    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String HOME_COMMUNITY_ID_PROPERTY = "localHomeCommunityId";

    /**
     * Helper Function to get an Endpoint from BOS by Service Name
     * @param srvName Name
     * @return The endpoint reference or null if BOS does not have a mapping
     */
    public static String getEndpointFromBOS(String srvName)
    {
        // Get the Home community ID for this box...
        //------------------------------------------
        String sHomeCommunityId = "";
        String sEndpointURL = "";
        try {
            sHomeCommunityId = PropertyAccessor.getProperty(GATEWAY_PROPERTY_FILE, HOME_COMMUNITY_ID_PROPERTY);
        } catch (Exception e) {
            log.error("Failed to read " + HOME_COMMUNITY_ID_PROPERTY +
                    " property from the " + GATEWAY_PROPERTY_FILE + ".properties  file.  Error: " +
                    e.getMessage(), e);
        }
        try {
            sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName(sHomeCommunityId, srvName);
        } catch (Exception e) {
            log.error("Failed to retrieve endpoint URL for service:" + srvName+
                    " from connection manager.  Error: " + e.getMessage(), e);
        }

        return sEndpointURL;


    }

}
