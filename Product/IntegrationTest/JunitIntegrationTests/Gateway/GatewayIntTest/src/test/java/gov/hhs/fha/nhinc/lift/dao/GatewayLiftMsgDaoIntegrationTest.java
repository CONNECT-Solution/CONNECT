package gov.hhs.fha.nhinc.lift.dao;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.lift.model.GatewayLiftMsgRecord;
import gov.hhs.fha.nhinc.util.StringUtil;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.Hibernate;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

/**
 * This class is used to test the ability to read and write records to the
 * gateway_lift_message table.
 *
 * @author Les Westberg
 */
public class GatewayLiftMsgDaoIntegrationTest
{
    SimpleDateFormat m_oFormat = new SimpleDateFormat("MM/dd/yyyy.HH:mm:ss");

    /**
     * Default constructor.
     */
    public GatewayLiftMsgDaoIntegrationTest()
    {
    }

    /**
     * This method is called when this class is initialized.
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception
    {
        System.setProperty("nhinc.properties.dir", System.getenv("NHINC_PROPERTIES_DIR"));
    }

    /**
     * This method is called after all the tests are run in this class.
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    /**
     * This method is called before each test is run.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * This method is called after each test is run.
     */
    @After
    public void tearDown()
    {
    }

    /**
     * This method creates the specified record number and returns it.
     *
     * @param iRecNum The record number of the record to be created.
     * @return The record to be returned.
     */
    private GatewayLiftMsgRecord CreateRecord(int iRecNum)
    {
        GatewayLiftMsgRecord oRecord = null;
        Date dtRightNow = new Date();
        Calendar oCal = Calendar.getInstance();
        oCal.setTime(dtRightNow);
        oCal.add(Calendar.HOUR, -1);
        Date dtOneHourAgo = oCal.getTime();
        oCal.add(Calendar.HOUR, -1);
        Date dtTwoHourAgo = oCal.getTime();

        switch (iRecNum)
        {
            case 1:
            {
                oRecord = new GatewayLiftMsgRecord();
                oRecord.setInitialEntryTimestamp(dtRightNow);
                oRecord.setMessageState("ENTERED");
                oRecord.setProcessingStartTimestamp(null);
                oRecord.setProducerProxyAddress("http://www.google1.com");
                oRecord.setProducerProxyPort(1111L);
                oRecord.setFileNameToRetrieve("test1.xml");
                oRecord.setRequestKeyGuid("11-11-11");
                oRecord.setMessageType("DEFERRED_DOCUMENT_SUBMISSION");

                String sMessage = "<message>message 1</message>";
                Blob oMessageBlob = Hibernate.createBlob(sMessage.getBytes());
                oRecord.setMessage(oMessageBlob);

                String sAssertion = "<assertion>assertion 1</assertion>";
                Blob oAssertionBlob = Hibernate.createBlob(sAssertion.getBytes());
                oRecord.setAssertion(oAssertionBlob);
                break;
            }
            case 2:
            {
                oRecord = new GatewayLiftMsgRecord();
                oRecord.setInitialEntryTimestamp(dtOneHourAgo);
                oRecord.setMessageState("ENTERED");
                oRecord.setProcessingStartTimestamp(null);
                oRecord.setProducerProxyAddress("http://www.google2.com");
                oRecord.setProducerProxyPort(2222L);
                oRecord.setFileNameToRetrieve("test2.xml");
                oRecord.setRequestKeyGuid("22-22-22");
                oRecord.setMessageType("DEFERRED_DOCUMENT_SUBMISSION");

                String sMessage = "<message>message 2</message>";
                Blob oMessageBlob = Hibernate.createBlob(sMessage.getBytes());
                oRecord.setMessage(oMessageBlob);

                String sAssertion = "<assertion>assertion 2</assertion>";
                Blob oAssertionBlob = Hibernate.createBlob(sAssertion.getBytes());
                oRecord.setAssertion(oAssertionBlob);
                break;
            }
            case 3:
            {
                oRecord = new GatewayLiftMsgRecord();
                oRecord.setInitialEntryTimestamp(dtTwoHourAgo);
                oRecord.setMessageState("ENTERED");
                oRecord.setProcessingStartTimestamp(null);
                oRecord.setProducerProxyAddress("http://www.google3.com");
                oRecord.setProducerProxyPort(2222L);
                oRecord.setFileNameToRetrieve("test3.xml");
                oRecord.setRequestKeyGuid("33-33-33");
                oRecord.setMessageType("DEFERRED_DOCUMENT_SUBMISSION");

                String sMessage = "<message>message 3</message>";
                Blob oMessageBlob = Hibernate.createBlob(sMessage.getBytes());
                oRecord.setMessage(oMessageBlob);

                String sAssertion = "<assertion>assertion 3</assertion>";
                Blob oAssertionBlob = Hibernate.createBlob(sAssertion.getBytes());
                oRecord.setAssertion(oAssertionBlob);
                break;
            }
            default:
            {
                oRecord = null;
                break;
            }
        }

        return oRecord;

    }

