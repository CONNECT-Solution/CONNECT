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
package gov.hhs.fha.nhinc.docsubmission.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionAuditTransformsConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.ServiceUtils;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;

/**
 *
 * @author tjafri
 * @param <T>
 * @param <K>
 */
public abstract class AbstractDocSubmissionAuditTransforms<T, K> extends AuditTransforms<T, K> {

    // PatientParticipantObjectIdentification is same for both Request and Response in case of DS
    protected AuditMessageType getPatientParticipantObjectIdentification(
        ProvideAndRegisterDocumentSetRequestType request, AuditMessageType auditMsg) {
        auditMsg.getParticipantObjectIdentification().add(createPatientParticipantObjectIdentification(
            getIdValue(request, DocSubmissionAuditTransformsConstants.XDS_SUBMISSIONSET_PATIENT_ID)));
        return auditMsg;
    }

    // SubmissionSetParticipantObjectIdentification is same for both Request and Response in case of DS
    protected AuditMessageType getSubmissionSetParticipantObjectIdentification(
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

        if (NullChecker.isNotNullish(pid)) {
            participantObject.setParticipantObjectID(pid);
        }
        return participantObject;
    }

    private ParticipantObjectIdentificationType createSubmissionSetParticipantObjectIdentification(
        String submissionId) {

        ParticipantObjectIdentificationType participantObject = createParticipantObject(
            DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_TYPE_CODE_SYSTEM,
            DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_TYPE_CODE_ROLE,
            DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_ID_TYPE_CODE,
            DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_ID_TYPE_CODE_SYSTEM,
            DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_ID_TYPE_DISPLAY_NAME);

        if (NullChecker.isNotNullish(submissionId)) {
            participantObject.setParticipantObjectID(submissionId);
        }
        return participantObject;
    }

    private ParticipantObjectIdentificationType createParticipantObject(short objTypeCodeSys,
        short objTypeCodeRole,
        String objIdTypeCode, String objIdTypeCodeSys, String objIdTypeDisplayName) {

        return createParticipantObjectIdentification(objTypeCodeSys, objTypeCodeRole,
            objIdTypeCode, objIdTypeCodeSys, objIdTypeDisplayName);
    }

    private static String getIdValue(ProvideAndRegisterDocumentSetRequestType request, String idType) {
        String idValue = null;
        RegistryObjectType registryObj = extractRegistryObject(
            request.getSubmitObjectsRequest().getRegistryObjectList());
        if (registryObj != null && NullChecker.isNotNullish(registryObj.getExternalIdentifier())) {
            idValue = getIdFromExternalIdentifiers(registryObj.getExternalIdentifier(), idType);
        }
        return idValue;
    }

    private static String getIdFromExternalIdentifiers(List<ExternalIdentifierType> externalIdentifiers, String type) {
        String id = null;
        for (ExternalIdentifierType identifier : externalIdentifiers) {
            if (identifier.getName() != null
                && NullChecker.isNotNullish(identifier.getName().getLocalizedString())
                && identifier.getName().getLocalizedString().get(0) != null
                && identifier.getName().getLocalizedString().get(0).getValue().equals(type)) {
                id = identifier.getValue();
                break;
            }
        }
        return id;
    }

    private static RegistryObjectType extractRegistryObject(RegistryObjectListType registryList) {
        return ServiceUtils.extractRegistryObject(registryList);
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
}
