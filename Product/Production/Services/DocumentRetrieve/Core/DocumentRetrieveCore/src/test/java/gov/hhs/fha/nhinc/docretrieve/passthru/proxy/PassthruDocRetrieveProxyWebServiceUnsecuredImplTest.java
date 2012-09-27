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
package gov.hhs.fha.nhinc.docretrieve.passthru.proxy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;


import javax.xml.ws.Service;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxydocretrieve.NhincProxyDocRetrievePortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

/**
 * 
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class PassthruDocRetrieveProxyWebServiceUnsecuredImplTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final RetrieveDocumentSetRequestType mockDockRetrieveRequest = context.mock(RetrieveDocumentSetRequestType.class);
    final AssertionType mockAssertion = context.mock(AssertionType.class);
    final NhinTargetSystemType mockTargetSystem = context.mock(NhinTargetSystemType.class);
    final NhincProxyDocRetrievePortType mockPort = context.mock(NhincProxyDocRetrievePortType.class);
    final Service mockService = context.mock(Service.class);
    final WebServiceProxyHelper mockWebServiceProxyHelper = context.mock(WebServiceProxyHelper.class);

    @Test
    public void testCreateLogger() {
        try {
            PassthruDocRetrieveProxyWebServiceUnsecuredImpl sut = new PassthruDocRetrieveProxyWebServiceUnsecuredImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper() {
                    return mockWebServiceProxyHelper;
                }
            };
            Log log = sut.createLogger();
            assertNotNull("Log was null", log);
        } catch (Throwable t) {
            System.out.println("Error running testCreateLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger test: " + t.getMessage());
        }
    }

    @Test
    public void testCreateWebServiceProxyHelper() {
        try {
            PassthruDocRetrieveProxyWebServiceUnsecuredImpl sut = new PassthruDocRetrieveProxyWebServiceUnsecuredImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper() {
                    return mockWebServiceProxyHelper;
                }
            };
            WebServiceProxyHelper wsProxyHelper = sut.createWebServiceProxyHelper();
            assertNotNull("WebServiceProxyHelper was null", wsProxyHelper);
        } catch (Throwable t) {
            System.out.println("Error running testCreateWebServiceProxyHelper test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateWebServiceProxyHelper test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayCrossGatewayRetrieveHappy() {
        try {
            final RetrieveDocumentSetResponseType mockResponse = context.mock(RetrieveDocumentSetResponseType.class);
            final WebServiceProxyHelper wsProxyHelper = new WebServiceProxyHelper() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                public String getUrlLocalHomeCommunity(String serviceName) {
                    return "url";
                }
            };
            final CONNECTClient<NhincProxyDocRetrievePortType> mockClient = new CONNECTClient<NhincProxyDocRetrievePortType>() {
                @Override
                public NhincProxyDocRetrievePortType getPort() {
                    // TODO Auto-generated method stub
                    return null;
                }
                
                /* (non-Javadoc)
                 * @see gov.hhs.fha.nhinc.messaging.client.CONNECTClient#invokePort(java.lang.Class, java.lang.String, java.lang.Object)
                 */
                @Override
                public Object invokePort(Class<NhincProxyDocRetrievePortType> portClass, String methodName,
                        Object operationInput) throws Exception {
                    return mockResponse;
                }
                
            };
            PassthruDocRetrieveProxyWebServiceUnsecuredImpl webProxy = new PassthruDocRetrieveProxyWebServiceUnsecuredImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper() {
                    return wsProxyHelper;
                }
                
                @Override
                protected CONNECTClient<NhincProxyDocRetrievePortType> getClient(String url, AssertionType assertion) {
                    return mockClient;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class))); 
                }
            });
            RetrieveDocumentSetResponseType response = webProxy.respondingGatewayCrossGatewayRetrieve(
                    mockDockRetrieveRequest, mockAssertion, mockTargetSystem);
            assertNotNull("RetrieveDocumentSetResponseType was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayCrossGatewayRetrieveHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayCrossGatewayRetrieveHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayCrossGatewayRetrieveNullUrl() {
        try {
            final RetrieveDocumentSetResponseType mockResponse = context.mock(RetrieveDocumentSetResponseType.class);
            final WebServiceProxyHelper wsProxyHelper = new WebServiceProxyHelper() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                public String getUrlLocalHomeCommunity(String serviceName) {
                    return null;
                }
            };
            final CONNECTClient<NhincProxyDocRetrievePortType> mockClient = new CONNECTClient<NhincProxyDocRetrievePortType>() {
                @Override
                public NhincProxyDocRetrievePortType getPort() {
                    // TODO Auto-generated method stub
                    return null;
                }
                
                /* (non-Javadoc)
                 * @see gov.hhs.fha.nhinc.messaging.client.CONNECTClient#invokePort(java.lang.Class, java.lang.String, java.lang.Object)
                 */
                @Override
                public Object invokePort(Class<NhincProxyDocRetrievePortType> portClass, String methodName,
                        Object operationInput) throws Exception {
                    return mockResponse;
                }
                
            };
            PassthruDocRetrieveProxyWebServiceUnsecuredImpl webProxy = new PassthruDocRetrieveProxyWebServiceUnsecuredImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper() {
                    return wsProxyHelper;
                }
                
                @Override
                protected CONNECTClient<NhincProxyDocRetrievePortType> getClient(String url, AssertionType assertion) {
                    return mockClient;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error(
                            "Failed to call the web service (" + NhincConstants.NHINC_PROXY_DOC_RETRIEVE_SERVICE_NAME
                                    + ").  The URL is null.");
                }
            });
            RetrieveDocumentSetResponseType response = webProxy.respondingGatewayCrossGatewayRetrieve(
                    mockDockRetrieveRequest, mockAssertion, mockTargetSystem);
            assertNull("RetrieveDocumentSetResponseType was not null", response);
        } catch (Throwable t) {
            System.out
                    .println("Error running testRespondingGatewayCrossGatewayRetrieveNullUrl test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayCrossGatewayRetrieveNullUrl test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayCrossGatewayRetrieveNullRequest() {
        try {
            final RetrieveDocumentSetResponseType mockResponse = context.mock(RetrieveDocumentSetResponseType.class);
            final WebServiceProxyHelper wsProxyHelper = new WebServiceProxyHelper() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                public String getUrlLocalHomeCommunity(String serviceName) {
                    return "url";
                }

                @Override
                public Object invokePort(Object portObject, Class portClass, String methodName, Object operationInput)
                        throws Exception {
                    return mockResponse;
                }
            };
            final CONNECTClient<NhincProxyDocRetrievePortType> mockClient = new CONNECTClient<NhincProxyDocRetrievePortType>() {
                @Override
                public NhincProxyDocRetrievePortType getPort() {
                    // TODO Auto-generated method stub
                    return null;
                }
                
                /* (non-Javadoc)
                 * @see gov.hhs.fha.nhinc.messaging.client.CONNECTClient#invokePort(java.lang.Class, java.lang.String, java.lang.Object)
                 */
                @Override
                public Object invokePort(Class<NhincProxyDocRetrievePortType> portClass, String methodName,
                        Object operationInput) throws Exception {
                    return mockResponse;
                }
                
            };
            PassthruDocRetrieveProxyWebServiceUnsecuredImpl webProxy = new PassthruDocRetrieveProxyWebServiceUnsecuredImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper() {
                    return wsProxyHelper;
                }
                
                @Override
                protected CONNECTClient<NhincProxyDocRetrievePortType> getClient(String url, AssertionType assertion) {
                    return mockClient;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            RetrieveDocumentSetResponseType response = webProxy.respondingGatewayCrossGatewayRetrieve(null,
                    mockAssertion, mockTargetSystem);
            assertNotNull("RetrieveDocumentSetResponseType was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayCrossGatewayRetrieveNullRequest test: "
                    + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayCrossGatewayRetrieveNullRequest test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayCrossGatewayRetrieveException() {
        try {
            final RetrieveDocumentSetResponseType mockResponse = context.mock(RetrieveDocumentSetResponseType.class);
            final WebServiceProxyHelper wsProxyHelper = new WebServiceProxyHelper() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                public String getUrlLocalHomeCommunity(String serviceName) {
                    return "url";
                }
            };
            final CONNECTClient<NhincProxyDocRetrievePortType> mockClient = new CONNECTClient<NhincProxyDocRetrievePortType>() {
                @Override
                public NhincProxyDocRetrievePortType getPort() {
                    // TODO Auto-generated method stub
                    return null;
                }
                
                /* (non-Javadoc)
                 * @see gov.hhs.fha.nhinc.messaging.client.CONNECTClient#invokePort(java.lang.Class, java.lang.String, java.lang.Object)
                 */
                @Override
                public Object invokePort(Class<NhincProxyDocRetrievePortType> portClass, String methodName,
                        Object operationInput) throws Exception {
                    throw new IllegalArgumentException("Thrown Exception");
                }
                
            };
            PassthruDocRetrieveProxyWebServiceUnsecuredImpl webProxy = new PassthruDocRetrieveProxyWebServiceUnsecuredImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper() {
                    return wsProxyHelper;
                }
                
                @Override
                protected CONNECTClient<NhincProxyDocRetrievePortType> getClient(String url, AssertionType assertion) {
                    return mockClient;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).error(
                            "Error: Failed to retrieve url for service: "
                                    + NhincConstants.NHINC_PROXY_DOC_RETRIEVE_SERVICE_NAME
                                    + " for local home community");
                    oneOf(mockLog).error(with(aNonNull(String.class)), with(aNonNull(IllegalArgumentException.class)));
                }
            });
            RetrieveDocumentSetResponseType response = webProxy.respondingGatewayCrossGatewayRetrieve(
                    mockDockRetrieveRequest, mockAssertion, mockTargetSystem);
            assertNull("RetrieveDocumentSetResponseType was not null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayCrossGatewayRetrieveException test: "
                    + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayCrossGatewayRetrieveException test: " + t.getMessage());
        }
    }

}