/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.transform.audit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBContext;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.Marshaller;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;

import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.auditlog.LogDocRetrieveRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogDocRetrieveResultRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;

import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
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
    public static LogEventRequestType transformDocRetrieveReq2AuditMsg(LogDocRetrieveRequestType message, String responseCommunityID) {
        AuditMessageType auditMsg = new AuditMessageType();
        LogEventRequestType response = new LogEventRequestType();
        response.setDirection(message.getDirection());
        response.setInterface(message.getInterface());

        log.info("******************************************************************");
        log.info("Entering transformDocRetrieveReq2AuditMsg() method.");
        log.info("******************************************************************");
        
        // Extract UserInfo from Message.Assertion
        UserType userInfo = new UserType();
        if (message != null &&
                message.getMessage() != null &&
                message.getMessage().getAssertion() != null &&
                message.getMessage().getAssertion().getUserInfo() != null) {
            userInfo = message.getMessage().getAssertion().getUserInfo();
        }

        // Create Event Identification Section
        // TODO: Determine what to do with Event Code and Event Code System (either auto-generate or map in AdhocQueryRequest
        CodedValueType eventId = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_DOCRETRIEVE_REQUEST, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_DOCRETRIEVE_REQUEST);
        CodedValueType eventTypeCode = AuditDataTransformHelper.createCodeValueType(AuditDataTransformConstants.EVENT_TYPE_CODE_DOCRETRIEVE, AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCRETRIEVE, AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCRETRIEVE_DISPNAME, AuditDataTransformConstants.EVENT_TYPE_CODE_DOCRETRIEVE_DISPNAME);

        EventIdentificationType eventIdentification = AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_READ, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventId);
        auditMsg.setEventIdentification(eventIdentification);

        eventIdentification.getEventTypeCode().add(eventTypeCode);

        // Create Active Participant Section
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        String uniquePatientId = "";
        if (message != null &&
                message.getMessage() != null &&
                message.getMessage().getAssertion() != null &&
                message.getMessage().getAssertion().getUniquePatientId() != null &&
                message.getMessage().getAssertion().getUniquePatientId().size() > 0) {
            uniquePatientId = message.getMessage().getAssertion().getUniquePatientId().get(0);
            log.debug("=====>>>>> Create Audit Source Identification Section --> Assertion Unique Patient Id is [" + uniquePatientId + "]");
        }

        // Create Audit Source Identification Section
        AuditSourceIdentificationType auditSrcId = null;
        if (responseCommunityID != null) {
            auditSrcId = AuditDataTransformHelper.createAuditSourceIdentification(responseCommunityID, responseCommunityID);
        } else {
            auditSrcId = AuditDataTransformHelper.createAuditSourceIdentificationFromUser(userInfo);
        }
        auditMsg.getAuditSourceIdentification().add(auditSrcId);

        // Create Audit Source Identification Section
        String documentId = null;
       if (message.getMessage() != null &&
                message.getMessage().getRetrieveDocumentSetRequest() != null &&
                message.getMessage().getRetrieveDocumentSetRequest().getDocumentRequest() != null &&
                message.getMessage().getRetrieveDocumentSetRequest().getDocumentRequest().size() > 0) {
            documentId = message.getMessage().getRetrieveDocumentSetRequest().getDocumentRequest().get(0).getDocumentUniqueId();

            List<DocumentRequest> documentRequestList = message.getMessage().getRetrieveDocumentSetRequest().getDocumentRequest();
            for (DocumentRequest documentRequest : documentRequestList) {
                if (documentRequest != null) {
                    log.debug("=====>>>>> Create Audit Source Identification Section --> DocumentRequest.documentUniqueId is [" + documentRequest.getDocumentUniqueId() + "]");
                    log.debug("=====>>>>> Create Audit Source Identification Section --> DocumentRequest.homeCommunityId is [" + documentRequest.getHomeCommunityId() + "]");
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

        // Fill in the message field with the contents of the event message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("ihe.iti.xds_b._2007");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            ihe.iti.xds_b._2007.ObjectFactory factory = new ihe.iti.xds_b._2007.ObjectFactory();
            JAXBElement oJaxbElement = factory.createRetrieveDocumentSetRequest(message.getMessage().getRetrieveDocumentSetRequest());
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            log.debug("Done marshalling the message.");

            partObjId.setParticipantObjectQuery(baOutStrm.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
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
     * @param  requestCommunityID
     * @return <code>LogEventRequestType</code>
     */
    public static LogEventRequestType transformDocRetrieveResp2AuditMsg(LogDocRetrieveResultRequestType message, String requestCommunityID) {
        AuditMessageType auditMsg = new AuditMessageType();
        LogEventRequestType response = new LogEventRequestType();
        response.setDirection(message.getDirection());
        response.setInterface(message.getInterface());

        log.info("******************************************************************");
        log.info("Entering transformDocRetrieveResp2AuditMsg() method.");
        log.info("******************************************************************");
        
        // Extract UserInfo from Message.Assertion
        UserType userInfo = new UserType();
        if (message != null &&
                message.getMessage() != null &&
                message.getMessage().getAssertion() != null &&
                message.getMessage().getAssertion().getUserInfo() != null) {
            userInfo = message.getMessage().getAssertion().getUserInfo();
        }

        // Create Event Identification Section
        // TODO: Determine what to do with Event Code and Event Code System (either auto-generate or map in AdhocQueryRequest
        CodedValueType eventId = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_DOCRETRIEVE_RESPONSE, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_DOCRETRIEVE_RESPONSE);
        CodedValueType eventTypeCode = AuditDataTransformHelper.createCodeValueType(AuditDataTransformConstants.EVENT_TYPE_CODE_DOCRETRIEVE, AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCRETRIEVE, AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCRETRIEVE_DISPNAME, AuditDataTransformConstants.EVENT_TYPE_CODE_DOCRETRIEVE_DISPNAME);

        EventIdentificationType eventIdentification = AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_READ, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventId);
        auditMsg.setEventIdentification(eventIdentification);

        eventIdentification.getEventTypeCode().add(eventTypeCode);

        // Create Active Participant Section
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        String uniquePatientId = "";
        if (message != null &&
                message.getMessage() != null &&
                message.getMessage().getAssertion() != null &&
                message.getMessage().getAssertion().getUniquePatientId() != null &&
                message.getMessage().getAssertion().getUniquePatientId().size() > 0) {
            uniquePatientId = message.getMessage().getAssertion().getUniquePatientId().get(0);
            log.debug("=====>>>>> Create Audit Source Identification Section --> Assertion Unique Patient Id is [" + uniquePatientId + "]");
        }

        // Create Audit Source Identification Section
        AuditSourceIdentificationType auditSrcId = null;
        if (requestCommunityID != null) {
            auditSrcId = AuditDataTransformHelper.createAuditSourceIdentification(requestCommunityID, requestCommunityID);
        } else {
            auditSrcId = AuditDataTransformHelper.createAuditSourceIdentificationFromUser(userInfo);
        }
        auditMsg.getAuditSourceIdentification().add(auditSrcId);

        // Create Audit Source Identification Section
        String documentId = null;
        if (message.getMessage() != null &&
                message.getMessage().getRetrieveDocumentSetResponse() != null &&
                message.getMessage().getRetrieveDocumentSetResponse().getDocumentResponse() != null &&
                message.getMessage().getRetrieveDocumentSetResponse().getDocumentResponse().size() > 0) {

            documentId = message.getMessage().getRetrieveDocumentSetResponse().getDocumentResponse().get(0).getDocumentUniqueId();

            List<DocumentResponse> documentResponseList = message.getMessage().getRetrieveDocumentSetResponse().getDocumentResponse();
            for (DocumentResponse documentResponse : documentResponseList) {
                if (documentResponse != null) {
                    log.debug("=====>>>>> Create Audit Source Identification Section --> DocumentResponse.documentUniqueId is [" + documentResponse.getDocumentUniqueId() + "]");
                    log.debug("=====>>>>> Create Audit Source Identification Section --> DocumentResponse.homeCommunityId is [" + documentResponse.getHomeCommunityId() + "]");
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

        // Fill in the message field with the contents of the event message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("ihe.iti.xds_b._2007");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            ihe.iti.xds_b._2007.ObjectFactory factory = new ihe.iti.xds_b._2007.ObjectFactory();
            JAXBElement oJaxbElement = factory.createRetrieveDocumentSetResponse(message.getMessage().getRetrieveDocumentSetResponse());
            marshaller.marshal(oJaxbElement, baOutStrm);
            log.debug("Done marshalling the message.");

            partObjId.setParticipantObjectQuery(baOutStrm.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        auditMsg.getParticipantObjectIdentification().add(partObjId);

        log.info("******************************************************************");
        log.info("Exiting transformDocRetrieveResp2AuditMsg() method.");
        log.info("******************************************************************");

        response.setAuditMessage(auditMsg);
        return response;
    }

}
