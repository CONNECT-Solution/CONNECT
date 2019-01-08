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

import javax.xml.bind.JAXBElement;
import org.hl7.v3.CE;
import org.hl7.v3.II;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Person;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class HL7PRPA201302TransformsTest {

    @Test
    public void createPRPA201302() {
        PRPAMT201301UV02Patient patient = createPRPAMT201301UV02Patient();
        String remotePatId = "D123401";
        String remoteDeviceId = "2.16.17.19";
        String senderOID = "1.1";
        String receiverOID = "2.2";
        HL7PRPA201302Transforms transforms = new HL7PRPA201302Transforms();
        PRPAIN201302UV02 result;
        result = transforms.createPRPA201302(patient, remotePatId, remoteDeviceId, senderOID, receiverOID);
        assertEquals(result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getId().get(0).getExtension(), "1.16.17.19");
    }

    @Test
    public void createPRPA201302ForPRPAMT201310UV02() {
        PRPAMT201310UV02Patient patient = createPRPAMT201310UV02Patient();
        String remotePatId = "D123401";
        String remoteDeviceId = "2.16.17.19";
        String senderOID = "1.1";
        String receiverOID = "2.2";
        HL7PRPA201302Transforms transforms = new HL7PRPA201302Transforms();
        PRPAIN201302UV02 result;
        result = transforms.createPRPA201302(patient, remotePatId, remoteDeviceId, senderOID, receiverOID);
        assertEquals(result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getId().get(0).getExtension(), "1.16.17.19");
    }

    private PRPAMT201310UV02Patient createPRPAMT201310UV02Patient() {
        org.hl7.v3.PRPAMT201310UV02Patient patient = new PRPAMT201310UV02Patient();
        PRPAMT201310UV02Person patientPerson = new PRPAMT201310UV02Person();
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201310UV02Person> patientPersonElement = new JAXBElement<>(xmlqname,
                PRPAMT201310UV02Person.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patient.getId().add(createII());
        patientPerson.getClassCode().add("ClassCode");

        patientPerson.setDeterminerCode("INSTANCE");
        patientPerson.setAdministrativeGenderCode(createCE());

        PNExplicit patientName = new PNExplicit();
        patientName.getContent().add(getFirstName());
        patientName.getContent().add(getLastName());
        patientName.getNullFlavor().add("NA");
        patientPerson.getName().add(patientName);
        return patient;
    }

    private PRPAMT201301UV02Patient createPRPAMT201301UV02Patient() {
        org.hl7.v3.PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        PRPAMT201301UV02Person patientPerson = new PRPAMT201301UV02Person();
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UV02Person> patientPersonElement = new JAXBElement<>(xmlqname,
                PRPAMT201301UV02Person.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patient.getId().add(createII());
        patientPerson.getClassCode().add("ClassCode");

        patientPerson.setDeterminerCode("INSTANCE");
        patientPerson.setAdministrativeGenderCode(createCE());

        PNExplicit patientName = new PNExplicit();
        patientName.getContent().add(getFirstName());
        patientName.getContent().add(getLastName());
        patientName.getNullFlavor().add("NA");
        patientPerson.getName().add(patientName);
        return patient;
    }

    private static String getFirstName() {
        String firstName = "Gallow";
        return firstName;
    }

    private static String getLastName() {
        String lastName = "Younger";
        return lastName;
    }

    private static String getNameType() {
        String nameType = "PNExplicitNameType";
        return nameType;
    }

    private CE createCE() {
        CE ce = new CE();
        ce.setCode("CONNECT");
        return ce;
    }

    private II createII() {
        II ii = new II();
        ii.setAssigningAuthorityName("CONNECT");
        ii.setExtension("1.16.17.19");
        ii.setRoot("1.1");
        return ii;
    }

}
