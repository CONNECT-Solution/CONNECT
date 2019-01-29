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

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.ext.WSSecurityException.ErrorCode;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.apache.wss4j.dom.validate.SamlAssertionValidator;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Saml2ExchangeAuthFrameworkValidator.
 *
 * @author msw
 */
public class Saml2ExchangeAuthFrameworkValidator extends SamlAssertionValidator {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(Saml2ExchangeAuthFrameworkValidator.class);

    /** The Constant invalidDomainNameChars. */
    private static final char[] invalidDomainNameChars = { '\\', '/', ':', '*', '?', '\"', '<', '>', '|' };

    /** The Constant invalidUserNameChars. */
    private static final char[] invalidUserNameChars = { ';', ':', '\"', '<', '>', '*', '+', '=', '\\', '|', '?', ',' };

    /*
     * (non-Javadoc)
     *
     * @see org.apache.wss4j.dom.validate.SamlAssertionValidator#validateAssertion(org.apache.wss4j.common.saml.
     * SamlAssertionWrapper)
     */
    @Override
    protected void validateAssertion(final SamlAssertionWrapper samlAssertion) throws WSSecurityException {

        try {
            super.validateAssertion(samlAssertion);
            validateSubject(samlAssertion.getSaml2().getSubject());
            validateIssuer(samlAssertion.getSaml2().getIssuer());

        } catch (WSSecurityException e) {
            LOG.error("Validation Fail {}", e.getLocalizedMessage(), e);
            throw new WSSecurityException(ErrorCode.FAILURE, e);
        }

    }

    /**
     * Validate issuer.
     *
     * @param issuer the issuer
     * @throws ValidationException the validation exception
     */
    protected static void validateIssuer(final Issuer issuer) throws WSSecurityException {
        if (issuer == null || StringUtils.isBlank(issuer.getFormat())) {
            throw new WSSecurityException(ErrorCode.FAILURE, "Issuer format cannot be blank.");
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
    protected static void validateSubject(final Subject subject) throws WSSecurityException {
        if (subject == null || subject.getNameID() == null) {
            throw new WSSecurityException(ErrorCode.FAILURE, "Subject is empty or invalid.");
        }
        final NameID name = subject.getNameID();
        final String format = name.getFormat();
        if (!SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_EMAIL_ADDRESS.equals(format)
            && !SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_X509.equals(format)) {
            throw new WSSecurityException(ErrorCode.FAILURE, "Subject Name Id format must be x509 or Email.");
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
    protected static void validateNameIdFormatValue(final String format, final String value)
        throws WSSecurityException {
        if (SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_EMAIL_ADDRESS.equals(format)) {
            validateEmail(value);
        } else if (SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_X509.equals(format)) {
            validateName(value);
        } else if (SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_WINDOWS_NAME.equals(format)) {
            validateParts(value);
        }
    }

    /**
     * @param value throws WSSecurityException
     */
    private static void validateParts(String value) throws WSSecurityException {
        final String[] parts = StringUtils.split(value, "\\");

        if (parts.length > 2) {
            throw new WSSecurityException(ErrorCode.FAILURE,
                "Invalid Windows Domain Name format: multiple backslashes.");
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
            throw new WSSecurityException(ErrorCode.FAILURE,
                "Invalid Windows Domain Name format: domain name contains invalid characters.");
        }

        if (StringUtils.containsAny(userName, invalidUserNameChars)) {
            throw new WSSecurityException(ErrorCode.FAILURE,
                "Invalid Windows Domain Name format: user name contains invalid characters.");
        }

    }

    /**
     * @param value throws WSSecurityException
     * @throws InvalidNameException
     */
    private static void validateName(String value) throws WSSecurityException {
        final WSSecurityException ldapException = new WSSecurityException(ErrorCode.FAILURE, "Not a valid X509 Subject Name.");
        if(NullChecker.isNullish(value)) {
            throw ldapException;
        }
        try {
            LdapName name = new LdapName(value);
            if(name.isEmpty()) {
                throw ldapException;
            }
        } catch (InvalidNameException e) {
            LOG.info("Validation of X509 Subject Name failed: {}", e.getLocalizedMessage(), e);
            throw ldapException;
        }
    }

    /**
     * @param value throws WSSecurityException
     */
    private static void validateEmail(String value) throws WSSecurityException {
        if (!EmailValidator.getInstance().isValid(value)) {
            throw new WSSecurityException(ErrorCode.FAILURE, "Not a valid email address.");
        }

    }
}
