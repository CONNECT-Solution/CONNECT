package gov.hhs.fha.nhinc.docretrieve.audit.transform;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.log4j.Logger;
import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import com.services.nhinc.schema.auditmessage.TypeValuePairType;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.audit.DocRetrieveAuditTransformsConstants;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 * This class is designed for supporting Audit Logging for Retrieve Document.
 *
 * @author vimehta
 */
public class DocRetrieveAuditTransforms
    extends AuditTransforms<RetrieveDocumentSetRequestType, RetrieveDocumentSetResponseType> {

    private static final Logger LOG = Logger.getLogger(DocRetrieveAuditTransforms.class);

    private static final String JAXB_HL7_CONTEXT_NAME = "org.hl7.v3";

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForRequest(RetrieveDocumentSetRequestType request,
        AssertionType assertion, AuditMessageType auditMsg) {

        // check to see if unique Patient Id exist or not - if created then only ParticipantObjectIdentification object will be created otherwise not
        if (assertion.getUniquePatientId() != null && assertion.getUniquePatientId().size() > 0 && assertion.getUniquePatientId().get(0).length() > 0) {
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
    protected AuditMessageType getParticipantObjectIdentificationForResponse(RetrieveDocumentSetRequestType request, RetrieveDocumentSetResponseType response,
        AssertionType assertion, AuditMessageType auditMsg) {

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
    private String getDocumentUniqueIdFromRequest(RetrieveDocumentSetRequestType request) {
        String documentUniqueId = null;

        if (request != null && request.getDocumentRequest() != null
            && request.getDocumentRequest().get(0) != null
            && request.getDocumentRequest().get(0).getDocumentUniqueId() != null) {
            documentUniqueId = request.getDocumentRequest().get(0).getDocumentUniqueId();
        } else {
            LOG.error("DocumentId doesn't exist in the received RetrieveDocumentSetRequestType message");
        }
        return documentUniqueId;
    }

    private String getRepositoryUniqueIdFromRequest(RetrieveDocumentSetRequestType request) {
        String repositoryUniqueId = null;

        if (request != null
            && request.getDocumentRequest() != null
            && request.getDocumentRequest().get(0) != null
            && request.getDocumentRequest().get(0).getRepositoryUniqueId() != null) {
            repositoryUniqueId = request.getDocumentRequest().get(0).getRepositoryUniqueId();
        } else {
            LOG.error("DocumentId doesn't exist in the received RetrieveDocumentSetRequestType message");
        }
        return repositoryUniqueId;
    }

    private String getHomeCommunityIdFromRequest(RetrieveDocumentSetRequestType request) {
        String homeCommunityId = null;

        if (request != null
            && request.getDocumentRequest() != null
            && request.getDocumentRequest().get(0) != null
            && request.getDocumentRequest().get(0).getHomeCommunityId() != null) {

            homeCommunityId = request.getDocumentRequest().get(0).getHomeCommunityId();

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
    private String getRepositoryUniqueIdFromResponse(RetrieveDocumentSetResponseType response) {
        String repositoryUniqueId = null;

        if (response != null
            && response.getDocumentResponse() != null
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
    private String getHomeCommunityIdFromResponse(RetrieveDocumentSetResponseType response) {
        String homeCommunityId = null;

        if (response != null
            && response.getDocumentResponse() != null
            && response.getDocumentResponse().get(0) != null
            && response.getDocumentResponse().get(0).getHomeCommunityId() != null) {
            homeCommunityId = response.getDocumentResponse().get(0).getHomeCommunityId();
        } else {
            LOG.error("HomeCommunityId doesn't exist in RetrieveDocumentSetResponseType message");
        }
        return homeCommunityId;
    }

    private String getDocumentUniqueIdFromResponse(RetrieveDocumentSetResponseType response) {
        if (response != null
            && response.getDocumentResponse() != null
            && response.getDocumentResponse().get(0) != null
            && response.getDocumentResponse().get(0).getDocumentUniqueId() != null) {
            return response.getDocumentResponse().get(0).getDocumentUniqueId();
        } else {
            return null;
        }
    }

    private AuditMessageType createPatientParticipantObjectIdentification(AuditMessageType auditMsg, String uniquePatientId) {

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

    private AuditMessageType getDocumentParticipantObjectIdentificationForRequest(RetrieveDocumentSetRequestType request,
        AuditMessageType auditMsg) throws JAXBException {

        ParticipantObjectIdentificationType participantObject = buildBaseParticipantObjectIdentificationType();
        participantObject.setParticipantObjectID(getDocumentUniqueIdFromRequest(request));

        getParticipantObjectDetailForRequest(request, participantObject);

        auditMsg.getParticipantObjectIdentification().add(participantObject);

        return auditMsg;
    }

    private ParticipantObjectIdentificationType getParticipantObjectDetailForRequest(RetrieveDocumentSetRequestType request,
        ParticipantObjectIdentificationType participantObject) {
        TypeValuePairType valueRepositoryUniqueId = new TypeValuePairType();
        valueRepositoryUniqueId.setType(DocRetrieveAuditTransformsConstants.PARTICIPANT_OBJECT_DETAIL_REPOSITORY_UNIQUE_TYPE);
        valueRepositoryUniqueId.setValue((getRepositoryUniqueIdFromRequest(request)).getBytes());
        participantObject.getParticipantObjectDetail().add(valueRepositoryUniqueId);

        TypeValuePairType valueHomeCommunityId = new TypeValuePairType();
        valueHomeCommunityId.setType(DocRetrieveAuditTransformsConstants.PARTICIPANT_OBJECT_DETAIL_HOME_COMMUNITY_ID_TYPE);
        valueHomeCommunityId.setValue((getHomeCommunityIdFromRequest(request)).getBytes());
        participantObject.getParticipantObjectDetail().add(valueHomeCommunityId);
        return participantObject;
    }

    private ParticipantObjectIdentificationType getParticipantObjectDetailForResponse(RetrieveDocumentSetResponseType response,
        ParticipantObjectIdentificationType participantObject) {
        TypeValuePairType valueRepositoryUniqueId = new TypeValuePairType();

        valueRepositoryUniqueId.setType(DocRetrieveAuditTransformsConstants.PARTICIPANT_OBJECT_DETAIL_REPOSITORY_UNIQUE_TYPE);
        valueRepositoryUniqueId.setValue((getRepositoryUniqueIdFromResponse(response)).getBytes());
        participantObject.getParticipantObjectDetail().add(valueRepositoryUniqueId);

        TypeValuePairType valueHomeCommunityId = new TypeValuePairType();
        valueHomeCommunityId.setType(DocRetrieveAuditTransformsConstants.PARTICIPANT_OBJECT_DETAIL_HOME_COMMUNITY_ID_TYPE);
        valueHomeCommunityId.setValue((getHomeCommunityIdFromResponse(response)).getBytes());
        participantObject.getParticipantObjectDetail().add(valueHomeCommunityId);
        return participantObject;
    }

    //TODO
    private AuditMessageType getDocumentParticipantObjectIdentificationForResponse(RetrieveDocumentSetResponseType response,
        AuditMessageType auditMsg) throws JAXBException {

        ParticipantObjectIdentificationType participantObject = buildBaseParticipantObjectIdentificationType();
        participantObject.setParticipantObjectID(getDocumentUniqueIdFromResponse(response));

        getParticipantObjectDetailForResponse(response, participantObject);

        auditMsg.getParticipantObjectIdentification().add(participantObject);
        return auditMsg;
    }

    private ParticipantObjectIdentificationType buildBaseParticipantObjectIdentificationType() {
        ParticipantObjectIdentificationType participantObject = createParticipantObjectIdentification(
            DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_TYPE_CODE,
            DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_TYPE_CODE_ROLE,
            DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_ID_TYPE_CODE,
            DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_ID_TYPE_CODE_SYSTEM,
            DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_ID_TYPE_DISPLAY_NAME);
        participantObject.setParticipantObjectName(HomeCommunityMap.formatHomeCommunityId(
            HomeCommunityMap.getLocalHomeCommunityId()));

        return participantObject;
    }

    private Marshaller getMarshaller() throws JAXBException {
        return new JAXBContextHandler().getJAXBContext(JAXB_HL7_CONTEXT_NAME).createMarshaller();
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

}
