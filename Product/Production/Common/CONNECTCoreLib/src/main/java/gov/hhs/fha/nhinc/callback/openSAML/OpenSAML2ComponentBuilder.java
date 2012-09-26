/**
 *
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

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
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
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

import gov.hhs.fha.nhinc.callback.SamlConstants;

/**
 * @author bhumphrey
 * 
 */
public class OpenSAML2ComponentBuilder implements SAMLCompontentBuilder {

    private final SAMLObjectBuilder<AuthnStatement> authnStatementBuilder;

    private final SAMLObjectBuilder<AuthnContext> authnContextBuilder;

    private final SAMLObjectBuilder<AuthnContextClassRef> authnContextClassRefBuilder;

    private final SAMLObjectBuilder<AttributeStatement> attributeStatementBuilder;

    private final SAMLObjectBuilder<Attribute> attributeBuilder;
    private static final String X509_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";
    private static final String DEFAULT_ISSUER_VALUE = "CN=SAML User,OU=SU,O=SAML User,L=Los Angeles,ST=CA,C=US";

    private final SAMLObjectBuilder<Assertion> assertionBuilder;

    private final SAMLObjectBuilder<NameID> nameIdBuilder;

    private final SAMLObjectBuilder<Conditions> conditionsBuilder;

    private final SAMLObjectBuilder<Action> actionElementBuilder;

    private final SAMLObjectBuilder<AuthzDecisionStatement> authorizationDecisionStatementBuilder;

    private final SAMLObjectBuilder<AudienceRestriction> audienceRestrictionBuilder;

    private final SAMLObjectBuilder<Audience> audienceBuilder;

    private final XSStringBuilder stringBuilder;

    private final SAMLObjectBuilder<Evidence> evidenceBuilder;

    private final XSAnyBuilder xsAnyBuilder;

    private static SAMLObjectBuilder<SubjectLocality> subjectLocalityBuilder;

    private static XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();

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

        audienceRestrictionBuilder = (SAMLObjectBuilder<AudienceRestriction>) builderFactory
                .getBuilder(AudienceRestriction.DEFAULT_ELEMENT_NAME);

        audienceBuilder = (SAMLObjectBuilder<Audience>) builderFactory.getBuilder(Audience.DEFAULT_ELEMENT_NAME);

        evidenceBuilder = (SAMLObjectBuilder<Evidence>) builderFactory.getBuilder(Evidence.DEFAULT_ELEMENT_NAME);

        stringBuilder = (XSStringBuilder) builderFactory.getBuilder(XSString.TYPE_NAME);

        attributeBuilder = (SAMLObjectBuilder<Attribute>) builderFactory.getBuilder(Attribute.DEFAULT_ELEMENT_NAME);

