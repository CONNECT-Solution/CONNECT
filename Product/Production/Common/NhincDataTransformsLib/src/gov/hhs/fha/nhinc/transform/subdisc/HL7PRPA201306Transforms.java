/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import java.math.BigInteger;
//import javax.xml.datatype.XMLGregorianCalendar;
//import javax.xml.datatype.DatatypeFactory;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;

/**
 *
 * @author dunnek
 */
public class HL7PRPA201306Transforms {

    private static Log log = LogFactory.getLog(HL7PRPA201306Transforms.class);

    public static PRPAIN201306UV02 createPRPA201306(PRPAMT201301UV02Patient patient, String senderOID, String receiverAAID, String receiverOID, String localDeviceId, PRPAIN201305UV02 query) {
        PRPAIN201306UV02 result = new PRPAIN201306UV02();

        log.debug("Create the 201306 message header fields");
        result.setITSVersion(HL7Constants.ITS_VERSION);
        result.setId(HL7MessageIdGenerator.GenerateHL7MessageId(receiverOID));
        result.setCreationTime(HL7DataTransformHelper.CreationTimeFactory());
        result.setInteractionId(HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "PRPA_IN201306UV"));
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

    public static PRPAIN201306UV02MFMIMT700711UV01ControlActProcess createQUQIMT021001UV01ControlActProcess(PRPAMT201301UV02Patient patient, String localDeviceId, PRPAIN201305UV02 query, String aaId, String orgId) {
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);

        controlActProcess.setCode(HL7DataTransformHelper.CDFactory("PRPA_TE201306UV", HL7Constants.INTERACTION_ID_ROOT));
        log.debug("Add the Subject");
        controlActProcess.getSubject().add(createSubject(patient, query, localDeviceId, aaId, orgId));
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
        respCode.setCode("OK");
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

    private static PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent createRegEvent(PRPAMT201301UV02Patient patient, PRPAIN201305UV02 query, String patientId, String aaID, String orgId) {
        PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent regEvent = new PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent();

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

        result.setAssignedEntity(createAssignEntity(oid));

        return result;
    }

    private static COCTMT090003UV01AssignedEntity createAssignEntity(String oid) {
        COCTMT090003UV01AssignedEntity assignedEntity = new COCTMT090003UV01AssignedEntity();

        II id = new II();
        id.setRoot(oid);
        assignedEntity.getId().add(id);

        return assignedEntity;
    }
}
