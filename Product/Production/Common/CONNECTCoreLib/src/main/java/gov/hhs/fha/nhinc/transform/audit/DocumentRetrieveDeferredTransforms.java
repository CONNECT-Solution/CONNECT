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
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class DocumentRetrieveDeferredTransforms {

    private Log log = null;

    /**
     * default constructor
     */
    public DocumentRetrieveDeferredTransforms() {
        log = createLogger();
    }

    /**
     *
     * @return Log
     */
    protected Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     * 
     * @param response
     * @param assertion
     * @param direction
     * @param _interface
     * @return LogEventRequestType
     */
    public LogEventRequestType transformAckResponseToAuditMsg(RegistryResponseType response, AssertionType assertion, String direction, String _interface) {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        if (response == null) {
            log.error("Requst Object was null");
            return null;
        }
        if (assertion == null) {
            log.error("Assertion was null");
            return null;
        }

        //check to see that the required fields are not null
        boolean missingReqFields = areRequiredResponseFieldsNull(response, assertion);

        if (missingReqFields) {
            log.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();

        auditMsg = new AuditMessageType();
        // Create EventIdentification
        CodedValueType eventId = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_DOCRETRIEVE_REQUEST, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_DOCRETRIEVE_REQUEST);
        CodedValueType eventTypeCode = AuditDataTransformHelper.createCodeValueType(AuditDataTransformConstants.EVENT_TYPE_CODE_DOCRETRIEVE, AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCRETRIEVE, AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCRETRIEVE_DISPNAME, AuditDataTransformConstants.EVENT_TYPE_CODE_DOCRETRIEVE_DISPNAME);

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

        marshalResponseMessage(baOutStrm, response);
        partObjId.setParticipantObjectQuery(baOutStrm.toByteArray());
        auditMsg.getParticipantObjectIdentification().add(partObjId);

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
     * @param userInfo
     * @return AuditSourceIdentificationType
     */
    private AuditSourceIdentificationType getAuditSourceIdentificationType(UserType userInfo) {
        AuditSourceIdentificationType result;

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
        result = AuditDataTransformHelper.createAuditSourceIdentification(communityId, communityName);
        return result;
    }

    /**
     * 
     * @param baOutStrm
     * @param response
     * @throws RuntimeException
     */
    protected void marshalResponseMessage(ByteArrayOutputStream baOutStrm, RegistryResponseType response) throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("oasis.names.tc.ebxml_regrep.xsd.rs._3");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0", "RegistryResponse");
            JAXBElement<RegistryResponseType> element;

            element = new JAXBElement<RegistryResponseType>(xmlqname, RegistryResponseType.class, response);


            marshaller.marshal(element, baOutStrm);
            log.debug("Done marshalling the message.");
        } catch (Exception e) {
            log.error("Exception while marshalling Acknowledgement", e);
            throw new RuntimeException();
        }
    }

    /**
     * 
     * @param response
     * @param assertion
     * @return boolean
     */
    protected boolean areRequiredResponseFieldsNull(RegistryResponseType response, AssertionType assertion) {
        if (assertion == null) {
            log.error("Assertion object is null");
            return true;
        }
        if (response == null) {
            log.error("RegistryResponseType object is null");
            return true;
        }
        if (areRequiredUserTypeFieldsNull(assertion)) {
            log.error("One of more UserInfo fields from the Assertion object were null.");
            return true;
        }
        if (response.getStatus() == null) {
            log.error("Response does not contain a status");
            return true;
        }
        if (response.getStatus().isEmpty()) {
            log.error("Response does not contain a status");
            return true;
        }

        return false;
    }

    /**
     * 
     * @param oAssertion
     * @return boolean
     */
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

    /**
     * 
     * @param acknowledgement
     * @param assertion
     * @param direction
     * @param _interface
     * @param action
     * @return LogEventRequestType
     */
    public LogEventRequestType transformAcknowledgementToAuditMsg(DocRetrieveAcknowledgementType acknowledgement, AssertionType assertion, String direction, String _interface, String action)
    {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        if(acknowledgement == null)
        {
            log.error("Acknowledgement is null");
            return null;
        }

        if(assertion == null)
        {
            log.error("Assertion is null");
            return null;
        }

        //check to see that the required fields are not null
        boolean missingReqFields = areRequiredAcknowledgementFieldsNull(acknowledgement, assertion);

        if (missingReqFields)
        {
            log.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();

        auditMsg = new AuditMessageType();
        // Create EventIdentification
        CodedValueType eventId = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_DOCRETRIEVE_REQUEST, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_DOCRETRIEVE_REQUEST);
        CodedValueType eventTypeCode = AuditDataTransformHelper.createCodeValueType(AuditDataTransformConstants.EVENT_TYPE_CODE_DOCRETRIEVE, AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCRETRIEVE, AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCRETRIEVE_DISPNAME, AuditDataTransformConstants.EVENT_TYPE_CODE_DOCRETRIEVE_DISPNAME);
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
    protected void marshalAcknowledgement(ByteArrayOutputStream baOutStrm, DocRetrieveAcknowledgementType acknowledgement) throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.healthit.nhin");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("http://www.hhs.gov/healthit/nhin", "DocRetrieveAcknowledgementType");
            JAXBElement<DocRetrieveAcknowledgementType> element;

            element = new JAXBElement<DocRetrieveAcknowledgementType>(xmlqname, DocRetrieveAcknowledgementType.class, acknowledgement);


            marshaller.marshal(element, baOutStrm);
            log.debug("Done marshalling the message.");
        } catch (Exception e) {
            log.error("Exception while marshalling Acknowledgement", e);
            throw new RuntimeException();
        }
    }

    /**
     * 
     * @param acknowledgement
     * @param assertion
     * @return boolean
     */
    protected boolean areRequiredAcknowledgementFieldsNull(DocRetrieveAcknowledgementType acknowledgement, AssertionType assertion)
    {
        if(assertion == null)
        {
            log.error("Assertion object is null");
            return true;
        }
        if(acknowledgement == null)
        {
            log.error("Acknowledge object is null");
            return true;
        }
        if (areRequiredUserTypeFieldsNull(assertion))
        {
            log.error("One of more UserInfo fields from the Assertion object were null.");
            return true;
        }
        if(acknowledgement.getMessage() == null)
        {
            log.error("Acknowledgement does not contain a message");
            return true;
        }

         return false;
    }
}
