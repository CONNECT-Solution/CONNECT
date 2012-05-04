/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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

import com.sun.org.apache.xml.internal.security.keys.KeyInfo;
import com.sun.xml.wss.XWSSecurityException;
import java.io.*;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import com.sun.xml.wss.impl.callback.*;
import com.sun.xml.wss.saml.*;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Statement;
/*import org.opensaml.saml2.core.impl.AssertionBuilder;
 import org.opensaml.saml2.core.impl.IssuerBuilder;
 import org.opensaml.saml2.core.impl.NameIDBuilder;
 import org.opensaml.saml2.core.impl.SubjectBuilder;
 import org.opensaml.saml2.core.impl.SubjectConfirmationBuilder;
 import org.opensaml.saml2.core.impl.SubjectConfirmationDataBuilder;*/
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.signature.Exponent;
import org.opensaml.xml.signature.Modulus;
import org.opensaml.xml.signature.RSAKeyValue;
/*import org.opensaml.xml.signature.impl.ExponentBuilder;
 import org.opensaml.xml.signature.impl.KeyInfoBuilder;
 import org.opensaml.xml.signature.impl.KeyValueBuilder;
 import org.opensaml.xml.signature.impl.ModulusBuilder;
 import org.opensaml.xml.signature.impl.RSAKeyValueBuilder;*/
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Evidence;
import org.opensaml.saml2.core.SubjectLocality;

import org.w3c.dom.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.security.auth.x500.X500Principal;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class implements the CallbackHandler which is invoked upon sending a
 * message requiring the SAML Assertion Token. It accesses the information
 * stored in NMProperties in order to build up the required token elements.
 */
public class OpenSAMLCallbackHandler implements CallbackHandler {

	private static Log log = LogFactory.getLog(OpenSAMLCallbackHandler.class);
	// Valid Authorization Decision values
	private static final String AUTHZ_DECISION_PERMIT = "Permit";
	private static final String AUTHZ_DECISION_DENY = "Deny";
	private static final String AUTHZ_DECISION_INDETERMINATE = "Indeterminate";
	private static final String[] VALID_AUTHZ_DECISION_ARRAY = {
			AUTHZ_DECISION_PERMIT, AUTHZ_DECISION_DENY,
			AUTHZ_DECISION_INDETERMINATE };
	private static final List<String> VALID_AUTHZ_DECISION_LIST = Collections
			.unmodifiableList(Arrays.asList(VALID_AUTHZ_DECISION_ARRAY));
	// Authorization Decision Action is always set to Execute
	private static final String AUTHZ_DECISION_ACTION_EXECUTE = "Execute";
	private static final String AUTHZ_DECISION_ACTION_NS = "urn:oasis:names:tc:SAML:1.0:action:rwedc";
	// Valid Name Identification values
	private static final String UNSPECIFIED_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified";
	private static final String EMAIL_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress";
	private static final String X509_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";
	private static final String WINDOWS_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:WindowsDomainQualifiedName";
	private static final String KERBEROS_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:kerberos";
	private static final String ENTITY_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:entity";
	private static final String PERSISTENT_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:persistent";
	private static final String TRANSIENT_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:transient";
	private static final String[] VALID_NAME_ID_ARRAY = { UNSPECIFIED_NAME_ID,
			EMAIL_NAME_ID, X509_NAME_ID, WINDOWS_NAME_ID, KERBEROS_NAME_ID,
			ENTITY_NAME_ID, PERSISTENT_NAME_ID, TRANSIENT_NAME_ID };
	private static final List<String> VALID_NAME_LIST = Collections
			.unmodifiableList(Arrays.asList(VALID_NAME_ID_ARRAY));
	// Valid Context Class references
	private static final String INTERNET_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:InternetProtocol";
	private static final String INTERNET_PASSWORD_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:InternetProtocolPassword";
	private static final String PASSWORD_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:Password";
	private static final String PASSWORD_TRANS_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport";
	private static final String KERBEROS_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:Kerberos";
	private static final String PREVIOUS_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:PreviousSession";
	private static final String REMOTE_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:SecureRemotePassword";
	private static final String TLS_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:TLSClient";
	private static final String X509_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:X509";
	private static final String PGP_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:PGP";
	private static final String SPKI_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:SPKI";
	private static final String DIG_SIGN_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:XMLDSig";
	private static final String UNSPECIFIED_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified";
	private static final String[] VALID_AUTHN_CNTX_CLS_ARRAY = {
			INTERNET_AUTHN_CNTX_CLS, INTERNET_PASSWORD_AUTHN_CNTX_CLS,
			PASSWORD_AUTHN_CNTX_CLS, PASSWORD_TRANS_AUTHN_CNTX_CLS,
			KERBEROS_AUTHN_CNTX_CLS, PREVIOUS_AUTHN_CNTX_CLS,
			REMOTE_AUTHN_CNTX_CLS, TLS_AUTHN_CNTX_CLS, X509_AUTHN_CNTX_CLS,
			PGP_AUTHN_CNTX_CLS, SPKI_AUTHN_CNTX_CLS, DIG_SIGN_AUTHN_CNTX_CLS,
			UNSPECIFIED_AUTHN_CNTX_CLS };
	private static final List<String> VALID_AUTHN_CNTX_CLS_LIST = Collections
			.unmodifiableList(Arrays.asList(VALID_AUTHN_CNTX_CLS_ARRAY));
	private static final String AUTHN_SESSION_INDEX = "123456";
	public static final String HOK_CONFIRM = "urn:oasis:names:tc:SAML:2.0:cm:holder-of-key";
	public static final String SV_CONFIRM = "urn:oasis:names:tc:SAML:2.0:cm:authorization-over-ssl";
	private static final String NHIN_NS = "http://www.hhs.gov/healthit/nhin";
	private static final String HL7_NS = "urn:hl7-org:v3";
	private static final int DEFAULT_NAME = 0;
	private static final int PRIMARY_NAME = 1;
	private HashMap<Object, Object> tokenVals = new HashMap<Object, Object>();
	private KeyStore keyStore;
	private KeyStore trustStore;
	private static Element svAssertion;
	private static Element hokAssertion20;
	private static final String ID_PREFIX = "_";

