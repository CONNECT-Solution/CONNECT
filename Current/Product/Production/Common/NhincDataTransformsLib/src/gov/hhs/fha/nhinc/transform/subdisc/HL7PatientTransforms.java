/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.CE;
import org.hl7.v3.II;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAMT201301UVOtherIDs;
import org.hl7.v3.PRPAMT201301UVPatient;
import org.hl7.v3.PRPAMT201301UVPerson;
import org.hl7.v3.TSExplicit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAMT201302UVOtherIDs;
import org.hl7.v3.PRPAMT201302UVPatient;
import org.hl7.v3.PRPAMT201302UVPerson;
import org.hl7.v3.PRPAMT201310UVOtherIDs;
import org.hl7.v3.PRPAMT201310UVPatient;
import org.hl7.v3.PRPAMT201310UVPerson;
import org.hl7.v3.PRPAMT201305UVPerson;
import org.hl7.v3.PRPAMT201305UVPatient;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7PatientTransforms {

    private static Log log = LogFactory.getLog(HL7PatientTransforms.class);

    public static PRPAMT201301UVPatient create201301Patient(JAXBElement<PRPAMT201301UVPerson> person, String patId) {
        return create201301Patient(person, HL7DataTransformHelper.IIFactory(patId));
    }

    public static PRPAMT201301UVPatient create201301Patient(JAXBElement<PRPAMT201301UVPerson> person, String patId, String assigningAuthority) {
        return create201301Patient(person, HL7DataTransformHelper.IIFactory(assigningAuthority, patId));
    }

    public static PRPAMT201301UVPatient create201301Patient(JAXBElement<PRPAMT201301UVPerson> person, II id) {
        PRPAMT201301UVPatient patient = new PRPAMT201301UVPatient();

        patient.setClassCode("PAT");
        patient.setStatusCode(HL7DataTransformHelper.CSFactory("active"));

        patient.getId().add(id);

        patient.setPatientPerson(person);

        patient.setProviderOrganization(null);

        return patient;
    }
    public static JAXBElement<PRPAMT201310UVPerson> create201310PatientPerson(JAXBElement<PRPAMT201301UVPerson> person201301)
    {
       //public static JAXBElement<PRPAMT201310UVPerson> create201310PatientPerson(String patFirstName, String patLastName, String gender, String birthTime, String ssn) {
       PRPAMT201301UVPerson origPerson = person201301.getValue();
       PRPAMT201310UVPerson newPerson = new PRPAMT201310UVPerson();

       TSExplicit birthTime = person201301.getValue().getBirthTime();
       CE gender = person201301.getValue().getAdministrativeGenderCode();
       PNExplicit patName = person201301.getValue().getName().get(0);
       String ssn = person201301.getValue().getAsOtherIDs().get(0).getId().get(0).getExtension();
       PRPAMT201310UVOtherIDs newIds = new PRPAMT201310UVOtherIDs();

       return create201310PatientPerson(patName, gender,birthTime,createPRPAMT201310UVOtherIDs(ssn));
       
        
    }
    public static PRPAMT201310UVPatient create201310Patient(PRPAMT201301UVPatient patient, String patientId, String orgId)
    {
        JAXBElement<PRPAMT201310UVPerson> person = create201310PatientPerson(patient.getPatientPerson());

        return create201310Patient(person, patientId, orgId);
    }

    public static PRPAMT201310UVPatient create201310Patient(JAXBElement<PRPAMT201310UVPerson> person, String patId) {
        log.debug("begin create201310Patient");
        return create201310Patient(person, HL7DataTransformHelper.IIFactory(patId));
    }

    public static PRPAMT201310UVPatient create201310Patient(JAXBElement<PRPAMT201310UVPerson> person, String patId, String assigningAuthority) {
        return create201310Patient(person, HL7DataTransformHelper.IIFactory(assigningAuthority, patId));
    }

    public static PRPAMT201310UVPatient create201310Patient(JAXBElement<PRPAMT201310UVPerson> person, II id) {
        PRPAMT201310UVPatient patient = new PRPAMT201310UVPatient();

        patient.setClassCode("PAT");
        patient.setStatusCode(HL7DataTransformHelper.CSFactory("active"));

        patient.getId().add(id);

        patient.setPatientPerson(person);

        patient.setProviderOrganization(null);

        return patient;
    }

    public static PRPAMT201302UVPatient create201302Patient(String remotePatId, JAXBElement<PRPAMT201301UVPerson> person, II localPatId) {
        return create201302Patient(HL7DataTransformHelper.IIFactory(remotePatId), person, localPatId);
    }

    public static PRPAMT201302UVPatient create201302Patient(String remotePatId, String remoteAssigningAuthority, JAXBElement<PRPAMT201301UVPerson> person, II localPatId) {
        return create201302Patient(HL7DataTransformHelper.IIFactory(remoteAssigningAuthority, remotePatId), person, localPatId);
    }

    public static PRPAMT201302UVPatient create201302Patient(II remotePatId, JAXBElement<PRPAMT201301UVPerson> person, II localPatId) {
        PRPAMT201302UVPatient patient = new PRPAMT201302UVPatient();

        patient.setClassCode("PAT");
        patient.setStatusCode(HL7DataTransformHelper.CSFactory("active"));

        patient.getId().add(localPatId);

        if (person.getValue() != null) {
            PRPAMT201301UVPerson inputPerson = person.getValue();
            JAXBElement<PRPAMT201302UVPerson> patientPerson = create201302PatientPerson(inputPerson.getName().get(0), inputPerson.getAdministrativeGenderCode(), inputPerson.getBirthTime(), inputPerson.getAsOtherIDs(), remotePatId);
            patient.setPatientPerson(patientPerson);
        }

        patient.setProviderOrganization(null);

        return patient;
    }

    public static PRPAMT201302UVPatient create201302Patient(JAXBElement<PRPAMT201310UVPerson> person, String remotePatId, II localPatId) {
        return create201302Patient(person, HL7DataTransformHelper.IIFactory(remotePatId), localPatId);
    }

    public static PRPAMT201302UVPatient create201302Patient(JAXBElement<PRPAMT201310UVPerson> person, String remotePatId, String remoteAssigningAuthority, II localPatId) {
        return create201302Patient(person, HL7DataTransformHelper.IIFactory(remoteAssigningAuthority, remotePatId), localPatId);
    }

    public static PRPAMT201302UVPatient create201302Patient(JAXBElement<PRPAMT201310UVPerson> person, II remotePatId, II localPatId) {
        PRPAMT201302UVPatient patient = new PRPAMT201302UVPatient();

        patient.setClassCode("PAT");
        patient.setStatusCode(HL7DataTransformHelper.CSFactory("active"));

        patient.getId().add(localPatId);

        if (person.getValue() != null) {
            PRPAMT201310UVPerson inputPerson = person.getValue();
            JAXBElement<PRPAMT201302UVPerson> patientPerson = create201302PatientPerson(inputPerson.getAsOtherIDs(), inputPerson.getName().get(0), inputPerson.getAdministrativeGenderCode(), inputPerson.getBirthTime(), remotePatId);
            patient.setPatientPerson(patientPerson);
        }

        patient.setProviderOrganization(null);

        return patient;
    }

    public static JAXBElement<PRPAMT201301UVPerson> create201301PatientPerson(String patFirstName, String patLastName, String gender, String birthTime, String ssn) {
        log.debug("begin create201301PatientPerson");
        PNExplicit name = null;

        log.debug("begin create Name");
        if (NullChecker.isNotNullish(patFirstName) ||
                NullChecker.isNotNullish(patLastName)) {
            log.debug("not nullish");
            name = HL7DataTransformHelper.CreatePNExplicit(patFirstName, patLastName);
        }

        log.debug("begin create gender");
        CE genderCode = null;
        if (NullChecker.isNotNullish(gender)) {
            genderCode = HL7DataTransformHelper.CEFactory(gender);
        }

        log.debug("begin create birthTime");
        TSExplicit bday = null;
        if (NullChecker.isNotNullish(birthTime)) {
            bday = HL7DataTransformHelper.TSExplicitFactory(birthTime);
        }

        log.debug("begin create otherIds");
        PRPAMT201301UVOtherIDs otherIds = null;
        if (NullChecker.isNotNullish(ssn)) {
            otherIds = createPRPAMT201301UVOtherIDs(ssn);
        }

        log.debug("end create201301PatientPerson");
        return create201301PatientPerson(name, genderCode, bday, otherIds);
    }

    public static JAXBElement<PRPAMT201301UVPerson> create201301PatientPerson(PNExplicit patName, CE gender, TSExplicit birthTime, PRPAMT201301UVOtherIDs otherIds) {
        PRPAMT201301UVPerson person = new PRPAMT201301UVPerson();

        // Set the Subject Name
        if (patName != null) {
            person.getName().add(patName);
        }

        // Set the Subject Gender
        if (gender != null) {
            person.setAdministrativeGenderCode(gender);
        }

        // Set the Birth Time
        if (birthTime != null) {
            person.setBirthTime(birthTime);
        }

        // Set the SSN
        if (otherIds != null) {
            person.getAsOtherIDs().add(otherIds);
        }

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UVPerson> result = new JAXBElement<PRPAMT201301UVPerson>(xmlqname, PRPAMT201301UVPerson.class, person);

        return result;
    }

    public static JAXBElement<PRPAMT201310UVPerson> create201310PatientPerson(String patFirstName, String patLastName, String gender, String birthTime, String ssn) {
        PNExplicit name = null;
        if (NullChecker.isNotNullish(patFirstName) &&
                NullChecker.isNotNullish(patLastName)) {
            name = HL7DataTransformHelper.CreatePNExplicit(patFirstName, patLastName);
        }

        CE genderCode = null;
        if (NullChecker.isNotNullish(gender)) {
            genderCode = HL7DataTransformHelper.CEFactory(gender);
        }

        TSExplicit bday = null;
        if (NullChecker.isNotNullish(birthTime)) {
            bday = HL7DataTransformHelper.TSExplicitFactory(birthTime);
        }

        PRPAMT201310UVOtherIDs otherIds = null;
        if (NullChecker.isNotNullish(ssn)) {
            otherIds = createPRPAMT201310UVOtherIDs(ssn);
        }

        return create201310PatientPerson(name, genderCode, bday, otherIds);
    }

    public static JAXBElement<PRPAMT201310UVPerson> create201310PatientPerson(PNExplicit patName, CE gender, TSExplicit birthTime, PRPAMT201310UVOtherIDs otherIds) {
        PRPAMT201310UVPerson person = new PRPAMT201310UVPerson();

        // Set the Subject Name
        if (patName != null) {
            person.getName().add(patName);
        }

        // Set the Subject Gender
        if (gender != null) {
            person.setAdministrativeGenderCode(gender);
        }

        // Set the Birth Time
        if (birthTime != null) {
            person.setBirthTime(birthTime);
        }

        // Set the SSN
        if (otherIds != null) {
            person.getAsOtherIDs().add(otherIds);
        }

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201310UVPerson> result = new JAXBElement<PRPAMT201310UVPerson>(xmlqname, PRPAMT201310UVPerson.class, person);

        return result;
    }

    public static JAXBElement<PRPAMT201302UVPerson> create201302PatientPerson(String patFirstName, String patLastName, String gender, String birthTime, String ssn, II remotePatId) {
        PNExplicit name = null;
        if (NullChecker.isNotNullish(patFirstName) &&
                NullChecker.isNotNullish(patLastName)) {
            name = HL7DataTransformHelper.CreatePNExplicit(patFirstName, patLastName);
        }

        CE genderCode = null;
        if (NullChecker.isNotNullish(gender)) {
            genderCode = HL7DataTransformHelper.CEFactory(gender);
        }

        TSExplicit bday = null;
        if (NullChecker.isNotNullish(birthTime)) {
            bday = HL7DataTransformHelper.TSExplicitFactory(birthTime);
        }

        PRPAMT201302UVOtherIDs otherIds = null;
        if (NullChecker.isNotNullish(ssn) ||
                (remotePatId != null &&
                NullChecker.isNotNullish(remotePatId.getRoot()) &&
                NullChecker.isNotNullish(remotePatId.getExtension()))) {
            otherIds = createPRPAMT201302UVOtherIDs(ssn, remotePatId);
        }

        return create201302PatientPerson(name, genderCode, bday, otherIds);
    }

    public static JAXBElement<PRPAMT201302UVPerson> create201302PatientPerson(PNExplicit patName, CE gender, TSExplicit birthTime, List<PRPAMT201301UVOtherIDs> otherIds, II remotePatId) {
        PRPAMT201302UVOtherIDs convertedOtherIds = null;
        if (otherIds != null &&
                otherIds.size() > 0 &&
                otherIds.get(0) != null &&
                otherIds.get(0).getId() != null &&
                otherIds.get(0).getId().size() > 0 &&
                otherIds.get(0).getId().get(0) != null &&
                NullChecker.isNotNullish(otherIds.get(0).getId().get(0).getExtension())){
            convertedOtherIds = createPRPAMT201302UVOtherIDs(otherIds.get(0).getId().get(0).getExtension(), remotePatId);
        }
        else if (remotePatId != null &&
                NullChecker.isNotNullish(remotePatId.getRoot()) &&
                NullChecker.isNotNullish(remotePatId.getExtension())) {
            convertedOtherIds = createPRPAMT201302UVOtherIDs(null, remotePatId);
        }

        return create201302PatientPerson(patName, gender, birthTime, convertedOtherIds);
    }

    public static JAXBElement<PRPAMT201302UVPerson> create201302PatientPerson(List<PRPAMT201310UVOtherIDs> otherIds, PNExplicit patName, CE gender, TSExplicit birthTime, II remotePatId) {
        PRPAMT201302UVOtherIDs convertedOtherIds = null;
        if (otherIds != null &&
                otherIds.size() > 0 &&
                otherIds.get(0) != null &&
                otherIds.get(0).getId() != null &&
                otherIds.get(0).getId().size() > 0 &&
                otherIds.get(0).getId().get(0) != null &&
                NullChecker.isNotNullish(otherIds.get(0).getId().get(0).getExtension())) {
            convertedOtherIds = createPRPAMT201302UVOtherIDs(otherIds.get(0).getId().get(0).getExtension(), remotePatId);
        }
        else if (remotePatId != null &&
                NullChecker.isNotNullish(remotePatId.getRoot()) &&
                NullChecker.isNotNullish(remotePatId.getExtension())) {
            convertedOtherIds = createPRPAMT201302UVOtherIDs(null, remotePatId);
        }

        return create201302PatientPerson(patName, gender, birthTime, convertedOtherIds);
    }

    public static JAXBElement<PRPAMT201302UVPerson> create201302PatientPerson(PNExplicit patName, CE gender, TSExplicit birthTime, PRPAMT201302UVOtherIDs otherIds) {
        PRPAMT201302UVPerson person = new PRPAMT201302UVPerson();

        // Set the Subject Name
        if (patName != null) {
            person.getName().add(patName);
        }

        // Set the Subject Gender
        if (gender != null) {
            person.setAdministrativeGenderCode(gender);
        }

        // Set the Birth Time
        if (birthTime != null) {
            person.setBirthTime(birthTime);
        }

        // Set the SSN
        if (otherIds != null) {
            person.getAsOtherIDs().add(otherIds);
        }

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201302UVPerson> result = new JAXBElement<PRPAMT201302UVPerson>(xmlqname, PRPAMT201302UVPerson.class, person);

        return result;
    }
    public static PRPAMT201305UVPatient create201305Patient(JAXBElement<PRPAMT201305UVPerson> person, II id) {
        PRPAMT201305UVPatient patient = new PRPAMT201305UVPatient();

        patient.setClassCode("PAT");
        patient.setStatusCode(HL7DataTransformHelper.CSFactory("active"));

        patient.getId().add(id);

        patient.setPatientPerson(person);

        patient.setProviderOrganization(null);

        return patient;
    }

    public static JAXBElement<PRPAMT201305UVPerson> create201305PatientPerson(String patFirstName, String patLastName, String gender, String birthTime, String ssn, II inputII) {
        PNExplicit name = null;
        if (NullChecker.isNotNullish(patFirstName) &&
                NullChecker.isNotNullish(patLastName)) {
            name = HL7DataTransformHelper.CreatePNExplicit(patFirstName, patLastName);
        }

        CE genderCode = null;
        if (NullChecker.isNotNullish(gender)) {
            genderCode = HL7DataTransformHelper.CEFactory(gender);
        }

        TSExplicit bday = null;
        if (NullChecker.isNotNullish(birthTime)) {
            bday = HL7DataTransformHelper.TSExplicitFactory(birthTime);
        }
log.debug("= = = = = =  inputII : " + inputII.getRoot() + " " + inputII.getExtension());
        return create201305PatientPerson(name, genderCode, bday, inputII);
    }
   
    public static JAXBElement<PRPAMT201305UVPerson> create201305PatientPerson(PNExplicit patName, CE gender, TSExplicit birthTime, II otherId) {
        PRPAMT201305UVPerson person = new PRPAMT201305UVPerson();

        // Set the Subject Name
        if (patName != null) {
            person.getName().add(patName);
        }

        // Set the Subject Gender
        if (gender != null) {
            person.setAdministrativeGenderCode(gender);
        }

        // Set the Birth Time
        if (birthTime != null) {
            person.setBirthTime(birthTime);
        }

        // Set the II
        if (otherId != null) {
            person.getId().add(otherId);
            log.debug("= = = = = = = setting person.getId to " + otherId.getExtension() + " " + otherId.getRoot());
        }

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201305UVPerson> result = new JAXBElement<PRPAMT201305UVPerson>(xmlqname, PRPAMT201305UVPerson.class, person);

        return result;
    }

    public static PRPAMT201301UVOtherIDs createPRPAMT201301UVOtherIDs(String ssn) {
        PRPAMT201301UVOtherIDs otherIds = new PRPAMT201301UVOtherIDs();

        otherIds.setClassCode("PAT");

        // Set the SSN
        if (NullChecker.isNotNullish(ssn)) {
            log.info("Setting Patient SSN: " + ssn);
            otherIds.getId().add(HL7DataTransformHelper.IIFactory(HL7Constants.SSN_ID_ROOT, ssn));
        }

        return otherIds;
    }

    public static PRPAMT201310UVOtherIDs createPRPAMT201310UVOtherIDs(String ssn) {
        PRPAMT201310UVOtherIDs otherIds = new PRPAMT201310UVOtherIDs();

        otherIds.setClassCode("PAT");

        // Set the SSN
        if (NullChecker.isNotNullish(ssn)) {
            log.info("Setting Patient SSN: " + ssn);
            otherIds.getId().add(HL7DataTransformHelper.IIFactory(HL7Constants.SSN_ID_ROOT, ssn));
        }

        return otherIds;
    }

    public static PRPAMT201302UVOtherIDs createPRPAMT201302UVOtherIDs(String ssn, II remotePatId) {
        PRPAMT201302UVOtherIDs otherIds = new PRPAMT201302UVOtherIDs();

        otherIds.setClassCode("PAT");

        // Set the SSN
        if (NullChecker.isNotNullish(ssn)) {
            log.info("Setting Patient SSN: " + ssn);
            otherIds.getId().add(HL7DataTransformHelper.IIFactory(HL7Constants.SSN_ID_ROOT, ssn));
        }

        if (remotePatId != null &&
                NullChecker.isNotNullish(remotePatId.getRoot()) &&
                NullChecker.isNotNullish(remotePatId.getExtension())) {
            log.info("Setting Remote Patient Id: " + remotePatId.getExtension());
            log.info("Setting Remote Assigning Authority: " + remotePatId.getRoot());
            otherIds.getId().add(remotePatId);
        }

        return otherIds;
    }

}
