/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.callback.openSAML.OpensamlObjectBuilderUtil;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.junit.Test;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.validation.ValidationException;

/**
 * The Class Saml2ExchangeAuthFrameworkValidatorTest.
 *
 * @author msw
 */
public class Saml2ExchangeAuthFrameworkValidatorTest {

    /**
     * Test validate happy path.
     *
     * @throws ValidationException the validation exception
     */
    @Test
    public void testValidate() throws ValidationException {
        Saml2ExchangeAuthFrameworkValidator validator = new Saml2ExchangeAuthFrameworkValidator();

        Assertion assertion = mock(Assertion.class);
        Subject subject = mock(Subject.class);
        NameID name = mock(NameID.class);
        Issuer issuer = mock(Issuer.class);

        when(assertion.getSubject()).thenReturn(subject);
        when(subject.getNameID()).thenReturn(name);
        when(name.getFormat()).thenReturn(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_X509);
        when(name.getValue()).thenReturn(NhincConstants.SAML_DEFAULT_ISSUER_NAME);
        when(assertion.getIssuer()).thenReturn(issuer);
        when(issuer.getFormat()).thenReturn(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_X509);
        when(issuer.getValue()).thenReturn(NhincConstants.SAML_DEFAULT_ISSUER_NAME);

        validator.validate(assertion);
    }

    /**
     * Test validate a different happy path.
     *
     * @throws ValidationException the validation exception
     */
    @Test
    public void testValidate2() throws ValidationException {
        Saml2ExchangeAuthFrameworkValidator validator = new Saml2ExchangeAuthFrameworkValidator();

        Assertion assertion = mock(Assertion.class);
        Subject subject = mock(Subject.class);
        NameID name = mock(NameID.class);
        Issuer issuer = mock(Issuer.class);

        when(assertion.getSubject()).thenReturn(subject);
        when(subject.getNameID()).thenReturn(name);
        when(name.getFormat()).thenReturn(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_EMAIL_ADDRESS);
        when(name.getValue()).thenReturn("example@example.org");
        when(assertion.getIssuer()).thenReturn(issuer);
        when(issuer.getFormat()).thenReturn(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_EMAIL_ADDRESS);
        when(issuer.getValue()).thenReturn("example@example.org");

        validator.validate(assertion);
    }

    /**
     * Test validate no name subject. This tests DIL test case 3.421.
     *
     * @throws ValidationException the validation exception
     */
    @Test(expected = ValidationException.class)
    public void testValidateNoNameSubject() throws ValidationException {
        Saml2ExchangeAuthFrameworkValidator validator = new Saml2ExchangeAuthFrameworkValidator();

        Assertion assertion = mock(Assertion.class);
        Subject subject = mock(Subject.class);

        when(assertion.getSubject()).thenReturn(subject);

        validator.validate(assertion);
    }

    /**
     * Test validate subject name format is not valid. This tests DIL test case 3.422.
     *
     * @throws ValidationException the validation exception
     */
    @Test(expected = ValidationException.class)
    public void testValidateSubjectWrongFormat() throws ValidationException {
        Saml2ExchangeAuthFrameworkValidator validator = new Saml2ExchangeAuthFrameworkValidator();

        Assertion assertion = mock(Assertion.class);
        Subject subject = mock(Subject.class);
        NameID name = mock(NameID.class);

        when(assertion.getSubject()).thenReturn(subject);
        when(subject.getNameID()).thenReturn(name);
        when(name.getFormat()).thenReturn("wrong value");

        validator.validate(assertion);
    }

    /**
     * Test validate no name issuer format. This tests DIL test case 3.410.
     *
     * @throws ValidationException the validation exception
     */
    @Test(expected = ValidationException.class)
    public void testValidateIssuerNoFormat() throws ValidationException {
        Saml2ExchangeAuthFrameworkValidator validator = new Saml2ExchangeAuthFrameworkValidator();

        Assertion assertion = mock(Assertion.class);
        Subject subject = mock(Subject.class);
        NameID name = mock(NameID.class);
        Issuer issuer = mock(Issuer.class);

        when(assertion.getSubject()).thenReturn(subject);
        when(subject.getNameID()).thenReturn(name);
        when(name.getFormat()).thenReturn(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_EMAIL_ADDRESS);
        when(assertion.getIssuer()).thenReturn(issuer);
        when(issuer.getFormat()).thenReturn(null);

        validator.validate(assertion);
    }

    /**
     * Test validate issuer is not a valid Email Address. This tests DIL test case 3.411.
     *
     * @throws ValidationException the validation exception
     * @throws ConfigurationException the configuration exception
     */
    @Test(expected = ValidationException.class)
    public void testValidateIssuerNotEmailAddress() throws ValidationException, ConfigurationException {
        Saml2ExchangeAuthFrameworkValidator validator = new Saml2ExchangeAuthFrameworkValidator();

        Assertion assertion = mock(Assertion.class);
        Subject subject = mock(Subject.class);
        NameID name = mock(NameID.class);
        Issuer issuer = generateIssuer(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_EMAIL_ADDRESS,
                "this is obviously not an email address....okkk?");

        when(assertion.getSubject()).thenReturn(subject);
        when(subject.getNameID()).thenReturn(name);
        when(name.getFormat()).thenReturn(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_EMAIL_ADDRESS);
        when(assertion.getIssuer()).thenReturn(issuer);

        validator.validate(assertion);
    }

