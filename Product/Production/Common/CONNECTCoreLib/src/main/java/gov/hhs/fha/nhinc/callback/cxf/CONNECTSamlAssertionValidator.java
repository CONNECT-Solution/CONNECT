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

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.ext.WSSecurityException.ErrorCode;
import org.apache.wss4j.common.saml.OpenSAMLUtil;
import org.apache.wss4j.common.saml.SAMLKeyInfo;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.validate.Credential;
import org.apache.wss4j.dom.validate.SamlAssertionValidator;
import org.opensaml.saml.saml2.core.Issuer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class CONNECTSamlAssertionValidator.
 *
 * {@inheritDoc}
 *
 * In addition, this class can be configured to allow Assertions with No Subjects. This is required for interoperability
 * with previous CONNECT gateways.
 */
public class CONNECTSamlAssertionValidator extends SamlAssertionValidator {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(CONNECTSamlAssertionValidator.class);

    /** The Constant String literal */
    private static final String IS_PRESENT = "is present.";

    /** The Constant ALLOW_NO_SUBJECT_ASSERTION_PROP. */
    private static final String ALLOW_NO_SUBJECT_ASSERTION_PROP = "allowNoSubjectAssertion";

    /** The property accessor. */
    private final PropertyAccessor propertyAccessor;

    /**
     * Instantiates a new cONNECT saml assertion validator.
     */
    public CONNECTSamlAssertionValidator() {
        propertyAccessor = PropertyAccessor.getInstance();
    }

    /**
     * Instantiates a new cONNECT saml assertion validator.
     *
     * @param propertyAccessor the property accessor
     */
    public CONNECTSamlAssertionValidator(final PropertyAccessor propertyAccessor) {
        this.propertyAccessor = propertyAccessor;
    }

    /**
     * Validate the assertion against schemas/profiles.
     *
     * @param assertion the assertion
     * @throws WSSecurityException the wS security exception
     */
    @Override
    protected void validateAssertion(final SamlAssertionWrapper assertion) throws WSSecurityException {
        if (assertion.getSaml1() != null) {
            assertion.validateSignatureAgainstProfile();
        } else if (assertion.getSaml2() != null) {
            final Issuer issuer = assertion.getSaml2().getIssuer();
            validateIssuer(issuer);
            try {
                // need to process custom saml validator
                for (final Saml2ExchangeAuthFrameworkValidator validator : getSaml2SpecValidators()) {
                    validator.validateAssertion(assertion);
                }
            } catch (final WSSecurityException e) {
                LOG.error("Saml Validation error: " + e.getMessage(), e);
                throw new WSSecurityException(ErrorCode.FAILED_CHECK, "invalidSAMLsecurity");
            }
        }
    }

    /**
     * @param issuer
     * @throws WSSecurityException
     */
    private static void validateIssuer(Issuer issuer) throws WSSecurityException {
        if (SamlConstants.URN_OASIS_NAMES.equals(issuer.getFormat())) {
            if (!StringUtils.isBlank(issuer.getSPProvidedID())) {
                throw new WSSecurityException(ErrorCode.FAILED_CHECK,
                    SamlConstants.SECURITY_ASSERTION_ISSUER_FORMAT + " " + SamlConstants.URN_OASIS_NAMES + " " + "and"
                        + SamlConstants.SECURITY_ASSERTION_ISSUER + " " + IS_PRESENT);
            }
            if (!StringUtils.isBlank(issuer.getNameQualifier())) {
                throw new WSSecurityException(ErrorCode.FAILED_CHECK,
                    SamlConstants.SECURITY_ASSERTION_ISSUER_FORMAT + " " + SamlConstants.URN_OASIS_NAMES + " " + "and"
                        + SamlConstants.SECURITY_ASSERTION_ISSUER + " " + IS_PRESENT);
            }

            if (!StringUtils.isBlank(issuer.getSPNameQualifier())) {
                throw new WSSecurityException(ErrorCode.FAILED_CHECK,
                    SamlConstants.SECURITY_ASSERTION_ISSUER_FORMAT + " " + SamlConstants.URN_OASIS_NAMES + " " + "and"
                        + SamlConstants.SECURITY_ASSERTION_ISSUER + " " + IS_PRESENT);

            }
        }

    }

    /**
     * Gets the exchange auth framework validator suite.
     *
     * @return the exchange auth framework validator suite
     */
    protected Saml2ExchangeAuthFrameworkValidator getExchangeAuthFrameworkValidatorSuite() {
        return new Saml2ExchangeAuthFrameworkValidator();
    }

