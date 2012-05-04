/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import gov.hhs.fha.nhinc.callback.SamlConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.joda.time.DateTime;
import org.opensaml.Configuration;
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
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.SubjectLocality;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSAnyBuilder;
import org.opensaml.xml.schema.impl.XSStringBuilder;

/**
 * @author bhumphrey
 * 
 */
public class OpenSAML2ComponentBuilder implements SAMLCompontentBuilder {

	private SAMLObjectBuilder<AuthnStatement> authnStatementBuilder;

	private SAMLObjectBuilder<AuthnContext> authnContextBuilder;

	private SAMLObjectBuilder<AuthnContextClassRef> authnContextClassRefBuilder;

	private SAMLObjectBuilder<AttributeStatement> attributeStatementBuilder;

	private SAMLObjectBuilder<Attribute> attributeBuilder;

	private SAMLObjectBuilder<Assertion> assertionBuilder;

	private SAMLObjectBuilder<NameID> nameIdBuilder;

	private SAMLObjectBuilder<Conditions> conditionsBuilder;

	private SAMLObjectBuilder<Action> actionElementBuilder;

	private SAMLObjectBuilder<AuthzDecisionStatement> authorizationDecisionStatementBuilder;

	private SAMLObjectBuilder<SubjectLocality> subjectLocalityBuilder;
	private XMLObjectBuilderFactory builderFactory;

	private SAMLObjectBuilder<AudienceRestriction> audienceRestrictionBuilder;

	private SAMLObjectBuilder<Audience> audienceBuilder;

	private XSStringBuilder stringBuilder;

	private SAMLObjectBuilder<Evidence> evidenceBuilder;

	private XSAnyBuilder xsAnyBuilder;

	private OpenSAML2ComponentBuilder() {

		builderFactory = Configuration.getBuilderFactory();

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

		actionElementBuilder = (SAMLObjectBuilder<Action>) builderFactory
				.getBuilder(Action.DEFAULT_ELEMENT_NAME);

		nameIdBuilder = (SAMLObjectBuilder<NameID>) builderFactory
				.getBuilder(NameID.DEFAULT_ELEMENT_NAME);

		assertionBuilder = (SAMLObjectBuilder<Assertion>) builderFactory
				.getBuilder(Assertion.DEFAULT_ELEMENT_NAME);

		conditionsBuilder = (SAMLObjectBuilder<Conditions>) builderFactory
				.getBuilder(Conditions.DEFAULT_ELEMENT_NAME);

		audienceRestrictionBuilder = (SAMLObjectBuilder<AudienceRestriction>) builderFactory
				.getBuilder(AudienceRestriction.DEFAULT_ELEMENT_NAME);

		audienceBuilder = (SAMLObjectBuilder<Audience>) builderFactory
				.getBuilder(Audience.DEFAULT_ELEMENT_NAME);

		evidenceBuilder = (SAMLObjectBuilder<Evidence>) builderFactory
				.getBuilder(Evidence.DEFAULT_ELEMENT_NAME);

		stringBuilder = (XSStringBuilder) builderFactory
				.getBuilder(XSString.TYPE_NAME);

		xsAnyBuilder = (XSAnyBuilder) builderFactory
				.getBuilder(XSAny.TYPE_NAME);

	}

	private static OpenSAML2ComponentBuilder INSTANCE = new OpenSAML2ComponentBuilder();

	public static OpenSAML2ComponentBuilder getInstance() {
		return INSTANCE;
	}

	/**
	 * @return
	 */
	public AuthnStatement createAuthenicationStatements(String cntxCls,
			String sessionIndex, DateTime authInstant, String inetAddr,
			String dnsName) {

		AuthnStatement authnStatement = authnStatementBuilder.buildObject();

		AuthnContextClassRef authnContextClassRef = authnContextClassRefBuilder
				.buildObject();

		authnContextClassRef.setAuthnContextClassRef(cntxCls);

		AuthnContext authnContext = authnContextBuilder.buildObject();
		authnContext.setAuthnContextClassRef(authnContextClassRef);
		authnStatement.setAuthnContext(authnContext);

		authnStatement.setSessionIndex(sessionIndex);

		authnStatement.setAuthnInstant(authInstant);

		if (inetAddr != null && dnsName != null) {

			SubjectLocality subjectLocality = subjectLocalityBuilder
					.buildObject();
			subjectLocality.setDNSName(dnsName);
			subjectLocality.setAddress(inetAddr);

			authnStatement.setSubjectLocality(subjectLocality);
		}

		return authnStatement;
	}

