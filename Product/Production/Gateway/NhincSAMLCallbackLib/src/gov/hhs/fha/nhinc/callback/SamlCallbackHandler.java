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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import javax.mail.internet.AddressException;
//import javax.mail.internet.InternetAddress;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class implements the CallbackHandler which is invoked upon sending a 
 * message requiring the SAML Assertion Token.  It accesses the information
 * stored in NMProperties in order to build up the required token elements.
 */
public class SamlCallbackHandler implements CallbackHandler {

    private static Log log = LogFactory.getLog(SamlCallbackHandler.class);
    private static final String AUTHN_DECISION = "Permit";
    private static final String EVIDENCE_FORM_TYPE = "application/pdf";
    public static final String HOK_CONFIRM = "urn:oasis:names:tc:SAML:2.0:cm:holder-of-key";
    public static final String SV_CONFIRM = "urn:oasis:names:tc:SAML:2.0:cm:authorization-over-ssl";
    private static final String X509_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";
    private static final String WIN_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:WindowsDomainQualifiedName";
    private static final String UNSPECIFIED = "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified";
    private static final String X509_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:X509";
    private static final String NHIN_NS = "http://www.hhs.gov/healthit/nhin";
    private static final int DEFAULT_NAME = 0;
    private static final int PRIMARY_NAME = 1;
    private static final String STORE_TYPE = "JKS";
    // Custom Property Names must match as defined in the WSDL
    private static final String ACTION_PROP = "action";
    private static final String RESOURCE_PROP = "resource";
    private static final String PURPOSE_CODE_PROP = "purposeForUseRoleCode";
    private static final String PURPOSE_SYST_PROP = "purposeForUseCodeSystem";
    private static final String PURPOSE_SYST_NAME_PROP = "purposeForUseCodeSystemName";
    private static final String PURPOSE_DISPLAY_PROP = "purposeForUseDisplayName";
    private static final String USER_FIRST_PROP = "userFirstName";
    private static final String USER_MIDDLE_PROP = "userMiddleName";
    private static final String USER_LAST_PROP = "userLastName";
    private static final String USER_NAME_PROP = "userName";
    private static final String USER_ORG_PROP = "userOrganization";
    private static final String USER_CODE_PROP = "userRoleCode";
    private static final String USER_SYST_PROP = "userRoleCodeSystem";
    private static final String USER_SYST_NAME_PROP = "userRoleCodeSystemName";
    private static final String USER_DISPLAY_PROP = "userRoleCodeDisplayName";
    private static final String EXPIRE_PROP = "expirationDate";
    private static final String SIGN_PROP = "signDate";
    private static final String CONTENT_REF_PROP = "contentReference";
    private static final String CONTENT_PROP = "content";
    private HashMap<Object, Object> tokenVals = new HashMap<Object, Object>();
    private KeyStore keyStore;
    private KeyStore trustStore;
    private static Element svAssertion;
    private static Element hokAssertion20;

