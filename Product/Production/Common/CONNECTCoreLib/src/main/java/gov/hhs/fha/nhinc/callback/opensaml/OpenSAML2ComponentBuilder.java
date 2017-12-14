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

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.namespace.QName;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.saml.OpenSAMLUtil;
import org.apache.wss4j.common.saml.bean.ActionBean;
import org.apache.wss4j.common.saml.bean.AuthDecisionStatementBean;
import org.apache.wss4j.common.saml.bean.AuthenticationStatementBean;
import org.apache.wss4j.common.saml.bean.ConditionsBean;
import org.apache.wss4j.common.saml.bean.KeyInfoBean;
import org.apache.wss4j.common.saml.bean.NameIDBean;
import org.apache.wss4j.common.saml.bean.SubjectBean;
import org.apache.wss4j.common.saml.bean.SubjectConfirmationDataBean;
import org.apache.wss4j.common.saml.bean.SubjectLocalityBean;
import org.apache.wss4j.common.saml.builder.SAML1ComponentBuilder;
import org.apache.wss4j.common.saml.builder.SAML2ComponentBuilder;
import org.joda.time.DateTime;
import org.opensaml.core.xml.XMLObjectBuilderFactory;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.schema.XSAny;
import org.opensaml.core.xml.schema.XSURI;
import org.opensaml.core.xml.schema.impl.XSAnyBuilder;
import org.opensaml.saml.common.SAMLObjectBuilder;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.AttributeValue;
import org.opensaml.saml.saml2.core.AuthnStatement;
import org.opensaml.saml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml.saml2.core.Conditions;
import org.opensaml.saml.saml2.core.Evidence;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.Subject;
import org.opensaml.saml.saml2.core.SubjectConfirmation;
import org.opensaml.saml.saml2.core.SubjectConfirmationData;
import org.opensaml.security.SecurityException;
import org.opensaml.security.x509.BasicX509Credential;
import org.opensaml.xmlsec.signature.KeyInfo;
import org.opensaml.xmlsec.signature.Signature;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class OpenSAML2ComponentBuilder.
 *
 * @author bhumphrey
 */
public class OpenSAML2ComponentBuilder implements SAMLCompontentBuilder {

    /**
     * The Constant attributeStatementBuilder.
     */
    private final SAMLObjectBuilder<AttributeStatement> attributeStatementBuilder;

    /**
     * The Constant X509_NAME_ID.
     */
    private static final String X509_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";

    /**
     * The Constant NAME_FORMAT_STRING.
     */
    private static final String NAME_FORMAT_STRING = "urn:oasis:names:tc:SAML:2.0:attrname-format:uri";

    /**
     * The evidence builder.
     */
    private final SAMLObjectBuilder<Evidence> evidenceBuilder;

    /**
     * The xsAnyBuilder.
     */
    private final XSAnyBuilder xsAnyBuilder;

    /**
     * The instance.
     */
    private static OpenSAML2ComponentBuilder openSamlInstance;

    /**
     * The subject locality builder.
     */
    /**
     * The builder factory.
     */

    private static final Logger LOG = LoggerFactory.getLogger(OpenSAML2ComponentBuilder.class);

