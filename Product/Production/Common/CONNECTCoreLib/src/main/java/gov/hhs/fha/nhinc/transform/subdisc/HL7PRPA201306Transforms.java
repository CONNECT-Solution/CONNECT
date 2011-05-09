/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import java.math.BigInteger;
//import javax.xml.datatype.XMLGregorianCalendar;
//import javax.xml.datatype.DatatypeFactory;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 *
 * @author dunnek
 */
public class HL7PRPA201306Transforms {

    private static Log log = LogFactory.getLog(HL7PRPA201306Transforms.class);
    private Log nonStaticLog = null;


    /**
     * default constructor
     */
    public HL7PRPA201306Transforms() {
        nonStaticLog = createLogger();
    }


    public static PRPAIN201306UV02 createPRPA201306(PRPAMT201301UV02Patient patient, String senderOID, String receiverAAID, String receiverOID, String localDeviceId, PRPAIN201305UV02 query) {
        PRPAIN201306UV02 result = new PRPAIN201306UV02();

        log.debug("Create the 201306 message header fields");
        result.setITSVersion(HL7Constants.ITS_VERSION);
        result.setId(HL7MessageIdGenerator.GenerateHL7MessageId(receiverOID));
        result.setCreationTime(HL7DataTransformHelper.CreationTimeFactory());
        result.setInteractionId(HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "PRPA_IN201306UV02"));
        result.setProcessingCode(HL7DataTransformHelper.CSFactory("P"));
        result.setProcessingModeCode(HL7DataTransformHelper.CSFactory("R"));
        result.setAcceptAckCode(HL7DataTransformHelper.CSFactory("AL"));

        result.getAcknowledgement().add(createAck(query));

