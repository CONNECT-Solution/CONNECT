/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author svalluripalli
 */
public class StringUtilTest {

    public StringUtilTest() {
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
     * Test of readTextFile method, of class StringUtil.
     */
//    @Test
//    public void testReadTextFile() throws Exception {
//        System.out.println("readTextFile");
//        String sFileName = "";
//        String expResult = "";
//        String result = StringUtil.readTextFile(sFileName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of extractStringFromTokens method, of class StringUtil.
     */
    @Test
    public void testExtractStringFromTokens() {
        System.out.println("extractStringFromTokens");
        String tokenString = "('1.2.3.4')";
        String tokens = "'()";
        String expResult = "1.2.3.4";
        String result = StringUtil.extractStringFromTokens(tokenString, tokens);
        System.out.println(result);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}