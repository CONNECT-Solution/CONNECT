/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Statement;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;
import org.w3c.dom.Element;

/**
 * @author bhumphrey
 *
 */
public class HOKSAMLAssertionBuilder extends SAMLAssertionBuilder {
	
	 private static Log log = LogFactory.getLog(HOKSAMLAssertionBuilder.class);
	 
	
	
	 /**
	 * @param properties
	 */
	public HOKSAMLAssertionBuilder(CallbackProperties properties) {
		super(properties);
	}



	/**
     * Creates the "Holder-of-Key" variant of the SAML Assertion token.
     * 
     * @return The Assertion element
     * @throws Exception
     */
	 public Element build() throws Exception {
        log.debug("SamlCallbackHandler.createHOKSAMLAssertion20() -- Begin");
        Element signedAssertion = null;
        try {
            Assertion assertion = null;
            assertion = (Assertion) OpenSAML2ComponentBuilder.getInstance().createAssertion();

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

            // sign the message
           signedAssertion = sign(assertion);
        } catch (Exception ex) {
            log.error("Unable to create HOK Assertion: " + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
        log.debug("SamlCallbackHandler.createHOKSAMLAssertion20() -- End");
        return signedAssertion;
    }
    

    /**
     * @param assertion
     * @throws Exception 
     */
    static Element sign(Assertion assertion) throws Exception {
        Signature signature = OpenSAML2ComponentBuilder.getInstance().createSignature();
        assertion.setSignature(signature);
               
        // marshall Assertion Java class into XML
        MarshallerFactory marshallerFactory = Configuration.getMarshallerFactory();
        Marshaller marshaller = marshallerFactory.getMarshaller(assertion);
        Element assertionElement = marshaller.marshall(assertion);
        try {
            Signer.signObject(signature);
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return assertionElement;
    }

    private Issuer createIssuer() {
        return createIssuer(getProperties());
    }
    
    
    static Issuer createIssuer(CallbackProperties properties) {
        Issuer issuer = null;

        String format = properties.getAssertionIssuerFormat();
        if (format != null) {
            log.debug("Setting Assertion Issuer format to: " + format);
            String sIssuer = properties.getIssuer();
            
            log.debug("Setting Assertion Issuer to: " + sIssuer);

            if (isValidNameidFormat(format)) {
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
 
        return createSubject(getProperties());
    }
    
    /**
     * @return
     * @throws Exception
     */
    static Subject createSubject(CallbackProperties properties) throws Exception {
        org.opensaml.saml2.core.Subject subject = null;
        String x509Name = "UID=" + properties.getUsername();
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
//        statements.addAll(createUserNameAttributeStatements());
//        statements.addAll(createOrganizationAttributeStatements());
//        statements.addAll(createHomeCommunityIdAttributeStatements());
//        statements.addAll(createPatientIdAttributeStatements());
//        statements.addAll(createUserRoleStatements());
//        statements.addAll(createPurposeOfUseStatements());
//        statements.addAll(createAuthenicationDecsionStatements());

        return statements;
    }

    /**
     * @return
     */
    private List<AuthnStatement> createAuthenicationStatements() {

        return createAuthenicationStatements(getProperties());
    }
    
    
    
   static List<AuthnStatement> createAuthenicationStatements(CallbackProperties properties) {

        List<AuthnStatement> authnStatements = new ArrayList<AuthnStatement>();

        String cntxCls =properties.getAuthenicationContextClass();
        if (cntxCls == null ) {
        	cntxCls =  X509_AUTHN_CNTX_CLS;
        } else if (!isValidAuthnCntxCls(cntxCls)) {
        	cntxCls = UNSPECIFIED_AUTHN_CNTX_CLS;
        }
        String sessionIndex = properties.getAuthenicationSessionIndex();
        
        if ( sessionIndex == null) {
        	sessionIndex =  AUTHN_SESSION_INDEX;
        }
        log.debug("Setting Authentication session index to: " + sessionIndex);

        DateTime authInstant = properties.getAuthenicationInstant();
        if (authInstant == null) {
        	authInstant = new DateTime();
        }

        String inetAddr = properties.getSubjectLocality();
        String dnsName = properties.getSubjectLocality();
        
        AuthnStatement authnStatement = OpenSAML2ComponentBuilder.getInstance().createAuthenicationStatements(cntxCls,
                sessionIndex, authInstant, inetAddr, dnsName);

        authnStatements.add(authnStatement);

        return authnStatements;
    }
    


   
//
//  
//    /**
//     * @return
//     */
//    private List<AuthzDecisionStatement> createAuthenicationDecsionStatements() {
//        List<AuthzDecisionStatement> authDecisionStatements = new ArrayList<AuthzDecisionStatement>();
//
//        Boolean hasAuthzStmt = getNullSafeBoolean(tokenVals, SamlConstants.AUTHZ_STATEMENT_EXISTS_PROP, Boolean.FALSE);
//        // The authorization Decision Statement is optional
//        if (hasAuthzStmt) {
//            // Create resource for Authentication Decision Statement
//            String resource = getNullSafeString(tokenVals, SamlConstants.RESOURCE_PROP);
//
//            // Options are Permit, Deny and Indeterminate
//            String decision = getNullSafeString(tokenVals, SamlConstants.AUTHZ_DECISION_PROP, AUTHZ_DECISION_PERMIT);
//
//            decision = validate(decision, VALID_AUTHZ_DECISION_LIST, AUTHZ_DECISION_PERMIT);
//
//            // As of Authorization Framework Spec 2.2 Action is a hard-coded
//            // value
//            // Therefore the value of the ACTION_PROP is no longer used
//            String action = AUTHZ_DECISION_ACTION_EXECUTE;
//
//            Evidence evidence = createEvidence();
//
//            authDecisionStatements.add(OpenSAML2ComponentBuilder.getInstance().createAuthzDecisionStatement(resource,
//                    decision, action, evidence));
//        }
//
//        return authDecisionStatements;
//    }
//
//    /**
//     * Creates the Evidence element that encompasses the Assertion defining the authorization form needed in cases where
//     * evidence of authorization to access the medical records must be provided along with the message request
//     * 
//     * @param factory The factory object used to assist in the construction of the SAML Assertion token
//     * @param issueInstant The calendar representing the time of Assertion issuance
//     * @return The Evidence element
//     */
//    private Evidence createEvidence() {
//        log.debug("SamlCallbackHandler.createEvidence() -- Begin");
//
//        List<Assertion> evidenceAssertions = new ArrayList<Assertion>();
//        String evAssertionID = getNullSafeString(tokenVals, SamlConstants.EVIDENCE_ID_PROP,
//                String.valueOf(UUID.randomUUID()));
//
//        DateTime issueInstant = getNullSafeDateTime(tokenVals, SamlConstants.EVIDENCE_INSTANT_PROP, new DateTime());
//
//        String format = getNullSafeString(tokenVals, SamlConstants.EVIDENCE_ISSUER_FORMAT_PROP);
//        format = validate(format, VALID_NAME_LIST, X509_NAME_ID);
//
//        String issuer = getNullSafeString(tokenVals, SamlConstants.EVIDENCE_ISSUER_PROP);
//
//        Issuer evIssuerId = OpenSAML2ComponentBuilder.getInstance().createIssuer(format, issuer);
//
//        DateTime beginValidTime = getNullSafeDateTime(tokenVals, SamlConstants.EVIDENCE_CONDITION_NOT_BEFORE_PROP,
//                new DateTime());
//
//        DateTime endValidTime = getNullSafeDateTime(tokenVals, SamlConstants.EVIDENCE_CONDITION_NOT_AFTER_PROP,
//                new DateTime().plusMinutes(5));
//
//        if (beginValidTime.isAfter(endValidTime)) {
//            // set beginning time to now
//            beginValidTime = new DateTime();
//            log.warn("The beginning time for the valid evidence should be before the ending time.  "
//                    + "Setting the beginning time to the current system time.");
//        }
//
//        Conditions conditions = OpenSAML2ComponentBuilder.getInstance().createConditions(beginValidTime, endValidTime,
//                null);
//
//        List<AttributeStatement> statements = createEvidenceStatements();
//
//        Assertion evidenceAssertion = OpenSAML2ComponentBuilder.getInstance().createAssertion(evAssertionID);
//
//        evidenceAssertion.getAttributeStatements().addAll(statements);
//        evidenceAssertion.setConditions(conditions);
//        evidenceAssertion.setIssueInstant(issueInstant);
//        evidenceAssertion.setIssuer(evIssuerId);
//
//        evidenceAssertions.add(evidenceAssertion);
//
//        Evidence evidence = OpenSAML2ComponentBuilder.getInstance().createEvidence(evidenceAssertions);
//
//        log.debug("SamlCallbackHandler.createEvidence() -- End");
//        return evidence;
//    }
//
//    /**
//     * Creates the Attribute Statements needed for the Evidence element. These include the Attributes for the Access
//     * Consent Policy and the Instance Access Consent Policy
//     * 
//     * @param factory The factory object used to assist in the construction of the SAML Assertion token
//     * @return The listing of the attribute statements for the Evidence element
//     */
//    private List<AttributeStatement> createEvidenceStatements() {
//        log.debug("SamlCallbackHandler.createEvidenceStatements() -- Begin");
//        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
//
//        List accessConstentValues = getNullSafeList(tokenVals, SamlConstants.EVIDENCE_ACCESS_CONSENT_PROP);
//        if (accessConstentValues == null) {
//            log.debug("No Access Consent found for Evidence");
//        }
//
//        // Set the Instance Access Consent
//        List evidenceInstanceAccessConsentValues = getNullSafeList(tokenVals,
//                SamlConstants.EVIDENCE_INST_ACCESS_CONSENT_PROP);
//        if (evidenceInstanceAccessConsentValues == null) {
//            log.debug("No Instance Access Consent found for Evidence");
//        }
//
//        statements = OpenSAML2ComponentBuilder.getInstance().createEvidenceStatements(accessConstentValues,
//                evidenceInstanceAccessConsentValues, NHIN_NS);
//
//        log.debug("SamlCallbackHandler.createEvidenceStatements() -- End");
//        return statements;
//    }
//
//    /**
//     * Creates the Attribute statements for UserName, UserOrganization, UserRole, and PurposeOfUse
//     * 
//     * @param factory The factory object used to assist in the construction of the SAML Assertion token
//     * @return The listing of all Attribute statements
//     */
//    private List<AttributeStatement> createUserNameAttributeStatements() {
//
//        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
//        List<Attribute> attributes = new ArrayList<Attribute>();
//
//        // Set the User Name Attribute
//        List<String> userNameValues = new ArrayList<String>();
//        String nameConstruct = getUserName();
//
//        if (nameConstruct.length() > 0) {
//            log.debug("UserName: " + nameConstruct);
//
//            userNameValues.add(nameConstruct);
//
//            attributes.add(OpenSAML2ComponentBuilder.getInstance().createAttribute(null, SamlConstants.USERNAME_ATTR,
//                    null, userNameValues));
//        } else {
//            log.warn("No information provided to fill in user name attribute");
//        }
//        if (!attributes.isEmpty()) {
//            statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
//        }
//
//        return statements;
//    }
//
//    /**
//     * Creates the Attribute statements UserRole
//     * 
//     * @param factory The factory object used to assist in the construction of the SAML Assertion token
//     * @return The listing of all Attribute statements
//     */
//    private List<AttributeStatement> createUserRoleStatements() {
//        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
//        List<Attribute> attributes = new ArrayList<Attribute>();
//
//        // Set the User Role Attribute
//        List userRoleAttributeValues = new ArrayList();
//        Map<QName, String> userRoleAttributes = new HashMap<QName, String>();
//
//        conditionallyAddValue(userRoleAttributes, tokenVals, SamlConstants.USER_CODE_PROP, new QName(SamlConstants.CE_CODE_ID));
//        conditionallyAddValue(userRoleAttributes, tokenVals, SamlConstants.USER_SYST_PROP, new QName(SamlConstants.CE_CODESYS_ID));
//        conditionallyAddValue(userRoleAttributes, tokenVals, SamlConstants.USER_SYST_NAME_PROP,
//                new QName(SamlConstants.CE_CODESYSNAME_ID));
//        conditionallyAddValue(userRoleAttributes, tokenVals, SamlConstants.USER_DISPLAY_PROP,
//                new QName(SamlConstants.CE_DISPLAYNAME_ID));
//
//        userRoleAttributes.put(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "hl7:CE");
//        
//        userRoleAttributeValues.add(OpenSAML2ComponentBuilder.getInstance().createAttributeValue(HL7_NS, "Role", "hl7",
//                userRoleAttributes));
//   
//        attributes.add(OpenSAML2ComponentBuilder.getInstance().createAttribute(null, SamlConstants.USER_ROLE_ATTR,
//                null, userRoleAttributeValues));
//
//        if (!attributes.isEmpty()) {
//            statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
//        }
//
//        return statements;
//
//    }
//
//    /**
//     * Creates the Attribute statements PurposeOfUse
//     * 
//     * @param factory The factory object used to assist in the construction of the SAML Assertion token
//     * @return The listing of all Attribute statements
//     */
//    private List<AttributeStatement> createPurposeOfUseStatements() {
//        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
//        List<Attribute> attributes = new ArrayList<Attribute>();
//
//        /*
//         * Gateway-347 - Support for both values will remain until NHIN Specs updated Determine whether to use
//         * PurposeOfUse or PuposeForUse
//         */
//        String purposeAttributeValueName = "hl7:PurposeOfUse";
//        if (isPurposeForUseEnabled()) {
//            purposeAttributeValueName = "hl7:PurposeForUse";
//        }
//
//        // Add the Purpose Of/For Use Attribute Value
//
//        List purposeOfUserValues = new ArrayList();
//        Map<QName, String> purposeOfUseAttributes = new HashMap<QName, String>();
//
//        conditionallyAddValue(purposeOfUseAttributes, tokenVals, SamlConstants.PURPOSE_CODE_PROP,
//                new QName(SamlConstants.CE_CODE_ID));
//
//        conditionallyAddValue(purposeOfUseAttributes, tokenVals, SamlConstants.PURPOSE_SYST_PROP,
//        		new QName(SamlConstants.CE_CODESYS_ID));
//
//        conditionallyAddValue(purposeOfUseAttributes, tokenVals, SamlConstants.PURPOSE_SYST_NAME_PROP,
//        		new QName(SamlConstants.CE_CODESYSNAME_ID));
//
//        conditionallyAddValue(purposeOfUseAttributes, tokenVals, SamlConstants.PURPOSE_DISPLAY_PROP,
//        		new QName(SamlConstants.CE_DISPLAYNAME_ID));
//
//        purposeOfUserValues.add(OpenSAML2ComponentBuilder.getInstance().createAttributeValue(HL7_NS,
//                purposeAttributeValueName, "hl7", purposeOfUseAttributes));
//
//        attributes.add(OpenSAML2ComponentBuilder.getInstance().createAttribute(null, SamlConstants.PURPOSE_ROLE_ATTR,
//                null, purposeOfUserValues));
//
//        if (!attributes.isEmpty()) {
//            statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
//        }
//
//        return statements;
//
//    }
//
//    /**
//     * Creates the Attribute statements for UserName, UserOrganization, UserRole, and PurposeOfUse
//     * 
//     * @param factory The factory object used to assist in the construction of the SAML Assertion token
//     * @return The listing of all Attribute statements
//     */
//    private List<AttributeStatement> createOrganizationAttributeStatements() {
//
//        log.debug("SamlCallbackHandler.addAssertStatements() -- Begin");
//        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
//        List<Attribute> attributes = new ArrayList<Attribute>();
//
//        // Set the User Organization ID Attribute
//        conditionallyAddAttribute(attributes, tokenVals, SamlConstants.USER_ORG_PROP, null,
//                SamlConstants.USER_ORG_ATTR, null);
//
//        if (!attributes.isEmpty()) {
//            statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
//        }
//
//        log.debug("SamlCallbackHandler.addAssertStatements() -- End");
//        return statements;
//
//    }
//
//    /**
//     * Creates the Attribute statements for UserName, UserOrganization, UserRole, and PurposeOfUse
//     * 
//     * @param factory The factory object used to assist in the construction of the SAML Assertion token
//     * @return The listing of all Attribute statements
//     */
//    private List<AttributeStatement> createHomeCommunityIdAttributeStatements() {
//
//        log.debug("SamlCallbackHandler.addAssertStatements() -- Begin");
//        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
//        List<Attribute> attributes = new ArrayList<Attribute>();
//
//        // Set the Home Community ID Attribute
//        conditionallyAddAttribute(attributes, tokenVals, SamlConstants.HOME_COM_PROP, null,
//                SamlConstants.HOME_COM_ID_ATTR, null);
//
//        if (!attributes.isEmpty()) {
//            statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
//        }
//
//        log.debug("SamlCallbackHandler.addAssertStatements() -- End");
//        return statements;
//
//    }
//
//    /**
//     * Creates the Attribute statements for UserName, UserOrganization, UserRole, and PurposeOfUse
//     * 
//     * @param factory The factory object used to assist in the construction of the SAML Assertion token
//     * @return The listing of all Attribute statements
//     */
//    private List<AttributeStatement> createPatientIdAttributeStatements() {
//
//        log.debug("SamlCallbackHandler.addAssertStatements() -- Begin");
//        List<AttributeStatement> statements = new ArrayList<AttributeStatement>();
//        List<Attribute> attributes = new ArrayList<Attribute>();
//
//        // Set the Patient ID Attribute
//        conditionallyAddAttribute(attributes, tokenVals, SamlConstants.PATIENT_ID_PROP, null,
//                SamlConstants.PATIENT_ID_ATTR, null);
//
//        if (!attributes.isEmpty()) {
//            statements.addAll(OpenSAML2ComponentBuilder.getInstance().createAttributeStatement(attributes));
//        }
//
//        log.debug("SamlCallbackHandler.addAssertStatements() -- End");
//        return statements;
//
//    }
//
//
//    /**
//     * Both the Issuer and the Subject elements have a NameID element which is formed through this method. Currently
//     * default data is used to specify the required Issuer information. However, the Subject information is defined
//     * based on the stored value of the userid. If this is a legal X509 structute the NameId is constructed in that
//     * format, if not it is constructed as an "Unspecified" format.
//     * 
//     * @param factory The factory object used to assist in the construction of the SAML Assertion token
//     * @param assId Identifies this as default usage case or one with declared value.
//     * @return The constructed NameID element
//     * @throws com.sun.xml.wss.saml.SAMLException derp derp
//     */
//    private NameID create509NameID(SAMLAssertionFactory factory, int assId) throws SAMLException {
//        log.debug("SamlCallbackHandler.create509NameID() -- Begin: " + assId);
//        NameID nmId = null;
//        String defCN = "SAML User";
//        String defOU = "SU";
//        String defO = "SAML User";
//        String defL = "Los Angeles";
//        String defST = "CA";
//        String defC = "US";
//
//        String identifier;
//        if (assId != PRIMARY_NAME || !tokenVals.containsKey(SamlConstants.USER_NAME_PROP)
//                || tokenVals.get(SamlConstants.USER_NAME_PROP) == null) {
//            identifier = "CN=" + defCN + "," + "OU=" + defOU + "," + "O=" + defO + "," + "L=" + defL + "," + "ST="
//                    + defST + "," + "C=" + defC;
//            nmId = factory.createNameID(identifier, null, X509_NAME_ID);
//            log.debug("Create default X509 name: " + identifier);
//        } else {
//            String x509Name = "UID=" + tokenVals.get(SamlConstants.USER_NAME_PROP).toString();
//            try {
//                X500Principal prin = new X500Principal(x509Name);
//                nmId = factory.createNameID(x509Name, null, X509_NAME_ID);
//                log.debug("Create X509 name: " + x509Name);
//            } catch (IllegalArgumentException iae) {
//                /* Could also test if email form if we wanted to support that */
//                log.warn("Set format as Unspecified. Invalid X509 format: "
//                        + tokenVals.get(SamlConstants.USER_NAME_PROP) + " " + iae.getMessage());
//                nmId = factory.createNameID(tokenVals.get(SamlConstants.USER_NAME_PROP).toString(), null,
//                        UNSPECIFIED_NAME_ID);
//            }
//        }
//
//        log.debug("SamlCallbackHandler.create509NameID() -- End: " + nmId.getValue());
//        return nmId;
//    }
//
//    /**
//     * Returns boolean condition on whether PurposeForUse is enabled
//     * 
//     * @return The PurposeForUse enabled setting
//     */
//    private boolean isPurposeForUseEnabled() {
//        boolean match = false;
//        try {
//            // Use CONNECT utility class to access gateway.properties
//            String purposeForUseEnabled = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
//                    PURPOSE_FOR_USE_DEPRECATED_ENABLED);
//            if (purposeForUseEnabled != null && purposeForUseEnabled.equalsIgnoreCase("true")) {
//                match = true;
//            }
//        } catch (PropertyAccessException ex) {
//            log.error("Error: Failed to retrieve " + PURPOSE_FOR_USE_DEPRECATED_ENABLED + " from property file: "
//                    + NhincConstants.GATEWAY_PROPERTY_FILE);
//            log.error(ex.getMessage());
//        }
//        return match;
//    }
//
//    /**
//     * Creates a calendar object representing the time given.
//     * 
//     * @param time following the UTC format as specified by the XML Schema type (dateTime) or for backward compatibility
//     *            following the simple date form MM/dd/yyyy HH:mm:ss
//     * @return The calendar object representing the given time
//     */
//    private GregorianCalendar createCal(String time) {
//
//        GregorianCalendar cal = calendarFactory();
//        try {
//            // times must be in UTC format as specified by the XML Schema type
//            // (dateTime)
//            DatatypeFactory xmlDateFactory = DatatypeFactory.newInstance();
//            XMLGregorianCalendar xmlDate = xmlDateFactory.newXMLGregorianCalendar(time.trim());
//            cal = xmlDate.toGregorianCalendar();
//        } catch (IllegalArgumentException iaex) {
//            try {
//                // try simple date format - backward compatibility
//                SimpleDateFormat dateForm = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//                cal.setTime(dateForm.parse(time));
//            } catch (ParseException ex) {
//                log.error("Date form is expected to be in dateTime format yyyy-MM-ddTHH:mm:ss.SSSZ Setting default date");
//            }
//        } catch (DatatypeConfigurationException dtex) {
//            log.error("Problem in creating XML Date Factory. Setting default date");
//        }
//
//        log.info("Created calendar instance: " + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH)
//                + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE)
//                + ":" + cal.get(Calendar.SECOND));
//        return cal;
//    }
//
//    /**
//     * Creates a calendar instance set to the current system time in GMT
//     * 
//     * @return The calendar instance
//     */
//    private GregorianCalendar calendarFactory() {
//        GregorianCalendar calendar = new GregorianCalendar();
//        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
//        return calendar;
//    }


}
