package gov.hhs.fha.nhinc.repository.dao;

import gov.hhs.fha.nhinc.repository.model.EventCode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.apache.commons.logging.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Unit test for the save method of the EventCodeDao class
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class EventCodeDaoSaveTest
{
    Mockery context = new JUnit4Mockery();

    public EventCodeDaoSaveTest()
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

    @Test
    public void testSaveMethodWithComponentsAvailable()
    {
        // Create mock objects
        final Log log = context.mock(Log.class);
        final SessionFactory sessionFactory = context.mock(SessionFactory.class);
        final Session session = context.mock(Session.class);
        
        // Build test object and prepare the test
        final EventCode eventCode = new EventCode();
        EventCodeDao dao = new EventCodeDao()
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
            oneOf (session).beginTransaction();
            oneOf (session).saveOrUpdate(eventCode);
            oneOf (session).close();
        }});

        // Execute the test
        dao.save(eventCode);
    }
}
