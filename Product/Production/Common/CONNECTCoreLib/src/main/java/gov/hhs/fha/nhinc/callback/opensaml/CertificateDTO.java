/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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

import java.security.cert.X509Certificate;
import org.apache.commons.lang.StringUtils;
import org.owasp.encoder.Encode;

/**
 *
 * @author tjafri
 */
public class CertificateDTO {

    private static final String EMPTY_FIELD = "-";
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
    private String expiryColorCoding;
    private long expiresInDays;
    private String issuerName;
    private String subjectName;
    private String subjectPublicKeyAlgorithm;
    private String subjectPublicKey;
    private String issuerUniqueIdentifier;
    private String certSignatureAlgorithm;
    private String certSignature;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlias() {
        return Encode.forHtmlContent(alias);
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
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
        return StringUtils.isBlank(subjectKeyID) ? EMPTY_FIELD : subjectKeyID;
    }

    public void setSubjectKeyID(String subjectKeyID) {
        this.subjectKeyID = subjectKeyID;
    }

    public String getAuthorityKeyID() {
        return StringUtils.isBlank(authorityKeyID) ? EMPTY_FIELD : authorityKeyID;
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
        return validStartDate;
    }

    public void setValidStartDate(String validStartDate) {
        this.validStartDate = validStartDate;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectPublicKeyAlgorithm() {
        return subjectPublicKeyAlgorithm;
    }

    public void setSubjectPublicKeyAlgorithm(String subjectPublicKeyAlgorithm) {
        this.subjectPublicKeyAlgorithm = subjectPublicKeyAlgorithm;
    }

    public String getSubjectPublicKey() {
        return subjectPublicKey;
    }

    public void setSubjectPublicKey(String subjectPublicKey) {
        this.subjectPublicKey = subjectPublicKey;
    }

    public String getIssuerUniqueIdentifier() {
        return issuerUniqueIdentifier;
    }

    public void setIssuerUniqueIdentifier(String issuerUniqueIdentifier) {
        this.issuerUniqueIdentifier = issuerUniqueIdentifier;
    }

    public String getCertSignatureAlgorithm() {
        return certSignatureAlgorithm;
    }

    public void setCertSignatureAlgorithm(String certSignatureAlgorithm) {
        this.certSignatureAlgorithm = certSignatureAlgorithm;
    }

    public String getCertSignature() {
        return certSignature;
    }

    public void setCertSignature(String certSignature) {
        this.certSignature = certSignature;
    }

    public long getExpiresInDays() {
        return expiresInDays;
    }

    public void setExpiresInDays(long expiresInDays) {
        this.expiresInDays = expiresInDays;
    }

    public String getExpiryColorCoding() {
        return expiryColorCoding;
    }

    public void setExpiryColorCoding(String expiryColorCoding) {
        this.expiryColorCoding = expiryColorCoding;
    }
}
