/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.testpep;

import gov.hhs.fha.nhinc.testpepschema.EnforceResourceInputType;
import gov.hhs.fha.nhinc.testpepschema.EnforceResourceOutputType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Admin
 */
public class TestPEPServiceTest {

    public TestPEPServiceTest() {
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
     * Test of enforceResource method, of class TestPEPService.
     */
    @Test
    public void testEnforceResource() {
        System.out.println("enforceResource");
        EnforceResourceInputType enforceResourceInput = new EnforceResourceInputType();
        enforceResourceInput.setInputParam("Test");
        TestPEPService instance = new TestPEPService();
        EnforceResourceOutputType result = instance.enforceResource(enforceResourceInput);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}