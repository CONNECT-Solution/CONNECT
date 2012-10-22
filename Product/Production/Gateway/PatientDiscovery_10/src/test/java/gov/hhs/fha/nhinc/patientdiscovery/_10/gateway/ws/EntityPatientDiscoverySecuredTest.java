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
package gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import gov.hhs.fha.nhinc.patientdiscovery._10.entity.EntityPatientDiscoveryImpl;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
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
public class EntityPatientDiscoverySecuredTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final EntityPatientDiscoveryImpl mockServiceImpl = context.mock(EntityPatientDiscoveryImpl.class);
    final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);
    final RespondingGatewayPRPAIN201305UV02RequestType mockRequest = context
            .mock(RespondingGatewayPRPAIN201305UV02RequestType.class);

    @Test
    public void defaultConstructor() {
        EntityPatientDiscoverySecured pdSecured = new EntityPatientDiscoverySecured();
        assertNotNull(pdSecured);
    }

    @Test
    public void testGetWebServiceContext() {
        try {
            EntityPatientDiscoverySecured pdSecured = new EntityPatientDiscoverySecured() {
                @Override
                protected WebServiceContext getWebServiceContext() {
                    return mockWebServiceContext;
                }
            };

            WebServiceContext webServiceContext = pdSecured.getWebServiceContext();
            assertNotNull("WebServiceContext was null", webServiceContext);
        } catch (Throwable t) {
            System.out.println("Error running testGetWebServiceContext: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetWebServiceContext: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02Happy() {
        try {
            EntityPatientDiscoverySecured pdSecured = new EntityPatientDiscoverySecured() {
                @Override
                protected WebServiceContext getWebServiceContext() {
                    return mockWebServiceContext;
                }
            };
            pdSecured.setOrchestratorImpl(mockServiceImpl);
            
            context.checking(new Expectations() {
                {
                    oneOf(mockServiceImpl).respondingGatewayPRPAIN201305UV02(
                            with(aNonNull(RespondingGatewayPRPAIN201305UV02RequestType.class)),
                            with(aNonNull(WebServiceContext.class)));
                }
            });

            RespondingGatewayPRPAIN201306UV02ResponseType response = pdSecured
                    .respondingGatewayPRPAIN201305UV02(mockRequest);
            assertNotNull("RespondingGatewayPRPAIN201306UV02ResponseType was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02Happy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02Happy: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullServiceImpl() {
        try {
            EntityPatientDiscoverySecured pdSecured = new EntityPatientDiscoverySecured() {
                @Override
                protected WebServiceContext getWebServiceContext() {
                    return mockWebServiceContext;
                }
            };
            pdSecured.setOrchestratorImpl(null);

            RespondingGatewayPRPAIN201306UV02ResponseType response = pdSecured
                    .respondingGatewayPRPAIN201305UV02(mockRequest);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullServiceImpl: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullServiceImpl: " + t.getMessage());
        }
    }

}