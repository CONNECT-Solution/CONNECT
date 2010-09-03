/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.callback;

import com.sun.org.apache.xml.internal.security.keys.KeyInfo;
import com.sun.xml.wss.XWSSecurityException;
import java.io.*;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.cert.Certificate;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import com.sun.xml.wss.impl.callback.*;
import com.sun.xml.wss.saml.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
 * message requiring the SAML Assertion Token.  It accesses the information
 * stored in NMProperties in order to build up the required token elements.
 */
public class SamlCallbackHandler implements CallbackHandler {

    private static Log log = LogFactory.getLog(SamlCallbackHandler.class);
    // Valid Evidence Assertion versions
    private static final String ASSERTION_VERSION_2_0 = "2.0";
    private static final String[] VALID_ASSERTION_VERSION_ARRAY = {ASSERTION_VERSION_2_0};
    private static final List<String> VALID_ASSERTION_VERSION_LIST = Collections.unmodifiableList(Arrays.asList(VALID_ASSERTION_VERSION_ARRAY));
    // Valid Authorization Decision values
    private static final String AUTHZ_DECISION_PERMIT = "Permit";
    private static final String AUTHZ_DECISION_DENY = "Deny";
    private static final String AUTHZ_DECISION_INDETERMINATE = "Indeterminate";
    private static final String[] VALID_AUTHZ_DECISION_ARRAY = {AUTHZ_DECISION_PERMIT,
        AUTHZ_DECISION_DENY, AUTHZ_DECISION_INDETERMINATE};
    private static final List<String> VALID_AUTHZ_DECISION_LIST = Collections.unmodifiableList(Arrays.asList(VALID_AUTHZ_DECISION_ARRAY));
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
    private static final String[] VALID_NAME_ID_ARRAY = {UNSPECIFIED_NAME_ID,
        EMAIL_NAME_ID, X509_NAME_ID, WINDOWS_NAME_ID, KERBEROS_NAME_ID,
        ENTITY_NAME_ID, PERSISTENT_NAME_ID, TRANSIENT_NAME_ID};
    private static final List<String> VALID_NAME_LIST = Collections.unmodifiableList(Arrays.asList(VALID_NAME_ID_ARRAY));
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
    private static final String[] VALID_AUTHN_CNTX_CLS_ARRAY = {INTERNET_AUTHN_CNTX_CLS,
        INTERNET_PASSWORD_AUTHN_CNTX_CLS, PASSWORD_AUTHN_CNTX_CLS, PASSWORD_TRANS_AUTHN_CNTX_CLS,
        KERBEROS_AUTHN_CNTX_CLS, PREVIOUS_AUTHN_CNTX_CLS, REMOTE_AUTHN_CNTX_CLS, TLS_AUTHN_CNTX_CLS,
        X509_AUTHN_CNTX_CLS, PGP_AUTHN_CNTX_CLS, SPKI_AUTHN_CNTX_CLS, DIG_SIGN_AUTHN_CNTX_CLS,
        UNSPECIFIED_AUTHN_CNTX_CLS};
    private static final List<String> VALID_AUTHN_CNTX_CLS_LIST = Collections.unmodifiableList(Arrays.asList(VALID_AUTHN_CNTX_CLS_ARRAY));
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
    private static HashMap<String, String> factoryVersionMap = new HashMap<String, String>();

