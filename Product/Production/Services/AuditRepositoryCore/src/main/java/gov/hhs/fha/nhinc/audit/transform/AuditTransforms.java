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
package gov.hhs.fha.nhinc.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditMessageType.ActiveParticipant;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.audit.AuditTransformsConstants;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.audit.AuditDataTransformHelper;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.lang.management.ManagementFactory;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.UUID;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This abstract class follows the Template design pattern. EventIdentification, ActiveParticipant (HumanRequestor,
 * Source, Destination), and AuditSourceIdentification are the same across all the exchange services (PD, DQ, DR, AD,
 * DS) and X12.
 * <p>
 * The ParticipantObjectIdentification is built using the requests and response types received from Inbound and Outbound
 * for any specific service. Abstract methods are provided for each core service to override, using T for request
 * messages and K for response messages.
 *
 * @author achidamb
 * @param <T> request Object Type
 * @param <K> response Object Type
 */
public abstract class AuditTransforms<T, K> {

    private static final Logger LOG = LoggerFactory.getLogger(AuditTransforms.class);

    private T request;
    private NhinTargetSystemType target;
    private AssertionType assertion;

    /**
     * Build an AuditLog Request Message from request
     *
     * @param request Request Object
     * @param assertion Assertion Object
     * @param target Target Community
     * @param direction Inbound/Outbound
     * @param _interface Entity, Adapter or Nwhin
     * @param isRequesting Initiating/Responding Gateway
     * @param webContextProperties Properties which hold destination URL and remote IP
     * @param serviceName Service Name
     * @return Audit Request
     */
    public final LogEventRequestType transformRequestToAuditMsg(T request, AssertionType assertion,
        NhinTargetSystemType target, String direction, String _interface, boolean isRequesting,
        Properties webContextProperties, String serviceName) {
        setRequest(request);
        setTarget(target);
        setAssertion(assertion);
        // TODO: auditMsg should either use a builder, or modify in-method with no return
        AuditMessageType auditMsg = createBaseAuditMessage(assertion, target, isRequesting, webContextProperties,
            serviceName);
        auditMsg = getParticipantObjectIdentificationForRequest(request, assertion, auditMsg);

        return buildLogEventRequestType(auditMsg, direction, getMessageCommunityId(assertion, target, isRequesting),
            serviceName, assertion, auditMsg.getEventIdentification().getEventID().getDisplayName(),
            auditMsg.getEventIdentification().getEventOutcomeIndicator(),
            auditMsg.getEventIdentification().getEventDateTime(), getUserId(auditMsg.getActiveParticipant()));
    }

    /**
     * Build and AuditLog Request Message from response
     *
     * @param request Request Object
     * @param response Response Object
     * @param assertion Assertion Object
     * @param target Target Community
     * @param direction Inbound/Outbound
     * @param _interface Entity, Adapter or Nwhin
     * @param isRequesting Initiating/Responding Gateway
     * @param webContextProperties Properties hold destination URL and remote IP
     * @param serviceName Service Name
     * @return Audit Request
     */
    public final LogEventRequestType transformResponseToAuditMsg(T request, K response, AssertionType assertion,
        NhinTargetSystemType target, String direction, String _interface, boolean isRequesting,
        Properties webContextProperties, String serviceName) {
        setRequest(request);
        setAssertion(assertion);
        // TODO: auditMsg should either use a builder, or modify in-method with no return
        AuditMessageType auditMsg = createBaseAuditMessage(assertion, target, isRequesting, webContextProperties,
            serviceName);
        auditMsg = getParticipantObjectIdentificationForResponse(request, response, assertion, auditMsg);

        return buildLogEventRequestType(auditMsg, direction, getMessageCommunityId(assertion, target, isRequesting),
            serviceName, assertion, auditMsg.getEventIdentification().getEventID().getDisplayName(),
            auditMsg.getEventIdentification().getEventOutcomeIndicator(),
            auditMsg.getEventIdentification().getEventDateTime(), getUserId(auditMsg.getActiveParticipant()));
    }

    /**
     * Adds Participant Object Identification information to auditMsg
     *
     * @param request
     * @param assertion
     * @param auditMsg
     * @return
     */
    protected abstract AuditMessageType getParticipantObjectIdentificationForRequest(T request, AssertionType assertion,
        AuditMessageType auditMsg);

    /**
     * Adds Participant Object Identification information to auditMsg
     *
     * @param request
     * @param response
     * @param assertion
     * @param auditMsg
     * @return
     */
    protected abstract AuditMessageType getParticipantObjectIdentificationForResponse(T request, K response,
        AssertionType assertion, AuditMessageType auditMsg);

