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
package gov.hhs.fha.nhinc.patientdiscovery.response;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author dunnek
 */
public class ResponseFactory {
    public static final String RESPONSE_MODE_VERIFY = "verify";
    public static final String RESPONSE_MODE_TRUST = "trust";
    public static final String RESPONSE_MODE_PASSTHRU = "passthrough";
    public static final int VERIFY_MODE = 0;
    public static final int TRUST_MODE = 1;
    public static final int PASSTHRU_MODE = 2;
    private Log log = null;

    public ResponseFactory() {
        log = createLogger();
    }

    public ResponseMode getResponseMode() {
        ResponseMode result = null;

        int mode = getResponseModeType();

        switch (mode) {
        case (VERIFY_MODE): {
            result = new VerifyMode();
            break;
        }
        case TRUST_MODE: {
            result = new TrustMode();
            break;
        }
        case PASSTHRU_MODE: {
            result = new PassThruMode();
            break;
        }
        default: {
            result = new VerifyMode();
        }
        }

        return result;
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected String getModeProperty() {
        String result = "";

        try {
            result = PropertyAccessor.getInstance().getProperty("gateway", "patientDiscoveryResponseMode");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return result;
    }

    public int getResponseModeType() {
        int result = 0;

        try {
            String property = getModeProperty();
            if (property == null) {
                result = VERIFY_MODE;
            } else if (property.equalsIgnoreCase(RESPONSE_MODE_VERIFY)) {
                result = VERIFY_MODE;
            } else if (property.equalsIgnoreCase(RESPONSE_MODE_TRUST)) {
                result = TRUST_MODE;
            } else if (property.equalsIgnoreCase(RESPONSE_MODE_PASSTHRU)) {
                result = PASSTHRU_MODE;
            } else {
                // unknown mode, use default response mode
                result = VERIFY_MODE;
            }

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result = VERIFY_MODE;
        }

        return result;

    }

}
