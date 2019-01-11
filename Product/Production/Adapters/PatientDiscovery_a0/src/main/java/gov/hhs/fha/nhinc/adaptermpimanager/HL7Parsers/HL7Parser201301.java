/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.adaptermpimanager.HL7Parsers;

import com.google.common.base.Optional;

import gov.hhs.fha.nhinc.mpilib.Identifier;
import gov.hhs.fha.nhinc.mpilib.Identifiers;
import gov.hhs.fha.nhinc.mpilib.Patient;
import gov.hhs.fha.nhinc.mpilib.PersonName;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.apache.commons.collections.CollectionUtils;
import org.hl7.v3.CE;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject2;
import org.hl7.v3.PRPAMT201301UV02OtherIDs;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.TSExplicit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rayj
 */
public class HL7Parser201301 {

    private static final Logger LOG = LoggerFactory.getLogger(HL7Parser201301.class);

    public static void PrintMessageIdFromMessage(org.hl7.v3.PRPAIN201301UV02 message) {
        if (message != null) {
            HL7Parser.PrintId(message.getId(), "message");
        }
    }

    public static String ExtractGender(PRPAMT201301UV02Person person) {
        String genderCode = null;
        CE administrativeGenderCode = person.getAdministrativeGenderCode();
        if (administrativeGenderCode == null) {
            LOG.info("message does not contain a gender code");
        } else {
            LOG.info("person.getAdministrativeGenderCode().getCode()=" + person.getAdministrativeGenderCode().getCode());
            LOG.info("person.getAdministrativeGenderCode().getDisplayName()="
                + person.getAdministrativeGenderCode().getDisplayName());
            genderCode = person.getAdministrativeGenderCode().getCode();
        }
        return genderCode;
    }

    public static String ExtractBirthdate(PRPAMT201301UV02Person person) {
        String birthDate = null;
        if (person.getBirthTime() == null) {
            LOG.info("message does not contain a birthtime");
        } else {
            birthDate = person.getBirthTime().getValue();
            if (birthDate == null) {
                LOG.info("message does not contain a birthtime");
            } else {
                LOG.info("person.getBirthTime().getValue()=" + person.getBirthTime().getValue());
            }
        }
        return birthDate;
    }

    public static PersonName ExtractPersonName(PRPAMT201301UV02Person person) {
        // temp logic to prove i can get to name - long term would want to store discrete name parts
        // also assume one name, not multiple names
        PersonName personname = new PersonName();

        LOG.info("patientPerson.getName().size() " + person.getName().size());
        if (CollectionUtils.isNotEmpty(person.getName()) && person.getName().get(0) != null
            && person.getName().get(0).getContent() != null) {

            List<Serializable> choice = person.getName().get(0).getContent();
            LOG.info("choice.size()=" + choice.size());

            Iterator<Serializable> iterSerialObjects = choice.iterator();

            String nameString = "";
            EnExplicitFamily lastname = new EnExplicitFamily();
            EnExplicitGiven firstname = new EnExplicitGiven();

            while (iterSerialObjects.hasNext()) {
                LOG.info("in iterSerialObjects.hasNext() loop");

                Serializable contentItem = iterSerialObjects.next();

                if (contentItem instanceof String) {
                    LOG.info("contentItem is string");
                    String strValue = (String) contentItem;

                    if (nameString != null) {
                        nameString += strValue;
                    } else {
                        nameString = strValue;
                    }
                    LOG.info("nameString=" + nameString);
                } else if (contentItem instanceof JAXBElement) {
                    LOG.info("contentItem is JAXBElement");

                    JAXBElement oJAXBElement = (JAXBElement) contentItem;

                    if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                        lastname = (EnExplicitFamily) oJAXBElement.getValue();
                        LOG.info("found lastname element; content=" + lastname.getContent());
                    } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                        firstname = (EnExplicitGiven) oJAXBElement.getValue();
                        LOG.info("found firstname element; content=" + firstname.getContent());
                    } else {
                        LOG.info("other name part=" + oJAXBElement.getValue());
                    }
                } else {
                    LOG.info("contentItem is other");
                }
            }

