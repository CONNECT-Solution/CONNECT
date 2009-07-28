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

/**
 *
 * @author MFLYNN02
 */
public class HL7Extractors {

    private static Log log = LogFactory.getLog(HL7Extractors.class);

    public static PRPAIN201301UVMFMIMT700701UV01Subject1 ExtractSubjectFromMessage(org.hl7.v3.PRPAIN201301UV message) {
        //assume one subject for now

        if (message == null) {
            log.info("message is null - no patient");
            return null;
        }
        PRPAIN201301UVMFMIMT700701UV01ControlActProcess controlActProcess = message.getControlActProcess();
        if (controlActProcess == null) {
            log.info("controlActProcess is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(controlActProcess.getId(), "controlActProcess");

        List<PRPAIN201301UVMFMIMT700701UV01Subject1> subjects = controlActProcess.getSubject();
        if ((subjects == null) || (subjects.size() == 0)) {
            log.info("subjects is blank/null - no patient");
            return null;
        }

        //for now, assume we only need one subject, this will need to be modified later
        PRPAIN201301UVMFMIMT700701UV01Subject1 subject = subjects.get(0);
        //HL7Parser.PrintId(subject.getTypeId(), "subject");

        return subject;
    }

    public static PRPAIN201302UVMFMIMT700701UV01Subject1 ExtractSubjectFromMessage(org.hl7.v3.PRPAIN201302UV message) {
        //assume one subject for now

        if (message == null) {
            log.info("message is null - no patient");
            return null;
        }
        PRPAIN201302UVMFMIMT700701UV01ControlActProcess controlActProcess = message.getControlActProcess();
        if (controlActProcess == null) {
            log.info("controlActProcess is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(controlActProcess.getId(), "controlActProcess");

        List<PRPAIN201302UVMFMIMT700701UV01Subject1> subjects = controlActProcess.getSubject();
        if ((subjects == null) || (subjects.size() == 0)) {
            log.info("subjects is blank/null - no patient");
            return null;
        }

        //for now, assume we only need one subject, this will need to be modified later
        PRPAIN201302UVMFMIMT700701UV01Subject1 subject = subjects.get(0);
        //HL7Parser.PrintId(subject.getTypeId(), "subject");

        return subject;
    }
    
    public static PRPAIN201303UVMFMIMT700701UV01Subject1 ExtractSubjectFromMessage(org.hl7.v3.PRPAIN201303UV message) {
        //assume one subject for now

        if (message == null) {
            log.info("message is null - no patient");
            return null;
        }
        PRPAIN201303UVMFMIMT700701UV01ControlActProcess controlActProcess = message.getControlActProcess();
        if (controlActProcess == null) {
            log.info("controlActProcess is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(controlActProcess.getId(), "controlActProcess");

        List<PRPAIN201303UVMFMIMT700701UV01Subject1> subjects = controlActProcess.getSubject();
        if ((subjects == null) || (subjects.size() == 0)) {
            log.info("subjects is blank/null - no patient");
            return null;
        }

        //for now, assume we only need one subject, this will need to be modified later
        PRPAIN201303UVMFMIMT700701UV01Subject1 subject = subjects.get(0);
        //HL7Parser.PrintId(subject.getTypeId(), "subject");

        return subject;
    }
    
    public static PRPAIN201310UVMFMIMT700711UV01Subject1 ExtractSubjectFromMessage(org.hl7.v3.PRPAIN201310UV message) {
        //assume one subject for now

        if (message == null) {
            log.info("message is null - no patient");
            return null;
        }
        
        PRPAIN201310UVMFMIMT700711UV01ControlActProcess controlActProcess = message.getControlActProcess();
        if (controlActProcess == null) {
            log.info("controlActProcess is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(controlActProcess.getId(), "controlActProcess");

        List<PRPAIN201310UVMFMIMT700711UV01Subject1> subjects = controlActProcess.getSubject();
        if ((subjects == null) || (subjects.size() == 0)) {
            log.info("subjects is blank/null - no patient");
            return null;
        }

        //for now, assume we only need one subject, this will need to be modified later
        PRPAIN201310UVMFMIMT700711UV01Subject1 subject = subjects.get(0);
        //HL7Parser.PrintId(subject.getTypeId(), "subject");

        return subject;
    }

    public static PRPAMT201301UVPatient ExtractHL7PatientFromMessage(org.hl7.v3.PRPAIN201301UV message) {
        PRPAMT201301UVPatient patient = null;
        log.info("in ExtractPatient");

        PRPAIN201301UVMFMIMT700701UV01Subject1 subject = ExtractSubjectFromMessage(message);
        if (subject == null) {
            return null;
        }
        PRPAIN201301UVMFMIMT700701UV01RegistrationEvent registrationevent = subject.getRegistrationEvent();
        if (registrationevent == null) {
            log.info("registrationevent is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(registrationevent.getTypeId(), "registrationevent");

        PRPAIN201301UVMFMIMT700701UV01Subject2 subject1 = registrationevent.getSubject1();
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

    public static PRPAMT201302UVPatient ExtractHL7PatientFromMessage(org.hl7.v3.PRPAIN201302UV message) {
        PRPAMT201302UVPatient patient = null;
        log.info("in ExtractPatient");

        PRPAIN201302UVMFMIMT700701UV01Subject1 subject = ExtractSubjectFromMessage(message);
        if (subject == null) {
            return null;
        }
        PRPAIN201302UVMFMIMT700701UV01RegistrationEvent registrationevent = subject.getRegistrationEvent();
        if (registrationevent == null) {
            log.info("registrationevent is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(registrationevent.getTypeId(), "registrationevent");

        PRPAIN201302UVMFMIMT700701UV01Subject2 subject1 = registrationevent.getSubject1();
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

    public static PRPAMT201305UVPatient ExtractHL7PatientFromMessage(org.hl7.v3.PRPAIN201303UV message) {
        PRPAMT201305UVPatient patient = null;
        log.info("in ExtractPatient");

        PRPAIN201303UVMFMIMT700701UV01Subject1 subject = ExtractSubjectFromMessage(message);
        if (subject == null) {
            return null;
        }
        PRPAIN201303UVMFMIMT700701UV01RegistrationEvent registrationevent = subject.getRegistrationEvent();
        if (registrationevent == null) {
            log.info("registrationevent is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(registrationevent.getTypeId(), "registrationevent");

        PRPAIN201303UVMFMIMT700701UV01Subject2 subject1 = registrationevent.getSubject1();
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
    
    public static PRPAMT201304UVPatient ExtractHL7PatientFromMessage(org.hl7.v3.PRPAIN201310UV message) {
        PRPAMT201304UVPatient patient = null;
        log.info("in ExtractPatient");

        PRPAIN201310UVMFMIMT700711UV01Subject1 subject = ExtractSubjectFromMessage(message);
        if (subject == null) {
            return null;
        }
        
        PRPAIN201310UVMFMIMT700711UV01RegistrationEvent registrationevent = subject.getRegistrationEvent();
        if (registrationevent == null) {
            log.info("registrationevent is null - no patient");
            return null;
        }
        //HL7Parser.PrintId(registrationevent.getTypeId(), "registrationevent");

        PRPAIN201310UVMFMIMT700711UV01Subject2 subject1 = registrationevent.getSubject1();
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

    public static PRPAMT201301UVPerson ExtractHL7PatientPersonFromHL7Patient(PRPAMT201301UVPatient patient) {
        JAXBElement<PRPAMT201301UVPerson> patientPersonElement = patient.getPatientPerson();
        PRPAMT201301UVPerson patientPerson = patientPerson = patientPersonElement.getValue();
        return patientPerson;
    }

    public static PRPAMT201302UVPerson ExtractHL7PatientPersonFromHL7Patient(PRPAMT201302UVPatient patient) {
        JAXBElement<PRPAMT201302UVPerson> patientPersonElement = patient.getPatientPerson();
        PRPAMT201302UVPerson patientPerson = patientPerson = patientPersonElement.getValue();
        return patientPerson;
    }

    public static PRPAMT201301UVPerson ExtractHL7PatientPersonFrom201301Message(org.hl7.v3.PRPAIN201301UV message) {
        //assume one subject for now
        PRPAMT201301UVPatient patient = ExtractHL7PatientFromMessage(message);
        PRPAMT201301UVPerson patientPerson = ExtractHL7PatientPersonFromHL7Patient(patient);
        return patientPerson;
    }

    public static PRPAMT201302UVPerson ExtractHL7PatientPersonFrom201302Message(org.hl7.v3.PRPAIN201302UV message) {
        //assume one subject for now
        PRPAMT201302UVPatient patient = ExtractHL7PatientFromMessage(message);
        PRPAMT201302UVPerson patientPerson = ExtractHL7PatientPersonFromHL7Patient(patient);
        return patientPerson;
    }
}
