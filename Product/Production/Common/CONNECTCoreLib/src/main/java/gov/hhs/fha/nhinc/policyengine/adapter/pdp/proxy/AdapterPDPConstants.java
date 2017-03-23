/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.policyengine.adapter.pdp.proxy;

/**
 *
 * @author Mastan.Ketha
 */
public class AdapterPDPConstants {

    public static final String DECISION_VALUE_PERMIT = "Permit";
    public static final String DECISION_VALUE_DENY = "Deny";
    public static final String DOCUMENT_CLASS_CODE = "57017-6";
    public static final String RULE_COMBINING_ALG_ID = "urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:first-applicable";
    public static final String MATCHID_FUNCTION_STRING_EQUAL = "urn:oasis:names:tc:xacml:1.0:function:string-equal";
    public static final String MATCHID_FUNCTION_ANYURI_EQUAL = "urn:oasis:names:tc:xacml:1.0:function:anyURI-equal";
    public static final String ATTRIBUTEVALUE_DATATYPE_STRING = "http://www.w3.org/2001/XMLSchema#string";
    public static final String ATTRIBUTEVALUE_DATATYPE_ANYURI = "http://www.w3.org/2001/XMLSchema#anyURI";
    public static final String ATTRIBUTE_DESIGNATOR_ATTRIBUTEID_ROLE = "urn:oasis:names:tc:xacml:2.0:subject:role";
    public static final String ATTRIBUTE_DESIGNATOR_ATTRIBUTEID_PURPOSEOFUSE = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
    public static final String ATTRIBUTE_DESIGNATOR_DATATYPE_STRING = "http://www.w3.org/2001/XMLSchema#string";
    public static final String ATTRIBUTE_DESIGNATOR_DATATYPE_ANYURI = "http://www.w3.org/2001/XMLSchema#anyURI";
    public static final String REQUEST_CONTEXT_ATTRIBUTE_RESOURCEID = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
    public static final String REQUEST_CONTEXT_ATTRIBUTE_AA_ID = "urn:gov:hhs:fha:nhinc:assigning-authority-id";
    public static final String REQUEST_CONTEXT_ATTRIBUTE_SERVICE_TYPE = "urn:gov:hhs:fha:nhinc:service-type";
    public static final String POLICY_RESULT_STATUS_CODE_OK = "urn:oasis:names:tc:xacml:1.0:status:ok";
    public static final String POLICY_RESULT_STATUS_CODE_MISSING_ATTRIBUTE = "urn:oasis:names:tc:xacml:1.0:status:missing-attribute";
    public static final String POLICY_RESULT_STATUS_CODE_SYNTAX_ERROR = "urn:oasis:names:tc:xacml:1.0:status:syntax-error";
    public static final String POLICY_RESULT_STATUS_CODE_PROCESSING_ERROR = "urn:oasis:names:tc:xacml:1.0:status:processing-error";
    public static final String POLICY_RESULT_STATUS_MESSAGE_OK = "Policy Evaluation Success";
    public static final String POLICY_RESULT_STATUS_MESSAGE_MISSING_ATTRIBUTE = "Attributes necessary to make a policy decision were not available or incomplete";
    public static final String POLICY_RESULT_STATUS_MESSAGE_SYNTAX_ERROR = "Attribute value contained a syntax error";
    public static final String POLICY_RESULT_STATUS_MESSAGE_PROCESSING_ERROR = "Error occurred during policy evaluation";
    public static final String REQUEST_ACTION_PATIENT_DISCOVERY_OUT = "PatientDiscoveryOut";
    public static final String REQUEST_ACTION_PATIENT_DISCOVERY_IN = "PatientDiscoveryIn";
    public static final String REQUEST_ACTION_DOCUMENT_QUERY_OUT = "DocumentQueryOut";
    public static final String REQUEST_ACTION_DOCUMENT_QUERY_IN = "DocumentQueryIn";
    public static final String REQUEST_ACTION_DOCUMENT_RETRIEVE_OUT = "DocumentRetrieveOut";
    public static final String REQUEST_ACTION_DOCUMENT_RETRIEVE_IN = "DocumentRetrieveIn";

}
