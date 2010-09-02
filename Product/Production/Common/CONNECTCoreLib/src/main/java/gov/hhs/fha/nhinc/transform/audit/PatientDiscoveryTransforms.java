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

import com.services.nhinc.schema.auditmessage.AuditMessageType.ActiveParticipant;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import javax.xml.bind.JAXBContext;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.Marshaller;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author shawc
 *
 * Note: requirements for the LogEventRequestType
 * "Direction", required = true
 * "Interface", required = true
 * "EventIdentification", required = true
 * "ActiveParticipant", required = true
 * "AuditSourceIdentification", required = true
 * "ParticipantObjectIdentification", required = false
 */
public class PatientDiscoveryTransforms {

    private static final String JAXB_HL7_CONTEXT_NAME = "org.hl7.v3";
    private Log log = null;//LogFactory.getLog(PatientDiscoveryTransforms.class);

    /**
     * Default Constructor
     */
    public PatientDiscoveryTransforms() {
        log = createLogger();
    }

    /**
     * This method tranforms a patient discovery request into an audit log message
     * but it leaves the direction decision needed for the audit log up to the
     * caller.
     *
     * @param message
     * @return
     */
    private LogEventRequestType transformPRPAIN201305RequestToAuditMsg(PRPAIN201305UV02 oPatientDiscoveryRequestMessage, AssertionType oAssertion, String direction, String _interface) {
        LogEventRequestType oReturnLogEventRequestType = null;

        AuditMessageType oAuditMessageType = null;

        addLogDebug("*********************************************************");
        addLogDebug("Entering transformPRPAIN201305RequestToAuditMsg() method.");
        addLogDebug("*********************************************************");

        oAuditMessageType = new AuditMessageType();

        //check to see that the required fields are not null
        boolean bRequiredFieldsAreNull = areRequired201305fieldsNull(oPatientDiscoveryRequestMessage, oAssertion);
        if (bRequiredFieldsAreNull) {
            //TODO add a unit test case...
            addLogError("One or more of the required fields needed to transform to an audit message request were null.");
            return null;
        } //else continue

        // Extract UserInfo from request assertion
        UserType oUserInfo = oAssertion.getUserInfo();//new UserType();

        oReturnLogEventRequestType = new LogEventRequestType();

        // Create EventIdentification
        CodedValueType eventID = getCodedValueTypeFor201305UV();

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        oAuditMessageType.setEventIdentification(oEventIdentificationType);
        addLogDebug("Audit record EventIdentificationType.getEventActionCode(): " + oEventIdentificationType.getEventActionCode());

        /* Create Active Participant Section */
        ActiveParticipant participant = getActiveParticipant(oUserInfo);
        oAuditMessageType.getActiveParticipant().add(participant);
        addLogDebug("Audit record AuditMessageType.getActiveParticipant().get(0).getUserName(): " + oAuditMessageType.getActiveParticipant().get(0).getUserName());

        /* Assign AuditSourceIdentification */
        String sCommunityId = "";
        String sCommunityName = "";
        String sPatientId = "";

        II oII = getHL7IdentifiersFromRequest(oPatientDiscoveryRequestMessage); //null values checked from the earlier call to areRequired201305fieldsNull() method
        if (oII != null) {
            sPatientId = oII.getExtension();
            sCommunityId = oII.getRoot();
            sPatientId = getCompositePatientId(sCommunityId, sPatientId);
        }

        addLogDebug("PatientId: " + sPatientId);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType(sPatientId);

        //resetting the community name and id information for the audit
        //source identification
        if (oUserInfo != null && oUserInfo.getOrg() != null) {
            if (oUserInfo.getOrg().getHomeCommunityId() != null) {
                sCommunityId = oUserInfo.getOrg().getHomeCommunityId();
            }
            if (oUserInfo.getOrg().getName() != null) {
                sCommunityName = oUserInfo.getOrg().getName();
            }
        }

        /* Create the AuditSourceIdentifierType object */
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(sCommunityId, sCommunityName);
        oAuditMessageType.getAuditSourceIdentification().add(auditSource);
        addLogDebug("Audit record AuditMessageType.getAuditSourceIdentification().get(0).getAuditSourceID(): " + oAuditMessageType.getAuditSourceIdentification().get(0).getAuditSourceID());

        // Put the contents of the actual message into the Audit Log Message
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
        marshalPatientDiscoveryMessage(baOutStrm, oPatientDiscoveryRequestMessage);
        participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        oAuditMessageType.getParticipantObjectIdentification().add(participantObject);
        addLogDebug("Audit record AuditMessageType.getParticipantObjectIdentification().get(0).getAuditSourceID(): " + oAuditMessageType.getParticipantObjectIdentification().get(0).getParticipantObjectID());
        addLogDebug("Audit record AuditMessageType.getParticipantObjectIdentification().get(0).getParticipantObjectName(): " + oAuditMessageType.getParticipantObjectIdentification().get(0).getParticipantObjectName());
        addLogDebug("Audit record AuditMessageType.getParticipantObjectIdentification().get(0).getParticipantObjectSensitivity(): " + oAuditMessageType.getParticipantObjectIdentification().get(0).getParticipantObjectSensitivity());

        oReturnLogEventRequestType.setAuditMessage(oAuditMessageType);
        oReturnLogEventRequestType.setDirection(direction);
        oReturnLogEventRequestType.setInterface(_interface);

        addLogDebug("*********************************************************");
        addLogDebug("Exiting transformPRPAIN201305RequestToAuditMsg() method.");
        addLogDebug("*********************************************************");

        return oReturnLogEventRequestType;

    }

