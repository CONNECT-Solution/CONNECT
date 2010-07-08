/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.audit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;

import gov.hhs.fha.nhinc.common.nhinccommon.UserType;

import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.List;
import java.util.TimeZone;

/**
 *
 * @author MFLYNN02
 */
public class AuditDataTransformHelper {

    private static Log log = LogFactory.getLog(AuditDataTransformHelper.class);

    public static EventIdentificationType createEventIdentification(String actionCode, Integer eventOutcome, CodedValueType eventId) {
        EventIdentificationType eventIdentification = new EventIdentificationType();

        // Set the Event Action Code
        eventIdentification.setEventActionCode(actionCode);

        // Set the Event Action Time
        try {
            java.util.GregorianCalendar today =
                    new java.util.GregorianCalendar(TimeZone.getTimeZone("GMT"));
            javax.xml.datatype.DatatypeFactory factory =
                    javax.xml.datatype.DatatypeFactory.newInstance();
            javax.xml.datatype.XMLGregorianCalendar calendar =
                    factory.newXMLGregorianCalendar(
                    today.get(java.util.GregorianCalendar.YEAR),
                    today.get(java.util.GregorianCalendar.MONTH) + 1,
                    today.get(java.util.GregorianCalendar.DAY_OF_MONTH),
                    today.get(java.util.GregorianCalendar.HOUR_OF_DAY),
                    today.get(java.util.GregorianCalendar.MINUTE),
                    today.get(java.util.GregorianCalendar.SECOND),
                    today.get(java.util.GregorianCalendar.MILLISECOND),
                    0);
            eventIdentification.setEventDateTime(calendar);
        } catch (Exception e) {
            log.error("Exception when createing XMLGregorian Date");
            log.error(" message: " + e.getMessage());
        }
        // TODO:  Set the Event Outcome Indicator
        eventIdentification.setEventOutcomeIndicator(BigInteger.ZERO);

        // Set the Event Id
        eventIdentification.setEventID(eventId);

        return eventIdentification;
    }

    public static CodedValueType createEventId(String eventCode, String eventCodeSys, String eventCodeSysName, String dispName) {
        CodedValueType eventId = new CodedValueType();

        // Set the Event Id Code
        eventId.setCode(eventCode);

        // Set the Event Id Codesystem
        eventId.setCodeSystem(eventCodeSys);

        // Set the Event Id Codesystem Name
        eventId.setCodeSystemName(eventCodeSysName);

        // Set the Event Id Display Name
        eventId.setDisplayName(dispName);

        return eventId;
    }

    public static CodedValueType createCodeValueType(String code, String codeSys, String codeSysName, String dispName) {
        CodedValueType codeValueType = new CodedValueType();

        // Set the Event Id Code
        codeValueType.setCode(code);

        // Set the Event Id Codesystem
        codeValueType.setCodeSystem(codeSys);

        // Set the Event Id Codesystem Name
        codeValueType.setCodeSystemName(codeSysName);

        // Set the Event Id Display Name
        codeValueType.setDisplayName(dispName);

        return codeValueType;
    }

    public static AuditMessageType.ActiveParticipant createActiveParticipantFromUser(UserType userInfo, Boolean userIsReq) {
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        String ipAddr = null;

        try {
            ipAddr = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }

        // Set the User Id
        String userId = null;
        if (userInfo != null &&
                userInfo.getUserName() != null &&
                userInfo.getUserName().length() > 0) {
            userId = userInfo.getUserName();
        }

        if (userId != null) {
            participant.setUserID(userId);
        }

        // If specified, set the User Name
        String userName = null;
        if (userInfo != null &&
                userInfo.getPersonName() != null) {
            if (userInfo.getPersonName().getGivenName() != null &&
                    userInfo.getPersonName().getGivenName().length() > 0) {
                userName = userInfo.getPersonName().getGivenName();
            }

            if (userInfo.getPersonName().getFamilyName() != null &&
                    userInfo.getPersonName().getFamilyName().length() > 0) {
                if (userName != null) {
                    userName += (" " + userInfo.getPersonName().getFamilyName());
                } else {
                    userName = userInfo.getPersonName().getFamilyName();
                }
            }
        }
        if (userName != null) {
            participant.setUserName(userName);
        }

        // Set the UserIsRequester Flag
        participant.setUserIsRequestor(userIsReq);

        // Set the Network Access Point Id to the IP Address of the machine
        participant.setNetworkAccessPointID(ipAddr);

        // Set the Network Access Point Typecode
        participant.setNetworkAccessPointTypeCode(AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_IP);

        return participant;
    }

