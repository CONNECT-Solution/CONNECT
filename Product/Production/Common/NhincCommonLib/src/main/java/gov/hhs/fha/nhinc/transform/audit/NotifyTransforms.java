/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.audit;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityNotifyResponseType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogNhinNotifyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class NotifyTransforms {

    private static Log log = LogFactory.getLog(NotifyTransforms.class);

    public LogEventRequestType transformNhinNotifyRequestToAuditMessage(LogNhinNotifyRequestType message) {
        LogEventRequestType response = new LogEventRequestType();
        AuditMessageType auditMsg = new AuditMessageType();
        response.setDirection(message.getDirection());
        response.setInterface(message.getInterface());

        log.info("******************************************************************");
        log.info("Entering transformNhinNotifyRequestToAuditMessage() method.");
        log.info("******************************************************************");

        // Extract UserInfo from Message.Assertion
        UserType userInfo = new UserType();
        if (message != null &&
                message.getMessage() != null &&
                message.getMessage().getAssertion() != null &&
                message.getMessage().getAssertion().getUserInfo() != null) {
            userInfo = message.getMessage().getAssertion().getUserInfo();
        }

        // Create EventIdentification
        CodedValueType eventID = new CodedValueType();
        eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_NOT, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_NOTIFY, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_NOT, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_NOTIFY);
        auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID));

        // Create Active Participant Section   
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        /* Assign AuditSourceIdentification */

        String patientId = "";
        if (NullChecker.isNotNullish(message.getMessage().getAssertion().getUniquePatientId()) &&
                NullChecker.isNotNullish(message.getMessage().getAssertion().getUniquePatientId().get(0))) {
            patientId = message.getMessage().getAssertion().getUniquePatientId().get(0);
        }

        String communityId = "";
        String communityName = "";
        if (userInfo != null &&
                userInfo.getOrg() != null) {

            if (userInfo.getOrg().getHomeCommunityId() != null) {
                communityId = userInfo.getOrg().getHomeCommunityId();
            }
            if (userInfo.getOrg().getName() != null) {
                communityName = userInfo.getOrg().getName();
            }
        }

        AuditSourceIdentificationType auditSource = AuditDataTransformHelper.createAuditSourceIdentification(communityId, communityName);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = AuditDataTransformHelper.createParticipantObjectIdentification(patientId);

        // Fill in the message field with the contents of the event message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext(org.oasis_open.docs.wsn.b_2.ObjectFactory.class, ihe.iti.xds_b._2007.ObjectFactory.class);
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            marshaller.marshal(message.getMessage().getNotify(), baOutStrm);
            log.debug("Done marshalling the message.");

            participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("EXCEPTION when marshalling Nhin Notify Request : " + e);
            throw new RuntimeException();
        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        response.setAuditMessage(auditMsg);

        log.info("******************************************************************");
        log.info("Exiting transformNhinNotifyRequestToAuditMessage() method.");
        log.info("******************************************************************");

        return response;
    }

    public LogEventRequestType transformEntityNotifyResponseToGenericAudit(LogEntityNotifyResponseType message) {
        LogEventRequestType response = new LogEventRequestType();
        AuditMessageType auditMsg = new AuditMessageType();
        response.setDirection(message.getDirection());
        response.setInterface(message.getInterface());

        log.info("******************************************************************");
        log.info("Entering transformEntityNotifyResponseToGenericAudit() method.");
        log.info("******************************************************************");

        // Extract UserInfo from Message.Assertion
        UserType userInfo = new UserType();
        if (message != null &&
                message.getMessage() != null &&
                message.getMessage().getAssertion() != null &&
                message.getMessage().getAssertion().getUserInfo() != null) {
            userInfo = message.getMessage().getAssertion().getUserInfo();
        }

        // Create EventIdentification
        CodedValueType eventID = new CodedValueType();
        eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_NOT, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_NOTIFY, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_NOT, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_NOTIFY);
        auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID));

        // Create Active Participant Section   
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        /* Assign AuditSourceIdentification */

        String patientId = "";
        if (NullChecker.isNotNullish(message.getMessage().getAssertion().getUniquePatientId()) &&
                NullChecker.isNotNullish(message.getMessage().getAssertion().getUniquePatientId().get(0))) {
            patientId = message.getMessage().getAssertion().getUniquePatientId().get(0);
        }

        String communityId = "";
        String communityName = "";
        if (userInfo != null &&
                userInfo.getOrg() != null) {

            if (userInfo.getOrg().getHomeCommunityId() != null) {
                communityId = userInfo.getOrg().getHomeCommunityId();
            }
            if (userInfo.getOrg().getName() != null) {
                communityName = userInfo.getOrg().getName();
            }
        }

        AuditSourceIdentificationType auditSource = AuditDataTransformHelper.createAuditSourceIdentification(communityId, communityName);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = AuditDataTransformHelper.createParticipantObjectIdentification(patientId);

        // Fill in the message field with the contents of the event message
//        try {
//            JAXBContext jc = JAXBContext.newInstance("gov.​hhs.​fha.​nhinc.​common.​nhinccommon");
//            Marshaller marshaller = jc.createMarshaller();
//            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
//            baOutStrm.reset();
//            
//            gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory factory = new gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory();
//            JAXBElement oJaxbElement = factory.createAcknowledgement(message.getMessage().getAck());
//            baOutStrm.close();
//            marshaller.marshal(oJaxbElement, baOutStrm);
//            
//            participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("EXCEPTION when marshalling Nhin Notify Request : " + e);
//            throw new RuntimeException();
//        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        response.setAuditMessage(auditMsg);
        
        log.info("******************************************************************");
        log.info("Exiting transformEntityNotifyResponseToGenericAudit() method.");
        log.info("******************************************************************");
        return response;
    }
}
