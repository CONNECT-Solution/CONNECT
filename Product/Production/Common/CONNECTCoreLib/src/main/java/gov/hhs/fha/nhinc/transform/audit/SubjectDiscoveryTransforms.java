/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.audit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import javax.xml.bind.JAXBContext;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.Marshaller;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectAddedRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectRevisedRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogNhinSubjectDiscoveryAckRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectReidentificationRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectReidentificationResponseType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Extractors;
import org.hl7.v3.*;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;

/**
 *
 * @author MFLYNN02
 */
public class SubjectDiscoveryTransforms {

    private static Log log = LogFactory.getLog(SubjectDiscoveryTransforms.class);
    private static final String JAXB_HL7_CONTEXT_NAME = "org.hl7.v3";


    public static LogEventRequestType transformPRPA2013012AuditMsg(LogSubjectAddedRequestType message) {
        AuditMessageType auditMsg = new AuditMessageType();
        LogEventRequestType response = new LogEventRequestType();
        response.setDirection(message.getDirection());
        response.setInterface(message.getInterface());

        log.info("******************************************************************");
        log.info("Entering transformPRPA2013012AuditMsg() method.");
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
        eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SDN, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SDNEW, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SDN, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SDNEW);
        auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID));

        // Create Active Participant Section   
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        /* Assign AuditSourceIdentification */
        String communityId = "";
        String communityName = "";
        String patientId = "";

        if (message != null &&
                message.getMessage() != null &&
                message.getMessage().getPRPAIN201301UV02() != null) {
            PRPAMT201301UV02Patient patient = HL7Extractors.ExtractHL7PatientFromMessage(message.getMessage().getPRPAIN201301UV02());
            communityId = patient.getId().get(0).getRoot();
            patientId = message.getMessage().getPRPAIN201301UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension();
            patientId = AuditDataTransformHelper.createCompositePatientId(communityId, patientId);

        }

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
            JAXBContext jc = oHandler.getJAXBContext(JAXB_HL7_CONTEXT_NAME);
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            marshaller.marshal(message.getMessage().getPRPAIN201301UV02(), baOutStrm);
            log.debug("Done marshalling the message.");

            participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("EXCEPTION when marshalling 201301 : " + e);
            throw new RuntimeException();
        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        log.info("******************************************************************");
        log.info("Exiting transformPRPA2013012AuditMsg() method.");
        log.info("******************************************************************");

        response.setAuditMessage(auditMsg);
        return response;
    }

    public static LogEventRequestType transformPRPA2013022AuditMsg(LogSubjectRevisedRequestType message) {
        AuditMessageType auditMsg = new AuditMessageType();
        LogEventRequestType response = new LogEventRequestType();
        response.setDirection(message.getDirection());
        response.setInterface(message.getInterface());

        log.info("******************************************************************");
        log.info("Entering transformPRPA2013022AuditMsg() method.");
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
        eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SDR, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SDREV, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SDR, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SDREV);
        auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_UPDATE, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID));

        // Create Active Participant Section   
        if (message != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        /* Assign AuditSourceIdentification */
        String communityId = "";
        String communityName = "";
        String patientId = "";

        if (message != null &&
                message.getMessage() != null &&
                message.getMessage().getPRPAIN201302UV02() != null) {
            PRPAMT201302UV02Patient patient = HL7Extractors.ExtractHL7PatientFromMessage(message.getMessage().getPRPAIN201302UV02());
            communityId = patient.getId().get(0).getRoot();
            patientId = message.getMessage().getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension();
            patientId = AuditDataTransformHelper.createCompositePatientId(communityId, patientId);

        }

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
            JAXBContext jc = oHandler.getJAXBContext(JAXB_HL7_CONTEXT_NAME);
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            marshaller.marshal(message.getMessage().getPRPAIN201302UV02(), baOutStrm);
            log.debug("Done marshalling the message.");

            participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        log.info("******************************************************************");
        log.info("Exiting transformPRPA2013022AuditMsg() method.");
        log.info("******************************************************************");

        response.setAuditMessage(auditMsg);
        return response;
    }

       public static LogEventRequestType transformAck2AuditMsg(LogNhinSubjectDiscoveryAckRequestType message) {

        log.info("******************************************************************");
        log.info("Entering transformAck2AuditMsg() method.");
        log.info("******************************************************************");

        AuditMessageType auditMsg = new AuditMessageType();
        LogEventRequestType response = new LogEventRequestType();
        response.setDirection(message.getDirection());
        response.setInterface(message.getInterface());

        // Extract UserInfo from Message.Assertion
        UserType userInfo = new UserType();
        if (message != null &&
                message.getMessage() != null &&
                message.getMessage().getPIXConsumerMCCIIN000002UV01Request().getAssertion() != null &&
                message.getMessage().getPIXConsumerMCCIIN000002UV01Request().getAssertion().getUserInfo() != null) {
            userInfo = message.getMessage().getPIXConsumerMCCIIN000002UV01Request().getAssertion().getUserInfo();
        }

        // Create EventIdentification
        CodedValueType eventID = new CodedValueType();
        eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_ACK, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_ACK, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_ACK, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_ACK);
        auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_EXECUTE, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID));

        // Create Active Participant Section   
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        // AuditSourceIdentification
        String enterpriseID = "";
        String sourceID = "";
        
        if (message != null &&
                message.getMessage().getPIXConsumerMCCIIN000002UV01Request().getMCCIIN000002UV01().getReceiver() != null &&
                message.getMessage().getPIXConsumerMCCIIN000002UV01Request().getMCCIIN000002UV01().getReceiver().size() > 0) {
            if (message.getMessage().getPIXConsumerMCCIIN000002UV01Request().getMCCIIN000002UV01().getReceiver().get(0).getDevice() != null &&
                    message.getMessage().getPIXConsumerMCCIIN000002UV01Request().getMCCIIN000002UV01().getReceiver().get(0).getDevice().getId() != null) {
                sourceID = message.getMessage().getPIXConsumerMCCIIN000002UV01Request().getMCCIIN000002UV01().getReceiver().get(0).getDevice().getId().get(0).getRoot();
                enterpriseID = message.getMessage().getPIXConsumerMCCIIN000002UV01Request().getMCCIIN000002UV01().getReceiver().get(0).getDevice().getId().get(0).getExtension();
                log.info("Setting ACK enterpriseID : " + enterpriseID + " sourceID : " + sourceID);
                AuditSourceIdentificationType auditSource = AuditDataTransformHelper.createAuditSourceIdentification(sourceID, enterpriseID);
                auditMsg.getAuditSourceIdentification().add(auditSource);
            }
        }
        // ParticipationObjectIdentification
        String objectID = null;
        if (message != null &&
                message.getMessage() != null &&
                message.getMessage().getPIXConsumerMCCIIN000002UV01Request() != null &&
                message.getMessage().getPIXConsumerMCCIIN000002UV01Request().getAssertion() != null) {
            AssertionType assertion = message.getMessage().getPIXConsumerMCCIIN000002UV01Request().getAssertion();
            if (assertion.getUniquePatientId() != null &&
                    assertion.getUniquePatientId().size() > 0)
                objectID = assertion.getUniquePatientId().get(0);
            log.debug("setting objectID for ACK " + objectID);
        }

        objectID = AuditDataTransformHelper.createCompositePatientId(sourceID, objectID);
        ParticipantObjectIdentificationType participantObject = AuditDataTransformHelper.createParticipantObjectIdentification(objectID);
        log.info("Setting ACK participantObject id : " + participantObject.getParticipantObjectID());

        // Put the contents of the actual message into the Audit Log Message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext(JAXB_HL7_CONTEXT_NAME);
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            marshaller.marshal(message.getMessage().getPIXConsumerMCCIIN000002UV01Request().getMCCIIN000002UV01(), baOutStrm);
            log.debug("Done marshalling the message.");

            participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);


        log.info("******************************************************************");
        log.info("Exiting transformAck2AuditMsg() method.");
        log.info("******************************************************************");

        response.setAuditMessage(auditMsg);
        return response;
    }

    public static LogEventRequestType transformPRPA2013092AuditMsg(LogSubjectReidentificationRequestType message) {
        AuditMessageType auditMsg = new AuditMessageType();
        LogEventRequestType response = new LogEventRequestType();
        response.setDirection(message.getDirection());
        response.setInterface(message.getInterface());

        log.info("******************************************************************");
        log.info("Entering transformPRPA2013092AuditMsg() method.");
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
        eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SRI, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SDRID, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SRI, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SDRID);
        auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_EXECUTE, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID));

        // Create Active Participant Section   
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        /* Assign AuditSourceIdentification */
        String communityId = "";
        String communityName = "";
        String patientId = "";

        if (message != null &&
                message.getMessage() != null &&
                message.getMessage().getPRPAIN201309UV02() != null) {
            log.debug("201309 is not null");
            if (message.getMessage().getPRPAIN201309UV02().getControlActProcess() != null &&
                    message.getMessage().getPRPAIN201309UV02().getControlActProcess().getQueryByParameter() != null) {
                log.debug("querybyparameter is not null");
                PRPAMT201307UV02QueryByParameter parameter = message.getMessage().getPRPAIN201309UV02().getControlActProcess().getQueryByParameter().getValue();
                if (parameter.getParameterList() != null &&
                        parameter.getParameterList().getPatientIdentifier() != null &&
                        parameter.getParameterList().getPatientIdentifier().size() > 0) {
                    log.debug("parameterList has " + parameter.getParameterList().getPatientIdentifier().size() + " element(s)");
                    org.hl7.v3.PRPAMT201307UV02PatientIdentifier patient = parameter.getParameterList().getPatientIdentifier().get(0);
                    if (patient.getValue() != null &&
                            patient.getValue().size() > 0) {
                        communityId = patient.getValue().get(0).getRoot();
                        patientId = patient.getValue().get(0).getExtension();
                    }
                }
            }
            patientId = AuditDataTransformHelper.createCompositePatientId(communityId, patientId);

        }

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
            JAXBContext jc = oHandler.getJAXBContext(JAXB_HL7_CONTEXT_NAME);
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            marshaller.marshal(message.getMessage().getPRPAIN201309UV02(), baOutStrm);
            log.debug("Done marshallaing HL7 message.");

            participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("EXCEPTION when marshalling 201309 : " + e);
            throw new RuntimeException();
        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        log.info("******************************************************************");
        log.info("Exiting transformPRPA2013092AuditMsg() method.");
        log.info("******************************************************************");

        response.setAuditMessage(auditMsg);
        return response;
    }
    
    public static LogEventRequestType transformPRPA2013102AuditMsg(LogSubjectReidentificationResponseType message) {
        AuditMessageType auditMsg = new AuditMessageType();
        LogEventRequestType response = new LogEventRequestType();
        response.setDirection(message.getDirection());
        response.setInterface(message.getInterface());

        log.info("******************************************************************");
        log.info("Entering transformPRPA2013102AuditMsg() method.");
        log.info("******************************************************************");

        // Extract UserInfo from Message.Assertion
        UserType userInfo = new UserType();
        if (message != null &&
                message.getMessage() != null &&
                message.getAssertion() != null &&
                message.getAssertion().getUserInfo() != null) {
            userInfo = message.getAssertion().getUserInfo();
        }

        // Create EventIdentification
        CodedValueType eventID = new CodedValueType();
        eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SRI, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SDRID, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SRI, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SDRID);
        auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_EXECUTE, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID));

        // Create Active Participant Section   
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        /* Assign AuditSourceIdentification */
        String communityId = "";
        String communityName = "";
        String patientId = "";

        if (message != null &&
                message.getMessage() != null &&
                message.getMessage().getPRPAIN201310UV02() != null) {
            PRPAMT201304UV02Patient patient = HL7Extractors.ExtractHL7PatientFromMessage(message.getMessage().getPRPAIN201310UV02());
            communityId = patient.getId().get(0).getRoot();
            patientId = patient.getId().get(0).getExtension();
            patientId = AuditDataTransformHelper.createCompositePatientId(communityId, patientId);

        }
    
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
            JAXBContext jc = oHandler.getJAXBContext(JAXB_HL7_CONTEXT_NAME);
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            marshaller.marshal(message.getMessage().getPRPAIN201310UV02(), baOutStrm);
            log.debug("Done marshalling the message.");

            participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("EXCEPTION when marshalling 201309 : " + e);
            throw new RuntimeException();
        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        log.info("******************************************************************");
        log.info("Exiting transformPRPA2013102AuditMsg() method.");
        log.info("******************************************************************");

        response.setAuditMessage(auditMsg);
        return response;
    }

}