    /**
     * Test validate issuer is not a valid X509 Subject Name. This tests DIL test case 3.412.
     *
     * @throws ValidationException the validation exception
     * @throws ConfigurationException the configuration exception
     */
    @Test(expected = ValidationException.class)
    public void testValidateIssuerNotX509SubjectName() throws ValidationException, ConfigurationException {
        Saml2ExchangeAuthFrameworkValidator validator = new Saml2ExchangeAuthFrameworkValidator();

        Assertion assertion = mock(Assertion.class);
        Subject subject = mock(Subject.class);
        NameID name = mock(NameID.class);
        Issuer issuer = generateIssuer(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_X509,
                "this is obviously not an x509 subject name....okkk?");

        when(assertion.getSubject()).thenReturn(subject);
        when(subject.getNameID()).thenReturn(name);
        when(name.getFormat()).thenReturn(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_X509);
        when(assertion.getIssuer()).thenReturn(issuer);

        validator.validate(assertion);
    }

    /**
     * Test validate issuer is not a valid Windows Domain Name. This tests DIL test case 3.413.
     *
     * @throws ValidationException the validation exception
     * @throws ConfigurationException the configuration exception
     */
    @Test(expected = ValidationException.class)
    public void testValidateIssuerNotWindowsDomainName() throws ValidationException, ConfigurationException {
        Saml2ExchangeAuthFrameworkValidator validator = new Saml2ExchangeAuthFrameworkValidator();

        Assertion assertion = mock(Assertion.class);
        Subject subject = mock(Subject.class);
        NameID name = mock(NameID.class);
        Issuer issuer = generateIssuer(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_WINDOWS_NAME,
                "this is obviously not an windows domain name....okkk?");

        when(assertion.getSubject()).thenReturn(subject);
        when(subject.getNameID()).thenReturn(name);
        when(name.getFormat()).thenReturn(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_X509);
        when(assertion.getIssuer()).thenReturn(issuer);

        validator.validate(assertion);
    }

    /**
     * Test validate happy path.
     *
     * @throws ValidationException the validation exception
     */
    @Test(expected = ValidationException.class)
    public void testValidateSubjectX509InvalidValue() throws ValidationException {
        Saml2ExchangeAuthFrameworkValidator validator = new Saml2ExchangeAuthFrameworkValidator();

        Assertion assertion = mock(Assertion.class);
        Subject subject = mock(Subject.class);
        NameID name = mock(NameID.class);
        Issuer issuer = mock(Issuer.class);

        when(assertion.getSubject()).thenReturn(subject);
        when(subject.getNameID()).thenReturn(name);
        when(name.getFormat()).thenReturn(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_X509);
        when(name.getValue()).thenReturn("not a valid X509 name.");
        when(assertion.getIssuer()).thenReturn(issuer);
        when(issuer.getFormat()).thenReturn(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_X509);
        when(issuer.getValue()).thenReturn(NhincConstants.SAML_DEFAULT_ISSUER_NAME);

        validator.validate(assertion);
    }

    /**
     * Test validate happy path.
     *
     * @throws ValidationException the validation exception
     */
    @Test(expected = ValidationException.class)
    public void testValidateSubjectEmailInvalidValue() throws ValidationException {
        Saml2ExchangeAuthFrameworkValidator validator = new Saml2ExchangeAuthFrameworkValidator();

        Assertion assertion = mock(Assertion.class);
        Subject subject = mock(Subject.class);
        NameID name = mock(NameID.class);
        Issuer issuer = mock(Issuer.class);

        when(assertion.getSubject()).thenReturn(subject);
        when(subject.getNameID()).thenReturn(name);
        when(name.getFormat()).thenReturn(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_EMAIL_ADDRESS);
        when(name.getValue()).thenReturn("not a valid email address.");
        when(assertion.getIssuer()).thenReturn(issuer);
        when(issuer.getFormat()).thenReturn(NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_X509);
        when(issuer.getValue()).thenReturn(NhincConstants.SAML_DEFAULT_ISSUER_NAME);

        validator.validate(assertion);
    }

    /**
     * Generate issuer.
     *
     * @param format the format
     * @param value the value
     * @return the issuer
     * @throws ConfigurationException the configuration exception
     */
    protected Issuer generateIssuer(String format, String value) throws ConfigurationException {
        OpensamlObjectBuilderUtil util = new OpensamlObjectBuilderUtil();

        Issuer issuer = (Issuer) util.createOpenSAMLObject(Issuer.DEFAULT_ELEMENT_NAME);

        issuer.setFormat(format);
        issuer.setValue(value);
        return issuer;
    }

}
