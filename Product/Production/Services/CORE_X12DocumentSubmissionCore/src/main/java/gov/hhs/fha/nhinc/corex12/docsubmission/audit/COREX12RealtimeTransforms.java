/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.corex12.docsubmission.audit;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import com.services.nhinc.schema.auditmessage.TypeValuePairType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.audit.AuditDataTransformConstants;
import gov.hhs.fha.nhinc.transform.audit.AuditDataTransformHelper;
import gov.hhs.fha.nhinc.transform.audit.XDRTransforms;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.io.ByteArrayOutputStream;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.log4j.Logger;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;

/**
 * Class provides services to Construct Audit Messages from the X12 Realtime request and response
 *
 * @author svalluripalli
 */
public class COREX12RealtimeTransforms {

    private static final Logger LOG = Logger.getLogger(COREX12RealtimeTransforms.class);
    private final XDRTransforms oXDR = new XDRTransforms();

    /**
     * Transforms the Realtime Request message to Audit logging specific message.
     *
     * @param request
     * @param assertion
     * @param target
     * @param direction
     * @param _interface
     * @param isRequesting
     * @return LogEventRequestType
     */
    public LogEventRequestType transformRequestToAuditMsg(COREEnvelopeRealTimeRequest request, AssertionType assertion, NhinTargetSystemType target, String direction, String _interface, boolean isRequesting) {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;
        
        LOG.trace("Begin COREX12RealtimeTransforms -> transformRequestToAuditMsg() -- NHIN");
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
        CodedValueType eventID = null;
        if (isRequesting) {
            eventID = AuditDataTransformHelper.createCodeValueType(CoreX12AuditDataTransformConstants.EVENT_ID_CODE_X12, null,
                AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, CoreX12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12_EXPORT);
        } else {
            eventID = AuditDataTransformHelper.createCodeValueType(CoreX12AuditDataTransformConstants.EVENT_ID_CODE_X12, null,
                AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, CoreX12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12_IMPORT);
        }

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID, isRequesting);
        oEventIdentificationType.getEventTypeCode().add(AuditDataTransformHelper.createCodeValueType(CoreX12AuditDataTransformConstants.EVENT_ID_CODE_SYS_CODE_X12, null,
            CoreX12AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_X12, CoreX12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12REALTIME));
        auditMsg.setEventIdentification(oEventIdentificationType);
        //ActiveParticipant for Human Requester entry only for the requesting gateway
        if (isRequesting) {
            AuditMessageType.ActiveParticipant participantHumanFactor = getActiveParticipant(assertion.getUserInfo());
            auditMsg.getActiveParticipant().add(participantHumanFactor);
        }
        AuditMessageType.ActiveParticipant participantSource = getActiveParticipantSource(isRequesting);
        AuditMessageType.ActiveParticipant participantDestination = getActiveParticipantDestination(target);
        auditMsg.getActiveParticipant().add(participantSource);
        auditMsg.getActiveParticipant().add(participantDestination);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType(request);
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        //find the requesting or responding gateway
        String messageDirection = isRequesting ? NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION : NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;

