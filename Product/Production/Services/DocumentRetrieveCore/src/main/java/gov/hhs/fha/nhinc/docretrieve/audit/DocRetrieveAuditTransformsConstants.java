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
