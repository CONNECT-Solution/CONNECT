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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessorFileUtilities;
import gov.hhs.fha.nhinc.properties.PropertyFileDAO;
import java.io.File;
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
import org.apache.commons.collections.CollectionUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
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
import org.opensaml.saml.saml2.core.SubjectConfirmation;
import org.w3c.dom.Element;

/**
 * @author bhumphrey
 *
 */
public class HOKSAMLAssertionBuilderTest {

    private static RSAPublicKey publicKey;
    private static PrivateKey privateKey;

    private static final String PROPERTY_FILE_NAME = "mock";
    private static final String PROPERTY_FILE_LOCATION = "config";
    private static final String PROPERTY_FILE_LOCATION_WITH_FILE = "/config/mock.properties";
    private static final String PROPERTY_NAME = "propertyName";
    private static final String PROPERTY_VALUE_STRING = "CN=SAML User1%OU=SU%O=SAML User%L=New York%ST=NY%C=US";
    private static final boolean PROPERTY_VALUE_BOOLEAN = true;
    private static final long PROPERTY_VALUE_LONG = 10;

    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final PropertyFileDAO mockFileDAO = context.mock(PropertyFileDAO.class);

    @Before
    public void setMockFileDAOExpectations() throws PropertyAccessException {
        context.checking(new Expectations() {
            {
                allowing(mockFileDAO).containsPropFile(with(any(String.class)));
                will(returnValue(true));

                allowing(mockFileDAO).getProperty(with(any(String.class)), with(any(String.class)));
                will(returnValue(PROPERTY_VALUE_STRING));

                allowing(mockFileDAO).getPropertyBoolean(with(any(String.class)), with(any(String.class)));
                will(returnValue(PROPERTY_VALUE_BOOLEAN));

                allowing(mockFileDAO).getPropertyLong(with(any(String.class)), with(any(String.class)));
                will(returnValue(PROPERTY_VALUE_LONG));

                allowing(mockFileDAO).loadPropertyFile(with(any(File.class)), with(any(String.class)));

                allowing(mockFileDAO).printToLog(with(any(String.class)));
            }
        });
    }

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

            @Override
            public KeyStore refreshKeyStore() {
                return null;
            }

            @Override
            public String getKeyStoreLocation() {
                return null;
            }

            @Override
            public String getTrustStoreLocation() {
                return null;
            }

            @Override
            public KeyStore refreshTrustStore() {
                throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods,
                                                                               // choose Tools | Templates.
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
    public void testSamlConditionsNotBeforeIsNull() throws PropertyAccessException {
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
    public void testSamlConditionsNotAfterIsNull() throws PropertyAccessException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder() {
            @Override
            protected boolean isConditionsDefaultValueEnabled() {
                return false;
            }
        };
        final DateTime conditionNotBefore = new DateTime();

        when(callbackProps.getSamlConditionsNotBefore()).thenReturn(conditionNotBefore);
        when(callbackProps.getSamlConditionsNotAfter()).thenReturn(null);

        final Conditions conditions = builder.createConditions(callbackProps);
        assertNull(conditions);
    }

    @Test
    public void testSamlConditionsPropertyOffBeginInvalid() throws PropertyAccessException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder() {
            @Override
            protected boolean isConditionsDefaultValueEnabled() {
                return false;
            }
        };
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
    public void testSamlConditionsPropertyOffAfterInvalid() throws PropertyAccessException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder() {
            @Override
            protected boolean isConditionsDefaultValueEnabled() {
                return false;
            }
        };
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
    public void testSamlConditionsPropertyOnInvalidBefore() throws PropertyAccessException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder() {
            @Override
            protected boolean isConditionsDefaultValueEnabled() {
                return true;
            }
        };
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
    public void testSamlConditionsPropertyOnInvalidAfter() throws PropertyAccessException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        final HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder() {
            @Override
            protected boolean isConditionsDefaultValueEnabled() {
                return true;
            }
        };
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
        final List<Object> evidenceAccessConstent = new ArrayList<>();
        final List<Object> evidenceInstantAccessConsent = new ArrayList<>();
        evidenceAccessConstent.add("urn:oid:1.2.3.4");
        evidenceInstantAccessConsent.add("urn:oid:1.2.3.4.123456789");

        when(callbackProps.getEvidenceConditionNotBefore()).thenReturn(conditionNotBefore);
        when(callbackProps.getEvidenceConditionNotAfter()).thenReturn(conditionNotAfter);
        when(callbackProps.getAuthorizationStatementExists()).thenReturn(true);
        when(callbackProps.getEvidenceAccessConstent()).thenReturn(evidenceAccessConstent);
        when(callbackProps.getEvidenceInstantAccessConsent()).thenReturn(evidenceInstantAccessConsent);

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
    public void testEvidenceConditionsNotBeforeAndNotAfterPresent() throws PropertyAccessException {
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
        assertNull(conditions);
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
        assertNull(conditions);
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
                return "CN=SAML User,OU=connect,O=FHA,L=Melbourne,ST=FL,C=US";
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

            @Override
            public List<SAMLSubjectConfirmation> getSubjectConfirmations() {
                return null;
            }
        };
    }

    @Test
    public void testCreateAuthenticationDecisionStatementsWithoutACPorIACP() throws PropertyAccessException {
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
    public void testIssuerName() throws PropertyAccessException {
        final CallbackProperties callbackProps = mock(CallbackProperties.class);
        String sIssuer = callbackProps.getIssuer();

        // Get value from property file if certificate is null.
        PropertyAccessor propAccessor = createPropertyAccessor();
        sIssuer = propAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_NAME);
        assertEquals(PROPERTY_VALUE_STRING, sIssuer);
        sIssuer = sIssuer.replace("%", ",");
    }

    private PropertyAccessor createPropertyAccessor() {
        PropertyAccessor propAccessor = new PropertyAccessor() {
            @Override
            protected PropertyFileDAO createPropertyFileDAO() {
                return mockFileDAO;
            }

            @Override
            protected PropertyAccessorFileUtilities createPropertyAccessorFileUtilities() {
                return new PropertyAccessorFileUtilities() {
                    @Override
                    public String getPropertyFileLocation(String propertyFileName) {
                        return PROPERTY_FILE_LOCATION_WITH_FILE;
                    }
                };
            }
        };
        propAccessor.setPropertyFile(PROPERTY_FILE_NAME);

        return propAccessor;
    }

    private SAMLSubjectConfirmation createSubjectConfirmationBean(String method) {
        SAMLSubjectConfirmation subjectConfirmationBean = new SAMLSubjectConfirmation();
        subjectConfirmationBean.setAddress("localhost");
        subjectConfirmationBean.setMethod(method);
        return subjectConfirmationBean;
    }

}
