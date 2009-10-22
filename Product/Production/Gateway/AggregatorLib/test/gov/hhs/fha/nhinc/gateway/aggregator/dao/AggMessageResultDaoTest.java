package gov.hhs.fha.nhinc.gateway.aggregator.dao;

import gov.hhs.fha.nhinc.gateway.aggregator.model.AggMessageResult;
import gov.hhs.fha.nhinc.gateway.aggregator.model.AggTransaction;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author westbergl
 */
@Ignore // TODO: Move to integration test
public class AggMessageResultDaoTest
{

    public AggMessageResultDaoTest()
    {
    }

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

    /**
     * Test of save method, of class AggMessageResultDao.
     */
    @Test
    public void testSaveFindAndDelete()
    {
        System.out.println("testSaveFindByIdAndDelete");

        System.out.println("Saving transaction with one MessageResult...");
        
        // AggMessageResult must be associated with a Transaction... So
        // let's create a transaction first.
        //-------------------------------------------------------------
        SimpleDateFormat oFormat = new SimpleDateFormat("MM/dd/yyyy.HH:mm:ss");
        Date dtNow = new Date();

        AggTransaction oAggTransaction = new AggTransaction();
        oAggTransaction.setServiceType("docquery");
        oAggTransaction.setTransactionStartTime(dtNow);
        AggMessageResult oAggMessageResult = new AggMessageResult();
        oAggMessageResult.setMessageKey("TheKey1");
        oAggMessageResult.setMessageOutTime(oAggTransaction.getTransactionStartTime());
        oAggMessageResult.setResponseMessage("TheMessage1");
        oAggMessageResult.setResponseMessageType("AMessageType1");
        oAggMessageResult.setResponseReceivedTime(dtNow);
        oAggMessageResult.setAggTransaction(oAggTransaction);
        HashSet<AggMessageResult> hMsgResults = new HashSet<AggMessageResult>();
        hMsgResults.add(oAggMessageResult);
        oAggTransaction.setAggMessageResults(hMsgResults);

        AggTransactionDao oAggTransactionDao = new AggTransactionDao();
        oAggTransactionDao.save(oAggTransaction);
        assertNotNull("AggTransaction:", oAggTransaction);
        String sTransactionId = oAggTransaction.getTransactionId();
        assertNotNull("AggTransaction.getAggMessageResults:", oAggTransaction.getAggMessageResults());
        assertEquals("AggMessageResults.size", oAggTransaction.getAggMessageResults().size(), 1);
        AggMessageResult oMsgResult = oAggTransaction.getAggMessageResults().toArray(new AggMessageResult[0])[0];
        String saMessageId[] = new String[2];
        saMessageId[0] = oMsgResult.getMessageId();

        System.out.println("Done saving transaction with one MessageResult...");

        System.out.println("Saving a new MessageResult for the same transaction...");

        // Add a MessageResult to the transaction
        //---------------------------------------
        oAggMessageResult = new AggMessageResult();
        oAggMessageResult.setMessageKey("TheKey2");
        oAggMessageResult.setMessageOutTime(oAggTransaction.getTransactionStartTime());
        oAggMessageResult.setResponseMessage("TheMessage2");
        oAggMessageResult.setResponseMessageType("AMessageType2");
        oAggMessageResult.setResponseReceivedTime(dtNow);
        oAggMessageResult.setAggTransaction(oAggTransaction);
        
        AggMessageResultDao oAggMessageResultDao = new AggMessageResultDao();
        oAggMessageResultDao.save(oAggMessageResult);
        saMessageId[1] = oAggMessageResult.getMessageId();
        
        System.out.println("Done saving a new MessageResult for the same transaction...");
        
        System.out.println("Retrieving by message Id");
        
        oAggMessageResult = oAggMessageResultDao.findById(saMessageId[0]);
        assertNotNull("AggMessageResult:", oAggMessageResult);
        assertEquals("MessageId", oAggMessageResult.getMessageId(), saMessageId[0]);
        assertEquals("MessageKey", oAggMessageResult.getMessageKey(), "TheKey1");
        assertEquals("MessageOutTime", oFormat.format(oAggMessageResult.getMessageOutTime()), oFormat.format(dtNow));
        assertEquals("ResponseMessage", oAggMessageResult.getResponseMessage(), "TheMessage1");
        assertEquals("ResponseMessageType", oAggMessageResult.getResponseMessageType(), "AMessageType1");
        assertEquals("ResponseReceivedTime", oFormat.format(oAggMessageResult.getResponseReceivedTime()), oFormat.format(dtNow));
        
        System.out.println("Done retrieving by message Id");

        System.out.println("Updating AggMessageResult...");
        oAggMessageResult.setResponseMessage("TheMessage1Updated");
        oAggMessageResultDao.save(oAggMessageResult);
        
        System.out.println("Updating AggMessageResult...");
        
        System.out.println("Retrieving by message key");

        try
        {
            oAggMessageResult = oAggMessageResultDao.findByMessageKey(sTransactionId, "TheKey1");
        }
        catch (Exception e)
        {
            fail("oAggMessageResultDao.findByMessageKey Exception occurred. " + e.getMessage());
        }
        assertNotNull("AggMessageResult:", oAggMessageResult);
        assertEquals("MessageId", oAggMessageResult.getMessageId(), saMessageId[0]);
        assertEquals("MessageKey", oAggMessageResult.getMessageKey(), "TheKey1");
        assertEquals("MessageOutTime", oFormat.format(oAggMessageResult.getMessageOutTime()), oFormat.format(dtNow));
        assertEquals("ResponseMessage", oAggMessageResult.getResponseMessage(), "TheMessage1Updated");
        assertEquals("ResponseMessageType", oAggMessageResult.getResponseMessageType(), "AMessageType1");
        assertEquals("ResponseReceivedTime", oFormat.format(oAggMessageResult.getResponseReceivedTime()), oFormat.format(dtNow));
        
        System.out.println("Done retrieving by message key");
        
        System.out.println("Deleting records that we created...");
        oAggMessageResult = oAggMessageResultDao.findById(saMessageId[0]);
        oAggMessageResultDao.delete(oAggMessageResult);
        
        oAggTransaction = oAggTransactionDao.findById(sTransactionId);
        oAggTransactionDao.delete(oAggTransaction);
        
        System.out.println("Done deleting records that we created...");

        System.out.println("Done with testSaveFindByIdAndDelete");
    }

}