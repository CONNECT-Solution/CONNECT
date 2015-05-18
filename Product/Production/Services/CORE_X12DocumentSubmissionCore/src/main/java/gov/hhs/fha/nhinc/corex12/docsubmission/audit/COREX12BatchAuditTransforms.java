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
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.audit.AuditDataTransformConstants;
import gov.hhs.fha.nhinc.transform.audit.AuditDataTransformHelper;
import gov.hhs.fha.nhinc.transform.audit.XDRTransforms;
import org.apache.log4j.Logger;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;

/**
 * Class provides services to Construct Audit Messages from the X12 Batch request and response
 *
 * @author svalluripalli
 */
public class COREX12BatchAuditTransforms {

    private static final Logger LOG = Logger.getLogger(COREX12BatchAuditTransforms.class);
    private final XDRTransforms oXDR = new XDRTransforms();
    private final COREX12RealtimeTransforms oCoreRealTimeTransforms = new COREX12RealtimeTransforms();

    /**
     * Transforms the Batch Request message to Audit logging specific message.
     *
     * @param request
     * @param assertion
     * @param target
     * @param direction
     * @param _interface
     * @return LogEventRequestType
     */
    public LogEventRequestType transformBatchRequestToAuditMsg(COREEnvelopeBatchSubmission request, AssertionType assertion, NhinTargetSystemType target, String direction, String _interface, boolean isRequesting) {
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
        boolean missingReqFields = areRequiredCOREX12BatchFiledsNull(request, assertion);
        if (missingReqFields) {
            LOG.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();
        auditMsg = new AuditMessageType();
        // Create EventIdentification
        CodedValueType eventID = AuditDataTransformHelper.createCodeValueType(CoreX12AuditDataTransformConstants.EVENT_ID_CODE_X12, null,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, CoreX12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12_EXPORT);

        EventIdentificationType oEventIdentificationType = oCoreRealTimeTransforms.getEventIdentificationType(eventID, isRequesting);
        oEventIdentificationType.getEventTypeCode().add(AuditDataTransformHelper.createCodeValueType(CoreX12AuditDataTransformConstants.EVENT_ID_CODE_SYS_CODE_X12, null,
            CoreX12AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_X12, CoreX12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12REALTIME));
        auditMsg.setEventIdentification(oEventIdentificationType);

        AuditMessageType.ActiveParticipant participantHumanFactor = oCoreRealTimeTransforms.getActiveParticipant(assertion.getUserInfo());
        AuditMessageType.ActiveParticipant participantSource = oCoreRealTimeTransforms.getActiveParticipantSource(isRequesting);
        AuditMessageType.ActiveParticipant participantDestination = oCoreRealTimeTransforms.getActiveParticipantDestination(target);
        auditMsg.getActiveParticipant().add(participantHumanFactor);
        auditMsg.getActiveParticipant().add(participantSource);
        auditMsg.getActiveParticipant().add(participantDestination);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = oCoreRealTimeTransforms.getParticipantObjectIdentificationType(request);
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        //find the requesting or responding gateway
        String messageDirection = isRequesting ? NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION : NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;


        /* Create the AuditSourceIdentifierType object */
        String communityId = oXDR.getMessageCommunityIdFromRequest(assertion, target, direction, _interface);
        AuditSourceIdentificationType auditSource = oCoreRealTimeTransforms.getAuditSourceIdentificationType();
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);
        LOG.trace("End COREX12RealtimeTransforms -> transformRequestToAuditMsg() -- NHIN");
        return result;
    }

