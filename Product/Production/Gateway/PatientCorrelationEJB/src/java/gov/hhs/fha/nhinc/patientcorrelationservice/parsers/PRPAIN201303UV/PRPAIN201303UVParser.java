/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientcorrelationservice.parsers.PRPAIN201303UV;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201303UV;
import org.hl7.v3.PRPAIN201303UVMFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAMT201305UVPatient;

/**
 *
 * @author svalluripalli
 */
public class PRPAIN201303UVParser {

    private static Log log = LogFactory.getLog(PRPAIN201303UVParser.class);

    public static PRPAMT201305UVPatient parseHL7PatientPersonFrom201303Message(PRPAIN201303UV message) {
        if (message == null) {
            return null;
        }
        List<PRPAIN201303UVMFMIMT700701UV01Subject1> subject1s = message.getControlActProcess().getSubject();
        PRPAMT201305UVPatient patient = null;
        if (subject1s == null) {
            return null;
        }
        PRPAIN201303UVMFMIMT700701UV01Subject1 subject = subject1s.get(0);
        if (subject.getRegistrationEvent() != null &&
                subject.getRegistrationEvent().getSubject1() != null &&
                subject.getRegistrationEvent().getSubject1().getPatient() != null) {

            if (subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson() != null) {
                patient = subject.getRegistrationEvent().getSubject1().getPatient();
            }
        }
        return patient;
    }
}
