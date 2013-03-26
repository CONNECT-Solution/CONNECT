package gov.hhs.fha.nhinc.callback.cxf;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.saml.SAMLKeyInfo;
import org.apache.ws.security.saml.ext.AssertionWrapper;
import org.apache.ws.security.saml.ext.OpenSAMLUtil;
import org.apache.ws.security.validate.Credential;
import org.apache.ws.security.validate.SamlAssertionValidator;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.ValidatorSuite;

/**
 * {@inheritDoc}
 * 
 * In addition, this class can be configured to allow Assertions with No
 * Subjects. This is required for interoperability with previous CONNECT
 * gateways.
 */
public class CONNECTSamlAssertionValidator extends SamlAssertionValidator {

	private static final Logger LOG = Logger
			.getLogger(CONNECTSamlAssertionValidator.class);

	private static final String ALLOW_NO_SUBJECT_ASSERTION_PROP = "allowNoSubjectAssertion";
	private static final String ALLOW_NO_SUBJECT_ASSERTION_ID = "saml2-core-spec-validator-allow-no-subject-assertion";
	private PropertyAccessor propertyAccessor;

	public CONNECTSamlAssertionValidator() {
		propertyAccessor = PropertyAccessor.getInstance();
	}

	public CONNECTSamlAssertionValidator(PropertyAccessor propertyAccessor) {
		this.propertyAccessor = propertyAccessor;
	}

	/**
	 * Validate the assertion against schemas/profiles
	 */
	@Override
	protected void validateAssertion(AssertionWrapper assertion)
			throws WSSecurityException {
		if (assertion.getSaml1() != null) {
			ValidatorSuite schemaValidators = org.opensaml.Configuration
					.getValidatorSuite("saml1-schema-validator");
			ValidatorSuite specValidators = org.opensaml.Configuration
					.getValidatorSuite("saml1-spec-validator");
			try {
				schemaValidators.validate(assertion.getSaml1());
				specValidators.validate(assertion.getSaml1());
			} catch (ValidationException e) {
				LOG.debug("Saml Validation error: " + e.getMessage(), e);
				throw new WSSecurityException(WSSecurityException.FAILURE,
						"invalidSAMLsecurity");
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
				throw new WSSecurityException(WSSecurityException.FAILURE,
						"invalidSAMLsecurity");
			}
		}
	}

	protected ValidatorSuite getSaml2SpecValidator() {
		try {
			Boolean allowNoSubjectAssertion = propertyAccessor
					.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
							ALLOW_NO_SUBJECT_ASSERTION_PROP);

			if (allowNoSubjectAssertion) {
				return getSaml2AllowNoSubjectAssertionSpecValidator();
			} else {
				return getSaml2AssertionSpecValidator();
			}

		} catch (Exception e) {
			LOG.warn(
					"Failed to get SAML 2 assertion validator. "
							+ e.getMessage(), e);
			return null;
		}
	}

	protected ValidatorSuite getSaml2AllowNoSubjectAssertionSpecValidator() {
		ValidatorSuite specValidators = org.opensaml.Configuration
				.getValidatorSuite(ALLOW_NO_SUBJECT_ASSERTION_ID);

		if (specValidators == null) {
			QName qName = new QName("urn:oasis:names:tc:SAML:2.0:assertion",
					"Assertion", "saml2");
			specValidators = new ValidatorSuite(ALLOW_NO_SUBJECT_ASSERTION_ID);
			specValidators.registerValidator(qName,
					new Saml2AllowNoSubjectAssertionSpecValidator());
			org.opensaml.Configuration.registerValidatorSuite(
					ALLOW_NO_SUBJECT_ASSERTION_ID, specValidators);
		}

		return specValidators;
	}

	protected ValidatorSuite getSaml2AssertionSpecValidator() {
		return org.opensaml.Configuration
				.getValidatorSuite("saml2-core-spec-validator");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ws.security.validate.SamlAssertionValidator(
	 * org.apache.ws.security.validate.Credential,
	 * org.apache.ws.security.handler.RequestData)
	 */
	@Override
	public Credential validate(Credential credential, RequestData data)
			throws WSSecurityException {
		if (credential == null || credential.getAssertion() == null) {
			throw new WSSecurityException(WSSecurityException.FAILURE,
					"noCredential");
		}
		AssertionWrapper assertion = credential.getAssertion();

		// Check HOK requirements
		String confirmMethod = null;
		List<String> methods = assertion.getConfirmationMethods();
		if (methods != null && methods.size() > 0) {
			confirmMethod = methods.get(0);
		}
		if (OpenSAMLUtil.isMethodHolderOfKey(confirmMethod)) {
			if (assertion.getSubjectKeyInfo() == null) {
				LOG.debug("There is no Subject KeyInfo to match the holder-of-key subject conf method");
				throw new WSSecurityException(WSSecurityException.FAILURE,
						"noKeyInSAMLToken");
			}
			// The assertion must have been signed for HOK
			if (!assertion.isSigned()) {
				LOG.debug("A holder-of-key assertion must be signed");
				throw new WSSecurityException(WSSecurityException.FAILURE,
						"invalidSAMLsecurity");
			}
		}

		// Check conditions
		checkConditions(assertion);

		// Validate the assertion against schemas/profiles
		validateAssertion(assertion);

		checkSignedAssertion(assertion, data);

		return credential;
	}

	protected void checkSignedAssertion(AssertionWrapper assertion,
			RequestData data) throws WSSecurityException {

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
