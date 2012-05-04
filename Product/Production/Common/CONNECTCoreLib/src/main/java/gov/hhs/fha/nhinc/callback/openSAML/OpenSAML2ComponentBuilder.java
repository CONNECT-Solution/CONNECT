/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Action;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.DecisionTypeEnumeration;
import org.opensaml.saml2.core.Evidence;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.SubjectLocality;
import org.opensaml.xml.XMLObjectBuilderFactory;

/**
 * @author bhumphrey
 * 
 */
public class OpenSAML2ComponentBuilder implements SAMLCompontentBuilder {

	private static SAMLObjectBuilder<AuthnStatement> authnStatementBuilder;

	private static SAMLObjectBuilder<AuthnContext> authnContextBuilder;

	private static SAMLObjectBuilder<AuthnContextClassRef> authnContextClassRefBuilder;

	private static SAMLObjectBuilder<AttributeStatement> attributeStatementBuilder;

	private static SAMLObjectBuilder<Attribute> attributeBuilder;

	private static SAMLObjectBuilder<Assertion> assertionBuilder;

	private static SAMLObjectBuilder<NameID> nameIdBuilder;

	private static SAMLObjectBuilder<Conditions> conditionsBuilder;

	
	private static SAMLObjectBuilder<Action> actionElementBuilder;

	private static SAMLObjectBuilder<AuthzDecisionStatement> authorizationDecisionStatementBuilder;

	private static SAMLObjectBuilder<SubjectLocality> subjectLocalityBuilder;
	private static XMLObjectBuilderFactory builderFactory = Configuration
			.getBuilderFactory();

	private OpenSAML2ComponentBuilder() {
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
		
		nameIdBuilder = (SAMLObjectBuilder<NameID>) 
                builderFactory.getBuilder(NameID.DEFAULT_ELEMENT_NAME);
		
		 assertionBuilder = (SAMLObjectBuilder<Assertion>) 
	                builderFactory.getBuilder(Assertion.DEFAULT_ELEMENT_NAME);
		 
		   conditionsBuilder = (SAMLObjectBuilder<Conditions>) 
	                builderFactory.getBuilder(Conditions.DEFAULT_ELEMENT_NAME);
	
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

}
