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
package gov.hhs.fha.nhinc.callback.cxf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import java.util.ArrayList;
import java.util.List;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.junit.Test;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.AuthnStatement;
import org.opensaml.saml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.Statement;
import org.opensaml.saml.saml2.core.Subject;

public class Saml2AllowNoSubjectAssertionSpecValidatorTest {

    @Test
    public void testValidateSubject() throws WSSecurityException {
        final Assertion assertion = mock(Assertion.class);
        final Saml2AllowNoSubjectAssertionSpecValidator validator = new Saml2AllowNoSubjectAssertionSpecValidator();

        final Statement statement = mock(Statement.class);
        final List<Statement> statementList = new ArrayList<>();
        statementList.add(statement);
        final AuthnStatement authnStatement = mock(AuthnStatement.class);
        final List<AuthnStatement> authnStatementList = new ArrayList<>();
        authnStatementList.add(authnStatement);
        final AttributeStatement attrStatement = mock(AttributeStatement.class);
        final List<AttributeStatement> attrStatementList = new ArrayList<>();
        attrStatementList.add(attrStatement);
        final AuthzDecisionStatement authzDecisionStatement = mock(AuthzDecisionStatement.class);
        final List<AuthzDecisionStatement> authzDecisionStatementList = new ArrayList<>();
        authzDecisionStatementList.add(authzDecisionStatement);
        final Subject subject = mock(Subject.class);
        final Issuer issuer = mock(Issuer.class);
        final NameID name = mock(NameID.class);

        when(assertion.getStatements()).thenReturn(statementList);
        when(assertion.getAuthnStatements()).thenReturn(authnStatementList);
        when(assertion.getAttributeStatements()).thenReturn(attrStatementList);
        when(assertion.getAuthzDecisionStatements()).thenReturn(authzDecisionStatementList);
        when(assertion.getSubject()).thenReturn(subject);
        when(subject.getNameID()).thenReturn(name);
        when(name.getFormat()).thenReturn(SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_X509);
        when(name.getValue()).thenReturn(SamlConstants.SAML_DEFAULT_ISSUER_NAME);
        when(assertion.getIssuer()).thenReturn(issuer);
        when(issuer.getFormat()).thenReturn(SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_X509);
        when(issuer.getValue()).thenReturn(SamlConstants.SAML_DEFAULT_ISSUER_NAME);
        validator.validateAssertion(new SamlAssertionWrapper(assertion));
        assertTrue(true);
    }

    @Test(expected = WSSecurityException.class)
    public void testValidateSubject_FailOnGetStatemtents() throws WSSecurityException {
        final String expectedMessage = "Subject is required when Statements are absent";
        final Assertion assertion = mock(Assertion.class);
        final Saml2AllowNoSubjectAssertionSpecValidator validator = new Saml2AllowNoSubjectAssertionSpecValidator();

        final List<Statement> statementList = new ArrayList<>();

        when(assertion.getStatements()).thenReturn(statementList);

        try {
            validator.validateAssertion(new SamlAssertionWrapper(assertion));
        } catch (final WSSecurityException e) {
            assertEquals(e.getMsgID(), expectedMessage);
            throw e;
        }
    }

    @Test(expected = WSSecurityException.class)
    public void testValidateSubject_FailOnGetAuthnStatements() throws WSSecurityException {
        final String expectedMessage = "Assertions containing AuthnStatements require a Subject";
        final Assertion assertion = mock(Assertion.class);
        final Saml2AllowNoSubjectAssertionSpecValidator validator = new Saml2AllowNoSubjectAssertionSpecValidator();

        final Statement statement = mock(Statement.class);
        final List<Statement> statementList = new ArrayList<>();
        statementList.add(statement);
        final AuthnStatement authnStatement = mock(AuthnStatement.class);
        final List<AuthnStatement> authnStatementList = new ArrayList<>();
        authnStatementList.add(authnStatement);

        when(assertion.getStatements()).thenReturn(statementList);
        when(assertion.getAuthnStatements()).thenReturn(authnStatementList);

        try {
            validator.validateAssertion(new SamlAssertionWrapper(assertion));
        } catch (final WSSecurityException e) {
            assertEquals(e.getMsgID(), expectedMessage);
            throw e;
        }
    }

    @Test(expected = WSSecurityException.class)
    public void testValidateSubject_FailOnGetAuthzDecisionStatements() throws WSSecurityException {
        final String expectedMessage = "Assertions containing AuthzDecisionStatements require a Subject";
        final Assertion assertion = mock(Assertion.class);
        final Saml2AllowNoSubjectAssertionSpecValidator validator = new Saml2AllowNoSubjectAssertionSpecValidator();

        final Statement statement = mock(Statement.class);
        final List<Statement> statementList = new ArrayList<>();
        statementList.add(statement);
        final List<AuthnStatement> authnStatementList = new ArrayList<>();
        final AttributeStatement attrStatement = mock(AttributeStatement.class);
        final List<AttributeStatement> attrStatementList = new ArrayList<>();
        attrStatementList.add(attrStatement);

        final AuthzDecisionStatement authzDecisionStatement = mock(AuthzDecisionStatement.class);
        final List<AuthzDecisionStatement> authzDecisionStatementList = new ArrayList<>();
        authzDecisionStatementList.add(authzDecisionStatement);

        when(assertion.getStatements()).thenReturn(statementList);
        when(assertion.getAuthnStatements()).thenReturn(authnStatementList);
        when(assertion.getAttributeStatements()).thenReturn(attrStatementList);
        when(assertion.getAuthzDecisionStatements()).thenReturn(authzDecisionStatementList);

        try {
            validator.validateAssertion(new SamlAssertionWrapper(assertion));
        } catch (final WSSecurityException e) {
            assertEquals(e.getMsgID(), expectedMessage);
            throw e;
        }
    }

}
