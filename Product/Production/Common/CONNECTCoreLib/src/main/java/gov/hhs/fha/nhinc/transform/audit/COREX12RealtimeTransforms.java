/*
 * Copyright (c) 2014, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import org.apache.log4j.Logger;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;

/**
 *
 * @author svalluripalli
 */
public class COREX12RealtimeTransforms {

    private static final Logger LOG = Logger.getLogger(COREX12RealtimeTransforms.class);
    private XDRTransforms oXDR = new XDRTransforms();

    /**
     *
     * @param msg
     * @param assertion
     * @param target
     * @param direction
     * @param _interface
     * @return LogEventRequestType
     */
    public LogEventRequestType transformRequestToAuditMsg(COREEnvelopeRealTimeRequest request, AssertionType assertion, NhinTargetSystemType target, String direction, String _interface) {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        LOG.debug("Begin COREX12RealtimeTransforms -> transformRequestToAuditMsg() -- NHIN");
        if (request == null) {
            LOG.error("Requst Object was null");
            return null;
        }

        if (assertion == null) {
            LOG.error("Assertion was null");
            return null;
        }

        // check to see that the required fields are not null
        boolean missingReqFields = areRequiredCOREX12RealtimeFiledsNull(request, assertion);
        if (missingReqFields) {
            LOG.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();
        auditMsg = new AuditMessageType();

        // Create EventIdentification
        CodedValueType eventID = getCodedValueTypeForXDRResponse();

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        auditMsg.setEventIdentification(oEventIdentificationType);

        AuditMessageType.ActiveParticipant participant = getActiveParticipant(assertion.getUserInfo());
        auditMsg.getActiveParticipant().add(participant);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType(request.getPayloadID());
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        /* Create the AuditSourceIdentifierType object */
        String communityId = oXDR.getMessageCommunityIdFromRequest(assertion, target, direction, _interface);
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(communityId);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        LOG.debug("end transformRequestToAuditMsg() -- NHIN");
        return result;
    }

    /**
     * 
     * @param response
     * @param assertion
     * @param target
     * @param direction
     * @param _interface
     * @param isRequesting
     * @return LogEventRequestType
     */
    public LogEventRequestType transformResponseToAuditMsg(COREEnvelopeRealTimeResponse response, AssertionType assertion,
            NhinTargetSystemType target, String direction, String _interface, boolean isRequesting) {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        if (response == null) {
            LOG.error("Response Object was null");
            return null;
        }

        if (assertion == null) {
            LOG.error("Assertion was null");
            return null;
        }

        // check to see that the required fields are not null
        boolean missingReqFields = areRequiredCOREX12RealtimeFiledsNull(response, assertion);
        if (missingReqFields) {
            LOG.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();
        auditMsg = new AuditMessageType();

        // Create EventIdentification
        CodedValueType eventID = getCodedValueTypeForXDRResponse();
        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        auditMsg.setEventIdentification(oEventIdentificationType);

        AuditMessageType.ActiveParticipant participant = getActiveParticipant(assertion.getUserInfo());
        auditMsg.getActiveParticipant().add(participant);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType(response.getPayloadID());
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        /* Create the AuditSourceIdentifierType object */
        String communityId = oXDR.getMessageCommunityIdFromRequest(assertion, target, direction, _interface);
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(communityId);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        LOG.debug("end transformResponseToAuditMsg() -- NHIN");

        return result;
    }

    private boolean areRequiredCOREX12RealtimeFiledsNull(COREEnvelopeRealTimeResponse msg, AssertionType assertion) {
        try {

            if (assertion == null) {
                LOG.error("Assertion object is null");
                return true;
            }
            if (msg == null) {
                LOG.error("COREEnvelopeRealTimeRequest object is null");
                return true;
            }

            if (oXDR.areRequiredUserTypeFieldsNull(assertion)) {
                LOG.error("One of more UserInfo fields from the Assertion object were null.");
                return true;
            }

            if (areRequiredRequestFieldsNull(msg)) {
                LOG.error("One or more fields in CORE X12 Realtime Request object were null.");
                return true;
            }


        } catch (Exception exp) {
            LOG.error("Encountered Error: " + exp.getMessage());
            return true;
        }
        return false;
    }

    private boolean areRequiredCOREX12RealtimeFiledsNull(COREEnvelopeRealTimeRequest msg, AssertionType assertion) {
        try {

            if (assertion == null) {
                LOG.error("Assertion object is null");
                return true;
            }
            if (msg == null) {
                LOG.error("COREEnvelopeRealTimeRequest object is null");
                return true;
            }

            if (oXDR.areRequiredUserTypeFieldsNull(assertion)) {
                LOG.error("One of more UserInfo fields from the Assertion object were null.");
                return true;
            }

            if (areRequiredRequestFieldsNull(msg)) {
                LOG.error("One or more fields in CORE X12 Realtime Request object were null.");
                return true;
            }

        } catch (Exception exp) {
            LOG.error("Encountered Error: " + exp.getMessage());
            return true;
        }
        return false;
    }

    private boolean areRequiredRequestFieldsNull(COREEnvelopeRealTimeRequest msg) {
        if (msg.getPayloadType() == null || msg.getPayloadType().length() <= 0) {
            LOG.error("CORE X12 Realtime PayloadType is empty...");
            return true;
        }

        if (msg.getProcessingMode() == null || msg.getProcessingMode().length() <= 0) {
            LOG.error("CORE X12 Realtime ProcessingMode is empty...");
            return true;
        }

        if (msg.getPayloadID() == null || msg.getPayloadID().length() <= 0) {
            LOG.error("CORE X12 Realtime PayloadID is empty...");
            return true;
        }

        if (msg.getTimeStamp() == null || msg.getTimeStamp().length() <= 0) {
            LOG.error("CORE X12 Realtime TimeStamp is empty...");
            return true;
        }

        if (msg.getSenderID() == null || msg.getSenderID().length() <= 0) {
            LOG.error("CORE X12 Realtime SenderID is empty...");
            return true;
        }

        if (msg.getReceiverID() == null || msg.getReceiverID().length() <= 0) {
            LOG.error("CORE X12 Realtime ReceiverID is empty...");
            return true;
        }

        if (msg.getCORERuleVersion() == null || msg.getCORERuleVersion().length() <= 0) {
            LOG.error("CORE X12 Realtime CORERuleVersion is empty...");
            return true;
        }
        return false;
    }

    private boolean areRequiredRequestFieldsNull(COREEnvelopeRealTimeResponse msg) {
        if (msg.getPayloadType() == null || msg.getPayloadType().length() <= 0) {
            LOG.error("CORE X12 Realtime PayloadType is empty...");
            return true;
        }

        if (msg.getProcessingMode() == null || msg.getProcessingMode().length() <= 0) {
            LOG.error("CORE X12 Realtime ProcessingMode is empty...");
            return true;
        }

        if (msg.getPayloadID() == null || msg.getPayloadID().length() <= 0) {
            LOG.error("CORE X12 Realtime PayloadID is empty...");
            return true;
        }

        if (msg.getTimeStamp() == null || msg.getTimeStamp().length() <= 0) {
            LOG.error("CORE X12 Realtime TimeStamp is empty...");
            return true;
        }

        if (msg.getSenderID() == null || msg.getSenderID().length() <= 0) {
            LOG.error("CORE X12 Realtime SenderID is empty...");
            return true;
        }

        if (msg.getReceiverID() == null || msg.getReceiverID().length() <= 0) {
            LOG.error("CORE X12 Realtime ReceiverID is empty...");
            return true;
        }

        if (msg.getCORERuleVersion() == null || msg.getCORERuleVersion().length() <= 0) {
            LOG.error("CORE X12 Realtime CORERuleVersion is empty...");
            return true;
        }
        return false;
    }

    private CodedValueType getCodedValueTypeForXDRResponse() {
        // Create EventIdentification
        CodedValueType eventID = AuditDataTransformHelper.createEventId(
                AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_X12,
                AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12REALTIME,
                AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_X12,
                AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12REALTIME);
        return eventID;
    }

    private EventIdentificationType getEventIdentificationType(CodedValueType eventID) {
        EventIdentificationType oEventIdentificationType = AuditDataTransformHelper.createEventIdentification(
                AuditDataTransformConstants.EVENT_ACTION_CODE_READ,
                AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID);
        return oEventIdentificationType;
    }

    private AuditMessageType.ActiveParticipant getActiveParticipant(UserType oUserInfo) {
        // Create Active Participant Section
        // create a method to call the AuditDataTransformHelper - one expectation
        AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(
                oUserInfo, true);
        return participant;
    }

    private ParticipantObjectIdentificationType getParticipantObjectIdentificationType(String sPatientId) {
        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = AuditDataTransformHelper
                .createParticipantObjectIdentification(sPatientId);
        return participantObject;
    }

    private AuditSourceIdentificationType getAuditSourceIdentificationType(String communityId) {
        return AuditDataTransformHelper.createAuditSourceIdentification(communityId, communityId);
    }
}
