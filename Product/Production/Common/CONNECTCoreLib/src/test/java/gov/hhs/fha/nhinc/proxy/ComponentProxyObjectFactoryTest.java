package gov.hhs.fha.nhinc.proxy;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class ComponentProxyObjectFactoryTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final Log mockLog = context.mock(Log.class);

    @Test
    public void testCreateLogger()
    {
        try
        {
            ComponentProxyObjectFactory sut = new ComponentProxyObjectFactory()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getConfigFileName()
                {
                    return "";
                }

            };
            Log log = sut.createLogger();
            assertNotNull("Log was null", log);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCreateLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger test: " + t.getMessage());
        }
    }

    @Test
    public void testGetPropertyFileURL()
    {
        try
        {
            ComponentProxyObjectFactory sut = new ComponentProxyObjectFactory()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getPropertyFileURL()
                {
                    return "TEST_PROPERTY_FILE_URL";
                }

                @Override
                protected String getConfigFileName()
                {
                    return "";
                }

            };
            String propertyFileURL = sut.getPropertyFileURL();
            assertNotNull("Property file URL was null", propertyFileURL);
            assertEquals("Property file URL not equal", "TEST_PROPERTY_FILE_URL", propertyFileURL);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetPropertyFileURL test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetPropertyFileURL test: " + t.getMessage());
        }
    }

    @Test
    public void testCreateApplicationContext()
    {
        try
        {
            final ApplicationContext mockContext = context.mock(ApplicationContext.class);

            ComponentProxyObjectFactory sut = new ComponentProxyObjectFactory()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected ApplicationContext createApplicationContext(String configFilePath)
                {
                    return mockContext;
                }

                @Override
                protected String getConfigFileName()
                {
                    return "";
                }

            };
            ApplicationContext appContext = sut.createApplicationContext("");
            assertNotNull("ApplicationContext was null", appContext);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCreateApplicationContext test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateApplicationContext test: " + t.getMessage());
        }
    }

    @Test
    public void testRefreshApplicationContext()
    {
        try
        {
            final ApplicationContext mockContext = context.mock(ApplicationContext.class);

            ComponentProxyObjectFactory sut = new ComponentProxyObjectFactory()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected void refreshConfigurationContext(ApplicationContext appContext)
                {
                }

                @Override
                protected String getConfigFileName()
                {
                    return "";
                }

            };
            sut.refreshConfigurationContext(mockContext);
            // Success if completes
            assertTrue(true);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRefreshApplicationContext test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRefreshApplicationContext test: " + t.getMessage());
        }
    }

    @Test
    public void testGetLastModified()
    {
        try
        {
            ComponentProxyObjectFactory sut = new ComponentProxyObjectFactory()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected long getLastModified(String filePath)
                {
                    return 6L;
                }

                @Override
                protected String getConfigFileName()
                {
                    return "";
                }

            };
            long lastModified = sut.getLastModified("");
            assertEquals("Last modified not equal", 6L, lastModified);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetLastModified test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetLastModified test: " + t.getMessage());
        }
    }

    @Test
    public void testGetConfigFileName()
    {
        try
        {
            ComponentProxyObjectFactory sut = new ComponentProxyObjectFactory()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getConfigFileName()
                {
                    return "TEST_CONFIG_FILE_NAME";
                }

            };
            String configFileName = sut.getConfigFileName();
            assertNotNull("Config file name was null", configFileName);
            assertEquals("Config file name not equal", "TEST_CONFIG_FILE_NAME", configFileName);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetConfigFileName test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetConfigFileName test: " + t.getMessage());
        }
    }

    @Test
    public void testGetContextHappy()
    {
        try
        {
            final ApplicationContext mockContext = context.mock(ApplicationContext.class);

            context.checking(new Expectations()
            {
                {
                    oneOf(mockLog).debug("ApplicationContext for: TestFile.xml was null - creating.");
                }
            });
            ComponentProxyObjectFactory sut = new ComponentProxyObjectFactory()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected long getLastModified(String filePath)
                {
                    return 5L;
                }

                @Override
                protected String getPropertyFileURL()
                {
                    return "/test/";
                }

                @Override
                protected String getConfigFileName()
                {
                    return "TestFile";
                }

                @Override
                protected ApplicationContext createApplicationContext(String configFilePath)
                {
                    return mockContext;
                }


            };
            assertNotNull("ApplicationContext from getContext", sut.getContext());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetContextHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetContextHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testGetContextRefresh()
    {
        try
        {
            final ApplicationContext mockContext = context.mock(ApplicationContext.class);

            ComponentProxyObjectFactory sut = new ComponentProxyObjectFactory()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected void refreshConfigurationContext(ApplicationContext appContext)
                {
                }

                @Override
                protected long getLastModified(String filePath)
                {
                    return 6L;
                }

                @Override
                protected String getPropertyFileURL()
                {
                    return "/test/";
                }

                @Override
                protected String getConfigFileName()
                {
                    return "TestFile";
                }

                @Override
                protected ApplicationContext createApplicationContext(String configFilePath)
                {
                    return mockContext;
                }

                @Override
                protected LocalApplicationContextInfo getAppContextInfo(String sKey)
                {
                    LocalApplicationContextInfo oInfo = new LocalApplicationContextInfo();
                    oInfo.setApplicationContext(mockContext);
                    oInfo.setConfigLastModified(5L);
                    return oInfo;
                }


            };
            context.checking(new Expectations()
            {
                {
                    oneOf(mockLog).debug("ApplicationContext for: TestFile.xml was not null - checking to see if it is stale.");
                    oneOf(mockLog).debug("Refreshing the Spring application context for: TestFile.xml");
                }
            });
            assertNotNull("ApplicationContext from getContext", sut.getContext());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetContextRefresh test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetContextRefresh test: " + t.getMessage());
        }
    }

    @Test
    public void testGetBeanHappy()
    {

        try
        {
            final String testBean = "testbean";
            final ApplicationContext appContext = new FileSystemXmlApplicationContext()
            {
                @Override
                public Object getBean(String beanName)
                {
                    return testBean;
                }
            };

            ComponentProxyObjectFactory sut = new ComponentProxyObjectFactory()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected ApplicationContext getContext()
                {
                    return appContext;
                }

                @Override
                protected String getConfigFileName()
                {
                    return "";
                }

            };
            assertEquals("getBean test value", "testbean", sut.getBean("", String.class));
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetBeanHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetBeanHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testGetBeanNullContext()
    {

        try
        {
            ComponentProxyObjectFactory sut = new ComponentProxyObjectFactory()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected ApplicationContext getContext()
                {
                    return null;
                }

                @Override
                protected String getConfigFileName()
                {
                    return "";
                }

            };
            context.checking(new Expectations()
            {
                {
                    oneOf(mockLog).warn("ApplicationContext was null - not retrieving bean.");
                }
            });
            assertNull("ApplicationContext from getContext", sut.getBean("", String.class));
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetBeanNullContext test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetBeanNullContext test: " + t.getMessage());
        }
    }

    @Test
    public void testGetBeanWrongType()
    {

        try
        {
            final String testBean = "testbean";
            final ApplicationContext appContext = new FileSystemXmlApplicationContext()
            {
                @Override
                public Object getBean(String beanName)
                {
                    return testBean;
                }
            };

            ComponentProxyObjectFactory sut = new ComponentProxyObjectFactory()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected ApplicationContext getContext()
                {
                    return appContext;
                }

                @Override
                protected String getConfigFileName()
                {
                    return "";
                }

            };
            sut.getBean("", Integer.class);
            fail("Should have had exception.");
        }
        catch(ClassCastException cce)
        {
            // Expected exception
            assertTrue(true);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetBeanWrongType test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetBeanWrongType test: " + t.getMessage());
        }
    }

}