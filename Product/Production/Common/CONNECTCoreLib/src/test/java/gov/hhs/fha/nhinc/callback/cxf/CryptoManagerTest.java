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
package gov.hhs.fha.nhinc.callback.cxf;

import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.security.auth.callback.CallbackHandler;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.CryptoType;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import static org.mockito.Mockito.mock;

public class CryptoManagerTest {

	private CryptoManager cryptoManager = new CryptoManager();

	@Test(expected=WSSecurityException.class)
	public void testGetBytesFromCertificate() throws WSSecurityException{
		X509Certificate[] certificateList = new X509Certificate[1];
		X509Certificate cert = mock(X509Certificate.class);
		certificateList[0] = cert;
		cryptoManager.getBytesFromCertificates(certificateList);
	}

	@Test(expected=WSSecurityException.class)
	public void testGetCertificateFactory() throws WSSecurityException{
		cryptoManager.getCertificateFactory();
	}

	@Test(expected=WSSecurityException.class)
	public void testGetCertificateFromBytes() throws WSSecurityException{
		String testString = "testing";
		byte[] bytes = testString.getBytes();
		cryptoManager.getCertificatesFromBytes(bytes);
	}

	@Test
	public void testGetCryptoProvider(){
		assertNull(cryptoManager.getCryptoProvider());
	}

	@Test(expected=WSSecurityException.class)
	public void testGetDefaultX509Identifier() throws WSSecurityException{
		cryptoManager.getDefaultX509Identifier();
	}

	@Test(expected=WSSecurityException.class)
	public void testGetPrivateKey_Callback() throws WSSecurityException{
		X509Certificate cert = mock(X509Certificate.class);
		CallbackHandler callback = mock(CallbackHandler.class);
		cryptoManager.getPrivateKey(cert, callback);
	}

	@Test(expected=WSSecurityException.class)
	public void testGetPrivateKey_Strings() throws WSSecurityException{
		cryptoManager.getPrivateKey("", "");
	}

	@Test(expected=WSSecurityException.class)
	public void testGetSKIBytesFromCert() throws WSSecurityException{
		X509Certificate cert = mock(X509Certificate.class);
		cryptoManager.getSKIBytesFromCert(cert);
	}

	@Test(expected=WSSecurityException.class)
	public void testGetX509Certificates() throws WSSecurityException{
		CryptoType cryptoType = mock(CryptoType.class);
		cryptoManager.getX509Certificates(cryptoType);
	}

	@Test(expected=WSSecurityException.class)
	public void testGetX509Identifier() throws WSSecurityException{
		X509Certificate cert = mock(X509Certificate.class);
		cryptoManager.getX509Identifier(cert);
	}

	@Test(expected=WSSecurityException.class)
	public void testLoadCertificate() throws WSSecurityException{
		InputStream input = mock(InputStream.class);
		cryptoManager.loadCertificate(input);
	}

	@Test(expected=WSSecurityException.class)
	public void testVerifyTrust_X509Certificate() throws WSSecurityException{
		X509Certificate[] certificateList = new X509Certificate[1];
		X509Certificate cert = mock(X509Certificate.class);
		certificateList[0] = cert;
		cryptoManager.verifyTrust(certificateList);
	}

	@Test(expected=WSSecurityException.class)
	public void testVerifyTrust_PublicKey() throws WSSecurityException{
		PublicKey publicKey = mock(PublicKey.class);
		cryptoManager.verifyTrust(publicKey);
	}

	@Test(expected=WSSecurityException.class)
	public void testVerifyTrust_X509CertificateAndBoolean() throws WSSecurityException{
		X509Certificate[] certificateList = new X509Certificate[1];
		X509Certificate cert = mock(X509Certificate.class);
		certificateList[0] = cert;
		cryptoManager.verifyTrust(certificateList, true);
	}

}
