/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.webserviceproxy;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.developer.WSBindingProvider;
import gov.hhs.fha.nhinc.async.AsyncHeaderCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.lang.IllegalArgumentException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.runner.RunWith;

/**
 *
 * @author westberg
 */
@RunWith(JMock.class)
public class WebServiceProxyHelperTest
{

    Mockery context = new JUnit4Mockery()
    {


        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final BindingProvider mockPort = context.mock(BindingProvider.class);
    final Map<String, Object> mockRequestContext = context.mock(Map.class);
    final HashMap<String, Object> oMap = new HashMap<String, Object>();
    final SamlTokenCreator mockTokenCreator = context.mock(SamlTokenCreator.class);
    final AsyncHeaderCreator mockAsyncHeaderCreator = context.mock(AsyncHeaderCreator.class);

    public WebServiceProxyHelperTest()
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
     * @param x an integer.
     * @param y an integer.
     * @param a result.
     */
    public Integer helperMethod2(Integer x, Integer y)
    {
        return x;
    }

    /**
     * Test the create logger method.
     */
    @Test
    public void testCreateLogger()
    {
        try
        {
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            Log oLog = oHelper.createLogger();
            assertNotNull("Log was null", oLog);
        }
        catch (Throwable t)
        {
            System.out.println("Error running testCreateLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger test: " + t.getMessage());
        }
    }

    /**
     * Test the GetExceptionText method happy path.
     */
    @Test
    public void testGetExceptionTextHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    return "PropertyAccessException";
                }
            };

            String sExceptionText = oHelper.getExceptionText();
            assertEquals("getExceptionText failed.", "PropertyAccessException", sExceptionText);
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetExceptionTextHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetExceptionTextHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the GetExceptionText method with PropertyAccessException.
     */
    @Test
    public void testGetExceptionTextPropertyException()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    throw new PropertyAccessException("Failed to retrieve property.");
                }
            };

            String sExceptionText = oHelper.getExceptionText();
            assertEquals("getExceptionText failed: ", "", sExceptionText);
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetExceptionTextPropertyException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetExceptionTextPropertyException test: " + t.getMessage());
        }
    }

    /**
     * Test the GetRetryAttempts method happy path.
     */
    @Test
    public void testGetRetryAttemptsHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    return "5";
                }
            };

            int iRetryAttempts = oHelper.getRetryAttempts();
            assertEquals("RetryAttempts failed.", 5, iRetryAttempts);
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetRetryAttemptsHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetRetryAttemptsHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the GetRetryAttempts method with PropertyAccessException.
     */
    @Test
    public void testGetRetryAttemptsPropertyException()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    throw new PropertyAccessException("Failed to retrieve property.");
                }
            };

            int iRetryAttempts = oHelper.getRetryAttempts();
            assertEquals("getRetryAttempts failed: ", 0, iRetryAttempts);
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetRetryAttemptsPropertyException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetRetryAttemptsPropertyException test: " + t.getMessage());
        }
    }

    /**
     * Test the GetRetryAttempts method with NumberFormatException.
     */
    @Test
    public void testGetRetryAttemptsNumberFormatException()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    return "A";
                }
            };

            int iRetryAttempts = oHelper.getRetryAttempts();
            assertEquals("getRetryAttempts failed: ", 0, iRetryAttempts);
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetRetryAttemptsNumberFormatException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetRetryAttemptsNumberFormatException test: " + t.getMessage());
        }
    }

    /**
     * Test the GetRetryDelay method happy path.
     */
    @Test
    public void testGetRetryDelayHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    return "5";
                }
            };

            int iRetryDelay = oHelper.getRetryDelay();
            assertEquals("RetryDelay failed.", 5, iRetryDelay);
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetRetryDelayHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetRetryDelayHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the GetRetryDelay method with PropertyAccessException.
     */
    @Test
    public void testGetRetryDelayPropertyException()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    throw new PropertyAccessException("Failed to retrieve property.");
                }
            };

            int iRetryDelay = oHelper.getRetryDelay();
            assertEquals("getRetryDelay failed: ", 0, iRetryDelay);
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetRetryDelayPropertyException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetRetryDelayPropertyException test: " + t.getMessage());
        }
    }

    /**
     * Test the GetRetryDelay method with NumberFormatException.
     */
    @Test
    public void testGetRetryDelayNumberFormatException()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    return "A";
                }
            };

            int iRetryDelay = oHelper.getRetryDelay();
            assertEquals("getRetryDelay failed: ", 0, iRetryDelay);
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetRetryDelayNumberFormatException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetRetryDelayNumberFormatException test: " + t.getMessage());
        }
    }

    /**
     * Test the GetTimeout method happy path.
     */
    @Test
    public void testGetTimeoutHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    return "5";
                }
            };

            int iTimeout = oHelper.getTimeout();
            assertEquals("Timeout failed.", 5, iTimeout);
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetTimeoutHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetTimeoutHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the GetTimeout method with PropertyAccessException.
     */
    @Test
    public void testGetTimeoutPropertyException()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    throw new PropertyAccessException("Failed to retrieve property.");
                }
            };

            int iTimeout = oHelper.getTimeout();
            assertEquals("getTimeout failed: ", 0, iTimeout);
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetTimeoutPropertyException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetTimeoutPropertyException test: " + t.getMessage());
        }
    }

    /**
     * Test the GetTimeout method with NumberFormatException.
     */
    @Test
    public void testGetTimeoutNumberFormatException()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    return "A";
                }
            };

            int iTimeout = oHelper.getTimeout();
            assertEquals("getTimeout failed: ", 0, iTimeout);
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetTimeoutNumberFormatException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetTimeoutNumberFormatException test: " + t.getMessage());
        }
    }

    /**
     * Test the initializePort no assertion class method happy path.
     */
    @Test
    public void testInitializePortNoAssertionHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(1).of(mockLog).warn(with(any(String.class)));
                    exactly(4).of(mockLog).info(with(any(String.class)));
                    exactly(3).of(mockLog).debug(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    return "5";
                }

                @Override
                protected Map getRequestContextFromPort(BindingProvider port)
                {
                    oMap.clear();
                    oMap.put("javax.xml.ws.service.endpoint.address", "http://www.someurlold.com");
                    return oMap;
                }
            };

            oHelper.initializePort(mockPort, "http://www.someurlnew.com");
            assertEquals("Failed to fill map.", 3, oMap.size());
            assertTrue("RequestContext Failed to have key: javax.xml.ws.service.endpoint.address", oMap.containsKey("javax.xml.ws.service.endpoint.address"));
            assertEquals("RequestContext failed to have correct value for key: javax.xml.ws.service.endpoint.address", "http://www.someurlnew.com", (String) oMap.get("javax.xml.ws.service.endpoint.address"));
            assertTrue("RequestContext Failed to have key: com.sun.xml.ws.connect.timeout", oMap.containsKey("com.sun.xml.ws.connect.timeout"));
            assertEquals("RequestContext failed to have correct value for key: com.sun.xml.ws.connect.timeout", 5, ((Integer) oMap.get("com.sun.xml.ws.connect.timeout")).intValue());
            assertTrue("RequestContext Failed to have key: com.sun.xml.ws.request.timeout", oMap.containsKey("com.sun.xml.ws.request.timeout"));
            assertEquals("RequestContext failed to have correct value for key: com.sun.xml.ws.request.timeout", 5, ((Integer) oMap.get("com.sun.xml.ws.request.timeout")).intValue());
        }
        catch (Throwable t)
        {
            System.out.println("Error running testInitializePortNoAssertionHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInitializePortNoAssertionHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the initializePort with assertion class method happy path.
     */
    @Test
    public void testInitializePortWithAssertionHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(4).of(mockLog).info(with(any(String.class)));
                    exactly(3).of(mockLog).debug(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    return "5";
                }

                @Override
                protected Map getRequestContextFromPort(BindingProvider port)
                {
                    oMap.clear();
                    oMap.put("javax.xml.ws.service.endpoint.address", "http://www.someurlold.com");
                    return oMap;
                }

                @Override
                protected SamlTokenCreator getSamlTokenCreator()
                {
                    return mockTokenCreator;
                }

                @Override
                protected Map createSamlRequestContext(SamlTokenCreator oTokenCreator, AssertionType oAssertion, String sUrl, String sServiceAction)
                {
                    HashMap<String, Object> oMap = new HashMap<String, Object>();
                    oMap.put("TestProp1", "TestValue1");
                    oMap.put("TestProp2", "TestValue2");
                    return oMap;
                }

                @Override
                protected List getWSAddressingHeaders(String url, String wsAddressingAction, AssertionType assertion)
                {
                    List oList = new ArrayList();
                    oList.add(wsAddressingAction);
                    return oList;
                }

                @Override
                protected void setOutboundHeaders(BindingProvider port, List createdHeaders)
                {
                    assertEquals("Failed to initialize headers", 1, createdHeaders.size());
                    assertTrue("Headers failed to contain correct value for WS-Addressing action", "Test-ws-action".equals(createdHeaders.get(0)));
                }
            };

            AssertionType oAssertion = new AssertionType();
            oHelper.initializePort(mockPort, "http://www.someurlnew.com", "service", "Test-ws-action", oAssertion);
            assertEquals("Failed to fill map.", 5, oMap.size());
            assertTrue("RequestContext Failed to have key: javax.xml.ws.service.endpoint.address", oMap.containsKey("javax.xml.ws.service.endpoint.address"));
            assertEquals("RequestContext failed to have correct value for key: javax.xml.ws.service.endpoint.address", "http://www.someurlnew.com", (String) oMap.get("javax.xml.ws.service.endpoint.address"));
            assertTrue("RequestContext Failed to have key: com.sun.xml.ws.connect.timeout", oMap.containsKey("com.sun.xml.ws.connect.timeout"));
            assertEquals("RequestContext failed to have correct value for key: com.sun.xml.ws.connect.timeout", 5, ((Integer) oMap.get("com.sun.xml.ws.connect.timeout")).intValue());
            assertTrue("RequestContext Failed to have key: com.sun.xml.ws.request.timeout", oMap.containsKey("com.sun.xml.ws.request.timeout"));
            assertEquals("RequestContext failed to have correct value for key: com.sun.xml.ws.request.timeout", 5, ((Integer) oMap.get("com.sun.xml.ws.request.timeout")).intValue());
            assertTrue("RequestContext Failed to have key: TestProp1", oMap.containsKey("TestProp1"));
            assertEquals("RequestContext failed to have correct value for key: TestProp1", "TestValue1", (String) oMap.get("TestProp1"));
            assertTrue("RequestContext Failed to have key: TestProp2", oMap.containsKey("TestProp2"));
            assertEquals("RequestContext failed to have correct value for key: TestProp2", "TestValue2", (String) oMap.get("TestProp2"));
        }
        catch (Throwable t)
        {
            System.out.println("Error running testInitializePortWithAssertionHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInitializePortWithAssertionHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the initializePort with assertion class method with empty serviceaction.
     */
    @Test
    public void testInitializePortWithAssertionEmptyServiceAction()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(1).of(mockLog).info(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    return "5";
                }

                @Override
                protected Map getRequestContextFromPort(BindingProvider port)
                {
                    oMap.clear();
                    oMap.put("javax.xml.ws.service.endpoint.address", "http://www.someurlold.com");
                    return oMap;
                }

                @Override
                protected SamlTokenCreator getSamlTokenCreator()
                {
                    return mockTokenCreator;
                }

                @Override
                protected Map createSamlRequestContext(SamlTokenCreator oTokenCreator, AssertionType oAssertion, String sUrl, String sServiceAction)
                {
                    HashMap<String, Object> oMap = new HashMap<String, Object>();
                    oMap.put("TestProp1", "TestValue1");
                    oMap.put("TestProp2", "TestValue2");
                    return oMap;
                }
            };

            AssertionType oAssertion = new AssertionType();
            oHelper.initializePort(mockPort, "http://www.someurlnew.com", "", null, oAssertion);
            assertEquals("Failed to fill map.", 3, oMap.size());
            assertTrue("RequestContext Failed to have key: javax.xml.ws.service.endpoint.address", oMap.containsKey("javax.xml.ws.service.endpoint.address"));
            assertEquals("RequestContext failed to have correct value for key: javax.xml.ws.service.endpoint.address", "http://www.someurlnew.com", (String) oMap.get("javax.xml.ws.service.endpoint.address"));
            assertTrue("RequestContext Failed to have key: com.sun.xml.ws.connect.timeout", oMap.containsKey("com.sun.xml.ws.connect.timeout"));
            assertEquals("RequestContext failed to have correct value for key: com.sun.xml.ws.connect.timeout", 5, ((Integer) oMap.get("com.sun.xml.ws.connect.timeout")).intValue());
            assertTrue("RequestContext Failed to have key: com.sun.xml.ws.request.timeout", oMap.containsKey("com.sun.xml.ws.request.timeout"));
            assertEquals("RequestContext failed to have correct value for key: com.sun.xml.ws.request.timeout", 5, ((Integer) oMap.get("com.sun.xml.ws.request.timeout")).intValue());
        }
        catch (RuntimeException e)
        {
            assertTrue("Incorrect exception: ", e.getMessage().contains("Unable to initialize secure port (serviceAction null)"));
        }
        catch (Throwable t)
        {
            System.out.println("Error running testInitializePortWithAssertionEmptyServiceAction test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInitializePortWithAssertionEmptyServiceAction test: " + t.getMessage());
        }
    }

    /**
     * Test the initializePort with assertion class method with null serviceaction.
     */
    @Test
    public void testInitializePortWithAssertionNullServiceAction()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(1).of(mockLog).info(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    return "5";
                }

                @Override
                protected Map getRequestContextFromPort(BindingProvider port)
                {
                    oMap.clear();
                    oMap.put("javax.xml.ws.service.endpoint.address", "http://www.someurlold.com");
                    return oMap;
                }

                @Override
                protected SamlTokenCreator getSamlTokenCreator()
                {
                    return mockTokenCreator;
                }

                @Override
                protected Map createSamlRequestContext(SamlTokenCreator oTokenCreator, AssertionType oAssertion, String sUrl, String sServiceAction)
                {
                    HashMap<String, Object> oMap = new HashMap<String, Object>();
                    oMap.put("TestProp1", "TestValue1");
                    oMap.put("TestProp2", "TestValue2");
                    return oMap;
                }
            };

            AssertionType oAssertion = new AssertionType();
            oHelper.initializePort(mockPort, "http://www.someurlnew.com", null, null, oAssertion);
            assertEquals("Failed to fill map.", 3, oMap.size());
            assertTrue("RequestContext Failed to have key: javax.xml.ws.service.endpoint.address", oMap.containsKey("javax.xml.ws.service.endpoint.address"));
            assertEquals("RequestContext failed to have correct value for key: javax.xml.ws.service.endpoint.address", "http://www.someurlnew.com", (String) oMap.get("javax.xml.ws.service.endpoint.address"));
            assertTrue("RequestContext Failed to have key: com.sun.xml.ws.connect.timeout", oMap.containsKey("com.sun.xml.ws.connect.timeout"));
            assertEquals("RequestContext failed to have correct value for key: com.sun.xml.ws.connect.timeout", 5, ((Integer) oMap.get("com.sun.xml.ws.connect.timeout")).intValue());
            assertTrue("RequestContext Failed to have key: com.sun.xml.ws.request.timeout", oMap.containsKey("com.sun.xml.ws.request.timeout"));
            assertEquals("RequestContext failed to have correct value for key: com.sun.xml.ws.request.timeout", 5, ((Integer) oMap.get("com.sun.xml.ws.request.timeout")).intValue());
        }
        catch (RuntimeException e)
        {
            assertTrue("Incorrect exception: ", e.getMessage().contains("Unable to initialize secure port (serviceAction null)"));
        }
        catch (Throwable t)
        {
            System.out.println("Error running testInitializePortWithAssertionNullServiceAction test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInitializePortWithAssertionNullServiceAction test: " + t.getMessage());
        }
    }

    /**
     * Test the initializePort with assertion class method with null port.
     */
    @Test
    public void testInitializePortWithAssertionNullPort()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(1).of(mockLog).info(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    return "5";
                }

                @Override
                protected Map getRequestContextFromPort(BindingProvider port)
                {
                    oMap.clear();
                    oMap.put("javax.xml.ws.service.endpoint.address", "http://www.someurlold.com");
                    return oMap;
                }

                @Override
                protected SamlTokenCreator getSamlTokenCreator()
                {
                    return mockTokenCreator;
                }

                @Override
                protected Map createSamlRequestContext(SamlTokenCreator oTokenCreator, AssertionType oAssertion, String sUrl, String sServiceAction)
                {
                    HashMap<String, Object> oMap = new HashMap<String, Object>();
                    oMap.put("TestProp1", "TestValue1");
                    oMap.put("TestProp2", "TestValue2");
                    return oMap;
                }
            };

            AssertionType oAssertion = new AssertionType();
            oHelper.initializePort(null, "http://www.someurlnew.com", "service", null, oAssertion);
            fail("We should have had an exception here and we did not.");
        }
        catch (RuntimeException re)
        {
            assertEquals("Incorrect exception thrown.", "Unable to initialize port (port null)", re.getMessage());
        }
        catch (Throwable t)
        {
            System.out.println("Error running testInitializePortWithAssertionNullPort test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInitializePortWithAssertionNullPort test: " + t.getMessage());
        }
    }

    /**
     * Test the initializePort with assertion class method with nullish URL.
     */
    @Test
    public void testInitializePortWithAssertionNullURL()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(2).of(mockLog).info(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    return "5";
                }

                @Override
                protected Map getRequestContextFromPort(BindingProvider port)
                {
                    oMap.clear();
                    oMap.put("javax.xml.ws.service.endpoint.address", "http://www.someurlold.com");
                    return oMap;
                }

                @Override
                protected SamlTokenCreator getSamlTokenCreator()
                {
                    return mockTokenCreator;
                }

                @Override
                protected Map createSamlRequestContext(SamlTokenCreator oTokenCreator, AssertionType oAssertion, String sUrl, String sServiceAction)
                {
                    HashMap<String, Object> oMap = new HashMap<String, Object>();
                    oMap.put("TestProp1", "TestValue1");
                    oMap.put("TestProp2", "TestValue2");
                    return oMap;
                }
            };

            try
            {
                AssertionType oAssertion = new AssertionType();
                oHelper.initializePort(mockPort, null, "service", null, oAssertion);
                fail("We should have had an exception here and we did not.");
            }
            catch (RuntimeException re)
            {
                assertEquals("Incorrect exception thrown.", "Unable to initialize port (url null)", re.getMessage());
            }

            try
            {
                AssertionType oAssertion = new AssertionType();
                oHelper.initializePort(mockPort, "", "service", null, oAssertion);
                fail("We should have had an exception here and we did not.");
            }
            catch (RuntimeException re)
            {
                assertEquals("Incorrect exception thrown.", "Unable to initialize port (url null)", re.getMessage());
            }

        }
        catch (Throwable t)
        {
            System.out.println("Error running testInitializePortWithAssertionNullURL test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInitializePortWithAssertionNullURL test: " + t.getMessage());
        }
    }

    /**
     * Test the initializePort with assertion class method with nullish RequestContext.
     */
    @Test
    public void testInitializePortWithAssertionNullRequestContext()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(1).of(mockLog).info(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    return "5";
                }

                @Override
                protected Map getRequestContextFromPort(BindingProvider port)
                {
                    return null;
                }

                @Override
                protected SamlTokenCreator getSamlTokenCreator()
                {
                    return mockTokenCreator;
                }

                @Override
                protected Map createSamlRequestContext(SamlTokenCreator oTokenCreator, AssertionType oAssertion, String sUrl, String sServiceAction)
                {
                    HashMap<String, Object> oMap = new HashMap<String, Object>();
                    oMap.put("TestProp1", "TestValue1");
                    oMap.put("TestProp2", "TestValue2");
                    return oMap;
                }
            };

            try
            {
                AssertionType oAssertion = new AssertionType();
                oHelper.initializePort(mockPort, "http://www.someurl.com", "service", null, oAssertion);
                fail("We should have had an exception here and we did not.");
            }
            catch (RuntimeException re)
            {
                assertEquals("Incorrect exception thrown.", "Unable to retrieve request context from the port.", re.getMessage());
            }
        }
        catch (Throwable t)
        {
            System.out.println("Error running testInitializePortWithAssertionNullRequestContext test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInitializePortWithAssertionNullRequestContext test: " + t.getMessage());
        }
    }

    /**
     * Test the initializePort no assertion class method with empty request context.
     */
    @Test
    public void testInitializePortNoAssertionEmptyRequestContext()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(1).of(mockLog).warn(with(any(String.class)));
                    exactly(3).of(mockLog).info(with(any(String.class)));
                    exactly(3).of(mockLog).debug(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    return "5";
                }

                @Override
                protected Map getRequestContextFromPort(BindingProvider port)
                {
                    oMap.clear();
                    return oMap;
                }
            };

            oHelper.initializePort(mockPort, "http://www.someurlnew.com");
            assertEquals("Failed to fill map.", 3, oMap.size());
            assertTrue("RequestContext Failed to have key: javax.xml.ws.service.endpoint.address", oMap.containsKey("javax.xml.ws.service.endpoint.address"));
            assertEquals("RequestContext failed to have correct value for key: javax.xml.ws.service.endpoint.address", "http://www.someurlnew.com", (String) oMap.get("javax.xml.ws.service.endpoint.address"));
            assertTrue("RequestContext Failed to have key: com.sun.xml.ws.connect.timeout", oMap.containsKey("com.sun.xml.ws.connect.timeout"));
            assertEquals("RequestContext failed to have correct value for key: com.sun.xml.ws.connect.timeout", 5, ((Integer) oMap.get("com.sun.xml.ws.connect.timeout")).intValue());
            assertTrue("RequestContext Failed to have key: com.sun.xml.ws.request.timeout", oMap.containsKey("com.sun.xml.ws.request.timeout"));
            assertEquals("RequestContext failed to have correct value for key: com.sun.xml.ws.request.timeout", 5, ((Integer) oMap.get("com.sun.xml.ws.request.timeout")).intValue());
        }
        catch (Throwable t)
        {
            System.out.println("Error running testInitializePortNoAssertionEmptyRequestContext test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInitializePortNoAssertionEmptyRequestContext test: " + t.getMessage());
        }
    }

    /**
     * Test the initializePort no assertion class method with 0 timeout.
     */
    @Test
    public void testInitializePortNoAssertionZeroTimeout()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(3).of(mockLog).info(with(any(String.class)));
                    exactly(2).of(mockLog).debug(with(any(String.class)));
                    exactly(2).of(mockLog).warn(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getGatewayProperty(String sKey)
                        throws PropertyAccessException
                {
                    return "0";
                }

                @Override
                protected Map getRequestContextFromPort(BindingProvider port)
                {
                    oMap.clear();
                    return oMap;
                }
            };

            oHelper.initializePort(mockPort, "http://www.someurlnew.com");
            assertEquals("Failed to fill map.", 1, oMap.size());
            assertTrue("RequestContext Failed to have key: javax.xml.ws.service.endpoint.address", oMap.containsKey("javax.xml.ws.service.endpoint.address"));
            assertEquals("RequestContext failed to have correct value for key: javax.xml.ws.service.endpoint.address", "http://www.someurlnew.com", (String) oMap.get("javax.xml.ws.service.endpoint.address"));
        }
        catch (Throwable t)
        {
            System.out.println("Error running testInitializePortNoAssertionZeroTimeout test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInitializePortNoAssertionZeroTimeout test: " + t.getMessage());
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
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };

            Method oMethod = oHelper.getMethod(this.getClass(), "helperMethod");
            assertNotNull("getMethod failed", oMethod);
            assertEquals("Incorrect method returned.", "helperMethod", oMethod.getName());
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetMethod test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetMethod test: " + t.getMessage());
        }
    }

    /**
     * Test the invokePort method happy path.
     */
    @Test
    public void testInvokePortHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(4).of(mockLog).debug(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                public int getRetryAttempts()
                {
                    return 3;
                }

                @Override
                public int getRetryDelay()
                {
                    return 10;
                }

                @Override
                public String getExceptionText()
                {
                    return "SocketTimeoutException";
                }

                @Override
                protected Object invokeTheMethod(Method oMethod, Object portObject, Object operationInput)
                {
                    return new Integer(100);
                }
            };

            Integer oResponse = (Integer) oHelper.invokePort(this, this.getClass(), "helperMethod", new Integer(100));
            assertNotNull("invokePort failed to return a value.", oResponse);
            assertTrue("Response was incorrect type.", oResponse instanceof Integer);
            assertEquals("Incorrect value returned.", 100, oResponse.intValue());
        }
        catch (Throwable t)
        {
            System.out.println("Error running testInvokePortHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInvokePortHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the invokePort method illegal argument exception.
     */
    @Test
    public void testInvokePortIllegalArgumentException()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(3).of(mockLog).debug(with(any(String.class)));
                    exactly(1).of(mockLog).error(with(any(String.class)), with(any(IllegalArgumentException.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                public int getRetryAttempts()
                {
                    return 3;
                }

                @Override
                public int getRetryDelay()
                {
                    return 10;
                }

                @Override
                public String getExceptionText()
                {
                    return "SocketTimeoutException";
                }
            };

            Integer oResponse = (Integer) oHelper.invokePort(this, this.getClass(), "helperMethod2", new Integer(100));
            assertNull("invokePort should have returned a null response.", oResponse);
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
        catch (Throwable t)
        {
            System.out.println("Error running testInvokePortIllegalArgumentException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInvokePortIllegalArgumentException test: " + t.getMessage());
        }
    }

    /**
     * Test the invokePort method with retry settings with exception.
     */
    @Test
    public void testInvokePortWithInvocationTargetException()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(2).of(mockLog).debug(with(any(String.class)));
                    exactly(1).of(mockLog).error(with(any(String.class)), with(any(WebServiceException.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                public int getRetryAttempts()
                {
                    return 0;
                }

                @Override
                public int getRetryDelay()
                {
                    return 0;
                }

                @Override
                public String getExceptionText()
                {
                    return "SocketTimeoutException";
                }

                @Override
                protected Object invokeTheMethod(Method oMethod, Object portObject, Object operationInput)
                        throws InvocationTargetException
                {
                    throw new InvocationTargetException(new Throwable(""), "Some bad argument.");
                }
            };

            Integer oResponse = (Integer) oHelper.invokePort(this, this.getClass(), "helperMethod", new Integer(100));
            assertNotNull("invokePort failed to return a value.", oResponse);
            assertTrue("Response was incorrect type.", oResponse instanceof Integer);
            assertEquals("Incorrect value returned.", 100, oResponse.intValue());
        }
        catch (InvocationTargetException e)
        {
            assertEquals("Incorrect exception was returned.", "Some bad argument.", e.getMessage());
        }
        catch (Throwable t)
        {
            System.out.println("Error running testInvokePortWithInvocationTargetException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInvokePortWithInvocationTargetException test: " + t.getMessage());
        }
    }

    /**
     * Test the invokePort method with retry settings happy path.
     */
    @Test
    public void testInvokePortRetrySettingsHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(4).of(mockLog).debug(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                public int getRetryAttempts()
                {
                    return 5;
                }

                @Override
                public int getRetryDelay()
                {
                    return 100;
                }

                @Override
                public String getExceptionText()
                {
                    return "javax.xml.ws.WebServiceException";
                }
            };

            Integer oResponse = (Integer) oHelper.invokePort(this, this.getClass(), "helperMethod", new Integer(100));
            assertNotNull("invokePort failed to return a value.", oResponse);
            assertTrue("Response was incorrect type.", oResponse instanceof Integer);
            assertEquals("Incorrect value returned.", 100, oResponse.intValue());
        }
        catch (Throwable t)
        {
            System.out.println("Error running testInvokePortHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInvokePortHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the invokePort method with retry settings with exception.
     */
    @Test
    public void testInvokePortRetrySettingsWithWebServiceException()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(7).of(mockLog).debug(with(any(String.class)));
                    exactly(2).of(mockLog).warn(with(any(String.class)));
                    exactly(2).of(mockLog).info(with(any(String.class)));
                    exactly(1).of(mockLog).error(with(any(String.class)), with(any(WebServiceException.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                public int getRetryAttempts()
                {
                    return 3;
                }

                @Override
                public int getRetryDelay()
                {
                    return 10;
                }

                @Override
                public String getExceptionText()
                {
                    return "SocketTimeoutException";
                }

                @Override
                protected Object invokeTheMethod(Method oMethod, Object portObject, Object operationInput)
                {
                    throw new WebServiceException("SocketTimeoutException");
                }
            };

            Integer oResponse = (Integer) oHelper.invokePort(this, this.getClass(), "helperMethod", new Integer(100));
            assertNotNull("invokePort failed to return a value.", oResponse);
            assertTrue("Response was incorrect type.", oResponse instanceof Integer);
            assertEquals("Incorrect value returned.", 100, oResponse.intValue());
        }
        catch (WebServiceException e)
        {
            assertEquals("Incorrect exception was returned.", "SocketTimeoutException", e.getMessage());
        }
        catch (Throwable t)
        {
            System.out.println("Error running testInvokePortRetrySettingsWithWebServiceException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInvokePortRetrySettingsWithWebServiceException test: " + t.getMessage());
        }
    }

    /**
     * Test the invokePort method with retry settings with exception.
     */
    @Test
    public void testInvokePortRetrySettingsWithWebServiceExceptionNoTextMatch()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(3).of(mockLog).debug(with(any(String.class)));
                    exactly(1).of(mockLog).error(with(any(String.class)), with(any(WebServiceException.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                public int getRetryAttempts()
                {
                    return 3;
                }

                @Override
                public int getRetryDelay()
                {
                    return 10;
                }

                @Override
                public String getExceptionText()
                {
                    return "SocketTimeoutException";
                }

                @Override
                protected Object invokeTheMethod(Method oMethod, Object portObject, Object operationInput)
                {
                    throw new WebServiceException("SomethingElse");
                }
            };

            Integer oResponse = (Integer) oHelper.invokePort(this, this.getClass(), "helperMethod", new Integer(100));
            assertNotNull("invokePort failed to return a value.", oResponse);
            assertTrue("Response was incorrect type.", oResponse instanceof Integer);
            assertEquals("Incorrect value returned.", 100, oResponse.intValue());
        }
        catch (WebServiceException e)
        {
            assertEquals("Incorrect exception was returned.", "SomethingElse", e.getMessage());
        }
        catch (Throwable t)
        {
            System.out.println("Error running testInvokePortRetrySettingsWithWebServiceExceptionNoTextMatch test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInvokePortRetrySettingsWithWebServiceExceptionNoTextMatch test: " + t.getMessage());
        }
    }

    /**
     * Test the invokePort method with retry settings with exception.
     */
    @Test
    public void testInvokePortRetrySettingsWithIllegalArgumentException()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(3).of(mockLog).debug(with(any(String.class)));
                    exactly(1).of(mockLog).error(with(any(String.class)), with(any(WebServiceException.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                public int getRetryAttempts()
                {
                    return 3;
                }

                @Override
                public int getRetryDelay()
                {
                    return 10;
                }

                @Override
                public String getExceptionText()
                {
                    return "SocketTimeoutException";
                }

                @Override
                protected Object invokeTheMethod(Method oMethod, Object portObject, Object operationInput)
                {
                    throw new IllegalArgumentException("Some bad argument.");
                }
            };

            Integer oResponse = (Integer) oHelper.invokePort(this, this.getClass(), "helperMethod", new Integer(100));
            assertNotNull("invokePort failed to return a value.", oResponse);
            assertTrue("Response was incorrect type.", oResponse instanceof Integer);
            assertEquals("Incorrect value returned.", 100, oResponse.intValue());
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Incorrect exception was returned.", "Some bad argument.", e.getMessage());
        }
        catch (Throwable t)
        {
            System.out.println("Error running testInvokePortRetrySettingsWithIllegalArgumentException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInvokePortRetrySettingsWithIllegalArgumentException test: " + t.getMessage());
        }
    }

    /**
     * Test the invokePort method with retry settings with exception.
     */
    @Test
    public void testInvokePortRetrySettingsWithInvocationTargetException()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(3).of(mockLog).debug(with(any(String.class)));
                    exactly(1).of(mockLog).error(with(any(String.class)), with(any(WebServiceException.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                public int getRetryAttempts()
                {
                    return 3;
                }

                @Override
                public int getRetryDelay()
                {
                    return 10;
                }

                @Override
                public String getExceptionText()
                {
                    return "SocketTimeoutException";
                }

                @Override
                protected Object invokeTheMethod(Method oMethod, Object portObject, Object operationInput)
                        throws InvocationTargetException
                {
                    throw new InvocationTargetException(new Throwable(""), "Some bad argument.");
                }
            };

            Integer oResponse = (Integer) oHelper.invokePort(this, this.getClass(), "helperMethod", new Integer(100));
            assertNotNull("invokePort failed to return a value.", oResponse);
            assertTrue("Response was incorrect type.", oResponse instanceof Integer);
            assertEquals("Incorrect value returned.", 100, oResponse.intValue());
        }
        catch (InvocationTargetException e)
        {
            assertEquals("Incorrect exception was returned.", "Some bad argument.", e.getMessage());
        }
        catch (Throwable t)
        {
            System.out.println("Error running testInvokePortRetrySettingsWithInvocationTargetException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInvokePortRetrySettingsWithInvocationTargetException test: " + t.getMessage());
        }
    }

    /**
     * Test the getUrlFromTargetSystem method happy path.
     */
    @Test
    public void testGetUrlFromTargetSystemHappyPath()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(3).of(mockLog).info(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getEndPointFromConnectionManager(NhinTargetSystemType oTargetSystem, String sServiceName)
                {
                    return "http://www.theurl.com";
                }
            };
            NhinTargetSystemType oTargetSystem = new NhinTargetSystemType();
            HomeCommunityType oHomeCommunity = new HomeCommunityType();
            oTargetSystem.setHomeCommunity(oHomeCommunity);
            oHomeCommunity.setHomeCommunityId("1.1");
            oHomeCommunity.setName("The name");
            oHomeCommunity.setDescription("The name");
            String sURL = oHelper.getUrlFromTargetSystem(oTargetSystem, NhincConstants.DOC_QUERY_SERVICE_NAME);
            assertEquals("URL was incorrect.", "http://www.theurl.com", sURL);
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetUrlFromTargetSystemHappyPath test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetUrlFromTargetSystemHappyPath test: " + t.getMessage());
        }
    }

    /**
     * Test the getUrlFromTargetSystem method null target system.
     */
    @Test
    public void testGetUrlFromTargetSystemNullTargetSystem()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(1).of(mockLog).error(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getEndPointFromConnectionManager(NhinTargetSystemType oTargetSystem, String sServiceName)
                {
                    return "http://www.theurl.com";
                }
            };
            String sURL = oHelper.getUrlFromTargetSystem(null, NhincConstants.DOC_QUERY_SERVICE_NAME);
            fail("An exception should have been thrown.");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Unexpected exception message.", "Target system passed into the proxy is null", e.getMessage());
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetUrlFromTargetSystemNullTargetSystem test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetUrlFromTargetSystemNullTargetSystem test: " + t.getMessage());
        }
    }

    /**
     * Test the getUrlFromTargetSystem method with ConnectionManagerException.
     */
    @Test
    public void testGetUrlFromTargetSystemConnectionManagerException()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(3).of(mockLog).info(with(any(String.class)));
                    exactly(1).of(mockLog).error(with(any(String.class)), with(any(Exception.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getEndPointFromConnectionManager(NhinTargetSystemType oTargetSystem, String sServiceName)
                        throws ConnectionManagerException
                {
                    throw new ConnectionManagerException("This is a forced exception");
                }
            };
            NhinTargetSystemType oTargetSystem = new NhinTargetSystemType();
            HomeCommunityType oHomeCommunity = new HomeCommunityType();
            oTargetSystem.setHomeCommunity(oHomeCommunity);
            oHomeCommunity.setHomeCommunityId("1.1");
            oHomeCommunity.setName("The name");
            oHomeCommunity.setDescription("The name");
            String sURL = oHelper.getUrlFromTargetSystem(oTargetSystem, NhincConstants.DOC_QUERY_SERVICE_NAME);
            fail("An exception should have been thrown.");
        }
        catch (ConnectionManagerException e)
        {
            assertEquals("Unexpected exception message.", "This is a forced exception", e.getMessage());
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetUrlFromTargetSystemConnectionManagerException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetUrlFromTargetSystemConnectionManagerException test: " + t.getMessage());
        }
    }

    /**
     * Test the getUrlFromHomeCommunity method happy path.
     */
    @Test
    public void testGetUrlFromHomeCommunity()
    {
        try
        {
            context.checking(new Expectations()
            {


                {
                    exactly(1).of(mockLog).info(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getEndPointFromConnectionManager(String sHomeCommunityId, String sServiceName)
                {
                    return "http://www.theurl.com";
                }
            };
            String sHomeCommunityId = "1.1";
            String sURL = oHelper.getUrlFromHomeCommunity(sHomeCommunityId, NhincConstants.DOC_QUERY_SERVICE_NAME);
            assertEquals("URL was incorrect.", "http://www.theurl.com", sURL);
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetUrlFromHomeCommunity test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetUrlFromHomeCommunity test: " + t.getMessage());
        }
    }

    /**
     * Test the getUrlFromHomeCommunity method null homeCommunityId.
     */
    @Test
    public void testGetUrlFromHomeCommunityNullId()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockLog).error(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getEndPointFromConnectionManager(String sHomeCommunityId, String sServiceName)
                {
                    return "http://www.theurl.com";
                }
            };
            String sHomeCommunityId = null;
            String sURL = oHelper.getUrlFromHomeCommunity(sHomeCommunityId, NhincConstants.DOC_QUERY_SERVICE_NAME);
        }
        catch (IllegalArgumentException e)
        {
            assertTrue("Invalid exception message: ", e.getMessage().contains("Home community passed into the WebServiceProxyHelper is null or empty"));
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetUrlFromHomeCommunityNullId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetUrlFromHomeCommunityNullId test: " + t.getMessage());
        }
    }

    /**
     * Test the getUrlFromHomeCommunity method empty homeCommunityId.
     */
    @Test
    public void testGetUrlFromHomeCommunityEmptyId()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockLog).error(with(any(String.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getEndPointFromConnectionManager(String sHomeCommunityId, String sServiceName)
                {
                    return "http://www.theurl.com";
                }
            };
            String sHomeCommunityId = "";
            String sURL = oHelper.getUrlFromHomeCommunity(sHomeCommunityId, NhincConstants.DOC_QUERY_SERVICE_NAME);
        }
        catch (IllegalArgumentException e)
        {
            assertTrue("Invalid exception message: ", e.getMessage().contains("Home community passed into the WebServiceProxyHelper is null or empty"));
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetUrlFromHomeCommunityEmptyId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetUrlFromHomeCommunityEmptyId test: " + t.getMessage());
        }
    }

    /**
     * Test the getUrlFromHomeCommunity method ConnectionManagerException.
     */
    @Test
    public void testGetUrlFromHomeCommunityConnectionManagerException()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockLog).info(with(any(String.class)));
                    exactly(1).of(mockLog).error(with(any(String.class)), with(any(ConnectionManagerException.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getEndPointFromConnectionManager(String sHomeCommunityId, String sServiceName)
                    throws ConnectionManagerException
                {
                    throw new ConnectionManagerException("Call failed.");
                }
            };
            String sHomeCommunityId = "1.1";
            String sURL = oHelper.getUrlFromHomeCommunity(sHomeCommunityId, NhincConstants.DOC_QUERY_SERVICE_NAME);
        }
        catch (ConnectionManagerException e)
        {
            assertTrue("Invalid exception message: ", e.getMessage().contains("Call failed."));
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetUrlFromHomeCommunityConnectionManagerException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetUrlFromHomeCommunityConnectionManagerException test: " + t.getMessage());
        }
    }

    /**
     * Test the getUrlLocalHomeCommunity method happy path.
     */
    @Test
    public void testGetUrlLocalHomeCommunity()
    {
        try
        {
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getLocalEndPointFromConnectionManager(String sServiceName)
                {
                    return "http://www.theurl.com";
                }
            };
            String sURL = oHelper.getUrlLocalHomeCommunity(NhincConstants.DOC_QUERY_SERVICE_NAME);
            assertEquals("URL was incorrect.", "http://www.theurl.com", sURL);
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetUrlLocalHomeCommunity test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetUrlLocalHomeCommunity test: " + t.getMessage());
        }
    }

    /**
     * Test the getUrlLocalHomeCommunity method ConnectionManagerException.
     */
    @Test
    public void testGetUrlLocalHomeCommunityConnectionManagerException()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockLog).error(with(any(String.class)), with(any(ConnectionManagerException.class)));
                }
            });
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
            {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected String getLocalEndPointFromConnectionManager(String sServiceName)
                    throws ConnectionManagerException
                {
                    throw new ConnectionManagerException("Call failed.");
                }
            };
            String sURL = oHelper.getUrlLocalHomeCommunity(NhincConstants.DOC_QUERY_SERVICE_NAME);
        }
        catch (ConnectionManagerException e)
        {
            assertTrue("Invalid exception message: ", e.getMessage().contains("Call failed."));
        }
        catch (Throwable t)
        {
            System.out.println("Error running testGetUrlLocalHomeCommunityConnectionManagerException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetUrlLocalHomeCommunityConnectionManagerException test: " + t.getMessage());
        }
    }


    /**
     * Tests the getMessageId method - Happy Path
     */
    @Test
    public void testGetMessageIdHappyPath()
    {

        WebServiceProxyHelper oHelper = new WebServiceProxyHelper();
        AssertionType oAssertion = new AssertionType()
        {

            @Override
            public String getMessageId()
            {
                return "Test_Message_Id";
            }
        };
        String messageId = oHelper.getMessageId(oAssertion);
        assertEquals("Test_Message_Id", messageId);
    }

    /**
     * Tests the getMessageId method - Null Assertion
     */
    @Test
    public void testGetMessageIdNullAssertion()
    {

        context.checking(new Expectations()
        {
            {
                allowing(mockLog).warn(with(any(String.class)));
            }
        });
        WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
        {

            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
        };
        String messageId = oHelper.getMessageId(null);
        assertNotNull("messageId", messageId);
        assertTrue("messageId was empty", messageId.length() > 0);
    }

    /**
     * Tests the getMessageId method - Null AsyncMessageId
     */
    @Test
    public void testGetMessageIdNullAsyncMessageId()
    {

        context.checking(new Expectations()
        {
            {
                allowing(mockLog).warn(with(any(String.class)));
            }
        });
        WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
        {

            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
        };
        AssertionType oAssertion = new AssertionType();
        oAssertion.setMessageId(null);
        String messageId = oHelper.getMessageId(oAssertion);
        assertNotNull("messageId", messageId);
        assertTrue("messageId was empty", messageId.length() > 0);
    }

    /**
     * Tests the getMessageId method - Empty AsyncMessageId
     */
    @Test
    public void testGetMessageIdEmptyAsyncMessageId()
    {

        context.checking(new Expectations()
        {
            {
                allowing(mockLog).warn(with(any(String.class)));
            }
        });
        WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
        {

            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
        };
        AssertionType oAssertion = new AssertionType();
        oAssertion.setMessageId("");
        String messageId = oHelper.getMessageId(oAssertion);
        assertNotNull("messageId", messageId);
        assertTrue("messageId was empty", messageId.length() > 0);
    }

    /**
     * Tests the getRelatesTo method
     * TODO - this will change with implementation
     */
    @Test
    public void testGetRelatesTo()
    {

        WebServiceProxyHelper oHelper = new WebServiceProxyHelper();
        AssertionType oAssertion = new AssertionType();
        List returnedRelatesTo = oHelper.getRelatesTo(oAssertion);
        assertNotNull("Returned relatesTo list is null", returnedRelatesTo);
        assertEquals("Expects Empty list at this time", 0, returnedRelatesTo.size());
    }

    /**
     * Tests the getWSAddressing method
     */
    @Test
    public void testGetWSAddressingHeaders()
    {

        WebServiceProxyHelper oHelper = new WebServiceProxyHelper()
        {

            @Override
            protected Log createLogger()
            {
                return mockLog;
            }

            @Override
            protected AsyncHeaderCreator getAsyncHeaderCreator()
            {
                return new AsyncHeaderCreator()
                {

                    @Override
                    public List createOutboundHeaders(String url, String action,
                            String messageId, List<String> relatesToIds)
                    {

                        List headers = new ArrayList();
                        headers.add(url);
                        headers.add(action);
                        headers.add(messageId);
                        headers.addAll(relatesToIds);
                        return headers;
                    }
                };
            }

            @Override
            protected String getMessageId(AssertionType assertion)
            {
                return "Test_Message_Id";
            }

            @Override
            protected List<String> getRelatesTo(AssertionType assertion)
            {
                List<String> allRelatesTo = new ArrayList();
                allRelatesTo.add("Test_Relates_1");
                allRelatesTo.add("Test_Relates_2");
                return allRelatesTo;
            }
        };

        AssertionType oAssertion = new AssertionType();
        List returnedHeaders = oHelper.getWSAddressingHeaders("Test_URL", "Test_ws_action", oAssertion);
        assertEquals("Number of created Headers is invalid.", 5, returnedHeaders.size());
        assertTrue("Test_URL header not found", returnedHeaders.contains("Test_URL"));
        assertTrue("Test_ws_action header not found", returnedHeaders.contains("Test_ws_action"));
        assertTrue("Test_Message_Id header not found", returnedHeaders.contains("Test_Message_Id"));
        assertTrue("Test_Relates_1 header not found", returnedHeaders.contains("Test_Relates_1"));
        assertTrue("Test_Relates_2 header not found", returnedHeaders.contains("Test_Relates_2"));
    }
    // The following commented tests came from ServiceUtilTest.java when the methods
    // from ServiceUtil were moved into the WebServiceProxyHelper.java class.  They
    // were commented out there and so they are commented out here.
    //-------------------------------------------------------------------------------