        // Create the Receiver
        log.debug("Create the Receiver");
        result.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000300UV01Receiver(receiverOID));

        // Create the Sender
        log.debug("Create the Sender");
        result.setSender(HL7SenderTransforms.createMCCIMT000300UV01Sender(senderOID));

        log.debug("Create the ControlActProcess");
        result.setControlActProcess(createQUQIMT021001UV01ControlActProcess(patient, localDeviceId, query, receiverAAID, receiverOID));

        return result;



    }

    /**
     * This method creates/transforms a patient discovery request (PRPAIN201305UV02)
     * into a patient discovery response (PRPAIN201306UV02) when a patient is not
     * found.
     * @param oRequest The patient discovery request to which no patients were found
     * @return Returns a patient discovery response for a no patient found scenario
     */
    public PRPAIN201306UV02 createPRPA201306ForPatientNotFound(PRPAIN201305UV02 oRequest) {

        addLogDebug("*** Entering createPRPA201306ForPatientNotFound() method ***");
        if (oRequest == null)
        {
            addLogError("The incomming patient discovery request, PRPAIN201305UV02, message was null.");
            return null;
        } //else continue

        boolean bRequiredFieldsAreNull = areIncommingRequiredPRPAIN201305FieldsNull(oRequest);
        if (bRequiredFieldsAreNull)
        {
            addLogError("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
            return null;
        }

        PRPAIN201306UV02 result = new PRPAIN201306UV02();

        addLogDebug("Create the 201306 message header fields");
        result.setITSVersion(HL7Constants.ITS_VERSION);
        //extract the receiverOID from the request message - it will become the sender
        String sReceiverOIDFromMessage = getReceiverOIDFromPRPAIN201305UV02Request(oRequest);
        String senderOID = sReceiverOIDFromMessage;
        //extract the senderOID from the request - it will become the receiver
        String sSenderOIDFromMessage = getSenderOIDFromPRPAIN201305UV02Request(oRequest);
        String receiverOID = sSenderOIDFromMessage;
        result.setId(getHL7MessageId(receiverOID));
        result.setCreationTime(getHL7CreationTime());
        result.setInteractionId(getHL7InteractionId());
        result.setProcessingCode(getHL7ProcessingCode());
        result.setProcessingModeCode(getHL7ProcessingModeCode());
        result.setAcceptAckCode(HL7DataTransformHelper.CSFactory("AL")); //TODO verify this is correct 'AL' ?

        //from spec - case 4, IHE_ITI_TF_Supplement_XCPD_PC _2009-08-10.pdf.
        //AA (application accept) is returned in Acknowledgement.typeCode (transmission wrapper).
        result.getAcknowledgement().add(createAck(oRequest));

        // Create the Receiver
        addLogDebug("Create the Receiver");
        result.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000300UV01Receiver(receiverOID));

        // Create the Sender
        addLogDebug("Create the Sender");
        result.setSender(HL7SenderTransforms.createMCCIMT000300UV01Sender(senderOID));

        //from spec - case 4
        //OK (data found, no errors) is returned in QueryAck.queryResponseCode (control act wrapper)
        //There is no RegistrationEvent returned in the response
        addLogDebug("Create the ControlActProcess");
        result.setControlActProcess(createQUQIMT021001UV01ControlActProcessWithNoRegistrationEvent(oRequest));

        addLogDebug("*** Exiting createPRPA201306ForPatientNotFound() method ***");
        return result;
    }

    /**
     * This method creates/transforms a patient discovery request (PRPAIN201305UV02)
     * into a patient discovery response (PRPAIN201306UV02) when a patient is not
     * found.
     * @param oRequest The patient discovery request to which no patients were found
     * @return Returns a patient discovery response for a no patient found scenario
     */
    public PRPAIN201306UV02 createPRPA201306ForErrors(PRPAIN201305UV02 oRequest, String sErrorCode) {

        addLogDebug("*** Entering createPRPA201306ForErrors() method ***");
        if (oRequest == null)
        {
            addLogError("The incomming patient discovery request, PRPAIN201305UV02, message was null.");
            return null;
        } //else continue

        boolean bRequiredFieldsAreNull = areIncommingRequiredPRPAIN201305FieldsNull(oRequest);
        if (bRequiredFieldsAreNull)
        {
            addLogError("One or more required fields from the patient discovery request are null.");
            return null;
        }

        if (NullChecker.isNullish(sErrorCode))
        {
            addLogError("The sErrorCode parameter was null.");
            return null;
        }

        PRPAIN201306UV02 result = new PRPAIN201306UV02();

        addLogDebug("Create the 201306 message header fields");
        result.setITSVersion(HL7Constants.ITS_VERSION);
        //extract the receiverOID from the request message - it will become the sender
        String sReceiverOIDFromMessage = getReceiverOIDFromPRPAIN201305UV02Request(oRequest);
        String senderOID = sReceiverOIDFromMessage;
        //extract the senderOID from the request - it will become the receiver
        String sSenderOIDFromMessage = getSenderOIDFromPRPAIN201305UV02Request(oRequest);
        String receiverOID = sSenderOIDFromMessage;
        result.setId(getHL7MessageId(receiverOID));
        result.setCreationTime(getHL7CreationTime());
        result.setInteractionId(getHL7InteractionId());
        result.setProcessingCode(getHL7ProcessingCode());
        result.setProcessingModeCode(getHL7ProcessingModeCode());
        result.setAcceptAckCode(HL7DataTransformHelper.CSFactory("AL")); //TODO verify this is correct 'AL' ?

        //from spec - case 5, IHE_ITI_TF_Supplement_XCPD_PC _2009-08-10.pdf.
        //AA (application accept) is returned in Acknowledgement.typeCode (transmission wrapper).
        result.getAcknowledgement().add(createAck(oRequest));

        // Create the Receiver
        addLogDebug("Create the Receiver");
        result.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000300UV01Receiver(receiverOID));

        // Create the Sender
        addLogDebug("Create the Sender");
        result.setSender(HL7SenderTransforms.createMCCIMT000300UV01Sender(senderOID));

        //from spec - case 5
        //There is no RegistrationEvent returned in the response
        addLogDebug("Create the ControlActProcess");
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oControlActProcess =
                createQUQIMT021001UV01ControlActProcessWithNoRegistrationEvent(oRequest);
        //QE (application error) is returned in QueryAck.queryResponseCode (control act wrapper)
        oControlActProcess.getQueryAck().getQueryResponseCode().setCode(HL7Constants.QUERY_ACK_QE);

        //set the detectedIssueEvent
        oControlActProcess.getReasonOf().add(createErrorReason(sErrorCode));

        result.setControlActProcess(oControlActProcess);

        addLogDebug("*** Exiting createPRPA201306ForErrors() method ***");
        return result;
    }

    public static PRPAIN201306UV02MFMIMT700711UV01ControlActProcess createQUQIMT021001UV01ControlActProcess(PRPAMT201301UV02Patient patient, String localDeviceId, PRPAIN201305UV02 query, String aaId, String orgId) {
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setClassCode(ActClassControlAct.CACT);
        controlActProcess.setCode(HL7DataTransformHelper.CDFactory("PRPA_TE201306UV", HL7Constants.INTERACTION_ID_ROOT));

        if (patient != null &&
                NullChecker.isNotNullish(patient.getId()) &&
                patient.getId().get(0) != null &&
                NullChecker.isNotNullish(patient.getId().get(0).getExtension()) &&
                NullChecker.isNotNullish(patient.getId().get(0).getRoot())) {
            log.debug("Add the Subject");
           controlActProcess.getSubject().add(createSubject(patient, query, patient.getId().get(0).getExtension(), patient.getId().get(0).getRoot(), orgId));
        }

        log.debug("Add the Query Ack");
        controlActProcess.setQueryAck(createQueryAck(query));

        // Add in query parameters
        if (query.getControlActProcess() != null &&
                query.getControlActProcess().getQueryByParameter() != null &&
                query.getControlActProcess().getQueryByParameter().getValue() != null) {
            log.debug("Add Query By Parameter");
            controlActProcess.setQueryByParameter(query.getControlActProcess().getQueryByParameter());
        }

        return controlActProcess;
    }

    public static PRPAIN201306UV02MFMIMT700711UV01ControlActProcess createQUQIMT021001UV01ControlActProcessWithNoRegistrationEvent(PRPAIN201305UV02 query) {
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);

        controlActProcess.setCode(HL7DataTransformHelper.CDFactory("PRPA_TE201306UV", HL7Constants.INTERACTION_ID_ROOT));
        log.debug("Add the Subject");
        controlActProcess.getSubject().add(createSubjectWithNoRegistrationEvent());
        log.debug("Add the Query Ack");
        controlActProcess.setQueryAck(createQueryAck(query));
        // Add in query parameters
        if (query.getControlActProcess() != null &&
                query.getControlActProcess().getQueryByParameter() != null &&
                query.getControlActProcess().getQueryByParameter().getValue() != null) {
            log.debug("Add Query By Parameter");
            controlActProcess.setQueryByParameter(query.getControlActProcess().getQueryByParameter());
        }

        return controlActProcess;
    }

    public static MFMIMT700711UV01QueryAck createQueryAck(PRPAIN201305UV02 query) {
        MFMIMT700711UV01QueryAck result = new MFMIMT700711UV01QueryAck();
        log.debug("Begin CreateQueryAck");

        if (query.getControlActProcess() != null &&
                query.getControlActProcess().getQueryByParameter() != null &&
                query.getControlActProcess().getQueryByParameter().getValue() != null &&
                query.getControlActProcess().getQueryByParameter().getValue().getQueryId() != null) {
            result.setQueryId(query.getControlActProcess().getQueryByParameter().getValue().getQueryId());
        } else {
            log.debug("No QueryByParameters");
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

        log.debug("end CreateQueryAck");
        return result;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01Subject1 createSubject(PRPAMT201301UV02Patient patient, PRPAIN201305UV02 query, String patientId, String aaId, String orgId) {
        PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject1();

        subject.getTypeCode().add("SUBJ");

        subject.setRegistrationEvent(createRegEvent(patient, query, patientId, aaId, orgId));

        return subject;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01Subject1 createSubjectWithNoRegistrationEvent() {
        PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject1();

        subject.getTypeCode().add("SUBJ");

//        subject.setRegistrationEvent(createRegEvent(patient, query, patientId, aaId, orgId));

        return subject;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent createRegEvent(PRPAMT201301UV02Patient patient, PRPAIN201305UV02 query, String patientId, String aaID, String orgId) {
        PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent regEvent = new PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent();
        regEvent.getMoodCode().add(HL7Constants.DETECTED_ISSUE_MOODCODE_EVN);
        regEvent.getClassCode().add("REG");
        II id = new II();
        id.getNullFlavor().add("NA");
        regEvent.getId().add(id);

        CS statusCode = new CS();
        statusCode.setCode("active");

        regEvent.setStatusCode(statusCode);

        log.debug("Setting Subject1");
        regEvent.setSubject1(createSubject2(patient, query, patientId, aaID));


        regEvent.setCustodian(createCustodian(orgId));

        return regEvent;
    }

    public static PRPAIN201306UV02MFMIMT700711UV01Subject2 createSubject2(PRPAMT201301UV02Patient patient, PRPAIN201305UV02 query, String patId, String orgId) {
        PRPAIN201306UV02MFMIMT700711UV01Subject2 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject2();
        subject.setTypeCode(ParticipationTargetSubject.SBJ);
        log.debug("patientID = " + patId);
        PRPAMT201310UV02Patient pat310 = HL7PatientTransforms.create201310Patient(patient, patId, orgId);


        pat310.setProviderOrganization(createProviderOrg(patient));

        // Add in query match observation
        pat310.getSubjectOf1().add(createSubjectOf1());

        // Add in patient
        log.debug("set patient");
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


        if (patient.getId() != null &&
                patient.getId().size() > 0 &&
                patient.getId().get(0).getRoot() != null &&
                patient.getId().get(0).getRoot().length() > 0) {
            id.setRoot(patient.getId().get(0).getRoot());
        }
        org.getId().add(id);

        org.getContactParty().add(null);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "providerOrganization");
        JAXBElement<COCTMT150003UV03Organization> result = new JAXBElement<COCTMT150003UV03Organization>(xmlqname, COCTMT150003UV03Organization.class, org);

        return result;

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
        id.setRoot(oid);
        assignedEntity.getId().add(id);

        return assignedEntity;
    }

    private MFMIMT700711UV01Reason createErrorReason(String sErrorCode)
    {
        MFMIMT700711UV01Reason oReason = new MFMIMT700711UV01Reason();
        MCAIMT900001UV01DetectedIssueEvent oDetectedIssueEvent = new MCAIMT900001UV01DetectedIssueEvent();
        //<detectedIssueEvent classCode="ALRT" moodCode="EVN">
        oDetectedIssueEvent.getClassCode().add(HL7Constants.DETECTED_ISSUE_CLASSCODE_ALRT);
        oDetectedIssueEvent.getMoodCode().add(HL7Constants.DETECTED_ISSUE_MOODCODE_EVN);

        //other errors may look a little differently than the one for responder busy
        if (NullChecker.isNotNullish(sErrorCode))
        {
            //<code code="ActAdministrativeDetectedIssueCode" codeSystem="2.16.840.1.113883.5.4"/>
            CD oCode = new CD();
            oCode.setCode(HL7Constants.DETECTED_ISSUE_CODE_ADMINISTRATIVE);
            oCode.setCodeSystem(HL7Constants.DETECTED_ISSUE_CODESYSTEM_ERROR_CODE);
            oDetectedIssueEvent.setCode(oCode);

            // mitigatedBy is a sourceOf object...
            //<mitigatedBy typeCode="MITGT">
            MCAIMT900001UV01SourceOf oMCAIMT900001UV01SourceOf = new MCAIMT900001UV01SourceOf();
            oMCAIMT900001UV01SourceOf.setTypeCode(ActRelationshipMitigates.MITGT);
            MCAIMT900001UV01DetectedIssueManagement oDetectedIssueManagement = new MCAIMT900001UV01DetectedIssueManagement();

            //<DetectedIssueManagement classCode="ACT" moodCode="RQO">
            //  <code code="ResponderBusy" codeSystem="1.3.6.1.4.1.19376.1.2.27.3"/>
            //</DetectedIssueManagement>
            CD oDetectedIssuesCode = new CD();
            oDetectedIssuesCode.setCode(sErrorCode);
            oDetectedIssuesCode.setCodeSystem(HL7Constants.DETECTEDISSUEMANAGEMENT_CODESYSTEM);
            oDetectedIssueManagement.setCode(oDetectedIssuesCode);
            oDetectedIssueManagement.getClassCode().add(HL7Constants.DETECTEDISSUEMANAGEMENT_CLASSCODE);
            oDetectedIssueManagement.setMoodCode(XActMoodDefEvn.EVN); //TODO verify this is correct, exxample from spec not consistant with this value. s/b RQO

            oMCAIMT900001UV01SourceOf.setDetectedIssueManagement(oDetectedIssueManagement);
            oDetectedIssueEvent.getMitigatedBy().add(oMCAIMT900001UV01SourceOf);
        }

        oReason.setDetectedIssueEvent(oDetectedIssueEvent);

        return oReason;

    }



    /**
     * Instantiating log4j logger
     * @return
     */
    protected Log createLogger()
    {
        return ((nonStaticLog != null) ? nonStaticLog : LogFactory.getLog(getClass()));
    }

    protected TSExplicit getHL7CreationTime() {
        return HL7DataTransformHelper.CreationTimeFactory();
    }

    protected II getHL7InteractionId() {
        return HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "PRPA_IN201306UV02");
    }

    protected II getHL7MessageId(String receiverOID) {
        return HL7MessageIdGenerator.GenerateHL7MessageId(receiverOID);
    }

    protected CS getHL7ProcessingCode() {
        return HL7DataTransformHelper.CSFactory("P");
    }

    protected CS getHL7ProcessingModeCode() {
        return HL7DataTransformHelper.CSFactory("R");
    }

    protected String getReceiverOIDFromPRPAIN201305UV02Request(PRPAIN201305UV02 oRequest) {
        //extract the receiverOID from the request message - it will become the sender
        String sReceiverOIDFromMessage = HL7Extractors.ExtractHL7ReceiverOID(oRequest);
        return sReceiverOIDFromMessage;
    }

    protected String getSenderOIDFromPRPAIN201305UV02Request(PRPAIN201305UV02 oRequest) {
        //extract the senderOID from the request - it will become the receiver
        String sSenderOIDFromMessage = HL7Extractors.ExtractHL7SenderOID(oRequest);
        return sSenderOIDFromMessage;
    }

    /**
     * add an info statement/message to the log file
     * @param message
     */
    private void addLogInfo(String message){
        nonStaticLog.info(message);
    }

    /**
     * add a debug statement to the log file
     * @param message
     */
    private void addLogDebug(String message){
        nonStaticLog.debug(message);
    }

    /**
     * add an error message/statement to the log file
     * @param message
     */
    private void addLogError(String message){
        nonStaticLog.error(message);
    }

    protected boolean areIncommingRequiredPRPAIN201305FieldsNull(PRPAIN201305UV02 oRequest) {

        //check receiverOID
        boolean bReceiverFieldsNull = areReceiverFieldsNull(oRequest);
        if (bReceiverFieldsNull)
        {
            addLogError("One or more request receiver fields are null or empty.");
            return true;
        }

        //check senderOID
        boolean bSenderFieldsNull = areSenderFieldsNull(oRequest);
        if (bSenderFieldsNull)
        {
            addLogError("One or more request sender fields are null or empty.");
            return true;
        }

        //check controlActProcess fields
        boolean bControlActProcessFieldsNull = areControlActProcessFieldsNull(oRequest);
        if (bControlActProcessFieldsNull)
        {
            addLogError("One or more ControlActProcess fields from the incomming request were null.");
            return true;
        }

        //check interactionId
        if (oRequest.getInteractionId() == null)
        {
            addLogError("The InteractionId object from the incomming request message is null.");
            return true;
        }

        return false;
    }

    protected boolean areReceiverFieldsNull(PRPAIN201305UV02 oRequest)
    {
        if (NullChecker.isNullish(oRequest.getReceiver()))
        {
            addLogError("The list of receiver objects from the incomming request " +
                    "message were null or empty.");
            return true;
        }

        if (oRequest.getReceiver().get(0) == null)
        {
            addLogError("The request's receiver object is null.");
            return true;
        }

        if (oRequest.getReceiver().get(0).getDevice() == null)
        {
            addLogError("The request's receiver device object is null.");
            return true;
        }

        if (NullChecker.isNullish(oRequest.getReceiver().get(0).getDevice().getId()))
        {
             addLogError("The list of device ids from the receiver object were null or empty.");
            return true;
        }

        if (oRequest.getReceiver().get(0).getDevice().getId().get(0) == null)
        {
            addLogError("The device II id object is null.");
            return true;
        }

        if (NullChecker.isNullish(oRequest.getReceiver().get(0).getDevice().getId().get(0).getRoot()))
        {
            addLogError("The device II id getRoot value is null.");
            return true;
        }
        //all fields are ok return false for null value check
        return false;
    }

    protected boolean areSenderFieldsNull(PRPAIN201305UV02 oRequest)
    {
        if (oRequest.getSender() == null)
        {
            addLogError("The sender object from the incomming request message was null.");
            return true;
        }

        if (oRequest.getSender().getDevice() == null)
        {
            addLogError("The request's sender device object is null.");
            return true;
        }

        if (NullChecker.isNullish(oRequest.getSender().getDevice().getId()))
        {
             addLogError("The list of device ids from the sender object were null or empty.");
            return true;
        }

        if (oRequest.getSender().getDevice().getId().get(0) == null)
        {
            addLogError("The sender device id (II) object is null.");
            return true;
        }

        if (NullChecker.isNullish(oRequest.getSender().getDevice().getId().get(0).getRoot()))
        {
            addLogError("The device id value (i.e. II.getRoot() or " +
                    "oRequest.getSender().getDevice().getId().get(0).getRoot()) is null.");
            return true;
        }
        //all fields are ok return false for null value check
        return false;
    }

    protected boolean areControlActProcessFieldsNull (PRPAIN201305UV02 oRequest)
    {
        if (oRequest.getControlActProcess() == null)
        {
            addLogError("The controlActProcess object from the incomming request message is null.");
            return true;
        }
        else if (oRequest.getControlActProcess().getQueryByParameter() == null)
        {
            addLogError("The JAXB queryByParameter object in the request's controlActProcess object is null.");
            return true;
        }
        else if (oRequest.getControlActProcess().getQueryByParameter().getValue() == null)
        {
            addLogError("The queryByParameter object in the request's controlActProcess object is null.");
            return true;
        }
        else if (oRequest.getControlActProcess().getQueryByParameter().getValue().getQueryId() == null)
        {
            addLogError("The queryId object in the request's controlActProcess object is null.");
            return true;
        } //else all is well, fields are not null; return false
        return false;
    }

}
