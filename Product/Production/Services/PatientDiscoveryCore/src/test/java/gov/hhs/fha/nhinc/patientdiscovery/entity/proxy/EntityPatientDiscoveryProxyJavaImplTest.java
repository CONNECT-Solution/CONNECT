/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 *  All rights reserved.
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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.patientdiscovery.outbound.StandardOutboundPatientDiscovery;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class EntityPatientDiscoveryProxyJavaImplTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final PRPAIN201305UV02 mockPdRequest = context.mock(PRPAIN201305UV02.class);
    final AssertionType mockAssertion = context.mock(AssertionType.class);
    final NhinTargetCommunitiesType mockTargetCommunities = context.mock(NhinTargetCommunitiesType.class);
    final StandardOutboundPatientDiscovery mockProcessor = context.mock(StandardOutboundPatientDiscovery.class);

    @Test
    public void testGetEntityPatientDiscoveryProcessor() {
        try {
            EntityPatientDiscoveryProxyJavaImpl sut = new EntityPatientDiscoveryProxyJavaImpl() {

                @Override
                protected StandardOutboundPatientDiscovery getEntityPatientDiscoveryProcessor() {
                    return mockProcessor;
                }
            };
            StandardOutboundPatientDiscovery processor = sut.getEntityPatientDiscoveryProcessor();
            assertNotNull("EntityPatientDiscoveryProcessor was null", processor);
        } catch (Throwable t) {
            System.out.println("Error running testGetEntityPatientDiscoveryProcessor test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEntityPatientDiscoveryProcessor test: " + t.getMessage());
        }
    }

    // @Ignore
    @Test
    public void testRespondingGatewayPRPAIN201305UV02Happy() {
        try {
            EntityPatientDiscoveryProxyJavaImpl sut = new EntityPatientDiscoveryProxyJavaImpl() {
                @Override
                protected StandardOutboundPatientDiscovery getEntityPatientDiscoveryProcessor() {
                    return mockProcessor;
                }
            };
            context.checking(new Expectations() {
                {
                    oneOf(mockProcessor).respondingGatewayPRPAIN201305UV02(
                            with(aNonNull(RespondingGatewayPRPAIN201305UV02RequestType.class)),
                            with(aNonNull(AssertionType.class)));
                }
            });
            RespondingGatewayPRPAIN201306UV02ResponseType response = sut.respondingGatewayPRPAIN201305UV02(
                    mockPdRequest, mockAssertion, mockTargetCommunities);
            assertNotNull("RespondingGatewayPRPAIN201306UV02ResponseType was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02Happy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02Happy test: " + t.getMessage());
        }
    }

    // @Ignore
    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullProcessor() {
        try {
            EntityPatientDiscoveryProxyJavaImpl sut = new EntityPatientDiscoveryProxyJavaImpl() {
                @Override
                protected StandardOutboundPatientDiscovery getEntityPatientDiscoveryProcessor() {
                    return null;
                }
            };

            RespondingGatewayPRPAIN201306UV02ResponseType response = sut.respondingGatewayPRPAIN201305UV02(
                    mockPdRequest, mockAssertion, mockTargetCommunities);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullProcessor test: "
                    + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullProcessor test: " + t.getMessage());
        }
    }

}
