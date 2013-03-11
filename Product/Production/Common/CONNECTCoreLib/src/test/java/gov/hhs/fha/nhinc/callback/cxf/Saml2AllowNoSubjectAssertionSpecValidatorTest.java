package gov.hhs.fha.nhinc.callback.cxf;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml2.core.Statement;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.validation.ValidationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Saml2AllowNoSubjectAssertionSpecValidatorTest {

	@Test
	public void testValidateSubject() throws ValidationException {
		Assertion assertion = mock(Assertion.class);
		Saml2AllowNoSubjectAssertionSpecValidator validator = new Saml2AllowNoSubjectAssertionSpecValidator();

		Statement statement = mock(Statement.class);
		List<Statement> statementList = new ArrayList<Statement>();
		statementList.add(statement);
		AuthnStatement authnStatement = mock(AuthnStatement.class);
		List<AuthnStatement> authnStatementList = new ArrayList<AuthnStatement>();
		authnStatementList.add(authnStatement);
		AttributeStatement attrStatement = mock(AttributeStatement.class);
		List<AttributeStatement> attrStatementList = new ArrayList<AttributeStatement>();
		attrStatementList.add(attrStatement);
		AuthzDecisionStatement authzDecisionStatement = mock(AuthzDecisionStatement.class);
		List<AuthzDecisionStatement> authzDecisionStatementList = new ArrayList<AuthzDecisionStatement>();
		authzDecisionStatementList.add(authzDecisionStatement);
		Subject subject = mock(Subject.class);

		when(assertion.getStatements()).thenReturn(statementList);
		when(assertion.getAuthnStatements()).thenReturn(authnStatementList);
		when(assertion.getAttributeStatements()).thenReturn(attrStatementList);
		when(assertion.getAuthzDecisionStatements()).thenReturn(
				authzDecisionStatementList);
		when(assertion.getSubject()).thenReturn(subject);

		validator.validate(assertion);

		assertTrue(true);
	}

	@Test(expected = ValidationException.class)
	public void testValidateSubject_FailOnGetStatemtents()
			throws ValidationException {
		String expectedMessage = "Subject is required when Statements are absent";
		Assertion assertion = mock(Assertion.class);
		Saml2AllowNoSubjectAssertionSpecValidator validator = new Saml2AllowNoSubjectAssertionSpecValidator();

		List<Statement> statementList = new ArrayList<Statement>();

		when(assertion.getStatements()).thenReturn(statementList);

		try {
			validator.validate(assertion);
		} catch (ValidationException e) {
			assertEquals(e.getMessage(), expectedMessage);
			throw e;
		}
	}

	@Test(expected = ValidationException.class)
	public void testValidateSubject_FailOnGetAuthnStatements()
			throws ValidationException {
		String expectedMessage = "Assertions containing AuthnStatements require a Subject";
		Assertion assertion = mock(Assertion.class);
		Saml2AllowNoSubjectAssertionSpecValidator validator = new Saml2AllowNoSubjectAssertionSpecValidator();

		Statement statement = mock(Statement.class);
		List<Statement> statementList = new ArrayList<Statement>();
		statementList.add(statement);
		AuthnStatement authnStatement = mock(AuthnStatement.class);
		List<AuthnStatement> authnStatementList = new ArrayList<AuthnStatement>();
		authnStatementList.add(authnStatement);

		when(assertion.getStatements()).thenReturn(statementList);
		when(assertion.getAuthnStatements()).thenReturn(authnStatementList);

		try {
			validator.validate(assertion);
		} catch (ValidationException e) {
			assertEquals(e.getMessage(), expectedMessage);
			throw e;
		}
	}

	@Test(expected = ValidationException.class)
	public void testValidateSubject_FailOnGetAuthzDecisionStatements()
			throws ValidationException {
		String expectedMessage = "Assertions containing AuthzDecisionStatements require a Subject";
		Assertion assertion = mock(Assertion.class);
		Saml2AllowNoSubjectAssertionSpecValidator validator = new Saml2AllowNoSubjectAssertionSpecValidator();

		Statement statement = mock(Statement.class);
		List<Statement> statementList = new ArrayList<Statement>();
		statementList.add(statement);
		List<AuthnStatement> authnStatementList = new ArrayList<AuthnStatement>();
		AttributeStatement attrStatement = mock(AttributeStatement.class);
		List<AttributeStatement> attrStatementList = new ArrayList<AttributeStatement>();
		attrStatementList.add(attrStatement);

		AuthzDecisionStatement authzDecisionStatement = mock(AuthzDecisionStatement.class);
		List<AuthzDecisionStatement> authzDecisionStatementList = new ArrayList<AuthzDecisionStatement>();
		authzDecisionStatementList.add(authzDecisionStatement);

		when(assertion.getStatements()).thenReturn(statementList);
		when(assertion.getAuthnStatements()).thenReturn(authnStatementList);
		when(assertion.getAttributeStatements()).thenReturn(attrStatementList);
		when(assertion.getAuthzDecisionStatements()).thenReturn(
				authzDecisionStatementList);

		try {
			validator.validate(assertion);
		} catch (ValidationException e) {
			assertEquals(e.getMessage(), expectedMessage);
			throw e;
		}
	}

}