    public static AuditMessageType.ActiveParticipant createActiveParticipant(String userId, String altUserId, String userName, Boolean userIsReq) {
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        String ipAddr = null;

        try {
            ipAddr = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }

        // Set the User Id
        if (userId != null) {
            participant.setUserID(userId);
        }

        // If specified, set the Alternative User Id
        if (altUserId != null) {
            participant.setAlternativeUserID(altUserId);
        }

        // If specified, set the User Name
        if (userName != null) {
            participant.setUserName(userName);
        }

        // Set the UserIsRequester Flag
        participant.setUserIsRequestor(userIsReq);

        // Set the Network Access Point Id to the IP Address of the machine
        participant.setNetworkAccessPointID(ipAddr);

        // Set the Network Access Point Typecode
        participant.setNetworkAccessPointTypeCode(AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_IP);

        return participant;
    }

    public static AuditSourceIdentificationType createAuditSourceIdentificationFromUser(UserType userInfo) {
        AuditSourceIdentificationType auditSrcId = new AuditSourceIdentificationType();

        // Home Community ID and name                   
        String communityId = null;
        String communityName = null;

        if (userInfo != null &&
                userInfo.getOrg() != null) {
            if (userInfo.getOrg().getHomeCommunityId() != null &&
                    userInfo.getOrg().getHomeCommunityId().length() > 0) {
                communityId = userInfo.getOrg().getHomeCommunityId();
            }

            if (userInfo.getOrg().getName() != null &&
                    userInfo.getOrg().getName().length() > 0) {
                communityName = userInfo.getOrg().getName();
            }
        }

        // Set the Audit Source Id (community id)
        if (communityId != null) {
            log.debug("communityId prior to remove urn:oid" + communityId);
            if (communityId.startsWith("urn:oid:")) {
                auditSrcId.setAuditSourceID(communityId.substring(8));
            } else {
                auditSrcId.setAuditSourceID(communityId);
            }
        }

        // If specified, set the Audit Enterprise Site Id (community name)
        if (communityName != null) {
            auditSrcId.setAuditEnterpriseSiteID(communityName);
        }

        return auditSrcId;
    }

    public static AuditSourceIdentificationType createAuditSourceIdentification(String communityId, String communityName) {
        AuditSourceIdentificationType auditSrcId = new AuditSourceIdentificationType();

        // Set the Audit Source Id (community id)
        if (communityId != null) {
            if (communityId.startsWith("urn:oid:")) {
                auditSrcId.setAuditSourceID(communityId.substring(8));
            } else {
                auditSrcId.setAuditSourceID(communityId);
            }
        }

        // If specified, set the Audit Enterprise Site Id (community name)
        if (communityName != null) {
            auditSrcId.setAuditEnterpriseSiteID(communityName);
        }

        return auditSrcId;
    }

    public static ParticipantObjectIdentificationType createParticipantObjectIdentification(String patientId) {
        ParticipantObjectIdentificationType partObjId = new ParticipantObjectIdentificationType();

        // Set the Partipation Object Id (patient id)
        if (patientId != null) {
            partObjId.setParticipantObjectID(patientId);
        }

        // Set the Participation Object Typecode
        partObjId.setParticipantObjectTypeCode(AuditDataTransformConstants.PARTICIPANT_OJB_TYPE_CODE_PERSON);

        // Set the Participation Object Typecode Role
        partObjId.setParticipantObjectTypeCodeRole(AuditDataTransformConstants.PARTICIPANT_OJB_TYPE_CODE_ROLE_PATIENT);

        // Set the Participation Object Id Type code
        CodedValueType partObjIdTypeCode = new CodedValueType();
        partObjIdTypeCode.setCode(AuditDataTransformConstants.PARTICIPANT_OJB_ID_TYPE_CODE_PATIENTNUM);
        partObjId.setParticipantObjectIDTypeCode(partObjIdTypeCode);

        return partObjId;
    }

