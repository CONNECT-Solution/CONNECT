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
package gov.hhs.fha.nhinc.admingui.event.model;

import gov.hhs.fha.nhinc.admingui.util.GUIConstants.CERT_EXPIRY_COLOR_CODING;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
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
 *
 * @author tjafri
 */
public class Certificate {

    private long id;
    private String alias = "";
    private String algorithm;
    private String signatureAlgorithm;
    private int keySize;
    private String expirationDate;
    private String serialNumber;
    private int version;
    private String subjectKeyID;
    private String authorityKeyID;
    private X509Certificate x509Cert;
    private String validStartDate;
    private String validEndDate;
    private String expiryColorCoding;
    private long expiresInDays;
    private String issuerName;
    private String subjectName;
    private String subjectPublicKeyAlgorithm;
    private String subjectPublicKey;
    private String issuerUniqueIdentifier;
    private String certSignatureAlgorithm;
    private String certSignature;
    private static final String AUTHORITY_KEY_ID = "2.5.29.35";
    private static final String SUBJECT_KEY_ID = "2.5.29.14";
    private static final String EMPTY_FIELD = "-";
    private static final int AUTHORITY_KEY_POSITION = 6;
    private static final Logger LOG = LoggerFactory.getLogger(Certificate.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlgorithm() {
        return StringUtils.isEmpty(algorithm) ? EMPTY_FIELD : algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getSignatureAlgorithm() {
        return StringUtils.isEmpty(signatureAlgorithm) ? EMPTY_FIELD : signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getSubjectKeyID() {
        if (subjectKeyID == null) {
            byte[] subjectKeyId = x509Cert.getExtensionValue(SUBJECT_KEY_ID);

            try {
                if (subjectKeyId != null) {
                    // this logic extracts from CryptoBase class inside wss4j
                    DERDecoder extVal = new DERDecoder(subjectKeyId);
                    extVal.expect(DERDecoder.TYPE_OCTET_STRING); // ExtensionValue OCTET STRING
                    extVal.getLength(); // leave this method alone. getlength modify array position.
                    extVal.expect(DERDecoder.TYPE_OCTET_STRING); // KeyIdentifier OCTET STRING
                    int keyIDLen = extVal.getLength();
                    subjectKeyID = Hex.encodeHexString(extVal.getBytes(keyIDLen));
                }
            } catch (WSSecurityException e) {
                LOG.error("Unable to convert SKI into human readable {}", e.getLocalizedMessage(), e);
            }
        }
        return StringUtils.isEmpty(subjectKeyID) ? EMPTY_FIELD : subjectKeyID;
    }

    public void setSubjectKeyID(String subjectKeyID) {
        this.subjectKeyID = subjectKeyID;
    }

    public String getAuthorityKeyID() {
        if(authorityKeyID == null){
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
        }
        return StringUtils.isEmpty(authorityKeyID) ? EMPTY_FIELD : authorityKeyID;
    }

    public void setAuthorityKeyID(String authorityKeyID) {
        this.authorityKeyID = authorityKeyID;
    }

    public X509Certificate getX509Cert() {
        return x509Cert;
    }

    public void setX509Cert(X509Certificate x509Cert) {
        this.x509Cert = x509Cert;
    }

    public String getValidStartDate() {
        validStartDate = DateFormat.getDateTimeInstance(
            DateFormat.LONG, DateFormat.LONG).format(x509Cert.getNotBefore());
        return validStartDate;
    }

    public void setValidStartDate(String validStartDate) {
        this.validStartDate = validStartDate;
    }

    public String getValidEndDate() {
        validEndDate = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(x509Cert.getNotAfter());
        return validEndDate;
    }

    public void setValidEndDate(String validEndDate) {
        this.validEndDate = validEndDate;
    }

    public String getIssuerName() {
        issuerName = StringUtils.isEmpty(x509Cert.getIssuerDN().getName()) ? EMPTY_FIELD
            : x509Cert.getIssuerDN().getName();
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getSubjectName() {
        subjectName = StringUtils.isEmpty(x509Cert.getSubjectDN().getName()) ? EMPTY_FIELD
            : x509Cert.getSubjectDN().getName();
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectPublicKeyAlgorithm() {
        if (subjectPublicKeyAlgorithm == null) {
            subjectPublicKeyAlgorithm = x509Cert.getPublicKey().getAlgorithm();
        }
        return subjectPublicKeyAlgorithm;
    }

    public void setSubjectPublicKeyAlgorithm(String subjectPublicKeyAlgorithm) {
        this.subjectPublicKeyAlgorithm = subjectPublicKeyAlgorithm;
    }

    public String getSubjectPublicKey() {
        if (subjectPublicKey == null) {
            subjectPublicKey = x509Cert.getPublicKey().toString();
        }
        return subjectPublicKey;
    }

    public void setSubjectPublicKey(String subjectPublicKey) {
        this.subjectPublicKey = subjectPublicKey;
    }

    public String getIssuerUniqueIdentifier() {
        if (issuerUniqueIdentifier == null) {
            issuerUniqueIdentifier = x509Cert.getIssuerUniqueID() != null
                ? Arrays.toString(x509Cert.getIssuerUniqueID())
                    : EMPTY_FIELD;
        }
        return issuerUniqueIdentifier;
    }

    public void setIssuerUniqueIdentifier(String issuerUniqueIdentifier) {
        this.issuerUniqueIdentifier = issuerUniqueIdentifier;
    }

    public String getCertSignatureAlgorithm() {
        if (certSignatureAlgorithm == null) {
            certSignatureAlgorithm = StringUtils.isEmpty(x509Cert.getSigAlgName()) ? EMPTY_FIELD
                : x509Cert.getSigAlgName();
        }
        return certSignatureAlgorithm;
    }

    public void setCertSignatureAlgorithm(String certSignatureAlgorithm) {
        this.certSignatureAlgorithm = certSignatureAlgorithm;
    }

    public String getCertSignature() {
        if (certSignature == null) {
            certSignature = new BigInteger(x509Cert.getSignature()).toString(16);
        }
        return certSignature;
    }

    public void setCertSignature(String certSignature) {
        this.certSignature = certSignature;
    }

    public long getExpiresInDays() {
        if (expiresInDays == 0) {
            Date certExpiryDate = x509Cert.getNotAfter();
            Date today = new Date();
            long dateDiff = certExpiryDate.getTime() - today.getTime();
            expiresInDays = dateDiff / (24 * 60 * 60 * 1000);
        }
        return expiresInDays;
    }

    public void setExpiresInDays(long expiresInDays) {
        this.expiresInDays = expiresInDays;
    }

    public String getExpiryColorCoding() {
        if (expiryColorCoding == null) {
            if (getExpiresInDays() <= 30) {
                setExpiryColorCoding(CERT_EXPIRY_COLOR_CODING.RED.toString());
            } else if (expiresInDays > 30 && expiresInDays <= 90) {
                setExpiryColorCoding(CERT_EXPIRY_COLOR_CODING.YELLOW.toString());
            } else {
                setExpiryColorCoding(CERT_EXPIRY_COLOR_CODING.GREEN.toString());
            }
        }
        return expiryColorCoding;
    }

    public void setExpiryColorCoding(String expiryColorCoding) {
        this.expiryColorCoding = expiryColorCoding;
    }

}
