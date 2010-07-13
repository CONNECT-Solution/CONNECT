/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.lift.persistence;

import java.io.File;
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
import org.hibernate.SessionFactory;

/**
 *
 * @author westberg
 */
@RunWith(JMock.class)
public class HibernateUtilTest
{

    Mockery context = new JUnit4Mockery()
    {


        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final SessionFactory mockSession = context.mock(SessionFactory.class);
    final File mockConfigFile = context.mock(File.class);

    public HibernateUtilTest()
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
     * Note to why this test is disabled:  When running from the debugger,
     * it runs this test first.  Because of the state of what is happening,
     * we can only test the exception path if this test is the first test run.
     * If it is not, then the exception path cannot be hit because of the
     * static variable in the class.   When running under the debugger the order
     * is fine.  But when running at other times, the order is not.
     */
    /**
     * Test error case of retrieving a session.
     */
    //@Test (expected= java.lang.ExceptionInInitializerError.class)
    public void testGetSessionFactoryWithException()
    {
        context.checking(new Expectations()
        {
            {
                allowing(mockLog).debug(with(any(String.class)));
                allowing(mockLog).error(with(any(String.class)));
            }
        });
        HibernateUtil oHibernateUtil = new HibernateUtil()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }

            @Override
            protected SessionFactory createNewSessionFactory()
            {
                throw new RuntimeException("");
            }

            @Override
            protected File getConfigFile()
            {
                return mockConfigFile;
            }
        };

        SessionFactory oSession = oHibernateUtil.getSessionFactory();

    }

    /**
     * Test retrieval of a session.
     */
    @Test
    public void testGetSessionFactory()
    {
        try
        {
            HibernateUtil oHibernateUtil = new HibernateUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected SessionFactory createNewSessionFactory()
                {
                    return mockSession;
                }

                @Override
                protected File getConfigFile()
                {
                    return mockConfigFile;
                }

            };
            // Test the case where the session is null
            //------------------------------------------
            SessionFactory oSession = oHibernateUtil.getSessionFactory();
            assertNotNull("Session was null", oSession);

            // This should test out the case where the session is not null - and we simply return it.
            //---------------------------------------------------------------------------------------
            SessionFactory oNewSession = oHibernateUtil.getSessionFactory();
            assertNotNull("Session was null", oNewSession);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetSessionFactory test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetSessionFactory test: " + t.getMessage());
        }
        
    }

    /**
     * Test the create logger method.
     */
    @Test
    public void testCreateLogger()
    {
        try
        {
            HibernateUtil oHibernateUtil = new HibernateUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected SessionFactory createSessionFactory()
                {
                    return mockSession;
                }

                @Override
                protected File getConfigFile()
                {
                    return mockConfigFile;
                }

            };
            Log oLog = oHibernateUtil.createLogger();
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
     * Test the get config file method.
     */
    @Test
    public void testGetConfigFile()
    {
        try
        {
            HibernateUtil oHibernateUtil = new HibernateUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected SessionFactory createSessionFactory()
                {
                    return mockSession;
                }

                @Override
                protected File getConfigFile()
                {
                    return mockConfigFile;
                }

            };
            File oFile = oHibernateUtil.getConfigFile();
            assertNotNull("Log was null", oFile);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetConfigFile test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetConfigFile test: " + t.getMessage());
        }

    }

}
