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

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.patientdiscovery.entity.EntityPatientDiscoveryOrchImpl;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;

/**
 * 
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class EntityPatientDiscoveryUnsecuredImplTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final EntityPatientDiscoveryOrchImpl mockEntityPatientDiscoveryProcessor = context
            .mock(EntityPatientDiscoveryOrchImpl.class);
    final PRPAIN201305UV02 mockPdMessage = context.mock(PRPAIN201305UV02.class);
    final AssertionType mockAssertion = context.mock(AssertionType.class);
    final NhinTargetCommunitiesType mockTargetCommunities = context.mock(NhinTargetCommunitiesType.class);
    final PerformanceManager mockPerformanceManager = context.mock(PerformanceManager.class);

    @Test
    public void testCreateLogger() {
        try {
            EntityPatientDiscoveryImpl pdUnsecuredImpl = new EntityPatientDiscoveryImpl() {
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

            Log log = pdUnsecuredImpl.createLogger();
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
            EntityPatientDiscoveryImpl pdUnsecuredImpl = new EntityPatientDiscoveryImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor() {
                    return mockEntityPatientDiscoveryProcessor;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });

            EntityPatientDiscoveryOrchImpl processor = pdUnsecuredImpl.getEntityPatientDiscoveryProcessor();
            assertNotNull("EntityPatientDiscoveryProcessor was null", processor);
        } catch (Throwable t) {
            System.out.println("Error running testGetEntityPatientDiscoveryProcessor: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEntityPatientDiscoveryProcessor: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02Happy() {
        try {
            EntityPatientDiscoveryImpl pdUnsecuredImpl = new EntityPatientDiscoveryImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor() {
                    return mockEntityPatientDiscoveryProcessor;
                }

                @Override
                protected PerformanceManager getPerformanceManager() {
                    return mockPerformanceManager;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockEntityPatientDiscoveryProcessor).respondingGatewayPRPAIN201305UV02(
                            with(aNonNull(RespondingGatewayPRPAIN201305UV02RequestType.class)),
                            with(aNonNull(AssertionType.class)));
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPdMessage);
            request.setAssertion(mockAssertion);
            request.setNhinTargetCommunities(mockTargetCommunities);

            RespondingGatewayPRPAIN201306UV02ResponseType response = pdUnsecuredImpl
                    .respondingGatewayPRPAIN201305UV02(request);
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
            EntityPatientDiscoveryImpl pdUnsecuredImpl = new EntityPatientDiscoveryImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor() {
                    return mockEntityPatientDiscoveryProcessor;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).warn("RespondingGatewayPRPAIN201305UV02RequestType was null.");
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = null;

            RespondingGatewayPRPAIN201306UV02ResponseType response = pdUnsecuredImpl
                    .respondingGatewayPRPAIN201305UV02(request);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullRequest: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullRequest: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullProcessor() {
        try {
            EntityPatientDiscoveryImpl pdUnsecuredImpl = new EntityPatientDiscoveryImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor() {
                    return null;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).warn("EntityPatientDiscoveryProcessor was null.");
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPdMessage);
            request.setAssertion(mockAssertion);
            request.setNhinTargetCommunities(mockTargetCommunities);

            RespondingGatewayPRPAIN201306UV02ResponseType response = pdUnsecuredImpl
                    .respondingGatewayPRPAIN201305UV02(request);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullProcessor: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullProcessor: " + t.getMessage());
        }
    }

    @Test
    @Ignore
    public void testRespondingGatewayPRPAIN201305UV02NullResponse() {
        try {
            EntityPatientDiscoveryImpl pdUnsecuredImpl = new EntityPatientDiscoveryImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor() {
                    EntityPatientDiscoveryOrchImpl processor = null;
                    // EntityPatientDiscoveryOrchImpl processor = new
                    // EntityPatientDiscoveryOrchImpl()
                    // {
                    // @Override
                    // public RespondingGatewayPRPAIN201306UV02ResponseType
                    // respondingGatewayPRPAIN201305UV02(RespondingGatewayPRPAIN201305UV02RequestType
                    // request, AssertionType assertion)
                    // {
                    // return null;
                    // }
                    // };
                    return processor;
                }

                @Override
                protected PerformanceManager getPerformanceManager() {
                    return mockPerformanceManager;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockPerformanceManager).logPerformanceStart(with(aNonNull(Timestamp.class)),
                            with(aNonNull(String.class)), with(aNonNull(String.class)), with(aNonNull(String.class)),
                            with(aNonNull(String.class)));
                    oneOf(mockPerformanceManager).logPerformanceStop(with(aNonNull(Long.class)),
                            with(aNonNull(Timestamp.class)), with(aNonNull(Timestamp.class)));
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPdMessage);
            request.setAssertion(mockAssertion);
            request.setNhinTargetCommunities(mockTargetCommunities);

            RespondingGatewayPRPAIN201306UV02ResponseType response = pdUnsecuredImpl
                    .respondingGatewayPRPAIN201305UV02(request);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullProcessor: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullResponse: " + t.getMessage());
        }
    }

}
