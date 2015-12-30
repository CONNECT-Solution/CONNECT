/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.saml.SAMLKeyInfo;
import org.apache.ws.security.saml.ext.AssertionWrapper;
import org.apache.ws.security.validate.Credential;
import org.joda.time.DateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml1.core.Statement;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.validation.Validator;
import org.opensaml.xml.validation.ValidatorSuite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CONNECTSamlAssertionValidatorTest {

    @Test
    public void testValidateAssertionSaml1() throws WSSecurityException {
        org.opensaml.saml1.core.Assertion saml1Assertion = mock(org.opensaml.saml1.core.Assertion.class);
        AssertionWrapper assertion = new AssertionWrapper(saml1Assertion);
        QName assertionQName = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "Assertion", "saml1");

        when(saml1Assertion.getElementQName()).thenReturn(assertionQName);
        when(saml1Assertion.getMajorVersion()).thenReturn(1);
        when(saml1Assertion.getMinorVersion()).thenReturn(0);
        when(saml1Assertion.getID()).thenReturn("Assertion_ID");
        when(saml1Assertion.getIssuer()).thenReturn("Issuer");
        DateTime dateTime = new DateTime();
        when(saml1Assertion.getIssueInstant()).thenReturn(dateTime);
        Statement statement = mock(Statement.class);
        List<Statement> statementList = new ArrayList<>();
        statementList.add(statement);
        when(saml1Assertion.getStatements()).thenReturn(statementList);

        CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator();

        validator.validateAssertion(assertion);

        verify(saml1Assertion, times(2)).getOrderedChildren();
    }

    @Test(expected = WSSecurityException.class)
    public void testValidateAssertionSaml1_ValidationFails() throws WSSecurityException {
        org.opensaml.saml1.core.Assertion saml1Assertion = mock(org.opensaml.saml1.core.Assertion.class);
        AssertionWrapper assertion = new AssertionWrapper(saml1Assertion);
        QName assertionQName = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "Assertion", "saml1");

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
        QName assertionQName = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion", "saml2");

        when(saml2Assertion.getElementQName()).thenReturn(assertionQName);
        org.opensaml.saml2.core.Issuer issuer = mock(org.opensaml.saml2.core.Issuer.class);
        when(issuer.getFormat()).thenReturn(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_X509);
        when(saml2Assertion.getIssuer()).thenReturn(issuer);
        when(issuer.getValue()).thenReturn(NhincConstants.SAML_DEFAULT_ISSUER_NAME);
        when(saml2Assertion.getVersion()).thenReturn(SAMLVersion.VERSION_20);
        when(saml2Assertion.getID()).thenReturn("Assertion_ID");
        DateTime dateTime = new DateTime();
        when(saml2Assertion.getIssueInstant()).thenReturn(dateTime);
        Subject subject = mock(Subject.class);
        when(saml2Assertion.getSubject()).thenReturn(subject);
        NameID name = mock(NameID.class);
        when(subject.getNameID()).thenReturn(name);
        when(name.getFormat()).thenReturn(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_X509);
        when(name.getValue()).thenReturn(NhincConstants.SAML_DEFAULT_ISSUER_NAME);

        CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {
            @Override
            protected Collection<ValidatorSuite> getSaml2SpecValidators() {
                return getSaml2DefaultAssertionSpecValidators();
            }
        };

        validator.validateAssertion(assertion);

        verify(saml2Assertion, times(3)).getOrderedChildren();
    }

    @Test
    public void testValidateAssertionSaml2_blankResource() throws WSSecurityException, XMLParserException, UnmarshallingException, ConfigurationException {

        /*
         * when(saml2Assertion.getElementQName()).thenReturn(assertionQName); org.opensaml.saml2.core.Issuer issuer =
         * mock(org.opensaml.saml2.core.Issuer.class);
         * when(issuer.getFormat()).thenReturn(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_X509);
         * when(saml2Assertion.getIssuer()).thenReturn(issuer);
         * when(issuer.getValue()).thenReturn(NhincConstants.SAML_DEFAULT_ISSUER_NAME);
         * when(saml2Assertion.getVersion()).thenReturn(SAMLVersion.VERSION_20);
         * when(saml2Assertion.getID()).thenReturn("Assertion_ID"); DateTime dateTime = new DateTime();
         * when(saml2Assertion.getIssueInstant()).thenReturn(dateTime); Subject subject = mock(Subject.class);
         * when(saml2Assertion.getSubject()).thenReturn(subject); NameID name = mock(NameID.class);
         * when(subject.getNameID()).thenReturn(name);
         * when(name.getFormat()).thenReturn(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_X509);
         * when(name.getValue()).thenReturn(NhincConstants.SAML_DEFAULT_ISSUER_NAME); AuthzDecisionStatement authd =
         * mock(AuthzDecisionStatement.class);
         * when(saml2Assertion.getAuthzDecisionStatements()).thenReturn(Collections.singletonList(authd));
         * when(authd.getDecision()).thenReturn(DecisionTypeEnumeration.PERMIT);
         * when(authd.getResource()).thenReturn(""); Action action = mock(Action.class);
         * when(authd.getActions()).thenReturn(Collections.singletonList(action));
         * when(action.getNamespace()).thenReturn("urn:oasis:names:tc:SAML:1.0:action:rwedc");
         * when(action.getAction()).thenReturn("Execute"); Evidence evidence = mock(Evidence.class);
         * when(authd.getEvidence()).thenReturn(evidence); org.opensaml.saml2.core.Assertion authdAssertion =
         * mock(org.opensaml.saml2.core.Assertion.class);
         * when(evidence.getAssertions()).thenReturn(Collections.singletonList(authdAssertion));
         * when(authdAssertion.getIssuer()).thenReturn(issuer);
         */

        String inCommonMDFile = "authFrameworkAssertion.xml";

        // Initialize the library
        DefaultBootstrap.bootstrap();

        // Get parser pool manager
        BasicParserPool ppMgr = new BasicParserPool();
        ppMgr.setNamespaceAware(true);

        // Parse metadata file
        InputStream in = CONNECTSamlAssertionValidatorTest.class.getResourceAsStream(inCommonMDFile);
        Document inCommonMDDoc = ppMgr.parse(in);
        Element metadataRoot = inCommonMDDoc.getDocumentElement();

        // Get apropriate unmarshaller
        UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
        Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion"));

        // Unmarshall using the document root element, an EntitiesDescriptor in this case
        org.opensaml.saml2.core.Assertion saml2Assertion = (org.opensaml.saml2.core.Assertion) unmarshaller.unmarshall(metadataRoot);

        AssertionWrapper assertion = new AssertionWrapper(saml2Assertion);

        CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {
            @Override
            protected Collection<ValidatorSuite> getSaml2SpecValidators() {
                return getSaml2DefaultAssertionSpecValidators();
            }
        };

        validator.validateAssertion(assertion);
    }

    @Test(expected = WSSecurityException.class)
    public void testValidateAssertionSaml2_ValidationFails() throws WSSecurityException {
        org.opensaml.saml2.core.Assertion saml2Assertion = mock(org.opensaml.saml2.core.Assertion.class);
        AssertionWrapper assertion = new AssertionWrapper(saml2Assertion);
        org.opensaml.saml2.core.Issuer issuer = mock(org.opensaml.saml2.core.Issuer.class);
        QName assertionQName = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion", "saml2");

        when(saml2Assertion.getElementQName()).thenReturn(assertionQName);
        when(saml2Assertion.getIssuer()).thenReturn(issuer);
        when(saml2Assertion.getIssuer().getFormat()).thenReturn("urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");

        CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {
            @Override
            protected Collection<ValidatorSuite> getSaml2SpecValidators() {
                return getSaml2DefaultAssertionSpecValidators();
            }
        };

        validator.validateAssertion(assertion);
    }

    @Test(expected = WSSecurityException.class)
    public void ValidateAssertionSaml2WhenSPProviderID() throws WSSecurityException {
        org.opensaml.saml2.core.Assertion saml2Assertion = mock(org.opensaml.saml2.core.Assertion.class);
        AssertionWrapper assertion = new AssertionWrapper(saml2Assertion);
        org.opensaml.saml2.core.Issuer issuer = mock(org.opensaml.saml2.core.Issuer.class);
        QName assertionQName = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion", "saml2");

        when(saml2Assertion.getElementQName()).thenReturn(assertionQName);
        when(saml2Assertion.getIssuer()).thenReturn(issuer);
        when(saml2Assertion.getIssuer().getSPProvidedID()).thenReturn("SPProvidedID");
        when(saml2Assertion.getIssuer().getFormat()).thenReturn("urn:oasis:names:tc:SAML:1.1:nameid-format:entity");

        CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {
            @Override
            protected Collection<ValidatorSuite> getSaml2SpecValidators() {
                return getSaml2DefaultAssertionSpecValidators();
            }
        };

        validator.validateAssertion(assertion);
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testGetSaml2AllowNoSubjectAssertionSpecValidator() {
        CONNECTSamlAssertionValidator connectValidator = new CONNECTSamlAssertionValidator();
        ValidatorSuite specValidators = connectValidator.getSaml2AllowNoSubjectAssertionSpecValidator();
        QName qName = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion", "saml2");
        List<Validator> validatorList = specValidators.getValidators(qName);

        assertNotNull(specValidators);
        assertEquals(validatorList.size(), 1);
    }

    @Test
    public void testGetSaml2SpecValidator() throws PropertyAccessException {
        PropertyAccessor propAccessor = mock(PropertyAccessor.class);
        CONNECTSamlAssertionValidator connectValidator = new CONNECTSamlAssertionValidator(propAccessor) {
            @Override
            protected Collection<ValidatorSuite> getSaml2DefaultAssertionSpecValidators() {
                return null;
            }
        };

        when(propAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, "allowNoSubjectAssertion"))
                .thenReturn(true, false);

        Collection<ValidatorSuite> validators = connectValidator.getSaml2SpecValidators();
        ValidatorSuite validator = null;
        for (ValidatorSuite v : validators) {
            if ("saml2-core-spec-validator-allow-no-subject-assertion".equals(v.getId())) {
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
        Credential credential = new Credential();
        final String SECRET_KEY = "secret";
        credential.setSecretKey(SECRET_KEY.getBytes());
        RequestData data = mock(RequestData.class);
        AssertionWrapper assertion = mock(AssertionWrapper.class);
        credential.setAssertion(assertion);

        List<String> methods = new ArrayList<>();
        final String METHOD_NAME = "urn:oasis:names:tc:SAML:" + "TESTING" + ":cm:holder-of-key";
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
        when(assertion.getSaml2()).thenReturn(saml2Assertion, saml2Assertion, saml2Assertion, null);
        when(saml2Assertion.getConditions()).thenReturn(conditions);
        when(conditions.getNotOnOrAfter()).thenReturn(testDate.plusDays(1));
        when(conditions.getNotBefore()).thenReturn(testDate.minusSeconds(5));

        CONNECTSamlAssertionValidator validator = new CONNECTSamlAssertionValidator() {
            @Override
            protected void checkSignedAssertion(AssertionWrapper assertion, RequestData data)
                    throws WSSecurityException {
                checkedSignedAssertion.add(true);
            }
        };

        Credential resultCredential = validator.validate(credential, data);

        assertFalse(checkedSignedAssertion.isEmpty());
        assertTrue(checkedSignedAssertion.get(0));
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
    public void testCheckSignedAssertion_ChainCertError() throws WSSecurityException {
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
