/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;

import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.auditlog.LogDocRetrieveRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogDocRetrieveResultRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.util.List;

/**
 * 
 * @author MFLYNN02
 */
public class DocumentRetrieveTransforms {
    private static Log log = LogFactory.getLog(DocumentRetrieveTransforms.class);

    /**
     * 
     * @param message
     * @return <code>LogEventRequestType</code>
     */
    public static LogEventRequestType transformDocRetrieveReq2AuditMsg(LogDocRetrieveRequestType message) {
        return transformDocRetrieveReq2AuditMsg(message, null);
    }

    /**
     * 
     * @param message
     * @param responseCommunityID
     * @return <code>LogEventRequestType</code>
     */
    public static LogEventRequestType transformDocRetrieveReq2AuditMsg(LogDocRetrieveRequestType message,
            String responseCommunityID) {
        AuditMessageType auditMsg = new AuditMessageType();
        LogEventRequestType response = new LogEventRequestType();
        response.setDirection(message.getDirection());
        response.setInterface(message.getInterface());

        log.info("******************************************************************");
        log.info("Entering transformDocRetrieveReq2AuditMsg() method.");
        log.info("******************************************************************");

        // Extract UserInfo from Message.Assertion
        UserType userInfo = new UserType();
        if (message != null && message.getMessage() != null && message.getMessage().getAssertion() != null
                && message.getMessage().getAssertion().getUserInfo() != null) {
            userInfo = message.getMessage().getAssertion().getUserInfo();
        }

        // Create Event Identification Section
        // TODO: Determine what to do with Event Code and Event Code System (either auto-generate or map in
        // AdhocQueryRequest
        CodedValueType eventId = AuditDataTransformHelper.createEventId(
                AuditDataTransformConstants.EVENT_ID_CODE_DOCRETRIEVE_REQUEST,
                AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC,
                AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC,
                AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_DOCRETRIEVE_REQUEST);
        CodedValueType eventTypeCode = AuditDataTransformHelper.createCodeValueType(
                AuditDataTransformConstants.EVENT_TYPE_CODE_DOCRETRIEVE,
                AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCRETRIEVE,
                AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCRETRIEVE_DISPNAME,
                AuditDataTransformConstants.EVENT_TYPE_CODE_DOCRETRIEVE_DISPNAME);

        EventIdentificationType eventIdentification = AuditDataTransformHelper.createEventIdentification(
                AuditDataTransformConstants.EVENT_ACTION_CODE_READ,
                AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventId);
        auditMsg.setEventIdentification(eventIdentification);

        eventIdentification.getEventTypeCode().add(eventTypeCode);

        // Create Active Participant Section
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(
                    userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        String uniquePatientId = "";
        if (message != null && message.getMessage() != null && message.getMessage().getAssertion() != null
                && message.getMessage().getAssertion().getUniquePatientId() != null
                && message.getMessage().getAssertion().getUniquePatientId().size() > 0) {
            uniquePatientId = message.getMessage().getAssertion().getUniquePatientId().get(0);
            log.debug("=====>>>>> Create Audit Source Identification Section --> Assertion Unique Patient Id is ["
                    + uniquePatientId + "]");
        }

        // Create Audit Source Identification Section
        AuditSourceIdentificationType auditSrcId = null;
        if (responseCommunityID != null) {
            auditSrcId = AuditDataTransformHelper.createAuditSourceIdentification(responseCommunityID,
                    responseCommunityID);
        } else {
            auditSrcId = AuditDataTransformHelper.createAuditSourceIdentificationFromUser(userInfo);
        }
        auditMsg.getAuditSourceIdentification().add(auditSrcId);

        // Create Audit Source Identification Section
        String documentId = null;
        if (message.getMessage() != null && message.getMessage().getRetrieveDocumentSetRequest() != null
                && message.getMessage().getRetrieveDocumentSetRequest().getDocumentRequest() != null
                && message.getMessage().getRetrieveDocumentSetRequest().getDocumentRequest().size() > 0) {
            documentId = message.getMessage().getRetrieveDocumentSetRequest().getDocumentRequest().get(0)
                    .getDocumentUniqueId();

            List<DocumentRequest> documentRequestList = message.getMessage().getRetrieveDocumentSetRequest()
                    .getDocumentRequest();
            for (DocumentRequest documentRequest : documentRequestList) {
                if (documentRequest != null) {
                    log.debug("=====>>>>> Create Audit Source Identification Section --> DocumentRequest.documentUniqueId is ["
                            + documentRequest.getDocumentUniqueId() + "]");
                    log.debug("=====>>>>> Create Audit Source Identification Section --> DocumentRequest.homeCommunityId is ["
                            + documentRequest.getHomeCommunityId() + "]");
                }
            }
        }

        // Create Participation Object Identification Section
        ParticipantObjectIdentificationType partObjId = new ParticipantObjectIdentificationType();
        if (uniquePatientId != null && !uniquePatientId.isEmpty()) {
            partObjId = AuditDataTransformHelper.createParticipantObjectIdentification(uniquePatientId);
        } else if (documentId != null && !documentId.isEmpty()) {
            partObjId = AuditDataTransformHelper.createDocumentParticipantObjectIdentification(documentId);
        }
        auditMsg.getParticipantObjectIdentification().add(partObjId);

        log.info("******************************************************************");
        log.info("Exiting transformDocRetrieveReq2AuditMsg() method.");
        log.info("******************************************************************");

        response.setAuditMessage(auditMsg);
        return response;
    }

    /**
     * 
     * @param message
     * @return <code>LogEventRequestType</code>
     */
    public static LogEventRequestType transformDocRetrieveResp2AuditMsg(LogDocRetrieveResultRequestType message) {
        return transformDocRetrieveResp2AuditMsg(message, null);
    }

    /**
     * 
     * @param message
     * @param requestCommunityID
     * @return <code>LogEventRequestType</code>
     */
    public static LogEventRequestType transformDocRetrieveResp2AuditMsg(LogDocRetrieveResultRequestType message,
            String requestCommunityID) {
        AuditMessageType auditMsg = new AuditMessageType();
        LogEventRequestType response = new LogEventRequestType();
        response.setDirection(message.getDirection());
        response.setInterface(message.getInterface());

        log.info("******************************************************************");
        log.info("Entering transformDocRetrieveResp2AuditMsg() method.");
        log.info("******************************************************************");

        // Extract UserInfo from Message.Assertion
        UserType userInfo = new UserType();
        if (message != null && message.getMessage() != null && message.getMessage().getAssertion() != null
                && message.getMessage().getAssertion().getUserInfo() != null) {
            userInfo = message.getMessage().getAssertion().getUserInfo();
        }

        // Create Event Identification Section
        // TODO: Determine what to do with Event Code and Event Code System (either auto-generate or map in
        // AdhocQueryRequest
        CodedValueType eventId = AuditDataTransformHelper.createEventId(
                AuditDataTransformConstants.EVENT_ID_CODE_DOCRETRIEVE_RESPONSE,
                AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC,
                AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC,
                AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_DOCRETRIEVE_RESPONSE);
        CodedValueType eventTypeCode = AuditDataTransformHelper.createCodeValueType(
                AuditDataTransformConstants.EVENT_TYPE_CODE_DOCRETRIEVE,
                AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCRETRIEVE,
                AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCRETRIEVE_DISPNAME,
                AuditDataTransformConstants.EVENT_TYPE_CODE_DOCRETRIEVE_DISPNAME);

        EventIdentificationType eventIdentification = AuditDataTransformHelper.createEventIdentification(
                AuditDataTransformConstants.EVENT_ACTION_CODE_READ,
                AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventId);
        auditMsg.setEventIdentification(eventIdentification);

        eventIdentification.getEventTypeCode().add(eventTypeCode);

        // Create Active Participant Section
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(
                    userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        String uniquePatientId = "";
        if (message != null && message.getMessage() != null && message.getMessage().getAssertion() != null
                && message.getMessage().getAssertion().getUniquePatientId() != null
                && message.getMessage().getAssertion().getUniquePatientId().size() > 0) {
            uniquePatientId = message.getMessage().getAssertion().getUniquePatientId().get(0);
            log.debug("=====>>>>> Create Audit Source Identification Section --> Assertion Unique Patient Id is ["
                    + uniquePatientId + "]");
        }

        // Create Audit Source Identification Section
        AuditSourceIdentificationType auditSrcId = null;
        if (requestCommunityID != null) {
            auditSrcId = AuditDataTransformHelper.createAuditSourceIdentification(requestCommunityID,
                    requestCommunityID);
        } else {
            auditSrcId = AuditDataTransformHelper.createAuditSourceIdentificationFromUser(userInfo);
        }
        auditMsg.getAuditSourceIdentification().add(auditSrcId);

        // Create Audit Source Identification Section
        String documentId = null;
        if (message.getMessage() != null && message.getMessage().getRetrieveDocumentSetResponse() != null
                && message.getMessage().getRetrieveDocumentSetResponse().getDocumentResponse() != null
                && message.getMessage().getRetrieveDocumentSetResponse().getDocumentResponse().size() > 0) {

            documentId = message.getMessage().getRetrieveDocumentSetResponse().getDocumentResponse().get(0)
                    .getDocumentUniqueId();

            List<DocumentResponse> documentResponseList = message.getMessage().getRetrieveDocumentSetResponse()
                    .getDocumentResponse();
            for (DocumentResponse documentResponse : documentResponseList) {
                if (documentResponse != null) {
                    log.debug("=====>>>>> Create Audit Source Identification Section --> DocumentResponse.documentUniqueId is ["
                            + documentResponse.getDocumentUniqueId() + "]");
                    log.debug("=====>>>>> Create Audit Source Identification Section --> DocumentResponse.homeCommunityId is ["
                            + documentResponse.getHomeCommunityId() + "]");
                }
            }
        }

        // Create Participation Object Identification Section
        ParticipantObjectIdentificationType partObjId = new ParticipantObjectIdentificationType();
        if (documentId != null && !documentId.isEmpty()) {
            partObjId = AuditDataTransformHelper.createDocumentParticipantObjectIdentification(documentId);
        } else if (uniquePatientId != null && !uniquePatientId.isEmpty()) {
            partObjId = AuditDataTransformHelper.createParticipantObjectIdentification(uniquePatientId);
        }

        auditMsg.getParticipantObjectIdentification().add(partObjId);

        log.info("******************************************************************");
        log.info("Exiting transformDocRetrieveResp2AuditMsg() method.");
        log.info("******************************************************************");

        response.setAuditMessage(auditMsg);
        return response;
    }

}
