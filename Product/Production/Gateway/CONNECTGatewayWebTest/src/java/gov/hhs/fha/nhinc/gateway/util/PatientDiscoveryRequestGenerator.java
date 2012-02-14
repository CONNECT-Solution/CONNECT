package gov.hhs.fha.nhinc.gateway.util;

import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;

/**
 * Generates a PD request for testing
 * 
 * @author paul.eftis
 */
public class PatientDiscoveryRequestGenerator {

    /**
     * @param patientId
     * @param aaId
     * @param first
     * @param last
     * @param gender
     * @param birthdate
     * @param ssn
     * @param senderOID
     * @param receiverOID
     * @return PRPAIN201305UV02 PD object
     */
    public static PRPAIN201305UV02 create201305(String patientId, String aaId, String fname, String lname,
            String gender, String dob, String ssn, String senderOid, String receiverOid) {

        PRPAIN201305UV02 resp = new PRPAIN201305UV02();

        String localDeviceId = senderOid;

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(fname, lname,
                gender, dob, ssn);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, patientId, localDeviceId);

        resp = HL7PRPA201305Transforms.createPRPA201305(patient, senderOid, receiverOid, localDeviceId);
        return resp;
    }

}
