/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201301UV;

import java.util.List;
import org.apache.log4j.Logger;
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

    private static final Logger LOG = Logger.getLogger(PRPAIN201301UVParser.class);

    /**
     * This method gets the patientPerson from HL7V3 message of type PRPAIN201301UV
     *
     * @param message of type PRPAIN201301UV
     * @return PRPAMT201301UVPerson
     */
    public static PRPAMT201301UV02Patient parseHL7PatientPersonFrom201301Message(org.hl7.v3.PRPAIN201301UV02 message) {
        // assume one subject for now
        PRPAMT201301UV02Patient patient = parseHL7PatientFromMessage(message);
        // PRPAMT201301UVPerson patientPerson = ParseHL7PatientPersonFromHL7Patient(patient);
        return patient;
    }

    /**
     * This method extracts Patient of type HL7V3 PRPAMT201301UVPatient from HL7V3 PRPAIN201301UV
     *
     * @param message of type PRPAIN201301UV
     * @return PRPAMT201301UVPatient
     */
    public static PRPAMT201301UV02Patient parseHL7PatientFromMessage(org.hl7.v3.PRPAIN201301UV02 message) {
        PRPAMT201301UV02Patient patient = null;
        LOG.info("in ExtractPatient");

        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = parseSubjectFromMessage(message);
        if (subject == null) {
            return null;
        }
        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent registrationevent = subject.getRegistrationEvent();
        if (registrationevent == null) {
            LOG.info("registrationevent is null - no patient");
            return null;
        }
        // HL7Parser.PrintId(registrationevent.getTypeId(), "registrationevent");

        PRPAIN201301UV02MFMIMT700701UV01Subject2 subject1 = registrationevent.getSubject1();
        if (subject1 == null) {
            LOG.info("subject1 is null - no patient");
            return null;
        }
        // HL7Parser.PrintId(subject1.getTypeId(), "subject1");

        patient = subject1.getPatient();
        if (patient == null) {
            LOG.info("patient is null - no patient");
            return null;
        }
        // HL7Parser.PrintId(patient.getId(), "patient");

        LOG.info("done with ExtractPatient");
        return patient;
    }

    public static PRPAIN201301UV02MFMIMT700701UV01Subject1 parseSubjectFromMessage(org.hl7.v3.PRPAIN201301UV02 message) {
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
        // HL7Parser.PrintId(controlActProcess.getId(), "controlActProcess");

        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = controlActProcess.getSubject();
        if ((subjects == null) || (subjects.size() == 0)) {
            LOG.info("subjects is blank/null - no patient");
            return null;
        }

        // for now, assume we only need one subject, this will need to be modified later
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = subjects.get(0);
        // HL7Parser.PrintId(subject.getTypeId(), "subject");

        return subject;
    }
}