    /**
     * This method extracts the string data from the blob and returns it in the
     * form of a string.
     *
     * @param oBlob The blob containing the data.
     * @return The blob data to be returned in the form of a string.
     */
    private String extractStringFromBlob(Blob oBlob)
        throws java.sql.SQLException, java.io.IOException
    {
        int iLen = (int) oBlob.length();
        byte[] byteResult = new byte[iLen];
        InputStream oInputStream = oBlob.getBinaryStream();
        oInputStream.read(byteResult);
        String sResult = new String(byteResult);

        return sResult;
    }

    /**
     * This method compares the original record with the new one to make sure that
     * they are equal.
     *
     * @param oOriginal The original record.
     * @param oNew The new one that is being compared.
     */
    private void assertRecordsEqual(GatewayLiftMsgRecord oOriginalRecord, GatewayLiftMsgRecord oNewRecord)
        throws java.sql.SQLException, java.io.IOException
    {
        assertEquals("InitialEntryTimestamp was not the same: ", m_oFormat.format(oOriginalRecord.getInitialEntryTimestamp()), m_oFormat.format(oNewRecord.getInitialEntryTimestamp()));
        assertEquals("MessageState was not the same: ", oOriginalRecord.getMessageState(), oNewRecord.getMessageState());

        if ((oOriginalRecord.getProcessingStartTimestamp() != null) && (oNewRecord.getProcessingStartTimestamp() != null))
        {
            assertEquals("ProcessingStartTimestamp was not the same: ", m_oFormat.format(oOriginalRecord.getProcessingStartTimestamp()), m_oFormat.format(oNewRecord.getProcessingStartTimestamp()));
        }
        else if ((oOriginalRecord.getProcessingStartTimestamp() == null) && (oNewRecord.getProcessingStartTimestamp() == null))
        {
            // this is OK too.  They are both null
        }
        else
        {
            // This means one is null and one is not.
            fail("ProcessingStartTimestamp was not the same: ");
        }

        assertEquals("ProducerProxyAddress was not the same: ", oOriginalRecord.getProducerProxyAddress(), oNewRecord.getProducerProxyAddress());

        if ((oOriginalRecord.getProducerProxyPort() != null) && (oNewRecord.getProducerProxyPort() != null))
        {
            assertEquals("ProducerProxyPort was not the same: ", oOriginalRecord.getProducerProxyPort().longValue(), oNewRecord.getProducerProxyPort().longValue());
        }
        else if ((oOriginalRecord.getProducerProxyPort() == null) && (oNewRecord.getProducerProxyPort() == null))
        {
            // this is OK too.  They are both null
        }
        else
        {
            // This means one is null and one is not.
            fail("ProducerProxyPort was not the same: ");
        }

        assertEquals("FileNameToRetrieve was not the same: ", oOriginalRecord.getFileNameToRetrieve(), oNewRecord.getFileNameToRetrieve());

        assertEquals("RequestKeyGuid was not the same: ", oOriginalRecord.getRequestKeyGuid(), oNewRecord.getRequestKeyGuid());

        assertEquals("MessageType was not the same: ", oOriginalRecord.getMessageType(), oNewRecord.getMessageType());

        if ((oOriginalRecord.getMessage() != null) && (oNewRecord.getMessage() != null))
        {
            String sOriginalMessage = extractStringFromBlob(oOriginalRecord.getMessage());
            String sNewMessage = extractStringFromBlob(oNewRecord.getMessage());
            assertEquals("Message was not the same: ", sOriginalMessage, sNewMessage);
        }
        else if ((oOriginalRecord.getProducerProxyPort() == null) && (oNewRecord.getProducerProxyPort() == null))
        {
            // this is OK too.  They are both null
        }
        else
        {
            // This means one is null and one is not.
            fail("Message was not the same: ");
        }

        if ((oOriginalRecord.getAssertion() != null) && (oNewRecord.getAssertion() != null))
        {
            String sOriginalAssertion = extractStringFromBlob(oOriginalRecord.getAssertion());
            String sNewAssertion = extractStringFromBlob(oNewRecord.getAssertion());
            assertEquals("Assertion was not the same: ", sOriginalAssertion, sNewAssertion);
        }
        else if ((oOriginalRecord.getProducerProxyPort() == null) && (oNewRecord.getProducerProxyPort() == null))
        {
            // this is OK too.  They are both null
        }
        else
        {
            // This means one is null and one is not.
            fail("Assertion was not the same: ");
        }

    }

