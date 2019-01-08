/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientlocationquery.v10.nhin;

import static org.junit.Assert.assertNotNull;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientlocationquery.inbound.PassthroughInboundPatientLocationQuery;
import ihe.iti.xcpd._2009.PatientLocationQueryRequestType;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType;
import java.util.Properties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author tjafri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/patientdiscovery/_10/applicationContext.xml"})
public class NhinPatientLocationQuerySpringContextTest {

    @Autowired
    NhinPatientLocationQuery nhinPLQ;

    @Autowired
    PassthroughInboundPatientLocationQuery inboundPLQ;

    @Test
    public void verifyBeanWiring() {
        assertNotNull(nhinPLQ);
        assertNotNull(inboundPLQ);
        assertNotNull(nhinPLQ.getInboundPLQ());
    }

    @Test
    public void testRespondingGatewayPatientLocationQuery() {
        PassthroughInboundPatientLocationQuery inboundSpy = Mockito.spy(inboundPLQ);
        PatientLocationQueryResponseType result = new PatientLocationQueryResponseType();
        Mockito.doReturn(result).when(inboundSpy).processPatientLocationQuery(Mockito.any(PatientLocationQueryRequestType.class),
            Mockito.any(AssertionType.class),
            Mockito.any(Properties.class));

        PatientLocationQueryRequestType request = new PatientLocationQueryRequestType();
        nhinPLQ.setInboundPLQ(inboundSpy); //Override the Autowired with our spy so we dont actually invoke SOAP services
        PatientLocationQueryResponseType response = nhinPLQ.respondingGatewayPatientLocationQuery(request);
        assertNotNull(response);
    }
}
