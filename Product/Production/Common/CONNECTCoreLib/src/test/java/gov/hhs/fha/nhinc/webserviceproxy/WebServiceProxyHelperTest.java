/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.webserviceproxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.hhs.fha.nhinc.async.AsyncHeaderCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sun.xml.ws.developer.WSBindingProvider;

/**
 * 
 * @author westberg
 */
@RunWith(JMock.class)
public class WebServiceProxyHelperTest extends AbstractWebServiceProxyHelpTest {

	final WSBindingProvider mockPort = context.mock(WSBindingProvider.class);
	@SuppressWarnings("unchecked")
	final Map<String, Object> mockRequestContext = context.mock(Map.class);
	final HashMap<String, Object> oMap = new HashMap<String, Object>();
	final SamlTokenCreator mockTokenCreator = context
			.mock(SamlTokenCreator.class);
	final AsyncHeaderCreator mockAsyncHeaderCreator = context
			.mock(AsyncHeaderCreator.class);

	
	final Log mockLog = context.mock(Log.class);

	WebServiceProxyHelper oHelper;
	
	

	@Before
	public void before() throws PropertyAccessException {
		initializationExpectations();	
	oHelper = new WebServiceProxyHelper(mockLog,
			mockPropertyAccessor);
	}
	
	
	