    /**
     * Delete the specified record from the database.
     *
     * @param oDao The dao object to use.
     * @param oRecord The record to be deleted.
     */
    private void deleteOldRecord(GatewayLiftMessageDao oDao, GatewayLiftMsgRecord oRecord)
    {
        if (oRecord != null)
        {
            String sRequestKeyGuid = oRecord.getRequestKeyGuid();
            GatewayLiftMsgRecord oRecToDelete = oDao.queryByRequestKeyGuid(sRequestKeyGuid);
            if (oRecToDelete != null)
            {
                oDao.deleteRecord(oRecToDelete);
            }
        }
    }

    /**
     * Delete all records in the list from the database.
     *
     * @param oDao The dao object to use.
     * @param olLiftRecs The record to be deleted.
     */
    private void deleteOldRecords(GatewayLiftMessageDao oDao, List<GatewayLiftMsgRecord> olLiftRecs)
    {
        for (GatewayLiftMsgRecord oRecord : olLiftRecs)
        {
            deleteOldRecord(oDao, oRecord);
        }
    }

    /**
     * This method tests the ability to insert, read, update, and delete a record
     * in the gateway_lift_message table.
     *
     */
    @Test
    public void testSingleInsertReadUpdateAndDeleteRecords()
    {

        try
        {
            ArrayList<GatewayLiftMsgRecord> olRecords = new ArrayList<GatewayLiftMsgRecord>();
            GatewayLiftMsgRecord oOriginalRecord = CreateRecord(1);
            olRecords.add(oOriginalRecord);
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao();

            // Delete any old records - before starting our test.
            //---------------------------------------------------
            deleteOldRecords(oDao, olRecords);

            // Insert a record
            //-----------------
            boolean bSuccess = oDao.insertRecords(olRecords);
            assertTrue("Failed to insert record.", bSuccess);

            // Read the record by GUID
            //------------------------
            String sRequestKeyGuid = oOriginalRecord.getRequestKeyGuid();
            GatewayLiftMsgRecord oRecordRead = oDao.queryByRequestKeyGuid(sRequestKeyGuid);
            assertNotNull("oRecordRead was null and should not have been.", oRecordRead);
            assertRecordsEqual(oOriginalRecord, oRecordRead);

            // Update the record.
            //--------------------
            Date dtProcessingTime = new Date();
            oRecordRead.setProcessingStartTimestamp(dtProcessingTime);
            oRecordRead.setMessageState("PROCESSING");
            oDao.updateRecord(oRecordRead);

            // Read the record again - this time by ID
            //------------------------------------------
            long lId = oRecordRead.getId();
            GatewayLiftMsgRecord oRecordRead2 = oDao.queryById(lId);
            assertNotNull("oRecordRead2 was null and should not have been.", oRecordRead2);
            assertRecordsEqual(oRecordRead, oRecordRead2);

            // Delete the record
            //---------------------
            oDao.deleteRecord(oRecordRead2);

            // Try reading it to see that it is really gone.
            //-----------------------------------------------
            GatewayLiftMsgRecord oRecordRead3 = oDao.queryById(lId);
            assertNull("oRecordRead3 should have been null and was not.", oRecordRead3);

        }
        catch (Exception e)
        {
            fail("Failed to successfully run testSingleInsertReadUpdateAndDeleteRecords.  Error: " + e.getMessage());
        }
    }

