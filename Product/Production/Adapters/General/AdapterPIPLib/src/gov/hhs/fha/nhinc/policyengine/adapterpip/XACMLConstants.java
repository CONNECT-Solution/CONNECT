package gov.hhs.fha.nhinc.policyengine.adapterpip;

/**
 * This class contains constants used to construct the XACML policy
 * document that represents the patient's consent information.
 *
 * @author Les Westberg
 */
public class XACMLConstants
{
    public static final String POLICY_DESCRIPTION = "CONSUMER CONSENT POLICY";
    public static final String RULE_COMBINING_ALG_FIRST_APPLICABLE = "urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:first-applicable";
    public static final String MATCH_ID_STRING_EQUAL = "urn:oasis:names:tc:xacml:1.0:function:string-equal";
    public static final String MATCH_ID_IID_EQUAL = "http://www.hhs.gov/healthit/nhin/function#instance-identifier-equal";
    public static final String ATTRIBUTE_VALUE_TYPE_ANYURI = "http://www.w3.org/2001/XMLSchema#anyURI";
    public static final String ATTRIBUTE_VALUE_TYPE_IID = "http://www.hhs.gov/healthit/nhin#instance-identifier";
    public static final String ATTRIBUTE_VALUE_TYPE_STRING = "http://www.w3.org/2001/XMLSchema#string";
    public static final String ATTRIBUTE_VALUE_RETRIEVE_DOCUMENT = "http://www.hhs.gov/healthit/nhin#retrieveDocument";
    public static final String ACTION_ID = "urn:oasis:names:tc:xacml:2.0:action";
    public static final String SUBJECT_SUBJECT_ID = "urn:oasis:names:tc:xacml:2.0:subject:subject-id";
    public static final String SUBJECT_LOCALITY = "urn:oasis:names:tc:xacml:2.0:subject:locality";
    public static final String SUBJECT_ROLE = "urn:oasis:names:tc:xacml:2.0:subject:role";
    public static final String SUBJECT_ROLE_START_DATE = "urn:gov:hhs:fha:nhinc:subject:role-start-date";
    public static final String SUBJECT_ROLE_END_DATE = "urn:gov:hhs:fha:nhinc:subject:role-end-date";
    public static final String SUBJECT_PURPOSE_OF_USE = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
    public static final String RESOURCE_RESOURCE_ID = "urn:oasis:names:tc:xacml:2.0:resource:resource-id";
    public static final String RESOURCE_CONFIDENTIALITY_CODE = "urn:oasis:names:tc:xspa:1.0:resource:patient:hl7:confidentiality-code";
    public static final String RESOURCE_SNOMEDCT_TYPE = "urn:oasis:names:tc:xspa:1.0:resource:snomedct:type";
    public static final String RESOURCE_PATIENT_OPT_IN = "urn:gov:hhs:fha:nhinc:patient-opt-in";

    public static final String PATIENT_SUBJECT_ID = "http://www.hhs.gov/healthit/nhin#subject-id";

    public static final String DESCRIPTION_OPT_IN_OUT_RULE = "Rule specifying preference for global opt in/opt out of NHIN.";
    public static final String DESCRIPTION_FINE_GRAINED_RULE = "Rule specifying fine grained criteria.";
    


    // Constants for an NHIN patient ID tag
    //-------------------------------------
    public static final String NHIN_PATIENT_ID_TAG = "nhin:PatientId";
    public static final String NHIN_PATIENT_ID_TAG_ROOT = "root";
    public static final String NHIN_PATIENT_ID_TAG_EXTENSION = "extension";

}
