/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.lift.dao;

import gov.hhs.fha.nhinc.lift.model.LiftTransferDataRecord;
import java.util.List;
import org.apache.commons.logging.Log;
import org.hibernate.SessionFactory;
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
import org.junit.runner.RunWith;
import org.hibernate.Session;
import org.junit.Assert;

/**
 *
 * @author vvickers
 */
@RunWith(JMock.class)
public class LiftTransferDataRecordDaoTest {
    public static final String TEST_GUID = "123456";
    LiftTransferDataRecord foundRecord = null;

    Mockery context = new JUnit4Mockery();

    public LiftTransferDataRecordDaoTest() {
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

    @Test
    public void testNewRecordSave()
    {
        // Create mock objects
        final Log log = context.mock(Log.class);
        final SessionFactory sessionFactory = context.mock(SessionFactory.class);
        final Session session = context.mock(Session.class);

        // Build test object and prepare the test
        final LiftTransferDataRecord transferRecord = new LiftTransferDataRecord();
        transferRecord.setRequestKeyGuid(TEST_GUID);
        transferRecord.setTransferState("ENTERED");
        LiftTransferDataRecordDao dao = new LiftTransferDataRecordDao()
        {
            @Override
            protected Log getLogger()
            {
                return log;
            }

            @Override
            protected SessionFactory getSessionFactory()
            {
                return sessionFactory;
            }

            @Override
            protected Session getSession(SessionFactory sessionFactory)
            {
                return session;
            }
        };

        // Set expectations
        context.checking(new Expectations(){{
            one (session).beginTransaction();
            one (session).saveOrUpdate(transferRecord);
            one (session).close();
        }});

        // Execute the test
        dao.save(transferRecord);
        context.assertIsSatisfied();
    }

    @Test
    public void testNewRecordSaveNullSessionFactory()
    {
        // Create mock objects
        final Log log = context.mock(Log.class);
        final SessionFactory sessionFactory = context.mock(SessionFactory.class);
        final Session session = context.mock(Session.class);

        // Build test object and prepare the test
        final LiftTransferDataRecord transferRecord = new LiftTransferDataRecord();
        transferRecord.setRequestKeyGuid(TEST_GUID);
        transferRecord.setTransferState("ENTERED");
        LiftTransferDataRecordDao dao = new LiftTransferDataRecordDao()
        {
            @Override
            protected Log getLogger()
            {
                return log;
            }

            @Override
            protected SessionFactory getSessionFactory()
            {
                return null;
            }

            @Override
            protected Session getSession(SessionFactory sessionFactory)
            {
                return session;
            }
        };

        // Set expectations
        context.checking(new Expectations(){{
            one (log).error("Session factory was null");
        }});

        // Execute the test
        dao.save(transferRecord);
        context.assertIsSatisfied();
    }

    @Test
    public void testNewRecordSaveNullSession()
    {
        // Create mock objects
        final Log log = context.mock(Log.class);
        final SessionFactory sessionFactory = context.mock(SessionFactory.class);
        final Session session = context.mock(Session.class);

        // Build test object and prepare the test
        final LiftTransferDataRecord transferRecord = new LiftTransferDataRecord();
        transferRecord.setRequestKeyGuid(TEST_GUID);
        transferRecord.setTransferState("ENTERED");
        LiftTransferDataRecordDao dao = new LiftTransferDataRecordDao()
        {
            @Override
            protected Log getLogger()
            {
                return log;
            }

            @Override
            protected SessionFactory getSessionFactory()
            {
                return sessionFactory;
            }

            @Override
            protected Session getSession(SessionFactory sessionFactory)
            {
                return null;
            }
        };

        // Set expectations
        context.checking(new Expectations(){{
            one (log).error("Failed to obtain a session from the sessionFactory");
        }});

        // Execute the test
        dao.save(transferRecord);
        context.assertIsSatisfied();
    }

    @Test
    public void testRecordFind()
    {
        // Create mock objects
        final Log log = context.mock(Log.class);
        final SessionFactory sessionFactory = context.mock(SessionFactory.class);
        final Session session = context.mock(Session.class);

        // Build test object and prepare the test
        LiftTransferDataRecordDao dao = new LiftTransferDataRecordDao()
        {
            @Override
            protected Log getLogger()
            {
                return log;
            }

            @Override
            protected SessionFactory getSessionFactory()
            {
                return sessionFactory;
            }

            @Override
            protected Session getSession(SessionFactory sessionFactory)
            {
                return session;
            }
        };

        // Set expectations
        context.checking(new Expectations(){{
            one (session).createCriteria(LiftTransferDataRecord.class);
            one (session).close();
        }});

        // Execute the test
        List<LiftTransferDataRecord> records = dao.findForGuid(TEST_GUID);
        context.assertIsSatisfied();
    }

    @Test
    public void testRecordFindNullSessionFactory()
    {
        // Create mock objects
        final Log log = context.mock(Log.class);
        final SessionFactory sessionFactory = context.mock(SessionFactory.class);
        final Session session = context.mock(Session.class);

        // Build test object and prepare the test
        LiftTransferDataRecordDao dao = new LiftTransferDataRecordDao()
        {
            @Override
            protected Log getLogger()
            {
                return log;
            }

            @Override
            protected SessionFactory getSessionFactory()
            {
                return null;
            }

            @Override
            protected Session getSession(SessionFactory sessionFactory)
            {
                return session;
            }
        };

        // Set expectations
        context.checking(new Expectations(){{
            one (log).error("Session factory was null");
        }});

        // Execute the test
        List<LiftTransferDataRecord> records = dao.findForGuid(TEST_GUID);
        context.assertIsSatisfied();
    }

    @Test
    public void testRecordFindNullSession()
    {
        // Create mock objects
        final Log log = context.mock(Log.class);
        final SessionFactory sessionFactory = context.mock(SessionFactory.class);
        final Session session = context.mock(Session.class);

        // Build test object and prepare the test
        LiftTransferDataRecordDao dao = new LiftTransferDataRecordDao()
        {
            @Override
            protected Log getLogger()
            {
                return log;
            }

            @Override
            protected SessionFactory getSessionFactory()
            {
                return sessionFactory;
            }

            @Override
            protected Session getSession(SessionFactory sessionFactory)
            {
                return null;
            }
        };

        // Set expectations
        context.checking(new Expectations(){{
            one (log).error("Failed to obtain a session from the sessionFactory");
        }});

        // Execute the test
        List<LiftTransferDataRecord> records = dao.findForGuid(TEST_GUID);
        context.assertIsSatisfied();
    }

    @Test
    public void testRecordDelete()
    {
        // Create mock objects
        final Log log = context.mock(Log.class);
        final SessionFactory sessionFactory = context.mock(SessionFactory.class);
        final Session session = context.mock(Session.class);

        // Build test object and prepare the test
        LiftTransferDataRecordDao dao = new LiftTransferDataRecordDao()
        {
            @Override
            protected Log getLogger()
            {
                return log;
            }

            @Override
            protected SessionFactory getSessionFactory()
            {
                return sessionFactory;
            }

            @Override
            protected Session getSession(SessionFactory sessionFactory)
            {
                return session;
            }
        };

        LiftTransferDataRecord transferRecord = new LiftTransferDataRecord();

        // Set expectations
        context.checking(new Expectations(){{
            one (session).beginTransaction();
            one (session).delete(with(any(LiftTransferDataRecord.class)));
            one (session).close();
        }});

        // Execute the test
        dao.delete(transferRecord);
        context.assertIsSatisfied();
    }

    @Test
    public void testRecordDeleteNullSessionFactory()
    {
        // Create mock objects
        final Log log = context.mock(Log.class);
        final SessionFactory sessionFactory = context.mock(SessionFactory.class);
        final Session session = context.mock(Session.class);

        // Build test object and prepare the test
        LiftTransferDataRecordDao dao = new LiftTransferDataRecordDao()
        {
            @Override
            protected Log getLogger()
            {
                return log;
            }

            @Override
            protected SessionFactory getSessionFactory()
            {
                return null;
            }

            @Override
            protected Session getSession(SessionFactory sessionFactory)
            {
                return session;
            }
        };

        LiftTransferDataRecord transferRecord = new LiftTransferDataRecord();

        // Set expectations
        context.checking(new Expectations(){{
            one (log).error("Session factory was null");
        }});

        // Execute the test
        dao.delete(transferRecord);
        context.assertIsSatisfied();
    }

    @Test
    public void testRecordDeleteNullSession()
    {
        // Create mock objects
        final Log log = context.mock(Log.class);
        final SessionFactory sessionFactory = context.mock(SessionFactory.class);
        final Session session = context.mock(Session.class);

        // Build test object and prepare the test
        LiftTransferDataRecordDao dao = new LiftTransferDataRecordDao()
        {
            @Override
            protected Log getLogger()
            {
                return log;
            }

            @Override
            protected SessionFactory getSessionFactory()
            {
                return sessionFactory;
            }

            @Override
            protected Session getSession(SessionFactory sessionFactory)
            {
                return null;
            }
        };

        LiftTransferDataRecord transferRecord = new LiftTransferDataRecord();

        // Set expectations
        context.checking(new Expectations(){{
            one (log).error("Failed to obtain a session from the sessionFactory");
        }});

        // Execute the test
        dao.delete(transferRecord);
        context.assertIsSatisfied();
    }
}