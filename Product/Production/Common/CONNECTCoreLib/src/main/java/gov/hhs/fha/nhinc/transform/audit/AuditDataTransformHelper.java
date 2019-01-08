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
package gov.hhs.fha.nhinc.transform.audit;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author MFLYNN02
 * @author richard.ettema
 */
public class AuditDataTransformHelper {

    private static final Logger LOG = LoggerFactory.getLogger(AuditDataTransformHelper.class);
    private static String ipAddr = null;

    /**
     * Create the <code>EventIdentificationType</code> for an audit log record.
     *
     * @param actionCode
     * @param eventOutcome
     * @param eventId
     * @return <code>EventIdentificationType</code>
     */
    public static EventIdentificationType createEventIdentification(final String actionCode, final Integer eventOutcome,
        final CodedValueType eventId) {
        final EventIdentificationType eventIdentification = new EventIdentificationType();

        // Set the Event Action Code
        eventIdentification.setEventActionCode(actionCode);

        // Set the Event Action Time
        try {
            final java.util.GregorianCalendar today = new java.util.GregorianCalendar(TimeZone.getTimeZone("GMT"));
            final javax.xml.datatype.DatatypeFactory factory = javax.xml.datatype.DatatypeFactory.newInstance();
            final javax.xml.datatype.XMLGregorianCalendar calendar = factory.newXMLGregorianCalendar(
                today.get(java.util.GregorianCalendar.YEAR), today.get(java.util.GregorianCalendar.MONTH) + 1,
                today.get(java.util.GregorianCalendar.DAY_OF_MONTH),
                today.get(java.util.GregorianCalendar.HOUR_OF_DAY), today.get(java.util.GregorianCalendar.MINUTE),
                today.get(java.util.GregorianCalendar.SECOND), today.get(java.util.GregorianCalendar.MILLISECOND),
                0);
            eventIdentification.setEventDateTime(calendar);
        } catch (DatatypeConfigurationException | ArrayIndexOutOfBoundsException e) {
            LOG.error("Exception when creating XMLGregorian Date message: {}", e.getLocalizedMessage(), e);
        }
        // Set the Event Outcome Indicator
        final BigInteger eventOutcomeBig = new BigInteger(eventOutcome.toString());
        eventIdentification.setEventOutcomeIndicator(eventOutcomeBig);

        // Set the Event Id
        eventIdentification.setEventID(eventId);

        return eventIdentification;
    }

