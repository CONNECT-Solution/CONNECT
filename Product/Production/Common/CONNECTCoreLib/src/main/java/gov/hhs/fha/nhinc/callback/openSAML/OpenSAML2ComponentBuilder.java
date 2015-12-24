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

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.namespace.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Action;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.DecisionTypeEnumeration;
import org.opensaml.saml2.core.Evidence;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.SubjectLocality;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSAnyBuilder;
import org.opensaml.xml.schema.impl.XSStringBuilder;
import org.opensaml.xml.security.keyinfo.KeyInfoHelper;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;

/**
 * The Class OpenSAML2ComponentBuilder.
 *
 * @author bhumphrey
 */
public class OpenSAML2ComponentBuilder implements SAMLCompontentBuilder {

    /**
     * The authn statement builder.
     */
    private final SAMLObjectBuilder<AuthnStatement> authnStatementBuilder;

    /**
     * The authn context builder.
     */
    private final SAMLObjectBuilder<AuthnContext> authnContextBuilder;

    /**
     * The authn context class ref builder.
     */
    private final SAMLObjectBuilder<AuthnContextClassRef> authnContextClassRefBuilder;

    /**
     * The attribute statement builder.
     */
    private final SAMLObjectBuilder<AttributeStatement> attributeStatementBuilder;

    /**
     * The attribute builder.
     */
    private final SAMLObjectBuilder<Attribute> attributeBuilder;

    /**
     * The Constant X509_NAME_ID.
     */
    private static final String X509_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";

    /**
     * The Constant DEFAULT_ISSUER_VALUE.
     */
    private static final String DEFAULT_ISSUER_VALUE = "CN=SAML User,OU=SU,O=SAML User,L=Los Angeles,ST=CA,C=US";

    /**
     * The assertion builder.
     */
    private final SAMLObjectBuilder<Assertion> assertionBuilder;

    /**
     * The name id builder.
     */
    private final SAMLObjectBuilder<NameID> nameIdBuilder;

    /**
     * The conditions builder.
     */
    private final SAMLObjectBuilder<Conditions> conditionsBuilder;

    /**
     * The action element builder.
     */
    private final SAMLObjectBuilder<Action> actionElementBuilder;

    /**
     * The authorization decision statement builder.
     */
    private final SAMLObjectBuilder<AuthzDecisionStatement> authorizationDecisionStatementBuilder;

    /**
     * The string builder.
     */
    private final XSStringBuilder stringBuilder;

    /**
     * The evidence builder.
     */
    private final SAMLObjectBuilder<Evidence> evidenceBuilder;

    /**
     * The xs any builder.
     */
    private final XSAnyBuilder xsAnyBuilder;

    /**
     * The subject locality builder.
     */
    private static SAMLObjectBuilder<SubjectLocality> subjectLocalityBuilder;

    /**
     * The builder factory.
     */
    private static XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();

    private static final Logger LOG = LoggerFactory.getLogger(OpenSAML2ComponentBuilder.class);

