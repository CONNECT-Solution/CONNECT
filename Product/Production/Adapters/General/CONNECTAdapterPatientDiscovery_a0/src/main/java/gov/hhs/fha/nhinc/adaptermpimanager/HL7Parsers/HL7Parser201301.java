/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adaptermpimanager.HL7Parsers;

import gov.hhs.fha.nhinc.adaptermpimanager.*;
import gov.hhs.fha.nhinc.mpilib.*;
import java.util.List;
import java.io.Serializable;
import java.util.Iterator;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;

/**
 *
 * @author rayj
 */
public class HL7Parser201301 {

    private static Log log = LogFactory.getLog(HL7Parser201301.class);

    public static void PrintMessageIdFromMessage(org.hl7.v3.PRPAIN201301UV02 message) {
        if (!(message == null)) {
            HL7Parser.PrintId(message.getId(), "message");
        }
    }

    public static String ExtractGender(PRPAMT201301UV02Person person) {
        String genderCode = null;
        CE administrativeGenderCode = person.getAdministrativeGenderCode();
        if (administrativeGenderCode == null) {
            log.info("message does not contain a gender code");
        } else {
            log.info("person.getAdministrativeGenderCode().getCode()=" + person.getAdministrativeGenderCode().getCode());
            log.info("person.getAdministrativeGenderCode().getDisplayName()=" + person.getAdministrativeGenderCode().getDisplayName());
            genderCode = person.getAdministrativeGenderCode().getCode();
        }
        return genderCode;
    }

    public static String ExtractBirthdate(PRPAMT201301UV02Person person) {
        String birthDate = null;
        if (person.getBirthTime() == null) {
            log.info("message does not contain a birthtime");
        } else {
            birthDate = person.getBirthTime().getValue();
            if (birthDate == null) {
                log.info("message does not contain a birthtime");
            } else {
                log.info("person.getBirthTime().getValue()=" + person.getBirthTime().getValue());
            }
        }
        return birthDate;
    }

    public static PersonName ExtractPersonName(PRPAMT201301UV02Person person) {
        //temp logic to prove i can get to name - long term would want to store discrete name parts
        //also assume one name, not multiple names
        PersonName personname = new PersonName();

        log.info("patientPerson.getName().size() " + person.getName().size());
        if (person.getName() != null &&
                person.getName().size() > 0 &&
                person.getName().get(0) != null &&
                person.getName().get(0).getContent() != null) {

            List<Serializable> choice = person.getName().get(0).getContent();
            log.info("choice.size()=" + choice.size());

            Iterator<Serializable> iterSerialObjects = choice.iterator();

            String nameString = "";
            EnExplicitFamily lastname = new EnExplicitFamily();
            EnExplicitGiven firstname = new EnExplicitGiven();

            while (iterSerialObjects.hasNext()) {
                log.info("in iterSerialObjects.hasNext() loop");

                Serializable contentItem = iterSerialObjects.next();

                if (contentItem instanceof String) {
                    log.info("contentItem is string");
                    String strValue = (String) contentItem;

                    if (nameString != null) {
                        nameString += strValue;
                    } else {
                        nameString = strValue;
                    }
                    log.info("nameString=" + nameString);
                } else if (contentItem instanceof JAXBElement) {
                    log.info("contentItem is JAXBElement");

                    JAXBElement oJAXBElement = (JAXBElement) contentItem;

                    if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                        lastname = (EnExplicitFamily) oJAXBElement.getValue();
                        log.info("found lastname element; content=" + lastname.getContent());
                    } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                        firstname = (EnExplicitGiven) oJAXBElement.getValue();
                        log.info("found firstname element; content=" + firstname.getContent());
                    } else {
                        log.info("other name part=" + (ENXPExplicit) oJAXBElement.getValue());
                    }
                } else {
                    log.info("contentItem is other");
                }
            }

            // If text string in patient name, then set in name
            // else set in element.
            boolean namefound = false;
            if (lastname.getContent() != null) {
                personname.setLastName(lastname.getContent());
                log.info("FamilyName : " + personname.getLastName());
                namefound = true;
            }

            if (firstname.getContent() != null) {
                personname.setFirstName(firstname.getContent());
                log.info("GivenName : " + personname.getFirstName());
                namefound = true;
            }

