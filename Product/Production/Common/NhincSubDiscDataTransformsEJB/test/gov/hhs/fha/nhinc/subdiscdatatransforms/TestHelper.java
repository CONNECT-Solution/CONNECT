/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subdiscdatatransforms;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Constants;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MCCIMT000200UV01Receiver;
import org.hl7.v3.MCCIMT000200UV01Sender;
import org.hl7.v3.MCCIMT000300UV01Receiver;
import org.hl7.v3.MCCIMT000300UV01Sender;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201301UV;
import org.hl7.v3.PRPAIN201301UVMFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201302UV;
import org.hl7.v3.PRPAIN201302UVMFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201305UV;
import org.hl7.v3.PRPAIN201305UVQUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201310UV;
import org.hl7.v3.PRPAMT201307UVQueryByParameter;
import static org.junit.Assert.*;

/**
 *
 * @author Jon Hoppesch
 */
public class TestHelper {

    private static Log log = LogFactory.getLog(TestHelper.class);

    public static void assertENNameEquals(ENExplicit enName, String family, String given) {
        List<Serializable> choice = enName.getContent();
        Iterator<Serializable> iterSerialObjects = choice.iterator();

        assertNameEquals(iterSerialObjects, family, given);
    }

    public static void assertPNNameEquals(PNExplicit pnName, String family, String given) {
        List<Serializable> choice = pnName.getContent();
        Iterator<Serializable> iterSerialObjects = choice.iterator();

        assertNameEquals(iterSerialObjects, family, given);
    }

