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
package gov.hhs.fha.nhinc.corex12.docsubmission.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import com.services.nhinc.schema.auditmessage.TypeValuePairType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.CORE_X12AuditDataTransformConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.audit.AuditDataTransformConstants;
import gov.hhs.fha.nhinc.transform.audit.AuditDataTransformHelper;
import gov.hhs.fha.nhinc.transform.audit.XDRTransforms;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.io.ByteArrayOutputStream;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.log4j.Logger;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;

/**
 * Class provides generic Audit Data Transformation APIs utilized across all the X12 Core services
 *
 * @author nsubrama
 */
public class CORE_X12AuditDataTransform {

    private static final XDRTransforms oXDR = new XDRTransforms();
    private static final Logger LOG = Logger.getLogger(CORE_X12AuditDataTransform.class);

    /**
     * Transforms the CORE X12 Batch Request or Response message to Audit logging specific message.
     *
     * @param message
     * @param assertion
     * @param target
     * @param direction
     * @param _interface
     * @param isRequesting
     * @param webContextProperties
     * @param serviceName
     * @return LogEventRequestType
     */
    public LogEventRequestType transformX12MsgToAuditMsg(Object message, AssertionType assertion,
        NhinTargetSystemType target, String direction, String _interface, boolean isRequesting, Properties webContextProperties, String serviceName) {
        LOG.trace("Begin CORE_X12AuditDataTransform -> transformX12MsgToAuditMsg() -- NHIN");
        if (message == null) {
            LOG.error("message Object was null");
            return null;
        }
        if (assertion == null) {
            LOG.error("Assertion was null");
            return null;
        }

        LogEventRequestType result = new LogEventRequestType();
        AuditMessageType auditMsg = new AuditMessageType();

        //****************************Construct Event Identification**************************
        CodedValueType eventID = AuditDataTransformHelper.createCodeValueType(CORE_X12AuditDataTransformConstants.EVENT_ID_CODE_X12, null,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, isRequesting ? CORE_X12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12_EXPORT : CORE_X12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12_IMPORT);

        EventIdentificationType oEventIdentificationType = getEventIdentificationType(eventID, isRequesting);
        oEventIdentificationType.getEventTypeCode().add(AuditDataTransformHelper.createCodeValueType(CORE_X12AuditDataTransformConstants.EVENT_ID_CODE_SYS_CODE_X12, null,
            CORE_X12AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_X12, CORE_X12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12REALTIME));
        auditMsg.setEventIdentification(oEventIdentificationType);

        //*********************************Construct Active Participant************************
        //Active Participant for human requester only required for requesting gateway
        if (isRequesting) {
            AuditMessageType.ActiveParticipant participantHumanFactor = getActiveParticipant(assertion.getUserInfo());
            auditMsg.getActiveParticipant().add(participantHumanFactor);
        }
        AuditMessageType.ActiveParticipant participantSource = getActiveParticipantSource(isRequesting, webContextProperties);
        AuditMessageType.ActiveParticipant participantDestination = getActiveParticipantDestination(target, isRequesting, webContextProperties, serviceName);
        auditMsg.getActiveParticipant().add(participantSource);
        auditMsg.getActiveParticipant().add(participantDestination);

        //******************************Constuct Participation Object Identification*************
        // Assign ParticipationObjectIdentification
        ParticipantObjectIdentificationType participantObject = getParticipantObjectIdentificationType(message);
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        //Create the AuditSourceIdentifierType object
        String communityId = getMessageCommunityId(assertion, target, isRequesting);
        AuditSourceIdentificationType auditSource = getAuditSourceIdentificationType();

        //******************************Constuct Audit Source Identification**********************
        auditMsg.getAuditSourceIdentification().add(auditSource);

        //Set the all the required data
        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);
        //set the target community identifier
        result.setCommunityId(communityId);

