/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
import com.services.nhinc.schema.auditmessage.AuditMessageType.ActiveParticipant;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.log4j.Logger;

/**
 *
 * @author dunnek
 */
public class XDRTransforms {

    private static final Logger LOG = Logger.getLogger(XDRTransforms.class);

    public LogEventRequestType transformRequestToAuditMsg(ProvideAndRegisterDocumentSetRequestType request,
        AssertionType assertion, NhinTargetSystemType target, String direction, String _interface) {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        LOG.debug("Begin transformRequestToAuditMsg() -- NHIN");
        if (request == null) {
            LOG.error("Requst Object was null");
            return null;
        }
        if (assertion == null) {
            LOG.error("Assertion was null");
            return null;
        }

        // check to see that the required fields are not null
        boolean missingReqFields = areRequiredXDSfieldsNull(request, assertion);

        if (missingReqFields) {
            LOG.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();

        String patId = getIdentifiersFromRequest(request); // null values checked from the earlier call to
        // areRequired201305fieldsNull() method
        auditMsg = new AuditMessageType();

        // Create EventIdentification
        CodedValueType eventID = getCodedValueTypeForXDR();

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        auditMsg.setEventIdentification(oEventIdentificationType);

        ActiveParticipant participant = getActiveParticipant(assertion.getUserInfo());
        auditMsg.getActiveParticipant().add(participant);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType(patId);
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        /* Create the AuditSourceIdentifierType object */
        String communityId = getMessageCommunityIdFromRequest(assertion, target, direction, _interface);
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(communityId);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        LOG.debug("end transformRequestToAuditMsg() -- NHIN");
        return result;
    }

    public LogEventRequestType transformRequestToAuditMsg(
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request,
        AssertionType assertion, NhinTargetSystemType target, String direction, String _interface) {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        if (request == null) {
            LOG.error("Requst Object was null");
            return null;
        }
        if (assertion == null) {
            LOG.error("Assertion was null");
            return null;
        }

        // check to see that the required fields are not null
        boolean missingReqFields = areRequiredXDSfieldsNull(request.getProvideAndRegisterDocumentSetRequest(),
            assertion);

        if (missingReqFields) {
            LOG.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();

        String patId = getIdentifiersFromRequest(request.getProvideAndRegisterDocumentSetRequest()); // null values
        // checked from the
        // earlier call to
        // areRequired201305fieldsNull()
        // method
        auditMsg = new AuditMessageType();

        // Create EventIdentification
        CodedValueType eventID = getCodedValueTypeForXDRProxy();

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        auditMsg.setEventIdentification(oEventIdentificationType);

        ActiveParticipant participant = getActiveParticipant(assertion.getUserInfo());
        auditMsg.getActiveParticipant().add(participant);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType(patId);
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        /* Create the AuditSourceIdentifierType object */
        String communityId = getMessageCommunityIdFromRequest(assertion, target, direction, _interface);
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(communityId);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        return result;
    }

    public LogEventRequestType transformRequestToAuditMsg(
        gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request,
        AssertionType assertion, NhinTargetSystemType target, String direction, String _interface) {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        LOG.debug("Begin transformRequestToAuditMsg() -- Entity");

        if (request == null) {
            LOG.error("Requst Object was null");
            return null;
        }
        if (assertion == null) {
            LOG.error("Assertion was null");
            return null;
        }

        // check to see that the required fields are not null
        boolean missingReqFields = areRequiredXDSfieldsNull(request.getProvideAndRegisterDocumentSetRequest(),
            assertion);

        if (missingReqFields) {
            LOG.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();

        String patId = getIdentifiersFromRequest(request.getProvideAndRegisterDocumentSetRequest()); // null values
        // checked from the
        // earlier call to
        // areRequired201305fieldsNull()
        // method
        auditMsg = new AuditMessageType();

        // Create EventIdentification
        CodedValueType eventID = getCodedValueTypeForXDREntity();

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        auditMsg.setEventIdentification(oEventIdentificationType);

        ActiveParticipant participant = getActiveParticipant(assertion.getUserInfo());
        auditMsg.getActiveParticipant().add(participant);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType(patId);
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        /* Create the AuditSourceIdentifierType object */
        String communityId = getMessageCommunityIdFromRequest(assertion, target, direction, _interface);
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(communityId);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        return result;
    }

    public LogEventRequestType transformRequestToAuditMsg(
        gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request,
        AssertionType assertion, NhinTargetSystemType target, String direction, String _interface) {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        if (request == null) {
            LOG.error("Requst Object was null");
            return null;
        }
        if (assertion == null) {
            LOG.error("Assertion was null");
            return null;
        }

        // check to see that the required fields are not null
        boolean missingReqFields = areRequiredResponseFieldsNull(request.getRegistryResponse(), assertion);

        if (missingReqFields) {
            LOG.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();

        auditMsg = new AuditMessageType();
        // Create EventIdentification
        CodedValueType eventID = getCodedValueTypeForXDRResponse();

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        auditMsg.setEventIdentification(oEventIdentificationType);

        ActiveParticipant participant = getActiveParticipant(assertion.getUserInfo());
        auditMsg.getActiveParticipant().add(participant);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType("");
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        /* Create the AuditSourceIdentifierType object */
        String communityId = getMessageCommunityIdFromRequest(assertion, target, direction, _interface);
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(communityId);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        return result;
    }

    public LogEventRequestType transformResponseToAuditMsg(RegistryResponseType response, AssertionType assertion,
        NhinTargetSystemType target, String direction, String _interface, boolean isRequesting) {
        LogEventRequestType result = null;
        AuditMessageType auditMsg = null;

        if (response == null) {
            LOG.error("Requst Object was null");
            return null;
        }
        if (assertion == null) {
            LOG.error("Assertion was null");
            return null;
        }

        // check to see that the required fields are not null
        boolean missingReqFields = areRequiredResponseFieldsNull(response, assertion);

        if (missingReqFields) {
            LOG.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();

        auditMsg = new AuditMessageType();
        // Create EventIdentification
        CodedValueType eventID = getCodedValueTypeForXDRResponse();

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        auditMsg.setEventIdentification(oEventIdentificationType);

        ActiveParticipant participant = getActiveParticipant(assertion.getUserInfo());
        auditMsg.getActiveParticipant().add(participant);

        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType("");
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        /* Create the AuditSourceIdentifierType object */
        String communityId = getMessageCommunityId(assertion, target, _interface, isRequesting);
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(communityId);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        return result;

    }

    /**
     * Retrieves the community id for auditing when the message being audited is a request message. For example, this
     * method should be used when the message being audited is an ProvideAndRegister request.
     *
     * @param assertion the assertion containing a homecommunity id
     * @param target the destination of the original request message
     * @param direction the direction of the message being auditied
     * @param _interface the interface (nhin, entity, adapter) of the audited message
     * @return the community id to use
     */
    public String getMessageCommunityIdFromRequest(AssertionType assertion, NhinTargetSystemType target,
        String direction, String _interface) {

        // if a request is going outbound, then the current audit is in the requesting side
        boolean isAuditInRequestingSide = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION.equalsIgnoreCase(direction);

        return getMessageCommunityId(assertion, target, _interface, isAuditInRequestingSide);
    }

    /**
     * Retrieves the community id for auditing when the message being audited is a response message. For example, this
     * method should be used when the message being audited is an Acknowledgement response of a request.
     *
     * @param assertion the assertion containing a homecommunity id
     * @param target the destination of the original request message
     * @param direction the direction of the message being auditied
     * @param _interface the interface (nhin, entity, adapter) of the audited message
     * @return the community id to use
     */
    public String getMessageCommunityIdFromResponse(AssertionType assertion, NhinTargetSystemType target,
        String direction, String _interface) {

        // if a response is going inbound, then the current audit is in the requesting side
        boolean isAuditInRequestingSide = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION.equalsIgnoreCase(direction);

        return getMessageCommunityId(assertion, target, _interface, isAuditInRequestingSide);
    }

    /**
     * Retrieves the community id for auditing.
     *
     * @param assertion the assertion containing a homecommunity id
     * @param target the destination of the original request message
     * @param _interface the interface (nhin, entity, adapter) of the audited message
     * @param isRequesting true if the message being audited is in the requesting side
     * @return the community id to use
     */
    public String getMessageCommunityId(AssertionType assertion, NhinTargetSystemType target,
        String _interface, boolean isRequesting) {
        String communityId = null;

        if (NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE.equalsIgnoreCase(_interface)
            || NhincConstants.AUDIT_LOG_ENTITY_INTERFACE.equalsIgnoreCase(_interface)) {
            communityId = getLocalHomeCommunityId();
        } else if (NhincConstants.AUDIT_LOG_NHIN_INTERFACE.equalsIgnoreCase(_interface)) {
            if (isRequesting) {
                communityId = HomeCommunityMap.getCommunityIdFromTargetSystem(target);
            } else {
                communityId = HomeCommunityMap.getHomeCommunityIdFromAssertion(assertion);
            }
        }

        return communityId;
    }

    protected String getLocalHomeCommunityId() {
        return HomeCommunityMap.getLocalHomeCommunityId();
    }

    protected boolean areRequiredXDSfieldsNull(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
        try {

            if (assertion == null) {
                LOG.error("Assertion object is null");
                return true;
            }
            if (body == null) {
                LOG.error("ProvideAndRegisterDocumentSetRequestType object is null");
                return true;
            }

            if (areRequiredUserTypeFieldsNull(assertion)) {
                LOG.error("One of more UserInfo fields from the Assertion object were null.");
                return true;
            }

            if (body.getSubmitObjectsRequest() == null) {
                LOG.error("No Registry Object");
                return true;
            }

            if (body.getSubmitObjectsRequest().getRegistryObjectList() == null) {
                LOG.error("No Registry Object List");
                return true;
            }
            if (body.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable() == null
                || body.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable().isEmpty()) {
                LOG.error("No Identifiables on Registry Object");
                return true;
            }
            return false;
        } catch (Exception ex) {
            LOG.error("Encountered Error: " + ex.getMessage());
            return true;
        }
    }

    protected boolean areRequiredResponseFieldsNull(RegistryResponseType response, AssertionType assertion) {
        if (assertion == null) {
            LOG.error("Assertion object is null");
            return true;
        }
        if (response == null) {
            LOG.error("RegistryResponseType object is null");
            return true;
        }
        if (areRequiredUserTypeFieldsNull(assertion)) {
            LOG.error("One of more UserInfo fields from the Assertion object were null.");
            return true;
        }
        if (response.getStatus() == null) {
            LOG.error("Response does not contain a status");
            return true;
        }
        if (response.getStatus().isEmpty()) {
            LOG.error("Response does not contain a status");
            return true;
        }

        return false;
    }

    protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion) {
        boolean bReturnVal = false;

        if ((oAssertion != null) && (oAssertion.getUserInfo() != null)) {
            if (oAssertion.getUserInfo().getUserName() != null) {
                LOG.debug("Incomming request.getAssertion.getUserInfo.getUserName: "
                    + oAssertion.getUserInfo().getUserName());
            } else {
                LOG.error("Incomming request.getAssertion.getUserInfo.getUserName was null.");
                bReturnVal = true;
                return true;
            }

            if (oAssertion.getUserInfo().getOrg().getHomeCommunityId() != null) {
                LOG.debug("Incomming request.getAssertion.getUserInfo.getOrg().getHomeCommunityId(): "
                    + oAssertion.getUserInfo().getOrg().getHomeCommunityId());
            } else {
                LOG.error("Incomming request.getAssertion.getUserInfo.getOrg().getHomeCommunityId() was null.");
                bReturnVal = true;
                return true;
            }

            if (oAssertion.getUserInfo().getOrg().getName() != null) {
                LOG.debug("Incomming request.getAssertion.getUserInfo.getOrg().getName() or Community Name: "
                    + oAssertion.getUserInfo().getOrg().getName());
            } else {
                LOG.error("Incomming request.getAssertion.getUserInfo.getOrg().getName() or Community Name was null.");
                bReturnVal = true;
                return true;
            }
        } else {
            LOG.error("The UserType object or request assertion object containing the assertion user info was null.");
            bReturnVal = true;
            return true;
        } // else continue

        return bReturnVal;
    }

    private ParticipantObjectIdentificationType getParticipantObjectIdentificationType(String sPatientId) {
        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = AuditDataTransformHelper
            .createParticipantObjectIdentification(sPatientId);
        return participantObject;
    }

    private ActiveParticipant getActiveParticipant(UserType oUserInfo) {
        // Create Active Participant Section
        // create a method to call the AuditDataTransformHelper - one expectation
        AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(
            oUserInfo, true);
        return participant;
    }

    private String getIdentifiersFromRequest(ProvideAndRegisterDocumentSetRequestType request) {
        String result = "";

        if (request == null) {
            LOG.error(("Incoming ProvideAndRegisterDocumentSetRequestType was null"));
            return null;
        }

        if (request.getSubmitObjectsRequest() == null) {
            LOG.error(("Incoming ProvideAndRegisterDocumentSetRequestType metadata was null"));
            return null;
        }

        RegistryObjectListType object = request.getSubmitObjectsRequest().getRegistryObjectList();

        for (int x = 0; x < object.getIdentifiable().size(); x++) {
            
            if (object.getIdentifiable().get(x).getDeclaredType().equals(RegistryPackageType.class)) {
                RegistryPackageType registryPackage = (RegistryPackageType) object.getIdentifiable().get(x).getValue();

                for (int y = 0; y < registryPackage.getExternalIdentifier().size(); y++) {
                    String test = registryPackage.getExternalIdentifier().get(y).getName().getLocalizedString().get(0)
                        .getValue();
                    if (test.equals("XDSSubmissionSet.patientId")) {
                        result = registryPackage.getExternalIdentifier().get(y).getValue();
                    }
                }
            }
        }

        return result;
    }

    protected void marshalRequestMessage(ByteArrayOutputStream baOutStrm,
        ProvideAndRegisterDocumentSetRequestType request) throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        LOG.debug("Begin marshalRequestMessage() -- NHIN Interface");
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("ihe.iti.xds_b._2007");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            JAXBElement<ProvideAndRegisterDocumentSetRequestType> element;

            ihe.iti.xds_b._2007.ObjectFactory factory = new ihe.iti.xds_b._2007.ObjectFactory();
            element = factory.createProvideAndRegisterDocumentSetRequest(request);

            marshaller.marshal(element, baOutStrm);
            LOG.debug("Done marshalling the ProvideAndRegisterDocumentSetRequestType  message.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    protected void marshalRequestMessage(
        ByteArrayOutputStream baOutStrm,
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request)
        throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonproxy");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:ihe:iti:xds-b:2007",
                "RespondingGatewayProvideAndRegisterDocumentSetSecuredRequest");
            JAXBElement<gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType> element;

            element = new JAXBElement<gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType>(
                xmlqname,
                gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class,
                request);

            marshaller.marshal(element, baOutStrm);
            LOG.debug("Done marshalling the message.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    protected void marshalRequestMessage(
        ByteArrayOutputStream baOutStrm,
        gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request)
        throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonproxy");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName(
                "urn:gov:hhs:fha:nhinc:common:nhinccommonentity",
                "RespondingGatewayProvideAndRegisterDocumentSetSecuredRequest");
            JAXBElement<gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType> element;

            element = new JAXBElement<gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType>(
                xmlqname,
                gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType.class,
                request);

            marshaller.marshal(element, baOutStrm);
            LOG.debug("Done marshalling the message.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    protected void marshalRequestMessage(
        ByteArrayOutputStream baOutStrm,
        gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request)
        throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonentity");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName(
                "urn:gov:hhs:fha:nhinc:common:nhinccommonentity",
                "RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType");
            JAXBElement<gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType> element;

            element = new JAXBElement<gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType>(
                xmlqname,
                gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType.class,
                request);

            marshaller.marshal(element, baOutStrm);
            LOG.debug("Done marshalling the RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType message.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    protected void marshalRequestMessage(
        ByteArrayOutputStream baOutStrm,
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request)
        throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonproxy");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName(
                "urn:gov:hhs:fha:nhinc:common:nhinccommonproxy",
                "RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType");
            JAXBElement<gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType> element;

