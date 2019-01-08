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
package gov.hhs.fha.nhinc.transform.subdisc;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.ActRelationshipMitigates;
import org.hl7.v3.BinaryDataEncoding;
import org.hl7.v3.CD;
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.COCTMT150003UV03Organization;
import org.hl7.v3.CS;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.II;
import org.hl7.v3.INT;
import org.hl7.v3.MCAIMT900001UV01DetectedIssueEvent;
import org.hl7.v3.MCAIMT900001UV01DetectedIssueManagement;
import org.hl7.v3.MCAIMT900001UV01SourceOf;
import org.hl7.v3.MCCIMT000300UV01Acknowledgement;
import org.hl7.v3.MFMIMT700711UV01Custodian;
import org.hl7.v3.MFMIMT700711UV01QueryAck;
import org.hl7.v3.MFMIMT700711UV01Reason;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201310UV02QueryMatchObservation;
import org.hl7.v3.PRPAMT201310UV02Subject;
import org.hl7.v3.ParticipationTargetSubject;
import org.hl7.v3.TSExplicit;
import org.hl7.v3.XActMoodDefEvn;
import org.hl7.v3.XActMoodIntentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class HL7PRPA201306Transforms {

    private static final Logger LOG = LoggerFactory.getLogger(HL7PRPA201306Transforms.class);

    private static HL7MessageIdGenerator idGenerator = new HL7MessageIdGenerator();

    public static PRPAIN201306UV02 createPRPA201306(PRPAMT201301UV02Patient patient, String senderOID,
        String receiverAAID, String receiverOID, String localDeviceId, PRPAIN201305UV02 query) {
        PRPAIN201306UV02 result = new PRPAIN201306UV02();

        LOG.trace("Create the 201306 message header fields");
        result.setITSVersion(HL7Constants.ITS_VERSION);
        result.setId(idGenerator.generateHL7MessageId(receiverOID));
        result.setCreationTime(HL7DataTransformHelper.creationTimeFactory());
        result.setInteractionId(
            HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "PRPA_IN201306UV02"));
        result.setProcessingCode(HL7DataTransformHelper.CSFactory("P"));
        result.setProcessingModeCode(HL7DataTransformHelper.CSFactory("R"));
        result.setAcceptAckCode(HL7DataTransformHelper.CSFactory("AL"));

        result.getAcknowledgement().add(createAck(query));

        // Create the Receiver
        LOG.trace("Create the Receiver");
        result.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000300UV01Receiver(receiverOID));

        // Create the Sender
        LOG.trace("Create the Sender");
        result.setSender(HL7SenderTransforms.createMCCIMT000300UV01Sender(senderOID));

        LOG.trace("Create the ControlActProcess");
        result.setControlActProcess(
            createQUQIMT021001UV01ControlActProcess(patient, localDeviceId, query, receiverAAID, receiverOID));

        return result;

    }

    /**
     * This method creates/transforms a patient discovery request (PRPAIN201305UV02) into a patient discovery response
     * (PRPAIN201306UV02) when a patient is not found.
     *
     * @param oRequest The patient discovery request to which no patients were found
     * @return Returns a patient discovery response for a no patient found scenario
     */
    public PRPAIN201306UV02 createPRPA201306ForPatientNotFound(PRPAIN201305UV02 oRequest) {

        LOG.trace("*** Entering createPRPA201306ForPatientNotFound() method ***");
        if (oRequest == null) {
            LOG.error("The incomming patient discovery request, PRPAIN201305UV02, message was null.");
            return null;
        } // else continue

        boolean bRequiredFieldsAreNull = areIncommingRequiredPRPAIN201305FieldsNull(oRequest);
        if (bRequiredFieldsAreNull) {
            LOG.error("One or more required fields from the patient discovery request "
                + "for the patient not found scenario are null.");
            return null;
        }

        PRPAIN201306UV02 result = new PRPAIN201306UV02();

        LOG.trace("Create the 201306 message header fields");
        result.setITSVersion(HL7Constants.ITS_VERSION);
        // extract the receiverOID from the request message - it will become the sender
        // extract the senderOID from the request - it will become the receiver
        result.setId(getHL7MessageId(getSenderOIDFromPRPAIN201305UV02Request(oRequest)));
        result.setCreationTime(getHL7CreationTime());
        result.setInteractionId(getHL7InteractionId());
        result.setProcessingCode(getHL7ProcessingCode());
        result.setProcessingModeCode(getHL7ProcessingModeCode());
        result.setAcceptAckCode(HL7DataTransformHelper.CSFactory("AL")); // TODO verify this is correct 'AL' ?

        // from spec - case 4, IHE_ITI_TF_Supplement_XCPD_PC _2009-08-10.pdf.
        // AA (application accept) is returned in Acknowledgement.typeCode (transmission wrapper).
        result.getAcknowledgement().add(createAck(oRequest));

        // Create the Receiver
        LOG.trace("Create the Receiver");
        result.getReceiver().add(HL7ReceiverTransforms
            .createMCCIMT000300UV01Receiver(getSenderOIDFromPRPAIN201305UV02Request(oRequest)));

        // Create the Sender
        LOG.trace("Create the Sender");
        result.setSender(
            HL7SenderTransforms.createMCCIMT000300UV01Sender(getReceiverOIDFromPRPAIN201305UV02Request(oRequest)));

        // from spec - case 4
        // OK (data found, no errors) is returned in QueryAck.queryResponseCode (control act wrapper)
        // There is no RegistrationEvent returned in the response
        LOG.trace("Create the ControlActProcess");
        result.setControlActProcess(createQUQIMT021001UV01ControlActProcessWithNoRegistrationEvent(oRequest));

        LOG.trace("*** Exiting createPRPA201306ForPatientNotFound() method ***");
        return result;
    }

    /**
     * This method creates/transforms a patient discovery request (PRPAIN201305UV02) into a patient discovery response
     * (PRPAIN201306UV02) when a patient is not found.
     *
     * @param oRequest The patient discovery request to which no patients were found
     * @return Returns a patient discovery response for a no patient found scenario
     */
    public PRPAIN201306UV02 createPRPA201306ForErrors(PRPAIN201305UV02 oRequest, String sErrorCode) {

        LOG.trace("*** Entering createPRPA201306ForErrors() method ***");
        if (oRequest == null) {
            LOG.error("The incomming patient discovery request, PRPAIN201305UV02, message was null.");
            return null;
        } // else continue

        boolean bRequiredFieldsAreNull = areIncommingRequiredPRPAIN201305FieldsNull(oRequest);
        if (bRequiredFieldsAreNull) {
            LOG.error("One or more required fields from the patient discovery request are null.");
            return null;
        }

        if (NullChecker.isNullish(sErrorCode)) {
            LOG.error("The sErrorCode parameter was null.");
            return null;
        }

        PRPAIN201306UV02 result = new PRPAIN201306UV02();

        LOG.trace("Create the 201306 message header fields");
        result.setITSVersion(HL7Constants.ITS_VERSION);

        // extract the receiverOID from the request message - it will become the sender
        String senderOID = getReceiverOIDFromPRPAIN201305UV02Request(oRequest);

        // Update 3.1.1: use the current home community id as defined in gateway.properties
        // If message fails to leave local community then senderid and receiverid will match
        try {
            LOG.info("Attempting to retrieve property: " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY
                + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            String sHomeCommunityId = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            if (sHomeCommunityId != null) {
                LOG.info("Retrieving local home community id");
                // If the property is set, then use this instead of from sending request
                if (!sHomeCommunityId.isEmpty()) {
                    senderOID = sHomeCommunityId;
                }
            }
        } catch (PropertyAccessException ex) {
            LOG.error("Error: Failed to retrieve {} from property file {}: {}",
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY, NhincConstants.GATEWAY_PROPERTY_FILE,
                ex.getLocalizedMessage(), ex);
        }

        // extract the senderOID from the request - it will become the receiver
        result.setId(getHL7MessageId(getSenderOIDFromPRPAIN201305UV02Request(oRequest)));

        result.setCreationTime(getHL7CreationTime());
        result.setInteractionId(getHL7InteractionId());
        result.setProcessingCode(getHL7ProcessingCode());
        result.setProcessingModeCode(getHL7ProcessingModeCode());
        result.setAcceptAckCode(HL7DataTransformHelper.CSFactory("NE")); // TODO verify this is correct 'AL' ?

        // from spec - case 5, IHE_ITI_TF_Supplement_XCPD_PC _2009-08-10.pdf.
        // AA (application accept) is returned in Acknowledgement.typeCode (transmission wrapper).
        result.getAcknowledgement().add(createAck(oRequest));

        // Create the Receiver
        LOG.trace("Create the Receiver");
        result.getReceiver().add(HL7ReceiverTransforms
            .createMCCIMT000300UV01Receiver(getSenderOIDFromPRPAIN201305UV02Request(oRequest)));

        // Create the Sender
        LOG.trace("Create the Sender");
        result.setSender(HL7SenderTransforms.createMCCIMT000300UV01Sender(senderOID));

        // from spec - case 5
        // There is no RegistrationEvent returned in the response
        LOG.trace("Create the ControlActProcess");
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oControlActProcess = createQUQIMT021001UV01ControlActProcessWithNoRegistrationEvent(
            oRequest);
        // QE (application error) is returned in QueryAck.queryResponseCode (control act wrapper)
        oControlActProcess.getQueryAck().getQueryResponseCode().setCode(HL7Constants.QUERY_ACK_QE);

        // set the detectedIssueEvent
        oControlActProcess.getReasonOf().add(createErrorReason(sErrorCode));

        result.setControlActProcess(oControlActProcess);

        LOG.trace("*** Exiting createPRPA201306ForErrors() method ***");
        return result;
    }

    /**
     * This method creates/transforms a patient discovery request (PRPAIN201305UV02) into a patient discovery response
     * (PRPAIN201306UV02) when a patient is not found.
     *
     * @param oRequest The patient discovery request to which no patients were found
     * @param sErrorCode The Error code as defined by the IHE_ITI_TF_Supplement_XCPD_PC
     * @param sErrorText Human readable contextual string for more information to the error
     * @return Returns a patient discovery response for a no patient found scenario
     */
    public PRPAIN201306UV02 createPRPA201306ForErrors(PRPAIN201305UV02 oRequest, String sErrorCode, String sErrorText) {

        LOG.trace("*** Entering createPRPA201306ForErrors() method ***");
        PRPAIN201306UV02 result = createPRPA201306ForErrors(oRequest, sErrorCode);

        // set the detectedIssueEvent
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oControlActProcess = result.getControlActProcess();
        MFMIMT700711UV01Reason oReason = oControlActProcess.getReasonOf().get(0);
        if (oReason != null) {
            MCAIMT900001UV01DetectedIssueEvent oDetectedIssueEvent = oReason.getDetectedIssueEvent();
            if (oDetectedIssueEvent != null) {
                EDExplicit ed = new EDExplicit();
                ed.getContent().add(sErrorText);
                oDetectedIssueEvent.setText(ed);
            }
        }

        LOG.trace("*** Exiting createPRPA201306ForErrors() method ***");
        return result;
    }

    public static PRPAIN201306UV02MFMIMT700711UV01ControlActProcess createQUQIMT021001UV01ControlActProcess(
        PRPAMT201301UV02Patient patient, String localDeviceId, PRPAIN201305UV02 query, String aaId, String orgId) {
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setClassCode(ActClassControlAct.CACT);
        controlActProcess
        .setCode(HL7DataTransformHelper.CDFactory("PRPA_TE201306UV02", HL7Constants.INTERACTION_ID_ROOT));

        if (patient != null && NullChecker.isNotNullish(patient.getId()) && patient.getId().get(0) != null
            && NullChecker.isNotNullish(patient.getId().get(0).getExtension())
            && NullChecker.isNotNullish(patient.getId().get(0).getRoot())) {
            LOG.trace("Add the Subject");
            controlActProcess.getSubject().add(createSubject(patient, query, patient.getId().get(0).getExtension(),
                patient.getId().get(0).getRoot(), orgId));
        }

        LOG.trace("Add the Query Ack");
        controlActProcess.setQueryAck(createQueryAck(query));

        // Add in query parameters
        if (query.getControlActProcess() != null && query.getControlActProcess().getQueryByParameter() != null
            && query.getControlActProcess().getQueryByParameter().getValue() != null) {
            LOG.trace("Add Query By Parameter");
            controlActProcess.setQueryByParameter(query.getControlActProcess().getQueryByParameter());
        }

        return controlActProcess;
    }

    public static PRPAIN201306UV02MFMIMT700711UV01ControlActProcess createQUQIMT021001UV01ControlActProcessWithNoRegistrationEvent(
        PRPAIN201305UV02 query) {
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);

        controlActProcess
        .setCode(HL7DataTransformHelper.CDFactory("PRPA_TE201306UV02", HL7Constants.INTERACTION_ID_ROOT));
        LOG.trace("Add the Subject");
        controlActProcess.getSubject().add(createSubjectWithNoRegistrationEvent());
        LOG.trace("Add the Query Ack");
        controlActProcess.setQueryAck(createQueryAck(query));
        // Add in query parameters
        if (query.getControlActProcess() != null && query.getControlActProcess().getQueryByParameter() != null
            && query.getControlActProcess().getQueryByParameter().getValue() != null) {
            LOG.trace("Add Query By Parameter");
            controlActProcess.setQueryByParameter(query.getControlActProcess().getQueryByParameter());
        }

        return controlActProcess;
    }

    public static MFMIMT700711UV01QueryAck createQueryAck(PRPAIN201305UV02 query) {
        MFMIMT700711UV01QueryAck result = new MFMIMT700711UV01QueryAck();
        LOG.trace("Begin CreateQueryAck");

        if (query.getControlActProcess() != null && query.getControlActProcess().getQueryByParameter() != null
            && query.getControlActProcess().getQueryByParameter().getValue() != null
            && query.getControlActProcess().getQueryByParameter().getValue().getQueryId() != null) {
            result.setQueryId(query.getControlActProcess().getQueryByParameter().getValue().getQueryId());
        } else {
            LOG.trace("No QueryByParameters");
        }

        CS respCode = new CS();
        respCode.setCode(HL7Constants.QUERY_ACK_OK);
        result.setQueryResponseCode(respCode);

        INT totalQuanity = new INT();
        totalQuanity.setValue(BigInteger.valueOf(1));
        result.setResultTotalQuantity(totalQuanity);

        INT currQuanity = new INT();
        currQuanity.setValue(BigInteger.valueOf(1));
        result.setResultCurrentQuantity(currQuanity);

        INT remainQuanity = new INT();
        remainQuanity.setValue(BigInteger.valueOf(0));
        result.setResultRemainingQuantity(remainQuanity);

        LOG.trace("end CreateQueryAck");
        return result;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01Subject1 createSubject(PRPAMT201301UV02Patient patient,
        PRPAIN201305UV02 query, String patientId, String aaId, String orgId) {
        PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject1();

        subject.getTypeCode().add("SUBJ");

        subject.setRegistrationEvent(createRegEvent(patient, query, patientId, aaId, orgId));

        return subject;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01Subject1 createSubjectWithNoRegistrationEvent() {
        PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject1();

        subject.getTypeCode().add("SUBJ");

        return subject;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent createRegEvent(PRPAMT201301UV02Patient patient,
        PRPAIN201305UV02 query, String patientId, String aaID, String orgId) {
        PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent regEvent = new PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent();
        regEvent.getMoodCode().add(HL7Constants.DETECTED_ISSUE_MOODCODE_EVN);
        regEvent.getClassCode().add("REG");
        II id = new II();
        id.getNullFlavor().add("NA");
        regEvent.getId().add(id);

        CS statusCode = new CS();
        statusCode.setCode("active");

        regEvent.setStatusCode(statusCode);

        LOG.trace("Setting Subject1");
        regEvent.setSubject1(createSubject2(patient, query, patientId, aaID));

        regEvent.setCustodian(createCustodian(orgId));

        return regEvent;
    }

    public static PRPAIN201306UV02MFMIMT700711UV01Subject2 createSubject2(PRPAMT201301UV02Patient patient,
        PRPAIN201305UV02 query, String patId, String orgId) {
        PRPAIN201306UV02MFMIMT700711UV01Subject2 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject2();
        subject.setTypeCode(ParticipationTargetSubject.SBJ);
        LOG.debug("patientID = " + patId);
        PRPAMT201310UV02Patient pat310 = HL7PatientTransforms.create201310Patient(patient, patId, orgId);

        pat310.setProviderOrganization(createProviderOrg(patient));

        // Add in query match observation
        pat310.getSubjectOf1().add(createSubjectOf1());

        // Add in patient
        LOG.trace("set patient");
        subject.setPatient(pat310);

        return subject;
    }

    private static MCCIMT000300UV01Acknowledgement createAck(PRPAIN201305UV02 query) {
        MCCIMT000300UV01Acknowledgement ack = new MCCIMT000300UV01Acknowledgement();
        ack.setTypeId(query.getInteractionId());

        CS typeCode = new CS();
        typeCode.setCode("AA");

        ack.setTypeCode(typeCode);

        return ack;
    }

    private static JAXBElement<COCTMT150003UV03Organization> createProviderOrg(PRPAMT201301UV02Patient patient) {
        COCTMT150003UV03Organization org = new COCTMT150003UV03Organization();
        org.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);
        org.setClassCode(HL7Constants.ORG_CLASS_CODE);
        II id = new II();

        if (CollectionUtils.isNotEmpty(patient.getId())
            && StringUtils.isNotEmpty(patient.getId().get(0).getRoot())) {
            id.setRoot(patient.getId().get(0).getRoot());
        }
        org.getId().add(id);

        org.getContactParty().add(null);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "providerOrganization");

        return new JAXBElement<>(xmlqname, COCTMT150003UV03Organization.class, org);

    }

    private static PRPAMT201310UV02Subject createSubjectOf1() {
        PRPAMT201310UV02Subject result = new PRPAMT201310UV02Subject();

        result.setQueryMatchObservation(createQueryMatch());
        return result;
    }

    private static PRPAMT201310UV02QueryMatchObservation createQueryMatch() {
        PRPAMT201310UV02QueryMatchObservation queryMatch = new PRPAMT201310UV02QueryMatchObservation();
        queryMatch.getMoodCode().add(HL7Constants.DETECTED_ISSUE_MOODCODE_EVN);
        queryMatch.getClassCode().add("CASE");
        CD code = new CD();
        code.setCode("IHE_PDQ");
        queryMatch.setCode(code);

        INT intValue = new INT();
        intValue.setValue(BigInteger.valueOf(100));
        queryMatch.setValue(intValue);

        return queryMatch;
    }

    private static MFMIMT700711UV01Custodian createCustodian(String oid) {
        MFMIMT700711UV01Custodian result = new MFMIMT700711UV01Custodian();
        result.getTypeCode().add("CST");
        result.setAssignedEntity(createAssignEntity(oid));

        return result;
    }

    private static COCTMT090003UV01AssignedEntity createAssignEntity(String oid) {
        COCTMT090003UV01AssignedEntity assignedEntity = new COCTMT090003UV01AssignedEntity();
        assignedEntity.setClassCode(HL7Constants.ASSIGNED_DEVICE_CLASS_CODE);
        II id = new II();
        id.setRoot(HomeCommunityMap.formatHomeCommunityId(oid));
        assignedEntity.getId().add(id);

        return assignedEntity;
    }

    private MFMIMT700711UV01Reason createErrorReason(String sError) {
        MFMIMT700711UV01Reason oReason = new MFMIMT700711UV01Reason();
        MCAIMT900001UV01DetectedIssueEvent oDetectedIssueEvent = new MCAIMT900001UV01DetectedIssueEvent();
        // <detectedIssueEvent classCode="ALRT" moodCode="EVN">
        oDetectedIssueEvent.getClassCode().add(HL7Constants.DETECTED_ISSUE_CLASSCODE_ALRT);
        oDetectedIssueEvent.getMoodCode().add(HL7Constants.DETECTED_ISSUE_MOODCODE_EVN);

        // other errors may look a little differently than the one for responder busy
        if (NullChecker.isNotNullish(sError)) {
            // <code code="ActAdministrativeDetectedIssueCode" codeSystem="2.16.840.1.113883.5.4"/>
            CD oCode = new CD();
            oCode.setCode(HL7Constants.DETECTED_ISSUE_CODE_ADMINISTRATIVE);
            oCode.setCodeSystem(HL7Constants.DETECTED_ISSUE_CODESYSTEM_ERROR_CODE);
            oDetectedIssueEvent.setCode(oCode);

            // mitigatedBy is a sourceOf object...
            // <mitigatedBy typeCode="MITGT">
            MCAIMT900001UV01SourceOf oMCAIMT900001UV01SourceOf = new MCAIMT900001UV01SourceOf();
            oMCAIMT900001UV01SourceOf.setTypeCode(ActRelationshipMitigates.MITGT);
            MCAIMT900001UV01DetectedIssueManagement oDetectedIssueManagement = new MCAIMT900001UV01DetectedIssueManagement();

            // <DetectedIssueManagement classCode="ACT" moodCode="EVN">
            // <code code="ResponderBusy" codeSystem="1.3.6.1.4.1.19376.1.2.27.3"/>
            // </DetectedIssueManagement>
            CD oDetectedIssuesCode = new CD();

            // set the error code element to AnswerNotAvailable
            // per the 2010 2.1 xcpd spec (only other option is ResponderBusy)
            oDetectedIssuesCode.setCode(NhincConstants.PATIENT_DISCOVERY_ANSWER_NOT_AVAIL_ERR_CODE);

            oDetectedIssuesCode.setCodeSystem(HL7Constants.DETECTEDISSUEMANAGEMENT_CODESYSTEM);
            oDetectedIssueManagement.setCode(oDetectedIssuesCode);
            oDetectedIssueManagement.getClassCode().add(HL7Constants.DETECTEDISSUEMANAGEMENT_CLASSCODE);
            // verify this is correct, example from spec not consistant with this value. s/b RQO
            // paul....this incorrect example is in 2.1 spec (IHE_ITI_Suppl_XCPD_Rev2-1_TI_2010-08_10)
            // 2.3 spec (IHE_ITI_Suppl_XCPD_Rev2-3_TI_2011-08_19) fixed example uses mood code EVN, so this is OK
            oDetectedIssueManagement.setMoodCode(XActMoodDefEvn.EVN);

            // put the error detail on the DetectedIssueManagement text element
            EDExplicit ed = new EDExplicit();
            ed.setRepresentation(BinaryDataEncoding.TXT);
            ed.getContent().add(sError);
            oDetectedIssueManagement.setText(ed);

            oMCAIMT900001UV01SourceOf.setDetectedIssueManagement(oDetectedIssueManagement);
            oDetectedIssueEvent.getMitigatedBy().add(oMCAIMT900001UV01SourceOf);
        }

        oReason.setDetectedIssueEvent(oDetectedIssueEvent);

        return oReason;

    }

    protected TSExplicit getHL7CreationTime() {
        return HL7DataTransformHelper.creationTimeFactory();
    }

    protected II getHL7InteractionId() {
        return HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "PRPA_IN201306UV02");
    }

    protected II getHL7MessageId(String receiverOID) {
        return idGenerator.generateHL7MessageId(receiverOID);
    }

    protected CS getHL7ProcessingCode() {
        return HL7DataTransformHelper.CSFactory("P");
    }

    protected CS getHL7ProcessingModeCode() {
        return HL7DataTransformHelper.CSFactory("R");
    }

    protected String getReceiverOIDFromPRPAIN201305UV02Request(PRPAIN201305UV02 oRequest) {
        // extract the receiverOID from the request message - it will become the sender
        return HL7Extractors.ExtractHL7ReceiverOID(oRequest);
    }

    protected String getSenderOIDFromPRPAIN201305UV02Request(PRPAIN201305UV02 oRequest) {
        // extract the senderOID from the request - it will become the receiver
        return HL7Extractors.ExtractHL7SenderOID(oRequest);
    }

    protected boolean areIncommingRequiredPRPAIN201305FieldsNull(PRPAIN201305UV02 oRequest) {

        // check receiverOID
        boolean bReceiverFieldsNull = areReceiverFieldsNull(oRequest);
        if (bReceiverFieldsNull) {
            LOG.error("One or more request receiver fields are null or empty.");
            return true;
        }

        // check senderOID
        boolean bSenderFieldsNull = areSenderFieldsNull(oRequest);
        if (bSenderFieldsNull) {
            LOG.error("One or more request sender fields are null or empty.");
            return true;
        }

        // check controlActProcess fields
        boolean bControlActProcessFieldsNull = areControlActProcessFieldsNull(oRequest);
        if (bControlActProcessFieldsNull) {
            LOG.error("One or more ControlActProcess fields from the incomming request were null.");
            return true;
        }

        // check interactionId
        if (oRequest.getInteractionId() == null) {
            LOG.error("The InteractionId object from the incomming request message is null.");
            return true;
        }

        return false;
    }

    protected boolean areReceiverFieldsNull(PRPAIN201305UV02 oRequest) {
        if (NullChecker.isNullish(oRequest.getReceiver())) {
            LOG.error("The list of receiver objects from the incomming request " + "message were null or empty.");
            return true;
        }

        if (oRequest.getReceiver().get(0) == null) {
            LOG.error("The request's receiver object is null.");
            return true;
        }

        if (oRequest.getReceiver().get(0).getDevice() == null) {
            LOG.error("The request's receiver device object is null.");
            return true;
        }

        if (NullChecker.isNullish(oRequest.getReceiver().get(0).getDevice().getId())) {
            LOG.error("The list of device ids from the receiver object were null or empty.");
            return true;
        }

        if (oRequest.getReceiver().get(0).getDevice().getId().get(0) == null) {
            LOG.error("The device II id object is null.");
            return true;
        }

        if (NullChecker.isNullish(oRequest.getReceiver().get(0).getDevice().getId().get(0).getRoot())) {
            LOG.error("The device II id getRoot value is null.");
            return true;
        }
        // all fields are ok return false for null value check
        return false;
    }

    protected boolean areSenderFieldsNull(PRPAIN201305UV02 oRequest) {
        if (oRequest.getSender() == null) {
            LOG.error("The sender object from the incomming request message was null.");
            return true;
        }

        if (oRequest.getSender().getDevice() == null) {
            LOG.error("The request's sender device object is null.");
            return true;
        }

        if (NullChecker.isNullish(oRequest.getSender().getDevice().getId())) {
            LOG.error("The list of device ids from the sender object were null or empty.");
            return true;
        }

        if (oRequest.getSender().getDevice().getId().get(0) == null) {
            LOG.error("The sender device id (II) object is null.");
            return true;
        }

        if (NullChecker.isNullish(oRequest.getSender().getDevice().getId().get(0).getRoot())) {
            LOG.error("The device id value (i.e. II.getRoot() or "
                + "oRequest.getSender().getDevice().getId().get(0).getRoot()) is null.");
            return true;
        }
        // all fields are ok return false for null value check
        return false;
    }

    protected boolean areControlActProcessFieldsNull(PRPAIN201305UV02 oRequest) {
        if (oRequest.getControlActProcess() == null) {
            LOG.error("The controlActProcess object from the incomming request message is null.");
            return true;
        } else if (oRequest.getControlActProcess().getQueryByParameter() == null) {
            LOG.error("The JAXB queryByParameter object in the request's controlActProcess object is null.");
            return true;
        } else if (oRequest.getControlActProcess().getQueryByParameter().getValue() == null) {
            LOG.error("The queryByParameter object in the request's controlActProcess object is null.");
            return true;
        } else if (oRequest.getControlActProcess().getQueryByParameter().getValue().getQueryId() == null) {
            LOG.error("The queryId object in the request's controlActProcess object is null.");
            return true;
        } // else all is well, fields are not null; return false
        return false;
    }

}
