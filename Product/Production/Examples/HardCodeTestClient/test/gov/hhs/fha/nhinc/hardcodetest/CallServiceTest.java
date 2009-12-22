/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hardcodetest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;


/**
 *
 * @author westberg
 */
public class CallServiceTest {

    public CallServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of makeCall method, of class CallService.
     */
    @Test
    public void testMakeCall()
    {
        System.out.println("makeCall");
        PersonNameType oName = new PersonNameType();
        oName.setFamilyName("Smith");
        CallService instance = new CallService();
        PersonNameType oResult = instance.makeCall(oName);

        assertEquals("Smith", oResult.getFamilyName());
    }

}