        /* Create the AuditSourceIdentifierType object */
        String communityId = oXDR.getMessageCommunityIdFromRequest(assertion, target, messageDirection, _interface);
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType();
        //This  will be used by persistence layer, needs to be changed in the future
        AuditSourceIdentificationType auditSourceLocal = AuditDataTransformHelper.createAuditSourceIdentification(communityId, "INTERNAL");
        auditMsg.getAuditSourceIdentification().add(auditSourceLocal);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        LOG.trace("end transformRequestToAuditMsg() -- NHIN");
        return result;
    }

    /**
     * Transforms the Realtime Response message to Audit logging specific message.
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
        LOG.trace("Begin transformResponseToAuditMsg() -- NHIN");
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
        CodedValueType eventID = null;
        if (isRequesting) {
            eventID = AuditDataTransformHelper.createCodeValueType(CoreX12AuditDataTransformConstants.EVENT_ID_CODE_X12, null,
                AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, CoreX12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12_EXPORT);
        } else {
            eventID = AuditDataTransformHelper.createCodeValueType(CoreX12AuditDataTransformConstants.EVENT_ID_CODE_X12, null,
                AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, CoreX12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12_IMPORT);
        }

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID, isRequesting);
        oEventIdentificationType.getEventTypeCode().add(AuditDataTransformHelper.createCodeValueType(CoreX12AuditDataTransformConstants.EVENT_ID_CODE_SYS_CODE_X12, null,
            CoreX12AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_X12, CoreX12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12REALTIME));
        auditMsg.setEventIdentification(oEventIdentificationType);

        //ActiveParticipant for Human Requester entry only for the requesting gateway
        if (isRequesting) {
            AuditMessageType.ActiveParticipant participantHumanFactor = getActiveParticipant(assertion.getUserInfo());
            auditMsg.getActiveParticipant().add(participantHumanFactor);
        }
        AuditMessageType.ActiveParticipant participantSource = getActiveParticipantSource(isRequesting);
        AuditMessageType.ActiveParticipant participantDestination = getActiveParticipantDestination(target);
        auditMsg.getActiveParticipant().add(participantSource);
        auditMsg.getActiveParticipant().add(participantDestination);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType(response);
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        //find the requesting or responding gateway
        String messageDirection = isRequesting ? NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION : NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        /* Create the AuditSourceIdentifierType object */
        String communityId = oXDR.getMessageCommunityIdFromRequest(assertion, target, messageDirection, _interface);
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType();
        //This  will be used by persistence layer, needs to be changed in the future
        AuditSourceIdentificationType auditSourceLocal = AuditDataTransformHelper.createAuditSourceIdentification(communityId, "INTERNAL");
        auditMsg.getAuditSourceIdentification().add(auditSourceLocal);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        LOG.trace("end transformResponseToAuditMsg() -- NHIN");

        return result;
    }

    private boolean areRequiredCOREX12RealtimeFiledsNull(COREEnvelopeRealTimeResponse msg, AssertionType assertion) {
        if (oXDR.areRequiredUserTypeFieldsNull(assertion)) {
            LOG.error("One of more UserInfo fields from the Assertion object were null.");
            return true;
        }

        if (areRequiredRequestFieldsNull(msg)) {
            LOG.error("One or more fields in CORE X12 Realtime Request object were null.");
            return true;
        }
        return false;
    }

    private boolean areRequiredCOREX12RealtimeFiledsNull(COREEnvelopeRealTimeRequest msg, AssertionType assertion) {
        if (oXDR.areRequiredUserTypeFieldsNull(assertion)) {
            LOG.error("One of more UserInfo fields from the Assertion object were null.");
            return true;
        }

        if (areRequiredRequestFieldsNull(msg)) {
            LOG.error("One or more fields in CORE X12 Realtime Request object were null.");
            return true;
        }
        return false;
    }

    private boolean areRequiredRequestFieldsNull(COREEnvelopeRealTimeRequest msg) {
        if (NullChecker.isNullish(msg.getPayloadType())) {
            LOG.error("CORE X12 Realtime PayloadType is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getProcessingMode())) {
            LOG.error("CORE X12 Realtime ProcessingMode is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getPayloadID())) {
            LOG.error("CORE X12 Realtime PayloadID is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getTimeStamp())) {
            LOG.error("CORE X12 Realtime TimeStamp is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getSenderID())) {
            LOG.error("CORE X12 Realtime SenderID is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getReceiverID())) {
            LOG.error("CORE X12 Realtime ReceiverID is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getCORERuleVersion())) {
            LOG.error("CORE X12 Realtime CORERuleVersion is empty...");
            return true;
        }
        return false;
    }

    private boolean areRequiredRequestFieldsNull(COREEnvelopeRealTimeResponse msg) {

        if (NullChecker.isNullish(msg.getPayloadType())) {
            LOG.error("CORE X12 Realtime PayloadType is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getProcessingMode())) {
            LOG.error("CORE X12 Realtime ProcessingMode is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getPayloadID())) {
            LOG.error("CORE X12 Realtime PayloadID is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getTimeStamp())) {
            LOG.error("CORE X12 Realtime TimeStamp is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getSenderID())) {
            LOG.error("CORE X12 Realtime SenderID is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getReceiverID())) {
            LOG.error("CORE X12 Realtime ReceiverID is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getCORERuleVersion())) {
            LOG.error("CORE X12 Realtime CORERuleVersion is empty...");
            return true;
        }
        return false;
    }

    protected EventIdentificationType getEventIdentificationType(CodedValueType eventID, boolean isRequesting) {
        EventIdentificationType oEventIdentificationType = AuditDataTransformHelper.createEventIdentification(
            isRequesting ? AuditDataTransformConstants.EVENT_ACTION_CODE_READ : AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE,
            AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID);
        return oEventIdentificationType;
    }

    protected AuditMessageType.ActiveParticipant getActiveParticipant(UserType oUserInfo) {
        // Create Active Participant Section
        // create a method to call the AuditDataTransformHelper - one expectation
        AuditMessageType.ActiveParticipant participant = createActiveParticipantFromUser(
            oUserInfo, Boolean.TRUE);
        if (null != oUserInfo.getRoleCoded()) {
            participant.getRoleIDCode().add(AuditDataTransformHelper.createCodeValueType(oUserInfo.getRoleCoded().getCode(), "",
                oUserInfo.getRoleCoded().getCodeSystemName(), oUserInfo.getRoleCoded().getDisplayName()));
        }
        return participant;
    }

    /**
     * Create the <code>AuditMessageType.ActiveParticipant</code> for an audit log record.
     *
     * @param userInfo
     * @param userIsReq
     * @return <code>AuditMessageType.ActiveParticipant</code>
     */
    private static AuditMessageType.ActiveParticipant createActiveParticipantFromUser(UserType userInfo,
        Boolean userIsReq) {
        String ipAddr = null;
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();

        if (ipAddr == null) {
            try {
                ipAddr = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException ex) {
                LOG.error("UnknownHostException thrown getting local host address.", ex);
                return null;
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
            if (userInfo.getPersonName().getGivenName() != null && userInfo.getPersonName().getGivenName().length() > 0) {
                userName = userInfo.getPersonName().getGivenName();
            }

            if (userInfo.getPersonName().getFamilyName() != null
                && userInfo.getPersonName().getFamilyName().length() > 0) {
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
        return participant;
    }

    protected AuditMessageType.ActiveParticipant getActiveParticipantSource(boolean isRequesting) {
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        participant.setUserID(CoreX12AuditDataTransformConstants.ACTIVE_PARTICPANT_USER_ID_SOURCE);
        participant.setAlternativeUserID(ManagementFactory.getRuntimeMXBean().getName());
        try {
            String ipAddr = InetAddress.getLocalHost().getHostAddress();
            participant.setNetworkAccessPointID(ipAddr);
            participant.setNetworkAccessPointTypeCode(AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_IP);
        } catch (UnknownHostException ex) {
            LOG.warn("UnknownHostException thrown getting local host address.", ex);
            return null;
        }

        participant.getRoleIDCode().add(AuditDataTransformHelper.createCodeValueType(CoreX12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_CDE, null,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, CoreX12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_SOURCE_DISPLAY_NAME));
        participant.setUserIsRequestor(isRequesting ? Boolean.TRUE : Boolean.FALSE);
        return participant;
    }

    protected AuditMessageType.ActiveParticipant getActiveParticipantDestination(NhinTargetSystemType target) {
        String strUrl = null;
        String strHost = null;
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        if (null != target) {
            if (NullChecker.isNotNullish(target.getUrl())) {
                strUrl = target.getUrl();
            } else {
                ConnectionManagerCache cm = ConnectionManagerCache.getInstance();
                try {
                    strUrl = cm.getEndpointURLFromNhinTarget(target, NhincConstants.NHIN_CORE_X12DS_REALTIME_SECURED_SERVICE_NAME);
                } catch (ConnectionManagerException ex) {
                    LOG.error(ex);
                }
            }
        }
        
        if (null != strUrl) {
            try {
                URL url = new URL(strUrl);
                participant.setUserID(strUrl);
                strHost = url.getHost();
                participant.setNetworkAccessPointID(strHost);
                if (Character.isDigit(strHost.charAt(0))) {
                    participant.setNetworkAccessPointTypeCode(AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_IP);
                } else {
                    participant.setNetworkAccessPointTypeCode(CoreX12AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME);
                }
            } catch (MalformedURLException ex) {
                LOG.error(ex);
            }
        }
        else{
            //for now set the user id to anonymouns
            participant.setUserID(CoreX12AuditDataTransformConstants.ACTIVE_PARTICPANT_USER_ID_SOURCE);
            //for now hardcode the value to localhost, need to find out if this needs to be set
            participant.setNetworkAccessPointTypeCode(CoreX12AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME);
            participant.setNetworkAccessPointID("localhost");
        }
        participant.setUserIsRequestor(Boolean.FALSE);
        participant.getRoleIDCode().add(AuditDataTransformHelper.createCodeValueType(CoreX12AuditDataTransformConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DEST, null,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, CoreX12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME));
        return participant;
    }

    protected ParticipantObjectIdentificationType getParticipantObjectIdentificationType(Object msg) {
        String payloadId = null;
        byte[] byteArray = null;

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = new ParticipantObjectIdentificationType();
        // Set the Partipation Object Id (patient id)
        if (msg != null) {
            if (msg instanceof COREEnvelopeRealTimeResponse) {
                payloadId = ((COREEnvelopeRealTimeResponse) msg).getPayloadID();
            } else if (msg instanceof COREEnvelopeRealTimeRequest) {
                payloadId = ((COREEnvelopeRealTimeRequest) msg).getPayloadID();
            } else if (msg instanceof COREEnvelopeBatchSubmission) {
                payloadId = ((COREEnvelopeBatchSubmission) msg).getPayloadID();
            } else if (msg instanceof COREEnvelopeBatchSubmissionResponse) {
                payloadId = ((COREEnvelopeBatchSubmissionResponse) msg).getPayloadID();
            }
            participantObject.setParticipantObjectID(payloadId);
        } else {
            return participantObject;
        }
        byteArray = marshallToByteArray(msg);
        TypeValuePairType oType = new TypeValuePairType();
        oType.setValue(byteArray);
        participantObject.getParticipantObjectDetail().add(oType);

        // Set the Participation Object Typecode
        participantObject.setParticipantObjectTypeCode(AuditDataTransformConstants.PARTICIPANT_OJB_TYPE_CODE_SYSTEM);

        // Set the Participation Object Typecode Role
        participantObject.setParticipantObjectTypeCodeRole(CoreX12AuditDataTransformConstants.PARTICIPANT_OJB_TYPE_CODE_ROLE_X12);

        // Set the Participation Object Id Type code
        CodedValueType partObjIdTypeCode = new CodedValueType();
        partObjIdTypeCode.setCode(payloadId);
        partObjIdTypeCode.setCodeSystemName(CoreX12AuditDataTransformConstants.CAQH_x12_CONNECTIVITY_CODED_SYS_NAME);
        partObjIdTypeCode.setDisplayName(CoreX12AuditDataTransformConstants.CAQH_x12_CONNECTIVITY_CODED_SYS_DISPLAY_NAME);
        participantObject.setParticipantObjectIDTypeCode(partObjIdTypeCode);

        return participantObject;
    }

    private static byte[] marshallToByteArray(Object msg) {
        byte[] bObject = null;
        COREEnvelopeRealTimeRequest oRequestNoPayload = null;
        COREEnvelopeRealTimeResponse oResponseNoPayload = null;
        COREEnvelopeBatchSubmission oBatchRequestNoPayload = null;
        COREEnvelopeBatchSubmissionResponse oBatchResponseNoPayload = null;
        javax.xml.namespace.QName xmlqname = null;
        JAXBContextHandler oHandler = new JAXBContextHandler();
        try {
            JAXBContext jc = oHandler.getJAXBContext(CoreX12AuditDataTransformConstants.CORE_X12_JAXB_CONTEXT);
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            if (msg instanceof COREEnvelopeRealTimeRequest) {
                oRequestNoPayload = (COREEnvelopeRealTimeRequest) msg;
                oRequestNoPayload.setPayload("");
                xmlqname = new javax.xml.namespace.QName(CoreX12AuditDataTransformConstants.CORE_X12_NAMESPACE_URI, CoreX12AuditDataTransformConstants.CORE_X12_REQUEST_LOCALPART);
                JAXBElement<org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest> element = new JAXBElement<org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest>(xmlqname, COREEnvelopeRealTimeRequest.class, oRequestNoPayload);
                marshaller.marshal(element, baOutStrm);
                bObject = baOutStrm.toByteArray();
            }

            if (msg instanceof COREEnvelopeRealTimeResponse) {
                oResponseNoPayload = (COREEnvelopeRealTimeResponse) msg;
                oResponseNoPayload.setPayload("");
                xmlqname = new javax.xml.namespace.QName(CoreX12AuditDataTransformConstants.CORE_X12_NAMESPACE_URI, CoreX12AuditDataTransformConstants.CORE_X12_RESPONSE_LOCALPART);
                JAXBElement<org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse> element = new JAXBElement<org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse>(xmlqname, COREEnvelopeRealTimeResponse.class, oResponseNoPayload);
                marshaller.marshal(element, baOutStrm);
                bObject = baOutStrm.toByteArray();
            }

            if (msg instanceof COREEnvelopeBatchSubmission) {
                oBatchRequestNoPayload = (COREEnvelopeBatchSubmission) msg;
                oBatchRequestNoPayload.setPayloadID("");
                xmlqname = new javax.xml.namespace.QName(CoreX12AuditDataTransformConstants.CORE_X12_NAMESPACE_URI, CoreX12AuditDataTransformConstants.CORE_X12_BATCH_REQUEST_LOCALPART);
                JAXBElement<org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission> element = new JAXBElement<org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission>(xmlqname, COREEnvelopeBatchSubmission.class, oBatchRequestNoPayload);
                marshaller.marshal(element, baOutStrm);
                bObject = baOutStrm.toByteArray();
            }

            if (msg instanceof COREEnvelopeBatchSubmissionResponse) {
                oBatchResponseNoPayload = (COREEnvelopeBatchSubmissionResponse) msg;
                oBatchResponseNoPayload.setPayloadID("");
                xmlqname = new javax.xml.namespace.QName(CoreX12AuditDataTransformConstants.CORE_X12_NAMESPACE_URI, CoreX12AuditDataTransformConstants.CORE_X12_BATCH_RESPONSE_LOCALPART);
                JAXBElement<org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse> element = new JAXBElement<org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse>(xmlqname, COREEnvelopeBatchSubmissionResponse.class, oBatchResponseNoPayload);
                marshaller.marshal(element, baOutStrm);
                bObject = baOutStrm.toByteArray();
            }
        } catch (JAXBException ex) {
            throw new RuntimeException(ex.getCause());
        }
        return bObject;
    }

    protected AuditSourceIdentificationType getAuditSourceIdentificationType() {
        String hcid = HomeCommunityMap.getLocalHomeCommunityId();
        String homeCommunityName = HomeCommunityMap.getHomeCommunityName(hcid);
        return AuditDataTransformHelper.createAuditSourceIdentification(hcid, homeCommunityName);
    }
}
