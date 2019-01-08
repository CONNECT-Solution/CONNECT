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
package gov.hhs.fha.nhinc.patientdiscovery.response;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class ResponseFactory {
    public static final String RESPONSE_MODE_VERIFY = "verify";
    public static final String RESPONSE_MODE_TRUST = "trust";
    public static final String RESPONSE_MODE_PASSTHRU = "passthrough";

    public static enum ResponseModeType {
        VERIFY, TRUST, PASSTHROUGH
    }

    private static final Logger LOG = LoggerFactory.getLogger(ResponseFactory.class);

    public ResponseMode getResponseMode() {
        ResponseMode result;

        ResponseModeType mode = getResponseModeType();

        switch (mode) {
        case VERIFY: {
            result = new VerifyMode();
            break;
        }
        case TRUST: {
            result = new TrustMode();
            break;
        }
        case PASSTHROUGH: {
            result = new PassThruMode();
            break;
        }
        default: {
            result = new VerifyMode();
        }
        }

        return result;
    }

    protected String getModeProperty() {
        String result = "";

        try {
            result = PropertyAccessor.getInstance().getProperty("gateway", "patientDiscoveryResponseMode");
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }

        return result;
    }

    public ResponseModeType getResponseModeType() {
        ResponseModeType result = ResponseModeType.VERIFY;

        try {
            String property = getModeProperty();
            if (property.equalsIgnoreCase(RESPONSE_MODE_TRUST)) {
                result = ResponseModeType.TRUST;
            } else if (property.equalsIgnoreCase(RESPONSE_MODE_PASSTHRU)) {
                result = ResponseModeType.PASSTHROUGH;
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }

        return result;

    }

}
