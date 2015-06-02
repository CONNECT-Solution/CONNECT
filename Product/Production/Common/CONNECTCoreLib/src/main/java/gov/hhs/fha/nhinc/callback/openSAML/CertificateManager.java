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
package gov.hhs.fha.nhinc.callback.openSAML;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

/**
 * @author bhumphrey
 *
 */
public interface CertificateManager {

	/**
	 * System property which controls the alias used to retrieve the private key to sign the SAML assertion and
	 * endorsing supporting token.
	 */
	public static final String CLIENT_KEY_ALIAS = "CLIENT_KEY_ALIAS";

	/**
	 * Default alias used to retrieve the private key to sign the SAML assertion and endorsing supporting token.
	 */
	public static final String DEFAULT_CLIENT_KEY_ALIAS = "gateway";

	/**
	 * Finds the X509 certificate in the keystore with the client alias as defined in the domain.xml system property
	 * CLIENT_KEY_ALIAS and establishes the private key on the SignatureKeyCallback request using this certificate.
	 *
	 * @param request The SignatureKeyCallback request object
	 * @throws Exception
	 */
	public abstract X509Certificate getDefaultCertificate() throws Exception;

	public abstract PrivateKey getDefaultPrivateKey()
			throws Exception;

	/**
	 * @return
	 */
	public abstract RSAPublicKey getDefaultPublicKey();
    /**
     * @return the keyStore
     */
    public abstract KeyStore getKeyStore();
    /**
     * @return the trustStore
     */
    public abstract KeyStore getTrustStore();

}