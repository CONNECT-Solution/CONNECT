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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.opensaml.core.xml.schema.XSAny;
import org.opensaml.saml.saml2.core.Action;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.AuthnStatement;
import org.opensaml.saml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml.saml2.core.Conditions;
import org.opensaml.saml.saml2.core.DecisionTypeEnumeration;
import org.opensaml.saml.saml2.core.Evidence;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.Subject;
import org.opensaml.saml.saml2.core.SubjectConfirmation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

/**
 * @author bhumphrey
 *
 */
public class HOKSAMLAssertionBuilderTest {

    private static RSAPublicKey publicKey;
    private static PrivateKey privateKey;

    @Before
    public void setMockFileDAOExpectations() throws NoSuchAlgorithmException {

        KeyPairGenerator keyGen;
        keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        publicKey = (RSAPublicKey) keyGen.genKeyPair().getPublic();
        privateKey = keyGen.genKeyPair().getPrivate();

    }

    private static CertificateManager setupCertManager() throws CertificateManagerException {
        CertificateManager certManager = mock(CertificateManager.class);
        X509Certificate cert = mock(X509Certificate.class);


        when(certManager.getPublicKeyBy(Mockito.any(String.class))).thenReturn(publicKey);
        when(certManager.getPrivateKeyBy(Mockito.any(String.class))).thenReturn(privateKey);
        when(certManager.getCertificateBy(Mockito.any(String.class))).thenReturn(cert);
        when(certManager.deleteCertificate(Mockito.isA(String.class))).thenReturn(false);
        when(certManager.updateCertificate(Mockito.isA(String.class), Mockito.isA(String.class),
            Mockito.isA(String.class), Mockito.isA(String.class), Mockito.isA(String.class),
            Mockito.isA(KeyStore.class))).thenReturn(false);

        when(cert.hasUnsupportedCriticalExtension()).thenReturn(false);
        when(cert.getNonCriticalExtensionOIDs()).thenReturn(Collections.EMPTY_SET);
        when(cert.getCriticalExtensionOIDs()).thenReturn(Collections.EMPTY_SET);
        when(cert.getPublicKey()).thenReturn(publicKey);

        when(cert.getSubjectX500Principal()).thenReturn(new X500Principal(SamlConstants.SAML_DEFAULT_ISSUER_NAME));
        when(cert.getIssuerX500Principal()).thenReturn(new X500Principal(SamlConstants.SAML_DEFAULT_ISSUER_NAME));
        return certManager;
    }

    @Test
    public void testBuild() throws SAMLAssertionBuilderException, CertificateManagerException {
        SAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder(setupCertManager());
        final Element assertion = builder.build(makeCallbackProperties(null), null);
        assertNotNull(assertion);
    }

    @Test
    public void testBuild_NoSubjectID() throws CertificateManagerException {
        HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder(setupCertManager());

        HashMap<String, Object> properties = getDefaultProperties();
        properties.put(SamlConstants.USER_FIRST_PROP, null);
        properties.put(SamlConstants.USER_MIDDLE_PROP, null);
        properties.put(SamlConstants.USER_LAST_PROP, null);

        try {
            builder.build(makeCallbackProperties(properties), null);
            fail("Builder does not fail when there is a missing user name / subject ID");
        } catch (SAMLAssertionBuilderException e) {
            assertEquals("No information provided to fill in Subject ID attribute.", e.getMessage());
        }

    }

    @Test
    public void testBuild_NoSubjectOrg() throws CertificateManagerException {

        SAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder(setupCertManager());

        HashMap<String, Object> properties = getDefaultProperties();
        properties.put(SamlConstants.USER_ORG_PROP, null);

        try {
            builder.build(makeCallbackProperties(properties), null);
            fail("Builder does not fail when there is a missing subject organization");
        } catch (SAMLAssertionBuilderException e) {
            assertEquals("No Organization Attribute statement provided.", e.getMessage());
        }
    }

