package gov.hhs.fha.nhinc.service;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.LoggingContextHelper;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.runner.RunWith;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;

@RunWith(JMock.class)
public class WebServiceHelperTest
{

    Mockery context = new JUnit4Mockery()
    {


        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);

    public WebServiceHelperTest()
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
     * This method is used to test out some of the dynamic invocaton methods.
     *
     * @param x an integer.
     * @param y a result.
     */
    public Integer helperMethod(Integer x)
    {
        return x;
    }

    /**
     * This method is used to test out some of the dynamic invocaton methods.
     *
     * @param operationInput The input parameter for the method.
     * @param assertion The assertion object as extracted from the web context
     * @param targets The NHIN target communities
     * @param a result.
     */
    public Integer helperMethod2(Integer operationInput, AssertionType assertion, Integer targets)
    {
        return operationInput * targets;
    }

    /**
     * This method is used to test out some of the dynamic invocaton methods.
     *
     * @param operationInput The input parameter for the method.
     * @param assertion The assertion object as extracted from the web context
     * @param a result.
     */
    public Integer helperMethod3(Integer operationInput, AssertionType assertion)
    {
        return operationInput;
    }
    /**
     * Test the create logger method.
     */
    @Test
    public void testCreateLogger()
    {
        try
        {
            WebServiceHelper oHelper = new WebServiceHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            Log oLog = oHelper.createLogger();
            assertNotNull("Log was null", oLog);
        } catch (Throwable t)
        {
            System.out.println("Error running testCreateLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger test: " + t.getMessage());
        }
    }

