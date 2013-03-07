/**
 *
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.util.AbstractSuppressRootLoggerTest;

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

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opensaml.saml2.core.Action;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.DecisionTypeEnumeration;
import org.opensaml.saml2.core.Evidence;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Subject;
import org.w3c.dom.Element;

/**
 * @author bhumphrey
 * 
 */
public class HOKSAMLAssertionBuilderTest extends AbstractSuppressRootLoggerTest{

	private static RSAPublicKey publicKey;
	private static PrivateKey privateKey;

	@BeforeClass
	static public void setUp() throws NoSuchAlgorithmException {

		KeyPairGenerator keyGen;
		keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(512);
		publicKey = (RSAPublicKey) keyGen.genKeyPair().getPublic();
		privateKey = keyGen.genKeyPair().getPrivate();

	}

	/*
	 * KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType()); 54
	 * InputStream is = null; 55 try { 56 is = new ClassPathResource(
	 * "/org/springframework/ws/soap/security/xwss/test-keystore.jks"
	 * ).getInputStream(); 57 keyStore.load(is, "password".toCharArray()); 58 }
	 * 59 finally { 60 if (is != null) { 61 is.close(); 62 } 63 } 64 certificate
	 * = (X509Certificate) keyStore.getCertificate("alias");
	 */

	/**
	 * 
	 * @throws Exception
	 */

	@Test
	public void testBuild() throws Exception {
		SAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder(
				new CertificateManager() {

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
					public X509Certificate getDefaultCertificate()
							throws Exception {
						return new X509Certificate() {

							@Override
							public boolean hasUnsupportedCriticalExtension() {

								return false;
							}

							@Override
							public Set<String> getNonCriticalExtensionOIDs() {

								return Collections.EMPTY_SET;
							}

							@Override
							public byte[] getExtensionValue(String oid) {

								return new byte[1];
							}

							@Override
							public Set<String> getCriticalExtensionOIDs() {

								return Collections.EMPTY_SET;
							}

							@Override
							public void verify(PublicKey key, String sigProvider)
									throws CertificateException,
									NoSuchAlgorithmException,
									InvalidKeyException,
									NoSuchProviderException, SignatureException {

							}

							@Override
							public void verify(PublicKey key)
									throws CertificateException,
									NoSuchAlgorithmException,
									InvalidKeyException,
									NoSuchProviderException, SignatureException {

							}

							@Override
							public String toString() {

								return null;
							}

							@Override
							public PublicKey getPublicKey() {

								return publicKey;
							}

							@Override
							public byte[] getEncoded()
									throws CertificateEncodingException {

								return new byte[1];
							}

							@Override
							public int getVersion() {

								return 0;
							}

							@Override
							public byte[] getTBSCertificate()
									throws CertificateEncodingException {

								return new byte[1];
							}

							@Override
							public boolean[] getSubjectUniqueID() {

								return new boolean[1];
							}

							@Override
							public Principal getSubjectDN() {

								return null;
							}

							@Override
							public byte[] getSignature() {

								return new byte[1];
							}

							@Override
							public byte[] getSigAlgParams() {

								return new byte[1];
							}

							@Override
							public String getSigAlgOID() {

								return null;
							}

							@Override
							public String getSigAlgName() {

								return null;
							}

							@Override
							public BigInteger getSerialNumber() {

								return null;
							}

							@Override
							public Date getNotBefore() {

								return null;
							}

							@Override
							public Date getNotAfter() {

								return null;
							}

							@Override
							public boolean[] getKeyUsage() {

								return new boolean[1];
							}

							@Override
							public boolean[] getIssuerUniqueID() {

								return new boolean[1];
							}

							@Override
							public Principal getIssuerDN() {

								return null;
							}

							@Override
							public int getBasicConstraints() {

								return 0;
							}

							@Override
							public void checkValidity(Date date)
									throws CertificateExpiredException,
									CertificateNotYetValidException {

							}

							@Override
							public void checkValidity()
									throws CertificateExpiredException,
									CertificateNotYetValidException {

							}
						};

					}
				});
		Element assertion = builder.build(getProperties());
		assertNotNull(assertion);
	}

	@Test
	public void testCreateAuthenicationStatement() {
		List<AuthnStatement> authnStatement = HOKSAMLAssertionBuilder
				.createAuthenicationStatements(getProperties());
		assertNotNull(authnStatement);

		assertFalse(authnStatement.isEmpty());
	}