	public AuthzDecisionStatement createAuthzDecisionStatement(String resource,
			String decisionTxt, String action, Evidence evidence) {
		AuthzDecisionStatement authDecision = authorizationDecisionStatementBuilder
				.buildObject();
		authDecision.setResource(resource);

		DecisionTypeEnumeration decision = DecisionTypeEnumeration.DENY;

		authDecision.setDecision(decision);

		Action actionElement = actionElementBuilder.buildObject();
		actionElement
				.setNamespace("urn:oasis:names:tc:SAML:1.0:action:rwedc-negation");
		actionElement.setAction(action);

		authDecision.getActions().add(actionElement);
		authDecision.setEvidence(evidence);

		return authDecision;

	}

	public Assertion createAssertion(final String uuid) {
		Assertion assertion = assertionBuilder.buildObject(
				Assertion.DEFAULT_ELEMENT_NAME, Assertion.TYPE_NAME);
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

	public Conditions createConditions(DateTime notBefore, DateTime notAfter,
			String audienceURI) {
		Conditions conditions = conditionsBuilder.buildObject();

		conditions.setNotBefore(notBefore);
		conditions.setNotOnOrAfter(notAfter);

		AudienceRestriction audienceRestriction = audienceRestrictionBuilder
				.buildObject();
		Audience audience = audienceBuilder.buildObject();
		audience.setAudienceURI(audienceURI);
		audienceRestriction.getAudiences().add(audience);
		conditions.getAudienceRestrictions().add(audienceRestriction);

		return conditions;
	}

	public Attribute createAttribute(String friendlyName, String name,
			String nameFormat) {

		Attribute attribute = attributeBuilder.buildObject();
		attribute.setFriendlyName(friendlyName);
		if (nameFormat == null) {
			attribute
					.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:uri");
		} else {
			attribute.setNameFormat(nameFormat);
		}
		attribute.setName(name);
		return attribute;
	}

	public Attribute createAttribute(String friendlyName, String name,
			String nameFormat, List<?> values) {

		Attribute attribute = createAttribute(friendlyName, name, nameFormat);

		for (Object value : values) {
			if (value instanceof String) {
				XSString attributeValue = stringBuilder
						.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME,
								XSString.TYPE_NAME);
				attributeValue.setValue((String) value);
				attribute.getAttributeValues().add(attributeValue);
			} else if (value instanceof XMLObject) {
				attribute.getAttributeValues().add((XMLObject) value);
			}
		}

		return attribute;
	}

	public XSAny createAttributeValue(final String namespace,
			final String name, final String prefix,
			Map<String, String> attributes) {

		XSAny attributeValue = xsAnyBuilder
				.buildObject(namespace, name, prefix);
		for (String atrName : attributes.keySet()) {
			attributeValue.getUnknownAttributes().put(new QName(atrName),
					attributes.get(atrName));

		}
		return attributeValue;
	}

	public List<AttributeStatement> createAttributeStatement(
			List<Attribute> attributes) {
		List<AttributeStatement> attributeStatements = new ArrayList<AttributeStatement>();
		if (attributes != null && attributes.size() > 0) {

			AttributeStatement attributeStatement = attributeStatementBuilder
					.buildObject();
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

	public List<AttributeStatement> createEvidenceStatements(
			List accessConstentValues,
			List evidenceInstanceAccessConsentValues, final String namespace) {
		List<AttributeStatement> statements = new ArrayList<AttributeStatement>();

		List<Attribute> attributes = new ArrayList<Attribute>();

		if (accessConstentValues != null) {
			attributes.add(OpenSAML2ComponentBuilder.getInstance()
					.createAttribute("AccessConsentPolicy", namespace, null,
							accessConstentValues));
		}

		if (evidenceInstanceAccessConsentValues != null) {
			attributes.add(OpenSAML2ComponentBuilder.getInstance()
					.createAttribute("InstanceAccessConsentPolicy", namespace,
							null, evidenceInstanceAccessConsentValues));
		}
		if (!attributes.isEmpty()) {
			statements = OpenSAML2ComponentBuilder.getInstance()
					.createAttributeStatement(attributes);
		}

		return statements;
	}

}