    static {
        //WORKAROUND NEEDED IN METRO1.4. TO BE REMOVED LATER.
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier() {

                    public boolean verify(String hostname,
                            javax.net.ssl.SSLSession sslSession) {
                        return true;
                    }
                });
        // Set up versioning mapping - currently only support 2.0
        factoryVersionMap.put(ASSERTION_VERSION_2_0, SAMLAssertionFactory.SAML2_0);
    }

    /**
     * Constructs the callback handler and initializes the keystore and
     * truststore references to the security certificates
     */
    public SamlCallbackHandler() {
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

    /**
     * This is the invoked implementation to handle the SAML Token creation upon
     * notification of an outgoing message needing SAML.  Based on the type of
     * confirmation method detected on the Callbace it creates either a
     * "Sender Vouches: or a "Holder-ok_Key" variant of the SAML Assertion.
     * @param callbacks The SAML Callback
     * @throws java.io.IOException
     * @throws javax.security.auth.callback.UnsupportedCallbackException
     */
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        log.debug(" **********************************  Handle SAML Callback Begin  **************************");
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
                    samlCallback.setAssertionElement(createSVSAMLAssertion20());
                    svAssertion = samlCallback.getAssertionElement();
                } else {
                    log.error("Unknown SAML Assertion Type: " + samlCallback.getConfirmationMethod());
                    throw new UnsupportedCallbackException(null, "SAML Assertion Type is not matched:" + samlCallback.getConfirmationMethod());
                }
            } else {
                log.error("Unknown Callback encountered: " + callbacks[i]);
                throw new UnsupportedCallbackException(null, "Unsupported Callback Type Encountered");
            }
        }
        log.debug("**********************************  Handle SAML Callback End  **************************");
    }

    /**
     * Currently not Used.
     * Creates the "Sender Vouches" variant of the SAML Assertion token.
     * @return The Assertion element
     */
    private Element createSVSAMLAssertion20() {
        log.debug("SamlCallbackHandler.createSVSAMLAssertion20() -- Begin");
        Assertion assertion = null;
        try {
            SAMLAssertionFactory factory = SAMLAssertionFactory.newInstance(SAMLAssertionFactory.SAML2_0);

            // create the assertion id
            String aID = String.valueOf(UUID.randomUUID());

            // name id of the issuer - For now just use default
            NameID issueId = create509NameID(factory, DEFAULT_NAME);

            // issue instant
            GregorianCalendar issueInstant = calendarFactory();

            // name id of the subject - user name
            String uname = "defUser";
            if (tokenVals.containsKey(SamlConstants.USER_NAME_PROP) &&
                    tokenVals.get(SamlConstants.USER_NAME_PROP) != null) {
                uname = tokenVals.get(SamlConstants.USER_NAME_PROP).toString();
            }
            NameID nmId = factory.createNameID(uname, null, X509_NAME_ID);
            Subject subj = factory.createSubject(nmId, null);

            // authentication statement
            List statements = createAuthnStatements(factory);

            assertion = factory.createAssertion(aID, issueId, issueInstant,
                    null, null, subj, statements);

            assertion.setVersion("2.0");

            log.debug("createSVSAMLAssertion20 end ()");
            return assertion.toElement(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the "Holder-of-Key" variant of the SAML Assertion token.
     * @return The Assertion element
     */
    private Element createHOKSAMLAssertion20() {
        log.debug("SamlCallbackHandler.createHOKSAMLAssertion20() -- Begin");
        Assertion assertion = null;
        try {
            SAMLAssertionFactory factory = SAMLAssertionFactory.newInstance(SAMLAssertionFactory.SAML2_0);

            // create the assertion id
            String aID = String.valueOf(UUID.randomUUID());

            // name id of the issuer - For now just use default
            NameID issueId = null;

            if (tokenVals.containsKey(SamlConstants.EVIDENCE_ISSUER_FORMAT_PROP) &&
                    tokenVals.containsKey(SamlConstants.EVIDENCE_ISSUER_PROP)) {
               String format = tokenVals.get(SamlConstants.EVIDENCE_ISSUER_FORMAT_PROP).toString();
               if (VALID_NAME_LIST.contains(format.trim())) {
                   log.debug("Setting Assertion Issuer format to: " + format);
                   String issuer = tokenVals.get(SamlConstants.EVIDENCE_ISSUER_PROP).toString();
                   log.debug("Setting Assertion Issuer to: " + issuer);
                   issueId = factory.createNameID(issuer, null, format);
               }
               else {
                   issueId = create509NameID(factory, DEFAULT_NAME);
               }
            } else {
                issueId = create509NameID(factory, DEFAULT_NAME);
            }

            // subject information
            NameID subjId = create509NameID(factory, PRIMARY_NAME);

            // default private key cert request
            SignatureKeyCallback.DefaultPrivKeyCertRequest request = new SignatureKeyCallback.DefaultPrivKeyCertRequest();
            getDefaultPrivKeyCert(request);
            if (request.getX509Certificate() == null) {
                throw new RuntimeException("Not able to resolve the Default Certificate");
            }
            PublicKey pubKey = request.getX509Certificate().getPublicKey();
            PrivateKey privKey = request.getPrivateKey();

            // subject confirmation
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            Document doc = docFactory.newDocumentBuilder().newDocument();
            KeyInfo keyInfo = new KeyInfo(doc);
            keyInfo.addKeyValue(pubKey);
            SubjectConfirmationData scd = factory.createSubjectConfirmationData(null, null, null, null, null, keyInfo.getElement());
            SubjectConfirmation scf = factory.createSubjectConfirmation(null, scd, HOK_CONFIRM);
            Subject subj = factory.createSubject(subjId, scf);

            // authentication statement
            List statements = createAuthnStatements(factory);

            // issue instant set to now.
            GregorianCalendar issueInstant = calendarFactory();
            assertion = factory.createAssertion(aID, issueId, issueInstant, null, null, subj, statements);
            assertion.setVersion("2.0");
            log.debug("SamlCallbackHandler.createHOKSAMLAssertion20() -- End");
            return assertion.sign(pubKey, privKey);
        } catch (ParserConfigurationException ex) {
            log.error("Unable to create HOK Assertion: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            log.error("Unable to create HOK Assertion: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (SAMLException ex) {
            log.error("Unable to create HOK Assertion: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (XWSSecurityException ex) {
            log.error("Unable to create HOK Assertion: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /**
     * Both the Issuer and the Subject elements have a NameID element which is
     * formed through this method.  Currently default data is used to specify
     * the required Issuer information.  However, the Subject information is
     * defined based on the stored value of the userid.  If this is a legal X509
     * structute the NameId is constructed in that format, if not it is
     * constructed as an "Unspecified" format.
     * @param factory The factory object used to assist in the construction of
     * the SAML Assertion token
     * @param assId Identifies this as default usage case or one with declared
     * value.
     * @return The constructed NameID element
     * @throws com.sun.xml.wss.saml.SAMLException
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
        if (assId != PRIMARY_NAME ||
                !tokenVals.containsKey(SamlConstants.USER_NAME_PROP) ||
                tokenVals.get(SamlConstants.USER_NAME_PROP) == null) {
            identifier = "CN=" + defCN + "," + "OU=" + defOU + "," +
                    "O=" + defO + "," + "L=" + defL + "," +
                    "ST=" + defST + "," + "C=" + defC;
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
                log.warn("Set format as Unspecified. Invalid X509 format: " +
                        tokenVals.get(SamlConstants.USER_NAME_PROP) + " " + iae.getMessage());
                nmId = factory.createNameID(tokenVals.get(SamlConstants.USER_NAME_PROP).toString(), null, UNSPECIFIED_NAME_ID);
            }
        }

        log.debug("SamlCallbackHandler.create509NameID() -- End: " + nmId.getValue());
        return nmId;
    }

    /*public boolean isValidEmailAddress(String address) {
    log.debug("SamlCallbackHandler.isValidEmailAddress() " + address + " -- Begin");
    boolean retBool = false;
    if (address != null && address.length() > 0) {
    try {
    InternetAddress emailAddr = new InternetAddress(address, true);
    String[] tokens = address.split("@");
    if (tokens.length == 2 && tokens[0].trim().length() > 0 && tokens[1].trim().length() > 0) {
    retBool = true;
    } else {
    log.debug("Address does not follow the form 'local-part@domain'");
    }
    } catch (AddressException ex) {
    // address does not comply with RFC822
    log.debug("Address is not of the RFC822 format");
    }
    }
    log.debug("SamlCallbackHandler.isValidEmailAddress() " + retBool + " -- End");
    return retBool;
    }*/
    /**
     * Creates the authentication statement, the attribute statements, and the
     * authorization decision statements for placement in the SAML Assertion.
     * @param factory The factory object used to assist in the construction of
     * the SAML Assertion token
     * @param issueInstant The calendar representing the time of Assertion issuance
     * @return A listing of all statements
     * @throws com.sun.xml.wss.saml.SAMLException
     */
    private List createAuthnStatements(SAMLAssertionFactory factory) throws SAMLException, XWSSecurityException {
        log.debug("SamlCallbackHandler.createAuthnStatements() -- Begin");
        List statements = new ArrayList();

        // Create Subject Locality
        SubjectLocality subjLoc = null;
        if (tokenVals.containsKey(SamlConstants.SUBJECT_LOCALITY_ADDR_PROP) &&
                tokenVals.get(SamlConstants.SUBJECT_LOCALITY_ADDR_PROP) != null &&
                tokenVals.containsKey(SamlConstants.SUBJECT_LOCALITY_DNS_PROP) &&
                tokenVals.get(SamlConstants.SUBJECT_LOCALITY_DNS_PROP) != null) {
            String inetAddr = tokenVals.get(SamlConstants.SUBJECT_LOCALITY_ADDR_PROP).toString();
            String dnsName = tokenVals.get(SamlConstants.SUBJECT_LOCALITY_DNS_PROP).toString();
            log.debug("Create Subject Locality as " + inetAddr + " in domain: " + dnsName);
            subjLoc = factory.createSubjectLocality(inetAddr, dnsName);
        } else {
            //default to empty locality
            log.debug("Create default Subject Locality");
            subjLoc = factory.createSubjectLocality();
        }

        AuthnContext authnContext = null;
        if (tokenVals.containsKey(SamlConstants.AUTHN_CONTEXT_CLASS_PROP) &&
                tokenVals.get(SamlConstants.AUTHN_CONTEXT_CLASS_PROP) != null) {
            String cntxCls = tokenVals.get(SamlConstants.AUTHN_CONTEXT_CLASS_PROP).toString();
            if (VALID_AUTHN_CNTX_CLS_LIST.contains(cntxCls.trim())) {
                log.debug("Create Authentication Context Class as: " + cntxCls);
                authnContext = factory.createAuthnContext(cntxCls, null);
            } else {
                log.debug(cntxCls + " is not recognized as valid, " +
                        "create default Authentication Context Class as: " + UNSPECIFIED_AUTHN_CNTX_CLS);
                log.debug("Should be one of: " + VALID_AUTHN_CNTX_CLS_LIST);
                authnContext = factory.createAuthnContext(UNSPECIFIED_AUTHN_CNTX_CLS, null);
            }
        } else {
            log.debug("Create default Authentication Context Class as: " + X509_AUTHN_CNTX_CLS);
            authnContext = factory.createAuthnContext(X509_AUTHN_CNTX_CLS, null);
        }

        GregorianCalendar issueInstant = calendarFactory();
        if (tokenVals.containsKey(SamlConstants.AUTHN_INSTANT_PROP) &&
                tokenVals.get(SamlConstants.AUTHN_INSTANT_PROP) != null) {
            String authnInstant = tokenVals.get(SamlConstants.AUTHN_INSTANT_PROP).toString();
            try {
                //times must be in UTC format as specified by the XML Schema type (dateTime)
                DatatypeFactory xmlDateFactory = DatatypeFactory.newInstance();
                XMLGregorianCalendar xmlDate = xmlDateFactory.newXMLGregorianCalendar(authnInstant.trim());
                issueInstant = xmlDate.toGregorianCalendar();
                log.debug("Setting Authentication instant to: " + xmlDate.toXMLFormat());
            } catch (IllegalArgumentException iaex) {
                log.debug("Authentication instant: " + authnInstant +
                        " is not in a valid dateTime format, defaulting to current time");
            } catch (DatatypeConfigurationException ex) {
                log.debug("Authentication instant: " + authnInstant +
                        " is not in a valid dateTime format, defaulting to current time");
            }
        } else {
            log.debug("Defaulting Authentication instant to current time");
        }

        String sessionIndex = AUTHN_SESSION_INDEX;
        if (tokenVals.containsKey(SamlConstants.AUTHN_SESSION_INDEX_PROP) &&
                tokenVals.get(SamlConstants.AUTHN_SESSION_INDEX_PROP) != null) {
            sessionIndex = tokenVals.get(SamlConstants.AUTHN_SESSION_INDEX_PROP).toString();
            log.debug("Setting Authentication session index to: " + sessionIndex);
        } else {
            log.debug("Defaulting Authentication session index to: " + sessionIndex);
        }

        // Create Authentication statement
        AuthnStatement authState = (com.sun.xml.wss.saml.assertion.saml20.jaxb20.AuthnStatement) factory.createAuthnStatement(issueInstant, subjLoc, authnContext, sessionIndex, null);

        if (authState != null) {
            statements.add(authState);
        }

        statements.addAll(addAssertStatements(factory));

        // Create resource for Authentication Decision Statement
        String resource = null;
        if (tokenVals.containsKey(SamlConstants.RESOURCE_PROP) &&
                tokenVals.get(SamlConstants.RESOURCE_PROP) != null) {
            resource = tokenVals.get(SamlConstants.RESOURCE_PROP).toString();
            log.debug("Setting Authentication Decision Resource to: " + resource);
        } else {
            log.debug("Default Authentication Decision Resource is: " + resource);
        }

        // Options are Permit, Deny and Indeterminate
        String decision = AUTHZ_DECISION_PERMIT;
        if (tokenVals.containsKey(SamlConstants.AUTHZ_DECISION_PROP) &&
                tokenVals.get(SamlConstants.AUTHZ_DECISION_PROP) != null) {
            String requestedDecision = tokenVals.get(SamlConstants.AUTHZ_DECISION_PROP).toString().trim();
            if (VALID_AUTHZ_DECISION_LIST.contains(requestedDecision)) {
                log.debug("Setting Authentication Decision to: " + requestedDecision);
                decision = requestedDecision;
            } else {
                log.debug(requestedDecision + " is not recognized as valid, " +
                        "create default Authentication Decision as: " + AUTHZ_DECISION_PERMIT);
                log.debug("Should be one of: " + VALID_AUTHZ_DECISION_LIST);
            }
        } else {
            log.debug("Create default Authentication Decision as: " + AUTHZ_DECISION_PERMIT);
        }

        // As of Authorization Framework Spec 2.2 Action is a hard-coded value
        // Therefore the value of the ACTION_PROP is no longer used
        List actions = new ArrayList();
        String actionAttr = AUTHZ_DECISION_ACTION_EXECUTE;
        log.debug("Setting Authentication Decision Action to: " + actionAttr);
        try {
            final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final Element elemURAttr = document.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "Action");
            elemURAttr.setAttribute("Namespace", AUTHZ_DECISION_ACTION_NS);
            elemURAttr.setTextContent(actionAttr);
            actions.add(elemURAttr);
        } catch (ParserConfigurationException ex) {
            actions.add(actionAttr);
        }

        // Evidence Assertion generation
        Evidence evidence = createEvidence();

        AuthnDecisionStatement authDecState = factory.createAuthnDecisionStatement(resource, decision, actions, evidence);
        if (authDecState != null) {
            statements.add(authDecState);
        }

        log.debug("SamlCallbackHandler.createAuthnStatements() -- End");
        return statements;
    }

    /**
     * Creates the Attribute statements for UserName, UserOrganization,
     * UserRole, and PurposeForUse
     * @param factory The factory object used to assist in the construction of
     * the SAML Assertion token
     * @return The listing of all Attribute statements
     * @throws com.sun.xml.wss.saml.SAMLException
     */
    private List addAssertStatements(SAMLAssertionFactory factory) throws SAMLException {

        log.debug("SamlCallbackHandler.addAssertStatements() -- Begin");
        List statements = new ArrayList();
        List attributes = new ArrayList();

        // Set the User Name Attribute
        List attributeValues1 = new ArrayList();
        StringBuffer nameConstruct = new StringBuffer();
        if (tokenVals.containsKey(SamlConstants.USER_FIRST_PROP) &&
                tokenVals.get(SamlConstants.USER_FIRST_PROP) != null) {
            nameConstruct.append(tokenVals.get(SamlConstants.USER_FIRST_PROP).toString() + " ");
        }
        if (tokenVals.containsKey(SamlConstants.USER_MIDDLE_PROP) &&
                tokenVals.get(SamlConstants.USER_MIDDLE_PROP) != null) {
            nameConstruct.append(tokenVals.get(SamlConstants.USER_MIDDLE_PROP).toString() + " ");
        }
        if (tokenVals.containsKey(SamlConstants.USER_LAST_PROP) &&
                tokenVals.get(SamlConstants.USER_LAST_PROP) != null) {
            nameConstruct.append(tokenVals.get(SamlConstants.USER_LAST_PROP).toString() + " ");
        }
        if (nameConstruct.length() > 0) {
            if (nameConstruct.charAt(nameConstruct.length() - 1) == ' ') {
                nameConstruct.setLength(nameConstruct.length() - 1);
            }
            log.debug("UserName: " + nameConstruct.toString());
            attributeValues1.add(nameConstruct.toString());
            attributes.add(factory.createAttribute(SamlConstants.USERNAME_ATTR, attributeValues1));
        } else {
            log.warn("No information provided to fill in user name attribute");
        }

        // Set the User Organization Attribute
        List attributeValues2 = new ArrayList();
        if (tokenVals.containsKey(SamlConstants.USER_ORG_PROP) &&
                tokenVals.get(SamlConstants.USER_ORG_PROP) != null) {
            log.debug("UserOrg: " + tokenVals.get(SamlConstants.USER_ORG_PROP).toString());
            attributeValues2.add(tokenVals.get(SamlConstants.USER_ORG_PROP).toString());
            attributes.add(factory.createAttribute(SamlConstants.USER_ORG_ATTR, attributeValues2));
        } else {
            log.warn("No information provided to fill in user organization attribute");
        }

        // Set the User Organization ID Attribute
        List attributeValues5 = new ArrayList();
        if (tokenVals.containsKey(SamlConstants.USER_ORG_ID_PROP) &&
                tokenVals.get(SamlConstants.USER_ORG_ID_PROP) != null) {
            log.debug("UserOrgID: " + tokenVals.get(SamlConstants.USER_ORG_ID_PROP).toString());
            attributeValues5.add(tokenVals.get(SamlConstants.USER_ORG_ID_PROP).toString());
            attributes.add(factory.createAttribute(SamlConstants.USER_ORG_ID_ATTR, attributeValues5));
        } else {
            log.warn("No information provided to fill in user organization ID attribute");
        }

        // Set the Home Community ID Attribute
        List attributeValues6 = new ArrayList();
        if (tokenVals.containsKey(SamlConstants.HOME_COM_PROP) &&
                tokenVals.get(SamlConstants.HOME_COM_PROP) != null) {
            log.debug("HomeCommunityID: " + tokenVals.get(SamlConstants.HOME_COM_PROP).toString());
            attributeValues6.add(tokenVals.get(SamlConstants.HOME_COM_PROP).toString());
            attributes.add(factory.createAttribute(SamlConstants.HOME_COM_ID_ATTR, attributeValues6));
        } else {
            log.warn("No information provided to fill in home community ID attribute");
        }

        try {
            // Set the User Role Attribute
            List attributeValues3 = new ArrayList();
            final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final Element elemURAttr = document.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "AttibuteValue");
            final Element userRole = document.createElementNS(HL7_NS, "hl7:Role");
            elemURAttr.appendChild(userRole);

            userRole.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "hl7:CE");

            if (tokenVals.containsKey(SamlConstants.USER_CODE_PROP) &&
                    tokenVals.get(SamlConstants.USER_CODE_PROP) != null) {
                log.debug("User Role Code: " + tokenVals.get(SamlConstants.USER_CODE_PROP));
                userRole.setAttribute(SamlConstants.CE_CODE_ID, tokenVals.get(SamlConstants.USER_CODE_PROP).toString());
            } else {
                log.warn("No information provided to fill in user role code attribute");
            }
            if (tokenVals.containsKey(SamlConstants.USER_SYST_PROP) &&
                    tokenVals.get(SamlConstants.USER_SYST_PROP) != null) {
                log.debug("User Role Code System: " + tokenVals.get(SamlConstants.USER_SYST_PROP).toString());
                userRole.setAttribute(SamlConstants.CE_CODESYS_ID, tokenVals.get(SamlConstants.USER_SYST_PROP).toString());
            } else {
                log.warn("No information provided to fill in user role code system attribute");
            }
            if (tokenVals.containsKey(SamlConstants.USER_SYST_NAME_PROP) &&
                    tokenVals.get(SamlConstants.USER_SYST_NAME_PROP) != null) {
                log.debug("User Role Code System Name: " + tokenVals.get(SamlConstants.USER_SYST_NAME_PROP).toString());
                userRole.setAttribute(SamlConstants.CE_CODESYSNAME_ID, tokenVals.get(SamlConstants.USER_SYST_NAME_PROP).toString());
            } else {
                log.warn("No information provided to fill in user role code system name attribute");
            }
            if (tokenVals.containsKey(SamlConstants.USER_DISPLAY_PROP) &&
                    tokenVals.get(SamlConstants.USER_DISPLAY_PROP) != null) {
                log.debug("User Role Display: " + tokenVals.get(SamlConstants.USER_DISPLAY_PROP).toString());
                userRole.setAttribute(SamlConstants.CE_DISPLAYNAME_ID, tokenVals.get(SamlConstants.USER_DISPLAY_PROP).toString());
            } else {
                log.warn("No information provided to fill in user role display attribute");
            }
            attributeValues3.add(elemURAttr);
            attributes.add(factory.createAttribute(SamlConstants.USER_ROLE_ATTR, attributeValues3));
        } catch (ParserConfigurationException ex) {
            log.debug("Unable to create an XML Document to set Attributes" + ex.getMessage());
        }

        try {
            // Add the PurposeForUse Attribute
            List attributeValues4 = new ArrayList();
            final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final Element elemPFUAttr = document.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "AttibuteValue");
            final Element purpose = document.createElementNS(HL7_NS, "hl7:PurposeForUse");
            elemPFUAttr.appendChild(purpose);

            purpose.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "hl7:CE");

            if (tokenVals.containsKey(SamlConstants.PURPOSE_CODE_PROP) &&
                    tokenVals.get(SamlConstants.PURPOSE_CODE_PROP) != null) {
                log.debug("Purpose Code: " + tokenVals.get(SamlConstants.PURPOSE_CODE_PROP).toString());
                purpose.setAttribute(SamlConstants.CE_CODE_ID, tokenVals.get(SamlConstants.PURPOSE_CODE_PROP).toString());
            }
            if (tokenVals.containsKey(SamlConstants.PURPOSE_SYST_PROP) &&
                    tokenVals.get(SamlConstants.PURPOSE_SYST_PROP) != null) {
                log.debug("Purpose Code System: " + tokenVals.get(SamlConstants.PURPOSE_SYST_PROP).toString());
                purpose.setAttribute(SamlConstants.CE_CODESYS_ID, tokenVals.get(SamlConstants.PURPOSE_SYST_PROP).toString());
            }
            if (tokenVals.containsKey(SamlConstants.PURPOSE_SYST_NAME_PROP) &&
                    tokenVals.get(SamlConstants.PURPOSE_SYST_NAME_PROP) != null) {
                log.debug("Purpose Code System Name: " + tokenVals.get(SamlConstants.PURPOSE_SYST_NAME_PROP).toString());
                purpose.setAttribute(SamlConstants.CE_CODESYSNAME_ID, tokenVals.get(SamlConstants.PURPOSE_SYST_NAME_PROP).toString());
            }
            if (tokenVals.containsKey(SamlConstants.PURPOSE_DISPLAY_PROP) &&
                    tokenVals.get(SamlConstants.PURPOSE_DISPLAY_PROP) != null) {
                log.debug("Purpose Display: " + tokenVals.get(SamlConstants.PURPOSE_DISPLAY_PROP).toString());
                purpose.setAttribute(SamlConstants.CE_DISPLAYNAME_ID, tokenVals.get(SamlConstants.PURPOSE_DISPLAY_PROP).toString());
            }
            attributeValues4.add(elemPFUAttr);
            attributes.add(factory.createAttribute(SamlConstants.PURPOSE_ROLE_ATTR, attributeValues4));

            if (!attributes.isEmpty()) {
                statements.add(factory.createAttributeStatement(attributes));
            }
        } catch (ParserConfigurationException ex) {
            log.debug("Unable to create an XML Document to set Attributes" + ex.getMessage());
        }

        // Set the Patient ID Attribute
        List attributeValues7 = new ArrayList();
        if (tokenVals.containsKey(SamlConstants.PATIENT_ID_PROP) &&
                tokenVals.get(SamlConstants.PATIENT_ID_PROP) != null) {
            log.debug("PatientID: " + tokenVals.get(SamlConstants.PATIENT_ID_PROP).toString());
            attributeValues7.add(tokenVals.get(SamlConstants.PATIENT_ID_PROP).toString());
            attributes.add(factory.createAttribute(SamlConstants.PATIENT_ID_ATTR, attributeValues7));
        } else {
            log.warn("No information provided to fill in patient ID attribute");
        }

        log.debug("SamlCallbackHandler.addAssertStatements() -- End");
        return statements;

    }

    /**
     * Creates the Evidence element that encompasses the Assertion defining the
     * authorization form needed in cases where evidence of authorization to
     * access the medical records must be provided along with the message request
     * @param factory The factory object used to assist in the construction of
     * the SAML Assertion token
     * @param issueInstant The calendar representing the time of Assertion issuance
     * @return The Evidence element
     * @throws com.sun.xml.wss.saml.SAMLException
     */
    private Evidence createEvidence() throws SAMLException, XWSSecurityException {
        log.debug("SamlCallbackHandler.createEvidence() -- Begin");

        String evAssertVersion = ASSERTION_VERSION_2_0;
        if ((tokenVals.containsKey(SamlConstants.EVIDENCE_VERSION_PROP) &&
                tokenVals.get(SamlConstants.EVIDENCE_VERSION_PROP) != null)) {
            String requestedVersion = tokenVals.get(SamlConstants.EVIDENCE_VERSION_PROP).toString();
            if (VALID_ASSERTION_VERSION_LIST.contains(requestedVersion.trim())) {
                log.debug("Setting Evidence Assertion Version to: " + requestedVersion);
                evAssertVersion = requestedVersion;
            } else {
                log.debug(requestedVersion + " is not recognized as valid, " +
                        "create evidence assertion version as: " + ASSERTION_VERSION_2_0);
                log.debug("Should be one of: " + VALID_ASSERTION_VERSION_LIST);
            }
        } else {
            log.debug("Defaulting Evidence version to: " + ASSERTION_VERSION_2_0);
        }
        String evAssertFactoryVersion = factoryVersionMap.get(evAssertVersion);
        SAMLAssertionFactory factory = SAMLAssertionFactory.newInstance(evAssertFactoryVersion);

        List evAsserts = new ArrayList();
        try {
            String evAssertionID = String.valueOf(UUID.randomUUID());
            if (tokenVals.containsKey(SamlConstants.EVIDENCE_ID_PROP) &&
                    tokenVals.get(SamlConstants.EVIDENCE_ID_PROP) != null) {
                evAssertionID = tokenVals.get(SamlConstants.EVIDENCE_ID_PROP).toString();
                log.debug("Setting Evidence assertion id to: " + evAssertionID);
            } else {
                log.debug("Defaulting Evidence assertion id to: " + evAssertionID);
            }

            GregorianCalendar issueInstant = calendarFactory();
            if (tokenVals.containsKey(SamlConstants.EVIDENCE_INSTANT_PROP) &&
                    tokenVals.get(SamlConstants.EVIDENCE_INSTANT_PROP) != null) {
                String authnInstant = tokenVals.get(SamlConstants.EVIDENCE_INSTANT_PROP).toString();
                try {
                    //times must be in UTC format as specified by the XML Schema type (dateTime)
                    DatatypeFactory xmlDateFactory = DatatypeFactory.newInstance();
                    XMLGregorianCalendar xmlDate = xmlDateFactory.newXMLGregorianCalendar(authnInstant.trim());
                    issueInstant = xmlDate.toGregorianCalendar();
                    log.debug("Setting Evidence assertion instant to: " + xmlDate.toXMLFormat());
                } catch (IllegalArgumentException iaex) {
                    log.debug("Evidence assertion instant: " + authnInstant +
                            " is not in a valid dateTime format, defaulting to current time");
                } catch (DatatypeConfigurationException ex) {
                    log.debug("Evidence assertion instant: " + authnInstant +
                            " is not in a valid dateTime format, defaulting to current time");
                }
            } else {
                log.debug("Defaulting Authentication instant to current time");
            }

            NameID evIssuerId = null;
            if (tokenVals.containsKey(SamlConstants.EVIDENCE_ISSUER_FORMAT_PROP) &&
                    tokenVals.get(SamlConstants.EVIDENCE_ISSUER_FORMAT_PROP) != null &&
                    tokenVals.containsKey(SamlConstants.EVIDENCE_ISSUER_PROP) &&
                    tokenVals.get(SamlConstants.EVIDENCE_ISSUER_PROP) != null) {

                String format = tokenVals.get(SamlConstants.EVIDENCE_ISSUER_FORMAT_PROP).toString();
                if (VALID_NAME_LIST.contains(format.trim())) {
                    log.debug("Setting Evidence Issuer format to: " + format);
                    String issuer = tokenVals.get(SamlConstants.EVIDENCE_ISSUER_PROP).toString();
                    log.debug("Setting Evidence Issuer to: " + issuer);
                    evIssuerId = factory.createNameID(issuer, null, format);
                } else {
                    log.debug(format + " is not recognized as valid, " +
                            "create default evidence issuer");
                    log.debug("Should be one of: " + VALID_NAME_LIST);
                    evIssuerId = create509NameID(factory, DEFAULT_NAME);
                }
            } else {
                log.debug("Defaulting Evidence issuer format to: " + X509_NAME_ID + " Default issuer identity");
                evIssuerId = create509NameID(factory, DEFAULT_NAME);
            }

            GregorianCalendar beginValidTime = calendarFactory();
            if ((tokenVals.containsKey(SamlConstants.EVIDENCE_CONDITION_NOT_BEFORE_PROP) &&
                    tokenVals.get(SamlConstants.EVIDENCE_CONDITION_NOT_BEFORE_PROP) != null)) {
                beginValidTime = createCal(tokenVals.get(SamlConstants.EVIDENCE_CONDITION_NOT_BEFORE_PROP).toString());
            } else {
                log.debug("Defaulting Evidence NotBefore condition to: current time");
            }

            GregorianCalendar endValidTime = calendarFactory();
            if ((tokenVals.containsKey(SamlConstants.EVIDENCE_CONDITION_NOT_AFTER_PROP) &&
                    tokenVals.get(SamlConstants.EVIDENCE_CONDITION_NOT_AFTER_PROP) != null)) {
                endValidTime = createCal(tokenVals.get(SamlConstants.EVIDENCE_CONDITION_NOT_AFTER_PROP).toString());
            } else {
                log.debug("Defaulting Evidence NotAfter condition to: current time");
            }

            if (beginValidTime.after(endValidTime)) {
                // set beginning time to now
                beginValidTime = calendarFactory();
                log.warn("The beginning time for the valid evidence should be before the ending time.  " +
                        "Setting the beginning time to the current system time.");
            }

            Conditions conditions = factory.createConditions(beginValidTime, endValidTime, null, null, null, null);

            List statements = createEvidenceStatements(factory);
            Assertion evAssert = factory.createAssertion(evAssertionID, evIssuerId, issueInstant, conditions, null, null, statements);
            evAssert.setVersion(evAssertVersion);
            evAsserts.add(evAssert);
        } catch (SAMLException ex) {
            log.debug("Unable to create Evidence Assertion: " + ex.getMessage());
        }
        Evidence evidence = factory.createEvidence(null, evAsserts);
        log.debug("SamlCallbackHandler.createEvidence() -- End");
        return evidence;
    }

    /**
     * Creates a calendar object representing the time given.
     * @param time following the UTC format as specified by the XML Schema type (dateTime)
     * or for backward compatibility following the simple date form MM/dd/yyyy HH:mm:ss
     * @return The calendar object representing the given time
     */
    private GregorianCalendar createCal(String time) {

        GregorianCalendar cal = calendarFactory();
        try {
            //times must be in UTC format as specified by the XML Schema type (dateTime)
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

        log.info("Created calendar instance: " + (cal.get(Calendar.MONTH) + 1) +
                "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR) +
                " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) +
                ":" + cal.get(Calendar.SECOND));
        return cal;
    }

    /**
     * Creates the Attribute Statements needed for the Evidence element.  These
     * include the Attributes for the Access Consent Policy and the Instance
     * Access Consent Policy
     * @param factory The factory object used to assist in the construction of
     * the SAML Assertion token
     * @return The listing of the attribute statements for the Evidence element
     * @throws com.sun.xml.wss.saml.SAMLException
     */
    private List createEvidenceStatements(
            SAMLAssertionFactory factory) throws SAMLException {
        log.debug("SamlCallbackHandler.createEvidenceStatements() -- Begin");
        List statements = new ArrayList();
        List attributes = new ArrayList();

        // Set the Access Consent
        List attributeValues1 = new ArrayList();
        if (tokenVals.containsKey(SamlConstants.EVIDENCE_ACCESS_CONSENT_PROP) &&
                tokenVals.get(SamlConstants.EVIDENCE_ACCESS_CONSENT_PROP) != null) {
            log.debug("Setting Evidence Access Consent to: " + tokenVals.get(SamlConstants.EVIDENCE_ACCESS_CONSENT_PROP).toString());
            attributeValues1.add(tokenVals.get(SamlConstants.EVIDENCE_ACCESS_CONSENT_PROP).toString());
        } else {
            log.debug("No Access Consent found for Evidence");
        }

        attributes.add(factory.createAttribute("AccessConsentPolicy", NHIN_NS, attributeValues1));

        // Set the Instance Access Consent
        List attributeValues2 = new ArrayList();
        if (tokenVals.containsKey(SamlConstants.EVIDENCE_INST_ACCESS_CONSENT_PROP) &&
                tokenVals.get(SamlConstants.EVIDENCE_INST_ACCESS_CONSENT_PROP) != null) {
            log.debug("Setting Evidence Instance Access Consent to: " + tokenVals.get(SamlConstants.EVIDENCE_INST_ACCESS_CONSENT_PROP).toString());
            attributeValues2.add(tokenVals.get(SamlConstants.EVIDENCE_INST_ACCESS_CONSENT_PROP).toString());
        } else {
            log.debug("No Instance Access Consent found for Evidence");
        }

        attributes.add(factory.createAttribute("InstanceAccessConsentPolicy", NHIN_NS, attributeValues2));

        if (!attributes.isEmpty()) {
            statements.add(factory.createAttributeStatement(attributes));
        }

        log.debug("SamlCallbackHandler.createEvidenceStatements() -- End");
        return statements;
    }

    /**
     * Initializes the keystore access using the system properties defined in
     * the domain.xml javax.net.ssl.keyStore and javax.net.ssl.keyStorePassword
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
     * @throws java.io.IOException
     */
    private void initTrustStore() throws IOException {
        log.debug("SamlCallbackHandler.initTrustStore() -- Begin");

        InputStream is = null;
        String storeType = System.getProperty("javax.net.ssl.trustStoreType");
        String password = System.getProperty("javax.net.ssl.trustStorePassword");
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
     * @param request The SignatureKeyCallback request object
     * @throws java.io.IOException
     */
    private void getDefaultPrivKeyCert(
            SignatureKeyCallback.DefaultPrivKeyCertRequest request)
            throws IOException {
        log.debug("SamlCallbackHandler.getDefaultPrivKeyCert() -- Begin");
        String uniqueAlias = null;
        String client_key_alias = System.getProperty("CLIENT_KEY_ALIAS");
        if (client_key_alias != null) {
            String password = System.getProperty("javax.net.ssl.keyStorePassword");
            if (password != null) {
                try {
                    Enumeration aliases = keyStore.aliases();
                    while (aliases.hasMoreElements()) {
                        String currentAlias = (String) aliases.nextElement();
                        if (currentAlias.equals(client_key_alias)) {
                            if (keyStore.isKeyEntry(currentAlias)) {
                                Certificate thisCertificate = keyStore.getCertificate(currentAlias);
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
                        request.setX509Certificate(
                                (X509Certificate) keyStore.getCertificate(uniqueAlias));
                        request.setPrivateKey(
                                (PrivateKey) keyStore.getKey(uniqueAlias, password.toCharArray()));
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
     * @return The calendar instance
     */
    private GregorianCalendar calendarFactory() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        return calendar;
    }
}
