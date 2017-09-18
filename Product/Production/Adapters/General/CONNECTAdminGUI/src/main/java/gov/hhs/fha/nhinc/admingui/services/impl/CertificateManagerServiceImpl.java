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
package gov.hhs.fha.nhinc.admingui.services.impl;

import gov.hhs.fha.nhinc.admingui.event.model.Certificate;
import gov.hhs.fha.nhinc.admingui.services.CertificateManagerService;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManager;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.codec.binary.Hex;
import org.apache.wss4j.common.crypto.DERDecoder;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class CertificateManagerServiceImpl implements CertificateManagerService {

  private static final Logger LOG = LoggerFactory.getLogger(CertificateManagerServiceImpl.class);
  private CertificateManager cmHelper = CertificateManagerImpl.getInstance();
  //NOSONAR
  private static final String AUTHORITY_KEY_ID = "2.5.29.35";
  //NOSONAR
  private static final String SUBJECT_KEY_ID = "2.5.29.14";
  private static final int AUTHORITY_KEY_POSITION = 6;

  @Override
  public List<Certificate> fetchKeyStores() {
    return buildCertificateList(cmHelper.getKeyStore());
  }

  @Override
  public List<Certificate> fetchTrustStores() {
    return buildCertificateList(cmHelper.getTrustStore());
  }

  @Override
  public String getKeyStoreLocation() {
    return cmHelper.getKeyStoreLocation();
  }

  @Override
  public String getTrustStoreLocation() {
    return cmHelper.getTrustStoreLocation();
  }

  @Override
  public List<Certificate> refreshKeyStores() {
    return buildCertificateList(cmHelper.refreshKeyStore());
  }

  private List<Certificate> buildCertificateList(KeyStore keystore) {
    List<Certificate> certs = null;
    try {
      Enumeration<String> aliases = keystore.aliases();
      if (aliases != null) {
        certs = new ArrayList<>();
        while (aliases.hasMoreElements()) {
          String alias = aliases.nextElement();
          java.security.cert.Certificate jCert = keystore.getCertificate(alias);
          X509Certificate x509 = (X509Certificate) jCert;
          Certificate obj = new Certificate();
          obj.setAlias(alias);
          obj.setAlgorithm(jCert.getPublicKey().getAlgorithm());
          obj.setExpirationDate(x509.getNotAfter());
          obj.setSerialNumber(getCertSerialNumber(x509));
          obj.setVersion(x509.getVersion());
          obj.setAuthorityKeyID(getAuthorityKeyIdentify(x509.getExtensionValue(AUTHORITY_KEY_ID)));
          obj.setSubjectKeyID(getSubjectKeyID(x509.getExtensionValue(SUBJECT_KEY_ID)));
          obj.setKeySize(getKeySize(x509));
          certs.add(obj);
        }
      }
    } catch (KeyStoreException ex) {
      LOG.error("Unable to fetch keystore: {}", ex);
    }
    return certs;
  }

  private static String getSubjectKeyID(byte[] subjectKeyID) {
    String ski = null;
    try {
      if (subjectKeyID != null) {
        // this logic extracts from CryptoBase class inside wss4j
        DERDecoder extVal = new DERDecoder(subjectKeyID);
        extVal.expect(DERDecoder.TYPE_OCTET_STRING); // ExtensionValue OCTET STRING
        extVal.getLength(); // leave this method alone. getlength modify array position.
        extVal.expect(DERDecoder.TYPE_OCTET_STRING); // KeyIdentifier OCTET STRING
        int keyIDLen = extVal.getLength();
        ski = Hex.encodeHexString(extVal.getBytes(keyIDLen));
      }
    } catch (WSSecurityException e) {
      LOG.error("Unable to convert SKI into human readable {}", e.getLocalizedMessage(), e);
    }
    return ski;
  }

  private static String getAuthorityKeyIdentify(byte[] authorityKey) {
    String aik = null;
    try {
      if (authorityKey != null) {
        DERDecoder extValA = new DERDecoder(authorityKey);
        extValA.skip(AUTHORITY_KEY_POSITION);
        int length = authorityKey.length - AUTHORITY_KEY_POSITION;
        aik = Hex.encodeHexString(extValA.getBytes(length));
      }
    } catch (WSSecurityException e) {
      LOG.error("Unable to convert AIK into human readable {} ", e.getLocalizedMessage(), e);
    }
    return aik;
  }

  private static String getCertSerialNumber(X509Certificate cert) {
    return new String(Hex.encodeHex(cert.getSerialNumber().toByteArray()));
  }
  private static int getKeySize(X509Certificate cert){
    PublicKey publicKey = cert.getPublicKey();
    if (publicKey instanceof RSAPublicKey){
      return ((RSAPublicKey)publicKey).getModulus().bitLength();
    }
    return -1;
  }
}