    protected PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent getRegistrationEventFromSubject(List<PRPAIN201306UV02MFMIMT700711UV01Subject1> oSubject1) {
        PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent oRegistrationEvent = oSubject1.get(0).getRegistrationEvent();
        return oRegistrationEvent;
    }

    /**
     * This method translates a patient discovery response to an audit log request.
     * It leave the direction decision needed for the audit log up to the caller.
     *
     * @param oRequest
     * @return
     */
    protected LogEventRequestType transformPRPAIN201306ResponseToAuditMsg(PRPAIN201306UV02 oPatientDiscoveryResponseMessage, AssertionType oAssertion, String direction, String _interface) {
        LogEventRequestType oReturnLogEventRequestType = null;
        AuditMessageType oAuditMessageType = null;
//        PRPAIN201306UV oPatientDiscoveryResponseMessage = null;

        addLogInfo("******************************************************************");
        addLogInfo("Entering transformPRPAIN201306ResponseToAuditMsg() method.");
        addLogInfo("******************************************************************");

        //check to see that the required fields are not null
        boolean bRequiredFieldsAreNull = areRequired201306fieldsNull(oPatientDiscoveryResponseMessage, oAssertion);
        if (bRequiredFieldsAreNull) {
            addLogError("One or more of the required fields needed to transform to an audit message request were null.");
            return null;
        } //else continue

        // Extract UserInfo from request assertion
        UserType oUserInfo = oAssertion.getUserInfo();

        oAuditMessageType = new AuditMessageType();
        oReturnLogEventRequestType = new LogEventRequestType();

        CodedValueType eventID = getCodedValueTypeFor201306UV();
        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        oAuditMessageType.setEventIdentification(oEventIdentificationType);

        // Create Active Participant Section
        ActiveParticipant participant = getActiveParticipant(oUserInfo);
        oAuditMessageType.getActiveParticipant().add(participant);
        addLogDebug("Audit record AuditMessageType.getActiveParticipant().get(0).getUserName(): " + oAuditMessageType.getActiveParticipant().get(0).getUserName());

        /* Assign AuditSourceIdentification */
        String sCommunityId = "";
        String sCommunityName = "";
        String sPatientId = "";
        //get the patient id from the response if present
        II oII = getHL7IdentitiersFromResponse(oPatientDiscoveryResponseMessage);
        if (oII != null) {
            sPatientId = oII.getExtension();
            sCommunityId = oII.getRoot();
        }
        sPatientId = getCompositePatientId(sCommunityId, sPatientId);
        addLogDebug("PatientId: " + sPatientId);


        //reset the community id and name to that found on the assertion??
        if (oUserInfo != null &&
                oUserInfo.getOrg() != null) {
            if (oUserInfo.getOrg().getHomeCommunityId() != null) {
                sCommunityId = oUserInfo.getOrg().getHomeCommunityId();
            }
            if (oUserInfo.getOrg().getName() != null) {
                sCommunityName = oUserInfo.getOrg().getName();
            }
        }

        AuditSourceIdentificationType oAuditSource = getAuditSourceIdentificationType(sCommunityId, sCommunityName);
        oAuditMessageType.getAuditSourceIdentification().add(oAuditSource);
        addLogDebug("Audit record AuditMessageType.getAuditSourceIdentification().get(0).getAuditSourceID(): " + oAuditMessageType.getAuditSourceIdentification().get(0).getAuditSourceID());

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType(sPatientId);

        // Put the contents of the actual message into the Audit Log Message
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
        marshalPatientDiscoveryMessage(baOutStrm, oPatientDiscoveryResponseMessage);
        participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        oAuditMessageType.getParticipantObjectIdentification().add(participantObject);
        addLogDebug("Audit record AuditMessageType.getParticipantObjectIdentification().get(0).getAuditSourceID(): " + oAuditMessageType.getParticipantObjectIdentification().get(0).getParticipantObjectID());
        addLogDebug("Audit record AuditMessageType.getParticipantObjectIdentification().get(0).getParticipantObjectName(): " + oAuditMessageType.getParticipantObjectIdentification().get(0).getParticipantObjectName());
        addLogDebug("Audit record AuditMessageType.getParticipantObjectIdentification().get(0).getParticipantObjectSensitivity(): " + oAuditMessageType.getParticipantObjectIdentification().get(0).getParticipantObjectSensitivity());

        addLogInfo("******************************************************************");
        addLogInfo("Exiting transformPRPAIN201306ResponseToAuditMsg() method.");
        addLogInfo("******************************************************************");

        oReturnLogEventRequestType.setAuditMessage(oAuditMessageType);
        oReturnLogEventRequestType.setDirection(direction);
        oReturnLogEventRequestType.setInterface(_interface);

        return oReturnLogEventRequestType;
    }