	private static final String PURPOSE_FOR_USE_DEPRECATED_ENABLED = "purposeForUseEnabled";
	private static final String DEFAULT_ISSUER_VALUE = "CN=SAML User,OU=SU,O=SAML User,L=Los Angeles,ST=CA,C=US";

	static {
		// WORKAROUND NEEDED IN METRO1.4. TO BE REMOVED LATER.
		javax.net.ssl.HttpsURLConnection
				.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {

					public boolean verify(String hostname,
							javax.net.ssl.SSLSession sslSession) {
						return true;
					}
				});
	}

	/**
	 * Constructs the callback handler and initializes the keystore and
	 * truststore references to the security certificates
	 */
	public OpenSAMLCallbackHandler() {
		log.debug("SamlCallbackHandler Constructor -- Begin");
		try {
			initKeyStore();
			initTrustStore();
		} catch (IOException e) {
			log.error("SamlCallbackHandler Exception: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		log.debug("SamlCallbackHandler Constructor -- Begin");
	}

	private XMLObject createOpenSAMLObject(QName qname) {
		return Configuration.getBuilderFactory().getBuilder(qname)
				.buildObject(qname);
	}

	/**
	 * This is the invoked implementation to handle the SAML Token creation upon
	 * notification of an outgoing message needing SAML. Based on the type of
	 * confirmation method detected on the Callbace it creates either a
	 * "Sender Vouches: or a "Holder-ok_Key" variant of the SAML Assertion.
	 * 
	 * @param callbacks
	 *            The SAML Callback
	 * @throws java.io.IOException
	 * @throws javax.security.auth.callback.UnsupportedCallbackException
	 */
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		log.debug(" **********************************  Handle SAML Callback Begin  **************************");
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof SAMLCallback) {
				SAMLCallback samlCallback = (SAMLCallback) callbacks[i];
				log.debug("=============== Print Runtime properties =============");
				tokenVals.putAll(samlCallback.getRuntimeProperties());
				log.debug(tokenVals);
				log.debug("=============== Completed Print properties =============");
				if (samlCallback.getConfirmationMethod().equals(
						SAMLCallback.HOK_ASSERTION_TYPE)) {
					samlCallback
							.setAssertionElement(createHOKSAMLAssertion20());
					hokAssertion20 = samlCallback.getAssertionElement();
				} else if (samlCallback.getConfirmationMethod().equals(
						SAMLCallback.SV_ASSERTION_TYPE)) {
		//			samlCallback.setAssertionElement(createSVSAMLAssertion20());
					svAssertion = samlCallback.getAssertionElement();
				} else {
					log.error("Unknown SAML Assertion Type: "
							+ samlCallback.getConfirmationMethod());
					throw new UnsupportedCallbackException(null,
							"SAML Assertion Type is not matched:"
									+ samlCallback.getConfirmationMethod());
				}
			} else {
				log.error("Unknown Callback encountered: " + callbacks[i]);
				throw new UnsupportedCallbackException(null,
						"Unsupported Callback Type Encountered");
			}
		}
		log.debug("**********************************  Handle SAML Callback End  **************************");
	}

