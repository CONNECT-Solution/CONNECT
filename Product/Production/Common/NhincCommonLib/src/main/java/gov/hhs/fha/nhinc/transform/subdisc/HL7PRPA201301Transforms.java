/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.II;
import org.hl7.v3.CS;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject2;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.XActMoodIntentEvent;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.QUQIMT021001UV01AuthorOrPerformer;
import org.hl7.v3.MFMIMT700701UV01AuthorOrPerformer;
import org.hl7.v3.MFMIMT700711UV01AuthorOrPerformer;
import org.hl7.v3.MFMIMT700701UV01DataEnterer;
import org.hl7.v3.MFMIMT700711UV01DataEnterer;
import org.hl7.v3.QUQIMT021001UV01DataEnterer;
import org.hl7.v3.MFMIMT700711UV01InformationRecipient;
import org.hl7.v3.MFMIMT700701UV01InformationRecipient;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MCCIMT000300UV01Sender;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7PRPA201301Transforms {

    public static PRPAIN201301UV02 createPRPA201301(PRPAMT201301UV02Patient patient, String localDeviceId, String senderOID, String receiverOID) {
        PRPAIN201301UV02 result = createPRPA201301Headers(localDeviceId);


        // Create the Sender
        result.setSender(HL7SenderTransforms.createMCCIMT000100UV01Sender(senderOID));

        // Create the Receiver
        result.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000100UV01Receiver(receiverOID));

        // Create the control act process
        result.setControlActProcess(createMFMIMT700701UV01ControlActProcess(patient, localDeviceId));

        return result;
    }

    public static PRPAIN201301UV02 createPRPA201301Headers(String localDeviceId) {
        PRPAIN201301UV02 result = new PRPAIN201301UV02();
        // Create the 201301 message header fields
        result.setITSVersion(HL7Constants.ITS_VERSION);
        result.setId(HL7MessageIdGenerator.GenerateHL7MessageId(localDeviceId));
        result.setCreationTime(HL7DataTransformHelper.CreationTimeFactory());
        result.setInteractionId(HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "PRPA_IN201301UV"));
        result.setProcessingCode(HL7DataTransformHelper.CSFactory("T"));
        result.setProcessingModeCode(HL7DataTransformHelper.CSFactory("T"));
        result.setAcceptAckCode(HL7DataTransformHelper.CSFactory("AL"));

        return result;
    }

    public static PRPAIN201301UV02MFMIMT700701UV01ControlActProcess createMFMIMT700701UV01ControlActProcess(PRPAMT201301UV02Patient patient, String localDeviceId) {
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);

        controlActProcess.setCode(HL7DataTransformHelper.CDFactory("PRPA_TE201301UV"));

        controlActProcess.getSubject().add(createPRPAIN201301UVMFMIMT700701UV01Subject1(patient, localDeviceId));
        return controlActProcess;
    }

    public static PRPAIN201301UV02MFMIMT700701UV01Subject1 createPRPAIN201301UVMFMIMT700701UV01Subject1(PRPAMT201301UV02Patient patient, String localDeviceId) {
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject1 = new PRPAIN201301UV02MFMIMT700701UV01Subject1();

        subject1.getTypeCode().add("SUBJ");
        subject1.setContextConductionInd(false);

        subject1.setRegistrationEvent(createPRPAIN201301UVMFMIMT700701UV01RegistrationEvent(patient, localDeviceId));

        return subject1;
    }

    public static PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent createPRPAIN201301UVMFMIMT700701UV01RegistrationEvent(PRPAMT201301UV02Patient patient, String localDeviceId) {
        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent regevent = new PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent();

        regevent.getClassCode().add("REG");
        regevent.getMoodCode().add("EVN");

        II regId = new II();
        regId.getNullFlavor().add("NA");
        regevent.getId().add(regId);

        regevent.setStatusCode(HL7DataTransformHelper.CSFactory("active"));

        regevent.setSubject1(createPRPAIN201301UVMFMIMT700701UV01Subject2(patient));

        regevent.setCustodian(HL7CustodianTransforms.createMFMIMT700701UV01Custodian(localDeviceId));

        return regevent;
    }

    public static PRPAIN201301UV02MFMIMT700701UV01Subject2 createPRPAIN201301UVMFMIMT700701UV01Subject2(PRPAMT201301UV02Patient patient) {
        PRPAIN201301UV02MFMIMT700701UV01Subject2 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject2();

        subject.setPatient(patient);

        return subject;
    }

    public static PRPAIN201301UV02 createPRPA201301(PRPAIN201305UV02 original, String localDeviceId) {
        if (original == null) {
            return null;
        }
        PRPAIN201301UV02 result = createPRPA201301Headers(localDeviceId);

        result.setVersionCode(original.getVersionCode());

        result = HL7ArrayTransforms.copyMCCIMT000100UV01AttentionLine(original, result);
        //result = HL7ArrayTransforms.copyMCCIMT000100UV01Receiver(original, result);
        for (MCCIMT000100UV01Receiver receiver : original.getReceiver()) {
            result.getReceiver().add(receiver);
        }

        result.setSecurityText(original.getSecurityText());
        result.setSender(original.getSender());
        result.setSequenceNumber(original.getSequenceNumber());
        result.setControlActProcess(copyControlActProcess(original.getControlActProcess(), localDeviceId));

        result = HL7ArrayTransforms.copyNullFlavors(original, result);
        result = HL7ArrayTransforms.copyRealmCodes(original, result);

        return result;
    }

    public static PRPAIN201301UV02 createPRPA201301(PRPAIN201306UV02 original, String localDeviceId) {
        if (original == null) {
            return null;
        }
        PRPAIN201301UV02 result = createPRPA201301Headers(localDeviceId);


        result.setVersionCode(original.getVersionCode());
        result.setSender(copySender(original.getSender()));

        result = HL7ArrayTransforms.copyMCCIMT000100UV01AttentionLine(original, result);
        result = HL7ArrayTransforms.copyMCCIMT000100UV01Receiver(original, result);


        result.setSecurityText(original.getSecurityText());

        result.setControlActProcess(copyControlActProcess(original.getControlActProcess(), localDeviceId));

        result = HL7ArrayTransforms.copyNullFlavors(original, result);
        result = HL7ArrayTransforms.copyRealmCodes(original, result);

        return result;
    }

    public static PRPAIN201301UV02MFMIMT700701UV01ControlActProcess copyControlActProcess(PRPAIN201306UV02MFMIMT700711UV01ControlActProcess original, String localDeviceId) {
        if (original == null) {
            return null;
        }
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess result = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();

        result.setClassCode(original.getClassCode());
        result.setCode(original.getCode());
        result.setEffectiveTime(original.getEffectiveTime());
        result.setLanguageCode(original.getLanguageCode());
        result.setMoodCode(original.getMoodCode());
        result.setText(original.getText());
        result.setTypeId(original.getTypeId());

        if (original.getAuthorOrPerformer() != null) {
            for (MFMIMT700711UV01AuthorOrPerformer item : original.getAuthorOrPerformer()) {
                MFMIMT700701UV01AuthorOrPerformer newItem = copyAuthorOrPerformer(item);
                result.getAuthorOrPerformer().add(newItem);
            }
        }
        if (original.getId() != null) {
            result.getId().clear();
            for (II item : original.getId()) {
                result.getId().add(item);
            }
        }
        if (original.getDataEnterer() != null) {
            result.getDataEnterer().clear();
            for (MFMIMT700711UV01DataEnterer item : original.getDataEnterer()) {
                MFMIMT700701UV01DataEnterer newItem = copyDataEnterer(item);
                result.getDataEnterer().add(newItem);
            }
        }

        PRPAMT201301UV02Patient patient;
        PRPAMT201306UV02QueryByParameter params;

        params = original.getQueryByParameter().getValue();

        patient = HL7PatientTransforms.createPRPAMT201301UVPatient(original.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient());

        result.getSubject().add(createPRPAIN201301UVMFMIMT700701UV01Subject1(patient, localDeviceId));


        result = HL7ArrayTransforms.copyNullFlavors(original, result);
        result = copyInformationReceipent(original, result);
        result = HL7ArrayTransforms.copyNullFlavors(original, result);


        return result;
    }

    public static PRPAIN201301UV02MFMIMT700701UV01ControlActProcess copyControlActProcess2(PRPAIN201306UV02MFMIMT700711UV01ControlActProcess original, String localDeviceId) {
        if (original == null) {
            return null;
        }
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess result = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();

        result.setClassCode(original.getClassCode());
        result.setCode(original.getCode());
        result.setEffectiveTime(original.getEffectiveTime());
        result.setLanguageCode(original.getLanguageCode());
        result.setMoodCode(original.getMoodCode());
        result.setText(original.getText());
        result.setTypeId(original.getTypeId());

        if (original.getAuthorOrPerformer() != null) {
            for (MFMIMT700711UV01AuthorOrPerformer item : original.getAuthorOrPerformer()) {
                MFMIMT700701UV01AuthorOrPerformer newItem = copyAuthorOrPerformer(item);
                result.getAuthorOrPerformer().add(newItem);
            }
        }
        if (original.getId() != null) {
            result.getId().clear();
            for (II item : original.getId()) {
                result.getId().add(item);
            }
        }
        if (original.getDataEnterer() != null) {
            result.getDataEnterer().clear();
            for (MFMIMT700711UV01DataEnterer item : original.getDataEnterer()) {
                MFMIMT700701UV01DataEnterer newItem = copyDataEnterer(item);
                result.getDataEnterer().add(newItem);
            }
        }

        PRPAMT201301UV02Patient patient;
        PRPAMT201306UV02QueryByParameter params;

        params = original.getQueryByParameter().getValue();

        patient = HL7PatientTransforms.create201301Patient(params.getParameterList());

        result.getSubject().add(createPRPAIN201301UVMFMIMT700701UV01Subject1(patient, localDeviceId));


        result = HL7ArrayTransforms.copyNullFlavors(original, result);
        result = copyInformationReceipent(original, result);
        result = HL7ArrayTransforms.copyNullFlavors(original, result);


        return result;
    }

    private static PRPAIN201301UV02MFMIMT700701UV01ControlActProcess copyInformationReceipent(PRPAIN201306UV02MFMIMT700711UV01ControlActProcess from, PRPAIN201301UV02MFMIMT700701UV01ControlActProcess to) {
        if (from != null & from.getInformationRecipient().size() > 0) {
            to.getInformationRecipient().clear();
            for (MFMIMT700711UV01InformationRecipient item : from.getInformationRecipient()) {
                to.getInformationRecipient().add(copyInformationRecepient(item));
            }
        }

        return to;
    }

    private static MFMIMT700701UV01InformationRecipient copyInformationRecepient(MFMIMT700711UV01InformationRecipient orig) {
        MFMIMT700701UV01InformationRecipient result = null;

        if (orig != null) {
            result = new MFMIMT700701UV01InformationRecipient();
            result.setAssignedPerson(orig.getAssignedPerson());
            result.setContextControlCode(orig.getContextControlCode());
            result.setTime(orig.getTime());
            result.setTypeCode(orig.getTypeCode());
            result.setTypeId(orig.getTypeId());

            result = HL7ArrayTransforms.copyNullFlavors(orig, result);
            result = HL7ArrayTransforms.copyRealmCodes(orig, result);
            result = HL7ArrayTransforms.copyTemplateIds(orig, result);


        }



        return result;
    }

    public static PRPAIN201301UV02MFMIMT700701UV01ControlActProcess copyControlActProcess(PRPAIN201305UV02QUQIMT021001UV01ControlActProcess original, String localDeviceId) {
        if (original == null) {
            return null;
        }
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess result = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();

        result.setClassCode(original.getClassCode());
        result.setCode(original.getCode());
        result.setEffectiveTime(original.getEffectiveTime());
        result.setLanguageCode(original.getLanguageCode());
        result.setMoodCode(original.getMoodCode());
        result.setText(original.getText());
        result.setTypeId(original.getTypeId());

        String originalAAId = null;

        if (original.getAuthorOrPerformer() != null) {
            if (original.getAuthorOrPerformer().get(0) != null &&
                    original.getAuthorOrPerformer().get(0).getAssignedDevice() != null &&
                    original.getAuthorOrPerformer().get(0).getAssignedDevice().getValue() != null &&
                    NullChecker.isNotNullish(original.getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId()) &&
                    original.getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0) != null &&
                    NullChecker.isNotNullish(original.getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot())) {
                originalAAId = original.getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot();
            }

            for (QUQIMT021001UV01AuthorOrPerformer item : original.getAuthorOrPerformer()) {
                MFMIMT700701UV01AuthorOrPerformer newItem = copyAuthorOrPerformer(item);
                result.getAuthorOrPerformer().add(newItem);
            }
        }
        if (original.getId() != null) {
            result.getId().clear();
            for (II item : original.getId()) {
                result.getId().add(item);
            }
        }
        if (original.getDataEnterer() != null) {
            result.getDataEnterer().clear();
            for (QUQIMT021001UV01DataEnterer item : original.getDataEnterer()) {
                MFMIMT700701UV01DataEnterer newItem = copyDataEnterer(item);
                result.getDataEnterer().add(newItem);
            }
        }

        if (original.getRealmCode() != null) {
            for (CS item : original.getRealmCode()) {
                result.getRealmCode().add(item);
            }
        }
        if (original.getInformationRecipient() != null) {
        }
        result = HL7ArrayTransforms.copyNullFlavors(original, result);

        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(original.getQueryByParameter().getValue().getParameterList(), originalAAId);

        result.getSubject().add(createPRPAIN201301UVMFMIMT700701UV01Subject1(patient, localDeviceId));

        return result;
    }

    public static MFMIMT700701UV01DataEnterer copyDataEnterer(MFMIMT700711UV01DataEnterer orig) {
        MFMIMT700701UV01DataEnterer result = new MFMIMT700701UV01DataEnterer();
        result.setAssignedPerson(orig.getAssignedPerson());
        result.setContextControlCode(orig.getContextControlCode());
        result.setTime(orig.getTime());
        result.setTypeId(orig.getTypeId());


        result = HL7ArrayTransforms.copyNullFlavors(orig, result);

        return result;
    }

    public static MFMIMT700701UV01DataEnterer copyDataEnterer(QUQIMT021001UV01DataEnterer orig) {
        MFMIMT700701UV01DataEnterer result = new MFMIMT700701UV01DataEnterer();
        result.setAssignedPerson(orig.getAssignedPerson());
        result.setContextControlCode(orig.getContextControlCode());
        result.setTime(orig.getTime());
        result.setTypeId(orig.getTypeId());


        result = HL7ArrayTransforms.copyNullFlavors(orig, result);

        return result;
    }

    public static MFMIMT700701UV01AuthorOrPerformer copyAuthorOrPerformer(QUQIMT021001UV01AuthorOrPerformer orig) {
        MFMIMT700701UV01AuthorOrPerformer result = new MFMIMT700701UV01AuthorOrPerformer();

        result.setAssignedDevice(orig.getAssignedDevice());
        result.setAssignedPerson(orig.getAssignedPerson());
        result.setContextControlCode(orig.getContextControlCode());
        result.setModeCode(orig.getModeCode());
        result.setNoteText(orig.getNoteText());
        result.setSignatureCode(orig.getSignatureCode());
        result.setSignatureText(orig.getSignatureText());
        result.setTime(orig.getTime());
        result.setTypeCode(orig.getTypeCode());
        result.setTypeId(orig.getTypeId());


        result = HL7ArrayTransforms.copyNullFlavors(orig, result);

        if (orig.getRealmCode() != null) {
            for (CS item : orig.getRealmCode()) {
                result.getRealmCode().add(item);
            }
        }
        if (orig.getTemplateId() != null) {
            for (II item : orig.getTemplateId()) {
                result.getTemplateId().add(item);
            }
        }

        return result;
    }

    public static MFMIMT700701UV01AuthorOrPerformer copyAuthorOrPerformer(MFMIMT700711UV01AuthorOrPerformer orig) {
        MFMIMT700701UV01AuthorOrPerformer result = new MFMIMT700701UV01AuthorOrPerformer();

        result.setAssignedDevice(orig.getAssignedDevice());
        result.setAssignedPerson(orig.getAssignedPerson());
        result.setContextControlCode(orig.getContextControlCode());
        result.setModeCode(orig.getModeCode());
        result.setNoteText(orig.getNoteText());
        result.setSignatureCode(orig.getSignatureCode());
        result.setSignatureText(orig.getSignatureText());
        result.setTime(orig.getTime());
        result.setTypeCode(orig.getTypeCode());
        result.setTypeId(orig.getTypeId());


        result = HL7ArrayTransforms.copyNullFlavors(orig, result);

        if (orig.getRealmCode() != null) {
            for (CS item : orig.getRealmCode()) {
                result.getRealmCode().add(item);
            }
        }
        if (orig.getTemplateId() != null) {
            for (II item : orig.getTemplateId()) {
                result.getTemplateId().add(item);
            }
        }

        return result;
    }

    private static MCCIMT000100UV01Sender copySender(MCCIMT000300UV01Sender orig) {
        MCCIMT000100UV01Sender result = null;
        org.hl7.v3.MCCIMT000100UV01Device newDevice;

        if (orig != null) {
            result = new MCCIMT000100UV01Sender();


            result.setTelecom(orig.getTelecom());
            result.setTypeCode(orig.getTypeCode());
            result.setTypeId(orig.getTypeId());

            if (orig.getDevice() != null) {
                newDevice = new org.hl7.v3.MCCIMT000100UV01Device();
                newDevice.setClassCode(orig.getDevice().getClassCode());
                newDevice.setDesc(orig.getDevice().getDesc());
                newDevice.setDeterminerCode(orig.getDevice().getDeterminerCode());
                newDevice.setExistenceTime(orig.getDevice().getExistenceTime());
                newDevice.setManufacturerModelName(orig.getDevice().getManufacturerModelName());
                newDevice.setSoftwareName(orig.getDevice().getSoftwareName());
                newDevice.setTypeId(orig.getDevice().getTypeId());
                newDevice.getId().add(orig.getDevice().getId().get(0));

                MCCIMT000100UV01Agent newAgent = new MCCIMT000100UV01Agent();
                MCCIMT000100UV01Organization newOrg = new MCCIMT000100UV01Organization();
                newOrg.setClassCode(HL7Constants.ORG_CLASS_CODE);
                newOrg.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);
                newOrg.getId().add(orig.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0));

                javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
                JAXBElement<MCCIMT000100UV01Organization> orgElem = new JAXBElement<MCCIMT000100UV01Organization>(xmlqnameorg, MCCIMT000100UV01Organization.class, newOrg);
                newAgent.setRepresentedOrganization(orgElem);
                newAgent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);

                javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
                JAXBElement<MCCIMT000100UV01Agent> agentElem = new JAXBElement<MCCIMT000100UV01Agent>(xmlqnameagent, MCCIMT000100UV01Agent.class, newAgent);

                newDevice.setAsAgent(agentElem);
                result.setDevice(newDevice);
            }
        }

        return result;

    }
}
