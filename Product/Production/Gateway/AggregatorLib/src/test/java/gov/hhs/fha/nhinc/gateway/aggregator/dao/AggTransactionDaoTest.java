package gov.hhs.fha.nhinc.gateway.aggregator.dao;

import gov.hhs.fha.nhinc.gateway.aggregator.model.AggMessageResult;
import gov.hhs.fha.nhinc.gateway.aggregator.model.AggTransaction;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
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
public class AggTransactionDaoTest
{
    public AggTransactionDaoTest()
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
     * Test of save method, of class AggTransactionDao.
     */
    @Test
    public void testSaveFindByIdAndDelete()
    {
        System.out.println("Testing save...");
        
        SimpleDateFormat oFormat = new SimpleDateFormat("MM/dd/yyyy.HH:mm:ss");
        Date oNow = new Date();
        
        AggTransaction oAggTransaction = new AggTransaction();
        oAggTransaction.setServiceType("docquery");
        oAggTransaction.setTransactionStartTime(oNow);
        AggMessageResult oAggMessageResults = new AggMessageResult();
        oAggMessageResults.setMessageKey("TheKey");
        oAggMessageResults.setMessageOutTime(oAggTransaction.getTransactionStartTime());
        oAggMessageResults.setResponseMessage("TheMessage");
        oAggMessageResults.setResponseMessageType("AMessageType");
        oAggMessageResults.setResponseReceivedTime(oNow);
        oAggMessageResults.setAggTransaction(oAggTransaction);
        HashSet<AggMessageResult> hMsgResults = new HashSet<AggMessageResult>();
        hMsgResults.add(oAggMessageResults);
        oAggTransaction.setAggMessageResults(hMsgResults);
        
        AggTransactionDao oAggTransactionDao = new AggTransactionDao();
        oAggTransactionDao.save(oAggTransaction);
        
        System.out.println("save completed...");
        
        System.out.println("Testing FindById...");
        String sTransactionId = oAggTransaction.getTransactionId();
        
        oAggTransaction = new AggTransaction();
        oAggTransaction = oAggTransactionDao.findById(sTransactionId);
        assertEquals("TransactionId", oAggTransaction.getTransactionId(), sTransactionId);
        assertEquals("ServiceKey", oAggTransaction.getServiceType(), "docquery");
        assertNotNull("AggMessageResults.", oAggTransaction.getAggMessageResults());
        assertEquals("AggMessageResults.size()", oAggTransaction.getAggMessageResults().size(), 1);
        AggMessageResult oMsgResults[] = oAggTransaction.getAggMessageResults().toArray(new AggMessageResult[0]);
        assertNotSame("MessageId", oMsgResults[0].getMessageId(), "");
        assertEquals("MessageKey", oMsgResults[0].getMessageKey(), "TheKey");
        assertEquals("MessageOutTime", oFormat.format(oMsgResults[0].getMessageOutTime()), oFormat.format(oNow));
        assertEquals("ResponseMessage", oMsgResults[0].getResponseMessage(), "TheMessage");
        assertEquals("ResponseMessageType", oMsgResults[0].getResponseMessageType(), "AMessageType");
        assertEquals("ResponseReceivedTime", oFormat.format(oMsgResults[0].getResponseReceivedTime()), oFormat.format(oNow));
        
        System.out.println("FindById completed...");

        System.out.println("Testing Delete...");
        
        oAggTransactionDao.delete(oAggTransaction);
        
        System.out.println("Delete completed...");
    }
    
