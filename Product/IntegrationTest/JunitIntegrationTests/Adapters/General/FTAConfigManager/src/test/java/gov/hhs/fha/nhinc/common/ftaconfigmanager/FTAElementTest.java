/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.common.ftaconfigmanager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dunnek
 */
public class FTAElementTest {

    private static final String TEST_NAME = "name";
    private static final String TEST_VALUE = "value";

    public FTAElementTest() {
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
     * Test of getName method, of class FTAElement.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        FTAElement instance = new FTAElement();
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);

        instance.setName(TEST_NAME);
        result = instance.getName();
        assertEquals(TEST_NAME, result);
    }

    /**
     * Test of getValue method, of class FTAElement.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        FTAElement instance = new FTAElement();
        String expResult = "";
        String result = instance.getValue();
        assertEquals(expResult, result);

        instance.setValue(TEST_VALUE);
        result = instance.getValue();
        assertEquals(TEST_VALUE, result);
    }
    @Test
    public void testInstantiation() {
        FTAElement instance = new FTAElement();
        String expValue = "";
        String expName = "";

        String actName;
        String actValue;

        actName = instance.getName();
        actValue = instance.getValue();

        assertEquals(expName, actName);
        assertEquals(expValue, actValue);

        instance = new FTAElement(TEST_NAME,TEST_VALUE);
        actName = instance.getName();
        actValue = instance.getValue();

        assertEquals(TEST_NAME, actName);
        assertEquals(TEST_VALUE, actValue);
        
    }


}