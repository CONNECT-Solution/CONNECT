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

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.ext.WSSecurityException.ErrorCode;
import org.apache.wss4j.common.saml.OpenSAMLUtil;
import org.apache.wss4j.common.saml.SAMLKeyInfo;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.validate.Credential;
import org.apache.wss4j.dom.validate.SamlAssertionValidator;
import org.opensaml.saml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.ValidatorSuite;
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

    /** The Constant ALLOW_NO_SUBJECT_ASSERTION_PROP. */
    private static final String ALLOW_NO_SUBJECT_ASSERTION_PROP = "allowNoSubjectAssertion";

    /** The Constant ALLOW_NO_SUBJECT_ASSERTION_ID. */
    private static final String ALLOW_NO_SUBJECT_ASSERTION_ID = "saml2-core-spec-validator-allow-no-subject-assertion";

    /** The Constant EXCHANGE_AUTH_FRWK_VALIDATOR_SUITE. */
    private static final String EXCHANGE_AUTH_FRWK_VALIDATOR_SUITE = "exchange-authorization-framework-validator-suite";

    /** The Constant TEMP_RESOURCE_FOR_VALIDATION. */
    private static final String TEMP_RESOURCE_FOR_VALIDATION = "TEMPORARY_RESOURCE_FOR_VALIDATION";

    /** The property accessor. */
    private PropertyAccessor propertyAccessor;

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
    public CONNECTSamlAssertionValidator(PropertyAccessor propertyAccessor) {
        this.propertyAccessor = propertyAccessor;
    }

    /**
     * Validate the assertion against schemas/profiles.
     *
     * @param assertion the assertion
     * @throws WSSecurityException the wS security exception
     */
    @Override
    protected void validateAssertion(SamlAssertionWrapper assertion) throws WSSecurityException {
        if (assertion.getSaml1() != null) {
            ValidatorSuite schemaValidators = org.opensaml.Configuration.getValidatorSuite("saml1-schema-validator");
            ValidatorSuite specValidators = org.opensaml.Configuration.getValidatorSuite("saml1-spec-validator");
            try {
                schemaValidators.validate((XMLObject)assertion.getSaml1());
                specValidators.validate((XMLObject)assertion.getSaml1());
            } catch (ValidationException e) {
                LOG.debug("Saml Validation error: " + e.getMessage(), e);
                throw new WSSecurityException(ErrorCode.FAILED_CHECK, "invalidSAMLsecurity");
            }
        } else if (assertion.getSaml2() != null) {
            List<ValidatorSuite> validators = new LinkedList<>();
            validators.add(org.opensaml.Configuration.getValidatorSuite("saml2-core-schema-validator"));
            validators.addAll(getSaml2SpecValidators());

            for (AuthzDecisionStatement auth : assertion.getSaml2().getAuthzDecisionStatements()) {
                if (StringUtils.isBlank(auth.getResource())) {
                    auth.setResource(TEMP_RESOURCE_FOR_VALIDATION);
                }
            }

            Issuer issuer = assertion.getSaml2().getIssuer();
            if (issuer.getFormat().equals("urn:oasis:names:tc:SAML:1.1:nameid-format:entity")) {
                if (!StringUtils.isBlank(issuer.getSPProvidedID())) {
                    throw new WSSecurityException(ErrorCode.FAILED_CHECK,"SOAP header element Security/Assertion/Issuer/@Format = " + ""
                            + "urn:oasis:names:tc:SAML:1.1:nameid-format:entity" + "" + "and"
                            + "Security/Assertion/Issuer/@SPProvidedID" + " " + "is present.");
                }
                if (!StringUtils.isBlank(issuer.getNameQualifier())) {
                    throw new WSSecurityException(ErrorCode.FAILED_CHECK,"SOAP header element Security/Assertion/Issuer/@Format = " + ""
                            + "urn:oasis:names:tc:SAML:1.1:nameid-format:entity" + "" + "and"
                            + "Security/Assertion/Issuer/@NameQualifier" + " " + "is present.");
                }

                if (!StringUtils.isBlank(issuer.getSPNameQualifier())) {
                    throw new WSSecurityException(ErrorCode.FAILED_CHECK,"SOAP header element Security/Assertion/Issuer/@Format = " + ""
                            + "urn:oasis:names:tc:SAML:1.1:nameid-format:entity" + "" + "and"
                            + "Security/Assertion/Issuer/@SPNameQualifier" + " " + "is present.");

                }
            }

            try {
                for (ValidatorSuite v : validators) {
                    v.validate((XMLObject)assertion.getSaml2());
                }
            } catch (ValidationException e) {
                LOG.error("Saml Validation error: " + e.getMessage(), e);
                throw new WSSecurityException(ErrorCode.FAILED_CHECK, "invalidSAMLsecurity");
            }

            for (AuthzDecisionStatement auth : assertion.getSaml2().getAuthzDecisionStatements()) {
                if (StringUtils.equals(auth.getResource(), TEMP_RESOURCE_FOR_VALIDATION)) {
                    auth.setResource(StringUtils.EMPTY);
                }
            }
        }
    }

    /**
     * Gets the exchange auth framework validator suite.
     *
     * @return the exchange auth framework validator suite
     */
    protected ValidatorSuite getExchangeAuthFrameworkValidatorSuite() {
        ValidatorSuite specValidator = org.opensaml.Configuration.getValidatorSuite(EXCHANGE_AUTH_FRWK_VALIDATOR_SUITE);

        if (specValidator == null) {
            QName qName = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion", "saml2");
            specValidator = new ValidatorSuite(EXCHANGE_AUTH_FRWK_VALIDATOR_SUITE);
            specValidator.registerValidator(qName, new Saml2ExchangeAuthFrameworkValidator());
            org.opensaml.Configuration.registerValidatorSuite(EXCHANGE_AUTH_FRWK_VALIDATOR_SUITE, specValidator);
        }

        return specValidator;
    }

    /**
     * Gets the saml2 spec validators.
     *
     * @return the saml2 spec validators
     */
    protected Collection<ValidatorSuite> getSaml2SpecValidators() {
        try {
            Boolean allowNoSubjectAssertion = propertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
                    ALLOW_NO_SUBJECT_ASSERTION_PROP);

            if (allowNoSubjectAssertion) {
                return getSaml2AllowNoSubjectAssertionSpecValidators();
            } else {
                return getSaml2DefaultAssertionSpecValidators();
            }

        } catch (Exception e) {
            LOG.warn("Failed to get SAML 2 assertion validator. " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Gets the saml2 allow no subject assertion spec validators.
     *
     * @return the saml2 allow no subject assertion spec validators
     */
    protected Collection<ValidatorSuite> getSaml2AllowNoSubjectAssertionSpecValidators() {
        return Collections.singleton(getSaml2AllowNoSubjectAssertionSpecValidator());
    }

    /**
     * Gets the saml2 allow no subject assertion spec validator.
     *
     * @return the saml2 allow no subject assertion spec validator
     */
    protected ValidatorSuite getSaml2AllowNoSubjectAssertionSpecValidator() {
        ValidatorSuite specValidators = org.opensaml.Configuration.getValidatorSuite(ALLOW_NO_SUBJECT_ASSERTION_ID);

        if (specValidators == null) {
            QName qName = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion", "saml2");
            specValidators = new ValidatorSuite(ALLOW_NO_SUBJECT_ASSERTION_ID);
            specValidators.registerValidator(qName, new Saml2AllowNoSubjectAssertionSpecValidator());
            org.opensaml.Configuration.registerValidatorSuite(ALLOW_NO_SUBJECT_ASSERTION_ID, specValidators);
        }

        return specValidators;
    }

    /**
     * Gets the saml2 assertion spec validator.
     *
     * @return the saml2 assertion spec validator
     */
    protected Collection<ValidatorSuite> getSaml2DefaultAssertionSpecValidators() {
        Collection<ValidatorSuite> suites = new HashSet<>();
        suites.add(org.opensaml.Configuration.getValidatorSuite("saml2-core-spec-validator"));
        suites.add(getExchangeAuthFrameworkValidatorSuite());
        return suites;
    }

    
    
    /* (non-Javadoc)
     * @see org.apache.wss4j.dom.validate.SamlAssertionValidator#validate(org.apache.wss4j.dom.validate.Credential, org.apache.wss4j.dom.handler.RequestData)
     */
    @Override
    public Credential validate(Credential credential, RequestData data) throws WSSecurityException {
        if (credential == null || credential.getSamlAssertion() == null) {
            throw new WSSecurityException(ErrorCode.FAILED_CHECK, "noCredential");
        }
        SamlAssertionWrapper assertion = credential.getSamlAssertion();

        // Check HOK requirements
        String confirmMethod = null;
        List<String> methods = assertion.getConfirmationMethods();
        if (methods != null && methods.size() > 0) {
            confirmMethod = methods.get(0);
        }
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

        // Check conditions
        checkConditions(assertion);

        // Validate the assertion against schemas/profiles
        validateAssertion(assertion);

        checkSignedAssertion(assertion, data);

        return credential;
    }

    /**
     * Check signed assertion.
     *
     * @param assertion the assertion
     * @param data the data
     * @throws WSSecurityException the wS security exception
     */
    protected void checkSignedAssertion(SamlAssertionWrapper assertion, RequestData data) throws WSSecurityException {

        SAMLKeyInfo samlKeyInfo = assertion.getSignatureKeyInfo();
        X509Certificate[] certs = samlKeyInfo.getCerts();
        PublicKey publicKey = samlKeyInfo.getPublicKey();

        try {
            super.verifySignedAssertion(assertion, data);
        } catch (WSSecurityException e) {
            if (certs == null && publicKey != null) {
                LOG.warn("Could not establish trust of the signature's public key because no matching public key "
                        + "exists in the truststore. Please see GATEWAY-3146 for more details.");
            } else {
                throw e;
            }
        }
    }

}
