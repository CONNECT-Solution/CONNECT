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
import static org.junit.Assert.assertSame;
import gov.hhs.fha.nhinc.aspect.EventAspectAdvice;
import gov.hhs.fha.nhinc.patientdiscovery._10.passthru.NhincProxyPatientDiscoveryImpl;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PatientDiscoveryEventAspect;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "aspectj-unit-tests.xml" })
public class NhincProxyPatientDiscoveryTest {

    private static final Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private static final PatientDiscoveryServiceFactory mockFactory = context
            .mock(PatientDiscoveryServiceFactory.class);
    
    private static final EventAspectAdvice mockEventAspectAdvice = context.mock(EventAspectAdvice.class);

    @Autowired
    private NhincProxyPatientDiscovery patientDiscovery;

    @Autowired
    private PatientDiscoveryEventAspect patientDiscoveryEventAspect;

    @Test
    public void testDefaultConstructor() {
        assertNotNull(patientDiscovery);
    }

    /**
     * Tests {@link NhincProxyPatientDiscovery#testProxyPRPAIN201305UV()}
     * Ensure aspect advice is invoked.
     */
    @Test
    public void testProxyPRPAIN201305UV() {

        final ProxyPRPAIN201305UVProxyRequestType mockBody = context.mock(ProxyPRPAIN201305UVProxyRequestType.class);
        final PRPAIN201306UV02 expectedResponse = context.mock(PRPAIN201306UV02.class);
        final NhincProxyPatientDiscoveryImpl mockService = context.mock(NhincProxyPatientDiscoveryImpl.class);

        patientDiscovery.setOrchestratorImpl(mockService);

        context.checking(new Expectations() {
            {
                oneOf(mockEventAspectAdvice).beginOutboundMessageEvent();
                oneOf(mockService).proxyPRPAIN201305UV(with(same(mockBody)), with(any(WebServiceContext.class)));
                will(returnValue(expectedResponse));
                oneOf(mockEventAspectAdvice).endOutboundMessageEvent();
            }
        });

        PRPAIN201306UV02 actualResponse = patientDiscovery.proxyPRPAIN201305UV(mockBody);

        assertSame(expectedResponse, actualResponse);
    }

    /**
     * This method is necessary for wiring in the factory to the NhincProxyPatientDiscovery in a spring config.
     * @return patient discovery service factory.
     */
    public static PatientDiscoveryServiceFactory getPatientDiscoveryServiceFactory() {
        return mockFactory;
    }
    
    /**
     * This method is necessary for wiring in the event aspect advice in a spring config.
     * @return patient discovery service factory.
     */
    public static EventAspectAdvice getEventAspectAdvice() {
        return mockEventAspectAdvice;
    }
    
}
