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
package gov.hhs.fha.nhinc.docdatasubmission.audit;

public class DocDataSubmissionAuditTransformsConstants {

    public static final String DDS_SUBMISSIONSET_PATIENT_ID = "XDSSubmissionSet.patientId";
    public static final String DDS_SUBMISSIONSET_UNIQUE_ID = "XDSSubmissionSet.uniqueId";

    public static final short PARTICIPANT_PATIENT_OBJ_TYPE_CODE_SYSTEM = 1;
    public static final short PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE = 1;
    public static final String PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE = "2";
    public static final String PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM = "RFC-3881";
    public static final String PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME = "Patient Number";
    public static final short PARTICIPANT_SUBMISSION_SET_OBJ_TYPE_CODE_SYSTEM = 2;
    public static final short PARTICIPANT_SUBMISSION_SET_OBJ_TYPE_CODE_ROLE = 20;
    public static final String PARTICIPANT_SUBMISSION_SET_OBJ_ID_TYPE_CODE
        = "urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd";
    public static final String PARTICIPANT_SUBMISSION_SET_OBJ_ID_TYPE_CODE_SYSTEM = "IHE XDS Metadata";
    public static final String PARTICIPANT_SUBMISSION_SET_OBJ_ID_TYPE_DISPLAY_NAME = "submission set classificationNode";

    public static final String EVENT_ID_CODE_DDS_SOURCE = "110106";
    public static final String EVENT_ID_CODE_DDS_RECIPIENT = "110107";
    public static final String EVENT_ID_CODE_SYSTEM = "DCM";
    public static final String EVENT_ID_DISPLAY_SOURCE = "Export";
    public static final String EVENT_ID_DISPLAY_RECIPIENT = "Import";

    public static final String EVENT_TYPE_CODE = "ITI-42";
    public static final String EVENT_TYPE_CODE_SYSTEM = "IHE Transactions";
    public static final String EVENT_TYPE_CODE_DISPLAY_NAME = "Register Document Set-b";
    public static final String EVENT_ACTION_CODE_SOURCE = "R";
    public static final String EVENT_ACTION_CODE_RECIPIENT = "C";

    private DocDataSubmissionAuditTransformsConstants() {
        throw new IllegalStateException("Utility class");
    }

}
