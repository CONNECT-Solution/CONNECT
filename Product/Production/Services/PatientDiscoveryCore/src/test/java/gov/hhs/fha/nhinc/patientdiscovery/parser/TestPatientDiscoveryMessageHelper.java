/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientdiscovery.parser;

import gov.hhs.fha.nhinc.mpilib.Identifier;
import gov.hhs.fha.nhinc.mpilib.Identifiers;
import gov.hhs.fha.nhinc.mpilib.Patient;
import gov.hhs.fha.nhinc.mpilib.Patients;
import gov.hhs.fha.nhinc.mpilib.PersonName;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.ParticipationTargetSubject;
import org.hl7.v3.XActMoodIntentEvent;

/**
 * The class is used to create PD request and response messages to facilitate testing.
 *
 * @author tjafri
 */
public class TestPatientDiscoveryMessageHelper {

    public static final String ROOT = "1.1";
    public static final String EXTENSION = "D123401";
    public static final String QUERY_ID_ROOT = "2.2";
    public static final String QUERY_ID_EXTENSION = "abd3453dcd24wkkks545";

    public static PRPAIN201305UV02 createPRPAIN201305UV02Request() {
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        request.setControlActProcess(createControlActProcess());
        return request;
    }

    public static PRPAIN201306UV02 createPRPAIN201306UV02Response() {
        PRPAIN201306UV02 response = new PRPAIN201306UV02();
        response.setControlActProcess(createResponseControlActProcess());
        return response;
    }

    private static PRPAIN201305UV02QUQIMT021001UV01ControlActProcess createControlActProcess() {
        II subjectId = new II();
        subjectId.setRoot(ROOT);
        subjectId.setExtension(EXTENSION);

        CD code = new CD();
        code.setCode("PRPA_TE201306UV02");
        code.setCodeSystem("2.16.840.1.113883.1.6");

        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess
            = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        controlActProcess.setClassCode(ActClassControlAct.CACT);
        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setCode(code);
        controlActProcess.setQueryByParameter(createQueryParams("Allen", "Wanderson", "M", "09-8-1971", subjectId));
        return controlActProcess;
    }

    private static JAXBElement<PRPAMT201306UV02QueryByParameter> createQueryParams(String firstName, String lastName,
        String gender, String birthTime, II subjectId) {

        II id = new II();
        id.setRoot(QUERY_ID_ROOT);
        id.setExtension(QUERY_ID_EXTENSION);
        CS statusCode = new CS();
        statusCode.setCode("new");

        PRPAMT201306UV02QueryByParameter params = new PRPAMT201306UV02QueryByParameter();
        params.setQueryId(id);
        params.setStatusCode(statusCode);
        params.setParameterList(createParamList(firstName, lastName, gender, birthTime, subjectId));

        return new JAXBElement<>(new QName("urn:hl7-org:v3", "queryByParameter"),
            PRPAMT201306UV02QueryByParameter.class, params);
    }

    private static PRPAMT201306UV02ParameterList createParamList(String firstName, String lastName, String gender,
        String birthTime, II subjectId) {

        PRPAMT201306UV02ParameterList paramList = new PRPAMT201306UV02ParameterList();

        // Set the Subject Gender Code
        paramList.getLivingSubjectAdministrativeGender().add(createGender(gender));

        // Set the Subject Birth Time
        paramList.getLivingSubjectBirthTime().add(createBirthTime(birthTime));

        // Set the Subject Name
        paramList.getLivingSubjectName().add(createName(firstName, lastName));

        // Set the subject Id
        paramList.getLivingSubjectId().add(createSubjectId(subjectId));

        return paramList;
    }

    private static PRPAMT201306UV02LivingSubjectId createSubjectId(II subjectId) {
        PRPAMT201306UV02LivingSubjectId id = new PRPAMT201306UV02LivingSubjectId();
        if (subjectId != null) {
            id.getValue().add(subjectId);
        }

        return id;
    }

    private static PRPAMT201306UV02LivingSubjectName createName(String firstName, String lastName) {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        ENExplicit name = factory.createENExplicit();
        List namelist = name.getContent();

        if (lastName != null && lastName.length() > 0) {
            EnExplicitFamily familyName = new EnExplicitFamily();
            familyName.setPartType("FAM");
            familyName.setContent(lastName);

            namelist.add(factory.createENExplicitFamily(familyName));
        }

        if (firstName != null && firstName.length() > 0) {
            EnExplicitGiven givenName = new EnExplicitGiven();
            givenName.setPartType("GIV");
            givenName.setContent(firstName);

            namelist.add(factory.createENExplicitGiven(givenName));
        }

        PRPAMT201306UV02LivingSubjectName subjectName = new PRPAMT201306UV02LivingSubjectName();
        subjectName.getValue().add(name);
        return subjectName;
    }

