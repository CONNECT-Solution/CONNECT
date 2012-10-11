/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 * All other rights reserved.
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
package gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * Common Service Helper Functions
 *
 *
 *
 * @author Jerry Goodnough
 */
public class ServiceHelper {

    private static Log log = LogFactory.getLog(
        ServiceHelper.class);
    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String HOME_COMMUNITY_ID_PROPERTY = "localHomeCommunityId";

    /**
     *
     * Helper Function to get an Endpoint from BOS by Service Name
     *
     * @param srvName Name
     *
     * @return The endpoint reference or null if BOS does not have a mapping
     */
    public static String getEndpointFromBOS(String srvName) {

        // Get the Home community ID for this box...

        // ------------------------------------------

        String sHomeCommunityId = "";

        String sEndpointURL = "";

        try {
            sHomeCommunityId = PropertyAccessor.getInstance().getProperty(GATEWAY_PROPERTY_FILE, HOME_COMMUNITY_ID_PROPERTY);
        } catch (Exception e) {

            log.error("Failed to read " + HOME_COMMUNITY_ID_PROPERTY +
                " property from the " + GATEWAY_PROPERTY_FILE + ".properties  file.  Error: " +
                e.getMessage(), e);

        }

        try {
            sEndpointURL = ConnectionManagerCache.getInstance().getDefaultEndpointURLByServiceName(sHomeCommunityId, srvName);
        } catch (Exception e) {

            log.error("Failed to retrieve endpoint URL for service:" + srvName +
                " from connection manager.  Error: " + e.getMessage(), e);

        }

        return sEndpointURL;

    }
}