    private AuditSourceIdentificationType getAuditSourceIdentificationType() {
        String hcid = HomeCommunityMap.getLocalHomeCommunityId();
        return createAuditSourceIdentification(hcid, HomeCommunityMap.getHomeCommunityName(hcid));
    }

    protected ActiveParticipant getActiveParticipant(UserType oUserInfo) {
        // Create Active Participant Section
        // create a method to call the AuditDataTransformHelper - one expectation
        ActiveParticipant participant = createActiveParticipantFromUser(oUserInfo);
        if (oUserInfo.getRoleCoded() != null) {
            participant.getRoleIDCode().add(AuditDataTransformHelper.createCodeValueType(oUserInfo.getRoleCoded()
                .getCode(), "", oUserInfo.getRoleCoded().getCodeSystemName(),
                oUserInfo.getRoleCoded().getDisplayName()));
        }
        return participant;
    }

    protected EventIdentificationType createEventIdentification(boolean isRequesting) {
        CodedValueType eventId = createCodeValueType(isRequesting ? getServiceEventIdCodeRequestor()
            : getServiceEventIdCodeResponder(), null, getServiceEventCodeSystem(),
            isRequesting ? getServiceEventDisplayRequestor() : getServiceEventDisplayResponder());

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventId, isRequesting);
        oEventIdentificationType.getEventTypeCode().add(AuditDataTransformHelper
            .createCodeValueType(getServiceEventTypeCode(), null, getServiceEventTypeCodeSystem(),
                getServiceEventTypeCodeDisplayName()));

