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
package gov.hhs.fha.nhinc.admingui.display;

import gov.hhs.fha.nhinc.admingui.proxy.AdminGUIProxyObjectFactory;
import gov.hhs.fha.nhinc.admingui.proxy.DirectConfigConstants;
import gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jsmith
 */
public class DirectDisplayController implements DisplayController {

    private final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    private static final Logger LOG = LoggerFactory.getLogger(DirectDisplayController.class);

    /**
     * Determines if direct is currently enabled and therefore direct pages can be displayed in the Admin GUI.
     */
    @Override
    public void checkDisplay() {
        DisplayHolder.getInstance().setDirectEnabled(directConfigEnabled());
    }

    private boolean directConfigEnabled() {
        boolean result;
        try {
            String url = oProxyHelper
                .getAdapterEndPointFromConnectionManager(DirectConfigConstants.DIRECT_CONFIG_SERVICE_NAME);
            if (NullChecker.isNullish(url)) {
                result = false;
            } else {
                result = pingDirectConfig(url);
            }
        } catch (Exception ex) {
            result = false;
            LOG.warn("Unable to ping DirectConfig service. Please make sure that Connect.ear with Direct profile is deployed first");
            LOG.warn(ex.getLocalizedMessage(), ex);
        }

        return result;
    }

    private boolean pingDirectConfig(String url) throws Exception {
        AdminGUIProxyObjectFactory objectFactory = new AdminGUIProxyObjectFactory();
        DirectConfigProxy directConfigProxy = objectFactory.getDirectConfigProxy();
        if (directConfigProxy != null) {
            return objectFactory.getDirectConfigProxy().pingDirectConfig(url);
        }
        return false;
    }

}