            if (!namefound && !nameString.trim().contentEquals("")) {
                log.info("setting name by nameString " + nameString);
                personname.setLastName(nameString);
            } else {
            }
        }
        log.info("returning personname");
        return personname;
    }

    public static Identifiers ExtractPersonIdentifiers(PRPAMT201301UV02Patient patient) {
        Identifiers ids = new Identifiers();
        for (II patientid : patient.getId()) {
            Identifier id = new Identifier();
            id.setId(patientid.getExtension());
            id.setOrganizationId(patientid.getRoot());
            log.info("Created id from patient identifier [organization=" + id.getOrganizationId() + "][id=" + id.getId() + "]");
            ids.add(id);
        }

        PRPAMT201301UV02Person person = ExtractHL7PatientPersonFromHL7Patient(patient);
        for (II personid : person.getId()) {
            Identifier id = new Identifier();
            id.setId(personid.getExtension());
            id.setOrganizationId(personid.getRoot());
            log.info("Created id from person identifier [organization=" + id.getOrganizationId() + "][id=" + id.getId() + "]");
            ids.add(id);
        }

        List<PRPAMT201301UV02OtherIDs> OtherIds = person.getAsOtherIDs();
        for (PRPAMT201301UV02OtherIDs otherPersonIds : OtherIds) {
            for (II otherPersonId : otherPersonIds.getId()) {
                if (!(otherPersonId.getRoot().contentEquals(HL7Parser.SSN_OID))) {
                    Identifier id = new Identifier();
                    id.setId(otherPersonId.getExtension());
                    id.setOrganizationId(otherPersonId.getRoot());
                    log.info("Created id from person other identifier [organization=" + id.getOrganizationId() + "][id=" + id.getId() + "]");
                    ids.add(id);
                }
            }
        }

        return ids;
    }
    
    public static String ExtractSsn (PRPAMT201301UV02Person person) {
        String ssn = null;
        
        List<PRPAMT201301UV02OtherIDs> OtherIds = person.getAsOtherIDs();
        for (PRPAMT201301UV02OtherIDs otherPersonIds : OtherIds) {
            for (II otherPersonId : otherPersonIds.getId()) {
                if (otherPersonId.getRoot().contentEquals(HL7Parser.SSN_OID)) {
                    ssn = otherPersonId.getExtension();
                }
            }
        }
        
        return ssn;
    }

    public static PRPAMT201301UV02Person ExtractHL7PatientPersonFromHL7Patient(PRPAMT201301UV02Patient patient) {
        JAXBElement<PRPAMT201301UV02Person> patientPersonElement = patient.getPatientPerson();
        PRPAMT201301UV02Person patientPerson = patientPerson = patientPersonElement.getValue();
        return patientPerson;
    }

    public static PRPAMT201301UV02Person ExtractHL7PatientPersonFrom201301Message(org.hl7.v3.PRPAIN201301UV02 message) {
        //assume one subject for now
        PRPAMT201301UV02Patient patient = ExtractHL7PatientFromMessage(message);
        PRPAMT201301UV02Person patientPerson = ExtractHL7PatientPersonFromHL7Patient(patient);
        return patientPerson;
    }

    public static PRPAMT201301UV02Patient ExtractHL7PatientFromMessage(org.hl7.v3.PRPAIN201301UV02 message) {
        //assume one subject for now
        PRPAMT201301UV02Patient patient = null;
        log.info("in ExtractPatient");

        if (message == null) {
            log.info("message is null - no patient");
            return null;
        }
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = message.getControlActProcess();
        if (controlActProcess == null) {
            log.info("controlActProcess is null - no patient");
            return null;
        }
        HL7Parser.PrintId(controlActProcess.getId(), "controlActProcess");

        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = controlActProcess.getSubject();
        if ((subjects == null) || (subjects.size() == 0)) {
            log.info("subjects is blank/null - no patient");
            return null;
        }

        //for now, assume we only need one subject, this will need to be modified later
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = subjects.get(0);
        HL7Parser.PrintId(subject.getTypeId(), "subject");

        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent registrationevent = subject.getRegistrationEvent();
        if (registrationevent == null) {
            log.info("registrationevent is null - no patient");
            return null;
        }
        HL7Parser.PrintId(registrationevent.getTypeId(), "registrationevent");

        PRPAIN201301UV02MFMIMT700701UV01Subject2 subject1 = registrationevent.getSubject1();
        if (subject1 == null) {
            log.info("subject1 is null - no patient");
            return null;
        }
        HL7Parser.PrintId(subject1.getTypeId(), "subject1");

        patient = subject1.getPatient();
        if (patient == null) {
            log.info("patient is null - no patient");
            return null;
        }
        HL7Parser.PrintId(patient.getId(), "patient");

        log.info("done with ExtractPatient");
        return patient;
    }

    public static Patient ExtractMpiPatientFromMessage(org.hl7.v3.PRPAIN201301UV02 message) {
        PRPAMT201301UV02Patient hl7patient = ExtractHL7PatientFromMessage(message);
        Patient mpipatient = ExtractMpiPatientFromHL7Patient(hl7patient);
        return mpipatient;
    }

    public static Patient ExtractMpiPatientFromHL7Patient(PRPAMT201301UV02Patient patient) {
        PRPAMT201301UV02Person patientPerson = ExtractHL7PatientPersonFromHL7Patient(patient);
        Patient mpiPatient = new Patient();
        mpiPatient.setName(ExtractPersonName(patientPerson));
        mpiPatient.setGender(ExtractGender(patientPerson));
        String birthdateString = ExtractBirthdate(patientPerson);
        mpiPatient.setDateOfBirth(birthdateString);
        mpiPatient.setSSN(ExtractSsn(patientPerson));

        Identifiers ids = ExtractPersonIdentifiers(patient);
        mpiPatient.setIdentifiers(ids);
        return mpiPatient;
    }

    public static org.hl7.v3.PRPAIN201301UV02 BuildMessagePRPAIN201301UV(Patient mpiPatient) {
        ObjectFactory factory = new ObjectFactory();
        org.hl7.v3.PRPAIN201301UV02 resultMessage = new org.hl7.v3.PRPAIN201301UV02();

        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject1();
        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent registrationevent = new PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent();
        subject.setRegistrationEvent(registrationevent);

        PRPAIN201301UV02MFMIMT700701UV01Subject2 subjectA = new PRPAIN201301UV02MFMIMT700701UV01Subject2();
        registrationevent.setSubject1(subjectA);

        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        subjectA.setPatient(patient);
        PRPAMT201301UV02Person patientPerson = new PRPAMT201301UV02Person();
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UV02Person> patientPersonElement = new JAXBElement<PRPAMT201301UV02Person>(xmlqname, PRPAMT201301UV02Person.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patientPersonElement.setValue(patientPerson);
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        resultMessage.setControlActProcess(controlActProcess);
        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = resultMessage.getControlActProcess().getSubject();
        subjects.add(subject);
        if (!(mpiPatient.getDateOfBirth() == null)) {
            TSExplicit birthtime = new TSExplicit();
            birthtime.setValue(mpiPatient.getDateOfBirth().toString());
            patientPerson.setBirthTime(birthtime);
        }

        CE administrativeGenderCode = new CE();
        administrativeGenderCode.setCode(mpiPatient.getGender());
        patientPerson.setAdministrativeGenderCode(administrativeGenderCode);

        //
        // Name.
        //
        PNExplicit name = (PNExplicit) (factory.createPNExplicit());
        List namelist = name.getContent();

        if (mpiPatient.getName() != null && mpiPatient.getName().getLastName().length() > 0) {
            log.info("familyName >" + mpiPatient.getName().getLastName() + "<");
            EnExplicitFamily familyName = new EnExplicitFamily();
            familyName.setContent(mpiPatient.getName().getLastName());
            familyName.setPartType("FAM");
            namelist.add(factory.createPNExplicitFamily(familyName));
        }

        if (mpiPatient.getName() != null && mpiPatient.getName().getFirstName().length() > 0) {
            log.info("givenName >" + mpiPatient.getName().getFirstName() + "<");
            EnExplicitGiven givenName = new EnExplicitGiven();
            givenName.setContent(mpiPatient.getName().getFirstName());
            givenName.setPartType("GIV");
            namelist.add(factory.createPNExplicitGiven(givenName));
        }

        namelist.add(" ");
        patientPerson.getName().add(name);

        for (Identifier resultPatientId : mpiPatient.getIdentifiers()) {
            II id = new II();
            id.setRoot(resultPatientId.getOrganizationId());
            id.setExtension(resultPatientId.getId());
            patient.getId().add(id);
        }

        Identifier resultPatientId = mpiPatient.getIdentifiers().get(0);
        II id = new II();
        id.setRoot(resultPatientId.getOrganizationId());
        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        device.getId().add(id);
        sender.setDevice(device);
        resultMessage.setSender(sender);

        return resultMessage;
    }
}
