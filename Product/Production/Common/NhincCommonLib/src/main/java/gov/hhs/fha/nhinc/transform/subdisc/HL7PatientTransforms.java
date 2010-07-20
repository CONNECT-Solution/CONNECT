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
import org.hl7.v3.ADExplicit;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.PRPAMT201301UV02OtherIDs;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.TSExplicit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAMT201302UV02OtherIDs;
import org.hl7.v3.PRPAMT201302UV02OtherIDsId;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201302UV02PatientId;
import org.hl7.v3.PRPAMT201302UV02PatientPatientPerson;
import org.hl7.v3.PRPAMT201302UV02PatientStatusCode;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201302UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201301UV02BirthPlace;
import org.hl7.v3.PRPAMT201302UV02BirthPlace;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02PatientAddress;
import org.hl7.v3.PRPAMT201306UV02PatientTelecom;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201310UV02BirthPlace;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7PatientTransforms {

    private static Log log = LogFactory.getLog(HL7PatientTransforms.class);

    public static org.hl7.v3.PRPAMT201301UV02Patient create201301Patient(PRPAMT201306UV02ParameterList paramList) {
        PRPAMT201301UV02Patient result = new PRPAMT201301UV02Patient();

        PRPAMT201301UV02Person person = new PRPAMT201301UV02Person();

        if (paramList == null) {
            return null;
        }
        // Set the Subject Gender Code
        if (paramList.getLivingSubjectAdministrativeGender() != null &&
                paramList.getLivingSubjectAdministrativeGender().size() > 0) {
            CE genderCode = paramList.getLivingSubjectAdministrativeGender().get(0).getValue().get(0);

            person.setAdministrativeGenderCode(genderCode);
        }

        // Set the Subject Birth Time
        if (paramList.getLivingSubjectBirthTime() != null && paramList.getLivingSubjectBirthTime().size() > 0) {

            person.setBirthTime(createBirthTime(paramList.getLivingSubjectBirthTime().get(0)));
        }
        // Set the address
        if (paramList.getPatientAddress() != null &&
                paramList.getPatientAddress().size() > 0) {
            for (PRPAMT201306UV02PatientAddress patAdd : paramList.getPatientAddress()) {
                for (ADExplicit newAdd : patAdd.getValue()) {
                    person.getAddr().add(newAdd);
                }
            }
        }
        //Set the telcom
        if (paramList.getPatientTelecom() != null && paramList.getPatientTelecom().size() > 0) {
            for (PRPAMT201306UV02PatientTelecom telcom : paramList.getPatientTelecom()) {
                if (telcom != null) {
                    for (TELExplicit newTelcom : telcom.getValue()) {
                        person.getTelecom().add(newTelcom);
                    }
                }

            }
        }

        // Set the Subject Name
        if (paramList.getLivingSubjectName() != null &&
                paramList.getLivingSubjectName().size() > 0) {
            for (PRPAMT201306UV02LivingSubjectName subjName : paramList.getLivingSubjectName()) {

                for (ENExplicit name : subjName.getValue()) {
                    PNExplicit newName = HL7DataTransformHelper.convertENtoPN(name);
                    newName = HL7ArrayTransforms.copyNullFlavors(name, newName);
                    person.getName().add(newName);
                }
            }
            //paramList.getLivingSubjectName().get(index)
        }

        // Set the subject Id
        if (paramList.getLivingSubjectId() != null &&
                paramList.getLivingSubjectId().size() > 0) {
            for (PRPAMT201306UV02LivingSubjectId subjId : paramList.getLivingSubjectId()) {
                for (II id : subjId.getValue()) {
                    result.getId().add(id);
                }
            }
        }

        person = HL7ArrayTransforms.copyNullFlavors(paramList, person);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UV02Person> jaxbPerson = new JAXBElement<PRPAMT201301UV02Person>(xmlqname, PRPAMT201301UV02Person.class, person);

        jaxbPerson.setValue(person);

        result.setPatientPerson(jaxbPerson);

        return result;
    }

    public static org.hl7.v3.PRPAMT201301UV02Patient create201301Patient(PRPAMT201306UV02ParameterList paramList, String aaId) {
        PRPAMT201301UV02Patient result = new PRPAMT201301UV02Patient();

        PRPAMT201301UV02Person person = new PRPAMT201301UV02Person();

        if (paramList == null) {
            return null;
        }
        // Set the Subject Gender Code
        if (paramList.getLivingSubjectAdministrativeGender() != null &&
                paramList.getLivingSubjectAdministrativeGender().size() > 0) {
            CE genderCode = paramList.getLivingSubjectAdministrativeGender().get(0).getValue().get(0);

            person.setAdministrativeGenderCode(genderCode);
        }

        // Set the Subject Birth Time
        if (paramList.getLivingSubjectBirthTime() != null && paramList.getLivingSubjectBirthTime().size() > 0) {

            person.setBirthTime(createBirthTime(paramList.getLivingSubjectBirthTime().get(0)));
        }
        // Set the address
        if (paramList.getPatientAddress() != null &&
                paramList.getPatientAddress().size() > 0) {
            for (PRPAMT201306UV02PatientAddress patAdd : paramList.getPatientAddress()) {
                for (ADExplicit newAdd : patAdd.getValue()) {
                    person.getAddr().add(newAdd);
                }
            }
        }
        //Set the telcom
        if (paramList.getPatientTelecom() != null && paramList.getPatientTelecom().size() > 0) {
            for (PRPAMT201306UV02PatientTelecom telcom : paramList.getPatientTelecom()) {
                if (telcom != null) {
                    for (TELExplicit newTelcom : telcom.getValue()) {
                        person.getTelecom().add(newTelcom);
                    }
                }

            }
        }

        // Set the Subject Name
        if (paramList.getLivingSubjectName() != null &&
                paramList.getLivingSubjectName().size() > 0) {
            for (PRPAMT201306UV02LivingSubjectName subjName : paramList.getLivingSubjectName()) {

                for (ENExplicit name : subjName.getValue()) {
                    PNExplicit newName = HL7DataTransformHelper.convertENtoPN(name);
                    newName = HL7ArrayTransforms.copyNullFlavors(name, newName);
                    person.getName().add(newName);
                }
            }
            //paramList.getLivingSubjectName().get(index)
        }

        // Set the subject Id
        if (paramList.getLivingSubjectId() != null &&
                paramList.getLivingSubjectId().size() > 0) {
            for (PRPAMT201306UV02LivingSubjectId subjId : paramList.getLivingSubjectId()) {
                for (II id : subjId.getValue()) {
                    if (id.getRoot().equalsIgnoreCase(aaId)) {
                        result.getId().add(id);
                    }
                    else {
                        PRPAMT201301UV02OtherIDs otherId = new PRPAMT201301UV02OtherIDs();
                        otherId.getId().add(id);
                        person.getAsOtherIDs().add(otherId);
                    }
                }
            }
        }

        person = HL7ArrayTransforms.copyNullFlavors(paramList, person);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UV02Person> jaxbPerson = new JAXBElement<PRPAMT201301UV02Person>(xmlqname, PRPAMT201301UV02Person.class, person);

        jaxbPerson.setValue(person);

        result.setPatientPerson(jaxbPerson);

        return result;
    }

    public static PRPAMT201301UV02Patient create201301Patient(JAXBElement<PRPAMT201301UV02Person> person, String patId) {
        return create201301Patient(person, HL7DataTransformHelper.IIFactory(patId));
    }

    public static PRPAMT201301UV02Patient createPRPAMT201301UVPatient(PRPAMT201310UV02Patient patient) {
        PRPAMT201301UV02Patient result = new PRPAMT201301UV02Patient();

        if (patient == null) {
            return null;
        }


        result.setEffectiveTime(patient.getEffectiveTime());
        if (patient.getProviderOrganization() != null) {
            result.setProviderOrganization(patient.getProviderOrganization().getValue());
        }

        result.setStatusCode(patient.getStatusCode());
        result.setTypeId(patient.getTypeId());
        result.setVeryImportantPersonCode(patient.getVeryImportantPersonCode());

        for (ADExplicit address : patient.getAddr()) {
            result.getAddr().add(address);
        }
        for (TELExplicit telephone : patient.getTelecom()) {
            result.getTelecom().add(telephone);
        }

        for (CE code : patient.getConfidentialityCode()) {
            result.getConfidentialityCode().add(code);
        }

        result.setPatientPerson(create201301PatientPerson(patient.getPatientPerson().getValue()));
        result = HL7ArrayTransforms.copyIIs(patient, result);

        return result;
    }

    public static PRPAMT201301UV02Patient createPRPAMT201301UVPatient(PRPAMT201302UV02Patient patient) {
        PRPAMT201301UV02Patient result = new PRPAMT201301UV02Patient();

        if (patient == null) {
            return null;
        }

        result.setEffectiveTime(patient.getEffectiveTime());
        if (patient.getProviderOrganization() != null) {
            result.setProviderOrganization(patient.getProviderOrganization().getValue());
        }

        result.setStatusCode(patient.getStatusCode());
        result.setTypeId(patient.getTypeId());
        result.setVeryImportantPersonCode(patient.getVeryImportantPersonCode());

        for (ADExplicit address : patient.getAddr()) {
            result.getAddr().add(address);
        }
        for (TELExplicit telephone : patient.getTelecom()) {
            result.getTelecom().add(telephone);
        }

        for (CE code : patient.getConfidentialityCode()) {
            result.getConfidentialityCode().add(code);
        }


        result.setPatientPerson(create201301PatientPerson(patient.getPatientPerson()));
        return result;
    }

    public static PRPAMT201301UV02Patient create201301Patient(JAXBElement<PRPAMT201301UV02Person> person, String patId, String assigningAuthority) {
        return create201301Patient(person, HL7DataTransformHelper.IIFactory(assigningAuthority, patId));
    }

    public static PRPAMT201301UV02Patient create201301Patient(JAXBElement<PRPAMT201301UV02Person> person, II id) {
        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();

        patient.getClassCode().add("PAT");
        patient.setStatusCode(HL7DataTransformHelper.CSFactory("active"));

        patient.getId().add(id);

        patient.setPatientPerson(person);

        patient.setProviderOrganization(null);

        return patient;
    }

    public static JAXBElement<PRPAMT201310UV02Person> create201310PatientPerson(JAXBElement<PRPAMT201301UV02Person> person201301) {
        //public static JAXBElement<PRPAMT201310UVPerson> create201310PatientPerson(String patFirstName, String patLastName, String gender, String birthTime, String ssn) {
        PRPAMT201301UV02Person origPerson = person201301.getValue();
        PRPAMT201310UV02Person newPerson = new PRPAMT201310UV02Person();

        TSExplicit birthTime = person201301.getValue().getBirthTime();
        CE gender = person201301.getValue().getAdministrativeGenderCode();
        PNExplicit patName = person201301.getValue().getName().get(0);
        String ssn = null;
        if (person201301 != null &&
                person201301.getValue() != null &&
                NullChecker.isNotNullish(person201301.getValue().getAsOtherIDs()) &&
                person201301.getValue().getAsOtherIDs().get(0) != null &&
                NullChecker.isNotNullish(person201301.getValue().getAsOtherIDs().get(0).getId()) &&
                person201301.getValue().getAsOtherIDs().get(0).getId().get(0) != null &&
                NullChecker.isNotNullish(person201301.getValue().getAsOtherIDs().get(0).getId().get(0).getExtension())) {
            ssn = person201301.getValue().getAsOtherIDs().get(0).getId().get(0).getExtension();
        }
        PRPAMT201310UV02OtherIDs newIds = new PRPAMT201310UV02OtherIDs();

        return create201310PatientPerson(patName, gender, birthTime, createPRPAMT201310UVOtherIDs(ssn));


    }

    public static PRPAMT201310UV02Patient create201310Patient(PRPAMT201301UV02Patient patient, String patientId, String orgId) {
        JAXBElement<PRPAMT201310UV02Person> person = create201310PatientPerson(patient.getPatientPerson());

        return create201310Patient(person, patientId, orgId);
    }

    public static PRPAMT201310UV02Patient create201310Patient(JAXBElement<PRPAMT201310UV02Person> person, String patId) {
        log.debug("begin create201310Patient");
        return create201310Patient(person, HL7DataTransformHelper.IIFactory(patId));
    }

    public static PRPAMT201310UV02Patient create201310Patient(JAXBElement<PRPAMT201310UV02Person> person, String patId, String assigningAuthority) {
        return create201310Patient(person, HL7DataTransformHelper.IIFactory(assigningAuthority, patId));
    }

    public static PRPAMT201310UV02Patient create201310Patient(JAXBElement<PRPAMT201310UV02Person> person, II id) {
        PRPAMT201310UV02Patient patient = new PRPAMT201310UV02Patient();

        patient.getClassCode().add("PAT");
        patient.setStatusCode(HL7DataTransformHelper.CSFactory("active"));

        patient.getId().add(id);

        patient.setPatientPerson(person);

        patient.setProviderOrganization(null);

        return patient;
    }

    public static PRPAMT201302UV02Patient create201302Patient(String remotePatId, JAXBElement<PRPAMT201301UV02Person> person, II localPatId) {
        return create201302Patient(HL7DataTransformHelper.IIFactory(remotePatId), person, localPatId);
    }

    public static PRPAMT201302UV02Patient create201302Patient(String remotePatId, String remoteAssigningAuthority, JAXBElement<PRPAMT201301UV02Person> person, II localPatId) {
        return create201302Patient(HL7DataTransformHelper.IIFactory(remoteAssigningAuthority, remotePatId), person, localPatId);
    }

    public static PRPAMT201302UV02Patient create201302Patient(II remotePatId, JAXBElement<PRPAMT201301UV02Person> person, II localPatId) {
        PRPAMT201302UV02Patient patient = new PRPAMT201302UV02Patient();

        patient.getClassCode().add("PAT");

        PRPAMT201302UV02PatientStatusCode statusCode = new PRPAMT201302UV02PatientStatusCode();
        statusCode.setCode("active");
        patient.setStatusCode(statusCode);
        PRPAMT201302UV02PatientId patId = new PRPAMT201302UV02PatientId();
        patId.setExtension(localPatId.getExtension());
        patId.setRoot(localPatId.getRoot());
        patient.getId().add(patId);

        if (person.getValue() != null) {
            PRPAMT201301UV02Person inputPerson = person.getValue();
            JAXBElement<PRPAMT201302UV02PatientPatientPerson> patientPerson = create201302PatientPerson(inputPerson.getName().get(0), inputPerson.getAdministrativeGenderCode(), inputPerson.getBirthTime(), inputPerson.getAsOtherIDs(), remotePatId);
            patient.setPatientPerson(patientPerson);
        }

        patient.setProviderOrganization(null);

        return patient;
    }

    public static PRPAMT201302UV02Patient create201302Patient(JAXBElement<PRPAMT201310UV02Person> person, String remotePatId, II localPatId) {
        return create201302Patient(person, HL7DataTransformHelper.IIFactory(remotePatId), localPatId);
    }

    public static PRPAMT201302UV02Patient create201302Patient(JAXBElement<PRPAMT201310UV02Person> person, String remotePatId, String remoteAssigningAuthority, II localPatId) {
        return create201302Patient(person, HL7DataTransformHelper.IIFactory(remoteAssigningAuthority, remotePatId), localPatId);
    }

    public static PRPAMT201302UV02Patient create201302Patient(JAXBElement<PRPAMT201310UV02Person> person, II remotePatId, II localPatId) {
        PRPAMT201302UV02Patient patient = new PRPAMT201302UV02Patient();

        patient.getClassCode().add("PAT");
        PRPAMT201302UV02PatientStatusCode statusCode = new PRPAMT201302UV02PatientStatusCode();
        statusCode.setCode("active");
        patient.setStatusCode(statusCode);

        PRPAMT201302UV02PatientId patId = new PRPAMT201302UV02PatientId();
        patId.setExtension(localPatId.getExtension());
        patId.setRoot(localPatId.getRoot());
        patient.getId().add(patId);

        if (person.getValue() != null) {
            PRPAMT201310UV02Person inputPerson = person.getValue();
            JAXBElement<PRPAMT201302UV02PatientPatientPerson> patientPerson = create201302PatientPerson(inputPerson.getAsOtherIDs(), inputPerson.getName().get(0), inputPerson.getAdministrativeGenderCode(), inputPerson.getBirthTime(), remotePatId);
            patient.setPatientPerson(patientPerson);
        }

        patient.setProviderOrganization(null);

        return patient;
    }

    public static JAXBElement<PRPAMT201301UV02Person> create201301PatientPerson(String patFirstName, String patLastName, String gender, String birthTime, String ssn) {
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
        PRPAMT201301UV02OtherIDs otherIds = null;
        if (NullChecker.isNotNullish(ssn)) {
            otherIds = createPRPAMT201301UVOtherIDs(ssn);
        }

        log.debug("end create201301PatientPerson");
        return create201301PatientPerson(name, genderCode, bday, otherIds);
    }

    public static JAXBElement<PRPAMT201301UV02Person> create201301PatientPerson(PNExplicit patName, CE gender, TSExplicit birthTime, PRPAMT201301UV02OtherIDs otherIds) {
        PRPAMT201301UV02Person person = new PRPAMT201301UV02Person();

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
        JAXBElement<PRPAMT201301UV02Person> result = new JAXBElement<PRPAMT201301UV02Person>(xmlqname, PRPAMT201301UV02Person.class, person);

        return result;
    }

    public static JAXBElement<PRPAMT201310UV02Person> create201310PatientPerson(String patFirstName, String patLastName, String gender, String birthTime, String ssn) {
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

        PRPAMT201310UV02OtherIDs otherIds = null;
        if (NullChecker.isNotNullish(ssn)) {
            otherIds = createPRPAMT201310UVOtherIDs(ssn);
        }

        return create201310PatientPerson(name, genderCode, bday, otherIds);
    }

    public static JAXBElement<PRPAMT201310UV02Person> create201310PatientPerson(PNExplicit patName, CE gender, TSExplicit birthTime, PRPAMT201310UV02OtherIDs otherIds) {
        PRPAMT201310UV02Person person = new PRPAMT201310UV02Person();

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
        JAXBElement<PRPAMT201310UV02Person> result = new JAXBElement<PRPAMT201310UV02Person>(xmlqname, PRPAMT201310UV02Person.class, person);

        return result;
    }

    public static JAXBElement<PRPAMT201302UV02PatientPatientPerson> create201302PatientPerson(String patFirstName, String patLastName, String gender, String birthTime, String ssn, II remotePatId) {
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

        PRPAMT201302UV02OtherIDs otherIds = null;
        if (NullChecker.isNotNullish(ssn) ||
                (remotePatId != null &&
                NullChecker.isNotNullish(remotePatId.getRoot()) &&
                NullChecker.isNotNullish(remotePatId.getExtension()))) {
            otherIds = createPRPAMT201302UVOtherIDs(ssn, remotePatId);
        }

        return create201302PatientPerson(name, genderCode, bday, otherIds);
    }

    public static JAXBElement<PRPAMT201302UV02PatientPatientPerson> create201302PatientPerson(PNExplicit patName, CE gender, TSExplicit birthTime, List<PRPAMT201301UV02OtherIDs> otherIds, II remotePatId) {
        PRPAMT201302UV02OtherIDs convertedOtherIds = null;
        if (otherIds != null &&
                otherIds.size() > 0 &&
                otherIds.get(0) != null &&
                otherIds.get(0).getId() != null &&
                otherIds.get(0).getId().size() > 0 &&
                otherIds.get(0).getId().get(0) != null &&
                NullChecker.isNotNullish(otherIds.get(0).getId().get(0).getExtension())) {
            convertedOtherIds = createPRPAMT201302UVOtherIDs(otherIds.get(0).getId().get(0).getExtension(), remotePatId);
        } else if (remotePatId != null &&
                NullChecker.isNotNullish(remotePatId.getRoot()) &&
                NullChecker.isNotNullish(remotePatId.getExtension())) {
            convertedOtherIds = createPRPAMT201302UVOtherIDs(null, remotePatId);
        }

        return create201302PatientPerson(patName, gender, birthTime, convertedOtherIds);
    }

    public static JAXBElement<PRPAMT201302UV02PatientPatientPerson> create201302PatientPerson(List<PRPAMT201310UV02OtherIDs> otherIds, PNExplicit patName, CE gender, TSExplicit birthTime, II remotePatId) {
        PRPAMT201302UV02OtherIDs convertedOtherIds = null;
        if (otherIds != null &&
                otherIds.size() > 0 &&
                otherIds.get(0) != null &&
                otherIds.get(0).getId() != null &&
                otherIds.get(0).getId().size() > 0 &&
                otherIds.get(0).getId().get(0) != null &&
                NullChecker.isNotNullish(otherIds.get(0).getId().get(0).getExtension())) {
            convertedOtherIds = createPRPAMT201302UVOtherIDs(otherIds.get(0).getId().get(0).getExtension(), remotePatId);
        } else if (remotePatId != null &&
                NullChecker.isNotNullish(remotePatId.getRoot()) &&
                NullChecker.isNotNullish(remotePatId.getExtension())) {
            convertedOtherIds = createPRPAMT201302UVOtherIDs(null, remotePatId);
        }

        return create201302PatientPerson(patName, gender, birthTime, convertedOtherIds);
    }

    public static JAXBElement<PRPAMT201302UV02PatientPatientPerson> create201302PatientPerson(PNExplicit patName, CE gender, TSExplicit birthTime, PRPAMT201302UV02OtherIDs otherIds) {
        PRPAMT201302UV02PatientPatientPerson person = new PRPAMT201302UV02PatientPatientPerson();

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
        JAXBElement<PRPAMT201302UV02PatientPatientPerson> result = new JAXBElement<PRPAMT201302UV02PatientPatientPerson>(xmlqname, PRPAMT201302UV02PatientPatientPerson.class, person);

        return result;
    }

    public static PRPAMT201301UV02OtherIDs createPRPAMT201301UVOtherIDs(String ssn) {
        PRPAMT201301UV02OtherIDs otherIds = new PRPAMT201301UV02OtherIDs();

        otherIds.getClassCode().add("PAT");

        // Set the SSN
        if (NullChecker.isNotNullish(ssn)) {
            log.info("Setting Patient SSN: " + ssn);
            otherIds.getId().add(HL7DataTransformHelper.IIFactory(HL7Constants.SSN_ID_ROOT, ssn));
        }

        return otherIds;
    }

    public static PRPAMT201310UV02OtherIDs createPRPAMT201310UVOtherIDs(String ssn) {
        PRPAMT201310UV02OtherIDs otherIds = new PRPAMT201310UV02OtherIDs();

        otherIds.getClassCode().add("PAT");

        // Set the SSN
        if (NullChecker.isNotNullish(ssn)) {
            log.info("Setting Patient SSN: " + ssn);
            otherIds.getId().add(HL7DataTransformHelper.IIFactory(HL7Constants.SSN_ID_ROOT, ssn));
        }

        return otherIds;
    }

    public static PRPAMT201302UV02OtherIDs createPRPAMT201302UVOtherIDs(String ssn, II remotePatId) {
        PRPAMT201302UV02OtherIDs otherIds = new PRPAMT201302UV02OtherIDs();

        otherIds.getClassCode().add("PAT");

        // Set the SSN
        if (NullChecker.isNotNullish(ssn)) {
            log.info("Setting Patient SSN: " + ssn);
            PRPAMT201302UV02OtherIDsId ssnId = new PRPAMT201302UV02OtherIDsId();
            ssnId.setExtension(ssn);
            ssnId.setRoot(HL7Constants.SSN_ID_ROOT);
            otherIds.getId().add(ssnId);
        }

        if (remotePatId != null &&
                NullChecker.isNotNullish(remotePatId.getRoot()) &&
                NullChecker.isNotNullish(remotePatId.getExtension())) {
            log.info("Setting Remote Patient Id: " + remotePatId.getExtension());
            log.info("Setting Remote Assigning Authority: " + remotePatId.getRoot());
            PRPAMT201302UV02OtherIDsId respondingId = new PRPAMT201302UV02OtherIDsId();
            respondingId.setExtension(remotePatId.getExtension());
            respondingId.setRoot(remotePatId.getRoot());
            otherIds.getId().add(respondingId);
        }

        return otherIds;
    }

    public static JAXBElement<PRPAMT201301UV02BirthPlace> createPRPAMT201301UVBirthPlace(PRPAMT201310UV02BirthPlace birthPlace) {
        PRPAMT201301UV02BirthPlace result = new PRPAMT201301UV02BirthPlace();

        if (birthPlace == null) {
            return null;
        }

        if (birthPlace.getBirthplace() != null) {
            result.setBirthplace(birthPlace.getBirthplace());
        }
        if (NullChecker.isNotNullish(birthPlace.getClassCode())) {
            for (String code : birthPlace.getClassCode()) {
                result.getClassCode().add(code);
            }
        }
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "birthPlace");
        JAXBElement<PRPAMT201301UV02BirthPlace> element = new JAXBElement<PRPAMT201301UV02BirthPlace>(xmlqname, PRPAMT201301UV02BirthPlace.class, result);


        return element;
    }

    public static JAXBElement<PRPAMT201301UV02Person> create201301PatientPerson(PRPAMT201310UV02Person person) {
        PRPAMT201301UV02Person result = new PRPAMT201301UV02Person();

        if (person == null) {
            return null;
        }

        for (PNExplicit name : person.getName()) {
            result.getName().add(name);
        }

        result.setAdministrativeGenderCode(person.getAdministrativeGenderCode());

        result.setBirthTime(person.getBirthTime());


        for (ADExplicit add : person.getAddr()) {
            result.getAddr().add(add);
        }

        if (person.getBirthPlace() != null) {
            result.setBirthPlace(createPRPAMT201301UVBirthPlace(person.getBirthPlace().getValue()));
        }

        for (II id : person.getId()) {
            result.getId().add(id);
        }

        for (TELExplicit telephone : person.getTelecom()) {
            result.getTelecom().add(telephone);
        }

        if (person.getAsOtherIDs() != null) {
            for (PRPAMT201310UV02OtherIDs otherId : person.getAsOtherIDs()) {
                PRPAMT201301UV02OtherIDs newId = new PRPAMT201301UV02OtherIDs();
                newId.getId().add(otherId.getId().get(0));
                result.getAsOtherIDs().add(newId);
            }
        }
        result.setDeceasedInd(person.getDeceasedInd());
        result.setDeceasedTime(person.getDeceasedTime());
        result.setDeterminerCode(person.getDeterminerCode());
        result.setDesc(person.getDesc());
        result.setEducationLevelCode(person.getEducationLevelCode());
        result.setLivingArrangementCode(person.getLivingArrangementCode());
        result.setMaritalStatusCode(person.getMaritalStatusCode());
        result.setMultipleBirthInd(person.getMultipleBirthInd());
        result.setMultipleBirthOrderNumber(person.getMultipleBirthOrderNumber());
        result.setOrganDonorInd(person.getOrganDonorInd());
        result.setReligiousAffiliationCode(person.getReligiousAffiliationCode());
        result.setTypeId(person.getTypeId());


        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UV02Person> element = new JAXBElement<PRPAMT201301UV02Person>(xmlqname, PRPAMT201301UV02Person.class, result);

        return element;

    }

    public static JAXBElement<PRPAMT201301UV02Person> create201301PatientPerson(JAXBElement<PRPAMT201302UV02PatientPatientPerson> person) {
        PRPAMT201301UV02Person result = new PRPAMT201301UV02Person();

        if (person == null) {
            return null;
        }

        for (PNExplicit name : person.getValue().getName()) {
            result.getName().add(name);
        }

        result.setAdministrativeGenderCode(person.getValue().getAdministrativeGenderCode());

        result.setBirthTime(person.getValue().getBirthTime());


        for (ADExplicit add : person.getValue().getAddr()) {
            result.getAddr().add(add);
        }

        if (person.getValue().getBirthPlace() != null) {
            result.setBirthPlace(createPRPAMT201301UVBirthPlace(person.getValue().getBirthPlace()));
        }

        for (II id : person.getValue().getId()) {
            result.getId().add(id);
        }

        for (TELExplicit telephone : person.getValue().getTelecom()) {
            result.getTelecom().add(telephone);
        }

        result.setDeceasedInd(person.getValue().getDeceasedInd());
        result.setDeceasedTime(person.getValue().getDeceasedTime());
        result.setDeterminerCode(person.getValue().getDeterminerCode());
        result.setDesc(person.getValue().getDesc());
        result.setEducationLevelCode(person.getValue().getEducationLevelCode());
        result.setLivingArrangementCode(person.getValue().getLivingArrangementCode());
        result.setMaritalStatusCode(person.getValue().getMaritalStatusCode());
        result.setMultipleBirthInd(person.getValue().getMultipleBirthInd());
        result.setMultipleBirthOrderNumber(person.getValue().getMultipleBirthOrderNumber());
        result.setOrganDonorInd(person.getValue().getOrganDonorInd());
        result.setReligiousAffiliationCode(person.getValue().getReligiousAffiliationCode());
        result.setTypeId(person.getValue().getTypeId());


        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UV02Person> element = new JAXBElement<PRPAMT201301UV02Person>(xmlqname, PRPAMT201301UV02Person.class, result);

        return element;

    }

    public static JAXBElement<PRPAMT201301UV02BirthPlace> createPRPAMT201301UVBirthPlace(JAXBElement<PRPAMT201302UV02BirthPlace> value) {
        PRPAMT201301UV02BirthPlace result = new PRPAMT201301UV02BirthPlace();
        PRPAMT201302UV02BirthPlace birthPlace;

        if (value == null) {
            return null;
        }

        birthPlace = value.getValue();

        if (birthPlace.getBirthplace() != null) {
            result.setBirthplace(birthPlace.getBirthplace());
        }
        if (NullChecker.isNotNullish(birthPlace.getClassCode())) {
            for (String code : birthPlace.getClassCode()) {
                result.getClassCode().add(code);
            }
        }
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "birthPlace");
        JAXBElement<PRPAMT201301UV02BirthPlace> element = new JAXBElement<PRPAMT201301UV02BirthPlace>(xmlqname, PRPAMT201301UV02BirthPlace.class, result);


        return element;
    }

    public static TSExplicit createBirthTime(PRPAMT201306UV02LivingSubjectBirthTime birthTime) {
        TSExplicit birthDay = null;
        IVLTSExplicit bday;

        if (birthTime != null) {
            bday = birthTime.getValue().get(0);
            birthDay = createBirthTime(bday);
        }

        return birthDay;

    }

    private static TSExplicit createBirthTime(IVLTSExplicit bday) {
        return HL7DataTransformHelper.TSExplicitFactory(bday.getValue());
    }
}
