/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.transform.audit;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogNhinUnsubscribeRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
/**
 * Transforms for unsubscribe messages
 * 
 * @author Neil Webb
 */
public class UnsubscribeTransforms 
{
    private static Log log = LogFactory.getLog(SubscribeTransforms.class);
    
    public LogEventRequestType transformNhinUnsubscribeRequestToAuditMessage(LogNhinUnsubscribeRequestType message)
    {
        LogEventRequestType response = new LogEventRequestType();
        AuditMessageType auditMsg = new AuditMessageType();
        response.setDirection(message.getDirection());
        response.setInterface(message.getInterface());

        log.info("******************************************************************");
        log.info("Entering transformNhinUnsubscribeRequestToAuditMessage() method.");
        log.info("******************************************************************");

        // Extract UserInfo from Message.Assertion
        UserType userInfo = new UserType();
        if (message != null &&
                message.getMessage() != null &&
                message.getMessage().getAssertion() != null &&
                message.getMessage().getAssertion().getUserInfo() != null)
        {
            userInfo = message.getMessage().getAssertion().getUserInfo();
        }

        // Create EventIdentification
        CodedValueType eventID = new CodedValueType();
        eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SUB, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SUBSCRIBE, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SUB, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SUBSCRIBE);
        auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID));

        // Create Active Participant Section   
        if (userInfo != null)
        {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        /* Assign AuditSourceIdentification */
        String communityId = "";
        String communityName = "";
        String patientId = "";

        // An unsubscribe message does not contain a patient identifier.

        if (userInfo != null &&
                userInfo.getOrg() != null)
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

        AuditSourceIdentificationType auditSource = AuditDataTransformHelper.createAuditSourceIdentification(communityId, communityName);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = AuditDataTransformHelper.createParticipantObjectIdentification(patientId);

        // Fill in the message field with the contents of the event message
        try
        {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.subscription");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            gov.hhs.fha.nhinc.common.subscription.ObjectFactory factory = new gov.hhs.fha.nhinc.common.subscription.ObjectFactory();
            JAXBElement oJaxbElement = factory.createUnsubscribe(message.getMessage().getUnsubscribe());
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            log.debug("Done marshalling the message.");

            participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());

        } catch (Exception e)
        {
            e.printStackTrace();
            log.error("EXCEPTION when marshalling unsubscribe request: " + e);
            throw new RuntimeException();
        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        response.setAuditMessage(auditMsg);

        log.info("******************************************************************");
        log.info("Exiting transformNhinUnsubscribeRequestToAuditMessage() method.");
        log.info("******************************************************************");

        return response;
    }
    
    public LogEventRequestType transformUnsubscribeResponseToGenericAudit(gov.hhs.fha.nhinc.common.hiemauditlog.LogUnsubscribeResponseType message)
    {
        LogEventRequestType response = new LogEventRequestType();
        AuditMessageType auditMsg = new AuditMessageType();
        response.setDirection(message.getDirection());
        response.setInterface(message.getInterface());

        log.info("******************************************************************");
        log.info("Entering transformUnsubscribeResponseToGenericAudit() method.");
        log.info("******************************************************************");

        // Extract UserInfo from Message.Assertion
        UserType userInfo = new UserType();
        if (message != null &&
                message.getMessage() != null &&
                message.getMessage().getAssertion() != null &&
                message.getMessage().getAssertion().getUserInfo() != null)
        {
            userInfo = message.getMessage().getAssertion().getUserInfo();
        }

        // Create EventIdentification
        CodedValueType eventID = new CodedValueType();
        eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SUB, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SUBSCRIBE, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SUB, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SUBSCRIBE);
        auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID));

        // Create Active Participant Section   
        if (userInfo != null)
        {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        /* Assign AuditSourceIdentification */
        String communityId = "";
        String communityName = "";
        String patientId = "";

        // An unsubscribe response message does not contain a patient identifier.

        if (userInfo != null &&
                userInfo.getOrg() != null)
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

        AuditSourceIdentificationType auditSource = AuditDataTransformHelper.createAuditSourceIdentification(communityId, communityName);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = AuditDataTransformHelper.createParticipantObjectIdentification(patientId);

        // Fill in the message field with the contents of the event message
        try
        { //org.oasis_open.docs.wsn.b_2
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.subscription");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            gov.hhs.fha.nhinc.common.subscription.ObjectFactory factory = new gov.hhs.fha.nhinc.common.subscription.ObjectFactory();
            marshaller.marshal(message.getMessage().getUnsubscribeResponse(), baOutStrm);
            log.debug("Done marshalling the message.");

            participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());

        } catch (Exception e)
        {
            e.printStackTrace();
            log.error("EXCEPTION when marshalling unsubscribe response: " + e);
            throw new RuntimeException();
        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        response.setAuditMessage(auditMsg);

        log.info("******************************************************************");
        log.info("Exiting transformUnsubscribeResponseToGenericAudit() method.");
        log.info("******************************************************************");

        return response;
    }
    
}
