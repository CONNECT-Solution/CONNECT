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
public class CMInternalConnectionInfoStateTest {

    public CMInternalConnectionInfoStateTest() {
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
     * Test of clear method, of class CMInternalConnectionInfoState.
     *   This tests the clear method when there is data already in the field.
     */
    @Test
    public void testClearWithData() {
        System.out.println("testClearWithData");
        
        CMInternalConnectionInfoState instance = new CMInternalConnectionInfoState();
        instance.setName("IL");

        assertEquals("IL", instance.getName());

        instance.clear();

        assertEquals("", instance.getName());
    }

    /**
     * Test of clear method, of class CMInternalConnectionInfoState.
     *   This tests the clear method when there is not data already in the field.
     */
    @Test
    public void testClearWithNoData() {
        System.out.println("testClearWithNoData");

        CMInternalConnectionInfoState instance = new CMInternalConnectionInfoState();

        instance.clear();

        assertEquals("", instance.getName());
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoState.
     *   Test that two empty objects are equal
     */
    @Test
    public void testEqualsEmptyObjects() {
        System.out.println("testEqualsEmptyObjects");

        CMInternalConnectionInfoState oCompare = new CMInternalConnectionInfoState();
        CMInternalConnectionInfoState instance = new CMInternalConnectionInfoState();   

        boolean result = instance.equals(oCompare);
        
        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoState.
     *   Test that two empty objects are not equal if one is empty
     */
    @Test
    public void testNotEqualsOneEmpty() {
        System.out.println("testNotEqualsOneEmpty");

        CMInternalConnectionInfoState oCompare = new CMInternalConnectionInfoState();
        CMInternalConnectionInfoState instance = new CMInternalConnectionInfoState();
        instance.setName("FL");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoState.
     *   Test that two empty objects are equal if both have the same value
     */
    @Test
    public void testEquals() {
        System.out.println("testEquals");

        CMInternalConnectionInfoState oCompare = new CMInternalConnectionInfoState();
        oCompare.setName("VA");
        CMInternalConnectionInfoState instance = new CMInternalConnectionInfoState();
        instance.setName("VA");

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoState.
     *   Test that two empty objects are not equal if values do not match
     */
    @Test
    public void testNotEquals() {
        System.out.println("testNotEquals");

        CMInternalConnectionInfoState oCompare = new CMInternalConnectionInfoState();
        oCompare.setName("VA");
        CMInternalConnectionInfoState instance = new CMInternalConnectionInfoState();
        instance.setName("FL");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoState.
     *   Test that two objects are equal if both have the same value, but
     *   one is lower case
     */
    @Test
    public void testEqualsOneLower() {
        System.out.println("testEqualsOneLower");

        CMInternalConnectionInfoState oCompare = new CMInternalConnectionInfoState();
        oCompare.setName("VA");
        CMInternalConnectionInfoState instance = new CMInternalConnectionInfoState();
        instance.setName("va");

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoState.
     *   Test that two objects are equal if both have the same value, but
     *   one is mixed case
     */
    @Test
    public void testEqualsOneMixed() {
        System.out.println("testEqualsOneMixed");

        CMInternalConnectionInfoState oCompare = new CMInternalConnectionInfoState();
        oCompare.setName("VA");
        CMInternalConnectionInfoState instance = new CMInternalConnectionInfoState();
        instance.setName("Va");

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoState.
     *   Test that two objects are equal if both are null
     */
    @Test
    public void testEqualsBothNull() {
        System.out.println("testEqualsBothNull");

        CMInternalConnectionInfoState oCompare = new CMInternalConnectionInfoState();
        oCompare.setName(null);
        CMInternalConnectionInfoState instance = new CMInternalConnectionInfoState();
        instance.setName(null);

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoState.
     *   Test that two objects are not equal if one is null and one is not
     */
    @Test
    public void testNotEqualsOneNull() {
        System.out.println("testNotEqualsOneNull");

        CMInternalConnectionInfoState oCompare = new CMInternalConnectionInfoState();
        oCompare.setName(null);
        CMInternalConnectionInfoState instance = new CMInternalConnectionInfoState();
        instance.setName("GA");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of getName method, of class CMInternalConnectionInfoState.
     *    Test empty case for Set/Get
     */
    @Test
    public void testSetGetNameEmpty() {
        System.out.println("testGetNameEmpty");

        CMInternalConnectionInfoState instance = new CMInternalConnectionInfoState();
        
        String result = instance.getName();

        assertEquals("", result);
    }

    /**
     * Test of getName method, of class CMInternalConnectionInfoState.
     *    Test case for Set/Get with a value (all upper)
     */
    @Test
    public void testSetGetNameUpper() {
        System.out.println("testSetGetNameUpper");

        CMInternalConnectionInfoState instance = new CMInternalConnectionInfoState();
        instance.setName("IL");

        String result = instance.getName();

        assertEquals("IL", result);
    }

    /**
     * Test of getName method, of class CMInternalConnectionInfoState.
     *    Test case for Set/Get with a value (all lower)
     */
    @Test
    public void testSetGetNameLower() {
        System.out.println("testSetGetNameLower");

        CMInternalConnectionInfoState instance = new CMInternalConnectionInfoState();
        instance.setName("il");

        String result = instance.getName();

        assertEquals("il", result);
    }

    /**
     * Test of getName method, of class CMInternalConnectionInfoState.
     *    Test case for Set/Get with a value (mixed)
     */
    @Test
    public void testSetGetNameMixed() {
        System.out.println("testSetGetNameMixed");

        CMInternalConnectionInfoState instance = new CMInternalConnectionInfoState();
        instance.setName("Il");

        String result = instance.getName();

        assertEquals("Il", result);
    }

    /**
     * Test of getName method, of class CMInternalConnectionInfoState.
     *    Test case for Set/Get with a null value
     */
    @Test
    public void testSetGetNameNull() {
        System.out.println("testSetGetNameNull");

        CMInternalConnectionInfoState instance = new CMInternalConnectionInfoState();
        instance.setName(null);

        String result = instance.getName();

        assertNull(result);
    }

}