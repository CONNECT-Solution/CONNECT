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
package gov.hhs.fha.nhinc.patientdiscovery.parser;

import gov.hhs.fha.nhinc.mpilib.Identifier;
import gov.hhs.fha.nhinc.mpilib.Identifiers;
import gov.hhs.fha.nhinc.mpilib.Patient;
import gov.hhs.fha.nhinc.mpilib.Patients;
import gov.hhs.fha.nhinc.mpilib.PersonName;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import ihe.iti.xcpd._2009.PatientLocationQueryRequestType;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType.PatientLocationResponse;
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

    private static final String CNTRL_CODE = "PRPA_TE201306UV02";
    private static final String CNTRL_CODE_SYSTEM = "2.16.840.1.113883.1.6";
    private static final String CNTRL_SUBJ_TYPECODE = "SUBJ";
    private static final String STATUS_CD = "active";
    private static final String REG_MOOD_CODE = "EVN";
    private static final String REG_CLASS_CODE = "REG";
    private static final String QUERY_BY_PARAMETER_TAG = "queryByParameter";
    private static final String NAMESPACE_FOR_QUERY_BY_PARAMETER = "urn:hl7-org:v3";
    private static final String NEW = "new";
    private static final String NA = "NA";
    private static final String FAMILY_NAME_CODE = "FAM";
    private static final String GIVEN_NAME_CODE = "GIV";
    private static final String SUBJ_PATIENT_STATUS_CODE = "SD";
    private static final String SUBJ_PATIENT_CLASS_CODE = "PAT";

    public static PRPAIN201305UV02 createPRPAIN201305UV02Request(String firstName, String lastName, String gender,
        String birthTime, String root, String extension, String queryIdRoot, String queryIdExtension) {
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        request.setControlActProcess(createControlActProcess(firstName, lastName, gender, birthTime, root, extension,
            queryIdRoot, queryIdExtension));
        return request;
    }

    public static PRPAIN201306UV02 createPRPAIN201306UV02Response(String firstName, String lastName, String gender,
        String birthTime, String root, String extension, String queryIdRoot, String queryIdExtension) {
        PRPAIN201306UV02 response = new PRPAIN201306UV02();
        response.setControlActProcess(createResponseControlActProcess(firstName, lastName, gender, birthTime, root,
            extension, queryIdRoot, queryIdExtension));
        return response;
    }

    private static PRPAIN201305UV02QUQIMT021001UV01ControlActProcess createControlActProcess(String firstName,
        String lastName, String gender, String birthTime, String root, String extension, String queryIdRoot,
        String queryIdExtension) {
        II subjectId = new II();
        subjectId.setRoot(root);
        subjectId.setExtension(extension);

        CD code = new CD();
        code.setCode(CNTRL_CODE);
        code.setCodeSystem(CNTRL_CODE_SYSTEM);

        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess
        = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        controlActProcess.setClassCode(ActClassControlAct.CACT);
        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setCode(code);
        controlActProcess.setQueryByParameter(createQueryParams(firstName, lastName, gender, birthTime, subjectId,
            queryIdRoot, queryIdExtension));
        return controlActProcess;
    }

    private static JAXBElement<PRPAMT201306UV02QueryByParameter> createQueryParams(String firstName, String lastName,
        String gender, String birthTime, II subjectId, String queryIdRoot, String queryIdExtension) {

        II id = new II();
        id.setRoot(queryIdRoot);
        id.setExtension(queryIdExtension);
        CS statusCode = new CS();
        statusCode.setCode(NEW);

        PRPAMT201306UV02QueryByParameter params = new PRPAMT201306UV02QueryByParameter();
        params.setQueryId(id);
        params.setStatusCode(statusCode);
        params.setParameterList(createParamList(firstName, lastName, gender, birthTime, subjectId));

        return new JAXBElement<>(new QName(NAMESPACE_FOR_QUERY_BY_PARAMETER, QUERY_BY_PARAMETER_TAG),
            PRPAMT201306UV02QueryByParameter.class, params);
    }

    private static PRPAMT201306UV02ParameterList createParamList(String firstName, String lastName, String gender,
        String birthTime, II subjectId) {

        PRPAMT201306UV02ParameterList paramList = new PRPAMT201306UV02ParameterList();
        paramList.getLivingSubjectAdministrativeGender().add(createGender(gender));
        paramList.getLivingSubjectBirthTime().add(createBirthTime(birthTime));
        paramList.getLivingSubjectName().add(createName(firstName, lastName));
        paramList.getLivingSubjectId().add(createSubjectId(subjectId));
        return paramList;
    }

    private static PRPAMT201306UV02LivingSubjectId createSubjectId(II subjectId) {
        PRPAMT201306UV02LivingSubjectId id = new PRPAMT201306UV02LivingSubjectId();
        id.getValue().add(subjectId);
        return id;
    }

    private static PRPAMT201306UV02LivingSubjectName createName(String firstName, String lastName) {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        ENExplicit name = factory.createENExplicit();

        if (lastName != null && lastName.length() > 0) {
            EnExplicitFamily familyName = new EnExplicitFamily();
            familyName.setPartType(FAMILY_NAME_CODE);
            familyName.setContent(lastName);

            name.getContent().add(factory.createENExplicitFamily(familyName));
        }

        if (firstName != null && firstName.length() > 0) {
            EnExplicitGiven givenName = new EnExplicitGiven();
            givenName.setPartType(GIVEN_NAME_CODE);
            givenName.setContent(firstName);

            name.getContent().add(factory.createENExplicitGiven(givenName));
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

    private static PRPAIN201306UV02MFMIMT700711UV01ControlActProcess createResponseControlActProcess(String firstName,
        String lastName, String gender, String birthTime, String root, String extension, String queryIdRoot,
        String queryIdExtension) {
        PRPAIN201305UV02 query = createPRPAIN201305UV02Request(firstName, lastName, gender, birthTime, root, extension,
            queryIdRoot, queryIdExtension);

        Patients patients = createPatients(firstName, lastName, gender, birthTime, root, extension);

        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess
        = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setClassCode(ActClassControlAct.CACT);

        CD code = new CD();
        code.setCode(CNTRL_CODE);
        code.setCodeSystem(CNTRL_CODE_SYSTEM);
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
        subject.getTypeCode().add(CNTRL_SUBJ_TYPECODE);
        subject.setRegistrationEvent(createRegEvent(patient, query));

        return subject;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent createRegEvent(Patient patient,
        PRPAIN201305UV02 query) {

        II id = new II();
        id.getNullFlavor().add(NA);

        CS statusCode = new CS();
        statusCode.setCode(STATUS_CD);

        PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent regEvent
        = new PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent();

        regEvent.getMoodCode().add(REG_MOOD_CODE);
        regEvent.getClassCode().add(REG_CLASS_CODE);
        regEvent.getId().add(id);
        regEvent.setStatusCode(statusCode);
        regEvent.setSubject1(createSubject1(patient, query));

        return regEvent;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01Subject2 createSubject1(Patient patient, PRPAIN201305UV02 query) {
        PRPAIN201306UV02MFMIMT700711UV01Subject2 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject2();
        subject.setTypeCode(ParticipationTargetSubject.SBJ);
        subject.setPatient(createPatient(patient, query));
        return subject;
    }

    private static PRPAMT201310UV02Patient createPatient(Patient patient, PRPAIN201305UV02 query) {
        CS statusCode = new CS();
        statusCode.setCode(SUBJ_PATIENT_STATUS_CODE);

        PRPAMT201310UV02Patient subjectPatient = new PRPAMT201310UV02Patient();
        subjectPatient.getClassCode().add(SUBJ_PATIENT_CLASS_CODE);
        subjectPatient.setStatusCode(statusCode);
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

    private static Identifier createIdentifier(String extId, String rootId) {
        Identifier subjectId = new Identifier();
        subjectId.setId(extId);
        subjectId.setOrganizationId(rootId);
        return subjectId;
    }

    private static Patients createPatients(String firstName,
        String lastName, String gender, String birthTime, String root, String extension) {
        Patients patients = new Patients();
        patients.add(createMpiPatient(firstName, lastName, gender, birthTime, createIdentifier(extension, root)));
        return patients;
    }

    private static Patient createMpiPatient(String firstName, String lastName, String gender, String birthTime,
        Identifier subjectId) {

        Patient result = new Patient();

        PersonName name = new PersonName();
        name.setFirstName(firstName);
        name.setLastName(lastName);
        result.getNames().add(name);
        result.setGender(gender);
        result.setDateOfBirth(birthTime);
        Identifiers ids = new Identifiers();
        ids.add(subjectId);
        result.setIdentifiers(ids);
        return result;
    }

    public static PatientLocationQueryRequestType createPatientLocationQueryRequestType(String extension, String root) {
        II subject = new II();
        subject.setRoot(root);
        subject.setExtension(extension);

        PatientLocationQueryRequestType request = new PatientLocationQueryRequestType();
        request.setRequestedPatientId(subject);

        return request;
    }

    public static PatientLocationQueryResponseType createPatientLocationQueryResponseType(String searchExtension,
        String searchRoot, String hcid, String foundExtension, String foundRoot) {

        II subject = new II();
        subject.setRoot(searchRoot);
        subject.setExtension(searchExtension);

        II found = new II();
        found.setRoot(foundRoot);
        found.setExtension(foundExtension);

        PatientLocationResponse location = new PatientLocationResponse();
        location.setHomeCommunityId(hcid);
        location.setCorrespondingPatientId(found);
        location.setRequestedPatientId(subject);

        PatientLocationQueryResponseType response = new PatientLocationQueryResponseType();
        response.getPatientLocationResponse().add(location);

        return response;
    }
}
