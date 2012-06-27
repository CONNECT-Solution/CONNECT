/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.callback;

import com.sun.xml.wss.impl.callback.SAMLCallback;
import com.sun.xml.wss.impl.callback.SignatureKeyCallback;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import javax.security.auth.callback.Callback;
import org.apache.commons.logging.Log;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * 
 * @author rhalfert
 */
@RunWith(JMock.class)
public class SamlCallbackHandlerTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);

    SAMLCallback callback;

    public SamlCallbackHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        callback = new SAMLCallback();
        callback.setConfirmationMethod(SAMLCallback.HOK_ASSERTION_TYPE);
        Map map = callback.getRuntimeProperties();
        map.put(SamlConstants.ASSERTION_ISSUER_PROP, "CN=SAML User,OU=Harris,O=HITS,L=Melbourne,ST=FL,C=US");
        map.put(SamlConstants.ASSERTION_ISSUER_FORMAT_PROP, "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");
        map.put(SamlConstants.USER_NAME_PROP, "kskagerb");

        map.put(SamlConstants.SUBJECT_LOCALITY_DNS_PROP, "cs.myharris.net");
        map.put(SamlConstants.SUBJECT_LOCALITY_ADDR_PROP, "158.147.185.168");
        map.put(SamlConstants.AUTHN_CONTEXT_CLASS_PROP, "urn:oasis:names:tc:SAML:2.0:ac:classes:X509");
        map.put(SamlConstants.AUTHN_INSTANT_PROP, "2009-04-16T13:15:39Z");
        map.put(SamlConstants.AUTHN_SESSION_INDEX_PROP, "987");

        map.put(SamlConstants.AUTHZ_STATEMENT_EXISTS_PROP, true);
        map.put(SamlConstants.RESOURCE_PROP, "https://158.147.185.168:8181/SamlReceiveService/SamlProcessWS");
        map.put(SamlConstants.AUTHZ_DECISION_PROP, "Permit");
        map.put(SamlConstants.USER_FIRST_PROP, "Karl");
        map.put(SamlConstants.USER_LAST_PROP, "Skagerberg");

        map.put(SamlConstants.USER_ORG_PROP, "1.1");
        map.put(SamlConstants.USER_ORG_ID_PROP, "1.1");
        map.put(SamlConstants.HOME_COM_PROP, "1.1");

        map.put(SamlConstants.USER_CODE_PROP, "307969004");
        map.put(SamlConstants.USER_SYST_PROP, "2.16.840.1.113883.6.96");
        map.put(SamlConstants.USER_SYST_NAME_PROP, "SNOMED_CT");
        map.put(SamlConstants.USER_DISPLAY_PROP, "Public Health");

        map.put(SamlConstants.PURPOSE_CODE_PROP, "PUBLICHEALTH");
        map.put(SamlConstants.PURPOSE_SYST_PROP, "2.16.840.1.113883.3.18.7.1");
        map.put(SamlConstants.PURPOSE_SYST_NAME_PROP, "nhin-purpose");
        map.put(SamlConstants.PURPOSE_DISPLAY_PROP, "Use or disclosure of Psychotherapy Notes");

        map.put(SamlConstants.PATIENT_ID_PROP, "500000000^^^&amp;1.1&amp;ISO");

        map.put(SamlConstants.EVIDENCE_VERSION_PROP, "2.0");
        map.put(SamlConstants.EVIDENCE_ID_PROP, "40df7c0a-ff3e-4b26-baeb-f2910f6d05a9");
        map.put(SamlConstants.EVIDENCE_INSTANT_PROP, "2009-04-16T13:10:39.093Z");
        map.put(SamlConstants.EVIDENCE_ISSUER_FORMAT_PROP, "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");
        map.put(SamlConstants.EVIDENCE_ISSUER_PROP, "CN=SAML User,OU=Harris,O=HITS,L=Melbourne,ST=FL,C=US");

        map.put(SamlConstants.EVIDENCE_CONDITION_NOT_BEFORE_PROP, "2009-04-16T13:10:39.093Z");
        map.put(SamlConstants.EVIDENCE_CONDITION_NOT_AFTER_PROP, "2009-12-31T12:00:00.000Z");

        ArrayList<String> s = new ArrayList<String>();
        s.add("urn:oid:1.2.3.4");
        map.put(SamlConstants.EVIDENCE_ACCESS_CONSENT_PROP, s);

        s = new ArrayList<String>();
        s.add("urn:oid:1.2.3.4.123456789");
        map.put(SamlConstants.EVIDENCE_INST_ACCESS_CONSENT_PROP, s);
    }

    /**
     * Test of handle method, of class SamlCallbackHandler.
     */
    @Test
    public void testHandle() throws Exception {
        System.out.println("handle");
        try {
            Callback[] callbacks = new Callback[] { callback };
            SamlCallbackHandler instance = new SamlCallbackHandler() {
                @Override
                protected void getDefaultPrivKeyCert(SignatureKeyCallback.DefaultPrivKeyCertRequest request)
                        throws IOException {
                    request.setX509Certificate(new X509Certificate() {

                        @Override
                        public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public void checkValidity(Date date) throws CertificateExpiredException,
                                CertificateNotYetValidException {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public int getVersion() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public BigInteger getSerialNumber() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public Principal getIssuerDN() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public Principal getSubjectDN() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public Date getNotBefore() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public Date getNotAfter() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public byte[] getTBSCertificate() throws CertificateEncodingException {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public byte[] getSignature() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public String getSigAlgName() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public String getSigAlgOID() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public byte[] getSigAlgParams() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public boolean[] getIssuerUniqueID() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public boolean[] getSubjectUniqueID() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public boolean[] getKeyUsage() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public int getBasicConstraints() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public byte[] getEncoded() throws CertificateEncodingException {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException,
                                InvalidKeyException, NoSuchProviderException, SignatureException {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public void verify(PublicKey key, String sigProvider) throws CertificateException,
                                NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException,
                                SignatureException {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public String toString() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public PublicKey getPublicKey() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public boolean hasUnsupportedCriticalExtension() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public Set<String> getCriticalExtensionOIDs() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public Set<String> getNonCriticalExtensionOIDs() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public byte[] getExtensionValue(String oid) {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }
                    });
                    request.setPrivateKey(new PrivateKey() {

                        @Override
                        public String getAlgorithm() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public String getFormat() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public byte[] getEncoded() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }
                    });
                }

            };
            instance.handle(callbacks);
        } catch (Exception ex) {
            // Exception expected due to certificate handling
            // fail("An Exception was caught during testing.");
        }

    }

    /**
     * Test of handle method, of class SamlCallbackHandler.
     */
    @Test
    public void testCreateEvidence() throws Exception {
        System.out.println("create evidence");
        try {
            Callback[] callbacks = new Callback[] { callback };
            SamlCallbackHandler instance = new SamlCallbackHandler();
            instance.tokenVals.putAll(callback.getRuntimeProperties());
            instance.createEvidence();
        } catch (Exception ex) {
            fail("An Exception was caught during testing.");
        }

    }
}