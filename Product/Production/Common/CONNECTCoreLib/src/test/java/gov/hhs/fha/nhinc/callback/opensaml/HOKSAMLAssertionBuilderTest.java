/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
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

        KeyPairGenerator keyGen;
        keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        publicKey = (RSAPublicKey) keyGen.genKeyPair().getPublic();
        privateKey = keyGen.genKeyPair().getPrivate();

    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testBuild() throws Exception {
        final SAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder(new CertificateManager() {
            @Override
            public RSAPublicKey getDefaultPublicKey() {
                return publicKey;
            }

            @Override
            public PrivateKey getDefaultPrivateKey() throws CertificateManagerException {
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
            public X509Certificate getDefaultCertificate() throws CertificateManagerException {
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
                    public byte[] getExtensionValue(final String oid) {
                        return new byte[1];
                    }

                    @Override
                    public Set<String> getCriticalExtensionOIDs() {
                        return Collections.EMPTY_SET;
                    }

                    @Override
                    public void verify(final PublicKey key, final String sigProvider) throws CertificateException,
                    NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
                    }

                    @Override
                    public void verify(final PublicKey key) throws CertificateException, NoSuchAlgorithmException,
                    InvalidKeyException, NoSuchProviderException, SignatureException {
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
                    public byte[] getEncoded() throws CertificateEncodingException {
                        return new byte[1];
                    }

                    @Override
                    public int getVersion() {
                        return 0;
                    }

                    @Override
                    public byte[] getTBSCertificate() throws CertificateEncodingException {
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
                    public void checkValidity(final Date date)
                        throws CertificateExpiredException, CertificateNotYetValidException {
                    }

                    @Override
                    public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
                    }
                };

            }
        });
        final Element assertion = builder.build(getProperties());
        assertNotNull(assertion);
    }

    @Test
    public void testCreateAuthenticationStatement() {
        final List<AuthnStatement> authnStatement = new HOKSAMLAssertionBuilder()
            .createAuthenicationStatements(getProperties());
        assertNotNull(authnStatement);

        assertFalse(authnStatement.isEmpty());
    }

    @Test
    public void testSamlConditionsNotBeforeAndNotAfterPresent() throws PropertyAccessException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder();
        final Subject subject = mock(Subject.class);
        final DateTime conditionNotBefore = new DateTime();
        final DateTime conditionNotAfter = new DateTime();
        final PropertyAccessor propertyAccessor = mock(PropertyAccessor.class);
        when(propertyAccessor.getProperty(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Boolean.TRUE.toString());

        when(callbackProps.getSamlConditionsNotBefore()).thenReturn(conditionNotBefore);
        when(callbackProps.getSamlConditionsNotAfter()).thenReturn(conditionNotAfter);

        final Conditions conditions = builder.createConditions(callbackProps);
        assertNotNull(conditions);
        assertTrue(conditionNotBefore.isBefore(conditions.getNotOnOrAfter()));

    }

    @Test
    public void testSamlConditionsNotBeforeIsNull() throws PropertyAccessException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder();
        final Subject subject = mock(Subject.class);
        final DateTime conditionNotAfter = new DateTime();
        final PropertyAccessor propertyAccessor = mock(PropertyAccessor.class);
        when(propertyAccessor.getProperty(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Boolean.FALSE.toString());

        when(callbackProps.getSamlConditionsNotAfter()).thenReturn(conditionNotAfter);
        when(callbackProps.getSamlConditionsNotBefore()).thenReturn(null);

        final Conditions conditions = builder.createConditions(callbackProps);
        assertNotNull(conditions);
    }

    @Test
    public void testSamlConditionsNotAfterIsNull() throws PropertyAccessException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder();
        final Subject subject = mock(Subject.class);
        final DateTime conditionNotBefore = new DateTime();
        final PropertyAccessor propertyAccessor = mock(PropertyAccessor.class);
        when(propertyAccessor.getProperty(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Boolean.FALSE.toString());

        when(callbackProps.getSamlConditionsNotBefore()).thenReturn(conditionNotBefore);
        when(callbackProps.getSamlConditionsNotAfter()).thenReturn(null);

        final Conditions conditions = builder.createConditions(callbackProps);
        assertNotNull(conditions);
    }

    @Test
    public void testBuildEvidence() {
        Map<Object, Object> propertiesMap = new HashMap<Object, Object>();
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
    public void testCreateAuthenticationDecisionStatements() throws PropertyAccessException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final Subject subject = mock(Subject.class);
        final DateTime beforeCreation = new DateTime();
        final DateTime conditionNotBefore = new DateTime();
        final DateTime conditionNotAfter = conditionNotBefore.plusMinutes(5);

        when(callbackProps.getEvidenceConditionNotBefore()).thenReturn(conditionNotBefore);
        when(callbackProps.getEvidenceConditionNotAfter()).thenReturn(conditionNotAfter);
        when(callbackProps.getAuthorizationStatementExists()).thenReturn(true);

        final List<AuthzDecisionStatement> statementList = getHOKSAMLAssertionBuilder()
            .createAuthorizationDecisionStatements(callbackProps, subject);

        assertFalse(statementList.isEmpty());
        final AuthzDecisionStatement statement = statementList.get(0);
        assertEquals(statement.getDecision(), DecisionTypeEnumeration.PERMIT);

        final Action action = statement.getActions().get(0);
        assertEquals(action.getAction(), SAMLAssertionBuilder.AUTHZ_DECISION_ACTION_EXECUTE);

        final Evidence evidence = statement.getEvidence();
        final Assertion assertion = evidence.getAssertions().get(0);
        assertTrue(assertion.getID().startsWith("_"));

        assertTrue(beforeCreation.isBefore(assertion.getIssueInstant())
            || beforeCreation.isEqual(assertion.getIssueInstant()));

        final Issuer issuer = assertion.getIssuer();
        assertEquals(issuer.getFormat(), SAMLAssertionBuilder.X509_NAME_ID);

        final Conditions conditions = assertion.getConditions();
        assertEquals(conditions.getNotBefore(), conditionNotBefore.withZone(DateTimeZone.UTC));
        assertEquals(conditions.getNotOnOrAfter(), conditionNotAfter.withZone(DateTimeZone.UTC));

        final List<AttributeStatement> attributeStatement = assertion.getAttributeStatements();
        assertEquals(attributeStatement.get(0).getAttributes().size(), 2);

        final Attribute firstAttribute = attributeStatement.get(0).getAttributes().get(0);
        final Attribute secondAttribute = attributeStatement.get(0).getAttributes().get(1);
        assertEquals(firstAttribute.getName(), "AccessConsentPolicy");
        assertEquals(secondAttribute.getName(), "InstanceAccessConsentPolicy");
    }

    @Test
    public void testEvidanceConditionsNotBeforeAndNotAfterPresent() throws PropertyAccessException {
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

        final List<AuthzDecisionStatement> statementList = getHOKSAMLAssertionBuilder()
            .createAuthorizationDecisionStatements(callbackProps, subject);

        assertFalse(statementList.isEmpty());
        final AuthzDecisionStatement statement = statementList.get(0);

        final Evidence evidence = statement.getEvidence();
        final Assertion assertion = evidence.getAssertions().get(0);

        final Conditions conditions = assertion.getConditions();
        assertEquals(conditions.getNotBefore(), conditionNotBefore.withZone(DateTimeZone.UTC));
        assertEquals(conditions.getNotOnOrAfter(), conditionNotAfter.withZone(DateTimeZone.UTC));

    }

    @Test
    public void testEvidanceConditionsNotBeforeIsNull() throws PropertyAccessException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final Subject subject = mock(Subject.class);
        final DateTime conditionNotAfter = new DateTime();
        final PropertyAccessor propertyAccessor = mock(PropertyAccessor.class);
        when(propertyAccessor.getProperty(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Boolean.FALSE.toString());

        when(callbackProps.getAuthorizationStatementExists()).thenReturn(true);
        when(callbackProps.getEvidenceConditionNotAfter()).thenReturn(conditionNotAfter);
        when(callbackProps.getEvidenceConditionNotBefore()).thenReturn(null);
        final List<AuthzDecisionStatement> statementList = getHOKSAMLAssertionBuilder()
            .createAuthorizationDecisionStatements(callbackProps, subject);

        assertFalse(statementList.isEmpty());
        final AuthzDecisionStatement statement = statementList.get(0);

        final Evidence evidence = statement.getEvidence();
        final Assertion assertion = evidence.getAssertions().get(0);

        final Conditions conditions = assertion.getConditions();
        assertNotNull(conditions);
    }

    @Test
    public void testEvidanceConditionsNotAfterIsNull() throws PropertyAccessException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final Subject subject = mock(Subject.class);
        final DateTime conditionNotBefore = new DateTime();
        final PropertyAccessor propertyAccessor = mock(PropertyAccessor.class);
        when(propertyAccessor.getProperty(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Boolean.FALSE.toString());

        when(callbackProps.getAuthorizationStatementExists()).thenReturn(true);
        when(callbackProps.getEvidenceConditionNotBefore()).thenReturn(conditionNotBefore);
        when(callbackProps.getEvidenceConditionNotAfter()).thenReturn(null);

        final List<AuthzDecisionStatement> statementList = getHOKSAMLAssertionBuilder()
            .createAuthorizationDecisionStatements(callbackProps, subject);

        assertFalse(statementList.isEmpty());
        final AuthzDecisionStatement statement = statementList.get(0);

        final Evidence evidence = statement.getEvidence();
        final Assertion assertion = evidence.getAssertions().get(0);

        final Conditions conditions = assertion.getConditions();
        assertNotNull(conditions);
    }

    HOKSAMLAssertionBuilder getHOKSAMLAssertionBuilder() {
        // return isConditionsDefaultValueEnabled flag to false
        return new HOKSAMLAssertionBuilder() {
            @Override
            protected boolean isConditionsDefaultValueEnabled() {
                return false;
            }
        };
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
            public DateTime getSamlConditionsNotBefore() {
                return new DateTime();
            }

            @Override
            public DateTime getSamlConditionsNotAfter() {
                return new DateTime();
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
            public String getAuthorizationResource() {
                return "resource";
            }

            @Override
            public Boolean getAuthorizationStatementExists() {
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
            public String getAuthorizationDecision() {
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

            @Override
            public String getNPI() {
                return "npi";
            }

            @Override
            public String getUserOrganizationId() {
                return "orgId";
            }
        };
    }
}
