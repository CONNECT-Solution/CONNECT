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

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.x500.X500Principal;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Evidence;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Statement;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectLocality;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.w3c.dom.Element;

import com.sun.xml.wss.impl.callback.SAMLCallback;
import com.sun.xml.wss.impl.callback.SignatureKeyCallback;
import com.sun.xml.wss.saml.NameID;
import com.sun.xml.wss.saml.SAMLAssertionFactory;
import com.sun.xml.wss.saml.SAMLException;

/**
 * This class implements the CallbackHandler which is invoked upon sending a message requiring the SAML Assertion Token.
 * It accesses the information stored in NMProperties in order to build up the required token elements.
 */
public class OpenSAMLCallbackHandler implements CallbackHandler {

    private static Log log = LogFactory.getLog(OpenSAMLCallbackHandler.class);
    // Valid Authorization Decision values
    private static final String AUTHZ_DECISION_PERMIT = "Permit";
    private static final String AUTHZ_DECISION_DENY = "Deny";
    private static final String AUTHZ_DECISION_INDETERMINATE = "Indeterminate";
    private static final String[] VALID_AUTHZ_DECISION_ARRAY = { AUTHZ_DECISION_PERMIT, AUTHZ_DECISION_DENY,
            AUTHZ_DECISION_INDETERMINATE };
    private static final List<String> VALID_AUTHZ_DECISION_LIST = Collections.unmodifiableList(Arrays
            .asList(VALID_AUTHZ_DECISION_ARRAY));
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
    private static final String[] VALID_NAME_ID_ARRAY = { UNSPECIFIED_NAME_ID, EMAIL_NAME_ID, X509_NAME_ID,
            WINDOWS_NAME_ID, KERBEROS_NAME_ID, ENTITY_NAME_ID, PERSISTENT_NAME_ID, TRANSIENT_NAME_ID };
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
    private static final String[] VALID_AUTHN_CNTX_CLS_ARRAY = { INTERNET_AUTHN_CNTX_CLS,
            INTERNET_PASSWORD_AUTHN_CNTX_CLS, PASSWORD_AUTHN_CNTX_CLS, PASSWORD_TRANS_AUTHN_CNTX_CLS,
            KERBEROS_AUTHN_CNTX_CLS, PREVIOUS_AUTHN_CNTX_CLS, REMOTE_AUTHN_CNTX_CLS, TLS_AUTHN_CNTX_CLS,
            X509_AUTHN_CNTX_CLS, PGP_AUTHN_CNTX_CLS, SPKI_AUTHN_CNTX_CLS, DIG_SIGN_AUTHN_CNTX_CLS,
            UNSPECIFIED_AUTHN_CNTX_CLS };
    private static final List<String> VALID_AUTHN_CNTX_CLS_LIST = Collections.unmodifiableList(Arrays
            .asList(VALID_AUTHN_CNTX_CLS_ARRAY));
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

    private static final DateTimeFormatter XML_DATE_TIME_FORMAT = ISODateTimeFormat.dateTimeNoMillis();

