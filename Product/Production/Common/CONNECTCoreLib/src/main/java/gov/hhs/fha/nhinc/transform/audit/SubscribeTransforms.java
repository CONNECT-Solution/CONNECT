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

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogNhinSubscribeRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogSubscribeResponseType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.io.ByteArrayOutputStream;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import org.apache.log4j.Logger;
import org.oasis_open.docs.wsn.b_2.Subscribe;

/**
 * Transforms for subscribe messages.
 *
 * @author webbn
 */
public class SubscribeTransforms {

    private static final Logger LOG = Logger.getLogger(SubscribeTransforms.class);
    private static final String SLOT_NAME_PATIENT_ID = "$XDSDocumentEntryPatientId";

    public LogEventRequestType transformNhinSubscribeRequestToAuditMessage(LogNhinSubscribeRequestType message) {
        LogEventRequestType response = new LogEventRequestType();
        AuditMessageType auditMsg = new AuditMessageType();
        if (message != null) {
            response.setDirection(message.getDirection());
            response.setInterface(message.getInterface());
        }

        LOG.info("******************************************************************");
        LOG.info("Entering transformNhinSubscribeRequestToAuditMessage() method.");
        LOG.info("******************************************************************");

        // Extract UserInfo from Message.Assertion
        UserType userInfo = new UserType();
        if (message != null && message.getMessage() != null && message.getMessage().getAssertion() != null
            && message.getMessage().getAssertion().getUserInfo() != null) {
            userInfo = message.getMessage().getAssertion().getUserInfo();
        }

        // Create EventIdentification
        CodedValueType eventID = null;
        eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SUB,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SUBSCRIBE,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SUB,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SUBSCRIBE);
        auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(
            AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE,
            AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID));

        // Create Active Participant Section
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(
                userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        /* Assign AuditSourceIdentification */
        String communityId = "";
        String communityName = "";
        String patientId = "";

        if ((message != null) && (message.getMessage() != null) && (message.getMessage().getSubscribe() != null)) {
            PatientInfo patientInfo = extractPatientInfo(message.getMessage().getSubscribe());

            if (patientInfo != null) {
                communityId = patientInfo.getCommunityId();
                communityName = patientInfo.getCommunityName();
                patientId = patientInfo.getPatientId();
            }
        }

        if (userInfo != null && userInfo.getOrg() != null) {
            if (userInfo.getOrg().getHomeCommunityId() != null) {
                communityId = userInfo.getOrg().getHomeCommunityId();
            }
            if (userInfo.getOrg().getName() != null) {
                communityName = userInfo.getOrg().getName();
            }
        }

        AuditSourceIdentificationType auditSource = AuditDataTransformHelper.createAuditSourceIdentification(
            communityId, communityName);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = AuditDataTransformHelper
            .createParticipantObjectIdentification(patientId);

        // Fill in the message field with the contents of the event message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext(org.oasis_open.docs.wsn.b_2.ObjectFactory.class,
                oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory.class);
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            if (message != null) {
                marshaller.marshal(message.getMessage().getSubscribe(), baOutStrm);
            }
            LOG.debug("Done marshalling the message.");

            participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("EXCEPTION when marshalling subscribe request: " + e);
            throw new RuntimeException();
        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        response.setAuditMessage(auditMsg);

        LOG.info("******************************************************************");
        LOG.info("Exiting transformNhinSubscribeRequestToAuditMessage() method.");
        LOG.info("******************************************************************");

        return response;
    }

    public LogEventRequestType transformSubscribeResponseToAuditMessage(LogSubscribeResponseType message) {
        LogEventRequestType response = new LogEventRequestType();
        AuditMessageType auditMsg = new AuditMessageType();
        if (message != null) {
            response.setDirection(message.getDirection());
            response.setInterface(message.getInterface());
        }

        LOG.info("******************************************************************");
        LOG.info("Entering transformSubscribeResponseToAuditMessage() method.");
        LOG.info("******************************************************************");

        // Extract UserInfo from Message.Assertion
        UserType userInfo = new UserType();
        if (message != null && message.getMessage() != null && message.getMessage().getAssertion() != null
            && message.getMessage().getAssertion().getUserInfo() != null) {
            userInfo = message.getMessage().getAssertion().getUserInfo();
        }

        // Create EventIdentification
        CodedValueType eventID = null;
        eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SUB,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SUBSCRIBE,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SUB,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SUBSCRIBE);
        auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(
            AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE,
            AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID));

        // Create Active Participant Section
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(
                userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        String communityId = "";
        String communityName = "";
        if (userInfo != null && userInfo.getOrg() != null) {
            if (userInfo.getOrg().getHomeCommunityId() != null) {
                communityId = userInfo.getOrg().getHomeCommunityId();
            }
            if (userInfo.getOrg().getName() != null) {
                communityName = userInfo.getOrg().getName();
            }
        }

        AuditSourceIdentificationType auditSource = AuditDataTransformHelper.createAuditSourceIdentification(
            communityId, communityName);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        String patientId = "unknown";
        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = AuditDataTransformHelper
            .createParticipantObjectIdentification(patientId);

        // Fill in the message field with the contents of the event message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("org.oasis_open.docs.wsn.b_2");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            if (message != null) {
                marshaller.marshal(message.getMessage().getSubscribeResponse(), baOutStrm);
            }
            LOG.debug("Done marshalling the message.");

            participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("EXCEPTION when marshalling subscribe response: " + e);
            throw new RuntimeException();
        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        response.setAuditMessage(auditMsg);

        LOG.info("******************************************************************");
        LOG.info("Exiting transformSubscribeResponseToAuditMessage() method.");
        LOG.info("******************************************************************");

        return response;
    }

    private PatientInfo extractPatientInfo(Subscribe subscribe) {
        PatientInfo patientInfo = new PatientInfo();
        if (subscribe != null) {
            AdhocQueryType adhocQuery = getAdhocQuery(subscribe);
            if (adhocQuery != null) {
                String formattedPatientId = getFormattedPatientId(adhocQuery);
                if (NullChecker.isNotNullish(formattedPatientId)) {
                    patientInfo.setPatientId(PatientIdFormatUtil.parsePatientId(formattedPatientId));
                    patientInfo.setCommunityId(PatientIdFormatUtil.parseCommunityId(formattedPatientId));
                }
            }
        }
        return patientInfo;
    }

    private AdhocQueryType getAdhocQuery(Subscribe nhinSubscribe) {
        AdhocQueryType adhocQuery = null;
        LOG.info("begin getAdhocQuery");
        List<Object> any = nhinSubscribe.getAny();
        LOG.info("found " + any.size() + " any item(s)");

        for (Object anyItem : any) {
            LOG.info("anyItem=" + anyItem);
            if (anyItem instanceof oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType) {
                adhocQuery = (AdhocQueryType) anyItem;
            }
            if (anyItem instanceof JAXBElement) {
                LOG.info("jaxbelement.getValue=" + ((JAXBElement) anyItem).getValue());
                if (((JAXBElement) anyItem).getValue() instanceof AdhocQueryType) {
                    adhocQuery = (AdhocQueryType) ((JAXBElement) anyItem).getValue();
                } else {
                    LOG.warn("unhandled anyitem jaxbelement value " + ((JAXBElement) anyItem).getValue());
                }
            } else {
                LOG.warn("unhandled anyitem " + anyItem);
            }
        }
        LOG.info("end getAdhocQuery");
        return adhocQuery;
    }

    private String getFormattedPatientId(AdhocQueryType adhocQuery) {
        String formattedPatientId = null;
        List<SlotType1> slots = adhocQuery.getSlot();
        if ((slots != null) && !(slots.isEmpty())) {
            for (SlotType1 slot : slots) {
                if (SLOT_NAME_PATIENT_ID.equals(slot.getName())) {
                    if (slot.getValueList() != null) {
                        List<String> slotValues = slot.getValueList().getValue();
                        for (String value : slotValues) {
                            if (NullChecker.isNotNullish(value)) {
                                formattedPatientId = value;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return formattedPatientId;
    }

    private static class PatientInfo {

        private String communityId;
        private String communityName;
        private String patientId;

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        public void setCommunityName(String communityName) {
            this.communityName = communityName;
        }

        public String getCommunityName() {
            return communityName;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public String getPatientId() {
            return patientId;
        }
    }
}