    public static void assertNameEquals(Iterator<Serializable> iterSerialObjects, String family, String given) {
        EnExplicitFamily enFamilyName = null;
        EnExplicitGiven enGivenName = new EnExplicitGiven();

        while (iterSerialObjects.hasNext()) {
            Serializable contentItem = iterSerialObjects.next();

            if (contentItem instanceof JAXBElement) {
                JAXBElement oJAXBElement = (JAXBElement) contentItem;

                if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                    enFamilyName = (EnExplicitFamily) oJAXBElement.getValue();
                    log.info("Family Name: " + enFamilyName.getContent());
                } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    enGivenName = (EnExplicitGiven) oJAXBElement.getValue();
                    log.info("Given Name: " + enGivenName.getContent());
                }
            }
        }

        if (NullChecker.isNotNullish(family)) {
            assertEquals(family, enFamilyName.getContent());
        }

        if (NullChecker.isNotNullish(given)) {
            assertEquals(given, enGivenName.getContent());
        }
    }

    public static void assertNameEquals(Iterator<Serializable> iterSerialObjects, EnExplicitFamily family, EnExplicitGiven given) {
        EnExplicitFamily enFamilyName = null;
        EnExplicitGiven enGivenName = new EnExplicitGiven();

        while (iterSerialObjects.hasNext()) {
            Serializable contentItem = iterSerialObjects.next();

            if (contentItem instanceof JAXBElement) {
                JAXBElement oJAXBElement = (JAXBElement) contentItem;

                if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                    enFamilyName = (EnExplicitFamily) oJAXBElement.getValue();
                    log.info("Family Name: " + enFamilyName.getContent());
                } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    enGivenName = (EnExplicitGiven) oJAXBElement.getValue();
                    log.info("Given Name: " + enGivenName.getContent());
                }
            }
        }

        if (family != null) {
            assertEquals(family.getContent(), enFamilyName.getContent());
        }

        if (given != null) {
            assertEquals(given.getContent(), enGivenName.getContent());
        }
    }

    public static void assertReceiverIdEquals(String receiverOID, PRPAIN201301UV message) {
        assertNotNull(message.getReceiver());
        assertNotNull(message.getReceiver().get(0));
        assertRecDeviceNotNull(message.getReceiver().get(0));

        if (receiverOID != null && !receiverOID.isEmpty()) {
            assertEquals(receiverOID, message.getReceiver().get(0).getDevice().getId().get(0).getRoot());
        }
    }

    public static void assertReceiverIdEquals(String receiverOID, PRPAIN201302UV message) {
        assertNotNull(message.getReceiver());
        assertNotNull(message.getReceiver().get(0));
        assertRecDeviceNotNull(message.getReceiver().get(0));

        if (receiverOID != null && !receiverOID.isEmpty()) {
            assertEquals(receiverOID, message.getReceiver().get(0).getDevice().getId().get(0).getRoot());
        }
    }

    public static void assertReceiverIdEquals(String receiverOID, PRPAIN201305UV message) {
        assertNotNull(message.getReceiver());
        assertNotNull(message.getReceiver().get(0));
        assertRecDeviceNotNull(message.getReceiver().get(0));

        if (receiverOID != null && !receiverOID.isEmpty()) {
            assertEquals(receiverOID, message.getReceiver().get(0).getDevice().getId().get(0).getRoot());
        }
    }

    static void assertReceiverIdEquals(String receiverOID, PRPAIN201310UV message) {
        assertNotNull(message.getReceiver());
        assertNotNull(message.getReceiver().get(0));
        assertRecDeviceNotNull(message.getReceiver().get(0));

        if (receiverOID != null && !receiverOID.isEmpty()) {
            assertEquals(receiverOID, message.getReceiver().get(0).getDevice().getId().get(0).getRoot());
        }
    }

    public static void assertRecDeviceNotNull(MCCIMT000100UV01Receiver receiver) {
        assertNotNull(receiver.getDevice());
        assertNotNull(receiver.getDevice().getId());
        assertNotNull(receiver.getDevice().getId().get(0));
    }

    public static void assertRecDeviceNotNull(MCCIMT000200UV01Receiver receiver) {
        assertNotNull(receiver.getDevice());
        assertNotNull(receiver.getDevice().getId());
        assertNotNull(receiver.getDevice().getId().get(0));
    }

    private static void assertRecDeviceNotNull(MCCIMT000300UV01Receiver receiver) {
        assertNotNull(receiver.getDevice());
        assertNotNull(receiver.getDevice().getId());
        assertNotNull(receiver.getDevice().getId().get(0));
    }

    public static void assertSenderIdEquals(String senderOID, PRPAIN201301UV message) {
        assertNotNull(message.getSender());
        assertSendDeviceNotNull(message.getSender());

        if (senderOID != null && !senderOID.isEmpty()) {
            assertEquals(senderOID, message.getSender().getDevice().getId().get(0).getRoot());
        }
    }

    public static void assertSenderIdEquals(String senderOID, PRPAIN201302UV message) {
        assertNotNull(message.getSender());
        assertSendDeviceNotNull(message.getSender());

        if (senderOID != null && !senderOID.isEmpty()) {
            assertEquals(senderOID, message.getSender().getDevice().getId().get(0).getRoot());
        }
    }

    public static void assertSenderIdEquals(String senderOID, PRPAIN201305UV message) {
        assertNotNull(message.getSender());
        assertSendDeviceNotNull(message.getSender());

        if (senderOID != null && !senderOID.isEmpty()) {
            assertEquals(senderOID, message.getSender().getDevice().getId().get(0).getRoot());
        }
    }

    static void assertSenderIdEquals(String senderOID, PRPAIN201310UV message) {
        assertNotNull(message.getSender());
        assertSendDeviceNotNull(message.getSender());

        if (senderOID != null && !senderOID.isEmpty()) {
            assertEquals(senderOID, message.getSender().getDevice().getId().get(0).getRoot());
        }
    }

    public static void assertSendDeviceNotNull(MCCIMT000100UV01Sender sender) {
        assertNotNull(sender.getDevice());
        assertNotNull(sender.getDevice().getId());
        assertNotNull(sender.getDevice().getId().get(0));
    }

    public static void assertSendDeviceNotNull(MCCIMT000200UV01Sender sender) {
        assertNotNull(sender.getDevice());
        assertNotNull(sender.getDevice().getId());
        assertNotNull(sender.getDevice().getId().get(0));
    }

    private static void assertSendDeviceNotNull(MCCIMT000300UV01Sender sender) {
        assertNotNull(sender.getDevice());
        assertNotNull(sender.getDevice().getId());
        assertNotNull(sender.getDevice().getId().get(0));
    }

    public static void assertPatientIdEquals(String patId, String localDeviceId, PRPAIN201301UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientNotNull(message.getControlActProcess());

        assertEquals(localDeviceId, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
        assertEquals(patId, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
    }

    public static void assertPatientIdEquals(String patId, String localDeviceId, PRPAIN201302UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientNotNull(message.getControlActProcess());

        assertEquals(localDeviceId, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
        assertEquals(patId, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
    }

    public static void assertPatientIdEquals(String patId, String localDeviceId, PRPAIN201305UV message) {
        assertNotNull(message.getControlActProcess());
        assertParamListNotNull(message.getControlActProcess());

        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId());
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId().get(0));
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId().get(0).getValue());
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId().get(0).getValue().get(0));
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId().get(0).getValue().get(0).getRoot());
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId().get(0).getValue().get(0).getExtension());
        assertEquals(localDeviceId, message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId().get(0).getValue().get(0).getRoot());
        assertEquals(patId, message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId().get(0).getValue().get(0).getExtension());
    }

    static void assertPatientIdEquals(String patientId, String assigningAuthorityId, String localDeviceId, PRPAIN201310UV message) {
        assertNotNull(message.getControlActProcess());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getCustodian().getAssignedEntity().getId().get(0));
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0));
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0));

        if (localDeviceId != null && !localDeviceId.isEmpty()) {
            assertEquals(localDeviceId, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getCustodian().getAssignedEntity().getId().get(0).getRoot());
        }
        if (assigningAuthorityId != null && !assigningAuthorityId.isEmpty()) {
            assertEquals(assigningAuthorityId, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
        }
        if (patientId != null && !patientId.isEmpty()) {
            assertEquals(patientId, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
        }
    }

    public static void assertSsnEquals(String ssn, PRPAIN201301UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientPersonNotNull(message.getControlActProcess());

        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().get(0));
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().get(0).getId());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().get(0).getId().get(0));
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().get(0).getId().get(0).getRoot());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().get(0).getId().get(0).getExtension());

        assertEquals(HL7Constants.SSN_ID_ROOT, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().get(0).getId().get(0).getRoot());
        assertEquals(ssn, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().get(0).getId().get(0).getExtension());
    }

    public static void assertSsnEquals(String ssn, PRPAIN201302UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientPersonNotNull(message.getControlActProcess());

        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().get(0));
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().get(0).getId());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().get(0).getId().get(0));
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().get(0).getId().get(0).getRoot());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().get(0).getId().get(0).getExtension());

        assertEquals(HL7Constants.SSN_ID_ROOT, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().get(0).getId().get(0).getRoot());
        assertEquals(ssn, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().get(0).getId().get(0).getExtension());
    }

    public static void assertSsnNull(PRPAIN201301UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientPersonNotNull(message.getControlActProcess());

        assertEquals(0, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().size());
    }

    public static void assertSsnNull(PRPAIN201302UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientPersonNotNull(message.getControlActProcess());

        assertEquals(0, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().size());
    }

    public static void assertPatientNameEquals(String firstName, String lastName, PRPAIN201301UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientPersonNotNull(message.getControlActProcess());

        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().get(0));

        PNExplicit pnName = message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().get(0);
        List<Serializable> choice = pnName.getContent();
        Iterator<Serializable> iterSerialObjects = choice.iterator();

        assertNameEquals(iterSerialObjects, lastName, firstName);
    }

    public static void assertPatientNameEquals(String firstName, String lastName, PRPAIN201302UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientPersonNotNull(message.getControlActProcess());

        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().get(0));

        PNExplicit pnName = message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().get(0);
        List<Serializable> choice = pnName.getContent();
        Iterator<Serializable> iterSerialObjects = choice.iterator();

        assertNameEquals(iterSerialObjects, lastName, firstName);
    }

    public static void assertPatientNameEquals(String firstName, String lastName, PRPAIN201305UV message) {
        assertNotNull(message.getControlActProcess());
        assertParamListNotNull(message.getControlActProcess());

        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectName());
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectName().get(0));
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectName().get(0).getValue());
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectName().get(0).getValue().get(0));

        ENExplicit enName = message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectName().get(0).getValue().get(0);
        List<Serializable> choice = enName.getContent();
        Iterator<Serializable> iterSerialObjects = choice.iterator();

        assertNameEquals(iterSerialObjects, lastName, firstName);
    }

    public static void assertPatientNameNull(PRPAIN201301UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientPersonNotNull(message.getControlActProcess());

        assertNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName());
    }

    public static void assertPatientNameNull(PRPAIN201302UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientPersonNotNull(message.getControlActProcess());

        assertNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName());
    }

    public static void assertPatientNameNull(PRPAIN201305UV message) {
        assertNotNull(message.getControlActProcess());
        assertParamListNotNull(message.getControlActProcess());

        assertNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectName());
    }

    public static void assertGenderEquals(String gender, PRPAIN201301UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientPersonNotNull(message.getControlActProcess());

        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAdministrativeGenderCode());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAdministrativeGenderCode().getCode());
        assertEquals(gender, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAdministrativeGenderCode().getCode());
    }

    public static void assertGenderEquals(String gender, PRPAIN201302UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientPersonNotNull(message.getControlActProcess());

        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAdministrativeGenderCode());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAdministrativeGenderCode().getCode());
        assertEquals(gender, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAdministrativeGenderCode().getCode());
    }

    public static void assertGenderEquals(String gender, PRPAIN201305UV message) {
        assertNotNull(message.getControlActProcess());
        assertParamListNotNull(message.getControlActProcess());

        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectAdministrativeGender());
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectAdministrativeGender().get(0));
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectAdministrativeGender().get(0).getValue());
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectAdministrativeGender().get(0).getValue().get(0));
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectAdministrativeGender().get(0).getValue().get(0).getCode());
        assertEquals(gender, message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectAdministrativeGender().get(0).getValue().get(0).getCode());
    }

    public static void assertGenderNull(PRPAIN201301UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientPersonNotNull(message.getControlActProcess());

        assertNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAdministrativeGenderCode());
    }

    public static void assertGenderNull(PRPAIN201302UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientPersonNotNull(message.getControlActProcess());

        assertNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAdministrativeGenderCode());
    }

    public static void assertGenderNull(PRPAIN201305UV message) {
        assertNotNull(message.getControlActProcess());
        assertParamListNotNull(message.getControlActProcess());

        assertEquals(0, message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectAdministrativeGender().size());
    }

    public static void assertBirthTimeEquals(String birthTime, PRPAIN201301UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientPersonNotNull(message.getControlActProcess());

        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getBirthTime());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getBirthTime().getValue());
        assertEquals(birthTime, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getBirthTime().getValue());
    }

    public static void assertBirthTimeEquals(String birthTime, PRPAIN201302UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientPersonNotNull(message.getControlActProcess());

        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getBirthTime());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getBirthTime().getValue());
        assertEquals(birthTime, message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getBirthTime().getValue());
    }

    public static void assertBirthTimeEquals(String birthTime, PRPAIN201305UV message) {
        assertNotNull(message.getControlActProcess());
        assertParamListNotNull(message.getControlActProcess());

        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectBirthTime());
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectBirthTime().get(0));
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectBirthTime().get(0).getValue());
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectBirthTime().get(0).getValue().get(0));
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectBirthTime().get(0).getValue().get(0).getValue());
        assertEquals(birthTime, message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectBirthTime().get(0).getValue().get(0).getValue());
    }

    public static void assertBirthTimeNull(PRPAIN201301UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientPersonNotNull(message.getControlActProcess());

        assertNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getBirthTime());
    }

    public static void assertBirthTimeNull(PRPAIN201302UV message) {
        assertNotNull(message.getControlActProcess());
        assertPatientPersonNotNull(message.getControlActProcess());

        assertNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getBirthTime());
    }

    public static void assertBirthTimeNull(PRPAIN201305UV message) {
        assertNotNull(message.getControlActProcess());
        assertParamListNotNull(message.getControlActProcess());

        assertEquals(0, message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectBirthTime().size());
    }

    public static void assertPatientNotNull(PRPAIN201301UVMFMIMT700701UV01ControlActProcess controlActProcess) {
        assertNotNull(controlActProcess.getSubject());
        assertNotNull(controlActProcess.getSubject().get(0));
        assertNotNull(controlActProcess.getSubject().get(0).getRegistrationEvent());
        assertNotNull(controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1());
        assertNotNull(controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient());
        assertNotNull(controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId());
        assertNotNull(controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0));
        assertNotNull(controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
    }

    public static void assertPatientNotNull(PRPAIN201302UVMFMIMT700701UV01ControlActProcess controlActProcess) {
        assertNotNull(controlActProcess.getSubject());
        assertNotNull(controlActProcess.getSubject().get(0));
        assertNotNull(controlActProcess.getSubject().get(0).getRegistrationEvent());
        assertNotNull(controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1());
        assertNotNull(controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient());
        assertNotNull(controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId());
        assertNotNull(controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0));
        assertNotNull(controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
    }

    public static void assertPatientPersonNotNull(PRPAIN201301UVMFMIMT700701UV01ControlActProcess controlActProcess) {
        assertPatientNotNull(controlActProcess);
        assertNotNull(controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson());
        assertNotNull(controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue());
    }

    public static void assertPatientPersonNotNull(PRPAIN201302UVMFMIMT700701UV01ControlActProcess controlActProcess) {
        assertPatientNotNull(controlActProcess);
        assertNotNull(controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson());
        assertNotNull(controlActProcess.getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue());
    }

    public static void assertParamListNotNull(PRPAIN201305UVQUQIMT021001UV01ControlActProcess controlActProcess) {
        assertNotNull(controlActProcess.getQueryByParameter());
        assertNotNull(controlActProcess.getQueryByParameter().getValue());
        assertNotNull(controlActProcess.getQueryByParameter().getValue().getParameterList());
    }

    static void assertQueryParam(PRPAMT201307UVQueryByParameter queryParam, PRPAIN201310UV message) {
        assertNotNull(message.getControlActProcess());
        assertNotNull(message.getControlActProcess().getQueryAck());
        assertNotNull(message.getControlActProcess().getQueryAck().getQueryResponseCode());

        if (queryParam.getQueryId() != null) {
            String matchId = queryParam.getQueryId().getRoot();
            if (matchId != null && !matchId.isEmpty()) {
                assertEquals(queryParam.getQueryId().getRoot(), message.getControlActProcess().getQueryAck().getQueryId().getRoot());
            }
        }
    }
}
