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
package gov.hhs.fha.nhinc.transform.audit;

/**
 *
 * @author mflynn02
 */
import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogFindAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import org.apache.log4j.Logger;

public class FindAuditEventsTransforms {

    private static final Logger LOG = Logger.getLogger(FindAuditEventsTransforms.class);

    public static LogEventRequestType transformFindAuditEventsReq2AuditMsg(LogFindAuditEventsRequestType message) {

        AuditMessageType auditMsg = new AuditMessageType();
        LogEventRequestType response = new LogEventRequestType();
        if (message != null) {
            response.setDirection(message.getDirection());
        }

        if (message != null) {
            LOG.info("FAE message is NOT null");

            // Extract UserInfo from Message.Assertion
            UserType userInfo = new UserType();
            if (message.getMessage() != null && message.getMessage().getAssertion() != null
                && message.getMessage().getAssertion().getUserInfo() != null) {
                userInfo = message.getMessage().getAssertion().getUserInfo();
            }

            int eventOutcomeID = 0;
            String userID = "";
            CodedValueType eventID = AuditDataTransformHelper.createEventId("ADQ", "AuditQuery", "ADQ", "AuditQuery");

            // EventIdentification
            auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(
                AuditDataTransformConstants.EVENT_ACTION_CODE_EXECUTE, eventOutcomeID, eventID));
            LOG.info("set EventIdentification");

            // ActiveParticipant
            // NOTE: This is [1..*] in schema but only one item to map to from FindAuditEventsType
            if (userInfo != null && NullChecker.isNotNullish(userInfo.getUserName())) {
                userID = userInfo.getUserName();
                LOG.info("userID " + userID);
            }
            String altUserID = "";
            String userName = "";
            if ((userInfo != null) && (userInfo.getPersonName() != null) && NullChecker.isNotNullish(userInfo.getPersonName().getGivenName())
                && NullChecker.isNotNullish(userInfo.getPersonName().getFamilyName())) {
                userName = userInfo.getPersonName().getGivenName() + " " + userInfo.getPersonName().getFamilyName();
            }
            AuditMessageType.ActiveParticipant activeParticipant = AuditDataTransformHelper.createActiveParticipant(
                userID, altUserID, userName, true);

            auditMsg.getActiveParticipant().add(activeParticipant);
            LOG.info("set ActiveParticiapnt");

            // AuditSourceIdentification
            // NOTE: This is [1..*] in the schema but only one item to map to from FindAuditEventsType
            String auditSourceID = "";
            String enterpriseSiteID = "";
            if (userInfo != null && userInfo.getOrg() != null) {
                if (userInfo.getOrg().getName() != null && userInfo.getOrg().getName().length() > 0) {
                    enterpriseSiteID = userInfo.getOrg().getName();
                }
                if (userInfo.getOrg().getHomeCommunityId() != null
                    && userInfo.getOrg().getHomeCommunityId().length() > 0) {

                    auditSourceID = userInfo.getOrg().getHomeCommunityId();
                    LOG.info("auditSourceID " + auditSourceID);
                }
            }
            AuditSourceIdentificationType auditSource = AuditDataTransformHelper.createAuditSourceIdentification(
                auditSourceID, enterpriseSiteID);
            auditMsg.getAuditSourceIdentification().add(auditSource);
            LOG.info("set AuditSourceIdentification");

            // ParticipationObjectIdentification
            // NOTE: This is [0..*] in the schema but only one item to map to from FindAuditEventsType
            String patientID = "";

            if (message.getMessage().getFindAuditEvents().getPatientId() != null
                && message.getMessage().getFindAuditEvents().getPatientId().length() > 0) {
                patientID = message.getMessage().getFindAuditEvents().getPatientId();
                LOG.info("patientID " + patientID);
            }
            ParticipantObjectIdentificationType partObject = AuditDataTransformHelper
                .createParticipantObjectIdentification(patientID);

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
                LOG.debug("Done marshalling the message.");

                partObject.setParticipantObjectQuery(baOutStrm.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }

            auditMsg.getParticipantObjectIdentification().add(partObject);
            LOG.info("set ParticipantObjectIdentification");
        }
        response.setAuditMessage(auditMsg);
        return response;
    }
}
