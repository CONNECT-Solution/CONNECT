/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.service;

//import gov.hhs.fha.nhinc.properties.PropertyAccessException;
//import java.net.MalformedURLException;
//import org.apache.commons.logging.Log;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
//import javax.xml.ws.Service;
//import org.jmock.Expectations;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class ServiceUtilTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void fakeTest()
    {
        assertTrue("Fake", true);
    }
}
//
//    final Log mockLog = context.mock(Log.class);
//    final Service mockService = context.mock(Service.class);
//
//    @Test
//    public void testCreateLogger()
//    {
//        try
//        {
//            ServiceUtil serviceUtil = new ServiceUtil()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//            };
//            Log log = serviceUtil.createLogger();
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
//            ServiceUtil serviceUtil = new ServiceUtil()
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
//            String wsdlPath = serviceUtil.getWsdlPath();
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
//            ServiceUtil serviceUtil = new ServiceUtil()
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
//            Service service = serviceUtil.constructService(wsdlUrl, namespaceURI, serviceLocalPart);
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
//            ServiceUtil serviceUtil = new ServiceUtil()
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
//            Service createdService = serviceUtil.createService(wsdlFile, namespaceURI, serviceLocalPart);
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
//            ServiceUtil serviceUtil = new ServiceUtil()
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
//            Service createdService = serviceUtil.createService(wsdlFile, namespaceURI, serviceLocalPart);
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
//            ServiceUtil serviceUtil = new ServiceUtil()
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
//            Service createdService = serviceUtil.createService(wsdlFile, namespaceURI, serviceLocalPart);
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
//            ServiceUtil serviceUtil = new ServiceUtil()
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
//            Service createdService = serviceUtil.createService(wsdlFile, namespaceURI, serviceLocalPart);
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
//            ServiceUtil serviceUtil = new ServiceUtil()
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
//            Service createdService = serviceUtil.createService(wsdlFile, namespaceURI, serviceLocalPart);
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
//            ServiceUtil serviceUtil = new ServiceUtil()
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
//            Service createdService = serviceUtil.createService(wsdlFile, namespaceURI, serviceLocalPart);
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
//            ServiceUtil serviceUtil = new ServiceUtil()
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
//            Service createdService = serviceUtil.createService(wsdlFile, namespaceURI, serviceLocalPart);
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
//            ServiceUtil serviceUtil = new ServiceUtil()
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
//            Service createdService = serviceUtil.createService(wsdlFile, namespaceURI, serviceLocalPart);
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
//            ServiceUtil serviceUtil = new ServiceUtil()
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
//            Service createdService = serviceUtil.createService(wsdlFile, namespaceURI, serviceLocalPart);
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
//}
