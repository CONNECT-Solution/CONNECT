/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docretrieve.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditMessageType.ActiveParticipant;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import com.services.nhinc.schema.auditmessage.TypeValuePairType;
import gov.hhs.fha.nhinc.audit.AuditTransformsConstants;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.docretrieve.audit.DocRetrieveAuditTransformsConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.audit.AuditDataTransformHelper;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.xml.bind.JAXBException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is designed for supporting Audit Logging for Retrieve Document.
 *
 * @author vimehta
 */
public class DocRetrieveAuditTransforms
extends AuditTransforms<RetrieveDocumentSetRequestType, RetrieveDocumentSetResponseType> {

    private static final Logger LOG = LoggerFactory.getLogger(DocRetrieveAuditTransforms.class);

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForRequest(RetrieveDocumentSetRequestType request,
        AssertionType assertion, AuditMessageType auditMsg) {

        /*
         * check to see if unique Patient Id exist or not - if created then only ParticipantObjectIdentification object
         * will be created otherwise not
         */
        if (CollectionUtils.isNotEmpty(assertion.getUniquePatientId())
            && StringUtils.isNotEmpty(assertion.getUniquePatientId().get(0))) {
            auditMsg = createPatientParticipantObjectIdentification(auditMsg, assertion.getUniquePatientId().get(0));
        }
        try {
            auditMsg = getDocumentParticipantObjectIdentificationForRequest(request, auditMsg);
        } catch (JAXBException ex) {
            LOG.error("Error while creating ParticipantObjectIdentificationQueryByParameters segment : "
                + ex.getLocalizedMessage(), ex);
        }

        return auditMsg;
    }

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForResponse(RetrieveDocumentSetRequestType request,
        RetrieveDocumentSetResponseType response, AssertionType assertion, AuditMessageType auditMsg) {

        try {
            auditMsg = getDocumentParticipantObjectIdentificationForResponse(response, auditMsg);
        } catch (JAXBException ex) {
            LOG.error("Error while creating ParticipantObjectIdentificationQueryByParameters segment : "
                + ex.getLocalizedMessage(), ex);
        }

        return auditMsg;
    }

    /**
     * Gets Unique Document Request Id from request
     *
     * @param request
     * @return
     */
    private static String getDocumentUniqueIdFromRequest(RetrieveDocumentSetRequestType request) {
        String documentUniqueId = null;

        if (request != null && request.getDocumentRequest() != null && request.getDocumentRequest().get(0) != null
            && request.getDocumentRequest().get(0).getDocumentUniqueId() != null) {
            documentUniqueId = request.getDocumentRequest().get(0).getDocumentUniqueId();
        } else {
            LOG.error("DocumentId doesn't exist in the received RetrieveDocumentSetRequestType message");
        }
        return documentUniqueId;
    }

    private static String getRepositoryUniqueIdFromRequest(RetrieveDocumentSetRequestType request) {
        String repositoryUniqueId = null;

        if (request != null && request.getDocumentRequest() != null && request.getDocumentRequest().get(0) != null
            && request.getDocumentRequest().get(0).getRepositoryUniqueId() != null) {
            repositoryUniqueId = request.getDocumentRequest().get(0).getRepositoryUniqueId();
        } else {
            LOG.error("DocumentId doesn't exist in the received RetrieveDocumentSetRequestType message");
        }
        return repositoryUniqueId;
    }

    private static String getHomeCommunityIdFromRequest(RetrieveDocumentSetRequestType request) {
        String homeCommunityId = null;

        if (request != null && request.getDocumentRequest() != null && request.getDocumentRequest().get(0) != null
            && request.getDocumentRequest().get(0).getHomeCommunityId() != null) {

            homeCommunityId = HomeCommunityMap
                .getHomeCommunityIdWithPrefix(request.getDocumentRequest().get(0).getHomeCommunityId());

        } else {
            LOG.error("HomeCommunityId doesn't exist in the received RetrieveDocumentSetRequestType message");
        }
        return homeCommunityId;
    }

    /**
     * This method gets unique repository unique id from response
     *
     * @param response
     * @return
     */
    private static String getRepositoryUniqueIdFromResponse(RetrieveDocumentSetResponseType response) {
        String repositoryUniqueId = null;

        if (response != null && CollectionUtils.isNotEmpty(response.getDocumentResponse())
            && response.getDocumentResponse().get(0) != null
            && response.getDocumentResponse().get(0).getRepositoryUniqueId() != null) {
            repositoryUniqueId = response.getDocumentResponse().get(0).getRepositoryUniqueId();
        } else {
            LOG.error("DocumentId doesn't exist in RetrieveDocumentSetResponseType message");
        }
        return repositoryUniqueId;
    }

    /**
     * This method gets home community id from response
     *
     * @param response
     * @return
     */
    private static String getHomeCommunityIdFromResponse(RetrieveDocumentSetResponseType response) {
        String homeCommunityId = null;

        if (response != null && CollectionUtils.isNotEmpty(response.getDocumentResponse())
            && response.getDocumentResponse().get(0) != null
            && response.getDocumentResponse().get(0).getHomeCommunityId() != null) {
            homeCommunityId = HomeCommunityMap
                .getHomeCommunityIdWithPrefix(response.getDocumentResponse().get(0).getHomeCommunityId());
        } else {
            LOG.error("HomeCommunityId doesn't exist in RetrieveDocumentSetResponseType message");
        }
        return homeCommunityId;
    }

    private static String getDocumentUniqueIdFromResponse(RetrieveDocumentSetResponseType response) {
        if (response != null && CollectionUtils.isNotEmpty(response.getDocumentResponse())
            && response.getDocumentResponse().get(0) != null
            && response.getDocumentResponse().get(0).getDocumentUniqueId() != null) {
            return response.getDocumentResponse().get(0).getDocumentUniqueId();
        }
        return null;
    }

    private AuditMessageType createPatientParticipantObjectIdentification(AuditMessageType auditMsg,
        String uniquePatientId) {

        ParticipantObjectIdentificationType participantObject = createParticipantObjectIdentification(
            DocRetrieveAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE,
            DocRetrieveAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE,
            DocRetrieveAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE,
            DocRetrieveAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM,
            DocRetrieveAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME);

        participantObject.setParticipantObjectID(uniquePatientId);
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        return auditMsg;
    }

    private AuditMessageType getDocumentParticipantObjectIdentificationForRequest(
        RetrieveDocumentSetRequestType request, AuditMessageType auditMsg) throws JAXBException {

        ParticipantObjectIdentificationType participantObject = buildBaseParticipantObjectIdentificationType();
        participantObject.setParticipantObjectID(getDocumentUniqueIdFromRequest(request));
        String repoId = getRepositoryUniqueIdFromRequest(request);
        String homeId = getHomeCommunityIdFromRequest(request);
        if (repoId != null && homeId != null) {
            getParticipantObjectDetail(repoId.getBytes(), homeId.getBytes(), participantObject);
        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);

        return auditMsg;
    }

    private AuditMessageType getDocumentParticipantObjectIdentificationForResponse(
        RetrieveDocumentSetResponseType response, AuditMessageType auditMsg) throws JAXBException {

        ParticipantObjectIdentificationType participantObject = buildBaseParticipantObjectIdentificationType();
        participantObject.setParticipantObjectID(getDocumentUniqueIdFromResponse(response));
        String repoResponseId = getRepositoryUniqueIdFromResponse(response);
        String hcidResponseId = getHomeCommunityIdFromResponse(response);
        if (repoResponseId != null && hcidResponseId != null) {
            getParticipantObjectDetail(repoResponseId.getBytes(), hcidResponseId.getBytes(), participantObject);
        }
        auditMsg.getParticipantObjectIdentification().add(participantObject);
        return auditMsg;
    }

    private static void getParticipantObjectDetail(byte[] repositoryUniqueId, byte[] homeCommunityId,
        ParticipantObjectIdentificationType participantObject) {

        participantObject.getParticipantObjectDetail().add(getTypeValuePair(
            DocRetrieveAuditTransformsConstants.PARTICIPANT_OBJECT_DETAIL_REPOSITORY_UNIQUE_TYPE, repositoryUniqueId));
        participantObject.getParticipantObjectDetail().add(getTypeValuePair(
            DocRetrieveAuditTransformsConstants.PARTICIPANT_OBJECT_DETAIL_HOME_COMMUNITY_ID_TYPE, homeCommunityId));
    }

    private ParticipantObjectIdentificationType buildBaseParticipantObjectIdentificationType() {
        ParticipantObjectIdentificationType participantObject = createParticipantObjectIdentification(
            DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_TYPE_CODE,
            DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_TYPE_CODE_ROLE,
            DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_ID_TYPE_CODE,
            DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_ID_TYPE_CODE_SYSTEM,
            DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_ID_TYPE_DISPLAY_NAME);

        return participantObject;
    }

    @Override
    protected String getServiceEventIdCodeRequestor() {
        return DocRetrieveAuditTransformsConstants.EVENTID_CODE_REQUESTOR;
    }

    @Override
    protected String getServiceEventIdCodeResponder() {
        return DocRetrieveAuditTransformsConstants.EVENTID_CODE_RESPONDER;
    }

    @Override
    protected String getServiceEventCodeSystem() {
        return DocRetrieveAuditTransformsConstants.EVENTID_CODE_SYSTEM;
    }

    @Override
    protected String getServiceEventDisplayRequestor() {
        return DocRetrieveAuditTransformsConstants.EVENTID_CODE_DISPLAY_REQUESTOR;
    }

    @Override
    protected String getServiceEventDisplayResponder() {
        return DocRetrieveAuditTransformsConstants.EVENTID_CODE_DISPLAY_RESPONDER;
    }

    @Override
    protected String getServiceEventTypeCode() {
        return DocRetrieveAuditTransformsConstants.EVENTTYPE_CODE_REQUESTOR;
    }

    @Override
    protected String getServiceEventTypeCodeSystem() {
        return DocRetrieveAuditTransformsConstants.EVENTTYPE_CODE_SYSTEM;
    }

    @Override
    protected String getServiceEventTypeCodeDisplayName() {
        return DocRetrieveAuditTransformsConstants.EVENTTYPE_CODE_DISPLAY_NAME;
    }

    @Override
    protected String getServiceEventActionCodeRequestor() {
        return DocRetrieveAuditTransformsConstants.EVENT_ACTION_CODE_REQUESTOR;
    }

    @Override
    protected String getServiceEventActionCodeResponder() {
        return DocRetrieveAuditTransformsConstants.EVENT_ACTION_CODE_RESPONDER;
    }

    /**
     * This method builds ActiveParticipant Source object
     *
     * @param target
     * @param serviceName
     * @param isRequesting
     * @param webContextProperties
     * @return
     */
    @Override
    protected ActiveParticipant getActiveParticipantSource(NhinTargetSystemType target, String serviceName,
        boolean isRequesting, Properties webContextProperties, UserType oUserInfo) {

        ActiveParticipant participant = new ActiveParticipant();

        /*
         * if Retrieve Document Service, Activeparticipant Source and destination is same on both initiator and
         * responder side. And for this service, source is considered who generates the document, meaning that
         * destination that generates the document is considered source for RD and vice versa for destination details
         * for ActiveParticipant
         */
        String hostDetails;
        if (isRequesting) {
            hostDetails = getWebServiceUrlFromRemoteObject(target, serviceName);
        } else {
            hostDetails = getWebServiceRequestUrl(webContextProperties);
        }

        if (hostDetails != null) {
            try {
                URL url = new URL(hostDetails);
                participant.setUserID(hostDetails);
                hostDetails = url.getHost();
                participant.setNetworkAccessPointID(hostDetails);
                participant.setNetworkAccessPointTypeCode(getNetworkAccessPointTypeCode(hostDetails));
            } catch (MalformedURLException ex) {
                LOG.error("Error while extracting remote address : " + ex.getLocalizedMessage(), ex);
                // The url is null or not a valid url; for now, set the user id to anonymous
                participant.setUserID(NhincConstants.WSA_REPLY_TO);
                // TODO: For now, hardcode the value to localhost; need to find out if this needs to be set
                participant.setNetworkAccessPointTypeCode(AuditTransformsConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME);
                participant.setNetworkAccessPointID(AuditTransformsConstants.ACTIVE_PARTICIPANT_UNKNOWN_IP_ADDRESS);
            }
        }

        /*
         * if it is not RD service or required if RD and it's Initiator - ActiveParticipant Destination and Responder -
         * ActiveParticipant Source
         */
        if (!isRequesting) {
            participant.setAlternativeUserID(ManagementFactory.getRuntimeMXBean().getName());
        }

        String userName = getUserName(oUserInfo);
        if (userName != null) {
            participant.setUserName(userName);
        }

        participant.getRoleIDCode()
        .add(AuditDataTransformHelper.createCodeValueType(
            AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE, null,
            AuditTransformsConstants.ACTIVE_PARTICIPANT_CODE_SYSTEM_NAME,
            AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE_DISPLAY_NAME));

        participant.setUserIsRequestor(Boolean.FALSE);

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
    @Override
    protected ActiveParticipant getActiveParticipantDestination(NhinTargetSystemType target, boolean isRequesting,
        Properties webContextProperties, String serviceName) {
        ActiveParticipant participant = new ActiveParticipant();
        String hostAddress;

        if (isRequesting) {
            hostAddress = getLocalHostAddress();
        } else {
            hostAddress = getRemoteHostAddress(webContextProperties);
        }

        participant.setUserID(NhincConstants.WSA_REPLY_TO);
        participant.setAlternativeUserID(ManagementFactory.getRuntimeMXBean().getName());
        participant.setNetworkAccessPointID(hostAddress);
        participant.setNetworkAccessPointTypeCode(getNetworkAccessPointTypeCode(hostAddress));

        /*
         * for RD service it is opposite than other services. For RD service it is required in initiating side and for
         * other services it is required for responding side
         */
        if (isRequesting) {
            participant.setAlternativeUserID(ManagementFactory.getRuntimeMXBean().getName());
        }

        // for RD service condition is opposite than other service
        participant.setUserIsRequestor(Boolean.TRUE);

        participant.getRoleIDCode().add(
            AuditDataTransformHelper.createCodeValueType(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DEST,
                null, AuditTransformsConstants.ACTIVE_PARTICIPANT_CODE_SYSTEM_NAME,
                AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME));
        return participant;
    }

    private static TypeValuePairType getTypeValuePair(String key, byte[] value) {
        TypeValuePairType type = new TypeValuePairType();
        type.setType(key);
        type.setValue(value);
        return type;
    }
}
