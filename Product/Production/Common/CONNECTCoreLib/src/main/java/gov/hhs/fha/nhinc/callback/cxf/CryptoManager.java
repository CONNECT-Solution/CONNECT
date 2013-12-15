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
package gov.hhs.fha.nhinc.callback.cxf;

import gov.hhs.fha.nhinc.util.PasswordUtil;

import java.io.IOException;
import java.util.Properties;

import org.apache.ws.security.components.crypto.Merlin;
import org.apache.ws.security.components.crypto.CredentialException;
import org.apache.ws.security.util.Loader;

/**
 * @author cmay
 *
 * Extends {@link org.apache.ws.security.components.crypto.Merlin} to allow for the keystore password to be encoded /
 * encrypted, rather than stored in plaintext format.
 */
public class CryptoManager extends Merlin {
    /**
     * Default constructor
     */
    public CryptoManager() {
    }
    
    /**
     * Calls CryptoManager(Properties, ClassLoader) with a CryptoManager ClassLoader
     * 
     * @see #CryptoManager(Properties, ClassLoader)
     */
    public CryptoManager(Properties properties) 
        throws CredentialException, IOException {

        this(properties, Loader.getClassLoader(CryptoManager.class));
    }

    /**
     * Creates the CryptoManager, populating the class variables using the passed-in Properties.
     * 
     * @see #loadProperties(Properties, ClassLoader)
     */
    public CryptoManager(Properties properties, ClassLoader loader) 
        throws CredentialException, IOException {

        loadProperties(properties, loader);
    }
    
    /**
     * Calls loadProperties(Properties, ClassLoader) with a CryptoManager ClassLoader
     *
     * @see #loadProperties(Properties, ClassLoader)
     */
    @Override
    public void loadProperties(Properties properties) 
        throws CredentialException, IOException {
        
        loadProperties(properties, Loader.getClassLoader(CryptoManager.class));
    }
    
    /**
     * Decodes the keystore password before loading the Merlin properties.
     *
     * @see org.apache.ws.security.components.crypto.Merlin#loadProperties(Properties, ClassLoader)
     */
    @Override
    public void loadProperties(Properties properties, ClassLoader loader) 
            throws CredentialException, IOException {
        
        properties.setProperty(KEYSTORE_PASSWORD, PasswordUtil.decode(properties.getProperty(KEYSTORE_PASSWORD)));
        super.loadProperties(properties, loader);
    }
}