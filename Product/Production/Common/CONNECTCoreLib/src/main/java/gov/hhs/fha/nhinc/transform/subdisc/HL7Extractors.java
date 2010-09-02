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
package gov.hhs.fha.nhinc.transform.subdisc;

import java.util.List;
import java.io.Serializable;
import java.util.Iterator;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 *
 * @author MFLYNN02
 */
public class HL7Extractors {

    private static Log log = LogFactory.getLog(HL7Extractors.class);

    public static PRPAIN201301UV02MFMIMT700701UV01Subject1 ExtractSubjectFromMessage(org.hl7.v3.PRPAIN201301UV02 message) {
        //assume one subject for now

        if (message == null) {
            log.info("message is null - no patient");
            return null;
        }
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = message.getControlActProcess();
        if (controlActProcess == null) {
            log.info("controlActProcess is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(controlActProcess.getId(), "controlActProcess");

        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = controlActProcess.getSubject();
        if ((subjects == null) || (subjects.size() == 0)) {
            log.info("subjects is blank/null - no patient");
            return null;
        }

        //for now, assume we only need one subject, this will need to be modified later
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = subjects.get(0);
        //HL7Parser.PrintId(subject.getTypeId(), "subject");

        return subject;
    }

    public static PRPAIN201302UV02MFMIMT700701UV01Subject1 ExtractSubjectFromMessage(org.hl7.v3.PRPAIN201302UV02 message) {
        //assume one subject for now

        if (message == null) {
            log.info("message is null - no patient");
            return null;
        }
        PRPAIN201302UV02MFMIMT700701UV01ControlActProcess controlActProcess = message.getControlActProcess();
        if (controlActProcess == null) {
            log.info("controlActProcess is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(controlActProcess.getId(), "controlActProcess");

        List<PRPAIN201302UV02MFMIMT700701UV01Subject1> subjects = controlActProcess.getSubject();
        if ((subjects == null) || (subjects.size() == 0)) {
            log.info("subjects is blank/null - no patient");
            return null;
        }

        //for now, assume we only need one subject, this will need to be modified later
        PRPAIN201302UV02MFMIMT700701UV01Subject1 subject = subjects.get(0);
        //HL7Parser.PrintId(subject.getTypeId(), "subject");

        return subject;
    }
    

    public static PRPAIN201310UV02MFMIMT700711UV01Subject1 ExtractSubjectFromMessage(org.hl7.v3.PRPAIN201310UV02 message) {
        //assume one subject for now

        if (message == null) {
            log.info("message is null - no patient");
            return null;
        }
        
        PRPAIN201310UV02MFMIMT700711UV01ControlActProcess controlActProcess = message.getControlActProcess();
        if (controlActProcess == null) {
            log.info("controlActProcess is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(controlActProcess.getId(), "controlActProcess");

        List<PRPAIN201310UV02MFMIMT700711UV01Subject1> subjects = controlActProcess.getSubject();
        if ((subjects == null) || (subjects.size() == 0)) {
            log.info("subjects is blank/null - no patient");
            return null;
        }

        //for now, assume we only need one subject, this will need to be modified later
        PRPAIN201310UV02MFMIMT700711UV01Subject1 subject = subjects.get(0);
        //HL7Parser.PrintId(subject.getTypeId(), "subject");

        return subject;
    }

    public static PRPAMT201301UV02Patient ExtractHL7PatientFromMessage(org.hl7.v3.PRPAIN201301UV02 message) {
        PRPAMT201301UV02Patient patient = null;
        log.info("in ExtractPatient");

        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = ExtractSubjectFromMessage(message);
        if (subject == null) {
            return null;
        }
        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent registrationevent = subject.getRegistrationEvent();
        if (registrationevent == null) {
            log.info("registrationevent is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(registrationevent.getTypeId(), "registrationevent");

        PRPAIN201301UV02MFMIMT700701UV01Subject2 subject1 = registrationevent.getSubject1();
        if (subject1 == null) {
            log.info("subject1 is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(subject1.getTypeId(), "subject1");

        patient = subject1.getPatient();
        if (patient == null) {
            log.info("patient is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(patient.getId(), "patient");

        log.info("done with ExtractPatient");
        return patient;
    }

    public static PRPAMT201302UV02Patient ExtractHL7PatientFromMessage(org.hl7.v3.PRPAIN201302UV02 message) {
        PRPAMT201302UV02Patient patient = null;
        log.info("in ExtractPatient");

        PRPAIN201302UV02MFMIMT700701UV01Subject1 subject = ExtractSubjectFromMessage(message);
        if (subject == null) {
            return null;
        }
        PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent registrationevent = subject.getRegistrationEvent();
        if (registrationevent == null) {
            log.info("registrationevent is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(registrationevent.getTypeId(), "registrationevent");

        PRPAIN201302UV02MFMIMT700701UV01Subject2 subject1 = registrationevent.getSubject1();
        if (subject1 == null) {
            log.info("subject1 is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(subject1.getTypeId(), "subject1");

        patient = subject1.getPatient();
        if (patient == null) {
            log.info("patient is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(patient.getId(), "patient");

        log.info("done with ExtractPatient");
        return patient;
    }
  
    public static PRPAMT201304UV02Patient ExtractHL7PatientFromMessage(org.hl7.v3.PRPAIN201310UV02 message) {
        PRPAMT201304UV02Patient patient = null;
        log.info("in ExtractPatient");

        PRPAIN201310UV02MFMIMT700711UV01Subject1 subject = ExtractSubjectFromMessage(message);
        if (subject == null) {
            return null;
        }
        
        PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent registrationevent = subject.getRegistrationEvent();
        if (registrationevent == null) {
            log.info("registrationevent is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(registrationevent.getTypeId(), "registrationevent");

        PRPAIN201310UV02MFMIMT700711UV01Subject2 subject1 = registrationevent.getSubject1();
        if (subject1 == null) {
            log.info("subject1 is null - no patient");
            return null;
        }

        patient = subject1.getPatient();
        if (patient == null) {
            log.info("patient is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(patient.getId(), "patient");

        log.info("done with ExtractPatient");
        return patient;
    }

    /**
     * This method translates a List<PN> into an PersonName object.
     *
     * @param The List of PN objects to be translated
     * @param The Person object 
     */
    public static PersonNameType translatePNListtoPersonNameType(List<PNExplicit> names) {
        log.debug("HL7Extractor.translatePNListtoPersonNameType() -- Begin");
        PersonNameType personName = new PersonNameType();
        // NameType
        if (names.get(0).getUse().size() > 0) {
            CeType nameType = new CeType();
            nameType.setCode(names.get(0).getUse().get(0));
            personName.setNameType(nameType);
        }
        // Name parts
        if (names.size() > 0 &&
                names.get(0).getContent() != null) {
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
                    if (nameString != null) {
                        nameString += strValue;
                    } else {
                        nameString = strValue;
                    }
                    System.out.println("Here is the contents of the string: " + strValue);
                } else if (contentItem instanceof JAXBElement) {
                    JAXBElement oJAXBElement = (JAXBElement) contentItem;
                    log.debug("Found JAXBElement");
                    if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                        familyName = (EnExplicitFamily) oJAXBElement.getValue();
                        log.debug("Found FamilyName " + familyName.getContent());
                        hasName = true;
                    } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                        givenName = (EnExplicitGiven) oJAXBElement.getValue();
                        log.debug("Found GivenName " + givenName.getContent());
                        hasName = true;
                    }
                }
            }
            // If text string, then set in familyName
            // else set in element.
            if (nameString != null && hasName == false) {
                log.debug("set org name text ");
                personName.setFamilyName(nameString);
            } else {
                if (givenName.getContent() != null &&
                        givenName.getContent().length() > 0) {
                    log.debug("set org name given ");
                    personName.setGivenName(givenName.getContent());

                }
                if (familyName.getContent() != null &&
                        familyName.getContent().length() > 0) {
                    log.debug("set org name family ");
                    personName.setFamilyName(familyName.getContent());
                }

            }
        }
        log.debug("HL7Extractor.translatePNListtoPersonNameType() -- End");
        return personName;

    }

    /**
     * This method translates a List of EN objects to a PersonName object.
     *
     * @param The List<EN> objects to be translated
     * @param The PersonName object
     */
    public static PersonNameType translateENListtoPersonNameType(List<ENExplicit> names) {
        log.debug("HL7Extractor.translateENListtoPersonNameType() -- Begin");
        PersonNameType personName = new PersonNameType();
        // NameType
        if (names.size() > 0 && names.get(0).getUse().size() > 0) {
            CeType nameType = new CeType();
            nameType.setCode(names.get(0).getUse().get(0));
            personName.setNameType(nameType);
        }
        if (names.size() > 0 &&
                names.get(0).getContent() != null) {
            List<Serializable> choice = names.get(0).getContent();
            Iterator<Serializable> iterSerialObjects = choice.iterator();

            String nameString = "";
            EnExplicitFamily familyName = new EnExplicitFamily();
            EnExplicitGiven givenName = new EnExplicitGiven();

            while (iterSerialObjects.hasNext()) {
                Serializable contentItem = iterSerialObjects.next();

                if (contentItem instanceof String) {
                    String strValue = (String) contentItem;
                    if (nameString != null) {
                        nameString += strValue;
                    } else {
                        nameString = strValue;
                    }
                    System.out.println("Here is the contents of the string: " + strValue);
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
            if (nameString != null) {
                log.debug("set org name text ");
                personName.setFamilyName(nameString);
            } else {
                if (givenName.getContent() != null &&
                        givenName.getContent().length() > 0) {
                    log.debug("set org name given ");
                    personName.setGivenName(givenName.getContent());

                }
                if (familyName.getContent() != null &&
                        familyName.getContent().length() > 0) {
                    log.debug("set org name family ");
                    personName.setFamilyName(familyName.getContent());
                }

            }

        }
        log.debug("HL7Extractor.translateENListtoPersonNameType() -- End");
        return personName;
    }

    public static PRPAMT201301UV02Person ExtractHL7PatientPersonFromHL7Patient(PRPAMT201301UV02Patient patient) {
        JAXBElement<PRPAMT201301UV02Person> patientPersonElement = patient.getPatientPerson();
        PRPAMT201301UV02Person patientPerson = patientPerson = patientPersonElement.getValue();
        return patientPerson;
    }

    public static PRPAMT201302UV02Person ExtractHL7PatientPersonFromHL7Patient(PRPAMT201302UV02Patient patient) {
        JAXBElement<PRPAMT201302UV02PatientPatientPerson> patientPersonElement = patient.getPatientPerson();
        PRPAMT201302UV02Person patientPerson = patientPerson = patientPersonElement.getValue();
        return patientPerson;
    }

    public static PRPAMT201301UV02Person ExtractHL7PatientPersonFrom201301Message(org.hl7.v3.PRPAIN201301UV02 message) {
        //assume one subject for now
        PRPAMT201301UV02Patient patient = ExtractHL7PatientFromMessage(message);
        PRPAMT201301UV02Person patientPerson = ExtractHL7PatientPersonFromHL7Patient(patient);
        return patientPerson;
    }

    public static PRPAMT201302UV02Person ExtractHL7PatientPersonFrom201302Message(org.hl7.v3.PRPAIN201302UV02 message) {
        //assume one subject for now
        PRPAMT201302UV02Patient patient = ExtractHL7PatientFromMessage(message);
        PRPAMT201302UV02Person patientPerson = ExtractHL7PatientPersonFromHL7Patient(patient);
        return patientPerson;
    }

    public static String ExtractHL7ReceiverOID (org.hl7.v3.PRPAIN201305UV02 oPRPAIN201305UV)
    {
        String sReceiverOID = null;

        if (oPRPAIN201305UV != null &&
                NullChecker.isNotNullish(oPRPAIN201305UV.getReceiver()) &&
                oPRPAIN201305UV.getReceiver().get(0) != null &&
                oPRPAIN201305UV.getReceiver().get(0).getDevice() != null &&
                oPRPAIN201305UV.getReceiver().get(0).getDevice().getAsAgent() != null &&
                oPRPAIN201305UV.getReceiver().get(0).getDevice().getAsAgent().getValue() != null &&
                oPRPAIN201305UV.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                oPRPAIN201305UV.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(oPRPAIN201305UV.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                oPRPAIN201305UV.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(oPRPAIN201305UV.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            sReceiverOID = oPRPAIN201305UV.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        }

        return sReceiverOID;
    }

    public static String ExtractHL7SenderOID (org.hl7.v3.PRPAIN201305UV02 oPRPAIN201305UV)
    {
        String sSenderOID = null;

        if (oPRPAIN201305UV != null &&
                oPRPAIN201305UV.getSender().getDevice() != null &&
                oPRPAIN201305UV.getSender().getDevice().getAsAgent() != null &&
                oPRPAIN201305UV.getSender().getDevice().getAsAgent().getValue() != null &&
                oPRPAIN201305UV.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                oPRPAIN201305UV.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(oPRPAIN201305UV.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                oPRPAIN201305UV.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(oPRPAIN201305UV.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            sSenderOID = oPRPAIN201305UV.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        }

        return sSenderOID;
    }

}