    private static PRPAMT201306UV02LivingSubjectBirthTime createBirthTime(String birthTime) {
        PRPAMT201306UV02LivingSubjectBirthTime subjectBirthTime = new PRPAMT201306UV02LivingSubjectBirthTime();
        IVLTSExplicit bday = new IVLTSExplicit();

        if (birthTime != null && birthTime.length() > 0) {
            bday.setValue(birthTime);
            subjectBirthTime.getValue().add(bday);
        }

        return subjectBirthTime;
    }

    private static PRPAMT201306UV02LivingSubjectAdministrativeGender createGender(String gender) {
        PRPAMT201306UV02LivingSubjectAdministrativeGender adminGender
            = new PRPAMT201306UV02LivingSubjectAdministrativeGender();

        if (gender != null && gender.length() > 0) {
            CE genderCode = new CE();
            genderCode.setCode(gender);
            adminGender.getValue().add(genderCode);
        }

        return adminGender;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01ControlActProcess createResponseControlActProcess() {
        PRPAIN201305UV02 query = createPRPAIN201305UV02Request();

        Patients patients = createPatients();

        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess
            = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setClassCode(ActClassControlAct.CACT);

        CD code = new CD();
        code.setCode("PRPA_TE201306UV02");
        code.setCodeSystem("2.16.840.1.113883.1.6");
        controlActProcess.setCode(code);

        if (patients != null && !patients.isEmpty()) {
            for (Patient patientEntry : patients) {
                controlActProcess.getSubject().add(createSubject(patientEntry, query));
            }
        }

        // Add in query parameters
        if (query.getControlActProcess() != null && query.getControlActProcess().getQueryByParameter() != null
            && query.getControlActProcess().getQueryByParameter().getValue() != null) {
            controlActProcess.setQueryByParameter(query.getControlActProcess().getQueryByParameter());
        }
        // Set original QueryByParameter in response
        controlActProcess.setQueryByParameter(query.getControlActProcess().getQueryByParameter());
        return controlActProcess;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01Subject1 createSubject(Patient patient, PRPAIN201305UV02 query) {
        PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject1();
        subject.getTypeCode().add("SUBJ");
        subject.setRegistrationEvent(createRegEvent(patient, query));

        return subject;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent createRegEvent(Patient patient,
        PRPAIN201305UV02 query) {

        II id = new II();
        id.getNullFlavor().add("NA");

        CS statusCode = new CS();
        statusCode.setCode("active");

        PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent regEvent
            = new PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent();

        regEvent.getMoodCode().add("EVN");
        regEvent.getClassCode().add("REG");
        regEvent.getId().add(id);
        regEvent.setStatusCode(statusCode);
        regEvent.setSubject1(createSubject1(patient, query));

        return regEvent;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01Subject2 createSubject1(Patient patient, PRPAIN201305UV02 query) {
        PRPAIN201306UV02MFMIMT700711UV01Subject2 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject2();
        subject.setTypeCode(ParticipationTargetSubject.SBJ);
        // Add in patient
        subject.setPatient(createPatient(patient, query));

        return subject;
    }

    private static PRPAMT201310UV02Patient createPatient(Patient patient, PRPAIN201305UV02 query) {
        CS statusCode = new CS();
        statusCode.setCode("SD");

        PRPAMT201310UV02Patient subjectPatient = new PRPAMT201310UV02Patient();
        subjectPatient.getClassCode().add("PAT");
        subjectPatient.setStatusCode(statusCode);
        // Add in patient id
        subjectPatient.getId().add(createSubjectId(patient));
        return subjectPatient;
    }

    private static II createSubjectId(Patient patient) {
        II id = new II();

        if (patient.getIdentifiers() != null && !patient.getIdentifiers().isEmpty()
            && patient.getIdentifiers().get(0) != null) {

            Identifier subjectId = patient.getIdentifiers().get(0);

            if (subjectId.getOrganizationId() != null && subjectId.getOrganizationId().length() > 0) {
                id.setRoot(HomeCommunityMap.formatHomeCommunityId(subjectId.getOrganizationId()));
            }

            if (subjectId.getId() != null && subjectId.getId().length() > 0) {
                id.setExtension(patient.getIdentifiers().get(0).getId());
            }
        }

        return id;
    }

    private static Identifier createIdentifier() {
        Identifier subjectId = new Identifier();
        subjectId.setId("D123401");
        subjectId.setOrganizationId("1.1");
        return subjectId;
    }

    private static Patients createPatients() {
        Patient patient = createMpiPatient("Gallow", "Younger", "M", "08-20-1976", createIdentifier());
        Patients patients = new Patients();
        patients.add(patient);
        return patients;
    }

    private static Patient createMpiPatient(String firstName, String lastName, String gender, String birthTime,
        Identifier subjectId) {

        Patient result = new Patient();

        // Set the patient name
        PersonName name = new PersonName();
        name.setFirstName(firstName);
        name.setLastName(lastName);
        result.getNames().add(name);

        // Set the patient gender
        result.setGender(gender);

        // Set the patient birth time
        result.setDateOfBirth(birthTime);

        // Set the patient Id
        Identifiers ids = new Identifiers();
        ids.add(subjectId);
        result.setIdentifiers(ids);

        return result;
    }

}
