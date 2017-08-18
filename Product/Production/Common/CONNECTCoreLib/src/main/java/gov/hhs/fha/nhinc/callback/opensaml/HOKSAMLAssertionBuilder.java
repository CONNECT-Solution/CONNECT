/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.callback.opensaml;

import gov.hhs.fha.nhinc.callback.PurposeOfForDecider;
import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.naming.Name;
import javax.naming.ldap.LdapName;
import org.apache.commons.lang.StringUtils;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.joda.time.DateTime;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.Marshaller;
import org.opensaml.core.xml.io.MarshallerFactory;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.AuthnStatement;
import org.opensaml.saml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml.saml2.core.Conditions;
import org.opensaml.saml.saml2.core.Evidence;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.Statement;
import org.opensaml.saml.saml2.core.Subject;
import org.opensaml.xmlsec.signature.Signature;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.opensaml.xmlsec.signature.support.SignatureException;
import org.opensaml.xmlsec.signature.support.Signer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author bhumphrey
 *
 */
public class HOKSAMLAssertionBuilder extends SAMLAssertionBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(HOKSAMLAssertionBuilder.class);
    private final CertificateManager certificateManager;

    public HOKSAMLAssertionBuilder() {
        certificateManager = CertificateManagerImpl.getInstance();
    }

    HOKSAMLAssertionBuilder(final CertificateManager certificateManager) {
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
    public Element build(final CallbackProperties properties) throws SAMLAssertionBuilderException {
        LOG.debug("SamlCallbackHandler.createHOKSAMLAssertion20()");
        Element signedAssertion = null;
        try {
            Assertion assertion;
            assertion = OpenSAML2ComponentBuilder.getInstance().createAssertion();

            // create the assertion id
            // Per GATEWAY-847 the id attribute should not be allowed to start
            // with a number (UUIDs can). Direction
            // given from 2011 specification set was to prepend with and
            // underscore.
            final String aID = createAssertionId();
            LOG.debug("Assertion ID: {}", aID);

            // set assertion Id
            assertion.setID(aID);

            // issue instant set to now.
            final DateTime issueInstant = new DateTime();
            assertion.setIssueInstant(issueInstant);

            // set issuer
            assertion.setIssuer(createIssuer(properties));

            final X509Certificate certificate = certificateManager.getDefaultCertificate();
            final PublicKey publicKey = certificateManager.getDefaultPublicKey();

            // set subject
            final Subject subject = createSubject(properties, certificate, publicKey);
            assertion.setSubject(subject);

            // add conditions statement
            final Conditions conditions = createConditions(properties);
            assertion.setConditions(conditions);

            // add attribute statements
            final Subject evidenceSubject = createEvidenceSubject(properties, certificate, publicKey);
            assertion.getStatements().addAll(createAttributeStatements(properties, evidenceSubject));

            final PrivateKey privateKey = certificateManager.getDefaultPrivateKey();
            // sign the message
            signedAssertion = sign(assertion, certificate, privateKey, publicKey);
        } catch (final SAMLComponentBuilderException | CertificateManagerException ex) {
            LOG.error("Unable to create HOK Assertion: {}", ex.getLocalizedMessage());
            throw new SAMLAssertionBuilderException(ex.getLocalizedMessage(), ex);
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
    protected Element sign(final Assertion assertion, final X509Certificate certificate, final PrivateKey privateKey,
        final PublicKey publicKey) throws SAMLAssertionBuilderException {
        Element assertionElement = null;
        try {
            final Signature signature = OpenSAML2ComponentBuilder.getInstance().createSignature(certificate, privateKey,
                publicKey);
            final SamlAssertionWrapper wrapper = new SamlAssertionWrapper(assertion);

            wrapper.setSignature(signature, SignatureConstants.ALGO_ID_DIGEST_SHA1);
            final MarshallerFactory marshallerFactory = XMLObjectProviderRegistrySupport.getMarshallerFactory();
            final Marshaller marshaller = marshallerFactory.getMarshaller(wrapper.getSamlObject());
            assertionElement = marshaller.marshall(wrapper.getSamlObject());
            Signer.signObject(signature);
        } catch (final SignatureException | WSSecurityException | MarshallingException e) {
            LOG.error("Unable to sign: {}", e.getLocalizedMessage());
            throw new SAMLAssertionBuilderException(e.getLocalizedMessage(), e);
        }
        return assertionElement;

    }

    protected Issuer createIssuer(final CallbackProperties properties) {
        Issuer issuer;
        final String format = properties.getAssertionIssuerFormat();
        if (format != null) {
            LOG.debug("Setting Assertion Issuer format to: {}", format);
            final String sIssuer = properties.getIssuer();
            LOG.debug("Setting Assertion Issuer to: {}", sIssuer);
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
    protected Subject createSubject(final CallbackProperties properties, final X509Certificate certificate,
        final PublicKey publicKey) throws SAMLComponentBuilderException {
        String x509Name = properties.getUsername();

        if ((NullChecker.isNullish(x509Name) || !checkDistinguishedName(x509Name)) && null != certificate
            && null != certificate.getSubjectDN()) {
            x509Name = certificate.getSubjectDN().getName();
        }
        return OpenSAML2ComponentBuilder.getInstance().createSubject(x509Name, publicKey);
    }

    /**
     * Checks Distinguished Name, method have to be static, since it's private and no Instance variables are used, as
     * per Sonar.
     *
     * @param userName
     * @return boolean
     */
    private static boolean checkDistinguishedName(final String userName) {
        Name name = null;
        try {
            name = new LdapName(userName);
        } catch (final Exception e) {
            LOG.trace("Exception checking DN: {}", e.getLocalizedMessage(), e);
        }
        return name != null;
    }

    protected Subject createEvidenceSubject(final CallbackProperties properties, final X509Certificate certificate,
        final PublicKey publicKey) throws SAMLComponentBuilderException {

        final String evidenceSubject = properties.getEvidenceSubject();
        String x509Name;
        if (NullChecker.isNullish(evidenceSubject)) {
            String userName = properties.getUsername();

            if ((NullChecker.isNullish(userName) || !checkDistinguishedName(userName)) && null != certificate
                && null != certificate.getSubjectDN()) {
                userName = certificate.getSubjectDN().getName();
            }
            x509Name = userName;
        } else {
            x509Name = evidenceSubject;
        }
        return OpenSAML2ComponentBuilder.getInstance().createSubject(x509Name, publicKey);
    }

    protected Conditions createConditions(final CallbackProperties properties) {
        DateTime issueInstant = new DateTime();
        DateTime beginValidTime = properties.getSamlConditionsNotBefore();
        DateTime endValidTime = properties.getSamlConditionsNotAfter();

        // Only create the Conditions if NotBefore and/or NotOnOrAfter is present
        if (beginValidTime != null || endValidTime != null) {
            beginValidTime = setBeginValidTime(beginValidTime, issueInstant);
            endValidTime = setEndValidTime(endValidTime, issueInstant);
            return OpenSAML2ComponentBuilder.getInstance().createConditions(beginValidTime, endValidTime);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Statement> createAttributeStatements(final CallbackProperties properties, final Subject subject) {
        final List<Statement> statements = new ArrayList<>();

        statements.addAll(createAuthenicationStatements(properties));
        statements.addAll(createUserNameAttributeStatements(properties));
        statements.addAll(createOrganizationAttributeStatements(properties));
        statements.addAll(createOrganizationIdAttributeStatements(properties));
        statements.addAll(createHomeCommunityIdAttributeStatements(properties));
        statements.addAll(createPatientIdAttributeStatements(properties));
        statements.addAll(createUserRoleStatements(properties));
        statements.addAll(createPurposeOfUseStatements(properties));
        statements.addAll(createNPIAttributeStatements(properties));
        statements.addAll(createAuthorizationDecisionStatements(properties, subject));
        return statements;
    }

    protected Collection<AttributeStatement> createOrganizationIdAttributeStatements(
        final CallbackProperties properties) {
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

    public List<AuthnStatement> createAuthenicationStatements(final CallbackProperties properties) {

        final List<AuthnStatement> authnStatements = new ArrayList<>();

        String cntxCls = properties.getAuthenticationContextClass();
        if (cntxCls == null) {
            cntxCls = X509_AUTHN_CNTX_CLS;
        } else if (!isValidAuthnCntxCls(cntxCls)) {
            cntxCls = UNSPECIFIED_AUTHN_CNTX_CLS;
        }
        final String sessionIndex = properties.getAuthenticationSessionIndex();

        LOG.debug("Setting Authentication session index to: {}", sessionIndex);

        DateTime authInstant = properties.getAuthenticationInstant();
        if (authInstant == null) {
            authInstant = new DateTime();
        }

        final String inetAddr = properties.getSubjectLocality();
        final String dnsName = properties.getSubjectDNS();

        final AuthnStatement authnStatement = OpenSAML2ComponentBuilder.getInstance()
            .createAuthenticationStatements(cntxCls, sessionIndex, authInstant, inetAddr, dnsName);

        authnStatements.add(authnStatement);

        return authnStatements;
    }

    /**
     * @param properties
     * @param subject
     * @return
     */
    public List<AuthzDecisionStatement> createAuthorizationDecisionStatements(final CallbackProperties properties,
        final Subject subject) {
        final List<AuthzDecisionStatement> authDecisionStatements = new ArrayList<>();

        final Boolean hasAuthzStmt = properties.getAuthorizationStatementExists();
        // The authorization Decision Statement is optional
        if (hasAuthzStmt) {
            // Create resource for Authentication Decision Statement
            final String resource = properties.getAuthorizationResource();

            final Evidence evidence = createEvidence(properties, subject);

            authDecisionStatements.add(OpenSAML2ComponentBuilder.getInstance().createAuthzDecisionStatement(resource,
                AUTHZ_DECISION_PERMIT, AUTHZ_DECISION_ACTION_EXECUTE, evidence));
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
    public Evidence createEvidence(final CallbackProperties properties, final Subject subject) {
        LOG.debug("SamlCallbackHandler.createEvidence() -- Begin");
        final List<AttributeStatement> statements = createEvidenceStatements(properties);
        return buildEvidence(properties, statements, subject);
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

    public Evidence buildEvidence(CallbackProperties properties, List<AttributeStatement> statements, Subject subject) {

        DateTime issueInstant = properties.getEvidenceInstant();
        String format = properties.getEvidenceIssuerFormat();
        String evAssertionID = properties.getEvidenceID();
        DateTime beginValidTime = properties.getEvidenceConditionNotBefore();
        DateTime endValidTime = properties.getEvidenceConditionNotAfter();

        final List<Assertion> evidenceAssertions = new ArrayList<>();
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

        final Issuer evIssuerId = OpenSAML2ComponentBuilder.getInstance().createIssuer(format,
            properties.getEvidenceIssuer());

        final Assertion evidenceAssertion = OpenSAML2ComponentBuilder.getInstance().createAssertion(evAssertionID);

        evidenceAssertion.getAttributeStatements().addAll(statements);

        // Following two methods
        // Only set the default value for
        // AuthzDecisionStatement->Evidence->Assertion->Conditions--> notBefore(setBeginValidTime)
        // and notOnOrAfter(setEndValidTime)
        // attributes if the enableAuthDecEvidenceConditionsDefaultValue flag
        // enabled or not provided in gateway properties
        if (isConditionsDefaultValueEnabled()) {
            beginValidTime = setBeginValidTime(beginValidTime, issueInstant);
            endValidTime = setEndValidTime(endValidTime, issueInstant);
        }
        // Only create the Conditions if NotBefore and/or NotOnOrAfter is present
        if (beginValidTime != null || endValidTime != null) {
            final Conditions conditions = OpenSAML2ComponentBuilder.getInstance().createConditions(beginValidTime,
                endValidTime);
            evidenceAssertion.setConditions(conditions);
        }
        evidenceAssertion.setIssueInstant(issueInstant);
        evidenceAssertion.setIssuer(evIssuerId);
        evidenceAssertion.setSubject(subject);

        evidenceAssertions.add(evidenceAssertion);

        final Evidence evidence = OpenSAML2ComponentBuilder.getInstance().createEvidence(evidenceAssertions);

        LOG.debug("SamlCallbackHandler.createEvidence() -- End");
        return evidence;
    }

    /**
     *
     * @param beginValidTimeArg
     * @param issueInstant
     * @return beginValidTime
     */
    private static DateTime setBeginValidTime(DateTime beginValidTimeArg, DateTime issueInstant) {

        DateTime beginValidTime = beginValidTimeArg;
        final DateTime now = new DateTime();

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

        return beginValidTime;
    }

    /**
     *
     * @param endValidTimeArg
     * @param issueInstant
     * @return endValidTime
     */
    private static DateTime setEndValidTime(DateTime endValidTimeArg, DateTime issueInstant) {
        DateTime endValidTime = endValidTimeArg;
        final DateTime now = new DateTime();

        // Make end datetime at a minimum 5 minutes from now
        if (endValidTime == null || endValidTime.isBefore(now.plusMinutes(5))) {
            endValidTime = now.plusMinutes(5);
        }

        // Ensure issueInstant is contained within valid times
        if (endValidTime.isBefore(issueInstant)) {
            endValidTime = issueInstant.plusMinutes(5);
        }

        return endValidTime;
    }

    /**
     * Creates the Attribute Statements needed for the Evidence element. These include the Attributes for the Access
     * Consent Policy and the Instance Access Consent Policy
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of the attribute statements for the Evidence element
     */
    protected List<AttributeStatement> createEvidenceStatements(final CallbackProperties properties) {
        LOG.debug("SamlCallbackHandler.createEvidenceStatements() -- Begin");

        final List accessConstentValues = properties.getEvidenceAccessConstent();
        final List evidenceInstanceAccessConsentValues = properties.getEvidenceInstantAccessConsent();

        return createEvidenceStatements(accessConstentValues, evidenceInstanceAccessConsentValues);
    }

    public List<AttributeStatement> createEvidenceStatements(final List accessConstentValues,
        final List evidenceInstanceAccessConsentValues) {
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

        LOG.trace("SamlCallbackHandler.createEvidenceStatements() -- End");
        return statements;
    }

    /**
     * Creates the Attribute statements for UserName.
     *
     */
    protected List<AttributeStatement> createUserNameAttributeStatements(final CallbackProperties properties) {

        final List<AttributeStatement> statements = new ArrayList<>();
        final List<Attribute> attributes = new ArrayList<>();

        // Set the User Name Attribute
        final List<String> userNameValues = new ArrayList<>();
        final String nameConstruct = properties.getUserFullName();

        if (StringUtils.isNotEmpty(nameConstruct)) {
            LOG.debug("UserName: {}", nameConstruct);

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
    protected List<AttributeStatement> createUserRoleStatements(final CallbackProperties properties) {
        final List<AttributeStatement> statements = new ArrayList<>();
        final List<Attribute> attributes = new ArrayList<>();

        // Set the User Role Attribute
        final String userCode = properties.getUserCode();
        final String userSystem = properties.getUserSystem();
        final String userSystemName = properties.getUserSystemName();
        final String userDisplay = properties.getUserDisplay();

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
    protected List<AttributeStatement> createPurposeOfUseStatements(final CallbackProperties properties) {
        List<AttributeStatement> statements;

        final String purposeCode = properties.getPurposeCode();
        final String purposeSystem = properties.getPurposeSystem();
        final String purposeSystemName = properties.getPurposeSystemName();
        final String purposeDisplay = properties.getPurposeDisplay();

        /*
         * Gateway-347 - Support for both values will remain until NHIN Specs updated Determine whether to use
         * PurposeOfUse or PuposeForUse
         */
        // Call out to the purpose decider to determine whether to use
        // purposeofuse or purposeforuse.
        final PurposeOfForDecider pd = new PurposeOfForDecider();
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
    protected List<AttributeStatement> createOrganizationAttributeStatements(final CallbackProperties properties) {

        LOG.debug("SamlCallbackHandler.addAssertStatements() -- Begin");
        final List<AttributeStatement> statements = new ArrayList<>();
        final List<Attribute> attributes = new ArrayList<>();

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
    protected List<AttributeStatement> createHomeCommunityIdAttributeStatements(final CallbackProperties properties) {

        LOG.debug("SamlCallbackHandler.addAssertStatements() -- Begin");
        List<AttributeStatement> statements = Collections.emptyList();

        // Set the Home Community ID Attribute
        final String communityId = properties.getHomeCommunity();
        if (StringUtils.isNotBlank(communityId)) {
            statements = OpenSAML2ComponentBuilder.getInstance().createHomeCommunitAttributeStatement(
                appendPrefixHomeCommunityID(communityId));
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
    protected List<AttributeStatement> createPatientIdAttributeStatements(final CallbackProperties properties) {

        final List<AttributeStatement> statements = new ArrayList<>();
        Attribute attribute;

        // Set the Patient ID Attribute
        final String patientId = properties.getPatientID();
        if (patientId != null) {
            attribute = OpenSAML2ComponentBuilder.getInstance().createPatientIDAttribute(patientId);

            statements
            .addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(Arrays.asList(attribute)));
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
    protected List<AttributeStatement> createNPIAttributeStatements(final CallbackProperties properties) {

        final List<AttributeStatement> statements = new ArrayList<>();
        Attribute attribute;

        // Set the NPI Attribute
        final String npi = properties.getNPI();
        if (npi != null) {
            attribute = OpenSAML2ComponentBuilder.getInstance().createNPIAttribute(npi);

            statements
            .addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(Arrays.asList(attribute)));
        } else {
            LOG.debug("npi is missing");
        }
        return statements;

    }

    private static String createAssertionId() {
        return ID_PREFIX.concat(String.valueOf(UUID.randomUUID())).replaceAll("-", "");
    }

    protected boolean isConditionsDefaultValueEnabled() {
        // if not provided or invalid return true else false
        try {
            final Boolean conditionsDefaultValueEnabled = PropertyAccessor.getInstance().getPropertyBoolean(
                NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ENABLE_CONDITIONS_DEFAULT_VALUE);
            return !conditionsDefaultValueEnabled.equals(Boolean.FALSE);
        } catch (final PropertyAccessException pae) {
            LOG.trace("Property not found exception: {}", pae.getLocalizedMessage(), pae);
        }
        return Boolean.TRUE;
    }

    public static String appendPrefixHomeCommunityID(final String homeCommunityId) {
        return checkPrefixBeforeAppend(homeCommunityId, NhincConstants.HCID_PREFIX);
    }

    public static String checkPrefixBeforeAppend(final String checkValue, final String checkPrefix) {
        final String tempValue = checkValue.trim().toLowerCase();
        final String tempPrefix = checkPrefix.toLowerCase();
        if(!tempValue.startsWith(tempPrefix)){
            return MessageFormat.format("{0}{1}", checkPrefix, checkValue);
        } else {
            return checkValue;
        }
    }
}