    /**
     * Transforms the Batch Response message to Audit logging specific message.
     *
     * @param response
     * @param assertion
     * @param target
     * @param direction
     * @param _interface
     * @param isRequesting
     * @return LogEventRequestType
     */
    public LogEventRequestType transformBatchResponseToAuditMsg(COREEnvelopeBatchSubmissionResponse response, AssertionType assertion,
        NhinTargetSystemType target, String direction, String _interface, boolean isRequesting) {
        LOG.trace("Begin COREX12RealtimeTransforms -> transformBatchResponseToAuditMsg() -- NHIN");
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;
        if (response == null) {
            LOG.error("response Object was null");
            return null;
        }

        if (assertion == null) {
            LOG.error("Assertion was null");
            return null;
        }

        // check to see that the required fields are not null
        boolean missingReqFields = areRequiredCOREX12BatchFiledsNull(response, assertion);
        if (missingReqFields) {
            LOG.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();
        auditMsg = new AuditMessageType();
        // Create EventIdentification
        CodedValueType eventID = AuditDataTransformHelper.createCodeValueType(CoreX12AuditDataTransformConstants.EVENT_ID_CODE_X12, null,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, CoreX12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12_EXPORT);
        EventIdentificationType oEventIdentificationType = oCoreRealTimeTransforms.getEventIdentificationType(eventID, isRequesting);
        oEventIdentificationType.getEventTypeCode().add(AuditDataTransformHelper.createCodeValueType(CoreX12AuditDataTransformConstants.EVENT_ID_CODE_SYS_CODE_X12, null,
            CoreX12AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_X12, CoreX12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12REALTIME));
        auditMsg.setEventIdentification(oEventIdentificationType);

        AuditMessageType.ActiveParticipant participantSource = oCoreRealTimeTransforms.getActiveParticipantSource(isRequesting);
        AuditMessageType.ActiveParticipant participantDestination = oCoreRealTimeTransforms.getActiveParticipantDestination(target);
        auditMsg.getActiveParticipant().add(participantSource);
        auditMsg.getActiveParticipant().add(participantDestination);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = oCoreRealTimeTransforms.getParticipantObjectIdentificationType(response);
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        //find the requesting or responding gateway
        String messageDirection = isRequesting ? NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION : NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;

        /* Create the AuditSourceIdentifierType object */
        String communityId = oXDR.getMessageCommunityIdFromRequest(assertion, target, messageDirection, _interface);
        AuditSourceIdentificationType auditSource = oCoreRealTimeTransforms.getAuditSourceIdentificationType();
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);
        LOG.trace("End COREX12RealtimeTransforms -> transformBatchResponseToAuditMsg() -- NHIN");
        return result;
    }

    private boolean areRequiredCOREX12BatchFiledsNull(COREEnvelopeBatchSubmission msg, AssertionType assertion) {
        if (oXDR.areRequiredUserTypeFieldsNull(assertion)) {
            LOG.error("One of more UserInfo fields from the Assertion object were null.");
            return true;
        }

        if (areRequiredRequestFieldsNull(msg)) {
            LOG.error("One or more fields in CORE X12 Batch Request object were null.");
            return true;
        }
        return false;
    }

    private boolean areRequiredCOREX12BatchFiledsNull(COREEnvelopeBatchSubmissionResponse response, AssertionType assertion) {
        if (oXDR.areRequiredUserTypeFieldsNull(assertion)) {
            LOG.error("One of more UserInfo fields from the Assertion object were null.");
            return true;
        }

        if (areRequiredRequestFieldsNull(response)) {
            LOG.error("One or more fields in CORE X12 Batch Response object were null.");
            return true;
        }
        return false;
    }

    private boolean areRequiredRequestFieldsNull(COREEnvelopeBatchSubmission msg) {
        if (NullChecker.isNullish(msg.getCORERuleVersion())) {
            LOG.error("CORE X12 Batch CORERuleVersion is empty...");
            return true;
        }
        //TODO: For some cases the payload id coming as null. Need to fix it as part of
        //Batch Request and Batch Response fix
        //if (NullChecker.isNullish(msg.getPayloadID())) {
        //    LOG.error("CORE X12 Batch PayloadID is empty...");
        //    return true;
        //}

        if (NullChecker.isNullish(msg.getPayloadType())) {
            LOG.error("CORE X12 Batch PayloadType is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getProcessingMode())) {
            LOG.error("CORE X12 Batch ProcessingMode is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getReceiverID())) {
            LOG.error("CORE X12 Batch ReceiverID is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getSenderID())) {
            LOG.error("CORE X12 Batch SenderID is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getTimeStamp())) {
            LOG.error("CORE X12 Batch TimeStamp is empty...");
            return true;
        }

        return false;
    }

    private boolean areRequiredRequestFieldsNull(COREEnvelopeBatchSubmissionResponse msg) {
        if (NullChecker.isNullish(msg.getCORERuleVersion())) {
            LOG.error("CORE X12 Batch CORERuleVersion is empty...");
            return true;
        }
        //TODO: For some cases the payload id coming as null. Need to fix it as part of
        //Batch Request and Batch Response fix
        //if (NullChecker.isNullish(msg.getPayloadID())) {
        //    LOG.error("CORE X12 Batch PayloadID is empty...");
        //    return true;
        //}

        if (NullChecker.isNullish(msg.getPayloadType())) {
            LOG.error("CORE X12 Batch PayloadType is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getProcessingMode())) {
            LOG.error("CORE X12 Batch ProcessingMode is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getReceiverID())) {
            LOG.error("CORE X12 Batch ReceiverID is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getSenderID())) {
            LOG.error("CORE X12 Batch SenderID is empty...");
            return true;
        }

        if (NullChecker.isNullish(msg.getTimeStamp())) {
            LOG.error("CORE X12 Batch TimeStamp is empty...");
            return true;
        }
        return false;
    }
}