    /**
     * Test the getMethod method.
     */
    @Test
    public void testGetMethod()
    {
        try
        {
            WebServiceHelper oHelper = new WebServiceHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };

            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });

            Method oMethod = oHelper.getMethod(this.getClass(), "helperMethod");
            assertNotNull("getMethod failed", oMethod);
            assertEquals("Incorrect method returned.", "helperMethod", oMethod.getName());
        } catch (Throwable t)
        {
            System.out.println("Error running testGetMethod test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetMethod test: " + t.getMessage());
        }
    }

    /**
     * Test the populateAssertionWithMessageId method.
     */
    @Test
    public void testPopulateAssertionWithMessageId()
    {
        WebServiceHelper oHelper = new WebServiceHelper();
        AssertionType assertion = new AssertionType()
        {

            @Override
            public void setMessageId(String value)
            {
                assertEquals("Population of Assertion with the messageId fails ", "TestId", value);
            }
        };

        String messageId = "TestId";
        oHelper.populateAssertionWithMessageId(assertion, messageId);
    }

    /**
     * Test the populateAssertionWithRelatesToList method.
     */
    @Test
    public void testPopulateAssertionWithRelatesToList()
    {
        WebServiceHelper oHelper = new WebServiceHelper();
        AssertionType assertion = new AssertionType()
        {

            protected List<String> relatesToList = null;

            @Override
            public List<String> getRelatesToList()
            {
                if (relatesToList == null)
                {
                    relatesToList = new ArrayList<String>();
                }
                return this.relatesToList;
            }
        };

        List<String> relatesToIds = new ArrayList();
        relatesToIds.add("TestId_1");
        relatesToIds.add("TestId_2");
        oHelper.populateAssertionWithRelatesToList(assertion, relatesToIds);

        List<String> assertionList = assertion.getRelatesToList();
        assertNotNull(assertionList);
        if (assertionList != null)
        {
            assertTrue("Assertion does not contain relatesTo Id: TestId_1 ", assertionList.contains("TestId_1"));
            assertTrue("Assertion does not contain relatesTo Id: TestId_2 ", assertionList.contains("TestId_2"));
        }
    }

    /**
     * Test the invokeSecureWebService method.
     */
    @Test
    public void testInvokeSecureWebService()
    {
        try
        {
            WebServiceHelper oHelper = new WebServiceHelper()
            {

                @Override
                protected LoggingContextHelper getLoggingContextHelper()
                {
                    LoggingContextHelper loggingContextHelper = new LoggingContextHelper()
                    {

                        @Override
                        public void setContext(WebServiceContext webServiceContext)
                        {
                        }

                        @Override
                        public void clearContext()
                        {
                        }
                    };
                    return loggingContextHelper;

                }

                @Override
                protected AssertionType getSamlAssertion(WebServiceContext context)
                {
                    AssertionType assertion = new AssertionType();
                    return assertion;
                }

                @Override
                protected String getMessageId(WebServiceContext context)
                {
                    return "Test";
                }

                @Override
                protected void populateAssertionWithMessageId(AssertionType assertion, String messageId)
                {
                }

                @Override
                protected List<String> getRelatesToList(WebServiceContext context)
                {
                    return new ArrayList();
                }

                @Override
                protected void populateAssertionWithRelatesToList(AssertionType assertion, List<String> relatesToIds)
                {
                }
            };
            Integer oResponse = (Integer) oHelper.invokeSecureWebService(this, this.getClass(), "helperMethod2", new Integer(10), new Integer(20), mockWebServiceContext);
            assertNotNull("invokePort failed to return a value.", oResponse);
            assertTrue("Response was incorrect type.", oResponse instanceof Integer);
            assertEquals("Incorrect value returned.", 200, oResponse.intValue());

        } catch (Exception ex)
        {
            fail("Error running testInvokeSecureWebService test: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Test the invokeUnsecureWebService method.
     */
    @Test
    public void testInvokeUnsecureWebService()
    {
        try
        {
            WebServiceHelper oHelper = new WebServiceHelper()
            {

                @Override
                protected LoggingContextHelper getLoggingContextHelper()
                {
                    LoggingContextHelper loggingContextHelper = new LoggingContextHelper()
                    {

                        @Override
                        public void setContext(WebServiceContext webServiceContext)
                        {
                        }

                        @Override
                        public void clearContext()
                        {
                        }
                    };
                    return loggingContextHelper;

                }

                @Override
                protected AssertionType getSamlAssertion(WebServiceContext context)
                {
                    AssertionType assertion = new AssertionType();
                    return assertion;
                }

                @Override
                protected String getMessageId(WebServiceContext context)
                {
                    return "Test";
                }

                @Override
                protected void populateAssertionWithMessageId(AssertionType assertion, String messageId)
                {
                }

                @Override
                protected List<String> getRelatesToList(WebServiceContext context)
                {
                    return new ArrayList();
                }

                @Override
                protected void populateAssertionWithRelatesToList(AssertionType assertion, List<String> relatesToIds)
                {
                }
            };
            Integer oResponse = (Integer) oHelper.invokeUnsecureWebService(this, this.getClass(), "helperMethod2", new Integer(10), oHelper.getSamlAssertion(mockWebServiceContext), new Integer(20), mockWebServiceContext);
            assertNotNull("invokePort failed to return a value.", oResponse);
            assertTrue("Response was incorrect type.", oResponse instanceof Integer);
            assertEquals("Incorrect value returned.", 200, oResponse.intValue());

        } catch (Exception ex)
        {
            fail("Error running testInvokeSecureWebService test: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    /**
     * Test the invokeSecureDeferredResponseWebService method.
     */
    @Test
    public void testInvokeSecureDeferredResponseWebService()
    {
        try
        {
            WebServiceHelper oHelper = new WebServiceHelper()
            {

                @Override
                protected LoggingContextHelper getLoggingContextHelper()
                {
                    LoggingContextHelper loggingContextHelper = new LoggingContextHelper()
                    {

                        @Override
                        public void setContext(WebServiceContext webServiceContext)
                        {
                        }

                        @Override
                        public void clearContext()
                        {
                        }
                    };
                    return loggingContextHelper;

                }

                @Override
                protected AssertionType getSamlAssertion(WebServiceContext context)
                {
                    AssertionType assertion = new AssertionType();
                    return assertion;
                }

                @Override
                protected String getMessageId(WebServiceContext context)
                {
                    return "Test";
                }

                @Override
                protected void populateAssertionWithMessageId(AssertionType assertion, String messageId)
                {
                }

                @Override
                protected List<String> getRelatesToList(WebServiceContext context)
                {
                    return new ArrayList();
                }

                @Override
                protected void populateAssertionWithRelatesToList(AssertionType assertion, List<String> relatesToIds)
                {
                }
            };
            Integer oResponse = (Integer) oHelper.invokeSecureDeferredResponseWebService(this, this.getClass(), "helperMethod3", new Integer(10), mockWebServiceContext);
            assertNotNull("invokePort failed to return a value.", oResponse);
            assertTrue("Response was incorrect type.", oResponse instanceof Integer);
            assertEquals("Incorrect value returned.", 10, oResponse.intValue());

        } catch (Exception ex)
        {
            fail("Error running testInvokeSecureDeferredResponseWebService test: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    /**
     * Test the invokeDeferredResponseWebService method.
     */
    @Test
    public void testInvokeDeferredResponseWebService()
    {
        try
        {
            WebServiceHelper oHelper = new WebServiceHelper()
            {

                @Override
                protected LoggingContextHelper getLoggingContextHelper()
                {
                    LoggingContextHelper loggingContextHelper = new LoggingContextHelper()
                    {

                        @Override
                        public void setContext(WebServiceContext webServiceContext)
                        {
                        }

                        @Override
                        public void clearContext()
                        {
                        }
                    };
                    return loggingContextHelper;

                }

                @Override
                protected AssertionType getSamlAssertion(WebServiceContext context)
                {
                    AssertionType assertion = new AssertionType();
                    return assertion;
                }

                @Override
                protected String getMessageId(WebServiceContext context)
                {
                    return "Test";
                }

                @Override
                protected void populateAssertionWithMessageId(AssertionType assertion, String messageId)
                {
                }

                @Override
                protected List<String> getRelatesToList(WebServiceContext context)
                {
                    return new ArrayList();
                }

                @Override
                protected void populateAssertionWithRelatesToList(AssertionType assertion, List<String> relatesToIds)
                {
                }
            };
            Integer oResponse = (Integer) oHelper.invokeDeferredResponseWebService(this, this.getClass(), "helperMethod", oHelper.getSamlAssertion(mockWebServiceContext),new Integer(100), mockWebServiceContext);
            assertNotNull("invokePort failed to return a value.", oResponse);
            assertTrue("Response was incorrect type.", oResponse instanceof Integer);
            assertEquals("Incorrect value returned.", 100, oResponse.intValue());

        } catch (Exception ex)
        {
            fail("Error running testInvokeDeferredResponseWebService test: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
