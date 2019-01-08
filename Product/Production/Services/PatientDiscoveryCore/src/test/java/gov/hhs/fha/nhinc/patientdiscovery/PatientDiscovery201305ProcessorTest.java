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
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.testhelper.TestHelper;
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
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author JHOPPESC
 */
public class PatientDiscovery201305ProcessorTest {

    public PatientDiscovery201305ProcessorTest() {
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
     * Test of process201305 method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testProcess201305() {
        System.out.println("testProcess201305");

        PatientDiscoveryProcessor instance = new PatientDiscovery201305Processor() {
            @Override
            protected void storeMapping(PRPAIN201305UV02 request) {
            }

            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
                JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe",
                        "Smith", "M", null, null);
                PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");

                return HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1",
                        "2.2.2", query);
            }

            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
                return true;
            }

            @Override
            protected void createPatientCorrelation(PRPAIN201306UV02 queryResult, II remotePatient,
                    AssertionType assertion, PRPAIN201305UV02 query) {
            }

            @Override
            protected void createPatientCorrelation(PRPAIN201306UV02 queryResult, AssertionType assertion,
                    PRPAIN201305UV02 query) {
            }
        };

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
                "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        AssertionType assertion = new AssertionType();

        PRPAIN201306UV02 result = null;
		try {
			result = instance.process201305(request, assertion);
		} catch (PatientDiscoveryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        TestHelper.assertPatientFound(result);
    }

    /**
     * Test of process201305 method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testProcess201305PatientNotFound() {
        System.out.println("testProcess201305PatientNotFound");

        PatientDiscoveryProcessor instance = new PatientDiscovery201305Processor() {
            @Override
            protected void storeMapping(PRPAIN201305UV02 request) {
            }

            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {

                return HL7PRPA201306Transforms.createPRPA201306(null, "2.2", null, "1.1", null, query);
            }

            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
                return false;
            }

            @Override
            protected void createPatientCorrelation(PRPAIN201306UV02 queryResult, II remotePatient,
                    AssertionType assertion, PRPAIN201305UV02 query) {
            }

            @Override
            protected void createPatientCorrelation(PRPAIN201306UV02 queryResult, AssertionType assertion,
                    PRPAIN201305UV02 query) {
            }
        };

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
                "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        AssertionType assertion = new AssertionType();

        PRPAIN201306UV02 result = null;
		try {
			result = instance.process201305(request, assertion);
		} catch (PatientDiscoveryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        TestHelper.assertPatientNotFound(result);
    }

    /**
     * Test of process201305 method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testProcess201305PolicyCheckFailure() {
        System.out.println("testProcess201305PolicyCheckFailure");

        PatientDiscoveryProcessor instance = new PatientDiscovery201305Processor() {
            @Override
            protected void storeMapping(PRPAIN201305UV02 request) {
            }

            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
                return null;
            }

            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
                return true;
            }

            @Override
            protected void createPatientCorrelation(PRPAIN201306UV02 queryResult, II remotePatient,
                    AssertionType assertion, PRPAIN201305UV02 query) {
            }

            @Override
            protected void createPatientCorrelation(PRPAIN201306UV02 queryResult, AssertionType assertion,
                    PRPAIN201305UV02 query) {
            }
        };

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
                "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        AssertionType assertion = new AssertionType();

        PRPAIN201306UV02 result = null;
		try {
			result = instance.process201305(request, assertion);
		} catch (PatientDiscoveryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        TestHelper.assertPatientNotFound(result);
    }

    /**
     * Test of createNewRequest method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testCreateNewRequest() {
        System.out.println("testCreateNewRequest");

        String targetCommunityId = "3.3";
        PatientDiscovery201305Processor instance = new PatientDiscovery201305Processor();

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
                "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        PRPAIN201305UV02 result = instance.createNewRequest(request, targetCommunityId);

        TestHelper.assertReceiverEquals(targetCommunityId, result);
    }

    /**
     * Test of createNewRequest method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testCreateNewRequestNullInput() {
        System.out.println("testCreateNewRequestNullInput");

        PatientDiscovery201305Processor instance = new PatientDiscovery201305Processor();

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
                "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        PRPAIN201305UV02 result = instance.createNewRequest(request, null);

        assertNull(result);
    }

    /**
     * Test of extractPatientIdFrom201305 method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testExtractPatientIdFrom201305() {
        System.out.println("testExtractPatientIdFrom201305");

        PatientDiscovery201305Processor instance = new PatientDiscovery201305Processor();

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
                "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        II result = instance.extractPatientIdFrom201305(request);

        assertNotNull(result);
        assertEquals("1234", result.getExtension());
        assertEquals("1.1.1", result.getRoot());
    }

    /**
     * Test of extractPatientIdFrom201305 method, of class PatientDiscovery201305Processor.
     */
    @Test
    public void testExtractPatientIdFrom201305NullInput() {
        System.out.println("testExtractPatientIdFrom201305NullInput");

        PatientDiscovery201305Processor instance = new PatientDiscovery201305Processor();

        II result = instance.extractPatientIdFrom201305(null);

        assertNull(result);
    }

}
