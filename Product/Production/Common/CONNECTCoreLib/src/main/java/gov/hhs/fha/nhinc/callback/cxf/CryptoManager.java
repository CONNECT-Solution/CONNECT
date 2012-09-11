/**
 *
 */
package gov.hhs.fha.nhinc.callback.cxf;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.callback.CallbackHandler;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoType;

/**
 * @author mweaver
 *
 */
public class CryptoManager implements Crypto {

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.ws.security.components.crypto.Crypto#getBytesFromCertificates(java.security.cert.X509Certificate[])
     */
    @Override
    public byte[] getBytesFromCertificates(X509Certificate[] arg0) throws WSSecurityException {
        throw new WSSecurityException("getBytesFromCertificates");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#getCertificateFactory()
     */
    @Override
    public CertificateFactory getCertificateFactory() throws WSSecurityException {
        throw new WSSecurityException("getCertificateFactory");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#getCertificatesFromBytes(byte[])
     */
    @Override
    public X509Certificate[] getCertificatesFromBytes(byte[] arg0) throws WSSecurityException {
        throw new WSSecurityException("getCertificatesFromBytes");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#getCryptoProvider()
     */
    @Override
    public String getCryptoProvider() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#getDefaultX509Identifier()
     */
    @Override
    public String getDefaultX509Identifier() throws WSSecurityException {
        throw new WSSecurityException("getDefaultX509Identifier");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#getPrivateKey(java.security.cert.X509Certificate,
     * javax.security.auth.callback.CallbackHandler)
     */
    @Override
    public PrivateKey getPrivateKey(X509Certificate arg0, CallbackHandler arg1) throws WSSecurityException {
        throw new WSSecurityException("getPrivateKey");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#getPrivateKey(java.lang.String, java.lang.String)
     */
    @Override
    public PrivateKey getPrivateKey(String arg0, String arg1) throws WSSecurityException {
        throw new WSSecurityException("getPrivateKey");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#getSKIBytesFromCert(java.security.cert.X509Certificate)
     */
    @Override
    public byte[] getSKIBytesFromCert(X509Certificate arg0) throws WSSecurityException {
        throw new WSSecurityException("getSKIBytesFromCert");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.ws.security.components.crypto.Crypto#getX509Certificates(org.apache.ws.security.components.crypto.
     * CryptoType)
     */
    @Override
    public X509Certificate[] getX509Certificates(CryptoType arg0) throws WSSecurityException {
        throw new WSSecurityException("getX509Certificates");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#getX509Identifier(java.security.cert.X509Certificate)
     */
    @Override
    public String getX509Identifier(X509Certificate arg0) throws WSSecurityException {
        throw new WSSecurityException("getX509Identifier");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#loadCertificate(java.io.InputStream)
     */
    @Override
    public X509Certificate loadCertificate(InputStream arg0) throws WSSecurityException {
        throw new WSSecurityException("loadCertificate");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#setCertificateFactory(java.lang.String,
     * java.security.cert.CertificateFactory)
     */
    @Override
    public void setCertificateFactory(String arg0, CertificateFactory arg1) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#setCryptoProvider(java.lang.String)
     */
    @Override
    public void setCryptoProvider(String arg0) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#setDefaultX509Identifier(java.lang.String)
     */
    @Override
    public void setDefaultX509Identifier(String arg0) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#verifyTrust(java.security.cert.X509Certificate[])
     */
    @Override
    public boolean verifyTrust(X509Certificate[] arg0) throws WSSecurityException {
        // TODO Auto-generated method stub
        throw new WSSecurityException("verifyTrust");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#verifyTrust(java.security.PublicKey)
     */
    @Override
    public boolean verifyTrust(PublicKey arg0) throws WSSecurityException {
        // TODO Auto-generated method stub
        throw new WSSecurityException("verifyTrust");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#verifyTrust(java.security.cert.X509Certificate[], boolean)
     */
    @Override
    public boolean verifyTrust(X509Certificate[] arg0, boolean arg1) throws WSSecurityException {
        // TODO Auto-generated method stub
        throw new WSSecurityException("verifyTrust");
    }

}
