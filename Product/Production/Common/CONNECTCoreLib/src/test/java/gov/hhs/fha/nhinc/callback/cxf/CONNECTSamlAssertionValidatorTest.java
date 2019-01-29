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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.callback.opensaml.OpenSAML2ComponentBuilder;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.xml.BasicParserPool;
import net.shibboleth.utilities.java.support.xml.XMLParserException;
import org.apache.wss4j.common.crypto.Crypto;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.saml.SAMLKeyInfo;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.apache.wss4j.common.saml.builder.SAML2ComponentBuilder;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.validate.Credential;
import org.joda.time.DateTime;
import org.junit.Test;
import org.opensaml.core.xml.XMLObjectBuilderFactory;
import org.opensaml.core.xml.config.XMLConfigurationException;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.UnmarshallingException;
import org.opensaml.core.xml.schema.impl.XSAnyImpl;
import org.opensaml.saml.common.SAMLObjectBuilder;
import org.opensaml.saml.common.SAMLObjectContentReference;
import org.opensaml.saml.common.SAMLVersion;
import org.opensaml.saml.saml1.core.Statement;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.Subject;
import org.opensaml.xmlsec.signature.Signature;
import org.opensaml.xmlsec.signature.impl.SignatureBuilder;
import org.opensaml.xmlsec.signature.support.ContentReference;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CONNECTSamlAssertionValidatorTest {

    private static final Set<String> VALIDATED_ATTRIBUTES = new HashSet<>(
        Arrays.asList(SamlConstants.ATTRIBUTE_NAME_SUBJECT_ID_XSPA, SamlConstants.ATTRIBUTE_NAME_ORG,
            SamlConstants.ATTRIBUTE_NAME_ORG_ID, SamlConstants.ATTRIBUTE_NAME_HCID,
            SamlConstants.ATTRIBUTE_NAME_SUBJECT_ROLE, SamlConstants.ATTRIBUTE_NAME_PURPOSE_OF_USE));

    @Test
    public void testValidateAssertionSaml1() throws WSSecurityException {
        final org.opensaml.saml.saml1.core.Assertion saml1Assertion = mock(org.opensaml.saml.saml1.core.Assertion.class);
        final SamlAssertionWrapper assertionWrapper = new SamlAssertionWrapper(saml1Assertion);
        final SamlAssertionWrapper assertion = spy(assertionWrapper);
        final QName assertionQName = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "Assertion", "saml1");

        when(saml1Assertion.getElementQName()).thenReturn(assertionQName);
        when(saml1Assertion.getMajorVersion()).thenReturn(1);
        when(saml1Assertion.getMinorVersion()).thenReturn(0);
        when(saml1Assertion.getID()).thenReturn("Assertion_ID");
        when(saml1Assertion.getIssuer()).thenReturn("Issuer");
        final DateTime dateTime = new DateTime();
        when(saml1Assertion.getIssueInstant()).thenReturn(dateTime);
        final Statement statement = mock(Statement.class);
        final List<Statement> statementList = new ArrayList<>();
        statementList.add(statement);
        when(saml1Assertion.getStatements()).thenReturn(statementList);
        final Signature signature = mock(Signature.class, RETURNS_DEEP_STUBS);
        final List<ContentReference> content = mock(List.class);
        final SAMLObjectContentReference b = mock(SAMLObjectContentReference.class);
        b.setDigestAlgorithm(SignatureConstants.ALGO_ID_DIGEST_SHA1);
        when(signature.getContentReferences()).thenReturn(content);
        when((SAMLObjectContentReference) content.get(0)).thenReturn(b);
        assertion.setSignature(signature);
        final CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {

            @Override
            protected boolean validateAttributes() {
                return false;
            }
        };

        validator.validateAssertion(assertion);
        verify(assertion).validateSignatureAgainstProfile();

    }

    @Test(expected = WSSecurityException.class)
    public void testValidateAssertionSaml1_ValidationFails() throws WSSecurityException {
        final org.opensaml.saml.saml1.core.Assertion saml1Assertion = mock(org.opensaml.saml.saml1.core.Assertion.class);
        final SamlAssertionWrapper assertion = new SamlAssertionWrapper(saml1Assertion);
        final QName assertionQName = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "Assertion", "saml1");

        when(saml1Assertion.getElementQName()).thenReturn(assertionQName);
        when(saml1Assertion.getMajorVersion()).thenReturn(1);
        when(saml1Assertion.getMinorVersion()).thenReturn(0);
        when(saml1Assertion.getID()).thenReturn(null);
        final Signature signature = new SignatureBuilder().buildObject();
        when(assertion.getSignature()).thenReturn(signature);
        final CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {
            @Override
            protected boolean validateAttributes() {
                return false;
            }
        };
        validator.validateAssertion(assertion);
    }

    @Test
    public void testValidateAssertionSaml2() throws WSSecurityException {
        final org.opensaml.saml.saml2.core.Assertion saml2Assertion = mock(org.opensaml.saml.saml2.core.Assertion.class);
        final SamlAssertionWrapper assertionWrapper = new SamlAssertionWrapper(saml2Assertion);
        final SamlAssertionWrapper assertion = spy(assertionWrapper);

        final QName assertionQName = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion", "saml2");

        when(saml2Assertion.getElementQName()).thenReturn(assertionQName);
        final org.opensaml.saml.saml2.core.Issuer issuer = mock(org.opensaml.saml.saml2.core.Issuer.class);
        when(issuer.getFormat()).thenReturn(SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_X509);
        when(saml2Assertion.getIssuer()).thenReturn(issuer);
        when(issuer.getValue()).thenReturn(SamlConstants.SAML_DEFAULT_ISSUER_NAME);
        when(saml2Assertion.getVersion()).thenReturn(org.opensaml.saml.common.SAMLVersion.VERSION_20);
        when(saml2Assertion.getID()).thenReturn("Assertion_ID");
        final DateTime dateTime = new DateTime();
        when(saml2Assertion.getIssueInstant()).thenReturn(dateTime);
        final Subject subject = mock(Subject.class);
        when(saml2Assertion.getSubject()).thenReturn(subject);
        final NameID name = mock(NameID.class);
        when(subject.getNameID()).thenReturn(name);
        when(name.getFormat()).thenReturn(SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_X509);
        when(name.getValue()).thenReturn(SamlConstants.SAML_DEFAULT_ISSUER_NAME);
        final CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {

            /* (non-Javadoc)
             * @see gov.hhs.fha.nhinc.callback.cxf.CONNECTSamlAssertionValidator#getSaml2DefaultAssertionSpecValidators()
             */
            @Override
            protected Collection<Saml2ExchangeAuthFrameworkValidator> getSaml2SpecValidators() {
                return getSaml2DefaultAssertionSpecValidators();
            }

            @Override
            protected boolean validateAttributes() {
                return false;
            }

        };

        validator.validateAssertion(assertion);

        verify(assertion).validateSignatureAgainstProfile();
    }

    @Test
    public void testValidateAssertionSaml2_blankResource()
        throws WSSecurityException, XMLParserException,
        UnmarshallingException, XMLConfigurationException, ComponentInitializationException {
        final String inCommonMDFile = "authFrameworkAssertion.xml";
        // Get parser pool manager
        final BasicParserPool ppMgr = new BasicParserPool();
        ppMgr.initialize();
        // Parse metadata file
        final InputStream in = CONNECTSamlAssertionValidatorTest.class.getResourceAsStream(inCommonMDFile);
        final Document inCommonMDDoc = ppMgr.parse(in);
        final Element metadataRoot = inCommonMDDoc.getDocumentElement();
        final SamlAssertionWrapper assertion = new SamlAssertionWrapper(metadataRoot);

        final CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {

            /* (non-Javadoc)
             * @see gov.hhs.fha.nhinc.callback.cxf.CONNECTSamlAssertionValidator#getSaml2SpecValidators()
             */
            @Override
            protected Collection<Saml2ExchangeAuthFrameworkValidator> getSaml2SpecValidators() {
                return getSaml2DefaultAssertionSpecValidators();
            }

            @Override
            protected boolean validateAttributes() {
                return false;
            }
        };

        validator.validateAssertion(assertion);
    }

    @Test(expected = WSSecurityException.class)
    public void testValidateAssertionSaml2_ValidationFails() throws WSSecurityException {
        final org.opensaml.saml.saml2.core.Assertion saml2Assertion = mock(org.opensaml.saml.saml2.core.Assertion.class);
        final SamlAssertionWrapper assertion = new SamlAssertionWrapper(saml2Assertion);
        final org.opensaml.saml.saml2.core.Issuer issuer = mock(org.opensaml.saml.saml2.core.Issuer.class);
        final QName assertionQName = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion", "saml2");

        when(saml2Assertion.getElementQName()).thenReturn(assertionQName);
        when(saml2Assertion.getIssuer()).thenReturn(issuer);
        when(saml2Assertion.getIssuer().getFormat())
        .thenReturn("urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");

        final CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {
            @Override
            protected Collection<Saml2ExchangeAuthFrameworkValidator> getSaml2SpecValidators() {
                return getSaml2DefaultAssertionSpecValidators();
            }

            @Override
            protected boolean validateAttributes() {
                return false;
            }
        };

        validator.validateAssertion(assertion);
    }

    @Test(expected = WSSecurityException.class)
    public void ValidateAssertionSaml2WhenSPProviderID() throws WSSecurityException {
        final org.opensaml.saml.saml2.core.Assertion saml2Assertion = mock(org.opensaml.saml.saml2.core.Assertion.class);
        final SamlAssertionWrapper assertion = new SamlAssertionWrapper(saml2Assertion);
        final org.opensaml.saml.saml2.core.Issuer issuer = mock(org.opensaml.saml.saml2.core.Issuer.class);
        final QName assertionQName = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion", "saml2");

        when(saml2Assertion.getElementQName()).thenReturn(assertionQName);
        when(saml2Assertion.getIssuer()).thenReturn(issuer);
        when(saml2Assertion.getIssuer().getSPProvidedID()).thenReturn("SPProvidedID");
        when(saml2Assertion.getIssuer().getFormat()).thenReturn("urn:oasis:names:tc:SAML:1.1:nameid-format:entity");

        final CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {

            @Override
            protected Collection<Saml2ExchangeAuthFrameworkValidator> getSaml2SpecValidators() {
                return getSaml2DefaultAssertionSpecValidators();
            }

            @Override
            protected boolean validateAttributes() {
                return false;
            }
        };

        validator.validateAssertion(assertion);
    }

    @Test
    public void testGetSaml2SpecValidator() throws PropertyAccessException {
        final PropertyAccessor propAccessor = mock(PropertyAccessor.class);
        final CONNECTSamlAssertionValidator connectValidator = new CONNECTSamlAssertionValidator(propAccessor) {
            @Override
            protected Collection<Saml2ExchangeAuthFrameworkValidator> getSaml2DefaultAssertionSpecValidators() {
                return null;
            }

            @Override
            protected boolean validateAttributes() {
                return false;
            }
        };

        when(propAccessor.getPropertyBoolean(NhincConstants.SAML_PROPERTY_FILE,
            SamlConstants.ALLOW_NO_SUBJECT_ASSERTION_PROP)).thenReturn(true, false);

        Collection<Saml2ExchangeAuthFrameworkValidator> validators = connectValidator.getSaml2SpecValidators();
        Saml2ExchangeAuthFrameworkValidator validator = null;
        for (final Saml2ExchangeAuthFrameworkValidator v : validators) {
            if (v instanceof Saml2ExchangeAuthFrameworkValidator) {
                validator = v;
            }
        }
        assertNotNull(validator);
        validators = connectValidator.getSaml2SpecValidators();
        assertNull(validators);
    }

    @Test
    public void testValidate() throws WSSecurityException {
        final List<Boolean> checkedSignedAssertion = new ArrayList<>();
        final Credential credential = new Credential();
        final String SECRET_KEY = "secret";
        credential.setSecretKey(SECRET_KEY.getBytes());
        final RequestData data = mock(RequestData.class);
        final SamlAssertionWrapper assertion = mock(SamlAssertionWrapper.class, RETURNS_DEEP_STUBS);
        final Issuer issuer = mock(Issuer.class);

        credential.setSamlAssertion(assertion);
        final List<String> methods = new ArrayList<>();
        final String METHOD_NAME = "urn:oasis:names:tc:SAML:" + "TESTING" + ":cm:holder-of-key";
        methods.add(METHOD_NAME);

        final SAMLKeyInfo keyInfo = mock(SAMLKeyInfo.class);
        final org.opensaml.saml.saml2.core.Assertion saml2Assertion = mock(org.opensaml.saml.saml2.core.Assertion.class);
        final org.opensaml.saml.saml2.core.Conditions conditions = mock(org.opensaml.saml.saml2.core.Conditions.class);
        final DateTime testDate = new DateTime();

        // For validate() calls
        when(assertion.getConfirmationMethods()).thenReturn(methods);
        when(assertion.getSubjectKeyInfo()).thenReturn(keyInfo);
        when(assertion.isSigned()).thenReturn(true);
        // For checkConditions() calls
        when(assertion.getSamlVersion()).thenReturn(SAMLVersion.VERSION_20);
        when(assertion.getSaml2()).thenReturn(saml2Assertion);
        when(saml2Assertion.getConditions()).thenReturn(conditions);
        when(conditions.getNotOnOrAfter()).thenReturn(testDate.plusDays(1));
        when(conditions.getNotBefore()).thenReturn(testDate.minusSeconds(5));
        when(saml2Assertion.getIssuer()).thenReturn(issuer);
        final CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {
            @Override
            protected void checkSignedAssertion(final SamlAssertionWrapper assertion, final RequestData data)
                throws WSSecurityException {
                checkedSignedAssertion.add(true);
            }

            @Override
            protected boolean validateAttributes() {
                return false;
            }
        };

        final Credential resultCredential = validator.validate(credential, data);

        assertFalse(checkedSignedAssertion.isEmpty());
        assertTrue(checkedSignedAssertion.get(0));
        final String resultSecretKey = new String(resultCredential.getSecretKey());
        assertEquals(resultSecretKey, SECRET_KEY);

    }

    @Test
    public void testCheckSignedAssertion_HappyPath() throws WSSecurityException {
        final SamlAssertionWrapper assertion = mock(SamlAssertionWrapper.class);
        final RequestData data = mock(RequestData.class);

        final SAMLKeyInfo keyInfo = mock(SAMLKeyInfo.class);
        final PublicKey publicKey = mock(PublicKey.class);
        final Crypto crypto = mock(Crypto.class);

        when(assertion.getSignatureKeyInfo()).thenReturn(keyInfo);
        when(keyInfo.getPublicKey()).thenReturn(publicKey);
        when(data.getDecCrypto()).thenReturn(crypto);

        final CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {
            @Override
            protected boolean validateAttributes() {
                return false;
            }
        };

        validator.checkSignedAssertion(assertion, data);

        assertNotNull(assertion.getSignatureKeyInfo().getPublicKey());
        assertNull(assertion.getSignatureKeyInfo().getCerts());
    }

    @Test
    public void testCheckSignedAssertion_ChainCertError() throws WSSecurityException {
        final SamlAssertionWrapper assertion = mock(SamlAssertionWrapper.class);
        final RequestData data = mock(RequestData.class);

        final SAMLKeyInfo keyInfo = mock(SAMLKeyInfo.class);
        final PublicKey publicKey = mock(PublicKey.class);
        final Crypto crypto = mock(Crypto.class);

        when(assertion.getSignatureKeyInfo()).thenReturn(keyInfo);
        when(keyInfo.getPublicKey()).thenReturn(publicKey);
        when(data.getSigVerCrypto()).thenReturn(crypto);

        final CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {
            @Override
            protected boolean validateAttributes() {
                return false;
            }
        };

        validator.checkSignedAssertion(assertion, data);

        assertNotNull(assertion.getSignatureKeyInfo().getPublicKey());
        assertNull(assertion.getSignatureKeyInfo().getCerts());
    }

    @Test(expected = WSSecurityException.class)
    public void testCheckSignedAssertion_Exception() throws WSSecurityException {
        final SamlAssertionWrapper assertion = mock(SamlAssertionWrapper.class);
        final RequestData data = mock(RequestData.class);

        final SAMLKeyInfo keyInfo = mock(SAMLKeyInfo.class);

        when(assertion.getSignatureKeyInfo()).thenReturn(keyInfo);

        final CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {
            @Override
            protected boolean validateAttributes() {
                return false;
            }
        };

        validator.checkSignedAssertion(assertion, data);
    }

    @Test
    public void testValidateAttributes() throws WSSecurityException {
        final org.opensaml.saml.saml2.core.Assertion saml2Assertion = mock(org.opensaml.saml.saml2.core.Assertion.class);
        final SamlAssertionWrapper assertionWrapper = new SamlAssertionWrapper(saml2Assertion);
        final XMLObjectBuilderFactory builderFactory = XMLObjectProviderRegistrySupport.getBuilderFactory();
        final SAMLObjectBuilder<AttributeStatement> attributeStatementBuilder = (SAMLObjectBuilder<AttributeStatement>) builderFactory
            .getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);

        List<Object> values = new ArrayList<>();
        values.add("value");
        List<Attribute> attributes = new ArrayList<>();
        for (String name : VALIDATED_ATTRIBUTES) {
            Attribute attr = SAML2ComponentBuilder.createAttribute("", name, "nameFormat", values);
            attributes.add(attr);
        }

        final AttributeStatement attributeStatement = attributeStatementBuilder.buildObject();
        attributeStatement.getAttributes().addAll(attributes);
        List<AttributeStatement> statements = new ArrayList<>();
        statements.add(attributeStatement);

        when(saml2Assertion.getAttributeStatements()).thenReturn(statements);

        CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator();
        validator.checkAttributes(assertionWrapper);
    }

    @Test(expected = WSSecurityException.class)
    public void testValidateMissingAttributesFail() throws WSSecurityException {
        final org.opensaml.saml.saml2.core.Assertion saml2Assertion = mock(org.opensaml.saml.saml2.core.Assertion.class);
        final SamlAssertionWrapper assertionWrapper = new SamlAssertionWrapper(saml2Assertion);
        final XMLObjectBuilderFactory builderFactory = XMLObjectProviderRegistrySupport.getBuilderFactory();
        final SAMLObjectBuilder<AttributeStatement> attributeStatementBuilder = (SAMLObjectBuilder<AttributeStatement>) builderFactory
            .getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);

        List<Object> values = new ArrayList<>();
        values.add("value");

        Set<String> partialList = new HashSet<>(
            Arrays.asList(SamlConstants.ATTRIBUTE_NAME_ORG, SamlConstants.ATTRIBUTE_NAME_ORG_ID));
        List<Attribute> attributes = new ArrayList<>();
        for (String name : partialList) {
            Attribute attr = SAML2ComponentBuilder.createAttribute("", name, "nameFormat", values);
            attributes.add(attr);
        }

        final AttributeStatement attributeStatement = attributeStatementBuilder.buildObject();
        attributeStatement.getAttributes().addAll(attributes);
        List<AttributeStatement> statements = new ArrayList<>();
        statements.add(attributeStatement);

        when(saml2Assertion.getAttributeStatements()).thenReturn(statements);

        CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator();
        validator.checkAttributes(assertionWrapper);
    }

    @Test(expected = WSSecurityException.class)
    public void testValidateAttributes_AttributeMissingStringValue() throws WSSecurityException {
        final org.opensaml.saml.saml2.core.Assertion saml2Assertion = mock(org.opensaml.saml.saml2.core.Assertion.class);
        final SamlAssertionWrapper assertionWrapper = new SamlAssertionWrapper(saml2Assertion);
        final XMLObjectBuilderFactory builderFactory = XMLObjectProviderRegistrySupport.getBuilderFactory();
        final SAMLObjectBuilder<AttributeStatement> attributeStatementBuilder = (SAMLObjectBuilder<AttributeStatement>) builderFactory
            .getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);

        List<Object> values = new ArrayList<>();
        values.add("value");
        List<Attribute> attributes = new ArrayList<>();
        for (String name : VALIDATED_ATTRIBUTES) {
            Attribute attr;
            if (name.equals(SamlConstants.ATTRIBUTE_NAME_HCID)) {
                attr = SAML2ComponentBuilder.createAttribute("", name, "nameFormat", new ArrayList<>());
            } else {
                attr = SAML2ComponentBuilder.createAttribute("", name, "nameFormat", values);
            }
            attributes.add(attr);
        }

        final AttributeStatement attributeStatement = attributeStatementBuilder.buildObject();
        attributeStatement.getAttributes().addAll(attributes);
        List<AttributeStatement> statements = new ArrayList<>();
        statements.add(attributeStatement);

        when(saml2Assertion.getAttributeStatements()).thenReturn(statements);

        CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator();
        validator.checkAttributes(assertionWrapper);
    }

    @Test(expected = WSSecurityException.class)
    public void testValidateAttributes_AttributeMissingXMLObjectValue() throws WSSecurityException {
        final org.opensaml.saml.saml2.core.Assertion saml2Assertion = mock(org.opensaml.saml.saml2.core.Assertion.class);
        final SamlAssertionWrapper assertionWrapper = new SamlAssertionWrapper(saml2Assertion);
        final XMLObjectBuilderFactory builderFactory = XMLObjectProviderRegistrySupport.getBuilderFactory();
        final SAMLObjectBuilder<AttributeStatement> attributeStatementBuilder = (SAMLObjectBuilder<AttributeStatement>) builderFactory
            .getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);

        List<Object> values = new ArrayList<>();
        values.add("value");
        List<Attribute> attributes = new ArrayList<>();
        for (String name : VALIDATED_ATTRIBUTES) {
            Attribute attr;
            if (name.equals(SamlConstants.ATTRIBUTE_NAME_PURPOSE_OF_USE)) {
                attr = OpenSAML2ComponentBuilder.getInstance().createPurposeOfUseAttribute(null, null, null, null);
            } else {
                attr = SAML2ComponentBuilder.createAttribute("", name, "nameFormat", values);
            }
            attributes.add(attr);
        }

        final AttributeStatement attributeStatement = attributeStatementBuilder.buildObject();
        attributeStatement.getAttributes().addAll(attributes);
        List<AttributeStatement> statements = new ArrayList<>();
        statements.add(attributeStatement);

        when(saml2Assertion.getAttributeStatements()).thenReturn(statements);

        CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator();
        validator.checkAttributes(assertionWrapper);
    }

    @Test
    public void testValidateAttributesAnyType() throws WSSecurityException {
        final org.opensaml.saml.saml2.core.Assertion saml2Assertion = mock(org.opensaml.saml.saml2.core.Assertion.class);
        final SamlAssertionWrapper assertionWrapper = new SamlAssertionWrapper(saml2Assertion);
        final XMLObjectBuilderFactory builderFactory = XMLObjectProviderRegistrySupport.getBuilderFactory();
        final SAMLObjectBuilder<AttributeStatement> attributeStatementBuilder = (SAMLObjectBuilder<AttributeStatement>) builderFactory
            .getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);
        final XSAnyImpl anyType = mock(XSAnyImpl.class);

        List<Object> values = new ArrayList<>();
        values.add("value");
        List<Attribute> attributes = new ArrayList<>();
        for (String name : VALIDATED_ATTRIBUTES) {
            Attribute attr = SAML2ComponentBuilder.createAttribute("", name, "nameFormat", values);
            if (name.equals(SamlConstants.ATTRIBUTE_NAME_ORG)) {
                attr.getAttributeValues().clear();
                attr.getAttributeValues().add(anyType);
            }
            attributes.add(attr);
        }

        final AttributeStatement attributeStatement = attributeStatementBuilder.buildObject();
        attributeStatement.getAttributes().addAll(attributes);
        List<AttributeStatement> statements = new ArrayList<>();
        statements.add(attributeStatement);

        when(saml2Assertion.getAttributeStatements()).thenReturn(statements);
        when(anyType.getTextContent()).thenReturn("value");

        CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator();
        validator.checkAttributes(assertionWrapper);
    }

}