    /**
     * Instantiates a new open sam l2 component builder.
     */
    private OpenSAML2ComponentBuilder() {
        OpenSAMLUtil.initSamlEngine();
        final XMLObjectBuilderFactory builderFactory = XMLObjectProviderRegistrySupport.getBuilderFactory();
        xsAnyBuilder = (XSAnyBuilder) builderFactory.getBuilder(XSAny.TYPE_NAME);
        attributeStatementBuilder = (SAMLObjectBuilder<AttributeStatement>) builderFactory
            .getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);
        evidenceBuilder = (SAMLObjectBuilder<Evidence>) builderFactory.getBuilder(Evidence.DEFAULT_ELEMENT_NAME);
    }

    /**
     * Gets the single instance of OpenSAML2ComponentBuilder.
     *
     * @return single instance of OpenSAML2ComponentBuilder
     */
    public static OpenSAML2ComponentBuilder getInstance() {
        if (openSamlInstance == null) {
            openSamlInstance = new OpenSAML2ComponentBuilder();
        }
        return openSamlInstance;
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
    public AuthnStatement createAuthenticationStatements(final String cntxCls, final String sessionIndex,
        final DateTime authInstant, final String inetAddr, final String dnsName) {
        final AuthenticationStatementBean authenticationBean = new AuthenticationStatementBean();
        authenticationBean.setAuthenticationInstant(authInstant);
        authenticationBean.setSessionIndex(sessionIndex);
        authenticationBean.setAuthenticationMethod(cntxCls);
        final SubjectLocalityBean subjectLocalityBean = new SubjectLocalityBean();
        subjectLocalityBean.setDnsAddress(dnsName);
        subjectLocalityBean.setIpAddress(inetAddr);
        authenticationBean.setSubjectLocality(subjectLocalityBean);
        return SAML2ComponentBuilder.createAuthnStatement(Collections.singletonList(authenticationBean)).get(0);
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
    public AuthzDecisionStatement createAuthzDecisionStatement(final String resource, final String decisionTxt,
        final String action, final Evidence evidence) {
        final AuthDecisionStatementBean authzBean = new AuthDecisionStatementBean();
        final ActionBean actionBean = new ActionBean();
        actionBean.setActionNamespace(NhincConstants.ACTION_NAMESPACE_STRING);
        actionBean.setContents(action);
        authzBean.setActions(Collections.singletonList(actionBean));
        authzBean.setResource(resource);
        authzBean.setDecision(AuthDecisionStatementBean.Decision.valueOf(decisionTxt.toUpperCase()));
        authzBean.setEvidence(evidence);
        return SAML2ComponentBuilder.createAuthorizationDecisionStatement(Collections.singletonList(authzBean)).get(0);
    }

    /**
     * Creates the assertion.
     *
     * @param uuid
     * @return the assertion
     */
    public Assertion createAssertion(final String uuid) {
        final Assertion assertion = SAML2ComponentBuilder.createAssertion();
        assertion.setID(uuid);
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
    public NameID createNameID(final String qualifier, final String format, final String value) {
        final NameIDBean nameIDBean = new NameIDBean();
        nameIDBean.setNameQualifier(qualifier);
        nameIDBean.setNameIDFormat(format);
        nameIDBean.setNameValue(value);
        return SAML2ComponentBuilder.createNameID(nameIDBean);
    }

    /**
     * Creates the name id.
     *
     * @param format the format
     * @param value the value
     * @return the name id
     */
    private NameID createNameID(final String format, final String value) {
        return createNameID(null, format, value);
    }

    /**
     * Creates the issuer.
     *
     * @param format the format
     * @param sIssuer the s issuer
     * @return the issuer
     */
    public Issuer createIssuer(final String format, final String sIssuer) {
        return SAML2ComponentBuilder.createIssuer(sIssuer, format, null);
    }

    /**
     * Creates the default issuer.
     *
     * @return the issuer
     */
    protected Issuer createDefaultIssuer() {
        return createIssuer(X509_NAME_ID, NhincConstants.SAML_DEFAULT_ISSUER_NAME);
    }

    /**
     * Creates the subject.
     */
    public Subject createSubject(final String x509Name, final PublicKey publicKey)
        throws SAMLComponentBuilderException {
        Subject subject = null;
        try {
            final SubjectBean subjectBean = new SubjectBean();
            subject = SAML2ComponentBuilder.createSaml2Subject(subjectBean);
            subject.setNameID(createNameID(X509_NAME_ID, x509Name));
            subject.getSubjectConfirmations().remove(0);// remove send-vouches
            final SubjectConfirmationData subjectConfirmationData = createSubjectConfirmationData(publicKey);
            final SubjectConfirmation subjectConfirmation = createHoKConfirmation(subjectConfirmationData);
            subject.getSubjectConfirmations().add(subjectConfirmation);
        } catch (SecurityException | WSSecurityException e) {
            LOG.error("Unable to create Saml2Subject ", e.getLocalizedMessage());
            throw new SAMLComponentBuilderException(e.getLocalizedMessage(), e);
        }
        return subject;
    }

    /**
     * Returns subject Confirmation Data
     */
    private static SubjectConfirmation createHoKConfirmation(final SubjectConfirmationData subjectConfirmationData) {
        return SAML2ComponentBuilder.createSubjectConfirmation(SubjectConfirmation.METHOD_HOLDER_OF_KEY,
            subjectConfirmationData);
    }

    /**
     * Construct additional subject Confirmation for saml assertion. Since HOK(holder of key) is supported by default,
     * we support additional sender-vouches/bearer subject confirmation
     *
     * @param samlSubjectConfirmation
     * @return
     * @throws SAMLComponentBuilderException
     */
    public SubjectConfirmation createSubjectConfirmation(SAMLSubjectConfirmation samlSubjectConfirmation)
        throws SAMLComponentBuilderException {
        SubjectConfirmationData subjectConfirmData = createSubjectConfirmationData(null, samlSubjectConfirmation);
        String method = samlSubjectConfirmation.getMethod();
        LOG.debug("Prepare to construct method {} for subject confirmation", method);
        if (SubjectConfirmation.METHOD_SENDER_VOUCHES.equalsIgnoreCase(method)) {
            return SAML2ComponentBuilder.createSubjectConfirmation(SubjectConfirmation.METHOD_SENDER_VOUCHES,
                subjectConfirmData);
        }
        if (SubjectConfirmation.METHOD_BEARER.equalsIgnoreCase(method)) {
            return SAML2ComponentBuilder.createSubjectConfirmation(SubjectConfirmation.METHOD_BEARER,
                subjectConfirmData);
        }
        return null;
    }

    /**
     * Creates the subject confirmation data.
     *
     * @param certificate the certificate
     * @param publicKey the public key
     * @return the subject confirmation data
     * @throws SAMLComponentBuilderException the exception
     */
    private static SubjectConfirmationData createSubjectConfirmationData(final PublicKey publicKey)
        throws SAMLComponentBuilderException {
        final KeyInfoBean keyInforBean = new KeyInfoBean();
        keyInforBean.setPublicKey(publicKey);
        return createSubjectConfirmationData(keyInforBean, new SubjectConfirmationDataBean());
    }

    private static SubjectConfirmationData createSubjectConfirmationData(final KeyInfoBean keyInforBean,
        final SubjectConfirmationDataBean confirDataBean) throws SAMLComponentBuilderException {
        try {
            return SAML2ComponentBuilder.createSubjectConfirmationData(confirDataBean, keyInforBean);
        } catch (SecurityException | WSSecurityException e) {
            LOG.error(e.getLocalizedMessage());
            throw new SAMLComponentBuilderException(e.getLocalizedMessage(), e);
        }
    }

    /**
     * Gets the key info.
     *
     * @param certificate the certificate
     * @param publicKey the public key
     * @return the key info
     * @throws SAMLComponentBuilderException the exception
     */

    public KeyInfo getKeyInfo(final PublicKey publicKey) throws SAMLComponentBuilderException {
        final KeyInfoBean keyInfoBean = new KeyInfoBean();
        KeyInfo keyInfo = null;
        keyInfoBean.setPublicKey(publicKey);
        try {
            keyInfo = SAML1ComponentBuilder.createKeyInfo(keyInfoBean);
        } catch (SecurityException | WSSecurityException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new SAMLComponentBuilderException(e.getLocalizedMessage(), e);
        }

        return keyInfo;
    }

    /**
     * Gets the public key.
     *
     * @return the public key
     * @throws SAMLComponentBuilderException the exception
     */
    public PublicKey getPublicKey() throws SAMLComponentBuilderException {
        final CertificateManager cm = CertificateManagerImpl.getInstance();
        X509Certificate certificate = null;
        try {
            certificate = cm.getDefaultCertificate();
        } catch (final CertificateManagerException e) {
            throw new SAMLComponentBuilderException(e.getLocalizedMessage(), e);
        }
        return certificate.getPublicKey();
    }

    /**
     * Creates the assertion.
     *
     * @return the assertion
     */
    public Assertion createAssertion() {
        return SAML2ComponentBuilder.createAssertion();
    }

    /**
     * Creates the conditions.
     *
     * @param notBefore the not before
     * @param notAfter the not after
     * @param audienceURI the audience uri
     * @return the conditions
     */
    public Conditions createConditions(final DateTime notBefore, final DateTime notAfter) {
        final ConditionsBean conditionsBean = new ConditionsBean();
        conditionsBean.setNotAfter(notAfter);
        conditionsBean.setNotBefore(notBefore);
        return SAML2ComponentBuilder.createConditions(conditionsBean);
    }

    /**
     * Creates the attribute.
     *
     * @param friendlyName the friendly name
     * @param name the name
     * @param nameFormat the name format
     * @return the attribute
     */
    Attribute createAttribute(final String friendlyName, final String name, final String nameFormat) {
        return SAML2ComponentBuilder.createAttribute(friendlyName, name,
            StringUtils.defaultIfBlank(nameFormat, NAME_FORMAT_STRING));
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
    Attribute createAttribute(final String friendlyName, final String name, final String nameFormat,
        final List<?> values) {
        return SAML2ComponentBuilder.createAttribute(friendlyName, name, nameFormat, (List<Object>) values);
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
    XSAny createAny(final String namespace, final String name, final String prefix,
        final Map<QName, String> attributes) {
        final XSAny any = createAny(namespace, name, prefix);
        for (final Entry<QName, String> keyValue : attributes.entrySet()) {
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
        final Map<QName, String> attributes) {
        final XSAny attribute = createAny(namespace, name, prefix, attributes);
        return createAttributeValue(Arrays.asList(attribute));
    }

    /**
     * Creates the attribute value.
     *
     * @param values the values
     * @return the xS any
     */

    XSAny createAttributeValue(final List<XSAny> values) {
        final XSAny attributeValue = xsAnyBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
        attributeValue.getUnknownXMLObjects().addAll(values);
        return attributeValue;
    }

    /**
     * Creates the attribute statement.
     *
     * @param attributes the attributes
     * @return the list
     */

    public List<AttributeStatement> createAttributeStatement(final List<Attribute> attributes) {
        final List<AttributeStatement> attributeStatements = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(attributes)) {
            final AttributeStatement attributeStatement = attributeStatementBuilder.buildObject();
            for (final Attribute attribute : attributes) {
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

    public Evidence createEvidence(final List<Assertion> assertions) {
        final Evidence evidence = evidenceBuilder.buildObject();
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
    public List<AttributeStatement> createEvidenceStatements(final List accessConstentValues,
        final List evidenceInstanceAccessConsentValues, final String namespace) {
        List<AttributeStatement> statements = new ArrayList<>();
        final List<Attribute> attributes = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(accessConstentValues)) {
            attributes.add(createAttribute(null, SamlConstants.ACCESS_CONSENT_ATTR, namespace, accessConstentValues));
        }
        if (CollectionUtils.isNotEmpty(evidenceInstanceAccessConsentValues)) {
            attributes.add(createAttribute(null, SamlConstants.INST_ACCESS_CONSENT_ATTR, namespace,
                evidenceInstanceAccessConsentValues));
        }
        if (CollectionUtils.isNotEmpty(attributes)) {
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
    public Attribute createUserRoleAttribute(final String userCode, final String userSystem,
        final String userSystemName, final String userDisplay) {
        final Object attributeValue = createHL7Attribute("Role", userCode, userSystem, userSystemName, userDisplay);
        if (OpenSAML2ComponentBuilder.getInstance() != null) {
            return OpenSAML2ComponentBuilder.getInstance().createAttribute(null, SamlConstants.USER_ROLE_ATTR, null,
                Arrays.asList(attributeValue));
        }
        return null;
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
    public XSAny createHL7Attribute(final String name, final String code, final String codeSystem,
        final String codeSystemName, final String displayName) {
        final Map<QName, String> userRoleAttributes = new HashMap<>();

        final boolean hasHl7prefix = getHl7PrefixProperty();

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

        return createAttributeValue(SamlConstants.HL7_NAMESPACE_URI, name, SamlConstants.HL7_PREFIX,
            userRoleAttributes);

    }

    QName createHl7QName(final String name, final boolean hasPrefix) {
        return hasPrefix ? new QName(SamlConstants.HL7_NAMESPACE_URI, name, SamlConstants.HL7_PREFIX) : new QName(name);
    }

    boolean getHl7PrefixProperty() {
        try {
            return PropertyAccessor.getInstance().getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HL7_PREFIX_FOR_ATTR_PROPERTY);
        } catch (final PropertyAccessException ex) {
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
    public Attribute createPatientIDAttribute(final String patientId) {
        return createAttribute(null, SamlConstants.PATIENT_ID_ATTR, null, Collections.singletonList(patientId));
    }

    public XSAny createUriAttributeValue(String value) {
        XSAny uri = createAny(SAMLConstants.SAML20_NS,
            org.opensaml.saml.saml1.core.AttributeValue.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20_PREFIX);
        uri.setTextContent(value);
        uri.getUnknownAttributes().put(new QName(SamlConstants.HL7_TYPE_NAMESPACE_URI,
            SamlConstants.HL7_TYPE_LOCAL_PART, SamlConstants.HL7_TYPE_PREFIX), XSURI.TYPE_NAME);
        return uri;
    }

    /**
     * Creates the npi attribute.
     *
     * @param npi the npi
     * @return the attribute
     */
    public Attribute createNPIAttribute(final String npi) {
        return createAttribute(null, SamlConstants.ATTRIBUTE_NAME_NPI, null, Arrays.asList(npi));
    }

    /**
     * Creates the home communit attribute statement.
     *
     * @param communityId the community id
     * @return the list
     */
    public List<AttributeStatement> createHomeCommunitAttributeStatement(final String communityId) {
        final List<AttributeStatement> statements = new ArrayList<>();
        final Attribute attribute = createHomeCommunityAttribute(communityId);

        statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(Arrays.asList(attribute)));

        return statements;
    }

    /**
     * Creates the home community attribute.
     *
     * @param communityId the community id
     * @return the attribute
     */
    Attribute createHomeCommunityAttribute(final String communityId) {
        return createAttribute(null, SamlConstants.HOME_COM_ID_ATTR, null, Arrays.asList(communityId));
    }

    /**
     * Creates the signature.
     *
     * @param certificate the certificate
     * @param privateKey the private key
     * @param publicKey the public key
     * @return the signature
     * @throws SAMLComponentBuilderException the exception
     */
    public Signature createSignature(final X509Certificate certificate, final PrivateKey privateKey,
        final PublicKey publicKey) throws SAMLAssertionBuilderException {
        final BasicX509Credential credential = new BasicX509Credential(certificate, privateKey);
        credential.setEntityCertificate(certificate);
        credential.setPrivateKey(privateKey);

        final Signature signature = OpenSAMLUtil.buildSignature();
        signature.setSigningCredential(credential);

        signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);

        signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
        try {
            signature.setKeyInfo(getKeyInfo(publicKey));
        } catch (final Exception ex) {
            throw new SAMLAssertionBuilderException(ex.getLocalizedMessage(), ex);
        }
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
    public List<AttributeStatement> createPurposeOfUseAttributeStatements(final String purposeCode,
        final String purposeSystem, final String purposeSystemName, final String purposeDisplay) {

        final List<AttributeStatement> statements = new ArrayList<>();
        final Attribute attribute = createPurposeOfUseAttribute(purposeCode, purposeSystem, purposeSystemName,
            purposeDisplay);
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
    public List<AttributeStatement> createPurposeForUseAttributeStatements(final String purposeCode,
        final String purposeSystem, final String purposeSystemName, final String purposeDisplay) {
        final List<AttributeStatement> statements = new ArrayList<>();
        final Attribute attribute = createPurposeForUseAttribute(purposeCode, purposeSystem, purposeSystemName,
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
    public Attribute createPurposeOfUseAttribute(final String purposeCode, final String purposeSystem,
        final String purposeSystemName, final String purposeDisplay) {
        final Object attributeValue = createHL7Attribute("PurposeOfUse", purposeCode, purposeSystem, purposeSystemName,
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
    Attribute createPurposeForUseAttribute(final String purposeCode, final String purposeSystem,
        final String purposeSystemName, final String purposeDisplay) {

        final Object attributeValue = createHL7Attribute("PurposeForUse", purposeCode, purposeSystem, purposeSystemName,
            purposeDisplay);
        if (OpenSAML2ComponentBuilder.getInstance() != null) {
            return OpenSAML2ComponentBuilder.getInstance().createAttribute(null, SamlConstants.PURPOSE_ROLE_ATTR, null,
                Arrays.asList(attributeValue));
        }
        return null;
    }

    /**
     * Creates the organization id attribute statement.
     *
     * @param organizationId the organization id
     * @return the list
     */
    public List<AttributeStatement> createOrganizationIdAttributeStatement(final String organizationId) {
        final List<AttributeStatement> statements = new ArrayList<>();
        final Attribute attribute = createAttribute(null, SamlConstants.USER_ORG_ID_ATTR, null,
            Arrays.asList(organizationId));
        statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(Arrays.asList(attribute)));
        return statements;
    }
}