    /**
     * Gets the saml2 spec validators.
     *
     * @return the saml2 spec validators
     */
    protected Collection<Saml2ExchangeAuthFrameworkValidator> getSaml2SpecValidators() {
        try {
            final Boolean allowNoSubjectAssertion = propertyAccessor
                .getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, ALLOW_NO_SUBJECT_ASSERTION_PROP);

            if (allowNoSubjectAssertion) {
                return getSaml2AllowNoSubjectAssertionSpecValidators();
            } else {
                return getSaml2DefaultAssertionSpecValidators();
            }

        } catch (final Exception e) {
            LOG.warn("Failed to get SAML 2 assertion validator. " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Gets the saml2 allow no subject assertion spec validators.
     *
     * @return the saml2 allow no subject assertion spec validators
     */
    protected Collection<Saml2ExchangeAuthFrameworkValidator> getSaml2AllowNoSubjectAssertionSpecValidators() {
        return Collections.singleton(getSaml2AllowNoSubjectAssertionSpecValidator());
    }

    /**
     * Gets the saml2 allow no subject assertion spec validator.
     *
     * @return the saml2 allow no subject assertion spec validator
     */

    protected Saml2ExchangeAuthFrameworkValidator getSaml2AllowNoSubjectAssertionSpecValidator() {
        return new Saml2AllowNoSubjectAssertionSpecValidator();
    }

    /**
     * Gets the saml2 assertion spec validator.
     *
     * @return the saml2 assertion spec validator
     */
    protected Collection<Saml2ExchangeAuthFrameworkValidator> getSaml2DefaultAssertionSpecValidators() {
        final Collection<Saml2ExchangeAuthFrameworkValidator> suites = new HashSet<>();
        suites.add(getExchangeAuthFrameworkValidatorSuite());
        return suites;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.wss4j.dom.validate.SamlAssertionValidator#validate(org.apache.wss4j.dom.validate.Credential,
     * org.apache.wss4j.dom.handler.RequestData)
     */
    @Override
    public Credential validate(final Credential credential, final RequestData data) throws WSSecurityException {
        if (credential == null || credential.getSamlAssertion() == null) {
            throw new WSSecurityException(ErrorCode.FAILED_CHECK, "noCredential");
        }
        final SamlAssertionWrapper assertion = credential.getSamlAssertion();

        // Check HOK requirements
        String confirmMethod = null;
        final List<String> methods = assertion.getConfirmationMethods();
        if (CollectionUtils.isNotEmpty(methods)) {
            confirmMethod = methods.get(0);
        }
        // Check assertion
        checkAssertion(assertion, confirmMethod);

        // Check conditions
        checkConditions(assertion);

        // Validate the assertion against schemas/profiles
        validateAssertion(assertion);

        checkSignedAssertion(assertion, data);

        return credential;
    }

    /**
     * @param assertion
     * @param confirmMethod
     * @throws WSSecurityException
     */
    private static void checkAssertion(SamlAssertionWrapper assertion, String confirmMethod)
        throws WSSecurityException {
        if (OpenSAMLUtil.isMethodHolderOfKey(confirmMethod)) {
            if (assertion.getSubjectKeyInfo() == null) {
                LOG.debug("There is no Subject KeyInfo to match the holder-of-key subject conf method");
                throw new WSSecurityException(ErrorCode.FAILED_CHECK, "noKeyInSAMLToken");
            }
            // The assertion must have been signed for HOK
            if (!assertion.isSigned()) {
                LOG.debug("A holder-of-key assertion must be signed");
                throw new WSSecurityException(ErrorCode.FAILED_CHECK, "invalidSAMLsecurity");
            }
        }

    }

    /**
     * Check signed assertion.
     *
     * @param assertion the assertion
     * @param data the data
     * @throws WSSecurityException the wS security exception
     */
    protected void checkSignedAssertion(final SamlAssertionWrapper assertion, final RequestData data)
        throws WSSecurityException {

        final SAMLKeyInfo samlKeyInfo = assertion.getSignatureKeyInfo();
        final X509Certificate[] certs = samlKeyInfo.getCerts();
        final PublicKey publicKey = samlKeyInfo.getPublicKey();

        try {
            super.verifySignedAssertion(assertion, data);
        } catch (final WSSecurityException e) {
            if (certs == null && publicKey != null) {
                LOG.warn("Could not establish trust of the signature's public key because no matching public key "
                    + "exists in the truststore. Please see GATEWAY-3146 for more details.");
            } else {
                throw e;
            }
        }
    }

}
