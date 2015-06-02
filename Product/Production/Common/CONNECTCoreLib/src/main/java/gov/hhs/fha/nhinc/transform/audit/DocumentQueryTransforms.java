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
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.common.auditlog.LogAdhocQueryRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogAdhocQueryResultRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import java.io.ByteArrayOutputStream;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectRefType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import org.apache.log4j.Logger;

/**
 *
 * @author MFLYNN02
 */
public class DocumentQueryTransforms {

    private static final Logger LOG = Logger.getLogger(DocumentQueryTransforms.class);
    private static final String PATIENT_ID_SLOT = "$XDSDocumentEntryPatientId";

    public DocumentQueryTransforms() {
    }

    /**
     *
     * @param message LogAdhocQueryRequestType
     * @return <code>LogEventRequestType</code>
     */
    public LogEventRequestType transformDocQueryReq2AuditMsg(LogAdhocQueryRequestType message) {
        return transformDocQueryReq2AuditMsg(message, null);
    }

    /**
     *
     * @param message LogAdhocQueryRequestType
     * @param responseCommunityID
     * @return <code>LogEventRequestType</code>
     */
    public LogEventRequestType transformDocQueryReq2AuditMsg(LogAdhocQueryRequestType message,
        String responseCommunityID) {
        AuditMessageType auditMsg = new AuditMessageType();
        LogEventRequestType response = new LogEventRequestType();
        if (message != null) {
            response.setDirection(message.getDirection());
            response.setInterface(message.getInterface());
        }
        LOG.trace("******************************************************************");
        LOG.trace("Entering transformDocQueryReq2AuditMsg() method.");
        LOG.trace("******************************************************************");

        // Extract UserInfo from Message.Assertion
        UserType userInfo = new UserType();
        if (message != null && message.getMessage() != null && message.getMessage().getAssertion() != null
            && message.getMessage().getAssertion().getUserInfo() != null) {
            userInfo = message.getMessage().getAssertion().getUserInfo();
        }

        // Create Event Identification Section
        // TODO: Determine what to do with Event Code and Event Code System (either auto-generate or map in
        // AdhocQueryRequest
        CodedValueType eventId = AuditDataTransformHelper.createEventId(
            AuditDataTransformConstants.EVENT_ID_CODE_DOCQUERY,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_DOCQUERY);
        CodedValueType eventTypeCode = AuditDataTransformHelper.createCodeValueType(
            AuditDataTransformConstants.EVENT_TYPE_CODE_DOCQUERY,
            AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCQUERY,
            AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCQUERY_DISPNAME,
            AuditDataTransformConstants.EVENT_TYPE_CODE_DOCQUERY_DISPNAME);

        EventIdentificationType eventIdentification = AuditDataTransformHelper.createEventIdentification(
            AuditDataTransformConstants.EVENT_ACTION_CODE_EXECUTE,
            AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventId);
        auditMsg.setEventIdentification(eventIdentification);

        eventIdentification.getEventTypeCode().add(eventTypeCode);

        // Create Active Participant Section
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(
                userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        // Create Audit Source Identification Section
        AuditSourceIdentificationType auditSrcId = null;
        if (responseCommunityID != null) {
            auditSrcId = AuditDataTransformHelper.createAuditSourceIdentification(responseCommunityID,
                responseCommunityID);
        } else {
            auditSrcId = AuditDataTransformHelper.createAuditSourceIdentificationFromUser(userInfo);
        }

        auditMsg.getAuditSourceIdentification().add(auditSrcId);

        String patientId = "";
        if (message != null && message.getMessage() != null && message.getMessage().getAdhocQueryRequest() != null
            && message.getMessage().getAdhocQueryRequest().getAdhocQuery() != null
            && message.getMessage().getAdhocQueryRequest().getAdhocQuery().getSlot() != null
            && message.getMessage().getAdhocQueryRequest().getAdhocQuery().getSlot().size() > 0) {
            List<SlotType1> slotItemsList = message.getMessage().getAdhocQueryRequest().getAdhocQuery().getSlot();
            for (SlotType1 slotItem : slotItemsList) {
                if (slotItem != null) {
                    if (PATIENT_ID_SLOT.equals(slotItem.getName())) {
                        if (slotItem.getValueList() != null && slotItem.getValueList().getValue() != null
                            && slotItem.getValueList().getValue().size() > 0) {
                            patientId = slotItem.getValueList().getValue().get(0);
                            break;
                        }
                    }
                }
            }
        }

        // Create Participation Object Identification Section
        ParticipantObjectIdentificationType partObjId = AuditDataTransformHelper
            .createParticipantObjectIdentification(patientId);

        // Fill in the message field with the contents of the event message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("oasis.names.tc.ebxml_regrep.xsd.query._3");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            if (message != null) {
                marshaller.marshal(message.getMessage().getAdhocQueryRequest(), baOutStrm);
            }
            LOG.debug("Done marshalling the message.");

            partObjId.setParticipantObjectQuery(baOutStrm.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        auditMsg.getParticipantObjectIdentification().add(partObjId);
        response.setAuditMessage(auditMsg);

        LOG.trace("******************************************************************");
        LOG.trace("Exiting transformDocQueryReq2AuditMsg() method.");
        LOG.trace("******************************************************************");

        return response;
    }

    /**
     *
     * @param message
     * @return <code>LogEventRequestType</code>
     */
    public LogEventRequestType transformDocQueryResp2AuditMsg(LogAdhocQueryResultRequestType message) {
        return transformDocQueryResp2AuditMsg(message, null);
    }

    /**
     *
     * @param message
     * @param requestCommunityID
     * @return <code>LogEventRequestType</code>
     */
    public LogEventRequestType transformDocQueryResp2AuditMsg(LogAdhocQueryResultRequestType message,
        String requestCommunityID) {
        AuditMessageType auditMsg = new AuditMessageType();
        LogEventRequestType response = new LogEventRequestType();
        if (message != null) {
            response.setDirection(message.getDirection());
            response.setInterface(message.getInterface());
        }
        LOG.trace("******************************************************************");
        LOG.trace("Entering transformDocQueryResp2AuditMsg() method.");
        LOG.trace("******************************************************************");

        // Extract UserInfo from Message.Assertion
        UserType userInfo = new UserType();
        if (message != null && message.getMessage() != null && message.getMessage().getAssertion() != null
            && message.getMessage().getAssertion().getUserInfo() != null) {
            userInfo = message.getMessage().getAssertion().getUserInfo();
            LOG.debug("***** ASSERTION UserInfo UserName contains: " + userInfo.getUserName() + " *****");
            LOG.debug("***** ASSERTION UserInfo Org HomeCommunityId contains: " + userInfo.getOrg().getHomeCommunityId()
                + " *****");
        } else {
            LOG.info("***** ASSERTION IS NULL *****");
        }

        // Create Event Identification Section
        // TODO: Figure out what to do with Event Code and Event Code System
        CodedValueType eventId = AuditDataTransformHelper.createEventId(
            AuditDataTransformConstants.EVENT_ID_CODE_DOCQUERY,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_DOCQUERY);
        CodedValueType eventTypeCode = AuditDataTransformHelper.createCodeValueType(
            AuditDataTransformConstants.EVENT_TYPE_CODE_DOCQUERY,
            AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCQUERY,
            AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCQUERY_DISPNAME,
            AuditDataTransformConstants.EVENT_TYPE_CODE_DOCQUERY_DISPNAME);

        EventIdentificationType eventIdentification = AuditDataTransformHelper.createEventIdentification(
            AuditDataTransformConstants.EVENT_ACTION_CODE_EXECUTE,
            AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventId);
        auditMsg.setEventIdentification(eventIdentification);

        eventIdentification.getEventTypeCode().add(eventTypeCode);

        // Create Active Participant Section
        if (userInfo != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(
                userInfo, true);
            auditMsg.getActiveParticipant().add(participant);
        }

        if ((message != null) && (message.getMessage() != null)
            && (message.getMessage().getAdhocQueryResponse() != null)
            && (message.getMessage().getAdhocQueryResponse().getRegistryObjectList() != null)
            && (message.getMessage().getAdhocQueryResponse().getRegistryObjectList().getIdentifiable() != null)
            && (message.getMessage().getAdhocQueryResponse().getRegistryObjectList().getIdentifiable().size() > 0)) {
            // Create Audit Source Identification Section
            List<JAXBElement<? extends IdentifiableType>> objList = message.getMessage().getAdhocQueryResponse()
                .getRegistryObjectList().getIdentifiable();
            ExtrinsicObjectType oExtObj = null;
            ObjectRefType oObjRef = null;

            // Look for the first ExtrinsicObject type.. We will use that one to extract the data.
            // -------------------------------------------------------------------------------------
            for (int i = 0; i < objList.size(); i++) {
                JAXBElement<? extends IdentifiableType> oJAXBObj = objList.get(i);

                if ((oJAXBObj != null)
                    && (oJAXBObj.getDeclaredType() != null)
                    && (oJAXBObj.getDeclaredType().getCanonicalName() != null)
                    && (oJAXBObj.getDeclaredType().getCanonicalName()
                    .equals("oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType"))
                    && (oJAXBObj.getValue() != null)) {
                    oExtObj = (ExtrinsicObjectType) oJAXBObj.getValue();
                    break; // We have what we want let's get out of here...
                } else if ((oJAXBObj != null)
                    && (oJAXBObj.getDeclaredType() != null)
                    && (oJAXBObj.getDeclaredType().getCanonicalName() != null)
                    && (oJAXBObj.getDeclaredType().getCanonicalName()
                    .equals("oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectRefType"))
                    && (oJAXBObj.getValue() != null)) {
                    oObjRef = (ObjectRefType) oJAXBObj.getValue();
                    break; // We have what we want let's get out of here...
                }

            } // for (int i = 0; i < objList.size(); i++)

            // Home Community ID
            // -------------------
            String communityId = null;
            if (requestCommunityID != null) {
                communityId = requestCommunityID;
                LOG.debug("=====>>>>> Create Audit Source Identification Section --> requestCommunityID is ["
                    + requestCommunityID + "]");
            } else if ((oExtObj != null) && (oExtObj.getHome() != null) && (oExtObj.getHome().length() > 0)) {
                communityId = oExtObj.getHome();
            } else if ((oObjRef != null) && (NullChecker.isNotNullish(oObjRef.getHome()))) {
                communityId = oObjRef.getHome();
            }
            if (communityId != null) {
                AuditSourceIdentificationType auditSrcId = AuditDataTransformHelper.createAuditSourceIdentification(
                    communityId, communityId);
                auditMsg.getAuditSourceIdentification().add(auditSrcId);
            }

            // Patient ID
            // ------------
            ParticipantObjectIdentificationType partObjId = new ParticipantObjectIdentificationType();
            String documentId = "";
            if (oExtObj != null && oExtObj.getExternalIdentifier() != null
                && oExtObj.getExternalIdentifier().size() > 0) {
                documentId = oExtObj.getExternalIdentifier().get(0).getValue();
            }
            partObjId = AuditDataTransformHelper.createDocumentParticipantObjectIdentification(documentId);

            // Fill in the message field with the contents of the event message
            try {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("oasis.names.tc.ebxml_regrep.xsd.query._3");
                Marshaller marshaller = jc.createMarshaller();
                ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
                baOutStrm.reset();
                marshaller.marshal(message.getMessage().getAdhocQueryResponse(), baOutStrm);
                LOG.debug("Done marshalling the message.");

                partObjId.setParticipantObjectQuery(baOutStrm.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
            auditMsg.getParticipantObjectIdentification().add(partObjId);
        }
        response.setAuditMessage(auditMsg);

        LOG.trace("******************************************************************");
        LOG.trace("Exiting transformDocQueryResp2AuditMsg() method.");
        LOG.trace("******************************************************************");
        return response;
    }

    /**
     *
     * @param acknowledgement
     * @param assertion
     * @param direction
     * @param _interface
     * @return <code>LogEventRequestType</code>
     */
    public LogEventRequestType transformAcknowledgementToAuditMsg(DocQueryAcknowledgementType acknowledgement,
        AssertionType assertion, String direction, String _interface) {
        return transformAcknowledgementToAuditMsg(acknowledgement, assertion, direction, _interface, null);
    }

    /**
     *
     * @param acknowledgement
     * @param assertion
     * @param direction
     * @param _interface
     * @param action
     * @return <code>LogEventRequestType</code>
     */
    public LogEventRequestType transformAcknowledgementToAuditMsg(DocQueryAcknowledgementType acknowledgement,
        AssertionType assertion, String direction, String _interface, String requestCommunityID) {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        if (acknowledgement == null) {
            LOG.error("Acknowledgement is null");
            return null;
        }

        if (assertion == null) {
            LOG.error("Assertion is null");
            return null;
        }

        result = new LogEventRequestType();

        auditMsg = new AuditMessageType();
        // Create EventIdentification
        CodedValueType eventId = AuditDataTransformHelper.createEventId(
            AuditDataTransformConstants.EVENT_ID_CODE_DOCQUERY,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_DOCQUERY);
        CodedValueType eventTypeCode = AuditDataTransformHelper.createCodeValueType(
            AuditDataTransformConstants.EVENT_TYPE_CODE_DOCQUERY,
            AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCQUERY,
            AuditDataTransformConstants.EVENT_TYPE_CODE_SYS_NAME_DOCQUERY_DISPNAME,
            AuditDataTransformConstants.EVENT_TYPE_CODE_DOCRETRIEVE_DISPNAME);
        EventIdentificationType eventIdentification = AuditDataTransformHelper.createEventIdentification(
            AuditDataTransformConstants.EVENT_ACTION_CODE_READ,
            AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventId);
        auditMsg.setEventIdentification(eventIdentification);
        eventIdentification.getEventTypeCode().add(eventTypeCode);
        auditMsg.setEventIdentification(eventIdentification);

        // Create Active Participant Section
        if (assertion.getUserInfo() != null) {
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(
                assertion.getUserInfo(), true);
            auditMsg.getActiveParticipant().add(participant);
        }

        /* Assign ParticipationObjectIdentification */
        // Create Participation Object Identification Section
        ParticipantObjectIdentificationType partObjId = new ParticipantObjectIdentificationType();
        if (assertion.getUniquePatientId() != null && assertion.getUniquePatientId().size() > 0
            && assertion.getUniquePatientId().get(0) != null) {
            partObjId = AuditDataTransformHelper.createParticipantObjectIdentification(assertion.getUniquePatientId()
                .get(0));
        }

        // Put the contents of the actual message into the Audit Log Message
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        marshalAcknowledgement(baOutStrm, acknowledgement);
        partObjId.setParticipantObjectQuery(baOutStrm.toByteArray());
        auditMsg.getParticipantObjectIdentification().add(partObjId);

        /* Create the AuditSourceIdentifierType object */

        // Home Community ID
        // -------------------
        /* Create the AuditSourceIdentifierType object */
        AuditSourceIdentificationType auditSource = null;
        if (requestCommunityID != null) {
            auditSource = AuditDataTransformHelper.createAuditSourceIdentification(requestCommunityID,
                requestCommunityID);
        } else {
            auditSource = AuditDataTransformHelper.createAuditSourceIdentificationFromUser(assertion.getUserInfo());
        }
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
    private void marshalAcknowledgement(ByteArrayOutputStream baOutStrm, DocQueryAcknowledgementType acknowledgement)
        throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.healthit.nhin");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("http://www.hhs.gov/healthit/nhin",
                "DocQueryAcknowledgementType");
            JAXBElement<DocQueryAcknowledgementType> element;

            element = new JAXBElement<DocQueryAcknowledgementType>(xmlqname, DocQueryAcknowledgementType.class,
                acknowledgement);

            marshaller.marshal(element, baOutStrm);
            LOG.debug("Done marshalling the message.");
        } catch (Exception e) {
            LOG.error("Exception while marshalling Acknowledgement", e);
            throw new RuntimeException();
        }
    }
}