    public static void logAuditMessage(AuditMessageType message) {
        log.debug("********** Audit Log Message ***********");
        log.debug("EventIdCode: " + message.getEventIdentification().getEventID().getCode());
        log.debug("EventIdCodeSystem: " + message.getEventIdentification().getEventID().getCodeSystem());

        if (message.getAuditSourceIdentification() != null &&
                message.getAuditSourceIdentification().size() > 0 &&
                message.getAuditSourceIdentification().get(0).getAuditSourceID() != null) {
            log.debug("Home Community Id: " + message.getAuditSourceIdentification().get(0).getAuditSourceID());
        } else {
            log.debug("Home Community Id: There was no AuditSourceID in the message");
        }

        if (message.getActiveParticipant() != null &&
                message.getActiveParticipant().size() > 0) {
            if (message.getActiveParticipant().get(0).getUserID() != null) {
                log.debug("UserId: " + message.getActiveParticipant().get(0).getUserID());
            } else {
                log.debug("UserId: There was no User Id in the message");
            }

            if (message.getActiveParticipant().get(0).getUserName() != null) {
                log.debug("UserName: " + message.getActiveParticipant().get(0).getUserName());
            }
        }

        if (message.getParticipantObjectIdentification() != null &&
                message.getParticipantObjectIdentification().size() > 0 &&
                message.getParticipantObjectIdentification().get(0).getParticipantObjectID() != null) {
            log.debug("PatientId: " + message.getParticipantObjectIdentification().get(0).getParticipantObjectID());
        } else {
            log.debug("PatientId: There was no Patient Id in the message");
        }
    }

    /**
     * This method locates the single ExternalIdentifer object based on the given Identification Scheme.
     * 
     * @param olExtId The list of external identifier objects to be searched.
     * @param sIdentScheme The identification scheme for the item being looked for.
     * @return The ExternalIdentifierType matching the search criteria.
     */
    public static ExternalIdentifierType findSingleExternalIdentifier(List<ExternalIdentifierType> olExtId, String sIdentScheme) {
        ExternalIdentifierType oExtId = null;

        if ((olExtId != null) &&
                (olExtId.size() > 0)) {
            for (int i = 0; i < olExtId.size(); i++) {
                if (olExtId.get(i).getIdentificationScheme().equals(sIdentScheme)) {
                    oExtId = olExtId.get(i);
                    break;          // We found what we were looking for - get out of here...
                }
            }
        }
        return oExtId;
    }

    /**
     * This method locates a single External Identifier by its Identification scheme and then extracts the 
     * value from it.
     * 
     * @param olExtId The List of external identifiers to be searched.
     * @param sIdentScheme The identification scheme to look for.
     * @return The value from the specified identification scheme.
     */
    public static String findSingleExternalIdentifierAndExtractValue(List<ExternalIdentifierType> olExtId, String sIdentScheme) {
        String sValue = null;
        ExternalIdentifierType oExtId = findSingleExternalIdentifier(olExtId, sIdentScheme);
        if ((oExtId != null) &&
                (oExtId.getValue() != null)) {
            sValue = oExtId.getValue();
        }
        return sValue;
    }

    /**
     * This method creates a patient id formatted as 'patientid^^^^&communityId&ISO'
     * 
     * @param communityId the communityId for the patient identifier
     * @param patientId the patientId for the patient
     * @return the properly formatted patientId.
     */
    public static String createCompositePatientId(String communityId, String patientId) {
        String sValue = null;
        sValue = patientId + "^^^&" + communityId + "&ISO";
        return sValue;
    }

    /**
     * This method creates a patient id formatted as 'patientid^^^^&communityId&ISO'
     * 
     * @param assertion. Fields of interest are UserInfo.org.homeCommunityId an uniquePatientId
     * @return the properly formatted patientId.
     */
    public static String createCompositePatientIdFromAssertion(UserType userInfo, String patientId) {
        String communityId = null;
        String compPatientId = null;

        if (userInfo != null &&
                userInfo.getOrg() != null &&
                userInfo.getOrg().getHomeCommunityId() != null) {
            communityId = userInfo.getOrg().getHomeCommunityId();
        }
        compPatientId = AuditDataTransformHelper.createCompositePatientId(communityId, patientId);


        return compPatientId;
    }
}
