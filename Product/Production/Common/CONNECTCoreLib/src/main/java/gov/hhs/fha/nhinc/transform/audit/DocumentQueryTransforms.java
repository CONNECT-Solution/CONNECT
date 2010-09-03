/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.transform.audit;

import java.util.List;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.Marshaller;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBContext;

import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;

import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;

import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.auditlog.LogAdhocQueryRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogAdhocQueryResultRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectRefType;

/**
 *
 * @author MFLYNN02
 */
public class DocumentQueryTransforms {

    private Log log = null;
    private static final String PATIENT_ID_SLOT = "$XDSDocumentEntryPatientId";

    public DocumentQueryTransforms() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public LogEventRequestType transformDocQueryReq2AuditMsg(LogAdhocQueryRequestType message) {
        AuditMessageType auditMsg = new AuditMessageType();
        LogEventRequestType response = new LogEventRequestType();
        response.setDirection(message.getDirection());
        response.setInterface(message.getInterface());

        log.info("******************************************************************");
        log.info("Entering transformDocQueryReq2AuditMsg() method.");
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
        CodedValueType eventId = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_DOCQUERY, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_DOCQUERY);
        CodedValueType eventTypeCode = AuditDataTransformHelper.createCodeValueType(AuditDataTransformConstants.EVENT_TYPE_CODE_DOCQUERY, AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCQUERY, AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCQUERY_DISPNAME, AuditDataTransformConstants.EVENT_TYPE_CODE_DOCQUERY_DISPNAME);

