/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte;

import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.CSHelper;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.Configuration;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.CreationTimeHelper;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.IIHelper;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.InteractionIdHelper;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.SenderReceiverHelper;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers.UniqueIdHelper;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.hl7.v3.*;

/**
 *
 * @author rayj
 */
public class PixAddBuilder {

    private static final String AcceptAckCodeValue = "AL";
    private static final String ITSVersion = "XML_1.0";
    private static final String InteractionIdExtension = "PRPA_IN201301UV";
    private static final String MoodCodeValue = "EVN";
    private static final String PatientClassCode = "PAT";
    private static final String PatientStatusCode = "active";
    private static final String ProcessingCodeValue = "P";
    private static final String ProcessingModeCode = "T";
    private static final String SubjectTypeCode = "SUBJ";
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PixAddBuilder.class);

    public static CreatePixAddResponseType createPixAdd(CreatePixAddRequestType createPixAddRequest) {
        CreatePixAddResponseType createPixAddResponse = new CreatePixAddResponseType();
        createPixAddResponse.setPRPAIN201301UV02(createPixAdd(createPixAddRequest.getAddPatientCorrelationRequest()));
        return createPixAddResponse;
    }

    public static PRPAIN201301UV02 createPixAdd(gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationRequestType addPatientCorrelationRequest) {
        log.info("begin createPixAdd");
        PRPAIN201301UV02 message = null;

        List<QualifiedSubjectIdentifierType> qualifiedIds = addPatientCorrelationRequest.getQualifiedPatientIdentifier();
        log.info("qualifiedIds is null? -> " + (qualifiedIds == null));
        log.info("qualifiedIds.size()=" + (qualifiedIds.size()));

        message = createTransmissionWrapper(Configuration.getMyCommunityId(), null);
        message.setControlActProcess(buildControlActProcess(qualifiedIds));

        log.info("end createPixAdd");
        return message;
    }

    private static PRPAIN201301UV02 createTransmissionWrapper(String senderId, String receiverId) {
        PRPAIN201301UV02 message = new PRPAIN201301UV02();

        message.setITSVersion(ITSVersion);
        message.setId(UniqueIdHelper.createUniqueId());
        message.setCreationTime(CreationTimeHelper.getCreationTime());
        message.setInteractionId(InteractionIdHelper.createInteractionId(InteractionIdExtension));

        message.setProcessingCode(CSHelper.buildCS(ProcessingCodeValue));
        message.setProcessingModeCode(CSHelper.buildCS(ProcessingModeCode));
        message.setAcceptAckCode(CSHelper.buildCS(AcceptAckCodeValue));

        message.getReceiver().add(SenderReceiverHelper.CreateReceiver(receiverId));
        message.setSender(SenderReceiverHelper.CreateSender(senderId));

        return message;
    }

    private static PRPAIN201301UV02MFMIMT700701UV01ControlActProcess buildControlActProcess(List<QualifiedSubjectIdentifierType> qualifiedIds) {
        log.info("begin buildControlActProcess");

        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);

        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject1();
        subject.getTypeCode().add(SubjectTypeCode);
        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent registrationEvent = buildRegistrationEvent(qualifiedIds);
        subject.setRegistrationEvent(registrationEvent);

        controlActProcess.getSubject().add(subject);

        log.info("end buildControlActProcess");
        log.info("controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().size()=" + controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().size());

        return controlActProcess;
    }

    private static PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent buildRegistrationEvent(List<QualifiedSubjectIdentifierType> qualifiedIds) {
        log.info("begin buildRegistrationEvent");

        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent registrationEvent = new PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent();
        registrationEvent.getId().add(IIHelper.IIFactoryCreateNull());
        registrationEvent.setStatusCode(CSHelper.buildCS("active"));

        PRPAIN201301UV02MFMIMT700701UV01Subject2 subject1 = new PRPAIN201301UV02MFMIMT700701UV01Subject2();
        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        patient.getClassCode().add(PatientClassCode);
        patient.setStatusCode(CSHelper.buildCS(PatientStatusCode));

        log.info("adding qualifiedSubjectIdentifiers");
        for (QualifiedSubjectIdentifierType qualifiedId : qualifiedIds) {
            II ii = IIHelper.IIFactory(qualifiedId);
            patient.getId().add(ii);
            log.info("adding a qualifiedSubjectIdentifier");
        }
        log.info("patient.getId().size()=" + patient.getId().size());

        log.info("building patientPerson");
        JAXBElement<PRPAMT201301UV02Person> patientPersonElement;
        PRPAMT201301UV02Person patientPerson = new PRPAMT201301UV02Person();

        PNExplicit patientName = new PNExplicit();
        patientName.getNullFlavor().add("NA");
        patientPerson.getName().add(patientName);
        
        QName xmlqname = new QName("urn:hl7-org:v3", "patientPerson");
        patientPersonElement = new JAXBElement<PRPAMT201301UV02Person>(xmlqname, PRPAMT201301UV02Person.class, patientPerson);

        patient.setPatientPerson(patientPersonElement);
        subject1.setPatient(patient);
        registrationEvent.setSubject1(subject1);

        log.info("end buildRegistrationEvent");
        log.info("registrationEvent.getSubject1().getPatient().getId().size()=" + registrationEvent.getSubject1().getPatient().getId().size());

        return registrationEvent;
    }    
}