    /**
     * Create the event id <code>CodedValueType</code> for an audit log record.
     *
     * @param eventCode
     * @param eventCodeSys
     * @param eventCodeSysName
     * @param dispName
     * @return <code>CodedValueType</code>
     */
    public static CodedValueType createEventId(final String eventCode, final String eventCodeSys,
        final String eventCodeSysName, final String dispName) {
        final CodedValueType eventId = new CodedValueType();

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

    /**
     * Create the <code>CodedValueType</code> for an audit log record.
     *
     * @param code
     * @param codeSys
     * @param codeSysName
     * @param dispName
     * @return <code>CodedValueType</code>
     */
    public static CodedValueType createCodeValueType(final String code, final String codeSys, final String codeSysName,
        final String dispName) {
        final CodedValueType codeValueType = new CodedValueType();

        // Set the Code
        if (NullChecker.isNotNullish(code)) {
            codeValueType.setCode(code);
        }

        // Set the Codesystem
        if (NullChecker.isNotNullish(codeSys)) {
            codeValueType.setCodeSystem(codeSys);
        }

        // Set the Codesystem Name
        if (NullChecker.isNotNullish(codeSysName)) {
            codeValueType.setCodeSystemName(codeSysName);
        }

        // Set the Display Name
        if (NullChecker.isNotNullish(dispName)) {
            codeValueType.setDisplayName(dispName);
        }

        return codeValueType;
    }

    /**
     * Create the <code>AuditMessageType.ActiveParticipant</code> for an audit log record.
     *
     * @param userInfo
     * @param userIsReq
     * @return <code>AuditMessageType.ActiveParticipant</code>
     */
    public static AuditMessageType.ActiveParticipant createActiveParticipantFromUser(final UserType userInfo,
        final Boolean userIsReq) {
        final AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();

        if (ipAddr == null) {
            try {
                ipAddr = InetAddress.getLocalHost().getHostAddress();
            } catch (final UnknownHostException ex) {
                LOG.error("UnknownHostException thrown getting local host address: {}", ex.getLocalizedMessage(), ex);
                throw new RuntimeException();
            }
        }

        // Set the User Id
        String userId = null;
        if (userInfo != null && userInfo.getUserName() != null && userInfo.getUserName().length() > 0) {
            userId = userInfo.getUserName();
        }

        if (userId != null) {
            participant.setUserID(userId);
        }

        // If specified, set the User Name
        String userName = null;
        if (userInfo != null && userInfo.getPersonName() != null) {
            if (StringUtils.isNotEmpty(userInfo.getPersonName().getGivenName())) {
                userName = userInfo.getPersonName().getGivenName();
            }

            if (StringUtils.isNotEmpty(userInfo.getPersonName().getFamilyName())) {
                if (userName != null) {
                    userName += " " + userInfo.getPersonName().getFamilyName();
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

    /**
     * Create the <code>AuditMessageType.ActiveParticipant</code> for an audit log record.
     *
     * @param userId
     * @param altUserId
     * @param userName
     * @param userIsReq
     * @return <code>AuditMessageType.ActiveParticipant</code>
     */
    public static AuditMessageType.ActiveParticipant createActiveParticipant(final String userId,
        final String altUserId, final String userName, final Boolean userIsReq) {
        final AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();

        if (ipAddr == null) {
            try {
                ipAddr = InetAddress.getLocalHost().getHostAddress();
            } catch (final UnknownHostException ex) {
                LOG.error("UnknownHostException thrown getting local host address: {}", ex.getLocalizedMessage(), ex);
                throw new RuntimeException();
            }
        }

        // Set the User Id
        if (StringUtils.isNotEmpty(userId)) {
            participant.setUserID(userId);
        }

        if (StringUtils.isNotEmpty(altUserId)) {
            participant.setAlternativeUserID(altUserId);
        }

        // If specified, set the User Name
        if (StringUtils.isNotEmpty(userName)) {
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

    /**
     * Create an <code>AuditSourceIdentificationType</code> based on the user info for an audit log record.
     *
     * @param userInfo
     * @return <code>AuditSourceIdentificationType</code>
     */
    public static AuditSourceIdentificationType createAuditSourceIdentificationFromUser(final UserType userInfo) {
        final AuditSourceIdentificationType auditSrcId = new AuditSourceIdentificationType();

        // Home Community ID and name
        String communityId = null;
        String communityName = null;

        if (userInfo != null && userInfo.getOrg() != null) {
            if (userInfo.getOrg().getHomeCommunityId() != null && userInfo.getOrg().getHomeCommunityId().length() > 0) {
                communityId = userInfo.getOrg().getHomeCommunityId();
            }

            if (userInfo.getOrg().getName() != null && userInfo.getOrg().getName().length() > 0) {
                communityName = userInfo.getOrg().getName();
            }
        }

        // Set the Audit Source Id (community id)
        if (communityId != null) {
            LOG.debug("communityId prior to remove urn:oid{}", communityId);
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

    /**
     * Create an <code>AuditSourceIdentificationType</code> based on the community id and name for an audit log record.
     *
     * @param communityId
     * @param communityName
     * @return <code>AuditSourceIdentificationType</code>
     */
    public static AuditSourceIdentificationType createAuditSourceIdentification(final String communityId,
        final String communityName) {
        final AuditSourceIdentificationType auditSrcId = new AuditSourceIdentificationType();

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

    /**
     * Create the <code>ParticipantObjectIdentificationType</code> based on the patient id for an audit log record.
     *
     * @param patientId
     * @return <code>ParticipantObjectIdentificationType</code>
     */
    public static ParticipantObjectIdentificationType createParticipantObjectIdentification(final String patientId) {
        final ParticipantObjectIdentificationType partObjId = new ParticipantObjectIdentificationType();

        // Set the Partipation Object Id (patient id)
        if (patientId != null) {
            partObjId.setParticipantObjectID(patientId);
        }

        // Set the Participation Object Typecode
        partObjId.setParticipantObjectTypeCode(AuditDataTransformConstants.PARTICIPANT_OJB_TYPE_CODE_PERSON);

        // Set the Participation Object Typecode Role
        partObjId.setParticipantObjectTypeCodeRole(AuditDataTransformConstants.PARTICIPANT_OJB_TYPE_CODE_ROLE_PATIENT);

        // Set the Participation Object Id Type code
        final CodedValueType partObjIdTypeCode = new CodedValueType();
        partObjIdTypeCode.setCode(AuditDataTransformConstants.PARTICIPANT_OJB_ID_TYPE_CODE_PATIENTNUM);
        partObjId.setParticipantObjectIDTypeCode(partObjIdTypeCode);

        return partObjId;
    }

    /**
     * Create the <code>ParticipantObjectIdentificationType</code> based on the patient id for an audit log record.
     *
     * @param documentId
     * @return <code>ParticipantObjectIdentificationType</code>
     */
    public static ParticipantObjectIdentificationType createDocumentParticipantObjectIdentification(
        final String documentId) {
        final ParticipantObjectIdentificationType partObjId = new ParticipantObjectIdentificationType();

        // Set the Partipation Object Id (documentId)
        if (documentId != null) {
            partObjId.setParticipantObjectID(documentId);
        }

        // Set the Participation Object Typecode
        partObjId.setParticipantObjectTypeCode(AuditDataTransformConstants.PARTICIPANT_OJB_TYPE_CODE_SYSTEM);

        // Set the Participation Object Typecode Role
        partObjId
        .setParticipantObjectTypeCodeRole(AuditDataTransformConstants.PARTICIPANT_OJB_TYPE_CODE_ROLE_DATA_REPO);

        // Set the Participation Object Id Type code
        final CodedValueType partObjIdTypeCode = new CodedValueType();
        partObjIdTypeCode.setCode(AuditDataTransformConstants.PARTICIPANT_OJB_ID_TYPE_CODE_REPORTNUM);
        partObjId.setParticipantObjectIDTypeCode(partObjIdTypeCode);

        return partObjId;
    }

    /**
     * Write out debug logging statements based on the given <code>AuditMessageType</code> message.
     *
     * @param message
     */
    public static void logAuditMessage(final AuditMessageType message) {
        LOG.debug("********** Audit Log Message ***********");
        LOG.debug("EventIdCode: {}", message.getEventIdentification().getEventID().getCode());
        LOG.debug("EventIdCodeSystem: {}", message.getEventIdentification().getEventID().getCodeSystem());

        if (CollectionUtils.isNotEmpty(message.getAuditSourceIdentification())
            && message.getAuditSourceIdentification().get(0).getAuditSourceID() != null) {
            LOG.debug("Home Community Id: {}", message.getAuditSourceIdentification().get(0).getAuditSourceID());
        } else {
            LOG.debug("Home Community Id: There was no AuditSourceID in the message");
        }

        if (CollectionUtils.isNotEmpty(message.getActiveParticipant())) {
            if (message.getActiveParticipant().get(0).getUserID() != null) {
                LOG.debug("UserId: {}", message.getActiveParticipant().get(0).getUserID());
            } else {
                LOG.debug("UserId: There was no User Id in the message");
            }

            if (message.getActiveParticipant().get(0).getUserName() != null) {
                LOG.debug("UserName: {}", message.getActiveParticipant().get(0).getUserName());
            }
        }

        if (CollectionUtils.isNotEmpty(message.getParticipantObjectIdentification())
            && message.getParticipantObjectIdentification().get(0).getParticipantObjectID() != null) {
            LOG.debug("PatientId: {}", message.getParticipantObjectIdentification().get(0).getParticipantObjectID());
        } else {
            LOG.debug("PatientId: There was no Patient Id in the message");
        }
    }

    /**
     * This method locates the single ExternalIdentifer object based on the given Identification Scheme.
     *
     * @param olExtId The list of external identifier objects to be searched.
     * @param sIdentScheme The identification scheme for the item being looked for.
     * @return The <code>ExternalIdentifierType</code> matching the search criteria.
     */
    public static ExternalIdentifierType findSingleExternalIdentifier(final List<ExternalIdentifierType> olExtId,
        final String sIdentScheme) {
        ExternalIdentifierType oExtId = null;

        if (CollectionUtils.isNotEmpty(olExtId)) {
            for (int i = 0; i < olExtId.size(); i++) {
                if (olExtId.get(i).getIdentificationScheme().equals(sIdentScheme)) {
                    oExtId = olExtId.get(i);
                    break; // We found what we were looking for - get out of here...
                }
            }
        }
        return oExtId;
    }

    /**
     * This method locates a single External Identifier by its Identification scheme and then extracts the value from
     * it.
     *
     * @param olExtId The List of external identifiers to be searched.
     * @param sIdentScheme The identification scheme to look for.
     * @return The value from the specified identification scheme.
     */
    public static String findSingleExternalIdentifierAndExtractValue(final List<ExternalIdentifierType> olExtId,
        final String sIdentScheme) {
        String sValue = null;
        final ExternalIdentifierType oExtId = findSingleExternalIdentifier(olExtId, sIdentScheme);
        if (oExtId != null && oExtId.getValue() != null) {
            sValue = oExtId.getValue();
        }
        return sValue;
    }

    /**
     * This method creates a patient id formatted as 'patientid^^^^&assigningAuthId&ISO'
     *
     * @param assigningAuthId the assigningAuthId for the patient identifier
     * @param patientId the patientId for the patient
     * @return the properly formatted patientId.
     */
    public static String createCompositePatientId(final String assigningAuthId, final String patientId) {
        String sValue;
        sValue = patientId + "^^^&" + assigningAuthId + "&ISO";
        return sValue;
    }

    /**
     * This method creates a patient id formatted as 'patientid^^^^&communityId&ISO'
     *
     * @param userInfo
     * @param patientId
     * @return the properly formatted patientId.
     */
    public static String createCompositePatientIdFromAssertion(final UserType userInfo, final String patientId) {
        String communityId = null;
        String compPatientId;

        if (userInfo != null && userInfo.getOrg() != null && userInfo.getOrg().getHomeCommunityId() != null) {
            communityId = userInfo.getOrg().getHomeCommunityId();
        }
        compPatientId = AuditDataTransformHelper.createCompositePatientId(communityId, patientId);

        return compPatientId;
    }
}
