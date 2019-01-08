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

import java.util.List;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02PatientAddress;
import org.hl7.v3.PRPAMT201306UV02PatientTelecom;
import org.hl7.v3.TELExplicit;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class HL7PatientTransformsTest {
    @Test
    public void create201301Patient() {
        String aaId = "1.1";
        org.hl7.v3.PRPAMT201301UV02Patient patient;
        HL7PatientTransforms transforms = new HL7PatientTransforms();
        patient = transforms.create201301Patient(createPRPAMT201306UV02ParameterList(), aaId);
        assertEquals(patient.getPatientPerson().getValue().getAdministrativeGenderCode().getCode(), "CONNECT");
        assertEquals(patient.getPatientPerson().getValue().getBirthTime().getValue(), "12-10-1765");
        assertEquals(patient.getId().get(0).getRoot(), "1.1");
        assertEquals(patient.getPatientPerson().getValue().getTelecom().get(0).getUse().get(0), "001-002-2345");
        assertEquals(patient.getPatientPerson().getValue().getAddr().get(0).getUse().get(0), "12601, FairLakes Circle");
    }

    private CS createCS() {
        CS cs = new CS();
        cs.setCode("CONNECT");
        return cs;
    }

    private PRPAMT201306UV02ParameterList createPRPAMT201306UV02ParameterList() {
        PRPAMT201306UV02ParameterList paramList = new PRPAMT201306UV02ParameterList();
        paramList.getLivingSubjectAdministrativeGender().add(createLivingSubjectAdministrativeGender());
        paramList.getLivingSubjectBirthTime().add(createLivingSubjectBirthTime());
        paramList.getLivingSubjectName().add(createLivingSubjectName());
        paramList.getLivingSubjectId().add(createLivingSubjectId());
        paramList.getPatientAddress().add(createPatientAddress());
        paramList.getPatientTelecom().add(createPatientTelecom());
        return paramList;

    }

    private PRPAMT201306UV02PatientTelecom createPatientTelecom() {
        PRPAMT201306UV02PatientTelecom telecom = new PRPAMT201306UV02PatientTelecom();
        telecom.getValue().add(createTelExplicit());
        return telecom;
    }

    private TELExplicit createTelExplicit() {
        TELExplicit tel = new TELExplicit();
        tel.getUse().add("001-002-2345");
        return tel;
    }

    private PRPAMT201306UV02PatientAddress createPatientAddress() {
        PRPAMT201306UV02PatientAddress patientAddress = new PRPAMT201306UV02PatientAddress();
        patientAddress.getValue().add(createADExplicit());
        return patientAddress;
    }

    private ADExplicit createADExplicit() {
        ADExplicit ad = new ADExplicit();
        ad.getUse().add("12601, FairLakes Circle");
        ad.getUse().add("Fairfax");
        ad.getUse().add("VA");
        return ad;
    }

    private PRPAMT201306UV02LivingSubjectId createLivingSubjectId() {
        PRPAMT201306UV02LivingSubjectId subjectId = new PRPAMT201306UV02LivingSubjectId();
        subjectId.getValue().add(createII());
        return subjectId;
    }

    private II createII() {
        II ii = new II();
        ii.setAssigningAuthorityName("CONNECT");
        ii.setExtension("D123401");
        ii.setRoot("1.1");
        return ii;
    }

    private PRPAMT201306UV02LivingSubjectName createLivingSubjectName() {
        PRPAMT201306UV02LivingSubjectName subjectName = new PRPAMT201306UV02LivingSubjectName();
        subjectName.getValue().add(createENExplicit());
        return subjectName;
    }

    private ENExplicit createENExplicit() {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        ENExplicit enName = factory.createENExplicit();
        List enNamelist = enName.getContent();
        EnExplicitFamily familyName = new EnExplicitFamily();
        familyName.setPartType("FAM");
        familyName.setContent(getLastName());
        enNamelist.add(factory.createENExplicitFamily(familyName));
        EnExplicitGiven result = new EnExplicitGiven();
        result.setPartType("GIV");
        result.setContent(getFirstName());
        enNamelist.add(factory.createENExplicitGiven(result));
        return enName;
    }

    private String getFirstName() {
        String firstName = "Gallow";
        return firstName;
    }

    private String getLastName() {
        String lastName = "Younger";
        return lastName;
    }

    private PRPAMT201306UV02LivingSubjectBirthTime createLivingSubjectBirthTime() {
        PRPAMT201306UV02LivingSubjectBirthTime birthTime = new PRPAMT201306UV02LivingSubjectBirthTime();
        birthTime.getValue().add(createIVLTSExplicit());
        return birthTime;
    }

    private IVLTSExplicit createIVLTSExplicit() {
        IVLTSExplicit ts = new IVLTSExplicit();
        ts.setValue("12-10-1765");
        return ts;
    }

    private PRPAMT201306UV02LivingSubjectAdministrativeGender createLivingSubjectAdministrativeGender() {
        PRPAMT201306UV02LivingSubjectAdministrativeGender gender = new PRPAMT201306UV02LivingSubjectAdministrativeGender();
        gender.getValue().add(createCE());
        return gender;
    }

    private CE createCE() {
        CE ce = new CE();
        ce.setCode("CONNECT");
        return ce;
    }

}
