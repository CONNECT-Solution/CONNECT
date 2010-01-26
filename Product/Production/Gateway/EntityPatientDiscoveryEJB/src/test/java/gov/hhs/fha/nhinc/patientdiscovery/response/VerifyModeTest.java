/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.hl7.v3.CS;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

/**
 *
 * @author dunnek
 */
public class VerifyModeTest {
    private Mockery context;

    private final String CONST_ROOT = "123";
    private final String CONST_EXTENSION = "123";
    private final String CONST_FIRST_NAME = "Joe";
    private final String CONST_LAST_NAME = "Smith";
    private final String CONST_GENDER = "M";
    private final String CONST_BIRTH_TIME = "March 1, 1956";


    public VerifyModeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        context = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCompareId_LivingSubjectId_True()
    {
        System.out.println("testCompareId_LivingSubjectId_True");



        TestHelper helper = new TestHelper();
        II localId = helper.createII(CONST_ROOT,CONST_EXTENSION);
        II remoteId = helper.createII(CONST_ROOT,CONST_EXTENSION);

        VerifyMode instance = getVerifyMode(localId);
        
        PRPAMT201306UV02LivingSubjectId localLivingSubjectId = helper.createSubjectId(localId);
        PRPAMT201306UV02LivingSubjectId remoteLivingSubjectId = helper.createSubjectId(remoteId);

        boolean result =instance.compareId(localLivingSubjectId, remoteLivingSubjectId);

        assertEquals(true, result);
    }
    @Test
    public void testCompareId_LivingSubjectId_False()
    {
        System.out.println("testCompareId_LivingSubjectId_False");

        VerifyMode instance = getVerifyMode(null);
        TestHelper helper = new TestHelper();

        II localId = helper.createII(CONST_ROOT,CONST_EXTENSION);
        II remoteId = helper.createII(CONST_ROOT,"9999");

        PRPAMT201306UV02LivingSubjectId localLivingSubjectId = helper.createSubjectId(localId);
        PRPAMT201306UV02LivingSubjectId remoteLivingSubjectId = helper.createSubjectId(remoteId);

        boolean result =instance.compareId(localLivingSubjectId, remoteLivingSubjectId);

        assertEquals(false, result);
    }
    /**
     * Test of patientExistsLocally method, of class VerifyMode.
     * */
    @Test
    public void testPatientExistsLocally() {
        System.out.println("testPatientExistsLocally");

        TestHelper helper = new TestHelper();
        II localId = helper.createII(CONST_ROOT,CONST_EXTENSION);

        VerifyMode instance = getVerifyMode(localId);

        PRPAIN201305UV02 query = null;
        AssertionType assertion = null;

        query = helper.build201305(CONST_FIRST_NAME, CONST_LAST_NAME,CONST_GENDER, CONST_BIRTH_TIME, localId);

        PRPAIN201306UV02 response = helper.build201306(CONST_FIRST_NAME, CONST_LAST_NAME,CONST_GENDER, CONST_BIRTH_TIME, localId);

        boolean expResult = true;
        boolean result = instance.patientExistsLocally(query, assertion, response);
        assertEquals(expResult, result);

    }
    @Test
    public void testPatientExistsLocally_NoIdMatch() {
        System.out.println("testPatientExistsLocally_NoIdMatch");

        TestHelper helper = new TestHelper();
        II unknownId = helper.createII(CONST_ROOT,"9999");
        II knownId = helper.createII(CONST_ROOT,CONST_EXTENSION);

        VerifyMode instance = getVerifyMode(knownId);

        PRPAIN201305UV02 query = null;
        AssertionType assertion = null;

        query = helper.build201305(CONST_FIRST_NAME, CONST_LAST_NAME,CONST_GENDER, CONST_BIRTH_TIME, unknownId);

        PRPAIN201306UV02 response = helper.build201306(CONST_FIRST_NAME, CONST_LAST_NAME,CONST_GENDER, CONST_BIRTH_TIME, unknownId);

        boolean expResult = false;
        boolean result = instance.patientExistsLocally(query, assertion, response);
        assertEquals(expResult, result);

    }
    @Test
    public void testPatientExistsLocally_NoNameMatch() {
        System.out.println("testPatientExistsLocally_NoNameMatch");

        TestHelper helper = new TestHelper();
        II localId = helper.createII(CONST_ROOT,"9999");
        II remoteId = helper.createII(CONST_ROOT,CONST_EXTENSION);

        VerifyMode instance = getVerifyMode();

        PRPAIN201305UV02 query = null;
        AssertionType assertion = null;

        query = helper.build201305(CONST_FIRST_NAME, CONST_LAST_NAME,CONST_GENDER, CONST_BIRTH_TIME, localId);

        PRPAIN201306UV02 response = helper.build201306("Jose", CONST_LAST_NAME,CONST_GENDER, CONST_BIRTH_TIME, remoteId);

        boolean expResult = false;
        boolean result = instance.patientExistsLocally(query, assertion, response);
        assertEquals(expResult, result);

    }

    private VerifyMode getVerifyMode(II subjId)
    {
        final Log mockLogger = context.mock(Log.class);
        TestHelper helper = new TestHelper();

        final PRPAIN201306UV02 patient = helper.build201306(CONST_FIRST_NAME, CONST_LAST_NAME, CONST_GENDER,CONST_BIRTH_TIME, subjId);

        VerifyMode result = new VerifyMode() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion)
            {
                return patient;
            }
            @Override
            protected String getLocalHomeCommunityId()
            {
                return CONST_ROOT;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).error("Error");
                will(returnValue(null));
            }
        });
        return result;
    }
    private VerifyMode getVerifyMode()
    {
        final Log mockLogger = context.mock(Log.class);
        TestHelper helper = new TestHelper();
        
        VerifyMode result = new VerifyMode() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion)
            {
                return null;
            }
            @Override
            protected String getLocalHomeCommunityId()
            {
                return CONST_ROOT;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).error("Error");
                will(returnValue(null));
            }
        });
        return result;
    }
 
}