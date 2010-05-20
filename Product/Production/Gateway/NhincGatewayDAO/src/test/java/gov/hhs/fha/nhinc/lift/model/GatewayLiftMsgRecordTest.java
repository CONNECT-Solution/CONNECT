package gov.hhs.fha.nhinc.lift.model;

import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class tests the GatewayLiftMsgRecord class.
 *
 * @author Les Westberg
 */
public class GatewayLiftMsgRecordTest
{

    /**
     * Default constructor.
     */
    public GatewayLiftMsgRecordTest()
    {
    }

    /**
     * Method run before the class is used.
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    /**
     * Method run after the class is run.
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    /**
     * Method run before each test.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Method run after each test.
     */
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getAssertion method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testGetAssertion()
    {
        System.out.println("getAssertion");
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        Blob result = instance.getAssertion();
        assertNull(result);
    }

    /**
     * Test of setAssertion method, of class GatewayLiftMsgRecord.
     *
     * This has been commented out because there is no way to get a Blob
     * without using another class like the Hibernate class and that cannot
     * be done in a unit test.
     */
//    @Test
//    public void testSetGetAssertion()
//    {
//        System.out.println("SetGetAssertion");
//        Blob assertion = null;
//        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
//        instance.setAssertion(assertion);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of getFileNameToRetrieve method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testGetFileNameToRetrieve()
    {
        System.out.println("getFileNameToRetrieve");
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        String result = instance.getFileNameToRetrieve();
        assertNull(result);
    }

    /**
     * Test of setFileNameToRetrieve method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testSetGetFileNameToRetrieve()
    {
        System.out.println("setGetFileNameToRetrieve");
        String fileNameToRetrieve = "test.xml";
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        instance.setFileNameToRetrieve(fileNameToRetrieve);
        String result = instance.getFileNameToRetrieve();
        assertEquals(fileNameToRetrieve, result);
    }

    /**
     * Test of getId method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testGetId()
    {
        System.out.println("getId");
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        Long result = instance.getId();
        assertNull(result);
    }

    /**
     * Test of setId method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testSetGetId()
    {
        System.out.println("setGetId");
        Long id = new Long(100);
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        instance.setId(id);
        Long result = instance.getId();
        assertEquals(id.longValue(), result.longValue());
    }

    /**
     * Test of getInitialEntryTimestamp method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testGetInitialEntryTimestamp()
    {
        System.out.println("getInitialEntryTimestamp");
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        Date result = instance.getInitialEntryTimestamp();
        assertNull(result);
    }

    /**
     * Test of setInitialEntryTimestamp method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testSetGetInitialEntryTimestamp()
    {
        SimpleDateFormat oFormat = new SimpleDateFormat("MM/dd/yyyy.HH:mm:ss");

        System.out.println("setGetInitialEntryTimestamp");
        Date initialEntryTimestamp = new Date();
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        instance.setInitialEntryTimestamp(initialEntryTimestamp);
        Date result = instance.getInitialEntryTimestamp();
        String sInitialEntryTimestamp = oFormat.format(initialEntryTimestamp);
        String sResult = oFormat.format(result);
        assertEquals(sInitialEntryTimestamp, sResult);
    }

    /**
     * Test of getMessage method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testGetMessage()
    {
        System.out.println("getMessage");
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        Blob result = instance.getMessage();
        assertNull(result);
    }

    /**
     * Test of setMessage method, of class GatewayLiftMsgRecord.
     *
     * This has been commented out because there is no way to get a Blob
     * without using another class like the Hibernate class and that cannot
     * be done in a unit test.
     */
//    @Test
//    public void testSetMessage()
//    {
//        System.out.println("setMessage");
//        Blob message = null;
//        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
//        instance.setMessage(message);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of getMessageState method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testGetMessageState()
    {
        System.out.println("getMessageState");
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        String result = instance.getMessageState();
        assertNull(result);
    }

    /**
     * Test of setMessageState method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testSetGetMessageState()
    {
        System.out.println("setGetMessageState");
        String messageState = "PROCESSING";
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        instance.setMessageState(messageState);
        String result = instance.getMessageState();
        assertEquals(messageState, result);
    }

    /**
     * Test of getMessageType method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testGetMessageType()
    {
        System.out.println("getMessageType");
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        String result = instance.getMessageType();
        assertNull(result);
    }

    /**
     * Test of setMessageType method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testSetGetMessageType()
    {
        System.out.println("setGetMessageType");
        String messageType = "DEFERRED_DOCUMENT_SUBMISSION";
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        instance.setMessageType(messageType);
        String result = instance.getMessageType();
        assertEquals(messageType, result);
    }

    /**
     * Test of getProcessingStartTimestamp method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testGetProcessingStartTimestamp()
    {
        System.out.println("getProcessingStartTimestamp");
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        Date result = instance.getProcessingStartTimestamp();
        assertNull(result);
    }
    
    /**
     * Test of setProcessingStartTimestamp method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testSetGetProcessingStartTimestamp()
    {
        SimpleDateFormat oFormat = new SimpleDateFormat("MM/dd/yyyy.HH:mm:ss");

        System.out.println("setGetProcessingStartTimestamp");
        Date processingStartTimestamp = new Date();
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        instance.setProcessingStartTimestamp(processingStartTimestamp);
        Date result = instance.getProcessingStartTimestamp();
        String sExpected = oFormat.format(processingStartTimestamp);
        String sResult = oFormat.format(result);
        assertEquals(sExpected, sResult);
    }

    /**
     * Test of getProducerProxyAddress method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testGetProducerProxyAddress()
    {
        System.out.println("getProducerProxyAddress");
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        String result = instance.getProducerProxyAddress();
        assertNull(result);
    }

    /**
     * Test of setProducerProxyAddress method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testSetGetProducerProxyAddress()
    {
        System.out.println("setGetProducerProxyAddress");
        String producerProxyAddress = "http://www.someplace.com";
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        instance.setProducerProxyAddress(producerProxyAddress);
        String result = instance.getProducerProxyAddress();
        assertEquals(producerProxyAddress, result);
    }

    /**
     * Test of getProducerProxyPort method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testGetProducerProxyPort()
    {
        System.out.println("getProducerProxyPort");
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        Long result = instance.getProducerProxyPort();
        assertNull(result);
    }

    /**
     * Test of setProducerProxyPort method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testSetGetProducerProxyPort()
    {
        System.out.println("setGetProducerProxyPort");
        Long producerProxyPort = new Long(5555);
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        instance.setProducerProxyPort(producerProxyPort);
        Long result = instance.getProducerProxyPort();
        assertEquals(producerProxyPort.longValue(), result.longValue());
    }

    /**
     * Test of getRequestKeyGuid method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testGetRequestKeyGuid()
    {
        System.out.println("getRequestKeyGuid");
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        String result = instance.getRequestKeyGuid();
        assertNull(result);
    }

    /**
     * Test of setRequestKeyGuid method, of class GatewayLiftMsgRecord.
     */
    @Test
    public void testSetGetRequestKeyGuid()
    {
        System.out.println("setGetRequestKeyGuid");
        String requestKeyGuid = "1.1.1";
        GatewayLiftMsgRecord instance = new GatewayLiftMsgRecord();
        instance.setRequestKeyGuid(requestKeyGuid);
        String result = instance.getRequestKeyGuid();
        assertEquals(requestKeyGuid, result);
    }
}