            element = new JAXBElement<gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType>(
                xmlqname,
                gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType.class,
                request);

            marshaller.marshal(element, baOutStrm);
            LOG.debug("Done marshalling the RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType message.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    protected void marshalResponseMessage(ByteArrayOutputStream baOutStrm, RegistryResponseType response)
        throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("oasis.names.tc.ebxml_regrep.xsd.rs._3");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName(
                "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0", "RegistryResponse");
            JAXBElement<RegistryResponseType> element;

            element = new JAXBElement<RegistryResponseType>(xmlqname, RegistryResponseType.class, response);

            marshaller.marshal(element, baOutStrm);
            LOG.debug("Done marshalling the message.");
        } catch (Exception e) {
            LOG.error("Exception while marshalling Acknowledgement", e);
            throw new RuntimeException();
        }
    }

    protected void marshalAcknowledgement(ByteArrayOutputStream baOutStrm, XDRAcknowledgementType acknowledgement)
        throws RuntimeException {
        // Put the contents of the actual message into the Audit Log Message
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.healthit.nhin");
            Marshaller marshaller = jc.createMarshaller();
            baOutStrm.reset();

            javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("http://www.hhs.gov/healthit/nhin",
                "XDRAcknowledgement");
            JAXBElement<XDRAcknowledgementType> element;

            element = new JAXBElement<XDRAcknowledgementType>(xmlqname, XDRAcknowledgementType.class, acknowledgement);

            marshaller.marshal(element, baOutStrm);
            LOG.debug("Done marshalling the message.");
        } catch (Exception e) {
            LOG.error("Exception while marshalling Acknowledgement", e);
            throw new RuntimeException();
        }
    }

    private AuditSourceIdentificationType getAuditSourceIdentificationType(String communityId) {
        return AuditDataTransformHelper.createAuditSourceIdentification(communityId, communityId);
    }

    private CodedValueType getCodedValueTypeForXDR() {
        // Create EventIdentification
        CodedValueType eventID = AuditDataTransformHelper.createEventId(
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDR,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDR,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDR,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDR);
        return eventID;
    }

    private CodedValueType getCodedValueTypeForXDRProxy() {
        // Create EventIdentification
        CodedValueType eventID = AuditDataTransformHelper.createEventId(
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDR,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDR_PROXY,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDR,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDR_PROXY);
        return eventID;
    }

    private CodedValueType getCodedValueTypeForXDREntity() {
        // Create EventIdentification
        CodedValueType eventID = AuditDataTransformHelper.createEventId(
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDR,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDR_ENTITY,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDR,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDR_ENTITY);
        return eventID;
    }

    private CodedValueType getCodedValueTypeForXDRResponse() {
        // Create EventIdentification
        CodedValueType eventID = AuditDataTransformHelper.createEventId(
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDR,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDRRESPONSE,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDR,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDRRESPONSE);
        return eventID;
    }

    private CodedValueType getCodedValueTypeForXDRRequestAcknowledgement() {
        // Create EventIdentification
        CodedValueType eventID = AuditDataTransformHelper.createEventId(
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDRREQUEST,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDRREQUEST,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDRREQUEST,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDRREQUEST);
        return eventID;
    }

    private CodedValueType getCodedValueTypeForXDRResponseAcknowledgement() {
        // Create EventIdentification
        CodedValueType eventID = AuditDataTransformHelper.createEventId(
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDRRESPONSE,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDRRESPONSE,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_XDRRESPONSE,
            AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_XDRRESPONSE);
        return eventID;
    }

    private EventIdentificationType getEventIdentificationType(CodedValueType eventID) {
        EventIdentificationType oEventIdentificationType = AuditDataTransformHelper.createEventIdentification(
            AuditDataTransformConstants.EVENT_ACTION_CODE_READ,
            AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID);
        return oEventIdentificationType;
    }

    /**
     *
     */
    public LogEventRequestType transformAcknowledgementToAuditMsg(XDRAcknowledgementType acknowledgement,
        AssertionType assertion, NhinTargetSystemType target, String direction, String _interface, String action) {
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

        // check to see that the required fields are not null
        boolean missingReqFields = areRequiredAcknowledgementFieldsNull(acknowledgement, assertion);

        if (missingReqFields) {
            LOG.error("One or more required fields was missing");
            return null;
        }

        result = new LogEventRequestType();

        auditMsg = new AuditMessageType();
        // Create EventIdentification
        CodedValueType eventID = null;

        if (NhincConstants.XDR_REQUEST_ACTION.equalsIgnoreCase(action)) {
            eventID = getCodedValueTypeForXDRRequestAcknowledgement();
        } else if (NhincConstants.XDR_RESPONSE_ACTION.equalsIgnoreCase(action)) {
            eventID = getCodedValueTypeForXDRResponseAcknowledgement();
        }

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID);
        auditMsg.setEventIdentification(oEventIdentificationType);

        ActiveParticipant participant = getActiveParticipant(assertion.getUserInfo());

        auditMsg.getActiveParticipant().add(participant);
        /* Assign ParticipationObjectIdentification */
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType("");
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        /* Create the AuditSourceIdentifierType object */
        String communityId = getMessageCommunityIdFromResponse(assertion, target, direction, _interface);
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType(communityId);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        return result;

    }

    /**
     *
     * @param acknowledgement
     * @param assertion
     * @return
     */
    protected boolean areRequiredAcknowledgementFieldsNull(XDRAcknowledgementType acknowledgement,
        AssertionType assertion) {
        if (assertion == null) {
            LOG.error("Assertion object is null");
            return true;
        }
        if (acknowledgement == null) {
            LOG.error("Acknowledge object is null");
            return true;
        }
        if (areRequiredUserTypeFieldsNull(assertion)) {
            LOG.error("One of more UserInfo fields from the Assertion object were null.");
            return true;
        }
        if (acknowledgement.getMessage() == null) {
            LOG.error("Acknowledgement does not contain a message");
            return true;
        }

        return false;
    }
}
