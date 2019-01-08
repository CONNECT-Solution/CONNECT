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
package gov.hhs.fha.nhinc.mpi.adapter.component;

import gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers.HL7Parser201306;
import gov.hhs.fha.nhinc.mpilib.Identifier;
import gov.hhs.fha.nhinc.mpilib.Patient;
import gov.hhs.fha.nhinc.mpilib.Patients;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Jon Hoppesch
 */
public class AdapterWebPatientCheckerTest {
    private static String propertyFileLocation = null;

    /**
     * Public constructor for test.
     */
    public AdapterWebPatientCheckerTest() {
    }

    //CHECKSTYLE:OFF
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        propertyFileLocation = PropertyAccessor.getInstance().getPropertyFileLocation();
    }

    @After
    public void tearDown() {
        PropertyAccessor.getInstance().setPropertyFileLocation(propertyFileLocation);
    }
    //CHECKSTYLE:ON

    /**
     * Test of FindPatient method, of class PatientChecker.
     */
    @Test
    @Ignore
    public void singlePatientExists() {
        II subjectId = new II();
        subjectId.setRoot("2.2");
        subjectId.setExtension("500000000");

        PRPAIN201305UV02 query = TestHelper.build201305("Gallow", "Younger", "M", "19630804", subjectId);

        Identifier patId = new Identifier();
        patId.setId("500000000");
        patId.setOrganizationId("2.2");
        Patient patient = TestHelper.createMpiPatient("Gallow", "Younger", "M", "19630804", patId);
        Patients patients = new Patients();
        patients.add(patient);

        PRPAIN201306UV02 expResult = HL7Parser201306.buildMessageFromMpiPatient(patients, query);
        PatientChecker checker = new PatientChecker();
        PRPAIN201306UV02 result = checker.findPatient(query);

        // Verify the messages match
        TestHelper.assertPatientIdsAreSame(expResult, result);
        TestHelper.assertPatientGendersAreSame(expResult, result);
        TestHelper.assertPatientBdaysAreSame(expResult, result);
        TestHelper.assertPatientNamesAreSame(expResult, result);
    }

    /**
     * Tests behavior if patient does not exist.
     */
    @Test
    public void patientDoesNotExist() {
        II subjectId = new II();
        subjectId.setRoot("2.16.840.1.113883.3.200");
        subjectId.setExtension("1239");
        PRPAIN201305UV02 query = TestHelper.build201305("Joe", "Anderson", "M", "19560301", subjectId);
        PatientChecker checker = new PatientChecker();
        PRPAIN201306UV02 result = checker.findPatient(query);
        assertNotNull(result);
    }
}