        return oEventIdentificationType;
    }

    /**
     * Create the ActiveParticipant for an audit log record.
     *
     * @param userInfo
     * @return
     */
    private ActiveParticipant createActiveParticipantFromUser(UserType userInfo) {
        ActiveParticipant participant = new ActiveParticipant();

        // Set the User Id
        if (userInfo != null && userInfo.getUserName() != null && userInfo.getUserName().length() > 0) {
            participant.setUserID(userInfo.getUserName());
        }

        // If specified, set the User Name
        String userName = getUserName(userInfo);
        if (userName != null) {
            participant.setUserName(userName);
        }

        participant.setUserIsRequestor(Boolean.TRUE);
        return participant;
    }

    protected EventIdentificationType getEventIdentificationType(CodedValueType eventId, boolean isRequesting) {
        EventIdentificationType eventIdentification = new EventIdentificationType();

        // Set the Event Action Code
        eventIdentification.setEventActionCode(
            isRequesting ? getServiceEventActionCodeRequestor() : getServiceEventActionCodeResponder());

        // Set the Event Action Time
        try {
            GregorianCalendar today = new java.util.GregorianCalendar(TimeZone.getTimeZone("GMT"));
            DatatypeFactory factory = javax.xml.datatype.DatatypeFactory.newInstance();
            XMLGregorianCalendar calendar = factory.newXMLGregorianCalendar(
                today.get(java.util.GregorianCalendar.YEAR), today.get(java.util.GregorianCalendar.MONTH) + 1,
                today.get(java.util.GregorianCalendar.DAY_OF_MONTH), today.get(java.util.GregorianCalendar.HOUR_OF_DAY),
                today.get(java.util.GregorianCalendar.MINUTE), today.get(java.util.GregorianCalendar.SECOND),
                today.get(java.util.GregorianCalendar.MILLISECOND), 0);
            eventIdentification.setEventDateTime(calendar);
        } catch (DatatypeConfigurationException | ArrayIndexOutOfBoundsException e) {
            LOG.error("Exception when creating XMLGregorian Date: " + e.getLocalizedMessage(), e);
            // TODO -- do we need to set anything on failure, or throw an exception?
        }

        // Set the Event Outcome Indicator
        eventIdentification.setEventOutcomeIndicator(
            new BigInteger(AuditTransformsConstants.EVENT_OUTCOME_INDICATOR_SUCCESS.toString()));

        // Set the Event Id
        eventIdentification.setEventID(eventId);

        return eventIdentification;
    }

    /**
     * This method builds ActiveParticipant Source object
     *
     * @param target
     * @param serviceName
     * @param isRequesting
     * @param webContextProperties
     * @param oUserInfo
     * @return
     */
    protected ActiveParticipant getActiveParticipantSource(NhinTargetSystemType target, String serviceName,
        boolean isRequesting, Properties webContextProperties, UserType oUserInfo) {

        String ipOrHost = isRequesting ? getLocalHostAddress() : getRemoteHostAddress(webContextProperties);

        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        participant.setUserID(isRequesting ? NhincConstants.WSA_REPLY_TO
            : getInboundReplyToFromHeader(webContextProperties));
        participant.setNetworkAccessPointID(ipOrHost);
        participant.setNetworkAccessPointTypeCode(getNetworkAccessPointTypeCode(ipOrHost));
        participant.getRoleIDCode().add(AuditDataTransformHelper.createCodeValueType(
            AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE, null,
            AuditTransformsConstants.ACTIVE_PARTICIPANT_CODE_SYSTEM_NAME,
            AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE_DISPLAY_NAME));
        participant.setUserIsRequestor(Boolean.TRUE);

        if (isRequesting) {
            participant.setAlternativeUserID(ManagementFactory.getRuntimeMXBean().getName());
        }

        String userName = getUserName(oUserInfo);
        if (userName != null) {
            participant.setUserName(userName);
        }
        return participant;

    }

    /**
     * This method builds ActiveParticipant Destination object
     *
     * @param target
     * @param isRequesting
     * @param webContextProperties
     * @param serviceName
     * @return
     */
    protected ActiveParticipant getActiveParticipantDestination(NhinTargetSystemType target, boolean isRequesting,
        Properties webContextProperties, String serviceName) {

        String url;
        String ipOrHost;

        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();

        url = isRequesting ? getWebServiceUrlFromRemoteObject(target, serviceName)
            : getWebServiceRequestUrl(webContextProperties);
        if (url != null) {
            try {
                participant.setUserID(url);
                ipOrHost = new URL(url).getHost();
                participant.setNetworkAccessPointID(ipOrHost);
                participant.setNetworkAccessPointTypeCode(getNetworkAccessPointTypeCode(ipOrHost));
            } catch (MalformedURLException ex) {
                LOG.error("Couldn't parse the given NetworkAccessPointID as a URL: " + ex.getLocalizedMessage(), ex);
                // The url is null or not a valid url; for now, set the user id to anonymous
                participant.setUserID(AuditTransformsConstants.ACTIVE_PARTICIPANT_USER_ID_SOURCE);
                // TODO: For now, hardcode the value to localhost; need to find out if this needs to be set
                participant.setNetworkAccessPointTypeCode(AuditTransformsConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME);
                participant.setNetworkAccessPointID(AuditTransformsConstants.ACTIVE_PARTICIPANT_UNKNOWN_IP_ADDRESS);
            }
        }
        if (!isRequesting) {
            participant.setAlternativeUserID(ManagementFactory.getRuntimeMXBean().getName());
        }
        participant.setUserIsRequestor(Boolean.FALSE);
        participant.getRoleIDCode().add(AuditDataTransformHelper.createCodeValueType(
            AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DEST, null,
            AuditTransformsConstants.ACTIVE_PARTICIPANT_CODE_SYSTEM_NAME,
            AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME));

        return participant;
    }

    protected String getMessageCommunityId(AssertionType assertion, NhinTargetSystemType target, boolean isRequesting) {
        String communityId;
        if (isRequesting) {
            communityId = HomeCommunityMap.getCommunityIdFromTargetSystem(target);
        } else {
            communityId = HomeCommunityMap.getHomeCommunityIdFromAssertion(assertion);
        }
        return HomeCommunityMap.formatHomeCommunityId(communityId);
    }

    protected Short getNetworkAccessPointTypeCode(String hostAddress) {
        if (InetAddressValidator.getInstance().isValid(hostAddress)) {
            return AuditTransformsConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_IP;
        }
        return AuditTransformsConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME;
    }

    // Getters below are "protected" scope to allow for overriding during unit testing
    protected String getRemoteHostAddress(Properties webContextProperties) {
        if (webContextProperties != null && !webContextProperties.isEmpty() && webContextProperties.getProperty(
            NhincConstants.REMOTE_HOST_ADDRESS) != null) {

            return webContextProperties.getProperty(NhincConstants.REMOTE_HOST_ADDRESS);
        }
        return AuditTransformsConstants.ACTIVE_PARTICIPANT_UNKNOWN_IP_ADDRESS;
    }

    protected String getWebServiceRequestUrl(Properties webContextProperties) {
        if (webContextProperties != null && !webContextProperties.isEmpty() && webContextProperties.getProperty(
            NhincConstants.WEB_SERVICE_REQUEST_URL) != null) {
            return webContextProperties.getProperty(NhincConstants.WEB_SERVICE_REQUEST_URL);
        }
        return AuditTransformsConstants.ACTIVE_PARTICIPANT_USER_ID_SOURCE;
    }

    protected String getWebServiceUrlFromRemoteObject(NhinTargetSystemType target, String serviceName) {
        if (target != null && serviceName != null) {
            try {
                return getConnectionManagerCache().getEndpointURLFromNhinTarget(target, serviceName);
            } catch (ConnectionManagerException ex) {
                LOG.error("Error retrieving endpoint URL from target: " + ex.getLocalizedMessage(), ex);
            }
        }
        return AuditTransformsConstants.ACTIVE_PARTICIPANT_USER_ID_SOURCE;
    }

    protected String getInboundReplyToFromHeader(Properties webContextProperties) {

        String inboundReplyTo = null;
        if (webContextProperties != null && !webContextProperties.isEmpty() && webContextProperties.getProperty(
            NhincConstants.INBOUND_REPLY_TO) != null) {

            inboundReplyTo = webContextProperties.getProperty(NhincConstants.INBOUND_REPLY_TO);
        }
        return inboundReplyTo;
    }

    protected String getLocalHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            LOG.error("Error while returning local host Address: " + ex.getLocalizedMessage(), ex);
            return AuditTransformsConstants.ACTIVE_PARTICIPANT_UNKNOWN_IP_ADDRESS;
        }
    }

    /**
     *
     * @param participantObjectCode
     * @param participantObjectCodeRole
     * @param participantObjectIdCode
     * @param particpantObjectIdCodeSystem
     * @param participantObjectIdDisplayName
     * @return
     */
    protected ParticipantObjectIdentificationType createParticipantObjectIdentification(short participantObjectCode,
        short participantObjectCodeRole, String participantObjectIdCode, String particpantObjectIdCodeSystem,
        String participantObjectIdDisplayName) {

        ParticipantObjectIdentificationType participantObject = new ParticipantObjectIdentificationType();

        // Set the Participation Object Typecode
        participantObject.setParticipantObjectTypeCode(participantObjectCode);

        // Set the Participation Object Typecode Role
        participantObject.setParticipantObjectTypeCodeRole(participantObjectCodeRole);

        // Set the Participation Object Id Type code
        CodedValueType partObjIdTypeCode = new CodedValueType();
        partObjIdTypeCode.setCode(participantObjectIdCode);
        partObjIdTypeCode.setCodeSystemName(particpantObjectIdCodeSystem);
        partObjIdTypeCode.setDisplayName(participantObjectIdDisplayName);
        participantObject.setParticipantObjectIDTypeCode(partObjIdTypeCode);
        return participantObject;
    }

    /**
     * Create the <code>CodedValueType</code> for an audit log record.
     *
     * @param code
     * @param codeSys
     * @param codeSysName
     * @param dispName
     * @return <code>CodedValueType</code>
     */
    protected static CodedValueType createCodeValueType(String code, String codeSys, String codeSysName,
        String dispName) {

        CodedValueType codeValueType = new CodedValueType();

        // Set the Code
        if (NullChecker.isNotNullish(code)) {
            codeValueType.setCode(code);
        }

        // Set the Codesystem
        if (NullChecker.isNotNullish(codeSys)) {
            codeValueType.setCodeSystem(codeSys);
        }

        // Set the Codesystem Name
        if (NullChecker.isNotNullish(codeSysName)) {
            codeValueType.setCodeSystemName(codeSysName);
        }

        // Set the Display Name
        if (NullChecker.isNotNullish(dispName)) {
            codeValueType.setDisplayName(dispName);
        }

        return codeValueType;
    }

    /**
     * Create an <code>AuditSourceIdentificationType</code> based on the community id and name for an audit log record.
     *
     * @param communityId
     * @param communityName
     * @return <code>AuditSourceIdentificationType</code>
     */
    private static AuditSourceIdentificationType createAuditSourceIdentification(String communityId,
        String communityName) {

        AuditSourceIdentificationType auditSrcId = new AuditSourceIdentificationType();

        // Set the Audit Source Id (community id)
        if (communityId != null) {
            /* HomeCommunityMap.getHomeCommunityIdWithPrefix(communityId) mehtod
             checks & takes care of prefix if not populated*/
            auditSrcId.setAuditSourceID(HomeCommunityMap.getHomeCommunityIdWithPrefix(communityId));
        }

        // If specified, set the Audit Enterprise Site Id (community name)
        if (communityName != null) {
            auditSrcId.setAuditEnterpriseSiteID(communityName);
        }

        return auditSrcId;
    }

    private AuditMessageType createBaseAuditMessage(AssertionType assertion, NhinTargetSystemType target,
        boolean isRequesting, Properties webContextProperties, String serviceName) {

        AuditMessageType auditMsg = new AuditMessageType();
        // ****************************Construct Event Identification**************************

        auditMsg.setEventIdentification(createEventIdentification(isRequesting));

        // *********************************Construct Active Participant************************
        // Active Participant for human requester only required for requesting gateway
        if (isRequesting) {
            ActiveParticipant participantHumanFactor = getActiveParticipant(assertion.getUserInfo());
            if (participantHumanFactor != null) {
                auditMsg.getActiveParticipant().add(participantHumanFactor);
            }
        }
        ActiveParticipant participantSource = getActiveParticipantSource(target, serviceName, isRequesting,
            webContextProperties, assertion.getUserInfo());
        ActiveParticipant participantDestination = getActiveParticipantDestination(target,
            isRequesting, webContextProperties, serviceName);
        auditMsg.getActiveParticipant().add(participantSource);
        auditMsg.getActiveParticipant().add(participantDestination);

        // ******************************Construct Participation Object Identification*************
        // Assign ParticipationObjectIdentification
        // Create the AuditSourceIdentifierType object
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType();

        // ******************************Construct Audit Source Identification**********************
        auditMsg.getAuditSourceIdentification().add(auditSource);
        return auditMsg;
    }

    protected String getUserName(UserType userInfo) {
        String userName = null;
        if (userInfo != null && userInfo.getPersonName() != null) {
            if (NullChecker.isNotNullish(userInfo.getPersonName().getGivenName())) {

                userName = userInfo.getPersonName().getGivenName();
            }

            if (NullChecker.isNotNullish(userInfo.getPersonName().getFamilyName())) {

                if (userName != null) {
                    userName += " " + userInfo.getPersonName().getFamilyName();
                } else {
                    userName = userInfo.getPersonName().getFamilyName();
                }
            }

        }
        return userName;
    }

    private LogEventRequestType buildLogEventRequestType(AuditMessageType auditMsg, String direction, String communityId,
        String serviceName, AssertionType assertion, String eventId, BigInteger outcome, XMLGregorianCalendar eventDate,
        String userId) {

        LogEventRequestType result = new LogEventRequestType();
        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        //set the target community identifier
        result.setRemoteHCID(communityId);
        result.setEventType(serviceName);
        result.setAssertion(assertion);
        result.setEventID(eventId);
        result.setEventOutcomeIndicator(outcome);
        result.setUserId(userId);
        result.setEventTimestamp(eventDate);
        result.setRelatesTo(getRelatesTo(assertion));
        return result;
    }

    protected ConnectionManagerCache getConnectionManagerCache() {
        return ConnectionManagerCache.getInstance();
    }

    protected String createUUID() {
        return UUID.randomUUID().toString();
    }

    protected abstract String getServiceEventIdCodeRequestor();

    protected abstract String getServiceEventIdCodeResponder();

    protected abstract String getServiceEventCodeSystem();

    protected abstract String getServiceEventDisplayRequestor();

    protected abstract String getServiceEventDisplayResponder();

    protected abstract String getServiceEventTypeCode();

    protected abstract String getServiceEventTypeCodeSystem();

    protected abstract String getServiceEventTypeCodeDisplayName();

    protected abstract String getServiceEventActionCodeRequestor();

    protected abstract String getServiceEventActionCodeResponder();

    //PD Deferred Response services require incoming Request. so getters and setters are created.
    protected void setRequest(T request) {
        this.request = request;
    }

    protected T getRequest() {
        return request;
    }

    //Getter and setters are provided for NhinTargetSystemType as DQ requires to store the responding gateway HCID in
    //ParticpantObjectDetail element
    protected NhinTargetSystemType getTarget() {
        return target;
    }

    protected void setTarget(NhinTargetSystemType target) {
        this.target = target;
    }

    //DS Deferred Response services require assertion. so getters and setters are created.
    protected AssertionType getAssertion() {
        return assertion;
    }

    protected void setAssertion(AssertionType assertion) {
        this.assertion = assertion;
    }

    private String getUserId(List<ActiveParticipant> participants) {
        for (ActiveParticipant obj : participants) {
            if (NullChecker.isNotNullish(obj.getRoleIDCode())
                && !obj.getRoleIDCode().get(0).getDisplayName().equals(
                AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE_DISPLAY_NAME)
                && !obj.getRoleIDCode().get(0).getDisplayName().equals(
                AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME)) {
                return obj.getUserID();
            }
        }
        return null;
    }

    private String getRelatesTo(AssertionType assertion) {
        return NullChecker.isNotNullish(assertion.getRelatesToList()) ? assertion.getRelatesToList().get(0) : null;
    }
}
