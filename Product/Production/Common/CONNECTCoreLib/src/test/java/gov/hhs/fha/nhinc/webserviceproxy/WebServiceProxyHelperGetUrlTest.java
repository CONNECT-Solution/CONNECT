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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.hhs.fha.nhinc.async.AsyncHeaderCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.junit.Test;

import com.sun.xml.ws.developer.WSBindingProvider;

public class WebServiceProxyHelperGetUrlTest extends AbstractWebServiceProxyHelpTest {
	public WSBindingProvider mockPort;
	@SuppressWarnings("unchecked")
	public Map<String, Object> mockRequestContext;
	public HashMap<String, Object> oMap;
	public SamlTokenCreator mockTokenCreator;
	public AsyncHeaderCreator mockAsyncHeaderCreator;
	public Log mockLog = context.mock(Log.class);
	public WebServiceProxyHelper oHelper;

	/**
	 * Test the getUrlFromTargetSystem method happy path.
	 * @throws Exception 
	 * @throws ConnectionManagerException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void testGetUrlFromTargetSystemHappyPath() throws IllegalArgumentException, ConnectionManagerException, Exception {
			context.checking(new Expectations() {

				{
					exactly(3).of(mockLog).info(with(any(String.class)));
				}
			});
			
			initializationExpectations();
			
			WebServiceProxyHelper oHelper = new WebServiceProxyHelper(mockLog, mockPropertyAccessor) {

				

				@Override
				protected String getEndPointFromConnectionManagerByGatewayAPILevel(
						NhinTargetSystemType oTargetSystem,
						String sServiceName, GATEWAY_API_LEVEL level) {
					return "http://www.theurl.com";
				}
			};
			NhinTargetSystemType oTargetSystem = new NhinTargetSystemType();
			HomeCommunityType oHomeCommunity = new HomeCommunityType();
			oTargetSystem.setHomeCommunity(oHomeCommunity);
			oHomeCommunity.setHomeCommunityId("1.1");
			oHomeCommunity.setName("The name");
			oHomeCommunity.setDescription("The name");
			String sURL = oHelper.getUrlFromTargetSystemByGatewayAPILevel(
					oTargetSystem, NhincConstants.DOC_QUERY_SERVICE_NAME,
					GATEWAY_API_LEVEL.LEVEL_g0);
			assertEquals("URL was incorrect.", "http://www.theurl.com", sURL);
	}

	/**
	 * Test the getUrlFromTargetSystem method null target system.
	 */
	@Test
	public void testGetUrlFromTargetSystemNullTargetSystem() {
		try {
			context.checking(new Expectations() {

				{
					exactly(1).of(mockLog).error(with(any(String.class)));
				}
			});
			initializationExpectations();
			
			
			WebServiceProxyHelper oHelper = new WebServiceProxyHelper(mockLog, mockPropertyAccessor) {

				@Override
				protected Log createLogger() {
					return mockLog;
				}

				@Override
				protected String getEndPointFromConnectionManagerByGatewayAPILevel(
						NhinTargetSystemType oTargetSystem,
						String sServiceName, GATEWAY_API_LEVEL level) {
					return "http://www.theurl.com";
				}
			};
			String sURL = oHelper.getUrlFromTargetSystemByGatewayAPILevel(null,
					NhincConstants.DOC_QUERY_SERVICE_NAME,
					GATEWAY_API_LEVEL.LEVEL_g0);
			fail("An exception should have been thrown.");
		} catch (IllegalArgumentException e) {
			assertEquals("Unexpected exception message.",
					"Target system passed into the proxy is null",
					e.getMessage());
		} catch (Throwable t) {
			System.out
					.println("Error running testGetUrlFromTargetSystemNullTargetSystem test: "
							+ t.getMessage());
			t.printStackTrace();
			fail("Error running testGetUrlFromTargetSystemNullTargetSystem test: "
					+ t.getMessage());
		}
	}

