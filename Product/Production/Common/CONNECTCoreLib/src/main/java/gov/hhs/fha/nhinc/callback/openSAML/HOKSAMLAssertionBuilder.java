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
package gov.hhs.fha.nhinc.callback.openSAML;

import gov.hhs.fha.nhinc.callback.PurposeOfForDecider;
import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.naming.Name;
import javax.naming.ldap.LdapName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * @author bhumphrey
 *
 */
public class HOKSAMLAssertionBuilder extends SAMLAssertionBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(HOKSAMLAssertionBuilder.class);
    private final CertificateManager certificateManager;

    /**
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
     * @param properties
     * @return The Assertion element
     * @throws Exception
     */
    @Override
    public Element build(CallbackProperties properties) throws Exception {
        LOG.debug("SamlCallbackHandler.createHOKSAMLAssertion20() -- Begin");
        Element signedAssertion;
        try {
            Assertion assertion;
            assertion = OpenSAML2ComponentBuilder.getInstance().createAssertion();

            // create the assertion id
            // Per GATEWAY-847 the id attribute should not be allowed to start
            // with a number (UUIDs can). Direction
            // given from 2011 specification set was to prepend with and
            // underscore.
            String aID = createAssertionId();
            LOG.debug("Assertion ID: " + aID);

            // set assertion Id
            assertion.setID(aID);

            // issue instant set to now.
            DateTime issueInstant = new DateTime();
            assertion.setIssueInstant(issueInstant);

            // set issuer
            assertion.setIssuer(createIssuer(properties));

            X509Certificate certificate = certificateManager.getDefaultCertificate();
            PublicKey publicKey = certificateManager.getDefaultPublicKey();

            // set subject
            Subject subject = createSubject(properties, certificate, publicKey);
            assertion.setSubject(subject);

            // add attribute statements
            Subject evidenceSubject = createEvidenceSubject(properties, certificate, publicKey);
            assertion.getStatements().addAll(createAttributeStatements(properties, evidenceSubject));

            PrivateKey privateKey = certificateManager.getDefaultPrivateKey();
            // sign the message
            signedAssertion = sign(assertion, certificate, privateKey, publicKey);
        } catch (Exception ex) {
            LOG.error("Unable to create HOK Assertion: " + ex.getMessage());
            throw ex;
        }
        LOG.debug("SamlCallbackHandler.createHOKSAMLAssertion20() -- End");
        return signedAssertion;
    }

    /**
     * @param assertion
     * @param privateKey
     * @param certificate
     * @param publicKey
     * @return
     * @throws Exception
     */
    protected Element sign(Assertion assertion, X509Certificate certificate, PrivateKey privateKey, PublicKey publicKey)
        throws Exception {
        Signature signature = OpenSAML2ComponentBuilder.getInstance().createSignature(certificate, privateKey,
            publicKey);
        assertion.setSignature(signature);

        // marshall Assertion Java class into XML
        MarshallerFactory marshallerFactory = Configuration.getMarshallerFactory();
        Marshaller marshaller = marshallerFactory.getMarshaller(assertion);
        Element assertionElement = marshaller.marshall(assertion);
        try {
            Signer.signObject(signature);
        } catch (SignatureException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new Exception(e);
        }
        return assertionElement;
    }

    protected Issuer createIssuer(CallbackProperties properties) {
        Issuer issuer;

        String format = properties.getAssertionIssuerFormat();
        if (format != null) {
            LOG.debug("Setting Assertion Issuer format to: " + format);
            String sIssuer = properties.getIssuer();

            LOG.debug("Setting Assertion Issuer to: " + sIssuer);

            if (isValidNameidFormat(format)) {
                issuer = OpenSAML2ComponentBuilder.getInstance().createIssuer(format, sIssuer);
            } else {
                LOG.debug("Not in valid listing of formats: Using default issuer");
                issuer = OpenSAML2ComponentBuilder.getInstance().createDefaultIssuer();
            }
        } else {
            LOG.debug("Assertion issuer not defined: Using default issuer");
            issuer = OpenSAML2ComponentBuilder.getInstance().createDefaultIssuer();
        }
        return issuer;
    }

    /**
     * @param PSApk
     * @param certificate
     * @return
     * @throws Exception
     */
    protected Subject createSubject(CallbackProperties properties, X509Certificate certificate, PublicKey publicKey)
        throws Exception {
        String x509Name = properties.getUsername();

        if (NullChecker.isNullish(x509Name) || !checkDistinguishedName(x509Name)) {
            if (null != certificate && null != certificate.getSubjectDN()) {
                x509Name = certificate.getSubjectDN().getName();
            }
        }
        return createSubject(x509Name, certificate, publicKey);
    }

    /**
     *
     * @param userName
     * @return boolean
     */
    private boolean checkDistinguishedName(String userName) {
        boolean isValid = true;
        try {
            Name name = new LdapName(userName);
        } catch (Exception e) {
            LOG.warn("Not a Valid Distinguished Name, setting the value from Certificate..");
            isValid = false;
        }
        return isValid;
    }

    protected Subject createEvidenceSubject(CallbackProperties properties, X509Certificate certificate,
        PublicKey publicKey)
        throws Exception {
        String evidenceSubject = properties.getEvidenceSubject();
        String x509Name;
        if (NullChecker.isNullish(evidenceSubject)) {
            String userName = properties.getUsername();

            if (NullChecker.isNullish(userName) || !checkDistinguishedName(userName)) {
                if (null != certificate && null != certificate.getSubjectDN()) {
                    userName = certificate.getSubjectDN().getName();
                }
            }
            x509Name = userName;
        } else {
            x509Name = evidenceSubject;
        }
        return createSubject(x509Name, certificate, publicKey);
    }

    protected Subject createSubject(String x509Name, X509Certificate certificate,
        PublicKey publicKey) throws Exception {
        Subject subject;
        subject = OpenSAML2ComponentBuilder.getInstance().createSubject(x509Name, certificate, publicKey);
        return subject;
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Statement> createAttributeStatements(CallbackProperties properties, Subject subject) {
        List<Statement> statements = new ArrayList<Statement>();

        statements.addAll(createAuthenicationStatements(properties));

        statements.addAll(createUserNameAttributeStatements(properties));
        statements.addAll(createOrganizationAttributeStatements(properties));
        statements.addAll(createOrganizationIdAttributeStatements(properties));
        statements.addAll(createHomeCommunityIdAttributeStatements(properties));
        statements.addAll(createPatientIdAttributeStatements(properties));
        statements.addAll(createUserRoleStatements(properties));
        statements.addAll(createPurposeOfUseStatements(properties));
        statements.addAll(createNPIAttributeStatements(properties));

        statements.addAll(createAuthenicationDecsionStatements(properties, subject));

        return statements;
    }

    /**
     * @param properties
     * @return
     */
    protected Collection<? extends Statement> createOrganizationIdAttributeStatements(CallbackProperties properties) {
        LOG.debug("SamlCallbackHandler.createOrganizationIdAttributeStatements() -- Begin");
        List<AttributeStatement> statements = Collections.emptyList();

        // Set the Organization ID Attribute
        final String organizationId = properties.getUserOrganizationId();
        if (organizationId != null) {

            statements = OpenSAML2ComponentBuilder.getInstance().createOrganizationIdAttributeStatement(organizationId);
        } else {
            LOG.debug("Home Community ID is missing");
        }

        return statements;
    }

    public List<AuthnStatement> createAuthenicationStatements(CallbackProperties properties) {

        List<AuthnStatement> authnStatements = new ArrayList<AuthnStatement>();

        String cntxCls = properties.getAuthenticationContextClass();
        if (cntxCls == null) {
            cntxCls = X509_AUTHN_CNTX_CLS;
        } else if (!isValidAuthnCntxCls(cntxCls)) {
            cntxCls = UNSPECIFIED_AUTHN_CNTX_CLS;
        }
        String sessionIndex = properties.getAuthenticationSessionIndex();

        LOG.debug("Setting Authentication session index to: " + sessionIndex);

        DateTime authInstant = properties.getAuthenticationInstant();
        if (authInstant == null) {
            authInstant = new DateTime();
        }

        String inetAddr = properties.getSubjectLocality();
        String dnsName = properties.getSubjectLocality();

        AuthnStatement authnStatement = OpenSAML2ComponentBuilder.getInstance().createAuthenicationStatements(cntxCls,
            sessionIndex, authInstant, inetAddr, dnsName);

        authnStatements.add(authnStatement);

        return authnStatements;
    }

    /**
     * @return
     */
    public List<AuthzDecisionStatement> createAuthenicationDecsionStatements(CallbackProperties properties,
        Subject subject) {
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

            authDecisionStatements.add(OpenSAML2ComponentBuilder.getInstance().createAuthzDecisionStatement(resource,
                decision, action, evidence));
        }

        return authDecisionStatements;
    }

    /**
     * Creates the Evidence element that encompasses the Assertion defining the authorization form needed in cases where
     * evidence of authorization to access the medical records must be provided along with the message request.
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @param issueInstant The calendar representing the time of Assertion issuance
     * @return The Evidence element
     */
    public Evidence createEvidence(CallbackProperties properties, Subject subject) {
        LOG.debug("SamlCallbackHandler.createEvidence() -- Begin");
        String evAssertionID = properties.getEvidenceID();
        DateTime issueInstant = properties.getEvidenceInstant();
        String format = properties.getEvidenceIssuerFormat();
        DateTime beginValidTime = properties.getEvidenceConditionNotBefore();
        DateTime endValidTime = properties.getEvidenceConditionNotAfter();
        List<AttributeStatement> statements = createEvidenceStatements(properties);

        String issuer = properties.getEvidenceIssuer();

        return buildEvidence(evAssertionID, issueInstant, format, beginValidTime, endValidTime, issuer, statements,
            subject);
    }

    /**
     *
     * @param evAssertionID
     * @param issueInstant
     * @param format
     * @param beginValidTime
     * @param endValidTime
     * @param issuer
     * @param statements
     * @param subject
     * @return
     */
    public Evidence buildEvidence(String evAssertionID, DateTime issueInstant, String format,
        DateTime beginValidTime, DateTime endValidTime, String issuer, List<AttributeStatement> statements,
        Subject subject) {

        List<Assertion> evidenceAssertions = new ArrayList<Assertion>();
        if (evAssertionID == null) {
            evAssertionID = createAssertionId();
        } else {
            if (!evAssertionID.startsWith("_")) {
                evAssertionID = ID_PREFIX.concat(evAssertionID);
            }
        }

        if (issueInstant == null) {
            issueInstant = new DateTime();
        }

        if (!isValidIssuerFormat(format)) {
            format = X509_NAME_ID;
        }

        Issuer evIssuerId = OpenSAML2ComponentBuilder.getInstance().createIssuer(format, issuer);

        Assertion evidenceAssertion = OpenSAML2ComponentBuilder.getInstance().createAssertion(evAssertionID);

        evidenceAssertion.getAttributeStatements().addAll(statements);

        //Only set the default value for
        //AuthzDecisionStatement->Evidence->Assertion->Conditions--> notBefore and notOnOrAfter
        //attributes if the enableAuthDecEvidenceConditionsDefaultValue flag
        //enabled or not provided in gateway proeprties
        if (isAuthDEvidenceConditionsDefaultValueEnabled()) {
            DateTime now = new DateTime();

            if (beginValidTime == null || beginValidTime.isAfter(now)) {
                beginValidTime = now;
            }
            // If provided time is after the given issue instant,
            // modify it to include the issue instant
            if (beginValidTime.isAfter(issueInstant)) {
                if (issueInstant.isAfter(now)) {
                    beginValidTime = now;
                }
            } else {
                beginValidTime = issueInstant;
            }

            // Make end datetime at a minimum 5 minutes from now
            if (endValidTime == null || endValidTime.isBefore(now.plusMinutes(5))) {
                endValidTime = now.plusMinutes(5);
            }

            // Ensure issueInstant is contained within valid times
            if (endValidTime.isBefore(issueInstant)) {
                endValidTime = issueInstant.plusMinutes(5);
            }
        }

        //Only create the Conditions if NotBefore and/or NotOnOrAfter is present
        if (beginValidTime != null || endValidTime != null) {
            Conditions conditions = OpenSAML2ComponentBuilder.getInstance().createConditions(beginValidTime,
                endValidTime, null);
            evidenceAssertion.setConditions(conditions);
        }
        evidenceAssertion.setIssueInstant(issueInstant);
        evidenceAssertion.setIssuer(evIssuerId);
        evidenceAssertion.setSubject(subject);

        evidenceAssertions.add(evidenceAssertion);

        Evidence evidence = OpenSAML2ComponentBuilder.getInstance().createEvidence(evidenceAssertions);

        LOG.debug("SamlCallbackHandler.createEvidence() -- End");
        return evidence;
    }

    /**
     * Creates the Attribute Statements needed for the Evidence element. These include the Attributes for the Access
     * Consent Policy and the Instance Access Consent Policy
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of the attribute statements for the Evidence element
     */
    protected List<AttributeStatement> createEvidenceStatements(CallbackProperties properties) {
        LOG.debug("SamlCallbackHandler.createEvidenceStatements() -- Begin");

        List accessConstentValues = properties.getEvidenceAccessConstent();
        List evidenceInstanceAccessConsentValues = properties.getEvidenceInstantAccessConsent();

        return createEvidenceStatements(accessConstentValues, evidenceInstanceAccessConsentValues);
    }

    public List<AttributeStatement> createEvidenceStatements(List accessConstentValues,
        List evidenceInstanceAccessConsentValues) {
        List<AttributeStatement> statements;
        if (accessConstentValues == null) {
            LOG.debug("No Access Consent found for Evidence");
        }

        // Set the Instance Access Consent
        if (evidenceInstanceAccessConsentValues == null) {
            LOG.debug("No Instance Access Consent found for Evidence");
        }

        statements = OpenSAML2ComponentBuilder.getInstance().createEvidenceStatements(accessConstentValues,
            evidenceInstanceAccessConsentValues, NHIN_NS);

        LOG.debug("SamlCallbackHandler.createEvidenceStatements() -- End");
        return statements;
    }

    /**
     * Creates the Attribute statements for UserName.
     *
     */
    protected List<AttributeStatement> createUserNameAttributeStatements(CallbackProperties properties) {

        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
        List<Attribute> attributes = new ArrayList<Attribute>();

        // Set the User Name Attribute
        List<String> userNameValues = new ArrayList<String>();
        String nameConstruct = properties.getUserFullName();

        if (nameConstruct.length() > 0) {
            LOG.debug("UserName: " + nameConstruct);

            userNameValues.add(nameConstruct);

            attributes.add(OpenSAML2ComponentBuilder.getInstance().createAttribute(null, SamlConstants.USERNAME_ATTR,
                null, userNameValues));
        } else {
            LOG.warn("No information provided to fill in user name attribute");
        }
        if (!attributes.isEmpty()) {
            statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
        }

        return statements;
    }

    /**
     * Creates the Attribute statements UserRole
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    protected List<AttributeStatement> createUserRoleStatements(CallbackProperties properties) {
        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
        List<Attribute> attributes = new ArrayList<Attribute>();

        // Set the User Role Attribute
        String userCode = properties.getUserCode();
        String userSystem = properties.getUserSystem();
        String userSystemName = properties.getUserSystemName();
        String userDisplay = properties.getUserDisplay();

        attributes.add(OpenSAML2ComponentBuilder.getInstance().createUserRoleAttribute(userCode, userSystem,
            userSystemName, userDisplay));

        if (!attributes.isEmpty()) {
            statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
        }

        return statements;

    }

    /**
     * Creates the Attribute statements PurposeOfUse
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    protected List<AttributeStatement> createPurposeOfUseStatements(CallbackProperties properties) {
        List<AttributeStatement> statements;

        final String purposeCode = properties.getPurposeCode();
        final String purposeSystem = properties.getPurposeSystem();
        final String purposeSystemName = properties.getPurposeSystemName();
        final String purposeDisplay = properties.getPurposeDisplay();

        /*
         * Gateway-347 - Support for both values will remain until NHIN Specs updated Determine whether to use
         * PurposeOfUse or PuposeForUse
         */
        // Call out to the purpose decider to determine whether to use purposeofuse or purposeforuse.
        PurposeOfForDecider pd = new PurposeOfForDecider();
        if (pd.isPurposeFor(properties)) {
            statements = OpenSAML2ComponentBuilder.getInstance().createPurposeForUseAttributeStatements(purposeCode,
                purposeSystem, purposeSystemName, purposeDisplay);
        } else {
            statements = OpenSAML2ComponentBuilder.getInstance().createPurposeOfUseAttributeStatements(purposeCode,
                purposeSystem, purposeSystemName, purposeDisplay);
        }

        return statements;
    }

    /**
     * Creates the Attribute statements for UserName, UserOrganization, UserRole, and PurposeOfUse
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    protected List<AttributeStatement> createOrganizationAttributeStatements(CallbackProperties properties) {

        LOG.debug("SamlCallbackHandler.addAssertStatements() -- Begin");
        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
        List<Attribute> attributes = new ArrayList<Attribute>();

        // Set the User Organization ID Attribute
        final String organizationId = properties.getUserOrganization();
        if (organizationId != null) {
            attributes.add(OpenSAML2ComponentBuilder.getInstance().createAttribute(null, SamlConstants.USER_ORG_ATTR,
                null, Arrays.asList(organizationId)));
        }

        if (!attributes.isEmpty()) {
            statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
        }

        LOG.debug("SamlCallbackHandler.addAssertStatements() -- End");
        return statements;

    }

    /**
     * Creates the Attribute statements for UserName, UserOrganization, UserRole, and PurposeOfUse
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    protected List<AttributeStatement> createHomeCommunityIdAttributeStatements(CallbackProperties properties) {

        LOG.debug("SamlCallbackHandler.addAssertStatements() -- Begin");
        List<AttributeStatement> statements = Collections.EMPTY_LIST;

        // Set the Home Community ID Attribute
        final String communityId = properties.getHomeCommunity();
        if (communityId != null) {

            statements = OpenSAML2ComponentBuilder.getInstance().createHomeCommunitAttributeStatement(communityId);
        } else {
            LOG.debug("Home Community ID is missing");
        }

        return statements;

    }

    /**
     * Creates the Attribute statements for UserName, UserOrganization, UserRole, and PurposeOfUse
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    protected List<AttributeStatement> createPatientIdAttributeStatements(CallbackProperties properties) {

        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
        Attribute attribute;

        // Set the Patient ID Attribute
        final String patientId = properties.getPatientID();
        if (patientId != null) {
            attribute = OpenSAML2ComponentBuilder.getInstance().createPatientIDAttribute(patientId);

            statements.addAll(OpenSAML2ComponentBuilder.getInstance()
                .createAttributeStatement(Arrays.asList(attribute)));
        } else {
            LOG.debug("patient id is missing");
        }
        return statements;

    }

    /**
     * Creates the Attribute statements for NPI
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    protected List<AttributeStatement> createNPIAttributeStatements(CallbackProperties properties) {

        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
        Attribute attribute;

        // Set the NPI Attribute
        final String npi = properties.getNPI();
        if (npi != null) {
            attribute = OpenSAML2ComponentBuilder.getInstance().createNPIAttribute(npi);

            statements.addAll(OpenSAML2ComponentBuilder.getInstance()
                .createAttributeStatement(Arrays.asList(attribute)));
        } else {
            LOG.debug("npi is missing");
        }
        return statements;

    }

    private static String createAssertionId() {
        return ID_PREFIX.concat(String.valueOf(UUID.randomUUID())).replaceAll("-", "");
    }

    protected boolean isAuthDEvidenceConditionsDefaultValueEnabled() {
        //if not provided or invalid return true else false
        try {
            String authDEvidenceConditionsDefaultValueEnabled = PropertyAccessor.getInstance().getProperty(
                NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ENABLE_AUTH_DEC_EVIDENCE_CONDITIONS_DEFAULT_VALUE);
            if (authDEvidenceConditionsDefaultValueEnabled != null) {
                return !authDEvidenceConditionsDefaultValueEnabled.equals(Boolean.FALSE.toString());
            }
        } catch (PropertyAccessException pae) {
            LOG.info("Proeprty Not found :" + NhincConstants.ENABLE_AUTH_DEC_EVIDENCE_CONDITIONS_DEFAULT_VALUE);
        }
        return Boolean.TRUE;
    }
}
