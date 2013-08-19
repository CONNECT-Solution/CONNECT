package gov.hhs.fha.nhinc.callback.cxf;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import javax.security.auth.callback.CallbackHandler;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.CryptoType;
import org.junit.Test;

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
