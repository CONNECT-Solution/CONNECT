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
package gov.hhs.fha.nhinc.docsubmission.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionAuditTransformsConstants;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.log4j.Logger;

/**
 * Document Submission audit transforms to support DS audit logging.
 *
 * @author tjafri
 */
public class DocSubmissionAuditTransforms extends
    AuditTransforms<ProvideAndRegisterDocumentSetRequestType, RegistryResponseType> {

    private static final Logger LOG = Logger.getLogger(DocSubmissionAuditTransforms.class);

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForRequest(
        ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, AuditMessageType auditMsg) {
        // PatientParticipantObjetIdentification  and SubmissionSetParticipantObjetIdentification is same for both
        //Request and Response in case of DS
        auditMsg = getPatientParticipantObjectIdentification(request, auditMsg);
        auditMsg = getSubmissionSetParticipantObjectIdentification(request, auditMsg);
        return auditMsg;
    }

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForResponse(
        ProvideAndRegisterDocumentSetRequestType request, RegistryResponseType response, AssertionType assertion,
        AuditMessageType auditMsg) {
        // PatientParticipantObjetIdentification  and SubmissionSetParticipantObjetIdentification is same for both
        //Request and Response in case of DS
        auditMsg = getPatientParticipantObjectIdentification(request, auditMsg);
        auditMsg = getSubmissionSetParticipantObjectIdentification(request, auditMsg);
        return auditMsg;
    }
    
        @Override
    protected String getServiceEventIdCodeRequestor() {
        return DocSubmissionAuditTransformsConstants.EVENT_ID_CODE_DS_SOURCE;
    }

    @Override
    protected String getServiceEventIdCodeResponder() {
        return DocSubmissionAuditTransformsConstants.EVENT_ID_CODE_DS_RECIPIENT;
    }

    @Override
    protected String getServiceEventCodeSystem() {
        return DocSubmissionAuditTransformsConstants.EVENT_ID_CODE_SYSTEM;
    }

    @Override
    protected String getServiceEventDisplayRequestor() {
        return DocSubmissionAuditTransformsConstants.EVENT_ID_DISPLAY_SOURCE;
    }

    @Override
    protected String getServiceEventDisplayResponder() {
        return DocSubmissionAuditTransformsConstants.EVENT_ID_DISPLAY_RECIPIENT;
    }

    @Override
    protected String getServiceEventTypeCode() {
        return DocSubmissionAuditTransformsConstants.EVENT_TYPE_CODE;
    }

    @Override
    protected String getServiceEventTypeCodeSystem() {
        return DocSubmissionAuditTransformsConstants.EVENT_TYPE_CODE_SYSTEM;
    }

    @Override
    protected String getServiceEventTypeCodeDisplayName() {
        return DocSubmissionAuditTransformsConstants.EVENT_TYPE_CODE_DISPLAY_NAME;
    }

    @Override
    protected String getServiceEventActionCodeRequestor() {
        return DocSubmissionAuditTransformsConstants.EVENT_ACTION_CODE_SOURCE;
    }

    @Override
    protected String getServiceEventActionCodeResponder() {
        return DocSubmissionAuditTransformsConstants.EVENT_ACTION_CODE_RECIPIENT;
    }

    // PatientParticipantObjetIdentification is same for both Request and Response in case of DS
    private AuditMessageType getPatientParticipantObjectIdentification(
        ProvideAndRegisterDocumentSetRequestType request, AuditMessageType auditMsg) {
        auditMsg.getParticipantObjectIdentification().add(createPatientParticipantObjectIdentification(
            getIdValue(request, DocSubmissionAuditTransformsConstants.XDS_SUBMISSIONSET_PATIENT_ID)));
        return auditMsg;
    }

    // SubmissionSetParticipantObjetIdentification is same for both Request and Response in case of DS
    private AuditMessageType getSubmissionSetParticipantObjectIdentification(
        ProvideAndRegisterDocumentSetRequestType request, AuditMessageType auditMsg) {
        auditMsg.getParticipantObjectIdentification().add(createSubmissionSetParticipantObjectIdentification(
            getIdValue(request, DocSubmissionAuditTransformsConstants.XDS_SUBMISSIONSET_UNIQUE_ID)));
        return auditMsg;
    }

    private ParticipantObjectIdentificationType createPatientParticipantObjectIdentification(String pid) {

        ParticipantObjectIdentificationType participantObject = createParticipantObject(
            DocSubmissionAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_SYSTEM,
            DocSubmissionAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE,
            DocSubmissionAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE,
            DocSubmissionAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM,
            DocSubmissionAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME);

        if (pid != null && !pid.isEmpty()) {
            participantObject.setParticipantObjectID(pid);
        }
        return participantObject;
    }

    private ParticipantObjectIdentificationType createSubmissionSetParticipantObjectIdentification(String submissionId) {

        ParticipantObjectIdentificationType participantObject = createParticipantObject(
            DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_TYPE_CODE_SYSTEM,
            DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_TYPE_CODE_ROLE,
            DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_ID_TYPE_CODE,
            DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_ID_TYPE_CODE_SYSTEM,
            DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_ID_TYPE_DISPLAY_NAME);

        if (submissionId != null && !submissionId.isEmpty()) {
            participantObject.setParticipantObjectID(submissionId);
        }
        return participantObject;
    }

    private ParticipantObjectIdentificationType createParticipantObject(short objTypeCodeSys, short objTypeCodeRole,
        String objIdTypeCode, String objIdTypeCodeSys, String objIdTypeDisplayName) {

        return createParticipantObjectIdentification(objTypeCodeSys, objTypeCodeRole,
            objIdTypeCode, objIdTypeCodeSys, objIdTypeDisplayName);
    }

    private String getIdValue(ProvideAndRegisterDocumentSetRequestType request, String idType) {
        String idValue = null;
        RegistryObjectType registryObj = extractRegistryObject(
            request.getSubmitObjectsRequest().getRegistryObjectList());
        if (registryObj != null && registryObj.getExternalIdentifier() != null
            && registryObj.getExternalIdentifier().size() > 0) {
            idValue = getIdFromExternalIdentifiers(registryObj.getExternalIdentifier(), idType);
        }
        return idValue;
    }

    private String getIdFromExternalIdentifiers(List<ExternalIdentifierType> externalIdentifiers, String type) {
        String patientId = null;
        for (ExternalIdentifierType identifier : externalIdentifiers) {
            if (identifier.getName() != null
                && identifier.getName().getLocalizedString() != null
                && identifier.getName().getLocalizedString().size() > 0
                && identifier.getName().getLocalizedString().get(0) != null
                && identifier.getName().getLocalizedString().get(0).getValue()
                .equals(type)) {
                patientId = identifier.getValue();
                break;
            }

        }
        return patientId;
    }

    private RegistryObjectType extractRegistryObject(RegistryObjectListType registryList) {
        RegistryObjectType registryObj = null;
        if (registryList != null && registryList.getIdentifiable() != null
            && registryList.getIdentifiable().size() > 0) {
            List<JAXBElement<? extends IdentifiableType>> identifiers = registryList.getIdentifiable();
            for (JAXBElement<? extends IdentifiableType> object : identifiers) {
                if (object.getDeclaredType() != null && object.getDeclaredType().equals(RegistryPackageType.class)) {
                    registryObj = (RegistryObjectType) object.getValue();
                    break;
                }
            }
        } else {
            LOG.error("RegistryPackage is null.");
        }
        return registryObj;
    }
}
