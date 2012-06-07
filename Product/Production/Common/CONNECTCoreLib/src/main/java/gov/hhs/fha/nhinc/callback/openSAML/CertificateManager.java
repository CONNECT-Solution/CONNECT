/**
 * 
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