	/**
	 * Test the getUrlFromTargetSystem method with ConnectionManagerException.
	 */
	@Test
	public void testGetUrlFromTargetSystemConnectionManagerException() {
		try {
			context.checking(new Expectations() {

				{
					exactly(3).of(mockLog).info(with(any(String.class)));
					exactly(1).of(mockLog).error(with(any(String.class)),
							with(any(Exception.class)));
				}
			});
			initializationExpectations();
			
			
			WebServiceProxyHelper oHelper = new WebServiceProxyHelper(mockLog, mockPropertyAccessor) {

				@Override
				protected Log createLogger() {
					return mockLog;
				}

				@Override
				protected String getEndPointFromConnectionManagerByGatewayAPILevel(
						NhinTargetSystemType oTargetSystem,
						String sServiceName, GATEWAY_API_LEVEL level)
						throws ConnectionManagerException {
					throw new ConnectionManagerException(
							"This is a forced exception");
				}
			};
			NhinTargetSystemType oTargetSystem = new NhinTargetSystemType();
			HomeCommunityType oHomeCommunity = new HomeCommunityType();
			oTargetSystem.setHomeCommunity(oHomeCommunity);
			oHomeCommunity.setHomeCommunityId("1.1");
			oHomeCommunity.setName("The name");
			oHomeCommunity.setDescription("The name");
			String sURL = oHelper.getUrlFromTargetSystemByGatewayAPILevel(
					oTargetSystem, NhincConstants.DOC_QUERY_SERVICE_NAME,
					GATEWAY_API_LEVEL.LEVEL_g0);
			fail("An exception should have been thrown.");
		} catch (ConnectionManagerException e) {
			assertEquals("Unexpected exception message.",
					"This is a forced exception", e.getMessage());
		} catch (Throwable t) {
			System.out
					.println("Error running testGetUrlFromTargetSystemConnectionManagerException test: "
							+ t.getMessage());
			t.printStackTrace();
			fail("Error running testGetUrlFromTargetSystemConnectionManagerException test: "
					+ t.getMessage());
		}
	}

	/**
	 * Test the getUrlFromHomeCommunity method happy path.
	 * @throws Exception 
	 * @throws ConnectionManagerException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void testGetUrlFromHomeCommunity() throws IllegalArgumentException, ConnectionManagerException, Exception {
			context.checking(new Expectations() {

				{
					exactly(1).of(mockLog).info(with(any(String.class)));
				}
			});
			initializationExpectations();
			
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
			String sHomeCommunityId = "1.1";
			String sURL = oHelper.getUrlFromHomeCommunity(sHomeCommunityId,
					NhincConstants.DOC_QUERY_SERVICE_NAME);
			assertEquals("URL was incorrect.", "http://www.theurl.com", sURL);
	
	}

	/**
	 * Test the getUrlFromHomeCommunity method null homeCommunityId.
	 * @throws Exception 
	 * @throws ConnectionManagerException 
	 * @throws IllegalArgumentException 
	 */
	@Test(expected = IllegalArgumentException.class )
	public void testGetUrlFromHomeCommunityNullId() throws IllegalArgumentException, ConnectionManagerException, Exception {
			context.checking(new Expectations() {

				{
					exactly(1).of(mockLog).error(with(any(String.class)));
				}
			});
			initializationExpectations();
			
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
			String sHomeCommunityId = null;
			String sURL = oHelper.getUrlFromHomeCommunity(sHomeCommunityId,
					NhincConstants.DOC_QUERY_SERVICE_NAME);
		
	}

