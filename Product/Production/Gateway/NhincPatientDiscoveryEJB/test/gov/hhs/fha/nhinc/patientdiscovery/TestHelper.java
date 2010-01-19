/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.II;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import static org.junit.Assert.*;

/**
 *
 * @author jhoppesc
 */
public class TestHelper {

    public static PRPAIN201306UV02 create201306(String senderOID, String receiverOID, PRPAIN201305UV02 query) {
        PRPAIN201306UV02 resp = null;

        if (query != null &&
                query.getControlActProcess() != null &&
                query.getControlActProcess().getQueryByParameter() != null &&
                query.getControlActProcess().getQueryByParameter().getValue() != null &&
                query.getControlActProcess().getQueryByParameter().getValue().getParameterList() != null) {
            PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(query.getControlActProcess().getQueryByParameter().getValue().getParameterList());

            if (patient != null) {
                resp = HL7PRPA201306Transforms.createPRPA201306(patient, senderOID, receiverOID, receiverOID, receiverOID, query);
            }
        }
        return resp;
    }

    public static PRPAIN201305UV02 create201305(String first, String last, String gender, String birthdate, String ssn, String senderOID, String receiverOID) {
        PRPAIN201305UV02 resp = new PRPAIN201305UV02();
        PRPAMT201301UV02Patient patient = createPatient(first, last, gender, birthdate, ssn);

        resp = HL7PRPA201305Transforms.createPRPA201305(patient, senderOID, receiverOID, senderOID);
        return resp;
    }

    public static void assertPatientMatch(PRPAIN201305UV02 query, PRPAIN201306UV02 result) {
        // Check that the messages are not null
        assertNotNull(query);
        assertNotNull(result);

        // Check that the control act processes are not null
        assertNotNull(query.getControlActProcess());
        assertNotNull(result.getControlActProcess());

        // Check that the names are not null
        assertNotNull(query.getControlActProcess().getQueryByParameter());
        assertNotNull(query.getControlActProcess().getQueryByParameter().getValue());
        assertNotNull(query.getControlActProcess().getQueryByParameter().getValue().getParameterList());
        assertNotNull(query.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectName());
        assert201305NameNotNull(query.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectName());

        assertNotNull(result.getControlActProcess().getSubject());
        assertNotNull(result.getControlActProcess().getSubject().get(0));
        assertNotNull(result.getControlActProcess().getSubject().get(0).getRegistrationEvent());
        assertNotNull(result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1());
        assertNotNull(result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient());
        assertNotNull(result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson());
        assertNotNull(result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue());
        assertNotNull(result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName());
        assert201306NameNotNull(result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName());

        assertNamesMatch(query.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectName(), result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName());
    }

    public static void assertNamesMatch (List<PRPAMT201306UV02LivingSubjectName> name1, List<PNExplicit> name2) {
        assert201305NameNotNull(name1);
        assert201306NameNotNull(name2);

        String name1First = getFirstName(name1.get(0).getValue().get(0).getContent().iterator());
        String name1Last = getLastName(name1.get(0).getValue().get(0).getContent().iterator());
        String name2First = getFirstName(name2.get(0).getContent().iterator());
        String name2Last = getLastName(name2.get(0).getContent().iterator());

        assertEquals(name1First, name2First);
        assertEquals(name1Last, name2Last);
    }

    private static void assert201305NameNotNull(List<PRPAMT201306UV02LivingSubjectName> name) {
        assertNotNull(name.get(0));
        assertNotNull(name.get(0).getValue());
        assertNotNull(name.get(0).getValue().get(0));
        assertNotNull(name.get(0).getValue().get(0).getContent());
        assertNotNull(name.get(0).getValue().get(0).getContent().iterator());
        assertNotNull(getFirstName(name.get(0).getValue().get(0).getContent().iterator()));
        assertNotNull(getLastName(name.get(0).getValue().get(0).getContent().iterator()));
    }

    private static void assert201306NameNotNull(List<PNExplicit> name) {
        assertNotNull(name);
        assertNotNull(name.get(0));
        assertNotNull(name.get(0).getContent());
        assertNotNull(name.get(0).getContent().iterator());
        assertNotNull(getFirstName(name.get(0).getContent().iterator()));
        assertNotNull(getLastName(name.get(0).getContent().iterator()));

    }

    public static String getFirstName(Iterator<Serializable> iterSerialObjects) {
        EnExplicitGiven first = null;

        while (iterSerialObjects.hasNext()) {
            Serializable contentItem = iterSerialObjects.next();

            if (contentItem instanceof JAXBElement) {
                JAXBElement oJAXBElement = (JAXBElement) contentItem;

                if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    first = (EnExplicitGiven) oJAXBElement.getValue();
                }
            }
        }

        if (NullChecker.isNullish(first.getContent())) {
            System.out.println("Returning Null for first name");
            return null;
        }
        return first.getContent();
    }

    public static String getLastName(Iterator<Serializable> iterSerialObjects) {
        EnExplicitFamily last = null;

        while (iterSerialObjects.hasNext()) {
            Serializable contentItem = iterSerialObjects.next();

            if (contentItem instanceof JAXBElement) {
                JAXBElement oJAXBElement = (JAXBElement) contentItem;

                if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                    last = (EnExplicitFamily) oJAXBElement.getValue();
                }
            }
        }

        if (NullChecker.isNullish(last.getContent())) {
            System.out.println("Returning Null for last name");
            return null;
        }

        return last.getContent();
    }

    private static PRPAMT201301UV02Patient createPatient(String first, String last, String gender, String birthdate, String ssn) {
        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(first, last, gender, birthdate, ssn);
        II id = new II();

        patient = HL7PatientTransforms.create201301Patient(person, id);

        return patient;
    }
}
