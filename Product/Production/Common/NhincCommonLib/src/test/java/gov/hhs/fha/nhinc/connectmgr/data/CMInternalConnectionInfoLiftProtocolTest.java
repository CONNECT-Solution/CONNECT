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
 * @author JHOPPESC
 */
public class CMInternalConnectionInfoLiftProtocolTest {

    public CMInternalConnectionInfoLiftProtocolTest() {
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
     * Test of clear method, of class CMInternalConnectionInfoLiftProtocol.
     *   This tests the clear method when there is data already in the field.
     */
    @Test
    public void testClearWithData() {
        System.out.println("testClearWithData");

        CMInternalConnectionInfoLiftProtocol instance = new CMInternalConnectionInfoLiftProtocol();
        instance.setLiftProtocol("HTTPS");

        assertEquals("HTTPS", instance.getLiftProtocol());

        instance.clear();

        assertEquals("", instance.getLiftProtocol());
    }

    /**
     * Test of clear method, of class CMInternalConnectionInfoLiftProtocol.
     *   This tests the clear method when there is not data already in the field.
     */
    @Test
    public void testClearWithNoData() {
        System.out.println("testClearWithNoData");

        CMInternalConnectionInfoLiftProtocol instance = new CMInternalConnectionInfoLiftProtocol();

        instance.clear();

        assertEquals("", instance.getLiftProtocol());
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocol.
     *   Test that two empty objects are equal
     */
    @Test
    public void testEqualsEmptyObjects() {
        System.out.println("testEqualsEmptyObjects");

        CMInternalConnectionInfoLiftProtocol oCompare = new CMInternalConnectionInfoLiftProtocol();
        CMInternalConnectionInfoLiftProtocol instance = new CMInternalConnectionInfoLiftProtocol();

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocol.
     *   Test that two empty objects are not equal if one is empty
     */
    @Test
    public void testNotEqualsOneEmpty() {
        System.out.println("testNotEqualsOneEmpty");

        CMInternalConnectionInfoLiftProtocol oCompare = new CMInternalConnectionInfoLiftProtocol();
        CMInternalConnectionInfoLiftProtocol instance = new CMInternalConnectionInfoLiftProtocol();
        instance.setLiftProtocol("HTTPS");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocol.
     */
    @Test
    public void testEquals() {
        System.out.println("testEquals");
        CMInternalConnectionInfoLiftProtocol oCompare = new CMInternalConnectionInfoLiftProtocol();
        oCompare.setLiftProtocol("FTP");
        CMInternalConnectionInfoLiftProtocol instance = new CMInternalConnectionInfoLiftProtocol();
        instance.setLiftProtocol("FTP");
        
        boolean expResult = true;

        boolean result = instance.equals(oCompare);

        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocol.
     *   Test that two empty objects are not equal if values do not match
     */
    @Test
    public void testNotEquals() {
        System.out.println("testNotEquals");

        CMInternalConnectionInfoLiftProtocol oCompare = new CMInternalConnectionInfoLiftProtocol();
        oCompare.setLiftProtocol("HTTPS");
        CMInternalConnectionInfoLiftProtocol instance = new CMInternalConnectionInfoLiftProtocol();
        instance.setLiftProtocol("FTP");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocol.
     *   Test that two objects are equal if both have the same value, but
     *   one is lower case
     */
    @Test
    public void testEqualsOneLower() {
        System.out.println("testEqualsOneLower");

        CMInternalConnectionInfoLiftProtocol oCompare = new CMInternalConnectionInfoLiftProtocol();
        oCompare.setLiftProtocol("HTTP");
        CMInternalConnectionInfoLiftProtocol instance = new CMInternalConnectionInfoLiftProtocol();
        instance.setLiftProtocol("http");

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocol.
     *   Test that two objects are equal if both have the same value, but
     *   one is mixed case
     */
    @Test
    public void testEqualsOneMixed() {
        System.out.println("testEqualsOneMixed");

        CMInternalConnectionInfoLiftProtocol oCompare = new CMInternalConnectionInfoLiftProtocol();
        oCompare.setLiftProtocol("HTTPS");
        CMInternalConnectionInfoLiftProtocol instance = new CMInternalConnectionInfoLiftProtocol();
        instance.setLiftProtocol("Https");

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocol.
     *   Test that two objects are equal if both are null
     */
    @Test
    public void testEqualsBothNull() {
        System.out.println("testEqualsBothNull");

        CMInternalConnectionInfoLiftProtocol oCompare = new CMInternalConnectionInfoLiftProtocol();
        oCompare.setLiftProtocol(null);
        CMInternalConnectionInfoLiftProtocol instance = new CMInternalConnectionInfoLiftProtocol();
        instance.setLiftProtocol(null);

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocol.
     *   Test that two objects are not equal if one is null and one is not
     */
    @Test
    public void testNotEqualsOneNull() {
        System.out.println("testNotEqualsOneNull");

        CMInternalConnectionInfoLiftProtocol oCompare = new CMInternalConnectionInfoLiftProtocol();
        oCompare.setLiftProtocol(null);
        CMInternalConnectionInfoLiftProtocol instance = new CMInternalConnectionInfoLiftProtocol();
        instance.setLiftProtocol("SMTP");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of getProtocol method, of class CMInternalConnectionInfoLiftProtocol.
     *    Test empty case for Set/Get
     */
    @Test
    public void testSetGetProtocolEmpty() {
        System.out.println("testSetGetProtocolEmpty");

        CMInternalConnectionInfoLiftProtocol instance = new CMInternalConnectionInfoLiftProtocol();

        String result = instance.getLiftProtocol();

        assertEquals("", result);
    }

    /**
     * Test of getProtocol method, of class CMInternalConnectionInfoLiftProtocol.
     *    Test case for Set/Get with a value (all upper)
     */
    @Test
    public void testSetGetProtocolUpper() {
        System.out.println("testSetGetProtocolUpper");

        CMInternalConnectionInfoLiftProtocol instance = new CMInternalConnectionInfoLiftProtocol();
        instance.setLiftProtocol("HTTPS");

        String result = instance.getLiftProtocol();

        assertEquals("HTTPS", result);
    }

    /**
     * Test of getName method, of class CMInternalConnectionInfoLiftProtocol.
     *    Test case for Set/Get with a value (all lower)
     */
    @Test
    public void testSetGetProtocolLower() {
        System.out.println("testSetGetProtocolLower");

        CMInternalConnectionInfoLiftProtocol instance = new CMInternalConnectionInfoLiftProtocol();
        instance.setLiftProtocol("ftp");

        String result = instance.getLiftProtocol();

        assertEquals("ftp", result);
    }

    /**
     * Test of getProtocol method, of class CMInternalConnectionInfoLiftProtocol.
     *    Test case for Set/Get with a value (mixed)
     */
    @Test
    public void testSetGetProtocolMixed() {
        System.out.println("testSetGetProtocolMixed");

        CMInternalConnectionInfoLiftProtocol instance = new CMInternalConnectionInfoLiftProtocol();
        instance.setLiftProtocol("HtTpS");

        String result = instance.getLiftProtocol();

        assertEquals("HtTpS", result);
    }

    /**
     * Test of getProtocol method, of class CMInternalConnectionInfoLiftProtocol.
     *    Test case for Set/Get with a null value
     */
    @Test
    public void testSetGetProtocolNull() {
        System.out.println("testSetGetProtocolNull");

        CMInternalConnectionInfoLiftProtocol instance = new CMInternalConnectionInfoLiftProtocol();
        instance.setLiftProtocol(null);

        String result = instance.getLiftProtocol();

        assertNull(result);
    }
}