	/**
	 * Test the getUrlFromHomeCommunity method empty homeCommunityId.
	 * @throws Exception 
	 * @throws ConnectionManagerException 
	 * @throws IllegalArgumentException 
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testGetUrlFromHomeCommunityEmptyId() throws IllegalArgumentException, ConnectionManagerException, Exception {
			context.checking(new Expectations() {

				{
					exactly(1).of(mockLog).error(with(any(String.class)));
				}
			});
			initializationExpectations();
			
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
			String sHomeCommunityId = "";
			String sURL = oHelper.getUrlFromHomeCommunity(sHomeCommunityId,
					NhincConstants.DOC_QUERY_SERVICE_NAME);
	
	}

	/**
	 * Test the getUrlFromHomeCommunity method ConnectionManagerException.
	 * @throws Exception 
	 * @throws ConnectionManagerException 
	 * @throws IllegalArgumentException 
	 */
	@Test(expected = ConnectionManagerException.class )
	public void testGetUrlFromHomeCommunityConnectionManagerException() throws IllegalArgumentException, ConnectionManagerException, Exception {
			context.checking(new Expectations() {

				{
					exactly(1).of(mockLog).info(with(any(String.class)));
					exactly(1).of(mockLog).error(with(any(String.class)),
							with(any(ConnectionManagerException.class)));
				}
			});
			initializationExpectations();
			WebServiceProxyHelper oHelper = new WebServiceProxyHelper(mockLog, mockPropertyAccessor) {

				@Override
				protected Log createLogger() {
					return mockLog;
				}

				@Override
				protected String getEndPointFromConnectionManager(
						String sHomeCommunityId, String sServiceName)
						throws ConnectionManagerException {
					throw new ConnectionManagerException("Call failed.");
				}
			};
			String sHomeCommunityId = "1.1";
			String sURL = oHelper.getUrlFromHomeCommunity(sHomeCommunityId,
					NhincConstants.DOC_QUERY_SERVICE_NAME);
		
	}

	/**
	 * Test the getUrlLocalHomeCommunity method happy path.
	 * @throws Exception 
	 * @throws ConnectionManagerException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void testGetUrlLocalHomeCommunity() throws IllegalArgumentException, ConnectionManagerException, Exception {
		initializationExpectations();
		
		WebServiceProxyHelper oHelper = new WebServiceProxyHelper(mockLog, mockPropertyAccessor) {

				@Override
				protected Log createLogger() {
					return mockLog;
				}

				@Override
				protected String getLocalEndPointFromConnectionManager(
						String sServiceName) {
					return "http://www.theurl.com";
				}
			};
			String sURL = oHelper
					.getUrlLocalHomeCommunity(NhincConstants.DOC_QUERY_SERVICE_NAME);
			assertEquals("URL was incorrect.", "http://www.theurl.com", sURL);
		
	}

	/**
	 * Test the getUrlLocalHomeCommunity method ConnectionManagerException.
	 */
	@Test
	public void testGetUrlLocalHomeCommunityConnectionManagerException() {
		try {
			initializationExpectations();
			
			
			context.checking(new Expectations() {

				{
					exactly(1).of(mockLog).error(with(any(String.class)),
							with(any(ConnectionManagerException.class)));
				}
			});
			WebServiceProxyHelper oHelper = new WebServiceProxyHelper(mockLog, mockPropertyAccessor) {

				@Override
				protected Log createLogger() {
					return mockLog;
				}

				@Override
				protected String getLocalEndPointFromConnectionManager(
						String sServiceName) throws ConnectionManagerException {
					throw new ConnectionManagerException("Call failed.");
				}
			};
			String sURL = oHelper
					.getUrlLocalHomeCommunity(NhincConstants.DOC_QUERY_SERVICE_NAME);
		} catch (ConnectionManagerException e) {
			assertTrue("Invalid exception message: ",
					e.getMessage().contains("Call failed."));
		} catch (Throwable t) {
			System.out
					.println("Error running testGetUrlLocalHomeCommunityConnectionManagerException test: "
							+ t.getMessage());
			t.printStackTrace();
			fail("Error running testGetUrlLocalHomeCommunityConnectionManagerException test: "
					+ t.getMessage());
		}
	}
}