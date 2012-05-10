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
package gov.hhs.fha.nhinc.callback.openSAML;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.xml.wss.impl.callback.SAMLCallback;

/**
 * This class implements the CallbackHandler which is invoked upon sending a message requiring the SAML Assertion Token.
 * It accesses the information stored in NMProperties in order to build up the required token elements.
 */
public class OpenSAMLCallbackHandler implements CallbackHandler {

    private static Log log = LogFactory.getLog(OpenSAMLCallbackHandler.class);
  
   
    
    public static final String HOK_CONFIRM = "urn:oasis:names:tc:SAML:2.0:cm:holder-of-key";
    public static final String SV_CONFIRM = "urn:oasis:names:tc:SAML:2.0:cm:authorization-over-ssl";
    private static final int DEFAULT_NAME = 0;
    private static final int PRIMARY_NAME = 1;
 	private SAMLAssertionBuilderFactory assertionBuilderFactory;
  
 
    static {
        // WORKAROUND NEEDED IN METRO1.4. TO BE REMOVED LATER.
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {

            public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
                return true;
            }
        });
    }

    /**
     * Constructs the callback handler and initializes the keystore and truststore references to the security
     * certificates
     */
    public OpenSAMLCallbackHandler() {
    	assertionBuilderFactory = new SAMLAssertionBuilderFactoryImpl();
    }
    
    

	OpenSAMLCallbackHandler(SAMLAssertionBuilderFactory assertionBuilderFactory) {
    	this.assertionBuilderFactory = assertionBuilderFactory;
    }

    /**
     * This is the invoked implementation to handle the SAML Token creation upon notification of an outgoing message
     * needing SAML. Based on the type of confirmation method detected on the Callbace it creates either a
     * "Sender Vouches: or a "Holder-ok_Key" variant of the SAML Assertion.
     * 
     * @param callbacks The SAML Callback
     * @throws java.io.IOException
     * @throws javax.security.auth.callback.UnsupportedCallbackException
     */
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        log.debug(" **********************************  Handle SAML Callback Begin  **************************");
        try {
            for (Callback callback : callbacks) {
                if (callback instanceof SAMLCallback) {
                    handleSAMLCallback((SAMLCallback)callback);
                } else {
                    log.error("Unknown Callback encountered: " + callback);
                    throw new UnsupportedCallbackException(null, "Unsupported Callback Type Encountered");
                }
            }
        } catch (Exception ex) {
            // catching all exceptions and making them IOExceptions. IO picked by flip of coin, neither IOException or
            // UnsupportedCallbackException are appropriate.
            throw new IOException(ex);
        }
        log.debug("**********************************  Handle SAML Callback End  **************************");
    }

	protected void handleSAMLCallback(SAMLCallback samlCallback)
			throws Exception {
		CallbackProperties properties = new CallbackMapProperties(samlCallback.getRuntimeProperties());
		final String confirmationMethod  = samlCallback.getConfirmationMethod();
		
		SAMLAssertionBuilder builder = assertionBuilderFactory.getBuilder(confirmationMethod);
		
		if( builder == null ) {
		    log.error("Unknown SAML Assertion Type: " + confirmationMethod );
		    throw new UnsupportedCallbackException(null, "SAML Assertion Type is not matched:"
		            + confirmationMethod);
		}
		
		samlCallback.setAssertionElement(builder.build(properties));
	}

  

}
