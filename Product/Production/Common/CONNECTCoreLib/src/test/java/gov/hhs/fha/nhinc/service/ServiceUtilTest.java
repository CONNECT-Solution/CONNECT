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
package gov.hhs.fha.nhinc.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;

import javax.xml.ws.Service;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * 
 * @author Neil Webb
 */
public class ServiceUtilTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

	final Service mockService = context.mock(Service.class);

	@Test
	public void testGetWsdlPath() {
		try {
			ServiceUtil serviceUtil = new ServiceUtil() {
				
				@Override
				protected String getWsdlPath() {
					return "WSDL path";
				}
			};
			String wsdlPath = serviceUtil.getWsdlPath();
			assertNotNull("WSDL path was null", wsdlPath);
			assertEquals("WSDL path incorrect", "WSDL path", wsdlPath);
		} catch (Throwable t) {
			System.out.println("Error running testCreateLogger test: "
					+ t.getMessage());
			t.printStackTrace();
			fail("Error running testCreateLogger test: " + t.getMessage());
		}
	}

	@Test
	public void testConstructService() {
		try {
			ServiceUtil serviceUtil = new ServiceUtil() {
				@Override
				protected Service constructService(String wsdlURL,
						String namespaceURI, String serviceLocalPart)
						throws MalformedURLException {
					return mockService;
				}
			};
			String wsdlUrl = "fake.url";
			String namespaceURI = "fake:namespace:uri";
			String serviceLocalPart = "FakeServiceName";

			Service service = serviceUtil.constructService(wsdlUrl,
					namespaceURI, serviceLocalPart);
			assertNotNull("Service was null", service);
		} catch (Throwable t) {
			System.out.println("Error running testConstructService test: "
					+ t.getMessage());
			t.printStackTrace();
			fail("Error running testConstructService test: " + t.getMessage());
		}
	}

	@Test
	public void testCreateServiceHappy() {
		try {
			ServiceUtil serviceUtil = new ServiceUtil() {
				@Override
				protected String getWsdlPath() {
					return "file:C:\\WSDL\\path\\";
				}

				@Override
				protected Service constructService(String wsdlURL,
						String namespaceURI, String serviceLocalPart)
						throws MalformedURLException {
					return mockService;
				}
			};

			String wsdlFile = "fake.wsdl";
			String namespaceURI = "fake:namespace:uri";
			String serviceLocalPart = "FakeServiceName";

			Service createdService = serviceUtil.createService(wsdlFile,
					namespaceURI, serviceLocalPart);
			assertNotNull("Service was null", createdService);
		} catch (Throwable t) {
			System.out.println("Error running testCreateServiceHappy test: "
					+ t.getMessage());
			t.printStackTrace();
			fail("Error running testCreateServiceHappy test: " + t.getMessage());
		}
	}

	@Test
	public void testCreateServiceNullWsdlFile() {
		try {
			ServiceUtil serviceUtil = new ServiceUtil() {
				
				@Override
				protected String getWsdlPath() {
					return "file:C:\\WSDL\\path\\";
				}

				@Override
				protected Service constructService(String wsdlURL,
						String namespaceURI, String serviceLocalPart)
						throws MalformedURLException {
					return mockService;
				}
			};

			String wsdlFile = null;
			String namespaceURI = "fake:namespace:uri";
			String serviceLocalPart = "FakeServiceName";

			Service createdService = serviceUtil.createService(wsdlFile,
					namespaceURI, serviceLocalPart);
			assertNull("Service was not null", createdService);
		} catch (Throwable t) {
			System.out
					.println("Error running testCreateServiceNullWsdlFile test: "
							+ t.getMessage());
			t.printStackTrace();
			fail("Error running testCreateServiceNullWsdlFile test: "
					+ t.getMessage());
		}
	}

	@Test
	public void testCreateServiceEmptyWsdlFile() {
		try {
			ServiceUtil serviceUtil = new ServiceUtil() {
				
				@Override
				protected String getWsdlPath() {
					return "file:C:\\WSDL\\path\\";
				}

				@Override
				protected Service constructService(String wsdlURL,
						String namespaceURI, String serviceLocalPart)
						throws MalformedURLException {
					return mockService;
				}
			};

			String wsdlFile = "";
			String namespaceURI = "fake:namespace:uri";
			String serviceLocalPart = "FakeServiceName";

			Service createdService = serviceUtil.createService(wsdlFile,
					namespaceURI, serviceLocalPart);
			assertNull("Service was not null", createdService);
		} catch (Throwable t) {
			System.out
					.println("Error running testCreateServiceEmptyWsdlFile test: "
							+ t.getMessage());
			t.printStackTrace();
			fail("Error running testCreateServiceEmptyWsdlFile test: "
					+ t.getMessage());
		}
	}

	@Test
	public void testCreateServiceNullNamespaceURI() {
		try {
			ServiceUtil serviceUtil = new ServiceUtil() {

				@Override
				protected String getWsdlPath() {
					return "file:C:\\WSDL\\path\\";
				}

				@Override
				protected Service constructService(String wsdlURL,
						String namespaceURI, String serviceLocalPart)
						throws MalformedURLException {
					return mockService;
				}
			};

			String wsdlFile = "fake.wsdl";
			String namespaceURI = null;
			String serviceLocalPart = "FakeServiceName";

			Service createdService = serviceUtil.createService(wsdlFile,
					namespaceURI, serviceLocalPart);
			assertNull("Service was not null", createdService);
		} catch (Throwable t) {
			System.out
					.println("Error running testCreateServiceNullNamespaceURI test: "
							+ t.getMessage());
			t.printStackTrace();
			fail("Error running testCreateServiceNullNamespaceURI test: "
					+ t.getMessage());
		}
	}

	@Test
	public void testCreateServiceEmptyNamespaceURI() {
		try {
			ServiceUtil serviceUtil = new ServiceUtil() {
				
				@Override
				protected String getWsdlPath() {
					return "file:C:\\WSDL\\path\\";
				}

				@Override
				protected Service constructService(String wsdlURL,
						String namespaceURI, String serviceLocalPart)
						throws MalformedURLException {
					return mockService;
				}
			};

			String wsdlFile = "fake.wsdl";
			String namespaceURI = "";
			String serviceLocalPart = "FakeServiceName";

			Service createdService = serviceUtil.createService(wsdlFile,
					namespaceURI, serviceLocalPart);
			assertNull("Service was not null", createdService);
		} catch (Throwable t) {
			System.out
					.println("Error running testCreateServiceEmptyNamespaceURI test: "
							+ t.getMessage());
			t.printStackTrace();
			fail("Error running testCreateServiceEmptyNamespaceURI test: "
					+ t.getMessage());
		}
	}

	@Test
	public void testCreateServiceNullServiceLocalPart() {
		try {
			ServiceUtil serviceUtil = new ServiceUtil() {

				@Override
				protected String getWsdlPath() {
					return "file:C:\\WSDL\\path\\";
				}

				@Override
				protected Service constructService(String wsdlURL,
						String namespaceURI, String serviceLocalPart)
						throws MalformedURLException {
					return mockService;
				}
			};

			String wsdlFile = "fake.wsdl";
			String namespaceURI = "fake:namespace:uri";
			String serviceLocalPart = null;

			Service createdService = serviceUtil.createService(wsdlFile,
					namespaceURI, serviceLocalPart);
			assertNull("Service was not null", createdService);
		} catch (Throwable t) {
			System.out
					.println("Error running testCreateServiceNullServiceLocalPart test: "
							+ t.getMessage());
			t.printStackTrace();
			fail("Error running testCreateServiceNullServiceLocalPart test: "
					+ t.getMessage());
		}
	}

	@Test
	public void testCreateServiceEmptyServiceLocalPart() {
		try {
			ServiceUtil serviceUtil = new ServiceUtil() {
				
				@Override
				protected String getWsdlPath() {
					return "file:C:\\WSDL\\path\\";
				}

				@Override
				protected Service constructService(String wsdlURL,
						String namespaceURI, String serviceLocalPart)
						throws MalformedURLException {
					return mockService;
				}
			};

			String wsdlFile = "fake.wsdl";
			String namespaceURI = "fake:namespace:uri";
			String serviceLocalPart = "";

			Service createdService = serviceUtil.createService(wsdlFile,
					namespaceURI, serviceLocalPart);
			assertNull("Service was not null", createdService);
		} catch (Throwable t) {
			System.out
					.println("Error running testCreateServiceEmptyServiceLocalPart test: "
							+ t.getMessage());
			t.printStackTrace();
			fail("Error running testCreateServiceEmptyServiceLocalPart test: "
					+ t.getMessage());
		}
	}

	@Test
	public void testCreateServiceNullWsdlPath() {
		try {
			ServiceUtil serviceUtil = new ServiceUtil() {

				@Override
				protected String getWsdlPath() {
					return null;
				}

				@Override
				protected Service constructService(String wsdlURL,
						String namespaceURI, String serviceLocalPart)
						throws MalformedURLException {
					return mockService;
				}
			};

			String wsdlFile = "fake.wsdl";
			String namespaceURI = "fake:namespace:uri";
			String serviceLocalPart = "FakeServiceName";

			Service createdService = serviceUtil.createService(wsdlFile,
					namespaceURI, serviceLocalPart);
			assertNull("Service was not null", createdService);
		} catch (Throwable t) {
			System.out
					.println("Error running testCreateServiceNullWsdlPath test: "
							+ t.getMessage());
			t.printStackTrace();
			fail("Error running testCreateServiceNullWsdlPath test: "
					+ t.getMessage());
		}
	}

	@Test
	public void testCreateServiceEmptyWsdlPath() {
		try {
			ServiceUtil serviceUtil = new ServiceUtil() {

				@Override
				protected String getWsdlPath() {
					return "";
				}

				@Override
				protected Service constructService(String wsdlURL,
						String namespaceURI, String serviceLocalPart)
						throws MalformedURLException {
					return mockService;
				}
			};

			String wsdlFile = "fake.wsdl";
			String namespaceURI = "fake:namespace:uri";
			String serviceLocalPart = "FakeServiceName";

			Service createdService = serviceUtil.createService(wsdlFile,
					namespaceURI, serviceLocalPart);
			assertNull("Service was not null", createdService);
		} catch (Throwable t) {
			System.out
					.println("Error running testCreateServiceEmptyWsdlPath test: "
							+ t.getMessage());
			t.printStackTrace();
			fail("Error running testCreateServiceEmptyWsdlPath test: "
					+ t.getMessage());
		}
	}

 }
