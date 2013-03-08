package gov.hhs.fha.nhinc.callback.cxf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.saml.ext.AssertionWrapper;
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
}
