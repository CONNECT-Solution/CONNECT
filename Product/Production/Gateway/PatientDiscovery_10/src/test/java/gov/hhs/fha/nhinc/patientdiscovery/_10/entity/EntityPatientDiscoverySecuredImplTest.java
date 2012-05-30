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
package gov.hhs.fha.nhinc.patientdiscovery._10.entity;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.entity.EntityPatientDiscoveryOrchImpl;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;

/**
 * 
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class EntityPatientDiscoverySecuredImplTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);
    final AssertionType mockAssertion = context.mock(AssertionType.class);
    final EntityPatientDiscoveryOrchImpl mockProcessor = context.mock(EntityPatientDiscoveryOrchImpl.class);
    final RespondingGatewayPRPAIN201305UV02RequestType mockRequest = context
            .mock(RespondingGatewayPRPAIN201305UV02RequestType.class);
    final PerformanceManager mockPerformanceManager = context.mock(PerformanceManager.class);

    @Test
    public void testCreateLogger() {
        try {
            EntityPatientDiscoveryImpl pdSecuredImpl = new EntityPatientDiscoveryImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });

            Log log = pdSecuredImpl.createLogger();
            assertNotNull("Log was null", log);
        } catch (Throwable t) {
            System.out.println("Error running testCreateLogger: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger: " + t.getMessage());
        }
    }

    @Test
    public void testGetEntityPatientDiscoveryProcessor() {
        try {
            EntityPatientDiscoveryImpl pdSecuredImpl = new EntityPatientDiscoveryImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor() {
                    return mockProcessor;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });

            EntityPatientDiscoveryOrchImpl processor = pdSecuredImpl.getEntityPatientDiscoveryProcessor();
            assertNotNull("EntityPatientDiscoveryProcessor was null", processor);
        } catch (Throwable t) {
            System.out.println("Error running testGetEntityPatientDiscoveryProcessor: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEntityPatientDiscoveryProcessor: " + t.getMessage());
        }
    }

    @Test
    public void testExtractAssertion() {
        try {
            EntityPatientDiscoveryImpl pdSecuredImpl = new EntityPatientDiscoveryImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected AssertionType extractAssertion(WebServiceContext context) {
                    return mockAssertion;
                }
            };

            AssertionType assertion = pdSecuredImpl.extractAssertion(mockWebServiceContext);
            assertNotNull("AssertionType was null", assertion);
        } catch (Throwable t) {
            System.out.println("Error running testExtractAssertion: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractAssertion: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02Happy() {
        try {
            EntityPatientDiscoveryImpl pdSecuredImpl = new EntityPatientDiscoveryImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor() {
                    return mockProcessor;
                }

                @Override
                protected AssertionType extractAssertion(WebServiceContext context) {
                    return mockAssertion;
                }

                @Override
                protected PerformanceManager getPerformanceManager() {
                    return mockPerformanceManager;
                }

                @Override
                protected String getLocalHomeCommunityId() {
                    return "1.1";
                }

            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockProcessor).respondingGatewayPRPAIN201305UV02(
                            with(aNonNull(RespondingGatewayPRPAIN201305UV02RequestType.class)),
                            with(aNonNull(AssertionType.class)));
                    oneOf(mockPerformanceManager).logPerformanceStart(with(aNonNull(String.class)),
                            with(aNonNull(String.class)), with(aNonNull(String.class)), with(aNonNull(String.class)));
                    oneOf(mockPerformanceManager).logPerformanceStop(with(aNonNull(String.class)),
                            with(aNonNull(String.class)), with(aNonNull(String.class)), with(aNonNull(String.class)));
                }
            });

            RespondingGatewayPRPAIN201306UV02ResponseType response = pdSecuredImpl.respondingGatewayPRPAIN201305UV02(
                    mockRequest, mockWebServiceContext);
            assertNotNull("RespondingGatewayPRPAIN201306UV02ResponseType was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02Happy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02Happy: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullRequest() {
        try {
            EntityPatientDiscoveryImpl pdSecuredImpl = new EntityPatientDiscoveryImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor() {
                    return mockProcessor;
                }

                @Override
                protected AssertionType extractAssertion(WebServiceContext context) {
                    return mockAssertion;
                }

                @Override
                protected String getLocalHomeCommunityId() {
                    return "1.1";
                }

            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).error("The incomming request was null.");
                }
            });

            RespondingGatewayPRPAIN201306UV02ResponseType response = pdSecuredImpl.respondingGatewayPRPAIN201305UV02(
                    null, mockWebServiceContext);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullRequest: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullRequest: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullContext() {
        try {
            EntityPatientDiscoveryImpl pdSecuredImpl = new EntityPatientDiscoveryImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor() {
                    return mockProcessor;
                }

                @Override
                protected AssertionType extractAssertion(WebServiceContext context) {
                    return mockAssertion;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).error("The incomming WebServiceContext parameter was null.");
                }
            });

            RespondingGatewayPRPAIN201306UV02ResponseType response = pdSecuredImpl.respondingGatewayPRPAIN201305UV02(
                    mockRequest, null);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullContext: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullContext: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullProcessor() {
        try {
            EntityPatientDiscoveryImpl pdSecuredImpl = new EntityPatientDiscoveryImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor() {
                    return null;
                }

                @Override
                protected AssertionType extractAssertion(WebServiceContext context) {
                    return mockAssertion;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).error("The EntityPatientDiscoveryProcessor was null.");
                }
            });

            RespondingGatewayPRPAIN201306UV02ResponseType response = pdSecuredImpl.respondingGatewayPRPAIN201305UV02(
                    mockRequest, mockWebServiceContext);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullProcessor: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullProcessor: " + t.getMessage());
        }
    }

}