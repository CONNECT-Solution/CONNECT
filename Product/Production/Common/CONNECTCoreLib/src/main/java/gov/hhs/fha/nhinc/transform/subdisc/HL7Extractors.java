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
package gov.hhs.fha.nhinc.transform.subdisc;

import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.apache.commons.collections.CollectionUtils;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject2;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01Subject2;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201302UV02Patient;
import org.hl7.v3.PRPAMT201302UV02PatientPatientPerson;
import org.hl7.v3.PRPAMT201302UV02Person;
import org.hl7.v3.PRPAMT201304UV02Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author MFLYNN02
 */
public class HL7Extractors {

    private static final Logger LOG = LoggerFactory.getLogger(HL7Extractors.class);

    public static PRPAIN201301UV02MFMIMT700701UV01Subject1 ExtractSubjectFromMessage(
        org.hl7.v3.PRPAIN201301UV02 message) {
        // assume one subject for now

        if (message == null) {
            LOG.info("message is null - no patient");
            return null;
        }
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = message.getControlActProcess();
        if (controlActProcess == null) {
            LOG.info("controlActProcess is null - no patient");
            return null;
        }

        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = controlActProcess.getSubject();
        if (subjects == null || subjects.isEmpty()) {
            LOG.info("subjects is blank/null - no patient");
            return null;
        }

        // for now, assume we only need one subject, this will need to be modified later

        return subjects.get(0);
    }

    public static PRPAIN201302UV02MFMIMT700701UV01Subject1 ExtractSubjectFromMessage(
        org.hl7.v3.PRPAIN201302UV02 message) {
        // assume one subject for now

        if (message == null) {
            LOG.info("message is null - no patient");
            return null;
        }
        PRPAIN201302UV02MFMIMT700701UV01ControlActProcess controlActProcess = message.getControlActProcess();
        if (controlActProcess == null) {
            LOG.info("controlActProcess is null - no patient");
            return null;
        }

        List<PRPAIN201302UV02MFMIMT700701UV01Subject1> subjects = controlActProcess.getSubject();
        if (subjects == null || subjects.isEmpty()) {
            LOG.info("subjects is blank/null - no patient");
            return null;
        }

        // for now, assume we only need one subject, this will need to be modified later

        return subjects.get(0);
    }

    public static PRPAIN201310UV02MFMIMT700711UV01Subject1 ExtractSubjectFromMessage(
        org.hl7.v3.PRPAIN201310UV02 message) {
        // assume one subject for now

        if (message == null) {
            LOG.info("message is null - no patient");
            return null;
        }

        PRPAIN201310UV02MFMIMT700711UV01ControlActProcess controlActProcess = message.getControlActProcess();
        if (controlActProcess == null) {
            LOG.info("controlActProcess is null - no patient");
            return null;
        }

        List<PRPAIN201310UV02MFMIMT700711UV01Subject1> subjects = controlActProcess.getSubject();
        if (subjects == null || subjects.isEmpty()) {
            LOG.info("subjects is blank/null - no patient");
            return null;
        }

        // for now, assume we only need one subject, this will need to be modified later

        return subjects.get(0);
    }

    public static PRPAMT201301UV02Patient ExtractHL7PatientFromMessage(org.hl7.v3.PRPAIN201301UV02 message) {
        PRPAMT201301UV02Patient patient;
        LOG.info("in ExtractPatient");

        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = ExtractSubjectFromMessage(message);
        if (subject == null) {
            return null;
        }
        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent registrationevent = subject.getRegistrationEvent();
        if (registrationevent == null) {
            LOG.info("registrationevent is null - no patient");
            return null;
        }

        PRPAIN201301UV02MFMIMT700701UV01Subject2 subject1 = registrationevent.getSubject1();
        if (subject1 == null) {
            LOG.info("subject1 is null - no patient");
            return null;
        }

        patient = subject1.getPatient();
        if (patient == null) {
            LOG.info("patient is null - no patient");
            return null;
        }

        LOG.info("done with ExtractPatient");
        return patient;
    }

    public static PRPAMT201302UV02Patient ExtractHL7PatientFromMessage(org.hl7.v3.PRPAIN201302UV02 message) {
        PRPAMT201302UV02Patient patient;
        LOG.info("in ExtractPatient");

        PRPAIN201302UV02MFMIMT700701UV01Subject1 subject = ExtractSubjectFromMessage(message);
        if (subject == null) {
            return null;
        }
        PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent registrationevent = subject.getRegistrationEvent();
        if (registrationevent == null) {
            LOG.info("registrationevent is null - no patient");
            return null;
        }

        PRPAIN201302UV02MFMIMT700701UV01Subject2 subject1 = registrationevent.getSubject1();
        if (subject1 == null) {
            LOG.info("subject1 is null - no patient");
            return null;
        }

        patient = subject1.getPatient();
        if (patient == null) {
            LOG.info("patient is null - no patient");
            return null;
        }

        LOG.info("done with ExtractPatient");
        return patient;
    }

