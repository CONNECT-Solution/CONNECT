/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.lift.file.manager;

import java.io.File;
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
        String url = "file:///C:/testdata.pdf";
        String destination = "C:/Temp";
        LiFTFileManager instance = new LiFTFileManager() {
            protected String createFileServerPath(String guid) {
                return "C:/Temp";
            }

            protected boolean transferFile(String filePath, File fileToMove) {
                return true;
            }

            protected boolean createDestDirectory (File destDirectory) {
                return true;
            }
        };

        boolean result = instance.copyFile(url, destination);

        assertEquals(true, result);
    }

    /**
     * Test of copyFile method, of class LiFTFileManager.
     */
    @Test
    public void testCopyFileTransferFailed() {
        System.out.println("testCopyFileTransferFailed");
        String url = "file:///C:/testdata.pdf";
        String destination = "C:/Temp";
        LiFTFileManager instance = new LiFTFileManager() {
            protected String createFileServerPath(String guid) {
                return "C:/Temp";
            }

            protected boolean transferFile(String filePath, File fileToMove) {
                return false;
            }

            protected boolean createDestDirectory (File destDirectory) {
                return true;
            }
        };

        boolean result = instance.copyFile(url, destination);

        assertEquals(false, result);
    }

    /**
     * Test of copyFile method, of class LiFTFileManager.
     */
    @Test
    public void testCopyFileDestDirFailed() {
        System.out.println("testCopyFileDestDirFailed");
        String url = "file:///C:/testdata.pdf";
        String destination = "C:/Temp";
        LiFTFileManager instance = new LiFTFileManager() {
            protected String createFileServerPath(String guid) {
                return "C:/Temp";
            }
            
            protected boolean transferFile(String filePath, File fileToMove) {
                return true;
            }

            protected boolean createDestDirectory (File destDirectory) {
                return false;
            }
        };

        boolean result = instance.copyFile(url, destination);

        assertEquals(false, result);
    }

    /**
     * Test of extractFile method, of class LiFTFileManager.
     */
    @Test
    public void testExtractFile() {
        System.out.println("testExtractFile");
        String url = "C:/Temp/testdata.pdf";
        LiFTFileManager instance = new LiFTFileManager();

       String result = instance.extractFile(url);

        assertEquals("testdata.pdf", result);
    }

}