        xsAnyBuilder = (XSAnyBuilder) builderFactory.getBuilder(XSAny.TYPE_NAME);

    }

    private static OpenSAML2ComponentBuilder INSTANCE;

    public static OpenSAML2ComponentBuilder getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new OpenSAML2ComponentBuilder();
            } catch (ConfigurationException e) {
                e.printStackTrace();
                INSTANCE = null;
            }

        }
        return INSTANCE;
    }

    private XMLObject createOpenSAMLObject(QName qname) {
        return builderFactory.getBuilder(qname).buildObject(qname);
    }

    /**
     * Create Authentication Statements.
     * 
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

    public Assertion createAssertion(final String uuid) {
        Assertion assertion = assertionBuilder.buildObject(Assertion.DEFAULT_ELEMENT_NAME, Assertion.TYPE_NAME);
        assertion.setID("_" + uuid);
        assertion.setVersion(SAMLVersion.VERSION_20);
        assertion.setIssueInstant(new DateTime());
        return assertion;
    }

    @SuppressWarnings("unchecked")
    public NameID createNameID(String qualifier, String format, String value) {
        NameID nameID = nameIdBuilder.buildObject();
        nameID.setNameQualifier(qualifier);
        nameID.setFormat(format);
        nameID.setValue(value);
        return nameID;
    }

    /**
     * @return
     */
    private NameID createNameID(String format, String value) {
        return createNameID(null, format, value);
    }

    /**
     * @param format
     * @param sIssuer
     * @return
     */
    public Issuer createIssuer(String format, String sIssuer) {
        Issuer issuer = (Issuer) createOpenSAMLObject(Issuer.DEFAULT_ELEMENT_NAME);
        issuer.setFormat(format);
        issuer.setValue(sIssuer);
        return issuer;
    }

    protected Issuer createDefaultIssuer() {
        return createIssuer(X509_NAME_ID, DEFAULT_ISSUER_VALUE);
    }

    /**
     * @param certificate
     * @return
     * @throws Exception
     */
    public Subject createSubject(String x509Name, X509Certificate certificate, PublicKey publicKey) throws Exception {
        Subject subject = (org.opensaml.saml2.core.Subject) createOpenSAMLObject(Subject.DEFAULT_ELEMENT_NAME);
        subject.setNameID(createNameID(X509_NAME_ID, x509Name));

        SubjectConfirmationData subjectConfirmationData = createSubjectConfirmationData(certificate, publicKey);
        SubjectConfirmation subjectConfirmation = createHoKConfirmation(subjectConfirmationData);
        subject.getSubjectConfirmations().add(subjectConfirmation);
        return subject;
    }

    private SubjectConfirmation createHoKConfirmation(SubjectConfirmationData subjectConfirmationData) throws Exception {
        SubjectConfirmation subjectConfirmation = (SubjectConfirmation) createOpenSAMLObject(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
        subjectConfirmation.setMethod(org.opensaml.saml2.core.SubjectConfirmation.METHOD_HOLDER_OF_KEY);
        subjectConfirmation.setSubjectConfirmationData(subjectConfirmationData);

        return subjectConfirmation;
    }

    private SubjectConfirmationData createSubjectConfirmationData(X509Certificate certificate, PublicKey publicKey)
            throws Exception {
        SubjectConfirmationData subjectConfirmationData = (SubjectConfirmationData) createOpenSAMLObject(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);

        subjectConfirmationData.getUnknownXMLObjects().add(getKeyInfo(certificate, publicKey));
        return subjectConfirmationData;
    }

    public KeyInfo getKeyInfo(X509Certificate certificate, PublicKey publicKey) throws Exception {
        KeyInfo ki = (KeyInfo) createOpenSAMLObject(KeyInfo.DEFAULT_ELEMENT_NAME);

        BasicX509Credential credential = new BasicX509Credential();
        credential.setEntityCertificate(certificate);

        KeyInfoHelper.addPublicKey(ki, publicKey);
        // KeyInfoHelper.addCertificate(ki, certificate);
        return ki;
    }

    public PublicKey getPublicKey() throws Exception {
        CertificateManager cm = CertificateManagerImpl.getInstance();

        X509Certificate certificate = cm.getDefaultCertificate();
        return certificate.getPublicKey();
    }

    /**
     * @return
     */
    public Assertion createAssertion() {
        Assertion assertion = (Assertion) createOpenSAMLObject(Assertion.DEFAULT_ELEMENT_NAME);
        return assertion;
    }

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

    XSAny createAny(final String namespace, final String name, final String prefix) {
        XSAny any = xsAnyBuilder.buildObject(namespace, name, prefix);
        return any;
    }

    XSAny createAny(final String namespace, final String name, final String prefix, Map<QName, String> attributes) {

        XSAny any = createAny(namespace, name, prefix);

        for (QName atrName : attributes.keySet()) {
            any.getUnknownAttributes().put(atrName, attributes.get(atrName));
        }
        return any;

    }

    XSAny createAttributeValue(final String namespace, final String name, final String prefix,
            Map<QName, String> attributes) {

        XSAny attribute = createAny(namespace, name, prefix, attributes);
        return createAttributeValue(Arrays.asList(attribute));
    }

    XSAny createAttributeValue(List<XSAny> values) {

        XSAny attributeValue = xsAnyBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        attributeValue.getUnknownXMLObjects().addAll(values);

        return attributeValue;
    }

    List<AttributeStatement> createAttributeStatement(List<Attribute> attributes) {
        List<AttributeStatement> attributeStatements = new ArrayList<AttributeStatement>();
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

    public Evidence createEvidence(List<Assertion> assertions) {
        Evidence evidence = evidenceBuilder.buildObject();
        evidence.getAssertions().addAll(assertions);
        return evidence;
    }

    public List<AttributeStatement> createEvidenceStatements(List accessConstentValues,
            List evidenceInstanceAccessConsentValues, final String namespace) {
        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();

        List<Attribute> attributes = new ArrayList<Attribute>();

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

    public Attribute createUserRoleAttribute(String userCode, String userSystem, String userSystemName,
            String userDisplay) {
        Object attributeValue = createHL7Attribute("Role", userCode, userSystem, userSystemName, userDisplay);

        return OpenSAML2ComponentBuilder.getInstance().createAttribute(null, SamlConstants.USER_ROLE_ATTR, null,
                Arrays.asList(attributeValue));
    }

    public XSAny createHL7Attribute(String name, String code, String codeSystem, String codeSystemName,
            String displayName) {
        Map<QName, String> userRoleAttributes = new HashMap<QName, String>();

        if (code != null) {
            userRoleAttributes.put(new QName(SamlConstants.CE_CODE_ID), code);
        }

        if (codeSystem != null) {
            userRoleAttributes.put(new QName(SamlConstants.CE_CODESYS_ID), codeSystem);
        }

        if (codeSystemName != null) {
            userRoleAttributes.put(new QName(SamlConstants.CE_CODESYSNAME_ID), codeSystemName);
        }

        if (displayName != null) {
            userRoleAttributes.put(new QName(SamlConstants.CE_DISPLAYNAME_ID), displayName);
        }

        userRoleAttributes.put(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "hl7:CE");

        XSAny attributeValue = createAttributeValue("urn:hl7-org:v3", name, "hl7", userRoleAttributes);
        return attributeValue;
    }

    public Attribute createPatientIDAttribute(String patientId) {
        return createAttribute(null, SamlConstants.PATIENT_ID_ATTR, null, Arrays.asList(patientId));
    }

    public List<AttributeStatement> createHomeCommunitAttributeStatement(String communityId) {
        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
        Attribute attribute = createHomeCommunityAttribute(communityId);

        statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(Arrays.asList(attribute)));

        return statements;
    }

    /**
     * @param communityId
     * @return
     */
    Attribute createHomeCommunityAttribute(String communityId) {
        return createAttribute(null, SamlConstants.HOME_COM_ID_ATTR, null, Arrays.asList(communityId));
    }

    /**
     * @param certificate
     * @param privateKey
     * @throws Exception
     * 
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
     * @param purposeCode
     * @param purposeSystem
     * @param purposeSystemName
     * @param purposeDisplay
     * @return
     */
    public List<AttributeStatement> createPurposeOfUseAttributeStatements(String purposeCode, String purposeSystem,
            String purposeSystemName, String purposeDisplay) {

        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
        Attribute attribute = createPurposeOfUseAttribute(purposeCode, purposeSystem, purposeSystemName, purposeDisplay);
        statements.addAll(createAttributeStatement(Arrays.asList(attribute)));
        return statements;
    }

    /**
     * PurposeForUse attribute statements.
     * 
     * @param purposeCode
     * @param purposeSystem
     * @param purposeSystemName
     * @param purposeDisplay
     * @return
     */
    public List<AttributeStatement> createPurposeForUseAttributeStatements(String purposeCode, String purposeSystem,
            String purposeSystemName, String purposeDisplay) {
        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
        Attribute attribute = createPurposeForUseAttribute(purposeCode, purposeSystem, purposeSystemName,
                purposeDisplay);
        statements.addAll(createAttributeStatement(Arrays.asList(attribute)));
        return statements;
    }

    public Attribute createPurposeOfUseAttribute(String purposeCode, String purposeSystem, String purposeSystemName,
            String purposeDisplay) {
        return createPurposeOfUseAttribute(purposeCode, purposeSystem, purposeSystemName, purposeDisplay,
                "PurposeOfUse");
    }

    Attribute createPurposeForUseAttribute(String purposeCode, String purposeSystem, String purposeSystemName,
            String purposeDisplay) {
        return createPurposeOfUseAttribute(purposeCode, purposeSystem, purposeSystemName, purposeDisplay,
                "PurposeForUse");
    }

    Attribute createPurposeOfUseAttribute(String purposeCode, String purposeSystem, String purposeSystemName,
            String purposeDisplay, String attributeName) {
        Map<QName, String> purposeOfUseAttributes = new HashMap<QName, String>();

        if (purposeCode != null) {
            purposeOfUseAttributes.put(new QName(SamlConstants.CE_CODE_ID), purposeCode);
        }

        if (purposeSystem != null) {
            purposeOfUseAttributes.put(new QName(SamlConstants.CE_CODESYS_ID), purposeSystem);
        }

        if (purposeSystemName != null) {
            purposeOfUseAttributes.put(new QName(SamlConstants.CE_CODESYSNAME_ID), purposeSystemName);
        }

        if (purposeDisplay != null) {
            purposeOfUseAttributes.put(new QName(SamlConstants.CE_DISPLAYNAME_ID), purposeDisplay);
        }

        Object attributeValue = OpenSAML2ComponentBuilder.getInstance().createAttributeValue("urn:hl7-org:v3",
                attributeName, "hl7", purposeOfUseAttributes);

        return OpenSAML2ComponentBuilder.getInstance().createAttribute(null, SamlConstants.PURPOSE_ROLE_ATTR, null,
                Arrays.asList(attributeValue));

    }

}
