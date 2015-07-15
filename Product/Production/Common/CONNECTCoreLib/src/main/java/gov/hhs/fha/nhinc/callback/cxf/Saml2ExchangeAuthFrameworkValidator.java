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

import javax.naming.Name;
import javax.naming.ldap.LdapName;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.validator.AssertionSpecValidator;
import org.opensaml.xml.validation.ValidationException;

import org.apache.commons.validator.routines.EmailValidator;
/**
 * The Class Saml2ExchangeAuthFrameworkValidator.
 *
 * @author msw
 */
public class Saml2ExchangeAuthFrameworkValidator extends AssertionSpecValidator {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(Saml2ExchangeAuthFrameworkValidator.class);

    /** The Constant invalidDomainNameChars. */
    private static final char[] invalidDomainNameChars = { '\\', '/', ':', '*', '?', '\"', '<', '>', '|' };

    /** The Constant invalidUserNameChars. */
    private static final char[] invalidUserNameChars = { ';', ':', '\"', '<', '>', '*', '+', '=', '\\', '|', '?', ',' };

    /**
     * (non-Javadoc).
     *
     * @param assertion the assertion
     * @throws ValidationException the validation exception
     * @see org.opensaml.saml2.core.validator.AssertionSpecValidator#validate(org.opensaml.saml2.core.Assertion)
     */
    @Override
    public void validate(Assertion assertion) throws ValidationException {
        super.validate(assertion);

        validateSubject(assertion);
        validateIssuer(assertion.getIssuer());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opensaml.saml2.core.validator.AssertionSpecValidator#validateSubject(org.opensaml.saml2.core.Assertion)
     */
    @Override
    protected void validateSubject(Assertion assertion) throws ValidationException {
        validateSubject(assertion.getSubject());

        super.validateSubject(assertion);
    }

    /**
     * Validate issuer.
     *
     * @param issuer the issuer
     * @throws ValidationException the validation exception
     */
    protected void validateIssuer(Issuer issuer) throws ValidationException {
        if (issuer == null || StringUtils.isBlank(issuer.getFormat())) {
            throw new ValidationException("Issuer format cannot be blank.");
        }

        validateNameIdFormatValue(issuer.getFormat(), issuer.getValue());
    }

    /**
     * Checks that the subject is valid (checks for the presence of a name id).
     *
     * @param subject the subject
     * @return true if Subject is valid, false otherwise.
     * @throws ValidationException the validation exception
     */
    protected void validateSubject(Subject subject) throws ValidationException {
        if (subject == null || subject.getNameID() == null) {
            throw new ValidationException("Subject is empty or invalid.");
        }

        NameID name = subject.getNameID();
        String format = name.getFormat();
        if (!NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_EMAIL_ADDRESS.equals(format)
                && !NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_X509.equals(format)) {
            throw new ValidationException("Subject Name Id format must be x509 or Email.");
        }

        validateNameIdFormatValue(format, name.getValue());
    }

    /**
     * Validate name id format value.
     *
     * @param format the format
     * @param value the value
     * @throws ValidationException the validation exception
     */
    protected void validateNameIdFormatValue(String format, String value) throws ValidationException {
        if (NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_EMAIL_ADDRESS.equals(format)) {
            EmailValidator validator = EmailValidator.getInstance();
            if (!validator.isValid(value)) {
                throw new ValidationException("Not a valid email address.");
            }
        } else if (NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_X509.equals(format)) {
            try {
                Name name = new LdapName(value);
            } catch (Exception e) {
                LOG.info("Validation of X509 Subject Name failed:", e);
                throw new ValidationException("Not a valid X509 Subject Name.");
            }
        } else if (NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_WINDOWS_NAME.equals(format)) {
            String[] parts = StringUtils.split(value, "\\");

            if (parts.length > 2) {
                throw new ValidationException("Invalid Windows Domain Name format: multiple backslashes.");
            }

            String domainName = null;
            String userName;
            if (parts.length == 1) {
                userName = parts[0];
            } else {
                domainName = parts[0];
                userName = parts[1];
            }

            if (StringUtils.containsAny(domainName, invalidDomainNameChars)) {
                throw new ValidationException(
                        "Invalid Windows Domain Name format: domain name contains invalid characters.");
            }

            if (StringUtils.containsAny(userName, invalidUserNameChars)) {
                throw new ValidationException(
                        "Invalid Windows Domain Name format: user name contains invalid characters.");
            }
        }
    }
}
