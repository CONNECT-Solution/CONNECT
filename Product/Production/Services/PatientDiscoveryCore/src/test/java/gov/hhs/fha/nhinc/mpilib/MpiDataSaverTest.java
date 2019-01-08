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
package gov.hhs.fha.nhinc.mpilib;

import java.io.File;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author akong
 */
public class MpiDataSaverTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final static String mockMpiName = System.getProperty("user.dir", ".") + File.separator + "mockmpi.xml";
    final static String mockMpiDirectoryName = System.getProperty("user.dir", ".") + File.separator;

    public MpiDataSaverTest() {
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
        deleteMockMpiFile();
    }

    protected static void deleteMockMpiFile() {
        File file = new File(mockMpiName);
        if (file.exists()) {
            file.delete();
        }
    }

    protected MpiDataSaver createMpiDataSaver() {
        MpiDataSaver mpiDataSaver = new MpiDataSaver() {

            @Override
            protected String getDefaultMpiFilename() {
                return mockMpiName;
            }
        };

        return mpiDataSaver;
    }

    protected Patient createPatient(String firstName, String lastName) {
        Patient patient = new Patient();

        PersonName name = new PersonName();
        name.setFirstName(firstName);
        name.setLastName(lastName);
        patient.getNames().add(name);

        return patient;
    }

    @Test
    public void testSaveAndLoadMpi_DefaultFile_NoPatients() {

        MpiDataSaver mpiDataSaver = createMpiDataSaver();
        mpiDataSaver.saveMpi(null);
        Patients patientList = mpiDataSaver.loadMpi();
        assertEquals(0, patientList.size());
    }

    @Test
    public void testSaveAndLoadMpi_DefaultFile_OnePatient() {

        MpiDataSaver mpiDataSaver = createMpiDataSaver();
        Patients patientList = new Patients();
        patientList.add(createPatient("John", "Doe"));
        mpiDataSaver.saveMpi(patientList);
        patientList = mpiDataSaver.loadMpi();

        assertEquals(1, patientList.size());
        Patient patient = patientList.get(0);
        assertEquals("John", patient.getNames().get(0).getFirstName());
        assertEquals("Doe", patient.getNames().get(0).getLastName());
    }

    @Test
    public void testSaveAndLoadMpi_DefaultFile_Resave() {

        MpiDataSaver mpiDataSaver = createMpiDataSaver();
        Patients patientList = new Patients();
        patientList.add(createPatient("John", "Doe"));
        patientList.add(createPatient("Bob", "Doe"));
        mpiDataSaver.saveMpi(patientList);
        patientList = mpiDataSaver.loadMpi();
        assertEquals(2, patientList.size());

        mpiDataSaver.saveMpi(null);
        patientList = mpiDataSaver.loadMpi();
        assertEquals(0, patientList.size());
    }

    @Test(expected = MpiException.class)
    public void testSaveMpi_BadFile() {

        MpiDataSaver mpiDataSaver = createMpiDataSaver();
        mpiDataSaver.saveMpi(null, mockMpiDirectoryName);
    }

    @Test
    public void testLoadMpi_NonExistingFile() {

        deleteMockMpiFile();

        MpiDataSaver mpiDataSaver = createMpiDataSaver();
        Patients patientList = mpiDataSaver.loadMpi(mockMpiName);
        assertEquals(0, patientList.size());
    }

}