    /**
     * Instantiates a new open sam l2 component builder.
     *
     * @throws ConfigurationException the configuration exception
     */
    private OpenSAML2ComponentBuilder() throws ConfigurationException {
        DefaultBootstrap.bootstrap();

        builderFactory = Configuration.getBuilderFactory();

        attributeStatementBuilder = (SAMLObjectBuilder<AttributeStatement>) builderFactory
            .getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);
        authnStatementBuilder = (SAMLObjectBuilder<AuthnStatement>) builderFactory
            .getBuilder(AuthnStatement.DEFAULT_ELEMENT_NAME);
        authnContextBuilder = (SAMLObjectBuilder<AuthnContext>) builderFactory
            .getBuilder(AuthnContext.DEFAULT_ELEMENT_NAME);
        authnContextClassRefBuilder = (SAMLObjectBuilder<AuthnContextClassRef>) builderFactory
            .getBuilder(AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
        subjectLocalityBuilder = (SAMLObjectBuilder<SubjectLocality>) builderFactory
            .getBuilder(SubjectLocality.DEFAULT_ELEMENT_NAME);

        authorizationDecisionStatementBuilder = (SAMLObjectBuilder<AuthzDecisionStatement>) builderFactory
            .getBuilder(AuthzDecisionStatement.DEFAULT_ELEMENT_NAME);

        actionElementBuilder = (SAMLObjectBuilder<Action>) builderFactory.getBuilder(Action.DEFAULT_ELEMENT_NAME);

        nameIdBuilder = (SAMLObjectBuilder<NameID>) builderFactory.getBuilder(NameID.DEFAULT_ELEMENT_NAME);

        assertionBuilder = (SAMLObjectBuilder<Assertion>) builderFactory.getBuilder(Assertion.DEFAULT_ELEMENT_NAME);

        conditionsBuilder = (SAMLObjectBuilder<Conditions>) builderFactory.getBuilder(Conditions.DEFAULT_ELEMENT_NAME);

        evidenceBuilder = (SAMLObjectBuilder<Evidence>) builderFactory.getBuilder(Evidence.DEFAULT_ELEMENT_NAME);

        stringBuilder = (XSStringBuilder) builderFactory.getBuilder(XSString.TYPE_NAME);

        attributeBuilder = (SAMLObjectBuilder<Attribute>) builderFactory.getBuilder(Attribute.DEFAULT_ELEMENT_NAME);

        xsAnyBuilder = (XSAnyBuilder) builderFactory.getBuilder(XSAny.TYPE_NAME);

    }

    /**
     * The instance.
     */
    private static OpenSAML2ComponentBuilder INSTANCE;

