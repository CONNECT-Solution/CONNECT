/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;

import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.auditlog.LogAdhocQueryRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogAdhocQueryResultRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;

/**
 *
 * @author MFLYNN02
 */
public class DocumentQueryTransforms {

    private static Log log = LogFactory.getLog(DocumentQueryTransforms.class);
    private static final String PATIENT_ID_SLOT = "$XDSDocumentEntryPatientId";

    public static LogEventRequestType transformDocQueryReq2AuditMsg(LogAdhocQueryRequestType message) {
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
        CodedValueType eventId = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_DOCQUERY, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_DOCQUERY);
        EventIdentificationType eventIdentification = AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_EXECUTE, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventId);
        auditMsg.setEventIdentification(eventIdentification);

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
            JAXBContext jc = JAXBContext.newInstance("oasis.names.tc.ebxml_regrep.xsd.query._3");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            marshaller.marshal(message.getMessage().getAdhocQueryRequest(), baOutStrm);

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

    public static LogEventRequestType transformDocQueryResp2AuditMsg(LogAdhocQueryResultRequestType message) {
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
        CodedValueType eventId = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_DOCQUERY, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_DOCQUERY);
        EventIdentificationType eventIdentification = AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_EXECUTE, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventId);
        auditMsg.setEventIdentification(eventIdentification);

        // Create Active Participant Section
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        if ((message != null) &&
                (message.getMessage().getAdhocQueryResponse().getRegistryObjectList() != null) &&
                (message.getMessage().getAdhocQueryResponse().getRegistryObjectList().getIdentifiable() != null) &&
                (message.getMessage().getAdhocQueryResponse().getRegistryObjectList().getIdentifiable().size() > 0)) {
            // Create Audit Source Identification Section
            List<JAXBElement<? extends IdentifiableType>> objList = message.getMessage().getAdhocQueryResponse().getRegistryObjectList().getIdentifiable();
            ExtrinsicObjectType oExtObj = null;

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
                }
            }   //for (int i = 0; i < objList.size(); i++)

            // Home Community ID                      
            //-------------------
            if ((oExtObj != null) &&
                    (oExtObj.getHome() != null) &&
                    (oExtObj.getHome().length() > 0)) {
                String communityId = oExtObj.getHome();
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
                JAXBContext jc = JAXBContext.newInstance("oasis.names.tc.ebxml_regrep.xsd.query._3");
                Marshaller marshaller = jc.createMarshaller();
                ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
                baOutStrm.reset();
                marshaller.marshal(message.getMessage().getAdhocQueryResponse(), baOutStrm);

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
}
