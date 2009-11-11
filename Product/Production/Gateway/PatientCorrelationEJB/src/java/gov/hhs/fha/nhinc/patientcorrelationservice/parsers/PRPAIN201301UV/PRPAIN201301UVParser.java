/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelationservice.parsers.PRPAIN201301UV;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject2;
import org.hl7.v3.PRPAMT201301UV02Patient;

/**
 *
 * @author svalluripalli
 */
public class PRPAIN201301UVParser {
    private static Log log = LogFactory.getLog(PRPAIN201301UVParser.class);
    
    /**
     * This method gets the patientPerson from HL7V3 message of type PRPAIN201301UV
     * @param message of type PRPAIN201301UV
     * @return PRPAMT201301UVPerson
     */
    public static PRPAMT201301UV02Patient ParseHL7PatientPersonFrom201301Message(org.hl7.v3.PRPAIN201301UV02 message) {
        //assume one subject for now
        PRPAMT201301UV02Patient patient = ParseHL7PatientFromMessage(message);
        //PRPAMT201301UVPerson patientPerson = ParseHL7PatientPersonFromHL7Patient(patient);
        return patient;
    }
    
    /**
     * This method extracts Patient of type HL7V3 PRPAMT201301UVPatient from HL7V3 PRPAIN201301UV
     * @param message of type PRPAIN201301UV
     * @return PRPAMT201301UVPatient
     */
    public static PRPAMT201301UV02Patient ParseHL7PatientFromMessage(org.hl7.v3.PRPAIN201301UV02 message) {
        PRPAMT201301UV02Patient patient = null;
        log.info("in ExtractPatient");

        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = ParseSubjectFromMessage(message);
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
    
    public static PRPAIN201301UV02MFMIMT700701UV01Subject1 ParseSubjectFromMessage(org.hl7.v3.PRPAIN201301UV02 message) {
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
}