    /**
     * Gets the single instance of OpenSAML2ComponentBuilder.
     *
     * @return single instance of OpenSAML2ComponentBuilder
     */
    public static OpenSAML2ComponentBuilder getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new OpenSAML2ComponentBuilder();
            } catch (ConfigurationException e) {
                LOG.error("Unable to get instance: {}", e.getLocalizedMessage(), e);
                INSTANCE = null;
            }

        }
        return INSTANCE;
    }

    /**
     * Creates the open saml object.
     *
     * @param qname the qname
     * @return the xML object
     */
    private XMLObject createOpenSAMLObject(QName qname) {
        return builderFactory.getBuilder(qname).buildObject(qname);
    }

    /**
     * Create Authentication Statements.
     *
     * @param cntxCls the cntx cls
     * @param sessionIndex the session index
     * @param authInstant the auth instant
     * @param inetAddr the inet addr
     * @param dnsName the dns name
     * @return an Authn Statement
     */
    @Override
    public AuthnStatement createAuthenicationStatements(String cntxCls, String sessionIndex, DateTime authInstant,
        String inetAddr, String dnsName) {

        AuthnStatement authnStatement = authnStatementBuilder.buildObject();

        AuthnContextClassRef authnContextClassRef = authnContextClassRefBuilder.buildObject();

        authnContextClassRef.setAuthnContextClassRef(cntxCls);

        AuthnContext authnContext = authnContextBuilder.buildObject();
        authnContext.setAuthnContextClassRef(authnContextClassRef);
        authnStatement.setAuthnContext(authnContext);

        authnStatement.setSessionIndex(sessionIndex);

        authnStatement.setAuthnInstant(authInstant);

        if (inetAddr != null && dnsName != null) {

            SubjectLocality subjectLocality = subjectLocalityBuilder.buildObject();
            subjectLocality.setDNSName(dnsName);
            subjectLocality.setAddress(inetAddr);

            authnStatement.setSubjectLocality(subjectLocality);
        }

        return authnStatement;
    }

    /**
     * Creates the authz decision statement.
     *
     * @param resource the resource
     * @param decisionTxt the decision txt
     * @param action the action
     * @param evidence the evidence
     * @return the authz decision statement
     */
    public AuthzDecisionStatement createAuthzDecisionStatement(String resource, String decisionTxt, String action,
        Evidence evidence) {
        AuthzDecisionStatement authDecision = authorizationDecisionStatementBuilder.buildObject();
        authDecision.setResource(resource);

        // DecisionTypeEnumeration decision = DecisionTypeEnumeration.DENY;
        // todo: use decisionTxt to set decision
        DecisionTypeEnumeration decision = DecisionTypeEnumeration.PERMIT;

        authDecision.setDecision(decision);

        Action actionElement = actionElementBuilder.buildObject();
        // actionElement
        // .setNamespace("urn:oasis:names:tc:SAML:1.0:action:rwedc-negation");
        actionElement.setNamespace("urn:oasis:names:tc:SAML:1.0:action:rwedc");
        actionElement.setAction(action);

        authDecision.getActions().add(actionElement);
        authDecision.setEvidence(evidence);

        return authDecision;

    }

    /**
     * Creates the assertion.
     *
     * @param uuid the uuid
     * @return the assertion
     */
    public Assertion createAssertion(final String uuid) {
        Assertion assertion = assertionBuilder.buildObject(Assertion.DEFAULT_ELEMENT_NAME, Assertion.TYPE_NAME);
        assertion.setID(uuid);
        assertion.setVersion(SAMLVersion.VERSION_20);
        assertion.setIssueInstant(new DateTime());
        return assertion;
    }

    /**
     * Creates the name id.
     *
     * @param qualifier the qualifier
     * @param format the format
     * @param value the value
     * @return the name id
     */
    @SuppressWarnings("unchecked")
    public NameID createNameID(String qualifier, String format, String value) {
        NameID nameID = nameIdBuilder.buildObject();
        nameID.setNameQualifier(qualifier);
        nameID.setFormat(format);
        nameID.setValue(value);
        return nameID;
    }

    /**
     * Creates the name id.
     *
     * @param format the format
     * @param value the value
     * @return the name id
     */
    private NameID createNameID(String format, String value) {
        return createNameID(null, format, value);
    }

    /**
     * Creates the issuer.
     *
     * @param format the format
     * @param sIssuer the s issuer
     * @return the issuer
     */
    public Issuer createIssuer(String format, String sIssuer) {
        Issuer issuer = (Issuer) createOpenSAMLObject(Issuer.DEFAULT_ELEMENT_NAME);
        issuer.setFormat(format);
        issuer.setValue(sIssuer);
        return issuer;
    }

    /**
     * Creates the default issuer.
     *
     * @return the issuer
     */
    protected Issuer createDefaultIssuer() {
        return createIssuer(X509_NAME_ID, DEFAULT_ISSUER_VALUE);
    }

    /**
     * Creates the subject.
     *
     * @param x509Name the x509 name
     * @param certificate the certificate
     * @param publicKey the public key
     * @return the subject
     * @throws Exception the exception
     */
    public Subject createSubject(String x509Name, X509Certificate certificate, PublicKey publicKey) throws Exception {
        Subject subject = (org.opensaml.saml2.core.Subject) createOpenSAMLObject(Subject.DEFAULT_ELEMENT_NAME);
        subject.setNameID(createNameID(X509_NAME_ID, x509Name));

        SubjectConfirmationData subjectConfirmationData = createSubjectConfirmationData(certificate, publicKey);
        SubjectConfirmation subjectConfirmation = createHoKConfirmation(subjectConfirmationData);
        subject.getSubjectConfirmations().add(subjectConfirmation);
        return subject;
    }

    /**
     * Creates the ho k confirmation.
     *
     * @param subjectConfirmationData the subject confirmation data
     * @return the subject confirmation
     * @throws Exception the exception
     */
    private SubjectConfirmation createHoKConfirmation(SubjectConfirmationData subjectConfirmationData) throws Exception {
        SubjectConfirmation subjectConfirmation = (SubjectConfirmation) createOpenSAMLObject(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
        subjectConfirmation.setMethod(org.opensaml.saml2.core.SubjectConfirmation.METHOD_HOLDER_OF_KEY);
        subjectConfirmation.setSubjectConfirmationData(subjectConfirmationData);

        return subjectConfirmation;
    }

    /**
     * Creates the subject confirmation data.
     *
     * @param certificate the certificate
     * @param publicKey the public key
     * @return the subject confirmation data
     * @throws Exception the exception
     */
    private SubjectConfirmationData createSubjectConfirmationData(X509Certificate certificate, PublicKey publicKey)
        throws Exception {
        SubjectConfirmationData subjectConfirmationData = (SubjectConfirmationData) createOpenSAMLObject(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);

        subjectConfirmationData.getUnknownXMLObjects().add(getKeyInfo(certificate, publicKey));
        return subjectConfirmationData;
    }

    /**
     * Gets the key info.
     *
     * @param certificate the certificate
     * @param publicKey the public key
     * @return the key info
     * @throws Exception the exception
     */
    public KeyInfo getKeyInfo(X509Certificate certificate, PublicKey publicKey) throws Exception {
        KeyInfo ki = (KeyInfo) createOpenSAMLObject(KeyInfo.DEFAULT_ELEMENT_NAME);

        BasicX509Credential credential = new BasicX509Credential();
        credential.setEntityCertificate(certificate);

        KeyInfoHelper.addPublicKey(ki, publicKey);
        // KeyInfoHelper.addCertificate(ki, certificate);
        return ki;
    }

    /**
     * Gets the public key.
     *
     * @return the public key
     * @throws Exception the exception
     */
    public PublicKey getPublicKey() throws Exception {
        CertificateManager cm = CertificateManagerImpl.getInstance();

        X509Certificate certificate = cm.getDefaultCertificate();
        return certificate.getPublicKey();
    }

    /**
     * Creates the assertion.
     *
     * @return the assertion
     */
    public Assertion createAssertion() {
        return (Assertion) createOpenSAMLObject(Assertion.DEFAULT_ELEMENT_NAME);
    }

    /**
     * Creates the conditions.
     *
     * @param notBefore the not before
     * @param notAfter the not after
     * @param audienceURI the audience uri
     * @return the conditions
     */
    public Conditions createConditions(DateTime notBefore, DateTime notAfter, String audienceURI) {
        Conditions conditions = conditionsBuilder.buildObject();

        conditions.setNotBefore(notBefore);
        conditions.setNotOnOrAfter(notAfter);

        // AudienceRestriction audienceRestriction = audienceRestrictionBuilder
        // .buildObject();
        // Audience audience = audienceBuilder.buildObject();
        // audience.setAudienceURI(audienceURI);
        // audienceRestriction.getAudiences().add(audience);
        // conditions.getAudienceRestrictions().add(audienceRestriction);
        return conditions;
    }

    /**
     * Creates the attribute.
     *
     * @param friendlyName the friendly name
     * @param name the name
     * @param nameFormat the name format
     * @return the attribute
     */
    Attribute createAttribute(String friendlyName, String name, String nameFormat) {

        Attribute attribute = attributeBuilder.buildObject();
        attribute.setFriendlyName(friendlyName);
        if (nameFormat == null) {
            attribute.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:uri");
        } else {
            attribute.setNameFormat(nameFormat);
        }
        attribute.setName(name);
        return attribute;
    }

    /**
     * Creates the attribute.
     *
     * @param friendlyName the friendly name
     * @param name the name
     * @param nameFormat the name format
     * @param values the values
     * @return the attribute
     */
    Attribute createAttribute(String friendlyName, String name, String nameFormat, List<?> values) {

        Attribute attribute = createAttribute(friendlyName, name, nameFormat);

        for (Object value : values) {
            if (value instanceof String) {
                XSString attributeValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME,
                    XSString.TYPE_NAME);
                attributeValue.setValue((String) value);
                attribute.getAttributeValues().add(attributeValue);
            } else if (value instanceof XMLObject) {
                attribute.getAttributeValues().add((XMLObject) value);
            }
        }

        return attribute;
    }

    /**
     * Creates the any.
     *
     * @param namespace the namespace
     * @param name the name
     * @param prefix the prefix
     * @return the xS any
     */
    XSAny createAny(final String namespace, final String name, final String prefix) {
        return xsAnyBuilder.buildObject(namespace, name, prefix);
    }

    /**
     * Creates the any.
     *
     * @param namespace the namespace
     * @param name the name
     * @param prefix the prefix
     * @param attributes the attributes
     * @return the xS any
     */
    XSAny createAny(final String namespace, final String name, final String prefix, Map<QName, String> attributes) {

        XSAny any = createAny(namespace, name, prefix);

        for (Entry<QName, String> keyValue : attributes.entrySet()) {
            any.getUnknownAttributes().put(keyValue.getKey(), keyValue.getValue());
        }
        return any;

    }

    /**
     * Creates the attribute value.
     *
     * @param namespace the namespace
     * @param name the name
     * @param prefix the prefix
     * @param attributes the attributes
     * @return the xS any
     */
    XSAny createAttributeValue(final String namespace, final String name, final String prefix,
        Map<QName, String> attributes) {

        XSAny attribute = createAny(namespace, name, prefix, attributes);
        return createAttributeValue(Arrays.asList(attribute));
    }

    /**
     * Creates the attribute value.
     *
     * @param values the values
     * @return the xS any
     */
    XSAny createAttributeValue(List<XSAny> values) {

        XSAny attributeValue = xsAnyBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        attributeValue.getUnknownXMLObjects().addAll(values);

        return attributeValue;
    }

    /**
     * Creates the attribute statement.
     *
     * @param attributes the attributes
     * @return the list
     */
    List<AttributeStatement> createAttributeStatement(List<Attribute> attributes) {
        List<AttributeStatement> attributeStatements = new ArrayList<>();
        if (attributes != null && attributes.size() > 0) {

            AttributeStatement attributeStatement = attributeStatementBuilder.buildObject();
            for (Attribute attribute : attributes) {
                attributeStatement.getAttributes().add(attribute);

            }
            // Add the completed attribute statementBean to the collection
            attributeStatements.add(attributeStatement);
        }

        return attributeStatements;
    }

    /**
     * Creates the evidence.
     *
     * @param assertions the assertions
     * @return the evidence
     */
    public Evidence createEvidence(List<Assertion> assertions) {
        Evidence evidence = evidenceBuilder.buildObject();
        evidence.getAssertions().addAll(assertions);
        return evidence;
    }

    /**
     * Creates the evidence statements.
     *
     * @param accessConstentValues the access constent values
     * @param evidenceInstanceAccessConsentValues the evidence instance access consent values
     * @param namespace the namespace
     * @return the list
     */
    public List<AttributeStatement> createEvidenceStatements(List accessConstentValues,
        List evidenceInstanceAccessConsentValues, final String namespace) {
        List<AttributeStatement> statements = new ArrayList<>();

        List<Attribute> attributes = new ArrayList<>();

        if (accessConstentValues != null) {
            attributes.add(createAttribute(null, "AccessConsentPolicy", namespace, accessConstentValues));
        }

        if (evidenceInstanceAccessConsentValues != null) {
            attributes.add(createAttribute(null, "InstanceAccessConsentPolicy", namespace,
                evidenceInstanceAccessConsentValues));
        }
        if (!attributes.isEmpty()) {
            statements = createAttributeStatement(attributes);
        }

        return statements;
    }

    /**
     * Creates the user role attribute.
     *
     * @param userCode the user code
     * @param userSystem the user system
     * @param userSystemName the user system name
     * @param userDisplay the user display
     * @return the attribute
     */
    public Attribute createUserRoleAttribute(String userCode, String userSystem, String userSystemName,
        String userDisplay) {
        Object attributeValue = createHL7Attribute("Role", userCode, userSystem, userSystemName, userDisplay);

        return OpenSAML2ComponentBuilder.getInstance().createAttribute(null, SamlConstants.USER_ROLE_ATTR, null,
            Arrays.asList(attributeValue));
    }

    /**
     * Creates the h l7 attribute.
     *
     * @param name the name
     * @param code the code
     * @param codeSystem the code system
     * @param codeSystemName the code system name
     * @param displayName the display name
     * @return the xS any
     */
    public XSAny createHL7Attribute(String name, String code, String codeSystem, String codeSystemName,
        String displayName) {
        Map<QName, String> userRoleAttributes = new HashMap<>();

        boolean hasHl7prefix = getHl7PrefixProperty();

        if (code != null) {
            userRoleAttributes.put(createHl7QName(SamlConstants.CE_CODE_ID, hasHl7prefix), code);
        }

        if (codeSystem != null) {
            userRoleAttributes.put(createHl7QName(SamlConstants.CE_CODESYS_ID, hasHl7prefix), codeSystem);
        }

        if (codeSystemName != null) {
            userRoleAttributes.put(createHl7QName(SamlConstants.CE_CODESYSNAME_ID, hasHl7prefix), codeSystemName);
        }

        if (displayName != null) {
            userRoleAttributes.put(createHl7QName(SamlConstants.CE_DISPLAYNAME_ID, hasHl7prefix), displayName);
        }

        userRoleAttributes.put(new QName(SamlConstants.HL7_TYPE_NAMESPACE_URI, SamlConstants.HL7_TYPE_LOCAL_PART,
            SamlConstants.HL7_TYPE_PREFIX), SamlConstants.HL7_TYPE_KEY_VALUE);

        return createAttributeValue(SamlConstants.HL7_NAMESPACE_URI, name, SamlConstants.HL7_PREFIX, userRoleAttributes);

    }

    QName createHl7QName(String name, boolean hasPrefix) {
        return hasPrefix ? new QName(SamlConstants.HL7_NAMESPACE_URI, name, SamlConstants.HL7_PREFIX) : new QName(name);
    }

    boolean getHl7PrefixProperty() {
        try {
            return PropertyAccessor.getInstance().getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HL7_PREFIX_FOR_ATTR_PROPERTY);
        } catch (PropertyAccessException ex) {
            LOG.warn(ex.getLocalizedMessage());
            LOG.trace("Get HL7 Prefix Property exception: {}", ex.getLocalizedMessage(), ex);
        }
        return false;
    }

    /**
     * Creates the patient id attribute.
     *
     * @param patientId the patient id
     * @return the attribute
     */
    public Attribute createPatientIDAttribute(String patientId) {
        return createAttribute(null, SamlConstants.PATIENT_ID_ATTR, null, Arrays.asList(patientId));
    }

    /**
     * Creates the npi attribute.
     *
     * @param npi the npi
     * @return the attribute
     */
    public Attribute createNPIAttribute(String npi) {
        return createAttribute(null, SamlConstants.ATTRIBUTE_NAME_NPI, null, Arrays.asList(npi));
    }

    /**
     * Creates the home communit attribute statement.
     *
     * @param communityId the community id
     * @return the list
     */
    public List<AttributeStatement> createHomeCommunitAttributeStatement(String communityId) {
        List<AttributeStatement> statements = new ArrayList<>();
        Attribute attribute = createHomeCommunityAttribute(communityId);

        statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(Arrays.asList(attribute)));

        return statements;
    }

    /**
     * Creates the home community attribute.
     *
     * @param communityId the community id
     * @return the attribute
     */
    Attribute createHomeCommunityAttribute(String communityId) {
        return createAttribute(null, SamlConstants.HOME_COM_ID_ATTR, null, Arrays.asList(communityId));
    }

    /**
     * Creates the signature.
     *
     * @param certificate the certificate
     * @param privateKey the private key
     * @param publicKey the public key
     * @return the signature
     * @throws Exception the exception
     */
    public Signature createSignature(X509Certificate certificate, PrivateKey privateKey, PublicKey publicKey)
        throws Exception {
        BasicX509Credential credential = new BasicX509Credential();

        credential.setEntityCertificate(certificate);
        credential.setPrivateKey(privateKey);
        Signature signature = (Signature) createOpenSAMLObject(Signature.DEFAULT_ELEMENT_NAME);
        signature.setSigningCredential(credential);
        signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
        signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
        signature.setKeyInfo(getKeyInfo(certificate, publicKey));
        return signature;
    }

    /**
     * PurposeOfUse attribute statements.
     *
     * @param purposeCode the purpose code
     * @param purposeSystem the purpose system
     * @param purposeSystemName the purpose system name
     * @param purposeDisplay the purpose display
     * @return the list
     */
    public List<AttributeStatement> createPurposeOfUseAttributeStatements(String purposeCode, String purposeSystem,
        String purposeSystemName, String purposeDisplay) {

        List<AttributeStatement> statements = new ArrayList<>();
        Attribute attribute = createPurposeOfUseAttribute(purposeCode, purposeSystem, purposeSystemName, purposeDisplay);
        statements.addAll(createAttributeStatement(Arrays.asList(attribute)));
        return statements;
    }

    /**
     * PurposeForUse attribute statements.
     *
     * @param purposeCode the purpose code
     * @param purposeSystem the purpose system
     * @param purposeSystemName the purpose system name
     * @param purposeDisplay the purpose display
     * @return the list
     */
    public List<AttributeStatement> createPurposeForUseAttributeStatements(String purposeCode, String purposeSystem,
        String purposeSystemName, String purposeDisplay) {
        List<AttributeStatement> statements = new ArrayList<>();
        Attribute attribute = createPurposeForUseAttribute(purposeCode, purposeSystem, purposeSystemName,
            purposeDisplay);
        statements.addAll(createAttributeStatement(Arrays.asList(attribute)));
        return statements;
    }

    /**
     * Creates the purpose of use attribute.
     *
     * @param purposeCode the purpose code
     * @param purposeSystem the purpose system
     * @param purposeSystemName the purpose system name
     * @param purposeDisplay the purpose display
     * @return the attribute
     */
    public Attribute createPurposeOfUseAttribute(String purposeCode, String purposeSystem, String purposeSystemName,
        String purposeDisplay) {
        Object attributeValue = createHL7Attribute("PurposeOfUse", purposeCode, purposeSystem, purposeSystemName,
            purposeDisplay);
        return OpenSAML2ComponentBuilder.getInstance().createAttribute(null, SamlConstants.PURPOSE_ROLE_ATTR, null,
            Arrays.asList(attributeValue));
    }

    /**
     * Creates the purpose for use attribute.
     *
     * @param purposeCode the purpose code
     * @param purposeSystem the purpose system
     * @param purposeSystemName the purpose system name
     * @param purposeDisplay the purpose display
     * @return the attribute
     */
    Attribute createPurposeForUseAttribute(String purposeCode, String purposeSystem, String purposeSystemName,
        String purposeDisplay) {

        Object attributeValue = createHL7Attribute("PurposeForUse", purposeCode, purposeSystem, purposeSystemName,
            purposeDisplay);
        return OpenSAML2ComponentBuilder.getInstance().createAttribute(null, SamlConstants.PURPOSE_ROLE_ATTR, null,
            Arrays.asList(attributeValue));
    }

    /**
     * Creates the organization id attribute statement.
     *
     * @param organizationId the organization id
     * @return the list
     */
    public List<AttributeStatement> createOrganizationIdAttributeStatement(String organizationId) {
        List<AttributeStatement> statements = new ArrayList<>();
        Attribute attribute = createAttribute(null, SamlConstants.USER_ORG_ID_ATTR, null, Arrays.asList(organizationId));

        statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(Arrays.asList(attribute)));

        return statements;
    }
}