	@Test
	public void testCreateAuthenticationDecisionStatements() {
		CallbackProperties callbackProps = mock(CallbackProperties.class);
		Subject subject = mock(Subject.class);
		DateTime beforeCreation = new DateTime();

		when(callbackProps.getAuthenicationStatementExists()).thenReturn(true);

		List<AuthzDecisionStatement> statementList = HOKSAMLAssertionBuilder
				.createAuthenicationDecsionStatements(callbackProps, subject);

		assertFalse(statementList.isEmpty());
		AuthzDecisionStatement statement = statementList.get(0);
		assertEquals(statement.getDecision(), DecisionTypeEnumeration.PERMIT);

		Action action = statement.getActions().get(0);
		assertEquals(action.getAction(),
				SAMLAssertionBuilder.AUTHZ_DECISION_ACTION_EXECUTE);

		Evidence evidence = statement.getEvidence();
		Assertion assertion = evidence.getAssertions().get(0);
		assertTrue(assertion.getID().startsWith("_"));

		assertTrue(beforeCreation.isBefore(assertion.getIssueInstant())
				|| beforeCreation.isEqual(assertion.getIssueInstant()));

		Issuer issuer = assertion.getIssuer();
		assertEquals(issuer.getFormat(), SAMLAssertionBuilder.X509_NAME_ID);

		Conditions conditions = assertion.getConditions();
		assertTrue(beforeCreation.isBefore(conditions.getNotBefore())
				|| beforeCreation.isEqual(conditions.getNotBefore()));
		assertTrue(beforeCreation.isBefore(conditions.getNotOnOrAfter())
				|| beforeCreation.isEqual(conditions.getNotOnOrAfter()));

		List<AttributeStatement> attributeStatement = assertion
				.getAttributeStatements();
		assertEquals(attributeStatement.get(0).getAttributes().size(), 2);

		Attribute firstAttribute = attributeStatement.get(0).getAttributes()
				.get(0);
		Attribute secondAttribute = attributeStatement.get(0).getAttributes()
				.get(1);
		assertEquals(firstAttribute.getName(), "AccessConsentPolicy");
		assertEquals(secondAttribute.getName(), "InstanceAccessConsentPolicy");
	}

	CallbackProperties getProperties() {
		return new CallbackProperties() {

			@Override
			public String getUsername() {
				return "userName";
			}

			@Override
			public String getUserSystemName() {
				return "sytemName";
			}

			@Override
			public String getUserSystem() {
				return "userSystem";
			}

			@Override
			public String getUserOrganization() {
				return "uerOrg";
			}

			@Override
			public String getUserFullName() {
				return "Full Name";
			}

			@Override
			public String getUserDisplay() {
				return "display";
			}

			@Override
			public String getUserCode() {
				return "userCode";
			}

			@Override
			public String getSubjectLocality() {
				return "subject";
			}

			@Override
			public String getSubjectDNS() {
				return "dns";
			}

			@Override
			public String getPurposeSystemName() {
				return "systemname";
			}

			@Override
			public String getPurposeSystem() {
				return "purpose";
			}

			@Override
			public String getPurposeDisplay() {
				return "disply";
			}

			@Override
			public String getPurposeCode() {
				return "code";
			}

			@Override
			public String getPatientID() {
				return "pid";
			}

			@Override
			public String getIssuer() {
				return "issuer";
			}

			@Override
			public String getHomeCommunity() {
				return "hci";
			}

			@Override
			public String getEvidenceIssuerFormat() {
				return "format";
			}

			@Override
			public String getEvidenceIssuer() {
				return "issuer";
			}

			@Override
			public String getEvidenceSubject() {
				return "evidenceSubject";
			}

			@Override
			public DateTime getEvidenceInstant() {
				return new DateTime();
			}

			@Override
			public List getEvidenceInstantAccessConsent() {
				return Collections.EMPTY_LIST;
			}

			@Override
			public String getEvidenceID() {
				return "evidence id";
			}

			@Override
			public DateTime getEvidenceConditionNotBefore() {
				return new DateTime();
			}

			@Override
			public DateTime getEvidenceConditionNotAfter() {
				return new DateTime();
			}

			@Override
			public List getEvidenceAccessConstent() {
				return Collections.EMPTY_LIST;
			}

			@Override
			public String getAuthnicationResource() {
				return "resource";
			}

			@Override
			public Boolean getAuthenicationStatementExists() {
				return false;
			}

			@Override
			public String getAuthenticationSessionIndex() {
				return "1";
			}

			@Override
			public DateTime getAuthenticationInstant() {
				return new DateTime();
			}

			@Override
			public String getAuthenicationDecision() {
				return null;
			}

			@Override
			public String getAuthenticationContextClass() {
				return "cntx";
			}

			@Override
			public String getAssertionIssuerFormat() {
				return "format";
			}

			@Override
			public String getTargetHomeCommunityId() {
				return "targetHomeCommunityId";
			}

			@Override
			public String getServiceName() {
				return "serviceName";
			}

			@Override
			public String getAction() {
				return "action";
			}

			@Override
			public GATEWAY_API_LEVEL getTargetApiLevel() {
				return GATEWAY_API_LEVEL.LEVEL_g1;
			}

		};
	}

}
