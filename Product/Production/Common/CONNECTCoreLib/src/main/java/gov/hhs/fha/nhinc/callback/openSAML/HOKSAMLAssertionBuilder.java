/**
 *
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Evidence;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Statement;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;
import org.w3c.dom.Element;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 * @author bhumphrey
 *
 */
public class HOKSAMLAssertionBuilder extends SAMLAssertionBuilder {

    private static Log log = LogFactory.getLog(HOKSAMLAssertionBuilder.class);

	private final CertificateManager certificateManager;

	/**
	 * @param properties
	 * @throws Exception
	 */
	public HOKSAMLAssertionBuilder() {
		certificateManager = CertificateManagerImpl.getInstance();
	}

	HOKSAMLAssertionBuilder(CertificateManager certificateManager) {
		this.certificateManager = certificateManager;
	}

	/**
	 * Creates the "Holder-of-Key" variant of the SAML Assertion token.
	 *
	 * @return The Assertion element
	 * @throws Exception
	 */
	@Override
    public Element build(CallbackProperties properties) throws Exception {
		log.debug("SamlCallbackHandler.createHOKSAMLAssertion20() -- Begin");
		Element signedAssertion = null;
		try {
			Assertion assertion = null;
			assertion = OpenSAML2ComponentBuilder.getInstance()
					.createAssertion();

			// create the assertion id
			// Per GATEWAY-847 the id attribute should not be allowed to start
			// with a number (UUIDs can). Direction
			// given from 2011 specification set was to prepend with and
			// underscore.
			String aID = ID_PREFIX.concat(String.valueOf(UUID.randomUUID())).replaceAll("-", "");
			log.debug("Assertion ID: " + aID);

			// set assertion Id
			assertion.setID(aID);

			// issue instant set to now.
			DateTime issueInstant = new DateTime();
			assertion.setIssueInstant(issueInstant);

			// set issuer
			assertion.setIssuer(createIssuer(properties));


			X509Certificate certificate = certificateManager
					.getDefaultCertificate();
			PublicKey publicKey = certificateManager.getDefaultPublicKey();

			// set subject
			Subject subject = createSubject(properties,certificate, publicKey);
			assertion.setSubject(subject);

			// add attribute statements
			Subject evidenceSubject = createEvidenceSubject(properties,certificate, publicKey);
			assertion.getStatements().addAll(
					createAttributeStatements(properties, evidenceSubject));


			PrivateKey privateKey = certificateManager.getDefaultPrivateKey();
			// sign the message
			signedAssertion = sign(assertion, certificate, privateKey, publicKey);
		} catch (Exception ex) {
			log.error("Unable to create HOK Assertion: " + ex.getMessage());
			throw ex;
		}
		log.debug("SamlCallbackHandler.createHOKSAMLAssertion20() -- End");
		return signedAssertion;
	}

