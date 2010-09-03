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
import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditMessageType.ActiveParticipant;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;


import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import javax.xml.bind.JAXBContext;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.Marshaller;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

/**
 *
 * @author dunnek
 */
public class AdminDistTransforms {
    private static final String JAXB_HL7_CONTEXT_NAME = "org.hl7.v3";
    private Log log = null;//LogFactory.getLog(PatientDiscoveryTransforms.class);

    public AdminDistTransforms()
    {
        log = createLogger();
    }

    /**
     * Instantiating log4j logger
     * @return
     */
    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    public LogEventRequestType transformEntitySendAlertToAuditMsg(RespondingGatewaySendAlertMessageType message, AssertionType assertion, String direction, String _interface) {
        LogEventRequestType oReturnLogEventRequestType = null;
        EDXLDistribution body = null;

        if (message == null) {
            log.error("The Incoming Send Alert message was Null");
            return null;
        } else {
            body = message.getEDXLDistribution();
        }


        if (body == null || assertion == null)
        {
            log.error("The SendAlert did not have an EDXLDistribution or Assertion Object ");
            return null;
        } 

        if ((message == null) || (assertion == null)) {
            log.error("The SendAlert did not have an EDXLDistribution or Assertion Object ");
            return null;
        } else {
            oReturnLogEventRequestType = transformEDXLDistributionRequestToAuditMsg(message.getEDXLDistribution(), assertion, direction, _interface);
        }

        if (oReturnLogEventRequestType == null) {
            log.error("There was a problem translating the request into an audit log request object.");
            oReturnLogEventRequestType = null;
        } else {
            oReturnLogEventRequestType.setDirection(direction);
            oReturnLogEventRequestType.setInterface(_interface);
        }

        log.info("Exiting transformEntityPRPAIN201305RequestToAuditMsg() method.");

        return oReturnLogEventRequestType;

    }

