/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.asyncmsgs.model;

import java.sql.Blob;
import java.util.Date;
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
public class AsyncMsgRecordTest {

    public AsyncMsgRecordTest() {
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
     * Test of getId and setMessageId methods, of class AsyncMsgRecord.
     */
    @Test
    public void testSetGetId() {
        System.out.println("testSetGetMessageId");

        AsyncMsgRecord instance = new AsyncMsgRecord();

        Long expResult = Long.valueOf(2);

        instance.setId(expResult);

        Long result = instance.getId();

        assertEquals(expResult, result);
    }

    /**
     * Test of getId method, of class AsyncMsgRecord.
     */
    @Test
    public void testGetInitId() {
        System.out.println("testGetInitMessageId");

        AsyncMsgRecord instance = new AsyncMsgRecord();

        Long result = instance.getId();

        assertNull(result);
    }

    /**
     * Test of getMessageId and setMessageId methods, of class AsyncMsgRecord.
     */
    @Test
    public void testSetGetMessageId() {
        System.out.println("testSetGetMessageId");

        AsyncMsgRecord instance = new AsyncMsgRecord();

        String expResult = "uuid:1234567890";
        instance.setMessageId(expResult);

        String result = instance.getMessageId();

        assertEquals(expResult, result);
    }

    /**
     * Test of getMessageId method, of class AsyncMsgRecord.
     */
    @Test
    public void testGetInitMessageId() {
        System.out.println("testGetInitMessageId");
        
        AsyncMsgRecord instance = new AsyncMsgRecord();

        String result = instance.getMessageId();

        assertNull(result);
    }

    /**
     * Test of getCreationTime and setCreationTime methods, of class AsyncMsgRecord.
     */
    @Test
    public void testSetGetCreationTime() {
        System.out.println("testSetGetCreationTime");

        AsyncMsgRecord instance = new AsyncMsgRecord();
        Date expResult = new Date();
        instance.setCreationTime(expResult);
       
        Date result = instance.getCreationTime();

        assertEquals(expResult, result);
    }

    /**
     * Test of getCreationTime method, of class AsyncMsgRecord.
     */
    @Test
    public void testGetInitCreationTime() {
        System.out.println("testGetInitCreationTime");

        AsyncMsgRecord instance = new AsyncMsgRecord();
        Date result = instance.getCreationTime();

        assertNull(result);
    }

    /**
     * Test of getServiceName and setServiceName methods, of class AsyncMsgRecord.
     */
    @Test
    public void testSetGetServiceName() {
        System.out.println("testSetGetServiceName");

        AsyncMsgRecord instance = new AsyncMsgRecord();
        String expResult = "PatientDiscovery";
        instance.setServiceName(expResult);

        String result = instance.getServiceName();

        assertEquals(expResult, result);
    }

    /**
     * Test of getServiceName method, of class AsyncMsgRecord.
     */
    @Test
    public void testGetInitServiceName() {
        System.out.println("testGetInitServiceName");
        
        AsyncMsgRecord instance = new AsyncMsgRecord();
        
        String result = instance.getServiceName();

        assertNull(result);
    }

//    /**
//     * Test of getMsgData and setMsgData methods, of class AsyncMsgRecord.
//     */
//    @Test
//    public void testSetGetMsgData() {
//        System.out.println("testSetGetMsgData");
//        AsyncMsgRecord instance = new AsyncMsgRecord();
//        Blob expResult = null;
//        instance.setMsgData(expResult);
//
//        Blob result = instance.getMsgData();
//
//        assertEquals(expResult, result);
//    }

    /**
     * Test of getMsgData method, of class AsyncMsgRecord.
     */
    @Test
    public void testGetInitMsgData() {
        System.out.println("testGetInitMsgData");
        
        AsyncMsgRecord instance = new AsyncMsgRecord();

        Blob result = instance.getMsgData();

        assertNull(result);
    }

}