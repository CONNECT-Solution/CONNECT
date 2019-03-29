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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import gov.hhs.fha.nhinc.cryptostore.StoreUtil;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mweaver/jsmith
 *
 */
public class CertificateManagerImplTest {

    private CertificateManagerImpl certManager;
    private final String KEY_STORE_PATH = "src/test/resources/gov/hhs/fha/nhinc/callback/gateway.jks";
    private final String TRUST_STORE_PATH = "src/test/resources/gov/hhs/fha/nhinc/callback/cacerts.jks";
    private final String KEY_DEFAULT_CERT = StoreUtil.getPrivateKeyAlias();

    @Before
    public void setUp() {
        final HashMap<String, String> keyStoreMap = new HashMap<>();
        keyStoreMap.put(CertificateManagerImpl.KEY_STORE_KEY, KEY_STORE_PATH);
        keyStoreMap.put(CertificateManagerImpl.KEY_STORE_PASSWORD_KEY, "changeit");
        keyStoreMap.put(CertificateManagerImpl.KEY_STORE_TYPE_KEY, "JKS");

        final HashMap<String, String> trustStoreMap = new HashMap<>();
        trustStoreMap.put(CertificateManagerImpl.TRUST_STORE_KEY, TRUST_STORE_PATH);
        trustStoreMap.put(CertificateManagerImpl.TRUST_STORE_PASSWORD_KEY, "changeit");
        trustStoreMap.put(CertificateManagerImpl.TRUST_STORE_TYPE_KEY, "JKS");

        certManager = (CertificateManagerImpl) CertificateManagerImpl.getInstance(keyStoreMap, trustStoreMap);
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
        PrivateKey privateKey = certManager.getPrivateKeyBy(KEY_DEFAULT_CERT);
        assertNotNull(privateKey);
        assertEquals(privateKey.getAlgorithm(), "RSA");
        assertEquals(privateKey.getFormat(), "PKCS#8");
    }

    @Test
    public void testGetDefaultPublicKey() {
        RSAPublicKey publicKey = certManager.getPublicKeyBy(KEY_DEFAULT_CERT);
        assertNotNull(publicKey);
        assertEquals(publicKey.getAlgorithm(), "RSA");
        assertEquals(publicKey.getFormat(), "X.509");
    }

    @Test
    public void testGetDefaultCertificate() throws Exception {
        X509Certificate certificate = certManager.getCertificateBy(KEY_DEFAULT_CERT);
        assertNotNull(certificate);
        assertEquals(certificate.getSigAlgName(), "SHA256withRSA");
        assertEquals(certificate.getSigAlgOID(), "1.2.840.113549.1.1.11");
    }

    @Test
    public void testCertificateChain() throws Exception {
        KeyStore keystore = CertificateUtil.loadKeyStore("JKS", "changeit", "src/test/resources/cacerts-chain.jks");

        Certificate leaf = keystore.getCertificate("gateway");
        Certificate interm = keystore.getCertificate("gateway-intermediate");
        Certificate root = keystore.getCertificate("gateway-root");

        Map<String, Certificate> chain = CertificateUtil.getChain("gateway", leaf, keystore);
        assertEquals(chain.size(), 3);

        assertTrue("gateway-root is in the chain", CertificateUtil.isInChain(root, chain));
        assertTrue("gateway-intermediate is in the chain", CertificateUtil.isInChain(interm, chain));

        assertEquals(CertificateUtil.getCertSubjectCN(leaf), "server");
        assertEquals(CertificateUtil.getCertSubjectCN(interm), "ca");
        assertEquals(CertificateUtil.getCertSubjectCN(root), "root");

        chain = CertificateUtil.getChain("gateway-intermediate", interm, keystore);
        assertEquals(chain.size(), 2);

        keystore = CertificateUtil.loadKeyStore("JKS", "changeit", TRUST_STORE_PATH);
        leaf = keystore.getCertificate("host1");
        chain = CertificateUtil.getChain("gateway", leaf, keystore);
        assertEquals(chain.size(), 1);

    }
}
