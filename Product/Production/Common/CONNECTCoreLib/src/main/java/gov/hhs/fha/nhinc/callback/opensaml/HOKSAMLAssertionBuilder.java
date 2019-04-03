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
package gov.hhs.fha.nhinc.callback.opensaml;

import gov.hhs.fha.nhinc.callback.PurposeOfForDecider;
import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.NhinUtils;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
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
import org.opensaml.xmlsec.signature.support.SignatureException;
import org.opensaml.xmlsec.signature.support.Signer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * This class builds the SAML2 Assertion for Outbound requests from the given CONNECT AssertionType. All Inbound
 * requests go through this assertion builder to in order to generate the appropriate SAML for Outbound requests via
 * CXFSAMLCallbackHandler::handle(Callback[] callbacks).
 * <p>
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
    public Element build(final CallbackProperties properties, String certificateAlias) {
        LOG.debug("SamlCallbackHandler.build() -- Start with certificate alias {}", certificateAlias);
        Element signedAssertion = null;
        try {
            final X509Certificate certificate = certificateManager.getCertificateBy(certificateAlias);
            final PublicKey publicKey = certificateManager.getPublicKeyBy(certificateAlias);
            final PrivateKey privateKey = certificateManager.getPrivateKeyBy(certificateAlias);
            Assertion assertion = componentBuilder.createAssertion();

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

            signedAssertion = sign(properties, assertion, certificate, privateKey, publicKey);
        } catch (final SAMLComponentBuilderException | CertificateManagerException ex) {
            LOG.error("Unable to create HOK Assertion: {}", ex.getLocalizedMessage());
            throw new SAMLAssertionBuilderException(ex.getLocalizedMessage(), ex);
        }
        LOG.debug("SamlCallbackHandler.build() -- End");
        return signedAssertion;
    }

    /**
     * @param properties
     * @param assertion
     * @param privateKey
     * @param certificate
     * @param publicKey
     * @return
     */
    protected Element sign(CallbackProperties properties, final Assertion assertion, final X509Certificate certificate, final PrivateKey privateKey,
        final PublicKey publicKey) {
        Element assertionElement = null;
        try {
            final Signature signature = OpenSAML2ComponentBuilder.getInstance().createSignature(properties, certificate, privateKey,
                publicKey);
            final SamlAssertionWrapper wrapper = new SamlAssertionWrapper(assertion);

            wrapper.setSignature(signature, properties.getDigestAlgorithm());
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

        if (StringUtils.isBlank(format) || !isValidNameidFormat(format)) {
            format = SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_X509;
        }

        // If it is a X509 Subject, check if the DN is valid. If not, grab it from the local cert.
        // If there is no local cert, grab it from the properties file.
        // If there is no properties file, use the default issuer name
        if (format.equals(SamlConstants.AUTH_FRWK_NAME_ID_FORMAT_X509)
            && (StringUtils.isBlank(sIssuer) || !NhinUtils.getInstance().checkDistinguishedName(sIssuer))) {

            sIssuer = certificate != null ? certificate.getIssuerX500Principal().getName() : PropertyAccessor.
                getInstance().getProperty(PROPERTY_FILE_NAME, PROPERTY_SAML_ISSUER_NAME,
                    SamlConstants.SAML_DEFAULT_ISSUER_NAME);
        }

        if (StringUtils.isBlank(sIssuer)) {
            sIssuer = SamlConstants.SAML_DEFAULT_ISSUER_NAME;
        }

        LOG.debug("Setting Assertion Issuer format to: {}", format);
        LOG.debug("Setting Assertion Issuer to: {}", sIssuer);
        return OpenSAML2ComponentBuilder.getInstance().createIssuer(format, sIssuer);
    }

    /**
     * @param properties
     * @param publicKey
     * @param certificate
     * @return
     * @throws gov.hhs.fha.nhinc.callback.opensaml.SAMLComponentBuilderException
     */
    protected Subject createSubject(final CallbackProperties properties, final X509Certificate certificate,
        final PublicKey publicKey) throws SAMLComponentBuilderException {
        String x509Name = properties.getUsername();
        x509Name = NhinUtils.getInstance().getSAMLSubjectNameDN(certificate, x509Name);
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

    protected Subject createEvidenceSubject(final CallbackProperties properties, final X509Certificate certificate,
        final PublicKey publicKey) throws SAMLComponentBuilderException {

        final String evidenceSubject = properties.getEvidenceSubject();
        String x509Name;
        if (NullChecker.isNullish(evidenceSubject)) {
            String userName = properties.getUsername();
            x509Name = NhinUtils.getInstance().getSAMLSubjectNameDN(certificate, userName);
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

    public List<Statement> createAttributeStatements(final CallbackProperties properties, final Subject subject) {
        final List<Statement> statements = new ArrayList<>();
        addOptionalStatement(statements, createAuthenicationStatements(properties));

        // The following 6 statements are required for NHIN Spec
        addOptionalStatement(statements, createSubjectIdAttributeStatement(properties));
        addOptionalStatement(statements, createOrganizationAttributeStatement(properties));
        addOptionalStatement(statements, createSubjectRoleStatement(properties));
        addOptionalStatement(statements, createPurposeOfUseStatement(properties));
        addOptionalStatement(statements, createHomeCommunityIdAttributeStatement(properties));
        addOptionalStatement(statements, createOrganizationIdAttributeStatement(properties));

        // These are optional and may be omitted in the NHIN Spec
        addOptionalStatement(statements, createResourceIdAttributeStatement(properties));
        addOptionalStatement(statements, createNPIAttributeStatement(properties));
        addOptionalAttributeStatements(statements, createAcpAttributeStatements(properties));
        if (isAcpOrIacpExists(properties)) {
            addOptionalStatement(statements, createAuthorizationDecisionStatement(properties, subject));
        }

        return statements;
    }

    public void addOptionalStatement(List<Statement> statements, Statement statement) {
        if (statement != null) {
            statements.add(statement);
        }
    }

    public void addOptionalAttributeStatements(List<Statement> statements, List<AttributeStatement> statementList) {
        if (CollectionUtils.isNotEmpty(statementList)) {
            statements.addAll(statementList);
        }
    }

    protected AttributeStatement createOrganizationIdAttributeStatement(
        final CallbackProperties properties) {
        String organizationId = properties.getUserOrganizationId();
        if (organizationId == null) {
            LOG.error("No Organization ID Attribute statement provided.");
            throw new SAMLAssertionBuilderException("No Organization ID Attribute statement provided.");
        }

        return componentBuilder.createOrganizationIdAttributeStatement(organizationId);
    }

    public AuthnStatement createAuthenicationStatements(final CallbackProperties properties) {

        AuthnStatement authnStatements = null;

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

            authnStatements = authnStatement;
        }
        return authnStatements;

    }

    /**
     * Creates the optional Authorization Decision Statement for the SAML 2 Assertion
     *
     * @param properties
     * @param subject
     * @return
     */
    public AuthzDecisionStatement createAuthorizationDecisionStatement(final CallbackProperties properties,
        final Subject subject) {
        AuthzDecisionStatement authDecisionStatement = null;
        final Boolean hasAuthzStmt = properties.getAuthorizationStatementExists();
        // The authorization Decision Statement is optional
        if (hasAuthzStmt) {
            // Get resource for Authentication Decision Statement
            final String resource = properties.getAuthorizationResource();
            final Evidence evidence = createEvidence(properties, subject);

            authDecisionStatement = componentBuilder.createAuthzDecisionStatement(resource, AUTHZ_DECISION_PERMIT,
                AUTHZ_DECISION_ACTION_EXECUTE, evidence);
        }

        return authDecisionStatement;
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
     * Creates the Attribute Statements needed for the Evidence element. These include the Attributes for the Access
     * Consent Policy and the Instance Access Consent Policy
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
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
     */
    public AttributeStatement createSubjectIdAttributeStatement(final CallbackProperties properties) {

        final String nameConstruct = properties.getUserFullName();

        if (StringUtils.isEmpty(nameConstruct)) {
            LOG.error("No information provided to fill in Subject ID attribute..");
            throw new SAMLAssertionBuilderException("No information provided to fill in Subject ID attribute.");
        }

        LOG.debug("UserName: {}", nameConstruct);
        Attribute attribute = componentBuilder.createAttribute(null, SamlConstants.ATTRIBUTE_NAME_SUBJECT_ID_XSPA, null,
            Arrays.asList(
            nameConstruct));
        return componentBuilder.createAttributeStatement(attribute);
    }

    /**
     * Creates the Attribute statements for Subject Role
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    protected AttributeStatement createSubjectRoleStatement(final CallbackProperties properties) {

        final String userCode = properties.getUserCode();
        final String userSystem = properties.getUserSystem();
        final String userSystemName = properties.getUserSystemName();
        final String userDisplay = properties.getUserDisplay();

        if (Arrays.asList(userCode, userSystem, userSystemName, userDisplay).contains(null)) {
            LOG.error("No information provided to fill in subject role attribute..");
            throw new SAMLAssertionBuilderException("No information provided to fill in subject role attribute.");
        }

        Attribute attribute = componentBuilder.createSubjectRoleAttribute(userCode, userSystem, userSystemName,
            userDisplay);
        return attribute != null ? componentBuilder.createAttributeStatement(attribute) : null;

    }

    /**
     * Creates the Attribute statements PurposeOfUse
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    protected AttributeStatement createPurposeOfUseStatement(final CallbackProperties properties) {
        AttributeStatement statement;

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
            statement = componentBuilder.createPurposeForUseAttributeStatement(purposeCode,
                purposeSystem, purposeSystemName, purposeDisplay);
        } else {
            statement = componentBuilder.createPurposeOfUseAttributeStatement(purposeCode,
                purposeSystem, purposeSystemName, purposeDisplay);
        }

        return statement;
    }

    /**
     * Creates the Attribute statements for UserOrganization
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    protected AttributeStatement createOrganizationAttributeStatement(final CallbackProperties properties) {

        Attribute attribute = null;
        final String organizationId = properties.getUserOrganization();
        if (organizationId != null) {
            attribute = componentBuilder.createAttribute(null, SamlConstants.ATTRIBUTE_NAME_ORG, null,
                Arrays.asList(organizationId));

        } else {
            LOG.error("No Organization Attribute statement provided.");
            throw new SAMLAssertionBuilderException("No Organization Attribute statement provided.");
        }
        return attribute != null ? componentBuilder.createAttributeStatement(attribute) : null;
    }

    /**
     * Creates the Attribute statements for Home Community ID
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    protected AttributeStatement createHomeCommunityIdAttributeStatement(final CallbackProperties properties) {

        final String communityId = properties.getHomeCommunity();
        if (StringUtils.isBlank(communityId)) {
            LOG.error("No Home Community ID Attribute statement provided.");
            throw new SAMLAssertionBuilderException("No Home Community ID Attribute statement provided.");
        }

        return componentBuilder.createHomeCommunitAttributeStatement(appendPrefixHomeCommunityID(communityId));

    }

    /**
     * Creates the Attribute statements for Resource ID
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    protected AttributeStatement createResourceIdAttributeStatement(final CallbackProperties properties) {
        Attribute attribute = null;
        final String patientId = properties.getPatientID();
        if (patientId != null) {
            attribute = componentBuilder.createResourceIDAttribute(patientId);
        } else {
            LOG.warn("Resource Id is missing");
        }
        return attribute != null ? componentBuilder.createAttributeStatement(attribute) : null;
    }

    /**
     * Creates the Optional Attribute statements for NPI
     *
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    protected AttributeStatement createNPIAttributeStatement(final CallbackProperties properties) {

        Attribute attribute = null;

        final String npi = properties.getNPI();
        if (npi != null) {
            attribute = componentBuilder.createNPIAttribute(npi);
        } else {
            LOG.warn("npi is missing");
        }
        return attribute != null ? componentBuilder.createAttributeStatement(attribute) : null;

    }

    protected List<AttributeStatement> createAcpAttributeStatements(CallbackProperties properties) {
        List<AttributeStatement> statements = new ArrayList<>();
        Attribute acpAttribute;
        Attribute iacpAttribute;

        final String acp = properties.getAcpAttribute();
        final String iacp = properties.getIacpAttribute();
        if (StringUtils.isNotEmpty(acp)) {
            acpAttribute = componentBuilder
                .createAttribute(SamlConstants.ATTRIBUTE_FRIENDLY_NAME_XUA_ACP, SamlConstants.ATTRIBUTE_NAME_XUA_ACP,
                    SamlConstants.URI_NAME_FORMAT);
            acpAttribute.getAttributeValues().add(componentBuilder.createUriAttributeValue(acp));
            statements.add(componentBuilder.createAttributeStatement(acpAttribute));
        }
        if (StringUtils.isNotEmpty(iacp)) {
            iacpAttribute = componentBuilder
                .createAttribute(SamlConstants.ATTRIBUTE_FRIENDLY_NAME_XUA_IACP, SamlConstants.ATTRIBUTE_NAME_XUA_IACP,
                    SamlConstants.URI_NAME_FORMAT);
            iacpAttribute.getAttributeValues().add(componentBuilder.createUriAttributeValue(iacp));
            statements.add(componentBuilder.createAttributeStatement(iacpAttribute));
        }
        return statements;
    }

    /**
     * Summer 2011 Authorization Framework specification has provided a clarification regarding the SAML assertion ID
     * attribute, in that this value should be a properly formatted W3C ID Type object. Currently this Id is
     * represented by a UUID, however this can be incorrect because a UUID can start with a number, where as the W3C
     * ID Type cannot. The specification provides guidance that an underscore can be prepended to a UUID to conform
     * to the spec
     */
    private static String createAssertionId() {
        return ID_PREFIX.concat(String.valueOf(UUID.randomUUID())).replaceAll("-", "");
    }

    protected boolean isConditionsDefaultValueEnabled() {
        // if not provided or invalid return true else false
        try {
            return PropertyAccessor.getInstance().getPropertyBoolean(
                NhincConstants.SAML_PROPERTY_FILE, SamlConstants.ENABLE_CONDITIONS_DEFAULT_VALUE);
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