            // If text string in patient name, then set in name
            // else set in element.
            boolean namefound = false;
            if (lastname.getContent() != null) {
                personname.setLastName(lastname.getContent());
                LOG.info("FamilyName : " + personname.getLastName());
                namefound = true;
            }

            if (firstname.getContent() != null) {
                personname.setFirstName(firstname.getContent());
                LOG.info("GivenName : " + personname.getFirstName());
                namefound = true;
            }

            if (!namefound && !nameString.trim().contentEquals("")) {
                LOG.info("setting name by nameString " + nameString);
                personname.setLastName(nameString);
            } else {
            }
        }
        LOG.info("returning personname");
        return personname;
    }

    public static Identifiers ExtractPersonIdentifiers(PRPAMT201301UV02Patient patient) {
        Identifiers ids = new Identifiers();
        for (II patientid : patient.getId()) {
            Identifier id = new Identifier();
            id.setId(patientid.getExtension());
            id.setOrganizationId(patientid.getRoot());
            LOG.info("Created id from patient identifier [organization=" + id.getOrganizationId() + "][id="
                + id.getId() + "]");
            ids.add(id);
        }
        Optional<PRPAMT201301UV02Person> personOptional = Optional.fromNullable(ExtractHL7PatientPersonFromHL7Patient(patient));
        PRPAMT201301UV02Person person = personOptional.or(new PRPAMT201301UV02Person());
        for (II personid : person.getId()) {
            Identifier id = new Identifier();
            id.setId(personid.getExtension());
            id.setOrganizationId(personid.getRoot());
            LOG.info("Created id from person identifier [organization=" + id.getOrganizationId() + "][id=" + id.getId()
                + "]");
            ids.add(id);
        }

        List<PRPAMT201301UV02OtherIDs> OtherIds = person.getAsOtherIDs();
        for (PRPAMT201301UV02OtherIDs otherPersonIds : OtherIds) {
            for (II otherPersonId : otherPersonIds.getId()) {
                if (!otherPersonId.getRoot().contentEquals(HL7Parser.SSN_OID)) {
                    Identifier id = new Identifier();
                    id.setId(otherPersonId.getExtension());
                    id.setOrganizationId(otherPersonId.getRoot());
                    LOG.info("Created id from person other identifier [organization=" + id.getOrganizationId()
                        + "][id=" + id.getId() + "]");
                    ids.add(id);
                }
            }
        }

        return ids;
    }

    public static String ExtractSsn(PRPAMT201301UV02Person person) {
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
        if (patient == null) {
            return null;
        }
        JAXBElement<PRPAMT201301UV02Person> patientPersonElement = patient.getPatientPerson();
        if (null == patientPersonElement) {
            return null;
        }
        return patientPersonElement.getValue();
    }

    public static PRPAMT201301UV02Person ExtractHL7PatientPersonFrom201301Message(org.hl7.v3.PRPAIN201301UV02 message) {
        // assume one subject for now
        PRPAMT201301UV02Patient patient = ExtractHL7PatientFromMessage(message);
            return ExtractHL7PatientPersonFromHL7Patient(patient);
    }

    public static PRPAMT201301UV02Patient ExtractHL7PatientFromMessage(org.hl7.v3.PRPAIN201301UV02 message) {
        // assume one subject for now
        PRPAMT201301UV02Patient patient;
        LOG.info("in ExtractPatient");

        if (message == null) {
            LOG.info("message is null - no patient");
            return null;
        }
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = message.getControlActProcess();
        if (controlActProcess == null) {
            LOG.info("controlActProcess is null - no patient");
            return null;
        }
        HL7Parser.PrintId(controlActProcess.getId(), "controlActProcess");

        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = controlActProcess.getSubject();
        if (CollectionUtils.isEmpty(subjects)) {
            LOG.info("subjects is blank/null - no patient");
            return null;
        }

        // for now, assume we only need one subject, this will need to be modified later
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = subjects.get(0);
        HL7Parser.PrintId(subject.getTypeId(), "subject");

        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent registrationevent = subject.getRegistrationEvent();
        if (registrationevent == null) {
            LOG.info("registrationevent is null - no patient");
            return null;
        }
        HL7Parser.PrintId(registrationevent.getTypeId(), "registrationevent");

        PRPAIN201301UV02MFMIMT700701UV01Subject2 subject1 = registrationevent.getSubject1();
        if (subject1 == null) {
            LOG.info("subject1 is null - no patient");
            return null;
        }
        HL7Parser.PrintId(subject1.getTypeId(), "subject1");

        patient = subject1.getPatient();
        if (patient == null) {
            LOG.info("patient is null - no patient");
            return null;
        }
        HL7Parser.PrintId(patient.getId(), "patient");

        LOG.info("done with ExtractPatient");
        return patient;
    }

    public static Patient ExtractMpiPatientFromMessage(org.hl7.v3.PRPAIN201301UV02 message) {
        PRPAMT201301UV02Patient hl7patient = ExtractHL7PatientFromMessage(message);
        return ExtractMpiPatientFromHL7Patient(hl7patient);
    }

    public static Patient ExtractMpiPatientFromHL7Patient(PRPAMT201301UV02Patient patient) {
        PRPAMT201301UV02Person patientPerson = ExtractHL7PatientPersonFromHL7Patient(patient);
        Patient mpiPatient = new Patient();
        if (patientPerson != null) {
            mpiPatient.getNames().add(ExtractPersonName(patientPerson));
            mpiPatient.setGender(ExtractGender(patientPerson));
            String birthdateString = ExtractBirthdate(patientPerson);
            mpiPatient.setDateOfBirth(birthdateString);
            mpiPatient.setSSN(ExtractSsn(patientPerson));
        }
        if (patient != null) {
            Identifiers ids = ExtractPersonIdentifiers(patient);
            mpiPatient.setIdentifiers(ids);
        }
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
        JAXBElement<PRPAMT201301UV02Person> patientPersonElement = new JAXBElement<>(xmlqname,
            PRPAMT201301UV02Person.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patientPersonElement.setValue(patientPerson);
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        resultMessage.setControlActProcess(controlActProcess);
        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = resultMessage.getControlActProcess().getSubject();
        subjects.add(subject);
        if (!(mpiPatient.getDateOfBirth() == null)) {
            TSExplicit birthtime = new TSExplicit();
            birthtime.setValue(mpiPatient.getDateOfBirth());
            patientPerson.setBirthTime(birthtime);
        }

        CE administrativeGenderCode = new CE();
        administrativeGenderCode.setCode(mpiPatient.getGender());
        patientPerson.setAdministrativeGenderCode(administrativeGenderCode);

        //
        // Name.
        //
        PNExplicit name = factory.createPNExplicit();
        List namelist = name.getContent();

        PersonName mpiPatientName = null;
        if (CollectionUtils.isNotEmpty(mpiPatient.getNames())) {
            mpiPatientName = mpiPatient.getNames().get(0);
        }

        if (mpiPatientName != null && mpiPatientName.getLastName().length() > 0) {
            LOG.info("familyName >" + mpiPatientName.getLastName() + "<");
            EnExplicitFamily familyName = new EnExplicitFamily();
            familyName.setContent(mpiPatientName.getLastName());
            familyName.setPartType("FAM");
            namelist.add(factory.createPNExplicitFamily(familyName));
        }

        if (mpiPatientName != null && mpiPatientName.getFirstName().length() > 0) {
            LOG.info("givenName >" + mpiPatientName.getFirstName() + "<");
            EnExplicitGiven givenName = new EnExplicitGiven();
            givenName.setContent(mpiPatientName.getFirstName());
            givenName.setPartType("GIV");
            namelist.add(factory.createPNExplicitGiven(givenName));
        }

        namelist.add(" ");
        patientPerson.getName().add(name);

        for (Identifier resultPatientId : mpiPatient.getIdentifiers()) {
            II id = new II();
            id.setRoot(HomeCommunityMap.formatHomeCommunityId(resultPatientId.getOrganizationId()));
            id.setExtension(resultPatientId.getId());
            patient.getId().add(id);
        }

        Identifier resultPatientId = mpiPatient.getIdentifiers().get(0);
        II id = new II();
        id.setRoot(HomeCommunityMap.formatHomeCommunityId(resultPatientId.getOrganizationId()));
        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        device.getId().add(id);
        sender.setDevice(device);
        resultMessage.setSender(sender);

        return resultMessage;
    }
}
