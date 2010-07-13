/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.lift.model;

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
public class LiftTransferDataRecordTest {

    public LiftTransferDataRecordTest() {
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
     * Test of getId method, of class LiftTransferDataRecord.
     */
    @Test
    public void testSetGetId() {
        System.out.println("testSetGetId");
        LiftTransferDataRecord instance = new LiftTransferDataRecord();
        int value = 33;
        Long id = new Long(value);

        instance.setId(id);

        Long result = instance.getId();

        assertEquals(id, result);
    }

    /**
     * Test of getId method, of class LiftTransferDataRecord.
     */
    @Test
    public void testGetNullId() {
        System.out.println("testGetNullId");
        LiftTransferDataRecord instance = new LiftTransferDataRecord();

        Long result = instance.getId();

        assertNull(result);
    }

    /**
     * Test of getId method, of class LiftTransferDataRecord.
     */
    @Test
    public void testSetNullId() {
        System.out.println("testSetNullId");
        LiftTransferDataRecord instance = new LiftTransferDataRecord();

        instance.setId(null);

        Long result = instance.getId();

        assertNull(result);
    }

    /**
     * Test of getRequestKeyGuid method, of class LiftTransferDataRecord.
     */
    @Test
    public void testSetGetRequestKeyGuid() {
        System.out.println("testSetGetRequestKeyGuid");
        LiftTransferDataRecord instance = new LiftTransferDataRecord();
        String requestKeyGuid = "59139453-9e74-4a33-ba99-4105f2112e54";
        instance.setRequestKeyGuid(requestKeyGuid);

        String result = instance.getRequestKeyGuid();

        assertEquals(requestKeyGuid, result);
    }

    /**
     * Test of getRequestKeyGuid method, of class LiftTransferDataRecord.
     */
    @Test
    public void testGetNullRequestKeyGuid() {
        System.out.println("testGetNullRequestKeyGuid");
        LiftTransferDataRecord instance = new LiftTransferDataRecord();

        String result = instance.getRequestKeyGuid();

        assertNull(result);
    }

    /**
     * Test of getRequestKeyGuid method, of class LiftTransferDataRecord.
     */
    @Test
    public void testSetNullRequestKeyGuid() {
        System.out.println("testSetNullRequestKeyGuid");
        LiftTransferDataRecord instance = new LiftTransferDataRecord();

        instance.setRequestKeyGuid(null);

        String result = instance.getRequestKeyGuid();

        assertNull(result);
    }

    /**
     * Test of getTransferState method, of class LiftTransferDataRecord.
     */
    @Test
    public void testSetGetTransferState() {
        System.out.println("testSetGetTransferState");
        LiftTransferDataRecord instance = new LiftTransferDataRecord();
        String transferState = "ENTERED";
        
        instance.setTransferState(transferState);
        
        String result = instance.getTransferState();

        assertEquals(transferState, result);
    }

    /**
     * Test of getTransferState method, of class LiftTransferDataRecord.
     */
    @Test
    public void testGetNullTransferState() {
        System.out.println("testGetNullTransferState");
        LiftTransferDataRecord instance = new LiftTransferDataRecord();

        String result = instance.getTransferState();

        assertNull(result);
    }

    /**
     * Test of getTransferState method, of class LiftTransferDataRecord.
     */
    @Test
    public void testSetNullTransferState() {
        System.out.println("testSetNullTransferState");
        LiftTransferDataRecord instance = new LiftTransferDataRecord();

        instance.setTransferState(null);

        String result = instance.getTransferState();

        assertNull(result);
    }

}