        LOG.trace("End CORE_X12AuditDataTransform -> transformX12MsgToAuditMsg() -- NHIN");
        return result;
    }

    public AuditSourceIdentificationType getAuditSourceIdentificationType() {
        String hcid = HomeCommunityMap.getLocalHomeCommunityId();
        String homeCommunityName = HomeCommunityMap.getHomeCommunityName(hcid);
        return AuditDataTransformHelper.createAuditSourceIdentification(hcid, homeCommunityName);
    }

    protected AuditMessageType.ActiveParticipant getActiveParticipant(UserType oUserInfo) {
        // Create Active Participant Section
        // create a method to call the AuditDataTransformHelper - one expectation
        AuditMessageType.ActiveParticipant participant = createActiveParticipantFromUser(
            oUserInfo, Boolean.TRUE);
        if (oUserInfo.getRoleCoded() != null) {
            participant.getRoleIDCode().add(AuditDataTransformHelper.createCodeValueType(oUserInfo.getRoleCoded().getCode(), "",
                oUserInfo.getRoleCoded().getCodeSystemName(), oUserInfo.getRoleCoded().getDisplayName()));
        }
        return participant;
    }

    /**
     * Create the ActiveParticipant for an audit log record.
     *
     * @param userInfo
     * @param userIsReq
     * @return
     */
    protected AuditMessageType.ActiveParticipant createActiveParticipantFromUser(UserType userInfo,
        Boolean userIsReq) {
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();

        // Set the User Id
        String userId = null;
        if (userInfo != null && userInfo.getUserName() != null && userInfo.getUserName().length() > 0) {
            userId = userInfo.getUserName();
            participant.setUserID(userId);
        }

        // If specified, set the User Name
        String userName = null;
        if (userInfo != null && userInfo.getPersonName() != null) {
            if (userInfo.getPersonName().getGivenName() != null && userInfo.getPersonName().getGivenName().length() > 0) {
                userName = userInfo.getPersonName().getGivenName();
            }

            if (userInfo.getPersonName().getFamilyName() != null
                && userInfo.getPersonName().getFamilyName().length() > 0) {
                if (userName != null) {
                    userName += (" " + userInfo.getPersonName().getFamilyName());
                } else {
                    userName = userInfo.getPersonName().getFamilyName();
                }
            }
            if (userName != null) {
                participant.setUserName(userName);
            }
        }

        participant.setUserIsRequestor(userIsReq);
        return participant;
    }

    protected EventIdentificationType getEventIdentificationType(CodedValueType eventID, boolean isRequesting) {
        EventIdentificationType oEventIdentificationType = AuditDataTransformHelper.createEventIdentification(
            isRequesting ? AuditDataTransformConstants.EVENT_ACTION_CODE_READ : AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE,
            AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID);
        return oEventIdentificationType;
    }

    protected AuditMessageType.ActiveParticipant getActiveParticipantSource(boolean isRequesting, Properties webContextProperties) {
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        participant.setUserID(CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_USER_ID_SOURCE);
        participant.setAlternativeUserID(ManagementFactory.getRuntimeMXBean().getName());
        String hostAddress = null;
        hostAddress = isRequesting ? getLocalHostAddress() : getRemoteHostAddress(webContextProperties);
        participant.setNetworkAccessPointID(hostAddress);
        participant.setNetworkAccessPointTypeCode(getNetworkAccessPointTypeCode(hostAddress));

        participant.getRoleIDCode().add(AuditDataTransformHelper.createCodeValueType(CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_CDE, null,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_SOURCE_DISPLAY_NAME));
        participant.setUserIsRequestor(isRequesting);
        return participant;
    }

    protected AuditMessageType.ActiveParticipant getActiveParticipantDestination(NhinTargetSystemType target, boolean isRequesting, Properties webContextProeprties, String serviceName) {
        String strUrl = null;
        String strHost = null;
        boolean setDefaultValue = true;

        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();

        strUrl = isRequesting ? getWebServiceUrlFromRemoteObject(target, serviceName) : getWebServiceRequestURL(webContextProeprties);
        if (strUrl != null) {
            try {
                URL url = new URL(strUrl);
                participant.setUserID(strUrl);
                strHost = url.getHost();
                participant.setNetworkAccessPointID(strHost);
                participant.setNetworkAccessPointTypeCode(getNetworkAccessPointTypeCode(strHost));
                setDefaultValue = false;
            } catch (MalformedURLException ex) {
                LOG.error(ex);
                setDefaultValue = true;
            }
        }
        //if the url is null or not a valid url
        if (setDefaultValue) {
            //for now set the user id to anonymouns
            participant.setUserID(CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_USER_ID_SOURCE);
            //for now hardcode the value to localhost, need to find out if this needs to be set
            participant.setNetworkAccessPointTypeCode(CORE_X12AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME);
            participant.setNetworkAccessPointID(CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_UNKNOWN_IP_ADDRESS);
        }
        participant.setUserIsRequestor(Boolean.FALSE);
        participant.getRoleIDCode().add(AuditDataTransformHelper.createCodeValueType(CORE_X12AuditDataTransformConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DEST, null,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME));
        return participant;
    }

    protected ParticipantObjectIdentificationType getParticipantObjectIdentificationType(Object msg) {
        String payloadId = null;
        byte[] byteArray = null;

        // Assign Participat Object Identification
        ParticipantObjectIdentificationType participantObject = new ParticipantObjectIdentificationType();
        // Set the Partipation Object Id (patient id)
        if (msg != null) {
            if (msg instanceof COREEnvelopeRealTimeResponse) {
                payloadId = ((COREEnvelopeRealTimeResponse) msg).getPayloadID();
            } else if (msg instanceof COREEnvelopeRealTimeRequest) {
                payloadId = ((COREEnvelopeRealTimeRequest) msg).getPayloadID();
            } else if (msg instanceof COREEnvelopeBatchSubmission) {
                payloadId = ((COREEnvelopeBatchSubmission) msg).getPayloadID();
            } else if (msg instanceof COREEnvelopeBatchSubmissionResponse) {
                payloadId = ((COREEnvelopeBatchSubmissionResponse) msg).getPayloadID();
            }
            participantObject.setParticipantObjectID(payloadId);
        } else {
            return participantObject;
        }
        byteArray = marshallToByteArray(msg);
        TypeValuePairType oType = new TypeValuePairType();
        oType.setValue(byteArray);
        participantObject.getParticipantObjectDetail().add(oType);

        // Set the Participation Object Typecode
        participantObject.setParticipantObjectTypeCode(AuditDataTransformConstants.PARTICIPANT_OJB_TYPE_CODE_SYSTEM);

        // Set the Participation Object Typecode Role
        participantObject.setParticipantObjectTypeCodeRole(CORE_X12AuditDataTransformConstants.PARTICIPANT_OBJ_TYPE_CODE_ROLE_X12);

        // Set the Participation Object Id Type code
        CodedValueType partObjIdTypeCode = new CodedValueType();
        partObjIdTypeCode.setCode(payloadId);
        partObjIdTypeCode.setCodeSystemName(CORE_X12AuditDataTransformConstants.CAQH_X12_CONNECTIVITY_CODED_SYS_NAME);
        partObjIdTypeCode.setDisplayName(CORE_X12AuditDataTransformConstants.CAQH_X12_CONNECTIVITY_CODED_SYS_DISPLAY_NAME);
        participantObject.setParticipantObjectIDTypeCode(partObjIdTypeCode);

        return participantObject;
    }

    protected byte[] marshallToByteArray(Object msg) {
        byte[] bObject = null;
        COREEnvelopeRealTimeRequest oRequestNoPayload = null;
        COREEnvelopeRealTimeResponse oResponseNoPayload = null;
        COREEnvelopeBatchSubmission oBatchRequestNoPayload = null;
        COREEnvelopeBatchSubmissionResponse oBatchResponseNoPayload = null;
        QName xmlQname = null;
        JAXBContextHandler oHandler = new JAXBContextHandler();
        try {
            JAXBContext jc = oHandler.getJAXBContext(CORE_X12AuditDataTransformConstants.CORE_X12_JAXB_CONTEXT);
            Marshaller marshaller = jc.createMarshaller();
            Object element = null;
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            if (msg instanceof COREEnvelopeRealTimeRequest) {
                oRequestNoPayload = (COREEnvelopeRealTimeRequest) msg;
                oRequestNoPayload.setPayload("");
                xmlQname = new QName(CORE_X12AuditDataTransformConstants.CORE_X12_NAMESPACE_URI, CORE_X12AuditDataTransformConstants.CORE_X12_REQUEST_LOCALPART);
                element = new JAXBElement<COREEnvelopeRealTimeRequest>(xmlQname, COREEnvelopeRealTimeRequest.class, oRequestNoPayload);
            } else if (msg instanceof COREEnvelopeRealTimeResponse) {
                oResponseNoPayload = (COREEnvelopeRealTimeResponse) msg;
                oResponseNoPayload.setPayload("");
                xmlQname = new javax.xml.namespace.QName(CORE_X12AuditDataTransformConstants.CORE_X12_NAMESPACE_URI, CORE_X12AuditDataTransformConstants.CORE_X12_RESPONSE_LOCALPART);
                element = new JAXBElement<COREEnvelopeRealTimeResponse>(xmlQname, COREEnvelopeRealTimeResponse.class, oResponseNoPayload);
            } else if (msg instanceof COREEnvelopeBatchSubmission) {
                oBatchRequestNoPayload = (COREEnvelopeBatchSubmission) msg;
                oBatchRequestNoPayload.setPayload(null);
                xmlQname = new javax.xml.namespace.QName(CORE_X12AuditDataTransformConstants.CORE_X12_NAMESPACE_URI, CORE_X12AuditDataTransformConstants.CORE_X12_BATCH_REQUEST_LOCALPART);
                element = new JAXBElement<COREEnvelopeBatchSubmission>(xmlQname, COREEnvelopeBatchSubmission.class, oBatchRequestNoPayload);
            } else if (msg instanceof COREEnvelopeBatchSubmissionResponse) {
                oBatchResponseNoPayload = (COREEnvelopeBatchSubmissionResponse) msg;
                oBatchResponseNoPayload.setPayload(null);
                xmlQname = new javax.xml.namespace.QName(CORE_X12AuditDataTransformConstants.CORE_X12_NAMESPACE_URI, CORE_X12AuditDataTransformConstants.CORE_X12_BATCH_RESPONSE_LOCALPART);
                element = new JAXBElement<COREEnvelopeBatchSubmissionResponse>(xmlQname, COREEnvelopeBatchSubmissionResponse.class, oBatchResponseNoPayload);
            }
            marshaller.marshal(element, baOutStrm);
            bObject = baOutStrm.toByteArray();
        } catch (JAXBException ex) {
            LOG.error("Error thrown from CORE_X12AuditDataTransform -> marshallToByteArray() service " + ex.getMessage());
        }
        return bObject;
    }

    protected String getRemoteHostAddress(Properties webContextProeprties) {
        if (webContextProeprties != null && !webContextProeprties.isEmpty() && webContextProeprties.getProperty(NhincConstants.REMOTE_HOST_ADDRESS) != null) {
            return webContextProeprties.getProperty(NhincConstants.REMOTE_HOST_ADDRESS);
        }
        return CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_UNKNOWN_IP_ADDRESS;
    }

    protected Short getNetworkAccessPointTypeCode(String hostAddress) {
        if (InetAddressValidator.getInstance().isValid(hostAddress)) {
            return AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_IP;
        } else {
            return CORE_X12AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME;
        }
    }

    protected String getWebServiceRequestURL(Properties webContextProeprties) {
        if (webContextProeprties != null && !webContextProeprties.isEmpty() && webContextProeprties.getProperty(NhincConstants.WEB_SERVICE_REQUEST_URL) != null) {
            return webContextProeprties.getProperty(NhincConstants.WEB_SERVICE_REQUEST_URL);
        }
        return CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_USER_ID_SOURCE;
    }

    protected String getWebServiceUrlFromRemoteObject(NhinTargetSystemType target, String serviceName) {
        if (target != null && serviceName != null) {
            try {
                return getConnectionManagerCache().getEndpointURLFromNhinTarget(target, serviceName);
            } catch (ConnectionManagerException ex) {
                LOG.error(ex);
            }
        }
        return CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_USER_ID_SOURCE;
    }

    protected ConnectionManagerCache getConnectionManagerCache() {
        return ConnectionManagerCache.getInstance();
    }

    protected String getLocalHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            return CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_UNKNOWN_IP_ADDRESS;
        }
    }

    protected String getMessageCommunityId(AssertionType assertion, NhinTargetSystemType target, boolean isRequesting) {
        String communityId = null;
        if (isRequesting) {
            communityId = HomeCommunityMap.getCommunityIdFromTargetSystem(target);
        } else {
            communityId = HomeCommunityMap.getHomeCommunityIdFromAssertion(assertion);
        }
        return HomeCommunityMap.formatHomeCommunityId(communityId);
    }
}
