/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.connectmgr.data;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vvickers
 */
public class CMBindingNamesTest {

    public CMBindingNamesTest() {
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
     * Test of createCopy method, of class CMBindingNames.
     */
    @Test
    public void testCreateCopy() {

        CMBindingNames origCMBindingNames = generateTestCMBindingNames();
        CMBindingNames copyCMBindingNames = origCMBindingNames.createCopy();
        assertTrue(origCMBindingNames.equals(copyCMBindingNames));
    }

    /**
     * Create a generic CMBindingNames filled with test data
     *
     * @return The test CMBindingNames
     */
    private CMBindingNames generateTestCMBindingNames() {
        CMBindingNames testBindingNames = new CMBindingNames();
        testBindingNames.getName().add("testBindingNames");
        return testBindingNames;
    }

}