    @Test
    public void testBuild_NoSubjectRole() throws CertificateManagerException {
        SAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder(setupCertManager());

        HashMap<String, Object> properties = getDefaultProperties();
        properties.put(SamlConstants.USER_CODE_PROP, null);
        properties.put(SamlConstants.USER_SYST_PROP, null);
        properties.put(SamlConstants.USER_SYST_NAME_PROP, null);
        properties.put(SamlConstants.USER_DISPLAY_PROP, null);

        try {
            builder.build(makeCallbackProperties(properties), null);
            fail("Builder does not fail when there is a missing subject role");
        } catch (SAMLAssertionBuilderException e) {
            assertEquals("No information provided to fill in subject role attribute.", e.getMessage());
        }
    }

    @Test
    public void testBuild_NoPurposeOfUse() throws CertificateManagerException {
        SAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder(setupCertManager());

        HashMap<String, Object> properties = getDefaultProperties();
        properties.put(SamlConstants.PURPOSE_CODE_PROP, null);
        properties.put(SamlConstants.PURPOSE_SYST_PROP, null);
        properties.put(SamlConstants.PURPOSE_SYST_NAME_PROP, null);
        properties.put(SamlConstants.PURPOSE_DISPLAY_PROP, null);

        try {
            builder.build(makeCallbackProperties(properties), null);
            fail("Builder does not fail when there is a missing purpose of use");
        } catch (SAMLAssertionBuilderException e) {
            assertEquals("No information provided to fill in Purpose For Use attribute.", e.getMessage());
        }
    }

    @Test
    public void testBuild_NoHCID() throws CertificateManagerException {
        SAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder(setupCertManager());

        HashMap<String, Object> properties = getDefaultProperties();
        properties.put(SamlConstants.HOME_COM_PROP, null);

        try {
            builder.build(makeCallbackProperties(properties), null);
            fail("Builder does not fail when there is a missing HCID");
        } catch (SAMLAssertionBuilderException e) {
            assertEquals("No Home Community ID Attribute statement provided.", e.getMessage());
        }
    }

    @Test
    public void testBuild_NoOrgID() throws CertificateManagerException {
        SAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder(setupCertManager());

        HashMap<String, Object> properties = getDefaultProperties();
        properties.put(SamlConstants.USER_ORG_ID_PROP, null);

        try {
            builder.build(makeCallbackProperties(properties), null);
            fail("Builder does not fail when there is a missing organization ID");
        } catch (SAMLAssertionBuilderException e) {
            assertEquals("No Organization ID Attribute statement provided.", e.getMessage());
        }
    }

    @Test
    public void testCreateAuthenticationStatement_NullProperty() {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder();
        when(callbackProps.getAuthenticationStatementExists()).thenReturn(true);
        try {
            builder.createAuthenicationStatements(callbackProps);
            fail("Builder does not fail when there is missing authentication");
        } catch (SAMLAssertionBuilderException e) {
            assertEquals("Assertion Authentication Statement <AuthnContext> element is null or not valid format.",
                e.getMessage());
        }
    }

    @Test
    public void testCreateAuthenticationStatementWithContext() throws SAMLAssertionBuilderException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder();
        DateTime AuthIns = new DateTime();
        when(callbackProps.getAuthenticationContextClass()).thenReturn("urn:oasis:names:tc:SAML:2.0:ac:classes:X509");
        when(callbackProps.getAuthenticationInstant()).thenReturn(AuthIns);
        when(callbackProps.getAuthenticationStatementExists()).thenReturn(true);

        final AuthnStatement authnStatement = builder.createAuthenicationStatements(callbackProps);
        assertNotNull(authnStatement);

