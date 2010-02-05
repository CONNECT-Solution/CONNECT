/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.ArrayList;
import java.util.List;
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
public class CMInternalConnectionInfoStatesTest {

    public CMInternalConnectionInfoStatesTest() {
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
     * Test of clear method, of class CMInternalConnectionInfoStates.
     *   This tests the clear method with a single entry in the list.
     */
    @Test
    public void testClearWithSingle() {
        System.out.println("testClearWithSingle");

        CMInternalConnectionInfoStates instance = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state = new CMInternalConnectionInfoState();
        state.setName("IL");
        instance.getState().add(state);

        assertEquals(1, instance.getState().size());
        assertEquals("IL", instance.getState().get(0).getName());

        instance.clear();

        assertEquals(true, instance.getState().isEmpty());
    }

    /**
     * Test of clear method, of class CMInternalConnectionInfoStates.
     *   This tests the clear method with a multiple entries in the list.
     */
    @Test
    public void testClearWithMultiple() {
        System.out.println("testClearWithMultiple");

        CMInternalConnectionInfoStates instance = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state1 = new CMInternalConnectionInfoState();
        state1.setName("IL");
        CMInternalConnectionInfoState state2 = new CMInternalConnectionInfoState();
        state2.setName("FL");
        CMInternalConnectionInfoState state3 = new CMInternalConnectionInfoState();
        state3.setName("VA");
        instance.getState().add(state1);
        instance.getState().add(state2);
        instance.getState().add(state3);

        assertEquals(3, instance.getState().size());
        assertEquals("IL", instance.getState().get(0).getName());
        assertEquals("FL", instance.getState().get(1).getName());
        assertEquals("VA", instance.getState().get(2).getName());

        instance.clear();

        assertEquals(true, instance.getState().isEmpty());
    }

    /**
     * Test of clear method, of class CMInternalConnectionInfoStates.
     *   This tests the clear method when there is no entries in the list.
     */
    @Test
    public void testClearWithNoEntries() {
        System.out.println("testClearWithNoEntries");

        CMInternalConnectionInfoStates instance = new CMInternalConnectionInfoStates();

        instance.clear();

        assertEquals(true, instance.getState().isEmpty());
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two empty objects are equal
     */
    @Test
    public void testEqualsEmptyObjects() {
        System.out.println("testEqualsEmptyObjects");

        CMInternalConnectionInfoStates oCompare = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoStates instance = new CMInternalConnectionInfoStates();

        boolean result = instance.equals(oCompare);
        
        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with single entries are equal
     */
    @Test
    public void testEqualsSingleEntries() {
        System.out.println("testEqualsSingleEntries");

        CMInternalConnectionInfoStates oCompare = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state1 = new CMInternalConnectionInfoState();
        state1.setName("IL");
        oCompare.getState().add(state1);

        CMInternalConnectionInfoStates instance = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state2 = new CMInternalConnectionInfoState();
        state2.setName("IL");
        instance.getState().add(state2);

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with multiple entries are equal
     */
    @Test
    public void testEqualsMultipleEntries() {
        System.out.println("testEqualsMultipleEntries");

        CMInternalConnectionInfoStates oCompare = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state1 = new CMInternalConnectionInfoState();
        state1.setName("IL");
        CMInternalConnectionInfoState state2 = new CMInternalConnectionInfoState();
        state2.setName("FL");
        CMInternalConnectionInfoState state3 = new CMInternalConnectionInfoState();
        state3.setName("VA");
        oCompare.getState().add(state1);
        oCompare.getState().add(state2);
        oCompare.getState().add(state3);

        CMInternalConnectionInfoStates instance = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state4 = new CMInternalConnectionInfoState();
        state4.setName("IL");
        CMInternalConnectionInfoState state5 = new CMInternalConnectionInfoState();
        state5.setName("FL");
        CMInternalConnectionInfoState state6 = new CMInternalConnectionInfoState();
        state6.setName("VA");
        instance.getState().add(state4);
        instance.getState().add(state5);
        instance.getState().add(state6);

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }
    
    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with multiple entries of different cases are equal
     */
    @Test
    public void testEqualsDifferentCases() {
        System.out.println("testEqualsMultipleEntries");

        CMInternalConnectionInfoStates oCompare = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state1 = new CMInternalConnectionInfoState();
        state1.setName("IL");
        CMInternalConnectionInfoState state2 = new CMInternalConnectionInfoState();
        state2.setName("Fl");
        CMInternalConnectionInfoState state3 = new CMInternalConnectionInfoState();
        state3.setName("Va");
        oCompare.getState().add(state1);
        oCompare.getState().add(state2);
        oCompare.getState().add(state3);

        CMInternalConnectionInfoStates instance = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state4 = new CMInternalConnectionInfoState();
        state4.setName("il");
        CMInternalConnectionInfoState state5 = new CMInternalConnectionInfoState();
        state5.setName("FL");
        CMInternalConnectionInfoState state6 = new CMInternalConnectionInfoState();
        state6.setName("vA");
        instance.getState().add(state4);
        instance.getState().add(state5);
        instance.getState().add(state6);

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with single entries are not equal
     */
    @Test
    public void testNotEqualsSingleEntries() {
        System.out.println("testNotEqualsSingleEntries");

        CMInternalConnectionInfoStates oCompare = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state1 = new CMInternalConnectionInfoState();
        state1.setName("IL");
        oCompare.getState().add(state1);

        CMInternalConnectionInfoStates instance = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state2 = new CMInternalConnectionInfoState();
        state2.setName("FL");
        instance.getState().add(state2);

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with multiple entries are not equal
     */
    @Test
    public void testNotEqualsMultipleEntriesOneMismatch() {
        System.out.println("testNotEqualsMultipleEntriesOneMismatch");

        CMInternalConnectionInfoStates oCompare = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state1 = new CMInternalConnectionInfoState();
        state1.setName("IL");
        CMInternalConnectionInfoState state2 = new CMInternalConnectionInfoState();
        state2.setName("FL");
        CMInternalConnectionInfoState state3 = new CMInternalConnectionInfoState();
        state3.setName("GA");
        oCompare.getState().add(state1);
        oCompare.getState().add(state2);
        oCompare.getState().add(state3);

        CMInternalConnectionInfoStates instance = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state4 = new CMInternalConnectionInfoState();
        state4.setName("IL");
        CMInternalConnectionInfoState state5 = new CMInternalConnectionInfoState();
        state5.setName("FL");
        CMInternalConnectionInfoState state6 = new CMInternalConnectionInfoState();
        state6.setName("VA");
        instance.getState().add(state4);
        instance.getState().add(state5);
        instance.getState().add(state6);

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with multiple entries are not equal
     */
    @Test
    public void testNotEqualsMultipleEntriesTwoMismatch() {
        System.out.println("testNotEqualsMultipleEntriesTwoMismatch");

        CMInternalConnectionInfoStates oCompare = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state1 = new CMInternalConnectionInfoState();
        state1.setName("IL");
        CMInternalConnectionInfoState state2 = new CMInternalConnectionInfoState();
        state2.setName("FL");
        CMInternalConnectionInfoState state3 = new CMInternalConnectionInfoState();
        state3.setName("GA");
        oCompare.getState().add(state1);
        oCompare.getState().add(state2);
        oCompare.getState().add(state3);

        CMInternalConnectionInfoStates instance = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state4 = new CMInternalConnectionInfoState();
        state4.setName("MD");
        CMInternalConnectionInfoState state5 = new CMInternalConnectionInfoState();
        state5.setName("FL");
        CMInternalConnectionInfoState state6 = new CMInternalConnectionInfoState();
        state6.setName("VA");
        instance.getState().add(state4);
        instance.getState().add(state5);
        instance.getState().add(state6);

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with multiple entries are not equal
     */
    @Test
    public void testNotEqualsMultipleEntriesAllMismatch() {
        System.out.println("testNotEqualsMultipleEntriesAllMismatch");

        CMInternalConnectionInfoStates oCompare = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state1 = new CMInternalConnectionInfoState();
        state1.setName("IL");
        CMInternalConnectionInfoState state2 = new CMInternalConnectionInfoState();
        state2.setName("NY");
        CMInternalConnectionInfoState state3 = new CMInternalConnectionInfoState();
        state3.setName("GA");
        oCompare.getState().add(state1);
        oCompare.getState().add(state2);
        oCompare.getState().add(state3);

        CMInternalConnectionInfoStates instance = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state4 = new CMInternalConnectionInfoState();
        state4.setName("MD");
        CMInternalConnectionInfoState state5 = new CMInternalConnectionInfoState();
        state5.setName("FL");
        CMInternalConnectionInfoState state6 = new CMInternalConnectionInfoState();
        state6.setName("VA");
        instance.getState().add(state4);
        instance.getState().add(state5);
        instance.getState().add(state6);

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with different sizes are not equal
     */
    @Test
    public void testNotEqualsDifferentSizes() {
        System.out.println("testNotEqualsDifferentSizes");

        CMInternalConnectionInfoStates oCompare = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state1 = new CMInternalConnectionInfoState();
        state1.setName("IL");
        CMInternalConnectionInfoState state2 = new CMInternalConnectionInfoState();
        state2.setName("FL");
        CMInternalConnectionInfoState state3 = new CMInternalConnectionInfoState();
        state3.setName("GA");
        oCompare.getState().add(state1);
        oCompare.getState().add(state2);
        oCompare.getState().add(state3);

        CMInternalConnectionInfoStates instance = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state4 = new CMInternalConnectionInfoState();
        state4.setName("IL");
        CMInternalConnectionInfoState state5 = new CMInternalConnectionInfoState();
        state5.setName("FL");
        instance.getState().add(state4);
        instance.getState().add(state5);

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with single entries are not equal if one is empty
     */
    @Test
    public void testNotEqualsSingleEntriesOneNull() {
        System.out.println("testNotEqualsSingleEntriesOneNull");

        CMInternalConnectionInfoStates oCompare = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state1 = new CMInternalConnectionInfoState();
        state1.setName("IL");
        oCompare.getState().add(state1);

        CMInternalConnectionInfoStates instance = new CMInternalConnectionInfoStates();

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of getStates method, of class CMInternalConnectionInfoStates.
     *    Test retreiving states from an empty list
     */
    @Test
    public void testGetStatesNone() {
        System.out.println("testGetStatesNone");

        CMInternalConnectionInfoStates instance = new CMInternalConnectionInfoStates();

        List expResult = new ArrayList<CMInternalConnectionInfoState>();

        List result = instance.getState();

        assertEquals(expResult, result);
        assertEquals(true, result.isEmpty());
    }

    /**
     * Test of getStates method, of class CMInternalConnectionInfoStates.
     *    Test retreiving states with a single entry
     */
    @Test
    public void testGetStatesOne() {
        System.out.println("testGetStatesNone");

        CMInternalConnectionInfoStates instance = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state1 = new CMInternalConnectionInfoState();
        state1.setName("IL");
        instance.getState().add(state1);

        List<CMInternalConnectionInfoState> result = instance.getState();

        assertEquals("IL", result.get(0).getName());
    }

    /**
     * Test of getStates method, of class CMInternalConnectionInfoStates.
     *    Test retreiving states with multiple entries
     */
    @Test
    public void testGetStatesMultiple() {
        System.out.println("testGetStatesNone");

        CMInternalConnectionInfoStates instance = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state1 = new CMInternalConnectionInfoState();
        state1.setName("IL");
        CMInternalConnectionInfoState state2 = new CMInternalConnectionInfoState();
        state2.setName("FL");
        CMInternalConnectionInfoState state3 = new CMInternalConnectionInfoState();
        state3.setName("VA");
        instance.getState().add(state1);
        instance.getState().add(state2);
        instance.getState().add(state3);

        List<CMInternalConnectionInfoState> result = instance.getState();

        assertEquals("IL", result.get(0).getName());
        assertEquals("FL", result.get(1).getName());
        assertEquals("VA", result.get(2).getName());
    }

}