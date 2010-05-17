/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.lift.file.manager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author JHOPPESC
 */
public class LiFTFileManagerTest {

    public LiFTFileManagerTest() {
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
     * Test of copyFile method, of class LiFTFileManager.
     */
    @Test
    public void testCopyFile() {
        System.out.println("testCopyFile");
        String url = "file://C:/testdata.pdf";
        String destination = "C:/Temp";
        LiFTFileManager instance = new LiFTFileManager();

        boolean result = instance.copyFile(url, destination);

        assertEquals(true, result);
    }

}