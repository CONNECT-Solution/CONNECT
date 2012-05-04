/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.xml.namespace.QName;

import gov.hhs.fha.nhinc.callback.SamlConstants;

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
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectLocality;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.signature.Exponent;
import org.opensaml.xml.signature.Modulus;
import org.opensaml.xml.signature.RSAKeyValue;

/**
 * @author bhumphrey
 * 
 */
public class OpenSAML2ComponentBuilder implements SAMLCompontentBuilder {

    private static final String X509_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";
    private static final String DEFAULT_ISSUER_VALUE = "CN=SAML User,OU=SU,O=SAML User,L=Los Angeles,ST=CA,C=US";
    
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
	
    private XMLObject createOpenSAMLObject(QName qname) {
        return Configuration.getBuilderFactory().getBuilder(qname).buildObject(qname);
    }

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
	
    /**
     * @return
     */
    private org.opensaml.saml2.core.NameID createNameID(String format, String value) {
        org.opensaml.saml2.core.NameID nameId = (org.opensaml.saml2.core.NameID) createOpenSAMLObject(org.opensaml.saml2.core.NameID.DEFAULT_ELEMENT_NAME);

        nameId.setFormat(format);
        nameId.setValue(value);

        return nameId;
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
        Issuer issuer = (Issuer) createOpenSAMLObject(Issuer.DEFAULT_ELEMENT_NAME);
        issuer.setFormat(X509_NAME_ID);
        issuer.setValue(DEFAULT_ISSUER_VALUE);
        return issuer;
    }
    /**
     * @return
     */
    public Subject createSubject(String x509Name) {
        Subject subject = (org.opensaml.saml2.core.Subject) createOpenSAMLObject(Subject.DEFAULT_ELEMENT_NAME);
        subject.setNameID(createNameID(X509_NAME_ID, x509Name));
        subject.getSubjectConfirmations().add(createHoKConfirmation());
        return subject;
    }

    private org.opensaml.saml2.core.SubjectConfirmation createHoKConfirmation() {
        org.opensaml.saml2.core.SubjectConfirmation subjectConfirmation = (org.opensaml.saml2.core.SubjectConfirmation) createOpenSAMLObject(org.opensaml.saml2.core.SubjectConfirmation.DEFAULT_ELEMENT_NAME);
        subjectConfirmation.setMethod(org.opensaml.saml2.core.SubjectConfirmation.METHOD_HOLDER_OF_KEY);
        subjectConfirmation.setSubjectConfirmationData(createSubjectConfirmationData());

        return subjectConfirmation;
    }

    private org.opensaml.saml2.core.SubjectConfirmationData createSubjectConfirmationData() {
        org.opensaml.saml2.core.SubjectConfirmationData subjectConfirmationData = (org.opensaml.saml2.core.SubjectConfirmationData) createOpenSAMLObject(org.opensaml.saml2.core.SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
        subjectConfirmationData.getUnknownAttributes().put(
                new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi"),
                "saml:KeyInfoConfirmationDataType");

        org.opensaml.xml.signature.KeyInfo ki = (org.opensaml.xml.signature.KeyInfo) createOpenSAMLObject(org.opensaml.xml.signature.KeyInfo.DEFAULT_ELEMENT_NAME);
        org.opensaml.xml.signature.KeyValue kv = (org.opensaml.xml.signature.KeyValue) createOpenSAMLObject(org.opensaml.xml.signature.KeyValue.DEFAULT_ELEMENT_NAME);

        RSAKeyValue _RSAKeyValue = (RSAKeyValue) createOpenSAMLObject(RSAKeyValue.DEFAULT_ELEMENT_NAME);
        Exponent exp = (Exponent) createOpenSAMLObject(Exponent.DEFAULT_ELEMENT_NAME);
        Modulus mod = (Modulus) createOpenSAMLObject(Modulus.DEFAULT_ELEMENT_NAME);

        RSAPublicKey RSAPk = (RSAPublicKey) getPublicKey();

        exp.setValue(RSAPk.getPublicExponent().toString());
        _RSAKeyValue.setExponent(exp);
        mod.setValue(RSAPk.getModulus().toString());
        _RSAKeyValue.setModulus(mod);
        
        kv.setRSAKeyValue(_RSAKeyValue);
        ki.getKeyValues().add(kv);

        subjectConfirmationData.getUnknownXMLObjects().add(ki);

        return subjectConfirmationData;
    }
    
    public PublicKey getPublicKey() {
        KeyStore ks;
        KeyStore.PrivateKeyEntry pkEntry = null;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            char[] password = "cspass".toCharArray();
            FileInputStream fis = new FileInputStream("c:/opt/keystores/clientKeystore.jks");
            ks.load(fis, password);
            fis.close();
            pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry("myclientkey",
                    new KeyStore.PasswordProtection("ckpass".toCharArray()));
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        X509Certificate certificate = (X509Certificate) pkEntry.getCertificate();
        return certificate.getPublicKey();
    }

    /**
     * @return
     */
    public org.opensaml.saml2.core.Assertion createAssertion() {
        Assertion assertion = assertionBuilder.buildObject(
                Assertion.DEFAULT_ELEMENT_NAME, Assertion.TYPE_NAME);
        return assertion;
    }

}
