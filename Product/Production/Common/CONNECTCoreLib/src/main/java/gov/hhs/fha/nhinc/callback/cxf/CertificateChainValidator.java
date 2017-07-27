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

import gov.hhs.fha.nhinc.callback.openSAML.CertificateManager;
import gov.hhs.fha.nhinc.callback.openSAML.CertificateManagerImpl;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.DERDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validate chain of trust
 *
 * @author mpnguyen
 *
 */
public class CertificateChainValidator {
    private static final Logger LOG = LoggerFactory.getLogger(CertificateChainValidator.class);
    private final CertificateManager certificateManager;

    // http://docs.oracle.com/javase/7/docs/api/java/security/cert/X509Extension.html#getExtensionValue(java.lang.String)
    private static final String AUTHORITY_KEY_ID = "2.5.29.35";
    private static final String SUBJECT_KEY_ID = "2.5.29.14";

    /**
     *
     */
    private CertificateChainValidator() {
        certificateManager = CertificateManagerImpl.getInstance();
    }

    /**
     * @param certificateManager
     */
    CertificateChainValidator(CertificateManager certificateManager) {
        this.certificateManager = certificateManager;
    }
    public static CertificateChainValidator getInstance() {
        return new CertificateChainValidator();
    }

    /**
     * ALL root CA are self-sign cert. Verify if cert is self-sign or chain of trust
     *
     * @param certToCheck
     * @return true if self-sign cert or false if chain of trust
     */
    public boolean isPartChainOfTrust(X509Certificate certToCheck) {
        try {
            // Try to verify certificate signature with its own public key
            PublicKey key = certToCheck.getPublicKey();
            certToCheck.verify(key);
            return false;
        } catch (SignatureException | InvalidKeyException | CertificateException | NoSuchAlgorithmException
            | NoSuchProviderException sigEx) {
            // LOG.warn(sigEx.getLocalizedMessage(),sigEx);
            return true;
        }

    }

    /**
     * Validate chain of trust.
     *
     * @param certToCheck
     * @return
     */
    public boolean validateCert(X509Certificate certToCheck) {
        List<X509Certificate> allCerts = getAllCertInKeyStore(certificateManager.getTrustStore());
        // check if cert is chain of trust
        if (isPartChainOfTrust(certToCheck)) {
            // extract intermediate/root out and compare with trust store
            // extract intermediate
            try {
                // ---------
                LOG.debug("Cert serial number to verify against our trustStore {}", certToCheck.getSerialNumber()
                    .toString(16));
                String leafIssuerString = certToCheck.getIssuerX500Principal().getName();
                LOG.debug("Issuer for parent {} ", leafIssuerString);

                String authorityKeyIdentifier = getAuthorityKeyIdentify(certToCheck.getExtensionValue(AUTHORITY_KEY_ID));
                for (X509Certificate cert : allCerts) {
                    String name = cert.getSubjectX500Principal().getName();
                    if (leafIssuerString.equalsIgnoreCase(name)) {
                        String subjectKeyId = getSubjectKeyID(cert.getExtensionValue(SUBJECT_KEY_ID));
                        if (StringUtils.isNotBlank(subjectKeyId) && StringUtils.isNotBlank(authorityKeyIdentifier)
                            && subjectKeyId.equalsIgnoreCase(authorityKeyIdentifier)) {
                            LOG.debug("Found the match with cert serial number{} ", cert.getSerialNumber().toString(16));
                            // check again if intermediate don't have root
                            if (isPartChainOfTrust(cert)) {
                                return validateCert(cert);
                            } else {
                                // we should return true since we don't have any parent cert.
                                return true;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error(e.getLocalizedMessage(), e);
            }
        } else {
            // handle self-sign cert
            LOG.debug("This cert is either self-sign cert or root CA");
            // need to find if we have this cert in our trust store
            for (X509Certificate cert : allCerts) {
                if (cert.equals(certToCheck)) {
                    LOG.debug("Found the cert in trust store with serial number {}", cert.getSerialNumber()
                        .toString(16));
                    return true;
                }
            }
            return false;
        }
        return false;// by default
    }

    private List<X509Certificate> getAllCertInKeyStore(KeyStore store) {
        List<X509Certificate> cerList = new ArrayList<>();
        try {
            for (Enumeration<String> e = store.aliases(); e.hasMoreElements();) {
                String alias = e.nextElement();
                // LOG.debug("Alias {}",alias);
                X509Certificate cert = (X509Certificate) store.getCertificate(alias);
                Certificate[] certs = store.getCertificateChain(alias);
                cerList.add(cert);
            }
        } catch (KeyStoreException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
        return cerList;
    }

    /**
     * Return Authority Key Identify (AIK)
     *
     * @param authorityKeyId
     * @return
     */
    private String getSubjectKeyID(byte[] subjectKeyID) {
        String SKI = null;
        try {
            if (subjectKeyID != null) {
                // this logic extracts from CryptoBase class inside wss4j
                DERDecoder extVal = new DERDecoder(subjectKeyID);
                extVal.expect(DERDecoder.TYPE_OCTET_STRING); // ExtensionValue OCTET STRING
                int Length = extVal.getLength(); // leave this method alone. getlength modify array position.
                extVal.expect(DERDecoder.TYPE_OCTET_STRING); // KeyIdentifier OCTET STRING
                int keyIDLen = extVal.getLength();
                byte[] hexValue = extVal.getBytes(keyIDLen);
                SKI = Hex.encodeHexString(hexValue);
            }
        } catch (WSSecurityException e) {
            LOG.error("Unable to convert SKI into human readable {}", e.getLocalizedMessage(), e);
        }
        return SKI;
    }

    private String getAuthorityKeyIdentify(byte[] authorityKey) {
        String AIK = null;
        try {
            if (authorityKey != null) {
                DERDecoder extValA = new DERDecoder(authorityKey);
                extValA.skip(6);
                AIK = Hex.encodeHexString(extValA.getBytes(20));
            }
        } catch (WSSecurityException e) {
            LOG.error("Unable to convert AIK into human readable {}", e.getLocalizedMessage(), e);
        }
        return AIK;

    }
}