	/**
	 * Test the create logger method.
	 */
	@Test
	public void testCreateLogger() {
		Log oLog = oHelper.createLogger();
		assertNotNull("Log was null", oLog);

	}
	
	
	/**
	 * Test the create the helper .
	 */
	@Test
	public void testCreate() {
		WebServiceProxyHelper webProxyHelper = new WebServiceProxyHelper();
		assertNotNull(webProxyHelper);

	}



	
	/**
	 * Tests the getMessageId method - Happy Path
	 */
	@Test
	public void testGetMessageIdHappyPath() {

		AssertionType oAssertion = new AssertionType() {

			@Override
			public String getMessageId() {
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
	public void testGetMessageIdNullAssertion() {

		context.checking(new Expectations() {

			{
				allowing(mockLog).warn(with(any(String.class)));
			}
		});
		
		String messageId = oHelper.getMessageId(null);
		assertNotNull("messageId", messageId);
		assertTrue("messageId was empty", messageId.length() > 0);
	}

	/**
	 * Tests the getMessageId method - Null AsyncMessageId
	 */
	@Test
	public void testGetMessageIdNullAsyncMessageId() {

		context.checking(new Expectations() {

			{
				allowing(mockLog).warn(with(any(String.class)));
			}
		});
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
	public void testGetMessageIdEmptyAsyncMessageId() {

		context.checking(new Expectations() {

			{
				allowing(mockLog).warn(with(any(String.class)));
			}
		});
		AssertionType oAssertion = new AssertionType();
		oAssertion.setMessageId("");
		String messageId = oHelper.getMessageId(oAssertion);
		assertNotNull("messageId", messageId);
		assertTrue("messageId was empty", messageId.length() > 0);
	}

	/**
	 * Tests the getRelatesTo method TODO - this will change with implementation
	 */
	@Test
	public void testGetRelatesTo() {

		AssertionType oAssertion = new AssertionType();
		List returnedRelatesTo = oHelper.getRelatesTo(oAssertion);
		assertNotNull("Returned relatesTo list is null", returnedRelatesTo);
		assertEquals("Expects Empty list at this time", 0,
				returnedRelatesTo.size());
	}

	
	/**
	 * Test of getEndPointFromConnectionManager method, of class
	 * WebServiceProxyHelper.
	 */
	@Test
	@Ignore
	public void TestGetUrlFromHomeCommunity() throws Exception {
		/*
		 * Mockery mockingContext; final ConnectionManagerCache
		 * mockedDependency; mockingContext = new JUnit4Mockery() {
		 * 
		 * { setImposteriser(ClassImposteriser.INSTANCE); } }; mockedDependency
		 * = mockingContext.mock(ConnectionManagerCache.getInstance().class);
		 * 
		 * 
		 * mockingContext.checking(new Expectations() {
		 * 
		 * {
		 * one(mockedDependency).getEndpointURLByServiceName(with(any(String.class
		 * )), with(any(String.class))); will(returnValue("someurl")); } });
		 */
		
		
		context.checking(new Expectations() {

			{
				allowing(mockLog).info(with(any(String.class)));
			}
		});

		WebServiceProxyHelper oHelper = new WebServiceProxyHelper(mockLog, mockPropertyAccessor) {

			@Override
			protected Log createLogger() {
				return mockLog;
			}

			@Override
			protected String getEndPointFromConnectionManager(
					String sHomeCommunityId, String sServiceName) {
				return "http://www.theurl.com";
			}
		};
		WebServiceProxyHelper instance = new WebServiceProxyHelper();
		String expResult = "https://localhost:8181/GatewayDocumentQuery/1_0/NhinService/RespondingGateway_Query_Service/DocQuery";
		String homeCommunityId = "1.1";
		String sServiceName = "QueryForDocument";
		String result = instance.getUrlFromHomeCommunity(homeCommunityId,
				sServiceName);
		assertEquals(expResult, result);
	}

	/**
	 * Test of getLocalEndPointFromConnectionManager method, of class
	 * WebServiceProxyHelper.
	 */
	@Test
	@Ignore
	public void TestGetUrlLocalHomeCommunity() throws Exception {
		/*
		 * Mockery mockingContext; final ConnectionManagerCache
		 * mockedDependency; mockingContext = new JUnit4Mockery() {
		 * 
		 * { setImposteriser(ClassImposteriser.INSTANCE); } }; mockedDependency
		 * = mockingContext.mock(ConnectionManagerCache.getInstance().class);
		 * 
		 * String expResult = "someurl"; mockingContext.checking(new
		 * Expectations() {
		 * 
		 * {
		 * one(mockedDependency).getLocalEndpointURLByServiceName(with(any(String
		 * .class))); will(returnValue("someurl")); } });
		 */
		WebServiceProxyHelper instance = new WebServiceProxyHelper();
		String expResult = "https://localhost:8181/GatewayDocumentQuery/1_0/NhinService/RespondingGateway_Query_Service/DocQuery";
		String sServiceName = "QueryForDocument";
		String result = instance.getUrlLocalHomeCommunity(sServiceName);
		assertEquals(expResult, result);
	}

	/**
	 * Test of getUrlFromTargetSystem method, of class WebServiceProxyHelper.
	 */
	/*
	 * @Test public void testGetUrlFromTargetSystem() throws Exception {
	 * /*Mockery mockingContext; final ConnectionManagerCache mockedDependency;
	 * mockingContext = new JUnit4Mockery() {
	 * 
	 * { setImposteriser(ClassImposteriser.INSTANCE); } }; mockedDependency =
	 * mockingContext.mock(ConnectionManagerCache.getInstance().class);
	 * 
	 * String expResult = "someurl"; mockingContext.checking(new Expectations()
	 * {
	 * 
	 * { one(mockedDependency).getEndpontURLFromNhinTarget(with(any(
	 * NhinTargetSystemType.class)), with(any(String.class)));
	 * will(returnValue("someurl")); } });
	 */
	/*
	 * WebServiceProxyHelper instance = new WebServiceProxyHelper(); String
	 * expResult =
	 * "https://localhost:8181/GatewayDocumentQuery/1_0/NhinService/RespondingGateway_Query_Service/DocQuery"
	 * ; NhinTargetSystemType oTargetSystem = new NhinTargetSystemType();
	 * oTargetSystem.setHomeCommunity(new HomeCommunityType());
	 * oTargetSystem.getHomeCommunity().setHomeCommunityId("1.1"); String
	 * sServiceName = "QueryForDocument"; String result =
	 * instance.getUrlFromTargetSystem(oTargetSystem, sServiceName);
	 * assertEquals(expResult, result); }
	 */

	/**
	 * Test of getUrlFromTargetSystemByGatewayAPILevel method, of class
	 * WebServiceProxyHelper.
	 */
	@Test
	@Ignore	
	public void testGetUrlFromTargetSystemByGatewayAPILevel() throws Exception {
		/*
		 * Mockery mockingContext; final ConnectionManagerCache
		 * mockedDependency; mockingContext = new JUnit4Mockery() {
		 * 
		 * { setImposteriser(ClassImposteriser.INSTANCE); } }; mockedDependency
		 * = mockingContext.mock(ConnectionManagerCache.getInstance().class);
		 * 
		 * String expResult = "someurl"; mockingContext.checking(new
		 * Expectations() {
		 * 
		 * { one(mockedDependency).getEndpontURLFromNhinTarget(with(any(
		 * NhinTargetSystemType.class)), with(any(String.class)),
		 * with(any(GATEWAY_API_LEVEL.class))); will(returnValue("someurl")); }
		 * });
		 */
		String expResult = "https://localhost:8181/GatewayDocumentQuery/1_0/NhinService/RespondingGateway_Query_Service/DocQuery";
		NhinTargetSystemType oTargetSystem = new NhinTargetSystemType();
		oTargetSystem.setHomeCommunity(new HomeCommunityType());
		oTargetSystem.getHomeCommunity().setHomeCommunityId("1.1");
		String sServiceName = "QueryForDocument";
		GATEWAY_API_LEVEL level = GATEWAY_API_LEVEL.LEVEL_g0;
		String result = oHelper
				.getEndPointFromConnectionManagerByGatewayAPILevel(
						oTargetSystem, sServiceName, level);
		assertEquals(expResult, result);
	}

	/**
	 * Test of getUrlFromTargetSystemByAdapterAPILevel method, of class
	 * WebServiceProxyHelper.
	 */
	@Test
	@Ignore
	
	
	public void testGetUrlFromTargetSystemByAdapterAPILevel() throws Exception {
		/*
		 * Mockery mockingContext; final ConnectionManagerCache
		 * mockedDependency; mockingContext = new JUnit4Mockery() {
		 * 
		 * { setImposteriser(ClassImposteriser.INSTANCE); } }; mockedDependency
		 * = mockingContext.mock(ConnectionManagerCache.getInstance().class);
		 * 
		 * String expResult = "someurl"; mockingContext.checking(new
		 * Expectations() {
		 * 
		 * { one(mockedDependency).getEndpontURLFromNhinTarget(with(any(
		 * NhinTargetSystemType.class)), with(any(String.class)),
		 * with(any(ADAPTER_API_LEVEL.class))); will(returnValue("someurl")); }
		 * });
		 */
		String expResult = "https://localhost:8181/GatewayDocumentQuery/1_0/NhinService/RespondingGateway_Query_Service/DocQuery";
		NhinTargetSystemType oTargetSystem = new NhinTargetSystemType();
		oTargetSystem.setHomeCommunity(new HomeCommunityType());
		oTargetSystem.getHomeCommunity().setHomeCommunityId("1.1");
		String sServiceName = "QueryForDocument";
		ADAPTER_API_LEVEL level = ADAPTER_API_LEVEL.LEVEL_a0;
		String result = oHelper
				.getEndPointFromConnectionManagerByAdapterAPILevel(
						sServiceName, level);
		assertEquals(expResult, result);
	}
	// The following commented tests came from ServiceUtilTest.java when the
	// methods
	// from ServiceUtil were moved into the WebServiceProxyHelper.java class.
	// They
	// were commented out there and so they are commented out here.
	// -------------------------------------------------------------------------------
	//
	// final Log mockLog = context.mock(Log.class);
	// final Service mockService = context.mock(Service.class);
	//
	// @Test
	// public void testCreateLogger()
	// {
	// try
	// {
	// WebServiceProxyHelper oWebServiceProxyHelper = new
	// WebServiceProxyHelper()
	// {
	// @Override
	// protected Log createLogger()
	// {
	// return mockLog;
	// }
	// };
	// Log log = oWebServiceProxyHelper.createLogger();
	// assertNotNull("Log was null", log);
	// }
	// catch(Throwable t)
	// {
	// System.out.println("Error running testCreateLogger test: " +
	// t.getMessage());
	// t.printStackTrace();
	// fail("Error running testCreateLogger test: " + t.getMessage());
	// }
	// }
	//
	// @Test
	// public void testGetWsdlPath()
	// {
	// try
	// {
	// WebServiceProxyHelper oWebServiceProxyHelper = new
	// WebServiceProxyHelper()
	// {
	// @Override
	// protected Log createLogger()
	// {
	// return mockLog;
	// }
	// @Override
	// protected String getWsdlPath() throws PropertyAccessException
	// {
	// return "WSDL path";
	// }
	// };
	// String wsdlPath = oWebServiceProxyHelper.getWsdlPath();
	// assertNotNull("WSDL path was null", wsdlPath);
	// assertEquals("WSDL path incorrect", "WSDL path", wsdlPath);
	// }
	// catch(Throwable t)
	// {
	// System.out.println("Error running testCreateLogger test: " +
	// t.getMessage());
	// t.printStackTrace();
	// fail("Error running testCreateLogger test: " + t.getMessage());
	// }
	// }
	//
	// @Test
	// public void testConstructService()
	// {
	// try
	// {
	// WebServiceProxyHelper oWebServiceProxyHelper = new
	// WebServiceProxyHelper()
	// {
	// @Override
	// protected Log createLogger()
	// {
	// return mockLog;
	// }
	// @Override
	// protected Service constructService(String wsdlURL, String namespaceURI,
	// String serviceLocalPart) throws MalformedURLException
	// {
	// return mockService;
	// }
	// };
	// String wsdlUrl = "fake.url";
	// String namespaceURI = "fake:namespace:uri";
	// String serviceLocalPart = "FakeServiceName";
	//
	// Service service = oWebServiceProxyHelper.constructService(wsdlUrl,
	// namespaceURI, serviceLocalPart);
	// assertNotNull("Service was null", service);
	// }
	// catch(Throwable t)
	// {
	// System.out.println("Error running testConstructService test: " +
	// t.getMessage());
	// t.printStackTrace();
	// fail("Error running testConstructService test: " + t.getMessage());
	// }
	// }
	//
	// @Test
	// public void testCreateServiceHappy()
	// {
	// try
	// {
	// WebServiceProxyHelper oWebServiceProxyHelper = new
	// WebServiceProxyHelper()
	// {
	// @Override
	// protected Log createLogger()
	// {
	// return mockLog;
	// }
	// @Override
	// protected String getWsdlPath() throws PropertyAccessException
	// {
	// return "file:C:\\WSDL\\path\\";
	// }
	// @Override
	// protected Service constructService(String wsdlURL, String namespaceURI,
	// String serviceLocalPart) throws MalformedURLException
	// {
	// return mockService;
	// }
	// };
	// context.checking(new Expectations()
	// {
	// {
	// allowing(mockLog).debug(with(any(String.class)));
	// }
	// });
	//
	// String wsdlFile = "fake.wsdl";
	// String namespaceURI = "fake:namespace:uri";
	// String serviceLocalPart = "FakeServiceName";
	//
	// Service createdService = oWebServiceProxyHelper.createService(wsdlFile,
	// namespaceURI, serviceLocalPart);
	// assertNotNull("Service was null", createdService);
	// }
	// catch(Throwable t)
	// {
	// System.out.println("Error running testCreateServiceHappy test: " +
	// t.getMessage());
	// t.printStackTrace();
	// fail("Error running testCreateServiceHappy test: " + t.getMessage());
	// }
	// }
	//
	// @Test
	// public void testCreateServiceNullWsdlFile()
	// {
	// try
	// {
	// WebServiceProxyHelper oWebServiceProxyHelper = new
	// WebServiceProxyHelper()
	// {
	// @Override
	// protected Log createLogger()
	// {
	// return mockLog;
	// }
	// @Override
	// protected String getWsdlPath() throws PropertyAccessException
	// {
	// return "file:C:\\WSDL\\path\\";
	// }
	// @Override
	// protected Service constructService(String wsdlURL, String namespaceURI,
	// String serviceLocalPart) throws MalformedURLException
	// {
	// return mockService;
	// }
	// };
	// context.checking(new Expectations()
	// {
	// {
	// allowing(mockLog).debug(with(any(String.class)));
	// oneOf(mockLog).error("WSDL file name is required.");
	// }
	// });
	//
	// String wsdlFile = null;
	// String namespaceURI = "fake:namespace:uri";
	// String serviceLocalPart = "FakeServiceName";
	//
	// Service createdService = oWebServiceProxyHelper.createService(wsdlFile,
	// namespaceURI, serviceLocalPart);
	// assertNull("Service was not null", createdService);
	// }
	// catch(Throwable t)
	// {
	// System.out.println("Error running testCreateServiceNullWsdlFile test: " +
	// t.getMessage());
	// t.printStackTrace();
	// fail("Error running testCreateServiceNullWsdlFile test: " +
	// t.getMessage());
	// }
	// }
	//
	// @Test
	// public void testCreateServiceEmptyWsdlFile()
	// {
	// try
	// {
	// WebServiceProxyHelper oWebServiceProxyHelper = new
	// WebServiceProxyHelper()
	// {
	// @Override
	// protected Log createLogger()
	// {
	// return mockLog;
	// }
	// @Override
	// protected String getWsdlPath() throws PropertyAccessException
	// {
	// return "file:C:\\WSDL\\path\\";
	// }
	// @Override
	// protected Service constructService(String wsdlURL, String namespaceURI,
	// String serviceLocalPart) throws MalformedURLException
	// {
	// return mockService;
	// }
	// };
	// context.checking(new Expectations()
	// {
	// {
	// allowing(mockLog).debug(with(any(String.class)));
	// oneOf(mockLog).error("WSDL file name is required.");
	// }
	// });
	//
	// String wsdlFile = "";
	// String namespaceURI = "fake:namespace:uri";
	// String serviceLocalPart = "FakeServiceName";
	//
	// Service createdService = oWebServiceProxyHelper.createService(wsdlFile,
	// namespaceURI, serviceLocalPart);
	// assertNull("Service was not null", createdService);
	// }
	// catch(Throwable t)
	// {
	// System.out.println("Error running testCreateServiceEmptyWsdlFile test: "
	// + t.getMessage());
	// t.printStackTrace();
	// fail("Error running testCreateServiceEmptyWsdlFile test: " +
	// t.getMessage());
	// }
	// }
	//
	// @Test
	// public void testCreateServiceNullNamespaceURI()
	// {
	// try
	// {
	// WebServiceProxyHelper oWebServiceProxyHelper = new
	// WebServiceProxyHelper()
	// {
	// @Override
	// protected Log createLogger()
	// {
	// return mockLog;
	// }
	// @Override
	// protected String getWsdlPath() throws PropertyAccessException
	// {
	// return "file:C:\\WSDL\\path\\";
	// }
	// @Override
	// protected Service constructService(String wsdlURL, String namespaceURI,
	// String serviceLocalPart) throws MalformedURLException
	// {
	// return mockService;
	// }
	// };
	// context.checking(new Expectations()
	// {
	// {
	// allowing(mockLog).debug(with(any(String.class)));
	// oneOf(mockLog).error("Namespace URI is required.");
	// }
	// });
	//
	// String wsdlFile = "fake.wsdl";
	// String namespaceURI = null;
	// String serviceLocalPart = "FakeServiceName";
	//
	// Service createdService = oWebServiceProxyHelper.createService(wsdlFile,
	// namespaceURI, serviceLocalPart);
	// assertNull("Service was not null", createdService);
	// }
	// catch(Throwable t)
	// {
	// System.out.println("Error running testCreateServiceNullNamespaceURI test: "
	// + t.getMessage());
	// t.printStackTrace();
	// fail("Error running testCreateServiceNullNamespaceURI test: " +
	// t.getMessage());
	// }
	// }
	//
	// @Test
	// public void testCreateServiceEmptyNamespaceURI()
	// {
	// try
	// {
	// WebServiceProxyHelper oWebServiceProxyHelper = new
	// WebServiceProxyHelper()
	// {
	// @Override
	// protected Log createLogger()
	// {
	// return mockLog;
	// }
	// @Override
	// protected String getWsdlPath() throws PropertyAccessException
	// {
	// return "file:C:\\WSDL\\path\\";
	// }
	// @Override
	// protected Service constructService(String wsdlURL, String namespaceURI,
	// String serviceLocalPart) throws MalformedURLException
	// {
	// return mockService;
	// }
	// };
	// context.checking(new Expectations()
	// {
	// {
	// allowing(mockLog).debug(with(any(String.class)));
	// oneOf(mockLog).error("Namespace URI is required.");
	// }
	// });
	//
	// String wsdlFile = "fake.wsdl";
	// String namespaceURI = "";
	// String serviceLocalPart = "FakeServiceName";
	//
	// Service createdService = oWebServiceProxyHelper.createService(wsdlFile,
	// namespaceURI, serviceLocalPart);
	// assertNull("Service was not null", createdService);
	// }
	// catch(Throwable t)
	// {
	// System.out.println("Error running testCreateServiceEmptyNamespaceURI test: "
	// + t.getMessage());
	// t.printStackTrace();
	// fail("Error running testCreateServiceEmptyNamespaceURI test: " +
	// t.getMessage());
	// }
	// }
	//
	// @Test
	// public void testCreateServiceNullServiceLocalPart()
	// {
	// try
	// {
	// WebServiceProxyHelper oWebServiceProxyHelper = new
	// WebServiceProxyHelper()
	// {
	// @Override
	// protected Log createLogger()
	// {
	// return mockLog;
	// }
	// @Override
	// protected String getWsdlPath() throws PropertyAccessException
	// {
	// return "file:C:\\WSDL\\path\\";
	// }
	// @Override
	// protected Service constructService(String wsdlURL, String namespaceURI,
	// String serviceLocalPart) throws MalformedURLException
	// {
	// return mockService;
	// }
	// };
	// context.checking(new Expectations()
	// {
	// {
	// allowing(mockLog).debug(with(any(String.class)));
	// oneOf(mockLog).error("Service local part name is required.");
	// }
	// });
	//
	// String wsdlFile = "fake.wsdl";
	// String namespaceURI = "fake:namespace:uri";
	// String serviceLocalPart = null;
	//
	// Service createdService = oWebServiceProxyHelper.createService(wsdlFile,
	// namespaceURI, serviceLocalPart);
	// assertNull("Service was not null", createdService);
	// }
	// catch(Throwable t)
	// {
	// System.out.println("Error running testCreateServiceNullServiceLocalPart test: "
	// + t.getMessage());
	// t.printStackTrace();
	// fail("Error running testCreateServiceNullServiceLocalPart test: " +
	// t.getMessage());
	// }
	// }
	//
	// @Test
	// public void testCreateServiceEmptyServiceLocalPart()
	// {
	// try
	// {
	// WebServiceProxyHelper oWebServiceProxyHelper = new
	// WebServiceProxyHelper()
	// {
	// @Override
	// protected Log createLogger()
	// {
	// return mockLog;
	// }
	// @Override
	// protected String getWsdlPath() throws PropertyAccessException
	// {
	// return "file:C:\\WSDL\\path\\";
	// }
	// @Override
	// protected Service constructService(String wsdlURL, String namespaceURI,
	// String serviceLocalPart) throws MalformedURLException
	// {
	// return mockService;
	// }
	// };
	// context.checking(new Expectations()
	// {
	// {
	// allowing(mockLog).debug(with(any(String.class)));
	// oneOf(mockLog).error("Service local part name is required.");
	// }
	// });
	//
	// String wsdlFile = "fake.wsdl";
	// String namespaceURI = "fake:namespace:uri";
	// String serviceLocalPart = "";
	//
	// Service createdService = oWebServiceProxyHelper.createService(wsdlFile,
	// namespaceURI, serviceLocalPart);
	// assertNull("Service was not null", createdService);
	// }
	// catch(Throwable t)
	// {
	// System.out.println("Error running testCreateServiceEmptyServiceLocalPart test: "
	// + t.getMessage());
	// t.printStackTrace();
	// fail("Error running testCreateServiceEmptyServiceLocalPart test: " +
	// t.getMessage());
	// }
	// }
	//
	// @Test
	// public void testCreateServiceNullWsdlPath()
	// {
	// try
	// {
	// WebServiceProxyHelper oWebServiceProxyHelper = new
	// WebServiceProxyHelper()
	// {
	// @Override
	// protected Log createLogger()
	// {
	// return mockLog;
	// }
	// @Override
	// protected String getWsdlPath() throws PropertyAccessException
	// {
	// return null;
	// }
	// @Override
	// protected Service constructService(String wsdlURL, String namespaceURI,
	// String serviceLocalPart) throws MalformedURLException
	// {
	// return mockService;
	// }
	// };
	// context.checking(new Expectations()
	// {
	// {
	// allowing(mockLog).debug(with(any(String.class)));
	// oneOf(mockLog).error("Unable to retrieve the WSDL path.");
	// }
	// });
	//
	// String wsdlFile = "fake.wsdl";
	// String namespaceURI = "fake:namespace:uri";
	// String serviceLocalPart = "FakeServiceName";
	//
	// Service createdService = oWebServiceProxyHelper.createService(wsdlFile,
	// namespaceURI, serviceLocalPart);
	// assertNull("Service was not null", createdService);
	// }
	// catch(Throwable t)
	// {
	// System.out.println("Error running testCreateServiceNullWsdlPath test: " +
	// t.getMessage());
	// t.printStackTrace();
	// fail("Error running testCreateServiceNullWsdlPath test: " +
	// t.getMessage());
	// }
	// }
	//
	// @Test
	// public void testCreateServiceEmptyWsdlPath()
	// {
	// try
	// {
	// WebServiceProxyHelper oWebServiceProxyHelper = new
	// WebServiceProxyHelper()
	// {
	// @Override
	// protected Log createLogger()
	// {
	// return mockLog;
	// }
	// @Override
	// protected String getWsdlPath() throws PropertyAccessException
	// {
	// return "";
	// }
	// @Override
	// protected Service constructService(String wsdlURL, String namespaceURI,
	// String serviceLocalPart) throws MalformedURLException
	// {
	// return mockService;
	// }
	// };
	// context.checking(new Expectations()
	// {
	// {
	// allowing(mockLog).debug(with(any(String.class)));
	// oneOf(mockLog).error("Unable to retrieve the WSDL path.");
	// }
	// });
	//
	// String wsdlFile = "fake.wsdl";
	// String namespaceURI = "fake:namespace:uri";
	// String serviceLocalPart = "FakeServiceName";
	//
	// Service createdService = oWebServiceProxyHelper.createService(wsdlFile,
	// namespaceURI, serviceLocalPart);
	// assertNull("Service was not null", createdService);
	// }
	// catch(Throwable t)
	// {
	// System.out.println("Error running testCreateServiceEmptyWsdlPath test: "
	// + t.getMessage());
	// t.printStackTrace();
	// fail("Error running testCreateServiceEmptyWsdlPath test: " +
	// t.getMessage());
	// }
	// }
	//
}
