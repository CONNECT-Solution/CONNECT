/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.fta;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dunnek
 */
@Ignore //TODO:move test to Integtation test suite
public class FTAServiceTest {

    public FTAServiceTest() {
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
     * Test of start method, of class FTAService.
     */
    @Test
    public void testStart() {
        System.out.println("start");
        FTAService instance = new FTAService();
        Boolean expResult = true;
        Boolean result = instance.start();
        assertEquals(expResult, result);

    }

    /**
     * Test of stop method, of class FTAService.
     */
    @Test
    public void testStop() {
        System.out.println("stop");
        FTAService instance = new FTAService();
        Boolean expResult = null;
        Boolean result = instance.stop();
        assertEquals(expResult, result);
    }

}