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

import gov.hhs.fha.nhinc.callback.openSAML.CertificateManagerImpl;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.apache.cxf.helpers.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mpnguyen
 *
 */
public class CertificateChainValidatorTest {
    private CertificateChainValidator certValidator;
    private CertificateManagerImpl certManager ;


    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        prepareSystemProperties("/gateway_self_sign.jks","/cacerts.jks");
        certManager = (CertificateManagerImpl) CertificateManagerImpl.getInstance();
        certValidator = new CertificateChainValidator(certManager);
    }


    /**
     *
     */

    private void prepareSystemProperties(String keyStoreName, String trustStoreName) {
        System.setProperty(CertificateManagerImpl.KEY_STORE_KEY, getFilePath(keyStoreName));
        System.setProperty(CertificateManagerImpl.KEY_STORE_PASSWORD_KEY, "changeit");
        System.setProperty(CertificateManagerImpl.KEY_STORE_TYPE_KEY, "JKS");


        System.setProperty(CertificateManagerImpl.TRUST_STORE_KEY, getFilePath(trustStoreName));
        System.setProperty(CertificateManagerImpl.TRUST_STORE_PASSWORD_KEY, "changeit");
        System.setProperty(CertificateManagerImpl.TRUST_STORE_TYPE_KEY, "JKS");
    }


    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        certValidator = null;
        certManager = null;

    }

    @Test
    public final void testWhetherChainOfTrust() {
        X509Certificate certToCheck = getTestCert("/cert_test.cer");
        Assert.assertTrue("Cert should be a part chain of trust", certValidator.isPartChainOfTrust(certToCheck));
    }
    @Test
    public final void testSelfSignedCert(){
        X509Certificate certToCheck = getTestCert("/self_sign.cer");
        Assert.assertFalse("Cert is self-sign cert", certValidator.isPartChainOfTrust(certToCheck));
    }
    @Test
    public final void testValidateCert(){
        X509Certificate certToCheck = getTestCert("/epic.cer");
        Assert.assertTrue("Our truststore should contain this cert",certValidator.validateCert(certToCheck));
    }
    @Test
    public final void testValidRoot(){
        /*certValidator = null;
        certManager = null;
        prepareSystemProperties("/gateway_self_sign.jks", "");*/
        X509Certificate certToCheck = getTestCert("/entrust_root_old.cer");
        Assert.assertTrue("Our truststore should contain root cert",certValidator.validateCert(certToCheck));
    }
    @Test
    public final void testInvalidCert(){
        X509Certificate certToCheck = getTestCert("/HOST1.cer");
        Assert.assertFalse("Our truststore should not contain",certValidator.validateCert(certToCheck));
    }
    @Test
    public final void testValidOnlyInterAndRootCert(){
        //This test cert that only has intermediate and root.
        X509Certificate certToCheck = getTestCert("/entrust_intermediate_old.cer");
        Assert.assertTrue("Our truststore should this chain of trust",certValidator.validateCert(certToCheck));
    }
    @Test
    public final void testMissingRoot(){
        certValidator = null;
        certManager = null;
        prepareSystemProperties("/gateway_self_sign.jks", "/cacerts_missing_root.jks");
        certManager = (CertificateManagerImpl) CertificateManagerImpl.getInstance();
        certValidator = new CertificateChainValidator(certManager);
        X509Certificate certToCheck = getTestCert("/epic.cer");
        Assert.assertFalse("Our truststore doesn't contain the root to validate this",certValidator.validateCert(certToCheck));
    }
    private X509Certificate getTestCert(String certName){
        CertificateFactory certificateFactory;
        X509Certificate certificateToCheck = null;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            FileInputStream inputStream = new FileInputStream(getFilePath(certName));
            certificateToCheck = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(IOUtils.readBytesFromStream(inputStream)));
            Assert.assertNotNull(certificateToCheck);
        } catch (CertificateException | IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }
        return certificateToCheck;

    }
    private String getFilePath(String filename){
        return this.getClass().getResource(filename).getFile();
    }

}
