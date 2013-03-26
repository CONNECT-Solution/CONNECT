package gov.hhs.fha.nhinc.callback.cxf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.saml.SAMLKeyInfo;
import org.apache.ws.security.saml.ext.AssertionWrapper;
import org.apache.ws.security.validate.Credential;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.handler.RequestData;
import org.joda.time.DateTime;
import org.junit.Test;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml1.core.Statement;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.validation.Validator;
import org.opensaml.xml.validation.ValidatorSuite;

import com.sun.identity.saml.common.SAMLException;

public class CONNECTSamlAssertionValidatorTest {

	@Test
	public void testValidateAssertionSaml1() throws WSSecurityException,
			SAMLException {
		org.opensaml.saml1.core.Assertion saml1Assertion = mock(org.opensaml.saml1.core.Assertion.class);
		AssertionWrapper assertion = new AssertionWrapper(saml1Assertion);
		QName assertionQName = new QName(
				"urn:oasis:names:tc:SAML:1.0:assertion", "Assertion", "saml1");

		when(saml1Assertion.getElementQName()).thenReturn(assertionQName);
		when(saml1Assertion.getMajorVersion()).thenReturn(1);
		when(saml1Assertion.getMinorVersion()).thenReturn(0);
		when(saml1Assertion.getID()).thenReturn("Assertion_ID");
		when(saml1Assertion.getIssuer()).thenReturn("Issuer");
		DateTime dateTime = new DateTime();
		when(saml1Assertion.getIssueInstant()).thenReturn(dateTime);
		Statement statement = mock(Statement.class);
		List<Statement> statementList = new ArrayList<Statement>();
		statementList.add(statement);
		when(saml1Assertion.getStatements()).thenReturn(statementList);

		CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator();

		validator.validateAssertion(assertion);

		verify(saml1Assertion, times(2)).getOrderedChildren();
	}

	@Test(expected = WSSecurityException.class)
	public void testValidateAssertionSaml1_ValidationFails()
			throws WSSecurityException {
		org.opensaml.saml1.core.Assertion saml1Assertion = mock(org.opensaml.saml1.core.Assertion.class);
		AssertionWrapper assertion = new AssertionWrapper(saml1Assertion);
		QName assertionQName = new QName(
				"urn:oasis:names:tc:SAML:1.0:assertion", "Assertion", "saml1");

		when(saml1Assertion.getElementQName()).thenReturn(assertionQName);
		when(saml1Assertion.getMajorVersion()).thenReturn(1);
		when(saml1Assertion.getMinorVersion()).thenReturn(0);
		when(saml1Assertion.getID()).thenReturn(null);

		CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator();

		validator.validateAssertion(assertion);
	}

	@Test
	public void testValidateAssertionSaml2() throws WSSecurityException {
		org.opensaml.saml2.core.Assertion saml2Assertion = mock(org.opensaml.saml2.core.Assertion.class);
		AssertionWrapper assertion = new AssertionWrapper(saml2Assertion);
		QName assertionQName = new QName(
				"urn:oasis:names:tc:SAML:2.0:assertion", "Assertion", "saml2");

		when(saml2Assertion.getElementQName()).thenReturn(assertionQName);
		org.opensaml.saml2.core.Issuer issuer = mock(org.opensaml.saml2.core.Issuer.class);
		when(saml2Assertion.getIssuer()).thenReturn(issuer);
		when(saml2Assertion.getVersion()).thenReturn(SAMLVersion.VERSION_20);
		when(saml2Assertion.getID()).thenReturn("Assertion_ID");
		DateTime dateTime = new DateTime();
		when(saml2Assertion.getIssueInstant()).thenReturn(dateTime);
		Subject subject = mock(Subject.class);
		when(saml2Assertion.getSubject()).thenReturn(subject);

		CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {
			@Override
			protected ValidatorSuite getSaml2SpecValidator() {
				return getSaml2AssertionSpecValidator();
			}
		};

		validator.validateAssertion(assertion);

		verify(saml2Assertion, times(2)).getOrderedChildren();
	}

