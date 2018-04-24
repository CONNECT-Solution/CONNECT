/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
import java.util.Locale;
import java.util.UUID;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.ldap.LdapName;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
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
import org.opensaml.saml.saml2.core.SubjectConfirmation;
import org.opensaml.xmlsec.signature.Signature;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.opensaml.xmlsec.signature.support.SignatureException;
import org.opensaml.xmlsec.signature.support.Signer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * This class builds the SAML2 Assertion for Outbound requests from the given CONNECT AssertionType. All Inbound
 * requests go through this assertion builder to in order to generate the appropriate SAML for Outbound requests via
 * CXFSAMLCallbackHandler::handle(Callback[] callbacks).
 *
 * For more information regarding the SAML 2 Spec for use with CONNECT, please see section 3.2
 * https://sequoiaproject.org/wp-content/uploads/2014/11/nhin-authorization-framework-production-specification-v3.0.pdf
 *
 * @author bhumphrey
 *
 */
public class HOKSAMLAssertionBuilder extends SAMLAssertionBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(HOKSAMLAssertionBuilder.class);
    private final CertificateManager certificateManager;
    private final OpenSAML2ComponentBuilder componentBuilder;
    private static final String PROPERTY_FILE_NAME = "assertioninfo";
    private static final String PROPERTY_SAML_ISSUER_NAME = "SamlIssuerName";

    public HOKSAMLAssertionBuilder() {
        certificateManager = CertificateManagerImpl.getInstance();
        componentBuilder = OpenSAML2ComponentBuilder.getInstance();
    }

    HOKSAMLAssertionBuilder(final CertificateManager certificateManager) {
        this.certificateManager = certificateManager;
        componentBuilder = OpenSAML2ComponentBuilder.getInstance();
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
        LOG.debug("SamlCallbackHandler.build() -- Start");
        Element signedAssertion = null;
        try {
            final X509Certificate certificate = certificateManager.getDefaultCertificate();
            final PublicKey publicKey = certificateManager.getDefaultPublicKey();
            final PrivateKey privateKey = certificateManager.getDefaultPrivateKey();
            Assertion assertion = componentBuilder.createAssertion();

            // create the assertion id
            // Per GATEWAY-847 the id attribute should not be allowed to start
            // with a number (UUIDs can). Direction
            // given from 2011 specification set was to prepend with and
            // underscore.
            final String aID = createAssertionId();
            LOG.debug("Assertion ID: {}", aID);

            assertion.setID(aID);

            final DateTime issueInstant = new DateTime();
            assertion.setIssueInstant(issueInstant);
            assertion.setIssuer(createIssuer(properties, certificate));
            assertion.setSubject(createSubject(properties, certificate, publicKey));
            assertion.setConditions(createConditions(properties));
            assertion.getStatements().addAll(
                createAttributeStatements(properties, createEvidenceSubject(properties, certificate, publicKey)));

            signedAssertion = sign(assertion, certificate, privateKey, publicKey);
        } catch (final SAMLComponentBuilderException | CertificateManagerException ex) {
            LOG.error("Unable to create HOK Assertion: {}", ex.getLocalizedMessage());
            throw new SAMLAssertionBuilderException(ex.getLocalizedMessage(), ex);
        }
        LOG.debug("SamlCallbackHandler.build() -- End");
        return signedAssertion;
    }

    /**
     * @param assertion
     * @param privateKey
     * @param certificate
     * @param publicKey
     * @return
     * @throws SAMLAssertionBuilderException
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

    protected Issuer createIssuer(final CallbackProperties properties, final X509Certificate certificate) {
        String format = properties.getAssertionIssuerFormat();
        String sIssuer = properties.getIssuer();
        if (!(StringUtils.isNotBlank(format) && isValidNameidFormat(format))) {
            format = NhincConstants.AUTH_FRWK_NAME_ID_FORMAT_X509;
        }

        if (!(StringUtils.isNotBlank(sIssuer) && checkDistinguishedName(sIssuer))) {
            if (certificate != null) {
                sIssuer = certificate.getSubjectX500Principal().getName();
            } else {
                try {
                    sIssuer = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME, PROPERTY_SAML_ISSUER_NAME);
                } catch (PropertyAccessException ex) {
                    LOG.error("HOKSAMLAssertionBuilder can not access assertioninfo property file: {}",
                        ex.getLocalizedMessage(), ex);
                }
            }
        }

        if (StringUtils.isBlank(sIssuer)) {
            sIssuer = NhincConstants.SAML_DEFAULT_ISSUER_NAME;
        }

        LOG.debug("Setting Assertion Issuer format to: {}", format);
        LOG.debug("Setting Assertion Issuer to: {}", sIssuer);
        return OpenSAML2ComponentBuilder.getInstance().createIssuer(format, sIssuer);
    }

    /**
     * @param PSApk
     * @param certificate
     * @return
     * @throws SAMLComponentBuilderException
     */
    protected Subject createSubject(final CallbackProperties properties, final X509Certificate certificate,
        final PublicKey publicKey) throws SAMLComponentBuilderException {
        String x509Name = properties.getUsername();
        if ((NullChecker.isNullish(x509Name) || !checkDistinguishedName(x509Name)) && null != certificate
            && null != certificate.getSubjectDN()) {
            x509Name = certificate.getSubjectDN().getName();
        }
        Subject subject = componentBuilder.createSubject(x509Name, publicKey);
        // Add additional subject confirmation if exist
        List<SAMLSubjectConfirmation> subjectConfirmations = properties.getSubjectConfirmations();
        if (CollectionUtils.isNotEmpty(subjectConfirmations)) {
            for (SAMLSubjectConfirmation confirmation : subjectConfirmations) {
                SubjectConfirmation subjectConfirmation = componentBuilder.createSubjectConfirmation(confirmation);
                if (subjectConfirmation != null) {
                    subject.getSubjectConfirmations().add(subjectConfirmation);
                }
            }
        }

        return subject;
    }

    /**
     * Checks Distinguished Name, method have to be static, since it's private
     * and no Instance variables are used, as per Sonar.
     *
     * @param userName
     * @return boolean
     */
    private static boolean checkDistinguishedName(final String userName) {
        Name name = null;
        try {
            name = new LdapName(userName);
        } catch (InvalidNameException e) {
            LOG.error("Invalid distinguished name {}", userName);
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
        return componentBuilder.createSubject(x509Name, publicKey);
    }

    protected Conditions createConditions(final CallbackProperties properties) {
        DateTime issueInstant = new DateTime();
        DateTime beginValidTime = properties.getSamlConditionsNotBefore();
        DateTime endValidTime = properties.getSamlConditionsNotAfter();

        // Only create the Conditions if NotBefore and/or NotOnOrAfter is present
        if (beginValidTime != null && endValidTime != null) {
            // Correct the conditions if property is set or validity times are out of order
            if (isConditionsDefaultValueEnabled() || beginValidTime.isAfter(endValidTime)) {
                beginValidTime = setBeginValidTime(beginValidTime, issueInstant);
                endValidTime = setEndValidTime(endValidTime, issueInstant);
            }

            return componentBuilder.createConditions(beginValidTime, endValidTime);
        }
        return null;
    }

    public List<Statement> createAttributeStatements(final CallbackProperties properties, final Subject subject)
        throws SAMLAssertionBuilderException {
        final List<Statement> statements = new ArrayList<>();

        // TODO: Not a *single* one of these methods ever returns a list with more than one element.
        // so in that case we can drop the whole "List" thing going on and just add individual elements.
        statements.addAll(createAuthenicationStatements(properties));

        // The following 6 statements are required for NHIN Spec
        statements.addAll(createSubjectIdAttributeStatement(properties));
        statements.addAll(createOrganizationAttributeStatements(properties));
        statements.addAll(createSubjectRoleStatement(properties));
        statements.addAll(createPurposeOfUseStatements(properties));
        statements.addAll(createHomeCommunityIdAttributeStatements(properties));
        statements.addAll(createOrganizationIdAttributeStatements(properties));

        // These are optional and may be omitted in the NHIN Spec
        statements.addAll(createResourceIdAttributeStatements(properties));
        statements.addAll(createNPIAttributeStatements(properties));
        statements.addAll(createAcpAttributeStatements(properties));
        if (isAcpOrIacpExists(properties)) {
            statements.addAll(createAuthorizationDecisionStatements(properties, subject));
        }

        return statements;
    }

    protected Collection<AttributeStatement> createOrganizationIdAttributeStatements(
        final CallbackProperties properties) throws SAMLAssertionBuilderException {
        String organizationId = properties.getUserOrganizationId();
        if (organizationId == null) {
            LOG.error("No Organization ID Attribute statement provided.");
            throw new SAMLAssertionBuilderException("No Organization ID Attribute statement provided.");
        }

        return componentBuilder.createOrganizationIdAttributeStatement(organizationId);
    }

    public List<AuthnStatement> createAuthenicationStatements(final CallbackProperties properties)
        throws SAMLAssertionBuilderException {

        final List<AuthnStatement> authnStatements = new ArrayList<>();
        if (properties.getAuthenticationStatementExists()) {
            String cntxCls = properties.getAuthenticationContextClass();
            if (cntxCls == null || !isValidAuthnCntxCls(cntxCls)) {
                LOG.error("Assertion Authentication Statement <AuthnContext> element is null or not valid format.");
                throw new SAMLAssertionBuilderException(
                    "Assertion Authentication Statement <AuthnContext> element is null or not valid format.");
            }
            final String sessionIndex = properties.getAuthenticationSessionIndex();

            LOG.debug("Setting Authentication session index to: {}", sessionIndex);

            DateTime authInstant = properties.getAuthenticationInstant();
            if (authInstant == null) {
                LOG.error("Assertion Authentication Statement <AuthnInstant> element is null.");
                throw new SAMLAssertionBuilderException(
                    "Assertion Authentication Statement <AuthnInstant> element is null.");
            }
            try {
                ISODateTimeFormat.dateTimeParser().parseDateTime(authInstant.toString());
            } catch (UnsupportedOperationException | IllegalArgumentException e) {
                LOG.error("Date Format is incorrect.");
                throw new SAMLAssertionBuilderException(e.getLocalizedMessage(), e);
            }

            final String inetAddr = properties.getSubjectLocality();
            final String dnsName = properties.getSubjectDNS();

            final AuthnStatement authnStatement = componentBuilder
                .createAuthenticationStatements(cntxCls, sessionIndex, authInstant, inetAddr, dnsName);

            authnStatements.add(authnStatement);
        }
        return authnStatements;

    }

    /**
     * Creates the optional Autorization Decision Statement for the SAML 2 Assertion
     *
     * @param properties
     * @param subject
     * @return
     * @throws SAMLAssertionBuilderException
     */
    public List<AuthzDecisionStatement> createAuthorizationDecisionStatements(final CallbackProperties properties,
        final Subject subject) throws SAMLAssertionBuilderException {
        final List<AuthzDecisionStatement> authDecisionStatements = new ArrayList<>();

        final Boolean hasAuthzStmt = properties.getAuthorizationStatementExists();
        // The authorization Decision Statement is optional
        if (hasAuthzStmt) {
            // Get resource for Authentication Decision Statement
            final String resource = properties.getAuthorizationResource();
            final Evidence evidence = createEvidence(properties, subject);

            authDecisionStatements.add(componentBuilder.createAuthzDecisionStatement(resource,
                AUTHZ_DECISION_PERMIT, AUTHZ_DECISION_ACTION_EXECUTE, evidence));
        }

        return authDecisionStatements;
    }

    /**
     * Creates the Evidence element that encompasses the Assertion defining the
     * authorization form needed in cases where evidence of authorization to
     * access the medical records must be provided along with the message
     * request.
     *
     * @param factory The factory object used to assist in the construction of
     * the SAML Assertion token
     * @param issueInstant The calendar representing the time of Assertion
     * issuance
     * @return The Evidence element
     * @throws SAMLAssertionBuilderException
     */
    public Evidence createEvidence(final CallbackProperties properties, final Subject subject)
        throws SAMLAssertionBuilderException {
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
        } else if (!evAssertionID.startsWith("_")) {
            evAssertionID = ID_PREFIX.concat(evAssertionID);
        }

        if (issueInstant == null) {
            issueInstant = new DateTime();
        }

        if (!isValidIssuerFormat(format)) {
            format = X509_NAME_ID;
        }

        final Issuer evIssuerId = componentBuilder.createIssuer(format, properties.getEvidenceIssuer());
        final Assertion evidenceAssertion = componentBuilder.createAssertion(evAssertionID);

        evidenceAssertion.getAttributeStatements().addAll(statements);

        // Only create the Conditions if NotBefore and NotOnOrAfter is present
        if (beginValidTime != null && endValidTime != null) {
            // Following two methods
            // Only set the default value for
            // AuthzDecisionStatement->Evidence->Assertion->Conditions--> notBefore(setBeginValidTime)
            // and notOnOrAfter(setEndValidTime)
            // attributes if the enableAuthDecEvidenceConditionsDefaultValue flag
            // enabled or not provided in gateway properties
            if (isConditionsDefaultValueEnabled() || beginValidTime.isAfter(endValidTime)) {
                beginValidTime = setBeginValidTime(beginValidTime, issueInstant);
                endValidTime = setEndValidTime(endValidTime, issueInstant);
            }

            final Conditions conditions = componentBuilder.createConditions(beginValidTime,
                endValidTime);
            evidenceAssertion.setConditions(conditions);
        }
        evidenceAssertion.setIssueInstant(issueInstant);
        evidenceAssertion.setIssuer(evIssuerId);
        evidenceAssertion.setSubject(subject);

        evidenceAssertions.add(evidenceAssertion);

        final Evidence evidence = componentBuilder.createEvidence(evidenceAssertions);

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
        if (beginValidTime.isAfter(issueInstant) && issueInstant.isAfter(now)) {
            beginValidTime = now;
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
     * Creates the Attribute Statements needed for the Evidence element. These
     * include the Attributes for the Access Consent Policy and the Instance
     * Access Consent Policy
     *
     * @param factory The factory object used to assist in the construction of
     * the SAML Assertion token
     * @return The listing of the attribute statements for the Evidence element
     */
    protected List<AttributeStatement> createEvidenceStatements(final CallbackProperties properties) {
        LOG.debug("SamlCallbackHandler.createEvidenceStatements() -- Begin");

        final List accessConstentValues = checkHcidPrefixInList(properties.getEvidenceAccessConstent());
        final List evidenceInstanceAccessConsentValues = checkHcidPrefixInList(
            properties.getEvidenceInstantAccessConsent());

        List<AttributeStatement> statements = componentBuilder.createEvidenceStatements(accessConstentValues,
            evidenceInstanceAccessConsentValues, NHIN_NS);

        LOG.debug("SamlCallbackHandler.createEvidenceStatements() -- End");
        return statements;
    }

    /**
     * Creates the Attribute statements for Subject ID.
     *
     * @throws SAMLAssertionBuilderException
     *
     */
    public List<AttributeStatement> createSubjectIdAttributeStatement(final CallbackProperties properties)
        throws SAMLAssertionBuilderException {

        final String nameConstruct = properties.getUserFullName();

        if (StringUtils.isEmpty(nameConstruct)) {
            LOG.error("No information provided to fill in Subject ID attribute..");
            throw new SAMLAssertionBuilderException("No information provided to fill in Subject ID attribute.");
        }

        final List<AttributeStatement> statements = new ArrayList<>();
        final List<Attribute> attributes = new ArrayList<>();

        LOG.debug("UserName: {}", nameConstruct);
        attributes.add(
            componentBuilder.createAttribute(null, SamlConstants.USERNAME_ATTR, null, Arrays.asList(nameConstruct)));

        statements.addAll(componentBuilder.createAttributeStatement(attributes));

        return statements;
    }

    /**
     * Creates the Attribute statements for Subject Role
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     * @throws SAMLAssertionBuilderException
     */
    protected List<AttributeStatement> createSubjectRoleStatement(final CallbackProperties properties)
        throws SAMLAssertionBuilderException {

        final String userCode = properties.getUserCode();
        final String userSystem = properties.getUserSystem();
        final String userSystemName = properties.getUserSystemName();
        final String userDisplay = properties.getUserDisplay();

        if (Arrays.asList(userCode, userSystem, userSystemName, userDisplay).contains(null)) {
            LOG.error("No information provided to fill in user role attribute..");
            throw new SAMLAssertionBuilderException("No information provided to fill in user role attribute.");
        }

        final List<AttributeStatement> statements = new ArrayList<>();
        final List<Attribute> attributes = new ArrayList<>();
        attributes.add(componentBuilder.createUserRoleAttribute(userCode, userSystem, userSystemName, userDisplay));

        if (!attributes.isEmpty()) {
            statements.addAll(componentBuilder.createAttributeStatement(attributes));
        }

        return statements;

    }

    /**
     * Creates the Attribute statements PurposeOfUse
     *
     * @param factory The factory object used to assist in the construction of
     * the SAML Assertion token
     * @return The listing of all Attribute statements
     * @throws SAMLAssertionBuilderException
     */
    protected List<AttributeStatement> createPurposeOfUseStatements(final CallbackProperties properties)
        throws SAMLAssertionBuilderException {
        List<AttributeStatement> statements;

        final String purposeCode = properties.getPurposeCode();
        final String purposeSystem = properties.getPurposeSystem();
        final String purposeSystemName = properties.getPurposeSystemName();
        final String purposeDisplay = properties.getPurposeDisplay();

        if (Arrays.asList(purposeCode, purposeSystem, purposeSystemName, purposeDisplay).contains(null)) {
            LOG.error("No information provided to fill in Purpose For Use attribute..");
            throw new SAMLAssertionBuilderException("No information provided to fill in Purpose For Use attribute.");
        }
        /*
         * Gateway-347 - Support for both values will remain until NHIN Specs updated Determine whether to use
         * PurposeOfUse or PuposeForUse
         */
        // Call out to the purpose decider to determine whether to use
        // purposeofuse or purposeforuse.
        final PurposeOfForDecider pd = new PurposeOfForDecider();
        if (pd.isPurposeFor(properties)) {
            statements = componentBuilder.createPurposeForUseAttributeStatements(purposeCode,
                purposeSystem, purposeSystemName, purposeDisplay);
        } else {
            statements = componentBuilder.createPurposeOfUseAttributeStatements(purposeCode,
                purposeSystem, purposeSystemName, purposeDisplay);
        }

        return statements;
    }

    /**
     * Creates the Attribute statements for UserOrganization
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     * @throws SAMLAssertionBuilderException
     */
    protected List<AttributeStatement> createOrganizationAttributeStatements(final CallbackProperties properties)
        throws SAMLAssertionBuilderException {

        final List<AttributeStatement> statements = new ArrayList<>();
        final List<Attribute> attributes = new ArrayList<>();

        final String organizationId = properties.getUserOrganization();
        if (organizationId != null) {
            attributes.add(componentBuilder.createAttribute(null, SamlConstants.USER_ORG_ATTR,
                null, Arrays.asList(organizationId)));
            if (!attributes.isEmpty()) {
                statements.addAll(componentBuilder.createAttributeStatement(attributes));
            }
        } else {
            LOG.error("No Organization Attribute statement provided.");
            throw new SAMLAssertionBuilderException("No Organization Attribute statement provided.");
        }

        return statements;

    }

    /**
     * Creates the Attribute statements for Home Community ID
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     * @throws SAMLAssertionBuilderException
     */
    protected List<AttributeStatement> createHomeCommunityIdAttributeStatements(final CallbackProperties properties)
        throws SAMLAssertionBuilderException {

        List<AttributeStatement> statements = Collections.emptyList();

        final String communityId = properties.getHomeCommunity();
        if (StringUtils.isNotBlank(communityId)) {
            statements = componentBuilder
                .createHomeCommunitAttributeStatement(appendPrefixHomeCommunityID(communityId));
        } else {
            LOG.error("No Home Community ID Attribute statement provided.");
            throw new SAMLAssertionBuilderException("No Home Community ID Attribute statement provided.");
        }

        return statements;

    }

    /**
     * Creates the Attribute statements for Resource ID
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    protected List<AttributeStatement> createResourceIdAttributeStatements(final CallbackProperties properties) {
        final List<AttributeStatement> statements = new ArrayList<>();
        final String patientId = properties.getPatientID();
        if (patientId != null) {
            Attribute attribute = componentBuilder.createPatientIDAttribute(patientId);
            statements.addAll(componentBuilder.createAttributeStatement(Arrays.asList(attribute)));
        } else {
            LOG.warn("Resource Id is missing");
        }
        return statements;
    }

    /**
     * Creates the Optional Attribute statements for NPI
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    protected List<AttributeStatement> createNPIAttributeStatements(final CallbackProperties properties) {

        final List<AttributeStatement> statements = new ArrayList<>();
        Attribute attribute;

        final String npi = properties.getNPI();
        if (npi != null) {
            attribute = componentBuilder.createNPIAttribute(npi);
            statements.addAll(componentBuilder.createAttributeStatement(Arrays.asList(attribute)));
        } else {
            LOG.warn("npi is missing");
        }
        return statements;

    }

    protected List<AttributeStatement> createAcpAttributeStatements(CallbackProperties properties) {
        final List<AttributeStatement> statements = new ArrayList<>();
        Attribute acpAttribute;
        Attribute iacpAttribute;

        final String acp = properties.getAcpAttribute();
        final String iacp = properties.getIacpAttribute();
        if (StringUtils.isNotEmpty(acp)) {
            acpAttribute = componentBuilder
                .createAttribute(SamlConstants.ATTRIBUTE_FRIENDLY_NAME_XUA_ACP, SamlConstants.ATTRIBUTE_NAME_XUA_ACP,
                    SamlConstants.URI_NAME_FORMAT);
            acpAttribute.getAttributeValues().add(componentBuilder.createUriAttributeValue(acp));
            statements
            .addAll(componentBuilder.createAttributeStatement(Arrays.asList(acpAttribute)));
        }
        if(StringUtils.isNotEmpty(iacp)) {
            iacpAttribute = componentBuilder
                .createAttribute(SamlConstants.ATTRIBUTE_FRIENDLY_NAME_XUA_IACP, SamlConstants.ATTRIBUTE_NAME_XUA_IACP,
                    SamlConstants.URI_NAME_FORMAT);
            iacpAttribute.getAttributeValues().add(componentBuilder.createUriAttributeValue(iacp));
            statements
            .addAll(componentBuilder.createAttributeStatement(Arrays.asList(iacpAttribute)));
        }
        return statements;
    }

    private static String createAssertionId() {
        return ID_PREFIX.concat(String.valueOf(UUID.randomUUID())).replaceAll("-", "");
    }

    protected boolean isConditionsDefaultValueEnabled() {
        // if not provided or invalid return true else false
        try {
            return PropertyAccessor.getInstance().getPropertyBoolean(
                NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ENABLE_CONDITIONS_DEFAULT_VALUE);
        } catch (final PropertyAccessException pae) {
            LOG.error("Property not found exception: {}", pae.getLocalizedMessage(), pae);
        }
        return true;
    }

    public static String appendPrefixHomeCommunityID(final String homeCommunityId) {
        return checkPrefixBeforeAppend(homeCommunityId, NhincConstants.HCID_PREFIX);
    }

    public static String checkPrefixBeforeAppend(final String checkValue, final String checkPrefix) {

        final String tempValue = checkValue.trim().toLowerCase(Locale.getDefault());
        final String tempPrefix = checkPrefix.toLowerCase(Locale.getDefault());
        if (!tempValue.startsWith(tempPrefix)) {
            return MessageFormat.format("{0}{1}", checkPrefix, checkValue);
        } else {
            return checkValue;
        }
    }

    public static List<Object> checkHcidPrefixInList(final List<Object> valueList) {
        List<Object> tempList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(valueList)) {
            for (Object eachValue : valueList) {
                tempList.add(checkPrefixBeforeAppend(eachValue.toString(), NhincConstants.HCID_PREFIX));
            }
        }
        return tempList;
    }

    protected boolean isAcpOrIacpExists(final CallbackProperties properties) {
        return CollectionUtils.isNotEmpty(properties.getEvidenceAccessConstent())
            || CollectionUtils.isNotEmpty(properties.getEvidenceInstantAccessConsent());
    }

}