    static {
        // WORKAROUND NEEDED IN METRO1.4. TO BE REMOVED LATER.
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {

            public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
                return true;
            }
        });
    }

    /**
     * Constructs the callback handler and initializes the keystore and truststore references to the security
     * certificates
     */
    public OpenSAMLCallbackHandler() {

    }

    /**
     * This is the invoked implementation to handle the SAML Token creation upon notification of an outgoing message
     * needing SAML. Based on the type of confirmation method detected on the Callbace it creates either a
     * "Sender Vouches: or a "Holder-ok_Key" variant of the SAML Assertion.
     * 
     * @param callbacks The SAML Callback
     * @throws java.io.IOException
     * @throws javax.security.auth.callback.UnsupportedCallbackException
     */
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        log.debug(" **********************************  Handle SAML Callback Begin  **************************");
        try {
            for (int i = 0; i < callbacks.length; i++) {
                if (callbacks[i] instanceof SAMLCallback) {
                    SAMLCallback samlCallback = (SAMLCallback) callbacks[i];
                    log.debug("=============== Print Runtime properties =============");
                    tokenVals.putAll(samlCallback.getRuntimeProperties());
                    log.debug(tokenVals);
                    log.debug("=============== Completed Print properties =============");
                    if (samlCallback.getConfirmationMethod().equals(SAMLCallback.HOK_ASSERTION_TYPE)) {
                        samlCallback.setAssertionElement(createHOKSAMLAssertion20());
                        hokAssertion20 = samlCallback.getAssertionElement();
                    } else if (samlCallback.getConfirmationMethod().equals(SAMLCallback.SV_ASSERTION_TYPE)) {
                        // samlCallback.setAssertionElement(createSVSAMLAssertion20());
                        svAssertion = samlCallback.getAssertionElement();
                    } else {
                        log.error("Unknown SAML Assertion Type: " + samlCallback.getConfirmationMethod());
                        throw new UnsupportedCallbackException(null, "SAML Assertion Type is not matched:"
                                + samlCallback.getConfirmationMethod());
                    }
                } else {
                    log.error("Unknown Callback encountered: " + callbacks[i]);
                    throw new UnsupportedCallbackException(null, "Unsupported Callback Type Encountered");
                }
            }
        } catch (Exception ex) {
            // catching all exceptions and making them IOExceptions. IO picked by flip of coin, neither IOException or
            // UnsupportedCallbackException are appropriate.
            throw new IOException(ex);
        }
        log.debug("**********************************  Handle SAML Callback End  **************************");
    }

    /**
     * Currently not Used. Creates the "Sender Vouches" variant of the SAML Assertion token.
     * 
     * @return The Assertion element
     */
    // private Element createSVSAMLAssertion20() {
    // log.debug("SamlCallbackHandler.createSVSAMLAssertion20() -- Begin");
    // Assertion assertion = null;
    // try {
    // SAMLAssertionFactory factory = SAMLAssertionFactory.newInstance(SAMLAssertionFactory.SAML2_0);
    //
    // // create the assertion id
    // // Per GATEWAY-847 the id attribute should not be allowed to start
    // // with a number (UUIDs can). Direction
    // // given from 2011 specification set was to prepend with and
    // // underscore.
    // String aID = ID_PREFIX.concat(String.valueOf(UUID.randomUUID()));
    // log.debug("Assertion ID: " + aID);
    //
    // // name id of the issuer - For now just use default
    // NameID issueId = create509NameID(factory, DEFAULT_NAME);
    //
    // // issue instant
    // GregorianCalendar issueInstant = calendarFactory();
    //
    // // name id of the subject - user name
    // String uname = "defUser";
    // if (tokenVals.containsKey(SamlConstants.USER_NAME_PROP)
    // && tokenVals.get(SamlConstants.USER_NAME_PROP) != null) {
    // uname = tokenVals.get(SamlConstants.USER_NAME_PROP).toString();
    // }
    // NameID nmId = factory.createNameID(uname, null, X509_NAME_ID);
    // Subject subj = factory.createSubject(nmId, null);
    //
    // // authentication statement
    // List statements = createAuthnStatements(factory);
    //
    // assertion = factory.createAssertion(aID, issueId, issueInstant, null, null, subj, statements);
    //
    // assertion.setVersion("2.0");
    //
    // log.debug("createSVSAMLAssertion20 end ()");
    // return assertion.toElement(null);
    // } catch (Exception e) {
    // e.printStackTrace();
    // throw new RuntimeException(e);
    // }
    // }

    /**
     * Creates the "Holder-of-Key" variant of the SAML Assertion token.
     * 
     * @return The Assertion element
     * @throws Exception
     */
    private Element createHOKSAMLAssertion20() throws Exception {
        log.debug("SamlCallbackHandler.createHOKSAMLAssertion20() -- Begin");
        org.opensaml.saml2.core.Assertion assertion = null;
        try {
            assertion = (org.opensaml.saml2.core.Assertion) OpenSAML2ComponentBuilder.getInstance().createAssertion();

            // create the assertion id
            // Per GATEWAY-847 the id attribute should not be allowed to start
            // with a number (UUIDs can). Direction
            // given from 2011 specification set was to prepend with and
            // underscore.
            String aID = ID_PREFIX.concat(String.valueOf(UUID.randomUUID()));
            log.debug("Assertion ID: " + aID);

            // set assertion Id
            assertion.setID(aID);

            // issue instant set to now.
            DateTime issueInstant = new DateTime();
            assertion.setIssueInstant(issueInstant);

            // set issuer
            assertion.setIssuer(createIssuer());

            // set subject
            assertion.setSubject(createSubject());

            // add attribute statements
            assertion.getStatements().addAll(createAttributeStatements());

            // TODO: need to sign the message
        } catch (Exception ex) {
            log.error("Unable to create HOK Assertion: " + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
        log.debug("SamlCallbackHandler.createHOKSAMLAssertion20() -- End");
        return null;
    }

    public Issuer createIssuer() {
        Issuer issuer = null;

        if (tokenVals.containsKey(SamlConstants.ASSERTION_ISSUER_FORMAT_PROP)
                && tokenVals.containsKey(SamlConstants.ASSERTION_ISSUER_PROP)) {

            String format = tokenVals.get(SamlConstants.ASSERTION_ISSUER_FORMAT_PROP).toString();
            log.debug("Setting Assertion Issuer format to: " + format);
            String sIssuer = tokenVals.get(SamlConstants.ASSERTION_ISSUER_PROP).toString();
            log.debug("Setting Assertion Issuer to: " + sIssuer);

            if (VALID_NAME_LIST.contains(format.trim())) {
                issuer = (Issuer) OpenSAML2ComponentBuilder.getInstance().createIssuer(format, sIssuer);
            } else {
                log.debug("Not in valid listing of formats: Using default issuer");
                issuer = OpenSAML2ComponentBuilder.getInstance().createDefaultIssuer();
            }
        } else {
            log.debug("Assertion issuer not defined: Using default issuer");
            issuer = OpenSAML2ComponentBuilder.getInstance().createDefaultIssuer();
        }
        return issuer;
    }

    /**
     * @return
     * @throws Exception
     */
    private Subject createSubject() throws Exception {
        org.opensaml.saml2.core.Subject subject = null;
        String x509Name = "UID=" + tokenVals.get(SamlConstants.USER_NAME_PROP).toString();
        OpenSAML2ComponentBuilder.getInstance().createSubject(x509Name);
        return subject;
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

        String cntxCls = getNullSafeString(tokenVals, SamlConstants.AUTHN_CONTEXT_CLASS_PROP, X509_AUTHN_CNTX_CLS);

        cntxCls = validate(cntxCls, VALID_AUTHN_CNTX_CLS_LIST, UNSPECIFIED_AUTHN_CNTX_CLS);

        String sessionIndex = getNullSafeString(tokenVals, SamlConstants.AUTHN_SESSION_INDEX_PROP, AUTHN_SESSION_INDEX);
        log.debug("Setting Authentication session index to: " + sessionIndex);

        DateTime authInstant = getNullSafeDateTime(tokenVals, SamlConstants.AUTHN_INSTANT_PROP, new DateTime());

        String inetAddr = getNullSafeString(tokenVals, SamlConstants.SUBJECT_LOCALITY_ADDR_PROP);
        String dnsName = getNullSafeString(tokenVals, SamlConstants.SUBJECT_LOCALITY_DNS_PROP);

        AuthnStatement authnStatement = OpenSAML2ComponentBuilder.getInstance().createAuthenicationStatements(cntxCls,
                sessionIndex, authInstant, inetAddr, dnsName);

        authnStatements.add(authnStatement);

        return authnStatements;
    }

    private String getNullSafeString(Map map, final String property) {

        return getNullSafeString(map, property, null);
    }

    private String getNullSafeString(Map map, final String property, String defaultValue) {
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

    private String validate(final String value, List<String> validValues, final String defaultValue) {
        String validValue = defaultValue;
        if (validValues.contains(value.trim())) {
            validValue = value;
        } else {
            log.debug(value + " is not recognized as valid, " + "creating evidence assertion version as: "
                    + defaultValue);
            log.debug("Should be one of: " + validValues);
        }
        return validValue;
    }

    private DateTime getNullSafeDateTime(Map map, final String property, DateTime defaultValue) {
        String dateTimeTxt = getNullSafeString(map, property);

        DateTime dateTime = defaultValue;
        if (dateTimeTxt != null) {
            dateTime = XML_DATE_TIME_FORMAT.parseDateTime(dateTimeTxt);
        }
        return dateTime;

    }

    /**
     * @return
     */
    private List<AuthzDecisionStatement> createAuthenicationDecsionStatements() {
        List<AuthzDecisionStatement> authDecisionStatements = new ArrayList<AuthzDecisionStatement>();

        Boolean hasAuthzStmt = getNullSafeBoolean(tokenVals, SamlConstants.AUTHZ_STATEMENT_EXISTS_PROP, Boolean.FALSE);
        // The authorization Decision Statement is optional
        if (hasAuthzStmt) {
            // Create resource for Authentication Decision Statement
            String resource = getNullSafeString(tokenVals, SamlConstants.RESOURCE_PROP);

            // Options are Permit, Deny and Indeterminate
            String decision = getNullSafeString(tokenVals, SamlConstants.AUTHZ_DECISION_PROP, AUTHZ_DECISION_PERMIT);

            decision = validate(decision, VALID_AUTHZ_DECISION_LIST, AUTHZ_DECISION_PERMIT);

            // As of Authorization Framework Spec 2.2 Action is a hard-coded
            // value
            // Therefore the value of the ACTION_PROP is no longer used
            String action = AUTHZ_DECISION_ACTION_EXECUTE;

            Evidence evidence = createEvidence();

            authDecisionStatements.add(OpenSAML2ComponentBuilder.getInstance().createAuthzDecisionStatement(resource,
                    decision, action, evidence));
        }

        return authDecisionStatements;
    }

    /**
     * Creates the Evidence element that encompasses the Assertion defining the authorization form needed in cases where
     * evidence of authorization to access the medical records must be provided along with the message request
     * 
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @param issueInstant The calendar representing the time of Assertion issuance
     * @return The Evidence element
     */
    private Evidence createEvidence() {
        log.debug("SamlCallbackHandler.createEvidence() -- Begin");

        List<Assertion> evidenceAssertions = new ArrayList<Assertion>();
        String evAssertionID = getNullSafeString(tokenVals, SamlConstants.EVIDENCE_ID_PROP,
                String.valueOf(UUID.randomUUID()));

        DateTime issueInstant = getNullSafeDateTime(tokenVals, SamlConstants.EVIDENCE_INSTANT_PROP, new DateTime());

        String format = getNullSafeString(tokenVals, SamlConstants.EVIDENCE_ISSUER_FORMAT_PROP);
        format = validate(format, VALID_NAME_LIST, X509_NAME_ID);

        String issuer = getNullSafeString(tokenVals, SamlConstants.EVIDENCE_ISSUER_PROP);

        org.opensaml.saml2.core.NameID evIssuerId = OpenSAML2ComponentBuilder.getInstance().createNameID(null, format,
                issuer);

        DateTime beginValidTime = getNullSafeDateTime(tokenVals, SamlConstants.EVIDENCE_CONDITION_NOT_BEFORE_PROP,
                new DateTime());

        DateTime endValidTime = getNullSafeDateTime(tokenVals, SamlConstants.EVIDENCE_CONDITION_NOT_AFTER_PROP,
                new DateTime().plusMinutes(5));

        if (beginValidTime.isAfter(endValidTime)) {
            // set beginning time to now
            beginValidTime = new DateTime();
            log.warn("The beginning time for the valid evidence should be before the ending time.  "
                    + "Setting the beginning time to the current system time.");
        }

        Conditions conditions = OpenSAML2ComponentBuilder.getInstance().createConditions(beginValidTime, endValidTime,
                null);

        List<AttributeStatement> statements = createEvidenceStatements();

        Assertion evidenceAssertion = OpenSAML2ComponentBuilder.getInstance().createAssertion(evAssertionID);

        evidenceAssertion.getAttributeStatements().addAll(statements);
        evidenceAssertion.setConditions(conditions);
        evidenceAssertion.setIssueInstant(issueInstant);
        evidenceAssertion.setIssuer((Issuer) evIssuerId);

        evidenceAssertions.add(evidenceAssertion);

        Evidence evidence = OpenSAML2ComponentBuilder.getInstance().createEvidence(evidenceAssertions);

        log.debug("SamlCallbackHandler.createEvidence() -- End");
        return evidence;
    }

    /**
     * Creates the Attribute Statements needed for the Evidence element. These include the Attributes for the Access
     * Consent Policy and the Instance Access Consent Policy
     * 
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of the attribute statements for the Evidence element
     */
    private List<AttributeStatement> createEvidenceStatements() {
        log.debug("SamlCallbackHandler.createEvidenceStatements() -- Begin");
        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();

        List accessConstentValues = getNullSafeList(tokenVals, SamlConstants.EVIDENCE_ACCESS_CONSENT_PROP);
        if (accessConstentValues == null) {
            log.debug("No Access Consent found for Evidence");
        }

        // Set the Instance Access Consent
        List evidenceInstanceAccessConsentValues = getNullSafeList(tokenVals,
                SamlConstants.EVIDENCE_INST_ACCESS_CONSENT_PROP);
        if (evidenceInstanceAccessConsentValues == null) {
            log.debug("No Instance Access Consent found for Evidence");
        }

        statements = OpenSAML2ComponentBuilder.getInstance().createEvidenceStatements(accessConstentValues,
                evidenceInstanceAccessConsentValues, NHIN_NS);

        log.debug("SamlCallbackHandler.createEvidenceStatements() -- End");
        return statements;
    }

    private Boolean getNullSafeBoolean(Map map, final String property, Boolean defaultValue) {
        Boolean value = defaultValue;
        if (map.containsKey(property) && map.get(property) != null) {
            value = Boolean.valueOf(map.get(property).toString());
        }
        return value;
    }

    /**
     * Creates the Attribute statements for UserName, UserOrganization, UserRole, and PurposeOfUse
     * 
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
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

            attributes.add(OpenSAML2ComponentBuilder.getInstance().createAttribute(null, SamlConstants.USERNAME_ATTR,
                    null, userNameValues));
        } else {
            log.warn("No information provided to fill in user name attribute");
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
    private List<AttributeStatement> createUserRoleStatements() {
        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
        List<Attribute> attributes = new ArrayList<Attribute>();

        // Set the User Role Attribute
        List userRoleAttributeValues = new ArrayList();
        Map<String, String> userRoleAttributes = new HashMap<String, String>();

        conditionallyAddValue(userRoleAttributes, tokenVals, SamlConstants.USER_CODE_PROP, SamlConstants.CE_CODE_ID);
        conditionallyAddValue(userRoleAttributes, tokenVals, SamlConstants.USER_SYST_PROP, SamlConstants.CE_CODESYS_ID);
        conditionallyAddValue(userRoleAttributes, tokenVals, SamlConstants.USER_SYST_NAME_PROP,
                SamlConstants.CE_CODESYSNAME_ID);
        conditionallyAddValue(userRoleAttributes, tokenVals, SamlConstants.USER_DISPLAY_PROP,
                SamlConstants.CE_DISPLAYNAME_ID);

        userRoleAttributeValues.add(OpenSAML2ComponentBuilder.getInstance().createAttributeValue(HL7_NS, "Role", "hl7",
                userRoleAttributes));

        attributes.add(OpenSAML2ComponentBuilder.getInstance().createAttribute(null, SamlConstants.USER_ROLE_ATTR,
                null, userRoleAttributeValues));

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
    private List<AttributeStatement> createPurposeOfUseStatements() {
        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
        List<Attribute> attributes = new ArrayList<Attribute>();

        /*
         * Gateway-347 - Support for both values will remain until NHIN Specs updated Determine whether to use
         * PurposeOfUse or PuposeForUse
         */
        String purposeAttributeValueName = "hl7:PurposeOfUse";
        if (isPurposeForUseEnabled()) {
            purposeAttributeValueName = "hl7:PurposeForUse";
        }

        // Add the Purpose Of/For Use Attribute Value

        List purposeOfUserValues = new ArrayList();
        Map<String, String> purposeOfUseAttributes = new HashMap<String, String>();

        conditionallyAddValue(purposeOfUseAttributes, tokenVals, SamlConstants.PURPOSE_CODE_PROP,
                SamlConstants.CE_CODE_ID);

        conditionallyAddValue(purposeOfUseAttributes, tokenVals, SamlConstants.PURPOSE_SYST_PROP,
                SamlConstants.CE_CODESYS_ID);

        conditionallyAddValue(purposeOfUseAttributes, tokenVals, SamlConstants.PURPOSE_SYST_NAME_PROP,
                SamlConstants.CE_CODESYSNAME_ID);

        conditionallyAddValue(purposeOfUseAttributes, tokenVals, SamlConstants.PURPOSE_DISPLAY_PROP,
                SamlConstants.CE_DISPLAYNAME_ID);

        purposeOfUserValues.add(OpenSAML2ComponentBuilder.getInstance().createAttributeValue(HL7_NS,
                purposeAttributeValueName, "hl7", purposeOfUseAttributes));

        attributes.add(OpenSAML2ComponentBuilder.getInstance().createAttribute(null, SamlConstants.PURPOSE_ROLE_ATTR,
                null, purposeOfUserValues));

        if (!attributes.isEmpty()) {
            statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
        }

        return statements;

    }

    /**
     * Creates the Attribute statements for UserName, UserOrganization, UserRole, and PurposeOfUse
     * 
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    private List<AttributeStatement> createOrganizationAttributeStatements() {

        log.debug("SamlCallbackHandler.addAssertStatements() -- Begin");
        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
        List<Attribute> attributes = new ArrayList<Attribute>();

        // Set the User Organization ID Attribute
        conditionallyAddAttribute(attributes, tokenVals, SamlConstants.USER_ORG_PROP, null,
                SamlConstants.USER_ORG_ATTR, null);

        if (!attributes.isEmpty()) {
            statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
        }

        log.debug("SamlCallbackHandler.addAssertStatements() -- End");
        return statements;

    }

    /**
     * Creates the Attribute statements for UserName, UserOrganization, UserRole, and PurposeOfUse
     * 
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    private List<AttributeStatement> createHomeCommunityIdAttributeStatements() {

        log.debug("SamlCallbackHandler.addAssertStatements() -- Begin");
        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
        List<Attribute> attributes = new ArrayList<Attribute>();

        // Set the Home Community ID Attribute
        conditionallyAddAttribute(attributes, tokenVals, SamlConstants.HOME_COM_PROP, null,
                SamlConstants.HOME_COM_ID_ATTR, null);

        if (!attributes.isEmpty()) {
            statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
        }

        log.debug("SamlCallbackHandler.addAssertStatements() -- End");
        return statements;

    }

    /**
     * Creates the Attribute statements for UserName, UserOrganization, UserRole, and PurposeOfUse
     * 
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @return The listing of all Attribute statements
     */
    private List<AttributeStatement> createPatientIdAttributeStatements() {

        log.debug("SamlCallbackHandler.addAssertStatements() -- Begin");
        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
        List<Attribute> attributes = new ArrayList<Attribute>();

        // Set the Patient ID Attribute
        conditionallyAddAttribute(attributes, tokenVals, SamlConstants.PATIENT_ID_PROP, null,
                SamlConstants.PATIENT_ID_ATTR, null);

        if (!attributes.isEmpty()) {
            statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
        }

        log.debug("SamlCallbackHandler.addAssertStatements() -- End");
        return statements;

    }

    protected void conditionallyAddAttribute(List<Attribute> attributes, Map map, final String property,
            final String friendlyName, final String attributeName, final String format) {

        Attribute attribute = null;
        List<String> attributeValues = getNullSafeList(map, property);

        if (attributeValues != null) {

            attribute = OpenSAML2ComponentBuilder.getInstance().createAttribute(null, attributeName, null,
                    attributeValues);
            attributes.add(attribute);
        } else {
            log.warn("No information provided to fill in " + SamlConstants.USER_ORG_ATTR);
        }

    }

    protected void conditionallyAddValue(Map valueMap, Map map, final String property, final String attributeName) {
        String value = getNullSafeString(map, property);
        if (value != null) {
            valueMap.put(attributeName, value);
        } else {
            log.warn("No information provided to fill in user role " + attributeName);
        }

    }

    protected String getUserName() {
        StringBuffer nameConstruct = new StringBuffer();

        String firstName = getNullSafeString(tokenVals, SamlConstants.USER_FIRST_PROP);
        if (firstName != null) {
            nameConstruct.append(firstName);
        }

        String middleName = getNullSafeString(tokenVals, SamlConstants.USER_MIDDLE_PROP);
        if (middleName != null) {
            if (nameConstruct.length() > 0) {
                nameConstruct.append(" ");
            }
            nameConstruct.append(middleName);
        }

        String lastName = getNullSafeString(tokenVals, SamlConstants.USER_LAST_PROP);
        if (lastName != null) {
            if (nameConstruct.length() > 0) {
                nameConstruct.append(" ");
            }
            nameConstruct.append(lastName);
        }
        return nameConstruct.toString();
    }

    /**
     * Both the Issuer and the Subject elements have a NameID element which is formed through this method. Currently
     * default data is used to specify the required Issuer information. However, the Subject information is defined
     * based on the stored value of the userid. If this is a legal X509 structute the NameId is constructed in that
     * format, if not it is constructed as an "Unspecified" format.
     * 
     * @param factory The factory object used to assist in the construction of the SAML Assertion token
     * @param assId Identifies this as default usage case or one with declared value.
     * @return The constructed NameID element
     * @throws com.sun.xml.wss.saml.SAMLException derp derp
     */
    private NameID create509NameID(SAMLAssertionFactory factory, int assId) throws SAMLException {
        log.debug("SamlCallbackHandler.create509NameID() -- Begin: " + assId);
        NameID nmId = null;
        String defCN = "SAML User";
        String defOU = "SU";
        String defO = "SAML User";
        String defL = "Los Angeles";
        String defST = "CA";
        String defC = "US";

        String identifier;
        if (assId != PRIMARY_NAME || !tokenVals.containsKey(SamlConstants.USER_NAME_PROP)
                || tokenVals.get(SamlConstants.USER_NAME_PROP) == null) {
            identifier = "CN=" + defCN + "," + "OU=" + defOU + "," + "O=" + defO + "," + "L=" + defL + "," + "ST="
                    + defST + "," + "C=" + defC;
            nmId = factory.createNameID(identifier, null, X509_NAME_ID);
            log.debug("Create default X509 name: " + identifier);
        } else {
            String x509Name = "UID=" + tokenVals.get(SamlConstants.USER_NAME_PROP).toString();
            try {
                X500Principal prin = new X500Principal(x509Name);
                nmId = factory.createNameID(x509Name, null, X509_NAME_ID);
                log.debug("Create X509 name: " + x509Name);
            } catch (IllegalArgumentException iae) {
                /* Could also test if email form if we wanted to support that */
                log.warn("Set format as Unspecified. Invalid X509 format: "
                        + tokenVals.get(SamlConstants.USER_NAME_PROP) + " " + iae.getMessage());
                nmId = factory.createNameID(tokenVals.get(SamlConstants.USER_NAME_PROP).toString(), null,
                        UNSPECIFIED_NAME_ID);
            }
        }

        log.debug("SamlCallbackHandler.create509NameID() -- End: " + nmId.getValue());
        return nmId;
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
            String purposeForUseEnabled = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    PURPOSE_FOR_USE_DEPRECATED_ENABLED);
            if (purposeForUseEnabled != null && purposeForUseEnabled.equalsIgnoreCase("true")) {
                match = true;
            }
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + PURPOSE_FOR_USE_DEPRECATED_ENABLED + " from property file: "
                    + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return match;
    }

    /**
     * Creates a calendar object representing the time given.
     * 
     * @param time following the UTC format as specified by the XML Schema type (dateTime) or for backward compatibility
     *            following the simple date form MM/dd/yyyy HH:mm:ss
     * @return The calendar object representing the given time
     */
    private GregorianCalendar createCal(String time) {

        GregorianCalendar cal = calendarFactory();
        try {
            // times must be in UTC format as specified by the XML Schema type
            // (dateTime)
            DatatypeFactory xmlDateFactory = DatatypeFactory.newInstance();
            XMLGregorianCalendar xmlDate = xmlDateFactory.newXMLGregorianCalendar(time.trim());
            cal = xmlDate.toGregorianCalendar();
        } catch (IllegalArgumentException iaex) {
            try {
                // try simple date format - backward compatibility
                SimpleDateFormat dateForm = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                cal.setTime(dateForm.parse(time));
            } catch (ParseException ex) {
                log.error("Date form is expected to be in dateTime format yyyy-MM-ddTHH:mm:ss.SSSZ Setting default date");
            }
        } catch (DatatypeConfigurationException dtex) {
            log.error("Problem in creating XML Date Factory. Setting default date");
        }

        log.info("Created calendar instance: " + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH)
                + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE)
                + ":" + cal.get(Calendar.SECOND));
        return cal;
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