	@Test(expected = WSSecurityException.class)
	public void testValidateAssertionSaml2_ValidationFails()
			throws WSSecurityException {
		org.opensaml.saml2.core.Assertion saml2Assertion = mock(org.opensaml.saml2.core.Assertion.class);
		AssertionWrapper assertion = new AssertionWrapper(saml2Assertion);
		QName assertionQName = new QName(
				"urn:oasis:names:tc:SAML:2.0:assertion", "Assertion", "saml2");

		when(saml2Assertion.getElementQName()).thenReturn(assertionQName);
		when(saml2Assertion.getIssuer()).thenReturn(null);

		CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {
			@Override
			protected ValidatorSuite getSaml2SpecValidator() {
				return getSaml2AssertionSpecValidator();
			}
		};

		validator.validateAssertion(assertion);
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void testGetSaml2AllowNoSubjectAssertionSpecValidator() {
		CONNECTSamlAssertionValidator connectValidator = new CONNECTSamlAssertionValidator();
		ValidatorSuite specValidators = connectValidator
				.getSaml2AllowNoSubjectAssertionSpecValidator();
		QName qName = new QName("urn:oasis:names:tc:SAML:2.0:assertion",
				"Assertion", "saml2");
		List<Validator> validatorList = specValidators.getValidators(qName);

		assertNotNull(specValidators);
		assertEquals(validatorList.size(), 1);
	}

	@Test
	public void testGetSaml2SpecValidator() throws PropertyAccessException {
		PropertyAccessor propAccessor = mock(PropertyAccessor.class);
		CONNECTSamlAssertionValidator connectValidator = new CONNECTSamlAssertionValidator(
				propAccessor) {
			@Override
			protected ValidatorSuite getSaml2AssertionSpecValidator() {
				return null;
			}
		};

		when(
				propAccessor.getPropertyBoolean(
						NhincConstants.GATEWAY_PROPERTY_FILE,
						"allowNoSubjectAssertion")).thenReturn(true, false);

		ValidatorSuite validator = connectValidator.getSaml2SpecValidator();
		assertEquals(validator.getId(),
				"saml2-core-spec-validator-allow-no-subject-assertion");
		validator = connectValidator.getSaml2SpecValidator();
		assertNull(validator);
	}

	@Test
	public void testValidate() throws WSSecurityException {
		final List<Boolean> checkedSignedAssertion = new ArrayList<>();
		Credential credential = new Credential();
		final String SECRET_KEY = "secret";
		credential.setSecretKey(SECRET_KEY.getBytes());
		RequestData data = mock(RequestData.class);
		AssertionWrapper assertion = mock(AssertionWrapper.class);
		credential.setAssertion(assertion);

		List<String> methods = new ArrayList<>();
		final String METHOD_NAME = "urn:oasis:names:tc:SAML:" + "TESTING"
				+ ":cm:holder-of-key";
		methods.add(METHOD_NAME);

		SAMLKeyInfo keyInfo = mock(SAMLKeyInfo.class);
		org.opensaml.saml2.core.Assertion saml2Assertion = mock(org.opensaml.saml2.core.Assertion.class);
		org.opensaml.saml2.core.Conditions conditions = mock(org.opensaml.saml2.core.Conditions.class);
		DateTime testDate = new DateTime();

		// For validate() calls
		when(assertion.getConfirmationMethods()).thenReturn(methods);
		when(assertion.getSubjectKeyInfo()).thenReturn(keyInfo);
		when(assertion.isSigned()).thenReturn(true);
		// For checkConditions() calls
		when(assertion.getSamlVersion()).thenReturn(SAMLVersion.VERSION_20);
		when(assertion.getSaml2()).thenReturn(saml2Assertion, saml2Assertion,
				saml2Assertion, null);
		when(saml2Assertion.getConditions()).thenReturn(conditions);
		when(conditions.getNotOnOrAfter()).thenReturn(testDate.plusDays(1));
		when(conditions.getNotBefore()).thenReturn(testDate.minusSeconds(5));

		CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {
			@Override
			protected void checkSignedAssertion(AssertionWrapper assertion,
					RequestData data) throws WSSecurityException {
				checkedSignedAssertion.add(true);
			}
		};

		Credential resultCredential = validator.validate(credential, data);

		assertFalse(checkedSignedAssertion.isEmpty());
		assertTrue(checkedSignedAssertion.get(0).booleanValue());
		String resultSecretKey = new String(resultCredential.getSecretKey());
		assertEquals(resultSecretKey, SECRET_KEY);

	}

	@Test
	public void testCheckSignedAssertion_HappyPath() throws WSSecurityException {
		AssertionWrapper assertion = mock(AssertionWrapper.class);
		RequestData data = mock(RequestData.class);

		SAMLKeyInfo keyInfo = mock(SAMLKeyInfo.class);
		PublicKey publicKey = mock(PublicKey.class);
		Crypto crypto = mock(Crypto.class);

		when(assertion.getSignatureKeyInfo()).thenReturn(keyInfo);
		when(keyInfo.getPublicKey()).thenReturn(publicKey);
		when(data.getSigCrypto()).thenReturn(crypto);
		when(crypto.verifyTrust(publicKey)).thenReturn(true);

		CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator();

		validator.checkSignedAssertion(assertion, data);

		assertNotNull(assertion.getSignatureKeyInfo().getPublicKey());
		assertNull(assertion.getSignatureKeyInfo().getCerts());
	}

	@Test
	public void testCheckSignedAssertion_ChainCertError()
			throws WSSecurityException {
		AssertionWrapper assertion = mock(AssertionWrapper.class);
		RequestData data = mock(RequestData.class);

		SAMLKeyInfo keyInfo = mock(SAMLKeyInfo.class);
		PublicKey publicKey = mock(PublicKey.class);
		Crypto crypto = mock(Crypto.class);

		when(assertion.getSignatureKeyInfo()).thenReturn(keyInfo);
		when(keyInfo.getPublicKey()).thenReturn(publicKey);
		when(data.getSigCrypto()).thenReturn(crypto);

		// Return false here for Chain Cert Error
		when(crypto.verifyTrust(publicKey)).thenReturn(false);

		CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator();

		validator.checkSignedAssertion(assertion, data);

		assertNotNull(assertion.getSignatureKeyInfo().getPublicKey());
		assertNull(assertion.getSignatureKeyInfo().getCerts());
	}

	@Test(expected = WSSecurityException.class)
	public void testCheckSignedAssertion_Exception() throws WSSecurityException {
		AssertionWrapper assertion = mock(AssertionWrapper.class);
		RequestData data = mock(RequestData.class);

		SAMLKeyInfo keyInfo = mock(SAMLKeyInfo.class);

		when(assertion.getSignatureKeyInfo()).thenReturn(keyInfo);

		CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator();

		validator.checkSignedAssertion(assertion, data);
	}

}