	/**
	 * @param assertion
	 * @param privateKey
	 * @param certificate
	 * @throws Exception
	 */
	static Element sign(Assertion assertion, X509Certificate certificate,
			PrivateKey privateKey, PublicKey publicKey) throws Exception {
		Signature signature = OpenSAML2ComponentBuilder.getInstance()
				.createSignature(certificate, privateKey, publicKey);
		assertion.setSignature(signature);

		// marshall Assertion Java class into XML
		MarshallerFactory marshallerFactory = Configuration
				.getMarshallerFactory();
		Marshaller marshaller = marshallerFactory.getMarshaller(assertion);
		Element assertionElement = marshaller.marshall(assertion);
		try {
			Signer.signObject(signature);
		} catch (SignatureException e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return assertionElement;
	}

	static Issuer createIssuer(CallbackProperties properties) {
		Issuer issuer = null;

		String format = properties.getAssertionIssuerFormat();
		if (format != null) {
			log.debug("Setting Assertion Issuer format to: " + format);
			String sIssuer = properties.getIssuer();

			log.debug("Setting Assertion Issuer to: " + sIssuer);

			if (isValidNameidFormat(format)) {
				issuer = OpenSAML2ComponentBuilder.getInstance()
						.createIssuer(format, sIssuer);
			} else {
				log.debug("Not in valid listing of formats: Using default issuer");
				issuer = OpenSAML2ComponentBuilder.getInstance()
						.createDefaultIssuer();
			}
		} else {
			log.debug("Assertion issuer not defined: Using default issuer");
			issuer = OpenSAML2ComponentBuilder.getInstance()
					.createDefaultIssuer();
		}
		return issuer;
	}

	/**
	 * @param PSApk
	 * @param certificate
	 * @return
	 * @throws Exception
	 */
    static Subject createSubject(CallbackProperties properties, X509Certificate certificate, PublicKey publicKey)
            throws Exception {
        String x509Name = "UID=" + properties.getUsername();

        return createSubject(x509Name, certificate, publicKey);
    }

    static Subject createEvidenceSubject(CallbackProperties properties, X509Certificate certificate,
            PublicKey publicKey)
            throws Exception {
        String evidenceSubject = properties.getEvidenceSubject();
        if (NullChecker.isNullish(evidenceSubject)) {
            evidenceSubject = properties.getUsername();
        }

        String x509Name = "UID=" + evidenceSubject;

        return createSubject(x509Name, certificate, publicKey);
    }

    static Subject createSubject(String x509Name, X509Certificate certificate, PublicKey publicKey)
            throws Exception {
        Subject subject;
        subject = OpenSAML2ComponentBuilder.getInstance().createSubject(x509Name,
				certificate, publicKey);
		return subject;
    }

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static List<Statement> createAttributeStatements(
			CallbackProperties properties, Subject subject) {
		List<Statement> statements = new ArrayList<Statement>();

		statements.addAll(createAuthenicationStatements(properties));


		statements.addAll(createUserNameAttributeStatements(properties));
		statements.addAll(createOrganizationAttributeStatements(properties));
		statements.addAll(createHomeCommunityIdAttributeStatements(properties));
		statements.addAll(createPatientIdAttributeStatements(properties));
		statements.addAll(createUserRoleStatements(properties));
		statements.addAll(createPurposeOfUseStatements(properties));

		statements.addAll(createAuthenicationDecsionStatements(properties, subject));

		return statements;
	}




	static List<AuthnStatement> createAuthenicationStatements(
			CallbackProperties properties) {

		List<AuthnStatement> authnStatements = new ArrayList<AuthnStatement>();

		String cntxCls = properties.getAuthenticationContextClass();
		if (cntxCls == null) {
			cntxCls = X509_AUTHN_CNTX_CLS;
		} else if (!isValidAuthnCntxCls(cntxCls)) {
			cntxCls = UNSPECIFIED_AUTHN_CNTX_CLS;
		}
		String sessionIndex = properties.getAuthenticationSessionIndex();

		log.debug("Setting Authentication session index to: " + sessionIndex);

		DateTime authInstant = properties.getAuthenticationInstant();
		if (authInstant == null) {
			authInstant = new DateTime();
		}

		String inetAddr = properties.getSubjectLocality();
		String dnsName = properties.getSubjectLocality();

		AuthnStatement authnStatement = OpenSAML2ComponentBuilder.getInstance()
				.createAuthenicationStatements(cntxCls, sessionIndex,
						authInstant, inetAddr, dnsName);

		authnStatements.add(authnStatement);

		return authnStatements;
	}

	/**
	 * @return
	 */
	static List<AuthzDecisionStatement> createAuthenicationDecsionStatements(
			CallbackProperties properties, Subject subject) {
		List<AuthzDecisionStatement> authDecisionStatements = new ArrayList<AuthzDecisionStatement>();

		Boolean hasAuthzStmt = properties.getAuthenicationStatementExists();
		// The authorization Decision Statement is optional
		if (hasAuthzStmt) {
			// Create resource for Authentication Decision Statement
			String resource = properties.getAuthnicationResource();

			// Options are Permit, Deny and Indeterminate
			String decision = properties.getAuthenicationDecision();
			if (decision == null) {
				decision = AUTHZ_DECISION_PERMIT;
			}

			if (!isValidAuthenicationDescision(decision)) {
				decision = AUTHZ_DECISION_PERMIT;
			}

			// As of Authorization Framework Spec 2.2 Action is a hard-coded
			// value
			// Therefore the value of the ACTION_PROP is no longer used
			String action = AUTHZ_DECISION_ACTION_EXECUTE;

			Evidence evidence = createEvidence(properties, subject);

			authDecisionStatements.add(OpenSAML2ComponentBuilder.getInstance()
					.createAuthzDecisionStatement(resource, decision, action,
							evidence));
		}

		return authDecisionStatements;
	}

	/**
	 * Creates the Evidence element that encompasses the Assertion defining the
	 * authorization form needed in cases where evidence of authorization to
	 * access the medical records must be provided along with the message
	 * request.
	 *
	 * @param factory
	 *            The factory object used to assist in the construction of the
	 *            SAML Assertion token
	 * @param issueInstant
	 *            The calendar representing the time of Assertion issuance
	 * @return The Evidence element
	 */
	static Evidence createEvidence(CallbackProperties properties, Subject subject) {
		log.debug("SamlCallbackHandler.createEvidence() -- Begin");
		String evAssertionID = properties.getEvidenceID();
		DateTime issueInstant = properties.getEvidenceInstant();
		String format = properties.getEvidenceIssuerFormat();
		DateTime beginValidTime = properties.getEvidenceConditionNotBefore();
		DateTime endValidTime = properties.getEvidenceConditionNotAfter();
		List<AttributeStatement> statements = createEvidenceStatements(properties);

		String issuer = properties.getEvidenceIssuer();

		return buildEvidence(evAssertionID, issueInstant, format, beginValidTime, endValidTime, issuer, statements, subject);
	}

    public static Evidence buildEvidence(String evAssertionID, DateTime issueInstant,
            String format, DateTime beginValidTime, DateTime endValidTime, String issuer,
            List<AttributeStatement> statements, Subject subject) {
        DateTime now = new DateTime();

		List<Assertion> evidenceAssertions = new ArrayList<Assertion>();
		if (evAssertionID == null) {
			evAssertionID = ID_PREFIX.concat(String.valueOf(UUID.randomUUID()));
		}

		if (issueInstant == null) {
			issueInstant = new DateTime();
		}

		if (!isValidIssuerFormat(format)) {
			format = X509_NAME_ID;
		}

        Issuer evIssuerId = OpenSAML2ComponentBuilder.getInstance().createIssuer(format, issuer);

        if (beginValidTime == null || beginValidTime.isAfter(now)) {
            beginValidTime = now;
        }

        // If provided time is after the given issue instant,
        // modify it to include the issue instant
        if (beginValidTime.isAfter(issueInstant)) {
            if (issueInstant.isAfter(now)) {
                beginValidTime = now;
            } else {
                beginValidTime = issueInstant;
            }
        }
        
        // Make end datetime at a minimum 5 minutes from now
        if (endValidTime == null || endValidTime.isBefore(now.plusMinutes(5))) {
            endValidTime = now.plusMinutes(5);
        }

        // Ensure issueInstant is contained within valid times
        if (endValidTime.isBefore(issueInstant)) {
            endValidTime = issueInstant.plusMinutes(5);
        }

        Conditions conditions = OpenSAML2ComponentBuilder.getInstance().createConditions(beginValidTime, endValidTime,
                null);

		Assertion evidenceAssertion = OpenSAML2ComponentBuilder.getInstance()
				.createAssertion(evAssertionID);

		evidenceAssertion.getAttributeStatements().addAll(statements);
		evidenceAssertion.setConditions(conditions);
		evidenceAssertion.setIssueInstant(issueInstant);
		evidenceAssertion.setIssuer(evIssuerId);
		evidenceAssertion.setSubject(subject);

		evidenceAssertions.add(evidenceAssertion);

		Evidence evidence = OpenSAML2ComponentBuilder.getInstance().createEvidence(evidenceAssertions);

		log.debug("SamlCallbackHandler.createEvidence() -- End");
		return evidence;
    }

	/**
	 * Creates the Attribute Statements needed for the Evidence element. These
	 * include the Attributes for the Access Consent Policy and the Instance
	 * Access Consent Policy
	 *
	 * @param factory
	 *            The factory object used to assist in the construction of the
	 *            SAML Assertion token
	 * @return The listing of the attribute statements for the Evidence element
	 */
	static List<AttributeStatement> createEvidenceStatements(
			CallbackProperties properties) {
		log.debug("SamlCallbackHandler.createEvidenceStatements() -- Begin");

		List accessConstentValues = properties.getEvidenceAccessConstent();
		List evidenceInstanceAccessConsentValues = properties
		        .getEvidenceInstantAccessConsent();

		return createEvidenceStatements(accessConstentValues, evidenceInstanceAccessConsentValues);
	}

    public static List<AttributeStatement> createEvidenceStatements(List accessConstentValues,
            List evidenceInstanceAccessConsentValues) {
        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
		if (accessConstentValues == null) {
			log.debug("No Access Consent found for Evidence");
		}

		// Set the Instance Access Consent
		if (evidenceInstanceAccessConsentValues == null) {
			log.debug("No Instance Access Consent found for Evidence");
		}

		statements = OpenSAML2ComponentBuilder.getInstance()
				.createEvidenceStatements(accessConstentValues,
						evidenceInstanceAccessConsentValues, NHIN_NS);

		log.debug("SamlCallbackHandler.createEvidenceStatements() -- End");
		return statements;
    }

	/**
	 * Creates the Attribute statements for UserName.
	 *
	 */
	static List<AttributeStatement> createUserNameAttributeStatements(
			CallbackProperties properties) {

		List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
		List<Attribute> attributes = new ArrayList<Attribute>();

		// Set the User Name Attribute
		List<String> userNameValues = new ArrayList<String>();
		String nameConstruct = properties.getUserFullName();

		if (nameConstruct.length() > 0) {
			log.debug("UserName: " + nameConstruct);

			userNameValues.add(nameConstruct);

			attributes.add(OpenSAML2ComponentBuilder.getInstance()
					.createAttribute(null, SamlConstants.USERNAME_ATTR, null,
							userNameValues));
		} else {
			log.warn("No information provided to fill in user name attribute");
		}
		if (!attributes.isEmpty()) {
			statements.addAll(OpenSAML2ComponentBuilder.getInstance()
					.createAttributeStatement(attributes));
		}

		return statements;
	}

	/**
	 * Creates the Attribute statements UserRole
	 *
	 * @param factory
	 *            The factory object used to assist in the construction of the
	 *            SAML Assertion token
	 * @return The listing of all Attribute statements
	 */
	static List<AttributeStatement> createUserRoleStatements(
			CallbackProperties properties) {
		List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
		List<Attribute> attributes = new ArrayList<Attribute>();

		// Set the User Role Attribute

		String userCode = properties.getUserCode();
		String userSystem = properties.getUserSystem();
		String userSystemName = properties.getUserSystemName();
		String userDisplay = properties.getUserDisplay();

		attributes.add(OpenSAML2ComponentBuilder.getInstance()
				.createUserRoleAttribute(userCode, userSystem, userSystemName,
						userDisplay));

		if (!attributes.isEmpty()) {
			statements.addAll(OpenSAML2ComponentBuilder.getInstance()
					.createAttributeStatement(attributes));
		}

		return statements;

	}

	/**
	 * Creates the Attribute statements PurposeOfUse
	 *
	 * @param factory
	 *            The factory object used to assist in the construction of the
	 *            SAML Assertion token
	 * @return The listing of all Attribute statements
	 */
	static List<AttributeStatement> createPurposeOfUseStatements(
			CallbackProperties properties) {
		List<AttributeStatement> statements = new ArrayList<AttributeStatement>();

		final String purposeCode = properties.getPurposeCode();
		final String purposeSystem = properties.getPurposeSystem();
		final String purposeSystemName = properties.getPurposeSystemName();
		final String purposeDisplay = properties.getPurposeDisplay();

		/*
		 * Gateway-347 - Support for both values will remain until NHIN Specs
		 * updated Determine whether to use PurposeOfUse or PuposeForUse
		 */
		if (isPurposeForUseEnabled(properties)) {
			statements = OpenSAML2ComponentBuilder.getInstance()
					.createPurposeForUseAttributeStatements(purposeCode, purposeSystem,
							purposeSystemName, purposeDisplay);
		} else {
			statements = OpenSAML2ComponentBuilder.getInstance()
					.createPurposeOfUseAttributeStatements(purposeCode, purposeSystem,
							purposeSystemName, purposeDisplay);
		}



		return statements;

	}

	/**
	 * Creates the Attribute statements for UserName, UserOrganization,
	 * UserRole, and PurposeOfUse
	 *
	 * @param factory
	 *            The factory object used to assist in the construction of the
	 *            SAML Assertion token
	 * @return The listing of all Attribute statements
	 */
	static List<AttributeStatement> createOrganizationAttributeStatements(
			CallbackProperties properties) {

		log.debug("SamlCallbackHandler.addAssertStatements() -- Begin");
		List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
		List<Attribute> attributes = new ArrayList<Attribute>();

		// Set the User Organization ID Attribute
		final String organizationId = properties.getUserOrganization();
		if (organizationId != null) {
			attributes.add(OpenSAML2ComponentBuilder.getInstance()
					.createAttribute(null, SamlConstants.USER_ORG_ATTR, null,
							Arrays.asList(organizationId)));
		}

		if (!attributes.isEmpty()) {
			statements.addAll(OpenSAML2ComponentBuilder.getInstance()
					.createAttributeStatement(attributes));
		}

		log.debug("SamlCallbackHandler.addAssertStatements() -- End");
		return statements;

	}

	/**
	 * Creates the Attribute statements for UserName, UserOrganization,
	 * UserRole, and PurposeOfUse
	 *
	 * @param factory
	 *            The factory object used to assist in the construction of the
	 *            SAML Assertion token
	 * @return The listing of all Attribute statements
	 */
	static List<AttributeStatement> createHomeCommunityIdAttributeStatements(
			CallbackProperties properties) {

		log.debug("SamlCallbackHandler.addAssertStatements() -- Begin");
		List<AttributeStatement> statements = Collections.EMPTY_LIST;

		// Set the Home Community ID Attribute

		final String communityId = properties.getHomeCommunity();
		if (communityId != null) {

			statements = OpenSAML2ComponentBuilder.getInstance()
					.createHomeCommunitAttributeStatement(communityId);
		} else {
			log.debug("Home Community ID is missing");
		}

		return statements;

	}

	/**
	 * Creates the Attribute statements for UserName, UserOrganization,
	 * UserRole, and PurposeOfUse
	 *
	 * @param factory
	 *            The factory object used to assist in the construction of the
	 *            SAML Assertion token
	 * @return The listing of all Attribute statements
	 */
	static List<AttributeStatement> createPatientIdAttributeStatements(
			CallbackProperties properties) {

		List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
		Attribute attribute;

		// Set the Patient ID Attribute
		final String patientId = properties.getPatientID();
		if (patientId != null) {
			attribute = OpenSAML2ComponentBuilder.getInstance()
					.createPatientIDAttribute(patientId);

			statements.addAll(OpenSAML2ComponentBuilder.getInstance()
					.createAttributeStatement(Arrays.asList(attribute)));
		} else {
			log.debug("patient id is missing");
		}
		return statements;

	}
}
