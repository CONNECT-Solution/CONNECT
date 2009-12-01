/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.muralmpi;

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
public class PersonNameTest {
    private static final String DEFAULT_STRING = "test";

    public PersonNameTest() {
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
     * Test of getFirstName method, of class PersonName.
     */
    @Test
    public void testGetFirstName() {
        System.out.println("getFirstName");
        PersonName instance = new PersonName();
        String expResult = "";
        String result = instance.getFirstName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
        instance.setFirstName("test");

        result = instance.getFirstName();
        assertEquals(DEFAULT_STRING, result);

    }

    /**
     * Test of getLastName method, of class PersonName.
     */
    @Test
    public void testGetLastName() {
        System.out.println("getLastName");
        PersonName instance = new PersonName();
        String expResult = "";
        String result = instance.getLastName();
        assertEquals(expResult, result);

        instance.setLastName(DEFAULT_STRING);
        result = instance.getLastName();

        assertEquals(DEFAULT_STRING, result);

    }

    /**
     * Test of getMiddleName method, of class PersonName.
     */
    @Test
    public void testGetMiddleName() {
        System.out.println("getMiddleName");
        PersonName instance = new PersonName();
        String expResult = "";
        String result = instance.getMiddleName();
        assertEquals(expResult, result);

        instance.setMiddleName(DEFAULT_STRING);
        result = instance.getMiddleName();
        assertEquals(DEFAULT_STRING, result);
    }


}