    static {
        //WORKAROUND NEEDED IN METRO1.4. TO BE REMOVED LATER.
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier() {

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
        log.debug(" **********************************  Handle SAML Callback Begin**************************");
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
        log.debug("**********************************  Handle SAML Callback End**************************");
    }

    /**
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
            if (tokenVals.containsKey(USER_NAME_PROP)) {
                uname = tokenVals.get(USER_NAME_PROP).toString();
            }
            NameID nmId = factory.createNameID(uname, null, WIN_ID);
            Subject subj = factory.createSubject(nmId, null);

            // authentication statement
            List statements = createAuthnStatements(factory, issueInstant);

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
            NameID issueId = create509NameID(factory, DEFAULT_NAME);

            // issue instant
            GregorianCalendar issueInstant = calendarFactory();

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
            List statements = createAuthnStatements(factory, issueInstant);

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
        log.debug("SamlCallbackHandler.createNameID() -- Begin " + assId);
        NameID nmId = null;
        String defCN = "SAML User";
        String defOU = "SU";
        String defO = "SAML User";
        String defL = "Los Angeles";
        String defST = "CA";
        String defC = "US";

        String identifier;
        if (assId != PRIMARY_NAME || !tokenVals.containsKey(USER_NAME_PROP)) {
            identifier = "CN=" + defCN + "," + "OU=" + defOU + "," +
                    "O=" + defO + "," + "L=" + defL + "," +
                    "ST=" + defST + "," + "C=" + defC;
            nmId = factory.createNameID(identifier, null, X509_ID);
            log.debug("Create default X509 name: " + identifier);
        } else {
            String x509Name = "UID=" + tokenVals.get(USER_NAME_PROP);
            try {
                X500Principal prin = new X500Principal(x509Name);
                nmId = factory.createNameID(x509Name, null, X509_ID);
                log.debug("Create X509 name: " + x509Name);
            } catch (IllegalArgumentException iae) {
                /* Could also test if email form if we wanted to support that */
                log.warn("Set format as Unspecified. Invalid X509 format: " +
                        tokenVals.get(USER_NAME_PROP) + " " + iae.getMessage());
                nmId = factory.createNameID(tokenVals.get(USER_NAME_PROP).toString(), null, UNSPECIFIED);
            }
        }

        log.debug("SamlCallbackHandler.createNameID() -- End");
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
    private List createAuthnStatements(SAMLAssertionFactory factory, GregorianCalendar issueInstant) throws SAMLException {
        log.debug("SamlCallbackHandler.createAuthnStatements() -- Begin");
        List statements = new ArrayList();

        // Create Subject Locality
        SubjectLocality subjLoc = null;
        /* This is currently an optional item
        try {
        subjLoc = factory.createSubjectLocality(InetAddress.getLocalHost().getHostAddress(), InetAddress.getLocalHost().getCanonicalHostName());
        } catch (UnknownHostException ex) {
        log.debug("Optional element SubjectLocality can not be determined: " + ex.getMessage());
        }*/
        AuthnContext authnContext = factory.createAuthnContext(X509_AUTHN_CNTX_CLS, null);

        // Create Authentication statement
        AuthnStatement authState = (com.sun.xml.wss.saml.assertion.saml20.jaxb20.AuthnStatement) factory.createAuthnStatement(issueInstant, subjLoc, authnContext, "123456", null);

        if (authState != null) {
            statements.add(authState);
        }

        statements.addAll(addAssertStatements(factory));

        // Create resource for Authentication Statement
        String resource = null;
        if (tokenVals.containsKey(RESOURCE_PROP)) {
            log.debug("Resource: " + tokenVals.get(RESOURCE_PROP));
            resource = tokenVals.get(RESOURCE_PROP).toString();
        }

        // Options are Permit (Deny and Indeterminate are not used at this time)
        String decision = AUTHN_DECISION;

        List actions = new ArrayList();
        if (tokenVals.containsKey(ACTION_PROP)) {
            String actionAttr = tokenVals.get(ACTION_PROP).toString();
            try {
                final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                final Element elemURAttr = document.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "Action");
                elemURAttr.setAttribute("Namespace", NHIN_NS);
                elemURAttr.setTextContent(actionAttr);
                actions.add(elemURAttr);
            } catch (ParserConfigurationException ex) {
                actions.add(actionAttr);
            }
        }

        // Evidence Assertion generation           
        Evidence evidence = createEvidence(factory, issueInstant);

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
        if (tokenVals.containsKey(USER_FIRST_PROP)) {
            nameConstruct.append(tokenVals.get(USER_FIRST_PROP) + " ");
        }
        if (tokenVals.containsKey(USER_MIDDLE_PROP)) {
            nameConstruct.append(tokenVals.get(USER_MIDDLE_PROP) + " ");
        }
        if (tokenVals.containsKey(USER_LAST_PROP)) {
            nameConstruct.append(tokenVals.get(USER_LAST_PROP) + " ");
        }
        if (nameConstruct.length() > 0) {
            if (nameConstruct.charAt(nameConstruct.length() - 1) == ' ') {
                nameConstruct.setLength(nameConstruct.length() - 1);
            }
            log.debug("UserName: " + nameConstruct.toString());
            attributeValues1.add(nameConstruct.toString());
            attributes.add(factory.createAttribute("UserName", NHIN_NS, attributeValues1));
        } else {
            log.warn("No information provided to fill in user name attribute");
        }

        // Set the User Organization Attribute
        List attributeValues2 = new ArrayList();
        if (tokenVals.containsKey(USER_ORG_PROP)) {
            log.debug("UserOrg: " + tokenVals.get(USER_ORG_PROP));
            attributeValues2.add(tokenVals.get(USER_ORG_PROP));
            attributes.add(factory.createAttribute("UserOrganization", NHIN_NS, attributeValues2));
        } else {
            log.warn("No information provided to fill in user organization attribute");
        }

        try {
            // Set the User Role Attribute
            List attributeValues3 = new ArrayList();
            final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final Element elemURAttr = document.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "AttibuteValue");
            final Element userRole = document.createElementNS(NHIN_NS, "nhin:Role");
            elemURAttr.appendChild(userRole);
            if (tokenVals.containsKey(USER_CODE_PROP)) {
                log.debug("User Role Code: " + tokenVals.get(USER_CODE_PROP));
                userRole.setAttribute("code", tokenVals.get(USER_CODE_PROP).toString());
            } else {
                log.warn("No information provided to fill in user role code attribute");
            }
            if (tokenVals.containsKey(USER_SYST_PROP)) {
                log.debug("User Role Code System: " + tokenVals.get(USER_SYST_PROP));
                userRole.setAttribute("codeSystem", tokenVals.get(USER_SYST_PROP).toString());
            } else {
                log.warn("No information provided to fill in user role code system attribute");
            }
            if (tokenVals.containsKey(USER_SYST_NAME_PROP)) {
                log.debug("User Role Code System Name: " + tokenVals.get(USER_SYST_NAME_PROP));
                userRole.setAttribute("codeSystemName", tokenVals.get(USER_SYST_NAME_PROP).toString());
            } else {
                log.warn("No information provided to fill in user role code system name attribute");
            }
            if (tokenVals.containsKey(USER_DISPLAY_PROP)) {
                log.debug("User Role Display: " + tokenVals.get(USER_DISPLAY_PROP));
                userRole.setAttribute("displayName", tokenVals.get(USER_DISPLAY_PROP).toString());
            } else {
                log.warn("No information provided to fill in user role display attribute");
            }
            attributeValues3.add(elemURAttr);
            attributes.add(factory.createAttribute("UserRole", NHIN_NS, attributeValues3));

            // Add the Purpose For Use Attribute
            List attributeValues4 = new ArrayList();
            final Element elemPFUAttr = document.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "AttibuteValue");
            final Element purpose = document.createElementNS(NHIN_NS, "nhin:PurposeForUse");
            elemPFUAttr.appendChild(purpose);
            if (tokenVals.containsKey(PURPOSE_CODE_PROP)) {
                log.debug("Purpose Code: " + tokenVals.get(PURPOSE_CODE_PROP));
                purpose.setAttribute("code", tokenVals.get(PURPOSE_CODE_PROP).toString());
            }
            if (tokenVals.containsKey(PURPOSE_SYST_PROP)) {
                log.debug("Purpose Code System: " + tokenVals.get(PURPOSE_SYST_PROP));
                purpose.setAttribute("codeSystem", tokenVals.get(PURPOSE_SYST_PROP).toString());
            }
            if (tokenVals.containsKey(PURPOSE_SYST_NAME_PROP)) {
                log.debug("Purpose Code System Name: " + tokenVals.get(PURPOSE_SYST_NAME_PROP));
                purpose.setAttribute("codeSystemName", tokenVals.get(PURPOSE_SYST_NAME_PROP).toString());
            }
            if (tokenVals.containsKey(PURPOSE_DISPLAY_PROP)) {
                log.debug("Purpose Display: " + tokenVals.get(PURPOSE_DISPLAY_PROP));
                purpose.setAttribute("displayName", tokenVals.get(PURPOSE_DISPLAY_PROP).toString());
            }
            attributeValues4.add(elemPFUAttr);
            attributes.add(factory.createAttribute("PurposeForUse", NHIN_NS, attributeValues4));

            if (!attributes.isEmpty()) {
                statements.add(factory.createAttributeStatement(attributes));
            }
        } catch (ParserConfigurationException ex) {
            log.debug("Unable to create an XML Document to set Attributes" + ex.getMessage());
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
    private Evidence createEvidence(SAMLAssertionFactory factory, GregorianCalendar issueInstant) throws SAMLException {
        log.debug("SamlCallbackHandler.createEvidence() -- Begin");

        List evAsserts = new ArrayList();
        try {
            String evAssertionID = String.valueOf(UUID.randomUUID());
            NameID evIssuerId = create509NameID(factory, DEFAULT_NAME);

            GregorianCalendar beginValidTime = calendarFactory();
            if (tokenVals.containsKey(SIGN_PROP)) {
                beginValidTime = createCal(tokenVals.get(SIGN_PROP).toString());
            }
            GregorianCalendar endValidTime = calendarFactory();
            if (tokenVals.containsKey(EXPIRE_PROP)) {
                endValidTime = createCal(tokenVals.get(EXPIRE_PROP).toString());
            }

            if (beginValidTime.after(endValidTime)) {
                // set beginning time to now
                beginValidTime = calendarFactory();
                log.warn("The beginning time for the valid evidence should be before the ending time.  " +
                        "Setting the beginning time to the current system time.");
            }

            Conditions conditions = factory.createConditions(beginValidTime, endValidTime, null, null, null, null);

            List statements = createEvidenceStatements(factory);
            evAsserts.add(factory.createAssertion(evAssertionID, evIssuerId, issueInstant, conditions, null, null, statements));
        } catch (SAMLException ex) {
            log.debug("Unable to create Evidence Assertion: " + ex.getMessage());
        }
        Evidence evidence = factory.createEvidence(null, evAsserts);
        log.debug("SamlCallbackHandler.createEvidence() -- End");
        return evidence;
    }

    /**
     * Creates a calendar object representing the time given.
     * @param time following the simple date form MM/dd/yyyy HH:mm:ss
     * @return The calendar object representing the given time
     */
    private GregorianCalendar createCal(String time) {
        GregorianCalendar cal = calendarFactory();
        try {
            SimpleDateFormat dateForm = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            cal.setTime(dateForm.parse(time));
            log.info("SamlCallbackHandler.createCal() Date: " + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
        } catch (ParseException ex) {
            log.error(SamlCallbackHandler.class.getName() + "Date form is expected to be MM/dd/yyyy HH:mm:ss set default date");
        }
        return cal;
    }

    /**
     * Creates the Attribute Statements needed for the Evidence element.  These 
     * include the Attributes for the ContentType, ContentReference, and the 
     * base64binary Content as well.
     * @param factory The factory object used to assist in the construction of 
     * the SAML Assertion token
     * @return The listing of the attribute statements for the Evidence element
     * @throws com.sun.xml.wss.saml.SAMLException
     */
    private List createEvidenceStatements(SAMLAssertionFactory factory) throws SAMLException {
        log.debug("SamlCallbackHandler.createEvidenceStatements() -- Begin");
        List statements = new ArrayList();
        List attributes = new ArrayList();

        // Set the Reference to the SSA-827 form
        List attributeValues1 = new ArrayList();
        if (tokenVals.containsKey(CONTENT_REF_PROP)) {
            attributeValues1.add(tokenVals.get(CONTENT_REF_PROP));
        }
        attributes.add(factory.createAttribute("ContentReference", NHIN_NS, attributeValues1));

        // Set the format of the SSA-827 form
        List attributeValues2 = new ArrayList();
        attributeValues2.add(EVIDENCE_FORM_TYPE);
        attributes.add(factory.createAttribute("ContentType", NHIN_NS, attributeValues2));

        // Set the content of the SSA-827 form
        List attributeValues3 = new ArrayList();
        if (tokenVals.containsKey(CONTENT_PROP)) {
            if (tokenVals.get(CONTENT_PROP) instanceof String) {
                byte[] contentForm = Base64Coder.decode(tokenVals.get(CONTENT_PROP).toString());
                attributeValues3.add(contentForm);
            } else {
                attributeValues3.add(tokenVals.get(CONTENT_PROP));
            }

        }
        attributes.add(factory.createAttribute("Content", NHIN_NS, attributeValues3));

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
        String storeLoc = System.getProperty("javax.net.ssl.keyStore");
        if (storeLoc != null) {
            String password = System.getProperty("javax.net.ssl.keyStorePassword");
            if (password != null) {
                try {
                    keyStore = KeyStore.getInstance(STORE_TYPE);
                    is = new FileInputStream(storeLoc);
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
                }
                log.debug("SamlCallbackHandler.initKeyStore() -- End");
            } else {
                log.error("javax.net.ssl.keyStorePassword is not defined in domain.xml");
            }
        } else {
            log.error("javax.net.ssl.keyStore is not defined in domain.xml");
        }
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
        String storeLoc = System.getProperty("javax.net.ssl.trustStore");
        if (storeLoc != null) {
            String password = System.getProperty("javax.net.ssl.trustStorePassword");
            if (password != null) {
                try {
                    trustStore = KeyStore.getInstance(STORE_TYPE);
                    is = new FileInputStream(storeLoc);
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
                }
            } else {
                log.error("javax.net.ssl.trustStorePassword is not defined in domain.xml");
            }
        } else {
            log.error("javax.net.ssl.trustStore is not defined in domain.xml");
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