    /**
     * Test of findOlderThan method, of class AggTransactionDao.
     */
    @Test
    public void testFindOlderThan() throws Exception
    {
        System.out.println("Starting testFindOlderThan method...");
        
        
        // Write out a couple of records so that we can retrieve based on time frame.
        //---------------------------------------------------------------------------
        String saTransId[] = {"", "", ""};
        SimpleDateFormat oFormat = new SimpleDateFormat("MM/dd/yyyy.HH:mm:ss");
        Date dtNow = new Date();
        
        Calendar oCal = Calendar.getInstance();
        oCal.add(Calendar.DAY_OF_MONTH, -1);
        Date dtYesterday = oCal.getTime();
        
        oCal.add(Calendar.DAY_OF_MONTH, -1);
        Date dtTwoDaysAgo = oCal.getTime();
        
        AggTransaction oAggTransaction = new AggTransaction();
        oAggTransaction.setServiceType("docquery1");
        oAggTransaction.setTransactionStartTime(dtNow);
        AggMessageResult oAggMessageResults = new AggMessageResult();
        oAggMessageResults.setMessageKey("TheKey1");
        oAggMessageResults.setMessageOutTime(oAggTransaction.getTransactionStartTime());
        oAggMessageResults.setResponseMessage("TheMessage1");
        oAggMessageResults.setResponseMessageType("AMessageType1");
        oAggMessageResults.setResponseReceivedTime(dtNow);
        oAggMessageResults.setAggTransaction(oAggTransaction);
        HashSet<AggMessageResult> hMsgResults = new HashSet<AggMessageResult>();
        hMsgResults.add(oAggMessageResults);
        oAggTransaction.setAggMessageResults(hMsgResults);
        AggTransactionDao oAggTransactionDao = new AggTransactionDao();
        oAggTransactionDao.save(oAggTransaction);
        saTransId[0] = oAggTransaction.getTransactionId();
        
        oAggTransaction = new AggTransaction();
        oAggTransaction.setServiceType("docquery2");
        oAggTransaction.setTransactionStartTime(dtYesterday);
        oAggMessageResults = new AggMessageResult();
        oAggMessageResults.setMessageKey("TheKey2");
        oAggMessageResults.setMessageOutTime(oAggTransaction.getTransactionStartTime());
        oAggMessageResults.setResponseMessage("TheMessage2");
        oAggMessageResults.setResponseMessageType("AMessageType2");
        oAggMessageResults.setResponseReceivedTime(dtYesterday);
        oAggMessageResults.setAggTransaction(oAggTransaction);
        hMsgResults = new HashSet<AggMessageResult>();
        hMsgResults.add(oAggMessageResults);
        oAggTransaction.setAggMessageResults(hMsgResults);
        oAggTransactionDao.save(oAggTransaction);
        saTransId[1] = oAggTransaction.getTransactionId();
        
        
        oAggTransaction = new AggTransaction();
        oAggTransaction.setServiceType("docquery3");
        oAggTransaction.setTransactionStartTime(dtTwoDaysAgo);
        oAggMessageResults = new AggMessageResult();
        oAggMessageResults.setMessageKey("TheKey3");
        oAggMessageResults.setMessageOutTime(oAggTransaction.getTransactionStartTime());
        oAggMessageResults.setResponseMessage("TheMessage3");
        oAggMessageResults.setResponseMessageType("AMessageType3");
        oAggMessageResults.setResponseReceivedTime(dtTwoDaysAgo);
        oAggMessageResults.setAggTransaction(oAggTransaction);
        hMsgResults = new HashSet<AggMessageResult>();
        hMsgResults.add(oAggMessageResults);
        oAggTransaction.setAggMessageResults(hMsgResults);
        oAggTransactionDao.save(oAggTransaction);
        saTransId[2] = oAggTransaction.getTransactionId();
        
        
        AggTransaction[] oaAggTransaction = oAggTransactionDao.findOlderThan(dtYesterday);
        assertNotNull("oaAggTransaction", oaAggTransaction);
        assertEquals("oaAggTransaction.length", oaAggTransaction.length, 2);
        boolean bFound[] = {false, false};
        for (AggTransaction oTrans : oaAggTransaction)
        {
            if (oTrans.getServiceType().equals("docquery2"))
            {
                bFound[0] = true;
                assertNotSame("TransactionId:", oTrans.getTransactionId(), "");
                assertEquals("TransactionStartTime", oFormat.format(oTrans.getTransactionStartTime()), 
                                                     oFormat.format(dtYesterday));
                assertNotNull("AggMessageResults", oTrans.getAggMessageResults());
                assertEquals("AggMessageResults.size", oTrans.getAggMessageResults().size(), 1);
                AggMessageResult oResults[] = oTrans.getAggMessageResults().toArray(new AggMessageResult[0]);
                assertNotSame("MessageId:", oResults[0].getMessageId(), "");
                assertEquals("MessageKey:", oResults[0].getMessageKey(), "TheKey2");
                assertEquals("MessageOutTime:", oFormat.format(oResults[0].getMessageOutTime()), 
                                                oFormat.format(dtYesterday));
                assertEquals("ResponseMessage:", oResults[0].getResponseMessage(), "TheMessage2");
                assertEquals("ResponseMessageType:", oResults[0].getResponseMessageType(), "AMessageType2");
                assertEquals("ResponseReceivedTime:", oFormat.format(oResults[0].getResponseReceivedTime()), 
                                                      oFormat.format(dtYesterday));
            }
            else if (oTrans.getServiceType().equals("docquery3"))
            {
                bFound[1] = true;
                assertNotSame("TransactionId:", oTrans.getTransactionId(), "");
                assertEquals("TransactionStartTime", oFormat.format(oTrans.getTransactionStartTime()), 
                                                     oFormat.format(dtTwoDaysAgo));
                assertNotNull("AggMessageResults", oTrans.getAggMessageResults());
                assertEquals("AggMessageResults.size", oTrans.getAggMessageResults().size(), 1);
                AggMessageResult oResults[] = oTrans.getAggMessageResults().toArray(new AggMessageResult[0]);
                assertNotSame("MessageId:", oResults[0].getMessageId(), "");
                assertEquals("MessageKey:", oResults[0].getMessageKey(), "TheKey3");
                assertEquals("MessageOutTime:", oFormat.format(oResults[0].getMessageOutTime()), 
                                                oFormat.format(dtTwoDaysAgo));
                assertEquals("ResponseMessage:", oResults[0].getResponseMessage(), "TheMessage3");
                assertEquals("ResponseMessageType:", oResults[0].getResponseMessageType(), "AMessageType3");
                assertEquals("ResponseReceivedTime:", oFormat.format(oResults[0].getResponseReceivedTime()), 
                                                      oFormat.format(dtTwoDaysAgo));
            }
            else
            {
                fail("Found an unexpected transaction in the query results.");
            }
        }
        assertTrue("Missing docquery2", bFound[0]);
        assertTrue("Missing docquery3", bFound[1]);
        
        // Now lets delete what we created...
        //------------------------------------
        oAggTransaction = oAggTransactionDao.findById(saTransId[0]);
        oAggTransactionDao.delete(oAggTransaction);
        
        oAggTransaction = oAggTransactionDao.findById(saTransId[1]);
        oAggTransactionDao.delete(oAggTransaction);
        
        oAggTransaction = oAggTransactionDao.findById(saTransId[2]);
        oAggTransactionDao.delete(oAggTransaction);
        
        System.out.println("Done running testFindOlderThan method...");
    }
}