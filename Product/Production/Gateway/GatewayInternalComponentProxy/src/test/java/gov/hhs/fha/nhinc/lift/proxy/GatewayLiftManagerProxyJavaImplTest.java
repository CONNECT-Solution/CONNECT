/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.lift.proxy;

import gov.hhs.fha.nhinc.gateway.lift.CompleteLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.CompleteLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.FailedLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.FailedLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.StartLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.StartLiftTransactionResponseType;
import gov.hhs.fha.nhinc.lift.dao.GatewayLiftMessageDao;
import gov.hhs.fha.nhinc.lift.model.GatewayLiftMsgRecord;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;

/**
 *
 * @author westberg
 */
@RunWith(JMock.class)
public class GatewayLiftManagerProxyJavaImplTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final Log mockLog = context.mock(Log.class);
    final GatewayLiftMessageDao mockDAO = context.mock(GatewayLiftMessageDao.class);
    final Socket mockSocket = context.mock(Socket.class);

    public GatewayLiftManagerProxyJavaImplTest()
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
     * Test of createLogger() method, of class GatewayLiftManagerProxyJavaImpl.
     */
    @Test
    public void testCreateLogger()
    {
        try
        {
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            Log oLog = oImpl.createLogger();
            assertNotNull("Log was null", oLog);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCreateLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger test: " + t.getMessage());
        }
    }

    /**
     * Test of getGatewayLiftMessageDao method, of class GatewayLiftManagerProxyJavaImpl.
     */
    @Test
    public void testGetGatewayLiftMessageDao()
    {
        try
        {
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDAO;
                }

            };
            GatewayLiftMessageDao oDao = oImpl.getGatewayLiftMessageDao();
            assertNotNull("GatewayLiftMessageDao was null", oDao);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetGatewayLiftMessageDao test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetGatewayLiftMessageDao test: " + t.getMessage());
        }
    }


    /**
     * Test of readRecord method with happy path, of class GatewayLiftManagerProxyJavaImpl.
     */
    @Test
    public void testReadRecordHappyPath()
    {
        System.out.println("testReadRecordHappyPath");

        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockDAO).queryByRequestKeyGuid(with(any(String.class)));
                }
            });
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDAO;
                }
            };

            String sRequestKeyGuid = "111-111";
            GatewayLiftMsgRecord oResult = oImpl.readRecord(sRequestKeyGuid);
            assertNotNull("Record should not be null.", oResult);

        }
        catch(Throwable t)
        {
            System.out.println("Error running testReadRecordHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testReadRecordHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test of readRecord method with null, of class GatewayLiftManagerProxyJavaImpl.
     */
    @Test
    public void testReadRecordNull()
    {
        System.out.println("testReadRecordNull");

        try
        {
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDAO;
                }
            };

            GatewayLiftMsgRecord oResult = oImpl.readRecord(null);
            assertNull("Record should be null.", oResult);

        }
        catch(Throwable t)
        {
            System.out.println("Error running testReadRecordNull test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testReadRecordNull test: " + t.getMessage());
        }
    }

    /**
     * Test of readRecord method with empty string, of class GatewayLiftManagerProxyJavaImpl.
     */
    @Test
    public void testReadRecordEmptyString()
    {
        System.out.println("testReadRecordEmptyString");

        try
        {
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDAO;
                }
            };

            GatewayLiftMsgRecord oResult = oImpl.readRecord("");
            assertNull("Record should be null.", oResult);

        }
        catch(Throwable t)
        {
            System.out.println("Error running testReadRecordEmptyString test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testReadRecordEmptyString test: " + t.getMessage());
        }
    }

    /**
     * Test of setRecordToProcessing method with happy path, of class GatewayLiftManagerProxyJavaImpl.
     */
    @Test
    public void testSetRecordToProcessingHappyPath()
    {
        System.out.println("testSetRecordToProcessingHappyPath");

        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockDAO).updateRecord(with(any(GatewayLiftMsgRecord.class)));
                }
            });
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDAO;
                }
            };

            GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
            oRecord.setRequestKeyGuid("111-111");
            oImpl.setRecordToProcessing(oRecord);
            assertEquals("Record state should have been set to PROCESSING and it was not. ", "PROCESSING", oRecord.getMessageState());
            assertNotNull("The processingStartTimestamp should have been set.", oRecord.getProcessingStartTimestamp());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testSetRecordToProcessingHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testSetRecordToProcessingHappyPath test: " + t.getMessage());
        }
    }


    /**
     * Test of startLiftTransactionHappyPath method, of class GatewayLiftManagerProxyJavaImpl.
     */
    @Test
    public void testStartLiftTransactionHappyPath()
    {
        System.out.println("testStartLiftTransactionHappyPath");

        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockSocket).close();
                    exactly(1).of(mockDAO).updateRecord(with(any(GatewayLiftMsgRecord.class)));
                }
            });
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDAO;
                }

                @Override
                protected GatewayLiftMsgRecord readRecord(String sRequestKeyGuid)
                {
                    GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
                    oRecord.setRequestKeyGuid(sRequestKeyGuid);
                    oRecord.setMessageState("ENTERED");
                    oRecord.setFileNameToRetrieve("/temp/temp.pdf");
                    oRecord.setProducerProxyAddress("localhost");
                    oRecord.setProducerProxyPort(4444L);
                    return oRecord;
                }

                @Override
                protected Socket getSocket(InetAddress oSocketAddr, int iPort)
                {
                    return mockSocket;
                }

                @Override
                protected void sendMessageToSocket(String sMessage, Socket oOutputStream)
                {
                }

                @Override
                protected String getClientIP()
                {
                    return "localhost";
                }

                @Override
                protected String getClientPort()
                {
                    return "4444";
                }

            };

            StartLiftTransactionRequestType oRequest = new StartLiftTransactionRequestType();
            oRequest.setRequestKeyGuid("111-111");

            StartLiftTransactionResponseType oResponse = oImpl.startLiftTransaction(oRequest);
            assertNotNull("StartLiftTransactionResponse should not have been null. ", oResponse);
            assertEquals("Status was incorrect: ", "SUCCESS", oResponse.getStatus());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testStartLiftTransactionHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStartLiftTransactionHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test of startLiftTransaction method with Http Exception.
     */
    @Test
    public void testStartLiftTransactionWithHttpException()
    {
        System.out.println("testStartLiftTransactionWithHttpException");

        try
        {
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).error(with(any(String.class)));
                    oneOf(mockLog).error(with(any(String.class)), with(any(Exception.class)));
                }
            });
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDAO;
                }

                @Override
                protected GatewayLiftMsgRecord readRecord(String sRequestKeyGuid)
                {
                    GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
                    oRecord.setRequestKeyGuid(sRequestKeyGuid);
                    oRecord.setMessageState("ENTERED");
                    oRecord.setFileNameToRetrieve("/temp/temp.pdf");
                    oRecord.setProducerProxyAddress("localhost");
                    oRecord.setProducerProxyPort(4444L);
                    return oRecord;
                }

                @Override
                protected Socket getSocket(InetAddress oSocketAddr, int iPort)
                {
                    return mockSocket;
                }

                @Override
                protected void sendMessageToSocket(String sMessage, Socket oOutputStream)
                {
                }

                @Override
                protected String getClientIP()
                {
                    return "localhost";
                }

                @Override
                protected String getClientPort()
                {
                    return "4444";
                }

                @Override
                protected void startHttpTransaction(GatewayLiftMsgRecord oRecord)
                    throws RuntimeException
                {
                    throw new RuntimeException("This is an exception.");
                }


            };

            StartLiftTransactionRequestType oRequest = new StartLiftTransactionRequestType();
            oRequest.setRequestKeyGuid("111-111");

            StartLiftTransactionResponseType oResponse = oImpl.startLiftTransaction(oRequest);
            assertNotNull("StartLiftTransactionResponse should not have been null. ", oResponse);
            assertTrue("Status message was incorrect. ", oResponse.getStatus().startsWith("FAILED: Failed to send message for RequestKeyGuid:"));
        }
        catch(Throwable t)
        {
            System.out.println("Error running testStartLiftTransactionWithHttpException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStartLiftTransactionWithHttpException test: " + t.getMessage());
        }
    }



    /**
     * Test of startLiftTransactionNull method, of class GatewayLiftManagerProxyJavaImpl.
     */
    @Test
    public void testStartLiftTransactionNull()
    {
        System.out.println("testStartLiftTransactionNull");

        try
        {
            context.checking(new Expectations()
            {
                {
                    oneOf(mockLog).error(with(any(String.class)));
                }
            });
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDAO;
                }
            };

            StartLiftTransactionResponseType oResponse = oImpl.startLiftTransaction(null);
            assertNotNull("StartLiftTransactionResponse should not have been null. ", oResponse);
            assertEquals("Status was incorrect: ", "FAILED: Tried to retrieve RequestKeyGuid: null.  It did not exist in the database.", oResponse.getStatus());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testStartLiftTransactionNull test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStartLiftTransactionNull test: " + t.getMessage());
        }
    }

    /**
     * Test of startLiftTransactionNullRequest method, of class GatewayLiftManagerProxyJavaImpl.
     */
    @Test
    public void testStartLiftTransactionNullRequest()
    {
        System.out.println("testStartLiftTransactionNullRequest");

        try
        {
            context.checking(new Expectations()
            {
                {
                    oneOf(mockLog).error(with(any(String.class)));
                }
            });
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDAO;
                }
            };

            StartLiftTransactionRequestType oRequest = new StartLiftTransactionRequestType();
            oRequest.setRequestKeyGuid(null);
            StartLiftTransactionResponseType oResponse = oImpl.startLiftTransaction(oRequest);
            assertNotNull("StartLiftTransactionResponse should not have been null. ", oResponse);
            assertEquals("Status was incorrect: ", "FAILED: Tried to retrieve RequestKeyGuid: null.  It did not exist in the database.", oResponse.getStatus());

        }
        catch(Throwable t)
        {
            System.out.println("Error running testStartLiftTransactionNullRequest test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStartLiftTransactionNullRequest test: " + t.getMessage());
        }
    }

    /**
     * Test of startLiftTransactionEmptyStringRequest method, of class GatewayLiftManagerProxyJavaImpl.
     */
    @Test
    public void testStartLiftTransactionEmptyStringRequest()
    {
        System.out.println("testStartLiftTransactionEmptyStringRequest");

        try
        {
            context.checking(new Expectations()
            {
                {
                    oneOf(mockLog).error(with(any(String.class)));
                }
            });
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDAO;
                }
            };

            StartLiftTransactionRequestType oRequest = new StartLiftTransactionRequestType();
            oRequest.setRequestKeyGuid("");
            StartLiftTransactionResponseType oResponse = oImpl.startLiftTransaction(oRequest);
            assertNotNull("StartLiftTransactionResponse should not have been null. ", oResponse);
            assertEquals("Status was incorrect: ", "FAILED: Tried to retrieve RequestKeyGuid: null.  It did not exist in the database.", oResponse.getStatus());

        }
        catch(Throwable t)
        {
            System.out.println("Error running testStartLiftTransactionEmptyStringRequest test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStartLiftTransactionEmptyStringRequest test: " + t.getMessage());
        }
    }

    /**
     * Test of testStartLiftTransaction method where no record is returned, of class GatewayLiftManagerProxyJavaImpl.
     */
    @Test
    public void testStartLiftTransactionNoRecord()
    {
        System.out.println("testStartLiftTransactionNoRecord");

        try
        {
            context.checking(new Expectations()
            {
                {
                    oneOf(mockLog).error(with(any(String.class)));
                }
            });
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDAO;
                }

                @Override
                protected GatewayLiftMsgRecord readRecord(String sRequestKeyGuid)
                {
                    return null;
                }
            };

            StartLiftTransactionRequestType oRequest = new StartLiftTransactionRequestType();
            oRequest.setRequestKeyGuid("111-111");
            StartLiftTransactionResponseType oResponse = oImpl.startLiftTransaction(oRequest);
            assertNotNull("StartLiftTransactionResponse should not have been null. ", oResponse);
            assertTrue("Response message should have started with 'FAILED:'. ", oResponse.getStatus().startsWith("FAILED:"));
        }
        catch(Throwable t)
        {
            System.out.println("Error running testStartLiftTransactionNoRecord test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStartLiftTransactionNoRecord test: " + t.getMessage());
        }
    }

    /**
     * Test the startLiftTransaction where the record is in an incorrect state.
     */
    @Test
    public void testStartLiftTransactionRecordIncorrectState()
    {
        System.out.println("testStartLiftTransactionRecordIncorrectState");

        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockDAO).queryByRequestKeyGuid(with(any(String.class)));
                    oneOf(mockLog).error(with(any(String.class)));
                }
            });
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDAO;
                }

                @Override
                protected boolean recordInCorrectState(GatewayLiftMsgRecord oRecord)
                {
                    return false;
                }

            };

            StartLiftTransactionRequestType oRequest = new StartLiftTransactionRequestType();
            oRequest.setRequestKeyGuid("111-111");

            StartLiftTransactionResponseType oResponse = oImpl.startLiftTransaction(oRequest);
            assertNotNull("StartLiftTransactionResponse should not have been null. ", oResponse);
            assertTrue("Status was incorrect: ", oResponse.getStatus().startsWith("FAILED: Incorrect message state for GATEWAY_LIFT_MESSAGE Record with RequestKeyGuid:"));
        }
        catch(Throwable t)
        {
            System.out.println("Error running testStartLiftTransactionHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStartLiftTransactionHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test recordInCorrectState method with happy path.
     */
    @Test
    public void testRecordInCorrectStateHappyPath()
    {
        System.out.println("testRecordInCorrectStateHappyPath");
        GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl();
        GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
        oRecord.setMessageState("ENTERED");
        oRecord.setRequestKeyGuid("111-111");
        oRecord.setFileNameToRetrieve("/test/temp.pdf");
        oRecord.setProducerProxyAddress("localhost");
        oRecord.setProducerProxyPort(4444L);
        boolean bResult = oImpl.recordInCorrectState(oRecord);
        assertEquals("recordInCorrectState should have returned true.  ", true, bResult);
    }

    /**
     * Test recordInCorrectState method with null record.
     */
    @Test
    public void testRecordInCorrectStateNull()
    {
        System.out.println("testRecordInCorrectStateNull");
        GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl();
        boolean bResult = oImpl.recordInCorrectState(null);
        assertEquals("recordInCorrectState should have returned false.  ", false, bResult);
    }

    /**
     * Test recordInCorrectState method with null Message state.
     */
    @Test
    public void testRecordInCorrectStateNullMessageState()
    {
        System.out.println("testRecordInCorrectStateNullMessageState");
        GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl();
        GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
        oRecord.setMessageState(null);
        oRecord.setRequestKeyGuid("111-111");
        oRecord.setFileNameToRetrieve("/test/temp.pdf");
        oRecord.setProducerProxyAddress("localhost");
        oRecord.setProducerProxyPort(4444L);
        boolean bResult = oImpl.recordInCorrectState(oRecord);
        assertEquals("recordInCorrectState should have returned false.  ", false, bResult);
    }

    /**
     * Test recordInCorrectState method with incorrect Message state.
     */
    @Test
    public void testRecordInCorrectStateIncorrectMessageState()
    {
        System.out.println("testRecordInCorrectStateIncorrectMessageState");
        GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl();
        GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
        oRecord.setMessageState("PROCESSED");
        oRecord.setRequestKeyGuid("111-111");
        oRecord.setFileNameToRetrieve("/test/temp.pdf");
        oRecord.setProducerProxyAddress("localhost");
        oRecord.setProducerProxyPort(4444L);
        boolean bResult = oImpl.recordInCorrectState(oRecord);
        assertEquals("recordInCorrectState should have returned false.  ", false, bResult);
    }

    /**
     * Test recordInCorrectState method with null request GUID.
     */
    @Test
    public void testRecordInCorrectStateNullRequestGuid()
    {
        System.out.println("testRecordInCorrectStateNullRequestGuid");
        GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl();
        GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
        oRecord.setMessageState("ENTERED");
        oRecord.setRequestKeyGuid(null);
        oRecord.setFileNameToRetrieve("/test/temp.pdf");
        oRecord.setProducerProxyAddress("localhost");
        oRecord.setProducerProxyPort(4444L);
        boolean bResult = oImpl.recordInCorrectState(oRecord);
        assertEquals("recordInCorrectState should have returned false.  ", false, bResult);
    }

    /**
     * Test recordInCorrectState method with empty request key guid.
     */
    @Test
    public void testRecordInCorrectStateEmptyRequestGuid()
    {
        System.out.println("testRecordInCorrectStateEmptyRequestGuid");
        GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl();
        GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
        oRecord.setMessageState("ENTERED");
        oRecord.setRequestKeyGuid("");
        oRecord.setFileNameToRetrieve("/test/temp.pdf");
        oRecord.setProducerProxyAddress("localhost");
        oRecord.setProducerProxyPort(4444L);
        boolean bResult = oImpl.recordInCorrectState(oRecord);
        assertEquals("recordInCorrectState should have returned false.  ", false, bResult);
    }

    /**
     * Test recordInCorrectState method with null file name.
     */
    @Test
    public void testRecordInCorrectStateNullFileName()
    {
        System.out.println("testRecordInCorrectStateNullFileName");
        GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl();
        GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
        oRecord.setMessageState("ENTERED");
        oRecord.setRequestKeyGuid("111-111");
        oRecord.setFileNameToRetrieve(null);
        oRecord.setProducerProxyAddress("localhost");
        oRecord.setProducerProxyPort(4444L);
        boolean bResult = oImpl.recordInCorrectState(oRecord);
        assertEquals("recordInCorrectState should have returned false.  ", false, bResult);
    }

    /**
     * Test recordInCorrectState method with empty file name.
     */
    @Test
    public void testRecordInCorrectStateEmptyFileName()
    {
        System.out.println("testRecordInCorrectStateEmptyFileName");
        GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl();
        GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
        oRecord.setMessageState("ENTERED");
        oRecord.setRequestKeyGuid("111-111");
        oRecord.setFileNameToRetrieve("");
        oRecord.setProducerProxyAddress("localhost");
        oRecord.setProducerProxyPort(4444L);
        boolean bResult = oImpl.recordInCorrectState(oRecord);
        assertEquals("recordInCorrectState should have returned false.  ", false, bResult);
    }

    /**
     * Test recordInCorrectState method with null proxy address.
     */
    @Test
    public void testRecordInCorrectStateNullProxyAddress()
    {
        System.out.println("testRecordInCorrectStateNullProxyAddress");
        GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl();
        GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
        oRecord.setMessageState("ENTERED");
        oRecord.setRequestKeyGuid("111-111");
        oRecord.setFileNameToRetrieve("/temp/temp.pdf");
        oRecord.setProducerProxyAddress(null);
        oRecord.setProducerProxyPort(4444L);
        boolean bResult = oImpl.recordInCorrectState(oRecord);
        assertEquals("recordInCorrectState should have returned false.  ", false, bResult);
    }

    /**
     * Test recordInCorrectState method with empty proxy address.
     */
    @Test
    public void testRecordInCorrectStateEmptyProxyAddress()
    {
        System.out.println("testRecordInCorrectStateEmptyProxyAddress");
        GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl();
        GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
        oRecord.setMessageState("ENTERED");
        oRecord.setRequestKeyGuid("111-111");
        oRecord.setFileNameToRetrieve("/temp/temp.pdf");
        oRecord.setProducerProxyAddress("");
        oRecord.setProducerProxyPort(4444L);
        boolean bResult = oImpl.recordInCorrectState(oRecord);
        assertEquals("recordInCorrectState should have returned false.  ", false, bResult);
    }

    /**
     * Test recordInCorrectState method with null port.
     */
    @Test
    public void testRecordInCorrectStateNullPort()
    {
        System.out.println("testRecordInCorrectStateNullPort");
        GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl();
        GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
        oRecord.setMessageState("ENTERED");
        oRecord.setRequestKeyGuid("111-111");
        oRecord.setFileNameToRetrieve("/temp/temp.pdf");
        oRecord.setProducerProxyAddress("localhost");
        oRecord.setProducerProxyPort(null);
        boolean bResult = oImpl.recordInCorrectState(oRecord);
        assertEquals("recordInCorrectState should have returned false.  ", false, bResult);
    }

    /**
     * Test recordInCorrectState method with 0 proxy address.
     */
    @Test
    public void testRecordInCorrectStateZeroProxyAddress()
    {
        System.out.println("testRecordInCorrectStateZeroProxyAddress");
        GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl();
        GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
        oRecord.setMessageState("ENTERED");
        oRecord.setRequestKeyGuid("111-111");
        oRecord.setFileNameToRetrieve("/temp/temp.pdf");
        oRecord.setProducerProxyAddress("localhost");
        oRecord.setProducerProxyPort(0L);
        boolean bResult = oImpl.recordInCorrectState(oRecord);
        assertEquals("recordInCorrectState should have returned false.  ", false, bResult);
    }

    /**
     * This tests the startHttpTransaction method with happy path.
     */
    @Test
    public void testStartHttpTransactionHappyPath()
    {
        System.out.println("testStartHttpTransactionHappyPath");

        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockSocket).close();
                }
            });
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected void sendMessageToSocket(String sMessage, Socket oOutputStream)
                {
                }

                @Override
                protected String getClientIP()
                {
                    return "localhost";
                }

                @Override
                protected String getClientPort()
                {
                    return "4444";
                }

                @Override
                protected Socket getSocket(InetAddress oSocketAddr, int iPort)
                {
                    return mockSocket;
                }

            };

            GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
            oRecord.setRequestKeyGuid("111-111");
            oRecord.setFileNameToRetrieve("/temp/temp.pdf");
            oRecord.setProducerProxyAddress("localhost");
            oRecord.setProducerProxyPort(3333L);
            oRecord.setMessageState("ENTERED");
            oImpl.startHttpTransaction(oRecord);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testStartHttpTransactionHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStartHttpTransactionHappyPath test: " + t.getMessage());
        }
    }

    /**
     * This tests the startHttpTransaction method with PropertyAccessException.
     */
    @Test
    public void testStartHttpTransactionPropertyAccessException()
    {
        System.out.println("testStartHttpTransactionPropertyAccessException");

        try
        {
            context.checking(new Expectations()
            {
                {
                    oneOf(mockLog).error(with(any(String.class)));
                }
            });
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected void sendMessageToSocket(String sMessage, Socket oOutputStream)
                {
                }

                @Override
                protected String getClientIP()
                    throws PropertyAccessException
                {
                    throw new PropertyAccessException("This is a PropertyAccessException");
                }

                @Override
                protected String getClientPort()
                {
                    return "4444";
                }

                @Override
                protected Socket getSocket(InetAddress oSocketAddr, int iPort)
                    throws IOException
                {
                    return mockSocket;
                }

            };

            GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
            oRecord.setRequestKeyGuid("111-111");
            oRecord.setFileNameToRetrieve("/temp/temp.pdf");
            oRecord.setProducerProxyAddress("localhost");
            oRecord.setProducerProxyPort(3333L);
            oRecord.setMessageState("ENTERED");
            oImpl.startHttpTransaction(oRecord);
        }
        catch (Exception e)
        {
            assertEquals("Incorrect exception occurred: ", 
                         "Failed to retrieve client socket IP and port properties from gateway.properties file.  Error: This is a PropertyAccessException",
                         e.getMessage());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testStartHttpTransactionPropertyAccessException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStartHttpTransactionPropertyAccessException test: " + t.getMessage());
        }
    }



    /**
     * This tests the startHttpTransaction method with IOException.
     */
    @Test
    public void testStartHttpTransactionIoException()
    {
        System.out.println("testStartHttpTransactionIoException");

        try
        {
            context.checking(new Expectations()
            {
                {
                    oneOf(mockLog).error(with(any(String.class)));
                }
            });
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected void sendMessageToSocket(String sMessage, Socket oOutputStream)
                {
                }

                @Override
                protected String getClientIP()
                {
                    return "localhost";
                }

                @Override
                protected String getClientPort()
                {
                    return "4444";
                }

                @Override
                protected Socket getSocket(InetAddress oSocketAddr, int iPort)
                    throws IOException
                {
                    throw new IOException("This is an IOException");
                }

            };

            GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
            oRecord.setRequestKeyGuid("111-111");
            oRecord.setFileNameToRetrieve("/temp/temp.pdf");
            oRecord.setProducerProxyAddress("localhost");
            oRecord.setProducerProxyPort(3333L);
            oRecord.setMessageState("ENTERED");
            oImpl.startHttpTransaction(oRecord);
        }
        catch (Exception e)
        {
            assertEquals("Incorrect exception occurred: ", "Failed to connect to client socket.  Error: This is an IOException", e.getMessage());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testStartHttpTransactionHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStartHttpTransactionHappyPath test: " + t.getMessage());
        }
    }

    /**
     * This tests the startHttpTransaction method with UnknownHostException.
     */
    @Test
    public void testStartHttpTransactionUnknownHostException()
    {
        System.out.println("testStartHttpTransactionUnknownHostException");

        try
        {
            context.checking(new Expectations()
            {
                {
                    oneOf(mockLog).error(with(any(String.class)));
                }
            });
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected void sendMessageToSocket(String sMessage, Socket oOutputStream)
                {
                }

                @Override
                protected String getClientIP()
                {
                    return "localhost";
                }

                @Override
                protected String getClientPort()
                {
                    return "4444";
                }

                @Override
                protected Socket getSocket(InetAddress oSocketAddr, int iPort)
                    throws UnknownHostException
                {
                    throw new UnknownHostException("This is an UnknownHostException");
                }

            };

            GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
            oRecord.setRequestKeyGuid("111-111");
            oRecord.setFileNameToRetrieve("/temp/temp.pdf");
            oRecord.setProducerProxyAddress("localhost");
            oRecord.setProducerProxyPort(3333L);
            oRecord.setMessageState("ENTERED");
            oImpl.startHttpTransaction(oRecord);
        }
        catch (Exception e)
        {
            assertEquals("Incorrect exception occurred: ", "Failed to connect to client socket.  Error: This is an UnknownHostException", e.getMessage());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testStartHttpTransactionUnknownHostException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStartHttpTransactionUnknownHostException test: " + t.getMessage());
        }
    }

    /**
     * This tests the startHttpTransaction method with RuntimeException.
     */
    @Test
    public void testStartHttpTransactionRuntimeException()
    {
        System.out.println("testStartHttpTransactionRuntimeException");

        try
        {
            context.checking(new Expectations()
            {
                {
                    oneOf(mockLog).error(with(any(String.class)));
                }
            });
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected void sendMessageToSocket(String sMessage, Socket oOutputStream)
                {
                }

                @Override
                protected String getClientIP()
                {
                    return "localhost";
                }

                @Override
                protected String getClientPort()
                {
                    return "4444";
                }

                @Override
                protected Socket getSocket(InetAddress oSocketAddr, int iPort)
                    throws RuntimeException
                {
                    throw new RuntimeException("This is a RuntimeException");
                }

            };

            GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
            oRecord.setRequestKeyGuid("111-111");
            oRecord.setFileNameToRetrieve("/temp/temp.pdf");
            oRecord.setProducerProxyAddress("localhost");
            oRecord.setProducerProxyPort(3333L);
            oRecord.setMessageState("ENTERED");
            oImpl.startHttpTransaction(oRecord);
        }
        catch (Exception e)
        {
            assertEquals("Incorrect exception occurred: ", 
                         "An unexpected error occurred while sending message to LiFT Client Manager Controller.  Error: This is a RuntimeException", e.getMessage());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testStartHttpTransactionRuntimeException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStartHttpTransactionRuntimeException test: " + t.getMessage());
        }
    }

    /**
     * Test of completeLiftTransaction method with happy path, of class GatewayLiftManagerProxyJavaImpl.
     */
    @Test
    public void testCompleteLiftTransactionHappyPath()
    {
        System.out.println("testCompleteLiftTransactionHappyPath");

        try
        {
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDAO;
                }
            };

            CompleteLiftTransactionRequestType oRequest = new CompleteLiftTransactionRequestType();
            oRequest.setRequestKeyGuid("111-111");
            oRequest.setFileURI("file://temp.file");

            CompleteLiftTransactionResponseType oResponse = oImpl.completeLiftTransaction(oRequest);
            assertNotNull("CompleteLiftTransactionResponse should not have been null. ", oResponse);
            assertEquals("Status was incorrect: ", "SUCCESS", oResponse.getStatus());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCompleteLiftTransactionHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCompleteLiftTransactionHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test of failedLiftTransaction method, of class GatewayLiftManagerProxyJavaImpl.
     */
    @Test
    public void testFailedLiftTransactionHappyPath()
    {
        System.out.println("testFailedLiftTransactionHappyPath");

        try
        {
            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected GatewayLiftMessageDao getGatewayLiftMessageDao()
                {
                    return mockDAO;
                }
            };

            FailedLiftTransactionRequestType oRequest = new FailedLiftTransactionRequestType();
            oRequest.setRequestKeyGuid("111-111");
            oRequest.setErrorMessage("This is the error.");

            FailedLiftTransactionResponseType oResponse = oImpl.failedLiftTransaction(oRequest);
            assertNotNull("FailedLiftTransactionResponse should not have been null. ", oResponse);
            assertEquals("Status was incorrect: ", "SUCCESS", oResponse.getStatus());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFailedLiftTransactionHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFailedLiftTransactionHappyPath test: " + t.getMessage());
        }
    }
}
