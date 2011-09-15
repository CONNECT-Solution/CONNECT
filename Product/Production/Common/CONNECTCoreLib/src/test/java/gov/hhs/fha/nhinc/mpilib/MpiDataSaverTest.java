/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */

package gov.hhs.fha.nhinc.mpilib;

import java.io.File;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author akong
 */
public class MpiDataSaverTest {

    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
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
            protected Log createLogger() {
                return mockLog;
            }

            @Override
            protected void logException(Exception e) {
                return;
            }

            @Override
            protected String getDefaultMpiFilename() {
                return mockMpiName;
            }
        };

        return mpiDataSaver;
    }

    protected Patient createPatient(String firstName, String lastName) {
        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        return patient;
    }

    @Test
    public void testSaveAndLoadMpi_DefaultFile_NoPatients() {
        context.checking(new Expectations() {
            {
                allowing(mockLog).info(with(any(String.class)));
            }
        });

        MpiDataSaver mpiDataSaver = createMpiDataSaver();
        mpiDataSaver.saveMpi(null);
        Patients patientList = mpiDataSaver.loadMpi();
        assertEquals(0, patientList.size());
    }

    @Test
    public void testSaveAndLoadMpi_DefaultFile_OnePatient() {
        context.checking(new Expectations() {
            {
                allowing(mockLog).info(with(any(String.class)));
            }
        });

        MpiDataSaver mpiDataSaver = createMpiDataSaver();
        Patients patientList = new Patients();
        patientList.add(createPatient("John", "Doe"));
        mpiDataSaver.saveMpi(patientList);
        patientList = mpiDataSaver.loadMpi();
        
        assertEquals(1, patientList.size());
        Patient patient = patientList.get(0);
        assertEquals("John", patient.getFirstName());
        assertEquals("Doe", patient.getLastName());
    }

    @Test
    public void testSaveAndLoadMpi_DefaultFile_Resave() {
        context.checking(new Expectations() {
            {
                allowing(mockLog).info(with(any(String.class)));
            }
        });

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

    @Test(expected=UnableToInitializeMpi.class)
    public void testSaveMpi_BadFile() {
        context.checking(new Expectations() {
            {
                allowing(mockLog).info(with(any(String.class)));
            }
        });

        MpiDataSaver mpiDataSaver = createMpiDataSaver();
        mpiDataSaver.saveMpi(null, mockMpiDirectoryName);
    }

    @Test
    public void testLoadMpi_NonExistingFile() {
        context.checking(new Expectations() {
            {
                allowing(mockLog).info(with(any(String.class)));
            }
        });

        deleteMockMpiFile();

        MpiDataSaver mpiDataSaver = createMpiDataSaver();
        Patients patientList = mpiDataSaver.loadMpi(mockMpiName);
        assertEquals(0, patientList.size());   
    }

}
