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

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.DateFormat;
import java.util.Arrays;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

/**
 * @author Poornima Venkatakrishnan
 *
 */
public class X509CertificateHelper {
    private static final String EMPTY_FIELD = "-";
    private X509Certificate x509Cert;
    private int keySize = -1;
    private DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

    public CertificateDTO buildCertificate(X509Certificate x509Cert) {
        this.x509Cert = x509Cert;

        CertificateDTO obj = new CertificateDTO();
        obj.setSerialNumber(getCertSerialNumber());
        obj.setIssuerName(getIssuerName());
        obj.setVersion(x509Cert.getVersion());
        obj.setSubjectKeyID(CertificateUtil.getCertKeyIdSubject(x509Cert));
        obj.setAlgorithm(x509Cert.getPublicKey().getAlgorithm());
        obj.setKeySize(getKeySize());
        obj.setAuthorityKeyID(CertificateUtil.getCertKeyIdAuthority(x509Cert));
        obj.setValidStartDate(getValidStartDate());
        obj.setExpirationDate(getValidEndDate());
        obj.setSubjectName(getSubjectName());
        obj.setSubjectPublicKeyAlgorithm(getSubjectPublicKeyAlgorithm());
        obj.setSubjectPublicKey(getSubjectPublicKey());
        obj.setIssuerUniqueIdentifier(getIssuerUniqueIdentifier());
        obj.setCertSignatureAlgorithm(getCertSignatureAlgorithm());
        obj.setCertSignature(getCertSignature());
        obj.setExpiresInDays(CertificateUtil.getDaysOfExpiration(x509Cert.getNotAfter()));
        obj.setSignatureAlgorithm(x509Cert.getSigAlgName());
        obj.setX509Cert(x509Cert);
        return obj;
    }

    private String getCertSerialNumber() {
        return new String(Hex.encodeHex(x509Cert.getSerialNumber().toByteArray()));
    }

    private int getKeySize() {
        PublicKey publicKey = x509Cert.getPublicKey();
        if (publicKey instanceof RSAPublicKey) {
            keySize = ((RSAPublicKey) publicKey).getModulus().bitLength();
        }
        return keySize;
    }

    private String getIssuerName() {
        return StringUtils.isEmpty(x509Cert.getIssuerDN().getName()) ? EMPTY_FIELD
            : x509Cert.getIssuerDN().getName();
    }

    private String getValidStartDate() {
        return formatter.format(x509Cert.getNotBefore());
    }

    private String getValidEndDate() {
        return formatter.format(x509Cert.getNotAfter());
    }

    private String getSubjectName() {
        return StringUtils.isEmpty(x509Cert.getSubjectDN().getName()) ? EMPTY_FIELD
            : x509Cert.getSubjectDN().getName();
    }

    private String getSubjectPublicKeyAlgorithm() {
        return x509Cert.getPublicKey().getAlgorithm();
    }

    private String getSubjectPublicKey() {
        return x509Cert.getPublicKey().toString();
    }

    private String getIssuerUniqueIdentifier() {
        return x509Cert.getIssuerUniqueID() != null ? Arrays.toString(x509Cert.getIssuerUniqueID())
            : EMPTY_FIELD;
    }

    private String getCertSignatureAlgorithm() {
        return StringUtils.isEmpty(x509Cert.getSigAlgName()) ? EMPTY_FIELD : x509Cert.getSigAlgName();
    }

    private String getCertSignature() {
        return new BigInteger(x509Cert.getSignature()).toString(16);
    }
}
