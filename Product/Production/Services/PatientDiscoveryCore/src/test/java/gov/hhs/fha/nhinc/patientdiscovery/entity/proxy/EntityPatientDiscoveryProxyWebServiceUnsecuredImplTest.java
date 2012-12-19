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
package gov.hhs.fha.nhinc.patientdiscovery.entity.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscoveryPortType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import javax.xml.ws.Service;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class EntityPatientDiscoveryProxyWebServiceUnsecuredImplTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final PRPAIN201305UV02 mockPdRequest = context.mock(PRPAIN201305UV02.class);
    final AssertionType mockAssertion = context.mock(AssertionType.class);
    final NhinTargetCommunitiesType mockTargetCommunities = context.mock(NhinTargetCommunitiesType.class);
    final EntityPatientDiscoveryPortType mockPort = context.mock(EntityPatientDiscoveryPortType.class);
    final Service mockService = context.mock(Service.class);
    final WebServiceProxyHelper mockWebServiceProxyHelper = context.mock(WebServiceProxyHelper.class);
    final CONNECTClient<EntityPatientDiscoveryPortType> mockCONNECTClient = context.mock(CONNECTClient.class);


    @Test
    public void testCreateWebServiceProxyHelper() {
        try {
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl sut = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl() {

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
    public void testInvokeConnectionManagerHappy() {
        try {
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl() {

                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper() {
                    return mockWebServiceProxyHelper;
                }

                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException {
                    return "test_endpoint";
                }
            };
            String endpointURL = webProxy.invokeConnectionManager("not_used_by_override");
            assertNotNull("EndpointURL was null", endpointURL);
            assertEquals("EndpointURL was not correct", "test_endpoint", endpointURL);
        } catch (Throwable t) {
            System.out.println("Error running testInvokeConnectionManagerHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testInvokeConnectionManagerHappy test: " + t.getMessage());
        }
    }

    @Test(expected = gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException.class)
    public void testInvokeConnectionManagerException() throws ConnectionManagerException {
        EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl() {

            @Override
            protected WebServiceProxyHelper createWebServiceProxyHelper() {
                return mockWebServiceProxyHelper;
            }

            @Override
            protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException {
                throw new ConnectionManagerException();
            }
        };
        webProxy.invokeConnectionManager("not_used_by_override");
        fail("Exception should have been thrown");
    }

    @Test
    public void testGetEndpointURLHappy() {
        try {
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl() {

                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper() {
                    return mockWebServiceProxyHelper;
                }

                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException {
                    return "test_endpoint";
                }
            };
            String endpointURL = webProxy.getEndpointURL();
            assertNotNull("EndpointURL was null", endpointURL);
            assertEquals("EndpointURL was not correct", "test_endpoint", endpointURL);
        } catch (Throwable t) {
            System.out.println("Error running testGetEndpointURLHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEndpointURLHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testGetEndpointURLException() {
        try {
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl() {

                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper() {
                    return mockWebServiceProxyHelper;
                }

                @Override
                protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException {
                    throw new ConnectionManagerException();
                }
            };
           
            String endpointURL = webProxy.getEndpointURL();
            assertNull("EndpointURL was not null", endpointURL);
        } catch (Throwable t) {
            System.out.println("Error running testGetEndpointURLException test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEndpointURLException test: " + t.getMessage());
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRespondingGatewayPRPAIN201305UV02Happy() {
        try {
            final RespondingGatewayPRPAIN201306UV02ResponseType mockResponse = context
                    .mock(RespondingGatewayPRPAIN201306UV02ResponseType.class);
            context.checking(new Expectations() {
                {
                    atLeast(1).of(mockCONNECTClient).invokePort(with(any(Class.class)), with(any(String.class)),
                            with(any(EntityPatientDiscoveryPortType.class)));
                    will(returnValue(mockResponse));
                }
            });
            final WebServiceProxyHelper wsProxyHelper = new WebServiceProxyHelper();
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl() {
                
                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper() {
                    return wsProxyHelper;
                }

                @Override
                protected String getEndpointURL() {
                    return "";
                }

                @Override
                protected CONNECTClient<EntityPatientDiscoveryPortType> getClient(String url, AssertionType assertion) {
                    return mockCONNECTClient;
                }
            };
          
            RespondingGatewayPRPAIN201306UV02ResponseType response = webProxy.respondingGatewayPRPAIN201305UV02(
                    mockPdRequest, mockAssertion, mockTargetCommunities);
            assertNotNull("RespondingGatewayPRPAIN201306UV02ResponseType was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02Happy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02Happy test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullPRPAIN201305UV02() {
        try {
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl() {

                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper() {
                    return mockWebServiceProxyHelper;
                }

                @Override
                protected String getEndpointURL() {
                    return "";
                }
            };
            RespondingGatewayPRPAIN201306UV02ResponseType response = webProxy.respondingGatewayPRPAIN201305UV02(null,
                    mockAssertion, mockTargetCommunities);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullPRPAIN201305UV02 test: "
                    + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullPRPAIN201305UV02 test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullAssertionType() {
        try {
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl() {

                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper() {
                    return mockWebServiceProxyHelper;
                }

                @Override
                protected String getEndpointURL() {
                    return "";
                }
            };
            RespondingGatewayPRPAIN201306UV02ResponseType response = webProxy.respondingGatewayPRPAIN201305UV02(
                    mockPdRequest, null, mockTargetCommunities);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullAssertionType test: "
                    + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullAssertionType test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullNhinTargetCommunitiesType() {
        try {
            EntityPatientDiscoveryProxyWebServiceUnsecuredImpl webProxy = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl() {

                @Override
                protected WebServiceProxyHelper createWebServiceProxyHelper() {
                    return mockWebServiceProxyHelper;
                }

                @Override
                protected String getEndpointURL() {
                    return "";
                }
            };
            RespondingGatewayPRPAIN201306UV02ResponseType response = webProxy.respondingGatewayPRPAIN201305UV02(
                    mockPdRequest, mockAssertion, null);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        } catch (Throwable t) {
            System.out
                    .println("Error running testRespondingGatewayPRPAIN201305UV02NullNhinTargetCommunitiesType test: "
                            + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullNhinTargetCommunitiesType test: "
                    + t.getMessage());
        }
    }

}