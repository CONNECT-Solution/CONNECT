/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.callback.opensaml;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.wss4j.common.crypto.DERDecoder;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Poornima Venkatakrishnan
 *
 */
public class X509CertificateHelper {
    // NOSONAR
    private static final String AUTHORITY_KEY_ID = "2.5.29.35";
    // NOSONAR
    private static final String SUBJECT_KEY_ID = "2.5.29.14";
    private static final int AUTHORITY_KEY_POSITION = 6;
    private static final String EMPTY_FIELD = "-";
    private X509Certificate x509Cert;
    private int keySize = -1;
    private String serialNumber;
    private String subjectKeyID;
    private String issuerName;
    private String authorityKeyID;
    private String validStartDate;
    private String validEndDate;
    private String subjectName;
    private String subjectPublicKeyAlgorithm;
    private String subjectPublicKey;
    private String issuerUniqueIdentifier;
    private String certSignatureAlgorithm;
    private String certSignature;
    private long expiresInDays;
    private DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
    private static final Logger LOG = LoggerFactory.getLogger(X509CertificateHelper.class);

    public CertificateDTO buildCertificate(X509Certificate x509Cert) {
        this.x509Cert = x509Cert;

        CertificateDTO obj = new CertificateDTO();
        obj.setSerialNumber(getCertSerialNumber());
        obj.setIssuerName(getIssuerName());
        obj.setVersion(x509Cert.getVersion());
        obj.setSubjectKeyID(getSubjectKeyID());
        obj.setAlgorithm(x509Cert.getPublicKey().getAlgorithm());
        obj.setKeySize(getKeySize());
        obj.setAuthorityKeyID(getAuthorityKeyID());
        obj.setValidStartDate(getValidStartDate());
        obj.setExpirationDate(getValidEndDate());
        obj.setSubjectName(getSubjectName());
        obj.setSubjectPublicKeyAlgorithm(getSubjectPublicKeyAlgorithm());
        obj.setSubjectPublicKey(getSubjectPublicKey());
        obj.setIssuerUniqueIdentifier(getIssuerUniqueIdentifier());
        obj.setCertSignatureAlgorithm(getCertSignatureAlgorithm());
        obj.setCertSignature(getCertSignature());
        obj.setExpiresInDays(getExpiresInDays());
        obj.setSignatureAlgorithm(x509Cert.getSigAlgName());
        // obj.setExpiryColorCoding(getExpiryColor());
        obj.setX509Cert(x509Cert);
        return obj;
    }

    private String getCertSerialNumber() {
        serialNumber = new String(Hex.encodeHex(x509Cert.getSerialNumber().toByteArray()));
        return serialNumber;
    }

    private int getKeySize() {
        PublicKey publicKey = x509Cert.getPublicKey();
        if (publicKey instanceof RSAPublicKey) {
            keySize = ((RSAPublicKey) publicKey).getModulus().bitLength();
        }
        return keySize;
    }

    private String getIssuerName() {
        issuerName = StringUtils.isEmpty(x509Cert.getIssuerDN().getName()) ? EMPTY_FIELD
            : x509Cert.getIssuerDN().getName();
        return issuerName;
    }

    private String getSubjectKeyID() {
        byte[] subjectKeyIdByte = x509Cert.getExtensionValue(SUBJECT_KEY_ID);

        try {
            if (subjectKeyIdByte != null) {
                // this logic extracts from CryptoBase class inside wss4j
                DERDecoder extVal = new DERDecoder(subjectKeyIdByte);
                extVal.expect(DERDecoder.TYPE_OCTET_STRING); // ExtensionValue OCTET STRING
                extVal.getLength(); // leave this method alone. getlength modify array position.
                extVal.expect(DERDecoder.TYPE_OCTET_STRING); // KeyIdentifier OCTET STRING
                int keyIDLen = extVal.getLength();
                subjectKeyID = Hex.encodeHexString(extVal.getBytes(keyIDLen));
            }
        } catch (WSSecurityException e) {
            LOG.error("Unable to convert SKI into human readable {}", e.getLocalizedMessage(), e);
        }

        return StringUtils.isEmpty(subjectKeyID) ? EMPTY_FIELD : subjectKeyID;
    }

    private String getAuthorityKeyID() {
        byte[] authorityKey = x509Cert.getExtensionValue(AUTHORITY_KEY_ID);
        try {
            if (authorityKey != null) {
                DERDecoder extValA = new DERDecoder(authorityKey);
                extValA.skip(AUTHORITY_KEY_POSITION);
                int length = authorityKey.length - AUTHORITY_KEY_POSITION;
                authorityKeyID = Hex.encodeHexString(extValA.getBytes(length));
            }
        } catch (WSSecurityException e) {
            LOG.error("Unable to convert AIK into human readable {} ", e.getLocalizedMessage(), e);
        }
        return StringUtils.isEmpty(authorityKeyID) ? EMPTY_FIELD : authorityKeyID;
    }

    private String getValidStartDate() {
        validStartDate = formatter.format(x509Cert.getNotBefore());
        return validStartDate;
    }

    private String getValidEndDate() {
        validEndDate = formatter.format(x509Cert.getNotAfter());
        return validEndDate;
    }

    private String getSubjectName() {
        subjectName = StringUtils.isEmpty(x509Cert.getSubjectDN().getName()) ? EMPTY_FIELD
            : x509Cert.getSubjectDN().getName();
        return subjectName;
    }

    private String getSubjectPublicKeyAlgorithm() {
        subjectPublicKeyAlgorithm = x509Cert.getPublicKey().getAlgorithm();
        return subjectPublicKeyAlgorithm;
    }

    private String getSubjectPublicKey() {
        subjectPublicKey = x509Cert.getPublicKey().toString();
        return subjectPublicKey;
    }

    private String getIssuerUniqueIdentifier() {
        issuerUniqueIdentifier = x509Cert.getIssuerUniqueID() != null ? Arrays.toString(x509Cert.getIssuerUniqueID())
            : EMPTY_FIELD;
        return issuerUniqueIdentifier;
    }

    private String getCertSignatureAlgorithm() {
        certSignatureAlgorithm = StringUtils.isEmpty(x509Cert.getSigAlgName()) ? EMPTY_FIELD
            : x509Cert.getSigAlgName();
        return certSignatureAlgorithm;
    }

    private String getCertSignature() {
        certSignature = new BigInteger(x509Cert.getSignature()).toString(16);
        return certSignature;
    }

    private long getExpiresInDays() {
        Date certExpiryDate = x509Cert.getNotAfter();
        Date today = new Date();
        long dateDiff = certExpiryDate.getTime() - today.getTime();
        expiresInDays = dateDiff / (24 * 60 * 60 * 1000);
        return expiresInDays;
    }
}
