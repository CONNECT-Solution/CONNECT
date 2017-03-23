/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientdiscovery.model.builder;

/**
 *
 * @author tjafri
 */
import java.util.ArrayList;
import java.util.List;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAMT201310UV02Patient;

public abstract class AbstractPatientSearchResultsModelBuilder {

    protected List<PRPAIN201306UV02MFMIMT700711UV01Subject1> getSubjects(PRPAIN201306UV02 response) {
        List<PRPAIN201306UV02MFMIMT700711UV01Subject1> subjects = new ArrayList<>();
        if (response != null && response.getControlActProcess() != null) {
            subjects = response.getControlActProcess().getSubject();
        }
        return subjects;
    }

    protected PRPAMT201310UV02Patient getSubject1Patient(PRPAIN201306UV02MFMIMT700711UV01Subject1 subject) {
        PRPAMT201310UV02Patient patient = new PRPAMT201310UV02Patient();
        if (subject != null && subject.getRegistrationEvent() != null
            && subject.getRegistrationEvent().getSubject1() != null
            && subject.getRegistrationEvent().getSubject1().getPatient() != null
            && subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson() != null
            && subject.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue() != null) {
            patient = subject.getRegistrationEvent().getSubject1().getPatient();
        }
        return patient;
    }
}
