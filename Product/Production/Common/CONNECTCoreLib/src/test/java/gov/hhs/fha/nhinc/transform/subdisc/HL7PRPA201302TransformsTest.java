package gov.hhs.fha.nhinc.transform.subdisc;

import static org.junit.Assert.assertEquals;

import javax.xml.bind.JAXBElement;

import org.hl7.v3.CE;

import org.hl7.v3.II;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.junit.Test;

/**
 * @author achidambaram
 * 
 */
public class HL7PRPA201302TransformsTest {

    @Test
    public void createPRPA201302() {
        PRPAMT201301UV02Patient patient = createPRPAMT201301UV02Patient();
        String remotePatId = "D123401";
        String remoteDeviceId = "2.16.17.19";
        String senderOID = "1.1";
        String receiverOID = "2.2";
        HL7PRPA201302Transforms transforms = new HL7PRPA201302Transforms();
        PRPAIN201302UV02 result = null;
        result = transforms.createPRPA201302(patient, remotePatId, remoteDeviceId, senderOID, receiverOID);
        assertEquals(result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getId().get(0).getExtension(), "1.16.17.19");
    }

    @Test
    public void createPRPA201302ForPRPAMT201310UV02() {
        PRPAMT201310UV02Patient patient = createPRPAMT201310UV02Patient();
        String remotePatId = "D123401";
        String remoteDeviceId = "2.16.17.19";
        String senderOID = "1.1";
        String receiverOID = "2.2";
        HL7PRPA201302Transforms transforms = new HL7PRPA201302Transforms();
        PRPAIN201302UV02 result = null;
        result = transforms.createPRPA201302(patient, remotePatId, remoteDeviceId, senderOID, receiverOID);
        assertEquals(result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getId().get(0).getExtension(), "1.16.17.19");
    }

    private PRPAMT201310UV02Patient createPRPAMT201310UV02Patient() {
        org.hl7.v3.PRPAMT201310UV02Patient patient = new PRPAMT201310UV02Patient();
        PRPAMT201310UV02Person patientPerson = new PRPAMT201310UV02Person();
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201310UV02Person> patientPersonElement = new JAXBElement<PRPAMT201310UV02Person>(xmlqname,
                PRPAMT201310UV02Person.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patient.getId().add(createII());
        patientPerson.getClassCode().add("ClassCode");

        patientPerson.setDeterminerCode("INSTANCE");
        patientPerson.setAdministrativeGenderCode(createCE());

        PNExplicit patientName = new PNExplicit();
        patientName.getContent().add(getFirstName());
        patientName.getContent().add(getLastName());
        patientName.getNullFlavor().add("NA");
        patientPerson.getName().add(patientName);
        return patient;
    }

    private PRPAMT201301UV02Patient createPRPAMT201301UV02Patient() {
        org.hl7.v3.PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        PRPAMT201301UV02Person patientPerson = new PRPAMT201301UV02Person();
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UV02Person> patientPersonElement = new JAXBElement<PRPAMT201301UV02Person>(xmlqname,
                PRPAMT201301UV02Person.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patient.getId().add(createII());
        patientPerson.getClassCode().add("ClassCode");

        patientPerson.setDeterminerCode("INSTANCE");
        patientPerson.setAdministrativeGenderCode(createCE());

        PNExplicit patientName = new PNExplicit();
        patientName.getContent().add(getFirstName());
        patientName.getContent().add(getLastName());
        patientName.getNullFlavor().add("NA");
        patientPerson.getName().add(patientName);
        return patient;
    }

    private static String getFirstName() {
        String firstName = "Gallow";
        return firstName;
    }

    private static String getLastName() {
        String lastName = "Younger";
        return lastName;
    }

    private static String getNameType() {
        String nameType = "PNExplicitNameType";
        return nameType;
    }

    private CE createCE() {
        CE ce = new CE();
        ce.setCode("CONNECT");
        return ce;
    }

    private II createII() {
        II ii = new II();
        ii.setAssigningAuthorityName("CONNECT");
        ii.setExtension("1.16.17.19");
        ii.setRoot("1.1");
        return ii;
    }

}
