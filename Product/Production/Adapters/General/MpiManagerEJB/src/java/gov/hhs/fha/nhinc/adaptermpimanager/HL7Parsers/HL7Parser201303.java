/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adaptermpimanager.HL7Parsers;

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
 * @author jhoppesc
 */
public class HL7Parser201303 {
    private static Log log = LogFactory.getLog(HL7Parser201303.class);
    public static void PrintMessageIdFromMessage(org.hl7.v3.PRPAIN201303UV message) {
        if (!(message == null)) {
            HL7Parser.PrintId(message.getId(), "message");
        }
    }
   
    public static PRPAMT201305UVPatient ExtractHL7PatientFromMessage(org.hl7.v3.PRPAIN201303UV message) {
        //assume one subject for now
        PRPAMT201305UVPatient patient = null;
        log.info("in ExtractPatient");

        if (message != null &&
                message.getControlActProcess() != null &&
                message.getControlActProcess().getSubject() != null &&
                message.getControlActProcess().getSubject().size() > 0) {
            List<PRPAIN201303UVMFMIMT700701UV01Subject1> subjects = message.getControlActProcess().getSubject();

            //for now, assume we only need one subject, this will need to be modified later
            PRPAIN201303UVMFMIMT700701UV01Subject1 subject = subjects.get(0);

            patient = subject.getRegistrationEvent().getSubject1().getPatient();
        } else {
            log.info("no message.controlactprocess.subjects");
        }

        log.info("done with ExtractPatient");
        return patient;
    }
    
    public static String ExtractGender(PRPAMT201305UVPerson person) {
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

    public static String ExtractBirthdate(PRPAMT201305UVPerson person) {
        String birthDate = person.getBirthTime().getValue();
        if (birthDate == null) {
            log.info("message does not contain a birthtime");
        } else {
            log.info("person.getBirthTime().getValue()=" + birthDate);
        }
        return birthDate;
    }

    public static PersonName ExtractPersonName(PRPAMT201305UVPerson person) {
        //temp logic to prove i can get to name - long term would want to store discrete name parts
        //also assume one name, not multiple names
        PersonName personname = new PersonName();

        log.info("patientPerson.getName().size() " + person.getName().size());
        if (person.getName() != null &&
                person.getName().size() > 0 &&
                person.getName().get(0) != null &&
                person.getName().get(0).getContent() != null) {
        
            List<Serializable> choice = person.getName().get(0).getContent();
            Iterator<Serializable> iterSerialObjects = choice.iterator();
            
            String nameString = null;
            EnExplicitFamily lastname = new EnExplicitFamily();
            EnExplicitGiven firstname = new EnExplicitGiven();

            while (iterSerialObjects.hasNext()) {
                Serializable contentItem = iterSerialObjects.next();

                if (contentItem instanceof String) {
                    String strValue = (String) contentItem;
                    
                    if (nameString != null) {
                        nameString += strValue;
                    } else {
                        nameString = strValue;
                    }
                } else if (contentItem instanceof JAXBElement) {
                    JAXBElement oJAXBElement = (JAXBElement) contentItem;

                    if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                        lastname = (EnExplicitFamily) oJAXBElement.getValue();                        
                    } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                        firstname = (EnExplicitGiven) oJAXBElement.getValue();                        
                    }
                }
            }
            
            // If text string in patient name, then set in name
            // else set in element.
            if (nameString != null) {
                personname.setLastName(nameString);
            } else {
                if (lastname.getContent() != null &&
                        lastname.getContent().length() > 0) {
                    personname.setLastName(lastname.getContent());
                    log.info("FamilyName : " + personname.getLastName());
                } 
                
                if (firstname.getContent() != null &&
                           firstname.getContent().length() > 0) {
                    personname.setFirstName(firstname.getContent());
                    log.info("GivenName : " + personname.getFirstName());
                }
            }
        }
        
        return personname;
    }

    public static Identifiers ExtractPersonIdentifiers(PRPAMT201305UVPatient patient) {
        Identifiers ids = new Identifiers();
        for (II patientid : patient.getId()) {
            Identifier id = new Identifier();
            id.setId(patientid.getExtension());
            id.setOrganizationId(patientid.getRoot());
            log.info("Created id from patient identifier [organization=" + id.getOrganizationId() + "][id=" + id.getId() + "]");
            ids.add(id);
        }

        PRPAMT201305UVPerson person = ExtractHL7PatientPersonFromHL7Patient(patient);
        for (II personid : person.getId()) {
            Identifier id = new Identifier();
            id.setId(personid.getExtension());
            id.setOrganizationId(personid.getRoot());
            log.info("Created id from person identifier [organization=" + id.getOrganizationId() + "][id=" + id.getId() + "]");
            ids.add(id);
        }

        return ids;
    }
    
    public static PRPAMT201305UVPerson ExtractHL7PatientPersonFromHL7Patient(PRPAMT201305UVPatient patient) {
        JAXBElement<PRPAMT201305UVPerson> patientPersonElement = patient.getPatientPerson();
        PRPAMT201305UVPerson patientPerson = patientPerson = patientPersonElement.getValue();
        return patientPerson;
    }
    
    public static Patient ExtractMpiPatientFromHL7Patient(PRPAMT201305UVPatient patient) {
        PRPAMT201305UVPerson patientPerson = ExtractHL7PatientPersonFromHL7Patient(patient);
        Patient mpiPatient = new Patient();
        mpiPatient.setName(ExtractPersonName(patientPerson));
        mpiPatient.setGender(ExtractGender(patientPerson));
        mpiPatient.setDateOfBirth(ExtractBirthdate(patientPerson));
        
        Identifiers ids = ExtractPersonIdentifiers(patient);
        mpiPatient.setIdentifiers(ids);
        return mpiPatient;
    }
}