//
//    final Log mockLog = context.mock(Log.class);
//    final Service mockService = context.mock(Service.class);
//
//    @Test
//    public void testCreateLogger()
//    {
//        try
//        {
//            WebServiceProxyHelper oWebServiceProxyHelper = new WebServiceProxyHelper()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//            };
//            Log log = oWebServiceProxyHelper.createLogger();
//            assertNotNull("Log was null", log);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testCreateLogger test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testCreateLogger test: " + t.getMessage());
//        }
//    }
//
//    @Test
//    public void testGetWsdlPath()
//    {
//        try
//        {
//            WebServiceProxyHelper oWebServiceProxyHelper = new WebServiceProxyHelper()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//                @Override
//                protected String getWsdlPath() throws PropertyAccessException
//                {
//                    return "WSDL path";
//                }
//            };
//            String wsdlPath = oWebServiceProxyHelper.getWsdlPath();
//            assertNotNull("WSDL path was null", wsdlPath);
//            assertEquals("WSDL path incorrect", "WSDL path", wsdlPath);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testCreateLogger test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testCreateLogger test: " + t.getMessage());
//        }
//    }
//
//    @Test
//    public void testConstructService()
//    {
//        try
//        {
//            WebServiceProxyHelper oWebServiceProxyHelper = new WebServiceProxyHelper()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//                @Override
//                protected Service constructService(String wsdlURL, String namespaceURI, String serviceLocalPart) throws MalformedURLException
//                {
//                    return mockService;
//                }
//            };
//            String wsdlUrl = "fake.url";
//            String namespaceURI = "fake:namespace:uri";
//            String serviceLocalPart = "FakeServiceName";
//
//            Service service = oWebServiceProxyHelper.constructService(wsdlUrl, namespaceURI, serviceLocalPart);
//            assertNotNull("Service was null", service);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testConstructService test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testConstructService test: " + t.getMessage());
//        }
//    }
//
//    @Test
//    public void testCreateServiceHappy()
//    {
//        try
//        {
//            WebServiceProxyHelper oWebServiceProxyHelper = new WebServiceProxyHelper()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//                @Override
//                protected String getWsdlPath() throws PropertyAccessException
//                {
//                    return "file:C:\\WSDL\\path\\";
//                }
//                @Override
//                protected Service constructService(String wsdlURL, String namespaceURI, String serviceLocalPart) throws MalformedURLException
//                {
//                    return mockService;
//                }
//            };
//            context.checking(new Expectations()
//            {
//                {
//                    allowing(mockLog).debug(with(any(String.class)));
//                }
//            });
//
//            String wsdlFile = "fake.wsdl";
//            String namespaceURI = "fake:namespace:uri";
//            String serviceLocalPart = "FakeServiceName";
//
//            Service createdService = oWebServiceProxyHelper.createService(wsdlFile, namespaceURI, serviceLocalPart);
//            assertNotNull("Service was null", createdService);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testCreateServiceHappy test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testCreateServiceHappy test: " + t.getMessage());
//        }
//    }
//
//    @Test
//    public void testCreateServiceNullWsdlFile()
//    {
//        try
//        {
//            WebServiceProxyHelper oWebServiceProxyHelper = new WebServiceProxyHelper()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//                @Override
//                protected String getWsdlPath() throws PropertyAccessException
//                {
//                    return "file:C:\\WSDL\\path\\";
//                }
//                @Override
//                protected Service constructService(String wsdlURL, String namespaceURI, String serviceLocalPart) throws MalformedURLException
//                {
//                    return mockService;
//                }
//            };
//            context.checking(new Expectations()
//            {
//                {
//                    allowing(mockLog).debug(with(any(String.class)));
//                    oneOf(mockLog).error("WSDL file name is required.");
//                }
//            });
//
//            String wsdlFile = null;
//            String namespaceURI = "fake:namespace:uri";
//            String serviceLocalPart = "FakeServiceName";
//
//            Service createdService = oWebServiceProxyHelper.createService(wsdlFile, namespaceURI, serviceLocalPart);
//            assertNull("Service was not null", createdService);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testCreateServiceNullWsdlFile test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testCreateServiceNullWsdlFile test: " + t.getMessage());
//        }
//    }
//
//    @Test
//    public void testCreateServiceEmptyWsdlFile()
//    {
//        try
//        {
//            WebServiceProxyHelper oWebServiceProxyHelper = new WebServiceProxyHelper()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//                @Override
//                protected String getWsdlPath() throws PropertyAccessException
//                {
//                    return "file:C:\\WSDL\\path\\";
//                }
//                @Override
//                protected Service constructService(String wsdlURL, String namespaceURI, String serviceLocalPart) throws MalformedURLException
//                {
//                    return mockService;
//                }
//            };
//            context.checking(new Expectations()
//            {
//                {
//                    allowing(mockLog).debug(with(any(String.class)));
//                    oneOf(mockLog).error("WSDL file name is required.");
//                }
//            });
//
//            String wsdlFile = "";
//            String namespaceURI = "fake:namespace:uri";
//            String serviceLocalPart = "FakeServiceName";
//
//            Service createdService = oWebServiceProxyHelper.createService(wsdlFile, namespaceURI, serviceLocalPart);
//            assertNull("Service was not null", createdService);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testCreateServiceEmptyWsdlFile test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testCreateServiceEmptyWsdlFile test: " + t.getMessage());
//        }
//    }
//
//    @Test
//    public void testCreateServiceNullNamespaceURI()
//    {
//        try
//        {
//            WebServiceProxyHelper oWebServiceProxyHelper = new WebServiceProxyHelper()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//                @Override
//                protected String getWsdlPath() throws PropertyAccessException
//                {
//                    return "file:C:\\WSDL\\path\\";
//                }
//                @Override
//                protected Service constructService(String wsdlURL, String namespaceURI, String serviceLocalPart) throws MalformedURLException
//                {
//                    return mockService;
//                }
//            };
//            context.checking(new Expectations()
//            {
//                {
//                    allowing(mockLog).debug(with(any(String.class)));
//                    oneOf(mockLog).error("Namespace URI is required.");
//                }
//            });
//
//            String wsdlFile = "fake.wsdl";
//            String namespaceURI = null;
//            String serviceLocalPart = "FakeServiceName";
//
//            Service createdService = oWebServiceProxyHelper.createService(wsdlFile, namespaceURI, serviceLocalPart);
//            assertNull("Service was not null", createdService);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testCreateServiceNullNamespaceURI test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testCreateServiceNullNamespaceURI test: " + t.getMessage());
//        }
//    }
//
//    @Test
//    public void testCreateServiceEmptyNamespaceURI()
//    {
//        try
//        {
//            WebServiceProxyHelper oWebServiceProxyHelper = new WebServiceProxyHelper()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//                @Override
//                protected String getWsdlPath() throws PropertyAccessException
//                {
//                    return "file:C:\\WSDL\\path\\";
//                }
//                @Override
//                protected Service constructService(String wsdlURL, String namespaceURI, String serviceLocalPart) throws MalformedURLException
//                {
//                    return mockService;
//                }
//            };
//            context.checking(new Expectations()
//            {
//                {
//                    allowing(mockLog).debug(with(any(String.class)));
//                    oneOf(mockLog).error("Namespace URI is required.");
//                }
//            });
//
//            String wsdlFile = "fake.wsdl";
//            String namespaceURI = "";
//            String serviceLocalPart = "FakeServiceName";
//
//            Service createdService = oWebServiceProxyHelper.createService(wsdlFile, namespaceURI, serviceLocalPart);
//            assertNull("Service was not null", createdService);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testCreateServiceEmptyNamespaceURI test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testCreateServiceEmptyNamespaceURI test: " + t.getMessage());
//        }
//    }
//
//    @Test
//    public void testCreateServiceNullServiceLocalPart()
//    {
//        try
//        {
//            WebServiceProxyHelper oWebServiceProxyHelper = new WebServiceProxyHelper()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//                @Override
//                protected String getWsdlPath() throws PropertyAccessException
//                {
//                    return "file:C:\\WSDL\\path\\";
//                }
//                @Override
//                protected Service constructService(String wsdlURL, String namespaceURI, String serviceLocalPart) throws MalformedURLException
//                {
//                    return mockService;
//                }
//            };
//            context.checking(new Expectations()
//            {
//                {
//                    allowing(mockLog).debug(with(any(String.class)));
//                    oneOf(mockLog).error("Service local part name is required.");
//                }
//            });
//
//            String wsdlFile = "fake.wsdl";
//            String namespaceURI = "fake:namespace:uri";
//            String serviceLocalPart = null;
//
//            Service createdService = oWebServiceProxyHelper.createService(wsdlFile, namespaceURI, serviceLocalPart);
//            assertNull("Service was not null", createdService);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testCreateServiceNullServiceLocalPart test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testCreateServiceNullServiceLocalPart test: " + t.getMessage());
//        }
//    }
//
//    @Test
//    public void testCreateServiceEmptyServiceLocalPart()
//    {
//        try
//        {
//            WebServiceProxyHelper oWebServiceProxyHelper = new WebServiceProxyHelper()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//                @Override
//                protected String getWsdlPath() throws PropertyAccessException
//                {
//                    return "file:C:\\WSDL\\path\\";
//                }
//                @Override
//                protected Service constructService(String wsdlURL, String namespaceURI, String serviceLocalPart) throws MalformedURLException
//                {
//                    return mockService;
//                }
//            };
//            context.checking(new Expectations()
//            {
//                {
//                    allowing(mockLog).debug(with(any(String.class)));
//                    oneOf(mockLog).error("Service local part name is required.");
//                }
//            });
//
//            String wsdlFile = "fake.wsdl";
//            String namespaceURI = "fake:namespace:uri";
//            String serviceLocalPart = "";
//
//            Service createdService = oWebServiceProxyHelper.createService(wsdlFile, namespaceURI, serviceLocalPart);
//            assertNull("Service was not null", createdService);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testCreateServiceEmptyServiceLocalPart test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testCreateServiceEmptyServiceLocalPart test: " + t.getMessage());
//        }
//    }
//
//    @Test
//    public void testCreateServiceNullWsdlPath()
//    {
//        try
//        {
//            WebServiceProxyHelper oWebServiceProxyHelper = new WebServiceProxyHelper()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//                @Override
//                protected String getWsdlPath() throws PropertyAccessException
//                {
//                    return null;
//                }
//                @Override
//                protected Service constructService(String wsdlURL, String namespaceURI, String serviceLocalPart) throws MalformedURLException
//                {
//                    return mockService;
//                }
//            };
//            context.checking(new Expectations()
//            {
//                {
//                    allowing(mockLog).debug(with(any(String.class)));
//                    oneOf(mockLog).error("Unable to retrieve the WSDL path.");
//                }
//            });
//
//            String wsdlFile = "fake.wsdl";
//            String namespaceURI = "fake:namespace:uri";
//            String serviceLocalPart = "FakeServiceName";
//
//            Service createdService = oWebServiceProxyHelper.createService(wsdlFile, namespaceURI, serviceLocalPart);
//            assertNull("Service was not null", createdService);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testCreateServiceNullWsdlPath test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testCreateServiceNullWsdlPath test: " + t.getMessage());
//        }
//    }
//
//    @Test
//    public void testCreateServiceEmptyWsdlPath()
//    {
//        try
//        {
//            WebServiceProxyHelper oWebServiceProxyHelper = new WebServiceProxyHelper()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//                @Override
//                protected String getWsdlPath() throws PropertyAccessException
//                {
//                    return "";
//                }
//                @Override
//                protected Service constructService(String wsdlURL, String namespaceURI, String serviceLocalPart) throws MalformedURLException
//                {
//                    return mockService;
//                }
//            };
//            context.checking(new Expectations()
//            {
//                {
//                    allowing(mockLog).debug(with(any(String.class)));
//                    oneOf(mockLog).error("Unable to retrieve the WSDL path.");
//                }
//            });
//
//            String wsdlFile = "fake.wsdl";
//            String namespaceURI = "fake:namespace:uri";
//            String serviceLocalPart = "FakeServiceName";
//
//            Service createdService = oWebServiceProxyHelper.createService(wsdlFile, namespaceURI, serviceLocalPart);
//            assertNull("Service was not null", createdService);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testCreateServiceEmptyWsdlPath test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testCreateServiceEmptyWsdlPath test: " + t.getMessage());
//        }
//    }
//
}
