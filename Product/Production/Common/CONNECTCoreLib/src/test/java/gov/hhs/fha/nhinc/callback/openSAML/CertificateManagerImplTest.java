/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import gov.hhs.fha.nhinc.util.AbstractSuppressRootLoggerTest;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

/**
 * @author mweaver/jsmith
 * 
 */
public class CertificateManagerImplTest extends AbstractSuppressRootLoggerTest{

	private CertificateManagerImpl certManager;
	private final String KEY_STORE_PATH = "src/test/resources/gov/hhs/fha/nhinc/callback/gateway.jks";
	private final String TRUST_STORE_PATH = "src/test/resources/gov/hhs/fha/nhinc/callback/cacerts.jks";

	@Before
	public void setUp() {
		final HashMap<String, String> keyStoreMap = new HashMap<String, String>();
		keyStoreMap.put(CertificateManagerImpl.KEY_STORE_KEY, KEY_STORE_PATH);
		keyStoreMap.put(CertificateManagerImpl.KEY_STORE_PASSWORD_KEY,
				"changeit");
		keyStoreMap.put(CertificateManagerImpl.KEY_STORE_TYPE_KEY, "JKS");

		final HashMap<String, String> trustStoreMap = new HashMap<String, String>();
		trustStoreMap.put(CertificateManagerImpl.TRUST_STORE_KEY,
				TRUST_STORE_PATH);
		trustStoreMap.put(CertificateManagerImpl.TRUST_STORE_PASSWORD_KEY,
				"changeit");
		trustStoreMap.put(CertificateManagerImpl.TRUST_STORE_TYPE_KEY, "JKS");

		certManager = (CertificateManagerImpl) CertificateManagerImpl
				.getInstance(keyStoreMap, trustStoreMap);
	}

	@Test
	public void testGetInstance() {
		assertTrue(certManager instanceof CertificateManagerImpl);
		assertNotNull(certManager.getKeyStore());
		assertNotNull(certManager.getTrustStore());
	}

	@Test
	public void testGetInstance_StoresNotSet() {
		HashMap<String, String> mockKeyStoreMap = mock(HashMap.class);
		HashMap<String, String> mockTrustStoreMap = mock(HashMap.class);
		CertificateManagerImpl certManager = (CertificateManagerImpl) CertificateManagerImpl
				.getInstance(mockKeyStoreMap, mockTrustStoreMap);
		assertTrue(certManager instanceof CertificateManagerImpl);
		assertNull(certManager.getKeyStore());
		assertNull(certManager.getTrustStore());
	}

	@Test
	public void testGetDefaultPrivateKey() throws Exception {
		PrivateKey privateKey = certManager.getDefaultPrivateKey();
		assertNotNull(privateKey);
		assertEquals(privateKey.getAlgorithm(), "RSA");
		assertEquals(privateKey.getFormat(), "PKCS#8");
	}

	@Test
	public void testGetDefaultPublicKey() {
		RSAPublicKey publicKey = certManager.getDefaultPublicKey();
		assertNotNull(publicKey);
		assertEquals(publicKey.getAlgorithm(), "RSA");
		assertEquals(publicKey.getFormat(), "X.509");
	}

	@Test
	public void testGetDefaultCertificate() throws Exception {
		X509Certificate certificate = certManager.getDefaultCertificate();
		assertNotNull(certificate);
		assertEquals(certificate.getSigAlgName(), "SHA256withRSA");
		assertEquals(certificate.getSigAlgOID(), "1.2.840.113549.1.1.11");
	}

}