    public LogEventRequestType transformEDXLDistributionRequestToAuditMsg(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target, String direction, String _interface) {
    {

        LogEventRequestType result = null;

        AuditMessageType auditMsg = null;

        log.debug("Entering transformPRPAIN201305RequestToAuditMsg() method.");


        auditMsg = new AuditMessageType();

        //check to see that the required fields are not null
        boolean bRequiredFieldsAreNull = areRequiredUserTypeFieldsNull( assertion);
        if (bRequiredFieldsAreNull) {
            //TODO add a unit test case...
            log.error("One or more of the required fields needed to transform to an audit message request were null.");
            return null;
        } //else continue
        if(target == null || target.getHomeCommunity() == null || target.getHomeCommunity().getHomeCommunityId() == null)
        {
            log.error("One or more of the required fields needed to transform to an audit message request were null.");
        }

        // Extract UserInfo from request assertion
        UserType userInfo = assertion.getUserInfo();//new UserType();

        result = new LogEventRequestType();

        // Create EventIdentification
        CodedValueType eventID = new CodedValueType();
        eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_ADMIN_DIST, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_ADMIN_DIST);
        auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID));



        // Create Active Participant Section
        AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, true);
        auditMsg.getActiveParticipant().add(participant);

        /* Assign AuditSourceIdentification */
        String communityId = "";
        String communityName = "";

        communityId = target.getHomeCommunity().getHomeCommunityId();
        communityName = target.getHomeCommunity().getName();


        /* Create the AuditSourceIdentifierType object */
        AuditSourceIdentificationType auditSource = AuditDataTransformHelper.createAuditSourceIdentification(communityId, communityName);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        ParticipantObjectIdentificationType participantObject = AuditDataTransformHelper.createParticipantObjectIdentification("N/A");
        // Fill in the message field with the contents of the event message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext(oasis.names.tc.emergency.edxl.de._1.ObjectFactory.class, oasis.names.tc.emergency.edxl.de._1.EDXLDistribution.class);
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            marshaller.marshal(body, baOutStrm);
            log.debug("Done marshalling the message.");

            participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("EXCEPTION when marshalling Nhin Notify Request : " + e);
            throw new RuntimeException();
        }
        // Put the contents of the actual message into the Audit Log Message
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();


        participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        log.debug("Exiting transformEDXLDistributionRequestToAuditMsg() method.");


        return result;
    }
}
public LogEventRequestType transformEDXLDistributionRequestToAuditMsg(EDXLDistribution body, AssertionType assertion, String direction, String _interface) {
        LogEventRequestType result = null;

        AuditMessageType auditMsg = null;
        
        log.debug("Entering transformPRPAIN201305RequestToAuditMsg() method.");


        auditMsg = new AuditMessageType();

        //check to see that the required fields are not null
        boolean bRequiredFieldsAreNull = areRequiredUserTypeFieldsNull( assertion);
        if (bRequiredFieldsAreNull) {
            //TODO add a unit test case...
            log.error("One or more of the required fields needed to transform to an audit message request were null.");
            return null;
        } //else continue


        // Extract UserInfo from request assertion
        UserType userInfo = assertion.getUserInfo();//new UserType();

        result = new LogEventRequestType();

        // Create EventIdentification
        CodedValueType eventID = new CodedValueType();
        eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_ADMIN_DIST, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_ADMIN_DIST);
        auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE, AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID));



        // Create Active Participant Section        
        AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, true);
        auditMsg.getActiveParticipant().add(participant);
        
        /* Assign AuditSourceIdentification */
        String communityId = "";
        String communityName = "";

        communityId = userInfo.getOrg().getHomeCommunityId();
        communityName = userInfo.getOrg().getName();


        /* Create the AuditSourceIdentifierType object */
        AuditSourceIdentificationType auditSource = AuditDataTransformHelper.createAuditSourceIdentification(communityId, communityName);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        ParticipantObjectIdentificationType participantObject = AuditDataTransformHelper.createParticipantObjectIdentification("N/A");
        // Fill in the message field with the contents of the event message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext(oasis.names.tc.emergency.edxl.de._1.ObjectFactory.class, oasis.names.tc.emergency.edxl.de._1.EDXLDistribution.class);
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            marshaller.marshal(body, baOutStrm);
            log.debug("Done marshalling the message.");

            participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("EXCEPTION when marshalling Nhin Notify Request : " + e);
            throw new RuntimeException();
        }
        // Put the contents of the actual message into the Audit Log Message
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();


        participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        log.debug("Exiting transformEDXLDistributionRequestToAuditMsg() method.");


        return result;

    }
 protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion) {
        boolean bReturnVal = false;

        if ((oAssertion != null) &&
                (oAssertion.getUserInfo() != null)) {
            if (oAssertion.getUserInfo().getUserName() != null) {
                log.debug("Incomming request.getAssertion.getUserInfo.getUserName: " + oAssertion.getUserInfo().getUserName());
            } else {
                log.error("Incomming request.getAssertion.getUserInfo.getUserName was null.");
                bReturnVal = true;
                return true;
            }

            if (oAssertion.getUserInfo().getOrg().getHomeCommunityId() != null) {
                log.debug("Incomming request.getAssertion.getUserInfo.getOrg().getHomeCommunityId(): " + oAssertion.getUserInfo().getOrg().getHomeCommunityId());
            } else {
                log.error("Incomming request.getAssertion.getUserInfo.getOrg().getHomeCommunityId() was null.");
                bReturnVal = true;
                return true;
            }

            if (oAssertion.getUserInfo().getOrg().getName() != null) {
                log.debug("Incomming request.getAssertion.getUserInfo.getOrg().getName() or Community Name: " + oAssertion.getUserInfo().getOrg().getName());
            } else {
                log.error("Incomming request.getAssertion.getUserInfo.getOrg().getName() or Community Name was null.");
                bReturnVal = true;
                return true;
            }
        } else {
            log.error("The UserType object or request assertion object containing the assertion user info was null.");
            bReturnVal = true;
            return true;
        } //else continue

        return bReturnVal;
    }

}
