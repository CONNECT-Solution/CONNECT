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
package gov.hhs.fha.nhinc.callback.cxf;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mweaver
 *
 */
public class CXFPasswordCallbackHandler implements CallbackHandler {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(CXFPasswordCallbackHandler.class);
    private static final String SYSTEM_PROPERTY_KEYSTORE_PASSWORD = "javax.net.ssl.keyStorePassword";

    /** Keystore Password property. **/
    private String keystorePassword;

    /**
     * One default constructor, free of charge.
     */
    public CXFPasswordCallbackHandler() {
        keystorePassword = System.getProperty(SYSTEM_PROPERTY_KEYSTORE_PASSWORD);
    }

    public CXFPasswordCallbackHandler(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    /**
     * It attempts to get the password from the private alias/passwords map.
     *
     * @param callbacks callback array
     *
     * @throws IOException exception encountered in executing callbacks
     * @throws UnsupportedCallbackException exception encountered in executing callbacks
     */
    @Override
    public final void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        LOG.trace("Executing CXFPasswordCallbackHandler.handle() ... ");

        if (keystorePassword == null || keystorePassword.isEmpty()) {
            throw new IOException("keystore password was not provided. Please provide the system property: "
                    + SYSTEM_PROPERTY_KEYSTORE_PASSWORD);
        }

        for (Callback callback : callbacks) {
            if (callback instanceof WSPasswordCallback) {
                WSPasswordCallback pc = (WSPasswordCallback) callback;
                pc.setPassword(keystorePassword);
            }
        }
        LOG.trace("end CXFPasswordCallbackHandler.handle() ... ");
    }

}
