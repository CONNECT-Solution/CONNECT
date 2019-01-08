/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.transform.subdisc;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7Constants {

    public static final String ITS_VERSION = "XML_1.0";
    public static final String SENDER_DETERMINER_CODE = "INSTANCE";
    public static final String RECEIVER_DETERMINER_CODE = "INSTANCE";
    public static final String DEFAULT_LOCAL_DEVICE_ID = "1.1";
    public static final String NULL_FLAVOR = "NA";
    public static final String INTERACTION_ID_ROOT = "2.16.840.1.113883.1.6";
    public static final String SSN_ID_ROOT = "2.16.840.1.113883.4.1";

    // ResponderBusy Error Constants
    public static final String QUERY_ACK_OK = "OK";
    public static final String QUERY_ACK_QE = "QE";
    public static final String DETECTED_ISSUE_CLASSCODE_ALRT = "ALRT";
    public static final String DETECTED_ISSUE_MOODCODE_EVN = "EVN";
    public static final String DETECTED_ISSUE_CODE_ADMINISTRATIVE = "ActAdministrativeDetectedIssueCode";
    public static final String DETECTED_ISSUE_CODESYSTEM_ERROR_CODE = "2.16.840.1.113883.5.4";
    public static final String DETECTED_ISSUE_MITIGATEDBY_TYPECODE_MITGT = "MITGT";
    public static final String DETECTEDISSUEMANAGEMENT_CLASSCODE = "ACT";
    public static final String DETECTEDISSUEMANAGEMENT_MOODCODE_RQO = "RQO";
    public static final String DETECTEDISSUEMANAGEMENT_CODE_RESPONDER_BUSY = "ResponderBusy";
    public static final String DETECTEDISSUEMANAGEMENT_CODESYSTEM = "1.3.6.1.4.1.19376.1.2.27.3";

    public static final String AGENT_CLASS_CODE = "AGNT";
    public static final String ORG_CLASS_CODE = "ORG";
    public static final String ASSIGNED_DEVICE_CLASS_CODE = "ASSIGNED";

    // ControlActProcess.ReasonOf.DetectedIssueEvent
    public static final String DETECTED_ISSUE_EVENT_OID = "2.16.578.1.34.5.3";
    public static final String DETECTED_ISSUE_EVENT_CODE_KNOWNPAT = "KNOWNPAT";
    public static final String DETECTED_ISSUE_EVENT_CODE_KNOWNPAT_DESC = "The patient is already known in the patient registry.";
    public static final String DETECTED_ISSUE_EVENT_CODE_VALIDATION = "VALIDATION";
    public static final String DETECTED_ISSUE_EVENT_CODE_VALIDATION_DESC = "The query parameters are wrong or insufficient.";
    public static final String DETECTED_ISSUE_EVENT_CODE_AUTHENTICATION = "AUTHENTICATION";
    public static final String DETECTED_ISSUE_EVENT_CODE_AUTHENTICATION_DESC = "The user cannot be authenticated.";
    public static final String DETECTED_ISSUE_EVENT_CODE_AUTHORIZATION = "AUTHORIZATION";
    public static final String DETECTED_ISSUE_EVENT_CODE_AUTHORIZATION_DESC = "The user is not authorized.";
}