        assertNotNull(authnStatement);
    }

    @Test
    public void testCreateAuthenticationStatementWithDateNull() {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder();

        when(callbackProps.getAuthenticationContextClass()).thenReturn("urn:oasis:names:tc:SAML:2.0:ac:classes:X509");
        when(callbackProps.getAuthenticationStatementExists()).thenReturn(true);

        try {
            builder.createAuthenicationStatements(callbackProps);
            fail("Auth statement did not throw when date provided is not provided");
        } catch (SAMLAssertionBuilderException e) {
            assertEquals("Assertion Authentication Statement <AuthnInstant> element is null.", e.getMessage());
        }
    }

    @Test
    public void testCreateAcpAttributeStatement() {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder();
        final String acp = "urn:oid:1.2.3.4";
        final String iacp = "urn:oid:1.2.3.4.5";

        when(callbackProps.getAcpAttribute()).thenReturn(acp);
        when(callbackProps.getIacpAttribute()).thenReturn(iacp);

        List<AttributeStatement> aStatement = builder.createAcpAttributeStatements(callbackProps);

        assertNotNull(aStatement);
        assertEquals(2, aStatement.size());

        boolean containsAcps = true;
        for(int i=0; i<2; i++) {
            XSAny xsValue = (XSAny) aStatement.get(i).getAttributes().get(0).getAttributeValues().get(0);
            if(!(acp.equals(xsValue.getTextContent()) || iacp.equals(xsValue.getTextContent()))) {
                containsAcps = false;
                break;
            }
        }
        assertTrue(containsAcps);
    }

    @Test
    public void testSamlConditionsNotBeforeAndNotAfterPresent() {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder() {
            @Override
            protected boolean isConditionsDefaultValueEnabled() {
                return true;
            }
        };
        final DateTime conditionNotBefore = new DateTime();
        final DateTime conditionNotAfter = new DateTime();

        when(callbackProps.getSamlConditionsNotBefore()).thenReturn(conditionNotBefore);
        when(callbackProps.getSamlConditionsNotAfter()).thenReturn(conditionNotAfter);

        final Conditions conditions = builder.createConditions(callbackProps);
        assertNotNull(conditions);
        assertTrue(conditionNotBefore.isBefore(conditions.getNotOnOrAfter()));
    }

    @Test
    public void testSamlConditionsNotBeforeIsNull() {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder() {
            @Override
            protected boolean isConditionsDefaultValueEnabled() {
                return false;
            }
        };

        final DateTime conditionNotAfter = new DateTime();

        when(callbackProps.getSamlConditionsNotAfter()).thenReturn(conditionNotAfter);
        when(callbackProps.getSamlConditionsNotBefore()).thenReturn(null);

        final Conditions conditions = builder.createConditions(callbackProps);
        assertNull(conditions);
    }

    @Test
    public void testSamlConditionsNotAfterIsNull() {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = getNonDefaultConditionBuilder();
        final DateTime conditionNotBefore = new DateTime();

        when(callbackProps.getSamlConditionsNotBefore()).thenReturn(conditionNotBefore);
        when(callbackProps.getSamlConditionsNotAfter()).thenReturn(null);

        final Conditions conditions = builder.createConditions(callbackProps);
        assertNull(conditions);
    }

    @Test
    public void testSamlConditionsPropertyOffBeginInvalid() {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = getNonDefaultConditionBuilder();
        final DateTime now = new DateTime();
        final DateTime conditionNotBefore = now.plusYears(2);
        final DateTime conditionNotAfter = now.plusYears(3);

        when(callbackProps.getSamlConditionsNotBefore()).thenReturn(conditionNotBefore);
        when(callbackProps.getSamlConditionsNotAfter()).thenReturn(conditionNotAfter);

        final Conditions conditions = builder.createConditions(callbackProps);
        assertNotNull(conditions);
        assertEquals(conditions.getNotBefore().getMillis(), conditionNotBefore.getMillis());
        assertEquals(conditions.getNotOnOrAfter().getMillis(), conditionNotAfter.getMillis());
    }

    @Test
    public void testSamlConditionsPropertyOffAfterInvalid() {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = getNonDefaultConditionBuilder();
        final DateTime now = new DateTime();
        final DateTime conditionNotBefore = now.minusYears(3);
        final DateTime conditionNotAfter = now.minusYears(2);

        when(callbackProps.getSamlConditionsNotBefore()).thenReturn(conditionNotBefore);
        when(callbackProps.getSamlConditionsNotAfter()).thenReturn(conditionNotAfter);

        final Conditions conditions = builder.createConditions(callbackProps);
        assertNotNull(conditions);
        assertEquals(conditions.getNotBefore().getMillis(), conditionNotBefore.getMillis());
        assertEquals(conditions.getNotOnOrAfter().getMillis(), conditionNotAfter.getMillis());
    }

    @Test
    public void testSamlConditionsPropertyOnInvalidBefore() {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = getDefaultConditionBuilder();
        final DateTime now = new DateTime();
        final DateTime conditionNotBefore = now.plusYears(2);
        final DateTime conditionNotAfter = now.plusYears(3);

        when(callbackProps.getSamlConditionsNotBefore()).thenReturn(conditionNotBefore);
        when(callbackProps.getSamlConditionsNotAfter()).thenReturn(conditionNotAfter);

        final Conditions conditions = builder.createConditions(callbackProps);
        assertNotNull(conditions);
        assertNotEquals(conditions.getNotBefore().getMillis(), conditionNotBefore.getMillis());
    }

    @Test
    public void testSamlConditionsPropertyOnInvalidAfter() {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = getDefaultConditionBuilder();
        final DateTime now = new DateTime();
        final DateTime conditionNotBefore = now.minusYears(3);
        final DateTime conditionNotAfter = now.minusYears(2);

        when(callbackProps.getSamlConditionsNotBefore()).thenReturn(conditionNotBefore);
        when(callbackProps.getSamlConditionsNotAfter()).thenReturn(conditionNotAfter);

        final Conditions conditions = builder.createConditions(callbackProps);
        assertNotNull(conditions);
        assertNotEquals(conditions.getNotOnOrAfter().getMillis(), conditionNotAfter.getMillis());
    }

    @Test
    public void testBuildEvidence() {
        Map<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put(SamlConstants.EVIDENCE_ID_PROP, "_45678fdgrt543sweqt");
        CallbackProperties properties = new CallbackMapProperties(propertiesMap);
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder();
        final Subject subject = mock(Subject.class);
        final AttributeStatement e = mock(AttributeStatement.class);
        final List<AttributeStatement> statements = new ArrayList<>();
        statements.add(0, e);
        final Evidence evidence1 = builder.buildEvidence(properties, statements, subject);
        assertTrue(evidence1.getAssertions().get(0).getID().startsWith("_"));
    }

    @Test
    public void testCreateAuthenticationDecisionStatements() throws SAMLAssertionBuilderException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final Subject subject = mock(Subject.class);
        final DateTime beforeCreation = new DateTime();
        final DateTime conditionNotBefore = new DateTime();
        final DateTime conditionNotAfter = conditionNotBefore.plusMinutes(5);
        final List<Object> evidenceAccessConstent = new ArrayList<>();
        final List<Object> evidenceInstantAccessConsent = new ArrayList<>();
        evidenceAccessConstent.add("urn:oid:1.2.3.4");
        evidenceInstantAccessConsent.add("urn:oid:1.2.3.4.123456789");

        when(callbackProps.getEvidenceConditionNotBefore()).thenReturn(conditionNotBefore);
        when(callbackProps.getEvidenceConditionNotAfter()).thenReturn(conditionNotAfter);
        when(callbackProps.getAuthorizationStatementExists()).thenReturn(true);
        when(callbackProps.getEvidenceAccessConstent()).thenReturn(evidenceAccessConstent);
        when(callbackProps.getEvidenceInstantAccessConsent()).thenReturn(evidenceInstantAccessConsent);

        final AuthzDecisionStatement statement = getNonDefaultConditionBuilder()
            .createAuthorizationDecisionStatement(callbackProps, subject);

        assertNotNull(statement);
        assertEquals(DecisionTypeEnumeration.PERMIT, statement.getDecision());

        final Action action = statement.getActions().get(0);
        assertEquals(SAMLAssertionBuilder.AUTHZ_DECISION_ACTION_EXECUTE, action.getAction());

        final Evidence evidence = statement.getEvidence();
        final Assertion assertion = evidence.getAssertions().get(0);
        assertTrue(assertion.getID().startsWith("_"));

        assertTrue(beforeCreation.isBefore(assertion.getIssueInstant())
            || beforeCreation.isEqual(assertion.getIssueInstant()));

        final Issuer issuer = assertion.getIssuer();
        assertEquals(SAMLAssertionBuilder.X509_NAME_ID, issuer.getFormat());

        final Conditions conditions = assertion.getConditions();

        assertEquals(conditions.getNotBefore(), conditionNotBefore.withZone(DateTimeZone.UTC));
        assertEquals(conditions.getNotOnOrAfter(), conditionNotAfter.withZone(DateTimeZone.UTC));

        final List<AttributeStatement> attributeStatement = assertion.getAttributeStatements();
        assertEquals(2, attributeStatement.get(0).getAttributes().size());

        final Attribute firstAttribute = attributeStatement.get(0).getAttributes().get(0);
        final Attribute secondAttribute = attributeStatement.get(0).getAttributes().get(1);
        assertEquals("AccessConsentPolicy", firstAttribute.getName());
        assertEquals("InstanceAccessConsentPolicy", secondAttribute.getName());
    }

    @Test
    public void testEvidenceConditionsNotBeforeAndNotAfterPresent()
        throws PropertyAccessException, SAMLAssertionBuilderException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final Subject subject = mock(Subject.class);
        final DateTime conditionNotBefore = new DateTime();
        final DateTime conditionNotAfter = new DateTime();
        final PropertyAccessor propertyAccessor = mock(PropertyAccessor.class);
        when(propertyAccessor.getProperty(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Boolean.TRUE.toString());

        when(callbackProps.getAuthorizationStatementExists()).thenReturn(true);
        when(callbackProps.getEvidenceConditionNotBefore()).thenReturn(conditionNotBefore);
        when(callbackProps.getEvidenceConditionNotAfter()).thenReturn(conditionNotAfter);

        final AuthzDecisionStatement statement = getNonDefaultConditionBuilder()
            .createAuthorizationDecisionStatement(callbackProps, subject);

        assertNotNull(statement);

        final Evidence evidence = statement.getEvidence();
        final Assertion assertion = evidence.getAssertions().get(0);

        final Conditions conditions = assertion.getConditions();
        assertEquals(conditions.getNotBefore(), conditionNotBefore.withZone(DateTimeZone.UTC));
        assertEquals(conditions.getNotOnOrAfter(), conditionNotAfter.withZone(DateTimeZone.UTC));

    }

    @Test
    public void testEvidanceConditionsNotBeforeIsNull() throws PropertyAccessException, SAMLAssertionBuilderException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final Subject subject = mock(Subject.class);
        final DateTime conditionNotAfter = new DateTime();
        final PropertyAccessor propertyAccessor = mock(PropertyAccessor.class);
        when(propertyAccessor.getProperty(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Boolean.FALSE.toString());

        when(callbackProps.getAuthorizationStatementExists()).thenReturn(true);
        when(callbackProps.getEvidenceConditionNotAfter()).thenReturn(conditionNotAfter);
        when(callbackProps.getEvidenceConditionNotBefore()).thenReturn(null);
        final AuthzDecisionStatement statement = getNonDefaultConditionBuilder()
            .createAuthorizationDecisionStatement(callbackProps, subject);

        assertNotNull(statement);

        final Evidence evidence = statement.getEvidence();
        final Assertion assertion = evidence.getAssertions().get(0);

        final Conditions conditions = assertion.getConditions();
        assertNull(conditions);
    }

    @Test
    public void testEvidanceConditionsNotAfterIsNull() throws PropertyAccessException, SAMLAssertionBuilderException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final Subject subject = mock(Subject.class);
        final DateTime conditionNotBefore = new DateTime();
        final PropertyAccessor propertyAccessor = mock(PropertyAccessor.class);
        when(propertyAccessor.getProperty(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Boolean.FALSE.toString());

        when(callbackProps.getAuthorizationStatementExists()).thenReturn(true);
        when(callbackProps.getEvidenceConditionNotBefore()).thenReturn(conditionNotBefore);
        when(callbackProps.getEvidenceConditionNotAfter()).thenReturn(null);

        final AuthzDecisionStatement statement = getNonDefaultConditionBuilder()
            .createAuthorizationDecisionStatement(callbackProps, subject);

        assertNotNull(statement);

        final Evidence evidence = statement.getEvidence();
        final Assertion assertion = evidence.getAssertions().get(0);

        final Conditions conditions = assertion.getConditions();
        assertNull(conditions);
    }

    // Since this project is heavily focused on statics/singletons, and not using Spring's wiring, we have to override
    // the method. Alternatively, we could use PowerMock with Mockito to mock the static calls, but that's overkill.
    private static HOKSAMLAssertionBuilder getNonDefaultConditionBuilder() {
        // return isConditionsDefaultValueEnabled flag to false
        return new HOKSAMLAssertionBuilder() {
            @Override
            protected boolean isConditionsDefaultValueEnabled() {
                return false;
            }
        };
    }
    private static HOKSAMLAssertionBuilder getDefaultConditionBuilder() {
        // return isConditionsDefaultValueEnabled flag to true
        return new HOKSAMLAssertionBuilder() {
            @Override
            protected boolean isConditionsDefaultValueEnabled() {
                return true;
            }
        };
    }

    private static HashMap<String, Object> getDefaultProperties() {

        DateTime date = new DateTime();

        HashMap<String, Object> values = new HashMap<String, Object>();
        values.put(SamlConstants.USER_NAME_PROP, "userName");
        values.put(SamlConstants.USER_SYST_NAME_PROP, "sytemName");
        values.put(SamlConstants.USER_SYST_PROP, "userSystem");
        values.put(SamlConstants.USER_ORG_PROP, "userOrg");
        values.put(SamlConstants.USER_ORG_ID_PROP, "userOrgId");
        values.put(SamlConstants.USER_FIRST_PROP, "First");
        values.put(SamlConstants.USER_MIDDLE_PROP, "Mid");
        values.put(SamlConstants.USER_LAST_PROP, "Last");
        values.put(SamlConstants.USER_DISPLAY_PROP, "display");
        values.put(SamlConstants.USER_CODE_PROP, "userCode");
        values.put(SamlConstants.SUBJECT_LOCALITY_ADDR_PROP, "locality");
        values.put(SamlConstants.SUBJECT_LOCALITY_DNS_PROP, "dns");
        values.put(SamlConstants.PURPOSE_SYST_NAME_PROP, "purposeSystemName");
        values.put(SamlConstants.PURPOSE_SYST_PROP, "purposeSystem");
        values.put(SamlConstants.PURPOSE_DISPLAY_PROP, "purposeDisplay");
        values.put(SamlConstants.PURPOSE_CODE_PROP, "purposecode");
        values.put(SamlConstants.PATIENT_ID_PROP, "patientId");
        values.put(SamlConstants.ASSERTION_ISSUER_PROP, "CN=SAML User,OU=connect,O=FHA,L=Melbourne,ST=FL,C=US");
        values.put(SamlConstants.HOME_COM_PROP, "hcid");
        values.put(SamlConstants.SAMLCONDITIONS_NOT_BEFORE_PROP, date);
        values.put(SamlConstants.SAMLCONDITIONS_NOT_AFTER_PROP, date);
        values.put(SamlConstants.EVIDENCE_ISSUER_FORMAT_PROP, "format");
        values.put(SamlConstants.EVIDENCE_ISSUER_PROP, "issuer");
        values.put(SamlConstants.EVIDENCE_SUBJECT_PROP, "evidenceSubject");
        values.put(SamlConstants.EVIDENCE_INSTANT_PROP, date);
        values.put(SamlConstants.EVIDENCE_INST_ACCESS_CONSENT_PROP, Collections.EMPTY_LIST);
        values.put(SamlConstants.EVIDENCE_ID_PROP, "evidence id");
        values.put(SamlConstants.EVIDENCE_CONDITION_NOT_BEFORE_PROP, date);
        values.put(SamlConstants.EVIDENCE_CONDITION_NOT_AFTER_PROP, date);
        values.put(SamlConstants.EVIDENCE_ACCESS_CONSENT_PROP, Collections.EMPTY_LIST);
        values.put(SamlConstants.RESOURCE_PROP, "resource");
        values.put(SamlConstants.AUTHZ_STATEMENT_EXISTS_PROP, false);
        values.put(SamlConstants.AUTHN_STATEMENT_EXISTS_PROP, false);
        values.put(SamlConstants.AUTHN_SESSION_INDEX_PROP, "1");
        values.put(SamlConstants.AUTHN_INSTANT_PROP, date);
        values.put(SamlConstants.AUTHN_CONTEXT_CLASS_PROP, "contextClass");
        values.put(SamlConstants.ASSERTION_ISSUER_FORMAT_PROP, "issueFormat");
        values.put(NhincConstants.WS_SOAP_TARGET_HOME_COMMUNITY_ID, "targetHCID");
        values.put(NhincConstants.SERVICE_NAME, "serviceName");
        values.put(SamlConstants.ACTION_PROP, "action");
        values.put(SamlConstants.TARGET_API_LEVEL, GATEWAY_API_LEVEL.LEVEL_g1);
        values.put(SamlConstants.ATTRIBUTE_NAME_NPI, "npi");
        values.put(SamlConstants.ACP_ATTRIBUTE_PROP, "urn:oid:1.2.3.4");
        values.put(SamlConstants.IACP_ATTRIBUTE_PROP, "urn:oid:1.2.3.4.5");

        return values;
    }

    private CallbackProperties makeCallbackProperties(HashMap<String, Object> overwriteValues) {
        HashMap<String, Object> values = overwriteValues;
        if (values == null) {
            values = getDefaultProperties();
        }

        return new CallbackMapProperties(values);
    }

    @Test
    public void testCreateAuthenticationDecisionStatementsWithoutACPorIACP() throws SAMLAssertionBuilderException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final Subject subject = mock(Subject.class);
        final DateTime beforeCreation = new DateTime();
        final DateTime conditionNotBefore = new DateTime();
        final DateTime conditionNotAfter = conditionNotBefore.plusMinutes(5);

        when(callbackProps.getEvidenceConditionNotBefore()).thenReturn(conditionNotBefore);
        when(callbackProps.getEvidenceConditionNotAfter()).thenReturn(conditionNotAfter);
        when(callbackProps.getAuthorizationStatementExists()).thenReturn(true);

        final AuthzDecisionStatement statement = getNonDefaultConditionBuilder()
            .createAuthorizationDecisionStatement(callbackProps, subject);

        assertNotNull(statement);
        assertEquals(DecisionTypeEnumeration.PERMIT, statement.getDecision());

        final Action action = statement.getActions().get(0);
        assertEquals(SAMLAssertionBuilder.AUTHZ_DECISION_ACTION_EXECUTE, action.getAction());

        final Evidence evidence = statement.getEvidence();
        final Assertion assertion = evidence.getAssertions().get(0);
        assertTrue(assertion.getID().startsWith("_"));

        assertTrue(beforeCreation.isBefore(assertion.getIssueInstant())
            || beforeCreation.isEqual(assertion.getIssueInstant()));

        final Issuer issuer = assertion.getIssuer();
        assertEquals(SAMLAssertionBuilder.X509_NAME_ID, issuer.getFormat());

        final Conditions conditions = assertion.getConditions();
        assertEquals(conditions.getNotBefore(), conditionNotBefore.withZone(DateTimeZone.UTC));
        assertEquals(conditions.getNotOnOrAfter(), conditionNotAfter.withZone(DateTimeZone.UTC));

        final List<AuthzDecisionStatement> authzDecisionStatements = assertion.getAuthzDecisionStatements();
        assertTrue(CollectionUtils.isEmpty(authzDecisionStatements));

        final List<AttributeStatement> attributeStatement = assertion.getAttributeStatements();
        assertTrue(CollectionUtils.isEmpty(attributeStatement));
    }

    @Test
    public void testCreateMultipleSubjectConfirmation() throws SAMLComponentBuilderException {
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder();
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        X509Certificate certificate = mock(X509Certificate.class);
        // Create subject bear
        List<SAMLSubjectConfirmation> samlSubjectConfirmations = new ArrayList<>();
        samlSubjectConfirmations.add(createSubjectConfirmationBean(SubjectConfirmation.METHOD_BEARER));
        // Create sender-vouches bean
        samlSubjectConfirmations.add(createSubjectConfirmationBean(SubjectConfirmation.METHOD_SENDER_VOUCHES));

        when(callbackProps.getUsername()).thenReturn("junittest");
        when(callbackProps.getSubjectConfirmations()).thenReturn(samlSubjectConfirmations);

        Subject subject = builder.createSubject(callbackProps, certificate, publicKey);

        List<SubjectConfirmation> subjectConfirmations = subject.getSubjectConfirmations();
        assertTrue(subjectConfirmations.size() == 3);
    }

    @Test
    public void testBuildIssuer_DefaultNulled() throws CertificateManagerException {
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder(setupCertManager());
        HashMap<String, Object> props = getDefaultProperties();
        props.put(SamlConstants.ASSERTION_ISSUER_FORMAT_PROP, "");
        props.put(SamlConstants.ASSERTION_ISSUER_PROP, "");

        final CallbackProperties callbackProps = makeCallbackProperties(props);

        Element element = builder.build(callbackProps, null);

        Node issuer = getIssuerNode(element.getChildNodes());
        String format = issuer.getAttributes().getNamedItem("Format").getNodeValue();
        String issuerName = issuer.getFirstChild().getNodeValue();
        assertEquals(SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_X509, format);
        assertEquals(SamlConstants.SAML_DEFAULT_ISSUER_NAME, issuerName);
    }

    @Test
    public void testBuildIssuer_X509Cert() throws CertificateManagerException {
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder(setupCertManager());

        String issuer = "CN=SAMLTest User,OU=SU,O=SAML Test User,L=Los Angeles,ST=CA,C=US";

        HashMap<String, Object> props = getDefaultProperties();
        props.put(SamlConstants.ASSERTION_ISSUER_FORMAT_PROP, SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_X509);
        props.put(SamlConstants.ASSERTION_ISSUER_PROP, issuer);

        final CallbackProperties callbackProps = makeCallbackProperties(props);

        Element element = builder.build(callbackProps, null);

        Node issuerNode = getIssuerNode(element.getChildNodes());
        String format = issuerNode.getAttributes().getNamedItem("Format").getNodeValue();
        String issuerName = issuerNode.getFirstChild().getNodeValue();
        assertEquals(SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_X509, format);
        assertEquals(issuer, issuerName);
    }

    @Test
    public void testBuildIssuer_DomainName() throws CertificateManagerException {
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder(setupCertManager());
        HashMap<String, Object> props = getDefaultProperties();
        props.put(SamlConstants.ASSERTION_ISSUER_FORMAT_PROP, SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_WINDOWS_NAME);
        props.put(SamlConstants.ASSERTION_ISSUER_PROP, "CONNECT\\plobre");

        final CallbackProperties callbackProps = makeCallbackProperties(props);

        Element element = builder.build(callbackProps, null);

        Node issuer = getIssuerNode(element.getChildNodes());
        String format = issuer.getAttributes().getNamedItem("Format").getNodeValue();
        String issuerName = issuer.getFirstChild().getNodeValue();
        assertEquals(SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_WINDOWS_NAME, format);
        assertEquals("CONNECT\\plobre", issuerName);
    }

    @Test
    public void testBuildIssuer_Email() throws CertificateManagerException {
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder(setupCertManager());
        HashMap<String, Object> props = getDefaultProperties();
        props.put(SamlConstants.ASSERTION_ISSUER_FORMAT_PROP, SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_EMAIL_ADDRESS);
        props.put(SamlConstants.ASSERTION_ISSUER_PROP, "connect@connect.net");

        final CallbackProperties callbackProps = makeCallbackProperties(props);

        Element element = builder.build(callbackProps, null);
        Node issuer = getIssuerNode(element.getChildNodes());
        String format = issuer.getAttributes().getNamedItem("Format").getNodeValue();
        String issuerName = issuer.getFirstChild().getNodeValue();
        assertEquals(SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_EMAIL_ADDRESS, format);
        assertEquals("connect@connect.net", issuerName);
    }

    private static Node getIssuerNode(NodeList list) {
        for ( int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeName().equals("saml2:Issuer")) {
                return node;
            }
        }
        return null;

    }

    private static SAMLSubjectConfirmation createSubjectConfirmationBean(String method) {
        SAMLSubjectConfirmation subjectConfirmationBean = new SAMLSubjectConfirmation();
        subjectConfirmationBean.setAddress("localhost");
        subjectConfirmationBean.setMethod(method);
        return subjectConfirmationBean;
    }

    // Small helper method to view the XML when testing in debug.
    public String getRawXML(Element element) {
        Document document = element.getOwnerDocument();
        DOMImplementationLS domImplLS = (DOMImplementationLS) document.getImplementation();
        LSSerializer serializer = domImplLS.createLSSerializer();
        String str = serializer.writeToString(element);
        return str;
    }

}
