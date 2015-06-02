/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.admingui.services.FhirResourceService;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.apache.log4j.Logger;

/**
 *
 * @author jassmit
 */
public class FhirDisplayController implements DisplayController {

    private static final Logger LOG = Logger.getLogger(FhirDisplayController.class);

    @Override
    public void checkDisplay() {
        String[] resourceNames = new String[]{FhirResourceService.BINARY_RESOURCE_NAME, FhirResourceService.DOCREF_RESOURCE_NAME,
            FhirResourceService.PATIENT_RESOURCE_NAME};

        for (String resourceName : resourceNames) {
            boolean hasResource;
            try {
                hasResource = checkForResource(resourceName);
            } catch (ConnectionManagerException e) {
                LOG.warn(e.getLocalizedMessage(), e);
                hasResource = false;
            }

            if (hasResource) {
                DisplayHolder.getInstance().setFhirEnabled(hasResource);
                return;
            }
        }
        DisplayHolder.getInstance().setFhirEnabled(false);
    }

    private boolean checkForResource(String resourceName) throws ConnectionManagerException {
        return NullChecker.isNotNullish(ConnectionManagerCache.getInstance().getAdapterEndpointURL(resourceName, NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0));
    }

}
