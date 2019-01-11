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
package gov.hhs.fha.nhinc.docretrieve.audit;

/**
 * Constants values related to Document Retrieve Audit Logger implementation.
 *
 * @author vimehta
 *
 */
public class DocRetrieveAuditTransformsConstants {

    public static final String EVENTID_CODE_SYSTEM = "DCM";
    public static final String EVENTTYPE_CODE_SYSTEM = "IHE Transactions";
    public static final String EVENTTYPE_CODE_DISPLAY_NAME = "Cross Gateway Retrieve";
    public static final String PARTICIPANT_OBJECT_DETAIL_REPOSITORY_UNIQUE_TYPE = "Repository Unique Id";
    public static final String PARTICIPANT_OBJECT_DETAIL_HOME_COMMUNITY_ID_TYPE = "ihe:homeCommunityID";

    //ParticipantObjectIdentification Patient Constants
    public static final String PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM = "RFC-3881";
    public static final String PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE = "2";
    public static final String PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME = "Patient Number";
    public static final short PARTICIPANT_PATIENT_OBJ_TYPE_CODE = 1;
    public static final short PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE = 1;

    // ParticipantObjectIdentification Document Constants
    public static final String PARTICIPANT_DOCUMENT_OBJ_ID_TYPE_CODE_SYSTEM = "RFC-3881";
    public static final String PARTICIPANT_DOCUMENT_OBJ_ID_TYPE_CODE = "9";
    public static final String PARTICIPANT_DOCUMENT_OBJ_ID_TYPE_DISPLAY_NAME = "Report Number";
    public static final short PARTICIPANT_DOCUMENT_OBJ_TYPE_CODE = 2;
    public static final short PARTICIPANT_DOCUMENT_OBJ_TYPE_CODE_ROLE = 3;

    // Requestor  constants
    public static final String EVENTID_CODE_REQUESTOR = "110107";
    public static final String EVENT_ACTION_CODE_REQUESTOR = "C";
    public static final String EVENTID_CODE_DISPLAY_REQUESTOR = "Import";
    public static final String EVENTTYPE_CODE_REQUESTOR = "ITI-39";

    // Responder constants
    public static final String EVENTID_CODE_RESPONDER = "110106";
    public static final String EVENT_ACTION_CODE_RESPONDER = "R";
    public static final String EVENTID_CODE_DISPLAY_RESPONDER = "Export";
    public static final String EVENTTYPE_CODE_RESPONDER = "ITI-43";

}