    protected PRPAMT201310UV02Patient getPatient(PRPAIN201306UV02MFMIMT700711UV01Subject2 oSubject2) {
        PRPAMT201310UV02Patient oPatient = oSubject2.getPatient();
        return oPatient;
    }

    /**
     * this method tranforms a patient discovery request from an inbound entity into an audit log message.
     * @param message
     * @return
     */
    public LogEventRequestType transformEntityPRPAIN201305RequestToAuditMsg(RespondingGatewayPRPAIN201305UV02RequestType oRequest, AssertionType oAssertion, String direction, String _interface) {
        LogEventRequestType oReturnLogEventRequestType = null;
        PRPAIN201305UV02 oPatientDiscoveryRequestMessage = null;

        addLogInfo("***************************************************************");
        addLogInfo("Entering transformEntityPRPAIN201305RequestToAuditMsg() method.");
        addLogInfo("***************************************************************");

        if (oRequest == null) {
            addLogError("The incomming Patient Discovery request message was null.");
            return null;
        } else {
            oPatientDiscoveryRequestMessage = oRequest.getPRPAIN201305UV02();
        }

        if ((oPatientDiscoveryRequestMessage == null) && (oAssertion == null)) {
            addLogError("The Patient Discovery request did not have a PRPAIN201305UV object or an AssertionType object.");
            return null;
        } else {
            oReturnLogEventRequestType = transformPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequestMessage, oAssertion, direction, _interface);
        }

        if (oReturnLogEventRequestType == null) {
            addLogError("There was a problem translating the request into an audit log request object.");
            oReturnLogEventRequestType = null;
        } else {
            oReturnLogEventRequestType.setDirection(direction);
            oReturnLogEventRequestType.setInterface(_interface);
        }

        addLogInfo("**************************************************************");
        addLogInfo("Exiting transformEntityPRPAIN201305RequestToAuditMsg() method.");
        addLogInfo("**************************************************************");

