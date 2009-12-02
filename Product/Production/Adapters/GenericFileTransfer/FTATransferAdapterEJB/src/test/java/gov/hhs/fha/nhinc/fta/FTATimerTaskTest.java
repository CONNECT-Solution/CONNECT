/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.fta;

import gov.hhs.fha.nhinc.common.ftaconfigmanager.FTAConfiguration;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;

/**
 *
 * @author dunnek
 */
public class FTATimerTaskTest {

    public FTATimerTaskTest() {
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
     * Test of getConfiguration method, of class FTATimerTask.
     */
    @Test
    public void testGetConfiguration() {
        System.out.println("getConfiguration");
        FTATimerTask instance = new FTATimerTask();
        FTAConfiguration expResult = null;

        instance.setConfiguration(expResult);

        FTAConfiguration result = instance.getConfiguration();
        assertEquals(expResult, result);

    }

    /**
     * Test of run method, of class FTATimerTask.
     */


    /**
     * Test of getFileContents method, of class FTATimerTask.
     */
    @Test
    public void testGetFileContents() {
        System.out.println("getFileContents");
        File aFile = new File("unitTest.txt");
        String expResult = "unitTest"+System.getProperty("line.separator");
        String result = Util.getFileContents(aFile);
        assertEquals(expResult, result);
    }

}