/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.lift.dao;

import gov.hhs.fha.nhinc.lift.model.GatewayLiftMsgRecord;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.runner.RunWith;
import org.apache.commons.logging.Log;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * This class is used to test functionality within the
 * GatewayLiftMessageDao table.
 *
 * @author Les Westberg
 */
@RunWith(JMock.class)
public class GatwayLiftMessageDaoTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final Session mockSession = context.mock(Session.class);
    final Transaction mockTx = context.mock(Transaction.class);
    final Criteria mockCriteria = context.mock(Criteria.class);

    public GatwayLiftMessageDaoTest()
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
     * Test the create logger method.
     */
    @Test
    public void testCreateLogger()
    {
        try
        {
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            Log oLog = oDao.createLogger();
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
     * Test the get session method.
     */
    @Test
    public void testGetSession()
    {
        try
        {
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    return mockSession;
                }
            };
            Session oSession = oDao.getSession();
            assertNotNull("Session was null", oSession);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetSession test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetSession test: " + t.getMessage());
        }
    }

    /**
     * Test the startTransaction method.
     */
    @Test
    public void testStartTransaction()
    {
        try
        {
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }
            };
            Transaction oTx = oDao.startTransaction(null);
            assertNotNull("Transaction was null", oTx);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testStartTransaction test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStartTransaction test: " + t.getMessage());
        }
    }

    /**
     * Test the insertRecords method.
     */
    @Test
    public void testInsertRecordsHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(2).of(mockSession).persist(with(any(GatewayLiftMsgRecord.class)));
                    exactly(1).of(mockTx).commit();
                    allowing(mockSession).close();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    return mockSession;
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }
            };

            ArrayList<GatewayLiftMsgRecord> olLiftRecord = new ArrayList<GatewayLiftMsgRecord>();
            GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
            olLiftRecord.add(oRecord);
            oRecord = new GatewayLiftMsgRecord();
            olLiftRecord.add(oRecord);

            boolean bSuccess = oDao.insertRecords(olLiftRecord);
            assertTrue("Failed to insert records", bSuccess);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testInsertRecordsHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInsertRecordsHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the insertRecords method.
     */
    @Test
    public void testInsertRecordsNull()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    return mockSession;
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }
            };

            boolean bSuccess = oDao.insertRecords(null);
            assertTrue("Failed to insert records", bSuccess);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testInsertRecordsNull test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInsertRecordsNull test: " + t.getMessage());
        }
    }

    /**
     * Test the insertRecordsException.
     */
    @Test
    public void testInsertRecordsException()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error(with("Error during insertion caused by :Forced Exception."));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    throw new RuntimeException("Forced Exception.");
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }
            };

            ArrayList<GatewayLiftMsgRecord> olLiftRecord = new ArrayList<GatewayLiftMsgRecord>();
            GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
            olLiftRecord.add(oRecord);
            oRecord = new GatewayLiftMsgRecord();
            olLiftRecord.add(oRecord);

            boolean bSuccess = oDao.insertRecords(olLiftRecord);
            assertFalse("Failed to correctly catch exception.", bSuccess);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testInsertRecordsNull test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInsertRecordsNull test: " + t.getMessage());
        }
    }

    /**
     * Test the queryById method happy path.
     */
    @Test
    public void testQueryByIdHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockCriteria).add(with(any(Criterion.class)));
                    oneOf(mockSession).close();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    return mockSession;
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }

                @Override
                protected Criteria createCriteria(Session oSession)
                {
                    return mockCriteria;
                }

                @Override
                protected List<GatewayLiftMsgRecord> getRecordList(Criteria oCriteria)
                {
                    // Return one record for this test.
                    //----------------------------------
                    ArrayList<GatewayLiftMsgRecord> olLiftMsg = new ArrayList<GatewayLiftMsgRecord>();
                    GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
                    olLiftMsg.add(oRecord);
                    return olLiftMsg;
                }

            };

            GatewayLiftMsgRecord oRecord = oDao.queryById(new Long(1));
            assertNotNull("Failed to retrieve record", oRecord);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testQueryByIdHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testQueryByIdHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the queryByIdNoResultsHappyPath method happy path.
     */
    @Test
    public void testQueryByIdNoResultsHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockCriteria).add(with(any(Criterion.class)));
                    allowing(mockSession).close();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    return mockSession;
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }

                @Override
                protected Criteria createCriteria(Session oSession)
                {
                    return mockCriteria;
                }

                @Override
                protected List<GatewayLiftMsgRecord> getRecordList(Criteria oCriteria)
                {
                    // Return null
                    //------------
                    return null;
                }

            };

            GatewayLiftMsgRecord oRecord = oDao.queryById(new Long(1));
            assertNull("Retrieved a record when there should have been none.", oRecord);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testQueryByIdNoResultsHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testQueryByIdNoResultsHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the queryByIdNoSession method happy path.
     */
    @Test
    public void testQueryByIdNoSession()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    never(mockSession).close();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                    oneOf(mockLog).error(with(any(String.class)));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    // No session
                    //-----------
                    return null;
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }

                @Override
                protected Criteria createCriteria(Session oSession)
                {
                    return mockCriteria;
                }

                @Override
                protected List<GatewayLiftMsgRecord> getRecordList(Criteria oCriteria)
                {
                    // Return null
                    //------------
                    return null;
                }

            };

            GatewayLiftMsgRecord oRecord = oDao.queryById(new Long(1));
            assertNull("Retrieved a record when there should have been none.", oRecord);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testQueryByIdHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testQueryByIdHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the queryByRequestKeyGuid method happy path.
     */
    @Test
    public void testQueryByRequestKeyGuidHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockCriteria).add(with(any(Criterion.class)));
                    allowing(mockSession).close();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    return mockSession;
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }

                @Override
                protected Criteria createCriteria(Session oSession)
                {
                    return mockCriteria;
                }

                @Override
                protected List<GatewayLiftMsgRecord> getRecordList(Criteria oCriteria)
                {
                    // Return one record for this test.
                    //----------------------------------
                    ArrayList<GatewayLiftMsgRecord> olLiftMsg = new ArrayList<GatewayLiftMsgRecord>();
                    GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
                    olLiftMsg.add(oRecord);
                    return olLiftMsg;
                }

            };

            GatewayLiftMsgRecord oRecord = oDao.queryByRequestKeyGuid("1.1.1");
            assertNotNull("Failed to retrieve record", oRecord);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testQueryByRequestKeyGuidHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testQueryByRequestKeyGuidHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the queryByRequestKeyGuidNoResultsHappyPath method happy path.
     */
    @Test
    public void testQueryByRequestKeyGuidNoResultsHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockCriteria).add(with(any(Criterion.class)));
                    allowing(mockSession).close();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    return mockSession;
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }

                @Override
                protected Criteria createCriteria(Session oSession)
                {
                    return mockCriteria;
                }

                @Override
                protected List<GatewayLiftMsgRecord> getRecordList(Criteria oCriteria)
                {
                    // Return null
                    //------------
                    return null;
                }

            };

            GatewayLiftMsgRecord oRecord = oDao.queryByRequestKeyGuid("1.1.1");
            assertNull("Retrieved a record when there should have been none.", oRecord);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testQueryByRequestKeyGuidNoResultsHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testQueryByRequestKeyGuidNoResultsHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the queryByRequestKeyGuidNoSession method happy path.
     */
    @Test
    public void testQueryByRequestKeyGuidNoSession()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    never(mockSession).close();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                    oneOf(mockLog).error(with(any(String.class)));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    // No session
                    //-----------
                    return null;
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }

                @Override
                protected Criteria createCriteria(Session oSession)
                {
                    return mockCriteria;
                }

                @Override
                protected List<GatewayLiftMsgRecord> getRecordList(Criteria oCriteria)
                {
                    // Return null
                    //------------
                    return null;
                }

            };

            GatewayLiftMsgRecord oRecord = oDao.queryByRequestKeyGuid("1.1.1");
            assertNull("Retrieved a record when there should have been none.", oRecord);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testQueryByRequestKeyGuidNoSession test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testQueryByRequestKeyGuidNoSession test: " + t.getMessage());
        }
    }

    /**
     * Test the queryByMessageTypeOrderByProcessingTime method happy path.
     */
    @Test
    public void testQueryByMessageTypeOrderByProcessingTimeHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(2).of(mockCriteria).add(with(any(Criterion.class)));
                    exactly(1).of(mockCriteria).addOrder(with(any(Order.class)));
                    allowing(mockSession).close();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    return mockSession;
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }

                @Override
                protected Criteria createCriteria(Session oSession)
                {
                    return mockCriteria;
                }

                @Override
                protected List<GatewayLiftMsgRecord> getRecordList(Criteria oCriteria)
                {
                    // Return two records for this test.
                    //----------------------------------
                    ArrayList<GatewayLiftMsgRecord> olLiftMsg = new ArrayList<GatewayLiftMsgRecord>();
                    GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
                    olLiftMsg.add(oRecord);
                    oRecord = new GatewayLiftMsgRecord();
                    olLiftMsg.add(oRecord);
                    return olLiftMsg;
                }

            };

            List<GatewayLiftMsgRecord> olRecord = oDao.queryByMessageTypeOrderByProcessingTime("PROCESSING", new Date());
            assertNotNull("Failed to retrieve record", olRecord);
            assertEquals("Failed to return the correct number of results.", 2, olRecord.size());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testQueryByMessageTypeOrderByProcessingTimeHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testQueryByMessageTypeOrderByProcessingTimeHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the queryByMessageTypeOrderByProcessingTimeNoResultsHappyPath method happy path.
     */
    @Test
    public void testQueryByMessageTypeOrderByProcessingTimeNoResultsHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(2).of(mockCriteria).add(with(any(Criterion.class)));
                    exactly(1).of(mockCriteria).addOrder(with(any(Order.class)));
                    allowing(mockSession).close();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    return mockSession;
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }

                @Override
                protected Criteria createCriteria(Session oSession)
                {
                    return mockCriteria;
                }

                @Override
                protected List<GatewayLiftMsgRecord> getRecordList(Criteria oCriteria)
                {
                    // Return null
                    //------------
                    return null;
                }

            };

            List<GatewayLiftMsgRecord> olRecord = oDao.queryByMessageTypeOrderByProcessingTime("PROCESSING", new Date());
            assertNull("Retrieved a record when there should have been none.", olRecord);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testQueryByMessageTypeOrderByProcessingTimeNoResultsHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testQueryByMessageTypeOrderByProcessingTimeNoResultsHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the queryByMessageTypeOrderByProcessingTimeNoSession method happy path.
     */
    @Test
    public void testQueryByMessageTypeOrderByProcessingTimeNoSession()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    never(mockSession).close();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                    oneOf(mockLog).error(with(any(String.class)));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    // No session
                    //-----------
                    return null;
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }

                @Override
                protected Criteria createCriteria(Session oSession)
                {
                    return mockCriteria;
                }

                @Override
                protected List<GatewayLiftMsgRecord> getRecordList(Criteria oCriteria)
                {
                    // Return null
                    //------------
                    return null;
                }

            };

            List<GatewayLiftMsgRecord> olRecord = oDao.queryByMessageTypeOrderByProcessingTime("PROCESSING", new Date());
            assertNull("Retrieved a record when there should have been none.", olRecord);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testQueryByMessageTypeOrderByProcessingTimeNoSession test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testQueryByMessageTypeOrderByProcessingTimeNoSession test: " + t.getMessage());
        }
    }

    /**
     * Test the DeleteRecord method for happy path.
     */
    @Test
    public void testDeleteRecordHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockSession).delete(with(any(GatewayLiftMsgRecord.class)));
                    exactly(1).of(mockTx).commit();
                    allowing(mockSession).close();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    return mockSession;
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }
            };

            GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
            oRecord = new GatewayLiftMsgRecord();
            oDao.deleteRecord(oRecord);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testInsertRecordsHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInsertRecordsHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the deleteRecordsException.
     */
    @Test
    public void testDeleteRecordException()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error(with("Error during delete caused by :Forced Exception."));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    throw new RuntimeException("Forced Exception.");
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }
            };

            GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
            oRecord = new GatewayLiftMsgRecord();
            oDao.deleteRecord(oRecord);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testDeleteRecordNull test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDeleteRecordNull test: " + t.getMessage());
        }
    }

    /**
     * Test the deleteRecord method.
     */
    @Test
    public void testDeleteRecordNull()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    return mockSession;
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }
            };

            oDao.deleteRecord(null);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testDeleteRecordNull test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDeleteRecordNull test: " + t.getMessage());
        }
    }

    /**
     * Test the UpdateRecord method for happy path.
     */
    @Test
    public void testUpdateRecordHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockSession).update(with(any(GatewayLiftMsgRecord.class)));
                    exactly(1).of(mockTx).commit();
                    allowing(mockSession).close();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    return mockSession;
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }
            };

            GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
            oRecord = new GatewayLiftMsgRecord();
            oDao.updateRecord(oRecord);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testUpdateRecordsHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testUpdateRecordsHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the updateRecordsException.
     */
    @Test
    public void testUpdateRecordException()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error(with("Error during update caused by: Forced Exception."));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    throw new RuntimeException("Forced Exception.");
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }
            };

            GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
            oRecord = new GatewayLiftMsgRecord();
            oDao.updateRecord(oRecord);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testUpdateRecordNull test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testUpdateRecordNull test: " + t.getMessage());
        }
    }

    /**
     * Test the updateRecord method.
     */
    @Test
    public void testUpdateRecordNull()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            GatewayLiftMessageDao oDao = new GatewayLiftMessageDao()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected Session getSession()
                {
                    return mockSession;
                }

                @Override
                protected Transaction startTransaction(Session oSession)
                {
                    return mockTx;
                }
            };

            oDao.updateRecord(null);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testUpdateRecordNull test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testUpdateRecordNull test: " + t.getMessage());
        }
    }


}
