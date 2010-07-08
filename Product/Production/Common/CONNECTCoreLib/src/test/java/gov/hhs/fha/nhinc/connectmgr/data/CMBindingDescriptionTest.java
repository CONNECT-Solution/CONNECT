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
public class CMBindingDescriptionTest {

    public CMBindingDescriptionTest() {
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
     * Test of createCopy method, of class CMBindingDescriptions.
     */
    @Test
    public void testCreateCopy() {

        CMBindingDescriptions origCMBindingDescriptions = generateTestCMBindingDescriptions();
        CMBindingDescriptions copyCMBindingDescriptions = origCMBindingDescriptions.createCopy();
        assertTrue(origCMBindingDescriptions.equals(copyCMBindingDescriptions));
    }

    /**
     * Create a generic CMBindingDescriptions filled with test data
     *
     * @return The test CMBindingDescriptions
     */
    private CMBindingDescriptions generateTestCMBindingDescriptions() {
        CMBindingDescriptions testBindingDescriptions = new CMBindingDescriptions();
        testBindingDescriptions.getDescription().add("testBindingDescription");
        return testBindingDescriptions;
    }
}