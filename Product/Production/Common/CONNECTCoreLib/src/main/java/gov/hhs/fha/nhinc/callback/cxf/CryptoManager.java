/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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

import java.util.Collection;
import java.util.regex.Pattern;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.security.auth.callback.CallbackHandler;
import org.apache.wss4j.common.crypto.Crypto;
import org.apache.wss4j.common.crypto.CryptoType;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.ext.WSSecurityException.ErrorCode;

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
        throw new WSSecurityException(ErrorCode.FAILURE,"getBytesFromCertificates");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#getCertificateFactory()
     */
    @Override
    public CertificateFactory getCertificateFactory() throws WSSecurityException {
        throw new WSSecurityException(ErrorCode.FAILURE,"getCertificateFactory");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#getCertificatesFromBytes(byte[])
     */
    @Override
    public X509Certificate[] getCertificatesFromBytes(byte[] arg0) throws WSSecurityException {
        throw new WSSecurityException(ErrorCode.FAILURE,"getCertificatesFromBytes");
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
        throw new WSSecurityException(ErrorCode.FAILURE,"getDefaultX509Identifier");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#getPrivateKey(java.security.cert.X509Certificate,
     * javax.security.auth.callback.CallbackHandler)
     */
    @Override
    public PrivateKey getPrivateKey(X509Certificate arg0, CallbackHandler arg1) throws WSSecurityException {
        throw new WSSecurityException(ErrorCode.FAILURE,"getPrivateKey");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#getPrivateKey(java.lang.String, java.lang.String)
     */
    @Override
    public PrivateKey getPrivateKey(String arg0, String arg1) throws WSSecurityException {
        throw new WSSecurityException(ErrorCode.FAILURE,"getPrivateKey");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#getSKIBytesFromCert(java.security.cert.X509Certificate)
     */
    @Override
    public byte[] getSKIBytesFromCert(X509Certificate arg0) throws WSSecurityException {
        throw new WSSecurityException(ErrorCode.FAILURE,"getSKIBytesFromCert");
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
        throw new WSSecurityException(ErrorCode.FAILURE,"getX509Certificates");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#getX509Identifier(java.security.cert.X509Certificate)
     */
    @Override
    public String getX509Identifier(X509Certificate arg0) throws WSSecurityException {
        throw new WSSecurityException(ErrorCode.FAILURE,"getX509Identifier");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ws.security.components.crypto.Crypto#loadCertificate(java.io.InputStream)
     */
    @Override
    public X509Certificate loadCertificate(InputStream arg0) throws WSSecurityException {
        throw new WSSecurityException(ErrorCode.FAILURE,"loadCertificate");
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
     * @see org.apache.ws.security.components.crypto.Crypto#verifyTrust(java.security.PublicKey)
     */
    @Override
    public void verifyTrust(PublicKey arg0) throws WSSecurityException {
        // TODO Auto-generated method stub
        
        throw new WSSecurityException(ErrorCode.FAILURE,"verifyTrust");
    }

    /* (non-Javadoc)
     * @see org.apache.wss4j.common.crypto.Crypto#getTrustProvider()
     */
    @Override
    public String getTrustProvider() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.wss4j.common.crypto.Crypto#setTrustProvider(java.lang.String)
     */
    @Override
    public void setTrustProvider(String provider) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.apache.wss4j.common.crypto.Crypto#setCertificateFactory(java.security.cert.CertificateFactory)
     */
    @Override
    public void setCertificateFactory(CertificateFactory certFactory) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.apache.wss4j.common.crypto.Crypto#verifyTrust(java.security.cert.X509Certificate[], boolean, java.util.Collection)
     */
    @Override
    public void verifyTrust(X509Certificate[] certs, boolean enableRevocation,
            Collection<Pattern> subjectCertConstraints) throws WSSecurityException {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.apache.wss4j.common.crypto.Crypto#verifyDirectTrust(java.security.cert.X509Certificate[])
     */
    @Override
    public void verifyDirectTrust(X509Certificate[] certs) throws WSSecurityException {
        // TODO Auto-generated method stub
        
    }

    

   

}