        return oReturnLogEventRequestType;

    }

    /**
     * this method tranforms a patient discovery request from an inbound NHIN into an audit log message.
     * @param message
     * @return
     */
    public LogEventRequestType transformNhinPRPAIN201305RequestToAuditMsg(PRPAIN201305UV02 oRequest, AssertionType oAssertion, String direction, String _interface) {
        LogEventRequestType oReturnLogEventRequestType = null;

        addLogInfo("************************************************");
        addLogInfo("Entering transformPRPA201305ToAuditMsg() method.");
        addLogInfo("************************************************");

        if (oRequest == null) {
            addLogError("The incomming Patient Discovery request message was null.");
            return null;
        } //else continue

        if (oAssertion == null) {
            addLogError("The AssertionType object was null.");
            return null;
        } //else continue

        oReturnLogEventRequestType = transformPRPAIN201305RequestToAuditMsg(oRequest, oAssertion, direction, _interface);


        if (oReturnLogEventRequestType == null) {
            addLogError("There was a problem translating the request into an audit log request object.");
            return null;
        }

        addLogInfo("**************************************************************");
        addLogInfo("Exiting transformEntityPRPAIN201305RequestToAuditMsg() method.");
        addLogInfo("**************************************************************");

        return oReturnLogEventRequestType;

    }

    /**
     * this method tranforms a patient discovery request from an inbound Adapter (pass-through mode) into an audit log message.
     * @param message
     * @return
     */
    public LogEventRequestType transformAdapterPRPAIN201305RequestToAuditMsg(PRPAIN201305UV02 oRequest, AssertionType oAssertion, String direction, String _interface) {
        LogEventRequestType oReturnLogEventRequestType = null;


        addLogInfo("************************************************");
        addLogInfo("Entering transformPRPA201305ToAuditMsg() method.");
        addLogInfo("************************************************");

        if (oRequest == null) {
            addLogError("The incomming Patient Discovery request message was null.");
            return null;
        } //else continue

        if (oAssertion == null) {
            addLogError("The AssertionType object was null.");
            return null;
        } //else continue

        oReturnLogEventRequestType = transformPRPAIN201305RequestToAuditMsg(oRequest, oAssertion, direction, _interface);


        if (oReturnLogEventRequestType == null) {
            addLogError("There was a problem translating the request into an audit log request object.");
            return null;
        }

        addLogInfo("**************************************************************");
        addLogInfo("Exiting transformEntityPRPAIN201305RequestToAuditMsg() method.");
        addLogInfo("**************************************************************");

        return oReturnLogEventRequestType;

    }

    public LogEventRequestType transformEntityPRPAIN201306ResponseToAuditMsg(List<CommunityPRPAIN201306UV02ResponseType> oRequest, AssertionType oAssertion, String direction, String _interface) {

        LogEventRequestType oReturnLogEventRequestType = null;

        for (CommunityPRPAIN201306UV02ResponseType response : oRequest) {
            oReturnLogEventRequestType = transformNhinPRPAIN201306ResponseToAuditMsg(response.getPRPAIN201306UV02(), oAssertion, direction, _interface);

        }

        return oReturnLogEventRequestType;
    }

    public LogEventRequestType transformEntityPRPAIN201306AsyncResponseToAuditMsg(RespondingGatewayPRPAIN201306UV02RequestType oRequest, AssertionType oAssertion, String direction, String _interface) {

        LogEventRequestType oReturnLogEventRequestType = null;

        oReturnLogEventRequestType = transformNhinPRPAIN201306ResponseToAuditMsg(oRequest.getPRPAIN201306UV02(), oAssertion, direction, _interface);

        return oReturnLogEventRequestType;
    }

    /**
     * this method tranforms a patient discovery response for an outbound Entity into an audit log message.
     * @param message
     * @return
     */
    public LogEventRequestType transformEntityPRPAIN201306ResponseToAuditMsg(RespondingGatewayPRPAIN201306UV02ResponseType oRequest, AssertionType oAssertion, String direction, String _interface) {
        LogEventRequestType oReturnLogEventRequestType = null;
        List<CommunityPRPAIN201306UV02ResponseType> communityResponses = null;

        addLogInfo("****************************************************************");
        addLogInfo("Entering transformEntityPRPAIN201306ResponseToAuditMsg() method.");
        addLogInfo("****************************************************************");

        if (oRequest == null) {
            addLogError("The Patient Discovery response message was null.");
            return null;
        } else {
            communityResponses = oRequest.getCommunityResponse();
        }

        if (communityResponses.size() == 0) {
            addLogError("The PRPAIN201306UV object from the request was null.");
            return null;
        }

        if (oAssertion == null) {
            addLogError("The assertion object parameter was null.");
            return null;
        } else {
            oReturnLogEventRequestType = transformEntityPRPAIN201306ResponseToAuditMsg(communityResponses, oAssertion, direction, _interface);
        }

        if (oReturnLogEventRequestType == null) {
            addLogError("There was a problem translating the response message to an audit log object");
            return null;
        }

        addLogInfo("***************************************************************");
        addLogInfo("Exiting transformEntityPRPAIN201306ResponseToAuditMsg() method.");
        addLogInfo("***************************************************************");

        return oReturnLogEventRequestType;
    }

    /**
     * this method tranforms a patient discovery response for an outbound Nhin response into an audit log message.
     * @param message
     * @return
     */
    public LogEventRequestType transformNhinPRPAIN201306ResponseToAuditMsg(PRPAIN201306UV02 oResponseMessage, AssertionType oAssertion, String direction, String _interface) {
        LogEventRequestType oReturnLogEventRequestType = null;

        addLogInfo("****************************************************************");
        addLogInfo("Entering transformNhinPRPAIN201306ResponseToAuditMsg() method.");
        addLogInfo("****************************************************************");

        if (oResponseMessage == null) {
            addLogError("The Patient Discovery response message was null.");
            return null;
        }

        if (oAssertion == null) {
            addLogError("The assertionType object parameter was null.");
            return null;
        } else {
            oReturnLogEventRequestType = transformPRPAIN201306ResponseToAuditMsg(oResponseMessage, oAssertion, direction, _interface);
        }

        if (oReturnLogEventRequestType == null) {
            addLogError("There was a problem translating the response message to an audit log object");
            return null;
        } else {
            oReturnLogEventRequestType.setDirection(direction);
            oReturnLogEventRequestType.setInterface(_interface);
        }

        addLogInfo("***************************************************************");
        addLogInfo("Exiting transformNhinPRPAIN201306ResponseToAuditMsg() method.");
        addLogInfo("***************************************************************");

        return oReturnLogEventRequestType;
    }

    /**
     * this method tranforms a patient discovery response for an outbound Adapter response into an audit log message.
     * @param message
     * @return
     */
    public LogEventRequestType transformAdapterPRPAIN201306ResponseToAuditMsg(PRPAIN201306UV02 oResponseMessage, AssertionType oAssertion, String direction, String _interface) {
        LogEventRequestType oReturnLogEventRequestType = null;

        addLogInfo("****************************************************************");
        addLogInfo("Entering transformAdapterPRPAIN201306ResponseToAuditMsg() method.");
        addLogInfo("****************************************************************");

        if (oResponseMessage == null) {
            addLogError("The Patient Discovery response message was null.");
            return null;
        }

        if (oAssertion == null) {
            addLogError("The assertionType object parameter was null.");
            return null;
        } else {
            oReturnLogEventRequestType = transformPRPAIN201306ResponseToAuditMsg(oResponseMessage, oAssertion, direction, _interface);
        }

        if (oReturnLogEventRequestType == null) {
            addLogError("There was a problem translating the response message to an audit log object");
            return null;
        } else {
            oReturnLogEventRequestType.setDirection(direction);
            oReturnLogEventRequestType.setInterface(_interface);
        }

        addLogInfo("***************************************************************");
        addLogInfo("Exiting transformAdapterPRPAIN201306ResponseToAuditMsg() method.");
        addLogInfo("***************************************************************");

        return oReturnLogEventRequestType;
    }

    /**
     * Instantiating log4j logger
     * @return
     */
    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected ActiveParticipant getActiveParticipant(UserType oUserInfo) {
        // Create Active Participant Section
        //create a method to call the AuditDataTransformHelper - one expectation
        AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(oUserInfo, true);
        return participant;
    }

    protected AuditSourceIdentificationType getAuditSourceIdentificationType(String sCommunityId, String sCommunityName) {
        AuditSourceIdentificationType auditSource = AuditDataTransformHelper.createAuditSourceIdentification(sCommunityId, sCommunityName);
        return auditSource;
    }

    protected CodedValueType getCodedValueTypeFor201305UV() {
        // Create EventIdentification
        CodedValueType eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_PRQ,
                AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_PDREQ,
                AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_PRQ,
                AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_PDREQ);
        return eventID;
    }

    protected CodedValueType getCodedValueTypeFor201306UV() {
        //else continue
        // Create EventIdentification
        CodedValueType eventID = new CodedValueType();
        eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_PRS, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_PDRES, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_PRS, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_PDRES);
        return eventID;
    }

    protected String getCompositePatientId(String sCommunityId, String sPatientId) {
        sPatientId = AuditDataTransformHelper.createCompositePatientId(sCommunityId, sPatientId);
        return sPatientId;
    }

    protected EventIdentificationType getEventIdentificationType(CodedValueType eventID) {
        EventIdentificationType oEventIdentificationType =
                AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_READ,
                AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS,
                eventID);
        return oEventIdentificationType;
    }

    protected ParticipantObjectIdentificationType getParticipantObjectIdentificationType(String sPatientId) {
        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = AuditDataTransformHelper.createParticipantObjectIdentification(sPatientId);
        return participantObject;
    }

    protected void marshalPatientDiscoveryMessage(ByteArrayOutputStream baOutStrm, Object oPatientDiscoveryMessage) throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("org.hl7.v3");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();
            marshaller.marshal(oPatientDiscoveryMessage, baOutStrm);
            log.debug("Done marshalling the message.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    //since it is not required to have a patient id to persist an audit log record this method is not required
    protected II getHL7IdentifiersFromRequest(PRPAIN201305UV02 oPatientDiscoveryRequestMessage) {
        II oII = null;

        if (oPatientDiscoveryRequestMessage == null) {
            addLogInfo("The request parameter object for the getHL7IdentifiersFromRequest() method is null.");
            return null;
        }

        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess oControlActProcess =
                oPatientDiscoveryRequestMessage.getControlActProcess();
        if (oControlActProcess == null) {
            addLogInfo("The ControlActProcess object was missing from the request");
            return null;
        }

        JAXBElement<PRPAMT201306UV02QueryByParameter> oQueryByParameter =
                oControlActProcess.getQueryByParameter();
        if (oQueryByParameter == null) {
            addLogInfo("The QueryByParameter object was missing from the request");
            return null;
        } else {
            oII = getHL7Identifiers(oQueryByParameter);
        }

        return oII;
    }

    private void addLogInfo(String message) {
        log.info(message);
    }

    private void addLogDebug(String message) {
        log.debug(message);
    }

    private void addLogError(String message) {
        log.error(message);
    }

    /**
     * This method checks to see if all of the audit log request values are
     * in the patient discovery request.
     *
     * Note: requirements for the LogEventRequestType
     * "Direction", required = true
     * "Interface", required = true
     * "EventIdentification", required = true
     * "ActiveParticipant", required = true
     * "AuditSourceIdentification", required = true
     * "ParticipantObjectIdentification", required = false
     *
     * @param oPatientDiscoveryRequestMessage
     * @return
     */
    protected boolean areRequired201305fieldsNull(PRPAIN201305UV02 oPatientDiscoveryRequestMessage, AssertionType oAssertion) {
        boolean bReturnVal = false;

        //check the userInfo object from the assertion object
        bReturnVal = areRequiredUserTypeFieldsNull(oAssertion);
        if (bReturnVal) {
            addLogError("One of more UserInfo fields from the Assertion object were null.");
            bReturnVal = true;
            return true;
        }

        // Create EventIdentification - values are not comming from the request, they are internal to this class

        // Create Active Participant Section - comes from the UserType/UserInfo object which is checked earlier

        // Assign AuditSourceIdentification - patient id may be blank
//        II oII = getHL7IdentifiersFromRequest(oPatientDiscoveryRequestMessage);
//        if (oII == null)
//        {
//            return true; //error log messages are created from the method above so no need to duplicate here
//        }

        //AuditSourceIdentification - comes from the UserType/UserInfo object which is checked earlier

        return bReturnVal;
    }

    protected boolean areRequired201306fieldsNull(PRPAIN201306UV02 oPatientDiscoveryResponseMessage, AssertionType oAssertion) {
        boolean bReturnVal = false;

        //check the userInfo object from the assertion object
        bReturnVal = areRequiredUserTypeFieldsNull(oAssertion);
        if (bReturnVal) {
            addLogError("One of more UserInfo fields from the Assertion object were null.");
            bReturnVal = true;
            return true;
        }

        // Create EventIdentification - values are not comming from the request, they are internal to this class

        // Create Active Participant Section - comes from the UserType/UserInfo object which is checked earlier

        /* Assign AuditSourceIdentification */
        II oII = getHL7IdentitiersFromResponse(oPatientDiscoveryResponseMessage);
        if (oII == null) {
            addLogInfo("The response message's II object required for translating to the audit request messasge's AuditSourceIdentification object was null.");
            return true;
        }

        String sPatientId = oII.getExtension();
        String sCommunityId = oII.getRoot();
        if (sPatientId == null) {
            addLogInfo("The patient id from the II.getExtension method from the response message's II object was null.");
            return true;
        } //else continue

        if (sCommunityId == null) {
            addLogInfo("The patient's assigning authority or community id from the response message's II object was null.");
            return true;
        }


        //AuditSourceIdentification - comes from the UserType/UserInfo object which is checked earlier

        return bReturnVal;
    }

    protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion) {
        boolean bReturnVal = false;

        if ((oAssertion != null) &&
                (oAssertion.getUserInfo() != null)) {
            if (oAssertion.getUserInfo().getUserName() != null) {
                addLogDebug("Incomming request.getAssertion.getUserInfo.getUserName: " + oAssertion.getUserInfo().getUserName());
            } else {
                addLogError("Incomming request.getAssertion.getUserInfo.getUserName was null.");
                bReturnVal = true;
                return true;
            }

            if (oAssertion.getUserInfo().getOrg().getHomeCommunityId() != null) {
                addLogDebug("Incomming request.getAssertion.getUserInfo.getOrg().getHomeCommunityId(): " + oAssertion.getUserInfo().getOrg().getHomeCommunityId());
            } else {
                addLogError("Incomming request.getAssertion.getUserInfo.getOrg().getHomeCommunityId() was null.");
                bReturnVal = true;
                return true;
            }

            if (oAssertion.getUserInfo().getOrg().getName() != null) {
                addLogDebug("Incomming request.getAssertion.getUserInfo.getOrg().getName() or Community Name: " + oAssertion.getUserInfo().getOrg().getName());
            } else {
                addLogError("Incomming request.getAssertion.getUserInfo.getOrg().getName() or Community Name was null.");
                bReturnVal = true;
                return true;
            }
        } else {
            addLogError("The UserType object or request assertion object containing the assertion user info was null.");
            bReturnVal = true;
            return true;
        } //else continue

        return bReturnVal;
    }

    protected II getHL7IdentitiersFromResponse(PRPAIN201306UV02 oPatientDiscoveryResponseMessage) {
        II oII = null;

        if (oPatientDiscoveryResponseMessage == null) {
            addLogInfo("Unable to extract patient identifiers from the response message due to a null value.");
            return null;
        } //else continue

        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oControlActProcess = oPatientDiscoveryResponseMessage.getControlActProcess();

        if (oControlActProcess == null) {
            addLogInfo("Unable to extract patient identifiers from the response message's ControlActProcess object due to a null value.");
            return null;
        } //else continue

        JAXBElement<PRPAMT201306UV02QueryByParameter> oQueryByParameter = oControlActProcess.getQueryByParameter();
        if (oQueryByParameter == null) {
            addLogInfo("The QueryByParameter object was missing from the response");
            return null;
        }
//        else
//        {
//            oII = getHL7Identifiers(oQueryByParameter);
//        }

        List<PRPAIN201306UV02MFMIMT700711UV01Subject1> oSubject1 = oControlActProcess.getSubject();

        if ((oSubject1 == null) || (oSubject1.size() < 1)) {
            addLogInfo("Unable to extract patient identifiers from the response message's Subject1 object due to a null or empty value.");
            return null;
        } //else continue

        PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent oRegistrationEvent = oSubject1.get(0).getRegistrationEvent();

        if (oRegistrationEvent == null) {
            addLogInfo("Unable to extract patient identifiers from the response message's RegistrationEvent object due to a null value.");
            return null;
        } //else continue

        PRPAIN201306UV02MFMIMT700711UV01Subject2 oSubject2 = oRegistrationEvent.getSubject1();

        if (oSubject2 == null) {
            addLogInfo("Unable to extract patient identifiers from the response message's Subject2 object due to a null value.");
            return null;
        } //else continue

        PRPAMT201310UV02Patient oPatient = oSubject2.getPatient();

        if (oPatient == null) {
            addLogInfo("Unable to extract patient identifiers from the response message's Patient object due to a null value.");
            return null;
        } //else continue

        List<II> olII = oPatient.getId();

        if ((olII == null) || (olII.isEmpty()) || (olII.size() < 1)) {
            addLogInfo("Unable to extract patient identifiers from the response message's II List object due to a null or empty value.");
            return null;
        } //else continue

        oII = olII.get(0);

        return oII;
    }

    protected II getHL7Identifiers(JAXBElement<PRPAMT201306UV02QueryByParameter> oQueryByParameter) {

        addLogDebug("Entering PatientDiscoveryTransforms.getHL7Identifiers method...");

        II oII = null;

        if (oQueryByParameter == null) {
            addLogInfo("The QueryByParameter object was null");
            return null;
        }

        if (oQueryByParameter.getValue() == null) {
            addLogInfo("The QueryByParameter value object was null");
            return null;
        }

        if (oQueryByParameter.getValue().getParameterList() == null) {
            addLogInfo("The ParameterList object was null");
            return null;
        }

        PRPAMT201306UV02ParameterList oParamList =
                oQueryByParameter.getValue().getParameterList();

        if (oParamList.getLivingSubjectId() == null) {
            addLogInfo("The LivingSubjectId list object was null");
            return null;
        }

        List<PRPAMT201306UV02LivingSubjectId> oLivingSubjectId = oParamList.getLivingSubjectId();
        if (NullChecker.isNullish(oLivingSubjectId)) {
            addLogInfo("The LivingSubjectId object was null");
            return null;
        }

        if (oLivingSubjectId.get(0) == null) {
            addLogInfo("oLivingSubjectId.get(0) == null");
            return null;
        }

        if (oLivingSubjectId.get(0).getValue() == null) {
            addLogInfo("oLivingSubjectId.get(0).getValue() == null");
            return null;
        }

        if (oLivingSubjectId.get(0).getValue().isEmpty()) {
            addLogInfo("oLivingSubjectId.get(0).getValue().isEmpty()");
            return null;
        }

        if (oLivingSubjectId.get(0).getValue().get(0) == null) {
            addLogInfo("oLivingSubjectId.get(0).getValue().get(0) == null");
            return null;
        }

        //all required fields have been checked, get the value.
        oII = oLivingSubjectId.get(0).getValue().get(0);

        addLogDebug("Exiting PatientDiscoveryTransforms.getHL7Identifiers method...");
        return oII;
    }

    public LogEventRequestType transformAck2AuditMsg(MCCIIN000002UV01 message, AssertionType assertion, String direction, String _interface) {

        addLogDebug("Entering PatientDiscoveryTransforms.transformAck2AuditMsg method...");

        AuditMessageType auditMsg = new AuditMessageType();
        LogEventRequestType response = new LogEventRequestType();
        response.setDirection(direction);
        response.setInterface(_interface);

        // Extract UserInfo from Message.Assertion
        UserType userInfo = new UserType();
        if (assertion != null &&
                assertion.getUserInfo() != null) {
            userInfo = assertion.getUserInfo();
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
                NullChecker.isNotNullish(message.getReceiver())) {
            if (message.getReceiver().get(0).getDevice() != null &&
                    message.getReceiver().get(0).getDevice().getId() != null) {
                sourceID = message.getReceiver().get(0).getDevice().getId().get(0).getRoot();
                enterpriseID = message.getReceiver().get(0).getDevice().getId().get(0).getExtension();

                log.info("Setting ACK enterpriseID : " + enterpriseID + " sourceID : " + sourceID);

                AuditSourceIdentificationType auditSource = AuditDataTransformHelper.createAuditSourceIdentification(sourceID, enterpriseID);
                auditMsg.getAuditSourceIdentification().add(auditSource);
            }
        }
        // ParticipationObjectIdentification
        String objectID = null;
        if (assertion != null &&
                NullChecker.isNotNullish(assertion.getUniquePatientId())) {
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
            marshaller.marshal(message, baOutStrm);
            log.debug("Done marshalling the message.");

            participantObject.setParticipantObjectQuery(baOutStrm.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        addLogDebug("Exiting PatientDiscoveryTransforms.transformAck2AuditMsg method...");

        response.setAuditMessage(auditMsg);
        return response;
    }
}