        EventIdentificationType eventIdentification = AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_EXECUTE, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventId);
        auditMsg.setEventIdentification(eventIdentification);

        eventIdentification.getEventTypeCode().add(eventTypeCode);

        // Create Active Participant Section
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        // Create Audit Source Identification Section
        AuditSourceIdentificationType auditSrcId = AuditDataTransformHelper.createAuditSourceIdentificationFromUser(userInfo);
        auditMsg.getAuditSourceIdentification().add(auditSrcId);

        String patientId = "";
        if (message != null &&
                message.getMessage() != null &&
                message.getMessage().getAdhocQueryRequest() != null &&
                message.getMessage().getAdhocQueryRequest().getAdhocQuery() != null &&
                message.getMessage().getAdhocQueryRequest().getAdhocQuery().getSlot() != null &&
                message.getMessage().getAdhocQueryRequest().getAdhocQuery().getSlot().size() > 0) {
            List<SlotType1> slotItemsList = message.getMessage().getAdhocQueryRequest().getAdhocQuery().getSlot();
            for (SlotType1 slotItem : slotItemsList) {
                if (slotItem != null) {
                    if (PATIENT_ID_SLOT.equals(slotItem.getName())) {
                        if (slotItem.getValueList() != null &&
                                slotItem.getValueList().getValue() != null &&
                                slotItem.getValueList().getValue().size() > 0) {
                            patientId = slotItem.getValueList().getValue().get(0);
                            break;
                        }
                    }
                }
            }
        }
        // Create Participation Object Identification Section
        ParticipantObjectIdentificationType partObjId = new ParticipantObjectIdentificationType();
        partObjId = AuditDataTransformHelper.createParticipantObjectIdentification(patientId);

        // Fill in the message field with the contents of the event message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("oasis.names.tc.ebxml_regrep.xsd.query._3");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            marshaller.marshal(message.getMessage().getAdhocQueryRequest(), baOutStrm);
            log.debug("Done marshalling the message.");

            partObjId.setParticipantObjectQuery(baOutStrm.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        auditMsg.getParticipantObjectIdentification().add(partObjId);
        response.setAuditMessage(auditMsg);

        log.info("******************************************************************");
        log.info("Exiting transformDocQueryReq2AuditMsg() method.");
        log.info("******************************************************************");

        return response;
    }

    public LogEventRequestType transformDocQueryResp2AuditMsg(LogAdhocQueryResultRequestType message) {
        AuditMessageType auditMsg = new AuditMessageType();
        LogEventRequestType response = new LogEventRequestType();
        response.setDirection(message.getDirection());
        response.setInterface(message.getInterface());

        log.info("******************************************************************");
        log.info("Entering transformDocQueryResp2AuditMsg() method.");
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
        // TODO: Figure out what to do with Event Code and Event Code System
        CodedValueType eventId = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_DOCQUERY, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_DOCQUERY);
        CodedValueType eventTypeCode = AuditDataTransformHelper.createCodeValueType(AuditDataTransformConstants.EVENT_TYPE_CODE_DOCQUERY, AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCQUERY, AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCQUERY_DISPNAME, AuditDataTransformConstants.EVENT_TYPE_CODE_DOCQUERY_DISPNAME);

        EventIdentificationType eventIdentification = AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_EXECUTE, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventId);
        auditMsg.setEventIdentification(eventIdentification);

        eventIdentification.getEventTypeCode().add(eventTypeCode);

        // Create Active Participant Section
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        if ((message != null) &&
                (message.getMessage() != null) &&
                (message.getMessage().getAdhocQueryResponse() != null) &&
                (message.getMessage().getAdhocQueryResponse().getRegistryObjectList() != null) &&
                (message.getMessage().getAdhocQueryResponse().getRegistryObjectList().getIdentifiable() != null) &&
                (message.getMessage().getAdhocQueryResponse().getRegistryObjectList().getIdentifiable().size() > 0)) {
            // Create Audit Source Identification Section
            List<JAXBElement<? extends IdentifiableType>> objList = message.getMessage().getAdhocQueryResponse().getRegistryObjectList().getIdentifiable();
            ExtrinsicObjectType oExtObj = null;
            ObjectRefType oObjRef = null;

            // Look for the first ExtrinsicObject type..  We will use that one to extract the data.
            //-------------------------------------------------------------------------------------
            for (int i = 0; i < objList.size(); i++) {
                JAXBElement<? extends IdentifiableType> oJAXBObj = objList.get(i);

                if ((oJAXBObj != null) &&
                        (oJAXBObj.getDeclaredType() != null) &&
                        (oJAXBObj.getDeclaredType().getCanonicalName() != null) &&
                        (oJAXBObj.getDeclaredType().getCanonicalName().equals("oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType")) &&
                        (oJAXBObj.getValue() != null)) {
                    oExtObj = (ExtrinsicObjectType) oJAXBObj.getValue();
                    break;          // We have what we want let's get out of here...
                } else if ((oJAXBObj != null) &&
                        (oJAXBObj.getDeclaredType() != null) &&
                        (oJAXBObj.getDeclaredType().getCanonicalName() != null) &&
                        (oJAXBObj.getDeclaredType().getCanonicalName().equals("oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectRefType")) &&
                        (oJAXBObj.getValue() != null)) {
                    oObjRef = (ObjectRefType) oJAXBObj.getValue();
                    break;          // We have what we want let's get out of here...
                }

            }   //for (int i = 0; i < objList.size(); i++)

            // Home Community ID                      
            //-------------------
            String communityId = null;
            if ((oExtObj != null) &&
                    (oExtObj.getHome() != null) &&
                    (oExtObj.getHome().length() > 0)) {
                communityId = oExtObj.getHome();
            } else if ((oObjRef != null) && (NullChecker.isNotNullish(oObjRef.getHome()))) {
                communityId = oObjRef.getHome();
            }
            if (communityId != null) {
                AuditSourceIdentificationType auditSrcId = AuditDataTransformHelper.createAuditSourceIdentification(communityId, null);
                auditMsg.getAuditSourceIdentification().add(auditSrcId);
            }

            // Patient ID
            //------------
            ParticipantObjectIdentificationType partObjId = new ParticipantObjectIdentificationType();
            String patientId = "";
            if (oExtObj != null &&
                    oExtObj.getExternalIdentifier() != null &&
                    oExtObj.getExternalIdentifier().size() > 0) {
                patientId = oExtObj.getExternalIdentifier().get(0).getValue();
            }
            if (userInfo != null) {
                partObjId = AuditDataTransformHelper.createParticipantObjectIdentification(patientId);
            }

            // Fill in the message field with the contents of the event message
            try {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("oasis.names.tc.ebxml_regrep.xsd.query._3");
                Marshaller marshaller = jc.createMarshaller();
                ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
                baOutStrm.reset();
                marshaller.marshal(message.getMessage().getAdhocQueryResponse(), baOutStrm);
                log.debug("Done marshalling the message.");

                partObjId.setParticipantObjectQuery(baOutStrm.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
            auditMsg.getParticipantObjectIdentification().add(partObjId);
        }
        response.setAuditMessage(auditMsg);

        log.info("******************************************************************");
        log.info("Exiting transformDocQueryResp2AuditMsg() method.");
        log.info("******************************************************************");
        return response;
    }

    /**
     *
     * @param acknowledgement
     * @param assertion
     * @param direction
     * @param _interface
     * @param action
     * @return LogEventRequestType
     */
    public LogEventRequestType transformAcknowledgementToAuditMsg(DocQueryAcknowledgementType acknowledgement, AssertionType assertion, String direction, String _interface) {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        if (acknowledgement == null) {
            log.error("Acknowledgement is null");
            return null;
        }

        if (assertion == null) {
            log.error("Assertion is null");
            return null;
        }

        result = new LogEventRequestType();

        auditMsg = new AuditMessageType();
        // Create EventIdentification
        CodedValueType eventId = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_DOCQUERY, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_DOCQUERY);
        CodedValueType eventTypeCode = AuditDataTransformHelper.createCodeValueType(AuditDataTransformConstants.EVENT_TYPE_CODE_DOCQUERY, AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCQUERY, AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCQUERY_DISPNAME, AuditDataTransformConstants.EVENT_TYPE_CODE_DOCRETRIEVE_DISPNAME);
        EventIdentificationType eventIdentification = AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_READ, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventId);
        auditMsg.setEventIdentification(eventIdentification);
        eventIdentification.getEventTypeCode().add(eventTypeCode);
        auditMsg.setEventIdentification(eventIdentification);

        // Create Active Participant Section
        if (assertion.getUserInfo() != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(assertion.getUserInfo(), true);
            auditMsg.getActiveParticipant().add(participant);
        }

        /* Assign ParticipationObjectIdentification */
        String patientId = new String();
        // Create Participation Object Identification Section
        ParticipantObjectIdentificationType partObjId = new ParticipantObjectIdentificationType();
        if (assertion.getUserInfo() != null) {
            partObjId = AuditDataTransformHelper.createParticipantObjectIdentification(patientId);
        }

        // Put the contents of the actual message into the Audit Log Message
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        marshalAcknowledgement(baOutStrm, acknowledgement);
        partObjId.setParticipantObjectQuery(baOutStrm.toByteArray());
        auditMsg.getParticipantObjectIdentification().add(partObjId);

        /* Create the AuditSourceIdentifierType object */
        
        // Home Community ID
        //-------------------
        /* Create the AuditSourceIdentifierType object */
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(assertion.getUserInfo());
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        return result;

    }

    /**
     *
     * @param baOutStrm
     * @param acknowledgement
     * @throws RuntimeException
     */
    private void marshalAcknowledgement(ByteArrayOutputStream baOutStrm, DocQueryAcknowledgementType acknowledgement) throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.healthit.nhin");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("http://www.hhs.gov/healthit/nhin", "DocQueryAcknowledgementType");
            JAXBElement<DocQueryAcknowledgementType> element;

            element = new JAXBElement<DocQueryAcknowledgementType>(xmlqname, DocQueryAcknowledgementType.class, acknowledgement);


            marshaller.marshal(element, baOutStrm);
            log.debug("Done marshalling the message.");
        } catch (Exception e) {
            log.error("Exception while marshalling Acknowledgement", e);
            throw new RuntimeException();
        }
    }

    private AuditSourceIdentificationType getAuditSourceIdentificationType(UserType userInfo)
    {
        AuditSourceIdentificationType result;

        String communityId = "";
        String communityName = "";
        if (userInfo != null && userInfo.getOrg() != null)
        {
            if (userInfo.getOrg().getHomeCommunityId() != null)
            {
                communityId = userInfo.getOrg().getHomeCommunityId();
            }
            if (userInfo.getOrg().getName() != null)
            {
                communityName = userInfo.getOrg().getName();
            }
        }
        result = AuditDataTransformHelper.createAuditSourceIdentification(communityId, communityName);
        return result;
    }
}
