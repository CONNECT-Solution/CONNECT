/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.common.ftaconfigmanager;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author dunnek
 */
//TODO: Move test to Integration Test Suite
public class FTAChannelTest {
    private static final String TEST_TOPIC = "nhinc:topic";
    private static final String TEST_TYPE = "file";
    private static final String TEST_LOCATION = "c:\\test\\testsubdir\\";


    public FTAChannelTest() {
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
     * Test of getTopic method, of class FTAChannel.
     */
    @Test
    public void testGetTopic() {
        System.out.println("getTopic");
        FTAChannel instance = new FTAChannel();
        String expResult = "";
        String result = instance.getTopic();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

        instance.setTopic(TEST_TOPIC);
        result = instance.getTopic();
        assertEquals(TEST_TOPIC, result);


    }

    /**
     * Test of getType method, of class FTAChannel.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        FTAChannel instance = new FTAChannel();
        String expResult = "";
        String result = instance.getType();
        assertEquals(expResult, result);

        instance.setType(TEST_TYPE);
        result = instance.getType();
        assertEquals(TEST_TYPE, result);
    }

    /**
     * Test of getLocation method, of class FTAChannel.
     */
    @Test
    public void testGetLocation() {
        System.out.println("getLocation");
        FTAChannel instance = new FTAChannel();
        String expResult = "";
        String result = instance.getLocation();
        assertEquals(expResult, result);

        instance.setLocation(TEST_LOCATION);
        result = instance.getLocation();
        assertEquals(TEST_LOCATION, result);
    
    }

    /**
     * Test of getAdditionalElements method, of class FTAChannel.
     */
    @Test
    public void testGetAdditionalElements() {
        System.out.println("getAdditionalElements");
        FTAChannel instance = new FTAChannel();
        List<FTAElement> expResult = null;
        List<FTAElement> result = instance.getAdditionalElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

        expResult = new ArrayList<FTAElement>();


        expResult.add(new FTAElement("name", "value"));

        instance.setAdditionalElements(expResult);
        result = instance.getAdditionalElements();
        assertEquals(expResult, result);


    }

}