    /**
     * This method tests the ability to insert, read, update, and delete a record
     * in the gateway_lift_message table.  But it uses the specific method of
     * queryByMessageTypeOrderByProcessingTime.
     *
     */
    @Test
    public void testQueryByMessageType()
    {

        try
        {
            ArrayList<GatewayLiftMsgRecord> olRecords = new ArrayList<GatewayLiftMsgRecord>();
            GatewayLiftMsgRecord oOriginalRecord1 = CreateRecord(1);
            olRecords.add(oOriginalRecord1);
            GatewayLiftMsgRecord oOriginalRecord2 = CreateRecord(2);
            olRecords.add(oOriginalRecord2);
            GatewayLiftMsgRecord oOriginalRecord3 = CreateRecord(3);
            olRecords.add(oOriginalRecord3);

            Date dtRightNow = new Date();
            Calendar oCal = Calendar.getInstance();
            oCal.setTime(dtRightNow);
            oCal.add(Calendar.MINUTE, -30);
            Date dtThirtyMinutesAgo = oCal.getTime();
            oCal.add(Calendar.MINUTE, -30);
            Date dtOneHourAgo = oCal.getTime();
            oCal.add(Calendar.HOUR, -1);
            Date dtTwoHourAgo = oCal.getTime();

            oOriginalRecord1.setMessageState("PROCESSING");
            oOriginalRecord1.setProcessingStartTimestamp(dtRightNow);

            oOriginalRecord2.setMessageState("PROCESSING");
            oOriginalRecord2.setProcessingStartTimestamp(dtOneHourAgo);

            oOriginalRecord3.setMessageState("PROCESSING");
            oOriginalRecord3.setProcessingStartTimestamp(dtTwoHourAgo);

            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao();

            // Delete any old records - before starting our test.
            //---------------------------------------------------
            deleteOldRecords(oDao, olRecords);

            // Insert records
            //-----------------
            boolean bSuccess = oDao.insertRecords(olRecords);
            assertTrue("Failed to insert record.", bSuccess);

            // Read the two oldest records.
            //-----------------------------
            List<GatewayLiftMsgRecord> olRecordRead = oDao.queryByMessageTypeOrderByProcessingTime("PROCESSING", dtThirtyMinutesAgo);
            assertNotNull("olRecordRead was null and should not have been. ", olRecordRead);
            assertEquals("There should have been exactly two records read. ", 2, olRecordRead.size());

            // The records should be oldest first.
            //-------------------------------------
            assertRecordsEqual(oOriginalRecord3, olRecordRead.get(0));
            assertRecordsEqual(oOriginalRecord2, olRecordRead.get(1));

            // Delete those records...
            //-------------------------
            oDao.deleteRecord(olRecordRead.get(0));
            oDao.deleteRecord(olRecordRead.get(1));

            // Now lets get the last record.
            //--------------------------------
            olRecordRead = oDao.queryByMessageTypeOrderByProcessingTime("PROCESSING", dtRightNow);
            assertNotNull("olRecordRead was null and should not have been. ", olRecordRead);
            assertEquals("There should have been exactly 1 record read. ", 1, olRecordRead.size());

            // Verify that the correct record was read
            //-----------------------------------------
            assertRecordsEqual(oOriginalRecord1, olRecordRead.get(0));


            // Delete the last record
            //-----------------------
            oDao.deleteRecord(olRecordRead.get(0));

        }
        catch (Exception e)
        {
            fail("Failed to successfully run testSingleInsertReadUpdateAndDeleteRecords.  Error: " + e.getMessage());
        }

    }
}
