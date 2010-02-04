/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.audit;

/**
 *
 * @author mflynn02
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import javax.xml.bind.Marshaller;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;

import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.auditlog.LogFindAuditEventsRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;

public class FindAuditEventsTransforms {

    private static Log log = LogFactory.getLog(FindAuditEventsTransforms.class);

    public static LogEventRequestType transformFindAuditEventsReq2AuditMsg(LogFindAuditEventsRequestType message) {

        AuditMessageType auditMsg = new AuditMessageType();
        LogEventRequestType response = new LogEventRequestType();
        response.setDirection(message.getDirection());
        response.setInterface(message.getInterface());

        if (message != null) {
            log.info("FAE message is NOT null");

            // Extract UserInfo from Message.Assertion
            UserType userInfo = new UserType();
            if (message != null &&
                    message.getMessage() != null &&
                    message.getMessage().getAssertion() != null &&
                    message.getMessage().getAssertion().getUserInfo() != null) {
                userInfo = message.getMessage().getAssertion().getUserInfo();
            }

            int eventOutcomeID = 0;
            String userID = "";
            CodedValueType eventID = AuditDataTransformHelper.createEventId("ADQ", "AuditQuery", "ADQ", "AuditQuery");

            // EventIdentification
            auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_EXECUTE, eventOutcomeID, eventID));
            log.info("set EventIdentification");

            //ActiveParticipant
            // NOTE: This is [1..*] in schema but only one item to map to from FindAuditEventsType

            if (userInfo != null &&
                    NullChecker.isNotNullish(userInfo.getUserName())) {
                userID = userInfo.getUserName();
                log.info("userID " + userID);
            }
            String altUserID = "";
            String userName = "";
            if (userInfo.getPersonName() != null &&
                    NullChecker.isNotNullish(userInfo.getPersonName().getGivenName()) &&
                    NullChecker.isNotNullish(userInfo.getPersonName().getFamilyName())) {
                userName = userInfo.getPersonName().getGivenName() + " " + userInfo.getPersonName().getFamilyName();
            }
            AuditMessageType.ActiveParticipant activeParticipant = AuditDataTransformHelper.createActiveParticipant(userID, altUserID, userName, true);

            auditMsg.getActiveParticipant().add(activeParticipant);
            log.info("set ActiveParticiapnt");

            // AuditSourceIdentification
            // NOTE: This is [1..*] in the schema but only one item to map to from FindAuditEventsType
            String auditSourceID = "";
            String enterpriseSiteID = "";
            if (userInfo != null &&
                    userInfo.getOrg() != null) {
                if (userInfo.getOrg().getName() != null &&
                        userInfo.getOrg().getName().length() > 0) {
                    enterpriseSiteID = userInfo.getOrg().getName();
                }
                if (userInfo.getOrg().getHomeCommunityId() != null &&
                        userInfo.getOrg().getHomeCommunityId().length() > 0) {

                    auditSourceID = userInfo.getOrg().getHomeCommunityId();
                    log.info("auditSourceID " + auditSourceID);
                }
            }
            AuditSourceIdentificationType auditSource = AuditDataTransformHelper.createAuditSourceIdentification(auditSourceID, enterpriseSiteID);
            auditMsg.getAuditSourceIdentification().add(auditSource);
            log.info("set AuditSourceIdentification");

            // ParticipationObjectIdentification
            // NOTE: This is [0..*] in the schema but only one item to map to from FindAuditEventsType
            String patientID = "";

            if (message.getMessage().getFindAuditEvents().getPatientId() != null &&
                    message.getMessage().getFindAuditEvents().getPatientId().length() > 0) {
                patientID = message.getMessage().getFindAuditEvents().getPatientId();
                log.info("patientID " + patientID);
            }
            ParticipantObjectIdentificationType partObject = AuditDataTransformHelper.createParticipantObjectIdentification(patientID);

            // Fill in the message field with the contents of the event message
            try {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("com.services.nhinc.schema.auditmessage");
                Marshaller marshaller = jc.createMarshaller();
                ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
                baOutStrm.reset();

                com.services.nhinc.schema.auditmessage.ObjectFactory factory = new com.services.nhinc.schema.auditmessage.ObjectFactory();
                JAXBElement oJaxbElement = factory.createFindAuditEvents(message.getMessage().getFindAuditEvents());
                baOutStrm.close();
                marshaller.marshal(oJaxbElement, baOutStrm);
                log.debug("Done marshalling the message.");

                partObject.setParticipantObjectQuery(baOutStrm.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }

            auditMsg.getParticipantObjectIdentification().add(partObject);
            log.info("set ParticipantObjectIdentification");
        }
        response.setAuditMessage(auditMsg);
        return response;
    }
}
