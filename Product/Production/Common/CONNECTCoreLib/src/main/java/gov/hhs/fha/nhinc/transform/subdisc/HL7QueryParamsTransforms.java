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

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.apache.commons.collections.CollectionUtils;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.CE;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAMT201301UV02OtherIDs;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02PatientAddress;
import org.hl7.v3.PRPAMT201306UV02PatientTelecom;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.STExplicit;
import org.hl7.v3.TELExplicit;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7QueryParamsTransforms {

    private static HL7MessageIdGenerator idGenerator = new HL7MessageIdGenerator();

    public static JAXBElement<PRPAMT201306UV02QueryByParameter> createQueryParams(PRPAMT201301UV02Patient patient,
        String localDeviceId) {
        PRPAMT201306UV02QueryByParameter params = new PRPAMT201306UV02QueryByParameter();

        params.setQueryId(idGenerator.generateHL7MessageId(localDeviceId));
        params.setStatusCode(HL7DataTransformHelper.CSFactory("new"));
        params.setResponseModalityCode(HL7DataTransformHelper.CSFactory("R"));
        params.setResponsePriorityCode(HL7DataTransformHelper.CSFactory("I"));

        params.setParameterList(createParamList(patient));

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "queryByParameter");

        return new JAXBElement<>(xmlqname, PRPAMT201306UV02QueryByParameter.class, params);
    }

    public static PRPAMT201306UV02ParameterList createParamList(PRPAMT201301UV02Patient patient) {
        PRPAMT201306UV02ParameterList paramList = new PRPAMT201306UV02ParameterList();
        PRPAMT201301UV02Person person = null;
        if (patient != null) {
            JAXBElement<PRPAMT201301UV02Person> jaxbPerson = patient.getPatientPerson();
            person = jaxbPerson.getValue();
        }

        // Set the Subject Gender Code
        if (person != null && person.getAdministrativeGenderCode() != null
            && NullChecker.isNotNullish(person.getAdministrativeGenderCode().getCode())) {
            paramList.getLivingSubjectAdministrativeGender()
            .add(createGender(person.getAdministrativeGenderCode().getCode()));
        }

        // Set the Subject Birth Time
        if (person != null && person.getBirthTime() != null
            && NullChecker.isNotNullish(person.getBirthTime().getValue())) {
            paramList.getLivingSubjectBirthTime().add(createBirthTime(person.getBirthTime().getValue()));
        }

        // Set the address
        if (person != null && CollectionUtils.isNotEmpty(person.getAddr())) {
            paramList.getPatientAddress().add(createAddress(person.getAddr()));
        }

        // Set telephone number
        if (person != null && CollectionUtils.isNotEmpty(person.getTelecom())) {
            paramList.getPatientTelecom().add(createTelecom(person.getTelecom()));
        }
        // Set the Subject Name
        if (person != null && CollectionUtils.isNotEmpty(person.getName())) {
            paramList.getLivingSubjectName().add(createName(person.getName()));
        }

        // Set the subject Id
        if (patient != null && CollectionUtils.isNotEmpty(patient.getId())
            && patient.getId().get(0) != null) {
            paramList.getLivingSubjectId().add(createSubjectId(patient.getId().get(0)));
        }

        // Set the other ids
        if (person != null && NullChecker.isNotNullish(person.getAsOtherIDs())) {
            for (PRPAMT201301UV02OtherIDs otherId : person.getAsOtherIDs()) {
                for (II id : otherId.getId()) {
                    paramList.getLivingSubjectId().add(createSubjectId(id));
                }
            }
        }

        return paramList;
    }

    public static PRPAMT201306UV02LivingSubjectId createSubjectId(II subjectId) {
        PRPAMT201306UV02LivingSubjectId id = new PRPAMT201306UV02LivingSubjectId();

        if (subjectId != null) {
            id.getValue().add(subjectId);
            STExplicit text = new STExplicit();
            id.setSemanticsText(text);
        }

        return id;
    }

    public static PRPAMT201306UV02LivingSubjectName createName(List<PNExplicit> patientNames) {
        if (patientNames == null || patientNames.isEmpty()) {
            return null;
        }

        PRPAMT201306UV02LivingSubjectName subjectName = new PRPAMT201306UV02LivingSubjectName();

        for (PNExplicit name : patientNames) {
            subjectName.getValue().add(HL7DataTransformHelper.convertPNToEN(name));
            STExplicit text = new STExplicit();
            subjectName.setSemanticsText(text);
        }

        return subjectName;
    }

    public static PRPAMT201306UV02LivingSubjectBirthTime createBirthTime(String birthTime) {
        PRPAMT201306UV02LivingSubjectBirthTime subjectBirthTime = new PRPAMT201306UV02LivingSubjectBirthTime();
        IVLTSExplicit bday = new IVLTSExplicit();

        if (birthTime != null && birthTime.length() > 0) {
            bday.setValue(birthTime);
            subjectBirthTime.getValue().add(bday);
            STExplicit text = new STExplicit();
            subjectBirthTime.setSemanticsText(text);
        }

        return subjectBirthTime;
    }

    public static PRPAMT201306UV02LivingSubjectAdministrativeGender createGender(String gender) {
        PRPAMT201306UV02LivingSubjectAdministrativeGender adminGender = new PRPAMT201306UV02LivingSubjectAdministrativeGender();
        CE genderCode = new CE();

        if (gender != null && gender.length() > 0) {
            genderCode.setCode(gender);
            adminGender.getValue().add(genderCode);

            STExplicit text = new STExplicit();
            adminGender.setSemanticsText(text);
        }

        return adminGender;
    }

    public static PRPAMT201306UV02PatientAddress createAddress(List<ADExplicit> patientAddress) {
        PRPAMT201306UV02PatientAddress subjectAddress = null;
        STExplicit text;

        if (patientAddress != null) {
            subjectAddress = new PRPAMT201306UV02PatientAddress();
            for (ADExplicit address : patientAddress) {
                subjectAddress.getValue().add(address);
                text = new STExplicit();
                subjectAddress.setSemanticsText(text);
            }
        }

        return subjectAddress;
    }

    public static PRPAMT201306UV02PatientTelecom createTelecom(List<TELExplicit> patientTelecom) {
        PRPAMT201306UV02PatientTelecom subjectTele = null;
        STExplicit text;

        if (patientTelecom != null) {
            subjectTele = new PRPAMT201306UV02PatientTelecom();
            for (TELExplicit tele : patientTelecom) {
                subjectTele.getValue().add(tele);
                text = new STExplicit();
                subjectTele.setSemanticsText(text);
            }
        }

        return subjectTele;
    }

}
