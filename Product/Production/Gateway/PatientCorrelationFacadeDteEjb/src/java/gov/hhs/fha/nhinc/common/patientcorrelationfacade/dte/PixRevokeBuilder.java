/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte;

import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RemovePatientCorrelationRequestType;
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
import org.hl7.v3.CreatePixRevokeRequestType;
import org.hl7.v3.CreatePixRevokeResponseType;
import org.hl7.v3.II;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201303UV;
import org.hl7.v3.PRPAIN201303UVMFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201303UVMFMIMT700701UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201303UVMFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201303UVMFMIMT700701UV01Subject2;
import org.hl7.v3.PRPAMT201303UVPerson;
import org.hl7.v3.PRPAMT201305UVPatient;
import org.hl7.v3.PRPAMT201305UVPerson;

/**
 *
 * @author jhoppesc
 */
public class PixRevokeBuilder {

    private static final String AcceptAckCodeValue = "AL";
    private static final String ITSVersion = "XML_1.0";
    private static final String InteractionIdExtension = "PRPA_IN201303UV";
    private static final String MoodCodeValue = "EVN";
    private static final String PatientClassCode = "PAT";
    private static final String PatientStatusCode = "active";
    private static final String ProcessingCodeValue = "T";
    private static final String ProcessingModeCode = "T";
    private static final String SubjectTypeCode = "SUBJ";
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PixRevokeBuilder.class);

    public static CreatePixRevokeResponseType createPixRevoke(CreatePixRevokeRequestType createPixRevokeRequest) {
        CreatePixRevokeResponseType createPixRevokeResponse = new CreatePixRevokeResponseType();
        createPixRevokeResponse.setPRPAIN201303UV(createPixRevoke(createPixRevokeRequest.getRemovePatientCorrelationRequest()));
        return createPixRevokeResponse;
    }

    public static PRPAIN201303UV createPixRevoke(RemovePatientCorrelationRequestType revokePatientCorrelationRequest) {
        log.info("begin createPixAdd");
        PRPAIN201303UV message = null;

        List<QualifiedSubjectIdentifierType> qualifiedIds = revokePatientCorrelationRequest.getQualifiedPatientIdentifier();
        log.info("qualifiedIds is null? -> " + (qualifiedIds == null));
        log.info("qualifiedIds.size()=" + (qualifiedIds.size()));

        message = createTransmissionWrapper(Configuration.getMyCommunityId(), null);
        message.setControlActProcess(buildControlActProcess(qualifiedIds));

        log.info("end createPixRevoke");
        return message;
    }

    private static PRPAIN201303UV createTransmissionWrapper(String senderId, String receiverId) {
        PRPAIN201303UV message = new PRPAIN201303UV();

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

    private static PRPAIN201303UVMFMIMT700701UV01ControlActProcess buildControlActProcess(List<QualifiedSubjectIdentifierType> qualifiedIds) {
        log.info("begin buildControlActProcess");

        PRPAIN201303UVMFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201303UVMFMIMT700701UV01ControlActProcess();
        controlActProcess.setMoodCode(MoodCodeValue);

        PRPAIN201303UVMFMIMT700701UV01Subject1 subject = new PRPAIN201303UVMFMIMT700701UV01Subject1();
        subject.getTypeCode().add(SubjectTypeCode);
        PRPAIN201303UVMFMIMT700701UV01RegistrationEvent registrationEvent = buildRegistrationEvent(qualifiedIds);
        subject.setRegistrationEvent(registrationEvent);

        controlActProcess.getSubject().add(subject);

        log.info("end buildControlActProcess");
        log.info("controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().size()=" + controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().size());

        return controlActProcess;
    }

    private static PRPAIN201303UVMFMIMT700701UV01RegistrationEvent buildRegistrationEvent(List<QualifiedSubjectIdentifierType> qualifiedIds) {
        log.info("begin buildRegistrationEvent");

        PRPAIN201303UVMFMIMT700701UV01RegistrationEvent registrationEvent = new PRPAIN201303UVMFMIMT700701UV01RegistrationEvent();
        registrationEvent.getId().add(IIHelper.IIFactoryCreateNull());
        registrationEvent.setStatusCode(CSHelper.buildCS("active"));

        PRPAIN201303UVMFMIMT700701UV01Subject2 subject1 = new PRPAIN201303UVMFMIMT700701UV01Subject2();
        PRPAMT201305UVPatient patient = new PRPAMT201305UVPatient();
        patient.setClassCode(PatientClassCode);
        patient.setStatusCode(CSHelper.buildCS(PatientStatusCode));

        log.info("adding qualifiedSubjectIdentifiers");
        for (QualifiedSubjectIdentifierType qualifiedId : qualifiedIds) {
            II ii = IIHelper.IIFactory(qualifiedId);
            patient.getId().add(ii);
            log.info("adding a qualifiedSubjectIdentifier");
        }
        log.info("patient.getId().size()=" + patient.getId().size());

        log.info("building patientPerson");
        JAXBElement<PRPAMT201305UVPerson> patientPersonElement;
        PRPAMT201305UVPerson patientPerson = new PRPAMT201305UVPerson();

        PNExplicit patientName = new PNExplicit();
        patientName.getNullFlavor().add("NA");
        patientPerson.getName().add(patientName);

        QName xmlqname = new QName("urn:hl7-org:v3", "patientPerson");
        patientPersonElement = new JAXBElement<PRPAMT201305UVPerson>(xmlqname, PRPAMT201305UVPerson.class, patientPerson);

        patient.setPatientPerson(patientPersonElement);
        subject1.setPatient(patient);
        registrationEvent.setSubject1(subject1);

        log.info("end buildRegistrationEvent");
        log.info("registrationEvent.getSubject1().getPatient().getId().size()=" + registrationEvent.getSubject1().getPatient().getId().size());

        return registrationEvent;
    }
}
