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
package gov.hhs.fha.nhinc.patientdiscovery;

import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;

import javax.xml.bind.JAXBElement;

import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author JHOPPESC
 */
public class NhinPatientDiscoveryUtilsTest {

    public NhinPatientDiscoveryUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of extractPatientIdFrom201306 method, of class NhinPatientDiscoveryUtils.
     */
    @Test
    public void testExtractPatientIdFrom201306() {
        System.out.println("testExtractPatientIdFrom201306");

        JAXBElement<PRPAMT201301UV02Person> queryPerson = HL7PatientTransforms.create201301PatientPerson("Joe",
                "Smith", "M", null, null);
        PRPAMT201301UV02Patient queryPatient = HL7PatientTransforms.create201301Patient(queryPerson, "1234", "1.1.1");
        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        query = HL7PRPA201305Transforms.createPRPA201305(queryPatient, "1.1", "2.2", "1.1.1");

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
                "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
        PRPAIN201306UV02 msg = new PRPAIN201306UV02();
        msg = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);

        II result = NhinPatientDiscoveryUtils.extractPatientIdFrom201306(msg);

        assertEquals("5678", result.getExtension());
        assertEquals("2.2.2", result.getRoot());
    }

    /**
     * Test of extractPatientIdFrom201306 method, of class NhinPatientDiscoveryUtils.
     */
    @Test
    public void testExtractNullPatientIdFrom201306() {
        System.out.println("testExtractNullPatientIdFrom201306");

        JAXBElement<PRPAMT201301UV02Person> queryPerson = HL7PatientTransforms.create201301PatientPerson("Joe",
                "Smith", "M", null, null);
        PRPAMT201301UV02Patient queryPatient = HL7PatientTransforms.create201301Patient(queryPerson, "1234", "1.1.1");
        PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(queryPatient, "1.1", "2.2", "1.1.1");

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
                "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, null, null);
        PRPAIN201306UV02 msg = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);

        II result = NhinPatientDiscoveryUtils.extractPatientIdFrom201306(msg);

        assertNull(result);
    }
}
