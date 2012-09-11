package gov.hhs.fha.nhinc.callback.cxf;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import javax.xml.namespace.QName;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.saml.ext.AssertionWrapper;
import org.apache.ws.security.validate.SamlAssertionValidator;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.ValidatorSuite;

/**
 * {@inheritDoc}
 * 
 * In addition, this class can be configured to allow Assertions with No Subjects. This is required for interoperability
 * with previous CONNECT gateways.
 */
public class CONNECTSamlAssertionValidator extends SamlAssertionValidator {

    private static org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory
            .getLog(CONNECTSamlAssertionValidator.class);

    private static final String ALLOW_NO_SUBJECT_ASSERTION_PROP = "allowNoSubjectAssertion";
    private static final String ALLOW_NO_SUBJECT_ASSERTION_ID = "saml2-core-spec-validator-allow-no-subject-assertion";

    /**
     * Validate the assertion against schemas/profiles
     */
    @Override
    protected void validateAssertion(AssertionWrapper assertion) throws WSSecurityException {
        if (assertion.getSaml1() != null) {
            ValidatorSuite schemaValidators = org.opensaml.Configuration.getValidatorSuite("saml1-schema-validator");
            ValidatorSuite specValidators = org.opensaml.Configuration.getValidatorSuite("saml1-spec-validator");
            try {
                schemaValidators.validate(assertion.getSaml1());
                specValidators.validate(assertion.getSaml1());
            } catch (ValidationException e) {
                LOG.debug("Saml Validation error: " + e.getMessage(), e);
                throw new WSSecurityException(WSSecurityException.FAILURE, "invalidSAMLsecurity");
            }
        } else if (assertion.getSaml2() != null) {
            ValidatorSuite schemaValidators = org.opensaml.Configuration
                    .getValidatorSuite("saml2-core-schema-validator");

            ValidatorSuite specValidators = getSaml2SpecValidator();

            try {
                schemaValidators.validate(assertion.getSaml2());
                specValidators.validate(assertion.getSaml2());
            } catch (ValidationException e) {
                LOG.error("Saml Validation error: " + e.getMessage(), e);
                throw new WSSecurityException(WSSecurityException.FAILURE, "invalidSAMLsecurity");
            }
        }
    }

    protected ValidatorSuite getSaml2SpecValidator() {
        try {
            Boolean allowNoSubjectAssertion = PropertyAccessor.getInstance().getPropertyBoolean(
                    NhincConstants.GATEWAY_PROPERTY_FILE, ALLOW_NO_SUBJECT_ASSERTION_PROP);

            if (allowNoSubjectAssertion) {
                return getSaml2AllowNoSubjectAssertionSpecValidator();
            } else {
                return getSaml2AssertionSpecValidator();
            }

        } catch (Exception e) {
            LOG.warn("Failed to get SAML 2 assertion validator. " + e.getMessage(), e);
            return null;
        }
    }

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

    protected ValidatorSuite getSaml2AssertionSpecValidator() {
        return org.opensaml.Configuration.getValidatorSuite("saml2-core-spec-validator");
    }

}
