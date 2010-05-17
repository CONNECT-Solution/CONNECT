package gov.hhs.fha.nhinc.gateway.aggregator.model;

import org.junit.*;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by
 * User: ralph
 * Date: May 17, 2010
 * Time: 4:12:00 PM
 */
public class AggMessageResultTest {
    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testMessageId() {
        AggMessageResult        messageResult;

        messageResult = new AggMessageResult();

        messageResult.setMessageId("xxx123");

        assertEquals(messageResult.getMessageId(), "xxx123");
    }

    @Test
    public void testMessageKey() {
        AggMessageResult        messageResult;

        messageResult = new AggMessageResult();

        messageResult.setMessageKey("yyy456");

        assertEquals(messageResult.getMessageKey(), "yyy456");
    }


    @Test
    public void testMessageOutTime() {
        AggMessageResult        messageResult;
        Date                    date;

        messageResult = new AggMessageResult();
        date = new Date();

        messageResult.setMessageOutTime(date);

        assertEquals(messageResult.getMessageOutTime(), date);
    }

    @Test
    public void testResponseReceivedTime() {
        AggMessageResult        messageResult;
        Date                    date;

        messageResult = new AggMessageResult();
        date = new Date();

        messageResult.setResponseReceivedTime(date);

        assertEquals(messageResult.getResponseReceivedTime(), date);
    }

    @Test
    public void testResponseMessageType() {
        AggMessageResult        messageResult;
        String                  responseType = "Response Type 1";

        messageResult = new AggMessageResult();

        messageResult.setResponseMessageType(responseType);

        assertEquals(messageResult.getResponseMessageType(), responseType);
    }

    @Test
    public void testResponseMessage() {
        AggMessageResult        messageResult;
        String                  message = "A simulated ResponseMessage";

        messageResult = new AggMessageResult();

        messageResult.setResponseMessage(message);

        assertEquals(messageResult.getResponseMessage(), message);
    }

    @Test
    public void testResponseMessageAsBytes() {
        AggMessageResult        messageResult;
        byte[]                  message = "A simulated ResponseMessage".getBytes();

        messageResult = new AggMessageResult();

        messageResult.setResponseMessageAsByteArray(message);

        assertEquals(messageResult.getResponseMessageAsByteArray(), message);
    }

    @Test
    public void testAggTransaction() {
        AggMessageResult        messageResult;
        AggTransaction          aggTrans;

        messageResult = new AggMessageResult();
        aggTrans = new AggTransaction();

        messageResult.setAggTransaction(aggTrans);

        assertEquals(messageResult.getAggTransaction(), aggTrans);
    }

}

