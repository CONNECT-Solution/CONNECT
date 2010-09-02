/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

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
    public static final String MATCH_ID_URI_EQUAL = "urn:oasis:names:tc:xacml:1.0:function:anyURI-equal";
    public static final String MATCH_ID_IID_EQUAL = "http://www.hhs.gov/healthit/nhin/function#instance-identifier-equal";
    public static final String MATCH_ID_EMAIL_EQUAL = "urn:oasis:names:tc:xacml:1.0:function:rfc822Name-match";
    public static final String MATCH_ID_X500_EQUAL = "urn:oasis:names:tc:xacml:1.0:function:x500Name-match";
    public static final String MATCH_ID_DATE_GREATER_OR_EQUAL = "urn:oasis:names:tc:xacml:1.0:function:date-greater-than-or-equal";
    public static final String MATCH_ID_DATE_LESS_OR_EQUAL = "urn:oasis:names:tc:xacml:1.0:function:date-less-than-or-equal";

    public static final String ATTRIBUTE_VALUE_TYPE_ANYURI = "http://www.w3.org/2001/XMLSchema#anyURI";
    public static final String ATTRIBUTE_VALUE_TYPE_IID = "urn:hl7-org:v3#II";
    public static final String ATTRIBUTE_VALUE_TYPE_STRING = "http://www.w3.org/2001/XMLSchema#string";
    public static final String ATTRIBUTE_VALUE_TYPE_EMAIL = "urn:oasis:names:tc:xacml:1.0:data-type:rfc822Name";
    public static final String ATTRIBUTE_VALUE_TYPE_X500 = "urn:oasis:names:tc:xacml:1.0:data-type:x500Name";
    public static final String ATTRIBUTE_VALUE_TYPE_DATE = "http://www.w3.org/2001/XMLSchema#date";

    public static final String ATTRIBUTE_VALUE_RETRIEVE_DOCUMENT = "urn:ihe:iti:2007:CrossGatewayRetrieve";
    public static final String ATTRIBUTE_VALUE_QUERY_DOCUMENT = "urn:ihe:iti:2007:CrossGatewayQuery";

    public static final String ACTION_ID = "urn:oasis:names:tc:xacml:1.0:action:action-id";

    public static final String SUBJECT_SUBJECT_ID = "urn:oasis:names:tc:xacml:2.0:subject:subject-id";
    public static final String SUBJECT_LOCALITY = "urn:oasis:names:tc:xacml:2.0:subject:locality";
    public static final String SUBJECT_ROLE = "urn:oasis:names:tc:xacml:2.0:subject:role";
    public static final String SUBJECT_PURPOSE_OF_USE = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
    public static final String SUBJECT_ORGANIZATION_ID = "urn:oasis:names:tc:xspa:1.0:subject:organization-id";
    public static final String SUBJECT_HOME_COMMUNITY_ID = "http://www.hhs.gov/healthit/nhin#HomeCommunityId";
    public static final String SUBJECT_USER_ID = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";

    public static final String RESOURCE_RESOURCE_ID = "urn:oasis:names:tc:xacml:2.0:resource:resource-id";
    public static final String RESOURCE_CONFIDENTIALITY_CODE = "urn:oasis:names:tc:xspa:1.0:resource:patient:hl7:confidentiality-code";
    public static final String RESOURCE_DOCUMENT_TYPE = "urn:oasis:names:tc:xspa:1.0:resource:hl7:type";
    public static final String RESOURCE_DOCUMENT_ID = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
    public static final String RESOURCE_PATIENT_OPT_IN = "urn:gov:hhs:fha:nhinc:patient-opt-in";

    public static final String ENVIRONMENT_RULE_START_DATE = "http://www.hhs.gov/healthit/nhin#rule-start-date";
    public static final String ENVIRONMENT_RULE_END_DATE = "http://www.hhs.gov/healthit/nhin#rule-end-date";

    public static final String QNAME_NAMESPACE_PREFIX = "xlmns";
    public static final String QNAME_NAMESPACE_LOCALPART_HL7 = "hl7";
    public static final String NAMESPACE_HL7_V3 = "urn:hl7-org:v3";


    public static final String PATIENT_SUBJECT_ID = "http://www.hhs.gov/healthit/nhin#subject-id";

    public static final String DESCRIPTION_OPT_IN_OUT_RULE = "Rule specifying preference for global opt in/opt out of NHIN.";
    public static final String DESCRIPTION_FINE_GRAINED_RULE = "Rule specifying fine grained criteria.";


    // Constants for an NHIN patient ID tag
    //-------------------------------------
    public static final String NHIN_PATIENT_ID_TAG = "hl7:PatientId";
    public static final String NHIN_PATIENT_ID_TAG_ROOT = "root";
    public static final String NHIN_PATIENT_ID_TAG_EXTENSION = "extension";

}
