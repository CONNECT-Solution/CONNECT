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
import gov.hhs.fha.nhinc.generic.GenericFactory;
import gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryImpl;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.InboundPatientDiscoveryOrchestration;
import gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms;
import ihe.iti.xcpd._2009.PRPAIN201305UV02Fault;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class NhinPatientDiscoveryTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testDefaultConstructor() {
        NhinPatientDiscovery patientDiscovery = new NhinPatientDiscovery();
        assertNotNull(patientDiscovery);
    }

    @Test
    public void testMockService() throws PatientDiscoveryException, PRPAIN201305UV02Fault {

        final PRPAIN201305UV02 mockBody = context.mock(PRPAIN201305UV02.class);
        final PRPAIN201306UV02 expectedResponse = context.mock(PRPAIN201306UV02.class);
        final NhinPatientDiscoveryImpl mockService = context.mock(NhinPatientDiscoveryImpl.class);
        final PatientDiscoveryServiceFactory mockFactory = context.mock(PatientDiscoveryServiceFactory.class);
        final PatientDiscoveryAuditLogger mockAuditLogger = context.mock(PatientDiscoveryAuditLogger.class);
        final GenericFactory<InboundPatientDiscoveryOrchestration> mockOrch = context.mock(GenericFactory.class);

        NhinPatientDiscovery patientDiscovery = new NhinPatientDiscovery(mockFactory) {
            @Override
            protected PatientDiscoveryAuditLogger getPatientDiscoveryAuditLogger() {
                return mockAuditLogger;
            }

            @Override
            protected GenericFactory<InboundPatientDiscoveryOrchestration> getOrchestrationFactory() {
                return mockOrch;
            }  
        };
        
        context.checking(new Expectations() {
            {                
                oneOf(mockService).configure(with(same(mockAuditLogger)), with(same(mockOrch)));
                
                oneOf(mockService).respondingGatewayPRPAIN201305UV02(with(same(mockBody)),
                        with(any(WebServiceContext.class)));
                will(returnValue(expectedResponse));
            }
        });
        
        patientDiscovery.setOrchestratorImpl(mockService);

        PRPAIN201306UV02 actualResponse = patientDiscovery.respondingGatewayPRPAIN201305UV02(mockBody);

        assertSame(expectedResponse, actualResponse);

    }

}
