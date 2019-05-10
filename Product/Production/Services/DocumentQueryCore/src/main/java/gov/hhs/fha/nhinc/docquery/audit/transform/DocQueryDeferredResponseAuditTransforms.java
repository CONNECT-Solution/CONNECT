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
package gov.hhs.fha.nhinc.docquery.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docquery.audit.DocQueryAuditTransformsConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.CoreHelpUtils;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ttang
 *
 */
public class DocQueryDeferredResponseAuditTransforms extends AuditTransforms<AdhocQueryResponse, RegistryResponseType> {

    private static final Logger LOG = LoggerFactory.getLogger(DocQueryAuditTransforms.class);

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForRequest(AdhocQueryResponse request,
        AssertionType assertion, AuditMessageType auditMsg) {
        // ParticipantObjectIdentification for Patient is an optional element and can range from 0..1 . If PatientId is
        // not present in the request, the Audit Object will not hold ParticipantObjectIdentification for Patient.
        auditMsg = getPatientParticipantObjectIdentificationForRequest(request, auditMsg);
        return auditMsg;
    }

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForResponse(AdhocQueryResponse request,
        RegistryResponseType response, AssertionType assertion, AuditMessageType auditMsg) {
        auditMsg = getPatientParticipantObjectIdentificationForResponse(request, auditMsg);
        return auditMsg;
    }

    @Override
    protected String getServiceEventIdCodeRequestor() {
        return DocQueryAuditTransformsConstants.EVENT_ID_CODE;
    }

    @Override
    protected String getServiceEventIdCodeResponder() {
        return DocQueryAuditTransformsConstants.EVENT_ID_CODE;
    }

    @Override
    protected String getServiceEventCodeSystem() {
        return DocQueryAuditTransformsConstants.EVENT_CODE_SYSTEM;
    }

    @Override
    protected String getServiceEventDisplayRequestor() {
        return DocQueryAuditTransformsConstants.EVENT_CODE_DISPLAY_REQUESTOR;
    }

    @Override
    protected String getServiceEventDisplayResponder() {
        return DocQueryAuditTransformsConstants.EVENT_CODE_DISPLAY_RESPONDER;
    }

    @Override
    protected String getServiceEventTypeCode() {
        return DocQueryAuditTransformsConstants.EVENT_TYPE_CODE;
    }

    @Override
    protected String getServiceEventTypeCodeSystem() {
        return DocQueryAuditTransformsConstants.EVENT_TYPE_CODE_SYSTEM;
    }

    @Override
    protected String getServiceEventTypeCodeDisplayName() {
        return DocQueryAuditTransformsConstants.EVENT_TYPE_CODE_DISPLAY_NAME;
    }

    @Override
    protected String getServiceEventActionCodeRequestor() {
        return DocQueryAuditTransformsConstants.EVENT_ACTION_CODE_REQUESTOR;
    }

    @Override
    protected String getServiceEventActionCodeResponder() {
        return DocQueryAuditTransformsConstants.EVENT_ACTION_CODE_RESPONDER;
    }

    // ParticipantObjectIdentification for Patient is an optional element and can range from 0..1 . If PatientId is
    // not present in the request, the Audit Object will not hold ParticipantObjectIdentification for Patient.
    private AuditMessageType getPatientParticipantObjectIdentificationForRequest(AdhocQueryResponse request,
        AuditMessageType auditMsg) {
        String patientId = getPatientIdFromRequest(request);
        if (NullChecker.isNotNullish(patientId)) {
            ParticipantObjectIdentificationType participantObj = createPatientParticipantObjectIdentification(
                patientId);
            auditMsg.getParticipantObjectIdentification().add(participantObj);
        }
        return auditMsg;
    }

    private AuditMessageType getPatientParticipantObjectIdentificationForResponse(AdhocQueryResponse request,
        AuditMessageType auditMsg) {
        String patientId = getPatientIdFromRequest(request);
        if (NullChecker.isNotNullish(patientId)) {
            ParticipantObjectIdentificationType participantObj = createPatientParticipantObjectIdentification(
                patientId);
            auditMsg.getParticipantObjectIdentification().add(participantObj);
        }
        return auditMsg;
    }

    // This is same for both Request and Response in case of DQ
    private ParticipantObjectIdentificationType createPatientParticipantObjectIdentification(String pid) {

        ParticipantObjectIdentificationType participantObject = createParticipantObject(
            DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_SYSTEM,
            DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE,
            DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE,
            DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM,
            DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME);

        if (NullChecker.isNotNullish(pid)) {
            participantObject.setParticipantObjectID(pid);
        }
        return participantObject;
    }

    private ParticipantObjectIdentificationType createParticipantObject(short objTypeCodeSys, short objTypeCodeRole,
        String objIdTypeCode, String objIdTypeCodeSys, String objIdTypeDisplayName) {

        return createParticipantObjectIdentification(objTypeCodeSys, objTypeCodeRole, objIdTypeCode, objIdTypeCodeSys,
            objIdTypeDisplayName);
    }

    private static String getSourcePatientIdFrom(AdhocQueryResponse request) {
        List<String> values = CoreHelpUtils.findFirstSlotOfExtrinsic(request.getRegistryObjectList(),
            "sourcePatientId");
        return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
    }

    // AdhocQueryResponse->RegistryObjectList-->ExtrinsicObject--> ExternalIdentifier-> XDSDocumentEntry.PatientId
    private static String getPatientIdFromRequest(AdhocQueryResponse request) {
        String patientId = getSourcePatientIdFrom(request);
        if (null == patientId) {
            LOG.error("PatientId doesn't exist in the received AdhocQueryResponse message");
        }
        return patientId;
    }
}
