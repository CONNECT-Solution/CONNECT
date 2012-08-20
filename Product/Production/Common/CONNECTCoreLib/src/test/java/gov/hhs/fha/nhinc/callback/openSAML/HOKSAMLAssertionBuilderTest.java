/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
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
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opensaml.saml2.core.AuthnStatement;
import org.w3c.dom.Element;

/**
 * @author bhumphrey
 * 
 */
public class HOKSAMLAssertionBuilderTest {

    private static RSAPublicKey publicKey;
    private static PrivateKey privateKey;

    @BeforeClass
    static public void setUp() throws NoSuchAlgorithmException {

        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.INFO);
        rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));

        KeyPairGenerator keyGen;
        keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        publicKey = (RSAPublicKey) keyGen.genKeyPair().getPublic();
        privateKey = keyGen.genKeyPair().getPrivate();

    }

    /*
     * KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType()); 54 InputStream is = null; 55 try { 56 is =
     * new ClassPathResource("/org/springframework/ws/soap/security/xwss/test-keystore.jks").getInputStream(); 57
     * keyStore.load(is, "password".toCharArray()); 58 } 59 finally { 60 if (is != null) { 61 is.close(); 62 } 63 } 64
     * certificate = (X509Certificate) keyStore.getCertificate("alias");
     */

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testBuild() throws Exception {
        SAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder(new CertificateManager() {

            @Override
            public RSAPublicKey getDefaultPublicKey() {
                return publicKey;

            }

            @Override
            public PrivateKey getDefaultPrivateKey() throws Exception {

                return privateKey;
            }

            @Override
            public KeyStore getKeyStore() {
                return null;
            }

            @Override
            public KeyStore getTrustStore() {
                return null;
            }

            @Override
            public X509Certificate getDefaultCertificate() throws Exception {
                return new X509Certificate() {

                    @Override
                    public boolean hasUnsupportedCriticalExtension() {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public Set<String> getNonCriticalExtensionOIDs() {
                        // TODO Auto-generated method stub
                        return Collections.EMPTY_SET;
                    }

                    @Override
                    public byte[] getExtensionValue(String oid) {
                        // TODO Auto-generated method stub
                        return new byte[1];
                    }

                    @Override
                    public Set<String> getCriticalExtensionOIDs() {
                        // TODO Auto-generated method stub
                        return Collections.EMPTY_SET;
                    }

                    @Override
                    public void verify(PublicKey key, String sigProvider) throws CertificateException,
                            NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException,
                            InvalidKeyException, NoSuchProviderException, SignatureException {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public String toString() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public PublicKey getPublicKey() {
                        // TODO Auto-generated method stub
                        return publicKey;
                    }

                    @Override
                    public byte[] getEncoded() throws CertificateEncodingException {
                        // TODO Auto-generated method stub
                        return new byte[1];
                    }

                    @Override
                    public int getVersion() {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public byte[] getTBSCertificate() throws CertificateEncodingException {
                        // TODO Auto-generated method stub
                        return new byte[1];
                    }

                    @Override
                    public boolean[] getSubjectUniqueID() {
                        // TODO Auto-generated method stub
                        return new boolean[1];
                    }

                    @Override
                    public Principal getSubjectDN() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public byte[] getSignature() {
                        // TODO Auto-generated method stub
                        return new byte[1];
                    }

                    @Override
                    public byte[] getSigAlgParams() {
                        // TODO Auto-generated method stub
                        return new byte[1];
                    }

                    @Override
                    public String getSigAlgOID() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public String getSigAlgName() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public BigInteger getSerialNumber() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public Date getNotBefore() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public Date getNotAfter() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public boolean[] getKeyUsage() {
                        // TODO Auto-generated method stub
                        return new boolean[1];
                    }

                    @Override
                    public boolean[] getIssuerUniqueID() {
                        // TODO Auto-generated method stub
                        return new boolean[1];
                    }

                    @Override
                    public Principal getIssuerDN() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public int getBasicConstraints() {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public void checkValidity(Date date) throws CertificateExpiredException,
                            CertificateNotYetValidException {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
                        // TODO Auto-generated method stub

                    }
                };

            }
        });
        Element assertion = builder.build(getProperties());
        assertNotNull(assertion);
    }

    @Test
    public void testCreateAuthenicationStatement() {
        List<AuthnStatement> authnStatement = HOKSAMLAssertionBuilder.createAuthenicationStatements(getProperties());
        assertNotNull(authnStatement);

        assertFalse(authnStatement.isEmpty());
    }

    CallbackProperties getProperties() {
        return new CallbackProperties() {

            @Override
            public String getUsername() {
                // TODO Auto-generated method stub
                return "userName";
            }

            @Override
            public String getUserSystemName() {
                // TODO Auto-generated method stub
                return "sytemName";
            }

            @Override
            public String getUserSystem() {
                // TODO Auto-generated method stub
                return "userSystem";
            }

            @Override
            public String getUserOrganization() {
                // TODO Auto-generated method stub
                return "uerOrg";
            }

            @Override
            public String getUserFullName() {
                // TODO Auto-generated method stub
                return "Full Name";
            }

            @Override
            public String getUserDisplay() {
                // TODO Auto-generated method stub
                return "display";
            }

            @Override
            public String getUserCode() {
                // TODO Auto-generated method stub
                return "userCode";
            }

            @Override
            public String getSubjectLocality() {
                // TODO Auto-generated method stub
                return "subject";
            }

            @Override
            public String getSubjectDNS() {
                // TODO Auto-generated method stub
                return "dns";
            }

            @Override
            public String getPurposeSystemName() {
                // TODO Auto-generated method stub
                return "systemname";
            }

            @Override
            public String getPurposeSystem() {
                // TODO Auto-generated method stub
                return "purpose";
            }

            @Override
            public String getPurposeDisplay() {
                // TODO Auto-generated method stub
                return "disply";
            }

            @Override
            public String getPurposeCode() {
                // TODO Auto-generated method stub
                return "code";
            }

            @Override
            public String getPatientID() {
                // TODO Auto-generated method stub
                return "pid";
            }

            @Override
            public String getIssuer() {
                // TODO Auto-generated method stub
                return "issuer";
            }

            @Override
            public String getHomeCommunity() {
                // TODO Auto-generated method stub
                return "hci";
            }

            @Override
            public String getEvidenceIssuerFormat() {
                // TODO Auto-generated method stub
                return "format";
            }

            @Override
            public String getEvidenceIssuer() {
                // TODO Auto-generated method stub
                return "issuer";
            }
            
            @Override
            public String getEvidenceSubject() {
                // TODO Auto-generated method stub
                return "evidenceSubject";
            }

            @Override
            public DateTime getEvidenceInstant() {
                // TODO Auto-generated method stub
                return new DateTime();
            }

            @Override
            public List getEvidenceInstantAccessConsent() {
                // TODO Auto-generated method stub
                return Collections.EMPTY_LIST;
            }

            @Override
            public String getEvidenceID() {
                // TODO Auto-generated method stub
                return "evidence id";
            }

            @Override
            public DateTime getEvidenceConditionNotBefore() {
                // TODO Auto-generated method stub
                return new DateTime();
            }

            @Override
            public DateTime getEvidenceConditionNotAfter() {
                // TODO Auto-generated method stub
                return new DateTime();
            }

            @Override
            public List getEvidenceAccessConstent() {
                // TODO Auto-generated method stub
                return Collections.EMPTY_LIST;
            }

            @Override
            public String getAuthnicationResource() {
                // TODO Auto-generated method stub
                return "resource";
            }

            @Override
            public Boolean getAuthenicationStatementExists() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public String getAuthenicationSessionIndex() {
                // TODO Auto-generated method stub
                return "1";
            }

            @Override
            public DateTime getAuthenicationInstant() {
                // TODO Auto-generated method stub
                return new DateTime();
            }

            @Override
            public String getAuthenicationDecision() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getAuthenicationContextClass() {
                // TODO Auto-generated method stub
                return "cntx";
            }

            @Override
            public String getAssertionIssuerFormat() {
                // TODO Auto-generated method stub
                return "format";
            }
        };
    }

}