//	/**
//	 * Currently not Used. Creates the "Sender Vouches" variant of the SAML
//	 * Assertion token.
//	 * 
//	 * @return The Assertion element
//	 */
//	private Element createSVSAMLAssertion20() {
//		log.debug("SamlCallbackHandler.createSVSAMLAssertion20() -- Begin");
//		Assertion assertion = null;
//		try {
//			SAMLAssertionFactory factory = SAMLAssertionFactory
//					.newInstance(SAMLAssertionFactory.SAML2_0);
//
//			// create the assertion id
//			// Per GATEWAY-847 the id attribute should not be allowed to start
//			// with a number (UUIDs can). Direction
//			// given from 2011 specification set was to prepend with and
//			// underscore.
//			String aID = ID_PREFIX.concat(String.valueOf(UUID.randomUUID()));
//			log.debug("Assertion ID: " + aID);
//
//			// name id of the issuer - For now just use default
//			NameID issueId = create509NameID(factory, DEFAULT_NAME);
//
//			// issue instant
//			GregorianCalendar issueInstant = calendarFactory();
//
//			// name id of the subject - user name
//			String uname = "defUser";
//			if (tokenVals.containsKey(SamlConstants.USER_NAME_PROP)
//					&& tokenVals.get(SamlConstants.USER_NAME_PROP) != null) {
//				uname = tokenVals.get(SamlConstants.USER_NAME_PROP).toString();
//			}
//			NameID nmId = factory.createNameID(uname, null, X509_NAME_ID);
//			Subject subj = factory.createSubject(nmId, null);
//
//			// authentication statement
//			List statements = createAuthnStatements(factory);
//
//			assertion = factory.createAssertion(aID, issueId, issueInstant,
//					null, null, subj, statements);
//
//			assertion.setVersion("2.0");
//
//			log.debug("createSVSAMLAssertion20 end ()");
//			return assertion.toElement(null);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}

	/**
	 * Creates the "Holder-of-Key" variant of the SAML Assertion token.
	 * 
	 * @return The Assertion element
	 */
	private Element createHOKSAMLAssertion20() {
		log.debug("SamlCallbackHandler.createHOKSAMLAssertion20() -- Begin");
		org.opensaml.saml2.core.Assertion ass = null;
		try {
			ass = (org.opensaml.saml2.core.Assertion) createOpenSAMLObject(org.opensaml.saml2.core.Assertion.DEFAULT_ELEMENT_NAME);

			// create the assertion id
			// Per GATEWAY-847 the id attribute should not be allowed to start
			// with a number (UUIDs can). Direction
			// given from 2011 specification set was to prepend with and
			// underscore.
			String aID = ID_PREFIX.concat(String.valueOf(UUID.randomUUID()));
			log.debug("Assertion ID: " + aID);

			// set assertion Id
			ass.setID(aID);

			// issue instant set to now.
			DateTime issueInstant = new DateTime();
			ass.setIssueInstant(issueInstant);

			// set issuer
			ass.setIssuer(createIssuer());

			// set subject
			ass.setSubject(createSubject());

			// add attribute statements
			ass.getStatements().addAll(createAttributeStatements());

			// TODO: need to sign the message
		} catch (Exception ex) {
			log.error("Unable to create HOK Assertion: " + ex.getMessage());
			ex.printStackTrace();
		}
		log.debug("SamlCallbackHandler.createHOKSAMLAssertion20() -- End");
		return null;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Statement> createAttributeStatements() {
		List<Statement> statements = new ArrayList<Statement>();

		statements.addAll(createAuthenicationStatements());
		statements.addAll(createUserNameAttributeStatements());
		statements.addAll(createOrganizationAttributeStatements());
		statements.addAll(createHomeCommunityIdAttributeStatements());
		statements.addAll(createPatientIdAttributeStatements());
		statements.addAll(createUserRoleStatements());
		statements.addAll(createPurposeOfUseStatements());
		statements.addAll(createAuthenicationDecsionStatements());
	
		return statements;
	}

	/**
	 * @return
	 */
	private List<AuthnStatement> createAuthenicationStatements() {

		List<AuthnStatement> authnStatements = new ArrayList<AuthnStatement>();

		String cntxCls = getNullSafeString(tokenVals,
				SamlConstants.AUTHN_CONTEXT_CLASS_PROP, X509_AUTHN_CNTX_CLS);

		cntxCls = validate(cntxCls, VALID_AUTHN_CNTX_CLS_LIST,
				UNSPECIFIED_AUTHN_CNTX_CLS);

		String sessionIndex = getNullSafeString(tokenVals,
				SamlConstants.AUTHN_SESSION_INDEX_PROP, AUTHN_SESSION_INDEX);
		log.debug("Setting Authentication session index to: " + sessionIndex);

		DateTime authInstant = getNullSafeDateTime(tokenVals,
				SamlConstants.AUTHN_INSTANT_PROP, new DateTime());

		String inetAddr = getNullSafeString(tokenVals,
				SamlConstants.SUBJECT_LOCALITY_ADDR_PROP);
		String dnsName = getNullSafeString(tokenVals,
				SamlConstants.SUBJECT_LOCALITY_DNS_PROP);

		AuthnStatement authnStatement = OpenSAML2ComponentBuilder.getInstance()
				.createAuthenicationStatements(cntxCls, sessionIndex,
						authInstant, inetAddr, dnsName);

		authnStatements.add(authnStatement);

		return authnStatements;
	}

	private String getNullSafeString(Map map, final String property) {

		return getNullSafeString(map, property, null);
	}

	private String getNullSafeString(Map map, final String property,
			String defaultValue) {
		String value = defaultValue;
		if (map.containsKey(property) && map.get(property) != null) {
			value = map.get(property).toString();
		}
		return value;
	}

	private List getNullSafeList(Map map, final String property) {
		List list = null;
		if (map.containsKey(property) && map.get(property) != null) {
			Object value = map.get(property);
			if (value instanceof List<?>) {
				list = (List) value;
			} else {
				list = new ArrayList();
				list.add(value);
			}
		}

		return list;
	}

	private String validate(final String value, List<String> validValues,
			final String defaultValue) {
		String validValue = defaultValue;
		if (validValues.contains(value.trim())) {
			validValue = value;
		} else {
			log.debug(value + " is not recognized as valid, "
					+ "creating evidence assertion version as: " + defaultValue);
			log.debug("Should be one of: " + validValues);
		}
		return validValue;
	}

	private DateTime getNullSafeDateTime(Map map, final String property,
			DateTime defaultValue) {
		String dateTimeTxt = getNullSafeString(map, property);

		DateTime dateTime = defaultValue;
		if (dateTimeTxt != null) {
			dateTime = XML_DATE_TIME_FORMAT.parseDateTime(dateTimeTxt);
		}
		return dateTime;

	}

	private static final DateTimeFormatter XML_DATE_TIME_FORMAT = ISODateTimeFormat
			.dateTimeNoMillis();

	/**
	 * @return
	 */
	private List<AuthzDecisionStatement> createAuthenicationDecsionStatements() {
		List<AuthzDecisionStatement> authDecisionStatements = new ArrayList<AuthzDecisionStatement>();

		Boolean hasAuthzStmt = getNullSafeBoolean(tokenVals,
				SamlConstants.AUTHZ_STATEMENT_EXISTS_PROP, Boolean.FALSE);
		// The authorization Decision Statement is optional
		if (hasAuthzStmt) {
			// Create resource for Authentication Decision Statement
			String resource = getNullSafeString(tokenVals,
					SamlConstants.RESOURCE_PROP);

			// Options are Permit, Deny and Indeterminate
			String decision = getNullSafeString(tokenVals,
					SamlConstants.AUTHZ_DECISION_PROP, AUTHZ_DECISION_PERMIT);

			decision = validate(decision, VALID_AUTHZ_DECISION_LIST,
					AUTHZ_DECISION_PERMIT);

			// As of Authorization Framework Spec 2.2 Action is a hard-coded
			// value
			// Therefore the value of the ACTION_PROP is no longer used
			String action = AUTHZ_DECISION_ACTION_EXECUTE;

			Evidence evidence = createEvidence();

			authDecisionStatements.add(OpenSAML2ComponentBuilder.getInstance()
					.createAuthzDecisionStatement(resource, decision, action,
							evidence));
		}

		return authDecisionStatements;
	}

	private Boolean getNullSafeBoolean(Map map, final String property,
			Boolean defaultValue) {
		Boolean value = defaultValue;
		if (map.containsKey(property) && map.get(property) != null) {
			value = Boolean.valueOf(map.get(property).toString());
		}
		return value;
	}

	/**
	 * @return
	 */
	private org.opensaml.saml2.core.Subject createSubject() {
		org.opensaml.saml2.core.Subject subject = (org.opensaml.saml2.core.Subject) createOpenSAMLObject(org.opensaml.saml2.core.Subject.DEFAULT_ELEMENT_NAME);
		String x509Name = "UID="
				+ tokenVals.get(SamlConstants.USER_NAME_PROP).toString();
		subject.setNameID(createNameID(X509_NAME_ID, x509Name));
		subject.getSubjectConfirmations().add(createHoKConfirmation());
		return subject;
	}

	private org.opensaml.saml2.core.SubjectConfirmation createHoKConfirmation() {
		org.opensaml.saml2.core.SubjectConfirmation subjectConfirmation = (org.opensaml.saml2.core.SubjectConfirmation) createOpenSAMLObject(org.opensaml.saml2.core.SubjectConfirmation.DEFAULT_ELEMENT_NAME);
		subjectConfirmation
				.setMethod(org.opensaml.saml2.core.SubjectConfirmation.METHOD_HOLDER_OF_KEY);
		subjectConfirmation
				.setSubjectConfirmationData(createSubjectConfirmationData());

		return subjectConfirmation;
	}

	private org.opensaml.saml2.core.SubjectConfirmationData createSubjectConfirmationData() {
		org.opensaml.saml2.core.SubjectConfirmationData subjectConfirmationData = (org.opensaml.saml2.core.SubjectConfirmationData) createOpenSAMLObject(org.opensaml.saml2.core.SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
		subjectConfirmationData.getUnknownAttributes().put(
				new QName("http://www.w3.org/2001/XMLSchema-instance", "type",
						"xsi"), "saml:KeyInfoConfirmationDataType");

		org.opensaml.xml.signature.KeyInfo ki = (org.opensaml.xml.signature.KeyInfo) createOpenSAMLObject(org.opensaml.xml.signature.KeyInfo.DEFAULT_ELEMENT_NAME);
		org.opensaml.xml.signature.KeyValue kv = (org.opensaml.xml.signature.KeyValue) createOpenSAMLObject(org.opensaml.xml.signature.KeyValue.DEFAULT_ELEMENT_NAME);

		RSAKeyValue _RSAKeyValue = (RSAKeyValue) createOpenSAMLObject(RSAKeyValue.DEFAULT_ELEMENT_NAME);
		Exponent arg0 = (Exponent) createOpenSAMLObject(Exponent.DEFAULT_ELEMENT_NAME);
		Modulus arg1 = (Modulus) createOpenSAMLObject(Modulus.DEFAULT_ELEMENT_NAME);

		RSAPublicKey RSAPk = (RSAPublicKey) getPublicKey();

		arg0.setValue(RSAPk.getPublicExponent().toString());
		_RSAKeyValue.setExponent(arg0);
		arg1.setValue(RSAPk.getModulus().toString());
		_RSAKeyValue.setModulus(arg1);
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
			FileInputStream fis = new FileInputStream(
					"c:/opt/keystores/clientKeystore.jks");
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

		X509Certificate certificate = (X509Certificate) pkEntry
				.getCertificate();
		return certificate.getPublicKey();
	}

	/**
	 * @return
	 */
	private org.opensaml.saml2.core.NameID createNameID(String format,
			String value) {
		org.opensaml.saml2.core.NameID nameId = (org.opensaml.saml2.core.NameID) createOpenSAMLObject(org.opensaml.saml2.core.NameID.DEFAULT_ELEMENT_NAME);

		nameId.setFormat(format);
		nameId.setValue(value);

		return nameId;
	}

	public org.opensaml.saml2.core.Issuer createIssuer() {
		Issuer issuer = null;

		if (tokenVals.containsKey(SamlConstants.ASSERTION_ISSUER_FORMAT_PROP)
				&& tokenVals.containsKey(SamlConstants.ASSERTION_ISSUER_PROP)) {

			String format = tokenVals.get(
					SamlConstants.ASSERTION_ISSUER_FORMAT_PROP).toString();
			log.debug("Setting Assertion Issuer format to: " + format);
			String sIssuer = tokenVals.get(SamlConstants.ASSERTION_ISSUER_PROP)
					.toString();
			log.debug("Setting Assertion Issuer to: " + sIssuer);

			if (VALID_NAME_LIST.contains(format.trim())) {
				issuer = (Issuer) createOpenSAMLObject(Issuer.DEFAULT_ELEMENT_NAME);
				issuer.setFormat(format);
				issuer.setValue(sIssuer);
			} else {
				log.debug("Not in valid listing of formats: Using default issuer");
				issuer = createDefaultIssuer();
			}
		} else {
			log.debug("Assertion issuer not defined: Using default issuer");
			issuer = createDefaultIssuer();
		}
		return issuer;
	}

	protected org.opensaml.saml2.core.Issuer createDefaultIssuer() {
		Issuer issuer = (Issuer) createOpenSAMLObject(Issuer.DEFAULT_ELEMENT_NAME);
		issuer.setFormat(X509_NAME_ID);
		issuer.setValue(DEFAULT_ISSUER_VALUE);
		return issuer;
	}

	/**
	 * Both the Issuer and the Subject elements have a NameID element which is
	 * formed through this method. Currently default data is used to specify the
	 * required Issuer information. However, the Subject information is defined
	 * based on the stored value of the userid. If this is a legal X509
	 * structute the NameId is constructed in that format, if not it is
	 * constructed as an "Unspecified" format.
	 * 
	 * @param factory
	 *            The factory object used to assist in the construction of the
	 *            SAML Assertion token
	 * @param assId
	 *            Identifies this as default usage case or one with declared
	 *            value.
	 * @return The constructed NameID element
	 * @throws com.sun.xml.wss.saml.SAMLException
	 */
	private NameID create509NameID(SAMLAssertionFactory factory, int assId)
			throws SAMLException {
		log.debug("SamlCallbackHandler.create509NameID() -- Begin: " + assId);
		NameID nmId = null;
		String defCN = "SAML User";
		String defOU = "SU";
		String defO = "SAML User";
		String defL = "Los Angeles";
		String defST = "CA";
		String defC = "US";

		String identifier;
		if (assId != PRIMARY_NAME
				|| !tokenVals.containsKey(SamlConstants.USER_NAME_PROP)
				|| tokenVals.get(SamlConstants.USER_NAME_PROP) == null) {
			identifier = "CN=" + defCN + "," + "OU=" + defOU + "," + "O="
					+ defO + "," + "L=" + defL + "," + "ST=" + defST + ","
					+ "C=" + defC;
			nmId = factory.createNameID(identifier, null, X509_NAME_ID);
			log.debug("Create default X509 name: " + identifier);
		} else {
			String x509Name = "UID="
					+ tokenVals.get(SamlConstants.USER_NAME_PROP).toString();
			try {
				X500Principal prin = new X500Principal(x509Name);
				nmId = factory.createNameID(x509Name, null, X509_NAME_ID);
				log.debug("Create X509 name: " + x509Name);
			} catch (IllegalArgumentException iae) {
				/* Could also test if email form if we wanted to support that */
				log.warn("Set format as Unspecified. Invalid X509 format: "
						+ tokenVals.get(SamlConstants.USER_NAME_PROP) + " "
						+ iae.getMessage());
				nmId = factory.createNameID(
						tokenVals.get(SamlConstants.USER_NAME_PROP).toString(),
						null, UNSPECIFIED_NAME_ID);
			}
		}

		log.debug("SamlCallbackHandler.create509NameID() -- End: "
				+ nmId.getValue());
		return nmId;
	}

	/*
	 * public boolean isValidEmailAddress(String address) {
	 * log.debug("SamlCallbackHandler.isValidEmailAddress() " + address +
	 * " -- Begin"); boolean retBool = false; if (address != null &&
	 * address.length() > 0) { try { InternetAddress emailAddr = new
	 * InternetAddress(address, true); String[] tokens = address.split("@"); if
	 * (tokens.length == 2 && tokens[0].trim().length() > 0 &&
	 * tokens[1].trim().length() > 0) { retBool = true; } else {
	 * log.debug("Address does not follow the form 'local-part@domain'"); } }
	 * catch (AddressException ex) { // address does not comply with RFC822
	 * log.debug("Address is not of the RFC822 format"); } }
	 * log.debug("SamlCallbackHandler.isValidEmailAddress() " + retBool +
	 * " -- End"); return retBool; }
	 */
	
	/**
	 * Creates the Attribute statements for UserName, UserOrganization,
	 * UserRole, and PurposeOfUse
	 * 
	 * @param factory
	 *            The factory object used to assist in the construction of the
	 *            SAML Assertion token
	 * @return The listing of all Attribute statements
	 */
	private List<AttributeStatement> createUserNameAttributeStatements() {

		List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
		List<Attribute> attributes = new ArrayList<Attribute>();

		// Set the User Name Attribute
		List<String> userNameValues = new ArrayList<String>();
		String nameConstruct = getUserName();

		if (nameConstruct.length() > 0) {
			log.debug("UserName: " + nameConstruct);

			userNameValues.add(nameConstruct);

			attributes.add(OpenSAML2ComponentBuilder.getInstance()
					.createAttribute(null, SamlConstants.USERNAME_ATTR, null,
							userNameValues));
		} else {
			log.warn("No information provided to fill in user name attribute");
		}
		if (!attributes.isEmpty()) {
			statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
		}
		
	return statements;
	}
	
	 /** Creates the Attribute statements UserRole
	 * 
	 * @param factory
	 *            The factory object used to assist in the construction of the
	 *            SAML Assertion token
	 * @return The listing of all Attribute statements
	 */
	private List<AttributeStatement> createUserRoleStatements() {
		List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
		List<Attribute> attributes = new ArrayList<Attribute>();
		
		// Set the User Role Attribute
				List userRoleAttributeValues = new ArrayList();
				Map<String, String> userRoleAttributes = new HashMap<String, String>();

				conditionallyAddValue(userRoleAttributes, tokenVals,
						SamlConstants.USER_CODE_PROP, SamlConstants.CE_CODE_ID);
				conditionallyAddValue(userRoleAttributes, tokenVals,
						SamlConstants.USER_SYST_PROP, SamlConstants.CE_CODESYS_ID);
				conditionallyAddValue(userRoleAttributes, tokenVals,
						SamlConstants.USER_SYST_NAME_PROP,
						SamlConstants.CE_CODESYSNAME_ID);
				conditionallyAddValue(userRoleAttributes, tokenVals,
						SamlConstants.USER_DISPLAY_PROP,
						SamlConstants.CE_DISPLAYNAME_ID);

				userRoleAttributeValues.add(OpenSAML2ComponentBuilder
						.getInstance()
						.createAttributeValue(HL7_NS, "Role", "hl7", userRoleAttributes));

				attributes.add(OpenSAML2ComponentBuilder.getInstance().createAttribute(
						null, SamlConstants.USER_ROLE_ATTR, null, userRoleAttributeValues));
		
		
		if (!attributes.isEmpty()) {
			statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
		}
		
	return statements;
	
	
	}
	
	
	 /** Creates the Attribute statements PurposeOfUse
		 * 
		 * @param factory
		 *            The factory object used to assist in the construction of the
		 *            SAML Assertion token
		 * @return The listing of all Attribute statements
		 */
		private List<AttributeStatement> createPurposeOfUseStatements() {
			List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
			List<Attribute> attributes = new ArrayList<Attribute>();
			
			/*
			 * Gateway-347 - Support for both values will remain until NHIN
			 * Specs updated Determine whether to use PurposeOfUse or
			 * PuposeForUse
			 */
			String purposeAttributeValueName = "hl7:PurposeOfUse";
			if (isPurposeForUseEnabled()) {
				purposeAttributeValueName = "hl7:PurposeForUse";
			}

			// Add the Purpose Of/For Use Attribute Value
			
			List purposeOfUserValues = new ArrayList();
			Map<String, String> purposeOfUseAttributes = new HashMap<String, String>();

			conditionallyAddValue(purposeOfUseAttributes, tokenVals,
					SamlConstants.PURPOSE_CODE_PROP, SamlConstants.CE_CODE_ID);
		
			conditionallyAddValue(purposeOfUseAttributes, tokenVals,
					SamlConstants.PURPOSE_SYST_PROP, SamlConstants.CE_CODESYS_ID);
	
			conditionallyAddValue(purposeOfUseAttributes, tokenVals,
					SamlConstants.PURPOSE_SYST_NAME_PROP, SamlConstants.CE_CODESYSNAME_ID);

			conditionallyAddValue(purposeOfUseAttributes, tokenVals,
					SamlConstants.PURPOSE_DISPLAY_PROP,SamlConstants.CE_DISPLAYNAME_ID);


			purposeOfUserValues.add(OpenSAML2ComponentBuilder
					.getInstance()
					.createAttributeValue(HL7_NS, purposeAttributeValueName, "hl7", purposeOfUseAttributes));
			
			attributes.add(OpenSAML2ComponentBuilder.getInstance().createAttribute(
					null, SamlConstants.PURPOSE_ROLE_ATTR, null, purposeOfUserValues));
			
			
			
			if (!attributes.isEmpty()) {
				statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
			}
			
		return statements;
		
		
		}


	/**
	 * Creates the Attribute statements for UserName, UserOrganization,
	 * UserRole, and PurposeOfUse
	 * 
	 * @param factory
	 *            The factory object used to assist in the construction of the
	 *            SAML Assertion token
	 * @return The listing of all Attribute statements
	 */
	private List<AttributeStatement> createOrganizationAttributeStatements() {

		log.debug("SamlCallbackHandler.addAssertStatements() -- Begin");
		List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
		List<Attribute> attributes = new ArrayList<Attribute>();

		
		// Set the User Organization ID Attribute
		conditionallyAddAttribute(attributes, tokenVals,
				SamlConstants.USER_ORG_PROP, null, SamlConstants.USER_ORG_ATTR,
				null);

		if (!attributes.isEmpty()) {
				statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
			}
			
		

		log.debug("SamlCallbackHandler.addAssertStatements() -- End");
		return statements;

	}
	
	/**
	 * Creates the Attribute statements for UserName, UserOrganization,
	 * UserRole, and PurposeOfUse
	 * 
	 * @param factory
	 *            The factory object used to assist in the construction of the
	 *            SAML Assertion token
	 * @return The listing of all Attribute statements
	 */
	private List<AttributeStatement> createHomeCommunityIdAttributeStatements() {

		log.debug("SamlCallbackHandler.addAssertStatements() -- Begin");
		List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
		List<Attribute> attributes = new ArrayList<Attribute>();

	
		// Set the Home Community ID Attribute
		conditionallyAddAttribute(attributes, tokenVals,
				SamlConstants.HOME_COM_PROP, null,
				SamlConstants.HOME_COM_ID_ATTR, null);
		
	
			if (!attributes.isEmpty()) {
				statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
			}
			
		

		log.debug("SamlCallbackHandler.addAssertStatements() -- End");
		return statements;

	}
	/**
	 * Creates the Attribute statements for UserName, UserOrganization,
	 * UserRole, and PurposeOfUse
	 * 
	 * @param factory
	 *            The factory object used to assist in the construction of the
	 *            SAML Assertion token
	 * @return The listing of all Attribute statements
	 */
	private List<AttributeStatement> createPatientIdAttributeStatements() {

		log.debug("SamlCallbackHandler.addAssertStatements() -- Begin");
		List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
		List<Attribute> attributes = new ArrayList<Attribute>();

		
			
		
		// Set the Patient ID Attribute
		conditionallyAddAttribute(attributes, tokenVals,
				SamlConstants.PATIENT_ID_PROP, null,
				SamlConstants.PATIENT_ID_ATTR, null);
			
			
			if (!attributes.isEmpty()) {
				statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
			}
			
		

		log.debug("SamlCallbackHandler.addAssertStatements() -- End");
		return statements;

	}

	protected void conditionallyAddAttribute(List<Attribute> attributes,
			Map map, final String property, final String friendlyName,
			final String attributeName, final String format) {

		Attribute attribute = null;
		List<String> attributeValues = getNullSafeList(map, property);

		if (attributeValues != null) {

			attribute = OpenSAML2ComponentBuilder
					.getInstance()
					.createAttribute(null, attributeName, null, attributeValues);
			attributes.add(attribute);
		} else {
			log.warn("No information provided to fill in "
					+ SamlConstants.USER_ORG_ATTR);
		}

	}

	protected void conditionallyAddValue(Map valueMap, Map map,
			final String property, final String attributeName) {
		String value = getNullSafeString(map, property);
		if (value != null) {
			valueMap.put(attributeName, value);
		} else {
			log.warn("No information provided to fill in user role "
					+ attributeName);
		}

	}

	protected String getUserName() {
		StringBuffer nameConstruct = new StringBuffer();

		String firstName = getNullSafeString(tokenVals,
				SamlConstants.USER_FIRST_PROP);
		if (firstName != null) {
			nameConstruct.append(firstName);
		}

		String middleName = getNullSafeString(tokenVals,
				SamlConstants.USER_MIDDLE_PROP);
		if (middleName != null) {
			if (nameConstruct.length() > 0) {
				nameConstruct.append(" ");
			}
			nameConstruct.append(middleName);
		}

		String lastName = getNullSafeString(tokenVals,
				SamlConstants.USER_LAST_PROP);
		if (lastName != null) {
			if (nameConstruct.length() > 0) {
				nameConstruct.append(" ");
			}
			nameConstruct.append(lastName);
		}
		return nameConstruct.toString();
	}

	/**
	 * Returns boolean condition on whether PurposeForUse is enabled
	 * 
	 * @return The PurposeForUse enabled setting
	 */
	private boolean isPurposeForUseEnabled() {
		boolean match = false;
		try {
			// Use CONNECT utility class to access gateway.properties
			String purposeForUseEnabled = PropertyAccessor.getProperty(
					NhincConstants.GATEWAY_PROPERTY_FILE,
					PURPOSE_FOR_USE_DEPRECATED_ENABLED);
			if (purposeForUseEnabled != null
					&& purposeForUseEnabled.equalsIgnoreCase("true")) {
				match = true;
			}
		} catch (PropertyAccessException ex) {
			log.error("Error: Failed to retrieve "
					+ PURPOSE_FOR_USE_DEPRECATED_ENABLED
					+ " from property file: "
					+ NhincConstants.GATEWAY_PROPERTY_FILE);
			log.error(ex.getMessage());
		}
		return match;
	}

	/**
	 * Creates the Evidence element that encompasses the Assertion defining the
	 * authorization form needed in cases where evidence of authorization to
	 * access the medical records must be provided along with the message
	 * request
	 * 
	 * @param factory
	 *            The factory object used to assist in the construction of the
	 *            SAML Assertion token
	 * @param issueInstant
	 *            The calendar representing the time of Assertion issuance
	 * @return The Evidence element
	 */
	private Evidence createEvidence() {
		log.debug("SamlCallbackHandler.createEvidence() -- Begin");

		List<Assertion> evidenceAssertions = new ArrayList<Assertion>();
		String evAssertionID = getNullSafeString(tokenVals,
				SamlConstants.EVIDENCE_ID_PROP,
				String.valueOf(UUID.randomUUID()));

		DateTime issueInstant = getNullSafeDateTime(tokenVals,
				SamlConstants.EVIDENCE_INSTANT_PROP, new DateTime());

		String format = getNullSafeString(tokenVals,
				SamlConstants.EVIDENCE_ISSUER_FORMAT_PROP);
		format = validate(format, VALID_NAME_LIST, X509_NAME_ID);

		String issuer = getNullSafeString(tokenVals,
				SamlConstants.EVIDENCE_ISSUER_PROP);

		org.opensaml.saml2.core.NameID evIssuerId = OpenSAML2ComponentBuilder
				.getInstance().createNameID(null, format, issuer);

		DateTime beginValidTime = getNullSafeDateTime(tokenVals,
				SamlConstants.EVIDENCE_CONDITION_NOT_BEFORE_PROP,
				new DateTime());

		DateTime endValidTime = getNullSafeDateTime(tokenVals,
				SamlConstants.EVIDENCE_CONDITION_NOT_AFTER_PROP,
				new DateTime().plusMinutes(5));

		if (beginValidTime.isAfter(endValidTime)) {
			// set beginning time to now
			beginValidTime = new DateTime();
			log.warn("The beginning time for the valid evidence should be before the ending time.  "
					+ "Setting the beginning time to the current system time.");
		}

		Conditions conditions = OpenSAML2ComponentBuilder.getInstance()
				.createConditions(beginValidTime, endValidTime, null);

		List<AttributeStatement> statements = createEvidenceStatements();

		Assertion evidenceAssertion = OpenSAML2ComponentBuilder.getInstance()
				.createAssertion(evAssertionID);

		evidenceAssertion.getAttributeStatements().addAll(statements);
		evidenceAssertion.setConditions(conditions);
		evidenceAssertion.setIssueInstant(issueInstant);
		evidenceAssertion.setIssuer((Issuer) evIssuerId);

		evidenceAssertions.add(evidenceAssertion);

		Evidence evidence = OpenSAML2ComponentBuilder.getInstance()
				.createEvidence(evidenceAssertions);

		log.debug("SamlCallbackHandler.createEvidence() -- End");
		return evidence;
	}

	/**
	 * Creates the Attribute Statements needed for the Evidence element. These
	 * include the Attributes for the Access Consent Policy and the Instance
	 * Access Consent Policy
	 * 
	 * @param factory
	 *            The factory object used to assist in the construction of the
	 *            SAML Assertion token
	 * @return The listing of the attribute statements for the Evidence element
	 */
	private List<AttributeStatement> createEvidenceStatements() {
		log.debug("SamlCallbackHandler.createEvidenceStatements() -- Begin");
		List<AttributeStatement> statements = new ArrayList<AttributeStatement>();

		List accessConstentValues = getNullSafeList(tokenVals,
				SamlConstants.EVIDENCE_ACCESS_CONSENT_PROP);
		if (accessConstentValues == null) {
			log.debug("No Access Consent found for Evidence");
		}

		// Set the Instance Access Consent
		List evidenceInstanceAccessConsentValues = getNullSafeList(tokenVals,
				SamlConstants.EVIDENCE_INST_ACCESS_CONSENT_PROP);
		if (evidenceInstanceAccessConsentValues == null) {
			log.debug("No Instance Access Consent found for Evidence");
		}

		statements = OpenSAML2ComponentBuilder.getInstance()
				.createEvidenceStatements(accessConstentValues,
						evidenceInstanceAccessConsentValues, NHIN_NS);

		log.debug("SamlCallbackHandler.createEvidenceStatements() -- End");
		return statements;
	}

	/**
	 * Creates a calendar object representing the time given.
	 * 
	 * @param time
	 *            following the UTC format as specified by the XML Schema type
	 *            (dateTime) or for backward compatibility following the simple
	 *            date form MM/dd/yyyy HH:mm:ss
	 * @return The calendar object representing the given time
	 */
	private GregorianCalendar createCal(String time) {

		GregorianCalendar cal = calendarFactory();
		try {
			// times must be in UTC format as specified by the XML Schema type
			// (dateTime)
			DatatypeFactory xmlDateFactory = DatatypeFactory.newInstance();
			XMLGregorianCalendar xmlDate = xmlDateFactory
					.newXMLGregorianCalendar(time.trim());
			cal = xmlDate.toGregorianCalendar();
		} catch (IllegalArgumentException iaex) {
			try {
				// try simple date format - backward compatibility
				SimpleDateFormat dateForm = new SimpleDateFormat(
						"MM/dd/yyyy HH:mm:ss");
				cal.setTime(dateForm.parse(time));
			} catch (ParseException ex) {
				log.error("Date form is expected to be in dateTime format yyyy-MM-ddTHH:mm:ss.SSSZ Setting default date");
			}
		} catch (DatatypeConfigurationException dtex) {
			log.error("Problem in creating XML Date Factory. Setting default date");
		}

		log.info("Created calendar instance: " + (cal.get(Calendar.MONTH) + 1)
				+ "/" + cal.get(Calendar.DAY_OF_MONTH) + "/"
				+ cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY)
				+ ":" + cal.get(Calendar.MINUTE) + ":"
				+ cal.get(Calendar.SECOND));
		return cal;
	}

	/**
	 * Initializes the keystore access using the system properties defined in
	 * the domain.xml javax.net.ssl.keyStore and javax.net.ssl.keyStorePassword
	 * 
	 * @throws java.io.IOException
	 */
	private void initKeyStore() throws IOException {
		log.debug("SamlCallbackHandler.initKeyStore() -- Begin");

		InputStream is = null;
		String storeType = System.getProperty("javax.net.ssl.keyStoreType");
		String password = System.getProperty("javax.net.ssl.keyStorePassword");
		String storeLoc = System.getProperty("javax.net.ssl.keyStore");

		if (storeType == null) {
			log.error("javax.net.ssl.keyStoreType is not defined in domain.xml");
			log.warn("Default to JKS keyStoreType");
			storeType = "JKS";
		}
		if (password != null) {
			if ("JKS".equals(storeType) && storeLoc == null) {
				log.error("javax.net.ssl.keyStore is not defined in domain.xml");
			} else {
				try {
					keyStore = KeyStore.getInstance(storeType);
					if ("JKS".equals(storeType)) {
						is = new FileInputStream(storeLoc);
					}
					keyStore.load(is, password.toCharArray());
				} catch (NoSuchAlgorithmException ex) {
					log.error("Error initializing KeyStore: " + ex);
					throw new IOException(ex.getMessage());
				} catch (CertificateException ex) {
					log.error("Error initializing KeyStore: " + ex);
					throw new IOException(ex.getMessage());
				} catch (KeyStoreException ex) {
					log.error("Error initializing KeyStore: " + ex);
					throw new IOException(ex.getMessage());
				} finally {
					try {
						if (is != null) {
							is.close();
						}
					} catch (IOException ex) {
						log.debug("KeyStoreCallbackHandler " + ex);
					}
				}
			}
		} else {
			log.error("javax.net.ssl.keyStorePassword is not defined in domain.xml");
		}
		log.debug("SamlCallbackHandler.initKeyStore() -- End");
	}

	/**
	 * Initializes the truststore access using the system properties defined in
	 * the domain.xml javax.net.ssl.trustStore and
	 * javax.net.ssl.trustStorePassword
	 * 
	 * @throws java.io.IOException
	 */
	private void initTrustStore() throws IOException {
		log.debug("SamlCallbackHandler.initTrustStore() -- Begin");

		InputStream is = null;
		String storeType = System.getProperty("javax.net.ssl.trustStoreType");
		String password = System
				.getProperty("javax.net.ssl.trustStorePassword");
		String storeLoc = System.getProperty("javax.net.ssl.trustStore");

		if (storeType == null) {
			log.error("javax.net.ssl.trustStoreType is not defined in domain.xml");
			log.warn("Default to JKS trustStoreType");
			storeType = "JKS";
		}
		if (password != null) {
			if ("JKS".equals(storeType) && storeLoc == null) {
				log.error("javax.net.ssl.trustStore is not defined in domain.xml");
			} else {
				try {
					trustStore = KeyStore.getInstance(storeType);
					if ("JKS".equals(storeType)) {
						is = new FileInputStream(storeLoc);
					}
					trustStore.load(is, password.toCharArray());
				} catch (NoSuchAlgorithmException ex) {
					log.error("Error initializing TrustStore: " + ex);
					throw new IOException(ex.getMessage());
				} catch (CertificateException ex) {
					log.error("Error initializing TrustStore: " + ex);
					throw new IOException(ex.getMessage());
				} catch (KeyStoreException ex) {
					log.error("Error initializing TrustStore: " + ex);
					throw new IOException(ex.getMessage());
				} finally {
					try {
						if (is != null) {
							is.close();
						}
					} catch (IOException ex) {
						log.debug("KeyStoreCallbackHandler " + ex);
					}
				}
			}
		} else {
			log.error("javax.net.ssl.trustStorePassword is not defined in domain.xml");
		}
		log.debug("SamlCallbackHandler.initTrustStore() -- End");
	}

	/**
	 * Finds the X509 certificate in the keystore with the client alias as
	 * defined in the domain.xml system property CLIENT_KEY_ALIAS and
	 * establishes the private key on the SignatureKeyCallback request using
	 * this certificate.
	 * 
	 * @param request
	 *            The SignatureKeyCallback request object
	 * @throws java.io.IOException
	 */
	private void getDefaultPrivKeyCert(
			SignatureKeyCallback.DefaultPrivKeyCertRequest request)
			throws IOException {
		log.debug("SamlCallbackHandler.getDefaultPrivKeyCert() -- Begin");
		String uniqueAlias = null;
		String client_key_alias = System.getProperty("CLIENT_KEY_ALIAS");
		if (client_key_alias != null) {
			String password = System
					.getProperty("javax.net.ssl.keyStorePassword");
			if (password != null) {
				try {
					Enumeration aliases = keyStore.aliases();
					while (aliases.hasMoreElements()) {
						String currentAlias = (String) aliases.nextElement();
						if (currentAlias.equals(client_key_alias)) {
							if (keyStore.isKeyEntry(currentAlias)) {
								Certificate thisCertificate = keyStore
										.getCertificate(currentAlias);
								if (thisCertificate != null) {
									if (thisCertificate instanceof X509Certificate) {
										if (uniqueAlias == null) {
											uniqueAlias = currentAlias;
											break;
										}
									}
								}
							}
						}
					}
					if (uniqueAlias != null) {
						request.setX509Certificate((X509Certificate) keyStore
								.getCertificate(uniqueAlias));
						request.setPrivateKey((PrivateKey) keyStore.getKey(
								uniqueAlias, password.toCharArray()));
					} else {
						log.error("Client key alais can not be determined");
					}

				} catch (UnrecoverableKeyException ex) {
					log.error("Error initializing Private Key: " + ex);
					throw new IOException(ex.getMessage());
				} catch (NoSuchAlgorithmException ex) {
					log.error("Error initializing Private Key: " + ex);
					throw new IOException(ex.getMessage());
				} catch (KeyStoreException ex) {
					log.error("Error initializing Private Key: " + ex);
					throw new IOException(ex.getMessage());
				}

			} else {
				log.error("javax.net.ssl.keyStorePassword is not defined in domain.xml");
			}

		} else {
			log.error("CLIENT_KEY_ALIAS is not defined in domain.xml");
		}

		log.debug("SamlCallbackHandler.getDefaultPrivKeyCert() -- End");
	}

	/**
	 * Creates a calendar instance set to the current system time in GMT
	 * 
	 * @return The calendar instance
	 */
	private GregorianCalendar calendarFactory() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
		return calendar;
	}
}
