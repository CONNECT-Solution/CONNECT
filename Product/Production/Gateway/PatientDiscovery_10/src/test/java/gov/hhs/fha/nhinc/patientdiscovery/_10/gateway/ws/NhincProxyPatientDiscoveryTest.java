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

import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.patientdiscovery._10.passthru.NhincProxyPatientDiscoveryImpl;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class NhincProxyPatientDiscoveryTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testDefaultConstructor() {
        NhincProxyPatientDiscovery patientDiscovery = new NhincProxyPatientDiscovery();
        assertNotNull(patientDiscovery);
    }

    @Test
    public void testMockService() {

        final ProxyPRPAIN201305UVProxyRequestType mockBody = context.mock(ProxyPRPAIN201305UVProxyRequestType.class);
        final PRPAIN201306UV02 expectedResponse = context.mock(PRPAIN201306UV02.class);
        final NhincProxyPatientDiscoveryImpl mockService = context.mock(NhincProxyPatientDiscoveryImpl.class);
        final PatientDiscoveryServiceFactory mockFactory = context.mock(PatientDiscoveryServiceFactory.class);

        NhincProxyPatientDiscovery patientDiscovery = new NhincProxyPatientDiscovery(mockFactory);
        patientDiscovery.setOrchestratorImpl(mockService);

        context.checking(new Expectations() {
            {
                oneOf(mockService).proxyPRPAIN201305UV(with(same(mockBody)), with(any(WebServiceContext.class)));
                will(returnValue(expectedResponse));
            }
        });

        PRPAIN201306UV02 actualResponse = patientDiscovery.proxyPRPAIN201305UV(mockBody);

        assertSame(expectedResponse, actualResponse);

    }

}