    public static PRPAMT201304UV02Patient ExtractHL7PatientFromMessage(org.hl7.v3.PRPAIN201310UV02 message) {
        PRPAMT201304UV02Patient patient;
        LOG.info("in ExtractPatient");

        PRPAIN201310UV02MFMIMT700711UV01Subject1 subject = ExtractSubjectFromMessage(message);
        if (subject == null) {
            return null;
        }

        PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent registrationevent = subject.getRegistrationEvent();
        if (registrationevent == null) {
            LOG.info("registrationevent is null - no patient");
            return null;
        }

        PRPAIN201310UV02MFMIMT700711UV01Subject2 subject1 = registrationevent.getSubject1();
        if (subject1 == null) {
            LOG.info("subject1 is null - no patient");
            return null;
        }

        patient = subject1.getPatient();
        if (patient == null) {
            LOG.info("patient is null - no patient");
            return null;
        }

        LOG.info("done with ExtractPatient");
        return patient;
    }

    /**
     * This method translates a List<PN> into an PersonName object.
     *
     * @param The List of PN objects to be translated
     * @param The Person object
     */
    public static PersonNameType translatePNListtoPersonNameType(List<PNExplicit> names) {
        LOG.debug("HL7Extractor.translatePNListtoPersonNameType() -- Begin");
        PersonNameType personName = new PersonNameType();
        // NameType
        if (CollectionUtils.isNotEmpty(names.get(0).getUse())) {
            CeType nameType = new CeType();
            nameType.setCode(names.get(0).getUse().get(0));
            personName.setNameType(nameType);
        }
        // Name parts
        if (CollectionUtils.isNotEmpty(names) && CollectionUtils.isNotEmpty(names.get(0).getContent())) {
            List<Serializable> choice = names.get(0).getContent();
            Iterator<Serializable> iterSerialObjects = choice.iterator();

            String nameString = "";
            EnExplicitFamily familyName = new EnExplicitFamily();
            EnExplicitGiven givenName = new EnExplicitGiven();
            Boolean hasName = false;

            while (iterSerialObjects.hasNext()) {
                Serializable contentItem = iterSerialObjects.next();

                if (contentItem instanceof String) {
                    String strValue = (String) contentItem;
                    if (!nameString.isEmpty()) {
                        nameString += strValue;
                    } else {
                        nameString = strValue;
                    }
                    LOG.debug("Here is the contents of the string: {}", strValue);
                } else if (contentItem instanceof JAXBElement) {
                    JAXBElement oJAXBElement = (JAXBElement) contentItem;
                    LOG.debug("Found JAXBElement");
                    if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                        familyName = (EnExplicitFamily) oJAXBElement.getValue();
                        LOG.debug("Found FamilyName " + familyName.getContent());
                        hasName = true;
                    } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                        givenName = (EnExplicitGiven) oJAXBElement.getValue();
                        LOG.debug("Found GivenName " + givenName.getContent());
                        hasName = true;
                    }
                }
            }
            // If text string, then set in familyName
            // else set in element.
            if (nameString != null && hasName == false) {
                LOG.debug("set org name text ");
                personName.setFamilyName(nameString);
            } else {
                if (givenName.getContent() != null && givenName.getContent().length() > 0) {
                    LOG.debug("set org name given ");
                    personName.setGivenName(givenName.getContent());

                }
                if (familyName.getContent() != null && familyName.getContent().length() > 0) {
                    LOG.debug("set org name family ");
                    personName.setFamilyName(familyName.getContent());
                }

            }
        }
        LOG.debug("HL7Extractor.translatePNListtoPersonNameType() -- End");
        return personName;

    }

    /**
     * This method translates a List of EN objects to a PersonName object.
     *
     * @param The List<EN> objects to be translated
     * @param The PersonName object
     */
    public static PersonNameType translateENListtoPersonNameType(List<ENExplicit> names) {
        LOG.debug("HL7Extractor.translateENListtoPersonNameType() -- Begin");
        PersonNameType personName = new PersonNameType();
        // NameType
        if (CollectionUtils.isNotEmpty(names) && CollectionUtils.isNotEmpty(names.get(0).getUse())) {
            CeType nameType = new CeType();
            nameType.setCode(names.get(0).getUse().get(0));
            personName.setNameType(nameType);
        }
        if (CollectionUtils.isNotEmpty(names) && names.get(0).getContent() != null) {
            List<Serializable> choice = names.get(0).getContent();
            Iterator<Serializable> iterSerialObjects = choice.iterator();

            String nameString = "";
            EnExplicitFamily familyName = new EnExplicitFamily();
            EnExplicitGiven givenName = new EnExplicitGiven();

            while (iterSerialObjects.hasNext()) {
                Serializable contentItem = iterSerialObjects.next();

                if (contentItem instanceof String) {
                    String strValue = (String) contentItem;
                    if (!nameString.isEmpty()) {
                        nameString += strValue;
                    } else {
                        nameString = strValue;
                    }
                    LOG.debug("Here is the contents of the string: {}", strValue);
                } else if (contentItem instanceof JAXBElement) {
                    JAXBElement oJAXBElement = (JAXBElement) contentItem;

                    if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                        familyName = (EnExplicitFamily) oJAXBElement.getValue();

                    } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                        givenName = (EnExplicitGiven) oJAXBElement.getValue();
                    }
                }

            }
            // If text string in HomeCommunity.representativeOrg, then set in familyName
            // else set in element.
            if (!nameString.isEmpty()) {
                LOG.debug("set org name text ");
                personName.setFamilyName(nameString);
            } else {
                if (givenName.getContent() != null && givenName.getContent().length() > 0) {
                    LOG.debug("set org name given ");
                    personName.setGivenName(givenName.getContent());

                }
                if (familyName.getContent() != null && familyName.getContent().length() > 0) {
                    LOG.debug("set org name family ");
                    personName.setFamilyName(familyName.getContent());
                }

            }

        }
        LOG.debug("HL7Extractor.translateENListtoPersonNameType() -- End");
        return personName;
    }

    public static PRPAMT201301UV02Person ExtractHL7PatientPersonFromHL7Patient(PRPAMT201301UV02Patient patient) {
        JAXBElement<PRPAMT201301UV02Person> patientPersonElement = patient.getPatientPerson();
        return patientPersonElement.getValue();
    }

    public static PRPAMT201302UV02Person ExtractHL7PatientPersonFromHL7Patient(PRPAMT201302UV02Patient patient) {
        JAXBElement<PRPAMT201302UV02PatientPatientPerson> patientPersonElement = patient.getPatientPerson();
        return patientPersonElement.getValue();
    }

    public static PRPAMT201301UV02Person ExtractHL7PatientPersonFrom201301Message(org.hl7.v3.PRPAIN201301UV02 message) {
        // assume one subject for now
        PRPAMT201301UV02Patient patient = ExtractHL7PatientFromMessage(message);
        if (patient != null) {
            return ExtractHL7PatientPersonFromHL7Patient(patient);
        }
        return null;
    }

    public static PRPAMT201302UV02Person ExtractHL7PatientPersonFrom201302Message(org.hl7.v3.PRPAIN201302UV02 message) {
        // assume one subject for now
        PRPAMT201302UV02Patient patient = ExtractHL7PatientFromMessage(message);
        if (patient != null) {
            return ExtractHL7PatientPersonFromHL7Patient(patient);
        }
        return null;
    }

    public static String ExtractHL7ReceiverOID(org.hl7.v3.PRPAIN201305UV02 oPRPAIN201305UV) {
        String sReceiverOID = null;

        if (oPRPAIN201305UV != null && NullChecker.isNotNullish(oPRPAIN201305UV.getReceiver())
            && oPRPAIN201305UV.getReceiver().get(0) != null && oPRPAIN201305UV.getReceiver().get(0).getDevice() != null
            && oPRPAIN201305UV.getReceiver().get(0).getDevice().getAsAgent() != null
            && oPRPAIN201305UV.getReceiver().get(0).getDevice().getAsAgent().getValue() != null
            && oPRPAIN201305UV.getReceiver().get(0).getDevice().getAsAgent().getValue()
                .getRepresentedOrganization() != null
            && oPRPAIN201305UV.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue() != null
            && NullChecker.isNotNullish(oPRPAIN201305UV.getReceiver().get(0).getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId())
            && oPRPAIN201305UV.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue().getId().get(0) != null
            && NullChecker.isNotNullish(oPRPAIN201305UV.getReceiver().get(0).getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            sReceiverOID = oPRPAIN201305UV.getReceiver().get(0).getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot();
        }

        return sReceiverOID;
    }

    public static String ExtractHL7SenderOID(org.hl7.v3.PRPAIN201305UV02 oPRPAIN201305UV) {
        String sSenderOID = null;

        if (oPRPAIN201305UV != null && oPRPAIN201305UV.getSender().getDevice() != null
            && oPRPAIN201305UV.getSender().getDevice().getAsAgent() != null
            && oPRPAIN201305UV.getSender().getDevice().getAsAgent().getValue() != null
            && oPRPAIN201305UV.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null
            && oPRPAIN201305UV.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue() != null
            && NullChecker.isNotNullish(oPRPAIN201305UV.getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId())
            && oPRPAIN201305UV.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
                .getId().get(0) != null
            && NullChecker.isNotNullish(oPRPAIN201305UV.getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            sSenderOID = oPRPAIN201305UV.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue().getId().get(0).getRoot();
        }

        return sSenderOID;
    }
}
